package com.t28.draggableview.demo.data.adapter;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Browser;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.t28.draggableview.DraggableView;
import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.data.model.Bookmark;
import com.t28.draggableview.demo.tool.DrawableFactory;

public class BookmarkAdapter extends DraggableView.Adapter<BookmarkAdapter.GridViewHolder> {
    private int mRowIdColumnIndex;
    private Cursor mCursor;

    public BookmarkAdapter() {
        mCursor = new MatrixCursor(new String[0]);
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.layout_list_item, parent, false);
        return new GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Failed to move position:" + position);
        }

        final Resources resources = holder.itemView.getResources();
        final Bookmark.Builder builder = new Bookmark.Builder()
                .setTitle(readTitle())
                .setUrl(readUrl())
                .setFavicon(readFavicon(resources))
                .setThumbnail(readThumbnail(resources));
        final Bookmark bookmark = builder.build();
        holder.bind(bookmark);
    }

    @Override
    public long getItemId(int position) {
        return mCursor.getLong(mRowIdColumnIndex);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public boolean move(int position1, int position2) {
        return false;
    }

    public void changeCursor(Cursor cursor) {
        mRowIdColumnIndex = cursor.getColumnIndexOrThrow(Browser.BookmarkColumns._ID);
        mCursor = cursor;
        notifyDataSetChanged();
    }

    private String readTitle() {
        final int columnIndex = mCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.TITLE);
        return mCursor.getString(columnIndex);
    }

    private Uri readUrl() {
        final int columnIndex = mCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.URL);
        return Uri.parse(mCursor.getString(columnIndex));
    }

    private Drawable readFavicon(Resources resources) {
        final int columnIndex = mCursor.getColumnIndexOrThrow(Browser.BookmarkColumns.FAVICON);
        final byte[] data = mCursor.getBlob(columnIndex);
        if (data == null || data.length == 0) {
            return null;
        }
        return DrawableFactory.decodeByteArray(resources, data);
    }

    private Drawable readThumbnail(Resources resources) {
        final int columnIndex = mCursor.getColumnIndexOrThrow("thumbnail");
        final byte[] data = mCursor.getBlob(columnIndex);
        if (data == null || data.length == 0) {
            return null;
        }
        return DrawableFactory.decodeByteArray(resources, data);
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mIconView;
        private final TextView mPrimaryTextView;
        private final TextView mSecondaryTextView;

        public GridViewHolder(View itemView) {
            super(itemView);
            mIconView = (ImageView) itemView.findViewById(R.id.linear_layout_item_icon);
            mPrimaryTextView = (TextView) itemView.findViewById(R.id.linear_layout_item_primary_text);
            mSecondaryTextView = (TextView) itemView.findViewById(R.id.linear_layout_item_secondary_text);
        }

        public void bind(Bookmark bookmark) {
            mIconView.setImageDrawable(bookmark.getThumbnail());
            mPrimaryTextView.setText(bookmark.getTitle());
            mSecondaryTextView.setText(bookmark.getUrl().toString());
        }
    }
}
