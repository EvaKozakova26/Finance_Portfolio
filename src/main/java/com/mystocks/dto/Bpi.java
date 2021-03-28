package com.mystocks.dto;

public class Bpi {

	private USD USD;
	private GBP GBP;
	private EUR EUR;

	public com.mystocks.dto.USD getUSD() {
		return USD;
	}

	public void setUSD(com.mystocks.dto.USD USD) {
		this.USD = USD;
	}

	public com.mystocks.dto.GBP getGBP() {
		return GBP;
	}

	public void setGBP(com.mystocks.dto.GBP GBP) {
		this.GBP = GBP;
	}

	public com.mystocks.dto.EUR getEUR() {
		return EUR;
	}

	public void setEUR(com.mystocks.dto.EUR EUR) {
		this.EUR = EUR;
	}
}

class GBP {
	private String code;
	private String symbol;
	private String rate;
	private String description;
	private float rateFloat;
}

class EUR {
	private String code;
	private String symbol;
	private String rate;
	private String description;
	private float rateFloat;
}
