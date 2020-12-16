package it.unibo.canteen;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import it.unibo.canteen.authentication.AuthUserData;

import it.unibo.canteen.cache.Cache;
import it.unibo.canteen.notification.UserNotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import it.unibo.canteen.dao.ReservationDAO;
import it.unibo.canteen.dao.RoomDAO;
import it.unibo.canteen.dao.SeatDAO;
import it.unibo.canteen.dao.UserDAO;
import it.unibo.canteen.model.Reservation;
import it.unibo.canteen.model.Room;
import it.unibo.canteen.model.Seat;
import it.unibo.canteen.model.User;

@RestController
public class CanteenController {
    private static final String htmlPage  = "index";
	private final Logger LOGGER = LoggerFactory.getLogger(CanteenController.class);

    @Autowired
    private UserNotificationSender userNotificationSender;
	@Autowired
	private ReservationDAO reservationDAO;
	@Autowired
	private RoomDAO roomDAO;
	@Autowired
	private SeatDAO seatDAO;
	@Autowired
	private UserDAO userDAO;
    @Autowired
    private Cache<Reservation> reservationCache;
    @Autowired
    private Cache<Room> roomCache;
    @Autowired
    private Cache<Seat> seatCache;
	@Autowired
    private Cache<User> userCache;

	public CanteenController() {
		System.out.println("**********************************************************");
		System.out.println("**********************************************************");
		System.out.println("**********************************************************");
		System.out.println("**********************************************************");
		System.out.println("--------------------****************----------------------");
		System.out.println("------------------********************--------------------");
		System.out.println("------------------STARTING....STARTING--------------------");
		System.out.println("------------------********************--------------------");
		System.out.println("--------------------****************----------------------");
		System.out.println("**********************************************************");
		System.out.println("**********************************************************");
		System.out.println("**********************************************************");
		System.out.println("**********************************************************");
	}
	
	@GetMapping("/") 		 
	public String entry(Model viewmodel) {
		viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons ");
		return htmlPage;
	} 

	@GetMapping("/user/{id}")
	public String getUser(
            @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @PathVariable("id") int userId
    ) throws IOException {
	    Optional<User> user = userCache.getAny("id-" + userId);
	    if (user.isEmpty()) {
            user = userDAO.findById(userId);
            if (user.isPresent()) {
                userCache.put("id-" + userId, user.get());
                userCache.put("mail-" + user.get().getMail(), user.get());
            }
        }

		if(user.isPresent()) {
            if (!authUser.getEmail().equals(user.get().getMail())) {
            	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
            	throw new AuthorizationServiceException("You have no rights to access this user");
            }
			LOGGER.debug("ACCESSED USER "+user.get().getMail()+" WITH SUCCESS");
            return new Gson().toJson(user.get());
		}
		else {
			LOGGER.error("USER "+userId+" COULDN'T BE FOUND");
			return "{\"error\": \"Couldn't find user!\"}";
		}
	}
	
	@GetMapping("/rooms")
	public String getAllRooms() throws IOException {
        List<Room> rooms;
        Optional<List<Room>> cachedList = roomCache.get("all");
        if (cachedList.isEmpty()) {
            rooms = StreamSupport.stream(roomDAO.findAll().spliterator(), false).collect(Collectors.toList());
            roomCache.put("all", rooms);
            for (Room r : rooms) {
                roomCache.put("id-" + r.getId(), r);
            }
        } else {
            rooms = cachedList.get();
        }

		LOGGER.debug("ACCESSED ROOMS WITH SUCCESS");
        return new Gson().toJson(rooms);
	}
	
	@GetMapping("/room/{id}")
	public String getRoom(@PathVariable("id") int roomId) throws IOException {
        Optional<Room> room = roomCache.getAny("id-" + roomId);
        if (room.isEmpty()) {
            room = roomDAO.findById(roomId);
            if (room.isPresent()) {
                roomCache.put("id-" + roomId, room.get());
            }
        }

		if(room.isPresent()) {
			LOGGER.debug("ACCESSED ROOM ID: "+roomId+" WITH SUCCESS");
            return new Gson().toJson(room.get());
		}
		else {
			LOGGER.error("ROOM WITH ID: "+roomId+" COULDN'T BE FOUND");
			return "{\"error\": \"Couldn't find room!\"}";
		}
	}	
	
	@GetMapping("/seats")
	public String getAllSeatsOfRoom(@RequestParam(name="roomId", required=true) int roomId) throws IOException {
        Optional<List<Seat>> seats = seatCache.get("roomId-" + roomId);
        if (seats.isEmpty()) {
            seats = Optional.of(seatDAO.findByRoomId(roomId));
            seatCache.put("roomId-" + roomId, seats.get());
            for (Seat s : seats.get()) {
                seatCache.put("id-" + s.getId(), s);
            }
        }
		LOGGER.debug("ACCESSED SEATS IN ROOM WITH ID: "+roomId+" WITH SUCCESS");
        return new Gson().toJson(seats.get());
	}
	
