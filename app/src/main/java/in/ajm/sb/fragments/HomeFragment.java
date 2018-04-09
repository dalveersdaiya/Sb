package in.ajm.sb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;

public class HomeFragment extends BaseFragment{

    public HomeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        setRetainInstance(true);
        ((BaseActivity)getActivity()).setupToolBar(getResources().getString(R.string.home_page));
        return v;
    }
}
