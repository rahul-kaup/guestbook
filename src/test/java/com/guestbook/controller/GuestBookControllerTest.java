package com.guestbook.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.guestbook.repository.UserRepository;
import com.guestbook.service.CustomUserDetailsService;
import com.guestbook.service.MessageService;
import com.guestbook.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(GuestBookController.class)
public class GuestBookControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MessageService messageService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private UserService userService;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;

	@WithAnonymousUser
	@Test
	public void testHomeWithAnonymousUser() throws Exception {
		mvc.perform(get("/guestbook")).andExpect(status().isOk()).andExpect(content().string(not(containsString("Please leave us a message!"))));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	public void testHomeWithAdminUser() throws Exception {
		mvc.perform(get("/guestbook")).andExpect(status().isOk()).andExpect(content().string(not(containsString("Please leave us a message!"))));
	}

	@WithAnonymousUser
	@Test
	public void testEditWithAnonymousUserRedirectsToLogin() throws Exception {
		mvc.perform(post("/guestbook/edit").param("messageId", "1").param("editedNote", "the note")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
	}

	@WithMockUser(roles = { "USER" })
	@Test
	public void testEditWithUserRedirectsToLogin() throws Exception {
		mvc.perform(post("/guestbook/edit").param("messageId", "1").param("editedNote", "the note")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?unauthorized"));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	public void testEditWithAdminRedirectsToGuestbook() throws Exception {
		mvc.perform(post("/guestbook/edit").param("messageId", "1").param("editedNote", "the note")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/guestbook"));
	}

	@WithAnonymousUser
	@Test
	public void testApproveWithAnonymousUserRedirectsToLogin() throws Exception {
		mvc.perform(post("/guestbook/approve").param("messageId", "1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
	}

	@WithMockUser(roles = { "USER" })
	@Test
	public void testApproveWithUserRedirectsToLogin() throws Exception {
		mvc.perform(post("/guestbook/approve").param("messageId", "1").with(user("user"))).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?unauthorized"));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	public void testApproveWithAdminRedirectsToGuestbook() throws Exception {
		mvc.perform(post("/guestbook/approve").param("messageId", "1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/guestbook"));
	}

	@WithAnonymousUser
	@Test
	public void testDeleteMessageWithAnonymousUserRedirectsToLogin() throws Exception {
		mvc.perform(post("/guestbook/delete").param("messageId", "1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
	}

	@WithMockUser(roles = { "USER" })
	@Test
	public void testDeleteMessageWithUserRedirectsToLogin() throws Exception {
		mvc.perform(post("/guestbook/delete").param("messageId", "1").with(user("user"))).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?unauthorized"));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	public void testDeleteMessageWithAdminRedirectsToGuestbook() throws Exception {
		mvc.perform(post("/guestbook/delete").param("messageId", "1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/guestbook"));
	}

	@WithAnonymousUser
	@Test
	public void testAddWithAnonymousUserRedirectsToLogin() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
		mvc.perform(multipart("/guestbook/add").file("picture", file.getBytes()).param("note", "the note").param("messageType", "note")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
	}

	@WithMockUser(roles = { "USER" })
	@Test
	public void testAddNoteWithUserRedirectsToHome() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, "".getBytes());
		mvc.perform(multipart("/guestbook/add").file("picture", file.getBytes()).param("note", "the note").param("messageType", "note")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/guestbook"));
	}

	@WithMockUser(roles = { "USER" })
	@Test
	public void testAddPictureWithUserRedirectsToHome() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "filename.jpg", MediaType.TEXT_PLAIN_VALUE, "data".getBytes());
		mvc.perform(multipart("/guestbook/add").file("picture", file.getBytes()).param("note", "the note").param("messageType", "picture")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/guestbook"));
	}

	@WithAnonymousUser
	@Test
	public void testUserRegistration() throws Exception {
		mvc.perform(post("/register").param("firstName", "firstName").param("lastName", "lastName").param("email", "email").param("password", "password").param("confirmedPassword", "password")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));
	}

}