	@GetMapping("/seat/available")
	public String checkSeatIsAvailableInDateAndBlock(@RequestParam(name="seatId", required=true) int seatId, 
			@RequestParam(name="reservationDate", required=true) String reservationDate,
			@RequestParam(name="block", required=true) int block) throws IOException {
        Optional<Seat> seat = seatCache.getAny("id-" + seatId);
        if (seat.isEmpty()) {
            seat = seatDAO.findById(seatId);
            if (seat.isPresent()) {
                seatCache.put("id-" + seatId, seat.get());
            }
        }

		if(seat.isPresent()) {
			LocalDate date = LocalDate.parse(reservationDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Optional<Reservation> reservation = reservationDAO.checkIfAvailableInDateAndBlock(seat.get(), date, block);
            LOGGER.debug("ACCESSED SEAT WITH ID: "+seat.get().getId()+" WITH SUCCESS");
            return "{\"busy\": " + reservation.isPresent() + "}";
		}
		else {
			LOGGER.error("SEAT WITH ID: "+seatId+" COULDN'T BE FOUND");
			return "{\"error\": \"Seat doesn't exist!\"}";
		}
	}
	
	@GetMapping("/seat/{id}")
	public String getSeat(@PathVariable("id") int seatId) throws IOException{
        Optional<Seat> seat = seatCache.getAny("id-" + seatId);
        if (seat.isEmpty()) {
            seat = seatDAO.findById(seatId);
            if (seat.isPresent()) {
                seatCache.put("id-" + seatId, seat.get());
            }
        }
		if(seat.isPresent()) {
			LOGGER.debug("ACCESSED SEAT WITH ID: "+seatId+" WITH SUCCESS");
            return new Gson().toJson(seat.get());
		}
		else {
			LOGGER.error("SEAT WITH ID: "+seatId+" COULDN'T BE FOUND");
			return "{\"error\": \"Couldn't find seat!\"}";
		}
	}
	
	@GetMapping("/reservation")
	public String getAllReservationsOfUser(
            @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @RequestParam(name="userId", required=true) int userId
    ) throws IOException {
        Optional<List<Reservation>> reservations = reservationCache.get("userId-" + userId);
        if (reservations.isEmpty()) {
            reservations = Optional.of(reservationDAO.findByUserId(userId));
            reservationCache.put("userId-" + userId, reservations.get());
            for (Reservation r : reservations.get()) {
                reservationCache.put("id-" + r.getId(), r);
            }
        }

		if(!reservations.get().isEmpty()) {
            if (!authUser.getEmail().equals(reservations.get().get(0).getUser().getMail())) {
            	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+ authUser.getEmail());
            	throw new AuthorizationServiceException("You have no rights to access this user");
            }
        }

		LOGGER.debug("ACCESSED RESERVATION OF USER WITH ID: "+userId+" WITH SUCCESS");
        return new Gson().toJson(reservations.get());
	}
	
	@GetMapping("/reservation/{id}")
	public String getReservation(
	        @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @PathVariable int id
    ) throws IOException {
        Optional<Reservation> reservation = reservationCache.getAny("id-" + id);
        if (reservation.isEmpty()) {
            reservation = reservationDAO.findById(id);
            if (reservation.isPresent()) {
                reservationCache.put("id-" + id, reservation.get());
            }
        }

		if(reservation.isPresent() && reservation.get().getEliminatedAt() == null) {
            if (!authUser.getEmail().equals(reservation.get().getUser().getMail())){
            	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
            	throw new AuthorizationServiceException("You have no rights to access this user");
            }
            LOGGER.debug("ACCESSED RESERVATION WITH ID: "+id+" WITH SUCCESS");
            return new Gson().toJson(reservation.get());
		}
		else {
			LOGGER.error("RESERVATION WITH ID: "+id+" COULDN'T BE FOUND");
			return "{\"error\": \"Couldn't find reservation!\"}";
		}
	}
	
	@PostMapping("/user")
	public String insertUser(
	        @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @RequestBody User user
    ) throws IOException {
	    if (!authUser.getEmail().equals(user.getMail())) {
	    	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
	    	throw new AuthorizationServiceException("You have no rights to access this user");
	    }

        Optional<User> alreadyAvailable = userCache.getAny("mail-" + user.getMail());
        if (alreadyAvailable.isEmpty()) {
            alreadyAvailable = userDAO.findByMail(user.getMail());
        }

		if(alreadyAvailable.isPresent()) {
		    alreadyAvailable.get().setDevice(user.getDevice());
			user = userDAO.save(alreadyAvailable.get());
		}
		else {
			user = userDAO.save(user);
		}

		userCache.put("mail-" + user.getMail(), user);
        userCache.put("id-" + user.getId(), user);
        LOGGER.debug("ACCESSED USER: "+user.getMail()+" WITH SUCCESS");
        return new Gson().toJson(user);
	}

