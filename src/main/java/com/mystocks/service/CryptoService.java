package com.mystocks.service;

import com.mystocks.dto.BtcInfoDto;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Service
public interface CryptoService {

	@GET("v1/bpi/currentprice/CZK.json")
	Call<BtcInfoDto> getBtcPriceNow();

	// d9990d90052de0e89b305e591e5840fa http://data.fixer.io/
	@GET("api/{date}?access_key=d9990d90052de0e89b305e591e5840fa&symbols=USD,CZK&format=1")
	Call<ForexDataDto> getForexData(@Path(value = "date", encoded = true) String date);
}
