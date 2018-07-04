package in.ajm.sb.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RadioButton;

import in.ajm.sb.helper.FontHelper;

/**
 * Created by DSD on 28/02/17.
 */

public class RadioButtonMedium extends RadioButton {
    public RadioButtonMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public RadioButtonMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadioButtonMedium(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setTypeface(FontHelper.getInstance(context).getMedium());
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
