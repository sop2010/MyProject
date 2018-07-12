package com.comons.uitl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

public final class ExcelUtil {
    private ExcelUtil() {
    }

    @SuppressWarnings("deprecation")
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        StringBuilder cellvalue = new StringBuilder();
        
        try {
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellvalue.append(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                cellvalue.append(cell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                try {
                    cellvalue.append(cell.getNumericCellValue());
                }catch (Exception e) {
                    cellvalue.append(cell.getRichStringCellValue());
                }
                break;
            case Cell.CELL_TYPE_NUMERIC:
                String val = null;
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    Date theDate = cell.getDateCellValue();
                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    val = dff.format(theDate);
                } else {
                    DecimalFormat df = new DecimalFormat("0");
                    val = df.format(cell.getNumericCellValue());
                }
                cellvalue.append(val);
                break;
            case Cell.CELL_TYPE_STRING:
                cellvalue.append(cell.getStringCellValue());
                break;
            default:
                break;
            }
        } catch (Exception e) {
            
        }

        return cellvalue.toString();
    }
}
