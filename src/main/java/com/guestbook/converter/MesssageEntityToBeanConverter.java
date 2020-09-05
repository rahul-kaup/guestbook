package com.guestbook.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guestbook.bean.MessageBean;
import com.guestbook.entity.Message;
import com.guestbook.repository.UserRepository;

@Component
public class MesssageEntityToBeanConverter {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Transfers message data from db entity to model bean
	 * 
	 * @param messageEntity
	 * @return MessageBean
	 */
	public MessageBean convert(Message messageEntity) {
		return new MessageBean(messageEntity.getMessageId(), userRepository.findById(messageEntity.getUserId()).get().getFullname(), messageEntity.getNote(), messageEntity.getImage(), (messageEntity.getIsApproved() == 1) ? true : false);
	}
}