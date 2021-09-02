package com.mystocks.dto;

public class PortfolioDetail {

	private String symbol;
	private String fullName;
	private String sharePercentageHistoric;
	private String sharePercentageCurrent;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSharePercentageHistoric() {
		return sharePercentageHistoric;
	}

	public void setSharePercentageHistoric(String sharePercentageHistoric) {
		this.sharePercentageHistoric = sharePercentageHistoric;
	}

	public String getSharePercentageCurrent() {
		return sharePercentageCurrent;
	}

	public void setSharePercentageCurrent(String sharePercentageCurrent) {
		this.sharePercentageCurrent = sharePercentageCurrent;
	}
}
