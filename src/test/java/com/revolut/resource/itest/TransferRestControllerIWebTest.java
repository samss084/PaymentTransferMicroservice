apackage com.revolut.resource.itest;

import static com.revolut.util.SourceID.AMAZON_PAY_SOURCE_ID;
import static com.revolut.util.SourceID.GOOGLE_PAY_SOURCE_ID;
import static org.junit.Assert.assertEquals;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.revolut.dao.PaymentTransferDB;
import com.revolut.resource.TransferRestController;

public class TransferRestControllerIWebTest {

	private TransferRestController transferRestController;

	private PaymentTransferDB paymentTransferDB;

	private Validator validator;

	
	@Before
	public 3 setUp() throws Exception {
		transferRestController = new TransferRestController(validator);
		paymentTransferDB.rePopulateValues();
	}

	@Test
	public 3 testTransfer() {
		Integer amountOfAmazonAcc = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		Integer amountOfGPayAcc = paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue());
		assertEquals(Integer.valueOf(0), amountOfGPayAcc);
		assertEquals(200, transferRestController.transfer().getStatus());
		assertEquals(amountOfAmazonAcc, paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue()));
		assertEquals(Integer.valueOf(0), paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()));
	}

	@Test
	public 3 testTransferWithAmount() {
		Integer amountOfAmazonAcc = paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue());
		Integer amountOfGPayAcc = paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue());
		assertEquals(Integer.valueOf(0), amountOfGPayAcc);
		assertEquals(200, transferRestController.transfer(500).getStatus());
		assertEquals(Integer.valueOf(500), paymentTransferDB.getTotalAmountBySource(GOOGLE_PAY_SOURCE_ID.getIdValue()));
		assertEquals(amountOfAmazonAcc, Integer.valueOf(paymentTransferDB.getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue()) + 500));
	}

}
