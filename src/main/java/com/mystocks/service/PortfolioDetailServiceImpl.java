package com.mystocks.service;

import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.*;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.enums.AssetName;
import com.mystocks.helper.AssetDataHelper;
import com.mystocks.model.CryptoTransaction;
import com.mystocks.repository.CryptoTransactionsRepository;
import com.mystocks.utils.RetrofitBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PortfolioDetailServiceImpl implements PortfolioDetailService {

	private final CryptoTransactionsRepository transactionsRepository;
	private final AssetService assetService;

	@Autowired
	public PortfolioDetailServiceImpl(CryptoTransactionsRepository transactionsRepository, AssetService assetService) {
		this.transactionsRepository = transactionsRepository;
		this.assetService = assetService;
	}

	@Override
	public PortfolioDetailListEntity getPortfolioDetail(String userId) {
		PortfolioDetailListEntity result = new PortfolioDetailListEntity();

		List<CryptoTransaction> allByUserId = transactionsRepository.findAllByUserId(userId);

		AssetDataListEntity assetData = assetService.getAssetData(userId);

		BigDecimal totalInvested = allByUserId.stream().map(CryptoTransaction::getTransactionValueInCrowns).reduce(BigDecimal.ZERO, BigDecimal::add);


		Map<String, List<CryptoTransaction>> mapBySymbol = allByUserId.stream()
				.collect(Collectors.groupingBy(CryptoTransaction::getType));


		AssetApiService assetApiService = RetrofitBuilder.assetApiService(ApiConfiguration.API_YAHOO_URL);
		Response<SharesDto> sharesResponse = null;
		Call<SharesDto> retrofitCallShares = assetApiService.getSharesData("USDCZK=X");
		try {
			sharesResponse =  retrofitCallShares.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}
		SharesDto sharesDto = sharesResponse != null ? sharesResponse.body() : new SharesDto();

		float czkToUsd = sharesDto.getMeta().getRegularMarketPrice();

		List<AssetData> filteredAssets = assetData.getAssetData().stream().filter(assetData1 -> !assetData1.getSymbol().equals("btc")).collect(Collectors.toList());
		List<AssetRate> collect = filteredAssets.stream().map(AssetData::getAssetBalanceList).flatMap(List::stream).collect(Collectors.toList());
		BigDecimal resultValue = new BigDecimal(0);
		for (AssetRate rate : collect) {
			BigDecimal temp = BigDecimal.valueOf(Double.parseDouble(rate.getAccBalance()));
			if (rate.getCurrency().equals(CurrencyEnum.USD.name())) {
				resultValue = resultValue.add(temp.multiply(BigDecimal.valueOf(czkToUsd)));
			} else {
				resultValue = resultValue.add(temp);
			}
		}

		// btc prepocet TODO smazat, az to sjednotim s yahoo
		Optional<AssetData> btc = assetData.getAssetData().stream().filter(assetData1 -> assetData1.getSymbol().equals("btc")).findFirst();
		if (btc.isPresent()) {
			List<AssetRate> assetBalanceList = btc.get().getAssetBalanceList();
			Optional<AssetRate> first = assetBalanceList.stream().filter(assetRate -> assetRate.getCurrency().equals(CurrencyEnum.CZK.name())).findFirst();
			if (first.isPresent()) {
				resultValue = resultValue.add(BigDecimal.valueOf(Double.parseDouble(first.get().getAccBalance())));
			}
		}

		BigDecimal currentMarketCap = resultValue;


		for (Map.Entry<String, List<CryptoTransaction>> entry : mapBySymbol.entrySet()) {
			AssetData assetDataForSymbol = assetData.getAssetData().stream()
					.filter(data -> data.getSymbol().equals(entry.getKey()))
					.findFirst()
					.get();

			BigDecimal currentMarketValue;
			if (AssetName.US_EXCHANGE.contains(assetDataForSymbol.getSymbol()) || AssetName.CRYPTO_EXCHANGE.contains(assetDataForSymbol.getSymbol())) {
				BigDecimal marketValue = BigDecimal.valueOf(Double.parseDouble(assetDataForSymbol.getAssetBalanceList().get(0).getAccBalance()));
				currentMarketValue = marketValue.multiply(BigDecimal.valueOf(czkToUsd));
			} else {
				currentMarketValue = BigDecimal.valueOf(Double.parseDouble(assetDataForSymbol.getAssetBalanceList().get(0).getAccBalance()));
			}

			PortfolioDetail portfolioDetail = createPortfolioDetail(entry.getValue(), entry.getKey(), totalInvested, currentMarketCap, currentMarketValue, assetDataForSymbol.getAssetBalanceList().get(0).getAccBalance());
			result.addDetail(portfolioDetail);
		}

		return result;
	}

	private PortfolioDetail createPortfolioDetail(List<CryptoTransaction> transactions, String symbol, BigDecimal totalInvested, BigDecimal currentMarketValue, BigDecimal currentMarketCap, String accBalance) {
		PortfolioDetail portfolioDetail = new PortfolioDetail();
		portfolioDetail.setSymbol(symbol);
		portfolioDetail.setFullName(AssetName.values.get(symbol));
		portfolioDetail.setSharePercentageHistoric(getPercentage(transactions, symbol, totalInvested));
		portfolioDetail.setSharePercentageCurrent(getPercentageCurrent(accBalance, symbol, currentMarketValue, currentMarketCap));
		return portfolioDetail;

	}

	private String getPercentageCurrent(String accBalance, String symbol, BigDecimal marketValue, BigDecimal currentMarketCap) {
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
