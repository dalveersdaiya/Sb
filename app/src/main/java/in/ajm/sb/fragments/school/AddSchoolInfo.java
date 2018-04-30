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
import in.ajm.sb.activities.school.SchoolDetails;
import in.ajm.sb.customviews.CircularImageView;

public class AddSchoolInfo extends Fragment implements View.OnClickListener {
    Button button_submit;
    EditText et_school_email;
    TextView tv_address_two;
    TextView tv_address_one;
    RadioButton radio_button_english;
    RadioButton radio_button_hindi;
    RadioGroup radio_group_medium;
    RadioButton radio_button_other_board;
    RadioButton radio_button_rbse;
    RadioButton radio_button_cbse;
    RadioGroup radio_group_board;
    EditText et_supervisor;
    RadioButton radio_button_sen_secondary;
    RadioButton radio_button_secondary;
    RadioButton radio_button_playgroup;
    RadioGroup radio_group_academic_status;
    EditText et_institute_name;
    CircularImageView civ_school_image;
    LinearLayout linear_scroll;
    ScrollView scroll_view;
    TextView tv_school_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        applyClickListeners();
    }

    public void findViewByIds(View view) {
        button_submit = view.findViewById(R.id.button_submit);
        et_school_email = view.findViewById(R.id.et_school_email);
        tv_address_two = view.findViewById(R.id.tv_address_two);
        tv_address_one = view.findViewById(R.id.tv_address_one);
        radio_button_english = view.findViewById(R.id.radio_button_english);
        radio_button_hindi = view.findViewById(R.id.radio_button_hindi);
        radio_group_medium = view.findViewById(R.id.radio_group_medium);
        radio_button_other_board = view.findViewById(R.id.radio_button_other_board);
        radio_button_rbse = view.findViewById(R.id.radio_button_rbse);
        radio_button_cbse = view.findViewById(R.id.radio_button_cbse);
        radio_group_board = view.findViewById(R.id.radio_group_board);
        et_supervisor = view.findViewById(R.id.et_supervisor);
        radio_button_sen_secondary = view.findViewById(R.id.radio_button_sen_secondary);
        radio_button_secondary = view.findViewById(R.id.radio_button_secondary);
        radio_button_playgroup = view.findViewById(R.id.radio_button_playgroup);
        radio_group_academic_status = view.findViewById(R.id.radio_group_academic_status);
        et_institute_name = view.findViewById(R.id.et_institute_name);
        civ_school_image = view.findViewById(R.id.civ_school_image);
        linear_scroll = view.findViewById(R.id.linear_scroll);
        scroll_view = view.findViewById(R.id.scroll_view);
        tv_school_info = view.findViewById(R.id.tv_school_info);
    }

    public void applyClickListeners() {
        button_submit.setOnClickListener(this);
        tv_address_one.setOnClickListener(this);
        tv_address_two.setOnClickListener(this);
        civ_school_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                ((SchoolDetails) getActivity()).changeFragment(this, new CreateSchoolAdminAccount());
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
