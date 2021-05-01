package com.mystocks.repository;

import com.mystocks.model.AccountBalance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountBalanceRepository extends MongoRepository<AccountBalance, String> {

	Optional<AccountBalance> findByUserId(String userId);
}
