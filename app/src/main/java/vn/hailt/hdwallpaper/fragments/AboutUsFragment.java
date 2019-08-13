package vn.hailt.hdwallpaper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.ultis.TitleToolbarManager;
import vn.hailt.hdwallpaper.ultis.Utils;

public class AboutUsFragment extends BaseFragment {

    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Utils.textJustify(view.findViewById(R.id.tvAboutContent));
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_about_us;
    }

    @Override
    public void initViews(View view) {

    }
}
