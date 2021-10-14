package com.mystocks.dto;

import java.util.List;

public class TransactionListEntity {

	private List<TransactionDto> transactions;

	private Double averageTransactionValueInDollars;

	private Double averageTransactionValueInCrowns;

	public List<TransactionDto> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionDto> transactionDtos) {
		this.transactions = transactionDtos;
	}

	public Double getAverageTransactionValueInDollars() {
		return averageTransactionValueInDollars;
	}

	public void setAverageTransactionValueInDollars(Double averageTransactionValueInDollars) {
		this.averageTransactionValueInDollars = averageTransactionValueInDollars;
	}

	public Double getAverageTransactionValueInCrowns() {
		return averageTransactionValueInCrowns;
	}

	public void setAverageTransactionValueInCrowns(Double averageTransactionValueInCrowns) {
		this.averageTransactionValueInCrowns = averageTransactionValueInCrowns;
	}
}
