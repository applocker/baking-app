package com.dappslocker.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.viewmodels.AddRecipeDetailViewModelFactory;
import com.dappslocker.bakingapp.viewmodels.RecipeDetailActivityViewModel;

public class RecipeDetailActivity extends AppCompatActivity implements DetailListFragment.OnRecipeDetailClickedListener  {
    private static final String TAG = "RecipeDetailActivity";
    DetailListFragment detailListFragment;
    private static final String RECIPE_ID = "recipe_id";
    private static final String RECIPE_NAME = "recipe_title";
    private static final String RECIPE = "recipe";
    private RecipeDetailActivityViewModel viewModel;
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
        if(intent.hasExtra(RECIPE_ID)&&intent.hasExtra(RECIPE_NAME)){
            Integer recipeId = intent.getIntExtra(RECIPE_ID,0);
            actionBar.setTitle(intent.getStringExtra(RECIPE_NAME));
            setupViewModel(recipeId);
        }

    }

    private void setupViewModel(Integer reciPeId) {
        AddRecipeDetailViewModelFactory factory = new AddRecipeDetailViewModelFactory(getApplication(),reciPeId);
        viewModel = ViewModelProviders.of(this,factory).get(RecipeDetailActivityViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                detailListFragment.setRecipe(recipe);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return viewModel.getIdlingResource();
    }

    @Override
    public void OnRecipeDetailClicked(int position, Recipe recipe) {
        if(position == 0){
            //display the list of ingridents
            IngredientsFrament ingridentsFrament = new IngredientsFrament();
            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPE,recipe);
            ingridentsFrament.setArguments(bundle);
            ingridentsFrament.show(getSupportFragmentManager(),"Ingridents Fragment");
        }
        else{
            //launch the video player
            Toast.makeText(this,"About to play Exoplayer",Toast.LENGTH_SHORT).show();
        }
    }


}
