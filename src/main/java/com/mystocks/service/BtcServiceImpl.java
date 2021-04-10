package com.mystocks.service;

import com.mystocks.dto.BtcInfoData;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.model.AccountBalance;
import com.mystocks.repository.AccountBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

		List<AccountBalance> allAccountBalances = accountBalanceRepository.findAll();
		Optional<AccountBalance> foundBalance = findAccBalance(userId, allAccountBalances);
		AccountBalance accountBalance = foundBalance.orElseGet(AccountBalance::new);

		btcInfoData.setBtcBalance(accountBalance.getBtc());
		String rate = btcInfoDto != null ? btcInfoDto.getBpi().getUSD().getRate() : null;

		String normalizedRate = DEFAULT_RATE;
		if (rate != null) {
			normalizedRate = normalizeRate(rate);
		}
		btcInfoData.setPriceInDollars(rate);
		btcInfoData.setAccBalance(String.valueOf(getFinalBalance(accountBalance, normalizedRate)));

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
