package com.branch.manager.address.book.exception;

public class UserAlreadyExistsException extends RuntimeException {

	private String errorMessage;

	public UserAlreadyExistsException() {
	}

	public UserAlreadyExistsException(String error) {
		super(error);
		this.errorMessage = error;
	}

}
