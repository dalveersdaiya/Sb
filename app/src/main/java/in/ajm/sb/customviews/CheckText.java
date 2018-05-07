package in.ajm.sb.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.ajm.sb.R;
import in.ajm.sb.helper.GeneralHelper;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by dalveersinghdaiya on 31/03/17.
 */

public class CheckText extends LinearLayout {

    ImageButton ib_check;
    ImageButton ib_cancel;
    ImageButton ib_edit;
    TextView tvListener;
    EditText editText;
    LinearLayout mylayout;

    String finalString = "";
    String initialString = "";

    String myCustomText;
    String myCustomHint;

    Drawable myCheckedIcon;
    Drawable myCancelIcon;
    Drawable myEditIcon;

    String myTextColor = "#000000";
    String myHintColor = "#000000";
    private ColorStateList mytintColor;
    int myImageHeight;
    int myImageWidth;
    int myTextSize;

    Context context;
    boolean ifhasFocus = false;

    private Vibrator myVib;
    OnCheckItemClicked onCheckItemClicked;

    public CheckText(Context context) {
        super(context);

    }

    public CheckText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CheckText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CheckText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        this.context = context;
        myVib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CheckText, 0, 0);
        try {
            // get the text and colors specified using the names in attrs.xml
            myCustomText = a.getString(R.styleable.CheckText_custom_text);
            myCustomHint = a.getString(R.styleable.CheckText_custom_hint);

            myCheckedIcon = a.getDrawable(R.styleable.CheckText_custom_checked_icon);
            myCancelIcon = a.getDrawable(R.styleable.CheckText_custom_cancel_icon);
            myEditIcon = a.getDrawable(R.styleable.CheckText_custom_edit_icon);

            myTextColor = a.getString(R.styleable.CheckText_custom_textcolor);
            myHintColor = a.getString(R.styleable.CheckText_custom_hintcolor);
            mytintColor = a.getColorStateList(R.styleable.CheckText_tint_color);

            myTextSize = a.getDimensionPixelOffset(R.styleable.CheckText_custom_text_size, 10);
            myImageHeight = a.getDimensionPixelSize(R.styleable.CheckText_custom_image_height, 100);
            myImageWidth = a.getDimensionPixelSize(R.styleable.CheckText_custom_image_width, 100);

        } finally {
            a.recycle();
        }

        inflate(getContext(), R.layout.check_text, this);
        this.ib_check = (ImageButton) findViewById(R.id.ib_check);
        this.ib_cancel = (ImageButton) findViewById(R.id.ib_cancel);
        this.ib_edit = (ImageButton) findViewById(R.id.ib_edit);
        this.editText = (EditText) findViewById(R.id.et);
        this.mylayout = (LinearLayout) findViewById(R.id.my_check_text);
        this.tvListener = (TextView) findViewById(R.id.tv_listener);

        this.ib_check.setBackgroundColor(000000000);
        this.ib_cancel.setBackgroundColor(000000000);
        this.ib_edit.setBackgroundColor(000000000);
        this.editText.setBackgroundColor(00000000);
        this.editText.setImeOptions(EditorInfo.IME_ACTION_DONE);


        this.ib_cancel.setImageDrawable(myCancelIcon);
        this.ib_check.setImageDrawable(myCheckedIcon);
        this.ib_edit.setImageDrawable(myEditIcon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.ib_cancel.setImageTintList(mytintColor);
            this.ib_check.setImageTintList(mytintColor);
        }


        if (this.editText.getTextColors() != null) {
            this.editText.setTextColor(Color.parseColor(myTextColor));
        }

        if (this.editText.getHintTextColors() != null) {
            this.editText.setHintTextColor(Color.parseColor(myHintColor));
        }

        this.editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize);
        this.editText.setText(myCustomText);
        this.editText.setHint(myCustomHint);


        Point point = GeneralHelper.getInstance(context).getScreenSize();
        ViewGroup.LayoutParams params = this.ib_cancel.getLayoutParams();
        params.height = params.MATCH_PARENT;
        params.width = params.width;
        this.ib_cancel.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = this.ib_check.getLayoutParams();
        params1.height = params.MATCH_PARENT;
        params1.width = params1.width;
        this.ib_check.setLayoutParams(params1);

        this.ib_check.requestLayout();
        this.ib_cancel.requestLayout();
        this.editText.setEnabled(false);


        hideIcons();

