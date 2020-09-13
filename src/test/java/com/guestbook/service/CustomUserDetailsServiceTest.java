package com.guestbook.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;

@ExtendWith(SpringExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	@Test
	void testDefaultAdminDetails() {

		// mock the user repo
		when(userRepository.getUserIdByUsername("username")).thenReturn(Long.valueOf(1L));

		// make the call and verfiy user id
		assertEquals(org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username("admin@guestbook.com").password("pass").roles("ADMIN").build(), customUserDetailsService.loadUserByUsername("admin@guestbook.com"));
	}

	@Test
	void testDefaultUserDetails() {

		// mock the user repo
		when(userRepository.getUserIdByUsername("username")).thenReturn(Long.valueOf(1L));

		// make the call and verfiy user id
		assertEquals(org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username("user@guestbook.com").password("pass").roles("USER").build(), customUserDetailsService.loadUserByUsername("user@guestbook.com"));
	}

	@Test
	void testUserDetails() {

		// create test user data
		User user = new User(Long.valueOf(0), "test@guestbook.com", "firstName lastName", "USER", "password123");

		// mock the user repo
		when(userRepository.getUserByUsername("test@guestbook.com")).thenReturn(user);

		// make the call and verfiy user id
		assertEquals(org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username("test@guestbook.com").password("password123").roles("USER").build(), customUserDetailsService.loadUserByUsername("test@guestbook.com"));
	}

}
