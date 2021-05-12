package com.mystocks.service;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.controller.StockController;
import com.mystocks.dto.*;
import com.mystocks.model.AccountBalance;
import com.mystocks.model.CryptoTransactions;
import com.mystocks.repository.AccountBalanceRepository;
import com.mystocks.repository.CryptoTransactionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BtcServiceImpl implements BtcService{

	public static final String REGEX = ",";
	private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

	private static final String DEFAULT_RATE = "0";

	@Autowired
	private AccountBalanceRepository accountBalanceRepository;

	@Autowired
	private CryptoTransactionsRepository cryptoTransactionsRepository;

	@Override
	public BtcInfoData processBtcData(BtcInfoDto btcInfoDto, String userId) {
		LOGGER.info("processBtcData has started for user {}", userId);

		BtcInfoData btcInfoData = new BtcInfoData();

		List<CryptoTransactions> allByUserId = cryptoTransactionsRepository.findAllByUserId(userId);
		BigDecimal totalAmount = BigDecimal.ZERO;
		if (!allByUserId.isEmpty()) {
			totalAmount = getTotalAmount(allByUserId);
		}

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
		return result.setScale(1, RoundingMode.HALF_UP);
	}


}
