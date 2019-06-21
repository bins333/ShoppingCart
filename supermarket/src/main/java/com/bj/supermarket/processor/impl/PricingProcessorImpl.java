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
 * @author bins
 *
 */

public class PricingProcessorImpl implements IPricingProcessor {

	final static Logger logger = Logger.getLogger(PricingProcessorImpl.class);
	
	private static Map<String, Product> productDetails = new HashMap<>();
	private static Map<String, OfferProduct> offerProductDetails = new HashMap<>();

	// Keep as volatile in case of double lock.
	private static PricingProcessorImpl myPricingProcessor = null;

	private PricingProcessorImpl() {

	}

	/**
	 * Get singleton object
	 * @return
	 */
	public static PricingProcessorImpl getInstance() {
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
		
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "processPricingRule [" + aPriceRule+ "]");
		boolean isValid = false;
		if (aPriceRule != null && !aPriceRule.isEmpty()) {
			// Split for | character
			String[] myInputStringArray = aPriceRule.trim().toUpperCase()
					.split(AppConstants.REGX_RULE_HIPHEN_DELIMITER);
			try {
				isValid = validatePricingRule(myInputStringArray);
				if (isValid) {
					// create product object since validation is success.
					Product myProduct = new Product();
					String myProductId = myInputStringArray[0].trim();
					myProduct.setProductId(myProductId);
					myProduct.setProductPrice(NumberUtils.toDouble(myInputStringArray[1]));
					// populate product map
					getProductDetails().put(myProductId, myProduct);
					
				} else {
					// something wrong with input
					throw new InvalidProductDetailsException(aPriceRule);
				}
			} catch (InvalidProductDetailsException aException) {
				// Not re-throwing here because we want to continue the loop to accept the correct
				// input
				logger.error("Exception "+ aException);
				System.out.println(aException.getMessage());
			}
		}
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "processPricingRule [" + aPriceRule+ "]");
		return isValid;
	}

	/**
	 * Validate input string is valid to map against product object. 
	 * @param aInputStringArray
	 * @return
	 */
	private boolean validatePricingRule(String[] aInputStringArray) {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "validatePricingRule");
		boolean isValid = false;
		// Check first and second entry are numbers with at least two objects in array
		if (aInputStringArray.length > 1 && NumberUtils.isParsable(aInputStringArray[1].trim())) {
			// for offer string it will have at least 3 object
			if (aInputStringArray.length >= 3) {
				// when find third object, check the syntax for offer object
				if (isOfferSyntaxValid(aInputStringArray)) {

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
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "validatePricingRule");
		return isValid;

	}

	/**
	 * Validate offer input string is valid to create {@link OfferProduct}.If valid
	 * offer object will be created and added to offer map
	 * 
	 * @param aOfferArray
	 * @return
	 */
	private boolean isOfferSyntaxValid(String[] aInputStringArray) {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "isOfferSyntaxValid");
		boolean isValid = false;
		// valid offer string has size of 3
		if (aInputStringArray.length == 3) {
			// get 3rd object and find values be removing for string.
			String[] myOfferArray = aInputStringArray[2].trim()
					.split(AppConstants.REGX_RULE_FOR_STRING_PARSE_DELIMITER);
			if (myOfferArray.length == 2 && NumberUtils.isParsable(myOfferArray[0].trim())
					&& NumberUtils.isParsable(myOfferArray[1].trim())) {
				// set true only when it can be converted to a string
				OfferProduct myOfferProduct = new OfferProduct();
				String myProductId = aInputStringArray[0].trim();
				myOfferProduct.setProductId(myProductId);
				myOfferProduct.setMinimumOfferUnits(NumberUtils.toInt(myOfferArray[0].trim()));
				myOfferProduct.setProductPrice(NumberUtils.toDouble(myOfferArray[1].trim()));
				getOfferProductDetails().put(myProductId, myOfferProduct);
				isValid = true;
			}
		}
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "isOfferSyntaxValid" );
		return isValid;
	}

	/**
	 * map which holds product details
	 */
	@Override
	public Map<String, Product> getProductDetails() {

		return productDetails;
	}

	
	/**
	 * Map which holds offer details
	 * @return
	 */
	@Override
	public Map<String, OfferProduct> getOfferProductDetails() {

		return offerProductDetails;
	}

}
