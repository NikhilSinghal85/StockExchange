package com.example.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import annotations.ExcelColumnName;
import excelPojo.ExcelEmployee;


@Component
public class CustomPoiReader<T> {


	public  /*List<T>*/ String readSheet(Class<T> bean, InputStream excelFile)
	{
		try {
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			Row currentRow = iterator.next();

			boolean valid = validateColumn(bean, currentRow);

			if (valid) {

				while (iterator.hasNext()) {


					Iterator<Cell> cellIterator = currentRow.iterator();

					while (cellIterator.hasNext()) {

						Cell currentCell = cellIterator.next();

						if (currentCell.getCellType() == CellType.STRING) {
							System.out.print(currentCell.getStringCellValue() + "                ");
						} else if (currentCell.getCellType() == CellType.NUMERIC) {
							System.out.print(currentCell.getNumericCellValue() + "          ");
						}

					}
					System.out.println();

				}
			}
			else {
				return "Mismatch in column Count";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private boolean validateColumn(Class<T> bean, Row row) {
		boolean valid = false;
		int coumnCount = row.getLastCellNum();
		
		Field[] fileds = bean.getDeclaredFields();
		
		boolean flag = false;
		if (coumnCount ==  fileds.length) {
			
			Iterator<Cell> cellIterator0 = row.iterator();
			while (cellIterator0.hasNext()) {
				String val = cellIterator0.next().getStringCellValue();
				for (int i = 0; i < fileds.length; i++) {
					flag = val.equals(fileds[i].getAnnotation(bean));
				}
				if (!flag) {
					return false;
				}
			}
			
			
		}
		else {
			return false;
		}

		


		Field d;
		try {
			d = ExcelEmployee.class.getDeclaredField("lastName");

			ExcelColumnName ec =	d.getAnnotation(ExcelColumnName.class);

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}

	
	public  Class<T> identifyClass() {
		
		// change later
		return (Class<T>) ExcelEmployee.class;
	}
}
