package in.ajm.sb.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by DSD on 26/03/18.
 */

public class AppConfigs {
    public static int REQUEST_CODE_SELECT_CLASS = 101;
    public static int REQUEST_CODE_SELECT_SECTION = 102;
    public static int REQUEST_CODE_IMAGE_PICKER = 10;
    public static int STUDENT_TYPE = 03;
    public static int TEACHER_TYPE = 02;
    public static int PARENT_TYPE = 01;
    public static int SCHOOL_ADMIN_TYPE = 04;
    public static String PREFERENCE_STUDENT_ID = "STUDENT_ID";
    public static String PREFERENCE_TEACHER_ID = "TEACHER_ID";
    public static String PREFERENCE_PARENT_ID = "PARENT_ID";
    public static String PREFERENCE_SCHOOL_ADMIN_ID = "SCHOOL_ADMIN_ID";
    public static String PREFERENCE_USER_ID = "USER_ID";
    public static String PREFERENCE_LOGGEDIN_USER_ID = "LOGGEDIN_USER_ID";
    public static String PREFERENCE_ORG_ID = "ORGID";
    public static String PREFERENCE_AUTH = "AUTH";
    public static String USER_TYPE = "USER_TYPE";
    public static String OPTION_TYPE = "OPTION_TYPE";
    public static String DATE_FORMAT = "MM/dd/yyyy";
    public static String YEAR_DATE_FORMAT = "yyyy-MM-dd";
    public static String DATE_TIME_FORMAT = "MM/dd/yyyy hh:mm a";
    public static String TIME_FORMAT = "hh:mm aa";
    public static String DATE_TIME_FORMAT_WITH_TIMEZONE = "dd/MM/yyyy hh:mm:ss z";


    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException ex) {
        }
        return "";
    }
}
