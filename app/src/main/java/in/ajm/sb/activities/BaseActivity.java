package in.ajm.sb.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.transition.Transition;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ajm.sb.R;
import in.ajm.sb.customviews.ElasticAction;
import in.ajm.sb.customviews.MaterialDialog;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.CustomSpanClass;
import in.ajm.sb.helper.FileHelper;
import in.ajm.sb.helper.FontHelper;
import in.ajm.sb.helper.LayoutToImageConverter;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.PreferencesManager;
import in.ajm.sb.helper.recorder.AudioRecorder;
import in.ajm.sb.helper.recorder.AudioRecorderBuilder;
import in.ajm.sb.helper.recorder.RecordingButton;
import in.ajm.sb_library.localization.LocalizationActivity;

import static in.ajm.sb.helper.PreferencesManager.getPreferenceByKey;

/**
 * Created by DSD on 26/03/18.
 */

public class BaseActivity extends LocalizationActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Context context = this;
    Vibrator vibrator;
    ValueAnimator animator;
    MaterialDialog materialDialog;
    Toolbar toolbar;
    private TextView toolbarTitleTxtView;
    private int len = 0;
    private ProgressDialog dialog;
    private AudioRecorder recorder;
    private boolean didRecording = false;
    private RecordingButton imgBtn_recording;
    public String TAG = "Daiya";

    public static String getUserId(Context context) {
        return PreferencesManager.getPreferenceByKey(context, AppConfigs.PREFERENCE_USER_ID);
    }

    public static String getLoggedInUserId(Context context) {
        return PreferencesManager.getPreferenceByKey(context, AppConfigs.PREFERENCE_LOGGEDIN_USER_ID);
    }

    /**
     * More Effective way for option showAs
     *
     * @param staticTypefaceFieldName
     * @param newTypeface
     */
    public static void replaceFont(String staticTypefaceFieldName,
                                   final Typeface newTypeface) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Map<String, Typeface> newMap = new HashMap<String, Typeface>();
            newMap.put("sans-serif", newTypeface);
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField("sSystemFontMap");
                staticField.setAccessible(true);
                staticField.set(null, newMap);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }



    public void showDialog(Context context, String message, DialogInterface.OnClickListener onPositiveClick) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.yes), onPositiveClick);

        builder1.setNegativeButton(
                getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder1.create();
        dialog.show();
        setTypeFaceForDialog(dialog);
    }

    public void showAlertBox(Context context, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder1.create();
        dialog.show();
        setTypeFaceForDialog(dialog);
    }

    public void setupToolBar(String title) {
        setupToolBar(title, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Do nothing
            }
        });
    }

    public void setupToolBar(String title, boolean showNavigationBtn) {
        setupToolBar(title, showNavigationBtn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setupToolBar(String title, View.OnClickListener onClickListener) {
        setupToolBar(title, true, onClickListener);
    }


    public void setupToolBar(String title, boolean showNavigationBtn, View.OnClickListener onClickListener) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbarTitleTxtView = findViewById(R.id.toolbar_title);
        if (title != null) {
            toolbarTitleTxtView.setText(title);
            toolbarTitleTxtView.setVisibility(View.VISIBLE);
        } else {
            toolbarTitleTxtView.setVisibility(View.GONE);
        }

        if (!showNavigationBtn) {
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        } else {
            final ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_backspace_black_24dp, null);
            drawable = DrawableCompat.wrap(drawable);
            actionBar.setHomeAsUpIndicator(drawable);
            DrawableCompat.setTint(drawable, getTextColor(context));
            toolbar.setNavigationOnClickListener(onClickListener);
        }
    }

    public void addBackButton(Context context) {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_backspace_black_24dp, null);
        drawable = DrawableCompat.wrap(drawable);
        actionBar.setHomeAsUpIndicator(drawable);
        DrawableCompat.setTint(drawable, getTextColor(context));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void addBackButton(Context context, String title) {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_backspace_black_24dp, null);
        drawable = DrawableCompat.wrap(drawable);
        actionBar.setHomeAsUpIndicator(drawable);
        DrawableCompat.setTint(drawable, getTextColor(context));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void disableEnableControls(ViewGroup vg, boolean enable) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls((ViewGroup) child, enable);
            }
        }
    }

    public void enableDisableView(View view, boolean enable) {
        view.setEnabled(enable);
        if (enable) {
            view.setAlpha(0.9f);
        } else {
            view.setAlpha(0.3f);
        }
    }

    /**
     * Set custom fonts to menu item
     *
     * @param menu
     * @param context
     */
    public void setTypeFaceForMenuItems(Menu menu, Context context) {
        Typeface typeface = FontHelper.getInstance(context).getRegularFont();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem, typeface);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi, typeface);
        }
    }

    public void applyFontToMenuItem(MenuItem mi, Typeface font) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomSpanClass("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    String getDeviceTypes() {
        boolean status = (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        if (status) {
            return "Tablet";
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return "Mobile";
        }
    }

    public void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(Intent.createChooser(browserIntent, "Open with"));
    }

    void openThirdPartyCallbackUrl(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getPreferenceByKey(context, "THIRD_PARTY_CALL_BACK_URL")));
        startActivity(i);
    }

    public void setCursorPosition(final EditText et) {
        et.setCursorVisible(false);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                et.setCursorVisible(true);
//                et.setSelection(et.getText().length());
//                et.append(et.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et.setCursorVisible(true);
//                et.setSelection(et.getText().length());
//                et.append(et.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et.length() == 0) {
                    et.setCursorVisible(false);
                } else {
                    et.setCursorVisible(true);
                }

