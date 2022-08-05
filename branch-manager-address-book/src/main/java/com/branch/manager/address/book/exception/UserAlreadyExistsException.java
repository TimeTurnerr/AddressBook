package com.branch.manager.address.book.exception;

public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException(String error) {
		super(error);
	}

}
