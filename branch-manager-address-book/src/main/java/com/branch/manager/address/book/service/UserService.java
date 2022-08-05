package com.branch.manager.address.book.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.branch.manager.address.book.exception.UserAlreadyExistsException;
import com.branch.manager.address.book.exception.UserNotFoundException;
import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> getUsers() {
		return userRepository.findAll();
	}

	public EntityModel<User> getUser(int id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("User Not Found");
		}
		EntityModel<User> model = EntityModel.of(user.get());
		WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).getUsers());
		model.add(linkToUsers.withRel("all-users"));
		return model;

	}

	public void deleteUser(int id) {
		userRepository.deleteById(id);
	}

	public ResponseEntity<Object> createUser(User user) {
		if (userExists(user)) {
			throw new UserAlreadyExistsException("User Already Exists");
		}
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	public ResponseEntity<Object> updateUser(int id, User newUser) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("User Not Found");
		}
		User oldUser = user.get();
		oldUser.setEmail(newUser.getEmail());
		oldUser.setName(newUser.getName());
		User savedUser = userRepository.save(oldUser);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	public void deleteUserById(int id) {
		userRepository.deleteById(id);
	}

	public boolean userExists(User user) {
		for (User us : getUsers()) {
			if (us.getEmail().equals(user.getEmail())) {
				return true;
			}
		}
		return false;
	}

}
