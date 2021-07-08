package com.mystocks.dto;

import com.mystocks.enums.AssetType;

import java.util.List;

public class AssetData {

	AssetType type;

	String symbol;

	List<AssetRate> assetRateList;

	private String assetBalance;

	private String investedInCrowns;

	public String getAssetBalance() {
		return assetBalance;
	}

	public void setAssetBalance(String assetBalance) {
		this.assetBalance = assetBalance;
	}

	public List<AssetRate> getAssetBalanceList() {
		return assetRateList;
	}

	public void setAssetBalanceList(List<AssetRate> assetRateList) {
		this.assetRateList = assetRateList;
	}

	public String getInvestedInCrowns() {
		return investedInCrowns;
	}

	public void setInvestedInCrowns(String investedInCrowns) {
		this.investedInCrowns = investedInCrowns;
	}

	public AssetType getType() {
		return type;
	}

	public void setType(AssetType type) {
		this.type = type;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
