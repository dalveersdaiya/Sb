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
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.data.User;

public class CreateSchoolAdminAccount extends Fragment implements View.OnClickListener {
    TextView tv_account_info;
    EditText     et_email;
    EditText et_password;
    EditText       et_confirm_password;
    RadioGroup radio_group_role;
    RadioButton     radio_button_director;
    RadioButton radio_button_manager;
    RadioButton        radio_button_principal;
    Button button_submit;
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

    public void getIntentValues(){
        user = ((SchoolBook)getActivity().getApplication()).getUser();
    }

    public void findViewByIds(View view) {
        button_submit = view.findViewById(R.id.button_submit);
        et_email = view.findViewById(R.id.et_email);
        tv_account_info = view.findViewById(R.id.tv_account_info);
        radio_button_director = view.findViewById(R.id.radio_button_director);
        radio_button_manager = view.findViewById(R.id.radio_button_manager);
        radio_button_principal = view.findViewById(R.id.radio_button_principal);
        radio_group_role = view.findViewById(R.id.radio_group_role);
        et_password = view.findViewById(R.id.et_password);
        et_confirm_password = view.findViewById(R.id.et_confirm_password);
    }

    public void applyClickListeners() {
        button_submit.setOnClickListener(this);
    }

    public void setUi(){
        et_email.setText(user.getEmail());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
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
