package in.ajm.sb.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;

import in.ajm.sb.R;
import in.ajm.sb.helper.GeneralHelper;
import in.ajm.sb_library.charts.FitChart;
import in.ajm.sb_library.charts.FitChartValue;

public class Profile extends BaseActivity {

    FitChart fitChartMonthly;
    FitChart fitChartYearly;
    FitChart fitChartOverAll;
    Collection<FitChartValue> values;
    Point point;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_profile);
        viewByIds();
        applyClickListeners();
        setupToolBar(getResources().getString(R.string.profile), true);
        setFitChartValues();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setChart(values, fitChartYearly);
                setChart(values, fitChartMonthly);
                setChart(values, fitChartOverAll);
            }
        }, 2000);

    }

    public void viewByIds() {
        fitChartMonthly = findViewById(R.id.fitChart_monthly);
        fitChartYearly = findViewById(R.id.fitChart_yearly);
        fitChartOverAll = findViewById(R.id.fitChart_over_all);
        point = GeneralHelper.getInstance(this).getScreenSize();
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
        values.add(new FitChartValue(78f, ContextCompat.getColor(this, R.color.md_blue_600)));
        values.add(new FitChartValue(93f, ContextCompat.getColor(this, R.color.red_600)));
        values.add(new FitChartValue(61f, ContextCompat.getColor(this, R.color.green_600)));
    }

    public void setLayoutParamsForChart(FitChart fitChart) {
        ViewGroup.LayoutParams layoutParams = fitChart.getLayoutParams();
        layoutParams.width = point.x / 3;
        layoutParams.height = point.x / 3;
        fitChart.setLayoutParams(layoutParams);
    }



}
