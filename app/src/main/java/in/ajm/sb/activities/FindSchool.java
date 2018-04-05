package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.ajm.sb.R;
import in.ajm.sb.helper.AppConfigs;

public class FindSchool extends BaseActivity {

    EditText etCode;
    Button buttonSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_school);
        viewById();
        applyClicklisteners();
    }

    public void viewById(){
        etCode = (EditText)findViewById(R.id.et_enter_code);
        buttonSearch = (Button)findViewById(R.id.button_search);
    }

    public void applyClicklisteners(){
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindSchool.this, SelectOption.class);
                intent.putExtra("option_type", AppConfigs.REQUEST_CODE_SELECT_CLASS);
                startActivity(intent);
            }
        });
    }

}
