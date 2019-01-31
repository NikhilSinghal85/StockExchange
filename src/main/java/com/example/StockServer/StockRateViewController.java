package com.example.StockServer;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.example.StockExchange.StockUtility;


@Component
public class StockRateViewController {
	
	private Logger logger = LoggerFactory.getLogger(StockRateViewController.class);
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	private DaoImpl daoImpl;
	
	
	@SendTo("/topic/stocks")
	public void refreshStockRate() {
		try {

			Thread t = new Thread(() -> {
				DecimalFormat df = new DecimalFormat("#.####");
				while(true)
				{
					double d1= daoImpl.randomNumberGenerator();
					double d2= daoImpl.randomNumberGenerator();
					double d3= daoImpl.randomNumberGenerator();
					double d4= daoImpl.randomNumberGenerator();
					double d5= daoImpl.randomNumberGenerator();
					double d6= daoImpl.randomNumberGenerator();

					StockUtility gg = new StockUtility();
					gg.setNse_CoalIndia(df.format(d1));
					gg.setNse_Idea(df.format(d2));
					gg.setNse_Tata(df.format(d3));
					gg.setBse_CoalIndia(df.format(d4));
					gg.setBse_Idea(df.format(d5));
					gg.setBse_Tata(df.format(d6));
					messagingTemplate.convertAndSend("/topic/stocks", gg);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {      
					}
				}
			});
			t.start();

		}
		catch(NullPointerException e) {
			logger.error("Invalid request made");
		}
	}
	
	

}
