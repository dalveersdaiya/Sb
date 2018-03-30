package in.ajm.sb.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by DSD on 27/03/18.
 */

public class GeneralHelper {
    private static GeneralHelper generalHelper;
    private Context context;

    public GeneralHelper(Context context) {
        this.context = context;
    }

    public static synchronized GeneralHelper getInstance(Context context) {
        if (generalHelper == null) {
            generalHelper = new GeneralHelper(context);
        }
        return generalHelper;
    }

    /**
     * Convert dp in pixels
     *
     * @param dp
     * @return
     */
    public int getPx(int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }

    @SuppressWarnings("deprecation")
    public Point getScreenSize() {

        Point size = new Point();
        WindowManager w = ((Activity) context).getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
        } else {
            Display d = w.getDefaultDisplay();
            size.x = d.getWidth();
            size.y = d.getHeight();
        }
        return size;
    }
}
