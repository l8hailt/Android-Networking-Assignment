package vn.hailt.hdwallpaper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.hailt.hdwallpaper.activities.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayout(), container, false);
        initViews(view);
        activity = (MainActivity) getActivity();

        return view;
    }

    public abstract int getLayout();

    public abstract void initViews(View view);

}
