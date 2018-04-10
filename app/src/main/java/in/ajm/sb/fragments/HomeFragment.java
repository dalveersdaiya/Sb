package in.ajm.sb.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.ajm.sb.R;
import in.ajm.sb.activities.HomeTestActivity;
import in.ajm.sb.adapter.HomeTodayAdapter;
import in.ajm.sb.data.HomeTodayData;
import in.ajm.sb.interfaces.OnThisDayItemClicked;

public class HomeFragment extends BaseFragment implements OnThisDayItemClicked {

    TextView tvCurrentYear;
    TextView tv_selected_school;
    TextView tv_current_class;
    TextView tv_current_section;
    TextView tv_this_day;
    TextView tv_this_unit_test;
    TextView tv_this_half_year;
    TextView tv_this_year;
    Context context;


    RecyclerView recyclerViewUnitTest;
    RecyclerView recyclerViewHalfYearly;
    RecyclerView recyclerViewYearly;

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
        ((HomeTestActivity) getActivity()).setHomePageTitle(getResources().getString(R.string.home_page));
        viewByIds(v);
        setUi();
        setRecyclerViewToday();
        setRecyclerViewUnitTest();
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
    }

    public void setUi() {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvCurrentYear.setText(context.getResources().getString(R.string.year) + " 2018");
        tv_current_class.setText(context.getResources().getString(R.string.classname) + " VII");
        tv_current_section.setText(context.getResources().getString(R.string.section) + " 2");
        tv_selected_school.setText(context.getResources().getString(R.string.school) + " " + context.getResources().getString(R.string.name));
        tv_this_day.setText(context.getResources().getString(R.string.this_day) + "(" + todayDate + ")");
    }

    public void setRecyclerViewToday() {
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
            homeTodayData.setUserId(i + "");
            homeTodayData.setResult(9 + i + "");
            homeTodayData.setTotalMarks(30 + "");
            homeTodayData.setUserName(context.getResources().getString(R.string.current_user) + " " + i);
            todayDataList.add(homeTodayData);
        }
        homeTodayAdapter = new HomeTodayAdapter(context, todayDataList, this);
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
        homeTodayAdapter = new HomeTodayAdapter(context, todayDataList, this);
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
}
