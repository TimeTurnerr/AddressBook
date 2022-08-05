package com.branch.manager.address.book.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userService.getUsers();
	}

	@GetMapping("/users/{id}")
	public EntityModel<User> getOneUser(@PathVariable int id) {
		return userService.getUser(id);

	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		return userService.createUser(user);
	}

	@PostMapping("/users/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable int id, @Valid @RequestBody User user) {
		return userService.updateUser(id, user);
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userService.deleteUserById(id);
	}
}
