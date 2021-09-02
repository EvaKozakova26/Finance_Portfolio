package com.mystocks.service;

import com.mystocks.dto.AssetDataListEntity;
import org.springframework.stereotype.Service;

@Service
public interface AssetService {

	AssetDataListEntity getAssetData(String userId);

}
