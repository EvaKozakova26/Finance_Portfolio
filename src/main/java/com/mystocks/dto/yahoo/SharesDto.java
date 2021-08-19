package com.mystocks.dto.yahoo;

public class SharesDto {

	private Chart chart;

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}

	public Meta getMeta() {
		return getChart().getResult().get(0).getMeta();
	}
}
