package com.bj.supermarket.model;

/**
 * Model to hold product details
 * 
 * @author bins
 *
 */
public class Product {

	private String productId;
	private double productPrice;
	private OfferProduct offerProduct;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public OfferProduct getOfferProduct() {
		return offerProduct;
	}

	public void setOfferProduct(OfferProduct offerProduct) {
		this.offerProduct = offerProduct;
	}


}
