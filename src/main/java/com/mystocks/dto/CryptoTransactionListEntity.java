package com.mystocks.dto;

import java.util.List;

public class CryptoTransactionListEntity {

	private List<CryptoTransactionDto> cryptoTransactions;

	public List<CryptoTransactionDto> getCryptoTransactions() {
		return cryptoTransactions;
	}

	public void setCryptoTransactions(List<CryptoTransactionDto> cryptoTransactions) {
		this.cryptoTransactions = cryptoTransactions;
	}
}
