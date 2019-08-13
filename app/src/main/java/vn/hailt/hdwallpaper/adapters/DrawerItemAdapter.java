package vn.hailt.hdwallpaper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.hailt.hdwallpaper.interfaces.IOnClickListener;
import vn.hailt.hdwallpaper.models.DrawerItem;
import vn.hailt.hdwallpaper.R;

public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.DrawerItemHolder> {

    private Context context;
    private ArrayList<DrawerItem> items;
    private IOnClickListener listener;

    public DrawerItemAdapter(Context context, ArrayList<DrawerItem> items) {
        this.context = context;
        this.items = items;
    }

    public void setListener(IOnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override

    public DrawerItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_drawer, viewGroup, false);
        return new DrawerItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerItemHolder holder, int i) {

        holder.imgIcon.setImageResource(items.get(i).getIcon());
        holder.tvTitle.setText(items.get(i).getTitle());

        if (listener != null)
            holder.itemView.setOnClickListener(view -> listener.onClick(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class DrawerItemHolder extends RecyclerView.ViewHolder {

        private ImageView imgIcon;
        private TextView tvTitle;

        DrawerItemHolder(@NonNull View itemView) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);

        }
    }
}
