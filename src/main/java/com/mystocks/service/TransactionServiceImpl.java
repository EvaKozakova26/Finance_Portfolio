package com.mystocks.service;

import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.TransactionCreateEntity;
import com.mystocks.model.Transaction;
import com.mystocks.repository.TransactionsRepository;
import com.mystocks.utils.RetrofitBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

@Component
public class TransactionServiceImpl implements TransactionService{

	private final TransactionsRepository transactionsRepository;

	@Autowired
	public TransactionServiceImpl(TransactionsRepository transactionsRepository) {
		this.transactionsRepository = transactionsRepository;
	}

	@Override
	public void createTransaction(TransactionCreateEntity ctce, String userId) {
		Transaction transaction = new Transaction();

		Response<ForexDataDto> response = null;
		AssetApiService assetApiService = RetrofitBuilder.assetApiService(ApiConfiguration.API_FOREX_URL);
		Call<ForexDataDto> retrofitCall = assetApiService.getForexData(ctce.getTransactionDate().substring(0,10));

		try {
			response =  retrofitCall.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}

		// todo check on null
		ForexDataDto forexData = response != null ? response.body() : new ForexDataDto();

		if (ctce.getAssetType().equalsIgnoreCase("btc")) {
			// TODO: 14.10.2021 musim jeste vymyslet, jak ukladat btc


			transaction.setUserId(userId);
			transaction.setDate(Date.valueOf(ctce.getTransactionDate().substring(0,10)));
			transaction.setTransactionValueInCrowns(new BigDecimal(ctce.getTransactionValue()));
			transaction.setAmount(new BigDecimal(ctce.getAmount()));
			transaction.setType(ctce.getAssetType());

			double transactionValueInDollars = Double.parseDouble(ctce.getTransactionValue()) / forexData.getRates().getCZK() * forexData.getRates().getUSD();

			double stockPriceInDollars = transactionValueInDollars / Double.parseDouble(ctce.getAmount());
			double stockPriceInCrowns = Double.parseDouble(ctce.getTransactionValue()) / Double.parseDouble(ctce.getAmount());

			transaction.setTransactionValueInDollars(BigDecimal.valueOf(transactionValueInDollars));
			transaction.setStockPriceInCrowns(BigDecimal.valueOf(stockPriceInCrowns));
			transaction.setStockPriceInDollars(BigDecimal.valueOf(stockPriceInDollars));
		} else {
			transaction.setAmount(new BigDecimal(ctce.getAmount()));
			transaction.setUserId(userId);
			transaction.setDate(Date.valueOf(ctce.getTransactionDate().substring(0,10)));
			transaction.setType(ctce.getAssetType());

			if (ctce.getCurrency().equals(CurrencyEnum.CZK.name())) {
				transaction.setTransactionValueInCrowns(new BigDecimal(ctce.getTransactionValue()));
				double stockPriceInCrowns = Double.parseDouble(ctce.getTransactionValue()) / Double.parseDouble(ctce.getAmount());
				transaction.setStockPriceInCrowns(BigDecimal.valueOf(stockPriceInCrowns));

			} else {
				transaction.setTransactionValueInDollars(new BigDecimal(ctce.getTransactionValue()));
				double stockPriceInDollars = Double.parseDouble(ctce.getTransactionValue()) / Double.parseDouble(ctce.getAmount());
				transaction.setStockPriceInDollars(BigDecimal.valueOf(stockPriceInDollars));
				transaction.setStockPriceInCrowns(BigDecimal.valueOf(stockPriceInDollars * 21.5)); // TODO: 14.10.2021 nasobit aktualnim kurzem, potrebuji to pok pri vytvareni detailu portfolia
			}

		}
		transactionsRepository.save(transaction);
	}
}
