package it.unibo.canteen.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import it.unibo.canteen.model.User;

@Transactional
public interface UserDAO extends CrudRepository<User, Integer> {

	public Optional<User> findByMail(String mail);
}
