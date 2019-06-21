package com.bj.supermarket.processor.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bj.supermarket.common.AppConstants;
import com.bj.supermarket.exception.InvalidPurchaseException;
import com.bj.supermarket.model.OfferProduct;
import com.bj.supermarket.model.Product;
import com.bj.supermarket.processor.ICartProcessor;
import com.bj.supermarket.util.AppUtil;

/**
 * This class process product scan
 * @author bins
 *
 */
public class CartProcessorImpl implements ICartProcessor {

	final static Logger logger = Logger.getLogger(CartProcessorImpl.class);
	
	private double checkOutAmount = 0.0;
	private static CartProcessorImpl myCartProcessor = null;
	private static Map<String, Integer> cartDetails = new HashMap<>();
	

	private CartProcessorImpl() {
		// TODO Auto-generated constructor stub
	}

	public static CartProcessorImpl getInstance() {
		if (myCartProcessor == null) {
			synchronized (CartProcessorImpl.class) {
				// Double check?
				myCartProcessor = new CartProcessorImpl();
			}
		}

		return myCartProcessor;
	}

	@Override
	public boolean scanProduct(String aItemName) throws InvalidPurchaseException {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "scanProduct [" + aItemName + "]");
		boolean productScanned = false;		
		try {
			if (null != aItemName) {
				String myItemId = aItemName.trim().toUpperCase();
				// Check product is available in store
				if (PricingProcessorImpl.getInstance().getProductDetails().containsKey(myItemId)) {
					// populate Map with item counts.
					if (cartDetails.containsKey(myItemId)) {
						cartDetails.put(myItemId, cartDetails.get(myItemId) + 1);
					} else {
						cartDetails.put(myItemId, 1);
					}						
					productScanned = true;
				} else {
					throw new InvalidPurchaseException(myItemId);
				}
			}
			
			
		} catch (InvalidPurchaseException aException) {
			// Not throwing here because we want to continue the loop to accept the correct
			// input
			System.out.println(aException.getMessage());
		}
		calculatePaymentAmount();
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "scanProduct [" + aItemName+ "]");
		return productScanned;
	}
	
	
	/**
	 * Method to recalculate the amount based on all products
	 * @return
	 */
	private double calculatePaymentAmount() {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "calculatePaymentAmount");
		checkOutAmount = 0.0;
		Set<String> myCartSet = cartDetails.keySet();
		for (String myPoductID : myCartSet) {			
			Integer myProductCounts = cartDetails.get(myPoductID);
			OfferProduct myOfferProduct = PricingProcessorImpl.getInstance().getOfferProductDetails().get(myPoductID);
			Product myProduct = PricingProcessorImpl.getInstance().getProductDetails().get(myPoductID);
			int myMinimumOfferCount = 0;
			if (null != myOfferProduct) {
				myMinimumOfferCount = myOfferProduct.getMinimumOfferUnits();
				if (myProductCounts >= myMinimumOfferCount) {
					checkOutAmount += (myProductCounts / myMinimumOfferCount) * myOfferProduct.getProductPrice()
							+ (myProductCounts % myMinimumOfferCount) * myProduct.getProductPrice();
				} else {
					checkOutAmount += myProductCounts * myProduct.getProductPrice();
				}

			} else {
				checkOutAmount += myProductCounts * myProduct.getProductPrice();
			}
			logger.debug("Checkout Amount " + checkOutAmount);
		}
		
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "calculatePaymentAmount");
		return checkOutAmount;
	}


	public Map<String, Integer> getCartDetails() {

		return cartDetails;
	}

	/**
	 * @return the checkOutAmount
	 */

	public String getCheckOutDisplayAmount() {

		return AppUtil.getCurrencySymbol() + (checkOutAmount / 100);
	}

	

}
