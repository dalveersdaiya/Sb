package in.ajm.sb.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.PreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    public String TAG = "Daiya";
    public BaseFragment() {
    }

    public String getStringRes(@StringRes int resId) {
        if (getContext() != null)
            return getResources().getString(resId);
        return "";
    }

    public String getStringRes(@StringRes int resId, Object... formatArgs) {
        if (getContext() != null)
            return getResources().getString(resId, formatArgs);
        return "";
    }

    String getSelectedOrgId() {
        return PreferencesManager.getPreferenceByKey(getContext(), AppConfigs.PREFERENCE_ORG_ID);
    }

    String getAuth() {
        return PreferencesManager.getPreferenceByKey(getContext(), AppConfigs.PREFERENCE_AUTH);
    }

    String getUserId() {
        if (getContext() != null) {
            return BaseActivity.getUserId(getContext());
        }
        return "0";
    }

    String getLoggedInUserId() {
        if (getContext() != null) {
            return BaseActivity.getLoggedInUserId(getContext());
        }
        return "0";
    }

    void showLoader() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showLoader();
        }
    }

    void hideLoader() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hideLoader();
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }



}
