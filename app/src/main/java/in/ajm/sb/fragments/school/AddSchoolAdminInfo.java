package in.ajm.sb.fragments.school;

import android.app.Fragment;
import android.content.DialogInterface;
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

import java.util.Calendar;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.activities.school.SchoolDetails;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.customviews.CircularImageView;
import in.ajm.sb.data.LocationObject;
import in.ajm.sb.data.User;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.DateTimeHelper;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.StringHelper;
import in.ajm.sb_library.daiyadatetimepicker.date.DatePickerDialog;
import in.ajm.sb_library.daiyadatetimepicker.time.RadialPickerLayout;
import in.ajm.sb_library.daiyadatetimepicker.time.TimePickerDialog;

public class AddSchoolAdminInfo extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    Button buttonSubmit;
    CircularImageView civUserImage;
    EditText etFirstName;
    EditText etLastName;
    EditText etMobNumber;
    EditText etEmail;
    TextView tvDob;
    RadioGroup radioGroupGender;
    RadioButton radioButtonMale;
    RadioButton radioButtonFemale;
    RadioButton radioButtonOther;
    RadioGroup radioGroupMaritalStatus;
    RadioButton radioButtonMarried;
    RadioButton radioButtonUnmarried;
    EditText etAddress;
    EditText etAadhar;
    EditText etPanNum;
    User user;
    LocationObject locationObject;
    boolean isMarried = false;
    String gender = "Male";
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_admin_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        applyClickListeners();
        setUi();
        setCheckChangeListeners();
    }

    private void findViewById(View view) {
        buttonSubmit = view.findViewById(R.id.button_submit);
        civUserImage = view.findViewById(R.id.civ_user_image);
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_lastName);
        etMobNumber = view.findViewById(R.id.et_mob_number);
        etEmail = view.findViewById(R.id.et_email);
        tvDob = view.findViewById(R.id.tv_dob);
        radioGroupGender = view.findViewById(R.id.radio_group_gender);
        radioButtonMale = view.findViewById(R.id.radio_button_male);
        radioButtonFemale = view.findViewById(R.id.radio_button_female);
        radioButtonOther = view.findViewById(R.id.radio_button_other);
        radioGroupMaritalStatus = view.findViewById(R.id.radio_group_marital_status);
        radioButtonMarried = view.findViewById(R.id.radio_button_married);
        radioButtonUnmarried = view.findViewById(R.id.radio_button_unmarried);
        etAddress = view.findViewById(R.id.et_address);
        etAadhar = view.findViewById(R.id.et_aadhar);
        etPanNum = view.findViewById(R.id.et_pan_num);
    }

    private void setUi() {
        user = ((SchoolBook) getActivity().getApplication()).getUser();
        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etMobNumber.setText(user.getMobileumber());
//        etAddress.setText(user.getAddress());
//        tvDob.setText(user.getDob());
//        etEmail.setText(user.getEmail());
        if(user.getGender().equalsIgnoreCase("Male")){
            radioButtonMale.setChecked(true);
        }else if(user.getGender().equalsIgnoreCase("Female")){
            radioButtonFemale.setChecked(true);
        }else{
            radioButtonOther.setChecked(true);
        }
        if(user.isIs_married()){
            radioButtonMarried.setChecked(true);
        }else{
            radioButtonUnmarried.setChecked(true);
        }
        etAadhar.setText(user.getAddhar_no());
        etPanNum.setText(user.getPanNumber());
    }

    private void applyClickListeners() {
        buttonSubmit.setOnClickListener(this);
        tvDob.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                checkForEmptyForm();
                break;
//            case R.id.tv_address:
//                ((BaseActivity) getActivity()).hideKeyboard();
//                ((SchoolDetails) getActivity()).changeFragment(this, new AddLocation("AddSchoolAdminInfo", true), FragmentTransactionExtended.TABLE_VERTICAL);
////                initiateLocationObject(tvAddress.getText().toString());
//                setUserValues();
//                break;
            case R.id.tv_dob:
                ((BaseActivity) getActivity()).hideKeyboard();
                setMyDateTime();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (tpd != null) tpd.setOnTimeSetListener(this);
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    private void initiateLocationObject(String address) {
        LocationObject locationObject = new LocationObject();
        if (!StringHelper.isEmpty(address) && !address.equalsIgnoreCase(getString(R.string.address))) {
            try {
                String[] addressArray = address.split(", ");
                locationObject.setAddress(addressArray[0]);
                locationObject.setCity(addressArray[1]);
                locationObject.setState(addressArray[2]);
                locationObject.setCountry(addressArray[3]);
                locationObject.setZip(addressArray[4]);
            } catch (Exception e) {
                LoggerCustom.printStackTrace(e);
                locationObject.setAddress("");
                locationObject.setCity("");
                locationObject.setState("");
                locationObject.setCountry("");
                locationObject.setZip("");
            }
        }
        ((SchoolBook) getActivity().getApplication()).setLocationObject(locationObject);
    }

    private String getAddressData() {
        String address = getString(R.string.address);
        locationObject = ((SchoolBook) getActivity().getApplication()).getLocationObject();
        if (locationObject != null) {
            address = locationObject.getAddress() + ", " + locationObject.getCity() + ", " + locationObject.getState() + ", " + locationObject.getCountry() + ", " + locationObject.getZip();
        }
        return address;
    }

    private void setCheckChangeListeners() {

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_button_male) {
                    gender = "Male";
                    radioButtonMale.setChecked(true);
                } else if (checkedId == R.id.radio_button_female) {
                    gender = "Male";
                    radioButtonFemale.setChecked(true);
                } else if (checkedId == R.id.radio_button_other) {
                    gender = "Other";
                    radioButtonOther.setChecked(true);
                }
            }
        });

        radioGroupMaritalStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_button_married) {
                    isMarried = true;
                    radioButtonMale.setChecked(true);
                } else if (checkedId == R.id.radio_button_female) {
                    isMarried = false;
                    radioButtonFemale.setChecked(true);
                }
            }
        });
    }

    private void setUserValues() {
        user.setEmail(etEmail.getText().toString());
        user.setFirstName(etFirstName.getText().toString());
        user.setLastName(etLastName.getText().toString());
        user.setMobileumber(etMobNumber.getText().toString());
        user.setDob(tvDob.getText().toString());
        user.setGender(gender);
        user.setIs_married(isMarried);
        user.setAddress(etAddress.getText().toString());
        user.setAadharNumber(etAadhar.getText().toString());
        user.setPanNumber(etPanNum.getText().toString());
        ((SchoolBook) getActivity().getApplication()).setUser(user);
    }

    public boolean checkForEmptyForm() {
        boolean isValid = false;
        if (etEmail.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.hint_email)));
            etEmail.requestFocus();
            isValid = false;
        } else if (etFirstName.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.firstName)));
            etFirstName.requestFocus();
            isValid = false;
        } else if (etLastName.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.firstName)));
            etLastName.requestFocus();
            isValid = false;
        } else if (etMobNumber.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.mob_num)));
            etMobNumber.requestFocus();
            isValid = false;
        } else if (tvDob.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showDialog(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.dob)), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tvDob.performClick();
                }
            });
            tvDob.performClick();
            isValid = false;
        } else if (etAddress.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.address)));
            etAddress.requestFocus();
            isValid = false;
        } else if (etAadhar.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.aadhar_num)));
            etAadhar.requestFocus();
            isValid = false;
        } else if (etPanNum.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.pan_card_num)));
            etPanNum.requestFocus();
            isValid = false;
        } else if (radioGroupMaritalStatus.getCheckedRadioButtonId() == -1) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.please_select_specified, getResources().getString(R.string.marital_status)));
            isValid = false;
        } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.please_select_specified, getResources().getString(R.string.gender)));
            isValid = false;
        } else {
            isValid = true;
            ((BaseActivity) getActivity()).hideKeyboard();
            ((SchoolDetails) getActivity()).changeFragment(this, new AddSchoolInfo());
            setUserValues();

        }
        return isValid;
    }

    private void setMyDateTime() {
        Calendar now = Calendar.getInstance();
        final DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(false);
        dpd.vibrate(true);
        dpd.dismissOnPause(true);
        dpd.setTitle(getResources().getString(R.string.please_select_specified, getResources().getString(R.string.time)));
        dpd.showYearPickerFirst(false);
        dpd.setAccentColor(((BaseActivity) getActivity()).getAccentColor(getActivity()));
        dpd.setCancelText(getString(R.string.cancel));
        dpd.setMaxDate(now);
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void setMyTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.vibrate(true);
        tpd.setTimeInterval(1, 30);
        tpd.dismissOnPause(true);
        tpd.enableSeconds(false);
        tpd.enableMinutes(true);
        tpd.setCancelText("");
        tpd.setResetText("Cancel");
        tpd.setTitle(getResources().getString(R.string.please_select_specified, getResources().getString(R.string.time)));
        tpd.setAccentColor(((BaseActivity) getActivity()).getAccentColor(getActivity()));
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        startDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        startDateCalendar.set(Calendar.MINUTE, minute);
        startDateCalendar.set(Calendar.SECOND, second);

        endDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        endDateCalendar.set(Calendar.MINUTE, minute);
        endDateCalendar.set(Calendar.SECOND, second);
        endDateCalendar.add(Calendar.MINUTE, 30);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        startDateCalendar.set(Calendar.YEAR, year);
        startDateCalendar.set(Calendar.MONTH, (monthOfYear));
        startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        endDateCalendar.set(Calendar.YEAR, year);
        endDateCalendar.set(Calendar.MONTH, (monthOfYear));
        endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tvDob.setText(getDateFormat(startDateCalendar));
//        setMyTime();
    }

    private String getDateFormat(Calendar selectedCalendarInstance) {
        String date = DateTimeHelper.formatCalendar(selectedCalendarInstance, AppConfigs.DATE_FORMAT);
        return date;
    }


}
