package com.mystocks.service;

import com.mystocks.dto.TransactionCreateEntity;
import com.mystocks.dto.TransactionListEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

	TransactionListEntity getAllTransactions(String userId);

	void createTransaction(TransactionCreateEntity ctce, String userId);
}
