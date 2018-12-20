package com.dappslocker.bakingapp.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.dappslocker.bakingapp.datasource.database.LocalRecipesDataSource;
import com.dappslocker.bakingapp.idlingResource.SimpleIdlingResource;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.DataSourceUtils.DataSourceIdentifers;

import java.util.List;

@SuppressWarnings("unused")
public class RecipesRepository implements DataSource,DataSource.LoadRecipeCallback {

    private volatile static RecipesRepository INSTANCE = null;

    private RecipesDataSource mRecipesRemoteDataSource;

    private RecipesDataSource mRecipesLocalDataSource;

    private static final String TAG = "RecipesRepository";

    private MutableLiveData<List<Recipe>> mCachedRecipes;

    private MutableLiveData<Recipe> mMmutableRecipe;


    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * set to true the first time we get the repository instance in the view model and each time a refresh is requested
     */
    private boolean mCacheIsDirty = false;


    // Prevent direct instantiation.
    private RecipesRepository(Context application,RecipesDataSource remote, RecipesDataSource localDatabase){
        mRecipesRemoteDataSource = remote;
        mRecipesRemoteDataSource.setLoadRecipeCallBack(this); //set the call backs
        mRecipesLocalDataSource = localDatabase;
        mRecipesLocalDataSource.setLoadRecipeCallBack(this);  //set the call backs
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
        // Respond immediately with cache if available and not dirty
        if (!mCacheIsDirty && !(mCachedRecipes == null)) {
            return mCachedRecipes;
        }
        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getRecipesFromRemoteDataSource();
        } else {
            getRecipesFromLocalDataSource();
        }
        return mCachedRecipes;
    }

    public LiveData<Recipe> getRecipe(Integer recipeId, @Nullable SimpleIdlingResource idlingResource) {
        mMmutableRecipe = new MutableLiveData<>();
        ((LocalRecipesDataSource)mRecipesLocalDataSource).getRecipe(recipeId,idlingResource);
        return  mMmutableRecipe;
    }

    private void getRecipesFromLocalDataSource() {
        initCacheIfNull();
        mRecipesLocalDataSource.getRecipes();
    }

    private void getRecipesFromRemoteDataSource() {
        initCacheIfNull();
        mRecipesRemoteDataSource.getRecipes();
    }
    private void initCacheIfNull() {
        if(mCachedRecipes == null){
            mCachedRecipes = new MutableLiveData<>();
        }
    }

    /***
     *  @param recipes recipies returned from the datasource
     */
    @Override
    public void onRecipeLoaded(List<Recipe> recipes, DataSourceIdentifers dataSourceIdentifier) {
        refreshCache(recipes);
        if(dataSourceIdentifier == DataSourceIdentifers.NETWORK){
            refreshLocalDataSources(recipes);
        }
    }

    /**
     * @param recipe the recipe returned from the database
     * @param dataSourceIdentifier used to identify the type of the datasouce
     */
    @Override
    public void onRecipeLoaded(Recipe recipe, DataSourceIdentifers dataSourceIdentifier) {
        if(dataSourceIdentifier == DataSourceIdentifers.DATABASE){
            if (mMmutableRecipe != null) {
                mMmutableRecipe.postValue(recipe);
                Log.d(TAG,"inside onRecipeLoaded() recipe returned from database");
            }
        }
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
        ((LocalRecipesDataSource)mRecipesLocalDataSource).deleteAllAndInsert(recipes);
    }

    @Override
    public void refreshRecipes() {
        mCacheIsDirty = true;
    }

    @VisibleForTesting
    @SuppressWarnings("SameReturnValue")
    public static boolean isTestSetup(String testString){
        return true;
    }

    public RecipesDataSource getRecipesRemoteDataSource() {
        return mRecipesRemoteDataSource;
    }

    public void setRecipesRemoteDataSource(RecipesDataSource mRecipesRemoteDataSource) {
        this.mRecipesRemoteDataSource = mRecipesRemoteDataSource;
    }

    public RecipesDataSource getRecipesLocalDataSource() {
        return mRecipesLocalDataSource;
    }

    public void setRecipesLocalDataSource(RecipesDataSource mRecipesLocalDataSource) {
        this.mRecipesLocalDataSource = mRecipesLocalDataSource;
    }

    public boolean isCacheIsDirty() {
        return mCacheIsDirty;
    }
}
