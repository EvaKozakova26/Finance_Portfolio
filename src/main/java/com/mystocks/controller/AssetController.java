package com.mystocks.controller;

import com.mystocks.dto.AssetDataListEntity;
import com.mystocks.dto.CryptoTransactionListEntity;
import com.mystocks.service.BtcService;
import com.mystocks.service.SharesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssetController.class);

	private final BtcService btcService;
	private final SharesService sharesService;

	@Autowired
	public AssetController(BtcService btcService, SharesService sharesService) {
		this.btcService = btcService;
		this.sharesService = sharesService;
	}

	@GetMapping("/all/{userId}")
	@CrossOrigin
	public CryptoTransactionListEntity getAllTransactions(@PathVariable("userId") String userId) {
		LOGGER.info("getAllTransactions has started for user {}", userId);
		CryptoTransactionListEntity allTransactions = btcService.getAllTransactions(userId);
		LOGGER.info("getAllTransactions has ended for user {} with this size of results: {}", userId, allTransactions.getCryptoTransactions().size());
		return allTransactions;
	}

	@GetMapping("/assets/{userId}")
	@CrossOrigin
	public AssetDataListEntity getAssetsData(@PathVariable("userId") String userId) {
		LOGGER.info("getAssetsData has started for user {}", userId);

		AssetDataListEntity result = new AssetDataListEntity();

		// process BTC data
		result.addAsset(btcService.processBtcData(userId));

		// process shares data
		result.assAssetDataList(sharesService.processSharesAssets(userId));

		LOGGER.info("getAssetsData has ended for user {} with this size of results: {}", userId, result.getAssetData().size());
		return result;
	}
}
