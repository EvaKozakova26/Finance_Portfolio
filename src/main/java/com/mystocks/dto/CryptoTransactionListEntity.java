package com.mystocks.dto;

import java.util.List;

public class CryptoTransactionListEntity {

	private List<CryptoTransactionDto> cryptoTransactions;

	private Double averageTransactionValueInDollars;

	private Double averageTransactionValueInCrowns;

	public List<CryptoTransactionDto> getCryptoTransactions() {
		return cryptoTransactions;
	}

	public void setCryptoTransactions(List<CryptoTransactionDto> cryptoTransactions) {
		this.cryptoTransactions = cryptoTransactions;
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
