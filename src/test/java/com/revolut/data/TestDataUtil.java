package com.revolut.data;

import static com.revolut.util.SourceID.AMAZON_PAY_SOURCE_ID;
import static com.revolut.util.SourceName.AMAZON_PAY_SOURCE;

import com.revolut.dto.Payment;
import com.revolut.util.Currency;

public class TestDataUtil {
	
	public static final Payment getPaymentDetailsMock() {
		return new Payment (AMAZON_PAY_SOURCE_ID.getIdValue(), AMAZON_PAY_SOURCE.getValue(), 777, Currency.INR.getValue(), null);
	}

}
