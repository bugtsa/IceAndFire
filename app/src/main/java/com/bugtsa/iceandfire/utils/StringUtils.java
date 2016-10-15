package com.bugtsa.iceandfire.utils;

public class StringUtils {

    public static String getIdFromUrlApi(String str) {
        String symbol = "/";
        if (str != null && str.length() > 0 && str.lastIndexOf(symbol) > 0) {
            str = str.substring(str.lastIndexOf(symbol) + 1, str.length());
        }
        return str;
    }

    public static String removeLastChar(String str, char lastChar) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == lastChar) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
