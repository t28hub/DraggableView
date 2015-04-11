package com.t28.draggableview.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t28.draggableview.DraggableView;
import com.t28.draggableview.demo.R;

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
}
