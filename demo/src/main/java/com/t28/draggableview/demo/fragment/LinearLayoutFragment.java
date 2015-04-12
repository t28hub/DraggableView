package com.t28.draggableview.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t28.draggableview.DraggableView;
import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.data.adapter.AppAdapter;
import com.t28.draggableview.demo.data.adapter.FragmentAdapter;
import com.t28.draggableview.demo.data.loader.AppListLoader;
import com.t28.draggableview.demo.data.model.App;

import java.util.List;

public class LinearLayoutFragment extends Fragment {
    private AppAdapter mAdapter;
    private LoaderManager.LoaderCallbacks<List<App>> mAppListCallback;

    public LinearLayoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DraggableView view = (DraggableView) inflater.inflate(R.layout.fragment_linear_layout, container, false);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new AppAdapter();
        getDraggableView().setAdapter(mAdapter);

        mAppListCallback = createAppListCallback();
        getLoaderManager().initLoader(0, null, mAppListCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLoaderManager().destroyLoader(0);
    }

    private DraggableView getDraggableView() {
        return (DraggableView) getView();
    }

    private void onChanged(List<App> newApps) {
        mAdapter.changeApps(newApps);
    }

    private LoaderManager.LoaderCallbacks<List<App>> createAppListCallback() {
        return new LoaderManager.LoaderCallbacks<List<App>>() {
            @Override
            public Loader<List<App>> onCreateLoader(int id, Bundle args) {
                return new AppListLoader(getActivity());
            }

            @Override
            public void onLoadFinished(Loader<List<App>> loader, List<App> data) {
                onChanged(data);
            }

            @Override
            public void onLoaderReset(Loader<List<App>> loader) {
                onChanged(null);
            }
        };
    }

    public static final class Factory implements FragmentAdapter.FragmentFactory {
        private final CharSequence mTitle;

        public Factory(CharSequence title) {
            mTitle = title;
        }

        @Override
        public Fragment create() {
            return new LinearLayoutFragment();
        }

        @Override
        public CharSequence getTitle() {
            return mTitle;
        }
    }
}
