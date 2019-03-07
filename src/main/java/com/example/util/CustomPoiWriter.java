package com.example.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.example.model.ExcelEmployee;


/**
 * 
 * @author nikhil.singhal
 *
 * @param <T>
 */
@Component
public class CustomPoiWriter<T> {


	public  String writeSheet(List<T> values) {
		
		XSSFWorkbook workbook = new XSSFWorkbook(); 

		// Create a blank sheet 
		XSSFSheet sheet = workbook.createSheet("Employee Details"); 

		// This data needs to be written (Object[]) 
		int rownum = 0; 
		for (T value : values) {
			// this creates a new row in the sheet 
			Row row = sheet.createRow(rownum++); 
			if(value instanceof ExcelEmployee) {

				int cellnum = 0; 
				// this line creates a cell in the next column of that row 
				 row.createCell(cellnum++).setCellValue(((ExcelEmployee)value).getUserId()); 
				 row.createCell(cellnum++).setCellValue(((ExcelEmployee)value).getFirstName()); 
				 row.createCell(cellnum++).setCellValue(((ExcelEmployee)value).getLastName()); 
				 row.createCell(cellnum++).setCellValue(((ExcelEmployee)value).getType()); 
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
