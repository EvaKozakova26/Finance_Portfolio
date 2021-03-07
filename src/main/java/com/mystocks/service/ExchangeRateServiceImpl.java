package com.mystocks.service;

import com.mystocks.UrlReader;
import com.mystocks.model.ExchangeRateRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeRateServiceImpl implements ExchangeRateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

	@Override
	public List<ExchangeRateRaw> download() {
		UrlReader urlReader = new UrlReader();
		List<ExchangeRateRaw> exchangeRateRaws = new ArrayList<>();
		try {
			exchangeRateRaws = urlReader.readFromUrl();
		} catch (IOException e) {
			LOGGER.error("Reading file failed");
			return exchangeRateRaws;
		}
		LOGGER.info("Reading file has finished");
		return exchangeRateRaws;
	}
}
