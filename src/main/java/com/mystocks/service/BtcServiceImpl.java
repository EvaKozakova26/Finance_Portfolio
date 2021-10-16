package com.mystocks.service;

import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.AssetData;
import com.mystocks.dto.AssetRate;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.enums.AssetType;
import com.mystocks.helper.AssetDataHelper;
import com.mystocks.model.Transaction;
import com.mystocks.repository.TransactionsRepository;
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

@Component
public class BtcServiceImpl implements BtcService{

	private static final Logger LOGGER = LoggerFactory.getLogger(BtcServiceImpl.class);

	final private TransactionsRepository transactionsRepository;
	final private AssetDataHelper assetDataHelper;

	@Autowired
	public BtcServiceImpl(TransactionsRepository transactionsRepository) {
		this.transactionsRepository = transactionsRepository;
		this.assetDataHelper = new AssetDataHelper();
	}

	@Override
	public AssetData processBtcData(String userId) {
		LOGGER.info("calling getBtcPrice has started for user {}", userId);

		Response<BtcInfoDto> btcResponse = null;
		AssetApiService assetApiService = RetrofitBuilder.assetApiService(ApiConfiguration.API_COINBASE_URL);
		Call<BtcInfoDto> retrofitCallBtc = assetApiService.getBtcPriceNow();

		try {
			btcResponse =  retrofitCallBtc.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}
		return processBtcData(btcResponse != null ? btcResponse.body() : new BtcInfoDto(), userId);
	}

	private AssetData processBtcData(BtcInfoDto btcInfoDto, String userId) {
		LOGGER.info("processBtcData has started for user {}", userId);

		AssetData assetData = new AssetData();

		List<Transaction> allByUserId = transactionsRepository.findAllByUserId(userId);
		BigDecimal totalAmount = BigDecimal.ZERO;
		if (!allByUserId.isEmpty()) {
			totalAmount = assetDataHelper.getTotal(allByUserId, "btc", Transaction::getAmount);
		}

		assetData.setInvestedInCrowns(String.valueOf(assetDataHelper.getTotal(allByUserId, "btc", Transaction::getTransactionValueInCrowns)));
		assetData.setAssetBalance(String.valueOf(totalAmount));

		List<AssetRate> btcRates = new ArrayList<>();

		// USD
		AssetRate assetRateUSD = assetDataHelper.getBtcBalance(btcInfoDto, totalAmount, CurrencyEnum.USD);
		btcRates.add(assetRateUSD);

		//CZK
		AssetRate assetRateCZK = assetDataHelper.getBtcBalance(btcInfoDto, totalAmount, CurrencyEnum.CZK);
		btcRates.add(assetRateCZK);

		assetData.setAssetBalanceList(btcRates);
		assetData.setType(AssetType.CRYPTO);
		assetData.setSymbol("btc");

		LOGGER.info("processBtcData has ended for user {}", userId);
		return assetData;
	}
}
