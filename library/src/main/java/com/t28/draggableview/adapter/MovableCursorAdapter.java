package com.t28.draggableview.adapter;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;

import com.t28.draggableview.DraggableView;
import com.t28.draggableview.NullCursor;

public abstract class MovableCursorAdapter<VH extends RecyclerView.ViewHolder> extends DraggableView.Adapter<VH> {
    private static final int NO_ITEM_COUNT = 0;

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
        mIsDataValid = true;
        mRowIdColumn = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (!mIsDataValid) {
            throw new IllegalStateException("Invalid cursor:" + mCursor);
        }

        final int cursorPosition = getCursorPosition(position);
        if (!mCursor.moveToPosition(cursorPosition)) {
            throw new IllegalStateException("Cannot move to position:" + cursorPosition);
        }
        onBindViewHolder(holder, mCursor);
    }

    @Override
    public long getItemId(int position) {
        if (!mIsDataValid) {
            return DraggableView.NO_ID;
        }

        if (mRowIdColumn < 0) {
            return DraggableView.NO_ID;
        }

        final int cursorPosition = getCursorPosition(position);
        if (!mCursor.moveToPosition(cursorPosition)) {
            return DraggableView.NO_ID;
        }
        return mCursor.getLong(mRowIdColumn);
    }

    @Override
    public int getItemCount() {
        if (!mIsDataValid) {
            return NO_ITEM_COUNT;
        }
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

        mIsDataValid = true;
        mRowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
        mCursor = newCursor;
        mPositionMap.clear();

        notifyDataSetChanged();
        return oldCursor;
    }

    protected abstract void onBindViewHolder(VH holder, Cursor cursor);

    protected void onContentChanged() {
    }

    protected boolean isValidPosition(int position) {
        return position >= 0 && position < mCursor.getCount();
    }

    @NonNull
    protected Cursor getCursor() {
        return mCursor;
    }

    protected int getCursorPosition(int layoutPosition) {
        return mPositionMap.get(layoutPosition, layoutPosition);
    }

    protected void setCursorPosition(int layoutPosition, int cursorPosition) {
        mPositionMap.append(layoutPosition, cursorPosition);
    }

    private void moveLower(int layoutPosition1, int layoutPosition2) {
        for (int position = layoutPosition1; position < layoutPosition2; position++) {
            swap(position, position + 1);
        }
    }

    private void moveUpper(int layoutPosition1, int layoutPosition2) {
        for (int position = layoutPosition1; position > layoutPosition2; position--) {
            swap(position, position - 1);
        }
    }

    private void swap(int layoutPosition1, int layoutPosition2) {
        final int cursorPosition1 = getCursorPosition(layoutPosition1);
        final int cursorPosition2 = getCursorPosition(layoutPosition2);
        setCursorPosition(layoutPosition1, cursorPosition2);
        setCursorPosition(layoutPosition2, cursorPosition1);
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
