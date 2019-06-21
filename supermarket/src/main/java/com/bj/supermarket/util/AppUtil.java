package com.bj.supermarket.util;

import java.util.Currency;
import java.util.Locale;

public class AppUtil {

	
	public static String getCurrencySymbol() {
		Currency myCurrency = Currency.getInstance(Locale.UK);
		return myCurrency.getSymbol(Locale.UK);
	}
}
