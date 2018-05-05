package in.ajm.sb.fragments.school;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.activities.student_parent.HomeTestActivity;
import in.ajm.sb.api.callback.APICallback;
import in.ajm.sb.api.caller.SchoolAdminSignUpCaller;
import in.ajm.sb.api.enums.ApiType;
import in.ajm.sb.api.params.ApiParams;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.data.User;
import in.ajm.sb.helper.AppConfigs;

public class CreateSchoolAdminAccount extends Fragment implements View.OnClickListener, APICallback {
    TextView tvAccountInfo;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    RadioGroup radioGroupRole;
    RadioButton radioButtonDirector;
    RadioButton radioButtonManager;
    RadioButton radioButtonPrincipal;
    Button buttonSubmit;
    User user;
    String role = "Principal";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_school_admin_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        applyClickListeners();
        getIntentValues();
        setUi();
        setCheckChangeListeners();
    }

    public void getIntentValues() {
        user = ((SchoolBook) getActivity().getApplication()).getUser();
    }

    public void findViewByIds(View view) {
        buttonSubmit = view.findViewById(R.id.button_submit);
        etEmail = view.findViewById(R.id.et_email);
        tvAccountInfo = view.findViewById(R.id.tv_account_info);
        radioButtonDirector = view.findViewById(R.id.radio_button_director);
        radioButtonManager = view.findViewById(R.id.radio_button_manager);
        radioButtonPrincipal = view.findViewById(R.id.radio_button_principal);
        radioGroupRole = view.findViewById(R.id.radio_group_role);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
    }

    public void applyClickListeners() {
        buttonSubmit.setOnClickListener(this);
    }

    public void setUi() {
        etEmail.setText(user.getEmail());
        if (user.getRoles().equalsIgnoreCase("Principal")) {
            radioButtonPrincipal.setChecked(true);
        } else if (user.getRoles().equalsIgnoreCase("Manager")) {
            radioButtonManager.setChecked(true);
        } else if (user.getRoles().equalsIgnoreCase("Director")) {
            radioButtonDirector.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                ((BaseActivity) getActivity()).hideKeyboard();
                checkForEmptyForm();
                break;

        }
    }

    private void setCheckChangeListeners() {

        radioGroupRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_button_director) {
                    role = "Director";
                    radioButtonDirector.setChecked(true);
                } else if (checkedId == R.id.radio_button_manager) {
                    role = "Manager";
                    radioButtonManager.setChecked(true);
                } else if (checkedId == R.id.radio_button_principal) {
                    role = "Principal";
                    radioButtonPrincipal.setChecked(true);
                }
            }
        });

    }

    public boolean checkForEmptyForm() {
        boolean isValid = false;
        if (etEmail.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.hint_email)));
            etEmail.requestFocus();
            isValid = false;
        } else if (etPassword.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.password_hint)));
            etPassword.requestFocus();
            isValid = false;
        } else if (etConfirmPassword.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.password_hint)));
            etConfirmPassword.requestFocus();
            isValid = false;
        } else if (radioGroupRole.getCheckedRadioButtonId() == -1) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.password_hint)));
            etConfirmPassword.requestFocus();
        } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.password_not_matching));
            isValid = false;
        } else {
            isValid = true;
            ((BaseActivity) getActivity()).hideKeyboard();
            setUserValues();
            callCreateAdminApi();

        }
        return isValid;
    }

    private void setUserValues() {
        user.setEmail(etEmail.getText().toString());
        user.setRoles(role);
        ((SchoolBook) getActivity().getApplication()).setUser(user);
    }



    private void callCreateAdminApi() {
        ((BaseActivity) getActivity()).showLoader();
        HashMap<String, String> loginHashMap = new HashMap<>();
        loginHashMap.put("admin[email]", user.getEmail());
        loginHashMap.put("admin[password]", etPassword.getText().toString());
        loginHashMap.put("admin[fname]", user.getFirstName());
        loginHashMap.put("admin[lname]", user.getLastName());
        loginHashMap.put("admin[gender]", user.getGender());
        loginHashMap.put("admin[dob]", user.getDob());
        loginHashMap.put("admin[is_married]", String.valueOf(user.isIs_married()));
        loginHashMap.put("admin[main_mobile]", user.getMobileumber());
        loginHashMap.put("admin[other_contact]", user.getMobileumber());
        loginHashMap.put("admin[address]", user.getAddress());
        loginHashMap.put("admin[city]", user.getAddress());
        loginHashMap.put("admin[state]", user.getAddress());
        loginHashMap.put("admin[zip]", user.getAddress());
        loginHashMap.put("admin[personal_email]", user.getEmail());
        loginHashMap.put("admin[pan_no]", user.getPanNumber());
        loginHashMap.put("admin[addhar_no]", user.getAadharNumber());
        loginHashMap.put("admin[inistitute]", user.getInstituteName());
        loginHashMap.put("admin[academic_status]", user.getAcademicStatus());
        loginHashMap.put("admin[roles]", user.getRoles());
        loginHashMap.put("admin[permission]", user.getPermission());
        ApiParams apiParams = new ApiParams();
        apiParams.mHashMap = loginHashMap;
        SchoolAdminSignUpCaller.getInstance().post(getActivity(), apiParams, this, ApiType.REGISTER);
    }

    @Override
    public void onResult(String result, ApiType apitype, int resultCode, String responseMessage) {
        ((BaseActivity) getActivity()).hideLoader();
        if (responseMessage.equalsIgnoreCase("Success")) {
            if (apitype.equals(ApiType.REGISTER)) {
                moveToHome();
            }
        }
    }

    public void moveToHome() {
        Intent intent = new Intent(getActivity(), HomeTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppConfigs.USER_TYPE, AppConfigs.STUDENT_TYPE);
        startActivity(intent);
    }

}
