package com.mystocks.controller;

import com.mystocks.UrlReader;
import com.mystocks.model.ExchangeRateRaw;
import com.mystocks.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
public class StockController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

	@Autowired
	private ExchangeRateService exchangeRateService;

	@Autowired
	private AccountBalanceRepository accBalanceRepository;

	private CryptoService cryptoService;

	@GetMapping("/exchangeRate")
	public List<ExchangeRate> getExchangeRate() {
		LOGGER.info("getExchangeRate has started");
		return exchangeRateService.download();
	}

	public StockController() {
		buildRetrofit();
	}

	@GetMapping("/btc")
	@CrossOrigin
	public BtcInfoData getBtcPrice() {
		LOGGER.info("getBtcPrice has started");

		Response<BtcInfoDto> response = null;
		Call<BtcInfoDto> retrofitCall = cryptoService.getBtcPriceNow();
		try {
			response =  retrofitCall.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fillBtcData(response != null ? response.body() : new BtcInfoDto());
	}

	private BtcInfoData fillBtcData(BtcInfoDto body) {
		BtcInfoData btcInfoData = new BtcInfoData();

		List<AccountBalance> all = accBalanceRepository.findAll();
		AccountBalance accountBalance = all.get(0);

		btcInfoData.setBtcBalance(accountBalance.getBtc());
		String rate = body != null ? body.getBpi().getUSD().getRate() : null;

		String normalizedRate = "0";
		if (rate != null) {
			normalizedRate = rate.replaceAll(",","");
		}
		btcInfoData.setPriceInDollars(rate);
		BigDecimal v1 = new BigDecimal(accountBalance.getBtc());
		BigDecimal v2 = new BigDecimal(normalizedRate);
		BigDecimal result = v1.multiply(v2);
		BigDecimal bigDecimal = result.setScale(3, RoundingMode.HALF_UP);
		btcInfoData.setAccBalance(String.valueOf(bigDecimal));

		return btcInfoData;
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
