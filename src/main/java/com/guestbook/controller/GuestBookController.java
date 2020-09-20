package com.guestbook.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.guestbook.bean.UserBean;
import com.guestbook.service.MessageService;
import com.guestbook.service.UserService;
import com.guestbook.util.GuestbookConstants;
import com.guestbook.util.SecurityUtil;

/**
 * Controller for guestbook requests
 */
@Controller
public class GuestBookController implements GuestbookConstants {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	private Logger logger = LoggerFactory.getLogger(GuestBookController.class);

	/**
	 * the home page of the application
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(MAPPING_GUESTBOOK_HOME)
	public String home(Model model) {

		// show all messages to admin and show only approved messages to user
		if (SecurityUtil.isLoggedInUserOfRoleAdmin()) {
			logger.debug("setting all messages for admin role");
			model.addAttribute(MODEL_ATTRIBUTE_MESSAGES, messageService.getAllMessages());
		} else {
			logger.debug("setting approved messages for anonymous and user role");
			model.addAttribute(MODEL_ATTRIBUTE_MESSAGES, messageService.getApprovedMessages());
		}

		return VIEW_HOME;
	}

	/**
	 * Mapping method for adding a new message
	 * 
	 * @param model
	 * @param note
	 * @param picture
	 * @param messageType
	 * @return
	 * @throws IOException
	 */
	@PostMapping(MAPPING_GUESTBOOK_ADD)
	@Secured(ROLE_USER)
	public RedirectView addMessage(Model model, @RequestParam String note, @RequestParam("picture") MultipartFile picture, @RequestParam("messageType") String messageType) throws IOException {
		logger.debug("addMessage() :: messageType = {}, note = {}, pictureSize = {}", messageType, note, Long.valueOf(picture.getSize()));

		if (MESSAGE_TYPE_NOTE.equals(messageType)) {
			messageService.addMessage(note);
		} else {
			messageService.addMessage(picture.getBytes());
		}
		return new RedirectView(MAPPING_GUESTBOOK_HOME);
	}

	/**
	 * Mapping method for editing an existing message
	 * 
	 * @param model
	 * @param messageId
	 * @param editedNote
	 * @return
	 * @throws IOException
	 */
	@PostMapping(MAPPING_GUESTBOOK_EDIT)
	@Secured(ROLE_ADMIN)
	public RedirectView editMessage(Model model, @RequestParam() String messageId, @RequestParam String editedNote) throws IOException {
		logger.debug("editMessage() :: messageId = {}, editedNote = {}", messageId, editedNote);
		if (SecurityUtil.isLoggedInUserOfRoleAdmin()) {
			messageService.editMessage(Long.valueOf(messageId), editedNote);
			return new RedirectView(MAPPING_GUESTBOOK_HOME);
		}
		return new RedirectView(REDIRECT_LOGIN_UNAUTHORIZED);
	}

	/**
	 * Mapping method for approving a message
	 * 
	 * @param model
	 * @param messageId
	 * @return
	 */
	@PostMapping(MAPPING_GUESTBOOK_APPROVE)
	@Secured(ROLE_ADMIN)
	public RedirectView approveMessage(Model model, @RequestParam Long messageId) {
		logger.debug("approveMessage() :: messageId = {}", messageId);
		if (SecurityUtil.isLoggedInUserOfRoleAdmin()) {
			messageService.approveMessage(messageId);
			return new RedirectView(MAPPING_GUESTBOOK_HOME);
		}
		return new RedirectView(REDIRECT_LOGIN_UNAUTHORIZED);
	}

	/**
	 * Mapping method for deleting a message
	 * 
	 * @param model
	 * @param messageId
	 * @return
	 */
	@PostMapping(MAPPING_GUESTBOOK_DELETE)
	@Secured(ROLE_ADMIN)
	public RedirectView deleteMessage(Model model, @RequestParam Long messageId) {
		logger.debug("deleteMessage() :: messageId = {}", messageId);
		if (SecurityUtil.isLoggedInUserOfRoleAdmin()) {
			messageService.deleteMessage(messageId);
			return new RedirectView(MAPPING_GUESTBOOK_HOME);
		}
		return new RedirectView(REDIRECT_LOGIN_UNAUTHORIZED);
	}

	@PostMapping(MAPPING_GUESTBOOK_REGISTER)
	public RedirectView createUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {
		logger.debug("createUser() :: email = {}", email);

		if (userService.isUserAlreadyRegistered(email)) {
			logger.warn("createUser() :: email {} already registered", email);
			return new RedirectView(REDIRECT_REGISTRATION_ALREADY_REGISTERED);
		}

		userService.createUser(new UserBean(email, firstName, lastName, password));
		return new RedirectView(REDIRECT_LOGIN);
	}

}
