package com.t28.draggableview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.t28.draggableview.DraggableView;

import java.util.ArrayList;
import java.util.List;

public class MovableListAdapter<T, VH extends RecyclerView.ViewHolder> extends DraggableView.Adapter<VH> {
    private final List<T> mItems;

    public MovableListAdapter() {
        this(null);
    }

    public MovableListAdapter(List<T> items) {
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
        return 0;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public boolean move(int position1, int position2) {
        return false;
    }
}
