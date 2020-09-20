package com.guestbook.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.guestbook.entity.User;

/**
 * Interface for custom queries on the user table
 */
public interface UserRepository extends CrudRepository<User, Long> {

	@Query(value = "SELECT user_id FROM user WHERE username = ?1", nativeQuery = true)
	Long getUserIdByUsername(String username);

	@Query(value = "SELECT * FROM user WHERE username = ?1", nativeQuery = true)
	User getUserByUsername(String username);

}
