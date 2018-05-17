package in.ajm.sb_library.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

import in.ajm.sb_library.R;

public class ThemeUtils {

    public static int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public static int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public static int getColorPrimaryDark(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public static int getColorMyThemelight(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_theme_light, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    /*White*/
    public static int getOppositeColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_text_color_inverse, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    /*Black*/
    public static int getTextColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_text_color, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }
}
