package com.framework.utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExcelUtil {

    public static List<Map<String, String>> readSheet(String pathInResources, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        try (InputStream is = ExcelUtil.class.getClassLoader().getResourceAsStream(pathInResources);
             XSSFWorkbook workbook = new XSSFWorkbook(Objects.requireNonNull(is))) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            Row header = sheet.getRow(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    String key = header.getCell(j).getStringCellValue();
                    String value = cell == null ? "" : cell.toString();
                    rowData.put(key, value);
                }
                data.add(rowData);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read excel file: " + pathInResources, e);
        }
    }
}