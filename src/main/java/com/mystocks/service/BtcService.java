package com.mystocks.service;

import com.mystocks.dto.AssetData;
import com.mystocks.dto.TransactionListEntity;
import org.springframework.stereotype.Service;

@Service
public interface BtcService {

	TransactionListEntity getAllTransactions(String userId);

	AssetData processBtcData(String userId);
}
