package com.bj.supermarket.processor;

import java.util.Map;

import com.bj.supermarket.exception.InvalidProductDetailsException;
import com.bj.supermarket.model.OfferProduct;
import com.bj.supermarket.model.Product;

public interface IPricingProcessor {

	/**
	 * Override to implement pricing rule
	 * @param aPriceRule
	 * @return
	 * @throws InvalidProductDetailsException
	 */
	boolean processPricingRule(String aPriceRule) throws InvalidProductDetailsException;

	/**
	 * Map to hold product details
	 * @return
	 */
	Map<String, Product> getProductDetails();

	
	/**
	 * Ma to hold offer details
	 * @return
	 */
	Map<String, OfferProduct> getOfferProductDetails();

}
