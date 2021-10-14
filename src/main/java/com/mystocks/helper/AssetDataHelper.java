package com.mystocks.helper;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.AssetRate;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.enums.AssetType;
import com.mystocks.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class AssetDataHelper {

	/**
	 * sums all values defined by given function
	 * e.g. Transaction::getTotalAmount
	 * e.g. Transaction::getInvestedInCrowns
	 * @param allByUserId - all transactions by specific user
	 * @return sum
	 */
	public BigDecimal getTotal(List<Transaction> allByUserId, String type, Function<Transaction, BigDecimal> function) {
		return allByUserId.stream()
				.filter(ct -> ct.getCode().equals(type))
				.map(function)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * transforms btc information into BtcBalance object
	 * @param btcInfoDto all information stored fetched about current price of BTC
	 * @param totalAmount sum of purchased BTC
	 * @param currency type of currency
	 * @return transformed info about BTC
	 */
	public AssetRate getBtcBalance(BtcInfoDto btcInfoDto, BigDecimal totalAmount, CurrencyEnum currency) {
		AssetRate assetRate = new AssetRate();
		assetRate.setCurrency(currency.name());
		String rate = getBtcRateByCurrency(btcInfoDto, currency);

		String normalizedRate = normalizeRate(rate);
		rate = rate.substring(0, rate.lastIndexOf("."));
		assetRate.setPrice(rate);

		assetRate.setAccBalance(String.valueOf(getFinalBalance(totalAmount, normalizedRate, AssetType.CRYPTO)));
		return assetRate;
	}


	public AssetRate getShareBalance(SharesDto sharesDto, BigDecimal totalAmount, CurrencyEnum currency) {
		AssetRate assetRate = new AssetRate();
		assetRate.setCurrency(currency.name());

		String rate = String.valueOf(sharesDto.getMeta().getRegularMarketPrice());
		String normalizedRate = normalizeRate(rate);
		assetRate.setPrice(rate);

		assetRate.setAccBalance(String.valueOf(getFinalBalance(totalAmount, normalizedRate, AssetType.SHARES)));

		return assetRate;
	}

	private String getBtcRateByCurrency(BtcInfoDto btcInfoDto, CurrencyEnum currencyEnum) {
		if (currencyEnum == CurrencyEnum.USD) {
			return btcInfoDto.getBpi().getUSD().getRate();
		} else {
			return btcInfoDto.getBpi().getCZK().getRate();
		}
	}

	private String normalizeRate(String rate) {
		return rate.replaceAll(",", "");
	}

	private BigDecimal getFinalBalance(BigDecimal totalAmount, String normalizedRate, AssetType assetType) {
		BigDecimal v2 = new BigDecimal(normalizedRate);
		BigDecimal result = totalAmount.multiply(v2);
		//BTC zaokrouhlim, protoze u takove castky nemaji pro me u desetinne mista vyznam
		if (assetType.equals(AssetType.CRYPTO)) {
			return result.setScale(0, RoundingMode.HALF_UP);
		} else {
			return result;
		}

	}
}
