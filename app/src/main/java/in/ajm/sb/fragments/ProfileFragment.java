package in.ajm.sb.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;

import in.ajm.sb.R;
import in.ajm.sb.activities.HomeTestActivity;
import in.ajm.sb.helper.GeneralHelper;
import in.ajm.sb_library.charts.FitChart;
import in.ajm.sb_library.charts.FitChartValue;

public class ProfileFragment extends BaseFragment{
    FitChart fitChartMonthly;
    FitChart fitChartYearly;
    FitChart fitChartOverAll;
    Collection<FitChartValue> values;
    Point point;
    

    public ProfileFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, null);
        setRetainInstance(true);
        viewByIds(v);
        applyClickListeners();
        ((HomeTestActivity)getActivity()).setHomePageTitle(getResources().getString(R.string.profile));
        setFitChartValues();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setChart(values, fitChartYearly);
                setChart(values, fitChartMonthly);
                setChart(values, fitChartOverAll);
            }
        }, 2000);


        return v;
    }

    public void viewByIds(View view) {
        fitChartMonthly = view.findViewById(R.id.fitChart_monthly);
        fitChartYearly = view.findViewById(R.id.fitChart_yearly);
        fitChartOverAll = view.findViewById(R.id.fitChart_over_all);
        point = GeneralHelper.getInstance(getActivity()).getScreenSize();
        setLayoutParamsForChart(fitChartMonthly);
        setLayoutParamsForChart(fitChartYearly);
        setLayoutParamsForChart(fitChartOverAll);

    }

    public void applyClickListeners() {

    }

    private void setChart(Collection<FitChartValue> values, FitChart fitChart) {
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(232f);
        fitChart.setValues(values);
        fitChart.setFocusable(false);
    }

    private void setFitChartValues() {
        values = new ArrayList<>();
        values.add(new FitChartValue(78f, ContextCompat.getColor(getActivity(), R.color.md_blue_600)));
        values.add(new FitChartValue(93f, ContextCompat.getColor(getActivity(), R.color.red_600)));
        values.add(new FitChartValue(61f, ContextCompat.getColor(getActivity(), R.color.green_600)));
    }

    public void setLayoutParamsForChart(FitChart fitChart) {
        ViewGroup.LayoutParams layoutParams = fitChart.getLayoutParams();
        layoutParams.width = point.x / 3;
        layoutParams.height = point.x / 3;
        fitChart.setLayoutParams(layoutParams);
    }
}
