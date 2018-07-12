package com.my.svn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

public class SvnFileUtils {
    
    public static void main(String[] args) {
        // 工程所在的文件夹
        String appDir = "D:\\work\\tywork2\\IAMPortal";
        // 导出所在文件夹
        String desDir = "D:\\dd\\amsportal";
        // svn日志存放位置
        File file = new File("d:\\changeLog.txt");
        copyFiles(file, appDir, desDir);
    }
    
    
    /**
     * 拷贝文件
     * 
     * @param file 想要读取svn日志的文件对象
     * @param appDir 工程所在的文件夹的路径
     * @param desDir 导出所在文件夹的路径
     * @return 返回文件内容
     */
    public static String copyFiles(File file, String appDir, String desDir) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
            String s = null;
            int size = 1;
            while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
                int index = s.indexOf("/IAMPortal");
                if (index < 0) {
                    continue;
                }
                s = s.substring(index + "/IAMPortal".length());
                // 把java文件路径替换成编译好的classes文件的路径
                if (s.contains("/src/")) {
                    s = s.replace("/src/", "/WebRoot/WEB-INF/classes/");
                    s = s.replace(".java", ".class");
                }
                // 过滤掉重复的源文件目录
                if (result.indexOf(s) > -1) {
                    continue;
                }
                String desStr = desDir + s;
                File srcFile = new File(appDir + s); // 源文件对象
                File destFile = new File(desStr); // 目标文件对象
                if (!(srcFile.exists())) { // 判断源文件是否存在
                    System.out.println(s + "文件不存在.......");
                    continue;
                }
                if (!(destFile.exists())) { // 判断目标文件是否存在
                    desStr = desStr.substring(0, desStr.lastIndexOf("/"));
                    File dir = new File(desStr);
                    // 创建文件夹
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    // 创建文件
                    destFile.createNewFile(); // 如果不存在则创建新文件
                }
                // 使用源文件对象创建文件输入流对象
                FileInputStream fis = new FileInputStream(srcFile);
                // 使用目标文件对象创建文件输出流对象
                FileOutputStream fos = new FileOutputStream(destFile);
                byte[] buf = new byte[(int) srcFile.length()]; // 创建字节数组，作为临时缓冲
                System.out.println("开始复制文件..." + size);
                // while (fis.read(buf) != -1) { //循环从文件输入流中读取数据
                fis.read(buf);
                fos.write(buf); // 写入到文件输出流中
                // }
                System.out.println("文件:" + s);
                System.out.println("文件复制成功！");
                fis.close(); // 关闭流
                fos.close();
                result.append(s + System.lineSeparator());
                size++;
            }
            size--;
            System.out.println("文件复制总数： " + size + "个！");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
