package com.innext.szqb.util.common;

import com.innext.szqb.app.App;

import net.grandcentrix.tray.AppPreferences;


/**
 * 跨进程存储工具类
 * hengxinyongli
 */
public class SpUtil {
    private static AppPreferences appPreferences;

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void putString(String key, String value) {
        getSharedPreferences().put(key, value);
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }
    public static boolean getBoolean(String key,boolean defValue) {
        return getSharedPreferences().getBoolean(key, false);
    }
    public static void putBoolean(String key, boolean value) {
        getSharedPreferences().put(key, value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }
    public static float getFloat(String key) {
        return getFloat(key, 0);
    }
    public static int getInt(String key, int value) {
        return getSharedPreferences().getInt(key, value);
    }
    public static float getFloat(String key, float value) {
        return getSharedPreferences().getFloat(key, value);
    }
    public static void putInt(String key, int value) {
        getSharedPreferences().put(key, value);
    }
    public static void putFloat(String key, float value) {
        getSharedPreferences().put(key, value);
    }

    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, 0);
    }

    public static void putLong(String key, long value) {
        getSharedPreferences().put(key, value);
    }

    public static void remove(String key) {
        getSharedPreferences().remove(key);
    }

    public static AppPreferences getSharedPreferences() {
        if (appPreferences ==null){
            appPreferences=new AppPreferences(App.getContext());
        }
        return appPreferences;
    }

}
