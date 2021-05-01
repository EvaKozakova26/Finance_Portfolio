package com.mystocks.dto;

public class BtcBalance {

	private String currency;
	private String price;
	private String accBalance;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAccBalance() {
		return accBalance;
	}

	public void setAccBalance(String accBalance) {
		this.accBalance = accBalance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
