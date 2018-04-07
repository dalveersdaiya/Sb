package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.ajm.sb.R;

public class Profile extends BaseActivity {

    Button buttonOpenSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_profile);
        viewByIds();
        applyClickListeners();
        setupToolBar(getResources().getString(R.string.profile), true);

    }

    public void viewByIds(){
        buttonOpenSettings = findViewById(R.id.button_test);
    }

    public void applyClickListeners(){
        buttonOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Settings.class);
                startActivity(intent);
            }
        });
    }

}
