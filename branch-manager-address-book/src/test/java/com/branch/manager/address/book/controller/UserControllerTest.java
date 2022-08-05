package com.branch.manager.address.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.branch.manager.address.book.exception.UserAlreadyExistsException;
import com.branch.manager.address.book.exception.UserNotFoundException;
import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Test Get_Request for 1 user")
	void testGetUser() throws Exception {
		User user = new User(1, "ABC", "test1@gmail.com");
		EntityModel<User> model = EntityModel.of(user);
		WebMvcLinkBuilder linkToUsers = linkTo(methodOn(UserService.class).getUsers());
		model.add(linkToUsers.withRel("all-users"));
		Mockito.when(userService.getUser(1)).thenReturn(model);
		mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
		verify(userService).getUser(1);
	}

	@Test
	@DisplayName("Test Get_Request for all users")
	void testGetAllUser() throws Exception {
		List<User> userList = new ArrayList<User>();
		userList.add(new User(1, "ABC", "test1@gmail.com"));
		userList.add(new User(2, "Jill", "test2@gmail.com"));
		userList.add(new User(3, "Jam", "test3@gmail.com"));

		Mockito.when(userService.getUsers()).thenReturn(userList);
		mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.is(3)));
		verify(userService).getUsers();
	}

	@Test
	@DisplayName("Test Delete_Request for a User")
	void testDeleteUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1))
				.andExpect(MockMvcResultMatchers.status().is(200));
		verify(userService).deleteUserById(1);
	}

	@Test
	@DisplayName("Test Create_Request for a User")
	void testCreateUser() throws Exception {
		User user = new User(4, "ABCDE", "test4@gmail.com");
		String requestBody = new ObjectMapper().valueToTree(user).toString();
		URI location = new URI("/users/4");
		Mockito.when(userService.createUser(any(User.class))).thenReturn(ResponseEntity.created(location).build());
		mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
				.content(requestBody).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(201)) // isCreated()
				.andExpect(MockMvcResultMatchers.header().string("Location", "/users/4"));
		verify(userService).createUser(any(User.class));
	}

	@Test
	@DisplayName("Test Update_Request for a User")
	void testUpdateUser() throws Exception {
		User user = new User(1, "ABCDEU", "test5@gmail.com");
		String requestBody = new ObjectMapper().valueToTree(user).toString();
		URI location = new URI("/users/1");
		Mockito.when(userService.updateUser(any(Integer.class), any(User.class)))
				.thenReturn(ResponseEntity.created(location).build());
		mockMvc.perform(MockMvcRequestBuilders.post("/users/1").contentType(MediaType.APPLICATION_JSON)
				.content(requestBody).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(201)) // isCreated()
				.andExpect(MockMvcResultMatchers.header().string("Location", "/users/1"));
		verify(userService).updateUser(any(Integer.class), any(User.class));
	}

	@Test
	@DisplayName("Test Get_Request for Non-Existing user")
	void testGetInvalidUser() throws Exception {
		Mockito.when(userService.getUser(1001)).thenThrow(UserNotFoundException.class);
		mockMvc.perform(MockMvcRequestBuilders.get("/users/1001")).andExpect(MockMvcResultMatchers.status().is(404)); // not_found
		verify(userService).getUser(1001);
	}

	@Test
	@DisplayName("Test Post_Request for existing user")
	void testCreateExistingUser() throws Exception {
		User user = new User(1, "ABC", "test1@gmail.com");
		String requestBody = new ObjectMapper().valueToTree(user).toString();
		Mockito.when(userService.createUser(any(User.class))).thenThrow(UserAlreadyExistsException.class);
		mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
				.content(requestBody).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(409)); // already_exists
		verify(userService).createUser(any(User.class));
	}

}
