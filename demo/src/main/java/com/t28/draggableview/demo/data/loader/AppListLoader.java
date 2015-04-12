package com.t28.draggableview.demo.data.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.t28.draggableview.demo.data.model.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppListLoader extends AsyncTaskLoader<List<App>> {
    private final PackageManager mPackageManager;

    public AppListLoader(Context context) {
        super(context);
        // 引数で与えられたContextがApplicationContext出ない可能性があるためgetContextを利用する。
        mPackageManager = getContext().getPackageManager();
    }

    @Override
    public List<App> loadInBackground() {
        final List<ApplicationInfo> infos = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        if (infos == null || infos.size() == 0) {
            return Collections.emptyList();
        }

        final List<App> apps = new ArrayList<>();
        for (ApplicationInfo info : infos) {
            final App.Builder builder = new App.Builder();
            builder.setPackageName(info.packageName)
                    .setName(info.loadLabel(mPackageManager).toString())
                    .setIcon(info.loadIcon(mPackageManager));
            apps.add(builder.build());
        }
        return apps;
    }
}
