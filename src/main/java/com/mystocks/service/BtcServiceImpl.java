package com.mystocks.service;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.controller.StockController;
import com.mystocks.dto.*;
import com.mystocks.model.CryptoTransactions;
import com.mystocks.repository.AccountBalanceRepository;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BtcServiceImpl implements BtcService{

	private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

	private static final String DEFAULT_RATE = "0";

	@Autowired
	private AccountBalanceRepository accountBalanceRepository;

	@Autowired
	private CryptoTransactionsRepository cryptoTransactionsRepository;

	@Override
	public CryptoTransactionListEntity getAllTransactions(String userId) {
		List<CryptoTransactions> allByUserId = cryptoTransactionsRepository.findAllByUserId(userId);

		CryptoTransactionListEntity cryptoTransactionListEntity = new CryptoTransactionListEntity();

		Map<BigDecimal, BigDecimal> transactionValuesDollarsMap = allByUserId.stream()
				.collect(Collectors.toMap(CryptoTransactions::getStockPriceInDollars, CryptoTransactions::getTransactionValueInDollars));

		Map<BigDecimal, BigDecimal> transactionValuesCrownsMap = allByUserId.stream()
				.collect(Collectors.toMap(CryptoTransactions::getStockPriceInCrowns, CryptoTransactions::getTransactionValueInCrowns));

		List<CryptoTransactionDto> transactionDtos = allByUserId.stream()
				.map(this::mapTransaction)
				.collect(Collectors.toList());



		cryptoTransactionListEntity.setCryptoTransactions(transactionDtos);
		cryptoTransactionListEntity.setAverageTransactionValueInDollars(MathUtils.weightedAverage(transactionValuesDollarsMap));
		cryptoTransactionListEntity.setAverageTransactionValueInCrowns(MathUtils.weightedAverage(transactionValuesCrownsMap));

		return cryptoTransactionListEntity;
	}

	private CryptoTransactionDto mapTransaction(CryptoTransactions cryptoTransaction) {
		LOGGER.debug("mapping all transactions has started");
		CryptoTransactionDto transactionDto = new CryptoTransactionDto();
		transactionDto.setAmountBtc(cryptoTransaction.getAmount().toString());
		transactionDto.setType(cryptoTransaction.getType());
		transactionDto.setBuySellValue(String.valueOf(cryptoTransaction.getTransactionValueInCrowns()));
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		String strDate = dateFormat.format(cryptoTransaction.getDate());
		transactionDto.setDate(strDate);
		return transactionDto;
	}

	@Override
	public BtcInfoData processBtcData(BtcInfoDto btcInfoDto, String userId) {
		LOGGER.info("processBtcData has started for user {}", userId);

		BtcInfoData btcInfoData = new BtcInfoData();

		List<CryptoTransactions> allByUserId = cryptoTransactionsRepository.findAllByUserId(userId);
		BigDecimal totalAmount = BigDecimal.ZERO;
		if (!allByUserId.isEmpty()) {
			totalAmount = getTotalAmount(allByUserId);
		}

		btcInfoData.setInvestedInCrowns(String.valueOf(getInvestedCrowns(allByUserId)));
		btcInfoData.setBtcBalance(String.valueOf(totalAmount));

		List<BtcBalance> btcRates = new ArrayList<>();

		// USD
		BtcBalance btcBalanceUSD = getBtcBalance(btcInfoDto, totalAmount, CurrencyEnum.USD);
		btcRates.add(btcBalanceUSD);

		//CZK
		BtcBalance btcBalanceCZK = getBtcBalance(btcInfoDto, totalAmount, CurrencyEnum.CZK);
		btcRates.add(btcBalanceCZK);

		btcInfoData.setBtcRates(btcRates);

		LOGGER.info("processBtcData has ended for user {}", userId);
		return btcInfoData;
	}

	private BigDecimal getInvestedCrowns(List<CryptoTransactions> allByUserId) {
		return allByUserId.stream()
				.filter(ct -> ct.getType().equals("btc"))
				.map(CryptoTransactions::getTransactionValueInCrowns)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal getTotalAmount(List<CryptoTransactions> allByUserId) {
		return allByUserId.stream()
				.filter(ct -> ct.getType().equals("btc"))
				.map(CryptoTransactions::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BtcBalance getBtcBalance(BtcInfoDto btcInfoDto, BigDecimal totalAmount, CurrencyEnum currency) {
		BtcBalance btcBalance = new BtcBalance();
		btcBalance.setCurrency(currency.name());
		String rate = btcInfoDto != null ? getRateByCurrency(btcInfoDto, currency) : null;

		String normalizedRate = DEFAULT_RATE;
		if (rate != null) {
			normalizedRate = normalizeRate(rate);
			rate = rate.substring(0, rate.lastIndexOf("."));
		}

		btcBalance.setPrice(rate);
		btcBalance.setAccBalance(String.valueOf(getFinalBalance(totalAmount, normalizedRate)));
		return btcBalance;
	}

	private String getRateByCurrency(BtcInfoDto btcInfoDto, CurrencyEnum currencyEnum) {
		if (currencyEnum == CurrencyEnum.USD) {
			return btcInfoDto.getBpi().getUSD().getRate();
		} else {
			return btcInfoDto.getBpi().getCZK().getRate();
		}
	}

	private String normalizeRate(String rate) {
		return rate.replaceAll(",", "");
	}

	private BigDecimal getFinalBalance(BigDecimal totalAmount, String normalizedRate) {
		BigDecimal v2 = new BigDecimal(normalizedRate);
		BigDecimal result = totalAmount.multiply(v2);
		return result.setScale(0, RoundingMode.HALF_UP);
	}


}
