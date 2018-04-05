package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import in.ajm.sb.R;


public class SplashActivity extends BaseActivity {

    ImageView ivProjectLogo;
    TextView tvAppText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_splash);
        viewById();
        setNavigation();
    }

    public void setNavigation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginRegister.class);
                startActivity(intent);
            }
        }, 1200);
    }

    public void viewById() {
        ivProjectLogo = (ImageView) findViewById(R.id.iv_project_logo);
        tvAppText = (TextView) findViewById(R.id.tv_app_text);
    }
}
