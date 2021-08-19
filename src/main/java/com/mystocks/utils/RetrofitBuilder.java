package com.mystocks.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mystocks.service.AssetApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

	public static Retrofit buildRetrofit(String url) {
		Gson gson = new GsonBuilder()
				.setLenient()
				.create();

		return new Retrofit.Builder()
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

	}

	public static AssetApiService assetApiService(String url) {
		Retrofit retrofit = buildRetrofit(url);
		return retrofit.create(AssetApiService.class);
	}
}
