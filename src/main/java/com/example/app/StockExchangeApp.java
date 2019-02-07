package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;


/**
 * This is the main app class.
 * @author nikhil.singhal
 *
 */


@SpringBootApplication
@ConfigurationProperties("oracle")	
@ComponentScan({ "com.example.*"})
@Controller
public class StockExchangeApp  {
	
	
	public static void main(String[] args) {
		SpringApplication.run(StockExchangeApp.class, args);
		
	}

	
	
}
