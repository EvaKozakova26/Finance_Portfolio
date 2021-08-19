package com.mystocks.service;

import com.mystocks.dto.CryptoTransactionCreateEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

	void createCryptoTransaction(CryptoTransactionCreateEntity ctce, String userId);
}
