package in.ajm.sb.activities.school;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.data.User;
import in.ajm.sb.fragments.school.AddSchoolAdminInfo;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb_library.fragment_transaction.FragmentTransactionExtended;

public class SchoolDetails extends BaseActivity {

    public AddSchoolAdminInfo addSchoolAdminInfoFragment;
    int userType = 01;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_school_details);
        getIntentValues();
        openFirstFragment();
        setupToolBar(getResources().getString(R.string.add_school), true, false);
    }

    public void getIntentValues() {
        userType = getIntent().getExtras().getInt(AppConfigs.USER_TYPE, 01);
        user = ((SchoolBook)getApplication()).getUser();
    }


    public void openFirstFragment(){
        addSchoolAdminInfoFragment = new AddSchoolAdminInfo();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, addSchoolAdminInfoFragment);
        fragmentTransaction.commit();
    }

    public void changeFragment(Fragment firstFragment, Fragment secondFragment) {
        if (getFragmentManager().getBackStackEntryCount()==0) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(this, fragmentTransaction, firstFragment, secondFragment, R.id.fragment_place);
            fragmentTransactionExtended.addTransition(FragmentTransactionExtended.GLIDE);
            fragmentTransactionExtended.commit();
        }else{
            getFragmentManager().popBackStack();
        }
    }

    public void changeFragment(Fragment firstFragment, Fragment secondFragment, int fragmentAnimationType) {
        if (getFragmentManager().getBackStackEntryCount()==0) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(this, fragmentTransaction, firstFragment, secondFragment, R.id.fragment_place);
            fragmentTransactionExtended.addTransition(fragmentAnimationType);
            fragmentTransactionExtended.commit();
        }else{
            getFragmentManager().popBackStack();
        }
    }

}
