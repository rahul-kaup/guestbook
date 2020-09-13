package com.guestbook.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guestbook.bean.MessageBean;
import com.guestbook.entity.NoteMessage;
import com.guestbook.entity.PictureMessage;
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
	public MessageBean convert(NoteMessage messageEntity) {
		return new MessageBean(messageEntity.getMessageId(), userRepository.findById(messageEntity.getUserId()).get().getFullname(), messageEntity.getNote(), null, (messageEntity.getIsApproved() == Boolean.TRUE) ? true : false);
	}

	/**
	 * Transfers message data from db entity to model bean
	 * 
	 * @param messageEntity
	 * @return MessageBean
	 */
	public MessageBean convert(PictureMessage messageEntity) {
		return new MessageBean(messageEntity.getMessageId(), userRepository.findById(messageEntity.getUserId()).get().getFullname(), null, messageEntity.getImage(), (messageEntity.getIsApproved() == Boolean.TRUE) ? true : false);
	}
}