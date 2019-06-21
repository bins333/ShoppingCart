package com.bj.supermarket.exception;


public class InvalidProductDetailsException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public InvalidProductDetailsException(String errorMessage) {
		
		super("Invalid user input, follow input rules for input string "+errorMessage);
	}

}
