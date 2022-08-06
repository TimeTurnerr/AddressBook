package com.branch.manager.address.book.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.branch.manager.address.book.exception.ContactAlreadyExistsException;
import com.branch.manager.address.book.exception.ContactNotFoundException;
import com.branch.manager.address.book.exception.UserNotFoundException;
import com.branch.manager.address.book.model.AddressBook;
import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.repository.AddressBookRepository;
import com.branch.manager.address.book.repository.UserRepository;

@Service
public class AddressBookService {

	@Autowired
	private AddressBookRepository addressBookRepository;

	@Autowired
	private UserRepository userRepository;

	public List<AddressBook> getAllContacts() {
		return addressBookRepository.findAll();
	}

	public List<AddressBook> getContacts(int userId) {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		return user.get().getUserContacts();
	}

	public AddressBook getContact(int userId, int contactsId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		Optional<AddressBook> contactsOptional = addressBookRepository.findById(contactsId);
		if (!contactsOptional.isPresent()) {
			throw new ContactNotFoundException("Contact not found");
		}
		User user = userOptional.get();
		if (!user.getUserContacts().contains(contactsOptional.get())) {
			throw new ContactNotFoundException("Contact not found for given user");
		} else {
			return contactsOptional.get();
		}
	}

	public ResponseEntity<Object> createContact(int userId, AddressBook newContact) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		User user = userOptional.get();
		if (contactExists(userId, newContact)) {
			throw new ContactAlreadyExistsException("Contact Already Exists");
		}
		newContact.setUser(user);
		addressBookRepository.save(newContact);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newContact.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	public ResponseEntity<Object> updateContact(int userId, int contactsId, AddressBook newContact) {
		AddressBook oldContact = getContact(userId, contactsId);
		oldContact.setPhoneNumber(newContact.getPhoneNumber());
		oldContact.setName(newContact.getName());
		addressBookRepository.save(oldContact);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newContact.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	public void deleteContact(int userId, int contactsId) {
		addressBookRepository.deleteById(contactsId);
	}

	public boolean contactExists(int userId, AddressBook contact) {
		for (AddressBook c : getContacts(userId)) {
			if (c.getName().equals(contact.getName()) && c.getPhoneNumber().equals(contact.getPhoneNumber())) {
				return true;
			}
		}
		return false;
	}

}
