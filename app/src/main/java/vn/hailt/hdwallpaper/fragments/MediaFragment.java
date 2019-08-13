package vn.hailt.hdwallpaper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.adapters.MediaAdapter;
import vn.hailt.hdwallpaper.adapters.PostAdapter;
import vn.hailt.hdwallpaper.models.Media;
import vn.hailt.hdwallpaper.models.Post;
import vn.hailt.hdwallpaper.retrofit.WppRetrofit;
import vn.hailt.hdwallpaper.ultis.TitleToolbarManager;

public class MediaFragment extends BaseFragment {

    public static MediaFragment newInstance(int parent) {
        MediaFragment fragment = new MediaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("parent", parent);
        fragment.setArguments(bundle);
        return fragment;
    }

    private RecyclerView rvMedia;
    private TextView tvNoPhotos;
    private ProgressBar prbLoading;
    private ArrayList<Media> media;
    private MediaAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity.toolbar.setNavigationIcon(R.drawable.ic_back);
        activity.toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());

        media = new ArrayList<>();
        adapter = new MediaAdapter(activity, media, position -> {

            ShowPhotoFragment fragment = ShowPhotoFragment.newInstance(media, position);
            activity.addFragment(fragment, ShowPhotoFragment.class.getName(), ShowPhotoFragment.class.getName());
        });

        rvMedia.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvMedia.setAdapter(adapter);

        fetchMedia();

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_media;
    }

    @Override
    public void initViews(View view) {
        rvMedia = view.findViewById(R.id.rvMedia);
        tvNoPhotos = view.findViewById(R.id.tvNoPhotos);
        prbLoading = view.findViewById(R.id.prbLoading);
    }

    private void fetchMedia() {
        if (this.getArguments() != null) {
            int parent = this.getArguments().getInt("parent");

            WppRetrofit.getInstance().getMediaOfPost(parent).enqueue(new Callback<List<Media>>() {
                @Override
                public void onResponse(Call<List<Media>> call, Response<List<Media>> response) {
                    if (response.code() == 200) {

                        if (response.body() != null) {
                            media.clear();
                            media.addAll(response.body());
                            adapter.notifyDataSetChanged();

                            prbLoading.setVisibility(View.GONE);

                            if (media.size() == 0)
                                tvNoPhotos.setVisibility(View.VISIBLE);
                        }

                    }
                }

                @Override
                public void onFailure(Call<List<Media>> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.setTitle(TitleToolbarManager.get.getTitle());
        activity.toolbar.setNavigationIcon(R.drawable.ic_hamburger);
        activity.toolbar.setNavigationOnClickListener(view -> activity.drawerLayout.openDrawer(Gravity.START));
    }
}
