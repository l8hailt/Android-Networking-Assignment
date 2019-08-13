package vn.hailt.hdwallpaper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.interfaces.IOnMediaClickListener;
import vn.hailt.hdwallpaper.models.Media;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaHolder> {

    private Context context;
    private List<Media> mediaList;
    private IOnMediaClickListener listener;

    public MediaAdapter(Context context, List<Media> posts, IOnMediaClickListener listener) {
        this.context = context;
        this.mediaList = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MediaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_media, viewGroup, false);
        return new MediaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaHolder holder, int i) {

        Media media = mediaList.get(i);

        Glide.with(context)
                .load(media.getMediaDetails().getSizes().getMedium().getSourceUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgThumbnail);

        holder.imgThumbnail.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    class MediaHolder extends RecyclerView.ViewHolder {

        private ImageView imgThumbnail;

        MediaHolder(@NonNull View itemView) {
            super(itemView);

            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);

        }
    }
}
