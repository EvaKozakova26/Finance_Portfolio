package com.mystocks.service;

import com.mystocks.dto.AssetData;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.model.CryptoTransaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SharesService {

	AssetData processData(SharesDto shares, String userId);

	List<CryptoTransaction> getAllSharesTransactions(String userId);
}
