package com.example.util;

import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.example.model.ExcelEmployee;

import annotations.ExcelColumnName;


@Component
public class CustomPoiReader<T>  {

	
	// Should we move these to method since class is singleton -- done	
	
	
	public  List<T> readSheet(Class<T> bean, InputStream excelFile)
	{
		List<T> data = new ArrayList<>();
		try {
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			
			Iterator<Row> iterator = datatypeSheet.iterator();
			Row currentRow = iterator.next();
			Field[] fields  = validateColumn(bean, currentRow);
			if (fields!= null) {
				while (iterator.hasNext()) {
					currentRow = iterator.next();
					Iterator<Cell> cellIterator = currentRow.iterator();
						Object obj;
						try {
							obj = Class.forName(bean.getCanonicalName()).getConstructor().newInstance();
							int count = 0;
							while (cellIterator.hasNext()) {
								Cell currentCell = cellIterator.next();

								PropertyDescriptor pd;
								pd = new PropertyDescriptor(fields[count].getName(), obj.getClass());
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

	
	//Can we use 2 sets/maps for comparison
	private Field[]  validateColumn(Class<T> bean, Row row) {
		boolean valid = false;
		Field[] fields = null;
		Field[]  temp = null;
		int excelColumnsCount = row.getLastCellNum();
		try {
			 fields = FieldUtils.getFieldsWithAnnotation(Class.forName(bean.getCanonicalName()), ExcelColumnName.class);
			 temp = fields.clone(); 

			//check for only annotated fields  -- done
			if (excelColumnsCount ==  fields.length) {

				Iterator<Cell> cellIterator0 = row.iterator();
				int j = 0;
				while (cellIterator0.hasNext()) {
					String val = cellIterator0.next().getStringCellValue();
					//TODO Can we use java 8 lambda streams?
					for (int i = 0; i < fields.length; i++) { 
						valid = val.equals(fields[i].getAnnotation(ExcelColumnName.class).name());
						if (valid)
						{
							temp[j] = fields[i];
							j ++;
							break;
						}
					}
					if (!valid) {
						return null;
					}
				}
			}
			else {
				return null;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		fields = temp.clone();
		return fields;
	}


	public  Class<T> identifyClass() {

		// change later
		return (Class<T>) ExcelEmployee.class;
	}
	
	
}
