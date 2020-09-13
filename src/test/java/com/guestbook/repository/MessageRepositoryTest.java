package com.guestbook.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.guestbook.entity.NoteMessage;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MessageRepositoryTest {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NoteMessageRepository noteMessageRepository;

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(userRepository).isNotNull();
		assertThat(noteMessageRepository).isNotNull();
	}

	@Test
	void testThatGetAllMessagesReturnsAllMessages() {

		// add an unapproved message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		noteMessageRepository.save(message);

		// verify that the message is retrieved with default values
		List<NoteMessage> messageList = noteMessageRepository.getAllMessages();
		assertEquals("verify there's just one message", 1, messageList.size());
		NoteMessage retrievedMessage = messageList.get(0);
		assertEquals("verify messageId is generated", Long.valueOf(1), retrievedMessage.getMessageId());
		assertEquals("verify note ", "my note", retrievedMessage.getNote());
		assertEquals("verify isApproved is set as false", Boolean.FALSE, retrievedMessage.getIsApproved());
		assertEquals("verify isDeleted is set as false", Boolean.FALSE, retrievedMessage.getIsDeleted());
	}

	@Test
	void testThatGetAllMessagesDoesNotReturnDeletedMessages() {

		// add an unapproved message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		noteMessageRepository.save(message);

		// verify that the message is retrieved with default values
		List<NoteMessage> messageList = noteMessageRepository.getAllMessages();
		assertEquals("verify there's just one message", 1, messageList.size());
		assertEquals("verify note ", "my note", messageList.get(0).getNote());

		// delete the message
		message.setIsDeleted(Boolean.TRUE);
		noteMessageRepository.save(message);

		// verify the deleted message is not returned
		assertEquals("verify there's no message", 0, noteMessageRepository.getAllMessages().size());
	}

	@Test
	void testThatGetApprovedMessagesReturnsOnlyApprovedMessages() {

		// add an unapproved message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		noteMessageRepository.save(message);

		// verify no messages are returned
		assertEquals("verify there's no approved message", 0, noteMessageRepository.getApprovedMessages().size());

		// approve the message
		message.setIsApproved(Boolean.TRUE);
		noteMessageRepository.save(message);

		// verify the approved message is returned
		NoteMessage retrievedMessage = noteMessageRepository.getApprovedMessages().get(0);
		assertEquals("verify note ", "my note", retrievedMessage.getNote());
		assertEquals("verify isApproved is set as true", Boolean.TRUE, retrievedMessage.getIsApproved());
		assertEquals("verify isDeleted is set as false", Boolean.FALSE, retrievedMessage.getIsDeleted());

	}

	@Test
	void testThatGetApprovedMessagesDoesNotReturnDeletedMessages() {

		// add a non deleted approved message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setIsApproved(Boolean.TRUE);
		noteMessageRepository.save(message);

		// verify the message is returned
		assertEquals("verify there's 1 message", 1, noteMessageRepository.getApprovedMessages().size());
		assertEquals("verify the note", "my note", noteMessageRepository.getApprovedMessages().get(0).getNote());

		// delete the message
		message.setIsDeleted(Boolean.TRUE);
		noteMessageRepository.save(message);

		// verify the deleted message is not returned
		assertEquals("verify there's no message", 0, noteMessageRepository.getApprovedMessages().size());

	}
}
