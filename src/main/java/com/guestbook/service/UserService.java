package com.guestbook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guestbook.bean.UserBean;
import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;
import com.guestbook.util.GuestbookConstants;

@Service
public class UserService implements GuestbookConstants {

	@Autowired
	private UserRepository userRepository;

	private final Logger logger = LoggerFactory.getLogger(UserService.class);

	/**
	 * Returns the userId by username
	 * 
	 * @return userid
	 */
	public Long getUseridByUsername(String username) {
		logger.debug("getUseridByUsername() :: username = {}", username);
		return userRepository.getUserIdByUsername(username);
	}

	public boolean createUser(UserBean userBean) {
		logger.debug("getUseridByUsername() :: username = {}", userBean.getEmail());
		User user = new User();
		user.setUsername(userBean.getEmail());
		user.setFullname(String.format("%s %s", userBean.getFirstName(), userBean.getLastName()));
		user.setRole(USER);
		user.setPassword(userBean.getPassword());
		return userRepository.save(user) != null;
	}

}
