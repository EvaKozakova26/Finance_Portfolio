# Finance portfolio app server
Backend app for Finance portfolio app UI

## Overview

Used technologies:
- Spring (Java)
- Retrofit for fetching and processing API
- MongoDB Atlas for database
- Automatic deploy on Heroku

## Architecture
![now](https://github.com/EvaKozakova26/Finance_Portfolio/blob/master/sources/architecture.png "Now")<br/>

## Summary

## API configuration
// references to APIs

## Controllers

### StockController
API for manipulating with assets

## Services
### AssetApiService
Methods using with retrofit to fetch API data
- Call<BtcInfoDto> <b>getBtcPriceNow();</b> - <i> - gets data about current bitcoin price</i>
- Call<ForexDataDto> <b>getForexData(@Path(value = "date", encoded = true) String date);</b> - <i>gets data about currency value at specific date</i>
- Call<SharesDto> <b>getSharesData(@Path(value = "code", encoded = true) String code);</b> - <i> gets data about specific shares (by its code)</i>
### BtcService
 // methods...
### SharesService
 // methods...

## Helpers
### AssetDataHelper
Helper providing methods for processing asset data
 - public BigDecimal <b>getInvestedCrowns</b>(List<CryptoTransaction> allByUserId, String type) - <i>sums all transactions values in crowns</i>
 - public BigDecimal <b>getTotalAmount</b>(List<CryptoTransaction> allByUserId, String type) - <i>sums all purchased assets</i>
 - public AssetRate <b>getBtcBalance</b>(BtcInfoDto btcInfoDto, BigDecimal totalAmount, CurrencyEnum currency) - <i>transforms btc information into AssetRate object</i>
 - public AssetRate <b>getShareBalance</b>(SharesDto sharesDto, BigDecimal totalAmount, CurrencyEnum currency) - <i>transforms shares information into AssetRate object</i>
  
## DTOs
### AssetData
```json
{
   "type": "crypto",
   "symbol": "BTC",
   "assetRateList": [
    {
      "currency": "USD",
      "price": "20000",
      "accBalance": "1000",
    }
  ],
  "assetBalance": "0.002",
  "investedInCrowns": "25000",
}
```
  
