package in.ajm.sb_library.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DSD on 27/09/16.
 */

public class TextViewRegularlight extends TextView {

    public TextViewRegularlight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TextViewRegularlight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewRegularlight(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setTypeface(FontHelper.getInstance(context).getRegularLight());
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
