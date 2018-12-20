package com.dappslocker.bakingapp.repository;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.DataSourceUtils.DataSourceIdentifers;

import android.arch.lifecycle.LiveData;

import java.util.List;


@SuppressWarnings("ALL")
public interface DataSource {

    interface LoadRecipeCallback {

        void onRecipeLoaded(List<Recipe> recipes, DataSourceIdentifers dataSourceIdentifier);

        void onRecipeLoaded(Recipe recipe, DataSourceIdentifers dataSourceIdentifier);

        void onDataNotAvailable(DataSourceIdentifers dataSourceIdentifier);
    }

    LiveData<List<Recipe>> getRecipes();

    void refreshRecipes();


}
