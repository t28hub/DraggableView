package com.t28.draggableview.demo.data.adapter;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Browser;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.t28.draggableview.adapter.MovableCursorAdapter;
import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.data.model.Bookmark;
import com.t28.draggableview.demo.tool.DrawableFactory;

public class BookmarkAdapter extends MovableCursorAdapter<BookmarkAdapter.GridItemViewHolder> {
    public BookmarkAdapter() {
        super();
    }

    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.layout_grid_item, parent, false);
        return new GridItemViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(GridItemViewHolder holder, Cursor cursor) {
        final Resources resources = holder.itemView.getResources();
        final Bookmark.Builder builder = new Bookmark.Builder()
                .setTitle(readTitle())
                .setUrl(readUrl())
                .setFavicon(readFavicon(resources))
                .setThumbnail(readThumbnail(resources));
        final Bookmark bookmark = builder.build();
        holder.bind(bookmark);
    }

    private String readTitle() {
        final Cursor cursor = getCursor();
        final int columnIndex = cursor.getColumnIndexOrThrow(Browser.BookmarkColumns.TITLE);
        return cursor.getString(columnIndex);
    }

    private Uri readUrl() {
        final Cursor cursor = getCursor();
        final int columnIndex = cursor.getColumnIndexOrThrow(Browser.BookmarkColumns.URL);
        return Uri.parse(cursor.getString(columnIndex));
    }

    private Drawable readFavicon(Resources resources) {
        final Cursor cursor = getCursor();
        final int columnIndex = cursor.getColumnIndexOrThrow(Browser.BookmarkColumns.FAVICON);
        final byte[] data = cursor.getBlob(columnIndex);
        if (data == null || data.length == 0) {
            return null;
        }
        return DrawableFactory.decodeByteArray(resources, data);
    }

    private Drawable readThumbnail(Resources resources) {
        final Cursor cursor = getCursor();
        // TODO: thumbnailの定数化
        final int columnIndex = cursor.getColumnIndexOrThrow("thumbnail");
        final byte[] data = cursor.getBlob(columnIndex);
        if (data == null || data.length == 0) {
            return null;
        }
        return DrawableFactory.decodeByteArray(resources, data);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, View view);
    }

    public class GridItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;
        private final TextView mPrimaryTextView;
        private final TextView mSecondaryTextView;

        public GridItemViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.grid_item_image);
            mPrimaryTextView = (TextView) itemView.findViewById(R.id.grid_item_primary_text);
            mSecondaryTextView = (TextView) itemView.findViewById(R.id.grid_item_secondary_text);
        }

        public void bind(Bookmark bookmark) {
            mImageView.setImageDrawable(bookmark.getThumbnail());
            mPrimaryTextView.setText(bookmark.getTitle());
            mSecondaryTextView.setText(bookmark.getUrl().toString());
        }
    }
}
