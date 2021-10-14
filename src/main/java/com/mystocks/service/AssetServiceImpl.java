package com.mystocks.service;

import com.mystocks.dto.AssetDataListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetServiceImpl implements AssetService {

	private final BtcService btcService;
	private final SharesService sharesService;

	@Autowired
	public AssetServiceImpl(BtcService btcService, SharesService sharesService) {
		this.btcService = btcService;
		this.sharesService = sharesService;
	}


	@Override
	public AssetDataListEntity getAssetData(String userId) {
		AssetDataListEntity result = new AssetDataListEntity();

		// process BTC data
		result.addAsset(btcService.processBtcData(userId));

		// process shares data
		result.addAssetDataList(sharesService.processSharesAssets(userId));

		return  result;
	}
}
