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
import android.widget.FrameLayout;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.viewmodels.AddRecipeDetailViewModelFactory;
import com.dappslocker.bakingapp.viewmodels.RecipeDetailActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements DetailListFragment.OnRecipeDetailClickedListener{
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.frameLayoutDetailListFragment_container)
    FrameLayout mFrameDetailListFragment;

    private static final String TAG = "RecipeDetailActivity";
    DetailListFragment detailListFragment;
    private static final String RECIPE_ID = "recipe_id";
    private static final String RECIPE_NAME = "recipe_title";
    private static final String KEY_RECIPE = "recipe";
    private static final String KEY_POSITION = "position";
    private static final String KEY_DETAIL_FRAGMENT = "detail_fragment";
    private static final String KEY_STEP_DETAIL_FRAGMENT = "step_detail_fragment";
    private RecipeDetailActivityViewModel viewModel;
    private Integer recipeId;
    private StepDetailFragment stepDetailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
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
            if(intent.hasExtra(RECIPE_ID)&&intent.hasExtra(RECIPE_NAME)){
                recipeId = intent.getIntExtra(RECIPE_ID,0);
                actionBar.setTitle(intent.getStringExtra(RECIPE_NAME));
                setupViewModel(recipeId);
            }
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
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            getSupportFragmentManager().popBackStackImmediate();
            setupViewModel(recipeId);
        }
        else {
            finish();
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
        bundle.putParcelable(KEY_RECIPE,recipe);
        bundle.putInt(KEY_POSITION,position);
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
            transaction.replace( R.id.frameLayoutDetailListFragment_container, stepDetailFragment);
            transaction.addToBackStack(Tag);
            transaction.commit();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipeId = savedInstanceState.getInt(RECIPE_ID);
        detailListFragment = (DetailListFragment) getSupportFragmentManager().getFragment(savedInstanceState,KEY_DETAIL_FRAGMENT);
        stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState,KEY_STEP_DETAIL_FRAGMENT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID, recipeId);
        getSupportFragmentManager().putFragment(outState,KEY_DETAIL_FRAGMENT,detailListFragment);
        getSupportFragmentManager().putFragment(outState,KEY_STEP_DETAIL_FRAGMENT,stepDetailFragment);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() == 1){
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
}
