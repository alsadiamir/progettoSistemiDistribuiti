package it.unibo.canteen.dao;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import it.unibo.canteen.model.Reservation;
import it.unibo.canteen.model.Seat;
import it.unibo.canteen.model.User;

@Transactional
public interface ReservationDAO extends CrudRepository<Reservation, Integer> {
	public List<Reservation> findByUserId(int userId);
	
	@Query("from Reservation where user = :user_id and seat = :seat_id and "
			+ ":first_block_reserved < firstBlockReserved and firstBlockReserved > :first_block_reserved + :blocks_reserved and eliminatedAt is null")
	public List<Reservation> findCollidingReservation(@Param("user_id") User user, @Param("seat_id") Seat seat, @Param("first_block_reserved") int first_block_reserved, 
			@Param("blocks_reserved") int blocks_reserved);
	
	@Query("from Reservation r where r.id = (select MAX(id) from Reservation where seat = :seat_id and firstBlockReserved = :first_block_reserved and reservationDate = :reservation_date and eliminatedAt is null)")
	public List<Reservation> findLastCollidingReservation(@Param("seat_id") Seat seat, @Param("first_block_reserved") int first_block_reserved, @Param("reservation_date") LocalDate reservationDate);

}


//select * from test.reservation where id = (select MAX(id) from test.reservation where first_block_reserved = 6 or (first_block_reserved < 6 and 10 < first_block_reserved + blocks_reserved));
