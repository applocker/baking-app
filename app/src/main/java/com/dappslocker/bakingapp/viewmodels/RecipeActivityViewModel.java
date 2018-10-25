package com.dappslocker.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dappslocker.bakingapp.repository.RecipesRepository;
import com.dappslocker.bakingapp.datasource.database.LocalRecipesDataSource;
import com.dappslocker.bakingapp.datasource.network.RemoteRecipesDataSource;
import com.dappslocker.bakingapp.model.Recipe;

import java.util.List;

public class RecipeActivityViewModel extends AndroidViewModel {
    private static final String TAG = RecipeActivityViewModel.class.getSimpleName();
    private static RecipesRepository mRecipesRepository;
    private final MutableLiveData<List<Recipe>> mRecipes;

    public RecipeActivityViewModel(@NonNull Application application) {
        super(application);
        RemoteRecipesDataSource remoteRecipesDataSource = RemoteRecipesDataSource.getInstance();
        LocalRecipesDataSource localRecipesDataSource = LocalRecipesDataSource.getInstance(application);
        mRecipesRepository = RecipesRepository.getInstance(application,remoteRecipesDataSource,localRecipesDataSource);
        mRecipesRepository.refreshRecipes();
        mRecipes = mRecipesRepository.getRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

}
