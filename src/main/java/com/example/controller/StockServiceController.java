package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.StockExchangeService;


/**
 * This is the main controller class will receive and process any incoming request from the client.
 * @author nikhil.singhal
 *
 */


@Controller
public class StockServiceController extends SpringBootServletInitializer {
	
	
	@Autowired
	private StockExchangeService stockExchangeService;
	
	
	@Autowired
	private StockRateViewController stockRateViewController;
	
	
	private Logger logger = LoggerFactory.getLogger(StockServiceController.class);
	

	@MessageMapping("/stock")
    @SendTo("/topic/stocks")
	void displayExchange() {
		stockRateViewController.refreshStockRate();
		
	}
	
	
	@GetMapping("/buy")
	@ResponseBody
	String  buyStocks(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock,
			@RequestParam("Quantity") Integer quantity) {
		logger.info("purchasing stock");
		return stockExchangeService.updateBuy(username, exchange, stock, quantity);
		
	}
	
	// todo move user fetch code in a method for reuse
	
	@GetMapping("/sell")
	@ResponseBody
	String sellStocks(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock,
			@RequestParam("Quantity") Integer quantity) {
		logger.info("Selling stock");
		
		return stockExchangeService.updateSell(username, exchange, stock, quantity);
	}
	
	
	@GetMapping("/history")
	@ResponseBody
	String averageBuyHistory(@RequestParam("average") Integer average) {
		logger.info("Average history");
		return	stockExchangeService.averageHistory(average );


	}
	
	
	@GetMapping("/topTransaction")
	@ResponseBody
	String topTransaction(@RequestParam("position") Integer position) {
		logger.info("top Transaction");
			return stockExchangeService.topTransaction(position );

	}
	
	@GetMapping("/buySellHistory")
	@ResponseBody
	String buySellHistory(@RequestParam("sdate") String sdate, @RequestParam("edate") String edate, @RequestParam("buysell") String buysell ) {
		
		logger.info("buySellHistory " + sdate + "::" +edate);
		
		return stockExchangeService.buySellHistory(sdate, edate, buysell);
		

	}
	

	@GetMapping("/login")
	@ResponseBody
	public String validateUser(  @RequestParam("UserName") String username, @RequestParam("Password") String pass) {
		logger.info("validating user");
		return stockExchangeService.loginValidation(username,pass );
		
	}
	
	
	
}
