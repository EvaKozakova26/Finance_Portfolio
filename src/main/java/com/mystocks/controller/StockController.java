package com.mystocks.controller;

import com.mystocks.UrlReader;
import com.mystocks.model.ExchangeRateRaw;
import com.mystocks.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlReader.class);

	private final ExchangeRateService exchangeRateService;

	@Autowired
	public StockController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	@GetMapping("/exchangeRate")
	public List<ExchangeRateRaw> getExchangeRate() {
		LOGGER.info("getExchangeRate has started");
		return exchangeRateService.download();
	}
}
