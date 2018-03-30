package com.org.besteverflatrate.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayHelper {
    public static int[] getDisplaySize(Context context) {
        int measuredWidth = 0;
        int measuredHeight = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager != null) {
            Display d = windowManager.getDefaultDisplay();
            measuredWidth = d.getWidth();
            measuredHeight = d.getHeight();
        }

        return new int[]{measuredWidth, measuredHeight};
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A int value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPx(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A int value to represent dp equivalent to px value
     */
    public static int convertPxToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    public static int convertPxToSp(final Context context, final float px) {
        return Math.round(px / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int convertSpToPx(final Context context, final float sp) {
        return Math.round(sp * context.getResources().getDisplayMetrics().scaledDensity);
    }
}
