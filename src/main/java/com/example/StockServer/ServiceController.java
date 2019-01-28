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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.StockExchange.GenericStock;
import com.example.StockExchange.StockAvailable;
import com.example.StockExchange.StockExchange;
import com.example.StockExchange.StockMapper;
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

	
		
	}
	
	
	@GetMapping("/buy")
	@ResponseBody
	String  buyStocks(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock, @RequestParam("Number") Integer amount) {
		System.out.println("purchasing stock");
		Map<String, ?> ss = new HashMap<>();
		synchronized (this.limit) {
			String result;
			String stt = "Select Stock_number from hr.Stock where stock_name = " + "'"+stock+"'" + " AND exchange_name = "+  "'"+exchange+"'";
			
			List<StockAvailable> resultSet = namedParameterJdbcTemplate.query(stt, new StockMapper());
			Integer number = resultSet.get(0).getNumber();
			// check if user have this stock available 
			System.out.println("buying number::" + number);
			
			if (number >= amount) {
				number = number - amount;
				System.out.println("Success remaining ::" +number);
				result = "Success remaining ::" + number;
				
				String stt2 = "UPDATE hr.Stock	SET Stock_number = " +number+	" where stock_name = " + "'"+stock+"'" + " AND exchange_name = "+  "'"+exchange+"'";
				namedParameterJdbcTemplate.update(stt2, ss);
				
				// Updating purchase record
				String stt3 = "Select Stock_number from hr.records where stock_name = " + "'"+stock+"'" + " AND exchange_name = "+  "'"+exchange+"'"+ " AND username = "+  "'"+username+"'";
				
				List<StockAvailable> resultSet2 = namedParameterJdbcTemplate.query(stt3, new StockMapper());
				
				if (resultSet2.size() > 0) {
					int updatedStock = resultSet2.get(0).getNumber() + amount; 
					//already exist hence updating
					String stt4 = "UPDATE hr.records SET Stock_number = " +updatedStock+	" where stock_name = " + "'"+stock+"'" + " AND exchange_name = "+  "'"+exchange+"'"+ " AND username = "+  "'"+username+"'";
					System.out.println(stt4);
					namedParameterJdbcTemplate.update(stt4, ss);
					}
				else
				{
					// first entry for given stock
					String stt5 = "insert into hr.records (records_id, username, exchange_name,stock_name,Stock_number ) values(hr.records_id_sequence.nextval, '" + 
							username+"','" +exchange+"', '" +stock+"', " + amount+ ")";
					System.out.println(stt5);
					namedParameterJdbcTemplate.update(stt5, ss);
				}
				
			}
			else if (number == 0 ) {
				System.out.println("failed no stock left");
				result = "Failed no stock left";
			}
			else {
				System.out.println("failed please select less stock ");
				result ="Failed please select less stock ";
			}
			
			return result;
		}
		

	}
	
	
	
	@GetMapping("/sell")
	@ResponseBody
	//@SendTo("/topic/sell")
	String sellStocks(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock, @RequestParam("Number") Integer amount) {
		System.out.println("Selling stock");
		Map<String, ?> ss = new HashMap<>();
		
		synchronized (this.limit) {
			
		
			String stt = "Select Stock_number from hr.records where stock_name = " + "'"+stock+"'" + " AND exchange_name = "+  "'"+exchange+"'"+ " AND username = "+  "'"+username+"'";
			
			List<StockAvailable> resultSet = namedParameterJdbcTemplate.query(stt, new StockMapper());
			if (resultSet.size() > 0) {
				Integer number = resultSet.get(0).getNumber();

				if (amount > number) {
					return "Not enough stock please select less stock";
				}
				else {
					number = number-amount;
					String stt6 = "UPDATE hr.records SET Stock_number = " +number+	" where stock_name = " + "'"+stock+"'" + " AND exchange_name = "+  "'"+exchange+"'"+ " AND username = "+  "'"+username+"'";
					System.out.println(stt6);
					namedParameterJdbcTemplate.update(stt6, ss);
					
					
					String stt7 = "Select Stock_number from hr.Stock where stock_name = " + "'"+stock+"'" + " AND exchange_name = "+  "'"+exchange+"'";
					
					List<StockAvailable> resultSet3 = namedParameterJdbcTemplate.query(stt7, new StockMapper());
					
					Integer updateValue = resultSet3.get(0).getNumber() + amount;
					String stt8 = "UPDATE hr.Stock	SET Stock_number = " +updateValue+	" where stock_name = " + "'"+stock+"'" + " AND exchange_name = "+  "'"+exchange+"'";
					namedParameterJdbcTemplate.update(stt8, ss);
					
					return "Selling successful remaining stock on you :::" + number;
					
				}
			}
			else {
				
				return "Not enough stock on you";
			}
		

		}

	}
	
//	@RequestMapping("/login")
//	String login() {
//		System.out.println("login success");
//		return "login.html";
//	}
	

	@GetMapping("/login")
	@ResponseBody
	public String validate(  @RequestParam("UserName") String username, @RequestParam("Password") String pass) {
		System.out.println("validating user");
		
		String stt = "Select * from hr.Login where username = " + "'"+username.toLowerCase()+"'" + " AND password = "+  "'"+pass+"'";
		
		List<User> resultSet = namedParameterJdbcTemplate.query(stt, new UserRowMapper());
		
		
		if (resultSet.size() == 0  ) {
			return "Invalid Username or Password";
		}
		else if (resultSet.get(0).getUser().equalsIgnoreCase(username) && resultSet.get(0).getPassword().equals(pass)) {
			return "Login success";
		}
		else if (!resultSet.get(0).getPassword().equals(pass)) {
			return "Incorrect Password";
		}
		else {
			return "Invalid Username or Password";
		}
	}
	
	
	
}
