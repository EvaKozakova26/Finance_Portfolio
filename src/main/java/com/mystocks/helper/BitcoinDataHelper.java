package com.mystocks.helper;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.BtcBalance;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.model.CryptoTransactions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BitcoinDataHelper {

	private static final String DEFAULT_RATE = "0";

	/**
	 * sums all transactions values in crowns
	 * @param allByUserId - all transactions by specific user
	 * @return sum
	 */
	public BigDecimal getInvestedCrowns(List<CryptoTransactions> allByUserId) {
		return allByUserId.stream()
				.filter(ct -> ct.getType().equals("btc"))
				.map(CryptoTransactions::getTransactionValueInCrowns)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * sums all purchased bitcoins
	 * @param allByUserId - all transactions by specific user
	 * @return sum
	 */
	public BigDecimal getTotalAmount(List<CryptoTransactions> allByUserId) {
		return allByUserId.stream()
				.filter(ct -> ct.getType().equals("btc"))
				.map(CryptoTransactions::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * transforms btc information into BtcBalance object
	 * @param btcInfoDto all information stored fetched about current price of BTC
	 * @param totalAmount sum of purchased BTC
	 * @param currency type of currency
	 * @return transformed info about BTC
	 */
	public BtcBalance getBtcBalance(BtcInfoDto btcInfoDto, BigDecimal totalAmount, CurrencyEnum currency) {
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
