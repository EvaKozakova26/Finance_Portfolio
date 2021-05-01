package com.mystocks.service;

import com.mystocks.dto.BtcBalance;
import com.mystocks.dto.BtcInfoData;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.model.AccountBalance;
import com.mystocks.repository.AccountBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BtcServiceImpl implements BtcService{

	private static final String DEFAULT_RATE = "0";

	@Autowired
	private AccountBalanceRepository accountBalanceRepository;

	@Override
	public BtcInfoData processBtcData(BtcInfoDto btcInfoDto, String userId) {
		BtcInfoData btcInfoData = new BtcInfoData();

		// TODO: 30.04.2021 rovnou volat podle ID
		List<AccountBalance> allAccountBalances = accountBalanceRepository.findAll();
		Optional<AccountBalance> foundBalance = findAccBalance(userId, allAccountBalances);
		AccountBalance accountBalance = foundBalance.orElseGet(AccountBalance::new);

		btcInfoData.setBtcBalance(accountBalance.getBtc());

		// TODO: 01.05.2021 refactor

		List<BtcBalance> btcRates = new ArrayList<>();
		// USD
		BtcBalance btcBalanceUSD = new BtcBalance();
		btcBalanceUSD.setCurrency("USD");
		String rate = btcInfoDto != null ? btcInfoDto.getBpi().getUSD().getRate() : null;

		String normalizedRate = DEFAULT_RATE;
		if (rate != null) {
			normalizedRate = normalizeRate(rate);
		}
		btcBalanceUSD.setPrice(rate);
		btcBalanceUSD.setAccBalance(String.valueOf(getFinalBalance(accountBalance, normalizedRate)));
		btcRates.add(btcBalanceUSD);

		//CZK
		BtcBalance btcBalanceCZK = new BtcBalance();
		btcBalanceCZK.setCurrency("CZK");
		String rateCZK = btcInfoDto != null ? btcInfoDto.getBpi().getCZK().getRate() : null;

		String normalizedRateCZK = DEFAULT_RATE;
		if (rateCZK != null) {
			normalizedRateCZK = normalizeRate(rateCZK);
		}
		btcBalanceCZK.setPrice(rateCZK);
		btcBalanceCZK.setAccBalance(String.valueOf(getFinalBalance(accountBalance, normalizedRateCZK)));
		btcRates.add(btcBalanceCZK);

		btcInfoData.setBtcRates(btcRates);
		return btcInfoData;
	}

	private String normalizeRate(String rate) {
		return rate.replaceAll(",", "");
	}

	private Optional<AccountBalance> findAccBalance(String userId, List<AccountBalance> allAccountBalances) {
		return allAccountBalances.stream()
				.filter(accountBalance -> accountBalance.getUserId().equals(userId))
				.findFirst();
	}

	private BigDecimal getFinalBalance(AccountBalance accountBalance, String normalizedRate) {
		BigDecimal v1 = new BigDecimal(accountBalance.getBtc());
		BigDecimal v2 = new BigDecimal(normalizedRate);
		BigDecimal result = v1.multiply(v2);
		return result.setScale(1, RoundingMode.HALF_UP);
	}


}
