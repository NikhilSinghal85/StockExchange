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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.StockExchange.ExchangeType;
import com.example.StockExchange.GenericStock;


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
	
	
	private Logger logger = LoggerFactory.getLogger(StockServiceController.class);
	
	
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
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				if (username.trim().equals("")) {
					 username = ((UserDetails)principal).getUsername();
				}
			} else {
				if (username.trim().equals("")) {
					 username = ((UserDetails)principal).getUsername();
				}
			}
			
			String result = daoImpl.updateBuy(username,ExchangeType.valueOf(exchange).getExchangeType(),stock,quantity );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
	}
	
	// todo move user fetch code in a method for reuse
	
	@GetMapping("/sell")
	@ResponseBody
	String sellStocks(@RequestParam("UserName") String username, @RequestParam("Exchange") String exchange, @RequestParam("Stock") String stock,
			@RequestParam("Quantity") Integer quantity) {
		logger.info("Selling stock");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			if (username.trim().equals("")) {
				 username = ((UserDetails)principal).getUsername();
			}
		} else {
			if (username.trim().equals("")) {
				 username = ((UserDetails)principal).getUsername();
			}
		}
		try  {
			String result = daoImpl.updateSell(username,ExchangeType.valueOf(exchange).getExchangeType(),stock,quantity );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
	}
	
	
	@GetMapping("/history")
	@ResponseBody
	String averageBuyHistory(@RequestParam("average") Integer average) {
		logger.info("Average history");
		try  {
			String result = daoImpl.averageHistory(average );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
		

	}
	
	
	@GetMapping("/topTransaction")
	@ResponseBody
	String topTransaction(@RequestParam("position") Integer position) {
		logger.info("top Transaction");
		try  {
			String result = daoImpl.topTransaction(position );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Not done these many transaction";
		}
		

	}
	
	@GetMapping("/buySellHistory")
	@ResponseBody
	String buySellHistory(@RequestParam("sdate") String sdate, @RequestParam("edate") String edate, @RequestParam("buysell") String buysell ) {
		
		if (sdate.compareTo(edate) > 0) {
			return "End date cannt be less than Start date";
		}
		
		try  {
		
//		StringTokenizer sdated = new StringTokenizer(sdate, "-");
//		
//		StringTokenizer edated = new StringTokenizer(edate, "-");
//		
//		String sday = sdated.nextToken();
//		String smonth = sdated.nextToken();
//		String syear = sdated.nextToken();
//		
//		String eday = edated.nextToken();
//		String emonth = edated.nextToken();
//		String eyear = edated.nextToken();
//	
//		
//		LocalDate lstart =  LocalDate.of(Integer.parseInt(syear), Integer.parseInt(smonth), Integer.parseInt(sday));
//		LocalDate lend =  LocalDate.of(Integer.parseInt(eyear), Integer.parseInt(emonth), Integer.parseInt(eday));
		
		
		
		logger.info("buySellHistory " + sdate + "::" +edate);
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
				 username = ((UserDetails)principal).getUsername();
		} else {
				 username = ((UserDetails)principal).getUsername();
		}
		
	
			String result = daoImpl.buySellHistory(username, sdate, edate, buysell );
			logger.info("sending result ::" + result);
			return result;
		}
		catch (Exception  e) {
			return "Incorrect Date Format";
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
