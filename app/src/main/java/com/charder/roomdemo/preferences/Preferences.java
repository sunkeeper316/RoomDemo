package com.charder.roomdemo.preferences;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Preferences {
    private final static String PREFERENCES_NAME = "preferences";
    private final static String KEY_ACCOUNT = "Account";
    private final static String KEY_PASSWORD = "Password";
    static String saveDeviceName = "";
    public static void saveAccount(Activity activity , String account){
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);;
        preferences.edit()
                .putString(KEY_ACCOUNT, account).apply();
    }

    public static String loadAccount(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);;
        return preferences.getString(KEY_ACCOUNT,"");

    }
    public static void savePassword(Activity activity , String password){
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);;
        preferences.edit()
                .putString(KEY_PASSWORD, password).apply();
    }

    public static String loadPassword(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);;
        return preferences.getString(KEY_PASSWORD,"");

    }
}
