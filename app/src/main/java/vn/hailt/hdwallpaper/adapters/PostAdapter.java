package vn.hailt.hdwallpaper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.interfaces.IOnPostClickListener;
import vn.hailt.hdwallpaper.models.Post;
import vn.hailt.hdwallpaper.ultis.PositionedCropTransformation;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private Context context;
    private List<Post> posts;
    private IOnPostClickListener listener;

    public PostAdapter(Context context, List<Post> posts, IOnPostClickListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_post, viewGroup, false);
        return new PostHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int i) {

        Post post = posts.get(i);

        holder.tvTitle.setText(post.getTitle().getRendered());
        holder.tvDate.setText(post.getDate());

        int begin = post.getContent().getRendered().lastIndexOf("http");
        int end = post.getContent().getRendered().lastIndexOf(".jpg") + 4;

        if (begin != -1) {
            holder.imgThumbnail.setVisibility(View.VISIBLE);
            String image = post.getContent().getRendered().substring(begin, end);
            Glide.with(context)
                    .load(image)
                    .transform(new PositionedCropTransformation(0f, 0f))
                    .into(holder.imgThumbnail);
        } else {
            holder.imgThumbnail.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            if (listener != null)
                listener.onClick(post);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {

        private ImageView imgThumbnail;
        private TextView tvTitle, tvDate;

        PostHolder(@NonNull View itemView) {
            super(itemView);

            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);

        }
    }
}
