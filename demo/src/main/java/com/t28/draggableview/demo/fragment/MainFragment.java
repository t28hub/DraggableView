package com.t28.draggableview.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.data.adapter.FragmentAdapter;
import com.t28.draggableview.demo.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        setUpViews(view);
        return view;
    }

    private void setUpViews(View view) {
        final FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager());
        adapter.addAll(createFactories());

        final ViewPager pager = (ViewPager) view.findViewById(R.id.main_view_pager);
        pager.setAdapter(adapter);

        final SlidingTabLayout slidingTab = (SlidingTabLayout) view.findViewById(R.id.main_sliding_tab);
        slidingTab.setViewPager(pager);
    }

    private List<FragmentAdapter.FragmentFactory> createFactories() {
        final List<FragmentAdapter.FragmentFactory> factories = new ArrayList<>();
        factories.add(new LinearLayoutFragment.Factory(getString(R.string.title_linear)));
        factories.add(new GridLayoutFragment.Factory(getString(R.string.title_grid)));
        return factories;
    }
}
