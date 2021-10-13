package com.mystocks.service;

import com.mystocks.configuration.ApiConfiguration;
import com.mystocks.dto.AssetDataListEntity;
import com.mystocks.dto.PortfolioDetailListEntity;
import com.mystocks.dto.yahoo.SharesDto;
import com.mystocks.helper.PortfolioDetailHelper;
import com.mystocks.model.CryptoTransaction;
import com.mystocks.repository.CryptoTransactionsRepository;
import com.mystocks.utils.RetrofitBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Component
public class PortfolioDetailServiceImpl implements PortfolioDetailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioDetailServiceImpl.class);

	public static final String USDCZK_X = "USDCZK=X";

	private final CryptoTransactionsRepository transactionsRepository;
	private final AssetService assetService;
	private final PortfolioDetailHelper portfolioDetailHelper;

	@Autowired
	public PortfolioDetailServiceImpl(CryptoTransactionsRepository transactionsRepository, AssetService assetService) {
		this.transactionsRepository = transactionsRepository;
		this.assetService = assetService;
		this.portfolioDetailHelper = new PortfolioDetailHelper();
	}

	@Override
	public PortfolioDetailListEntity getPortfolioDetail(String userId) {
		LOGGER.info("getPortfolioDetail has started for user {}", userId);
		PortfolioDetailListEntity result = new PortfolioDetailListEntity();

		List<CryptoTransaction> allByUserId = transactionsRepository.findAllByUserId(userId);
		AssetDataListEntity assetData = assetService.getAssetData(userId);

		AssetApiService assetApiService = RetrofitBuilder.assetApiService(ApiConfiguration.API_YAHOO_URL);
		Response<SharesDto> sharesResponse = null;
		Call<SharesDto> retrofitCallShares = assetApiService.getSharesData(USDCZK_X);

		try {
			sharesResponse =  retrofitCallShares.execute();
		} catch (IOException e) {
			// TODO: 10.04.2021 exception mapper
			e.printStackTrace();
		}
		SharesDto sharesDto = sharesResponse != null ? sharesResponse.body() : new SharesDto();

		if (sharesDto != null) {
			LOGGER.info("createPortfolioDetail started for user {}", userId);
			result = portfolioDetailHelper.createPortfolioDetail(sharesDto, assetData, allByUserId);
			LOGGER.info("createPortfolioDetail ended for user {}", userId);
		} else {
			LOGGER.error("Could not find any shares from API");
		}

		LOGGER.info("getPortfolioDetail has finished user {}, result size is: {}", userId, result.getPortfolioDetails().size());
		return result;
	}

}
