package in.ajm.sb.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Stack;

import in.ajm.sb.R;
import in.ajm.sb.broadcastreceivers.NetWorkStateReceiver;
import in.ajm.sb.fragments.HomeFragment;
import in.ajm.sb.fragments.ProfileFragment;
import in.ajm.sb.fragments.SettingsFragment;
import in.ajm.sb.helper.AppConfigs;

public class HomeTestActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetWorkStateReceiver.NetworkStateReceiverListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView textViewTitleHome;
    int userType = 01;
    private NetWorkStateReceiver networkStateReceiver;
    private Stack<Fragment> fragmentstack;
    private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_home_test);
        viewByIds();
        applyClickListeners();
        setUpDrawer();
        setNetworkStateReceiver();
        getIntentValues();
        openNewFragment(new HomeFragment(), true, savedInstanceState);
        setHomePageTitle(getResources().getString(R.string.home_page));
        setUserCredentials();
    }

    public void setHomePageTitle(String title) {
        textViewTitleHome.setText(title);
    }


    public void setUpDrawer() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void getIntentValues() {
        userType = getIntent().getExtras().getInt(AppConfigs.USER_TYPE, AppConfigs.PARENT_TYPE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        setTypeFaceForMenuItems(menu, context);
//        setTypeFaceForMenuItemsForActionSingle(menu, context);
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeTestActivity.this, Settings.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            if (fragment.getClass() != ProfileFragment.class) {
                Bundle bundle = new Bundle();
                bundle.putString("org_id", getSelectedOrgId());
                openNewFragment(new ProfileFragment(), true, bundle);
            }


        } else if (id == R.id.nav_home) {
            if (fragment.getClass() != HomeFragment.class) {
                Bundle bundle = new Bundle();
                bundle.putString("org_id", getSelectedOrgId());
                openNewFragment(new HomeFragment(), true, bundle);
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void viewByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        textViewTitleHome = findViewById(R.id.toolbar_title_home);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setTypeFaceForMenuItems(navigationView.getMenu(), this);
        fragmentstack = new Stack<>();

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

    public void openNewFragment(Fragment f, boolean savedinstack, Bundle bundle) {
        if (savedinstack) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentstack.push(fragment);
            f.setArguments(bundle);
            ft.replace(R.id.frame, f);
            fragment = f;
            ft.commitAllowingStateLoss();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            f.setArguments(bundle);
            ft.replace(R.id.frame, f);
            ft.commitAllowingStateLoss();
        }
    }

    private void back() {
        fragment = fragmentstack.pop();
        if (fragment.getClass() == SettingsFragment.class) {
//            setBottomBarUI("dashboard");
        } else if (fragment.getClass() == ProfileFragment.class) {
//            setBottomBarUI("mycall");//TODO : here
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.frame, fragment);
        ft.commitAllowingStateLoss();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            try {
                if (fragmentstack.size() < 0) {
                    showDialog(context, getResources().getString(R.string.wanna_exit_app), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                } else {
                    back();
                }

            } catch (Exception e) {
                showDialog(context, getResources().getString(R.string.wanna_exit_app), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }

        }
    }

    // TODO: 10/04/18 Rectify this
    public void setUserCredentials() {
        setUserId("UserID" + userType);
    }
}
