package com.bj.supermarket;

import static com.bj.supermarket.common.AppConstants.*;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bj.supermarket.common.AppConstants;
import com.bj.supermarket.exception.InvalidProductDetailsException;
import com.bj.supermarket.exception.InvalidPurchaseException;
import com.bj.supermarket.manager.CartModelManager;
import com.bj.supermarket.processor.ICartProcessor;
import com.bj.supermarket.processor.IPricingProcessor;
import com.bj.supermarket.processor.impl.CartProcessorImpl;
import com.bj.supermarket.processor.impl.PricingProcessorImpl;

/*
 * Implement the code for a checkout system that handles pricing schemes such as
 * “apples cost 50 pence, three apples cost £1.30.” Implement the code for a
 * supermarket checkout that calculates the total price of a number of items. In
 * a normal supermarket, things are identified using Stock Keeping Units, or
 * SKUs. In our store, we’ll use individual letters of the alphabet (A, B, C,
 * and so on). Our goods are priced individually. In addition, some items are
 * multi-priced: buy ‘n’ of them, and they’ll cost you ‘y’ pence. For example,
 * item ‘A’ might cost 50 pence individually, but this week we have a special
 * offer: buy three ‘A’s and they’ll cost you £1.30.
 * 
 * A 50 3 for 130 B 30 2 for 45 C 20 D 15
 * 
 * @author bins
 *
 */
public class App {

	final static Logger logger = Logger.getLogger(App.class);

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Application execution started");
		// try- resource since we need scanner object to be available for product scan

		try (Scanner myScanner = new Scanner(System.in)) {

			/*
			 * displayPricingRuleInfoMessage(); processPricingRule(myScanner);
			 * displayProductScanInfoMessage(); processProductScan(myScanner);
			 */
			
			// invoking menu logic
			processAppForExplicitExit(myScanner);

		} catch (InvalidPurchaseException | InvalidProductDetailsException | InterruptedException e) {
			logger.error(e);
		}
		logger.info("Application execution end");
	}

	
	/**
	 * Building menu
	 * @param aScanner
	 * @throws InvalidProductDetailsException
	 * @throws InvalidPurchaseException
	 * @throws InterruptedException
	 */
	private static void processAppForExplicitExit(Scanner aScanner)
			throws InvalidProductDetailsException, InvalidPurchaseException, InterruptedException {
		boolean isAppRunning = true;
		while (isAppRunning) {
			displayOptions();
			String key = "";
			if (aScanner.hasNextLine()) {
				key = aScanner.nextLine();
			}
			logger.info("Key enterd is " + key);
			switch (key) {
			case "1":
				logger.info("<<Invoking price rule module>>");
				displayPricingRuleInfoMessage();
				processPricingRule(aScanner);
				logger.info("<<Price rule module execution completed>>");
				break;

			case "2":
				logger.info("<<Invoking checkout rule module>>");
				displayProductScanInfoMessage();
				boolean success = processProductScan(aScanner);
				if(success) {
				System.out.println(PAYMENT_PROGRESS_MESSAGE);
				Thread.sleep(2000);
				System.out.println(PAYMENT_COMPLETED_MESSAGE);
				Thread.sleep(2000);
				}
				logger.info("<<Checkout module execution completed>>");
				break;

			case "3":
				logger.info("<<Exiting application>>");
				System.out.println(APPLICATION_EXIT_MESSAGE);				
				isAppRunning = false;
				break;

			default:
				System.out.println(INVALID_SELECTION_MESSAGE);
				logger.info("<<Executing default option>>");
				continue;
			}
		}

	}

	/**
	 * This method read the input in [Product Name] | [unit price] | [X for Y]
	 * format <b>Ex: A | 50 | 3 for 130</b>
	 * 
	 * @param aScanner
	 * @throws InvalidProductDetailsException
	 */
	private static void processPricingRule(Scanner aScanner) throws InvalidProductDetailsException {
		IPricingProcessor myProcessor = PricingProcessorImpl.getInstance();
		while (true) {
			String myProductDetails = aScanner.nextLine();
			if (myProductDetails.equalsIgnoreCase(DONE)) {
				break;
			} else {
				boolean isProductAdded = myProcessor.processPricingRule(myProductDetails);
				if (isProductAdded) {
					System.out.println(AppConstants.PRODUCT_RULE_SUCESS_MESSAGE);
				} else {
					System.out.println(PRODUCT_RULE_FAILURE_MESSAGE + myProductDetails);
				}
			}
		}

	}

	/**
	 * This method will scan the product and calculate the running total.
	 * 
	 * @param aScanner
	 * @throws InvalidPurchaseException
	 */
	private static boolean processProductScan(Scanner aScanner) throws InvalidPurchaseException {
		// myScanner = new Scanner(System.in);
		ICartProcessor myProcessor = CartProcessorImpl.getInstance();
		CartModelManager myCartModelManager = new CartModelManager();
		boolean isScanSucess  = false;
		while (true) {
			String myProduct = aScanner.nextLine();
			if (myProduct.equalsIgnoreCase(PAY)) {
				break;
			} else {
				isScanSucess = myProcessor.scanProduct(myCartModelManager, myProduct);
				if (!isScanSucess) {
					System.out.println(AppConstants.PRODUCT_NOT_AVAILABLE_START_MESSAGE + myProduct
							+ AppConstants.PRODUCT_NOT_AVAILABLE_END_MESSAGE);
				}
				String myPaymentAmount = ICartProcessor.getTotalPaymentAmount(myCartModelManager);
				System.out.println(RUNNING_AMOUNT_DESC + myPaymentAmount + PRODUCT_SCAN_MESSAGE_PCOMPLETE);
			}

		}
		System.out.println(TOTAL_AMOUNT_DESC + ICartProcessor.getTotalPaymentAmount(myCartModelManager));
		return isScanSucess;
	}

	/**
	 * Info message for price rule input
	 */
	private static void displayPricingRuleInfoMessage() {
		StringBuilder myStringBuilder = new StringBuilder();
		myStringBuilder.append("Enter Pricing rule for product in below format").append(System.lineSeparator()).append(
				"<Product Name> | <unit price> | <X for Y> where X stands for number of minimum units and Y stands for offer price.")
				.append(System.lineSeparator()).append("Ex: A | 50 | 3 for 130").append(System.lineSeparator())
				.append("Or A | 50 [In case no offer for that product]").append(System.lineSeparator())
				.append("Once all rules are added, type DONE and press enter!");
		System.out.println(myStringBuilder.toString());
	}

	/**
	 * Info message for product scan
	 */
	private static void displayProductScanInfoMessage() {
		System.out.println(SCAN_INFO_MESSAGE_MAIN);
	}

	/**
	 * Menu details
	 */
	private static void displayOptions() {
		StringBuilder myStringBuilder = new StringBuilder();
		myStringBuilder.append(MENU_HEADER).append(System.lineSeparator())
				.append(MENU_ONE).append(System.lineSeparator()).append(MENU_TWO)
				.append(System.lineSeparator()).append(MENU_THREE).append(System.lineSeparator());
		System.out.println(myStringBuilder.toString());
	}

}
