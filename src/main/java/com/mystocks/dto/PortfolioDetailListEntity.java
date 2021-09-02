package com.mystocks.dto;

import java.util.ArrayList;
import java.util.List;

public class PortfolioDetailListEntity {

	private List<PortfolioDetail> portfolioDetails;

	public PortfolioDetailListEntity() {
		this.portfolioDetails = new ArrayList<>();
	}

	public void addDetail(PortfolioDetail portfolioDetail) {
		this.portfolioDetails.add(portfolioDetail);
	}

	public List<PortfolioDetail> getPortfolioDetails() {
		return portfolioDetails;
	}

	public void setPortfolioDetails(List<PortfolioDetail> portfolioDetails) {
		this.portfolioDetails = portfolioDetails;
	}
}
