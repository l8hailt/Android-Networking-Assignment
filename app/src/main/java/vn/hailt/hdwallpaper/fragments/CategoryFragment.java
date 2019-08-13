package vn.hailt.hdwallpaper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.activities.MainActivity;
import vn.hailt.hdwallpaper.adapters.CategoryAdapter;
import vn.hailt.hdwallpaper.models.Category;
import vn.hailt.hdwallpaper.models.Post;
import vn.hailt.hdwallpaper.retrofit.WppRetrofit;

public class CategoryFragment extends BaseFragment {

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private SwipeRefreshLayout refreshLayout;
    private ShimmerFrameLayout shimmerViewContainer;
    private RecyclerView rvCategory;
    private List<Category> categories;
    private CategoryAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        categories = new ArrayList<>();
        adapter = new CategoryAdapter(activity, categories, category -> {

            PostsOfCategoryFragment fragment = PostsOfCategoryFragment.newInstance(category.getId());
            activity.addFragment(fragment, PostsOfCategoryFragment.class.getName(), PostsOfCategoryFragment.class.getName());
            activity.setTitle(category.getName());

        });
        rvCategory.setLayoutManager(new LinearLayoutManager(activity));
        rvCategory.setAdapter(adapter);

        fetchCategories();

        refreshLayout.setOnRefreshListener(() -> {
            fetchCategories();
            refreshLayout.setRefreshing(false);
        });

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_category;
    }

    @Override
    public void initViews(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        shimmerViewContainer = view.findViewById(R.id.shimmerViewContainer);
        rvCategory = view.findViewById(R.id.rvCategory);
    }

    private void fetchCategories() {
        WppRetrofit.getInstance().getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                if (response.code() == 200) {

                    if (response.body() != null) {
                        categories.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
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
