package com.revolut.util;

public enum SourceID {

	AMAZON_PAY_SOURCE_ID(1), GOOGLE_PAY_SOURCE_ID(2);

	private Integer value;

	SourceID(Integer value) {
		this.value = value;
	}

	public Integer getIdValue() {
		return value;
	}
}
