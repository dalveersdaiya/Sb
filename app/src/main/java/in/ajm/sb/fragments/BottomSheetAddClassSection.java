package in.ajm.sb.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import in.ajm.sb.R;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.interfaces.OnClickAddClassSection;


/**
 * Created by dsd on 26/03/18.
 */

public class BottomSheetAddClassSection extends BottomSheetDialogFragment implements View.OnClickListener {

    OnClickAddClassSection onClickAddClassSection;
    EditText etAdd;
    Button buttonSubmitOtp;
    int type = AppConfigs.ADD_CLASS;
    String className = "";

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public BottomSheetAddClassSection() {

    }

    @SuppressLint("ValidFragment")
    public BottomSheetAddClassSection(OnClickAddClassSection onClickAddClassSection, int type, String className) {
        this.onClickAddClassSection = onClickAddClassSection;
        this.type = type;
        this.className = className;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_add_class_section, null);
        viewById(contentView);
        setUi(type);
        applyClickListeners();
        dialog.setContentView(contentView);
        setDialogBorder(dialog);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

    }


    /**
     * Intended for the Rounded Background of the dialog view.
     *
     * @param dialog
     */
    public void setDialogBorder(Dialog dialog) {
        FrameLayout bottomSheet = (FrameLayout) dialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bottomSheet.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        setMargins(bottomSheet, 10, 0, 10, 20);
    }

    /**
     * Setting the margins for the ViewGroup Programmatically
     *
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    public void viewById(View contentView) {
        buttonSubmitOtp = (Button) contentView.findViewById(R.id.button_submit);
        etAdd = (EditText) contentView.findViewById(R.id.et_add);
    }

    public void applyClickListeners() {
        buttonSubmitOtp.setOnClickListener(this);
    }

    private void setUi(int type){
        if(type == AppConfigs.ADD_CLASS){
            etAdd.setHint(getString(R.string.add_class));
        }else{
            etAdd.setHint(getString(R.string.add_section));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                dismiss();
                if (onClickAddClassSection != null) {
                    if(type == AppConfigs.ADD_CLASS){
                        onClickAddClassSection.onAddClass(etAdd.getText().toString());
                    }else{
                        onClickAddClassSection.onAddSection(etAdd.getText().toString(), className);
                    }
                }
                break;

            default:
                break;
        }
    }


}