package com.guestbook.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.guestbook.entity.Message;
import com.guestbook.repository.UserRepository;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void testGetUserIdByUsername() {

		// create a test message
		Message message = new Message();
		message.setNote("my note");
		message.setUserId(1);

		// mock the message repo to return the message
		when(userRepository.getUserIdByUsername("username")).thenReturn(1L);

		// make the call
		assertEquals(1, userService.getUseridByUsername("username"));

	}

}
