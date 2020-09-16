package com.guestbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.guestbook.entity.PictureMessage;

/**
 * Interface for custom queries on the message table
 */
public interface PictureMessageRepository extends CrudRepository<PictureMessage, Long> {

	@Query(value = "SELECT message_id, user_id, image, is_approved, is_deleted FROM picture_message WHERE is_approved = 1 AND is_deleted = 0 order by message_id desc", nativeQuery = true)
	List<PictureMessage> getApprovedMessages();

	@Query(value = "SELECT message_id, user_id, image, is_approved, is_deleted FROM picture_message WHERE is_deleted = 0 order by message_id desc", nativeQuery = true)
	List<PictureMessage> getAllMessages();

}
