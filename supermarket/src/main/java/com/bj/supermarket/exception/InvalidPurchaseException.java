package com.bj.supermarket.exception;

public class InvalidPurchaseException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidPurchaseException(String errorMessage) {
		
		super("Sorry, product " + errorMessage + " is not available. Scan available products ");
		
	}
	
	
}
