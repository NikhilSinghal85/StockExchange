package com.example.StockServer;

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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.StockExchange.GenericStock;
import com.example.StockExchange.StockExchange;


/**
 * This is the main controller class will receive and process any incoming request from the client.
 * @author nikhil.singhal
 *
 */


@SpringBootApplication
@ComponentScan({ "com.example.*"})
@EnableJms
@RestController
public class ServiceController extends SpringBootServletInitializer {
	
	@Autowired
	private GenericStock genericStock;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
	
	static boolean flag = false;
	
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
	void getResult(StockMessage message) {
		try {
			String val = genericStock.getResult(message.getName().toLowerCase().trim(), 
					message.getValue().toLowerCase().trim());
			registerUserRequest(message);
			
			Thread t = new Thread(() -> {
				DecimalFormat df = new DecimalFormat("#.####");
				while(true)
				{
					double d=randomNumberGenerator();
					Stocks gg = new Stocks();
					gg.setContent(val + " "+df.format(d));
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
	
	
	private double randomNumberGenerator() {
		Map<String, Object> namedParameters = new HashMap<>();
		List<Map<String, Object>> generator = namedParameterJdbcTemplate.queryForList("Select * from generator", namedParameters);
		Collection<Object> val = generator.get(0).values();
		String vv = val.toString();
		String[] dd = vv.split(",");
		return 2+Math.random() + ((Integer.parseInt(dd[1].trim())) * .001);
	}
	
	private void registerUserRequest(StockMessage message) {
		logger.info("Entering user request in DB for tracking");
		
		String query = "INSERT INTO stockexchange.user (Timestamp, UserName, Data) VALUES (:Timestamp,:UserName,:Data)";
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("Timestamp", LocalDateTime.now());
		namedParameters.put("UserName", "Nikhil");
		namedParameters.put("Data", message.getName() + " ::" + message.getValue() );
		try {
		namedParameterJdbcTemplate.update(query, namedParameters);
		}
		catch (Exception e) {
			System.out.println("Exception :: " + e.getMessage());
		}
	
		
	}
	
	
	
	//do later
	@PostMapping("/order")
	void setResult(@RequestParam("value") String val) {
		// to do code to save values later

	}
	
	
	
//	@Bean
//	  public static PropertySourcesPlaceholderConfigurer properties() {
//	      PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
//	      YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//	      yaml.setResources(new ClassPathResource("appConfig.yml"));
//	      propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
//	      return propertySourcesPlaceholderConfigurer;
//	  }
	
	
	
}
