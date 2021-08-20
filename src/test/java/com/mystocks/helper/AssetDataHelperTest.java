package com.mystocks.helper;

import com.mystocks.model.CryptoTransaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class AssetDataHelperTest {

	public static final String CRYPTO = "crypto";
	public static final String SHARES = "shares";
	private static AssetDataHelper assetDataHelper;

	@BeforeAll
	static void setUp() {
		assetDataHelper = new AssetDataHelper();
	}

	@Test
	public void getTotalInvestedInCrownsTest() {
		BigDecimal investedCrowns = assetDataHelper.getTotal(createTransactions(), CRYPTO, CryptoTransaction::getTransactionValueInCrowns);
		AssertionErrors.assertEquals(null, BigDecimal.valueOf(4000), investedCrowns);
	}

	@Test
	public void getTotalAmountTest() {
		BigDecimal investedCrowns = assetDataHelper.getTotal(createTransactions(), CRYPTO, CryptoTransaction::getAmount);
		AssertionErrors.assertEquals(null, BigDecimal.valueOf(2), investedCrowns);
	}

	private List<CryptoTransaction> createTransactions() {
		return Arrays.asList(
				createTransaction(CRYPTO),
				createTransaction(CRYPTO),
				createTransaction(SHARES));
	}

	private CryptoTransaction createTransaction(String type) {
		CryptoTransaction cryptoTransaction = new CryptoTransaction();
		cryptoTransaction.setType(type);
		cryptoTransaction.setTransactionValueInCrowns(BigDecimal.valueOf(2000));
		cryptoTransaction.setAmount(BigDecimal.ONE);
		return cryptoTransaction;
	}

}
