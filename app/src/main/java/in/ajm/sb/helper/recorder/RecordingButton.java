/**
 * * Copyright 2016 andy
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.ajm.sb.helper.recorder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import in.ajm.sb.helper.DisplayHelper;

/**
 * Author : andy
 * Date   : 16/1/21 11:28
 * Email  : andyxialm@gmail.com
 * Github : github.com/andyxialm
 * Description : A custom CheckBox with animation for Android
 */

public class RecordingButton extends ImageButton
{
	private static final int DEF_ANIM_DURATION = 500;
	private static final int COLOR_CHECKED = Color.parseColor("#90D349");
	private static final int COLOR_UNCHECKED = Color.WHITE;
	private static final int COLOR_FLOOR_UNCHECKED = Color.parseColor("#494D6A");


	private float mScaleVal = 1.0f;
	private Paint mPaint, mFloorPaint;
	private Point mCenterPoint;
	private int mStrokeWidth;
	private int mCheckedColor, mUnCheckedColor, mFloorColor;
	private boolean stopAnimation = false;

	public RecordingButton(Context context)
	{
		this(context, null);
	}

	public RecordingButton(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public RecordingButton(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public RecordingButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	private static int getGradientColor(int startColor, int endColor, float percent)
	{
		int sr = (startColor & 0xff0000) >> 0x10;
		int sg = (startColor & 0xff00) >> 0x8;
		int sb = (startColor & 0xff);

		int er = (endColor & 0xff0000) >> 0x10;
		int eg = (endColor & 0xff00) >> 0x8;
		int eb = (endColor & 0xff);

		int cr = (int) (sr * (1 - percent) + er * percent);
		int cg = (int) (sg * (1 - percent) + eg * percent);
		int cb = (int) (sb * (1 - percent) + eb * percent);
		return Color.argb(0xff, cr, cg, cb);
	}

	private void init(Context context, AttributeSet attrs)
	{
		mStrokeWidth = DisplayHelper.convertDpToPx(context, 1);
		mCheckedColor = COLOR_CHECKED;
		mUnCheckedColor = COLOR_UNCHECKED;
		mFloorColor = COLOR_FLOOR_UNCHECKED;

		mFloorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mFloorPaint.setStyle(Paint.Style.FILL);
		mFloorPaint.setColor(mFloorColor);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mCheckedColor);

		mCenterPoint = new Point();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		int mWidth = getMeasuredWidth();
		mStrokeWidth = (mStrokeWidth == 0 ? getMeasuredWidth() / 20 : mStrokeWidth);
		mStrokeWidth = mStrokeWidth > getMeasuredWidth() / 5 ? getMeasuredWidth() / 5 : mStrokeWidth;
		mStrokeWidth = (mStrokeWidth < 3) ? 3 : mStrokeWidth;
		mCenterPoint.x = mWidth / 2;
		mCenterPoint.y = getMeasuredHeight() / 2;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		drawBorder(canvas);
		drawCenter(canvas);
		drawImage(canvas);
	}

	private void drawCenter(Canvas canvas)
	{
		mPaint.setColor(mUnCheckedColor);
		float radius = (mCenterPoint.x - mStrokeWidth) * mScaleVal;
		canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaint);
	}

	private void drawBorder(Canvas canvas)
	{
		mFloorPaint.setColor(mFloorColor);
		int radius = mCenterPoint.x;
		float mFloorScale = 1.0f;
		canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius * mFloorScale, mFloorPaint);
	}

	private void drawImage(Canvas canvas)
	{
		Bitmap imgBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
		canvas.drawBitmap(imgBitmap, mCenterPoint.x - (imgBitmap.getWidth() / 2), mCenterPoint.y - (imgBitmap.getHeight() / 2), mPaint);
	}


	public void startAnimation()
	{
		stopAnimation = false;
		fullExpandBorder();
	}

	public void stopAnimation()
	{
		stopAnimation = true;
	}

	private void fullExpandBorder()
	{
		ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.3f);
		animator.setDuration(DEF_ANIM_DURATION);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				mScaleVal = (float) animation.getAnimatedValue();
				mFloorColor = getGradientColor(mUnCheckedColor, mCheckedColor, 1 - mScaleVal);
				postInvalidate();
			}
		});
		animator.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						fullCollapseBorder();
					}
				}, 100);
			}
		});
		animator.start();
	}

	private void fullCollapseBorder()
	{
		ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 0.9f);
		animator.setDuration(DEF_ANIM_DURATION);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				mScaleVal = (float) animation.getAnimatedValue();
				mFloorColor = getGradientColor(mUnCheckedColor, mCheckedColor, 1 - mScaleVal);
				postInvalidate();
			}
		});
		animator.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						if (!stopAnimation)
							expandBorder();
						else
						{
							mFloorColor = COLOR_FLOOR_UNCHECKED;
							postInvalidate();
						}
					}
				}, 100);
			}
		});
		animator.start();
	}

	private void expandBorder()
	{
		ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.8f);
		animator.setDuration(DEF_ANIM_DURATION);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				mScaleVal = (float) animation.getAnimatedValue();
				mFloorColor = getGradientColor(mUnCheckedColor, mCheckedColor, 1 - mScaleVal);
				postInvalidate();
			}
		});
		animator.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						collapseBorder();
					}
				}, 100);
			}
		});
		animator.start();
	}

	private void collapseBorder()
	{
		ValueAnimator floorAnimator = ValueAnimator.ofFloat(0.8f, 1.0f);
		floorAnimator.setDuration(DEF_ANIM_DURATION);
		floorAnimator.setInterpolator(new LinearInterpolator());
		floorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				mScaleVal = (float) animation.getAnimatedValue();
				mFloorColor = getGradientColor(mUnCheckedColor, mCheckedColor, 1 - mScaleVal);
				postInvalidate();
			}
		});
		floorAnimator.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						if (!stopAnimation)
							expandBorder();
						else
							fullExpandBorder();
					}
				}, 100);
			}
		});
		floorAnimator.start();
	}
}
