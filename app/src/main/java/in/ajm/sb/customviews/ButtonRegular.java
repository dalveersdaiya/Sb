package in.ajm.sb.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import in.ajm.sb.helper.FontHelper;

/**
 * Created by Himanshu Chouhan on 28/02/17.
 */

public class ButtonRegular extends Button
{
	public ButtonRegular(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public ButtonRegular(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public ButtonRegular(Context context)
	{
		super(context);
		init(context);
	}

	private void init(Context context)
	{
		setTypeface(FontHelper.getInstance(context).getRegularFont());
		setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}
}
