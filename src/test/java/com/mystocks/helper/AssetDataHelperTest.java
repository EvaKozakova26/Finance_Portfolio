package com.mystocks.helper;

import com.mystocks.model.Transaction;
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
		BigDecimal investedCrowns = assetDataHelper.getTotal(createTransactions(), CRYPTO, Transaction::getTransactionValueInCrowns);
		AssertionErrors.assertEquals(null, BigDecimal.valueOf(4000), investedCrowns);
	}

	@Test
	public void getTotalAmountTest() {
		BigDecimal investedCrowns = assetDataHelper.getTotal(createTransactions(), CRYPTO, Transaction::getAmount);
		AssertionErrors.assertEquals(null, BigDecimal.valueOf(2), investedCrowns);
	}

	private List<Transaction> createTransactions() {
		return Arrays.asList(
				createTransaction(CRYPTO),
				createTransaction(CRYPTO),
				createTransaction(SHARES));
	}

	private Transaction createTransaction(String type) {
		Transaction transaction = new Transaction();
		transaction.setType(type);
		transaction.setTransactionValueInCrowns(BigDecimal.valueOf(2000));
		transaction.setAmount(BigDecimal.ONE);
		return transaction;
	}

}
