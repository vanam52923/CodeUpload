package com.tradestore.service;

import com.tradestore.dao.TradeRepository;
import com.tradestore.dto.TradeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeStoreService {

    private static final Logger log = LoggerFactory.getLogger(TradeStoreService.class);

    @Autowired
    TradeRepository tradeRepository;

    public boolean isValidTrade(TradeDTO trade){
        if(validateMaturityDate(trade)) {
            Optional<TradeDTO> existingTrade = tradeRepository.findById(trade.getTradeId());
            return existingTrade.map(tradeDTO -> validateVersionId(trade, tradeDTO)).orElse(true);
         }
         return false;
    }

    private boolean validateVersionId(TradeDTO trade,TradeDTO oldTrade) {
        return trade.getVersion() >= oldTrade.getVersion();
      }


    private boolean validateMaturityDate(TradeDTO trade){
        return !trade.getMaturityDate().isBefore(LocalDate.now());
    }

    public void  persist(TradeDTO trade){
        trade.setCreatedDate(LocalDate.now());
        tradeRepository.save(trade);
    }

    public List<TradeDTO> findAll(){
       return  tradeRepository.findAll();
        }

    public void updateExpiryFlagOfTrade(){

        tradeRepository.findAll().forEach(fetchedTrade->{
                if (!validateMaturityDate(fetchedTrade)) {
                fetchedTrade.setIsExpired('Y');
                log.info("Trade updated {}", fetchedTrade);
                tradeRepository.save(fetchedTrade);
            }
        });
              }


}
