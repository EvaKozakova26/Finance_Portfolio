package com.mystocks.data;

import com.mystocks.model.ExchangeRate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestExchangeData {

	public static List<ExchangeRate> getData() {
		List<ExchangeRate> exchangeRates = new ArrayList<>();
		// TODO: 01.11.2020 builder
		ExchangeRate exchangeRate = new ExchangeRate();
		exchangeRate.setBic("CZ0005112300");
		exchangeRate.setName("ČEZ");
		exchangeRate.setCode("BAACEZ");
		exchangeRate.setDate(Date.from(Instant.now()));
		exchangeRate.setRefPrice(BigDecimal.valueOf(10));
		exchangeRate.setEndRate(BigDecimal.valueOf(10));
		exchangeRate.setDayMaxPrice(BigDecimal.valueOf(15));
		exchangeRate.setDayMinPrice(BigDecimal.valueOf(10));
		exchangeRate.setDayCount(1000);
		exchangeRate.setObjemObchodu(BigDecimal.valueOf(100));
		exchangeRate.setPocetObchodu(100);
		exchangeRates.add(exchangeRate);
		return exchangeRates;
	}
}
