package com.bugtsa.iceandfire.utils;

public class StringUtils {

    public static String getIdFromUrlApi(String str) {
        String symbol = "/";
        if (str != null && str.length() > 0 && str.lastIndexOf(symbol) > 0) {
            str = str.substring(str.lastIndexOf(symbol) + 1, str.length());
        }
        return str;
    }
}
