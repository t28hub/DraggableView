package com.t28.draggableview.demo.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.t28.draggableview.DraggableView;
import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.data.model.App;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends DraggableView.Adapter<AppListAdapter.ItemViewHolder> {
    private final List<App> mApps;
    private OnItemLongClickListener mItemLongClickListener;

    public AppListAdapter() {
        super();
        mApps = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.linear_layout_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final App app = getItem(position);
        holder.bind(app);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    @Override
    public boolean move(int position1, int position2) {
        if (!isValidPosition(position1)) {
            throw new IndexOutOfBoundsException("'position1' is invalid:" + position1);
        }
        if (!isValidPosition(position2)) {
            throw new IndexOutOfBoundsException("'position2' is invalid:" + position2);
        }

        if (position1 == position2) {
            return false;
        }

        mApps.add(position2, mApps.remove(position1));
        notifyItemMoved(position1, position2);
        return true;
    }

    public void changeApps(List<App> apps) {
        mApps.clear();
        if (apps != null && apps.size() != 0) {
            mApps.addAll(apps);
        }
        notifyDataSetChanged();
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }

    private App getItem(int position) {
        return mApps.get(position);
    }

    private boolean isValidPosition(int position) {
        return position >= 0 && position < mApps.size();
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, View view);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private final ImageView mIconView;
        private final TextView mPrimaryTextView;
        private final TextView mSecondaryTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            mIconView = (ImageView) itemView.findViewById(R.id.linear_layout_item_icon);
            mPrimaryTextView = (TextView) itemView.findViewById(R.id.linear_layout_item_primary_text);
            mSecondaryTextView = (TextView) itemView.findViewById(R.id.linear_layout_item_secondary_text);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mItemLongClickListener == null) {
                return false;
            }

            mItemLongClickListener.onItemLongClick(getAdapterPosition(), itemView);
            return true;
        }

        public void bind(App app) {
            mIconView.setImageDrawable(app.getIcon());
            mPrimaryTextView.setText(app.getName());
            mSecondaryTextView.setText(app.getPackageName());
        }
    }
}
