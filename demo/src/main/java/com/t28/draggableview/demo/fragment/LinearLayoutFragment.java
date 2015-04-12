package com.t28.draggableview.demo.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t28.draggableview.DraggableView;
import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.data.adapter.AppAdapter;
import com.t28.draggableview.demo.data.adapter.FragmentAdapter;
import com.t28.draggableview.demo.data.model.App;

import java.util.ArrayList;
import java.util.List;

public class LinearLayoutFragment extends Fragment {
    private AppAdapter mAdapter;

    public LinearLayoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DraggableView view = (DraggableView) inflater.inflate(R.layout.fragment_linear_layout, container, false);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new AppAdapter();
        view.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO: サブスレッドに逃す
        final PackageManager manager = getActivity().getPackageManager();
        final List<ApplicationInfo> applications = manager.getInstalledApplications(PackageManager.GET_META_DATA);
        final List<App> apps = new ArrayList<>();
        for (ApplicationInfo info : applications) {
            if (!info.enabled) {
                continue;
            }

            final App.Builder builder = new App.Builder();
            builder.setPackageName(info.packageName)
                    .setName(info.loadLabel(manager).toString())
                    .setIcon(info.loadIcon(manager));

            apps.add(builder.build());
        }
        mAdapter.changeApps(apps);
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
