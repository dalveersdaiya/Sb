package in.ajm.sb.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.adapter.SwitchUserAdapter;
import in.ajm.sb.data.User;
import in.ajm.sb.helper.PreferencesManager;
import in.ajm.sb.interfaces.OnUserSwitched;

public class Settings extends BaseActivity implements View.OnClickListener, OnUserSwitched {
    Button button_select_theme;
    Button button_select_language;
    Button button_switch_user;
    TextView tv_selected_theme;
    TextView tv_current_user;
    TextView tv_selected_language;
    Context context;
    AlertDialog alert11;
    List<User> userList = new ArrayList<>();
    User user;
    boolean isEdited = false;
    int selectedPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_settings);
        viewByIds();
        applyClickListeners();
        setupToolBar(getResources().getString(R.string.settings), true);
        setlanguagetext(tv_selected_language);
        setSelectedTheme(tv_selected_theme);
    }

    public void viewByIds() {
        context = this;
        button_select_language = findViewById(R.id.button_select_language);
        button_select_theme = findViewById(R.id.button_select_theme);
        button_switch_user = findViewById(R.id.button_switch_user);
        tv_current_user = findViewById(R.id.tv_current_user);
        tv_selected_theme = findViewById(R.id.tv_selected_theme);
        tv_selected_language = findViewById(R.id.tv_selected_language);
    }

    public void applyClickListeners() {
        button_select_theme.setOnClickListener(this);
        button_select_language.setOnClickListener(this);
        button_switch_user.setOnClickListener(this);
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
                setSwitchUserDialog(context, isEdited);
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

    public void setSwitchUserDialog(Context context, boolean isEdited) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(getResources().getString(R.string.switch_user));
        builder1.setCancelable(true);
        final View view = LayoutInflater.from(context).inflate(R.layout.switch_user, null);
        Button buttonAddUser = view.findViewById(R.id.button_add_user);
        Button buttonCancel = view.findViewById(R.id.button_cancel);
        Button buttonOk = view.findViewById(R.id.button_ok);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_selected_user);
        userList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            user = new User();
            user.setUserName(getResources().getString(R.string.current_user) + " " + i);
            user.setUserId(i + "");
            if (isEdited) {
                if (i == selectedPosition) {
                    user.setSelected(true);
                } else {
                    user.setSelected(false);
                }
            } else {
                if (i == 0) {
                    user.setSelected(true);
                } else {
                    user.setSelected(false);
                }
            }
            userList.add(user);
        }

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        SwitchUserAdapter switchUserAdapter = new SwitchUserAdapter(context, userList, this);
        recyclerView.setAdapter(switchUserAdapter);

        builder1.setView(view);
        alert11 = builder1.create();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
            }
        });
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
            }
        });


        alert11.show();
    }


    @Override
    public void onUserSwitched(int pos, String id) {
        isEdited = true;
        selectedPosition = pos;
    }
}
