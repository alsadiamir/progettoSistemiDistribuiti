package it.unibo.canteen;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	

	@Autowired
	private ReservationDAO reservationDAO;
	@Autowired
	private RoomDAO roomDAO;
	@Autowired
	private SeatDAO seatDAO;
	@Autowired
	private UserDAO userDAO;
	
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
	public String getUser(@PathVariable("id") int userId) {
		Optional<User> user = userDAO.findById(userId);
		if(user.isPresent()) {
			String response = new Gson().toJson(user.get());
			return response;
		}
		else return "{\"error\": \"Couldn't find user!\"}";
	}
	
	@GetMapping("/rooms")
	public String getAllRooms() {
		List<Room> rooms = (List<Room>) roomDAO.findAll();
        String response = new Gson().toJson(rooms);
        return response;
	}
	
	@GetMapping("/room/{id}")
	public String getRoom(@PathVariable("id") int roomId) {
		Optional<Room> room = roomDAO.findById(roomId);
		if(room.isPresent()) {
			String response = new Gson().toJson(room.get());
			return response;
		}
		else return "{\"error\": \"Couldn't find room!\"}";
	}	
	
	@GetMapping("/seats")
	public String getAllSeatsOfRoom(@RequestParam(name="roomId", required=true) int roomId) {
		List<Seat> seats = seatDAO.findByRoomId(roomId);
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
            return "{\"busy\": " + reservation.isPresent() + "}";
		}
		else return "{\"error\": \"Seat doesn't exist!\"}";
	}
	
	@GetMapping("/seat/{id}")
	public String getSeat(@PathVariable("id") int seatId) {
		Optional<Seat> seat = seatDAO.findById(seatId);
		if(seat.isPresent()) {
			String response = new Gson().toJson(seat.get());
			return response;
		}
		else return "{\"error\": \"Couldn't find seat!\"}";
	}
	
	@GetMapping("/reservation")
	public String getAllReservationsOfUser(@RequestParam(name="userId", required=true) int userId) {
		List<Reservation> reservations = reservationDAO.findByUserId(userId);
        String response = new Gson().toJson(reservations);
        return response;
	}
	
	@GetMapping("/reservation/{id}")
	public String getReservation(@PathVariable int reservationId) {
		Optional<Reservation> reservation = reservationDAO.findById(reservationId);
		if(reservation.isPresent()) {
			String response = new Gson().toJson(reservation.get());
			return response;
		}
		else return "{\"error\": \"Couldn't find reservation!\"}";
	}
	
	@PostMapping("/user")
	public String insertUser(@RequestBody User user) {
		Optional<User> alreadyAvailable = userDAO.findByMail(user.getMail());
		if(alreadyAvailable.isPresent()) {
		    user = alreadyAvailable.get();
			user.setDevice(user.getDevice());
			user = userDAO.save(alreadyAvailable.get());
			String response = new Gson().toJson(alreadyAvailable);
			return response;
		}
		else {
			user = userDAO.save(user);
			String response = new Gson().toJson(user);
			return response;
		}		
	}

	@PostMapping("/room")
	public String insertRoom(@RequestBody Room room) {
		room = roomDAO.save(room);
		String response = new Gson().toJson(room);
		return response;
	}

	@PostMapping("/seat")
	public String insertSeat(@RequestBody Seat seat) {
		seat = seatDAO.save(seat);
		String response = new Gson().toJson(seat);
		return response;
	}
	
	@PostMapping("/reservation")
	public String insertReservation(@RequestBody Reservation reservation) {	
		
		//ATTENZIONE: flusso di esecuzione: creazione reservation -> eliminazione reservation 
		
		if(!reservationDAO.checkIfAlreadyPresentInDate(reservation.getUser(),reservation.getSeat(), reservation.getReservationDate()).isEmpty()) 
			return "{\"error\": \"A reservation in this date and seat already exists for this user\"}";
		
		Optional<Reservation> result = reservationDAO.findPreviousReservation(reservation.getSeat(), reservation.getFirstBlockReserved(), reservation.getBlocksReserved(), reservation.getReservationDate());
		if(!result.isEmpty()) reservation.setPreviousReservation(result.get().getId());
		reservationDAO.save(reservation);	
		String response = new Gson().toJson(reservation);
		return response;
					
	}

	@PostMapping("/reservation/update/{id}")
	public String updateReservation(@RequestBody Reservation reservation, @PathVariable int id) {
		Optional<Reservation> toEliminate = reservationDAO.findById(id);
		if(toEliminate.isPresent()) {
			toEliminate.get().setEliminatedAt(LocalDateTime.now());
			reservationDAO.save(toEliminate.get());
			insertReservation(reservation);
			String response = new Gson().toJson(reservation);
			return response;
		}
		else return "{\"error\": \"Reservation to update not found :(\"}";

	}

	@PostMapping("/reservation/delete/{id}")
	public String deleteReservation(@PathVariable int id) {
		Optional<Reservation> reservation = reservationDAO.findById(id);
		if(reservation.isPresent()) {

			Optional<Reservation> toInform = reservationDAO.findByPreviousReservationId(reservation.get().getId());
			toInform.ifPresent((v) -> {
                toInform.get().setPreviousReservation(0);
                reservationDAO.save(toInform.get());
			    // TODO: Send push notification
            });

            Reservation r = reservation.get();
            r.setEliminatedAt(LocalDateTime.now());
			r = reservationDAO.save(r);
			String response = new Gson().toJson(r);
			return response;	
		}
		else return "{\"error\": \"Couldn't delete entry! :(\"}";
	}
}
