package com.mystocks.service;

import com.mystocks.dto.PortfolioDetailListEntity;
import org.springframework.stereotype.Service;

@Service
public interface PortfolioDetailService {

	PortfolioDetailListEntity getPortfolioDetail(String userId);
}
