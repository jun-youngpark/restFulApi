package com.mnwise.wiseu.web.common.util;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtils {

    public static String[] rowToArray(Row row) {

        String[] result =new String[row.getLastCellNum()];
        Iterator<Cell> cellIterator = row.cellIterator();
        int i=0;
        while (cellIterator.hasNext()) {
            result[i++]=cellReader(cellIterator.next());
        }
        return result;
    }

    public static String cellReader(Cell cell) {
        String value = "";
        switch(cell.getCellTypeEnum()) {
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case NUMERIC:
                value = Integer.toString((int)cell.getNumericCellValue());
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue()+"";
                break;
            case ERROR:
                value = cell.getErrorCellValue()+"";
                break;
            default:
                break;
            }
        return value;
    }

}
