package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import in.ajm.sb.R;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.StringHelper;


public class SplashActivity extends BaseActivity {

    ImageView ivProjectLogo;
    TextView tvAppText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_splash);
        viewById();
        setNavigationScheme();
    }

    public void viewById() {
        ivProjectLogo = (ImageView) findViewById(R.id.iv_project_logo);
        tvAppText = (TextView) findViewById(R.id.tv_app_text);
    }

    public boolean checkIfUserAdded() {
        if (!StringHelper.isEmpty(getUserId(this))) {
            return true;
        } else {
            return false;
        }
    }

    public void setNavigationScheme() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkIfUserAdded()) {
                    moveToHome();
                } else {
                    moveToLoginResgister();
                }
            }
        }, 1500);
    }

    public void moveToHome() {
        Intent intent = new Intent(SplashActivity.this, HomeTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppConfigs.USER_TYPE, 01);
        startActivity(intent);
    }

    public void moveToLoginResgister() {
        Intent intent = new Intent(SplashActivity.this, LoginRegister.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
