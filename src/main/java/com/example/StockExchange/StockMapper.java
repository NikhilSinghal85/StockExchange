package com.example.StockExchange;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class StockMapper implements RowMapper<StockAvailable>
{
    @Override
    public StockAvailable mapRow(ResultSet rs, int rowNum) throws SQLException {
    	StockAvailable stock = new StockAvailable();
    	//stock.setStockName(rs.getString("stock_name"));
    	//stock.setExchangeName(rs.getString("exchange_name"));
    	stock.setNumber(rs.getInt("Stock_number"));
        return stock;
    }
}
