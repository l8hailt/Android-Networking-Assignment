package vn.hailt.hdwallpaper.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import vn.hailt.hdwallpaper.interfaces.IOnCategoryClickListener;
import vn.hailt.hdwallpaper.ultis.ColorWheel;
import vn.hailt.hdwallpaper.R;
import vn.hailt.hdwallpaper.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private Context context;
    private List<Category> categories;
    private IOnCategoryClickListener listener;

    public CategoryAdapter(Context context, List<Category> categories, IOnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int i) {

        Category category = categories.get(i);

        holder.tvCateNameAndNumberPost.setText(
                context.getString(R.string.category_name_and_number_posts,
                        category.getName(),
                        category.getCount()));

        holder.itemView.setOnClickListener(view -> {
            if (listener != null)
                listener.onClick(category);
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        private TextView tvCateNameAndNumberPost;
        private RelativeLayout rlItem;

        CategoryHolder(@NonNull View itemView) {
            super(itemView);

            tvCateNameAndNumberPost = itemView.findViewById(R.id.tvCateNameAndNumberPost);
            rlItem = itemView.findViewById(R.id.rlItem);

            int[] gradientColors = ColorWheel.getGradientColors();

            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{gradientColors[0], gradientColors[1]});

            rlItem.setBackground(gradientDrawable);

        }
    }
}
