package com.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guestbook.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Returns the userId by username
	 * 
	 * @return userid
	 */
	public long getUseridByUsername(String username) {
		return userRepository.getUserIdByUsername(username);
	}

}
