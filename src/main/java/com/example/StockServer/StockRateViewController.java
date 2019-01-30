package com.example.StockServer;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class StockRateViewController {
	
private static StockRateViewController singelton = new StockRateViewController();
	
	private Logger logger = LoggerFactory.getLogger(StockRateViewController.class);
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	public static StockRateViewController getInstance() {
		return singelton;
	}
	
	private StockRateViewController() {
		
	}
	
	
	@SendTo("/topic/stocks")
	public void refreshStockRate() {
		try {

			Thread t = new Thread(() -> {
				DecimalFormat df = new DecimalFormat("#.####");
				while(true)
				{
					double d1= DaoImpl.randomNumberGenerator();
					double d2= DaoImpl.randomNumberGenerator();
					double d3= DaoImpl.randomNumberGenerator();
					double d4= DaoImpl.randomNumberGenerator();
					double d5= DaoImpl.randomNumberGenerator();
					double d6= DaoImpl.randomNumberGenerator();

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
