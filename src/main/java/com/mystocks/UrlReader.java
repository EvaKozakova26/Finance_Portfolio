package com.mystocks;


import com.mystocks.constants.ApiConstants;
import com.mystocks.dto.ExchangeRateRaw;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UrlReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlReader.class);

	/**
	 * Parses file from BCPP
	 * @return List of parsed data
	 * @throws IOException e
	 */
	public List<ExchangeRateRaw> readFromUrl() throws IOException {
		String url= ApiConstants.URL;
		List<ExchangeRateRaw> exchangeRateRaws = new ArrayList<>();
		try {
			InputStream is = new URL(url).openConnection().getInputStream();
			ZipInputStream zipIn = new ZipInputStream(is);
			ZipEntry entry;
			while ((entry = zipIn.getNextEntry()) != null) {
				if (entry.getName().startsWith(ApiConstants.FILE_PREFIX)) {
					InputStreamReader inputStreamReader = new InputStreamReader(zipIn);
					LOGGER.info("Starting to read file: {}", entry.getName());
					exchangeRateRaws = getExchangeRate(inputStreamReader);
				}
				zipIn.closeEntry();
			}
		} catch (IOException e) {
			LOGGER.error("Reading file failed");
			return exchangeRateRaws;
		}
		LOGGER.info("Reading file has finished");
		return exchangeRateRaws;
	}

	/**
	 * builds ExchangeRate from csv based on annotations, suppressed warnings
	 * @param inputStreamReader - representation of file to be parsed
	 * @return parsed ExChange rates
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private List<ExchangeRateRaw> getExchangeRate(InputStreamReader inputStreamReader) {
		return (List<ExchangeRateRaw>) new CsvToBeanBuilder(inputStreamReader)
				.withType(ExchangeRateRaw.class)
				.build()
				.parse();
	}

}
