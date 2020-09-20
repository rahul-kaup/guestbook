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
		when(userRepository.getUserIdByUsername("username")).thenReturn(Long.valueOf(1L));

		// make the call and verfiy user id
		assertEquals(Long.valueOf(1L), userService.getUseridByUsername("username"));
	}

	@Test
	void testCreateUserSuccess() {

		// create test user data
		User user = new User();
		user.setUsername("email");
		user.setFullname("firstName lastName");
		user.setRole("USER");
		user.setPassword("password");

		// mock the user repo
		when(userRepository.save(user)).thenReturn(user);

		// make the call and verify success response
		assertTrue(userService.createUser(new UserBean("email", "firstName", "lastName", "password")));
	}

	@Test
	void testCreateUserFailure() {

		User user = new User(Long.valueOf(1), "email", "firstName lastName", "USER", "password");

		// mock the user repo
		when(userRepository.save(user)).thenReturn(null);

		// make the call and verify success response
		assertFalse(userService.createUser(new UserBean("email", "firstName", "lastName", "password")));
	}

	@Test
	void testIsUserAlreadyRegistered() {

		// create test user data
		User user = new User();
		user.setUsername("test@test.com");
		user.setFullname("firstName lastName");
		user.setRole("USER");
		user.setPassword("password");

		// mock the user repo
		when(userRepository.getUserByUsername("test@test.com")).thenReturn(user);

		// make the call and verify success response
		assertTrue(userService.isUserAlreadyRegistered("test@test.com"));
	}
	
	@Test
	void testIsUserNotAlreadyRegistered() {

		// create test user data
		User user = new User();
		user.setUsername("test@test.com");
		user.setFullname("firstName lastName");
		user.setRole("USER");
		user.setPassword("password");

		// mock the user repo
		when(userRepository.getUserByUsername("test@test.com")).thenReturn(null);

		// make the call and verify success response
		assertFalse(userService.isUserAlreadyRegistered("test@test.com"));
	}

}
