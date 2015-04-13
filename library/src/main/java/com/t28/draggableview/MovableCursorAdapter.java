package com.t28.draggableview;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class MovableCursorAdapter<VH extends RecyclerView.ViewHolder> extends DraggableView.Adapter<VH> {
    private int mRowIdColumn;
    private Cursor mCursor;

    public MovableCursorAdapter() {
        this(null);
    }

    public MovableCursorAdapter(Cursor cursor) {
        if (cursor == null) {
            mCursor = new NullCurosr();
        } else {
            mCursor = cursor;
        }
        mRowIdColumn = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
        setHasStableIds(true);
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