	@PostMapping("/room")
	public String insertRoom(@RequestBody Room room) throws IOException {
		room = roomDAO.save(room);
		roomCache.put("id-" + room.getId(), room);
		
		LOGGER.debug("INSERTED ROOM WITH NAME: "+room.getName()+" WITH SUCCESS");
        return new Gson().toJson(room);
	}

	@PostMapping("/seat")
	public String insertSeat(@RequestBody Seat seat) throws IOException {
		seat = seatDAO.save(seat);
        seatCache.put("id-" + seat.getId(), seat);
		
		LOGGER.debug("INSERTED SEAT WITH ID: "+seat.getId()+" WITH SUCCESS");
        return new Gson().toJson(seat);
	}
	
	@PostMapping("/reservation")
	public String insertReservation(
            @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @RequestBody Reservation reservation
    ) throws IOException {
        if (!authUser.getEmail().equals(reservation.getUser().getMail())) {
        	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
        	throw new AuthorizationServiceException("You have no rights to access this user");
        }           

		//ATTENZIONE: flusso di esecuzione: creazione reservation -> eliminazione reservation 
		
		if(reservationDAO.checkIfAlreadyPresentInDate(
		        reservation.getUser(),
                reservation.getSeat(),
                reservation.getReservationDate()
        ).isPresent()) {
			LOGGER.error("RESERVATION FOR USER: "+reservation.getUser().getMail()+" IN DATE SPECIFIED ALREADY EXISTS");
			return "{\"error\": \"A reservation in this date and seat already exists for this user\"}";
		}
		
		Optional<Reservation> previous = reservationDAO.findPreviousReservation(reservation.getSeat(), reservation.getFirstBlockReserved(), reservation.getBlocksReserved(), reservation.getReservationDate());
        if(previous.isPresent()) {
            reservation.setPreviousReservation(previous.get().getId());
        }
		reservation = reservationDAO.save(reservation);
        reservationCache.delete("userId-" + reservation.getUser().getId());
		reservationCache.put("id-" + reservation.getId(), reservation);

		int reservationUserId = reservation.getUser().getId();
		Optional<User> reservationUser = userCache.getAny("id-" + reservationUserId);
		if (reservationUser.isEmpty()) {
		    reservationUser = userDAO.findById(reservationUserId);
        }
		if(reservationUser.isPresent()) {
            userNotificationSender.sendTextMessage(
                    reservationUser.get(),
                    "Your reservation has been created hehe"
            );
        }

		LOGGER.debug("RESERVATION WITH ID: "+reservation.getId()+" INSERTED WITH SUCCESS");
        return new Gson().toJson(reservation);
	}

	@PostMapping("/reservation/delete/{id}")
	public String deleteReservation(
            @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @PathVariable int id
    ) throws IOException {
		Optional<Reservation> reservation = reservationDAO.findById(id);
		if(reservation.isPresent()) {
            if (!authUser.getEmail().equals(reservation.get().getUser().getMail())){
            	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
            	throw new AuthorizationServiceException("You have no rights to access this user");
            }

			Optional<Reservation> toInform = reservationDAO.findByPreviousReservationId(reservation.get().getId());
			if (toInform.isPresent()) {
                Reservation reservationToInform = toInform.get();
                reservationToInform.setPreviousReservation(0);
                reservationToInform = reservationDAO.save(reservationToInform);
                reservationCache.delete("userId-" + reservationToInform.getUser().getId());
                reservationCache.put("id-" + reservationToInform.getId(), reservationToInform);
                LOGGER.debug("RESERVATION WITH ID: "+reservationToInform.getId()+" UNLINKED TO PREVIOUS WITH SUCCESS");

                Optional<User> userToInform = userCache.getAny("id-" + reservationToInform.getUser().getId());
                if (userToInform.isEmpty()) {
                    userToInform = userDAO.findById(reservationToInform.getUser().getId());
                }
                if(userToInform.isPresent()) {
                    userNotificationSender.sendTextMessage(
                            userToInform.get(),
                            "Someone removed their reservation, you are now the first in line for "
                                    + reservationToInform.getReservationDate() + "!"
                    );
                }
            }

            Reservation r = reservation.get();
            r.setEliminatedAt(LocalDateTime.now());
			r = reservationDAO.save(r);
            reservationCache.delete("userId-" + r.getUser().getId());
            reservationCache.put("id-" + r.getId(), r);
			
			LOGGER.debug("RESERVATION WITH ID: "+r.getId()+" DELETED WITH SUCCESS");
            return new Gson().toJson(r);
		}
		else {
			
			LOGGER.error("RESERVATION WITH ID: "+id + " COULDN'T BE DELETED");
			return "{\"error\": \"Couldn't delete entry! :(\"}";
		}
	}
}
