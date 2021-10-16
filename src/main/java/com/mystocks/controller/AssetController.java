package com.mystocks.controller;

import com.mystocks.dto.AssetDataListEntity;
import com.mystocks.service.AssetService;
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

	private final AssetService assetService;

	@Autowired
	public AssetController(AssetService assetService) {
		this.assetService = assetService;
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
