package in.ajm.sb.helper;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Himanshu Chouhan on 28/02/17.
 */

public class ButtonMedium extends Button {
    public ButtonMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ButtonMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ButtonMedium(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        setTypeface(FontHelper.getInstance(context).getMedium());
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
