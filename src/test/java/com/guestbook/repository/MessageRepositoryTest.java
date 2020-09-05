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

import com.guestbook.entity.Message;

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
	private MessageRepository messageRepository;

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(userRepository).isNotNull();
		assertThat(messageRepository).isNotNull();
	}

	@Test
	void testThatGetAllMessagesReturnsAllMessages() {

		// add an unapproved message
		Message message = new Message();
		message.setNote("my note");
		messageRepository.save(message);

		// verify that the message is retrieved with default values
		List<Message> messageList = messageRepository.getAllMessages();
		assertEquals("verify there's just one message", 1, messageList.size());
		Message retrievedMessage = messageList.get(0);
		assertEquals("verify messageId is generated", 1L, retrievedMessage.getMessageId());
		assertEquals("verify note ", "my note", retrievedMessage.getNote());
		assertEquals("verify isApproved is set as 0", 0, retrievedMessage.getIsApproved());
		assertEquals("verify isDeleted is set as 0", 0, retrievedMessage.getIsDeleted());
	}

	@Test
	void testThatGetAllMessagesDoesNotReturnDeletedMessages() {

		// add an unapproved message
		Message message = new Message();
		message.setNote("my note");
		messageRepository.save(message);

		// verify that the message is retrieved with default values
		List<Message> messageList = messageRepository.getAllMessages();
		assertEquals("verify there's just one message", 1, messageList.size());
		assertEquals("verify note ", "my note", messageList.get(0).getNote());

		// delete the message
		message.setIsDeleted(1);
		messageRepository.save(message);

		// verify the deleted message is not returned
		assertEquals("verify there's no message", 0, messageRepository.getAllMessages().size());
	}

	@Test
	void testThatGetApprovedMessagesReturnsOnlyApprovedMessages() {

		// add an unapproved message
		Message message = new Message();
		message.setNote("my note");
		messageRepository.save(message);

		// verify no messages are returned
		assertEquals("verify there's no approved message", 0, messageRepository.getApprovedMessages().size());

		// approve the message
		message.setIsApproved(1);
		messageRepository.save(message);

		// verify the approved message is returned
		Message retrievedMessage = messageRepository.getApprovedMessages().get(0);
		assertEquals("verify note ", "my note", retrievedMessage.getNote());
		assertEquals("verify isApproved is set as 1", 1, retrievedMessage.getIsApproved());
		assertEquals("verify isDeleted is set as 0", 0, retrievedMessage.getIsDeleted());

	}

	@Test
	void testThatGetApprovedMessagesDoesNotReturnDeletedMessages() {

		// add a non deleted approved message
		Message message = new Message();
		message.setNote("my note");
		message.setIsApproved(1);
		messageRepository.save(message);

		// verify the message is returned
		assertEquals("verify there's 1 message", 1, messageRepository.getApprovedMessages().size());
		assertEquals("verify the note", "my note", messageRepository.getApprovedMessages().get(0).getNote());

		// delete the message
		message.setIsDeleted(1);
		messageRepository.save(message);

		// verify the deleted message is not returned
		assertEquals("verify there's no message", 0, messageRepository.getApprovedMessages().size());

	}
}
