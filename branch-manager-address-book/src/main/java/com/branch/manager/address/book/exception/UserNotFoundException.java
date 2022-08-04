package com.branch.manager.address.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private String errorMessage;

	public UserNotFoundException() {
	}

	public UserNotFoundException(String error) {
		super(error);
		this.errorMessage = error;
	}

}
