package com.bj.supermarket.model;

/**
 * Model to hold Offer details
 * 
 * @author bins
 *
 */

public class OfferProduct {

	private int minimumOfferUnits;
	private double offerPrice;
	

	public int getMinimumOfferUnits() {
		return minimumOfferUnits;
	}

	public void setMinimumOfferUnits(int minimumOfferUnits) {
		this.minimumOfferUnits = minimumOfferUnits;
	}

	public double getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(double offerPrice) {
		this.offerPrice = offerPrice;
	}
	
}
