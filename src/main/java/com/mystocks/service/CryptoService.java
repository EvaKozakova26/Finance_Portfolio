package com.mystocks.service;

import com.mystocks.dto.BtcInfoDto;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.GET;

@Service
public interface CryptoService {

	@GET("v1/bpi/currentprice.json")
	Call<BtcInfoDto> getBtcPriceNow();
}
