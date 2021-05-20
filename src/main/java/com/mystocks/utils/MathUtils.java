package com.mystocks.utils;

import java.math.BigDecimal;
import java.util.Map;

public class MathUtils {

	/**
	 * Calculates the weighted average of a map.
	 *
	 * @throws ArithmeticException If divide by zero happens
	 * @param map A map of values and weights
	 * @return The weighted average of the map
	 */
	public static Double weightedAverage(Map<BigDecimal, BigDecimal> map) throws ArithmeticException {
		double num = 0;
		double denom = 0;
		for (Map.Entry<BigDecimal, BigDecimal> entry : map.entrySet()) {
			num += entry.getKey().doubleValue() * entry.getValue().doubleValue();
			denom += entry.getValue().doubleValue();
		}
		return num / denom;
	}
}
