package com.mystocks.service;

import com.mystocks.dto.BtcInfoDto;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.GET;

@Service
public interface CryptoService {

	@GET("v1/bpi/currentprice/CZK.json")
	Call<BtcInfoDto> getBtcPriceNow();
}
