package vn.hailt.hdwallpaper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import vn.hailt.hdwallpaper.models.Media;

public class PhotoPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Media> media;
    private IOnPhotoClickListener listener;

    public PhotoPagerAdapter(Context context, List<Media> media, IOnPhotoClickListener listener) {
        this.context = context;
        this.media = media;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return media.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(context);

        Media medium = media.get(position);

        Glide.with(context)
                .load(medium.getMediaDetails().getSizes().getFull().getSourceUrl())
                .override(medium.getMediaDetails().getSizes().getFull().getWidth(),
                        medium.getMediaDetails().getSizes().getFull().getHeight())
                .into(photoView);

        photoView.setOnPhotoTapListener((view, x, y) -> {
            if (listener != null)
                listener.onClick();
        });

        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface IOnPhotoClickListener {
        void onClick();
    }
}
