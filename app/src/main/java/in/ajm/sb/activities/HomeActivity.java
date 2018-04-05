package in.ajm.sb.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import in.ajm.sb.R;
import in.ajm.sb.broadcastreceivers.NetWorkStateReceiver;

public class HomeActivity extends BaseActivity implements NetWorkStateReceiver.NetworkStateReceiverListener {


    private NetWorkStateReceiver networkStateReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewByIds();
        applyClickListeners();
        setNetworkStateReceiver();
    }

    public void viewByIds() {

    }

    public void applyClickListeners() {

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
