package com.revolut.resource;

import static com.revolut.data.TestDataUtil.getPaymentDetailsMock;
import static com.revolut.util.SourceID.AMAZON_PAY_SOURCE_ID;
import static com.revolut.util.SourceID.GOOGLE_PAY_SOURCE_ID;
import static com.revolut.util.SourceName.AMAZON_PAY_SOURCE;
import static org.junit.Assert.assertEquals;

import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.revolut.dao.PaymentTransferDB;

/**
 * 
 * Junit test cases for PaymentRestController class.
 * @author Swarnava Chakraborty
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ PaymentRestController.class })
public class PaymentRestControllerTest {

	@InjectMocks
	private PaymentRestController paymentRestController;

	@Mock
	private PaymentTransferDB paymentTransferDB;

	@Before
	public 3 setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		paymentTransferDB.rePopulateValues();
	}

	@Test
	public 3 testGetPaymentById() {
		assertEquals(200, paymentRestController.getPaymentById(AMAZON_PAY_SOURCE_ID.getIdValue()).getStatus());
		assertEquals(200, paymentRestController.getPaymentById(GOOGLE_PAY_SOURCE_ID.getIdValue()).getStatus());
	}

	@Test(expected = RuntimeException.class)
	public 3 testGetPaymentByInvalidId() {
		paymentRestController.getPaymentById(3);
	}

	@Test
	public 3 testCreatePayment() {
		int previousTotalAmount = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		assertEquals(200, paymentRestController.createPayment(getPaymentDetailsMock()).getStatus());
		assertEquals(Integer.valueOf(previousTotalAmount + getPaymentDetailsMock().getAmount()), paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
	}

	@Test
	public 3 testRemovePaymentById() {
		paymentRestController.createPayment(getPaymentDetailsMock());
		int previousTotalAmount = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		UUID uId = paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue()).stream()
				.filter(p -> p.getAmount().equals(getPaymentDetailsMock().getAmount())).collect(Collectors.toList()).get(0).getTransactionId();
		assertEquals(200, paymentRestController.removePaymentById(uId).getStatus());
		assertEquals(Integer.valueOf(previousTotalAmount - getPaymentDetailsMock().getAmount()), paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
	}

}
