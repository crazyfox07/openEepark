package com.bitcom.openepark.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
    public static Date strtoDate(String str) {
        if ("".equals(str.trim()))
            return null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(str);
        } catch (Exception _e) {
            System.out.println(_e.toString());
        }
        return date;
    }


    public static String dateToStr(Date dt) {
        if (dt == null) {
            dt = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(dt);
    }


    public static void main(String[] args) {
        System.out.println(dateToStr(new Date()));
    }
}



