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
import in.ajm.sb.interfaces.OnClickOtp;


/**
 * Created by dsd on 26/03/18.
 */

public class BottomSheetAddOtp extends BottomSheetDialogFragment implements View.OnClickListener {

    OnClickOtp onClickOtp;
    EditText etOtp;
    Button buttonSubmitOtp;

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

    public BottomSheetAddOtp() {

    }

    @SuppressLint("ValidFragment")
    public BottomSheetAddOtp(OnClickOtp onClickOtp) {
        this.onClickOtp = onClickOtp;

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_otp, null);
        viewById(contentView);
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
        etOtp = (EditText) contentView.findViewById(R.id.et_otp);
    }

    public void applyClickListeners() {
        buttonSubmitOtp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                dismiss();
                if (onClickOtp != null) {
                    onClickOtp.onClickOtp();
                }
                break;

            default:
                break;
        }
    }


}