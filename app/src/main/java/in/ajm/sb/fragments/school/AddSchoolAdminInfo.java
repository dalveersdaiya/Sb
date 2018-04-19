package in.ajm.sb.fragments.school;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.ajm.sb.R;
import in.ajm.sb.activities.school.SchoolDetails;

public class AddSchoolAdminInfo extends Fragment implements View.OnClickListener {

    Button buttonSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_admin_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        applyClickListeners();
    }

    public void findViewById(View view){
        buttonSubmit = view.findViewById(R.id.button_submit);
    }

    public void applyClickListeners(){
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_submit:
                ((SchoolDetails)getActivity()).changeFragment(this, new AddSchoolInfo());
                break;

        }
    }
}
