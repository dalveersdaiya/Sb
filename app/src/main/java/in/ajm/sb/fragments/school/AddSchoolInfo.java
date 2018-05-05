package in.ajm.sb.fragments.school;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.activities.school.SchoolDetails;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.customviews.CircularImageView;
import in.ajm.sb.data.LocationObject;
import in.ajm.sb.data.User;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.StringHelper;

public class AddSchoolInfo extends Fragment implements View.OnClickListener {

    Button buttonSubmit;
    EditText etSchoolEmail;
    EditText etAddressTwo;
    EditText etAddressOne;
    RadioButton radioButtonEnglish;
    RadioButton radioButtonHindi;
    RadioGroup radioGroupMedium;
    RadioButton radioButtonOtherBoard;
    RadioButton radioButtonRbse;
    RadioButton radioButtonCbse;
    RadioGroup radioGroupBoard;
    EditText etSupervisor;
    RadioButton radioButtonSenSecondary;
    RadioButton radioButtonSecondary;
    RadioButton radioButtonPlaygroup;
    RadioGroup radioGroupAcademicStatus;
    EditText etInstituteName;
    CircularImageView civSchoolImage;
    LinearLayout linearScroll;
    ScrollView scrollView;
    TextView tvSchoolInfo;
    LocationObject locationObject;
    User user;
    String academicStatus = "Playgroup";
    String medium = "English";
    String board = "RBSE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_details, container, false);
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

    private void getIntentValues() {
        user = ((SchoolBook) getActivity().getApplication()).getUser();
    }

    public void findViewByIds(View view) {
        buttonSubmit = view.findViewById(R.id.button_submit);
        etSchoolEmail = view.findViewById(R.id.et_school_email);
        etAddressOne = view.findViewById(R.id.et_address_one);
        etAddressTwo = view.findViewById(R.id.et_address_two);
        radioButtonEnglish = view.findViewById(R.id.radio_button_english);
        radioButtonHindi = view.findViewById(R.id.radio_button_hindi);
        radioGroupMedium = view.findViewById(R.id.radio_group_medium);
        radioButtonOtherBoard = view.findViewById(R.id.radio_button_other_board);
        radioButtonRbse = view.findViewById(R.id.radio_button_rbse);
        radioButtonCbse = view.findViewById(R.id.radio_button_cbse);
        radioGroupBoard = view.findViewById(R.id.radio_group_board);
        etSupervisor = view.findViewById(R.id.et_supervisor);
        radioButtonSenSecondary = view.findViewById(R.id.radio_button_sen_secondary);
        radioButtonSecondary = view.findViewById(R.id.radio_button_secondary);
        radioButtonPlaygroup = view.findViewById(R.id.radio_button_playgroup);
        radioGroupAcademicStatus = view.findViewById(R.id.radio_group_academic_status);
        etInstituteName = view.findViewById(R.id.et_institute_name);
        civSchoolImage = view.findViewById(R.id.civ_school_image);
        linearScroll = view.findViewById(R.id.linear_scroll);
        scrollView = view.findViewById(R.id.scroll_view);
        tvSchoolInfo = view.findViewById(R.id.tv_school_info);
    }

    private void setUserValues() {
        user.setSchoolEmail(etSchoolEmail.getText().toString());
        user.setAddressOne(etAddressOne.getText().toString());
        user.setAddressTwo(etAddressTwo.getText().toString());
        user.setMedium(medium);
        user.setBoard(board);
        user.setSupervisor(etSupervisor.getText().toString());
        user.setAcademicStatus(academicStatus);
        user.setInstituteName(etInstituteName.getText().toString());
        ((SchoolBook) getActivity().getApplication()).setUser(user);
    }

    public void applyClickListeners() {
        buttonSubmit.setOnClickListener(this);
        civSchoolImage.setOnClickListener(this);
    }

