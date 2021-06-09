package com.mystocks.service;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.controller.StockController;
import com.mystocks.dto.*;
import com.mystocks.helper.BitcoinDataHelper;
import com.mystocks.model.CryptoTransaction;
import com.mystocks.repository.CryptoTransactionsRepository;
import com.mystocks.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BtcServiceImpl implements BtcService{

	private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

	final private CryptoTransactionsRepository cryptoTransactionsRepository;
	final private BitcoinDataHelper bitcoinDataHelper;

	@Autowired
	public BtcServiceImpl(CryptoTransactionsRepository cryptoTransactionsRepository) {
		this.cryptoTransactionsRepository = cryptoTransactionsRepository;
		this.bitcoinDataHelper = new BitcoinDataHelper();
	}

	@Override
	public CryptoTransactionListEntity getAllTransactions(String userId) {
		List<CryptoTransaction> allByUserId = cryptoTransactionsRepository.findAllByUserId(userId);

		allByUserId.sort(Comparator.comparing(CryptoTransaction::getDate).reversed());

		CryptoTransactionListEntity cryptoTransactionListEntity = new CryptoTransactionListEntity();

		Map<BigDecimal, BigDecimal> transactionValuesDollarsMap = allByUserId.stream()
				.collect(Collectors.toMap(CryptoTransaction::getStockPriceInDollars, CryptoTransaction::getTransactionValueInDollars));

		Map<BigDecimal, BigDecimal> transactionValuesCrownsMap = allByUserId.stream()
				.collect(Collectors.toMap(CryptoTransaction::getStockPriceInCrowns, CryptoTransaction::getTransactionValueInCrowns));

		List<CryptoTransactionDto> transactionDtos = allByUserId.stream()
				.map(this::mapTransaction)
				.collect(Collectors.toList());

		cryptoTransactionListEntity.setCryptoTransactions(transactionDtos);
		cryptoTransactionListEntity.setAverageTransactionValueInDollars(MathUtils.weightedAverage(transactionValuesDollarsMap));
		cryptoTransactionListEntity.setAverageTransactionValueInCrowns(MathUtils.weightedAverage(transactionValuesCrownsMap));

		return cryptoTransactionListEntity;
	}

	private CryptoTransactionDto mapTransaction(CryptoTransaction cryptoTransaction) {
		LOGGER.debug("mapping all transactions has started");
		CryptoTransactionDto transactionDto = new CryptoTransactionDto();
		transactionDto.setAmountBtc(cryptoTransaction.getAmount().toString());
		transactionDto.setType(cryptoTransaction.getType());
		transactionDto.setBuySellValue(String.valueOf(cryptoTransaction.getTransactionValueInCrowns().setScale(0, RoundingMode.HALF_UP)));
		transactionDto.setBuySellValueInDollars(String.valueOf(cryptoTransaction.getTransactionValueInDollars().setScale(0, RoundingMode.HALF_UP)));
		transactionDto.setStockPriceInCrowns(String.valueOf(cryptoTransaction.getStockPriceInCrowns().setScale(0, RoundingMode.HALF_UP)));
		transactionDto.setStockPriceInDollars(String.valueOf(cryptoTransaction.getStockPriceInDollars().setScale(0, RoundingMode.HALF_UP)));
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		String strDate = dateFormat.format(cryptoTransaction.getDate());
		transactionDto.setDate(strDate);
		return transactionDto;
	}

	@Override
	public BtcInfoData processBtcData(BtcInfoDto btcInfoDto, String userId) {
		LOGGER.info("processBtcData has started for user {}", userId);

		BtcInfoData btcInfoData = new BtcInfoData();

		List<CryptoTransaction> allByUserId = cryptoTransactionsRepository.findAllByUserId(userId);
		BigDecimal totalAmount = BigDecimal.ZERO;
		if (!allByUserId.isEmpty()) {
			totalAmount = bitcoinDataHelper.getTotalAmount(allByUserId);
		}

		btcInfoData.setInvestedInCrowns(String.valueOf(bitcoinDataHelper.getInvestedCrowns(allByUserId)));
		btcInfoData.setBtcBalance(String.valueOf(totalAmount));

		List<BtcBalance> btcRates = new ArrayList<>();

		// USD
		BtcBalance btcBalanceUSD = bitcoinDataHelper.getBtcBalance(btcInfoDto, totalAmount, CurrencyEnum.USD);
		btcRates.add(btcBalanceUSD);

		//CZK
		BtcBalance btcBalanceCZK = bitcoinDataHelper.getBtcBalance(btcInfoDto, totalAmount, CurrencyEnum.CZK);
		btcRates.add(btcBalanceCZK);

		btcInfoData.setBtcRates(btcRates);

		LOGGER.info("processBtcData has ended for user {}", userId);
		return btcInfoData;
	}

}
