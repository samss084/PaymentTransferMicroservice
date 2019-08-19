package com.revolut.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * 
 * @author SWCHAKR
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(as = Payment.class)
public class Payment {

	@NotNull
	@JsonProperty("sourceId")
	private Integer sourceID;

	@JsonProperty("sourceName")
	private String sourceName;

	@JsonProperty("amount")
	private Integer amount;

	@JsonProperty("transactionId")
	private UUID transactionId;

	@JsonProperty("currency")
	private String currency;

	public Payment() {
		super();
	}

	public Payment(Integer sourceID, String sourceName, Integer amount, String currency, UUID uuid) {
		super();
		this.sourceID = sourceID;
		this.sourceName = sourceName;
		this.amount = amount;
		this.currency = currency;
		this.transactionId = uuid;
	}

	public Integer getSourceID() {
		return sourceID;
	}

	public void setSourceID(Integer sourceID) {
		this.sourceID = sourceID;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public UUID getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Payment [sourceID=" + sourceID + ", sourceName=" + sourceName + ", amount=" + amount
				+ ", transactionId=" + transactionId + ", currency=" + currency + "]";
	}

}
