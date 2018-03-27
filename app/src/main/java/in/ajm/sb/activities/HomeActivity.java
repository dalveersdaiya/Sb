package in.ajm.sb.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.ajm.sb.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewByIds();
        applyClickListeners();
    }

    public void viewByIds() {

    }

    public void applyClickListeners() {

    }

}
