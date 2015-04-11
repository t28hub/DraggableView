package com.t28.draggableview.demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final List<FragmentFactory> mFactories;

    public FragmentAdapter(FragmentManager manager) {
        super(manager);
        mFactories = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return getFactory(position).create();
    }

    @Override
    public int getCount() {
        return mFactories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getFactory(position).getTitle();
    }

    public void addAll(List<FragmentFactory> factories) {
        if (factories == null || factories.size() == 0) {
            return;
        }
        mFactories.addAll(factories);
        notifyDataSetChanged();
    }

    private FragmentFactory getFactory(int position) {
        return mFactories.get(position);
    }

    public interface FragmentFactory {
        Fragment create();

        CharSequence getTitle();
    }
}
