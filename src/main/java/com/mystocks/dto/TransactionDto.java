package com.mystocks.dto;

public class TransactionDto {

	private String type;
	private String date;
	private String amountBtc;
	private String buySellValue;
	private String buySellValueInDollars;
	private String stockPriceInDollars;
	private String stockPriceInCrowns;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmountBtc() {
		return amountBtc;
	}

	public void setAmountBtc(String amountBtc) {
		this.amountBtc = amountBtc;
	}

	public String getBuySellValue() {
		return buySellValue;
	}

	public void setBuySellValue(String buySellValue) {
		this.buySellValue = buySellValue;
	}

	public String getBuySellValueInDollars() {
		return buySellValueInDollars;
	}

	public void setBuySellValueInDollars(String buySellValueInDollars) {
		this.buySellValueInDollars = buySellValueInDollars;
	}

	public String getStockPriceInDollars() {
		return stockPriceInDollars;
	}

	public void setStockPriceInDollars(String stockPriceInDollars) {
		this.stockPriceInDollars = stockPriceInDollars;
	}

	public String getStockPriceInCrowns() {
		return stockPriceInCrowns;
	}

	public void setStockPriceInCrowns(String stockPriceInCrowns) {
		this.stockPriceInCrowns = stockPriceInCrowns;
	}
}
