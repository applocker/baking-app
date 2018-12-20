package com.dappslocker.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dappslocker.bakingapp.datasource.network.GetRecipeDataService;
import com.dappslocker.bakingapp.datasource.network.RetrofitClient;
import com.dappslocker.bakingapp.idlingResource.SimpleIdlingResource;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.BakingAppUtils;
import com.dappslocker.bakingapp.viewmodels.AddRecipeViewModelFactory;
import com.dappslocker.bakingapp.viewmodels.RecipeActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends BaseActivity
        implements  MasterListFragment.OnRecipeClickedListener {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view_recipe)
    RecyclerView mRecylerGridView;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.error_loading_indicator)
    LinearLayout mErrorLoadingRecipes;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.textView_error_loading_message)
    TextView mErrorLoadingMessage;

    private static final String TAG = "RecipeActivity";
    //private static final String RECIPE_ID = "recipe_id";
    //private static final String RECIPE_NAME = "recipe_title";
    private MasterListFragment mMasterFragment;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = getMasterFragmentContainer();
        getLayoutInflater().inflate(R.layout.activity_recipe,frameLayout);
        ButterKnife.bind(this);
        mMasterFragment = (MasterListFragment) getSupportFragmentManager().findFragmentById(R.id.master_list_fragment);
        getIdlingResource();
        setupViewModel();

    }

    private void setupViewModel() {
        startRecipesLoadingIndicator();
        AddRecipeViewModelFactory factory = new AddRecipeViewModelFactory(getApplication(),mIdlingResource);
        RecipeActivityViewModel viewModel = ViewModelProviders.of(this,factory).get(RecipeActivityViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if(recipes!= null){
                    Log.d(TAG,"Recipe count = " + recipes.size());
                    for (Recipe recipe : recipes) {
                        Log.d(TAG,"Recipe Name: " + recipe.getName()+ "\n");
                    }
                    showRecipes();
                }
                else{
                    Log.d(TAG,"There was an errror while retrieving the resource");
                    displayErrorMessage();
                }
                mMasterFragment.setRecipeList(recipes);
            }
        });
    }
    private void startRecipesLoadingIndicator() {
        showRecipes();
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecylerGridView.setVisibility(View.INVISIBLE);
    }
    private void showRecipes() {
        mErrorLoadingRecipes.setVisibility(View.INVISIBLE);
        mRecylerGridView.setVisibility(View.VISIBLE);
    }

    private void displayErrorMessage() {
        mErrorLoadingMessage.setText(getResources().getString(R.string.error_message));
        mRecylerGridView.setVisibility(View.INVISIBLE);
        mErrorLoadingRecipes.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_item_refresh) {
            //reset the adapter data
            mMasterFragment.setRecipeList(null);
            //reload data
            startRecipesLoadingIndicator();
            RecipeActivityViewModel viewModel = ViewModelProviders.of(this).get(RecipeActivityViewModel.class);
            viewModel.refreshData();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Log.d(TAG,"onClick starting detail activity ...");
        Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
        intent.putExtra(BakingAppUtils.RECIPE_ID,recipe.getId());
        intent.putExtra(BakingAppUtils.RECIPE_NAME,recipe.getName());
        this.startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
