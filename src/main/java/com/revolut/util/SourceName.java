package com.revolut.util;

public enum SourceName {

	AMAZON_PAY_SOURCE("AmazonPay"), GOOGLE_PAY_SOURCE("GPay");
	
	private String value;

	SourceName(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
