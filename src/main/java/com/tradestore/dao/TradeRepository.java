package com.tradestore.dao;


import com.tradestore.dto.TradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<TradeDTO,String> {
}
