package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.ajm.sb.R;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.data.User;
import in.ajm.sb.fragments.BottomSheetAddDetails;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.interfaces.OnClickSubmitDetails;

public class FindSchool extends BaseActivity implements OnClickSubmitDetails {

    EditText etCode;
    Button buttonSearch;
    int userType = 01;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_find_school);
        viewById();
        applyClickListeners();
        getIntentValues();
        setUi();
    }

    private void setUi() {
        if(userType == AppConfigs.PARENT_TYPE){
            setupToolBar(getResources().getString(R.string.find_school), true);
        }else if(userType == AppConfigs.TEACHER_TYPE){
            setupToolBar(getResources().getString(R.string.find_school), true);
        }else if(userType == AppConfigs.STUDENT_TYPE){
            setupToolBar(getResources().getString(R.string.find_school), true);
        }else if(userType == AppConfigs.SCHOOL_ADMIN_TYPE){
            setupToolBar(getResources().getString(R.string.add_school), true);
            buttonSearch.setText(getResources().getString(R.string.add_school));
        }

    }

    public void viewById() {
        etCode = (EditText) findViewById(R.id.et_enter_code);
        buttonSearch = (Button) findViewById(R.id.button_search);
    }

    public void getIntentValues() {
        userType = getIntent().getExtras().getInt(AppConfigs.USER_TYPE, 01);
        user = ((SchoolBook)getApplication()).getUser();
    }

    public void applyClickListeners() {
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userType == AppConfigs.SCHOOL_ADMIN_TYPE){

                }else{
                    openOtpBottomSheet(userType);
                }
            }
        });
    }

    public void openOtpBottomSheet(int userType) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetAddDetails(this, userType);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }


    public void openSelectOption() {
        Intent intent = new Intent(FindSchool.this, SelectOption.class);
        intent.putExtra(AppConfigs.OPTION_TYPE, AppConfigs.REQUEST_CODE_SELECT_CLASS);
        intent.putExtra(AppConfigs.USER_TYPE, userType);
        startActivity(intent);
    }



    @Override
    public void onClickSubmitStudentParent(String enrollmentNumber, String aadharNumber, int userType) {
        openSelectOption();
    }

    @Override
    public void onClickSubmitTeacher(String teacherCode, String aadharNumber) {
        showAlertBox(context, getResources().getString(R.string.under_construction));
    }

    @Override
    public void onClickSubmitSchoolAdmin(String schoolPin, String aadharNumber) {
        showAlertBox(context, getResources().getString(R.string.under_construction));
    }
}
