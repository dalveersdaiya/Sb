package in.ajm.sb_library.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by DSD on 28/02/17.
 */

public class ButtonBold extends Button {
    public ButtonBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ButtonBold(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setTypeface(FontHelper.getInstance(context).getBoldFont());
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
