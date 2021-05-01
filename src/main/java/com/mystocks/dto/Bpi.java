package com.mystocks.dto;

public class Bpi {

	private USD USD;
	private CZK CZK;

	public com.mystocks.dto.USD getUSD() {
		return USD;
	}

	public void setUSD(com.mystocks.dto.USD USD) {
		this.USD = USD;
	}

	public com.mystocks.dto.CZK getCZK() {
		return CZK;
	}

	public void setCZK(com.mystocks.dto.CZK CZK) {
		this.CZK = CZK;
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
