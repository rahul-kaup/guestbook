package com.guestbook.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
import com.guestbook.entity.NoteMessage;
import com.guestbook.entity.PictureMessage;
import com.guestbook.repository.NoteMessageRepository;
import com.guestbook.repository.PictureMessageRepository;
import com.guestbook.repository.UserRepository;

@ExtendWith(SpringExtension.class)
class MessageServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private NoteMessageRepository noteMessageRepository;

	@Mock
	private PictureMessageRepository pictureMessageRepository;

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
		assertThat(noteMessageRepository).isNotNull();
	}

	@Test
	void testGetAllMessages() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(1L));

		// mock the message repo to return the message
		when(noteMessageRepository.getAllMessages()).thenReturn(Lists.list(message));

		// mock the converter
		when(messsageEntityToBeanConverter.convert(message)).thenReturn(new MessageBean(Long.valueOf(1L), "author", "note", null, false));

		// make the call
		List<MessageBean> messageBeanList = messageService.getAllMessages();

		// verify that the message bean
		assertEquals("verify there's one message", 1, messageBeanList.size());
		MessageBean messageBean = messageBeanList.get(0);
		assertEquals("verify messageId is generated", Long.valueOf(1), messageBean.getMessageId());
		assertEquals("verify note ", "note", messageBean.getNote());
		assertFalse("verify isApproved is set as false", messageBean.isApproved());
		assertEquals("verify author", "author", messageBean.getAuthor());
	}

	@Test
	void testGetApprovedMessages() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the message repo to return the message
		when(noteMessageRepository.getApprovedMessages()).thenReturn(Lists.list(message));

		// mock the converter
		when(messsageEntityToBeanConverter.convert(message)).thenReturn(new MessageBean(Long.valueOf(1L), "author", "note", null, true));

		// make the call
		List<MessageBean> messageBeanList = messageService.getApprovedMessages();

		// verify that the message bean
		assertEquals("verify there's one message", 1, messageBeanList.size());
		MessageBean messageBean = messageBeanList.get(0);
		assertEquals("verify messageId is generated", Long.valueOf(1), messageBean.getMessageId());
		assertEquals("verify note ", "note", messageBean.getNote());
		assertTrue("verify isApproved is set as true", messageBean.isApproved());
		assertEquals("verify author", "author", messageBean.getAuthor());
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddNoteMessageSuccess() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(1L));

		// mock the user service
		when(userService.getUseridByUsername("user")).thenReturn(Long.valueOf(1L));

		// mock the message repo to return the message
		when(noteMessageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.addMessage("my note"));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddImageMessageSuccess() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setImage("IMG".getBytes());
		message.setUserId(Long.valueOf(1L));

		// mock
		// when(userRepository.getUserIdByUsername("user")).thenReturn(1L);
		when(userService.getUseridByUsername("user")).thenReturn(Long.valueOf(1L));

		// mock the message repo to return the message
		when(pictureMessageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.addMessage("IMG".getBytes()));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddNoteFailureWithIllegalArgumentException() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(0));

		// mock the message repo save to throw exception
		when(noteMessageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.addMessage("my note"));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddImageFailureWithIllegalArgumentException() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setImage("IMG".getBytes());
		message.setUserId(Long.valueOf(0));

		// mock the message repo save to throw exception
		when(pictureMessageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.addMessage("IMG".getBytes()));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddNoteFailure() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(noteMessageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.addMessage("my note"));
	}

	@Test
	@WithMockUser(roles = { "USER" })
	void testAddImageFailure() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setImage("IMG".getBytes());
		message.setUserId(Long.valueOf(1L));

		// mock the messages repo to find the message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(pictureMessageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.addMessage("IMG".getBytes()));
	}

	@Test
	void testEditNoteMessageSuccess() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(noteMessageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.editMessage(Long.valueOf(1L), "my note"));
	}

	@Test
	void testEditNoteMessageFailureWithIllegalArgumentException() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo save to throw exception
		when(noteMessageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.editMessage(Long.valueOf(1L), "my note"));
	}

	@Test
	void testEditNoteMessageFailure() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(noteMessageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.editMessage(Long.valueOf(1L), "my note"));
	}

	@Test
	void testEditNoteMessageMessageNotFound() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("my note");
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to not find message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.empty());

		// make the call and assert true
		assertFalse(messageService.editMessage(Long.valueOf(1L), "my note"));
	}

	@Test
	void testEditPictureMessageSuccess() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setImage("IMG".getBytes());
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(pictureMessageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.editMessage(Long.valueOf(1L), "IMG".getBytes()));
	}

	@Test
	void testEditPictureMessageFailureWithIllegalArgumentException() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setImage("IMG".getBytes());
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo save to throw exception
		when(pictureMessageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.editMessage(Long.valueOf(1L), "IMG".getBytes()));
	}

	@Test
	void testEditPictureMessageFailure() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setImage("IMG".getBytes());
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(pictureMessageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.editMessage(Long.valueOf(1L), "IMG".getBytes()));
	}

	@Test
	void testEditPictureMessageMessageNotFound() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setImage("IMG".getBytes());
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to not find message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.empty());

		// make the call and assert true
		assertFalse(messageService.editMessage(Long.valueOf(1L), "IMG".getBytes()));
	}

	@Test
	void testApproveNoteMessageSuccess() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(noteMessageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.approveMessage(Long.valueOf(1L)));
	}

	@Test
	void testApprovePictureMessageSuccess() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(pictureMessageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.approveMessage(Long.valueOf(1L)));
	}

	@Test
	void testApproveNoteMessageFailure() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(noteMessageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.approveMessage(Long.valueOf(1L)));
	}

	@Test
	void testApprovePictureMessageFailure() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(pictureMessageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.approveMessage(Long.valueOf(1L)));
	}

	@Test
	void testApproveMessageFailureWithIllegalArgumentException() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(noteMessageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.approveMessage(Long.valueOf(1L)));
	}

	@Test
	void testApproveMessageMessageNotFound() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to not find message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.empty());

		// make the call and assert true
		assertFalse(messageService.approveMessage(Long.valueOf(1L)));
	}

	@Test
	void testDeleteNoteMessageSuccess() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(noteMessageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.deleteMessage(Long.valueOf(1L)));
	}

	@Test
	void testDeletePictureMessageSuccess() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to return the message
		when(pictureMessageRepository.save(message)).thenReturn(message);

		// make the call and assert true
		assertTrue(messageService.deleteMessage(Long.valueOf(1L)));
	}

	@Test
	void testDeleteMessageFailureWithIllegalArgumentException() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(noteMessageRepository.save(message)).thenThrow(new IllegalArgumentException());

		// make the call and assert true
		assertFalse(messageService.deleteMessage(Long.valueOf(1L)));
	}

	@Test
	void testDeleteNoteMessageFailure() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(noteMessageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.deleteMessage(Long.valueOf(1L)));
	}

	@Test
	void testDeletePictureMessageFailure() {

		// create a test message
		PictureMessage message = new PictureMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to find the message by id
		when(pictureMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(message));

		// mock the message repo to not save successfully
		when(pictureMessageRepository.save(message)).thenReturn(null);

		// make the call and assert true
		assertFalse(messageService.deleteMessage(Long.valueOf(1L)));
	}

	@Test
	void testDeleteMessageMessageNotFound() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		// mock the messages repo to not find message by id
		when(noteMessageRepository.findById(Long.valueOf(1L))).thenReturn(Optional.empty());

		// make the call and assert true
		assertFalse(messageService.deleteMessage(Long.valueOf(1L)));
	}

}
