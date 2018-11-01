package com.dappslocker.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider.NewInstanceFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dappslocker.bakingapp.idlingResource.SimpleIdlingResource;

public class AddRecipeViewModelFactory extends NewInstanceFactory {
    private Application mApplication;
    private SimpleIdlingResource mIdlingResource;

    public AddRecipeViewModelFactory(@NonNull Application application, @Nullable SimpleIdlingResource simpleIdlingResource ){
        mApplication = application;
        mIdlingResource = simpleIdlingResource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeActivityViewModel(mApplication, mIdlingResource);
    }
}
