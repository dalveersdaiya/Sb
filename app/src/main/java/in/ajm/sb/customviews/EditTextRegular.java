package in.ajm.sb.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.EditText;

import in.ajm.sb.helper.FontHelper;

/**
 * Created by Himanshu Chouhan on 27/09/16.
 */

public class EditTextRegular extends EditText
{
	private float mOriginalLeftPadding = -1;
	private int index = 0;

	public EditTextRegular(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public EditTextRegular(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public EditTextRegular(Context context)
	{
		super(context);
		init(context);
	}

	private void init(Context context)
	{
		setCursorVisible(true);
		setTypeface(FontHelper.getInstance(context).getRegularFont());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec,
							 int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		calculatePrefix();
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	private void calculatePrefix()
	{
		if (getTag() == null)
			return;

		if (mOriginalLeftPadding == -1)
		{
			String prefix = (String) getTag();
			float[] widths = new float[prefix.length()];
			getPaint().getTextWidths(prefix, widths);
			float textWidth = 0;
			for (float w : widths)
			{
				textWidth += w;
			}
			mOriginalLeftPadding = getCompoundPaddingLeft();
			setPadding((int) (textWidth + mOriginalLeftPadding), getPaddingRight(), getPaddingTop(), getPaddingBottom());
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (getTag() != null)
		{
			String prefix = (String) getTag();
			canvas.drawText(prefix, mOriginalLeftPadding, getLineBounds(0, null), getPaint());
		}
	}
}
