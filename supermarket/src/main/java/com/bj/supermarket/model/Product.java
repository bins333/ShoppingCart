package com.bj.supermarket.model;

import java.util.Objects;


/**
 * Model to hold product details
 * @author bins
 *
 */
public class Product {

	protected String productId;
	protected double productPrice;

	public Product() {
		super();
	}

	public Product(String productId, double productPrice) {
		super();
		this.productId = productId;
		this.productPrice = productPrice;
	}

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

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productPrice=" + productPrice + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, productPrice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(productId, other.productId)
				&& Double.doubleToLongBits(productPrice) == Double.doubleToLongBits(other.productPrice);
	}

}
