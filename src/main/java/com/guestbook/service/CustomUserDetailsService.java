package com.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("deprecation")
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		if (email.equals("admin@guestbook.com")) { // default admin
			return org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username("admin@guestbook.com").password("pass").roles("ADMIN").build();
		} else if (email.equals("user@guestbook.com")) { // default user
			return org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username("user@guestbook.com").password("pass").roles("USER").build();
		} else { // pick user details from db
			User user = userRepository.getUserByUsername(email);
			return org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build();
		}
	}

}
