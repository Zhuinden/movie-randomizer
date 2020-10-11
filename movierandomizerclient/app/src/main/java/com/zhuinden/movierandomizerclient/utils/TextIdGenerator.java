package com.zhuinden.movierandomizerclient.utils;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class TextIdGenerator {
    private TextIdGenerator() {
    }

    public static long getTextValue(String string) { // should return different value for each not-same string
        if(string == null) {
            return 0;
        }
        long val = 37;
        for(int i = 0, size = string.length(); i < size; i++) {
            char c = string.charAt(i);
            val += (37*c)+(i+1)*41;
        }
        return val;
    }
}
