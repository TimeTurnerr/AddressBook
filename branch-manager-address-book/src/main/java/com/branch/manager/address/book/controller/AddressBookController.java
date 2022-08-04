package com.branch.manager.address.book.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.branch.manager.address.book.exception.ContactNotFoundException;
import com.branch.manager.address.book.exception.UserNotFoundException;
import com.branch.manager.address.book.model.AddressBook;
import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.repository.AddressBookRepository;
import com.branch.manager.address.book.repository.UserRepository;

@RestController
public class AddressBookController {

	@Autowired
	private AddressBookRepository addressBookRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users/{userId}/contacts")
	public List<AddressBook> getContacts(@PathVariable int userId) {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserNotFoundException();
		}
		return user.get().getUserContacts();
	}

	@GetMapping("/users/{userId}/contacts/{contactsId}")
	public AddressBook getContact(@PathVariable int userId, @PathVariable int contactsId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent()) {
			throw new UserNotFoundException();
		}
		Optional<AddressBook> contactsOptional = addressBookRepository.findById(contactsId);
		if (!contactsOptional.isPresent()) {
			throw new ContactNotFoundException("id - " + contactsId);
		}
		User user = userOptional.get();
		if (!user.getUserContacts().contains(contactsOptional.get())) {
			throw new ContactNotFoundException("id - " + contactsId);
		} else {
			return contactsOptional.get();
		}
	}

	@PostMapping("/users/{userId}/contacts")
	public ResponseEntity<Object> createContacts(@PathVariable int userId, @RequestBody AddressBook newContacts) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent()) {
			throw new UserNotFoundException();
		}
		User user = userOptional.get();
		newContacts.setUser(user);
		addressBookRepository.save(newContacts);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newContacts.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{userId}/contacts/{contactsId}")
	public void deleteContact(@PathVariable int userId, @PathVariable int contactsId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent()) {
			throw new UserNotFoundException();
		}
		Optional<AddressBook> contactsOptional = addressBookRepository.findById(contactsId);
		if (!contactsOptional.isPresent()) {
			throw new ContactNotFoundException("id - " + contactsId);
		}
		User user = userOptional.get();
		if (!user.getUserContacts().contains(contactsOptional.get())) {
			throw new ContactNotFoundException("id - " + contactsId);
		} else {
			addressBookRepository.deleteById(contactsId);
		}

	}

}