package in.ajm.sb.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import in.ajm.sb.R;
import in.ajm.sb.fragments.BottomSheetAddOtp;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.interfaces.OnClickOtp;

public class LoginRegister extends BaseActivity implements OnClickOtp {

    EditText etFirstName;
    EditText etLastname;
    EditText etMobileNumber;
    Button buttonSubmit;
    RelativeLayout root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.login_register_activity);
        viewByIds();
        applyClickListeners();
        setFocuslistener();
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

    }

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

    public void viewByIds() {
        etFirstName = (EditText) findViewById(R.id.et_first_name);
        etLastname = (EditText) findViewById(R.id.et_lastName);
        etMobileNumber = (EditText) findViewById(R.id.et_mob_number);
        buttonSubmit = (Button) findViewById(R.id.button_submit);
        root = findViewById(R.id.root);

    }

    public void setFocuslistener() {
        etFirstName.setOnFocusChangeListener(focuslistener);
        etLastname.setOnFocusChangeListener(focuslistener);
        etMobileNumber.setOnFocusChangeListener(focuslistener);
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
        Intent intent = new Intent(LoginRegister.this, SelectUserType.class);
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

        } else if (etMobileNumber.getText().toString().isEmpty() || etMobileNumber.getText().toString().length() < 10) {
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

    public void setOtp(String string) {
        LoggerCustom.logD("Daiya", string);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                LoggerCustom.logD("Daiya", message);
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


}
