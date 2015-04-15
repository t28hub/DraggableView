package com.t28.draggableview.demo.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.t28.draggableview.adapter.MovableArrayAdapter;
import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.data.model.App;

public class AppAdapter extends MovableArrayAdapter<App, AppAdapter.ItemViewHolder> {
    private OnItemLongClickListener mItemLongClickListener;

    public AppAdapter() {
        super();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.layout_list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final App app = getItem(position);
        holder.bind(app);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
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
