package com.mystocks.data;

import com.mystocks.dto.ExchangeRateRaw;

import java.util.ArrayList;
import java.util.List;

public class TestExchangeData {

	public static List<ExchangeRateRaw> getData() {
		List<ExchangeRateRaw> exchangeRateRaws = new ArrayList<>();
		// TODO: 01.11.2020 builder
		ExchangeRateRaw exchangeRateRaw = new ExchangeRateRaw();
		exchangeRateRaw.setBic("CZ0005112300");
		exchangeRateRaw.setName("ČEZ");
		exchangeRateRaw.setCode("BAACEZ");
		exchangeRateRaw.setDate("20.1.2020");
		exchangeRateRaw.setRefPrice("BigDecimal.valueOf(10)");
		exchangeRateRaw.setEndRate("BigDecimal.valueOf(10)");
		exchangeRateRaw.setDayMaxPrice("BigDecimal.valueOf(10)");
		exchangeRateRaw.setDayMinPrice("BigDecimal.valueOf(10)");
		exchangeRateRaw.setDayCount("BigDecimal.valueOf(10)");
		exchangeRateRaw.setObjemObchodu("BigDecimal.valueOf(10)");
		exchangeRateRaw.setPocetObchodu("BigDecimal.valueOf(10)");
		exchangeRateRaws.add(exchangeRateRaw);
		return exchangeRateRaws;
	}
}
