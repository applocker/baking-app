package com.dappslocker.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.BakingAppUtils;
import com.dappslocker.bakingapp.viewmodels.AddRecipeDetailViewModelFactory;
import com.dappslocker.bakingapp.viewmodels.RecipeDetailActivityViewModel;

public class RecipeDetailActivity extends AppCompatActivity implements DetailListFragment.OnRecipeDetailClickedListener,
         StepDetailFragment.PlayVideoLandCliCkListener,StepDetailFragment.ActionBarListener, StepDetailFragment.PrevNextClickListener {

    private DetailListFragment detailListFragment;

    private RecipeDetailActivityViewModel viewModel;
    private Integer recipeId;
    private StepDetailFragment stepDetailFragment;
    private boolean isTwoPaneLayout;
    private static Recipe widgetRecipe = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        isTwoPaneLayout = getResources().getBoolean(R.bool.isTablet);
        if(savedInstanceState == null) {
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            FragmentManager fm = getSupportFragmentManager();
            detailListFragment = (DetailListFragment)fm.findFragmentById(R.id.frameLayoutDetailListFragment_container);

            if (detailListFragment == null) {
                detailListFragment = new DetailListFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.frameLayoutDetailListFragment_container, detailListFragment);
                ft.commit();
            }
            Intent intent = getIntent();
            if(intent.hasExtra(BakingAppUtils.RECIPE_ID) && intent.hasExtra(BakingAppUtils.RECIPE_NAME)){
                recipeId = intent.getIntExtra(BakingAppUtils.RECIPE_ID,0);
                //noinspection ConstantConditions
                actionBar.setTitle(intent.getStringExtra(BakingAppUtils.RECIPE_NAME));
                setupViewModel(recipeId);
            }
        }
    }


    private void setupViewModel(Integer recipeId) {
        AddRecipeDetailViewModelFactory factory = new AddRecipeDetailViewModelFactory(getApplication(),recipeId);
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

        if (!isTwoPaneLayout && getSupportFragmentManager().getBackStackEntryCount() >= 1){
            getSupportFragmentManager().popBackStackImmediate();
            setupViewModel(recipeId);
        }
        else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isTwoPaneLayout && getSupportFragmentManager().getBackStackEntryCount() >= 1){
                    getSupportFragmentManager().popBackStackImmediate();
                        setupViewModel(recipeId);
                    return true;
                }
                else{
                    return super.onOptionsItemSelected(item);
                }
            default:
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return viewModel.getIdlingResource();
    }

    @Override
    public void OnRecipeDetailClicked(int position, Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BakingAppUtils.KEY_RECIPE,recipe);
        bundle.putInt(BakingAppUtils.KEY_POSITION,position);
        if(position == 0){
            //display the list of ingredients
            IngredientsFrament ingredientsFrament = new IngredientsFrament();
            ingredientsFrament.setArguments(bundle);
            ingredientsFrament.show(getSupportFragmentManager(),"Ingredients Fragment");
        }
        else{
            String Tag = StepDetailFragment.class.getSimpleName();
            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentByTag(Tag);
            if(stepDetailFragment == null){
                stepDetailFragment = new StepDetailFragment();
            }
            stepDetailFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(isTwoPaneLayout){
                transaction.replace( R.id.frameLayoutStepsDeatilFragment_container, stepDetailFragment);
            }else{
                transaction.replace( R.id.frameLayoutDetailListFragment_container, stepDetailFragment);
            }
            transaction.addToBackStack(Tag);
            transaction.commit();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipeId = savedInstanceState.getInt(BakingAppUtils.RECIPE_ID);
        detailListFragment = (DetailListFragment) getSupportFragmentManager().getFragment(savedInstanceState, BakingAppUtils.KEY_DETAIL_FRAGMENT);
        stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, BakingAppUtils.KEY_STEP_DETAIL_FRAGMENT);
        if(stepDetailFragment == null){
            setupViewModel(recipeId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BakingAppUtils.RECIPE_ID, recipeId);
        getSupportFragmentManager().putFragment(outState, BakingAppUtils.KEY_DETAIL_FRAGMENT,detailListFragment);
        if(stepDetailFragment != null && stepDetailFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, BakingAppUtils.KEY_STEP_DETAIL_FRAGMENT, stepDetailFragment);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onPLayVideoLand(String videoUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(BakingAppUtils.KEY_VIDEO_URL,videoUrl);
        ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
        exoPlayerFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace( R.id.frameLayoutDetailListFragment_container, exoPlayerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().hasExtra(BakingAppUtils.RECIPE_LAUNCED_FROM_WIDGET) &&
                getIntent().getBooleanExtra(BakingAppUtils.RECIPE_LAUNCED_FROM_WIDGET, false)){
            OnRecipeDetailClicked(0, widgetRecipe);
        }
    }

    @Override
    public void prevNextClicked(int position) {
        if(detailListFragment != null){
            detailListFragment.prevNextClicked(position);
        }

    }

    public static void setWidgetRecipe(Recipe widgetRecipe) {
        RecipeDetailActivity.widgetRecipe = widgetRecipe;
    }
}
