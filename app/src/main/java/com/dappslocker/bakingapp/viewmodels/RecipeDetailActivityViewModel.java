package com.dappslocker.bakingapp.viewmodels;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.dappslocker.bakingapp.datasource.database.LocalRecipesDataSource;
import com.dappslocker.bakingapp.datasource.network.RemoteRecipesDataSource;
import com.dappslocker.bakingapp.idlingResource.SimpleIdlingResource;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.repository.RecipesRepository;


public class RecipeDetailActivityViewModel extends ViewModel {
    private static RecipesRepository mRecipesRepository;
    private LiveData<Recipe> recipe;
    private SimpleIdlingResource mIdlingResource;


    public RecipeDetailActivityViewModel(@NonNull Application application, Integer recipeId) {
        RemoteRecipesDataSource remoteRecipesDataSource = RemoteRecipesDataSource.getInstance(null);
        LocalRecipesDataSource localRecipesDataSource = LocalRecipesDataSource.getInstance(application);
        mRecipesRepository = RecipesRepository.getInstance(application,remoteRecipesDataSource,localRecipesDataSource);
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        recipe = mRecipesRepository.getRecipe(recipeId, mIdlingResource);
    }

    public SimpleIdlingResource getIdlingResource() {
        return mIdlingResource;
    }


    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
}
