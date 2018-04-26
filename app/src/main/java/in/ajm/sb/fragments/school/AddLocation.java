package in.ajm.sb.fragments.school;

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
    EditText et_address;
    EditText et_city;
    EditText et_state;
    EditText et_country;
    EditText et_zip_code;
    Button button_submit;
    LocationObject locationObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_location, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewById(view);
        locationObject = ((SchoolBook) getActivity().getApplication()).getLocationObject();
        setUi();

        applyClickListeners();
    }

    private void applyClickListeners() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForEmptyFields();
            }
        });
    }

    private void viewById(View convertView) {
        et_address = convertView.findViewById(R.id.et_address);
        et_city = convertView.findViewById(R.id.et_city);
        et_state = convertView.findViewById(R.id.et_state);
        et_country = convertView.findViewById(R.id.et_country);
        et_zip_code = convertView.findViewById(R.id.et_zip_code);
        button_submit = convertView.findViewById(R.id.button_submit);
    }

    private void setUi() {
        if (locationObject != null) {
            et_address.setText(locationObject.getAddress());
            et_city.setText(locationObject.getCity());
            et_country.setText(locationObject.getCountry());
            et_state.setText(locationObject.getState());
            et_zip_code.setText(locationObject.getZip());
        }
    }

    private void setLocationObject() {
        locationObject.address = et_address.getText().toString();
        locationObject.city = et_city.getText().toString();
        locationObject.state = et_state.getText().toString();
        locationObject.country = et_country.getText().toString();
        locationObject.zip = et_zip_code.getText().toString();
        ((SchoolBook) getActivity().getApplication()).setLocationObject(locationObject);
    }

    private void checkForEmptyFields() {
        if (StringHelper.isEmpty(et_address.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.address)), et_address);
            return;
        } else if (StringHelper.isEmpty(et_city.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.city)), et_city);
            return;
        } else if (StringHelper.isEmpty(et_state.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.state)), et_state);
            return;
        } else if (StringHelper.isEmpty(et_country.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.country)), et_country);
            return;
        } else if (StringHelper.isEmpty(et_zip_code.getText().toString())) {
            showAlertBox(getString(R.string.empty_alert, getString(R.string.zip_code)), et_zip_code);
            return;
        } else {
            setLocationObject();
            ((SchoolDetails) getActivity()).changeFragment(this, new AddSchoolAdminInfo(), FragmentTransactionExtended.TABLE_VERTICAL);
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
        ((BaseActivity)getActivity()).setTypeFaceForDialog(dialog);
    }
}
