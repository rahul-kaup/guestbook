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
import com.guestbook.entity.Message;
import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;

@ExtendWith(SpringExtension.class)
public class MesssageEntityToBeanConverterTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private MesssageEntityToBeanConverter messsageEntityToBeanConverter;

	@Test
	void testConvertOfApprovedMessage() {

		// create a test message
		Message message = new Message();
		message.setNote("note");
		message.setUserId(1);
		message.setIsApproved(1);

		User user = new User(1L, "username", "fullname", "role", "password");

		// mock the user repo
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		MessageBean messageBean = messsageEntityToBeanConverter.convert(message);
		assertEquals(messageBean.getNote(), "note");
		assertTrue(messageBean.isApproved());
	}

	@Test
	void testConvertOfUnApprovedMessage() {

		// create a test message
		Message message = new Message();
		message.setNote("note");
		message.setUserId(1);
		message.setIsApproved(0);

		User user = new User(1L, "username", "fullname", "role", "password");

		// mock the user repo
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		MessageBean messageBean = messsageEntityToBeanConverter.convert(message);
		assertEquals(messageBean.getNote(), "note");
		assertFalse(messageBean.isApproved());
	}
}
