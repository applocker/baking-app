package com.dappslocker.bakingapp.viewmodels;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.dappslocker.bakingapp.datasource.database.LocalRecipesDataSource;
import com.dappslocker.bakingapp.datasource.network.RemoteRecipesDataSource;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.repository.RecipesRepository;


public class RecipeDetailActivityViewModel extends ViewModel {
    private static RecipesRepository mRecipesRepository;
    private LiveData<Recipe> recipe;

    public RecipeDetailActivityViewModel(@NonNull Application application, Integer recipeId) {
        RemoteRecipesDataSource remoteRecipesDataSource = RemoteRecipesDataSource.getInstance(null);
        LocalRecipesDataSource localRecipesDataSource = LocalRecipesDataSource.getInstance(application);
        mRecipesRepository = RecipesRepository.getInstance(application,remoteRecipesDataSource,localRecipesDataSource);
        recipe = mRecipesRepository.getRecipe(recipeId);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
}
