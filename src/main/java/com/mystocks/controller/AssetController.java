package com.mystocks.controller;

import com.mystocks.dto.AssetDataListEntity;
import com.mystocks.dto.TransactionListEntity;
import com.mystocks.service.AssetService;
import com.mystocks.service.BtcService;
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
	private final AssetService assetService;

	@Autowired
	public AssetController(BtcService btcService, AssetService assetService) {
		this.btcService = btcService;
		this.assetService = assetService;
	}

	@GetMapping("/all/{userId}")
	@CrossOrigin
	public TransactionListEntity getAllTransactions(@PathVariable("userId") String userId) {
		LOGGER.info("getAllTransactions has started for user {}", userId);
		TransactionListEntity allTransactions = btcService.getAllTransactions(userId);
		LOGGER.info("getAllTransactions has ended for user {} with this size of results: {}", userId, allTransactions.getTransactions().size());
		return allTransactions;
	}

	@GetMapping("/assets/{userId}")
	@CrossOrigin
	public AssetDataListEntity getAssetsData(@PathVariable("userId") String userId) {
		LOGGER.info("getAssetsData has started for user {}", userId);
		AssetDataListEntity result = assetService.getAssetData(userId);
		LOGGER.info("getAssetsData has ended for user {} with this size of results: {}", userId, result.getAssetData().size());
		return result;
	}
}
