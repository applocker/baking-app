package com.dappslocker.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.dappslocker.bakingapp.model.Recipe;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity  {
    private static final String TAG = "RecipeDetailActivity";
    DetailListFragment detailListFragment;
    private static final String RECIPE_ID = "recipe_id";
    private  Recipe mRecipeAtPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        detailListFragment = (DetailListFragment) getSupportFragmentManager().findFragmentById(R.id.detail_list_fragment);
        Intent intent = getIntent();
        if(intent.hasExtra(RECIPE_ID)){
            int position = intent.getIntExtra(RECIPE_ID,0);
            mRecipeAtPosition = RecipeAdapter.getRecipeAtPosition(position);
            Log.d(TAG,"inside onCreate(): retrieved " + mRecipeAtPosition.getName());
            detailListFragment.setRecipe(mRecipeAtPosition);
        }

    }

    Recipe getRecipeAtPosition() {
        return mRecipeAtPosition;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }


}
