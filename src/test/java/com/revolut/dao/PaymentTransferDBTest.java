package com.revolut.dao;

import static com.revolut.data.TestDataUtil.getPaymentDetailsMock;
import static com.revolut.util.SourceID.AMAZON_PAY_SOURCE_ID;
import static com.revolut.util.SourceID.GOOGLE_PAY_SOURCE_ID;
import static com.revolut.util.SourceName.AMAZON_PAY_SOURCE;
import static com.revolut.util.SourceName.GOOGLE_PAY_SOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.revolut.dto.Payment;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PaymentTransferDB.class })
public class PaymentTransferDBTest {

	@InjectMocks
	private PaymentTransferDB paymentTransferDB;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		paymentTransferDB.rePopulateValues();
	}

	@Test
	public void testGetTotalAmountBySource() {
		assertNotNull(paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
		assertNotNull(paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue()));
		assertEquals(Integer.valueOf(0), paymentTransferDB.getTotalAmountBySource(3));
		assertEquals(Integer.valueOf(0), paymentTransferDB.getTotalAmountBySource(0));
	}

	@Test
	public void testAddAmount() {
		Map<String, List<Payment>> afterMap = paymentTransferDB.addAmount(getPaymentDetailsMock());
		List<Payment> payments = afterMap.get(AMAZON_PAY_SOURCE.getValue()).stream()
				.filter(p -> p.getAmount().equals(getPaymentDetailsMock().getAmount())).collect(Collectors.toList());
		assertNotNull(payments);
		assertEquals(getPaymentDetailsMock().getAmount(), payments.get(0).getAmount());
	}

	@Test
	public void testWithdrawPayment() {
		int initialSize = paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue()).size();
		UUID idToRemove = paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue()).get(0).getTransactionId();
		paymentTransferDB.withdrawPayment(idToRemove);
		assertEquals(initialSize - 1, paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue()).size());
		assertEquals(0, paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue()).stream()
				.filter(p -> p.getTransactionId().equals(idToRemove)).count());
	}

	@Test
	public void testTransferAmountFirst() {

		int initialFirst = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		int initialSecond = paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue());
		paymentTransferDB.transferAmount(100);
		assertEquals(Integer.valueOf(initialFirst - 100), paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
		assertEquals(Integer.valueOf(initialSecond + 100), paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue()));
	}
	
	@Test
	public void testTransferAmountSecond() {

		int initialFirst = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		int initialSecond = paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue());
		paymentTransferDB.transferAmount(1000);
		assertEquals(Integer.valueOf(initialFirst - 1000), paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
		assertEquals(Integer.valueOf(initialSecond + 1000), paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue()));
	}
	
	@Test
	public void testTransferAmountThird() {

		Integer initialFirst = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		Integer initialSecond = paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue());
		paymentTransferDB.transferAmount(10000);
		assertEquals(initialFirst, paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
		assertEquals(initialSecond, paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue()));
	}
	
	@Test
	public void testTransferPaymentByTransactionIdFirst() {

		List<Payment> amazons = paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue());
		List<Payment> gPay = paymentTransferDB.getAllDetails().get(GOOGLE_PAY_SOURCE.getValue());

		UUID idToTransfer = amazons.get(0).getTransactionId();

		assertNotEquals(0, amazons.stream().filter(p -> p.getTransactionId().equals(idToTransfer)).count());
		assertEquals(0, gPay.stream().filter(p -> p.getTransactionId().equals(idToTransfer)).count());

		paymentTransferDB.transferPaymentByTransactionId(idToTransfer);

		assertEquals(0, amazons.stream().filter(p -> p.getTransactionId().equals(idToTransfer)).count());
		assertEquals(1, gPay.stream().filter(p -> p.getTransactionId().equals(idToTransfer)).count());
	}
	
	@Test
	public void testTransferPaymentByTransactionIdSecond() {

		List<Payment> amazons = paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue());
		List<Payment> gPay = paymentTransferDB.getAllDetails().get(GOOGLE_PAY_SOURCE.getValue());

		UUID idToTransfer = UUID.randomUUID();

		assertEquals(0, amazons.stream().filter(p -> p.getTransactionId().equals(idToTransfer)).count());
		assertEquals(0, gPay.stream().filter(p -> p.getTransactionId().equals(idToTransfer)).count());

		paymentTransferDB.transferPaymentByTransactionId(idToTransfer);

		assertEquals(0, amazons.stream().filter(p -> p.getTransactionId().equals(idToTransfer)).count());
		assertEquals(0, gPay.stream().filter(p -> p.getTransactionId().equals(idToTransfer)).count());
	}

	@Test
	public void testTransferAllAmount() {
		assertNotEquals(0, paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue()).size());
		paymentTransferDB.transferAllAmount();
		assertEquals(0, paymentTransferDB.getAllDetails().get(AMAZON_PAY_SOURCE.getValue()).size());
	}
}
