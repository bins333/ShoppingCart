package com.bj.supermarket.exception;

import static com.bj.supermarket.common.AppConstants.PRODUCT_RULE_FAILURE_MESSAGE;

public class InvalidProductDetailsException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public InvalidProductDetailsException(String errorMessage) {
		
		super(PRODUCT_RULE_FAILURE_MESSAGE+errorMessage);
	}

}
