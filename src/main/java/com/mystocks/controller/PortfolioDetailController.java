package com.mystocks.controller;

import com.mystocks.dto.PortfolioDetailListEntity;
import com.mystocks.service.PortfolioDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortfolioDetailController {

	private final PortfolioDetailService portfolioDetailService;

	@Autowired
	public PortfolioDetailController(PortfolioDetailService portfolioDetailService) {
		this.portfolioDetailService = portfolioDetailService;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioDetailController.class);

	@GetMapping("/detail/{userId}")
	@CrossOrigin
	public PortfolioDetailListEntity getPortfolioDetails(@PathVariable("userId") String userId) {
		LOGGER.info("getPortfolioDetails has started for user {}", userId);
		PortfolioDetailListEntity portfolioDetailResult = portfolioDetailService.getPortfolioDetail(userId);
		LOGGER.info("getPortfolioDetails has ended for user {} with this size of results: {}", userId, portfolioDetailResult.getPortfolioDetails().size());
		return portfolioDetailResult;
	}
}
