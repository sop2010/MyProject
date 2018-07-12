package com.my.jt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.comons.uitl.ExcelUtil;

public class Sensitive {
    public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
        Workbook wb = WorkbookFactory.create(new File("D:\\down\\涉敏资产库.xlsx"));
        Sheet sheet = wb.getSheetAt(0);

        BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\down\\LOG.txt"));
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String ip = ExcelUtil.getCellValue(row.getCell(1));
            if (StringUtils.isEmpty(ip)) {
                continue;
            }
            String name = ExcelUtil.getCellValue(row.getCell(2));
            String flag = ExcelUtil.getCellValue(row.getCell(3));
            String mname = ExcelUtil.getCellValue(row.getCell(4));
            String mrange = ExcelUtil.getCellValue(row.getCell(5));
            String mleve = ExcelUtil.getCellValue(row.getCell(6));

            String sql = "INSERT INTO E_SENSITIVE_RES VALUES('" + uuid + "','" + ip + "','" + name + "','" + flag + "','" + mname + "','" + mrange + "','" + mleve + "');";
            System.out.println(sql);
            writer.write(sql);
            writer.newLine();
        }

        writer.close();
    }
}
