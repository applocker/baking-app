package com.dappslocker.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dappslocker.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailListFragment extends Fragment {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view_recipe_detail)
    RecyclerView mRecylerGridView;

    private  RecipeDetailAdapter mRecipeDetailAdapter;
    private static final String TAG = "DetailListFragment";
    private RecipeDetailActivity mRecipeDetailActivity;

    public DetailListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RecipeDetailActivity) {
            mRecipeDetailActivity = (RecipeDetailActivity)context;
        }
        else {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.detail_fragment_recipe, container, false);
        ButterKnife.bind(this,rootView);
        // Create the adapter
        mRecipeDetailAdapter = new RecipeDetailAdapter(new Recipe());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecylerGridView.setLayoutManager(layoutManager);
        mRecylerGridView.setAdapter(mRecipeDetailAdapter);
        //create an item decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecylerGridView.getContext(),
                layoutManager.getOrientation()
        );
        //set a drawable for this item decoration
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(
                getContext(),
                R.drawable.recyclerview_divider_white
        ));
        //now add the divider to the recyclerview
        mRecylerGridView.addItemDecoration(dividerItemDecoration);

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        mRecipeDetailAdapter.setRecipe(recipe);
    }
}
