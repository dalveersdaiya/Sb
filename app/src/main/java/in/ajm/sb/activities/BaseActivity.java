package in.ajm.sb.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import in.ajm.sb.R;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.CustomSpanClass;
import in.ajm.sb.helper.FontHelper;
import in.ajm.sb.helper.PreferencesManager;

import static in.ajm.sb.helper.PreferencesManager.getPreferenceByKey;

/**
 * Created by DSD on 26/03/18.
 */

public class BaseActivity extends AppCompatActivity {

    Context context = this;
    private TextView toolbarTitleTxtView;
    private int len = 0;
    private ProgressDialog dialog;

    public static String getUserId(Context context) {
        return PreferencesManager.getPreferenceByKey(context, AppConfigs.PREFERENCE_USER_ID);
    }

    public static String getLoggedInUserId(Context context) {
        return PreferencesManager.getPreferenceByKey(context, AppConfigs.PREFERENCE_LOGGEDIN_USER_ID);
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

        AlertDialog alert11 = builder1.create();
        alert11.show();
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

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    void setupToolBar(String title) {
        setupToolBar(title, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void setupToolBar(String title, boolean showNavigationBtn) {
        setupToolBar(title, showNavigationBtn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void setupToolBar(String title, View.OnClickListener onClickListener) {
        setupToolBar(title, true, onClickListener);
    }

    void setToolBarTitle(String title) {
        if (toolbarTitleTxtView != null) {
            if (title != null) {
                toolbarTitleTxtView.setText(title);
                toolbarTitleTxtView.setVisibility(View.VISIBLE);
            } else {
                toolbarTitleTxtView.setVisibility(View.GONE);
            }
        }
    }

    private void setupToolBar(String title, boolean showNavigationBtn, View.OnClickListener onClickListener) {
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

        toolbar.setNavigationOnClickListener(onClickListener);
        if (!showNavigationBtn) {
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        }
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

    /**
     * Example call device api
     *
     * private void callApi()
     {
     String vesioncode = Build.VERSION.RELEASE;
     String deviceType = Build.VERSION.SDK_INT + "";
     HashMap<String, String> hashMap = new HashMap<>();
     hashMap.put("push_token", PreferencesManager.getPreferenceByKey(LoginActivity.this, AppConfigs.PREFERENCE_PUSH_TOKEN));
     try
     {
     if (Device.get().getUserId() == null && Device.get().getOrgId() == null)
     {
     hashMap.put("org_id", "");
     hashMap.put("user_id", "");
     } else
     {
     hashMap.put("org_id", Device.get().getOrgId());
     hashMap.put("user_id", Device.get().getUserId());
     }
     } catch (Exception e)
     {
     hashMap.put("org_id", "");
     hashMap.put("user_id", "");
     }

     hashMap.put("device_type", deviceType);
     hashMap.put("device_os", "ANDROID");
     hashMap.put("device_version", vesioncode);
     hashMap.put("lat", "");
     hashMap.put("lng", "");

     ApiParams apiParams = new ApiParams();
     apiParams.mHashMap = hashMap;
     apiParams.orgId = getSelectedOrgId();
     DeviceCaller.instance().post(LoginActivity.this, apiParams, this, ApiType.REGISTER);
     }
     */
}
