package in.ajm.sb.fragments.school;

import android.app.Fragment;
import android.os.Bundle;
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
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.StringHelper;
import in.ajm.sb_library.fragment_transaction.FragmentTransactionExtended;

public class AddSchoolInfo extends Fragment implements View.OnClickListener {

    Button buttonSubmit;
    EditText etSchoolEmail;
    TextView tvAddressTwo;
    TextView tvAddressOne;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        applyClickListeners();
        setUi();
    }

    public void findViewByIds(View view) {
        buttonSubmit = view.findViewById(R.id.button_submit);
        etSchoolEmail = view.findViewById(R.id.et_school_email);
        tvAddressTwo = view.findViewById(R.id.tv_address_two);
        tvAddressOne = view.findViewById(R.id.tv_address_one);
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

    public void applyClickListeners() {
        buttonSubmit.setOnClickListener(this);
        tvAddressOne.setOnClickListener(this);
        tvAddressTwo.setOnClickListener(this);
        civSchoolImage.setOnClickListener(this);
    }

    public void setUi(){
        setAddressType();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                ((BaseActivity)getActivity()).hideKeyboard();
                ((SchoolDetails) getActivity()).changeFragment(this, new CreateSchoolAdminAccount());
                break;
            case R.id.tv_address_one:
                ((SchoolDetails) getActivity()).changeFragment(this, new AddLocation("AddSchoolInfo", true), FragmentTransactionExtended.TABLE_VERTICAL);
                initiateLocationObject(tvAddressOne.getText().toString());
                break;
            case R.id.tv_address_two:
                ((SchoolDetails) getActivity()).changeFragment(this, new AddLocation("AddSchoolInfo", false), FragmentTransactionExtended.TABLE_VERTICAL);
                initiateLocationObject(tvAddressOne.getText().toString());
                break;
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
        if(locationObject.getIsPrimary().equalsIgnoreCase("1")){
            tvAddressOne.setText(getAddressData());
        }else{
            tvAddressTwo.setText(getAddressData());
        }
    }
}
