package com.mystocks.service;

import com.mystocks.dto.AssetData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SharesService {

	List<AssetData> processSharesAssets(String userId);
}
