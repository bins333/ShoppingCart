package com.bj.supermarket.processor.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import com.bj.supermarket.common.AppConstants;
import com.bj.supermarket.exception.InvalidProductDetailsException;
import com.bj.supermarket.model.OfferProduct;
import com.bj.supermarket.model.Product;
import com.bj.supermarket.processor.IPricingProcessor;

/**
 * Class to process pricing rules and building product objects.
 * 
 * @author bins
 *
 */

public class PricingProcessorImpl implements IPricingProcessor {

	final static Logger logger = Logger.getLogger(PricingProcessorImpl.class);
	// Keep as volatile in case of double lock.
	private static IPricingProcessor myPricingProcessor = null;
	
	private static Map<String, Product> productDetails = new HashMap<>();
	
	private PricingProcessorImpl() {

	}

	/**
	 * Get singleton object
	 * 
	 * @return
	 */
	public static IPricingProcessor getInstance() {
		if (myPricingProcessor == null) {
			synchronized (PricingProcessorImpl.class) {
				// Can have a double lock over here.
				myPricingProcessor = new PricingProcessorImpl();
			}
		}
		return myPricingProcessor;
	}

	/**
	 * This method will read input rule string and populate the product and offer
	 * objects if the input is valid.
	 */
	@Override
	public boolean processPricingRule(String aPriceRule) throws InvalidProductDetailsException {

		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "processPricingRule [" + aPriceRule + "]");
		boolean isRuleValid = false;
		boolean isOfferRuleValid = false;
		if (aPriceRule != null && !aPriceRule.isEmpty()) {
			// Split for | character
			String[] myInputStringArray = aPriceRule.trim().toUpperCase()
					.split(AppConstants.REGX_RULE_HIPHEN_DELIMITER);
			try {
				isRuleValid = validateProductRule(myInputStringArray);
				if (isRuleValid) {					
					// create product object since validation is success.
					Product myProduct = new Product();
					String myProductId = myInputStringArray[0].trim();
					myProduct.setProductId(myProductId);
					myProduct.setProductPrice(NumberUtils.toDouble(myInputStringArray[1]));					
					isOfferRuleValid = validateOfferRule(myInputStringArray);
					if (isOfferRuleValid) {
						OfferProduct myOfferProduct = new OfferProduct();
						String[] myOfferArray = myInputStringArray[2].trim().split(AppConstants.REGX_RULE_FOR_STRING_PARSE_DELIMITER);
						myOfferProduct.setMinimumOfferUnits(NumberUtils.toInt(myOfferArray[0].trim()));
						myOfferProduct.setOfferPrice(NumberUtils.toDouble(myOfferArray[1].trim()));
						myProduct.setOfferProduct(myOfferProduct);
					}
					getProductDetails().put(myProductId, myProduct);
					
				} else {
					// something wrong with input
					throw new InvalidProductDetailsException(aPriceRule);
				}
			} catch (InvalidProductDetailsException aException) {				
				// Not re-throwing here because we want to continue the loop to accept user input
				logger.error(AppConstants.PRODUCT_RULE_EXCEPTION + aException.getMessage(), aException);
				
			}
		}
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "processPricingRule [" + aPriceRule + "]");
		return isRuleValid;
	}

	/**
	 * Validate input string is valid
	 * @param aInputStringArray
	 * @return
	 */
	private boolean validateProductRule(String[] aInputStringArray) {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "validateProductRule");
		boolean isValid = false;
		// check only if array has either 2 or 3 entry
		if (aInputStringArray.length > 1 && aInputStringArray.length < 4
				&& NumberUtils.isParsable(aInputStringArray[1].trim())) {
			// for offer string it will have at least 3 object
			if (aInputStringArray.length > 2) {
				// when find third object, check the syntax for offer object
				if (validateOfferRule(aInputStringArray)) {
					isValid = true;
				} else {
					// set false since something wrong with offer object
					isValid = false;
				}
			} else {
				// set two object when string has valid price and id.
				isValid = true;
			}
		}
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "validateProductRule");
		return isValid;
	}

	/**
	 * Validate offer string is valid
	 * @param aInputStringArray
	 * @return
	 */
	private boolean validateOfferRule(String[] aInputStringArray) {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "validateOfferRule");
		boolean isValid = false;
		// valid offer string has size of 3
		if (aInputStringArray.length == 3) {
			// get 3rd object and find values be removing for string.
			String[] myOfferArray = aInputStringArray[2].trim()
					.split(AppConstants.REGX_RULE_FOR_STRING_PARSE_DELIMITER);
			if (myOfferArray.length == 2 && NumberUtils.isParsable(myOfferArray[0].trim())
					&& NumberUtils.isParsable(myOfferArray[1].trim())) {
				isValid = true;
			}
		}
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "validateOfferRule");
		return isValid;
	}

	/**
	 * map which holds product details
	 */
	@Override
	public Map<String, Product> getProductDetails() {

		return productDetails;
	}


	
}
