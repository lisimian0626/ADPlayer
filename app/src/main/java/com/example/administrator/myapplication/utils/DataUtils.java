package com.example.administrator.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataUtils {
    private static final String DATE_FORMAT_HHMM = "HH:mm";
    public static boolean inTimeArea(String on, String off) {
        try {
            L.test("on:"+str2Long(on)+"    off:"+str2Long(off)+"   cur:"+str2Long(getTimeforString(System.currentTimeMillis())));
            long curtime=str2Long(getTimeforString(System.currentTimeMillis()));
            long ontime=str2Long(on);
            long offtime=str2Long(off);
            if(curtime>ontime&&curtime<offtime){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String addTime(){
        long time=str2Long(getTimeforString(System.currentTimeMillis()))+4*60*1000;
        return getTimeforString(time);
    }

    private static long str2Long(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_HHMM, Locale.CHINA).parse(dateStr).getTime();
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private static String getTimeforString(long longtime){
        String time="";
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_HHMM);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(longtime));
        Date y = c.getTime();
        try {
            time = format.format(y);
        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }
}
