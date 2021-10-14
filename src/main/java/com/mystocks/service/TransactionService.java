package com.mystocks.service;

import com.mystocks.dto.TransactionCreateEntity;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

	void createTransaction(TransactionCreateEntity ctce, String userId);
}
