package com.branch.manager.address.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.branch.manager.address.book.exception.ContactAlreadyExistsException;
import com.branch.manager.address.book.exception.ContactNotFoundException;
import com.branch.manager.address.book.model.AddressBook;
import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.service.AddressBookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AddressBookController.class)
class AddressBookControllerTest {

	@MockBean
	private AddressBookService addressBookService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Test Get_Request for 1 contact")
	void testGetUser() throws Exception {
		User user = new User(1, "ABC", "test1@gmail.com");
		AddressBook contact = new AddressBook(1, "Name1", "1234567891", user);
		Mockito.when(addressBookService.getContact(1, 1)).thenReturn(contact);
		mockMvc.perform(MockMvcRequestBuilders.get("/users/1/contacts/1"))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
		verify(addressBookService).getContact(1, 1);
	}

	@Test
	@DisplayName("Test Get_Request for all contacts")
	void testGetAllUser() throws Exception {
		List<AddressBook> contactList = new ArrayList<>();
		contactList.add(new AddressBook(1, "Name1", "1234567891", new User(1, "ABC", "test1@gmail.com")));
		contactList.add(new AddressBook(2, "Name2", "1234567892", new User(1, "ABC", "test1@gmail.com")));
		Mockito.when(addressBookService.getContacts(1)).thenReturn(contactList);
		mockMvc.perform(MockMvcRequestBuilders.get("/users/1/contacts"))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
		verify(addressBookService).getContacts(1);
	}

	@Test
	@DisplayName("Test Delete_Request for a contact")
	void testDeleteContact() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/contacts/{id}", 1))
				.andExpect(MockMvcResultMatchers.status().is(200));
		verify(addressBookService).deleteContact(1, 1);
	}

	@Test
	@DisplayName("Test Create_Request for a Contact")
	void testCreateContact() throws Exception {
		AddressBook contact = new AddressBook(5, "Name5", "1234567891", new User(1, "ABC", "test1@gmail.com"));
		String requestBody = new ObjectMapper().valueToTree(contact).toString();
		URI location = new URI("/users/1/contacts/5");
		Mockito.when(addressBookService.createContact(any(Integer.class), any(AddressBook.class)))
				.thenReturn(ResponseEntity.created(location).build());
		mockMvc.perform(MockMvcRequestBuilders.post("/users/1/contacts").contentType(MediaType.APPLICATION_JSON)
				.content(requestBody).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(201)) // isCreated()
				.andExpect(MockMvcResultMatchers.header().string("Location", "/users/1/contacts/5"));
		verify(addressBookService).createContact(any(Integer.class), any(AddressBook.class));
	}

	@Test
	@DisplayName("Test Update_Request for a Contact")
	void testUpdateContact() throws Exception {

		AddressBook contact = new AddressBook(1, "Name1", "1234567891", new User(1, "ABC", "test1@gmail.com"));
		String requestBody = new ObjectMapper().valueToTree(contact).toString();
		URI location = new URI("/users/1/contacts/1");
		Mockito.when(addressBookService.updateContact(any(Integer.class), any(Integer.class), any(AddressBook.class)))
				.thenReturn(ResponseEntity.created(location).build());
		mockMvc.perform(MockMvcRequestBuilders.post("/users/1/contacts/1").contentType(MediaType.APPLICATION_JSON)
				.content(requestBody).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(201)) // isCreated()
				.andExpect(MockMvcResultMatchers.header().string("Location", "/users/1/contacts/1"));
		verify(addressBookService).updateContact(any(Integer.class), any(Integer.class), any(AddressBook.class));
	}

	@Test
	@DisplayName("Test Get_Request for Non-Existing Contact")
	void testGetInvalidUser() throws Exception {
		Mockito.when(addressBookService.getContact(1, 1001)).thenThrow(ContactNotFoundException.class);
		mockMvc.perform(MockMvcRequestBuilders.get("/users/1/contacts/1001"))
				.andExpect(MockMvcResultMatchers.status().is(404)); // not_found
		verify(addressBookService).getContact(1, 1001);
	}

	@Test
	@DisplayName("Test Post_Request for existing contact")
	void testCreateExistingContact() throws Exception {
		AddressBook contact = new AddressBook(1, "Name1", "1234567891", new User(1, "ABC", "test1@gmail.com"));
		String requestBody = new ObjectMapper().valueToTree(contact).toString();
		Mockito.when(addressBookService.createContact(any(Integer.class), any(AddressBook.class)))
				.thenThrow(ContactAlreadyExistsException.class);
		mockMvc.perform(MockMvcRequestBuilders.post("/users/1/contacts/").contentType(MediaType.APPLICATION_JSON)
				.content(requestBody).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(409)); // already_exists
		verify(addressBookService).createContact(any(Integer.class), any(AddressBook.class));
	}

}
