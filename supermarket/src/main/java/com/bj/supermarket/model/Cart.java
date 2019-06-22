package com.bj.supermarket.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author bins
 *
 */
public class Cart {

	private Map<String, Integer> checkoutProducts = new HashMap<>();
	private double paymentAmount;

	
	public Map<String, Integer> getCheckoutProducts() {
		return checkoutProducts;
	}

	public void setCheckoutProducts(Map<String, Integer> checkoutProducts) {
		this.checkoutProducts = checkoutProducts;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

}
