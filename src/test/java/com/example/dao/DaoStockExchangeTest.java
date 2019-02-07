package com.example.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.StockExchangeApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockExchangeApp.class)
@Transactional
public class DaoStockExchangeTest {

	@Autowired
	private DaoStockExchange dataExchangeDao;


	@Test
	@Rollback(true)
	public void updateBuyTest() {
		
		String result  = dataExchangeDao.updateBuy("user1", 1, "Comp1", 111);
		Assert.assertEquals("PurchaseSuccessful", result);
	}

	@Test
	@Rollback(true)
	public void updateSellTest() {

		String result  = dataExchangeDao.updateSell("user1", 1, "Comp1", 111);
		Assert.assertEquals("SellingSuccessful", result);

	}
	
//	@Test
//	public void buySellHistoryTest() {
//
//		String result  = dataExchangeDao.buySellHistory("user1", "01-01-2012", "01-01-2222", "buy");
//		Assert.assertEquals("PurchaseSuccessful", result);
//	}

	@Test
	@Rollback(true)
	public void averageHistoryTest() {

		String result  = dataExchangeDao.averageHistory(80);
		Assert.assertEquals("--> user2 Average Buy Price is :: Rs 88", result.trim());
	}

	@Test
	@Rollback(true)
	public void loginValidationTest() {

		String result  = dataExchangeDao.loginValidation("user1", "pass1");
		Assert.assertEquals("Login success", result);
	}
	
	@Test
	@Rollback(true)
	public void loginValidationTest2() {

		String result  = dataExchangeDao.loginValidation("user1", "pass2");
		Assert.assertEquals("Invalid Username or Password", result);
	}

	@Test
	@Rollback(true)
	public void  topTransactionTest() {

		String result  = dataExchangeDao.topTransaction(1);
		Assert.assertEquals("The price value at this position is --> 98", result.trim());
	}
}
