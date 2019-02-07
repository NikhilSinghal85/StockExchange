package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.dao.DaoStockExchange;
import com.example.model.ExchangeType;
import com.example.service.StockExchangeService;

@Component
public class StockExchangeServiceImpl implements StockExchangeService {

	
	@Autowired
	private DaoStockExchange dataExchangeDao;
	
	@Override
	public String updateBuy(String username, String exchange, String stock, Integer quantity) {
		
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
			
			String result = dataExchangeDao.updateBuy(username,ExchangeType.valueOf(exchange).getExchangeType(),stock,quantity );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
	}

	@Override
	public String topTransaction(Integer position) {
		try  {
			String result = dataExchangeDao.topTransaction(position );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Not done these many transaction";
		}
	}

	@Override
	public String loginValidation(String username, String pass) {
		 return dataExchangeDao.loginValidation(username,pass );
	}

	@Override
	public String buySellHistory(String sdate, String edate, String buysell) {
		if (sdate.compareTo(edate) > 0) {
			return "End date cannt be less than Start date";
		}
		
		try  {
		
		
		
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
				 username = ((UserDetails)principal).getUsername();
		} else {
				 username = ((UserDetails)principal).getUsername();
		}
			String result = dataExchangeDao.buySellHistory(username, sdate, edate, buysell );
			return result;
		}
		catch (Exception  e) {
			return "Incorrect Date Format";
		}
	}

	@Override
	public String averageHistory(Integer average) {
		try  {
			String result = dataExchangeDao.averageHistory(average );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
	}

	@Override
	public String updateSell(String username, String exchange, String stock, Integer quantity) {
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
			String result = dataExchangeDao.updateSell(username,ExchangeType.valueOf(exchange).getExchangeType(),stock,quantity );
			return result;
		}
		catch (IllegalArgumentException e) {
			return "Incorrect Options Selected";
		}
	}

}
