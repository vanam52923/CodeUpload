package com.tradestore;

import com.tradestore.controller.TradeStoreController;
import com.tradestore.exception.InvalidTradeException;
import com.tradestore.dto.TradeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeStoreApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private TradeStoreController tradeController;

	@Test
	void testValidateAndStoreTrade() {
		ResponseEntity responseEntity = tradeController.validateAndStoreTrade(createNewTrade("TRADE1",1,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<TradeDTO> tradeList =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("TRADE1",tradeList.get(0).getTradeId());
	}

	@Test
	void testTradeWithPastMaturityDate() {
		try {
			LocalDate localDate = getLocalDate(2016, 01, 01);
			ResponseEntity responseEntity = tradeController.validateAndStoreTrade(createNewTrade("TRADE2", 1, localDate));
		}catch (InvalidTradeException ie) {
			Assertions.assertEquals("Invalid Trade: TRADE2  Trade Id not found", ie.getMessage());
		}
	}

	@Test
	void testTradeWithOldVersionId() {

		ResponseEntity responseEntity = tradeController.validateAndStoreTrade(createNewTrade("TRADE1",2,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<TradeDTO> tradeList =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("TRADE1",tradeList.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList.get(0).getVersion());
		Assertions.assertEquals("TRADE1Bk1",tradeList.get(0).getBookId());


		try {
			ResponseEntity responseEntity1 = tradeController.validateAndStoreTrade(createNewTrade("TRADE1", 1, LocalDate.now()));


		}catch (InvalidTradeException e){
			System.out.println(e.getId());
			System.out.println(e.getMessage());
		}
		List<TradeDTO> tradeList1 =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList1.size());
		Assertions.assertEquals("TRADE1",tradeList1.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList1.get(0).getVersion());
		Assertions.assertEquals("TRADE1Bk1",tradeList.get(0).getBookId());
	}

	@Test
	void testTradeWithSameVersionTrade(){
		ResponseEntity responseEntity = tradeController.validateAndStoreTrade(createNewTrade("TRADE1",2,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<TradeDTO> tradeList =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("TRADE1",tradeList.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList.get(0).getVersion());
		Assertions.assertEquals("TRADE1Bk1",tradeList.get(0).getBookId());


		TradeDTO trade2 = createNewTrade("TRADE1",2,LocalDate.now());
		trade2.setBookId("TRADE1Bk2");
		ResponseEntity responseEntity2 = tradeController.validateAndStoreTrade(trade2);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity2);
		List<TradeDTO> tradeList2 =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList2.size());
		Assertions.assertEquals("TRADE1",tradeList2.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList2.get(0).getVersion());
		Assertions.assertEquals("TRADE1Bk2",tradeList2.get(0).getBookId());


		TradeDTO trade3 = createNewTrade("TRADE1",2,LocalDate.now());
		trade3.setBookId("TRADE1Bk3");
		ResponseEntity responseEntity3 = tradeController.validateAndStoreTrade(trade3);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity3);
		List<TradeDTO> tradeList3 =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList3.size());
		Assertions.assertEquals("TRADE1",tradeList3.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList3.get(0).getVersion());
		Assertions.assertEquals("TRADE1Bk3",tradeList3.get(0).getBookId());

	}
	private TradeDTO createNewTrade(String tradeId,int version,LocalDate  maturityDate){
		TradeDTO trade = new TradeDTO();
		trade.setTradeId(tradeId);
		trade.setBookId(tradeId+"Bk1");
		trade.setVersion(version);
		trade.setCounterPartyId(tradeId+"Cp1");
		trade.setMaturityDate(maturityDate);
		trade.setIsExpired('Y');
		return trade;
	}

	public static LocalDate getLocalDate(int year,int month, int day){
		LocalDate localDate = LocalDate.of(year,month,day);
		return localDate;
	}




}
