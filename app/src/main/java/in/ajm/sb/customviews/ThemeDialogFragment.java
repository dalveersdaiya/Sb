package in.ajm.sb.customviews;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import in.ajm.sb.R;
import in.ajm.sb.helper.DisplayHelper;
import in.ajm.sb.helper.PreferencesManager;

public class ThemeDialogFragment extends DialogFragment {
    Context context;
    private ThemeDialogFragment mDialogFragment;
    private OnButtonsClickListener mOnButtonsClickListener;

    @SuppressLint("ValidFragment")
    public ThemeDialogFragment(Context context) {
        mDialogFragment = this;
        this.context = context;
    }

    public ThemeDialogFragment() {
        mDialogFragment = this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.theme_layout);


        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);

        final RadioButton radio_btn_default = (RadioButton) dialog.findViewById(R.id.radio_btn_default);
        final RadioButton radio_btn_blue = (RadioButton) dialog.findViewById(R.id.radio_btn_blue);
        final RadioButton radio_btn_red = (RadioButton) dialog.findViewById(R.id.radio_btn_red);
        final RadioButton radio_btn_yellow = (RadioButton) dialog.findViewById(R.id.radio_btn_yellow);
        final RadioButton radio_btn_teal = (RadioButton) dialog.findViewById(R.id.radio_btn_teal);
        final RadioButton radio_btn_green = (RadioButton) dialog.findViewById(R.id.radio_btn_green);
        final RadioButton radio_btn_purple = (RadioButton) dialog.findViewById(R.id.radio_btn_purple);
        final RadioButton radio_btn_pink = (RadioButton) dialog.findViewById(R.id.radio_btn_pink);
        final RadioButton radio_btn_orange = (RadioButton) dialog.findViewById(R.id.radio_btn_orange);
        final RadioButton radio_btn_cyan = (RadioButton) dialog.findViewById(R.id.radio_btn_cyan);
        final RadioButton radio_btn_brown = (RadioButton) dialog.findViewById(R.id.radio_btn_brown);
        final RadioButton radio_btn_white = (RadioButton) dialog.findViewById(R.id.radio_btn_white);
        final RadioButton radio_btn_black = (RadioButton) dialog.findViewById(R.id.radio_btn_black);

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

        if (dialog.getWindow() != null) {
            int[] screenSize = DisplayHelper.getDisplaySize(getContext());
            int maxWidth = DisplayHelper.convertDpToPx(getContext(), 380);
            int width = (int) ((double) ((double) screenSize[0] * 80)) / 100;
            if (width > maxWidth)
                width = maxWidth;
            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button buttonOk = dialog.findViewById(R.id.btn_p);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedmemberId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedbutton = (RadioButton) dialog.findViewById(selectedmemberId);

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
//                    recreate();
                    if (mOnButtonsClickListener == null)
                        dialog.dismiss();
                    else
                        mOnButtonsClickListener.onOk(mDialogFragment);
                }
                dialog.dismiss();
            }
        });

        Button buttonCancel = dialog.findViewById(R.id.btn_n);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mOnButtonsClickListener == null)
                    dialog.dismiss();
                else
                    mOnButtonsClickListener.onCancel(mDialogFragment);
            }
        });

        dialog.show();
        return dialog;
    }

    public void setOnButtonsClickListener(OnButtonsClickListener onButtonsClickListener) {
        this.mOnButtonsClickListener = onButtonsClickListener;
    }

    public interface OnButtonsClickListener {
        void onCancel(DialogFragment dialogFragment);

        void onOk(DialogFragment dialogFragment);
    }
}
