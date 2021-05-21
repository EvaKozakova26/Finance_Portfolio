package com.mystocks.service;

public class ForexDataDto {

	private boolean success;
	private Integer timestamp;
	private boolean historical;
	private String base;
	private String date;
	private Rates rates;

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Rates getRates() {
		return rates;
	}

	public void setRates(Rates rates) {
		this.rates = rates;
	}
}

class Rates {

	private double USD;
	private double CZK;

	public double getUSD() {
		return USD;
	}

	public void setUSD(double USD) {
		this.USD = USD;
	}

	public double getCZK() {
		return CZK;
	}

	public void setCZK(double CZK) {
		this.CZK = CZK;
	}
}
