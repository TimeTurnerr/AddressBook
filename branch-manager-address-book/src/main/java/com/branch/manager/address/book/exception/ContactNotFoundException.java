package com.branch.manager.address.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContactNotFoundException extends RuntimeException {

	private String errorMessage;

	public ContactNotFoundException() {
	}

	public ContactNotFoundException(String error) {
		super(error);
		this.errorMessage = error;
	}

}
