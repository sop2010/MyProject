package com.comons.uitl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * @author yewen
 *
 */
public final class FileUtil {
    private FileUtil() {
    }

    public static List<File> getAllFileList(String filepath) {
        List<File> filelist = new ArrayList<>();
        List<File> allfilelist = new ArrayList<>();
        File pfile = new File(filepath);
        if (pfile.isFile()) {
            allfilelist.add(pfile);
        } else {
            filelist.add(pfile);
            while (!filelist.isEmpty()) {
                List<File> dfileList = new ArrayList<>();
                for (File file : filelist) {
                    for(File file_ :file.listFiles()) {
                        if (file_.isFile()) {
                            allfilelist.add(file_);
                        } else if (file_.isDirectory()) {
                            dfileList.add(file_);
                        }
                    }
                }
                filelist = dfileList;
            }
        }
        return allfilelist;
    }
}
