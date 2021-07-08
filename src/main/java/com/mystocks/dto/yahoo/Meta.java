package com.mystocks.dto.yahoo;

public class Meta {

	private String currency;
	private String symbol;
	private String exchangeName;
	private float regularMarketPrice;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public float getRegularMarketPrice() {
		return regularMarketPrice;
	}

	public void setRegularMarketPrice(float regularMarketPrice) {
		this.regularMarketPrice = regularMarketPrice;
	}
}
