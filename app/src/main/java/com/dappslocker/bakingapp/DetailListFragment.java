package com.dappslocker.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dappslocker.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailListFragment extends Fragment implements RecipeDetailAdapter.RecipeDetailAdapterOnClickHandler {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view_recipe_detail)
    RecyclerView mRecylerView;

    private  RecipeDetailAdapter mRecipeDetailAdapter;
    private static final String TAG = "DetailListFragment";
    private OnRecipeDetailClickedListener mRecipeDetailClickedListener;


    public DetailListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RecipeDetailActivity) {
             if (context instanceof OnRecipeDetailClickedListener) {
                mRecipeDetailClickedListener = (OnRecipeDetailClickedListener)context;
            }
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
        mRecipeDetailAdapter = new RecipeDetailAdapter(null,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecylerView.setLayoutManager(layoutManager);
        mRecylerView.setAdapter(mRecipeDetailAdapter);
        //create an item decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecylerView.getContext(),
                layoutManager.getOrientation()
        );
        //set a drawable for this item decoration
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(
                getContext(),
                R.drawable.recyclerview_divider_white
        ));
        //now add the divider to the recyclerview
        mRecylerView.addItemDecoration(dividerItemDecoration);
        setRetainInstance(true);

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        if(recipe == null){
            Toast.makeText(getContext(),"There was an error loading the recipe",Toast.LENGTH_SHORT).show();
        }
        mRecipeDetailAdapter.setRecipe(recipe);
    }

    @Override
    public void onClick(int position) {
        if(mRecipeDetailClickedListener != null){
            mRecipeDetailClickedListener.OnRecipeDetailClicked(position,mRecipeDetailAdapter.getRecipe());
        }
    }

    /**
     * This interface is implemented by {@link com.dappslocker.bakingapp.RecipeDetailActivity}
     * to allow an interaction in this fragment to be communicated to the activity
     */
    public interface OnRecipeDetailClickedListener {
        void OnRecipeDetailClicked(int position, Recipe recipe);
    }

    public void prevNextClicked(int position){
        mRecipeDetailAdapter.prevNextClicked(++position);
    }
}
