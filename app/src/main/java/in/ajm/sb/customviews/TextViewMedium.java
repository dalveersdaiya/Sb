package in.ajm.sb.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import in.ajm.sb.helper.FontHelper;

/**
 * Created by Himanshu Chouhan on 27/09/16.
 */

public class TextViewMedium extends TextView
{

	public TextViewMedium(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public TextViewMedium(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public TextViewMedium(Context context)
	{
		super(context);
		init(context);
	}

	private void init(Context context)
	{

		setTypeface(FontHelper.getInstance(context).getMedium());
		setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}
}
