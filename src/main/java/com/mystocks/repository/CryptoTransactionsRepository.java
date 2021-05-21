package com.mystocks.repository;

import com.mystocks.model.CryptoTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CryptoTransactionsRepository extends MongoRepository<CryptoTransaction, String> {

	List<CryptoTransaction> findAllByUserId(String userId);
}
