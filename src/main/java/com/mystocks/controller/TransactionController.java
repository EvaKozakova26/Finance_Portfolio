package com.mystocks.controller;

import com.mystocks.dto.CryptoTransactionCreateEntity;
import com.mystocks.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class TransactionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

	private final TransactionService transactionService;

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/crypto/create/{userId}")
	@CrossOrigin
	public Void createCryptoTransaction(@RequestBody CryptoTransactionCreateEntity ctce, @PathVariable("userId") String userId) {
		LOGGER.info("createCryptoTransaction has started for user {}", userId);
		transactionService.createCryptoTransaction(ctce, userId);
		// TODO: 08.08.2021 not return null!!
		return null;
	}
}
