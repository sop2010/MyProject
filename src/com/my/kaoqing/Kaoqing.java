package com.my.kaoqing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.comons.uitl.DateUtil;

public class Kaoqing {
    public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
        Workbook wb = WorkbookFactory.create(new File("D:\\work\\qq\\IT需求工单需补充数据汇总v03b.xlsx"));
        Sheet sheet = wb.getSheetAt(2);

        String[] cellnums = { "f","z", "aa", "ad", "ae", "ah", "ai", "al", "am", "ap", "aq", "ar", "ay", "az", "bd", "be", "bg", "bh", "bk", "bl", "bs", "bt", "bx",
                "by", "cc", "cd" };

        String startdatestr = "2017-12-14 09:00:00";
        Date enddate = DateUtil.parse("2018-03-14 18:00:00");

        int lastRowNum = sheet.getLastRowNum();
        for (int i = 5; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            setDateToCell(cellnums, startdatestr,enddate, row);
        }

        wb.write(new FileOutputStream("D:\\work\\qq\\IT需求工单需补充数据汇总v03.xlsx"));
    }

    private static void setDateToCell(String[] cellnums, String startdatestr, Date enddatestr, Row row) {
        Date startdate = DateUtil.parse(startdatestr);
        for (String cellnumstr : cellnums) {
            int cellnum = toNum(cellnumstr);
            Cell cell = row.getCell(cellnum);
            if (cell == null) {
                cell = row.createCell(cellnum);
            }
            startdate = getDate(startdate);
            cell.setCellValue(DateUtil.format(startdate));
            if(startdate.after(enddatestr)) {
                setDateToCell(cellnums, startdatestr, enddatestr, row);
            }
        }
    }

    public static int toNum(String colStr) {
        colStr = colStr.toUpperCase();
        int length = colStr.length();
        int num = 0;
        int result = 0;
        for (int i = 0; i < length; i++) {
            char ch = colStr.charAt(length - i - 1);
            num = (int) (ch - 'A' + 1);
            num *= Math.pow(26, i);
            result += num;
        }
        return result - 1;
    }

    public static Date getDate(Date date) {
        while(true) {
            date = DateUtils.addSeconds(date, RandomUtils.nextInt(1, 56));
            date = DateUtils.addHours(date, RandomUtils.nextInt(1, 12));
            date = DateUtils.addDays(date, RandomUtils.nextInt(0, 2));

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (week == 0 || week == 6 || hour < 9 || hour > 18) {
                System.out.println("時間不對：" + DateUtil.format(date));
            }else {
                return date;
            }
        }
    }
}
