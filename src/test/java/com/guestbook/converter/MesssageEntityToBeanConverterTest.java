package com.guestbook.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.guestbook.bean.MessageBean;
import com.guestbook.entity.NoteMessage;
import com.guestbook.entity.PictureMessage;
import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;

@ExtendWith(SpringExtension.class)
public class MesssageEntityToBeanConverterTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private MesssageEntityToBeanConverter messsageEntityToBeanConverter;

	@Test
	void testConvertOfApprovedNoteMessage() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("note");
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		User user = new User(Long.valueOf(1L), "username", "fullname", "role", "password");

		// mock the user repo
		when(userRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(user));

		MessageBean messageBean = messsageEntityToBeanConverter.convert(message);
		assertEquals(messageBean.getNote(), "note");
		assertTrue(messageBean.isApproved());
	}

	@Test
	void testConvertOfUnApprovedNoteMessage() {

		// create a test message
		NoteMessage message = new NoteMessage();
		message.setNote("note");
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.FALSE);

		User user = new User(Long.valueOf(1L), "username", "fullname", "role", "password");

		// mock the user repo
		when(userRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(user));

		MessageBean messageBean = messsageEntityToBeanConverter.convert(message);
		assertEquals(messageBean.getNote(), "note");
		assertFalse(messageBean.isApproved());
	}

	@Test
	void testConvertOfApprovedPictureMessage() {

		// create a test message
		PictureMessage message = new PictureMessage();
		byte[] imageInBytes = "IMG".getBytes();
		message.setImage(imageInBytes);
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.TRUE);

		User user = new User(Long.valueOf(1L), "username", "fullname", "role", "password");

		// mock the user repo
		when(userRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(user));

		MessageBean messageBean = messsageEntityToBeanConverter.convert(message);
		assertEquals(messageBean.getPicture(), imageInBytes);
		assertTrue(messageBean.isApproved());
	}

	@Test
	void testConvertOfUnApprovedPictureMessage() {

		// create a test message
		PictureMessage message = new PictureMessage();
		byte[] imageInBytes = "IMG".getBytes();
		message.setImage(imageInBytes);
		message.setUserId(Long.valueOf(1L));
		message.setIsApproved(Boolean.FALSE);

		User user = new User(Long.valueOf(1L), "username", "fullname", "role", "password");

		// mock the user repo
		when(userRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(user));

		MessageBean messageBean = messsageEntityToBeanConverter.convert(message);
		assertEquals(messageBean.getPicture(), imageInBytes);
		assertFalse(messageBean.isApproved());
	}
}
