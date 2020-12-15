package it.unibo.canteen;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import it.unibo.canteen.authentication.AuthUserData;

import it.unibo.canteen.cache.Cache;
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
	Logger LOGGER = LoggerFactory.getLogger(CanteenController.class);

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
	
	private String htmlPage  = "index";
	
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
	    Optional<User> cachedUser = userCache.getAny("id-" + userId);
	    if (cachedUser.isPresent()) {
            String response = new Gson().toJson(cachedUser.get());
            return response;
        }

		Optional<User> user = userDAO.findById(userId);
		if(user.isPresent()) {
            if (!authUser.getEmail().equals(user.get().getMail())) {     
            	
            	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
            	throw new AuthorizationServiceException("You have no rights to access this user");
            }       
            
			LOGGER.debug("ACCESSED USER "+user.get().getMail()+" WITH SUCCESS");
            String response = new Gson().toJson(user.get());
			return response;
		}
		else {
			LOGGER.error("USER "+user.get().getMail()+" COULDN'T BE FOUND");
			return "{\"error\": \"Couldn't find user!\"}";
		}
	}
	
	@GetMapping("/rooms")
	public String getAllRooms() {
		List<Room> rooms = (List<Room>) roomDAO.findAll();
		
		LOGGER.debug("ACCESSED ROOMS WITH SUCCESS");
        String response = new Gson().toJson(rooms);
        return response;
	}
	
	@GetMapping("/room/{id}")
	public String getRoom(@PathVariable("id") int roomId) {
		Optional<Room> room = roomDAO.findById(roomId);
		if(room.isPresent()) {
			
			LOGGER.debug("ACCESSED ROOM ID: "+roomId+" WITH SUCCESS");
			String response = new Gson().toJson(room.get());
			return response;
		}
		else {
			
			LOGGER.error("ROOM WITH ID: "+room.get().getId()+" COULDN'T BE FOUND");
			return "{\"error\": \"Couldn't find room!\"}";
		}
	}	
	
	@GetMapping("/seats")
	public String getAllSeatsOfRoom(@RequestParam(name="roomId", required=true) int roomId) {
		List<Seat> seats = seatDAO.findByRoomId(roomId);
		
		LOGGER.debug("ACCESSED SEATS IN ROOM WITH ID: "+roomId+" WITH SUCCESS");
        String response = new Gson().toJson(seats);
        return response;
	}
	
	@GetMapping("/seat/available")
	public String checkSeatIsAvailableInDateAndBlock(@RequestParam(name="seatId", required=true) int seatId, 
			@RequestParam(name="reservationDate", required=true) String reservationDate,
			@RequestParam(name="block", required=true) int block) {
		Optional<Seat> seat = seatDAO.findById(seatId);
		if(seat.isPresent()) {
			LocalDate date = LocalDate.parse(reservationDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println(date);
            Optional<Reservation> reservation = reservationDAO.checkIfAvailableInDateAndBlock(seat.get(), date, block);
            
            LOGGER.debug("ACCESSED SEAT WITH ID: "+seat.get().getId()+" WITH SUCCESS");
            return "{\"busy\": " + reservation.isPresent() + "}";
		}
		else {
			
			LOGGER.error("SEAT WITH ID: "+seat.get().getId()+" COULDN'T BE FOUND");
			return "{\"error\": \"Seat doesn't exist!\"}";
		}
	}
	
	@GetMapping("/seat/{id}")
	public String getSeat(@PathVariable("id") int seatId) {
		Optional<Seat> seat = seatDAO.findById(seatId);
		if(seat.isPresent()) {
			
			LOGGER.debug("ACCESSED SEAT WITH ID: "+seatId+" WITH SUCCESS");
			String response = new Gson().toJson(seat.get());
			return response;
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
    ) {
		List<Reservation> reservations = reservationDAO.findByUserId(userId);
		if(!reservations.isEmpty()) {
            if (!authUser.getEmail().equals(reservations.get(0).getUser().getMail())) {
            	
            	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
            	throw new AuthorizationServiceException("You have no rights to access this user");
            }               
        }
		
		LOGGER.debug("ACCESSED RESERVATION OF USER WITH ID: "+userId+" WITH SUCCESS");
        String response = new Gson().toJson(reservations);
        return response;
	}
	
	@GetMapping("/reservation/{id}")
	public String getReservation(
	        @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @PathVariable int id
    ) {
		Optional<Reservation> reservation = reservationDAO.findById(id);
		if(reservation.isPresent() && reservation.get().getEliminatedAt() == null) {
            if (!authUser.getEmail().equals(reservation.get().getUser().getMail())){
            	
            	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
            	throw new AuthorizationServiceException("You have no rights to access this user");
            }
            
            LOGGER.debug("ACCESSED RESERVATION WITH ID: "+id+" WITH SUCCESS");
			String response = new Gson().toJson(reservation.get());
			return response;
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
		    user = alreadyAvailable.get();
			user.setDevice(user.getDevice());
			user = userDAO.save(alreadyAvailable.get());
		}
		else {
			user = userDAO.save(user);
		}

		userCache.put("mail-" + user.getMail(), user);
        userCache.put("id-" + user.getId(), user);
        LOGGER.debug("ACCESSED USER: "+user.getMail()+" WITH SUCCESS");
        String response = new Gson().toJson(user);
        return response;
	}

	@PostMapping("/room")
	public String insertRoom(@RequestBody Room room) {
		room = roomDAO.save(room);
		
		LOGGER.debug("INSERTED ROOM WITH NAME: "+room.getName()+" WITH SUCCESS");
		String response = new Gson().toJson(room);
		return response;
	}

	@PostMapping("/seat")
	public String insertSeat(@RequestBody Seat seat) {
		seat = seatDAO.save(seat);
		
		LOGGER.debug("INSERTED SEAT WITH ID: "+seat.getId()+" WITH SUCCESS");
		String response = new Gson().toJson(seat);
		return response;
	}
	
	@PostMapping("/reservation")
	public String insertReservation(
            @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @RequestBody Reservation reservation
    ) {
        if (!authUser.getEmail().equals(reservation.getUser().getMail())) {
        	
        	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
        	throw new AuthorizationServiceException("You have no rights to access this user");
        }           

		//ATTENZIONE: flusso di esecuzione: creazione reservation -> eliminazione reservation 
		
		if(!reservationDAO.checkIfAlreadyPresentInDate(reservation.getUser(),reservation.getSeat(), reservation.getReservationDate()).isEmpty()) {
			
			LOGGER.error("RESERVATION FOR USER: "+reservation.getUser().getMail()+" IN DATE SPECIFIED ALREADY EXISTS");
			return "{\"error\": \"A reservation in this date and seat already exists for this user\"}";
		}
		
		Optional<Reservation> result = reservationDAO.findPreviousReservation(reservation.getSeat(), reservation.getFirstBlockReserved(), reservation.getBlocksReserved(), reservation.getReservationDate());
		if(!result.isEmpty()) reservation.setPreviousReservation(result.get().getId());
		reservationDAO.save(reservation);	
		
		LOGGER.debug("RESERVATION WITH ID: "+reservation.getId()+" INSERTED WITH SUCCESS");
		String response = new Gson().toJson(reservation);
		return response;
	}

	@PostMapping("/reservation/update/{id}")
	public String updateReservation(
	        @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @RequestBody Reservation reservation,
            @PathVariable int id) {
		Optional<Reservation> toEliminate = reservationDAO.findById(id);
		if(toEliminate.isPresent()) {
			toEliminate.get().setEliminatedAt(LocalDateTime.now());
			reservationDAO.save(toEliminate.get());
			insertReservation(authUser, reservation);
			
			LOGGER.debug("RESERVATION WITH ID: "+reservation.getId()+" UPDATED WITH SUCCESS");
			String response = new Gson().toJson(reservation);
			return response;
		}
		else {
			
			LOGGER.error("RESERVATION TO UPDATE WITH ID: "+reservation.getId()+" COULDN'T BE FOUND");
			return "{\"error\": \"Reservation to update not found :(\"}";
		}

	}

	@PostMapping("/reservation/delete/{id}")
	public String deleteReservation(
            @RequestAttribute(AuthUserData.ATTR_NAME) AuthUserData authUser,
            @PathVariable int id
    ) {
		Optional<Reservation> reservation = reservationDAO.findById(id);
		if(reservation.isPresent()) {
            if (!authUser.getEmail().equals(reservation.get().getUser().getMail())){
            	
            	LOGGER.error("ACCESS DENIED FOR REQUESTED USER: "+authUser.getEmail());
            	throw new AuthorizationServiceException("You have no rights to access this user");
            }

			Optional<Reservation> toInform = reservationDAO.findByPreviousReservationId(reservation.get().getId());
			toInform.ifPresent((v) -> {
                toInform.get().setPreviousReservation(0);
                reservationDAO.save(toInform.get());
                
                LOGGER.debug("RESERVATION WITH ID: "+toInform.get().getId()+" UNLINKED TO PREVIOUS WITH SUCCESS");
			    // TODO: Send push notification
            });

            Reservation r = reservation.get();
            r.setEliminatedAt(LocalDateTime.now());
			r = reservationDAO.save(r);
			
			LOGGER.debug("RESERVATION WITH ID: "+r.getId()+" DELETED WITH SUCCESS");
			String response = new Gson().toJson(r);
			return response;	
		}
		else {
			
			LOGGER.error("RESERVATION WITH ID: "+reservation.get().getId() + " COULDN'T BE DELETED");
			return "{\"error\": \"Couldn't delete entry! :(\"}";
		}
	}
}
