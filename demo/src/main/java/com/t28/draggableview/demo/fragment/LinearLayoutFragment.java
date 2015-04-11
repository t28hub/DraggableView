package com.t28.draggableview.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t28.draggableview.DraggableView;
import com.t28.draggableview.demo.R;
import com.t28.draggableview.demo.adapter.FragmentAdapter;

public class LinearLayoutFragment extends Fragment {
    public LinearLayoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DraggableView view = (DraggableView) inflater.inflate(R.layout.fragment_linear_layout, container, false);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
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
