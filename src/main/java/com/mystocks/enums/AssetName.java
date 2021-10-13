package com.mystocks.enums;

import java.util.List;
import java.util.Map;

public class AssetName {

	public static Map<String, String> values = Map.of(
			"btc", "Bitcoin",
			"SOFI", "SoFi Technologies, Inc,",
			"MCD", "McDonald's Corporation",
			"SPY", "SPDR S&P 500 ETF Trust",
			"INTC", "Intel Corporation",
			"TABAK.PR", "Philip Morris CR a.s.",
			"MONET.PR", "MONETA Money Bank, a.s.",
			"CEZ.PR", "CEZ, a. s.",
			"SPG", "Simon Property Group, Inc.",
			"FB", "Facebook, Inc."
	);

	public static List<String> PRA_EXCHANGE = List.of("TABAK.PR", "MONET.PR", "CEZ.PR");
	public static List<String> US_EXCHANGE = List.of("SOFI", "MCD", "SPY", "INTC", "FB", "SPG");
	public static List<String> CRYPTO_EXCHANGE = List.of("btc");

}
