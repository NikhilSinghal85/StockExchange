package com.example.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class CustomPoiWriter<T> {


	public  String writeSheet(List<T> values) {
		
		XSSFWorkbook workbook = new XSSFWorkbook(); 

		// Create a blank sheet 
		XSSFSheet sheet = workbook.createSheet("Employee Details"); 

		// This data needs to be written (Object[]) 
		Map<String, Object[]> data = new TreeMap<String, Object[]>(); 
		data.put("1", new Object[]{ "ID", "NAME", "LASTNAME" }); 
		data.put("2", new Object[]{ 1, "nikhil", "singhal" }); 
		data.put("3", new Object[]{ 2, "rohit", "gupta" }); 
		data.put("4", new Object[]{ 3, "chotu", "chotu" }); 

		// Iterate over data and write to sheet 
		Set<String> keyset = data.keySet(); 
		int rownum = 0; 
		for (String key : keyset) { 
			// this creates a new row in the sheet 
			Row row = sheet.createRow(rownum++); 
			Object[] objArr = data.get(key); 
			int cellnum = 0; 
			for (Object obj : objArr) { 
				// this line creates a cell in the next column of that row 
				Cell cell = row.createCell(cellnum++); 
				if (obj instanceof String) 
					cell.setCellValue((String)obj); 
				else if (obj instanceof Integer) 
					cell.setCellValue((Integer)obj); 
			} 
		} 
		try { 
			// this Writes the workbook Download 
			FileOutputStream out = new FileOutputStream(new File("E:\\Download.xlsx")); 
			workbook.write(out); 
			out.close(); 
			System.out.println("Download.xlsx written successfully on disk."); 
			
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		}
		return "E:\\Download.xlsx";
	}

}
