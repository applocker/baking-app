package com.dappslocker.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dappslocker.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class DetailListFragment extends Fragment {
    public DetailListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.detail_fragment_recipe, container, false);

        return rootView;
    }
}