    public void setUi() {
        etSchoolEmail.setText(user.getSchoolEmail());
        if(user.getMedium().equalsIgnoreCase("English")){
            radioButtonEnglish.setChecked(true);
        }else if(user.getMedium().equalsIgnoreCase("Hindi")){
            radioButtonHindi.setChecked(true);
        }

        if(user.getBoard().equalsIgnoreCase("CBSE")){
            radioButtonCbse.setChecked(true);
        }else if (user.getBoard().equalsIgnoreCase("RBSE")){
            radioButtonRbse.setChecked(true);
        }else{
            radioButtonOtherBoard.setChecked(true);
        }

        if(user.getAcademicStatus().equalsIgnoreCase("Playgroup")){
            radioButtonPlaygroup.setChecked(true);
        }else if(user.getAcademicStatus().equalsIgnoreCase("Secondary")){
            radioButtonSecondary.setChecked(true);
        }else if(user.getAcademicStatus().equalsIgnoreCase("SeniorSecondary")){
            radioButtonSenSecondary.setChecked(true);
        }

        etInstituteName.setText(user.getInstituteName());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                checkForEmptyForm();

                break;
//            case R.id.tv_address_one:
//                ((SchoolDetails) getActivity()).changeFragment(this, new AddLocation("AddSchoolInfo", true), FragmentTransactionExtended.TABLE_VERTICAL);
//                initiateLocationObject(tvAddressOne.getText().toString());
//                setUserValues();
//                break;
//            case R.id.tv_address_two:
//                ((SchoolDetails) getActivity()).changeFragment(this, new AddLocation("AddSchoolInfo", false), FragmentTransactionExtended.TABLE_VERTICAL);
//                initiateLocationObject(tvAddressOne.getText().toString());
//                setUserValues();
//                break;
            case R.id.civ_school_image:
                break;
        }
    }

    public void initiateLocationObject(String address) {
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

    public String getAddressData() {
        String address = getString(R.string.address);
        if (locationObject != null) {
            address = locationObject.getAddress() + ", " + locationObject.getCity() + ", " + locationObject.getState() + ", " + locationObject.getCountry() + ", " + locationObject.getZip();
        }
        return address;
    }

    private void setAddressType() {
        locationObject = ((SchoolBook) getActivity().getApplication()).getLocationObject();
        if (locationObject.getIsPrimary().equalsIgnoreCase("1")) {
            etAddressOne.setText(getAddressData());
        } else {
            etAddressTwo.setText(getAddressData());
        }
    }

    public boolean checkForEmptyForm() {
        boolean isValid = false;
        if (etSchoolEmail.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.hint_email)));
            etSchoolEmail.requestFocus();
            isValid = false;
        } else if (etSupervisor.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.supervisor)));
            etSupervisor.requestFocus();
            isValid = false;
        } else if (etInstituteName.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.institute_name)));
            etInstituteName.requestFocus();
            isValid = false;
        } else if (etAddressOne.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.address)));
            etAddressTwo.requestFocus();
            isValid = false;
        } else if (etAddressTwo.getText().toString().isEmpty()) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.empty_alert, getResources().getString(R.string.address)));
            etAddressTwo.requestFocus();
            isValid = false;
        } else if (radioGroupAcademicStatus.getCheckedRadioButtonId() == -1) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.please_select_specified, getResources().getString(R.string.academic_status)));
            isValid = false;
        } else if (radioGroupBoard.getCheckedRadioButtonId() == -1) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.please_select_specified, getResources().getString(R.string.board)));
            isValid = false;
        } else if (radioGroupMedium.getCheckedRadioButtonId() == -1) {
            ((BaseActivity) getActivity()).showAlertBox(getActivity(), getResources().getString(R.string.please_select_specified, getResources().getString(R.string.medium)));
            isValid = false;
        } else {
            ((BaseActivity) getActivity()).hideKeyboard();
            ((SchoolDetails) getActivity()).changeFragment(this, new CreateSchoolAdminAccount());
            setUserValues();
        }
        return isValid;
    }

    private void setCheckChangeListeners() {

        radioGroupAcademicStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_button_playgroup) {
                    academicStatus = "Playgroup";
                    radioButtonPlaygroup.setChecked(true);
                } else if (checkedId == R.id.radio_button_secondary) {
                    academicStatus = "Secondary";
                    radioButtonSecondary.setChecked(true);
                } else if (checkedId == R.id.radio_button_sen_secondary) {
                    academicStatus = "SeniorSecondary";
                    radioButtonSenSecondary.setChecked(true);
                }
            }
        });

        radioGroupBoard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_button_rbse) {
                    board = "RBSE";
                    radioButtonRbse.setChecked(true);
                } else if (checkedId == R.id.radio_button_cbse) {
                    board = "CBSE";
                    radioButtonCbse.setChecked(true);
                } else if (checkedId == R.id.radio_group_board) {
                    board = "Other";
                    radioButtonOtherBoard.setChecked(true);
                }
            }
        });
        radioGroupMedium.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_button_english) {
                    medium = "English";
                    radioButtonEnglish.setChecked(true);
                } else if (checkedId == R.id.radio_button_hindi) {
                    medium = "Hindi";
                    radioButtonHindi.setChecked(true);
                }

            }
        });
    }


}
