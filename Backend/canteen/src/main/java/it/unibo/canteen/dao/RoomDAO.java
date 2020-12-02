package it.unibo.canteen.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import it.unibo.canteen.model.Room;

@Transactional
public interface RoomDAO extends CrudRepository<Room, Integer> {

}
