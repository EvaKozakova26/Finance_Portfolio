package com.mystocks.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.dto.BtcInfoData;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.dto.ExchangeRateRaw;
import com.mystocks.service.BtcService;
import com.mystocks.service.CryptoService;
import com.mystocks.service.ExchangeRateService;
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
	private CryptoService cryptoService;

	@Autowired
	public StockController(ExchangeRateService exchangeRateService, BtcService btcService) {
		this.exchangeRateService = exchangeRateService;
		this.btcService = btcService;
		buildRetrofit();
	}

	@GetMapping("/exchangeRate")
	public List<ExchangeRateRaw> getExchangeRate() {
		LOGGER.info("getExchangeRate has started");
		return exchangeRateService.download();
	}

	@GetMapping("/btc/{userId}")
	@CrossOrigin
	public BtcInfoData getBtcPrice(@PathVariable("userId") String userId) {
		LOGGER.info("getBtcPrice has started for user {}", userId);

		Response<BtcInfoDto> response = null;
		Call<BtcInfoDto> retrofitCall = cryptoService.getBtcPriceNow();
		try {
			response =  retrofitCall.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}

		return btcService.processBtcData(response != null ? response.body() : new BtcInfoDto(), userId);
	}

	private void buildRetrofit() {
		Gson gson = new GsonBuilder()
				.setLenient()
				.create();

		String baseUrl = ApiConfiguration.API_BASE_URL;

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		cryptoService = retrofit.create(CryptoService.class);
	}
}
