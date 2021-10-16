package com.mystocks.service;

import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.TransactionCreateEntity;
import com.mystocks.dto.TransactionDto;
import com.mystocks.dto.TransactionListEntity;
import com.mystocks.enums.AssetType;
import com.mystocks.model.Transaction;
import com.mystocks.repository.TransactionsRepository;
import com.mystocks.utils.RetrofitBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionServiceImpl implements TransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

	private final TransactionsRepository transactionsRepository;

	@Autowired
	public TransactionServiceImpl(TransactionsRepository transactionsRepository) {
		this.transactionsRepository = transactionsRepository;
	}

	@Override
	public TransactionListEntity getAllTransactions(String userId) {

		List<Transaction> allByUserId = transactionsRepository.findAllByUserId(userId);

		allByUserId.sort(Comparator.comparing(Transaction::getDate).reversed());

		TransactionListEntity transactionListEntity = new TransactionListEntity();
		List<TransactionDto> transactionDtos = getMappedTransactions(allByUserId);
		transactionListEntity.setTransactions(transactionDtos);
		// to be deleted
		transactionListEntity.setAverageTransactionValueInDollars(0.0);
		transactionListEntity.setAverageTransactionValueInCrowns(0.0);

		LOGGER.info("getAllTransactions has ended with result size: {}", transactionListEntity.getTransactions().size());
		return transactionListEntity;
	}

	private List<TransactionDto> getMappedTransactions(List<Transaction> allByUserId) {
		return allByUserId.stream()
				.map(this::mapTransaction)
				.collect(Collectors.toList());
	}

	private TransactionDto mapTransaction(Transaction transaction) {
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAmount(transaction.getAmount().toString());
		transactionDto.setType(transaction.getType());
		transactionDto.setCode(transaction.getCode());
		transactionDto.setBuySellValue(String.valueOf(transaction.getTransactionValueInCrowns().setScale(0, RoundingMode.HALF_UP)));
		transactionDto.setBuySellValueInDollars(String.valueOf(transaction.getTransactionValueInDollars().setScale(0, RoundingMode.HALF_UP)));
		transactionDto.setStockPriceInCrowns(String.valueOf(transaction.getStockPriceInCrowns().setScale(0, RoundingMode.HALF_UP)));
		transactionDto.setStockPriceInDollars(String.valueOf(transaction.getStockPriceInDollars().setScale(0, RoundingMode.HALF_UP)));
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		String strDate = dateFormat.format(transaction.getDate());
		transactionDto.setDate(strDate);
		return transactionDto;
	}

	@Override
	public void createTransaction(TransactionCreateEntity ctce, String userId) {
		Response<ForexDataDto> response = null;
		AssetApiService assetApiService = RetrofitBuilder.assetApiService(ApiConfiguration.API_FOREX_URL);
		Call<ForexDataDto> retrofitCall = assetApiService.getForexData(ctce.getTransactionDate().substring(0, 10));

		try {
			response = retrofitCall.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}

		ForexDataDto forexData = response != null ? response.body() : new ForexDataDto();
		Transaction transaction = new Transaction();

		LOGGER.info("creating transaction data");
		if (ctce.getAssetType().equalsIgnoreCase("btc")) {
			if (forexData != null) {
				// TODO: 14.10.2021 musim jeste vymyslet, jak ukladat btc
				transaction = createTransactionDataBtc(ctce, userId, forexData);
			} else {
				LOGGER.error("Cannot find UDS rate on Forex API, transaction for BTC could not be saved");
			}
		} else {
			transaction = createTransactionShares(ctce, userId);
		}


		Transaction savedTr = transactionsRepository.save(transaction);
		LOGGER.info("Transaction has been saved, id: {}", savedTr.getId());
	}

	private Transaction createTransactionShares(TransactionCreateEntity ctce, String userId) {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(ctce.getAmount()));
		transaction.setUserId(userId);
		transaction.setDate(Date.valueOf(ctce.getTransactionDate().substring(0, 10)));
		transaction.setType(AssetType.SHARES.name());
		transaction.setCode(ctce.getAssetType());

		if (ctce.getCurrency().equals(CurrencyEnum.CZK.name())) {
			transaction.setTransactionValueInCrowns(new BigDecimal(ctce.getTransactionValue()));
			double stockPriceInCrowns = Double.parseDouble(ctce.getTransactionValue()) / Double.parseDouble(ctce.getAmount());
			transaction.setStockPriceInCrowns(BigDecimal.valueOf(stockPriceInCrowns));

		} else {
			BigDecimal trVal = new BigDecimal(ctce.getTransactionValue());
			transaction.setTransactionValueInDollars(trVal);
			double stockPriceInDollars = Double.parseDouble(ctce.getTransactionValue()) / Double.parseDouble(ctce.getAmount());
			transaction.setStockPriceInDollars(BigDecimal.valueOf(stockPriceInDollars));
			transaction.setTransactionValueInCrowns(new BigDecimal("21.5").multiply(trVal)); // TODO: 14.10.2021 nasobit aktualnim kurzem, potrebuji to pok pri vytvareni detailu portfolia
		}
		return transaction;
	}

	private Transaction createTransactionDataBtc(TransactionCreateEntity ctce, String userId, ForexDataDto forexData) {
		Transaction transaction = new Transaction();
		transaction.setUserId(userId);
		transaction.setDate(Date.valueOf(ctce.getTransactionDate().substring(0, 10)));
		transaction.setTransactionValueInCrowns(new BigDecimal(ctce.getTransactionValue()));
		transaction.setAmount(new BigDecimal(ctce.getAmount()));
		transaction.setType(AssetType.CRYPTO.name());
		transaction.setCode(ctce.getAssetType());

		double transactionValueInDollars = Double.parseDouble(ctce.getTransactionValue()) / forexData.getRates().getCZK() * forexData.getRates().getUSD();

		double stockPriceInDollars = transactionValueInDollars / Double.parseDouble(ctce.getAmount());
		double stockPriceInCrowns = Double.parseDouble(ctce.getTransactionValue()) / Double.parseDouble(ctce.getAmount());

		transaction.setTransactionValueInDollars(BigDecimal.valueOf(transactionValueInDollars));
		transaction.setStockPriceInCrowns(BigDecimal.valueOf(stockPriceInCrowns));
		transaction.setStockPriceInDollars(BigDecimal.valueOf(stockPriceInDollars));
		return transaction;
	}
}
