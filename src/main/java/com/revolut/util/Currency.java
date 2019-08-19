package com.revolut.util;

public enum Currency {

	INR("INR"), GBP("GBP");

	private String value;

	Currency(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
