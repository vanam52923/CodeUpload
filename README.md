
TradeStore Application

To store trades: 

http://localhost:8080/tradeStoreAdd

application/json:

{
  "tradeId": "TR1",
  "version": 1,
  "counterPartyId": "counterParty1",
  "bookId": "bookId1",
  "maturityDate": "2021-08-20",
  "isExpired": "N"
}

To fetch trades:

http://localhost:8080/tradeStoreFetch
