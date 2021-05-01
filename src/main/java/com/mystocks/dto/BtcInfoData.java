package com.mystocks.dto;

import java.util.List;

public class BtcInfoData {

	List<BtcBalance> btcRates;

	private String btcBalance;

	public String getBtcBalance() {
		return btcBalance;
	}

	public void setBtcBalance(String btcBalance) {
		this.btcBalance = btcBalance;
	}

	public List<BtcBalance> getBtcRates() {
		return btcRates;
	}

	public void setBtcRates(List<BtcBalance> btcRates) {
		this.btcRates = btcRates;
	}
}
