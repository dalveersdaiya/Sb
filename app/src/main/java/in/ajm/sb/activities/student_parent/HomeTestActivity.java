package in.ajm.sb.activities.student_parent;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Stack;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.activities.Settings;
import in.ajm.sb.adapter.ViewPagerAdapter;
import in.ajm.sb.broadcastreceivers.NetWorkStateReceiver;
import in.ajm.sb.fragments.parent_student.HomeFragment;
import in.ajm.sb.fragments.parent_student.ProfileFragment;
import in.ajm.sb.fragments.parent_student.SettingsFragment;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.SlideMenuHelper;

public class HomeTestActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetWorkStateReceiver.NetworkStateReceiverListener {

    public int userType = 01;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView textViewTitleHome;
    String currentFragment = "HomeFragment.class";
    CoordinatorLayout appBarMain;
    private NetWorkStateReceiver networkStateReceiver;
    private Stack<Fragment> fragmentStack;
    private Fragment fragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        if (currentFragment.contains("HomeFragment")) {
            openNewFragment(new HomeFragment(), true, savedInstanceState);
        } else {
            openNewFragment(new ProfileFragment(), true, savedInstanceState);
        }
        setHomePageTitle(getResources().getString(R.string.app_name));
        setUserCredentials();
        setUpViewPager();

    }

    public void viewByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        textViewTitleHome = findViewById(R.id.toolbar_title_home);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setTypeFaceForMenuItems(navigationView.getMenu(), this);
        fragmentStack = new Stack<>();
        appBarMain = findViewById(R.id.app_bar_main);

    }

    public void applyClickListeners() {

    }

    public void setUpViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getResources().getString(R.string.home_page));
        adapter.addFragment(new ProfileFragment(), getResources().getString(R.string.profile));
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setTypeFaceForTabLayout(tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setViewPagerPosition(int position){
        viewPager.setCurrentItem(position);
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
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                SlideMenuHelper.setSlideMenu(drawerView, slideOffset, appBarMain);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void getIntentValues() {
        userType = getIntent().getExtras().getInt(AppConfigs.USER_TYPE, AppConfigs.PARENT_TYPE);
        currentFragment = getIntent().getExtras().getString("currentFragment", "HomeFragment.class");
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
            openSettings();
        } else if (id == R.id.nav_profile) {
            if (!currentFragment.contains("ProfileFragment")) {
                Bundle bundle = new Bundle();
                bundle.putString("org_id", getSelectedOrgId());
                openNewFragment(new ProfileFragment(), true, bundle);
            }
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_home) {
            if (!currentFragment.contains("HomeFragment")) {
                Bundle bundle = new Bundle();
                bundle.putString("org_id", getSelectedOrgId());
                openNewFragment(new HomeFragment(), true, bundle);
            }
            viewPager.setCurrentItem(0);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void openSettings() {
        Intent intent = new Intent(HomeTestActivity.this, Settings.class);
        intent.putExtra("currentFragment", currentFragment);
        startActivity(intent);
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
    }

    @Override
    public void networkUnavailable() {
    }

    public void openNewFragment(Fragment f, boolean savedinstack, Bundle bundle) {
        if (savedinstack) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentStack.push(fragment);
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
        currentFragment = f.toString();
    }

    private void back() {
        fragment = fragmentStack.pop();
        if (fragment.getClass() == SettingsFragment.class) {
        } else if (fragment.getClass() == ProfileFragment.class) {
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.frame, fragment);
        ft.commitAllowingStateLoss();
        LoggerCustom.logD(TAG, "back " + fragment.getClass().toString());
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            try {
                if (fragmentStack.size() < 0) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                LoggerCustom.logV(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
//                    downloadPdfFile();
                } else {
//                    progress.dismiss();
                }
                break;

            case 3:
                LoggerCustom.logV(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
//                    SharePdfFile();
                } else {
//                    progress.dismiss();
                }
                break;
        }
    }
}
