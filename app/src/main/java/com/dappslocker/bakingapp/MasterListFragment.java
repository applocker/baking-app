package com.dappslocker.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dappslocker.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRecipeClickedListener} interface
 * to handle interaction events.
 */
public class MasterListFragment extends Fragment implements  RecipeAdapter.RecipeAdapterOnClickHandler {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view_recipe)
    RecyclerView mRecylerGridView;

    private OnRecipeClickedListener mListener;
    private static final String TAG = "MasterListFragment";
    private  RecipeAdapter mRecipeAdapter;
    private RecipeActivity mRecipeActivityInstance;
    private int spanCount = 1;
    public MasterListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.main_fragment_recipe, container, false);
        ButterKnife.bind(this,rootView);

        // Create the adapter
        mRecipeAdapter = new RecipeAdapter(new ArrayList<Recipe>(), this);
        setupSpanCount();
        GridLayoutManager  layoutManager = new GridLayoutManager(mRecipeActivityInstance,spanCount,
                GridLayoutManager.VERTICAL,false) ;
        //layoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false) ;
        mRecylerGridView.setLayoutManager(layoutManager);
        // Set the adapter on the GridView
        mRecylerGridView.setAdapter(mRecipeAdapter);

        // Return the root view
        return rootView;
    }

    private void setupSpanCount() {
        boolean isDeviceTablet = getResources().getBoolean(R.bool.isTablet);
        if (isDeviceTablet) {
            spanCount = 3;
        } else {
            spanCount = 1;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeActivity) {
            mRecipeActivityInstance = (RecipeActivity)context;
            if (context instanceof OnRecipeClickedListener) {
                mListener = (OnRecipeClickedListener) context;
            }
        }
         else {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     *  @param position the recipe position that was clicked
     */
    @Override
    public void onClick(int position) {
        //get the recipe id for the recipe at this position
        Recipe recipe = mRecipeAdapter.getRecipeAtPosition(position);
        mListener.onRecipeClicked(recipe);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), BakingAppWidgetProvider.class));
        BakingAppWidgetProvider.setRecipe(recipe,getContext(),appWidgetManager,appWidgetIds);
    }

    public void setRecipeList(List<Recipe> recipes) {
        mRecipeAdapter.setRecipeList(recipes);
    }

    /**
     * This interface is implemented by {@link com.dappslocker.bakingapp.RecipeActivity}
     * to allow an interaction in this fragment to be communicated to the activity
     */
    public interface OnRecipeClickedListener {
        void onRecipeClicked(Recipe recipe);
    }
}
