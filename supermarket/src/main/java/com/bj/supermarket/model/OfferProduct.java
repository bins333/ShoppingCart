package com.bj.supermarket.model;

import java.util.Objects;

/**
 * Model to hold Offer details
 * @author bins
 *
 */

public class OfferProduct extends Product {

	int minimumOfferUnits;

	public OfferProduct() {
		super();
	}

	public OfferProduct(String productId, double productPrice, int minimumOfferUnits) {
		super(productId, productPrice);
		this.minimumOfferUnits = minimumOfferUnits;
	}

	public int getMinimumOfferUnits() {
		return minimumOfferUnits;
	}

	public void setMinimumOfferUnits(int minimumOfferUnits) {
		this.minimumOfferUnits = minimumOfferUnits;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(minimumOfferUnits, productId, productPrice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OfferProduct other = (OfferProduct) obj;
		return minimumOfferUnits == other.minimumOfferUnits && Objects.equals(productId, other.productId)
				&& Double.doubleToLongBits(productPrice) == Double.doubleToLongBits(other.productPrice);

	}

	

}
