package com.guestbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.guestbook.entity.NoteMessage;

/**
 * Interface for custom queries on the message table
 */
public interface NoteMessageRepository extends CrudRepository<NoteMessage, Long> {

	@Query(value = "SELECT message_id, user_id, note, is_approved, is_deleted FROM note_message WHERE is_approved = 1 AND is_deleted = 0 order by message_id desc", nativeQuery = true)
	List<NoteMessage> getApprovedMessages();

	@Query(value = "SELECT message_id, user_id, note, is_approved, is_deleted FROM note_message WHERE is_deleted = 0 order by message_id desc", nativeQuery = true)
	List<NoteMessage> getAllMessages();

}
