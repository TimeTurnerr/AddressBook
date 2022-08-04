package com.branch.manager.address.book.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.branch.manager.address.book.exception.UserNotFoundException;
import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/users/{id}")
	public EntityModel<User> getUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException();
		}
		EntityModel<User> model = EntityModel.of(user.get());
		WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).getUsers());
		model.add(linkToUsers.withRel("all-users"));
		return model;

	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}
}
