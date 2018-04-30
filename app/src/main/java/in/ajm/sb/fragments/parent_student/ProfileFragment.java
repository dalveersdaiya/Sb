package in.ajm.sb.fragments.parent_student;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import in.ajm.sb.R;
import in.ajm.sb.activities.student_parent.HomeTestActivity;
import in.ajm.sb.api.model.UserCredentials;
import in.ajm.sb.customviews.CircularImageView;
import in.ajm.sb.fragments.BaseFragment;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.FileHelper;
import in.ajm.sb.helper.GeneralHelper;
import in.ajm.sb.helper.LoggerCustom;
import in.ajm.sb.helper.StringHelper;
import in.ajm.sb_library.charts.FitChart;
import in.ajm.sb_library.charts.FitChartValue;

import static in.ajm.sb.helper.AppConfigs.PREFERENCE_USER_ID;
import static in.ajm.sb.helper.AppConfigs.REQUEST_CODE_IMAGE_PICKER;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    FitChart fitChartMonthly;
    FitChart fitChartYearly;
    FitChart fitChartOverAll;
    Collection<FitChartValue> values;
    Point point;
    CircularImageView civUserImage;
    Context context;
    UserCredentials userCredentials;

    public ProfileFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, null);
        setRetainInstance(true);
        viewByIds(v);
        applyClickListeners();
        ((HomeTestActivity) getActivity()).setHomePageTitle(getResources().getString(R.string.app_name));
        setUi();
        return v;
    }

    public void setUi() {
        setFitChartValues();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setChart(values, fitChartYearly);
                setChart(values, fitChartMonthly);
                setChart(values, fitChartOverAll);
            }
        }, 1000);

        userCredentials = UserCredentials.getByUserId(PREFERENCE_USER_ID);
        if(userCredentials != null){
            Picasso.with(getContext())
                    .load(userCredentials.getUserImage())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .noPlaceholder()
                    .into(civUserImage);

        }
    }

    public void viewByIds(View view) {
        context = getActivity();
        fitChartMonthly = view.findViewById(R.id.fitChart_monthly);
        fitChartYearly = view.findViewById(R.id.fitChart_yearly);
        fitChartOverAll = view.findViewById(R.id.fitChart_over_all);
        civUserImage = view.findViewById(R.id.civ_user_image);
        point = GeneralHelper.getInstance(getActivity()).getScreenSize();
        setLayoutParamsForChart(fitChartMonthly);
        setLayoutParamsForChart(fitChartYearly);
        setLayoutParamsForChart(fitChartOverAll);

    }

    public void applyClickListeners() {
        civUserImage.setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_user_image:
                chooseImagePicker();
                break;
        }
    }

    private void chooseImagePicker() {
        new TedPermission(context)
                .setDeniedMessage(getStringRes(R.string.permission_required))
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.pickImage(ProfileFragment.this, REQUEST_CODE_IMAGE_PICKER);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET)
                .check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICKER) {
            Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), requestCode, resultCode, data);
            if (bitmap != null) {
                final String imgPath = FileHelper.saveImageToDirectory(getContext(), "temp", StringHelper.uniqueId() + ".jpg", bitmap, true);
                String destFilePath = FileHelper.getAppDirectory(getContext(), true).getPath() + File.separator + "temp" + File.separator + StringHelper.uniqueId() + ".jpg";
                cropImage(imgPath, destFilePath);
                LoggerCustom.logD(TAG, imgPath + "\n" + destFilePath);
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
//                callimageUploadApi(String.valueOf(resultUri));
                setImageInDbAndShow(resultUri);
                LoggerCustom.logD(TAG, resultUri.toString());
            } else if (resultCode == UCrop.RESULT_ERROR) {
                LoggerCustom.logD(TAG, "Error occurred");
                final Throwable cropError = UCrop.getError(data);
                LoggerCustom.printStackTrace(cropError);
            }
        }
    }

    private void cropImage(String srcFilePath, String destFilePath) {
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(getColorPrimaryDark(context));
        options.setToolbarColor(getColorPrimary(context));
        options.setActiveWidgetColor(getAccentColor(context));
        options.setShowCropFrame(true);
        options.setCircleDimmedLayer(true);
        options.setFreeStyleCropEnabled(true);

        UCrop.of(Uri.fromFile(new File(srcFilePath)), Uri.fromFile(new File(destFilePath)))
                .withAspectRatio(2, 2)
                .withMaxResultSize(512, 512).
                withOptions(options)
                .start(context, this);
    }

    public void setImageInDbAndShow(Uri resultUri) {
        LoggerCustom.logD(TAG, resultUri.toString());
        userCredentials = UserCredentials.getByUserId(AppConfigs.PREFERENCE_USER_ID);
        beginRealmTransaction();
        try{

            userCredentials.setUserImage(resultUri.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        realm.copyToRealmOrUpdate(userCredentials);
        commitAndCloseRealmTransaction(userCredentials);
        Picasso.with(getContext())
                .load(userCredentials.getUserImage())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .noPlaceholder()
                .into(civUserImage);
    }


}
