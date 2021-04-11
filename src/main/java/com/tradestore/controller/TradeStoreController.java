package com.tradestore.controller;

import com.tradestore.exception.InvalidTradeException;
import com.tradestore.dto.TradeDTO;
import com.tradestore.service.TradeStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeStoreController {

     @Autowired
     TradeStoreService tradeStoreService;

     @PostMapping("/tradeStoreAdd")
    public ResponseEntity<String> validateAndStoreTrade(@RequestBody TradeDTO trade){
       if(tradeStoreService.isValidTrade(trade)) {
           tradeStoreService.persist(trade);
       }else{
           throw new InvalidTradeException(trade.getTradeId() + "  Trade Id not found");
       }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/tradeStoreFetch")
    public List<TradeDTO> findAllTrades(){
        return tradeStoreService.findAll();
    }
}
