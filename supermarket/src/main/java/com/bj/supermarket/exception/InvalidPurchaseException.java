package com.bj.supermarket.exception;

import com.bj.supermarket.common.AppConstants;

public class InvalidPurchaseException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidPurchaseException(String errorMessage) {
		
		super(AppConstants.PRODUCT_NOT_AVAILABLE_START_MESSAGE + errorMessage + AppConstants.PRODUCT_NOT_AVAILABLE_END_MESSAGE);
		
	}
	
	
}
