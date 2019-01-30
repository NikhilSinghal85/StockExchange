package com.example.StockServer;

import java.text.DecimalFormat;
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
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class ServiceController extends SpringBootServletInitializer {
	
	@Autowired
	private GenericStock genericStock;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	
	private static Map<String, StockExchange> EXHANGE_REPOSITORY = new HashMap<>();
	
	private Logger logger = LoggerFactory.getLogger(ServiceController.class);
	
	@PostConstruct
	private void loadAvailableStock() {
		EXHANGE_REPOSITORY.put("genericStock", genericStock);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceController.class, args);
		
	}

	@MessageMapping("/stock")
    @SendTo("/topic/stocks")
	void displayExchange() {
		
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
	
	
	@GetMapping("/buy")
	@ResponseBody
	String  buyStocks(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock,
			@RequestParam("Quantity") Integer quantity) {
		logger.info("purchasing stock");
			// check if user have this stock available 
			logger.info("Buying quantity::" + quantity);
			String result = DaoImpl.getInstance().updateBuy(username,exchange,stock,quantity );
			return result;
	}
	
	
	
	@GetMapping("/sell")
	@ResponseBody
	String sellStocks(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock,
			@RequestParam("Quantity") Integer quantity) {
		logger.info("Selling stock");
		String result = DaoImpl.getInstance().updateSell(username,exchange,stock,quantity );
		return result;

	}
	

	@GetMapping("/login")
	@ResponseBody
	public String validateUser(  @RequestParam("UserName") String username, @RequestParam("Password") String pass) {
		logger.info("validating user");
		String result = DaoImpl.getInstance().loginValidation(username,pass );
		return result;
		
	}
	
	
	
}
