package com.revolut.resource.itest;

import static com.revolut.data.TestDataUtil.getPaymentDetailsMock;
import static com.revolut.util.SourceID.AMAZON_PAY_SOURCE_ID;
import static com.revolut.util.SourceID.GOOGLE_PAY_SOURCE_ID;
import static com.revolut.util.SourceName.AMAZON_PAY_SOURCE;
import static org.junit.Assert.assertEquals;

import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.revolut.dao.PaymentTransferDB;
import com.revolut.resource.PaymentRestController;

public class PaymentRestControllerIWebTest {

	private PaymentRestController paymentRestController;

	private PaymentTransferDB paymentTransferDB;

	private Validator validator;

	@Before
	public void setUp() throws Exception {
		paymentRestController = new PaymentRestController(validator);
		paymentTransferDB.rePopulateValues();
	}

	@Test
	public void testGetPaymentById() {
		assertEquals(200, paymentRestController.getPaymentById(AMAZON_PAY_SOURCE_ID.getIdValue()).getStatus());
		assertEquals(200, paymentRestController.getPaymentById(GOOGLE_PAY_SOURCE_ID.getIdValue()).getStatus());
	}

	@Test(expected = RuntimeException.class)
	public void testGetPaymentByInvalidId() {
		paymentRestController.getPaymentById(3);
	}

	@Test
	public void testCreatePayment() {
		int previousTotalAmount = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		assertEquals(200, paymentRestController.createPayment(getPaymentDetailsMock()).getStatus());
		assertEquals(Integer.valueOf(previousTotalAmount + getPaymentDetailsMock().getAmount()), paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
	}

	@Test
	public void testRemovePaymentById() {
		paymentRestController.createPayment(getPaymentDetailsMock());
		int previousTotalAmount = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		UUID uId = paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue()).stream()
				.filter(p -> p.getAmount().equals(getPaymentDetailsMock().getAmount())).collect(Collectors.toList())
				.get(0).getTransactionId();
		assertEquals(200, paymentRestController.removePaymentById(uId).getStatus());
		assertEquals(Integer.valueOf(previousTotalAmount - getPaymentDetailsMock().getAmount()), paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
	}

}
