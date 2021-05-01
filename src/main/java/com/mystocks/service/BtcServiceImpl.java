package com.mystocks.service;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.controller.StockController;
import com.mystocks.dto.*;
import com.mystocks.model.AccountBalance;
import com.mystocks.repository.AccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BtcServiceImpl implements BtcService{

	private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

	private static final String DEFAULT_RATE = "0";

	@Autowired
	private AccountBalanceRepository accountBalanceRepository;

	@Override
	public BtcInfoData processBtcData(BtcInfoDto btcInfoDto, String userId) {
		LOGGER.info("processBtcData has started for user {}", userId);

		BtcInfoData btcInfoData = new BtcInfoData();

		Optional<AccountBalance> foundBalance = accountBalanceRepository.findByUserId(userId);
		// TODO: 01.05.2021 throw or log exception orElseGet
		AccountBalance accountBalance = foundBalance.orElseGet(AccountBalance::new);
		btcInfoData.setBtcBalance(accountBalance.getBtc());

		List<BtcBalance> btcRates = new ArrayList<>();

		// USD
		BtcBalance btcBalanceUSD = getBtcBalance(btcInfoDto, accountBalance, CurrencyEnum.USD);
		btcRates.add(btcBalanceUSD);

		//CZK
		BtcBalance btcBalanceCZK = getBtcBalance(btcInfoDto, accountBalance, CurrencyEnum.CZK);
		btcRates.add(btcBalanceCZK);

		btcInfoData.setBtcRates(btcRates);

		LOGGER.info("processBtcData has ended for user {}", userId);
		return btcInfoData;
	}

	private BtcBalance getBtcBalance(BtcInfoDto btcInfoDto, AccountBalance accountBalance, CurrencyEnum currency) {
		BtcBalance btcBalance = new BtcBalance();
		btcBalance.setCurrency(currency.name());
		String rate = btcInfoDto != null ? getRateByCurrency(btcInfoDto, currency) : null;

		String normalizedRate = DEFAULT_RATE;
		if (rate != null) {
			normalizedRate = normalizeRate(rate);
		}
		btcBalance.setPrice(rate);
		btcBalance.setAccBalance(String.valueOf(getFinalBalance(accountBalance, normalizedRate)));
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

	private BigDecimal getFinalBalance(AccountBalance accountBalance, String normalizedRate) {
		BigDecimal v1 = new BigDecimal(accountBalance.getBtc());
		BigDecimal v2 = new BigDecimal(normalizedRate);
		BigDecimal result = v1.multiply(v2);
		return result.setScale(1, RoundingMode.HALF_UP);
	}


}
