package in.ajm.sb.fragments.school;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.activities.school.SchoolDetails;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.data.LocationObject;
import in.ajm.sb.helper.StringHelper;
import in.ajm.sb_library.fragment_transaction.FragmentTransactionExtended;

public class AddLocation extends Fragment {

    EditText etAddress;
    EditText etCity;
    EditText etState;
    EditText etCountry;
    EditText etZipCode;
    Button buttonSubmit;
    LocationObject locationObject;
    String origin = "";
    boolean isPrimary = false;

    public AddLocation() {

    }

    @SuppressLint("ValidFragment")
    public AddLocation(String origin, boolean isPrimary) {
        this.origin = origin;
        this.isPrimary = isPrimary;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_location, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewById(view);
        locationObject = ((SchoolBook) getActivity().getApplication()).getLocationObject();
        applyClickListeners();
        setUi();
    }

    private void applyClickListeners() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).hideKeyboard();
                checkForEmptyFields();
            }
        });
    }

    private void viewById(View convertView) {
        etAddress = convertView.findViewById(R.id.et_address);
        etCity = convertView.findViewById(R.id.et_city);
        etState = convertView.findViewById(R.id.et_state);
        etCountry = convertView.findViewById(R.id.et_country);
        etZipCode = convertView.findViewById(R.id.et_zip_code);
        buttonSubmit = convertView.findViewById(R.id.button_submit);
    }

    private void setUi() {
        if (locationObject != null) {
            etAddress.setText(locationObject.getAddress());
            etCity.setText(locationObject.getCity());
            etCountry.setText(locationObject.getCountry());
            etState.setText(locationObject.getState());
            etZipCode.setText(locationObject.getZip());
        }
    }

    private void setLocationObject() {
        locationObject.setAddress( etAddress.getText().toString());
        locationObject.setCity(etCity.getText().toString());
        locationObject.setState(etState.getText().toString());
        locationObject.setCountry(etCountry.getText().toString());
        locationObject.setZip(etZipCode.getText().toString());
        if(isPrimary){
            locationObject.setIsPrimary("1");
        }
        ((SchoolBook) getActivity().getApplication()).setLocationObject(locationObject);
    }

    /**
     * Check if any field is empty :
     * Check the origin and return results to the origin i.e. the initial class fragment
     */
    private void checkForEmptyFields() {
        if (StringHelper.isEmpty(etAddress.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.address)), etAddress);
            return;
        } else if (StringHelper.isEmpty(etCity.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.city)), etCity);
            return;
        } else if (StringHelper.isEmpty(etState.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.state)), etState);
            return;
        } else if (StringHelper.isEmpty(etCountry.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.country)), etCountry);
            return;
        } else if (StringHelper.isEmpty(etZipCode.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.zip_code)), etZipCode);
            return;
        } else {
            setLocationObject();
            switch (origin){
                case "AddSchoolAdminInfo":
                    ((SchoolDetails) getActivity()).changeFragment(this, new AddSchoolAdminInfo(), FragmentTransactionExtended.TABLE_VERTICAL);
                    break;
                case "AddSchoolInfo":
                    ((SchoolDetails) getActivity()).changeFragment(this, new AddSchoolInfo(), FragmentTransactionExtended.TABLE_VERTICAL);
                    break;
            }

        }
    }

    public void showAlertBox(String message, final EditText editText) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(message);
        builder1.setCancelable(true);
        final AlertDialog dialog = builder1.create();
        builder1.setPositiveButton(
                getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        editText.requestFocus();
                    }
                });


        dialog.show();
        ((BaseActivity) getActivity()).setTypeFaceForDialog(dialog);
    }
}
