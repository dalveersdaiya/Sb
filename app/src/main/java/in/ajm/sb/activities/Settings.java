package in.ajm.sb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.ajm.sb.R;
import in.ajm.sb.helper.PreferencesManager;

public class Settings extends BaseActivity implements View.OnClickListener {
    Button button_select_theme;
    Button button_select_language;
    TextView tv_selected_theme;
    TextView tv_selected_language;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_settings);
        viewByIds();
        applyClickListeners();
        setupToolBar(getResources().getString(R.string.settings), true);
        setlanguagetext(tv_selected_language);
    }

    public void viewByIds() {
        context = this;
        button_select_language = findViewById(R.id.button_select_language);
        button_select_theme = findViewById(R.id.button_select_theme);
        tv_selected_theme = findViewById(R.id.tv_selected_theme);
        tv_selected_language = findViewById(R.id.tv_selected_language);
    }

    public void applyClickListeners() {
        button_select_theme.setOnClickListener(this);
        button_select_language.setOnClickListener(this);
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
            default:
                break;

        }
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


}
