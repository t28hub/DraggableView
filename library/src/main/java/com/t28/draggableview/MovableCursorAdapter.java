package com.t28.draggableview;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class MovableCursorAdapter<VH extends RecyclerView.ViewHolder> extends DraggableView.Adapter<VH> {
    private Cursor mCursor;

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
