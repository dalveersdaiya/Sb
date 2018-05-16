package in.ajm.sb_library.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by DSD on 28/02/17.
 */

public class ButtonRegularLight extends Button {
    public ButtonRegularLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ButtonRegularLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ButtonRegularLight(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setTypeface(FontHelper.getInstance(context).getRegularLight());
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
