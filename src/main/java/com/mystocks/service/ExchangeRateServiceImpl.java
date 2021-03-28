package com.mystocks.service;

import com.mystocks.UrlReader;
import com.mystocks.dto.ExchangeRateRaw;
import com.mystocks.model.Test;
import com.mystocks.repository.TestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeRateServiceImpl implements ExchangeRateService {

	@Autowired
	private TestRepository testRepository;

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
		List<Test> testRepositoryAll = testRepository.findAll();

		return exchangeRateRaws;
	}
}
