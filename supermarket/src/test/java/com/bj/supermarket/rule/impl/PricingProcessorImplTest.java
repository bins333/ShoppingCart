package com.bj.supermarket.rule.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;

import com.bj.supermarket.exception.InvalidProductDetailsException;
import com.bj.supermarket.model.OfferProduct;
import com.bj.supermarket.model.Product;
import com.bj.supermarket.processor.impl.PricingProcessorImpl;

import junit.framework.TestCase;

public class PricingProcessorImplTest extends TestCase {

	PricingProcessorImpl myRuleProcessor = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		myRuleProcessor = PricingProcessorImpl.getInstance();
	}

	@Override
	protected void tearDown() throws Exception {
		myRuleProcessor = null;
		super.tearDown();
	}

	@Test
	public void testPricingruleWithEmpty() {
		try {
			String myRule = "";
			assertFalse(myRuleProcessor.processPricingRule(myRule));

		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.printStackTrace();
		}
	}

	@Test
	public void testPricingRuleWithNull() {
		try {
			String myRule = null;
			assertFalse(myRuleProcessor.processPricingRule(myRule));

		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}

	@Test
	public void testValidPricingRuleWithValidInput() {
		try {
			String myRule = "A | 50 | 3 for 130";
			assertTrue(myRuleProcessor.processPricingRule(myRule));
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}

	
	@Test
	public void testValidPricingRuleInMap() {
		try {
			String myRule = "A | 50 | 2 for 130";
			OfferProduct myOfferProduct =  new OfferProduct("A", 130, 2);			
			Product myProduct =  new Product("A", 50);
			assertTrue(myRuleProcessor.processPricingRule(myRule));
			assertTrue(myRuleProcessor.getOfferProductDetails().get("A").equals(myOfferProduct));
			assertTrue(myRuleProcessor.getProductDetails().get("A").equals(myProduct));
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}
	
	@Test
	public void testValidPricingRuleInMapWithMultipleObject() {
		try {
			myRuleProcessor.getOfferProductDetails().clear();
			String myRuleA1 = "A | 40 | 2 for 120";
			String myRuleA2 = "A | 50 | 2 for 130";
			String myRuleB = "B | 30 | 2 for 40";
			String myRuleC = "C | 20";
			String myRuleD = "D | 15 ";
			OfferProduct myOfferProduct =  new OfferProduct("A", 130, 2);			
			Product myProduct =  new Product("A", 50);
			myRuleProcessor.processPricingRule(myRuleA1);
			myRuleProcessor.processPricingRule(myRuleA2);
			myRuleProcessor.processPricingRule(myRuleB);
			myRuleProcessor.processPricingRule(myRuleC);
			myRuleProcessor.processPricingRule(myRuleD);
			
			// Test size
			assertThat(myRuleProcessor.getProductDetails().size(), is(4));
	        assertThat(myRuleProcessor.getOfferProductDetails().size(), is(2));
	        

	        // Test map entry
	        assertThat(myRuleProcessor.getProductDetails(), IsMapContaining.hasEntry("A", myProduct));
	        assertThat(myRuleProcessor.getOfferProductDetails(), IsMapContaining.hasEntry("A", myOfferProduct));

	       
	        //4. Test map key
	        assertThat(myRuleProcessor.getProductDetails(), IsMapContaining.hasKey("D"));
	        assertThat(myRuleProcessor.getOfferProductDetails(), IsMapContaining.hasKey("B"));
	        
	        
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}
	
	@Test
	public void testValidPricingRuleWithValidInputIgnoreCase() {
		try {
			String myRule = "a | 50 | 3 FoR 130";
			assertTrue(myRuleProcessor.processPricingRule(myRule));
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}

	@Test
	public void testValidPricingRuleWithSpaces() {
		try {
			String myRule = " A  |  50  |  3  for  130";
			assertTrue(myRuleProcessor.processPricingRule(myRule));
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}
	
	@Test
	public void testValidPricingRuleWithNoSpaces() {
		try {
			String myRule = " A|50|3  for  130";
			assertTrue(myRuleProcessor.processPricingRule(myRule));
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}

	@Test
	public void testValidPricingRuleWithoutOffer() {
		try {
			String myRule = "A | 50";
			assertTrue(myRuleProcessor.processPricingRule(myRule));
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}

	@Test
	public void testValidPricingRuleWithIncorrectOffer() {
		try {
			String myRule = "A | 50 | 2";
			assertFalse(myRuleProcessor.processPricingRule(myRule));
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}
	
	@Test
	public void testValidPricingRuleWithIncorrectOffer01() {
		try {
			String myRule = "A | 50 | 2 for A";
			assertFalse(myRuleProcessor.processPricingRule(myRule));
			
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}
	
	@Test
	public void testValidPricingRuleWithOnlyProductID() {
		try {
			String myRule = "A";
			assertFalse(myRuleProcessor.processPricingRule(myRule));
		} catch (InvalidProductDetailsException aException) {
			fail();
			aException.getMessage();
		}
	}
	

}
