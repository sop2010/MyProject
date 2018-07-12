package com.my.smp;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.comons.uitl.ExcelUtil;
import com.comons.uitl.StringUtil;

public class CustomSave {
    private String url = "jdbc:oracle:thin:@127.0.0.1:2721/ORAISMP";
    private String username = "ESP_HOT";
    private String passwd = "2017ultra4A";

    public void save() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection(url, username, passwd);

        String sql = "INSERT INTO somc.cssc_custom (IP,CUSTOM_UUID,TIME) VALUES(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        conn.setAutoCommit(false);

        Workbook wb1 = WorkbookFactory.create(new File("d:\\漏洞管理设备信息收集.xls"));
        Sheet sheet1 = wb1.getSheet("Sheet1");
        int lastRowNum = sheet1.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet1.getRow(i);
            if (row == null) {
                continue;
            }

            String ips = ExcelUtil.getCellValue(row.getCell(0));
            if (StringUtil.isEmpty(ips)) {
                continue;
            }

            for (String ip : ips.split("、|,")) {
                if (ip.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                    ps.setString(1, ip);
                    ps.setString(2, "ac_liujiang");
                    ps.setString(3, "2018-07-05 14:50:00");
                    ps.addBatch();
                }
            }

        }
        ps.executeBatch();

        conn.commit();
        ps.close();
        conn.close();
    }

    public static void main(String[] args) throws Exception {
        new CustomSave().save();
    }
}
