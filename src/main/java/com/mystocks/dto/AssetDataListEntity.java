package com.mystocks.dto;

import java.util.ArrayList;
import java.util.List;

public class AssetDataListEntity {

	List<AssetData> assetData;

	public AssetDataListEntity() {
		assetData = new ArrayList<>();
	}

	public List<AssetData> getAssetData() {
		return assetData;
	}

	public void setAssetData(List<AssetData> assetData) {
		this.assetData = assetData;
	}

	public void addAsset(AssetData assetData) {
		this.assetData.add(assetData);
	}
}
