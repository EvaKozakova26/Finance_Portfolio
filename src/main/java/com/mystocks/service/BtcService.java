package com.mystocks.service;

import com.mystocks.dto.AssetData;
import org.springframework.stereotype.Service;

@Service
public interface BtcService {

	AssetData processBtcData(String userId);
}
