package in.ajm.sb.fragments.parent_student;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.ajm.sb.R;
import in.ajm.sb.activities.student_parent.HomeTestActivity;
import in.ajm.sb.adapter.HomeTodayAdapter;
import in.ajm.sb.data.HomeTodayData;
import in.ajm.sb.fragments.BaseFragment;
import in.ajm.sb.helper.LayoutToImageConverter;
import in.ajm.sb.interfaces.OnThisDayItemClicked;

public class HomeFragment extends BaseFragment implements OnThisDayItemClicked, View.OnClickListener, LayoutToImageConverter.OnLayoutCaptured {

    TextView tvCurrentYear;
    TextView tv_selected_school;
    TextView tv_current_class;
    TextView tv_current_section;
    TextView tv_this_day;
    TextView tv_this_unit_test;
    TextView tv_this_half_year;
    TextView tv_this_year;
    ImageView iv_share_this_day;
    TextView tv_user_name;
    RecyclerView recyclerViewUnitTest;
    RecyclerView recyclerViewHalfYearly;
    RecyclerView recyclerViewYearly;

    Context context;

    List<HomeTodayData> todayDataList;
    HomeTodayAdapter homeTodayAdapter;
    RecyclerView recyclerViewToday;
    LinearLayoutManager linearLayoutManagerToday;
    LinearLayoutManager linearLayoutManagerUnitTest;
    LinearLayoutManager linearLayoutManagerHalfYearly;
    LinearLayoutManager linearLayoutManagerYearly;

    public HomeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        setRetainInstance(true);
        ((HomeTestActivity) getActivity()).setHomePageTitle(getResources().getString(R.string.app_name));
        viewByIds(v);
        setUi();
        setRecyclerViewToday(false);
        setRecyclerViewUnitTest();
        applyClickListeners();
        return v;
    }

    public void viewByIds(View view) {
        context = getActivity();
        tvCurrentYear = view.findViewById(R.id.tv_current_year);
        tv_selected_school = view.findViewById(R.id.tv_selected_school);
        tv_current_class = view.findViewById(R.id.tv_current_class);
        tv_current_section = view.findViewById(R.id.tv_current_section);
        tv_this_day = view.findViewById(R.id.tv_this_day);
        tv_this_unit_test = view.findViewById(R.id.tv_this_unit_test);
        tv_this_half_year = view.findViewById(R.id.tv_this_half_year);
        tv_this_year = view.findViewById(R.id.tv_this_year);
        recyclerViewToday = view.findViewById(R.id.recycler_view_todays);
        recyclerViewHalfYearly = view.findViewById(R.id.recycler_view_half_yearly);
        recyclerViewUnitTest = view.findViewById(R.id.recycler_view_unit_test);
        recyclerViewYearly = view.findViewById(R.id.recycler_view_yearly);
        linearLayoutManagerToday = new LinearLayoutManager(context);
        linearLayoutManagerHalfYearly = new LinearLayoutManager(context);
        linearLayoutManagerUnitTest = new LinearLayoutManager(context);
        linearLayoutManagerYearly = new LinearLayoutManager(context);
        iv_share_this_day = view.findViewById(R.id.iv_share_this_day);
        tv_user_name = view.findViewById(R.id.tv_user_name);
    }

    public void applyClickListeners() {
        iv_share_this_day.setOnClickListener(this);
    }

    public void setUi() {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvCurrentYear.setText(context.getResources().getString(R.string.year) + " 2018");
        tv_current_class.setText(context.getResources().getString(R.string.classname) + " VII");
        tv_current_section.setText(context.getResources().getString(R.string.section) + " 2");
        tv_selected_school.setText(context.getResources().getString(R.string.school) + " " + context.getResources().getString(R.string.name));
        tv_this_day.setText(context.getResources().getString(R.string.this_day) + "(" + todayDate + ")");
    }

    public void setRecyclerViewToday(boolean showScreenShotInfo) {
        recyclerViewToday.setHasFixedSize(true);
        recyclerViewToday.setLayoutManager(linearLayoutManagerToday);
        todayDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HomeTodayData homeTodayData = new HomeTodayData();
            if (i == 4) {
                homeTodayData.setPersonal(true);
            } else {
                homeTodayData.setPersonal(false);
            }
            homeTodayData.setUserName(tv_user_name.getText().toString());
            homeTodayData.setSchoolName(tv_selected_school.getText().toString());
            homeTodayData.setClassname(tv_current_class.getText().toString());
            homeTodayData.setSection(tv_current_section.getText().toString());
            homeTodayData.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            homeTodayData.setCurrentYear(tvCurrentYear.getText().toString());
            homeTodayData.setUserId(i + "");
            homeTodayData.setResult(9 + i + "");
            homeTodayData.setTotalMarks(30 + "");
            homeTodayData.setUserName(context.getResources().getString(R.string.current_user) + " " + i);

            todayDataList.add(homeTodayData);
        }
        homeTodayAdapter = new HomeTodayAdapter(context, todayDataList, this, showScreenShotInfo);
        recyclerViewToday.setAdapter(homeTodayAdapter);


    }

    public void setRecyclerViewUnitTest() {
        recyclerViewUnitTest.setHasFixedSize(true);
        recyclerViewUnitTest.setLayoutManager(linearLayoutManagerUnitTest);
        todayDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HomeTodayData homeTodayData = new HomeTodayData();
            if (i == 4) {
                homeTodayData.setPersonal(true);
            } else {
                homeTodayData.setPersonal(false);
            }
            homeTodayData.setUserId(i + "");
            homeTodayData.setResult(9 + i + "");
            homeTodayData.setTotalMarks(30 + "");
            homeTodayData.setUserName(context.getResources().getString(R.string.current_user) + " " + i);
            todayDataList.add(homeTodayData);
        }
        homeTodayAdapter = new HomeTodayAdapter(context, todayDataList, this, false);
        recyclerViewUnitTest.setAdapter(homeTodayAdapter);

    }

    public void setRecyclerViewHalfYearly() {


    }

    public void setRecyclerViewYearly() {

    }

    @Override
    public void onThisDayItemClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("org_id", getSelectedOrgId());
        ((HomeTestActivity) getActivity()).openNewFragment(new ProfileFragment(), true, bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share_this_day:
//                shareLayoutImage(recyclerViewToday);
                if (!isReadStoragePermissionGranted()) {
                    return;
                } else if (!isWriteStoragePermissionGranted()) {
                    return;
                } else {
                    setRecyclerViewToday(true);
                    shareLayoutImage(recyclerViewToday);
                }

                break;

        }
    }

    private void shareLayoutImage(final RecyclerView recyclerView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 500);
        LayoutToImageConverter layoutToImageConverter = new LayoutToImageConverter(context, recyclerView);
        layoutToImageConverter.setOnLayoutCapturedListener(HomeFragment.this);
        layoutToImageConverter.shareLayoutImage(getResources().getString(R.string.share_result), tv_this_day.getText().toString());

    }


    @Override
    public void screenshotCaptured() {
        setRecyclerViewToday(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
//                    downloadPdfFile();
                } else {
//                    progress.dismiss();
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
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
