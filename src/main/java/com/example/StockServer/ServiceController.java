package com.example.StockServer;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.StockExchange.GenericStock;
import com.example.StockExchange.StockExchange;
import com.example.StockExchange.User;
import com.example.StockExchange.UserRowMapper;


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
	
	@Autowired
	private NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate  jdbcTemplate;
	
	
	private static volatile Integer limit = 5;
	
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
	void displayExchange() {

		try {

			Thread t = new Thread(() -> {
				DecimalFormat df = new DecimalFormat("#.####");
				while(true)
				{
					double d1=randomNumberGenerator();
					double d2=randomNumberGenerator();
					double d3=randomNumberGenerator();
					double d4=randomNumberGenerator();
					double d5=randomNumberGenerator();
					double d6=randomNumberGenerator();
					
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
	
	
	private double randomNumberGenerator() {
		return 2+Math.random();// + ((Integer.parseInt(dd[1].trim())) * .001);
	}
	
	private void registerUserRequest(StockMessage message) {
		logger.info("Entering user request in DB for tracking");
		
		String query = "INSERT INTO stockexchange.user (Timestamp, UserName, Data) VALUES (:Timestamp,:UserName,:Data)";
//		Map<String, Object> namedParameters = new HashMap<>();
//		namedParameters.put("Timestamp", LocalDateTime.now());
//		namedParameters.put("UserName", "Nikhil");
//		namedParameters.put("Data", message.getName() + " ::" + message.getValue() );
//		try {
//		namedParameterJdbcTemplate.update(query, namedParameters);
//		}
//		catch (Exception e) {
//			System.out.println("Exception :: " + e.getMessage());
//		}
	
		
	}
	
	
	
	@RequestMapping("/buy")
	void buyStocks(StockMessage message) {
		System.out.println("purchasing stock");
		Map<String, ?> ss = new HashMap<>();
		synchronized (this.limit) {
			String stt = "Select Stock_Number from Stock where 'Stock.name' = " + message.getStock() + " AND 'Stock.exchange' = " +  message.getExchange();
			List<Map<String, Object>> resultSet = namedParameterJdbcTemplate.queryForList(stt, ss);
			
			if (limit >= message.getNumber()) {
				limit = limit - message.getNumber();
				System.out.println("Success");
			}
			else if (limit == 0 ) {
				System.out.println("failed no stock left");
			}
			else {
				System.out.println("failed please select less stock ");
			}
			
			
		}

	}
	
	
	
	@RequestMapping("/sell")
	//@SendTo("/topic/sell")
	void sellStocks(StockMessage message) {
		System.out.println("Selling stock");
		Map<String, ?> ss = new HashMap<>();
		
		synchronized (this.limit) {
			String stt = "Select Stock_Number from Stock where 'Stock.name' = " + message.getStock() + " AND 'Stock.exchange' = " +  message.getExchange();
			List<Map<String, Object>> resultSet = namedParameterJdbcTemplate.queryForList(stt, ss);
			// check if user have this stock available 
			System.out.println("Success");
			
			// if not this much stock available
			System.out.println("failed you dont have enough stock number");

		}

	}
	
	@RequestMapping("/login")
	String login() {
		System.out.println("login success");
		return "login.html";
	}
	
	//@RequestMapping("/validate"  @RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	//@RequestMapping(method=RequestMethod.POST, path = "/{id}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	@RequestMapping(value ="/validate" ,method = RequestMethod.POST, consumes = "application/json")
	@RequestMapping("/validate")
	@ResponseBody
	String validate(@RequestBody User user) {
		System.out.println("validating user");
		String username = user.getUser();
		String password = user.getPassword();
		System.out.println("username ::" + username);
		System.out.println("password ::" + password);
		String getUser = "Select * from User where User.name = " +  username;
		
		UserRowMapper um = new UserRowMapper();
		List<User>  ll = namedParameterJdbcTemplate.query(getUser, um);
		
		if (ll.size() == 0 ) {
			return "invalid user";
		}
		else if (ll.get(0).getPassword().equals(password)) {
			return "login success";
		}
		else {
			return "Incorrect Password";
		}
	}
	
	
	
}
