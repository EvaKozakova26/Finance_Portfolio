package com.mystocks.repository;

import com.mystocks.model.AccountBalance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountBalanceRepository extends MongoRepository<AccountBalance, String> {
}
