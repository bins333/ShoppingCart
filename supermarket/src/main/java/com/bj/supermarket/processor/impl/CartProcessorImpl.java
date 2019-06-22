package com.bj.supermarket.processor.impl;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bj.supermarket.common.AppConstants;
import com.bj.supermarket.exception.InvalidPurchaseException;
import com.bj.supermarket.manager.CartModelManager;
import com.bj.supermarket.model.Cart;
import com.bj.supermarket.model.OfferProduct;
import com.bj.supermarket.model.Product;
import com.bj.supermarket.processor.ICartProcessor;

/**
 * This class process product scan
 * @author bins
 *
 */
public class CartProcessorImpl implements ICartProcessor {

	final static Logger logger = Logger.getLogger(CartProcessorImpl.class);
	
	private static ICartProcessor myCartProcessor = null;

	private CartProcessorImpl() {
	}

	public static ICartProcessor getInstance() {
		if (myCartProcessor == null) {
			synchronized (CartProcessorImpl.class) {
				// Double check?
				myCartProcessor = new CartProcessorImpl();
			}
		}

		return myCartProcessor;
	}

	
	/**
	 * Method to scan new product
	 */
	@Override
	public boolean scanProduct(CartModelManager aManager,String aItemName) throws InvalidPurchaseException {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "scanProduct [" + aItemName + "]");
		boolean productScanned = false;		
		try {
			if (null != aItemName && null != aManager ) {
				if(aManager.getCart() == null) {
					aManager.setCart(new Cart());
				}
				String myItemId = aItemName.trim().toUpperCase();
				// Check product is available in store
				if (PricingProcessorImpl.getInstance().getProductDetails().containsKey(myItemId)) {
					Map<String, Integer> myProductPurchaseMap =aManager.getCart().getCheckoutProducts();
					// populate Map with item counts.
					if (myProductPurchaseMap.containsKey(myItemId)) {
						myProductPurchaseMap.put(myItemId, myProductPurchaseMap.get(myItemId) + 1);
					} else {
						myProductPurchaseMap.put(myItemId, 1);
					}						
					productScanned = true;
				} else {
					throw new InvalidPurchaseException(myItemId);
				}
			}		
			
		} catch (InvalidPurchaseException aException) {
			// Not throwing here because we want to continue the loop to accept the correct
			// input
			logger.error(aException.getMessage());
		}
		calculatePaymentAmount(aManager);
		//String myPaymentAmount = ICartProcessor.getTotalPaymentAmount(aManager);
		//System.out.println("Bill Amount :" + myPaymentAmount + " [ enter DONE to complete scanning! ] ");
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "scanProduct [" + aItemName+ "]");
		return productScanned;
	}
	
	

	/**
	 * Method to recalculate the amount based on all products
	 * @return
	 */
	private double calculatePaymentAmount(CartModelManager aManager) {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "calculatePaymentAmount");
		double checkOutAmount = 0.0;
		if(null != aManager && null != aManager.getCart()) {		
			Map<String, Integer> myProductPurchaseMap =aManager.getCart().getCheckoutProducts();
			Set<String> myCartSet = myProductPurchaseMap.keySet();
			for (String myPoductID : myCartSet) {			
				Integer myProductCounts = myProductPurchaseMap.get(myPoductID);				
				Product myProduct = PricingProcessorImpl.getInstance().getProductDetails().get(myPoductID);
				OfferProduct myOfferProduct = myProduct.getOfferProduct();
				int myMinimumOfferCount = 0;
				if (null != myOfferProduct) {
					myMinimumOfferCount = myOfferProduct.getMinimumOfferUnits();
					if (myProductCounts >= myMinimumOfferCount) {
						checkOutAmount += (myProductCounts / myMinimumOfferCount) * myOfferProduct.getOfferPrice()
								+ (myProductCounts % myMinimumOfferCount) * myProduct.getProductPrice();
					} else {
						checkOutAmount += myProductCounts * myProduct.getProductPrice();
					}

				} else {
					checkOutAmount += myProductCounts * myProduct.getProductPrice();
				}	
				logger.debug("Checkout Amount after scanning product [" + myPoductID + "] is:"+checkOutAmount);
			}
			//set checkout Amount
			aManager.getCart().setPaymentAmount(checkOutAmount);
			logger.info("Checkout Amount " + checkOutAmount);
		}
		
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "calculatePaymentAmount");
		return checkOutAmount;
	}

}
