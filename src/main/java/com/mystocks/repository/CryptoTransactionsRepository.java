package com.mystocks.repository;

import com.mystocks.model.CryptoTransactions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CryptoTransactionsRepository extends MongoRepository<CryptoTransactions, String> {

	List<CryptoTransactions> findAllByUserId(String userId);
}
