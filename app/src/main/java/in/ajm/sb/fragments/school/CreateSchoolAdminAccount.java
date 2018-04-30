package in.ajm.sb.fragments.school;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.data.User;

public class CreateSchoolAdminAccount extends Fragment implements View.OnClickListener {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                ((BaseActivity)getActivity()).hideKeyboard();
                break;
            case R.id.tv_address_one:
                break;
            case R.id.tv_address_two:
                break;
            case R.id.civ_school_image:
                break;
        }
    }
}
