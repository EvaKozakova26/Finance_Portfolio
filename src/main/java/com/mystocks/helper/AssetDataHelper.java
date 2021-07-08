package com.mystocks.helper;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.AssetRate;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.enums.AssetType;
import com.mystocks.model.CryptoTransaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class AssetDataHelper {

	private static final String DEFAULT_RATE = "0";

	/**
	 * sums all transactions values in crowns
	 * @param allByUserId - all transactions by specific user
	 * @return sum
	 */
	public BigDecimal getInvestedCrowns(List<CryptoTransaction> allByUserId, String type) {
		return allByUserId.stream()
				.filter(ct -> ct.getType().equals(type))
				.map(CryptoTransaction::getTransactionValueInCrowns)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * sums all purchased assets
	 * @param allByUserId - all transactions by specific user
	 * @return sum
	 */
	public BigDecimal getTotalAmount(List<CryptoTransaction> allByUserId, String type) {
		return allByUserId.stream()
				.filter(ct -> ct.getType().equals(type))
				.map(CryptoTransaction::getAmount)
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
		String rate = btcInfoDto != null ? getBtcRateByCurrency(btcInfoDto, currency) : null;

		String normalizedRate = DEFAULT_RATE;
		if (rate != null) {
			normalizedRate = normalizeRate(rate);
			rate = rate.substring(0, rate.lastIndexOf("."));
		}

		assetRate.setPrice(rate);
		assetRate.setAccBalance(String.valueOf(getFinalBalance(totalAmount, normalizedRate, AssetType.CRYPTO)));
		return assetRate;
	}


	public AssetRate getShareBalance(SharesDto sharesDto, BigDecimal totalAmount, CurrencyEnum currency) {
		AssetRate assetRate = new AssetRate();
		assetRate.setCurrency(currency.name());
		String rate = sharesDto != null ? getShareRateByCurrency(sharesDto, currency) : null;

		String normalizedRate = DEFAULT_RATE;
		if (rate != null) {
			normalizedRate = normalizeRate(rate);
		}

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

	private String getShareRateByCurrency(SharesDto sharesDto, CurrencyEnum currencyEnum) {
		if (currencyEnum == CurrencyEnum.USD) {
			return "sharesDto.getBpi().getUSD().getRate()";
		} else {
			return String.valueOf(sharesDto.getChart().getResult().get(0).getMeta().getRegularMarketPrice());
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
