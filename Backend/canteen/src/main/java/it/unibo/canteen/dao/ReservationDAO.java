package it.unibo.canteen.dao;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RequestParam;

import it.unibo.canteen.model.Reservation;
import it.unibo.canteen.model.Seat;
import it.unibo.canteen.model.User;

@Transactional
public interface ReservationDAO extends CrudRepository<Reservation, Integer> {
    @Query("select r from Reservation r where eliminatedAt is null and user_id = :userId")
	public List<Reservation> findByUserId(@Param("userId") int userId);
	
	public Optional<Reservation> findByPreviousReservationId(int previousReservationId);
	
//	@Query("from Reservation where user = :user and seat = :seat and not ( (:firstBlockReserved < firstBlockReserved and :firstBlockReserved + :blocksReserved < firstBlockReserved + blocksReserved) or :firstBlockReserved > firstBlockReserved) and eliminatedAt is null")
//	public List<Reservation> findCollidingReservations(@Param("user") User user, @Param("seat") Seat seat, @Param("firstBlockReserved") int firstBlockReserved, @Param("blocksReserved") int blocksReserved);
	
	@Query("select r from Reservation r where r.id = (select MAX(id) from Reservation where seat = :seat and not ( :firstBlockReserved + :blocksReserved < firstBlockReserved or :firstBlockReserved > firstBlockReserved + blocksReserved ) and reservationDate = :reservationDate and eliminatedAt is null)")
	public Optional<Reservation> findPreviousReservation(@Param("seat") Seat seat, @Param("firstBlockReserved") int firstBlockReserved, @Param("blocksReserved") int blocksReserved, @Param("reservationDate") LocalDate reservationDate);
	
	@Query("select r from Reservation r where r.seat = :seat and r.reservationDate = :reservationDate and r.eliminatedAt is null and r.firstBlockReserved <= :block and :block <= r.firstBlockReserved + r.blocksReserved and r.previousReservationId = 0")
	public Optional<Reservation> checkIfAvailableInDateAndBlock(@Param("seat") Seat seat,@Param("reservationDate") LocalDate reservationDate,@Param("block") int block);

	@Query("select r from Reservation r where r.user = :user and r.seat = :seat and r.reservationDate = :reservationDate and eliminatedAt is null")
	public Optional<Reservation> checkIfAlreadyPresentInDate(@Param("user") User user, @Param("seat") Seat seat, @Param("reservationDate") LocalDate reservationDate);

//	@Query("select r from Reservation r where r.user = :user and r.seat = :seat and r.reservationDate = :reservationDate and r.blocksReserved = :blocksReserved and r.firstBlockReserved = :firstBlockReserved and r.eliminatedAt is null")
//	public Optional<Reservation> findIfAlreadyPersistedInGivenDate(@Param("user") User user,@Param("seat") Seat seat,@Param("reservationDate") LocalDate reservationDate,@Param("firstBlockReserved") int firstBlockReserved,
//			@Param("blocksReserved") int blocksReserved);
	
//	@Query("select r from Reservation r where r.user = :user and r.seat = :seat and r.reservationDate = :reservationDate and r.blocksReserved = :blocksReserved and r.firstBlockReserved = :firstBlockReserved and r.eliminatedAt is not null")
//	public Optional<Reservation> findIfAlreadyPersistedAndEliminated(@Param("user") User user,@Param("seat") Seat seat,@Param("reservationDate") LocalDate reservationDate,@Param("firstBlockReserved") int firstBlockReserved,
//			@Param("blocksReserved") int blocksReserved);	
}


//select * from test.reservation where id = (select MAX(id) from test.reservation where first_block_reserved = 6 or (first_block_reserved < 6 and 10 < first_block_reserved + blocks_reserved));