//        setScrollingEditText(editText);


        tvListener.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initialString = editText.getText().toString();
                editText.setEnabled(true);
                editText.requestFocus();
                editText.setSelection(editText.getText().length());
                editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                showSoftKeyBoard(context, v);
                showIcons();
                setHapticFeedBack(v, 40);
                getEtData();
                onCheckItemClicked.onItemClick(v.getId());
                tvListener.setVisibility(View.GONE);
            }
        });
//        tvListener.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                initialString = editText.getText().toString();
//                editText.setEnabled(true);
//                editText.requestFocus();
//                editText.setSelection(editText.getText().length());
//                editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//                showSoftKeyBoard(context, v);
//                showIcons();
//                setHapticFeedBack(v, 40);
//                getEtData();
//                tvListener.setVisibility(View.GONE);
//
//                return false;
//            }
//        });

        ib_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initialString = editText.getText().toString();
                editText.setSelection(editText.getText().length());
                editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                editText.setEnabled(true);
                editText.requestFocus();
                showSoftKeyBoard(context, v);
                showIcons();
                setHapticFeedBack(v, 40);
            }
        });


        setOnCancelListener();
        setOnCheckListener();

        this.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    finalString = initialString;
                } else {
                    finalString = editText.getText().toString();
                }
            }
        });

        this.editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showIcons();
                    ifhasFocus = true;
