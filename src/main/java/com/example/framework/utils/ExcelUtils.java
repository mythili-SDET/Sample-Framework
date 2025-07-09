package com.example.framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple utility for reading data from Excel `.xlsx` files located on classpath.
 */
public final class ExcelUtils {

    private ExcelUtils() {}

    public static List<List<String>> readSheet(String fileName, String sheetName) {
        try (InputStream is = ExcelUtils.class.getResourceAsStream("/" + fileName);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " not found in " + fileName);
            }
            List<List<String>> result = new ArrayList<>();
            for (Row row : sheet) {
                List<String> cells = new ArrayList<>();
                for (Cell cell : row) {
                    cell.setCellType(CellType.STRING);
                    cells.add(cell.getStringCellValue());
                }
                result.add(cells);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel file " + fileName, e);
        }
    }
}