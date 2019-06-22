package com.bj.supermarket.processor;

import org.apache.log4j.Logger;

import com.bj.supermarket.common.AppConstants;
import com.bj.supermarket.exception.InvalidPurchaseException;
import com.bj.supermarket.manager.CartModelManager;
import com.bj.supermarket.util.AppUtil;

public interface ICartProcessor {

	final static Logger logger = Logger.getLogger(ICartProcessor.class);

	/**
	 * Default method to get payment display amount.
	 * 
	 * @param aManager
	 * @return
	 */
	static public String getTotalPaymentAmount(CartModelManager aManager) {
		logger.info(AppConstants.METHOD_ENTRY_MESSAGE + "getTotalPaymentAmount");
		double myAmount = 0.0;
		if (null != aManager && null != aManager.getCart()) {
			myAmount = aManager.getCart().getPaymentAmount();
			logger.debug("Final Amount : " + myAmount);
		}
		String myDisplayAmount = AppUtil.getCurrencySymbol() + (myAmount / 100);
		logger.info(AppConstants.METHOD_EXIT_MESSAGE + "getTotalPaymentAmount");
		return myDisplayAmount;
	}

	/**
	 * Override to implement product scan logic
	 * 
	 * @param aItemName
	 * @return
	 * @throws InvalidPurchaseException
	 */
	boolean scanProduct(CartModelManager myManager, String aItemName) throws InvalidPurchaseException;

}
