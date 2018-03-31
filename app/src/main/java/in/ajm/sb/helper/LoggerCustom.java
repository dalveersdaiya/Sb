package in.ajm.sb.helper;

import android.util.Log;

import in.ajm.sb.BuildConfig;

public class LoggerCustom {
    public static void systemOutPrint(String log_str) {
        if (BuildConfig.DEBUG)
            System.out.println(log_str);
    }

    public static void systemErrPrint(String log_str) {
        if (BuildConfig.DEBUG)
            System.err.println(log_str);
    }

    public static void printStackTrace(Exception e) {
        if (BuildConfig.DEBUG)
            e.printStackTrace();
    }

    public static void printStackTrace(Throwable t) {
        if (BuildConfig.DEBUG)
            t.printStackTrace();
    }

    public static void logD(String tag, String log_str) {
        if (BuildConfig.DEBUG)
            Log.d(tag, log_str);
    }

    public static void logE(String tag, String log_str) {
        if (BuildConfig.DEBUG)
            Log.e(tag, log_str);
    }

    public static void logI(String tag, String log_str) {
        if (BuildConfig.DEBUG)
            Log.i(tag, log_str);
    }

    public static void logV(String tag, String log_str) {
        if (BuildConfig.DEBUG)
            Log.v(tag, log_str);
    }

    public static void logW(String tag, String log_str) {
        if (BuildConfig.DEBUG)
            Log.w(tag, log_str);
    }
}
