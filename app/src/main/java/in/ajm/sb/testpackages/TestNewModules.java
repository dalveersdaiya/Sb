package in.ajm.sb.testpackages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.activities.ChipTestActivity;
import in.ajm.sb.activities.expandabledata.ExpandableClassExample;


public class TestNewModules extends BaseActivity implements View.OnClickListener {

    Button buttonChips;
    Button buttonExpandable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_test_new_modules);
        viewByIds();
        setUi();
        applyClickListeners();
    }

    private void viewByIds() {
        buttonChips = findViewById(R.id.button_test_chips);
        buttonExpandable = findViewById(R.id.button_test_expandable);
    }

    private void applyClickListeners() {
        buttonExpandable.setOnClickListener(this);
        buttonChips.setOnClickListener(this);
    }

    public void setUi() {
        setupToolBar(getResources().getString(R.string.test), true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void openChipTestActivity() {
        Intent intent = new Intent(this, ChipTestActivity.class);
        startActivity(intent);
    }

    public void openExpandableTestActivity() {
        Intent intent = new Intent(this, ExpandableClassExample.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_test_chips:
                openChipTestActivity();
                break;
            case R.id.button_test_expandable:
                openExpandableTestActivity();
                break;
        }
    }
}
