package com.guestbook.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.guestbook.bean.MessageBean;
import com.guestbook.converter.MesssageEntityToBeanConverter;
import com.guestbook.entity.NoteMessage;
import com.guestbook.entity.PictureMessage;
import com.guestbook.repository.NoteMessageRepository;
import com.guestbook.repository.PictureMessageRepository;
import com.guestbook.util.SecurityUtil;

import lombok.AllArgsConstructor;

/**
 * Service class for all message related features
 *
 */
@AllArgsConstructor
@Service
public class MessageService {

	private final NoteMessageRepository noteMessageRepository;
	private final PictureMessageRepository pictureMessageRepository;
	private final UserService userService;
	private final MesssageEntityToBeanConverter messsageEntityToBeanConverter;
	private final Logger logger = LoggerFactory.getLogger(MessageService.class);

	/**
	 * Returns list of all messages including non-approved ones
	 * 
	 * @return
	 */
	public List<MessageBean> getAllMessages() {
		List<MessageBean> messages = new ArrayList<>();

		// add all note messages
		messages.addAll(noteMessageRepository.getAllMessages().stream().map(messsageEntityToBeanConverter::convert).collect(Collectors.toList()));

		// add all picture messages
		messages.addAll(pictureMessageRepository.getAllMessages().stream().map(messsageEntityToBeanConverter::convert).collect(Collectors.toList()));

		// sort for display
		Collections.sort(messages);

		return messages;
	}

	/**
	 * Returns list of all approved messages
	 * 
	 * @return
	 */
	public List<MessageBean> getApprovedMessages() {
		List<MessageBean> messages = new ArrayList<>();

		// add all note messages
		messages.addAll(noteMessageRepository.getApprovedMessages().stream().map(messsageEntityToBeanConverter::convert).collect(Collectors.toList()));

		// add all picture messages
		messages.addAll(pictureMessageRepository.getApprovedMessages().stream().map(messsageEntityToBeanConverter::convert).collect(Collectors.toList()));

		// sort for display
		Collections.sort(messages);

		return messages;
	}

	/**
	 * Add a message of type note
	 * 
	 * @param message String
	 */
	public boolean addMessage(String message) {
		logger.debug("addMessage() :: message = {}", message);
		try {
			NoteMessage messageEntity = new NoteMessage();
			messageEntity.setNote(message);
			messageEntity.setUserId(userService.getUseridByUsername(SecurityUtil.getLoggedInUsername()));
			return noteMessageRepository.save(messageEntity) != null;
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
		logger.debug("addMessage() :: image size = {}", Integer.valueOf(image.length));
		try {
			PictureMessage messageEntity = new PictureMessage();
			messageEntity.setImage(image);
			messageEntity.setUserId(userService.getUseridByUsername(SecurityUtil.getLoggedInUsername()));
			return pictureMessageRepository.save(messageEntity) != null;
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
	public boolean editMessage(Long messageId, String note) {
		logger.debug("editMessage() :: messageId = {}, note = {}", messageId, note);
		try {
			Optional<NoteMessage> messageEntity = noteMessageRepository.findById(messageId);
			if (messageEntity.isPresent()) {
				NoteMessage message = messageEntity.get();
				message.setNote(note);
				return noteMessageRepository.save(message) != null;
			}
			logger.error("editMessage() :: message not found while attempting to edit");
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
	public boolean approveMessage(Long messageId) {
		logger.debug("approveMessage() :: messageId = {}", messageId);

		try {
			// approve note message
			Optional<NoteMessage> messageEntity = noteMessageRepository.findById(messageId);
			if (messageEntity.isPresent()) {
				NoteMessage message = messageEntity.get();
				message.setIsApproved(Boolean.TRUE);
				return noteMessageRepository.save(message) != null;
			}

			// or approve picture message
			Optional<PictureMessage> pictureMessageEntity = pictureMessageRepository.findById(messageId);
			if (pictureMessageEntity.isPresent()) {
				PictureMessage pictureMessage = pictureMessageEntity.get();
				pictureMessage.setIsApproved(Boolean.TRUE);
				return pictureMessageRepository.save(pictureMessage) != null;
			}

			logger.error("approveMessage() :: message with id {} not found while attempting to approve", messageId);
			return false;
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
	public boolean deleteMessage(Long messageId) {
		logger.debug("deleteMessage() :: messageId = {}", messageId);
		try {
			// delete note message
			Optional<NoteMessage> messageEntity = noteMessageRepository.findById(messageId);
			if (messageEntity.isPresent()) {
				NoteMessage message = messageEntity.get();
				message.setIsDeleted(Boolean.TRUE);
				return noteMessageRepository.save(message) != null;
			}

			// or delete picture message
			Optional<PictureMessage> pictureMessageEntity = pictureMessageRepository.findById(messageId);
			if (pictureMessageEntity.isPresent()) {
				PictureMessage pictureMessage = pictureMessageEntity.get();
				pictureMessage.setIsDeleted(Boolean.TRUE);
				return pictureMessageRepository.save(pictureMessage) != null;
			}
			logger.error("deleteMessage() :: message with id {} not found while attempting to delete", messageId);
		} catch (IllegalArgumentException e) {
			logger.error("deleteMessage() :: approveMessage() :: exception while approving message", e);
		}
		return false;
	}

}
