package com.mystocks.repository;

import com.mystocks.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionsRepository extends MongoRepository<Transaction, String> {

	List<Transaction> findAllByUserId(String userId);

	List<Transaction> findAllByCodeAndUserId(String code, String userId);
}
