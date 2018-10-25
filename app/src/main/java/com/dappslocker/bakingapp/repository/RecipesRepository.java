package com.dappslocker.bakingapp.repository;


import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;


import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.DataSourceUtils.DataSourceIdentifers;

import java.util.List;


public class RecipesRepository implements DataSource,DataSource.LoadRecipeCallback {

    private volatile static RecipesRepository INSTANCE = null;

    private RecipesDataSource mRecipesRemoteDataSource;

     //Todo: private RecipesDataSource mRecipesLocalDataSource;

    private static final String TAG = "RecipesRepository";

    MutableLiveData<List<Recipe>> mCachedRecipes;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * set to true the first time we get the repository instance in the view model and each time a refresh is requested
     */
     boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private RecipesRepository(Context application,RecipesDataSource remote, RecipesDataSource localDatabase){
        mRecipesRemoteDataSource = remote;
        mRecipesRemoteDataSource.setLoadRecipeCallBack(this); //set the call backs
        //Todo: mRecipesLocalDataSource = localDatabase;
        //Todo: mRecipesLocalDataSource = setLoadRecipeCallBack(this);  //set the call backs

    }

    /**
     *
     * @param application the application context
     * @param remote the remote data source for recipes
     * @param localDatabase the local data source for recipes
     * @return  an instance of RecipesRepository
     */
    public static RecipesRepository getInstance(Context application, RecipesDataSource remote, RecipesDataSource localDatabase) {
        if (INSTANCE == null) {
            synchronized (RecipesRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipesRepository(application,remote,localDatabase);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public MutableLiveData<List<Recipe>> getRecipes() {
        if(mCachedRecipes == null){
            mCachedRecipes = new MutableLiveData<>();
            mCacheIsDirty = true;
        }
        // Respond immediately with cache if available and not dirty
        if (mCachedRecipes != null && !mCacheIsDirty) {
            return mCachedRecipes;
        }
        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getRecipesFromRemoteDataSource();
        } else {
          //todo: query device database
           Log.d(TAG, "querying device database for recipies");
        }
        return mCachedRecipes;
    }

    private void getRecipesFromRemoteDataSource() {
        mRecipesRemoteDataSource.getRecipes();
    }

    /***
     *  @param recipes recipies returned from the datasource
     */
    @Override
    public void onRecipeLoaded(List<Recipe> recipes, DataSourceIdentifers dataSourceIdentifier) {
        refreshCache(recipes);
        refreshLocalDataSources(recipes);
    }

    /**
     * @param dataSourceIdentifier used to identify the type of the datasouce
     */
    @Override
    public void onDataNotAvailable(DataSourceIdentifers dataSourceIdentifier) {
        if(dataSourceIdentifier == DataSourceIdentifers.DATABASE){
            getRecipesFromRemoteDataSource();
        }else{
            //pass an error message down the line so that an error fetching resource message will be displayed
            mCachedRecipes.setValue(null);
            mCacheIsDirty = true;
        }
    }

    private void refreshCache(List<Recipe> recipes) {
        mCachedRecipes.setValue(recipes);
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSources(List<Recipe> recipes) {
        //Todo: clear the database
        //Todo: insert the new values in the database
    }

    @Override
    public void refreshRecipes() {
        mCacheIsDirty = true;
    }


    public static boolean isTestSetup(String testString){
        return true;
    }


}
