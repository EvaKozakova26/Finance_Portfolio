package com.mystocks.service;

import com.mystocks.dto.CryptoTransactionCreateEntity;
import com.mystocks.model.CryptoTransaction;
import com.mystocks.repository.CryptoTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;

@Component
public class TransactionServiceImpl implements TransactionService{

	private final CryptoTransactionsRepository cryptoTransactionsRepository;

	@Autowired
	public TransactionServiceImpl(CryptoTransactionsRepository cryptoTransactionsRepository) {
		this.cryptoTransactionsRepository = cryptoTransactionsRepository;
	}

	@Override
	public void createCryptoTransaction(ForexDataDto forexData, CryptoTransactionCreateEntity ctce, String userId) {
		CryptoTransaction cryptoTransaction = new CryptoTransaction();

		cryptoTransaction.setUserId(userId);
		cryptoTransaction.setDate(Date.valueOf(ctce.getTransactionDate().substring(0,10)));
		cryptoTransaction.setTransactionValueInCrowns(new BigDecimal(ctce.getTransactionValue()));
		cryptoTransaction.setAmount(new BigDecimal(ctce.getAmount()));
		cryptoTransaction.setType(ctce.getAssetType());

		double transactionValueInDollars = Double.parseDouble(ctce.getTransactionValue()) / forexData.getRates().getCZK() * forexData.getRates().getUSD();

		double stockPriceInDollars = transactionValueInDollars / Double.parseDouble(ctce.getAmount());
		double stockPriceInCrowns = Double.parseDouble(ctce.getTransactionValue()) / Double.parseDouble(ctce.getAmount());

		cryptoTransaction.setTransactionValueInDollars(BigDecimal.valueOf(transactionValueInDollars));
		cryptoTransaction.setStockPriceInCrowns(BigDecimal.valueOf(stockPriceInCrowns));
		cryptoTransaction.setStockPriceInDollars(BigDecimal.valueOf(stockPriceInDollars));

		cryptoTransactionsRepository.save(cryptoTransaction);

	}
}
