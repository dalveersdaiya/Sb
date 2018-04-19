package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.activities.school.SchoolDetails;
import in.ajm.sb.adapter.UserTypeAdapter;
import in.ajm.sb.application.SchoolBook;
import in.ajm.sb.data.User;
import in.ajm.sb.data.UserTypeData;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.interfaces.OnUserTypeSelected;

public class SelectUserType extends BaseActivity implements OnUserTypeSelected {

    RecyclerView recyclerViewType;
    List<UserTypeData> userTypeDataList = new ArrayList<>();
    UserTypeAdapter userTypeAdapter;
//    String[] userTypeArray;

    String[] userTypeArray = new String[]{};
    User user;
//    String[] userTypeArray = new String[]{getResources().getString(R.string.parent), getResources().getString(R.string.teacher),
//            getResources().getString(R.string.student), getResources().getString(R.string.school_admin)};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_select_user_type);
        getIntentValues();
        viewByIds();
        setupToolBar(getResources().getString(R.string.select_user_type), true);
        userTypeArray = new String[]{getResources().getString(R.string.parent), getResources().getString(R.string.teacher),
                getResources().getString(R.string.student), getResources().getString(R.string.school_admin)};
        setRecyclerViewType();
    }


    public void setRecyclerViewType() {
        recyclerViewType.setHasFixedSize(true);
        for (int i = 0; i < userTypeArray.length; i++) {
            UserTypeData userTypeData = new UserTypeData();
            userTypeData.setUserName(userTypeArray[i]);
            userTypeDataList.add(userTypeData);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewType.setLayoutManager(linearLayoutManager);
        userTypeAdapter = new UserTypeAdapter(this, userTypeDataList, this);
        recyclerViewType.setAdapter(userTypeAdapter);
    }

    public void viewByIds() {
        recyclerViewType = (RecyclerView) findViewById(R.id.recycler_view);

    }


    @Override
    public void userTypeSelected(int pos, String type) {

        if(pos == 3){
            openSchoolDetails();
        }else{
            openFindSchool(pos);
        }
    }

    public void getIntentValues(){
        user = ((SchoolBook)getApplication()).getUser();
    }

    public void setUserData(int userType){
        user.setUserType(userType);
        ((SchoolBook)getApplication()).setUser(user);
    }

    public void openSchoolDetails() {
        Intent intent = new Intent(this, SchoolDetails.class);
        intent.putExtra(AppConfigs.USER_TYPE, AppConfigs.SCHOOL_ADMIN_TYPE);
        startActivity(intent);
    }

    public void openFindSchool(int pos) {
        setUserData(pos);
        Intent intent = new Intent(SelectUserType.this, FindSchool.class);
        intent.putExtra(AppConfigs.USER_TYPE, pos + 1);
        startActivity(intent);
    }
}
