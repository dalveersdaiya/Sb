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
    public static String PREFERENCE_USER_ID = "USER_ID";
    public static String PREFERENCE_LOGGEDIN_USER_ID = "LOGGEDIN_USER_ID";
    public static String PREFERENCE_ORG_ID = "ORGID";
    public static String PREFERENCE_AUTH = "AUTH";

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
        } catch (PackageManager.NameNotFoundException ex) {}
        return "";
    }
}
