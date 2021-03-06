package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.StockExchangeService;


/**
 * This is the main controller class will receive and process any incoming request from the client.
 * @author nikhil.singhal
 *
 */


@RestController
public class StockServiceController {
	
	
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
		String temp = stockExchangeService.updateBuy(username, exchange, stock, quantity);
		System.out.println(temp);
		return temp;
		
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
	
	
	/*//@RequestMapping(value = "/upload", method = RequestMethod.POST)
//	@PostMapping(path = "/upload", consumes = "application/vnd.ms-excel", produces = "application/json")
	@GetMapping("/upload")
	public ResponseEntity<?> uploadRecord(@RequestPart("file") MultipartFile file) throws IOException {
		logger.info("uploading ...");
		stockExchangeService.uploadRecord(file);
		
		return ResponseEntity.ok().build();
	}*/
	
	
	@PostMapping("/upload")
	public ResponseEntity<?> uploadRecord(@RequestParam(name="reqFile") MultipartFile file) throws IOException {
		logger.info("uploading ...");
		String result = stockExchangeService.uploadRecord(file);
		if (result.equals("Invalid File")) {
			return ResponseEntity.badRequest().body("Excel file is in incorrect format");
		}
		else {
			return ResponseEntity.ok().body("Report successfully uploaded");
		}
		
	}
	
	@GetMapping("/download")
	public ResponseEntity<?> downloadRecord(@RequestParam(name="class") String cls) throws IOException {
		logger.info("download ...");
		String result = stockExchangeService.downloadRecord(cls);
//		URI path = null;
//		try {
//			path = new URI(result);
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if (result.equals("Invalid File")) {
			return ResponseEntity.badRequest().body("Failed to Download");
		}
		else {
			return ResponseEntity.ok().body("File at location:: " +result);
		}
		
	}
	
	@GetMapping("/version")
    public String versionInformation() {
        return readGitProperties();
    }
	
	

private String readGitProperties() {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("git.properties");
    try {
        return readFromInputStream(inputStream);
    } catch (IOException e) {
        e.printStackTrace();
        return "Version information could not be retrieved";
    }
}
private String readFromInputStream(InputStream inputStream)
throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        while ((line = br.readLine()) != null) {
            resultStringBuilder.append(line).append("\n");
        }
    }
    return resultStringBuilder.toString();
}
	
}
