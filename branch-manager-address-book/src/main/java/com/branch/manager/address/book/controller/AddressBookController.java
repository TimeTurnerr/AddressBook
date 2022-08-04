package com.branch.manager.address.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.branch.manager.address.book.model.AddressBook;
import com.branch.manager.address.book.service.AddressBookService;

@RestController
public class AddressBookController {

	@Autowired
	private AddressBookService addressBookService;

	@GetMapping("/users/{userId}/contacts")
	public List<AddressBook> getAllContacts(@PathVariable int userId) {
		return addressBookService.getContacts(userId);
	}

	@GetMapping("/users/{userId}/contacts/{contactsId}")
	public AddressBook getOneContact(@PathVariable int userId, @PathVariable int contactsId) {
		return addressBookService.getContact(userId, contactsId);
	}

	@PostMapping("/users/{userId}/contacts")
	public ResponseEntity<Object> createContactForUser(@PathVariable int userId, @RequestBody AddressBook newContact) {
		return addressBookService.createContact(userId, newContact);
	}

	@PostMapping("/users/{userId}/contacts/{contactsId}")
	public ResponseEntity<Object> updateContactForUser(@PathVariable int userId, @PathVariable int contactsId,
			@RequestBody AddressBook newContact) {
		return addressBookService.updateContact(userId, contactsId, newContact);
	}

	@DeleteMapping("/users/{userId}/contacts/{contactsId}")
	public void deleteContact(@PathVariable int userId, @PathVariable int contactsId) {
		addressBookService.deleteContact(userId, contactsId);
	}

//	@PostMapping("/users/{userId}/contacts")
//	public void createContactsForUser(@PathVariable int userId, @RequestBody List<AddressBook> newContacts) {
//		addressBookService.createContacts(userId, newContacts);
//	}

}