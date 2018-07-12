package com.my.kaoqing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.comons.uitl.ExcelUtil;

public class Kaoqing2 {
    public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
        Workbook wb = WorkbookFactory.create(new File("D:\\work\\qq\\考勤签字表2018_3.xls"));
        Sheet sheet = wb.getSheetAt(0);

        int lastRowNum = sheet.getLastRowNum();
        for (int i = 4; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                String cellValue = ExcelUtil.getCellValue(cell);
                if ("√".equals(cellValue)) {
                    int mi = RandomUtils.nextInt(10, 59);
                    int ss = RandomUtils.nextInt(10, 59);
                    if (i % 2 == 0) {
                        cell.setCellValue("08:" + mi + ":" + ss);
                    } else {
                        cell.setCellValue("18:" + mi + ":" + ss);
                    }
                }
            }
        }

        wb.write(new FileOutputStream("D:\\work\\qq\\考勤签字表2018_3_zhuan.xls"));
    }

}
