package com.mystocks.service;

import com.mystocks.dto.AssetData;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.dto.CryptoTransactionListEntity;
import org.springframework.stereotype.Service;

@Service
public interface BtcService {

	CryptoTransactionListEntity getAllTransactions(String userId);

	AssetData processBtcData(BtcInfoDto btcInfoDto, String userId);
}
