package com.comons.uitl;

import java.util.UUID;

/**
 * 字符串工具类
 * 
 * @author yewen
 *
 */
public final class StringUtil {
    private StringUtil() {

    }

    /**
     * 比较两个字符串是否相等
     * 
     * @param str1 字符串
     * @param str2 字符串
     * @return 结果
     */
    public static boolean equals(String str1, String str2) {
        return str1 == null ? (str2 == null) : str1.equals(str2);
    }

    /**
     * 比较两个字符串忽略大小写是否相等
     * 
     * @param str1 字符串
     * @param str2 字符串
     * @return 结果
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? (str2 == null) : str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串是否为空 str1 == null要放在前面
     * 
     * @param str1
     * @return
     */
    public static boolean isEmpty(String str1) {
        return str1 == null || str1.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     * 
     * @param str1
     * @return
     */
    public static boolean isNotEmpty(String str1) {
        return !isEmpty(str1);
    }

    /**
     * 得到UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
