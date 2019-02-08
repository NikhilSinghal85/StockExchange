package com.example.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.app.StockExchangeApp;
import com.example.service.StockExchangeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockExchangeApp.class)
@AutoConfigureMockMvc
public class StockServiceControllerTest {

	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StockExchangeService stockExchangeService;
	
	@Test
	public void buyStocksTest() throws Exception {
		
		

		Mockito.when(
				stockExchangeService.updateBuy(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn("PurchaseSuccessful");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/buy?UserName=user1&Exchange=NSE&Stock=comp1&Quantity=100");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "PurchaseSuccessful";
		
		Assert.assertEquals(expected, result.getResponse().getContentAsString());
	}
}
