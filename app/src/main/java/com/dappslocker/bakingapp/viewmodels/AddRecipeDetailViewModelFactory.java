package com.dappslocker.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dappslocker.bakingapp.idlingResource.SimpleIdlingResource;

/**
 * Created by Tiwuya on 13,November,2018
 */
public class AddRecipeDetailViewModelFactory  extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private Integer recipeId;

    public AddRecipeDetailViewModelFactory(@NonNull Application application,@NonNull Integer recipeId){
        mApplication = application;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeDetailActivityViewModel(mApplication, recipeId);
    }
}
