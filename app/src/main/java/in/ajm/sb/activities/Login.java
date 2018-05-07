package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import in.ajm.sb.R;
import in.ajm.sb.activities.student_parent.HomeTestActivity;
import in.ajm.sb.api.callback.APICallback;
import in.ajm.sb.api.caller.SchoolAdminLoginCaller;
import in.ajm.sb.api.caller.StudentLoginCaller;
import in.ajm.sb.api.caller.TeacherLoginCaller;
import in.ajm.sb.api.enums.ApiType;
import in.ajm.sb.api.model.UserCredentials;
import in.ajm.sb.api.params.ApiParams;
import in.ajm.sb.helper.AppConfigs;

public class Login extends BaseActivity implements APICallback {

    TextView tvRegister;
    EditText etPassword;
    EditText etEmail;
    RelativeLayout root;
    Button buttonSubmit;
    RadioGroup radio_group_type;
    RadioButton radio_button_school_admin;
    RadioButton radio_button_teacher;
    RadioButton radio_button_student;
    private int userType= 03;

    View.OnFocusChangeListener focuslistener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                animateOnFocus(v, root);
            } else {
                animateOnFocusLost(v, root);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_login);
        viewByIds();
        applyClickListeners();
        setFocuslistener();
        setEditorAction(etPassword);
        setCheckChangeListeners();
    }

    public void viewByIds() {
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        root = findViewById(R.id.root);
        tvRegister = findViewById(R.id.tv_register);
        buttonSubmit = (Button) findViewById(R.id.button_submit);
        radio_group_type = findViewById(R.id.radio_group_type);
        radio_button_school_admin = findViewById(R.id.radio_button_school_admin);
        radio_button_teacher = findViewById(R.id.radio_button_teacher);
        radio_button_student = findViewById(R.id.radio_button_student);
    }

    public void applyClickListeners() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForEmptyForm();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToRegister();
            }
        });
    }

    public void setFocuslistener() {
        etEmail.setOnFocusChangeListener(focuslistener);
        etPassword.setOnFocusChangeListener(focuslistener);
    }

    public void setEditorAction(EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkForEmptyForm();
                    return true;
                }
                return false;
            }
        });
    }

    public boolean checkForEmptyForm() {
        boolean isValid = false;
        if (etEmail.getText().toString().isEmpty()) {
            showAlertBox(this, getResources().getString(R.string.first_name_empty));
            etEmail.requestFocus();
            etPassword.clearFocus();
            isValid = false;
        } else if (etPassword.getText().toString().isEmpty()) {
            showAlertBox(this, getResources().getString(R.string.last_name_empty));
            etEmail.clearFocus();
            etPassword.requestFocus();
            isValid = false;
        }
        else if (radio_group_type.getCheckedRadioButtonId() == -1) {
            showAlertBox(this, getResources().getString(R.string.please_select_type));
            isValid = false;
        }else {
            isValid = true;
            callLoginApi(etEmail.getText().toString(), etPassword.getText().toString(), userType);
        }
        return isValid;
    }


    public void moveToRegister() {
        Intent intent = new Intent(this, Register.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onResult(String result, ApiType apitype, int resultCode, String responseMessage) {
        hideLoader();
        if (responseMessage.equalsIgnoreCase("You are Successfully Sign in.")) {
            if (apitype.equals(ApiType.LOGIN)) {
                moveToHome();
            }
        }
    }

    public void moveToHome() {
        Intent intent = new Intent(this, HomeTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppConfigs.USER_TYPE, AppConfigs.STUDENT_TYPE);
        setRandomData(AppConfigs.PREFERENCE_USER_ID, String.valueOf(AppConfigs.STUDENT_TYPE));
        startActivity(intent);
    }

    public void setRandomData(String userId, String userType) {
        beginRealmTransaction();
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setSelected(true);
        userCredentials.setUserId(userId);
        userCredentials.setUserImage("file:///storage/emulated/0/Android/data/in.ajm.sb.beta/temp/42A91CDCCFA4455587AC1FC276A186D5.jpg");
        userCredentials.setUserType(userType);
        commitAndCloseRealmTransaction(userCredentials);

    }

    private void setCheckChangeListeners() {
        radio_group_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_button_school_admin) {
                    userType = AppConfigs.SCHOOL_ADMIN_TYPE;
                    radio_button_school_admin.setChecked(true);
                } else if (checkedId == R.id.radio_button_teacher) {
                    userType = AppConfigs.TEACHER_TYPE;
                    radio_button_teacher.setChecked(true);
                } else if (checkedId == R.id.radio_button_student) {
                    userType = AppConfigs.STUDENT_TYPE;
                    radio_button_student.setChecked(true);
                }
            }
        });
    }

    /**
     * Calling login for student as of now
     */
    private void callLoginApi(String email, String password, int userType) {
        showLoader();
        HashMap<String, String> loginHashMap = new HashMap<>();
        if(userType == AppConfigs.SCHOOL_ADMIN_TYPE){
            loginHashMap.put("admin[email]", email);
            loginHashMap.put("admin[password]", password);
        }else if (userType == AppConfigs.TEACHER_TYPE){
            loginHashMap.put("teacher[email]", email);
            loginHashMap.put("teacher[password]", password);
        }else if(userType == AppConfigs.STUDENT_TYPE){
            loginHashMap.put("student[email]", email);
            loginHashMap.put("student[password]", password);
        }
        ApiParams apiParams = new ApiParams();
        apiParams.mHashMap = loginHashMap;
        callLoginAccordingToType(userType, apiParams);
    }

    public void callLoginAccordingToType(int userType, ApiParams apiParams){
        if(userType == AppConfigs.SCHOOL_ADMIN_TYPE){
            SchoolAdminLoginCaller.getInstance().post(this, apiParams, this, ApiType.LOGIN);
        }else if (userType == AppConfigs.TEACHER_TYPE){
            TeacherLoginCaller.getInstance().post(this, apiParams, this, ApiType.LOGIN);
        }else if(userType == AppConfigs.STUDENT_TYPE){
            StudentLoginCaller.getInstance().post(this, apiParams, this, ApiType.LOGIN);
        }
    }
}
