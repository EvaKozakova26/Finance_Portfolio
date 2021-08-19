package com.mystocks.controller;

import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.dto.*;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.model.CryptoTransaction;
import com.mystocks.service.*;
import com.mystocks.utils.RetrofitBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AssetController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssetController.class);

	// todo zapouzdrit
	private final ExchangeRateService exchangeRateService;
	private final BtcService btcService;
	private final TransactionService transactionService;
	private final SharesService sharesService;
	private AssetApiService assetApiService;

	@Autowired
	public AssetController(ExchangeRateService exchangeRateService, BtcService btcService, TransactionService transactionService, SharesService sharesService) {
		this.exchangeRateService = exchangeRateService;
		this.btcService = btcService;
		this.transactionService = transactionService;
		this.sharesService = sharesService;
	}

	@GetMapping("/exchangeRate")
	public List<ExchangeRateRaw> getExchangeRate() {
		LOGGER.info("getExchangeRate has started");
		return exchangeRateService.download();
	}

	@PostMapping("/crypto/create/{userId}")
	@CrossOrigin
	public Void createCryptoTransaction(@RequestBody CryptoTransactionCreateEntity ctce,  @PathVariable("userId") String userId) {
		LOGGER.info("createCryptoTransaction has started for user {}", userId);
		Retrofit retrofit = RetrofitBuilder.buildRetrofit(ApiConfiguration.API_FOREX_URL);
		assetApiService = retrofit.create(AssetApiService.class);

		Response<ForexDataDto> response = null;
		String substring = ctce.getTransactionDate().substring(0, 10);
		Call<ForexDataDto> retrofitCall = assetApiService.getForexData(ctce.getTransactionDate().substring(0,10));

		try {
			response =  retrofitCall.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}

		transactionService.createCryptoTransaction(response != null ? response.body() : new ForexDataDto(), ctce, userId);

		// TODO: 08.08.2021 not return null!!
		return null;
	}

	@GetMapping("/all/{userId}")
	@CrossOrigin
	public CryptoTransactionListEntity getAllTransactions(@PathVariable("userId") String userId) {
		LOGGER.info("getAllTransactions has started for user {}", userId);
		return btcService.getAllTransactions(userId);
	}

	@GetMapping("/assets/{userId}")
	@CrossOrigin
	public AssetDataListEntity getAssetsData(@PathVariable("userId") String userId) {
		LOGGER.info("getAssetsData has started for user {}", userId);

		AssetDataListEntity result = new AssetDataListEntity();

		// process BTC data
		Retrofit cryptoRetrofit = RetrofitBuilder.buildRetrofit(ApiConfiguration.API_COINBASE_URL);
		assetApiService = cryptoRetrofit.create(AssetApiService.class);
		result.addAsset(processBtcData(userId));

		// process shares data
		Retrofit sharesRetrofit = RetrofitBuilder.buildRetrofit(ApiConfiguration.API_YAHOO_URL);
		assetApiService = sharesRetrofit.create(AssetApiService.class);

		result.assAssetDataList(processSharesAssets(userId));

		return result;
	}

	private AssetData processBtcData(String userId) {
		Response<BtcInfoDto> btcResponse = null;
		LOGGER.info("calling getBtcPrice has started for user {}", userId);
		Call<BtcInfoDto> retrofitCallBtc = assetApiService.getBtcPriceNow();
		try {
			btcResponse =  retrofitCallBtc.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}
		return btcService.processBtcData(btcResponse != null ? btcResponse.body() : new BtcInfoDto(), userId);
	}

	private List<AssetData> processSharesAssets(String userId) {
		List<AssetData> result = new ArrayList<>();
		List<CryptoTransaction> allSharesTransactions = sharesService.getAllSharesTransactions(userId);
		Set<String> sharesCodes = getSharesCodes(allSharesTransactions);
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
				String symbol = sharesDto.getChart().getResult().get(0).getMeta().getSymbol();
				LOGGER.info("calling process Shares data has started for user {} and share {}", userId, symbol);
				result.add(sharesService.processData(sharesDto, userId));
			}
		}
		return result;
	}

	private Set<String> getSharesCodes(List<CryptoTransaction> allSharesTransactions) {
		return allSharesTransactions.stream()
				.map(CryptoTransaction::getType)
				.collect(Collectors.toSet());
	}
}
