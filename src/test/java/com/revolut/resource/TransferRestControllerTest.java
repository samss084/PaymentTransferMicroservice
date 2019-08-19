package com.revolut.resource;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.revolut.dao.PaymentTransferDB;
import static com.revolut.util.SourceID.*;

/**
 * 
 * Junit test cases for TransferRestController class.
 * @author Swarnava Chakraborty
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ TransferRestController.class })
public class TransferRestControllerTest {

	@InjectMocks
	private TransferRestController transferRestController;

	@Mock
	private PaymentTransferDB paymentTransferDB;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		paymentTransferDB.rePopulateValues();
	}

	@Test
	public void testTransfer() {
		Integer amountOfAmazonAcc = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		Integer amountOfGPayAcc = paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue());
		assertEquals(Integer.valueOf(0), amountOfGPayAcc);
		assertEquals(200, transferRestController.transfer().getStatus());
		assertEquals(amountOfAmazonAcc, paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue()));
		assertEquals(Integer.valueOf(0), paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
	}

	@Test
	public void testTransferWithAmount() {
		Integer amountOfAmazonAcc = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		Integer amountOfGPayAcc = paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue());
		assertEquals(Integer.valueOf(0), amountOfGPayAcc);
		assertEquals(200, transferRestController.transfer(500).getStatus());
		assertEquals(Integer.valueOf(500), paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue()));
		assertEquals(amountOfAmazonAcc, Integer.valueOf(paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()) + 500));
	}

}
