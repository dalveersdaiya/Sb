package in.ajm.sb.fragments;


import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;


import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.helper.PreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {
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




}
