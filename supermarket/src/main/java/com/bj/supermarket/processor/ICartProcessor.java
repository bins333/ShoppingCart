package com.bj.supermarket.processor;

import com.bj.supermarket.exception.InvalidPurchaseException;

public interface ICartProcessor {
	
	/**
	 * Override to implement product scan logic
	 * @param aItemName
	 * @return
	 * @throws InvalidPurchaseException
	 */
	boolean scanProduct(String aItemName) throws InvalidPurchaseException;
	
	

}