//                    Log.d("vracto", "has focus editext initialstring : " + initialString);
//                    Log.d("vracto", "has focus editext finalString : " + finalString);
                } else {
                    hideIcons();
                    ifhasFocus = false;
                    set_mycustom_text(finalString);
//                    Log.d("vracto", "no focus focus editext initialstring : " + initialString);
//                    Log.d("vracto", "no focus focus editext finalString : " + finalString);

                }
            }
        });

        this.editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    hideIcons();
                    set_mycustom_text(finalString);
                    hideSoftKeyboard(context, editText);
                    editText.clearFocus();
                    return true;
                }
                return false;
            }

        });

    }

    public boolean isFocused() {

        return ifhasFocus;
    }

    public void setChildWidth(int width) {
        myImageWidth = width;
    }

    public void setChildHeight(int height) {
        myImageHeight = height;
    }

    public void hideText() {
        editText.postDelayed(new Runnable() {
            public void run() {
                editText.setVisibility(View.INVISIBLE);
            }
        }, 3000);
    }

    public String get_my_custom_hint() {
        return myCustomHint;
    }

    public void set_mycustom_hint(String mycustom_hint) {
        this.myCustomText = mycustom_hint;
        if (editText != null) {
            editText.setText(mycustom_hint);
        }
    }

    public String get_my_custom_text() {
        return myCustomText;
    }

    public void set_mycustom_text(String mycustom_text) {
        this.myCustomText = mycustom_text;
        if (editText != null) {
            editText.setText(mycustom_text);
        }
    }

    public Drawable get_editicon() {
        return myEditIcon;
    }

    public void set_edit_icon(Drawable icon) {
        this.myEditIcon = (icon);
        if (ib_check != null) {
            ib_check.setImageDrawable(icon);
        }
    }

    public Drawable get_checked_icon() {
        return myCheckedIcon;
    }

    public void set_checked_icon(Drawable icon) {
        this.myCheckedIcon = (icon);
        if (ib_check != null) {
            ib_check.setImageDrawable(icon);
        }
    }


    public Drawable get_cancel_icon() {
        return myCancelIcon;
    }

    public void set_cancel_icon(Drawable icon) {
        this.myCancelIcon = (icon);
        if (ib_cancel != null) {
            ib_cancel.setImageDrawable(icon);
        }
    }

    public String get_my_custom_hintcolor() {
        return myHintColor;
    }

    public void set_mycustom_hintcolor(String mycustom_hintcolor) {
        this.myHintColor = mycustom_hintcolor;
        if (editText.getHintTextColors() != null) {
            editText.setHintTextColor(Color.parseColor(myHintColor));
        }
    }

    public String get_my_custom_textColor() {
        return myTextColor;
    }

    public void set_mycustom_textColor(String mycustom_textcolor) {
        this.myTextColor = mycustom_textcolor;
        if (editText.getTextColors() != null) {
            editText.setTextColor(Color.parseColor(myTextColor));
        }
    }

    public Integer get_my_custom_textSize() {
        return myTextSize;
    }

    public void set_mycustom_textSize(int mycustom_textsize) {
        this.myTextSize = mycustom_textsize;

        editText.setTextSize(Float.parseFloat(myTextColor));

    }


    public Integer get_my_custom_ImageHeight() {
        return myImageHeight;
    }

    public void set_mycustom_ImageHeight(int mycustom_ImageHeight) {
        this.myImageHeight = mycustom_ImageHeight;

        ib_cancel.setMaxWidth(myImageHeight);
        ib_check.setMaxWidth(myImageHeight);
    }


    public Integer get_my_custom_ImageWidth() {
        return myImageWidth;
    }

    public void set_mycustom_ImageWidth(int mycustom_MyImageWidth) {
        this.myImageWidth = mycustom_MyImageWidth;

        ib_cancel.setMaxWidth(myImageWidth);
        ib_check.setMaxWidth(myImageWidth);

    }

    private void getEtData() {
        if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
            finalString = initialString;
        }
    }


    public void showIcons() {
        ib_cancel.setVisibility(View.VISIBLE);
        ib_check.setVisibility(View.VISIBLE);
        ib_edit.setVisibility(View.GONE);
        try {
            onCheckItemClicked.isShowOnEditMode(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void hideIcons() {
        ib_cancel.setVisibility(View.GONE);
        ib_check.setVisibility(View.GONE);
        try {
            onCheckItemClicked.isShowOnEditMode(false);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
//        ib_edit.setVisibility(View.VISIBLE);
    }

    public void setOnCheckListener() {

        this.ib_check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideIcons();
                finalString = editText.getText().toString();
                setHapticFeedBack(v, 40);
                editText.clearFocus();
                editText.setGravity(Gravity.LEFT);
                hideSoftKeyboard(getContext(), v);
//                editText.setHint(finalString);
                editText.setText(finalString);
                editText.setEnabled(false);
                tvListener.setVisibility(View.VISIBLE);
                set_mycustom_text(editText.getText().toString());
                try {
                    onCheckItemClicked.onOkClick(getId());
                }catch (Exception e)
                {

                }
            }
        });

    }

    public void setOnCancelListener() {

        this.ib_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideIcons();
                setHapticFeedBack(v, 40);
                editText.setGravity(Gravity.LEFT);
                editText.clearFocus();
                hideSoftKeyboard(getContext(), v);
                editText.setText(initialString);
//                editText.setHint(initialString);
                editText.setEnabled(false);
                tvListener.setVisibility(View.VISIBLE);
                try {
                    onCheckItemClicked.onCancelClick(getId());
                }catch (Exception e)
                {

                }
            }
        });
    }


    public void setHapticFeedBack(View view, int duration) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        this.myVib.vibrate(duration);

    }

    public void setScrollingEditText(EditText et) {
        et.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (view.getId() == R.id.et) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
    }


    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static void showSoftKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public interface OnCheckItemClicked {
        void onOkClick(int id);

        void onCancelClick(int id);

        void onItemClick(int id);

        void isShowOnEditMode(boolean isShow);

    }

    public void setOnCheckItemClicked(OnCheckItemClicked checkItemClicked) {
        this.onCheckItemClicked = checkItemClicked;

    }

    /**
     * <declare-styleable name="CheckText">
     <attr name="custom_text" format="string" />
     <attr name="custom_hint" format="string" />
     <attr name="custom_checked_icon" format="reference" />
     <attr name="custom_edit_icon" format="reference" />
     <attr name="custom_cancel_icon" format="reference" />
     <attr name="custom_textcolor" format="color|reference" />
     <attr name="tint_color" format="reference|color" />
     <attr name="custom_hintcolor" format="color|reference" />
     <attr name="custom_text_size" format="dimension" />
     <attr name="custom_image_height" format="dimension" />
     <attr name="custom_image_width" format="dimension" />
     </declare-styleable>


     */

}
