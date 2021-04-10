package com.mystocks.service;

import com.mystocks.dto.BtcInfoData;
import com.mystocks.dto.BtcInfoDto;
import org.springframework.stereotype.Service;

@Service
public interface BtcService {

	BtcInfoData processBtcData(BtcInfoDto btcInfoDto, String userId);
}
