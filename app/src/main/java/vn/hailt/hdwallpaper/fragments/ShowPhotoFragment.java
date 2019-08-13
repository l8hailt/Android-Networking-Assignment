package vn.hailt.hdwallpaper.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.hailt.hdwallpaper.BuildConfig;
import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.adapters.PhotoPagerAdapter;
import vn.hailt.hdwallpaper.asynctask.DownloadTask;
import vn.hailt.hdwallpaper.models.Media;
import vn.hailt.hdwallpaper.ultis.ShowPhotoUtils;
import vn.hailt.hdwallpaper.ultis.Utils;

public class ShowPhotoFragment extends BaseFragment {

    public static ShowPhotoFragment newInstance(ArrayList<Media> media, int position) {
        ShowPhotoFragment fragment = new ShowPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("media", media);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ViewPager vpgPhoto;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabSave;
    private FloatingActionButton fabSetAs;
    private ProgressDialog progressDialog;

    private ArrayList<Media> media;
    private int position;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.getArguments() != null) {
            media = this.getArguments().getParcelableArrayList("media");
            position = this.getArguments().getInt("position");

            PhotoPagerAdapter adapter = new PhotoPagerAdapter(activity, media, () -> {

            });
            vpgPhoto.setAdapter(adapter);
            vpgPhoto.setCurrentItem(position);

            vpgPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    position = i;
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

        }

        initActions();

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_show_photo;
    }

    @Override
    public void initViews(View view) {
        vpgPhoto = view.findViewById(R.id.vpgPhoto);
        fabMenu = view.findViewById(R.id.fab_menu);
        fabShare = view.findViewById(R.id.fab_share);
        fabSave = view.findViewById(R.id.fab_save);
        fabSetAs = view.findViewById(R.id.fab_set_as);
    }

    private void initActions() {
        fabShare.setOnClickListener(view -> {
            fabMenu.collapse();

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                this.requestPermissions(permissions, 543);
            } else {
                shareImage();
            }
        });

        fabSave.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                this.requestPermissions(permissions, 543);
            } else {
                saveImage();
            }

        });

        fabSetAs.setOnClickListener(view -> {
            fabMenu.collapse();
            setAsWallpaper();
        });
    }

    private void shareImage() {
        Media medium = media.get(position);
        Glide.with(activity)
                .asBitmap()
                .load(medium.getMediaDetails().getSizes().getFull().getSourceUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(new Date());
                        String fileName = "wpp_" + timeStamp + ".jpg";
                        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HDWallpaper";

                        File folder = new File(dirPath);

                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        File file = new File(dirPath, fileName);

                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            int quality = 100;
                            resource.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                            fos.flush();
                            fos.close();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                        Uri uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", file);

                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("image/*");

                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        try {
                            activity.startActivity(Intent.createChooser(intent, "Share via"));
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "App not available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Toast.makeText(activity, "Failed to load Image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveImage() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Downloading...");
        progressDialog.show();

        Media medium = media.get(position);
        Glide.with(activity)
                .asBitmap()
                .load(medium.getMediaDetails().getSizes().getFull().getSourceUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        DownloadTask downloadTask = new DownloadTask(() -> {
                            progressDialog.dismiss();
                            fabMenu.collapse();
                            Toast.makeText(activity, "Save", Toast.LENGTH_SHORT).show();
                        });
                        downloadTask.execute(resource);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void setAsWallpaper() {
        Media medium = media.get(position);
        Glide.with(activity)
                .asBitmap()
                .load(medium.getMediaDetails().getSizes().getFull().getSourceUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
                        try {
                            wallpaperManager.setBitmap(resource);
                            Toast.makeText(activity, "Set Wallpaper Successful", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 543) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                saveImage();

            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
