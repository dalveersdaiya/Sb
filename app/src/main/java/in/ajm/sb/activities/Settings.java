package in.ajm.sb.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.data.User;
import in.ajm.sb.fragments.BottomSheetSwitchUser;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.PreferencesManager;
import in.ajm.sb.interfaces.OnUserChanged;

public class Settings extends BaseActivity implements View.OnClickListener, OnUserChanged {

    Button button_select_theme;
    Button button_select_language;
    Button button_switch_user;
    Button button_test_new;
    TextView tv_selected_theme;
    TextView tv_current_user;
    TextView tv_selected_language;
    Context context;
    AlertDialog alert11;
    List<User> userList = new ArrayList<>();
    User user;
    boolean isEdited = false;
    int selectedPosition = 0;
    int userType = 01;
    String current_fragment = "HomeFragment.class";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_settings);
        viewByIds();
        applyClickListeners();
        getIntentValues();
        setlanguagetext(tv_selected_language);
        setSelectedTheme(tv_selected_theme);
        setTitle(getResources().getString(R.string.switch_user));
        setUi();
    }

    public void getIntentValues() {
        userType = getIntent().getExtras().getInt(AppConfigs.USER_TYPE, AppConfigs.PARENT_TYPE);
        current_fragment = getIntent().getExtras().getString("current_fragment", "HomeFragment.class");
    }

    public void viewByIds() {
        context = this;
        button_select_language = findViewById(R.id.button_select_language);
        button_select_theme = findViewById(R.id.button_select_theme);
        button_switch_user = findViewById(R.id.button_switch_user);
        tv_current_user = findViewById(R.id.tv_current_user);
        tv_selected_theme = findViewById(R.id.tv_selected_theme);
        tv_selected_language = findViewById(R.id.tv_selected_language);
        button_test_new = findViewById(R.id.button_test_new);
    }

    public void applyClickListeners() {
        button_select_theme.setOnClickListener(this);
        button_select_language.setOnClickListener(this);
        button_switch_user.setOnClickListener(this);
        button_test_new.setOnClickListener(this);
    }

    public void setUi() {
        setupToolBar(getResources().getString(R.string.settings), true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToHomeActivity();
            }
        });
    }

    public void moveToHomeActivity() {
        Intent intent = new Intent(Settings.this, HomeTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppConfigs.USER_TYPE, 01);
        intent.putExtra("current_fragment", current_fragment);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_select_language:
                setLanguagedialog(context, tv_selected_language);
                break;
            case R.id.button_select_theme:
                setThemeSelectionDialog(context);
                break;
            case R.id.button_switch_user:
                openBottomSheetSwitchUser();
                break;
            case R.id.button_test_new:
                openTestActivity();
                break;
            default:
                break;

        }
    }

    public void openBottomSheetSwitchUser() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetSwitchUser(this, isEdited, selectedPosition);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    private void setlanguagetext(TextView selected_language) {
        if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_english))) {
            selected_language.setText("English");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_hindi))) {
            selected_language.setText("हिंदी");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_japanese))) {
            selected_language.setText("日本語");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_french))) {
            selected_language.setText("français");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_german))) {
            selected_language.setText("Deutsche");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_portuguese))) {
            selected_language.setText("Português");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_russian))) {
            selected_language.setText("русский");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_mandarin))) {
            selected_language.setText("普通话");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_cantonese))) {
            selected_language.setText("廣東話");
        } else if (PreferencesManager.getPreferenceByKey(context, getResources().getString(R.string.language)).equals(getResources().getString(R.string.language_spanish))) {
            selected_language.setText("Español");
        }
    }

    public void setSelectedTheme(TextView selected_theme) {
        if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.default_theme))) {
            selected_theme.setText(getResources().getString(R.string.default_theme_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.blue))) {
            selected_theme.setText(getResources().getString(R.string.blue_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.red))) {
            selected_theme.setText(getResources().getString(R.string.red_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.yellow))) {
            selected_theme.setText(getResources().getString(R.string.yellow_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.teal))) {
            selected_theme.setText(getResources().getString(R.string.teal_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.green))) {
            selected_theme.setText(getResources().getString(R.string.green_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.brown))) {
            selected_theme.setText(getResources().getString(R.string.brown_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.purple))) {
            selected_theme.setText(getResources().getString(R.string.purple_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.pink))) {
            selected_theme.setText(getResources().getString(R.string.pink_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.orange))) {
            selected_theme.setText(getResources().getString(R.string.orange_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.cyan))) {
            selected_theme.setText(getResources().getString(R.string.cyan_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.white))) {
            selected_theme.setText(getResources().getString(R.string.white_theme));
        } else if (PreferencesManager.getPreferenceByKey(Settings.this, getResources().getString(R.string.theme)).contains(getResources().getString(R.string.black))) {
            selected_theme.setText(getResources().getString(R.string.black_theme));
        } else {
            selected_theme.setText(getResources().getString(R.string.default_theme_theme));
        }
    }

    @Override
    public void onUserSwitched(int pos, String id, String userName) {
        isEdited = true;
        selectedPosition = pos;
        tv_current_user.setText(userName);
    }

    public void openTestActivity(){
        Intent intent = new Intent(this, ChipTestActivity.class);
        startActivity(intent);
    }


}
