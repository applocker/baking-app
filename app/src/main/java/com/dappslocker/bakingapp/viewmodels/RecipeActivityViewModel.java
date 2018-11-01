package com.dappslocker.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dappslocker.bakingapp.idlingResource.SimpleIdlingResource;
import com.dappslocker.bakingapp.repository.RecipesRepository;
import com.dappslocker.bakingapp.datasource.database.LocalRecipesDataSource;
import com.dappslocker.bakingapp.datasource.network.RemoteRecipesDataSource;
import com.dappslocker.bakingapp.model.Recipe;

import java.util.List;

/*public class RecipeActivityViewModel extends AndroidViewModel {*/
public class RecipeActivityViewModel extends ViewModel {
    private static final String TAG = RecipeActivityViewModel.class.getSimpleName();
    private static RecipesRepository mRecipesRepository;
    private static MutableLiveData<List<Recipe>> mRecipes;

    public RecipeActivityViewModel(@NonNull Application application, @Nullable SimpleIdlingResource simpleIdlingResource) {
        //super(application);
        RemoteRecipesDataSource remoteRecipesDataSource = RemoteRecipesDataSource.getInstance(simpleIdlingResource);
        LocalRecipesDataSource localRecipesDataSource = LocalRecipesDataSource.getInstance(application);
        mRecipesRepository = RecipesRepository.getInstance(application,remoteRecipesDataSource,localRecipesDataSource);
        mRecipesRepository.refreshRecipes();
        mRecipes = mRecipesRepository.getRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public void refreshData() {
        mRecipesRepository.refreshRecipes();
        mRecipes = mRecipesRepository.getRecipes();
    }
}
