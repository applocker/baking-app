package com.dappslocker.bakingapp.repository;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.DataSourceUtils.DataSourceIdentifers;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;


public interface DataSource {

    interface LoadRecipeCallback {

        void onRecipeLoaded(List<Recipe> recipes, DataSourceIdentifers dataSourceIdentifier);

        void onDataNotAvailable(DataSourceIdentifers dataSourceIdentifier);
    }

    LiveData<List<Recipe>> getRecipes();

    void refreshRecipes();


}
