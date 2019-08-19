package com.revolut.dao;

import static com.revolut.util.Currency.INR;
import static com.revolut.util.SourceID.AMAZON_PAY_SOURCE_ID;
import static com.revolut.util.SourceID.GOOGLE_PAY_SOURCE_ID;
import static com.revolut.util.SourceName.AMAZON_PAY_SOURCE;
import static com.revolut.util.SourceName.GOOGLE_PAY_SOURCE;
import static java.util.UUID.randomUUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revolut.dto.Payment;

public class PaymentTransferDB {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentTransferDB.class);

	private static List<Payment> paymentSourceAmazon = new ArrayList<>();
	private static List<Payment> paymentSourceGoogle = new ArrayList<>();
	private static Map<String, List<Payment>> map = new HashMap<>();

	static {
		populateValues();
	}
	
	public static final void rePopulateValues() {
		map = new HashMap<>();
		paymentSourceAmazon = new ArrayList<>();
		paymentSourceGoogle = new ArrayList<>();
		populateValues();
	}
	
	private static final void populateValues() {
		paymentSourceAmazon.add(new Payment(AMAZON_PAY_SOURCE_ID.getIdValue(), AMAZON_PAY_SOURCE.getValue(), 1000, INR.getValue(), randomUUID()));
		paymentSourceAmazon.add(new Payment(AMAZON_PAY_SOURCE_ID.getIdValue(), AMAZON_PAY_SOURCE.getValue(), 2000, INR.getValue(), randomUUID()));
		paymentSourceAmazon.add(new Payment(AMAZON_PAY_SOURCE_ID.getIdValue(), AMAZON_PAY_SOURCE.getValue(), 3000, INR.getValue(), randomUUID()));
		map.put(AMAZON_PAY_SOURCE.getValue(), paymentSourceAmazon);
		map.put(GOOGLE_PAY_SOURCE.getValue(), paymentSourceGoogle);
	}

	public static Map<String, List<Payment>> getAllDetails() {
		return map;
	}
	
	public static Integer getTotalAmountBySource(Integer id) {

		return id.equals(AMAZON_PAY_SOURCE_ID.getIdValue())
				? paymentSourceAmazon.stream().filter(Objects::nonNull).filter(p -> p.getSourceID().equals(id))
						.mapToInt(Payment::getAmount).sum()
				: (id.equals(GOOGLE_PAY_SOURCE_ID.getIdValue())
						? null == paymentSourceGoogle || paymentSourceGoogle.size() == 0 ? 0
								: paymentSourceGoogle.stream().filter(Objects::nonNull).filter(p -> p.getSourceID().equals(id)).mapToInt(Payment::getAmount).sum()
						: 0);
	}

	public static Map<String, List<Payment>> addAmount(Payment p) {
		p.setTransactionId(randomUUID());
		paymentSourceAmazon.add(p);
		map.put(AMAZON_PAY_SOURCE.getValue(), paymentSourceAmazon);
		return map;
	}

	public static Map<String, List<Payment>> withdrawPayment(UUID transactionId) {
		paymentSourceAmazon.removeIf(p -> p.getTransactionId().equals(transactionId));
		map.put(AMAZON_PAY_SOURCE.getValue(), paymentSourceAmazon);
		return map;
	}

	public static Map<String, List<Payment>> transferAllAmount() {
		paymentSourceAmazon.stream().forEach(payment -> {
			payment.setSourceName(GOOGLE_PAY_SOURCE.getValue());
			payment.setSourceID(GOOGLE_PAY_SOURCE_ID.getIdValue());
			payment.setTransactionId(randomUUID());
		});
		paymentSourceGoogle.addAll(paymentSourceAmazon);
		paymentSourceAmazon = new ArrayList<>();
		map.put(AMAZON_PAY_SOURCE.getValue(), paymentSourceAmazon);
		map.put(GOOGLE_PAY_SOURCE.getValue(), paymentSourceGoogle);
		return map;
	}

	public static Map<String, List<Payment>> transferPaymentByTransactionId(UUID transactionId) {

		List<Payment> payments = paymentSourceAmazon.stream().filter(p -> (p.getTransactionId().equals( transactionId)))
				.collect(Collectors.toList());
		if (null != payments && payments.size() > 0) {
			paymentSourceAmazon.removeIf(p -> (p.getTransactionId().equals( transactionId)));
			payments.stream().forEach(payment -> {
				payment.setSourceName(GOOGLE_PAY_SOURCE.getValue());
				payment.setSourceID(GOOGLE_PAY_SOURCE_ID.getIdValue());
				payment.setTransactionId(transactionId);
			});
			paymentSourceGoogle.addAll(payments);
			map.put(AMAZON_PAY_SOURCE.getValue(), paymentSourceAmazon);
			map.put(GOOGLE_PAY_SOURCE.getValue(), paymentSourceGoogle);
		}
		return map;
	}

	public static Map<String, List<Payment>> transferAmount(Integer amount) {

		List<Payment> filtered = paymentSourceAmazon.stream().filter(p0 -> p0.getAmount().equals(amount))
				.collect(Collectors.toList());
		int a = 0;
		if (null == filtered || filtered.size() == 0) {
			filtered = paymentSourceAmazon.stream().filter(p0 -> p0.getAmount() > amount).collect(Collectors.toList());
			if (null == filtered || filtered.size() == 0) {
				LOGGER.info("Invalid Attempt!!");
				return map;
			} else {
				Payment p = filtered.get(0);
				a = p.getAmount();
				paymentSourceAmazon.remove(p);
				p.setAmount(a - amount);
				paymentSourceAmazon.add(p);
			}
		} else {
			Payment p = filtered.get(0);
			paymentSourceAmazon.remove(p);
		}
		paymentSourceGoogle.add(new Payment(GOOGLE_PAY_SOURCE_ID.getIdValue(), GOOGLE_PAY_SOURCE.getValue(), amount, INR.getValue(), randomUUID()));
		map.put(AMAZON_PAY_SOURCE.getValue(), paymentSourceAmazon);
		map.put(GOOGLE_PAY_SOURCE.getValue(), paymentSourceGoogle);
		return map;
	}

}