//                et.setSelection(et.getText().length());
//                et.append(et.getText().toString());
            }
        });
    }

    public String phoneNumberFormat(String str) {
        str = str.replaceAll("\\D+", "");
        int len = str.length();
        if (len < 4) {
            str = str.substring(0, str.length());
        } else if (len < 7) {
            String firstthree = str.substring(0, 3);
            String nextthree = str.substring(3, str.length());
            str = firstthree + "-" + nextthree;
        } else if (len < 11) {
            String firstthree = str.substring(0, 3);
            String nextthree = str.substring(3, 6);
            String lastfour = str.substring(6, str.length());

            str = firstthree + "-" + nextthree + "-" + lastfour;
        } else {
            String firstthree = str.substring(0, 3);
            String nextthree = str.substring(3, 6);
            String lastfour = str.substring(6, 10);
            String tenplus = str.substring(10, str.length());
            str = firstthree + "-" + nextthree + "-" + lastfour + "-" + tenplus;
        }
        return str;
    }

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void validationForPhoneNumber(final EditText edittext) {

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (edittext.getText().length() <= 12) {
                        if (len > edittext.getText().length()) {
                            len--;
                            return;
                        }
                        len = edittext.getText().length();

                        if (edittext.getText().toString().charAt(edittext.getText().toString().length() - 1) != '-') {
                            if (len == 4 || len == 8) {
                                String number = edittext.getText().toString();
                                String dash = number.charAt(number.length() - 1) == '-' ? "" : "-";
                                number = number.substring(0, (len - 1)) + dash + number.substring((len - 1), number.length());
                                try {
                                    edittext.setText(number + "");
                                    edittext.setSelection(number.length());
                                } catch (Exception e) {
                                    edittext.setText(number + "");
                                }
                            }
                        }
                    }

                } catch (Exception e) {

                }
            }
        });
    }

    String[] splitName(String nameStr) {
        int firstSpace = nameStr.indexOf(" ");
        if (firstSpace != -1) {
            String firstName = nameStr.substring(0, firstSpace);
            String lastName = nameStr.substring(firstSpace).trim();
            return new String[]{firstName, lastName};
        } else {
            return new String[]{nameStr, ""};
        }
    }

    public void hideKeyboard() {
        try {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public void onbackPressed(View view) {
        hideKeyboard();
        onBackPressed();
    }

    String getSelectedOrgId() {
        return PreferencesManager.getPreferenceByKey(context, AppConfigs.PREFERENCE_ORG_ID);
    }

    void setSelectedOrgId(String orgId) {
        PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_ORG_ID, orgId);
    }

    public void setUserId(String userId) {
        PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_USER_ID, userId);
    }

    public void setLoggedInUserId(String userId) {
        PreferencesManager.setPreferenceByKey(context, AppConfigs.PREFERENCE_LOGGEDIN_USER_ID, userId);
    }

    public void showLoader() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.show();
        } else if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void hideLoader() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public void animateOnFocus(View v, ViewGroup rootLayout) {
        final CardView first_container = (CardView) v.getParent();
        final CardView second_container = (CardView) first_container.getParent();

        final int first_curr_radius = (int) getResources().getDimension(R.dimen.first_card_radius);
        final int first_target_radius = (int) getResources().getDimension(R.dimen.first_card_radius_on_focus);

        final int second_curr_radius = (int) getResources().getDimension(R.dimen.second_card_radius);
        final int second_target_radius = (int) getResources().getDimension(R.dimen.second_card_radius_on_focus);

        final int first_curr_color = ContextCompat.getColor(this, android.R.color.transparent);
        final int first_target_color = getAccentColor(this);

        final int second_curr_color = getAccentColor(this);
        final int second_target_color = getAccentColor(this);

        ValueAnimator first_anim = new ValueAnimator();
        first_anim.setIntValues(first_curr_color, first_target_color);
        first_anim.setEvaluator(new ArgbEvaluator());
        first_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                first_container.setCardBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        ValueAnimator second_anim = new ValueAnimator();
        second_anim.setIntValues(second_curr_color, second_target_color);
        second_anim.setEvaluator(new ArgbEvaluator());
        second_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                second_container.setCardBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int diff_radius = first_curr_radius - first_target_radius;
                int radius = first_target_radius + (int) (diff_radius - (diff_radius * interpolatedTime));

                first_container.setRadius(radius);
                first_container.requestLayout();

                diff_radius = second_curr_radius - second_target_radius;
                radius = second_target_radius + (int) (diff_radius - (diff_radius * interpolatedTime));

                second_container.setRadius(radius);
                second_container.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        first_anim.setDuration(200);
        second_anim.setDuration(200);

        first_anim.start();
        second_anim.start();
        first_container.startAnimation(a);
    }

    public void animateOnFocusLost(View v, ViewGroup rootLayout) {
        final CardView first_container = (CardView) v.getParent();
        final CardView second_container = (CardView) first_container.getParent();

        final int first_curr_radius = (int) getResources().getDimension(R.dimen.first_card_radius_on_focus);
        final int first_target_radius = (int) getResources().getDimension(R.dimen.first_card_radius);

        final int second_curr_radius = (int) getResources().getDimension(R.dimen.second_card_radius_on_focus);
        final int second_target_radius = (int) getResources().getDimension(R.dimen.second_card_radius);

        final int first_curr_color = getAccentColor(this);
        final int first_target_color = ContextCompat.getColor(this, android.R.color.transparent);

        final int second_curr_color = getAccentColor(this);
        final int second_target_color = getAccentColor(this);

        ValueAnimator first_anim = new ValueAnimator();
        first_anim.setIntValues(first_curr_color, first_target_color);
        first_anim.setEvaluator(new ArgbEvaluator());
        first_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                first_container.setCardBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        ValueAnimator second_anim = new ValueAnimator();
        second_anim.setIntValues(second_curr_color, second_target_color);
        second_anim.setEvaluator(new ArgbEvaluator());
        second_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                second_container.setCardBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int diff_radius = first_curr_radius - first_target_radius;
                int radius = first_target_radius + (int) (diff_radius - (diff_radius * interpolatedTime));

                first_container.setRadius(radius);
                first_container.requestLayout();

                diff_radius = second_curr_radius - second_target_radius;
                radius = second_target_radius + (int) (diff_radius - (diff_radius * interpolatedTime));

                second_container.setRadius(radius);
                second_container.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        first_anim.setDuration(200);
        second_anim.setDuration(200);

        first_anim.start();
        second_anim.start();
        first_container.startAnimation(a);

    }


    public boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void setElasticAction(ViewGroup anyView, float scaleXY, int duration, boolean wannaVibrate) {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ElasticAction.doAction(anyView, duration, scaleXY, scaleXY);
        if (wannaVibrate) {
            vibrator.vibrate(50);
        }
    }

    private void startRecording(AudioRecorder recorder, final RecordingButton imgBtn_recording, String parentDirName) {
        try {
            imgBtn_recording.setVisibility(View.GONE);
            boolean status = FileHelper.createNewFile(context, parentDirName, "audio.m4a", true, true);
            if (status) {
                if (recorder == null) {
                    String filePath = FileHelper.getDirOrFilePath(context, parentDirName, "audio.m4a", true);
                    recorder = AudioRecorderBuilder.with(context)
                            .fileName(filePath == null ? "" : filePath)
                            .config(AudioRecorder.MediaRecorderConfig.DEFAULT)
                            .loggable()
                            .build();
                }

                if (recorder != null) {
                    recorder.start(new AudioRecorder.OnStartListener() {
                        @Override
                        public void onStarted() {
                            didRecording = true;
                            imgBtn_recording.setVisibility(View.VISIBLE);
                            imgBtn_recording.startAnimation();
                            imgBtn_recording.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                        }

                        @Override
                        public void onException(Exception e) {
                            didRecording = false;
                            imgBtn_recording.setVisibility(View.GONE);
                        }
                    });
                }
            }
        } catch (Exception e) {
            LoggerCustom.printStackTrace(e);
            didRecording = false;
            imgBtn_recording.setVisibility(View.GONE);
        }
    }

    private void cancelRecoding(AudioRecorder recorder, final RecordingButton imgBtn_recording) {
        if (recorder != null && recorder.isRecording()) {
            recorder.pause(new AudioRecorder.OnPauseListener() {
                @Override
                public void onPaused(String activeRecordFileName) {
                    didRecording = false;
                    imgBtn_recording.setVisibility(View.VISIBLE);
                    imgBtn_recording.stopAnimation();
                    imgBtn_recording.setImageResource(R.drawable.ic_mic_black_24dp);
                }

                @Override
                public void onException(Exception e) {
                    didRecording = false;
                    imgBtn_recording.setVisibility(View.GONE);
                }
            });
        }
    }

    private void pauseRecording(AudioRecorder recorder, final RecordingButton imgBtn_recording) {
        if (recorder != null && recorder.isRecording()) {
            recorder.pause(new AudioRecorder.OnPauseListener() {
                @Override
                public void onPaused(String activeRecordFileName) {
                    didRecording = false;
                    imgBtn_recording.setVisibility(View.VISIBLE);
                    imgBtn_recording.stopAnimation();
                    imgBtn_recording.setImageResource(R.drawable.ic_mic_black_24dp);
                }

                @Override
                public void onException(Exception e) {
                    didRecording = false;
                    imgBtn_recording.setVisibility(View.GONE);
                }
            });
        }
    }

    public void setRecorderOnDestroy(AudioRecorder recorder) {
        didRecording = false;
        if (recorder != null && recorder.isRecording())
            recorder.cancel(null);
    }

    public void checkForTheme(Context context) {
        try {
            String mySelectedTheme = PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme));
            if (mySelectedTheme.contains(getResources().getString(R.string.default_theme))) {
                setTheme(R.style.MyTheme_Default);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.red))) {
                setTheme(R.style.MyTheme_Red);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.white))) {
                setTheme(R.style.MyTheme_White);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.black))) {
                setTheme(R.style.MyTheme_Black);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.teal))) {
                setTheme(R.style.MyTheme_Teal);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.brown))) {
                setTheme(R.style.MyTheme_Brown);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.cyan))) {
                setTheme(R.style.MyTheme_Cyan);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.pink))) {
                setTheme(R.style.MyTheme_Pink);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.orange))) {
                setTheme(R.style.MyTheme_Orange);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.green))) {
                setTheme(R.style.MyTheme_Green);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.purple))) {
                setTheme(R.style.MyTheme_Purple);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.blue))) {
                setTheme(R.style.MyTheme_Blue);
            } else if (mySelectedTheme.contains(getResources().getString(R.string.yellow))) {
                setTheme(R.style.MyTheme_Yellow);
            } else {
                setTheme(R.style.MyTheme_Default);
            }
        } catch (Exception e) {
            setTheme(R.style.MyTheme_Default);
        }
    }

    public int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public int getColorPrimaryDark(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public int getColorMyThemelight(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_theme_light, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    /*White*/
    public int getOppositeColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_text_color_inverse, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    /*Black*/
    public int getTextColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_text_color, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Transition transition;
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.BOTTOM);
            slideTransition.setDuration(300);
            Slide slideTransition1 = new Slide();
            slideTransition1.setSlideEdge(Gravity.BOTTOM);
            slideTransition1.setDuration(300);
            getWindow().setEnterTransition(slideTransition);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setupWindowAnimationss() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Transition transition;
            Slide slideTransition = new Slide();
            slideTransition.excludeTarget(android.R.id.statusBarBackground, false);
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(300);
            Slide slideTransition2 = new Slide();
            slideTransition2.setSlideEdge(Gravity.LEFT);
            slideTransition2.setDuration(300);
            getWindow().setEnterTransition(slideTransition);
            getWindow().setExitTransition(slideTransition2);
        }
    }

    public float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public void showAddBButton(ImageButton addtypebutton) {

        addtypebutton.setVisibility(View.VISIBLE);
        addtypebutton.animate().translationX(0 - dpToPixels(0, this)).setInterpolator(new DecelerateInterpolator()).setDuration(200).start();

    }

    public void hideAddButton(boolean delay, final ImageButton addtypebutton) {

        long duration = 0;
        if (delay) {
            duration = 250;
        }

        addtypebutton.animate().translationX(addtypebutton.getBottom() + dpToPixels(5, this)).setInterpolator
                (new AccelerateInterpolator()).setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                addtypebutton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();

    }

    private void hideAddButton(ImageButton addtypebutton) {
        hideAddButton(false, addtypebutton);
    }

    public void recyclerScroll(RecyclerView recyclerviewType, final ImageButton addtypebutton) {
        recyclerviewType.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx > 0 || dx < 0 && addtypebutton.isShown()) {
                    hideAddButton(addtypebutton);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showAddBButton(addtypebutton);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void expandingView(final ViewGroup viewGroup) {
        viewGroup.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewGroup.getViewTreeObserver().removeOnPreDrawListener(this);
//                    linearLayout.setVisibility(View.GONE);
                final int widthSpec = View.MeasureSpec.makeMeasureSpec(
                        0, View.MeasureSpec.UNSPECIFIED);
                final int heightSpec = View.MeasureSpec
                        .makeMeasureSpec(0,
                                View.MeasureSpec.UNSPECIFIED);
                viewGroup.measure(widthSpec, heightSpec);

                animator = slideAnimator(0,
                        viewGroup.getMeasuredHeight(), viewGroup);
                return true;
            }
        });
    }

    public void expand(int duration, ViewGroup viewGroup) {
        animator.setDuration(duration);
        viewGroup.setVisibility(View.VISIBLE);
        animator.start();
    }

    public void collapse(int duration, final ViewGroup viewGroup) {
        animator.setDuration(duration);
        int finalHeight = viewGroup.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, viewGroup);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                // Height=0, but it set visibility to GONE
                viewGroup.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end, final ViewGroup viewGroup) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = viewGroup
                        .getLayoutParams();
                layoutParams.height = value;
                viewGroup.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void setThemeSelectionDialog(final Context context) {

        final AlertDialog.Builder materialDialog = new AlertDialog.Builder(context);
        materialDialog.setCancelable(true);
        materialDialog.setTitle(getResources().getString(R.string.select_theme));
        final View view = LayoutInflater.from(context).inflate(R.layout.theme_layout, null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        final RadioButton radio_btn_default = (RadioButton) view.findViewById(R.id.radio_btn_default);
        final RadioButton radio_btn_blue = (RadioButton) view.findViewById(R.id.radio_btn_blue);
        final RadioButton radio_btn_red = (RadioButton) view.findViewById(R.id.radio_btn_red);
        final RadioButton radio_btn_yellow = (RadioButton) view.findViewById(R.id.radio_btn_yellow);
        final RadioButton radio_btn_teal = (RadioButton) view.findViewById(R.id.radio_btn_teal);
        final RadioButton radio_btn_green = (RadioButton) view.findViewById(R.id.radio_btn_green);
        final RadioButton radio_btn_purple = (RadioButton) view.findViewById(R.id.radio_btn_purple);
        final RadioButton radio_btn_pink = (RadioButton) view.findViewById(R.id.radio_btn_pink);
        final RadioButton radio_btn_orange = (RadioButton) view.findViewById(R.id.radio_btn_orange);
        final RadioButton radio_btn_cyan = (RadioButton) view.findViewById(R.id.radio_btn_cyan);
        final RadioButton radio_btn_brown = (RadioButton) view.findViewById(R.id.radio_btn_brown);
        final RadioButton radio_btn_white = (RadioButton) view.findViewById(R.id.radio_btn_white);
        final RadioButton radio_btn_black = (RadioButton) view.findViewById(R.id.radio_btn_black);

        radio_btn_default.setTag(getResources().getString(R.string.default_theme));
        radio_btn_blue.setTag(getResources().getString(R.string.blue));
        radio_btn_red.setTag(getResources().getString(R.string.red));
        radio_btn_yellow.setTag(getResources().getString(R.string.yellow));
        radio_btn_teal.setTag(getResources().getString(R.string.teal));
        radio_btn_green.setTag(getResources().getString(R.string.green));
        radio_btn_brown.setTag(getResources().getString(R.string.brown));
        radio_btn_purple.setTag(getResources().getString(R.string.purple));
        radio_btn_pink.setTag(getResources().getString(R.string.pink));
        radio_btn_orange.setTag(getResources().getString(R.string.orange));
        radio_btn_cyan.setTag(getResources().getString(R.string.cyan));
        radio_btn_white.setTag(getResources().getString(R.string.white));
        radio_btn_black.setTag(getResources().getString(R.string.black));

        if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.default_theme))) {
            radio_btn_default.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.blue))) {
            radio_btn_blue.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.red))) {
            radio_btn_red.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.yellow))) {
            radio_btn_yellow.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.teal))) {
            radio_btn_teal.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.green))) {
            radio_btn_green.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.brown))) {
            radio_btn_brown.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.purple))) {
            radio_btn_purple.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.pink))) {
            radio_btn_pink.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.orange))) {
            radio_btn_orange.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.cyan))) {
            radio_btn_cyan.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.white))) {
            radio_btn_white.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.black))) {
            radio_btn_black.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_btn_default) {
                    radio_btn_default.setChecked(true);
                } else if (checkedId == R.id.radio_btn_blue) {
                    radio_btn_blue.setChecked(true);
                } else if (checkedId == R.id.radio_btn_red) {
                    radio_btn_red.setChecked(true);
                } else if (checkedId == R.id.radio_btn_yellow) {
                    radio_btn_yellow.setChecked(true);
                } else if (checkedId == R.id.radio_btn_teal) {

                    radio_btn_teal.setChecked(true);
                } else if (checkedId == R.id.radio_btn_green) {

                    radio_btn_green.setChecked(true);
                } else if (checkedId == R.id.radio_btn_brown) {

                    radio_btn_brown.setChecked(true);
                } else if (checkedId == R.id.radio_btn_purple) {

                    radio_btn_purple.setChecked(true);
                } else if (checkedId == R.id.radio_btn_pink) {

                    radio_btn_pink.setChecked(true);
                } else if (checkedId == R.id.radio_btn_orange) {

                    radio_btn_orange.setChecked(true);
                } else if (checkedId == R.id.radio_btn_cyan) {

                    radio_btn_cyan.setChecked(true);
                } else if (checkedId == R.id.radio_btn_white) {

                    radio_btn_white.setChecked(true);
                } else if (checkedId == R.id.radio_btn_black) {

                    radio_btn_black.setChecked(true);
                }
            }
        });
        materialDialog.setView(view);

        materialDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedmemberId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedbutton = (RadioButton) view.findViewById(selectedmemberId);
                if (!PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.theme)).equals(selectedbutton.getTag().toString())) {
                    if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.default_theme))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.default_theme));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.blue))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.blue));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.red))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.red));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.yellow))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.yellow));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.teal))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.teal));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.green))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.green));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.brown))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.brown));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.purple))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.purple));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.pink))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.pink));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.orange))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.orange));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.cyan))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.cyan));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.white))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.white));
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.black))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.theme), getResources().getString(R.string.black));
                    }
                    PreferencesManager.setPreferenceBooleanByKey(context, "setTheme", true);
                    recreate();
                }
                dialog.dismiss();
            }
        });

        materialDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = materialDialog.create();
        dialog.show();
        setTypeFaceForDialog(dialog);
    }

    public void setLanguagedialog(final Context context, final TextView selectedLanguage) {

        final AlertDialog.Builder materialDialog = new AlertDialog.Builder(context);
        materialDialog.setCancelable(true);
        materialDialog.setTitle(getResources().getString(R.string.select_language));

        final View view = LayoutInflater.from(context).inflate(R.layout.language_layout, null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        final RadioButton radio_btn_english = (RadioButton) view.findViewById(R.id.radio_btn_english);
        final RadioButton radio_btn_hindi = (RadioButton) view.findViewById(R.id.radio_btn_hindi);
        final RadioButton radio_btn_japanese = (RadioButton) view.findViewById(R.id.radio_btn_japanese);
        final RadioButton radio_btn_french = (RadioButton) view.findViewById(R.id.radio_btn_french);
        final RadioButton radio_btn_german = (RadioButton) view.findViewById(R.id.radio_btn_german);
        final RadioButton radio_btn_portuguese = (RadioButton) view.findViewById(R.id.radio_btn_portuguese);
        final RadioButton radio_btn_russian = (RadioButton) view.findViewById(R.id.radio_btn_russian);
        final RadioButton radio_btn_mandarin = (RadioButton) view.findViewById(R.id.radio_btn_mandarin);
        final RadioButton radio_btn_cantonese = (RadioButton) view.findViewById(R.id.radio_btn_cantonese);
        final RadioButton radio_btn_spanish = (RadioButton) view.findViewById(R.id.radio_btn_spanish);

        radio_btn_english.setTag(getResources().getString(R.string.language_english));
        radio_btn_hindi.setTag(getResources().getString(R.string.language_hindi));
        radio_btn_japanese.setTag(getResources().getString(R.string.language_japanese));
        radio_btn_french.setTag(getResources().getString(R.string.language_french));
        radio_btn_german.setTag(getResources().getString(R.string.language_german));
        radio_btn_portuguese.setTag(getResources().getString(R.string.language_portuguese));
        radio_btn_russian.setTag(getResources().getString(R.string.language_russian));
        radio_btn_mandarin.setTag(getResources().getString(R.string.language_mandarin));
        radio_btn_cantonese.setTag(getResources().getString(R.string.language_cantonese));
        radio_btn_spanish.setTag(getResources().getString(R.string.language_spanish));

        if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_english))) {
            radio_btn_english.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_hindi))) {
            radio_btn_hindi.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_japanese))) {
            radio_btn_japanese.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_french))) {
            radio_btn_french.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_german))) {
            radio_btn_german.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_portuguese))) {
            radio_btn_portuguese.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_russian))) {
            radio_btn_russian.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_mandarin))) {
            radio_btn_mandarin.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_cantonese))) {
            radio_btn_cantonese.setChecked(true);
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_spanish))) {
            radio_btn_spanish.setChecked(true);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_btn_english) {

                    radio_btn_english.setChecked(true);
                } else if (checkedId == R.id.radio_btn_hindi) {

                    radio_btn_hindi.setChecked(true);
                } else if (checkedId == R.id.radio_btn_japanese) {

                    radio_btn_japanese.setChecked(true);
                } else if (checkedId == R.id.radio_btn_french) {

                    radio_btn_french.setChecked(true);
                } else if (checkedId == R.id.radio_btn_german) {

                    radio_btn_german.setChecked(true);
                } else if (checkedId == R.id.radio_btn_portuguese) {

                    radio_btn_portuguese.setChecked(true);
                } else if (checkedId == R.id.radio_btn_russian) {

                    radio_btn_russian.setChecked(true);
                } else if (checkedId == R.id.radio_btn_mandarin) {

                    radio_btn_mandarin.setChecked(true);
                } else if (checkedId == R.id.radio_btn_cantonese) {

                    radio_btn_cantonese.setChecked(true);
                } else if (checkedId == R.id.radio_btn_spanish) {

                    radio_btn_spanish.setChecked(true);
                }

            }
        });
        materialDialog.setView(view);
        materialDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferencesManager.setPreferenceBooleanByKey(context, "SETUI_LANGUAGE", true);
                int selectedmemberId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedbutton = (RadioButton) view.findViewById(selectedmemberId);
                if (!PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(selectedbutton.getTag().toString())) {
                    if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_english))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_english));
                        setLanguage("en");
                        selectedLanguage.setText("English");
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_hindi))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_hindi));
                        setLanguage("hi");
                        selectedLanguage.setText("हिंदी");
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_japanese))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_japanese));
                        setLanguage("ja");
                        selectedLanguage.setText(radio_btn_japanese.getText().toString());
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_french))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_french));
                        setLanguage("fr");
                        selectedLanguage.setText(radio_btn_french.getText().toString());
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_german))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_german));
                        setLanguage("de");
                        selectedLanguage.setText(radio_btn_german.getText().toString());
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_portuguese))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_portuguese));
                        setLanguage("pt");
                        selectedLanguage.setText(radio_btn_portuguese.getText().toString());
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_russian))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_russian));
                        setLanguage("ru");
                        selectedLanguage.setText(radio_btn_russian.getText().toString());
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_mandarin))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_mandarin));
                        setLanguage("zh");
                        selectedLanguage.setText(radio_btn_mandarin.getText().toString());
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_cantonese))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_cantonese));
                        setLanguage("zho");
                        selectedLanguage.setText(radio_btn_cantonese.getText().toString());
                    } else if (selectedbutton.getTag().toString().equals(getResources().getString(R.string.language_spanish))) {
                        PreferencesManager.setPreferenceByKey(context, getResources().getString(R.string.language), getResources().getString(R.string.language_spanish));
                        setLanguage("es");
                        selectedLanguage.setText(radio_btn_spanish.getText().toString());
                    }
                    PreferencesManager.setPreferenceBooleanByKey(context, "setTheme", true);
                    recreate();
                }
                dialog.dismiss();
            }
        });

        materialDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = materialDialog.create();
        dialog.show();
        setTypeFaceForDialog(dialog);
    }

    public float calculateValue(int total, int value) {
        try {
            float r = ((value * 100) / total);
            return (float) Math.round(r * 100.0f) / 100.0f;
        } catch (Exception e) {
            return 0.00f;
        }
    }

    public void setTypeFaceForMenuItemsForActionSingle(Menu menu, Context context) {
        Typeface typeface = FontHelper.getInstance(context).getRegularFont();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    replaceFont(subMenuItem.getTitle().toString(), typeface);
                }
            }
            //the method we have create in activity
            replaceFont(mi.getTitle().toString(), typeface);
        }
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    private class RecodingBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (recorder != null) {
                if (recorder.isRecording()) {

                }
//                    pauseRecording();
                else {

                }
//                    startRecording();
            } else {
//                startRecording();
            }
        }
    }

    public void setTypeFaceForDialog(AlertDialog dialog){
        TextView textView = (TextView) dialog.getWindow().findViewById(android.R.id.message);
        TextView alertTitle = (TextView) dialog.getWindow().findViewById(R.id.alertTitle);
        Button button1 = (Button) dialog.getWindow().findViewById(android.R.id.button1);
        Button button2 = (Button) dialog.getWindow().findViewById(android.R.id.button2);
        textView.setTypeface(FontHelper.getInstance(context).getRegularFont());
        alertTitle.setTypeface(FontHelper.getInstance(context).getRegularFont());
        button1.setTypeface(FontHelper.getInstance(context).getRegularFont());
        button2.setTypeface(FontHelper.getInstance(context).getRegularFont());
    }


    public Bitmap getImageBitmapFromLayout(View view){
        LayoutToImageConverter layoutToImageConverter = new LayoutToImageConverter(context, view);
        return layoutToImageConverter.getImageFromLayout();
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }


}
