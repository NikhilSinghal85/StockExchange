package com.example.util;

import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.example.constant.QueryConstants;
import com.example.mapper.CompanyRowMapper;
import com.example.model.Company;

import annotations.ExcelColumnName;
import excelPojo.ExcelEmployee;


@Component
public class CustomPoiReader<T>  {

	Field[] fileds ;
	List<T> data = new ArrayList<>();
	
	
	public  List<T> readSheet(Class<T> bean, InputStream excelFile)
	{
		try {
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			
			Iterator<Row> iterator = datatypeSheet.iterator();
			Row currentRow = iterator.next();
			boolean valid = validateColumn(bean, currentRow);
			if (valid) {
				while (iterator.hasNext()) {
					currentRow = iterator.next();
					Iterator<Cell> cellIterator = currentRow.iterator();
						Object obj;
						try {
//							String myClass = bean.getName();
//							int index = myClass.indexOf('.');
//							if (index != 0) {
//								myClass = myClass.substring(index + 1);
//							}
							obj = Class.forName(bean.getCanonicalName()).getConstructor().newInstance();
							int count = 0;
							while (cellIterator.hasNext()) {
								Cell currentCell = cellIterator.next();

								PropertyDescriptor pd;
								pd = new PropertyDescriptor(fileds[count].getName(), obj.getClass());
								if (currentCell.getCellType() == CellType.STRING) { 
									pd.getWriteMethod().invoke(obj,currentCell.getStringCellValue().trim() ); 
									count++;
								} else if (currentCell.getCellType() == CellType.NUMERIC) {
									Integer i = (int) currentCell.getNumericCellValue();
									pd.getWriteMethod().invoke(obj,i ); 
									count++;
								}
								else
									return null;
							}
							data.add((T) obj);
						}
							catch (Exception e) {
								return null;
							}
						}
			}
			else {
				return null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		}
		return data;
	}

	private boolean validateColumn(	Class<T> bean, Row row) {
		boolean valid = false;
		int coumnCount = row.getLastCellNum();

		fileds = bean.getDeclaredFields();
		Field[]  temp = fileds.clone(); 

		if (coumnCount ==  fileds.length) {

			Iterator<Cell> cellIterator0 = row.iterator();
			int j = 0;
			while (cellIterator0.hasNext()) {
				String val = cellIterator0.next().getStringCellValue();
				for (int i = 0; i < fileds.length; i++) { 
					valid = val.equals(fileds[i].getAnnotation(ExcelColumnName.class).name());
					if (valid)
					{
						temp[j] = fileds[i];
						j ++;
						break;
					}
				}
				if (!valid) {
					return false;
				}
			}
		}
		else {
			return false;
		}
		fileds = temp.clone();
		return valid;
	}


	public  Class<T> identifyClass() {

		// change later
		return (Class<T>) ExcelEmployee.class;
	}
	
	
}
