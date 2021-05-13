package com.mystocks.service;

import com.mystocks.dto.BtcInfoData;
import com.mystocks.dto.BtcInfoDto;
import com.mystocks.dto.CryptoTransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BtcService {

	List<CryptoTransactionDto> getAllTransactions(String userId);

	BtcInfoData processBtcData(BtcInfoDto btcInfoDto, String userId);
}
