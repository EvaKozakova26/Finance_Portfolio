# Finance portfolio app server
Backend app for Finance portfolio app UI </b>
This app provides investing data about shares, indexes and cryptocurrencies. It is divided into several layers including feching data from open APIs to catch current price data about each asset, processing these data and providing it in form of json objects to be easily read by client application.
## Overview

Used technologies:
- Spring (Java)
- Retrofit for fetching and processing API
- MongoDB Atlas for database
- Automatic deploy on Heroku

## Architecture
![now](https://github.com/EvaKozakova26/Finance_Portfolio/blob/master/sources/architecture.png "Now")<br/>

## API configuration
// TODO... 

## Controllers

### AssetController
API for manipulating with assets
| HTTP method | url              | method name           |return type           
|-------------|------------------|-----------------------|-------------|         
| GET         | assets/{userId} | getAssetsData |AssetDataListEntity

### PortfolioDetailController
API for retrieving porfolio detail data
| HTTP method | url              | method name           |return type           
|-------------|------------------|-----------------------|-------------|         
| GET         | /detail/{userId} | getPortfolioDetails |PortfolioDetailListEntity           

### TransactionsController
API for manipulating with transactions

## Services
### AssetApiService
Methods using with retrofit to fetch API data
- Call<BtcInfoDto> <b>getBtcPriceNow();</b> - <i> - gets data about current bitcoin price</i>
- Call<ForexDataDto> <b>getForexData(@Path(value = "date", encoded = true) String date);</b> - <i>gets data about currency value at specific date</i>
- Call<SharesDto> <b>getSharesData(@Path(value = "code", encoded = true) String code);</b> - <i> gets data about specific shares (by its code)</i>
### SharesService
 - PortfolioDetailListEntity <b>getPortfolioDetail(String userId);</b><i> - processes and return portfolio detail data</i>
 
### BtcService
 // methods...
### SharesService
 // methods...

## Helpers
### AssetDataHelper
Helper providing methods for processing asset data
 * public BigDecimal <b>getTotal</b>(List<Transaction> allByUserId, String type, Function<Transaction, BigDecimal> function) - <i>sums all values defined by given function (see example below)</i>
   *  ```assetDataHelper.getTotal(allByTypeAndUserId, sharesMeta.getSymbol(), Transaction::getTransactionValueInCrowns))```
 * public AssetRate <b>getBtcBalance</b>(BtcInfoDto btcInfoDto, BigDecimal totalAmount, CurrencyEnum currency) - <i>transforms btc information into AssetRate object</i>
 * public AssetRate <b>getShareBalance</b>(SharesDto sharesDto, BigDecimal totalAmount, CurrencyEnum currency) - <i>transforms shares information into AssetRate object</i>

### PortfolioDetailHelper
 * public PortfolioDetailListEntity <b>createPortfolioDetail</b>(SharesDto sharesDto, AssetDataListEntity assetData, List<Transaction> allByUserId) - <i>creates portfolio detail from given information and transforms it into PortfolioDetailListEntity object </i>
 
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
 
 ### PortfolioDetail
```json
{
   "symbol": "BTC",
   "fullName": "Bitcoin",
   "sharePercentageHistoric": "27",
   "sharePercentageCurrent": "35",
}
```
 ### SharesDto
```json
{
   "chart": {
    "result": [{
       "meta": {
        "currency": "CZK",
        "symbol": "CEZ.PR",
        "exchangeName": "PRA",
        "regularMarketPrice": 638.0
       },
     }],
   }
}
```
  
