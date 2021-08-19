package com.mystocks.service;

import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.AssetData;
import com.mystocks.dto.AssetRate;
import com.mystocks.dto.yahoo.Meta;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.enums.AssetType;
import com.mystocks.helper.AssetDataHelper;
import com.mystocks.model.CryptoTransaction;
import com.mystocks.repository.CryptoTransactionsRepository;
import com.mystocks.utils.RetrofitBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SharesServiceImpl implements SharesService{

	private static final Logger LOGGER = LoggerFactory.getLogger(SharesServiceImpl.class);

	private final CryptoTransactionsRepository transactionsRepository;
	final private AssetDataHelper assetDataHelper;

	@Autowired
	public SharesServiceImpl(CryptoTransactionsRepository transactionsRepository) {
		this.transactionsRepository = transactionsRepository;
		this.assetDataHelper = new AssetDataHelper();
	}

	@Override
	public List<AssetData> processSharesAssets(String userId) {
		List<AssetData> result = new ArrayList<>();
		List<CryptoTransaction> allSharesTransactions = getAllSharesTransactions(userId);
		Set<String> sharesCodes = getSharesCodes(allSharesTransactions);

		AssetApiService assetApiService = RetrofitBuilder.assetApiService(ApiConfiguration.API_YAHOO_URL);

		for (String code : sharesCodes) {
			Response<SharesDto> sharesResponse = null;
			Call<SharesDto> retrofitCallShares = assetApiService.getSharesData(code);
			try {
				sharesResponse =  retrofitCallShares.execute();
			} catch (IOException e) {
				// TODO: 10.04.2021 exception mapper
				e.printStackTrace();
			}
			SharesDto sharesDto = sharesResponse != null ? sharesResponse.body() : new SharesDto();
			if (sharesDto != null) {
				String symbol = sharesDto.getMeta().getSymbol();
				LOGGER.info("calling process Shares data has started for user {} and share {}", userId, symbol);
				result.add(processData(sharesDto, userId));
			}
		}
		return result;
	}

	private AssetData processData(SharesDto shares, String userId) {
		AssetData assetData = new AssetData();
		assetData.setType(AssetType.SHARES);

		// result is always ONE - 1 day history
		Meta sharesMeta = shares.getMeta();

		List<CryptoTransaction> allByTypeAndUserId = transactionsRepository.findAllByTypeAndUserId(sharesMeta.getSymbol(), userId);
		BigDecimal totalAmount = assetDataHelper.getTotalAmount(allByTypeAndUserId, sharesMeta.getSymbol());

		assetData.setInvestedInCrowns(String.valueOf(assetDataHelper.getInvestedCrowns(allByTypeAndUserId, sharesMeta.getSymbol())));
		assetData.setAssetBalance(String.valueOf(totalAmount));


		List<AssetRate> shareRates = new ArrayList<>();

		if (sharesMeta.getCurrency().equals(CurrencyEnum.CZK.name())) {
			// CZK
			AssetRate assetRateCZK = assetDataHelper.getShareBalance(shares, totalAmount, CurrencyEnum.CZK);
			shareRates.add(assetRateCZK);
		} else {
			// TODO: 06.07.2021 NON-PRA markets process
		}

		assetData.setAssetBalanceList(shareRates);
		assetData.setSymbol(sharesMeta.getSymbol());
		return assetData;
	}

	private List<CryptoTransaction> getAllSharesTransactions(String userId) {
		List<CryptoTransaction> allByUserId = transactionsRepository.findAllByUserId(userId);
		// TODO: 06.07.2021 je potreba prdat v DB i asset type
		return allByUserId.stream()
				.filter(transaction -> !transaction.getType().equals("btc"))
				.collect(Collectors.toList());
	}

	private Set<String> getSharesCodes(List<CryptoTransaction> allSharesTransactions) {
		return allSharesTransactions.stream()
				.map(CryptoTransaction::getType)
				.collect(Collectors.toSet());
	}

}
