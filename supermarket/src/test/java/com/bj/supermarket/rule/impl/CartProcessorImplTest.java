package com.bj.supermarket.rule.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bj.supermarket.exception.InvalidPurchaseException;
import com.bj.supermarket.manager.CartModelManager;
import com.bj.supermarket.model.OfferProduct;
import com.bj.supermarket.model.Product;
import com.bj.supermarket.processor.ICartProcessor;
import com.bj.supermarket.processor.impl.CartProcessorImpl;
import com.bj.supermarket.processor.impl.PricingProcessorImpl;
import com.bj.supermarket.util.AppUtil;

import junit.framework.TestCase;

public class CartProcessorImplTest extends TestCase {

	ICartProcessor myCartProcessor = null;
	
	CartModelManager myManager = null;

	static Map<String, Product> myProductMap = null;

	static {
		
		myProductMap = new HashMap<>();
		myProductMap.put("A", populateProductObject("A", 50, populatefferProductObject(3, 130)));
		myProductMap.put("B", populateProductObject("B", 30, populatefferProductObject(2, 45)));
		myProductMap.put("C", populateProductObject("C", 20,null));
		myProductMap.put("D", populateProductObject("D", 15, null));
	}

	

	@Before
	public void setUp() throws Exception {
		super.setUp();
		myCartProcessor = CartProcessorImpl.getInstance();
		myManager = new CartModelManager();
		PricingProcessorImpl.getInstance().getProductDetails().clear();
		PricingProcessorImpl.getInstance().getProductDetails().putAll(myProductMap);
	}

	@After
	public void tearDown() throws Exception {
		myManager = null;
		myCartProcessor = null;
		super.tearDown();
	}

	@Test
	public void testProductWhichIsNotAvailable() {
		try {
			assertEquals(false, myCartProcessor.scanProduct(myManager, "X"));
		} catch (InvalidPurchaseException e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	public void testProductWhichIsAvailable() {
		try {
			
			assertEquals(true, myCartProcessor.scanProduct(myManager, "A"));
		} catch (InvalidPurchaseException e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	public void testAddTwoProducts() {
		try {

			
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "B");

			assertEquals(getExpectedAmount(0.80), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (Exception e) {
			e.printStackTrace();

			fail();

		}
	}

	@Test
	public void testAddAllProducts() {
		try {

			
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "B");
			myCartProcessor.scanProduct(myManager, "C");
			myCartProcessor.scanProduct(myManager, "D");

			assertEquals(getExpectedAmount(1.15), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testAddAllProductsWithFourA() {
		try {

			
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "B");
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "C");
			myCartProcessor.scanProduct(myManager, "D");
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "A");

			assertEquals(getExpectedAmount(2.45), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testBAB() {
		try {

			
			myCartProcessor.scanProduct(myManager, "B");
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "B");
			assertEquals(getExpectedAmount(0.95), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testAAA() {
		try {

			
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "A");
			assertEquals(getExpectedAmount(1.30), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testAAAA() {
		try {

			
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "A");
			assertEquals(getExpectedAmount(1.80), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testCD() {
		try {

			
			myCartProcessor.scanProduct(myManager, "C");
			myCartProcessor.scanProduct(myManager, "D");
			assertEquals(getExpectedAmount(0.35), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testX() {
		try {
			
			myCartProcessor.scanProduct(myManager, "X");
			assertEquals(getExpectedAmount(0.0), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testAXB() {
		try {
			
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "X");
			myCartProcessor.scanProduct(myManager, "B");
			assertEquals(getExpectedAmount(0.8), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testXXABXA() {
		try {
			
			myCartProcessor.scanProduct(myManager, "X");
			myCartProcessor.scanProduct(myManager, "X");
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "B");
			myCartProcessor.scanProduct(myManager, "X");
			myCartProcessor.scanProduct(myManager, "A");
			assertEquals(getExpectedAmount(1.3), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testABX() {
		try {
			
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "B");
			myCartProcessor.scanProduct(myManager, "X");
			assertEquals(getExpectedAmount(0.8), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testXAB() {
		try {
			
			myCartProcessor.scanProduct(myManager, "A");
			myCartProcessor.scanProduct(myManager, "B");
			myCartProcessor.scanProduct(myManager, "X");
			assertEquals(getExpectedAmount(0.8), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNull() {
		try {
			
			myCartProcessor.scanProduct(myManager, null);
			assertEquals(getExpectedAmount(0.0), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testEmptyString() {
		try {
			
			myCartProcessor.scanProduct(myManager, "");
			assertEquals(getExpectedAmount(0.0), ICartProcessor.getTotalPaymentAmount(myManager));
		} catch (InvalidPurchaseException e) {
			e.printStackTrace();
			fail();
		}
	}


	private String getExpectedAmount(double aAmount) {
		return AppUtil.getCurrencySymbol() + String.valueOf(aAmount);
	}
	
	
	private static Product populateProductObject(String id, double price, OfferProduct aOfferProduct) {
		Product myProduct = new Product();
		myProduct.setProductId(id);
		myProduct.setProductPrice(price);
		myProduct.setOfferProduct(aOfferProduct);
		return myProduct;
	}
	
	private static OfferProduct populatefferProductObject(int units, double offerPrice) {		
		OfferProduct myProduct = new OfferProduct();
		myProduct.setMinimumOfferUnits(units);
		myProduct.setOfferPrice(offerPrice);
		
		return myProduct;
		
	}
}
