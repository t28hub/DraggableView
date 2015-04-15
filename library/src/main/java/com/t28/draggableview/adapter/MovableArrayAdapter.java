package com.t28.draggableview.adapter;

import android.support.v7.widget.RecyclerView;

import com.t28.draggableview.DraggableView;

import java.util.ArrayList;
import java.util.List;

public abstract class MovableArrayAdapter<T, VH extends RecyclerView.ViewHolder> extends DraggableView.Adapter<VH> {
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
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
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

        mItems.add(position2, mItems.remove(position1));
        notifyItemMoved(position1, position2);
        return true;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    public void remove(T item) {
        final int position = mItems.indexOf(item);
        if (position < 0) {
            throw new IllegalArgumentException("Item does not found:" + item);
        }
        notifyItemRemoved(position);
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    private boolean isValidPosition(int position) {
        return position >= 0 && position < mItems.size();
    }
}
