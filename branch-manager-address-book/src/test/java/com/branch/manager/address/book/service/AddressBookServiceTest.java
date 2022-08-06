package com.branch.manager.address.book.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.branch.manager.address.book.model.AddressBook;
import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.repository.AddressBookRepository;
import com.branch.manager.address.book.repository.UserRepository;

@WebMvcTest(controllers = UserService.class)
class AddressBookServiceTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AddressBookRepository addressBookRepository;

	@MockBean
	private AddressBookService addressBookService;

	@Test
	@DisplayName("Test findAll for all contacts")
	void testGetAllContacts() throws Exception {
		List<AddressBook> contactList = new ArrayList<>();
		contactList.add(new AddressBook(1, "Name1", "1234567891", new User(1, "ABC", "test1@gmail.com")));
		contactList.add(new AddressBook(2, "Name2", "1234567892", new User(1, "ABC", "test1@gmail.com")));
		contactList.add(new AddressBook(3, "Name3", "1234567893", new User(2, "Jill", "test2@gmail.com")));
		contactList.add(new AddressBook(4, "Name4", "1234567894", new User(3, "Jam", "test3@gmail.com")));
		Mockito.when(addressBookRepository.findAll()).thenReturn(contactList);
		List<AddressBook> contacts = addressBookRepository.findAll();
		Assertions.assertEquals(4, contacts.size(), "findAll should return 4 contacts");
	}

	@Test
	@DisplayName("Test get all contacts for user")
	void testGetAllUserContacts() throws Exception {
		List<AddressBook> contactList = new ArrayList<>();
		contactList.add(new AddressBook(1, "Name1", "1234567891", new User(1, "ABC", "test1@gmail.com")));
		contactList.add(new AddressBook(2, "Name2", "1234567892", new User(1, "ABC", "test1@gmail.com")));
		Mockito.when(addressBookService.getContacts(1)).thenReturn(contactList);
		Assertions.assertEquals(2, addressBookService.getContacts(1).size(), "should return 2");
	}

	@Test
	@DisplayName("Test findById for 1 contact")
	void testGetOneContact() throws Exception {
		Mockito.when(addressBookRepository.findById(1)).thenReturn(
				Optional.of(new AddressBook(1, "Name1", "1234567891", new User(1, "ABC", "test1@gmail.com"))));
		Optional<AddressBook> contact = addressBookRepository.findById(1);
		Assertions.assertEquals(1, contact.get().getId(), "findById should return id 1");
	}

	@Test
	@DisplayName("Test deleteById for 1 contact")
	void testDeleteOneContact() throws Exception {
		addressBookRepository.deleteById(1);
		verify(addressBookRepository, times(1)).deleteById(1);
	}

	@Test
	@DisplayName("Test create contact")
	void testCreateContact() throws Exception {
		AddressBook contact = new AddressBook(5, "Name5", "1234567895", new User(1, "ABC", "test1@gmail.com"));
		Mockito.when(addressBookRepository.findById(5)).thenReturn(Optional.of(contact));
		addressBookRepository.save(contact);
		Assertions.assertEquals(5, addressBookRepository.findById(5).get().getId(), "findById should return id 5");
		verify(addressBookRepository, times(1)).save(contact);
	}

	@Test
	@DisplayName("Test update contact")
	void testUpdateUser() throws Exception {
		AddressBook contact = new AddressBook(1, "Name11", "12345678911", new User(1, "ABC", "test1@gmail.com"));
		Mockito.when(addressBookRepository.findById(1)).thenReturn(Optional.of(contact));
		addressBookRepository.save(contact);
		Assertions.assertEquals("Name11", addressBookRepository.findById(1).get().getName(),
				"findById should return name Name11");
		verify(addressBookRepository, times(1)).save(contact);
	}

	@Test
	@DisplayName("Test findById for non existing contact")
	void testGetNonExistsContact() throws Exception {
		Mockito.when(addressBookRepository.findById(10)).thenReturn(null);
		Optional<AddressBook> contact = addressBookRepository.findById(10);
		Assertions.assertEquals(null, contact, "findById should return null");
		verify(addressBookRepository).findById(10);
	}
}
