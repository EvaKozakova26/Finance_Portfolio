package com.mystocks.service;

import com.mystocks.constants.CurrencyEnum;
import com.mystocks.dto.AssetData;
import com.mystocks.dto.AssetRate;
import com.mystocks.dto.yahoo.Meta;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.enums.AssetType;
import com.mystocks.helper.AssetDataHelper;
import com.mystocks.model.CryptoTransaction;
import com.mystocks.repository.CryptoTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SharesServiceImpl implements SharesService{

	private final CryptoTransactionsRepository transactionsRepository;
	final private AssetDataHelper assetDataHelper;

	@Autowired
	public SharesServiceImpl(CryptoTransactionsRepository transactionsRepository) {
		this.transactionsRepository = transactionsRepository;
		this.assetDataHelper = new AssetDataHelper();
	}

	@Override
	public AssetData processData(SharesDto shares, String userId) {
		AssetData assetData = new AssetData();
		assetData.setType(AssetType.SHARES);

		// result is always ONE - 1 day history
		Meta sharesMeta = shares.getChart().getResult().get(0).getMeta();

		List<CryptoTransaction> allByTypeAndUserId = transactionsRepository.findAllByTypeAndUserId(sharesMeta.getSymbol(), userId);
		BigDecimal totalAmount = assetDataHelper.getTotalAmount(allByTypeAndUserId, sharesMeta.getSymbol());

		assetData.setInvestedInCrowns(String.valueOf(assetDataHelper.getInvestedCrowns(allByTypeAndUserId, sharesMeta.getSymbol())));
		assetData.setAssetBalance(String.valueOf(totalAmount));


		List<AssetRate> shareRates = new ArrayList<>();

		if (sharesMeta.getCurrency().equals(CurrencyEnum.CZK.name())) {
			// CZK
			AssetRate assetRateCZK = assetDataHelper.getShareBalance(shares, totalAmount, CurrencyEnum.CZK);
			shareRates.add(assetRateCZK);
		} else {
			// TODO: 06.07.2021 NON-PRA markets process
		}

		assetData.setAssetBalanceList(shareRates);
		assetData.setSymbol(sharesMeta.getSymbol());
		return assetData;
	}

	@Override
	public List<CryptoTransaction> getAllSharesTransactions(String userId) {
		List<CryptoTransaction> allByUserId = transactionsRepository.findAllByUserId(userId);
		// TODO: 06.07.2021 je potreba prdat v DB i asset type
		return allByUserId.stream()
				.filter(transaction -> !transaction.getType().equals("btc"))
				.collect(Collectors.toList());
	}

}
