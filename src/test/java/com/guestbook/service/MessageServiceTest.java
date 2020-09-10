package com.guestbook.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.guestbook.bean.MessageBean;
import com.guestbook.converter.MesssageEntityToBeanConverter;
import com.guestbook.entity.Message;
import com.guestbook.repository.MessageRepository;
import com.guestbook.repository.UserRepository;

@ExtendWith(SpringExtension.class)
class MessageServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private MesssageEntityToBeanConverter messsageEntityToBeanConverter;

	@Mock
	private SecurityContextHolder securityContextHolder;

	@Mock
	private Authentication authentication;

	@Mock
	private UserService userService;

	@InjectMocks
	private MessageService messageService;

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(userRepository).isNotNull();
		assertThat(messageRepository).isNotNull();
	}

	@Test
	void testGetAllMessages() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);

		// mock the message repo to return the message
		when(messageRepository.getAllMessages()).thenReturn(Lists.list(message));

		// mock the converter
		when(messsageEntityToBeanConverter.convert(message)).thenReturn(new MessageBean(1L, "author", "note", null, false));

		// make the call
		List<MessageBean> messageBeanList = messageService.getAllMessages();

		// verify that the message bean
		assertEquals("verify there's one message", 1, messageBeanList.size());
		MessageBean messageBean = messageBeanList.get(0);
		assertEquals("verify messageId is generated", 1, messageBean.getMessageId());
		assertEquals("verify note ", "note", messageBean.getNote());
		assertFalse("verify isApproved is set as false", messageBean.isApproved());
		assertEquals("verify author", "author", messageBean.getAuthor());
	}

	@Test
	void testGetApprovedMessages() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the message repo to return the message
		when(messageRepository.getApprovedMessages()).thenReturn(Lists.list(message));

		// mock the converter
		when(messsageEntityToBeanConverter.convert(message)).thenReturn(new MessageBean(1L, "author", "note", null, true));

		// make the call
		List<MessageBean> messageBeanList = messageService.getApprovedMessages();

		// verify that the message bean
		assertEquals("verify there's one message", 1, messageBeanList.size());
		MessageBean messageBean = messageBeanList.get(0);
		assertEquals("verify messageId is generated", 1, messageBean.getMessageId());
		assertEquals("verify note ", "note", messageBean.getNote());
		assertTrue("verify isApproved is set as true", messageBean.isApproved());
		assertEquals("verify author", "author", messageBean.getAuthor());
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddNoteMessageSuccess() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);

		// mock
		// when(userRepository.getUserIdByUsername("user")).thenReturn(1L);
		when(userService.getUseridByUsername("user")).thenReturn(1L);

		// mock the message repo to return the message
		when(messageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.addMessage("my note"));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddImageMessageSuccess() throws IOException {

		// create a test message
		Message message = new Message();
		message.setImage("IMG".getBytes());
		message.setUserId(1);

		// mock
		// when(userRepository.getUserIdByUsername("user")).thenReturn(1L);
		when(userService.getUseridByUsername("user")).thenReturn(1L);

		// mock the message repo to return the message
		when(messageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.addMessage("IMG".getBytes()));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddNoteFailureWithIllegalArgumentException() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");

		// mock the message repo save to throw exception
		when(messageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.addMessage("my note"));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddImageFailureWithIllegalArgumentException() {

		// create a test message
		Message message = new Message();
		message.setImage("IMG".getBytes());

		// mock the message repo save to throw exception
		when(messageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.addMessage("IMG".getBytes()));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddNoteFailure() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(messageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.addMessage("my note"));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddImageFailure() {

		// create a test message
		Message message = new Message();
		message.setImage("IMG".getBytes());
		message.setUserId(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(messageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.addMessage("IMG".getBytes()));
	}

	@Test
	void testEditMessageSuccess() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(messageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.editMessage(1, "my note"));
	}

	@Test
	void testEditMessageFailureWithIllegalArgumentException() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo save to throw exception
		when(messageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.editMessage(1, "my note"));
	}

	@Test
	void testEditMessageFailure() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(messageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.editMessage(1, "my note"));
	}

	@Test
	void testEditMessageMessageNotFound() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to not find message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.empty());

		// make the call and assert true
		assertFalse(messageService.editMessage(1, "my note"));
	}

	@Test
	void testApproveMessageSuccess() {

		// create a test message
		Message message = new Message();
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(messageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.approveMessage(1));
	}

	@Test
	void testApproveMessageFailure() {

		// create a test message
		Message message = new Message();
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(messageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.approveMessage(1));
	}

	@Test
	void testApproveMessageFailureWithIllegalArgumentException() {

		// create a test message
		Message message = new Message();
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(messageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.approveMessage(1));
	}

	@Test
	void testApproveMessageMessageNotFound() {

		// create a test message
		Message message = new Message();
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to not find message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.empty());

		// make the call and assert true
		assertFalse(messageService.approveMessage(1));
	}

	@Test
	void testDeleteMessageSuccess() {

		// create a test message
		Message message = new Message();
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(messageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.deleteMessage(1));
	}

	@Test
	void testDeleteMessageFailureWithIllegalArgumentException() {

		// create a test message
		Message message = new Message();
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(messageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.deleteMessage(1));
	}

	@Test
	void testDeleteMessageFailure() {

		// create a test message
		Message message = new Message();
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to find the message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(messageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.deleteMessage(1));
	}

	@Test
	void testDeleteMessageMessageNotFound() {

		// create a test message
		Message message = new Message();
		message.setUserId(1);
		message.setIsApproved(1);

		// mock the messages repo to not find message by id
		when(messageRepository.findById(1L)).thenReturn(Optional.empty());

		// make the call and assert true
		assertFalse(messageService.deleteMessage(1));
	}

}
