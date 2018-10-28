package com.dappslocker.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dappslocker.bakingapp.datasource.network.GetRecipeDataService;
import com.dappslocker.bakingapp.datasource.network.RetrofitClient;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.viewmodels.RecipeActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecipeAdapter.RecipeAdapterOnClickHandler {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view) RecyclerView mRecylerGridView;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.nav_view)  NavigationView  navigationView;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.toolbar) Toolbar toolbar;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.pb_loading_indicator)  ProgressBar mLoadingIndicator;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.error_loading_indicator)   LinearLayout mErrorLoadingRecipes;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.textView_error_loading_message)   TextView mErrorLoadingMessage;



    private static final String TAG = "RecipeActivity";
    private  RecipeAdapter mRecipeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        mRecipeAdapter = new RecipeAdapter(new ArrayList<Recipe>(),this);
        mRecylerGridView.setAdapter(mRecipeAdapter);
        GridLayoutManager layoutManager;
        //Todo: change grid span based on device and configuration
        layoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false) ;
        mRecylerGridView.setLayoutManager(layoutManager);
        //testRetrofit();
        setupViewModel();
    }

    private void setupViewModel() {
        //Completed: Add a progress indicator before fetching startRecipeLoadingIndicator()
        startRecipesLoadingIndicator();
        RecipeActivityViewModel viewModel = ViewModelProviders.of(this).get(RecipeActivityViewModel.class);
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
                mRecipeAdapter.setRecipeList(recipes);
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
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            mRecipeAdapter.setRecipeList(null);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Todo: customise template c
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void testRetrofit() {
        GetRecipeDataService service = RetrofitClient.getRetrofitInstance().create(GetRecipeDataService.class);
        Call<ArrayList<Recipe>> call =
                service.getRecipies();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                if(response.isSuccessful()){
                    displayUserReviewListResponseData(response.body());
                }
                else{
                    displayUserReviewListResponseData(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                Log.d(TAG,"inside onFailure(): network error" );
                displayUserReviewListResponseData(null);
            }
        });

    }

    private void displayUserReviewListResponseData(ArrayList<Recipe> recipes) {
        if (recipes != null) {
            Log.d(TAG,"Recipe count = " + recipes.size());
            for (Recipe recipe : recipes) {
                Log.d(TAG,"Recipe Name: " + recipe.getName()+ "\n");
            }
        } else {
            Log.d(TAG,"There was an errror while retrieving the resource");
        }
    }

    @Override
    public void onClick(int position) {
        //Todo: start recipe detail activity
        Log.d(TAG,"onClick starting detail activity ...");

    }
}
