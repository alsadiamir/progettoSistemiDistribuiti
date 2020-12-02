package it.unibo.canteen;

import java.time.LocalDateTime;
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
	public User getUser(@PathVariable("id") int userId) {
		Optional<User> user = userDAO.findById(userId);
		return user.get();
	}
	
	@GetMapping("/rooms")
	public List<Room> getAllRooms() {
		List<Room> rooms = (List<Room>) roomDAO.findAll();
		return rooms;
	}
	
	@GetMapping("/room/{id}")
	public Room getRoom(@PathVariable("id") int roomId) {
		Optional<Room> room = roomDAO.findById(roomId);
		return room.get();
	}	
	
	@GetMapping("/seats")
	public List<Seat> getAllSeatsOfRoom(@RequestParam(name="roomId", required=true) int roomId) {
		List<Seat> seats = seatDAO.findByRoomId(roomId);
		if(seats != null) return seats;
		else return null;
	}
	
	@GetMapping("/seats/{id}")
	public Seat getSeat(@PathVariable("id") int seatId) {
		Optional<Seat> seat = seatDAO.findById(seatId);
		return seat.get();
	}
	
	@GetMapping("/reservation")
	public List<Reservation> getAllReservationsOfUser(@RequestParam(name="userId", required=true) int userId) {
		List<Reservation> reservations = reservationDAO.findByUserId(userId);
		if(reservations != null) return reservations;
		else return null;
	}
	
	@GetMapping("/reservation/{id}")
	public Reservation getReservation(@PathVariable int reservationId) {
		Optional<Reservation> reservation = reservationDAO.findById(reservationId);
		return reservation.get();
	}
	
	@PostMapping("/user")
	public String insertUser(@RequestBody User user) {
		System.out.println(user);
		userDAO.save(user);
		return "User succesfully created! (id = " + user.getId() + ")";
	}
	
	@PostMapping("/reservation")
	public String insertReservation(@RequestBody Reservation reservation) {
		System.out.println("USER ID = "+reservation.getUser().getId());
		System.out.println("SEAT ID = "+reservation.getSeat().getId());
		System.out.println("BLOCKSRESERVED = "+reservation.getBlocksReserved());
		if(!reservationDAO.findCollidingReservation(reservation.getUser(), reservation.getSeat(), reservation.getFirstBlockReserved(), reservation.getBlocksReserved()).isEmpty()) {
			//manda messaggio
			return "Couldn't create reservation! Collision!";
		}
		else {
			List<Reservation> results = reservationDAO.findLastCollidingReservation(reservation.getSeat(), reservation.getFirstBlockReserved(), reservation.getReservationDate());
			for(Reservation r : results) 
				System.out.println("ID = " +r.getId());
			if(!results.isEmpty()) {
				reservation.setPreviousReservation(results.get(0).getId());
			}
			reservation.setCreatedAt(LocalDateTime.now());
			reservationDAO.save(reservation);	
			return "Reservation succesfully created! (id = " + reservation.getId() + ")";
		}		
	}
	
	@PostMapping("/reservation/update/{id}")
	public String updateReservation(@RequestBody Reservation reservation, @PathVariable int id) {
		Optional<Reservation> toEliminate = reservationDAO.findById(id);
		if(toEliminate.isPresent()) {
			toEliminate.get().setEliminatedAt(LocalDateTime.now());
			reservationDAO.save(toEliminate.get());
			insertReservation(reservation);
			return "Reservation succesfully updated! (id = " + reservation.getId() + ")";
		}
		else return "Reservation not found :(";
		
	}	
	
	@PostMapping("/reservation/delete/{id}")
	public String deleteReservation(@PathVariable int id) {
		Optional<Reservation> reservation = reservationDAO.findById(id);
		if(reservation.isPresent()) {
			reservation.get().setEliminatedAt(LocalDateTime.now());
			reservationDAO.save(reservation.get());
			return "Reservation succesfully deleted! (id = " + id + ")";	
		}
		else return "Couldn't delete entry! :(";
	}
}
