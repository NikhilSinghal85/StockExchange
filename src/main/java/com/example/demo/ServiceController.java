package com.example.demo;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * This is the main controller class will receive and process any incoming request from the client.
 * @author nikhil.singhal
 *
 */


@SpringBootApplication
@EnableJms
@RestController
public class ServiceController extends SpringBootServletInitializer {
	
	@Autowired
	private NSE nse;
	
	@Autowired
	private BSE bse;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	static boolean flag = false;
	
	private static Map<String, StockExchange> EXHANGE_REPOSITORY = new HashMap<>();
	
	private Logger logger = LoggerFactory.getLogger(ServiceController.class);
	
	@PostConstruct
	private void loadAvailableStock() {
		EXHANGE_REPOSITORY.put("nse", nse);
		EXHANGE_REPOSITORY.put("bse", bse);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceController.class, args);
		
	}

	@MessageMapping("/stock")
    @SendTo("/topic/stocks")
	void getResult(StockMessage message) {
		
		logger.info("This is the log ::");
		try {
			StockExchange se = EXHANGE_REPOSITORY.get(message.getName().toLowerCase().trim());
			String val = se.getResult(message.getValue().toLowerCase().trim());
			
	        
	        Thread  rateThread=new Thread(){
	        	   public void run() {
	        	    DecimalFormat df = new DecimalFormat("#.####");
	        	    while(true)
	        	    {
	        	    	
	        	     double d=randomNumberGenerator();
	        	     Stocks gg = new Stocks();
	        	    	   gg.setContent(val + " "+df.format(d));
	        	    	   messagingTemplate.convertAndSend("/topic/stocks", gg);
	        	     try {
	        	      sleep(3000);
	        	     } catch (InterruptedException e) {      
	        	     }
	        	    }
	        	   };
	        	  } ;
	        	  rateThread.start();
			
		}
		catch(NullPointerException e) {
			logger.error("Invalid request made");
		}
	}
	
	
	private double randomNumberGenerator() {
		List<Map<String, Object>> generator = jdbcTemplate.queryForList("Select * from generator");
		Collection<Object> val = generator.get(0).values();
		String vv = val.toString();
		String[] dd = vv.split(",");
		return 2+Math.random() + ((Integer.parseInt(dd[1].trim())) * .001);
	}
	
	private void registerUserRequest(StockMessage message) {
		logger.info("Entering user request in DB for tracking");
		jdbcTemplate.update("INSERT INTO stockexchange.user (Timestamp, UserName, data) VALUES ( ?, ?, ?)"
				,  LocalDateTime.now(), "Nikhil", message.getName() + " ::" + message.getValue()  );
		
	}
	
	
	
	//do later
	@PostMapping("/order")
	void setResult(@RequestParam("value") String val) {
		// to do code to save values later

	}
	
	
	
	
	
	
	
}
