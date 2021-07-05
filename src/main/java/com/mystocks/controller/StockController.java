package com.mystocks.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.dto.*;
import com.mystocks.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

@RestController
public class StockController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

	private final ExchangeRateService exchangeRateService;
	private final BtcService btcService;
	private final TransactionService transactionService;
	private CryptoService cryptoService;

	@Autowired
	public StockController(ExchangeRateService exchangeRateService, BtcService btcService, TransactionService transactionService) {
		this.exchangeRateService = exchangeRateService;
		this.btcService = btcService;
		this.transactionService = transactionService;

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
		buildForexRetrofit();
		Response<ForexDataDto> response = null;
		String substring = ctce.getTransactionDate().substring(0, 10);
		Call<ForexDataDto> retrofitCall = cryptoService.getForexData(ctce.getTransactionDate().substring(0,10));

		try {
			response =  retrofitCall.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}

		transactionService.createCryptoTransaction(response != null ? response.body() : new ForexDataDto(), ctce, userId);

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
		// TODO: 05.07.2021 not onlz btc but all assets
		LOGGER.info("getBtcPrice has started for user {}", userId);

		AssetDataListEntity result = new AssetDataListEntity();

		buildCryptoRetrofit();

		Response<BtcInfoDto> response = null;
		Call<BtcInfoDto> retrofitCall = cryptoService.getBtcPriceNow();
		try {
			response =  retrofitCall.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}

		result.addAsset(btcService.processBtcData(response != null ? response.body() : new BtcInfoDto(), userId));
		return result;
	}

	private void buildCryptoRetrofit() {
		Gson gson = new GsonBuilder()
				.setLenient()
				.create();

		String baseUrl = ApiConfiguration.API_COINBASE_URL;

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		cryptoService = retrofit.create(CryptoService.class);
	}

	private void buildForexRetrofit() {
		Gson gson = new GsonBuilder()
				.setLenient()
				.create();

		String baseUrl = ApiConfiguration.API_FOREX_URL;

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		cryptoService = retrofit.create(CryptoService.class);
	}
}
