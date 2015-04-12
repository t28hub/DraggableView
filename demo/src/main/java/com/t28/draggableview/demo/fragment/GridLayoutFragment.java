package com.t28.draggableview.demo.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t28.draggableview.DraggableView;
import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.data.adapter.BookmarkAdapter;
import com.t28.draggableview.demo.data.adapter.FragmentAdapter;

public class GridLayoutFragment extends Fragment {
    private static final int SPAN_COUNT = 2;

    private BookmarkAdapter mBookmarkAdapter;

    public GridLayoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DraggableView view = (DraggableView) inflater.inflate(R.layout.fragment_grid_layout, container, false);
        view.setHasFixedSize(true);
        view.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBookmarkAdapter = new BookmarkAdapter();
        getDraggableView().setAdapter(mBookmarkAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: CursorLoader化
        String[] projection = new String[]{
                Browser.BookmarkColumns._ID,
                Browser.BookmarkColumns.FAVICON,
                Browser.BookmarkColumns.TITLE,
                Browser.BookmarkColumns.URL,
                "thumbnail"
        };
        Cursor cursor =
                getActivity().getContentResolver().query(
                        Browser.BOOKMARKS_URI, projection, null, null, null);
        mBookmarkAdapter.changeCursor(cursor);
        Log.d("TAG", "cursor:" + cursor.getCount());
    }

    private DraggableView getDraggableView() {
        return (DraggableView) getView();
    }

    public static final class Factory implements FragmentAdapter.FragmentFactory {
        private final CharSequence mTitle;

        public Factory(CharSequence title) {
            mTitle = title;
        }

        @Override
        public Fragment create() {
            return new GridLayoutFragment();
        }

        @Override
        public CharSequence getTitle() {
            return mTitle;
        }
    }
}
