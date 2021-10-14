package com.mystocks.dto;

public class TransactionCreateEntity {

	String amount;
	String transactionValue;
	String assetType;
	String transactionDate;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTransactionValue() {
		return transactionValue;
	}

	public void setTransactionValue(String transactionValue) {
		this.transactionValue = transactionValue;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
}
