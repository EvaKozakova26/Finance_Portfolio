package com.mystocks.helper;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.*;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.enums.AssetName;
import com.mystocks.model.CryptoTransaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PortfolioDetailHelper {

	public static final String BTC = "btc";

	public PortfolioDetailListEntity createPortfolioDetail(SharesDto sharesDto, AssetDataListEntity assetData, List<CryptoTransaction> allByUserId) {
		PortfolioDetailListEntity result = new PortfolioDetailListEntity();

		float czkToUsd = sharesDto.getMeta().getRegularMarketPrice();

		List<AssetData> filteredAssets = filterAssetData(assetData);
		List<AssetRate> assetRates = collectAssetRates(filteredAssets);

		BigDecimal currentMarketCap = getCurrentMarketCap(assetData, czkToUsd, assetRates);
		Map<String, List<CryptoTransaction>> mapBySymbol = getMapBySymbol(allByUserId);
		for (Map.Entry<String, List<CryptoTransaction>> entry : mapBySymbol.entrySet()) {
			result.addDetail(getPortfolioDetail(allByUserId, assetData, czkToUsd, currentMarketCap, entry));
		}
		return result;
	}

	private BigDecimal getCurrentMarketCap(AssetDataListEntity assetData, float czkToUsd, List<AssetRate> assetRates) {
		BigDecimal currentMarketCap = new BigDecimal(0);
		for (AssetRate rate : assetRates) {
			BigDecimal temp = BigDecimal.valueOf(Double.parseDouble(rate.getAccBalance()));
			if (rate.getCurrency().equals(CurrencyEnum.USD.name())) {
				currentMarketCap = currentMarketCap.add(temp.multiply(BigDecimal.valueOf(czkToUsd)));
			} else {
				currentMarketCap = currentMarketCap.add(temp);
			}
		}
		return getResultValue(assetData, currentMarketCap);
	}

	private BigDecimal getResultValue(AssetDataListEntity assetData, BigDecimal resultValue) {
		// btc prepocet TODO smazat, az to sjednotim s yahoo
		Optional<AssetData> btcAssetData = getBtcAssetData(assetData);
		if (btcAssetData.isPresent()) {
			List<AssetRate> assetBalanceList = btcAssetData.get().getAssetBalanceList();
			Optional<AssetRate> czkAssetRate = getCzkAssetRate(assetBalanceList);
			if (czkAssetRate.isPresent()) {
				resultValue = resultValue.add(BigDecimal.valueOf(Double.parseDouble(czkAssetRate.get().getAccBalance())));
			}
		}
		return resultValue;
	}

	private PortfolioDetail getPortfolioDetail(List<CryptoTransaction> allByUserId, AssetDataListEntity assetData, float czkToUsd, BigDecimal currentMarketCap, Map.Entry<String, List<CryptoTransaction>> entry) {
		AssetData assetDataForSymbol = getAssetDataForSymbol(assetData, entry);

		BigDecimal currentMarketValue;
		if (AssetName.US_EXCHANGE.contains(assetDataForSymbol.getSymbol()) || AssetName.CRYPTO_EXCHANGE.contains(assetDataForSymbol.getSymbol())) {
			BigDecimal marketValue = BigDecimal.valueOf(Double.parseDouble(assetDataForSymbol.getAssetBalanceList().get(0).getAccBalance()));
			currentMarketValue = marketValue.multiply(BigDecimal.valueOf(czkToUsd));
		} else {
			currentMarketValue = BigDecimal.valueOf(Double.parseDouble(assetDataForSymbol.getAssetBalanceList().get(0).getAccBalance()));
		}

		BigDecimal totalInvested = getTotalInvested(allByUserId);
		return createPortfolioDetail(entry.getValue(), entry.getKey(), totalInvested, currentMarketCap, currentMarketValue);
	}

	private AssetData getAssetDataForSymbol(AssetDataListEntity assetData, Map.Entry<String, List<CryptoTransaction>> entry) {
		Optional<AssetData> data = assetData.getAssetData().stream()
				.filter(d -> d.getSymbol().equals(entry.getKey()))
				.findFirst();
		return data.orElse(null); // todo vrace prazdny objekt a ne null!!

	}

	private Optional<AssetRate> getCzkAssetRate(List<AssetRate> assetBalanceList) {
		return assetBalanceList.stream()
				.filter(assetRate -> assetRate.getCurrency().equals(CurrencyEnum.CZK.name()))
				.findFirst();
	}

	private Optional<AssetData> getBtcAssetData(AssetDataListEntity assetData) {
		return assetData.getAssetData().stream()
				.filter(assetData1 -> assetData1.getSymbol().equals(BTC))
				.findFirst();
	}

	private List<AssetRate> collectAssetRates(List<AssetData> filteredAssets) {
		return filteredAssets.stream()
				.map(AssetData::getAssetBalanceList)
				.flatMap(List::stream).collect(Collectors.toList());
	}

	private List<AssetData> filterAssetData(AssetDataListEntity assetData) {
		// need to get all assets but BTC
		return assetData.getAssetData().stream()
				.filter(data -> !data.getSymbol().equals(BTC))
				.collect(Collectors.toList());
	}

	private Map<String, List<CryptoTransaction>> getMapBySymbol(List<CryptoTransaction> allByUserId) {
		return allByUserId.stream()
				.collect(Collectors.groupingBy(CryptoTransaction::getType));
	}

	private BigDecimal getTotalInvested(List<CryptoTransaction> allByUserId) {
		return allByUserId.stream()
				.map(CryptoTransaction::getTransactionValueInCrowns)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private PortfolioDetail createPortfolioDetail(List<CryptoTransaction> transactions, String symbol, BigDecimal totalInvested, BigDecimal currentMarketValue, BigDecimal currentMarketCap) {
		PortfolioDetail portfolioDetail = new PortfolioDetail();
		portfolioDetail.setSymbol(symbol);
		portfolioDetail.setFullName(AssetName.values.get(symbol));
		portfolioDetail.setSharePercentageHistoric(getPercentage(transactions, symbol, totalInvested));
		portfolioDetail.setSharePercentageCurrent(getPercentageCurrent(currentMarketValue, currentMarketCap));
		return portfolioDetail;

	}

	private String getPercentageCurrent(BigDecimal marketValue, BigDecimal currentMarketCap) {
		BigDecimal multiply = currentMarketCap.multiply(BigDecimal.valueOf(100));
		BigDecimal sharePercentage = multiply.divide(marketValue, RoundingMode.HALF_UP).setScale(2,RoundingMode.HALF_UP);
		return String.valueOf(sharePercentage);
	}

	private String getPercentage(List<CryptoTransaction> transactions, String symbol, BigDecimal totalInvested) {
		BigDecimal totalForSymbol = new AssetDataHelper().getTotal(transactions, symbol, CryptoTransaction::getTransactionValueInCrowns);
		BigDecimal multiply = totalForSymbol.multiply(BigDecimal.valueOf(100));
		BigDecimal sharePercentage = multiply.divide(totalInvested, RoundingMode.HALF_UP).setScale(2,RoundingMode.HALF_UP);
		return String.valueOf(sharePercentage);
	}

}
