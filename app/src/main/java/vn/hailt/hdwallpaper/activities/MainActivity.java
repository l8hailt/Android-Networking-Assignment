package vn.hailt.hdwallpaper.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;

import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.ultis.TitleToolbarManager;
import vn.hailt.hdwallpaper.adapters.DrawerItemAdapter;
import vn.hailt.hdwallpaper.fragments.AboutUsFragment;
import vn.hailt.hdwallpaper.fragments.CategoryFragment;
import vn.hailt.hdwallpaper.fragments.LatestFragment;
import vn.hailt.hdwallpaper.models.DrawerItem;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public Toolbar toolbar;
    private NavigationView navigationView;
    private View headerNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(Gravity.START));

        drawerLayout = findViewById(R.id.drawerLayout);

        navigationView = findViewById(R.id.navigationView);
        headerNavigationView = navigationView.getHeaderView(0);
        initItemDrawer(headerNavigationView.findViewById(R.id.recyclerView));

        setTitle("Latest");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, LatestFragment.newInstance())
                .commit();
    }

    private void initItemDrawer(RecyclerView recyclerView) {
        ArrayList<DrawerItem> items = new ArrayList<>();
        items.add(new DrawerItem(R.drawable.ic_home, "Latest"));
        items.add(new DrawerItem(R.drawable.ic_category, "Category"));
        items.add(new DrawerItem(R.drawable.ic_gifs, "GIFs"));
        items.add(new DrawerItem(R.drawable.ic_favorite, "My Favorites"));
        items.add(new DrawerItem(R.drawable.ic_rate, "Rate App"));
        items.add(new DrawerItem(R.drawable.ic_moreapp, "More App"));
        items.add(new DrawerItem(R.drawable.ic_about, "About Us"));
        items.add(new DrawerItem(R.drawable.ic_setting, "Setting"));
        items.add(new DrawerItem(R.drawable.ic_privacy, "Privacy Policy"));

        DrawerItemAdapter adapter = new DrawerItemAdapter(this, items);
        recyclerView.setAdapter(adapter);
        adapter.setListener(position -> {

            switch (position) {
                case 0:
                    replaceFragment(LatestFragment.newInstance(), LatestFragment.class.getName());

                    setTitle(items.get(position).getTitle());
                    TitleToolbarManager.get.putTitle(items.get(position).getTitle());
                    drawerLayout.closeDrawer(Gravity.START);
                    break;

                case 1:
                    replaceFragment(CategoryFragment.newInstance(), CategoryFragment.class.getName());

                    setTitle(items.get(position).getTitle());
                    TitleToolbarManager.get.putTitle(items.get(position).getTitle());
                    drawerLayout.closeDrawer(Gravity.START);
                    break;

                case 6:
                    replaceFragment(AboutUsFragment.newInstance(), AboutUsFragment.class.getName());

                    setTitle(items.get(position).getTitle());
                    drawerLayout.closeDrawer(Gravity.START);
                    break;
            }
        });
    }

    public void replaceFragment(Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null)

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, tag)
                    .commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment, String backStack, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {

            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(backStack)
                    .setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_in, R.anim.anim_out)
                    .add(R.id.fragmentContainer, fragment, tag)
                    .commitAllowingStateLoss();
        }
    }

    public void addFragmentFullScreen(Fragment fragment, String backStack, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {

            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(backStack)
                    .add(R.id.drawerLayout, fragment, tag)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START);
        else if (getFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

}
