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
### AssetService

## Helpers
### AssetDataHelper
Helper providing methods for processing asset data
 - public BigDecimal <b>getInvestedCrowns</b>(List<CryptoTransaction> allByUserId, String type) - sums all transactions values in crowns
 - public BigDecimal <b>getTotalAmount</b>(List<CryptoTransaction> allByUserId, String type) - sums all purchased assets
 - public AssetRate <b>getBtcBalance</b>(BtcInfoDto btcInfoDto, BigDecimal totalAmount, CurrencyEnum currency) - transforms btc information into AssetRate object
 - public AssetRate <b>getShareBalance</b>(SharesDto sharesDto, BigDecimal totalAmount, CurrencyEnum currency) - transforms shares information into AssetRate object
  
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
  
