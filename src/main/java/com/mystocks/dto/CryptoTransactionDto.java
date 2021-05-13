package com.mystocks.dto;

public class CryptoTransactionDto {

	private String type;
	private String date;
	private String amountBtc;
	private String buySellValue;

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
}
