package com.t28.draggableview;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;

public abstract class MovableCursorAdapter<VH extends RecyclerView.ViewHolder> extends DraggableView.Adapter<VH> {
    private final SparseIntArray mPositionMap;
    private final ContentObserver mContentObserver;
    private final DataSetObserver mDataSetObserver;

    private boolean mIsDataValid;
    private int mRowIdColumn;
    private Cursor mCursor;

    public MovableCursorAdapter() {
        this(null);
    }

    public MovableCursorAdapter(Cursor cursor) {
        mPositionMap = new SparseIntArray();
        mContentObserver = createContentObserver();
        mDataSetObserver = createDataSetObserver();

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
        if (mRowIdColumn < 0) {
            return DraggableView.NO_ID;
        }

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
        if (!isValidPosition(position1)) {
            throw new IndexOutOfBoundsException("'position1' is out of bounds:" + position1);
        }
        if (!isValidPosition(position2)) {
            throw new IndexOutOfBoundsException("'position2' is out of bounds:" + position2);
        }

        if (position1 == position2) {
            return false;
        }

        if (position1 < position2) {
            moveLower(position1, position2);
        } else {
            moveUpper(position1, position2);
        }
        notifyItemMoved(position1, position2);
        return false;
    }

    public void changeCursor(Cursor newCursor) {
        final Cursor oldCursor = swapCursor(newCursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        final Cursor oldCursor = mCursor;
        if (oldCursor.equals(newCursor)) {
            return null;
        }

        oldCursor.unregisterContentObserver(mContentObserver);
        oldCursor.unregisterDataSetObserver(mDataSetObserver);

        if (newCursor == null) {
            newCursor = new NullCursor();
        }

        newCursor.registerContentObserver(mContentObserver);
        newCursor.registerDataSetObserver(mDataSetObserver);

        mRowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
        mCursor = newCursor;
        notifyDataSetChanged();
        return oldCursor;
    }

    protected void onContentChanged() {
    }

    protected boolean isValidPosition(int position) {
        return position >= 0 && position < mCursor.getCount();
    }

    protected int getCursorPosition(int position) {
        return mPositionMap.get(position, position);
    }

    protected void setCursorPosition(int position, int cursorPosition) {
        mPositionMap.append(position, cursorPosition);
    }

    private void moveLower(int position1, int position2) {
        for (int position = position1; position < position2; position++) {
            swap(position, position + 1);
        }
    }

    private void moveUpper(int position1, int position2) {
        for (int position = position1; position > position2; position--) {
            swap(position, position - 1);
        }
    }

    private void swap(int position1, int position2) {
        final int cursorPosition1 = getCursorPosition(position1);
        final int cursorPosition2 = getCursorPosition(position2);
        setCursorPosition(position1, cursorPosition2);
        setCursorPosition(position2, cursorPosition1);
    }

    private ContentObserver createContentObserver() {
        return new ContentObserver(new Handler()) {
            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange) {
                onContentChanged();
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                onContentChanged();
            }
        };
    }

    private DataSetObserver createDataSetObserver() {
        return new DataSetObserver() {
            @Override
            public void onChanged() {
                mIsDataValid = true;
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                mIsDataValid = false;
                notifyDataSetChanged();
            }
        };
    }
}
