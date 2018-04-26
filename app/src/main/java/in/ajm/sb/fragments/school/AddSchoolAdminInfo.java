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
import in.ajm.sb.activities.school.SchoolDetails;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.customviews.CircularImageView;
import in.ajm.sb.data.LocationObject;
import in.ajm.sb.data.User;
import in.ajm.sb.helper.StringHelper;
import in.ajm.sb_library.fragment_transaction.FragmentTransactionExtended;

public class AddSchoolAdminInfo extends Fragment implements View.OnClickListener {

    Button buttonSubmit;
    CircularImageView civ_user_image;
    EditText et_first_name;
    EditText et_lastName;
    EditText et_mob_number;
    EditText et_email;
    TextView tv_dob;
    RadioGroup radio_group_gender;
    RadioButton radio_button_male;
    RadioButton radio_button_female;
    RadioButton radio_button_other;
    RadioGroup radio_group_marital_status;
    RadioButton radio_button_married;
    RadioButton radio_button_unmarried;
    TextView tv_address;
    EditText et_aadhar;
    EditText et_pan_num;
    User user;
    LocationObject locationObject;

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
    }

    public void findViewById(View view) {
        buttonSubmit = view.findViewById(R.id.button_submit);
        civ_user_image = view.findViewById(R.id.civ_user_image);
        et_first_name = view.findViewById(R.id.et_first_name);
        et_lastName = view.findViewById(R.id.et_lastName);
        et_mob_number = view.findViewById(R.id.et_mob_number);
        et_email = view.findViewById(R.id.et_email);
        tv_dob = view.findViewById(R.id.tv_dob);
        radio_group_gender = view.findViewById(R.id.radio_group_gender);
        radio_button_male = view.findViewById(R.id.radio_button_male);
        radio_button_female = view.findViewById(R.id.radio_button_female);
        radio_button_other = view.findViewById(R.id.radio_button_other);
        radio_group_marital_status = view.findViewById(R.id.radio_group_marital_status);
        radio_button_married = view.findViewById(R.id.radio_button_married);
        radio_button_unmarried = view.findViewById(R.id.radio_button_unmarried);
        tv_address = view.findViewById(R.id.tv_address);
        et_aadhar = view.findViewById(R.id.et_aadhar);
        et_pan_num = view.findViewById(R.id.et_pan_num);
    }

    public void setUi() {
        user = ((SchoolBook) getActivity().getApplication()).getUser();
        et_first_name.setText(user.getFirstName());
        et_lastName.setText(user.getLastName());
        et_mob_number.setText(user.getMobileumber());
        tv_address.setText(getAddressData());

    }

    public void applyClickListeners() {
        buttonSubmit.setOnClickListener(this);
        tv_dob.setOnClickListener(this);
        tv_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                ((SchoolDetails) getActivity()).changeFragment(this, new AddSchoolInfo());
                break;
            case R.id.tv_address:
                ((SchoolDetails) getActivity()).changeFragment(this, new AddLocation(), FragmentTransactionExtended.TABLE_VERTICAL);
                initiateLocationObject(tv_address.getText().toString());
                break;
            case R.id.tv_dob:
                break;
        }
    }

    public void initiateLocationObject(String address) {
        LocationObject locationObject = new LocationObject();
        if (!StringHelper.isEmpty(address) && !address.equalsIgnoreCase(getString(R.string.address))) {
            String[] addressArray = address.split(", ");
            locationObject.address = addressArray[0];
            locationObject.city = addressArray[1];
            locationObject.state = addressArray[2];
            locationObject.country = addressArray[3];
            locationObject.zip = addressArray[4];
        }
        ((SchoolBook) getActivity().getApplication()).setLocationObject(locationObject);
    }

    public String getAddressData() {
        String address = getString(R.string.address);
        locationObject = ((SchoolBook) getActivity().getApplication()).getLocationObject();
        if (locationObject != null) {
            address = locationObject.address + ", " + locationObject.city + ", " + locationObject.state + ", " + locationObject.country + ", " + locationObject.zip;
        }
        return address;
    }
}
