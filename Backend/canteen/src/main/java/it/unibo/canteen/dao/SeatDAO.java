package it.unibo.canteen.dao;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import it.unibo.canteen.model.Seat;

@Transactional
public interface SeatDAO extends CrudRepository<Seat, Integer> {
	public List<Seat> findByRoomId(int roomId);
}
