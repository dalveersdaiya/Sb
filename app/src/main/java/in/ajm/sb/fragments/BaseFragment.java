package in.ajm.sb.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.PreferencesManager;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    public String TAG = "Daiya";
    public String APPNAME = "sb";
    public Context context = ((BaseActivity) getActivity());
    public Realm realm;

    public BaseFragment() {

    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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

    public String getSelectedOrgId() {
        return PreferencesManager.getPreferenceByKey(getContext(), AppConfigs.PREFERENCE_ORG_ID);
    }

    public String getAuth() {
        return PreferencesManager.getPreferenceByKey(getContext(), AppConfigs.PREFERENCE_AUTH);
    }

    public String getUserId() {
        if (getContext() != null) {
            return BaseActivity.getUserId(getContext());
        }
        return "0";
    }

    public String getLoggedInUserId() {
        if (getContext() != null) {
            return BaseActivity.getLoggedInUserId(getContext());
        }
        return "0";
    }

    public void showLoader() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showLoader();
        }
    }

    void hideLoader() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hideLoader();
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
            return true;
        }
    }

    public void beginRealmTransaction() {
        realm = Realm.getDefaultInstance();
        if (!realm.isInTransaction())
            realm.beginTransaction();
    }

    public void commitAndCloseRealmTransaction(RealmObject object) {
        realm.copyToRealmOrUpdate(object);
        if (realm.isInTransaction())
            realm.commitTransaction();
        if (realm.isInTransaction())
            realm.commitTransaction();
    }

    public void commitRealmTransaction() {
        if (realm.isInTransaction())
            realm.commitTransaction();
    }

    public void closeRealmTransaction() {
        if (!realm.isClosed())
            realm.close();
    }

    public int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public int getColorPrimaryDark(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public int getColorMyThemelight(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_theme_light, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    /*White*/
    public int getOppositeColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_text_color_inverse, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    /*Black*/
    public int getTextColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.my_text_color, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

}
