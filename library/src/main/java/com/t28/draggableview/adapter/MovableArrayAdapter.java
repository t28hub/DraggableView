package com.t28.draggableview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.t28.draggableview.DraggableView;

import java.util.ArrayList;
import java.util.List;

public class MovableArrayAdapter<T, VH extends RecyclerView.ViewHolder> extends DraggableView.Adapter<VH> {
    private final List<T> mItems;

    public MovableArrayAdapter() {
        this(null);
    }

    public MovableArrayAdapter(List<T> items) {
        mItems = new ArrayList<>();
        if (items != null && items.size() != 0) {
            mItems.addAll(items);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public boolean move(int position1, int position2) {
        return false;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }
}
