package com.charder.roomdemo.common;

import com.charder.roomdemo.room.AppDataBase;
import com.charder.roomdemo.room.entity.Account;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    final static public String BodyCompositionDb = "BodyCompositionDb";

    final static public String[] permissions = {"管理者" , "使用者" };

    static public Account currentAccount = null;

    static public AppDataBase db;

    static public Date dateFormatTime(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    static public String dateFormatTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(date);
    }
}
