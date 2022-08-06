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

import com.branch.manager.address.book.model.User;
import com.branch.manager.address.book.repository.UserRepository;

@WebMvcTest(controllers = UserService.class)
class UserServiceTest {

	@MockBean
	private UserRepository userRepository;

	@Test
	@DisplayName("Test findAll for all users")
	void testGetAllUser() throws Exception {
		List<User> userListExpected = new ArrayList<User>();
		userListExpected.add(new User(1, "ABC", "test1@gmail.com"));
		userListExpected.add(new User(2, "Jill", "test2@gmail.com"));
		userListExpected.add(new User(3, "Jam", "test3@gmail.com"));
		Mockito.when(userRepository.findAll()).thenReturn(userListExpected);
		List<User> userListActual = userRepository.findAll();
		Assertions.assertEquals(3, userListActual.size(), "findAll should return 3 Users");
	}

	@Test
	@DisplayName("Test findById for 1 users")
	void testGetOneUser() throws Exception {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(new User(1, "ABC", "test1@gmail.com")));
		Optional<User> user = userRepository.findById(1);
		Assertions.assertEquals(1, user.get().getId(), "findById should return id 1");
	}

	@Test
	@DisplayName("Test deleteById for 1 users")
	void testDeleteOneUser() throws Exception {
		userRepository.deleteById(1);
		verify(userRepository, times(1)).deleteById(1);
	}

	@Test
	@DisplayName("Test create user")
	void testCreateUser() throws Exception {
		User user = new User(4, "ABCDE", "test4@gmail.com");
		Mockito.when(userRepository.findById(4)).thenReturn(Optional.of(user));
		userRepository.save(user);
		Assertions.assertEquals(4, userRepository.findById(4).get().getId(), "findById should return id 4");
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("Test update user")
	void testUpdateUser() throws Exception {
		User user = new User(1, "ABCDE", "test4@gmail.com");
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		userRepository.save(user);
		Assertions.assertEquals("test4@gmail.com", userRepository.findById(1).get().getEmail(),
				"findById should return email test4@gmail.com");
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("Test findById for non existing users")
	void testGetNonExistsUser() throws Exception {
		Mockito.when(userRepository.findById(10)).thenReturn(null);
		Optional<User> user = userRepository.findById(10);
		Assertions.assertEquals(null, user, "findById should return null");
		verify(userRepository).findById(10);
	}
}
