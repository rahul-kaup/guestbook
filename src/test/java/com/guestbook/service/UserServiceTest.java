package com.guestbook.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.guestbook.bean.UserBean;
import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void testGetUserIdByUsername() {

		// mock the user repo
		when(userRepository.getUserIdByUsername("username")).thenReturn(1L);

		// make the call and verfiy user id
		assertEquals(1, userService.getUseridByUsername("username"));
	}

	@Test
	void testCreateUserSuccess() {

		// create test user data
		User user = new User(0, "email", "firstName lastName", "USER", "password");

		// mock the user repo
		when(userRepository.save(user)).thenReturn(user);

		// make the call and verify success response
		assertTrue(userService.createUser(new UserBean("email", "firstName", "lastName", "password")));
	}

	@Test
	void testCreateUserFailure() {

		// create test user data
		User user = new User(0, "email", "firstName lastName", "USER", "password");

		// mock the user repo
		when(userRepository.save(user)).thenReturn(null);

		// make the call and verify success response
		assertFalse(userService.createUser(new UserBean("email", "firstName", "lastName", "password")));
	}

}
