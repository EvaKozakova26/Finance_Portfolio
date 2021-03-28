package com.mystocks.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account_balance")
public class AccountBalance {

	@Id
	private String id;

	private String btc;

	public String getBtc() {
		return btc;
	}

	public void setBtc(String btc) {
		this.btc = btc;
	}
}
