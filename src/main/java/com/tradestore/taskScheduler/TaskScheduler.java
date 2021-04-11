
package com.tradestore.taskScheduler;

import com.tradestore.service.TradeStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TaskScheduler {

	private static final Logger log = LoggerFactory.getLogger(TaskScheduler.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	TradeStoreService tradeStoreService;

	@Scheduled(cron = "${trade.expiry.triggerInterval}")
	public void reportCurrentTime() {
		log.info("Expiry Check at {}", dateFormat.format(new Date()));
		tradeStoreService.updateExpiryFlagOfTrade();
		log.info("Expiry Check Completed at {}", dateFormat.format(new Date()));
	}
}