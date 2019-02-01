package com.example.StockServer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

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

import com.example.StockExchange.ExchangeType;
import com.example.StockExchange.GenericStock;
import com.example.StockExchange.StockExchange;


/**
 * This is the main controller class will receive and process any incoming request from the client.
 * @author nikhil.singhal
 *
 */


@SpringBootApplication
@ConfigurationProperties("oracle")	
@ComponentScan({ "com.example.*"})
@Controller
public class StockServiceController extends SpringBootServletInitializer {
	
	@Autowired
	private GenericStock genericStock;
	
	
	@Autowired
	private DaoImpl daoImpl;
	
	@Autowired
	private StockRateViewController stockRateViewController;
	
	private static Map<String, StockExchange> EXHANGE_REPOSITORY = new HashMap<>();
	
	private Logger logger = LoggerFactory.getLogger(StockServiceController.class);
	
	@PostConstruct
	private void loadAvailableStock() {
		EXHANGE_REPOSITORY.put("genericStock", genericStock);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(StockServiceController.class, args);
		
	}

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
		// check if user have this stock available 
		try  {
			String result = daoImpl.updateBuy(username,ExchangeType.valueOf(exchange).toString(),stock,quantity );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
	}
	
	
	
	@GetMapping("/sell")
	@ResponseBody
	String sellStocks(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock,
			@RequestParam("Quantity") Integer quantity) {
		logger.info("Selling stock");
		try  {
			String result = daoImpl.updateSell(username,ExchangeType.valueOf(exchange).toString(),stock,quantity );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
		

	}
	
	
	@GetMapping("/history")
	@ResponseBody
	String averageHistory(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock,
			@RequestParam("Quantity") Integer quantity) {
		logger.info("Average history");
		try  {
			String result = daoImpl.averageHistory(username,ExchangeType.valueOf(exchange).toString(),stock,quantity );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
		

	}
	

	@GetMapping("/login")
	@ResponseBody
	public String validateUser(  @RequestParam("UserName") String username, @RequestParam("Password") String pass) {
		logger.info("validating user");
		String result = daoImpl.loginValidation(username,pass );
		return result;
		
	}
	
	
	
}