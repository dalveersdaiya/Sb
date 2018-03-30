package in.ajm.sb.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DSD on 16/09/16.
 */

public class NetworkUtil {
    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;


    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isNetworkAvailable(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        boolean isConnected = false;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
            isConnected = true;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
            isConnected = true;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
            isConnected = false;
        }
        return isConnected;
    }
}
