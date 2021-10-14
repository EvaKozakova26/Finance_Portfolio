package com.mystocks.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "transactions")
public class Transaction {

	@Id
	private String id;

	private String userId;

	private String type;

	private BigDecimal amount;

	private Date date;

	private BigDecimal transactionValueInCrowns;

	private BigDecimal stockPriceInDollars;

	private BigDecimal stockPriceInCrowns;

	private BigDecimal transactionValueInDollars;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getTransactionValueInCrowns() {
		return transactionValueInCrowns;
	}

	public void setTransactionValueInCrowns(BigDecimal transactionValueInCrowns) {
		this.transactionValueInCrowns = transactionValueInCrowns;
	}

	public BigDecimal getStockPriceInDollars() {
		return stockPriceInDollars;
	}

	public void setStockPriceInDollars(BigDecimal stockPriceInDollars) {
		this.stockPriceInDollars = stockPriceInDollars;
	}

	public BigDecimal getStockPriceInCrowns() {
		return stockPriceInCrowns;
	}

	public void setStockPriceInCrowns(BigDecimal stockPriceInCrowns) {
		this.stockPriceInCrowns = stockPriceInCrowns;
	}

	public BigDecimal getTransactionValueInDollars() {
		return transactionValueInDollars;
	}

	public void setTransactionValueInDollars(BigDecimal transactionValueInDollars) {
		this.transactionValueInDollars = transactionValueInDollars;
	}
}
