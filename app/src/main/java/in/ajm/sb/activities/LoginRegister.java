package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.ajm.sb.R;
import in.ajm.sb.fragments.BottomSheetAddOtp;
import in.ajm.sb.interfaces.OnClickOtp;

public class LoginRegister extends BaseActivity implements OnClickOtp {

    EditText etFirstName;
    EditText etLastname;
    EditText etMobileNumber;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);
        viewByIds();
        applyClickListeners();

    }

    public void viewByIds() {
        etFirstName = (EditText) findViewById(R.id.et_first_name);
        etLastname = (EditText) findViewById(R.id.et_lastName);
        etMobileNumber = (EditText) findViewById(R.id.et_mob_number);
        buttonSubmit = (Button) findViewById(R.id.button_submit);

    }

    public void applyClickListeners() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForEmptyForm();
            }
        });
    }

    public void openOtpBottomSheet() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetAddOtp(this);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public void onClickOtp() {
        Intent intent = new Intent(LoginRegister.this, FindSchool.class);
        startActivity(intent);
    }

    public boolean checkForEmptyForm() {
        boolean isValid = false;
        if (etFirstName.getText().toString().isEmpty()) {
            showAlertBox(this, getResources().getString(R.string.first_name_empty));
            etFirstName.requestFocus();
            etLastname.clearFocus();
            etMobileNumber.clearFocus();
            isValid = false;
        } else if (etLastname.getText().toString().isEmpty()) {
            showAlertBox(this, getResources().getString(R.string.last_name_empty));
            etFirstName.clearFocus();
            etLastname.requestFocus();
            etMobileNumber.clearFocus();
            isValid = false;

        } else if (etMobileNumber.getText().toString().isEmpty() || etMobileNumber.getText().toString().length() < 10  ) {
            showAlertBox(this, getResources().getString(R.string.mob_num_empty));
            etFirstName.clearFocus();
            etLastname.clearFocus();
            etMobileNumber.requestFocus();
            isValid = false;
        } else {
            isValid = true;
            openOtpBottomSheet();
        }
        return isValid;
    }
}
