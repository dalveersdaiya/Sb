package in.ajm.sb.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import in.ajm.sb.R;
import in.ajm.sb.broadcastreceivers.NetWorkStateReceiver;

public class HomeActivity extends BaseActivity implements NetWorkStateReceiver.NetworkStateReceiverListener {


    Button buttonOpenSettings;
    private NetWorkStateReceiver networkStateReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_home);
        viewByIds();
        applyClickListeners();
        setNetworkStateReceiver();
        setupToolBar(getResources().getString(R.string.app_name), true);
    }

    public void viewByIds() {
        buttonOpenSettings = findViewById(R.id.button_test);

    }

    public void applyClickListeners() {
        buttonOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        unRegisterNetworkStateReceiver();
    }

    public void setNetworkStateReceiver() {
        networkStateReceiver = new NetWorkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unRegisterNetworkStateReceiver() {
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void networkAvailable() {
        Snackbar.make(findViewById(android.R.id.content), "Internet available", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void networkUnavailable() {
        Snackbar.make(findViewById(android.R.id.content), "Internet not available", Snackbar.LENGTH_SHORT).show();
    }
}
