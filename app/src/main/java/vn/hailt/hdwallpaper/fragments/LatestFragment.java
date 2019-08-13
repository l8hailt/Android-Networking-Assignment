package vn.hailt.hdwallpaper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.adapters.PostAdapter;
import vn.hailt.hdwallpaper.models.Post;
import vn.hailt.hdwallpaper.retrofit.WppRetrofit;

public class LatestFragment extends BaseFragment {

    public static LatestFragment newInstance() {
        LatestFragment fragment = new LatestFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private SwipeRefreshLayout refreshLayout;
    private ShimmerFrameLayout shimmerViewContainer;
    private RecyclerView rvPost;
    private List<Post> posts;
    private PostAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        posts = new ArrayList<>();
        adapter = new PostAdapter(activity, posts, post -> {
            MediaFragment fragment = MediaFragment.newInstance(post.getId());
            activity.addFragment(fragment, MediaFragment.class.getName(), MediaFragment.class.getName());
            activity.setTitle(post.getTitle().getRendered());
        });
        rvPost.setLayoutManager(new LinearLayoutManager(activity));
        rvPost.setAdapter(adapter);

        fetchPosts();

        refreshLayout.setOnRefreshListener(() -> {
            fetchPosts();
            refreshLayout.setRefreshing(false);
        });

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_latest;
    }

    @Override
    public void initViews(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        shimmerViewContainer = view.findViewById(R.id.shimmerViewContainer);
        rvPost = view.findViewById(R.id.rvPost);
    }

    private void fetchPosts() {
        WppRetrofit.getInstance().getAllPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.code() == 200) {

                    if (response.body() != null) {
                        posts.clear();
                        posts.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        shimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerViewContainer.stopShimmer();
    }
}
