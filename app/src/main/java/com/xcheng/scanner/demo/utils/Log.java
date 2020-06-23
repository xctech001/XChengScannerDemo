package com.xcheng.scanner.demo.utils;

public class Log {
    private static final String DEF_TAG = "XCScannerDemo";

    public static void info(String format, Object... args) {
        android.util.Log.i(DEF_TAG, String.format(format, args));
    }

    public static void debug(String format, Object... args) {
        android.util.Log.d(DEF_TAG, String.format(format, args));
    }

    public static void error(String format, Object... args) {
        android.util.Log.e(DEF_TAG, String.format(format, args));
    }
}
