package com.mystocks.service;

import com.mystocks.dto.CryptoTransactionCreateEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

	void createCryptoTransaction(ForexDataDto body, CryptoTransactionCreateEntity ctce, String userId);
}
