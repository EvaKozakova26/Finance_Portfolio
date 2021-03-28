package com.mystocks.dto;

public class BtcInfoData {

	private String priceInDollars;

	private String btcBalance;

	private String accBalance;

	public String getPriceInDollars() {
		return priceInDollars;
	}

	public void setPriceInDollars(String priceInDollars) {
		this.priceInDollars = priceInDollars;
	}

	public String getBtcBalance() {
		return btcBalance;
	}

	public void setBtcBalance(String btcBalance) {
		this.btcBalance = btcBalance;
	}

	public String getAccBalance() {
		return accBalance;
	}

	public void setAccBalance(String accBalance) {
		this.accBalance = accBalance;
	}
}
