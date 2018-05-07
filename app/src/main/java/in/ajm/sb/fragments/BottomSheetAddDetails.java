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
import android.widget.TextView;

import in.ajm.sb.R;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.interfaces.OnClickSubmitDetails;


/**
 * Created by dsd on 26/03/18.
 */

public class BottomSheetAddDetails extends BottomSheetDialogFragment implements View.OnClickListener {

    OnClickSubmitDetails onClickSubmitDetails;
    EditText etEnrollment;
    EditText etAadhar;
    Button buttonSubmit;
    int userType = 01;
    EditText etSchoolPassphrase;
    EditText etTeacherCode;
    TextView tvHeader;

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

    public BottomSheetAddDetails() {

    }

    @SuppressLint("ValidFragment")
    public BottomSheetAddDetails(OnClickSubmitDetails onClickSubmitDetails, int userType) {
        this.onClickSubmitDetails = onClickSubmitDetails;
        this.userType = userType;

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_add_details, null);
        viewById(contentView);
        applyClickListeners();
        dialog.setContentView(contentView);
        setDialogBorder(dialog);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        setUi();
    }

    private void setUi() {
        if (userType == AppConfigs.PARENT_TYPE) {
            tvHeader.setText(getResources().getString(R.string.enter_enrollment));
            etTeacherCode.setVisibility(View.GONE);
            etSchoolPassphrase.setVisibility(View.GONE);
        } else if (userType == AppConfigs.TEACHER_TYPE) {
            tvHeader.setText(getResources().getString(R.string.enter_details));
            etSchoolPassphrase.setVisibility(View.GONE);
            etEnrollment.setVisibility(View.GONE);
        } else if (userType == AppConfigs.STUDENT_TYPE) {
            tvHeader.setText(getResources().getString(R.string.enter_enrollment));
            etTeacherCode.setVisibility(View.GONE);
            etSchoolPassphrase.setVisibility(View.GONE);
        } else if (userType == AppConfigs.SCHOOL_ADMIN_TYPE) {
            etEnrollment.setVisibility(View.GONE);
            etTeacherCode.setVisibility(View.GONE);
            tvHeader.setText(getResources().getString(R.string.enter_details));
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
        buttonSubmit = (Button) contentView.findViewById(R.id.button_submit);
        etEnrollment = (EditText) contentView.findViewById(R.id.et_enrollment);
        etAadhar = (EditText) contentView.findViewById(R.id.et_aadhar);
        etSchoolPassphrase = (EditText) contentView.findViewById(R.id.et_school_passphrase);
        etTeacherCode = (EditText) contentView.findViewById(R.id.et_teacher_code);
        tvHeader = (TextView) contentView.findViewById(R.id.tv_header);

    }

    public void applyClickListeners() {
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                dismiss();
                onClickSubmit(userType);
                break;

            default:
                break;
        }
    }

    public void onClickSubmit(int userType) {
        if (onClickSubmitDetails != null) {
            if (userType == AppConfigs.PARENT_TYPE) {
                String enrollmentNumber = etEnrollment.getText().toString();
                String aadharNumber = etAadhar.getText().toString();
                onClickSubmitDetails.onClickSubmitStudentParent(enrollmentNumber, aadharNumber, AppConfigs.PARENT_TYPE);
            } else if (userType == AppConfigs.TEACHER_TYPE) {
                String teacherPin = etTeacherCode.getText().toString();
                String aadharNumber = etAadhar.getText().toString();
                onClickSubmitDetails.onClickSubmitTeacher(teacherPin, aadharNumber);
            } else if (userType == AppConfigs.STUDENT_TYPE) {
                String enrollmentNumber = etEnrollment.getText().toString();
                String aadharNumber = etAadhar.getText().toString();
                onClickSubmitDetails.onClickSubmitStudentParent(enrollmentNumber, aadharNumber, AppConfigs.PARENT_TYPE);
            } else if (userType == AppConfigs.SCHOOL_ADMIN_TYPE) {
                String shcoolPin = etSchoolPassphrase.getText().toString();
                String aadharNumber = etAadhar.getText().toString();
                onClickSubmitDetails.onClickSubmitSchoolAdmin(shcoolPin, aadharNumber);
            }
        }
    }


}