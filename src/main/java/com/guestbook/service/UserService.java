package com.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guestbook.bean.UserBean;
import com.guestbook.entity.User;
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

	public boolean createUser(UserBean userBean) {
		User user = new User();
		user.setUsername(userBean.getEmail());
		user.setFullname(userBean.getFirstName().concat(" ").concat(userBean.getLastName()));
		user.setRole("USER");
		user.setPassword(userBean.getPassword());
		return userRepository.save(user) != null;
	}

}
