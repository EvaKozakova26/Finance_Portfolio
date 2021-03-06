package com.mystocks.controller;

import com.mystocks.data.TestExchangeData;
import com.mystocks.model.ExchangeRate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockController {

	@GetMapping("/exchangeRate")
	public List<ExchangeRate> getExchangeRate() {
		return TestExchangeData.getData();
	}
}
