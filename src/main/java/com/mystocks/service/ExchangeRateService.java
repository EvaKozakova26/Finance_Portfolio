package com.mystocks.service;

import com.mystocks.dto.ExchangeRateRaw;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExchangeRateService {

	/**
	 * Downloads Exchange Rate for today
	 * @return Parsed Exchange Rate
	 */
	List<ExchangeRateRaw> download();
}
