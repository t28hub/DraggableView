package com.t28.draggableview;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;

public abstract class MovableCursorAdapter<VH extends RecyclerView.ViewHolder> extends DraggableView.Adapter<VH> {
    private int mRowIdColumn;
    private Cursor mCursor;

    public MovableCursorAdapter() {
        this(null);
    }

    public MovableCursorAdapter(Cursor cursor) {
        if (cursor == null) {
            mCursor = new NullCursor();
        } else {
            mCursor = cursor;
        }
        mRowIdColumn = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        if (mCursor.moveToPosition(position)) {
            return DraggableView.NO_ID;
        }
        return mCursor.getLong(mRowIdColumn);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public boolean move(int position1, int position2) {
        return false;
    }
}
