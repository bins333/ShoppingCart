package com.bj.supermarket.rule.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bj.supermarket.exception.InvalidPurchaseException;
import com.bj.supermarket.model.OfferProduct;
import com.bj.supermarket.model.Product;
import com.bj.supermarket.processor.impl.CartProcessorImpl;
import com.bj.supermarket.processor.impl.PricingProcessorImpl;
import com.bj.supermarket.util.AppUtil;

import junit.framework.TestCase;

public class CartProcessorImplTest extends TestCase {

	CartProcessorImpl myCartProcessor = null;

	static Map<String, Product> myProductMap = null;
	static Map<String, OfferProduct> myOfferMap = null;

	static {
		myProductMap = new HashMap<>();
		myProductMap.put("A", new Product("A", 50));
		myProductMap.put("B", new Product("B", 30));
		myProductMap.put("C", new Product("C", 20));
		myProductMap.put("D", new Product("D", 15));
	}

	static {
		myOfferMap = new HashMap<>();
		myOfferMap.put("A", new OfferProduct("A", 130, 3));
		myOfferMap.put("B", new OfferProduct("B", 45, 2));
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		myCartProcessor = CartProcessorImpl.getInstance();
		PricingProcessorImpl.getInstance().getProductDetails().clear();
		PricingProcessorImpl.getInstance().getOfferProductDetails().clear();
		PricingProcessorImpl.getInstance().getProductDetails().putAll(myProductMap);
		PricingProcessorImpl.getInstance().getOfferProductDetails().putAll(myOfferMap);
	}

	@After
	public void tearDown() throws Exception {
		myCartProcessor = null;
		super.tearDown();
	}

	@Test
	public void testProductWhichIsNotAvailable() {
		try {
			assertEquals(false, myCartProcessor.scanProduct("X"));
		} catch (InvalidPurchaseException e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	public void testProductWhichIsAvailable() {
		try {
			assertEquals(true, myCartProcessor.scanProduct("A"));
		} catch (InvalidPurchaseException e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	public void testAddTwoProducts() {
		try {

			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("B");

			assertEquals(getExpectedAmount(0.80), myCartProcessor.getCheckOutDisplayAmount());
		} catch (Exception e) {
			e.printStackTrace();

			fail();

		}
	}

	@Test
	public void testAddAllProducts() {
		try {

			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("B");
			myCartProcessor.scanProduct("C");
			myCartProcessor.scanProduct("D");

			assertEquals(getExpectedAmount(1.15), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testAddAllProductsWithFourA() {
		try {

			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("B");
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("C");
			myCartProcessor.scanProduct("D");
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("A");

			assertEquals(getExpectedAmount(2.45), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testBAB() {
		try {

			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("B");
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("B");
			assertEquals(getExpectedAmount(0.95), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testAAA() {
		try {

			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("A");
			assertEquals(getExpectedAmount(1.30), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testAAAA() {
		try {

			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("A");
			assertEquals(getExpectedAmount(1.80), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testCD() {
		try {

			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("C");
			myCartProcessor.scanProduct("D");
			assertEquals(getExpectedAmount(0.35), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testX() {
		try {
			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("X");
			assertEquals(getExpectedAmount(0.0), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testAXB() {
		try {
			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("X");
			myCartProcessor.scanProduct("B");
			assertEquals(getExpectedAmount(0.8), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testXXABXA() {
		try {
			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("X");
			myCartProcessor.scanProduct("X");
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("B");
			myCartProcessor.scanProduct("X");
			myCartProcessor.scanProduct("A");
			assertEquals(getExpectedAmount(1.3), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testABX() {
		try {
			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("B");
			myCartProcessor.scanProduct("X");
			assertEquals(getExpectedAmount(0.8), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testXAB() {
		try {
			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("A");
			myCartProcessor.scanProduct("B");
			myCartProcessor.scanProduct("X");
			assertEquals(getExpectedAmount(0.8), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNull() {
		try {
			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct(null);
			assertEquals(getExpectedAmount(0.0), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testEmptyString() {
		try {
			myCartProcessor.getCartDetails().clear();
			myCartProcessor.scanProduct("");
			assertEquals(getExpectedAmount(0.0), myCartProcessor.getCheckOutDisplayAmount());
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}


	private String getExpectedAmount(double aAmount) {
		return AppUtil.getCurrencySymbol() + String.valueOf(aAmount);
	}
}
