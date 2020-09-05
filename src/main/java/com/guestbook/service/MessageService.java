package com.guestbook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.guestbook.bean.MessageBean;
import com.guestbook.converter.MesssageEntityToBeanConverter;
import com.guestbook.entity.Message;
import com.guestbook.repository.MessageRepository;
import com.guestbook.util.SecurityUtil;

import lombok.AllArgsConstructor;

/**
 * Service class for all message related features
 *
 */
@AllArgsConstructor
@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final UserService userService;
	private final MesssageEntityToBeanConverter messsageEntityToBeanConverter;
	private final Logger logger = LoggerFactory.getLogger(MessageService.class);

	/**
	 * Returns list of all messages including non-approved ones
	 * 
	 * @return
	 */
	public List<MessageBean> getAllMessages() {
		return messageRepository.getAllMessages().stream().map(messsageEntityToBeanConverter::convert).collect(Collectors.toList());
	}

	/**
	 * Returns list of all approved messages
	 * 
	 * @return
	 */
	public List<MessageBean> getApprovedMessages() {
		return messageRepository.getApprovedMessages().stream().map(messsageEntityToBeanConverter::convert).collect(Collectors.toList());
	}

	/**
	 * Add a message of type note
	 * 
	 * @param message String
	 */
	public boolean addMessage(String message) {
		logger.debug("addMessage() :: message = " + message);
		try {
			Message messageEntity = new Message();
			messageEntity.setNote(message);
			messageEntity.setUserId(userService.getUseridByUsername(SecurityUtil.getLoggedInUsername()));
			return messageRepository.save(messageEntity) != null;
		} catch (IllegalArgumentException e) {
			logger.error("addMessage() :: exception while adding note message ", e);
		}
		return false;
	}

	/**
	 * Add a message of type picture
	 * 
	 * @param image byte[]
	 */
	public boolean addMessage(byte[] image) {
		logger.debug("addMessage() :: image size = " + image.length);
		try {
			Message messageEntity = new Message();
			messageEntity.setImage(image);
			messageEntity.setUserId(userService.getUseridByUsername(SecurityUtil.getLoggedInUsername()));
			return messageRepository.save(messageEntity) != null;
		} catch (IllegalArgumentException e) {
			logger.error("addMessage() :: exception while adding image message", e);
		}
		return false;
	}

	/**
	 * Edits a message
	 * 
	 * @param messageId long
	 * @param note      String
	 */
	public boolean editMessage(long messageId, String note) {
		logger.debug("editMessage() :: messageId = " + messageId + ", note = " + note);
		try {
			Optional<Message> messageEntity = messageRepository.findById(messageId);
			if (messageEntity.isPresent()) {
				Message message = messageEntity.get();
				message.setNote(note);
				return messageRepository.save(message) != null;
			} else {
				logger.error("editMessage() :: message not found while attempting to edit");
			}
		} catch (IllegalArgumentException e) {
			logger.error("editMessage() :: exception while editing message ", e);
		}
		return false;
	}

	/**
	 * Approves a message
	 * 
	 * @param messageId long
	 */
	public boolean approveMessage(long messageId) {
		logger.debug("approveMessage() :: messageId = " + messageId);
		Optional<Message> messageEntity = messageRepository.findById(messageId);
		try {
			if (messageEntity.isPresent()) {
				Message message = messageEntity.get();
				message.setIsApproved(1);
				return messageRepository.save(message) != null;
			} else {
				logger.error("approveMessage() :: message with id " + messageId + " not found while attempting to approve");
				return false;
			}
		} catch (IllegalArgumentException e) {
			logger.error("approveMessage() :: exception while approving message", e);
			return false;
		}
	}

	/**
	 * Deletes a message
	 * 
	 * @param messageId long
	 */
	public boolean deleteMessage(long messageId) {
		logger.debug("deleteMessage() :: messageId = " + messageId);
		try {
			Optional<Message> messageEntity = messageRepository.findById(messageId);
			if (messageEntity.isPresent()) {
				Message message = messageEntity.get();
				message.setIsDeleted(1);
				return messageRepository.save(message) != null;
			} else {
				logger.error("deleteMessage() :: message with id " + messageId + " not found while attempting to delete");
			}
		} catch (IllegalArgumentException e) {
			logger.error("deleteMessage() :: approveMessage() :: exception while approving message", e);
		}
		return false;
	}

}
