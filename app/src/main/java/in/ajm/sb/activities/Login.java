package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import in.ajm.sb.R;
import in.ajm.sb.activities.student_parent.HomeTestActivity;
import in.ajm.sb.api.callback.APICallback;
import in.ajm.sb.api.caller.StudentLoginCaller;
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
    }

    public void viewByIds() {
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        root = findViewById(R.id.root);
        tvRegister = findViewById(R.id.tv_register);
        buttonSubmit = (Button) findViewById(R.id.button_submit);
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

        } else {
            isValid = true;
            callLoginApi(etEmail.getText().toString(), etPassword.getText().toString());
        }
        return isValid;
    }


    /**
     * Calling login for student as of now
     */
    private void callLoginApi(String email, String password){
        showLoader();
        HashMap<String, String> loginHashMap = new HashMap<>();
        loginHashMap.put("student[email]",email );
        loginHashMap.put("student[password]",password );
        ApiParams apiParams = new ApiParams();
        apiParams.mHashMap = loginHashMap;
        StudentLoginCaller.getInstance().post(this, apiParams, this, ApiType.LOGIN);

    }

    public void moveToRegister() {
        Intent intent = new Intent(this, Register.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onResult(String result, ApiType apitype, int resultCode, String responseMessage) {
        hideLoader();
        if(responseMessage.equalsIgnoreCase("You are Successfully Sign in.")){
            if(apitype.equals(ApiType.LOGIN)){

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
}
