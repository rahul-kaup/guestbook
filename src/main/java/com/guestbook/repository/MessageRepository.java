package com.guestbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.guestbook.entity.Message;

/**
 * Interface for custom queries on the message table
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

	@Query(value = "SELECT message_id, user_id, note, image, is_approved, is_deleted FROM message WHERE is_approved = 1 AND is_deleted = 0 order by message_id desc", nativeQuery = true)
	List<Message> getApprovedMessages();

	@Query(value = "SELECT message_id, user_id, note, image, is_approved, is_deleted FROM message WHERE is_deleted = 0 order by message_id desc", nativeQuery = true)
	List<Message> getAllMessages();

}
