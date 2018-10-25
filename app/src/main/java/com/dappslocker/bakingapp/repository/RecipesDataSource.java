package com.dappslocker.bakingapp.repository;

import android.support.annotation.NonNull;

import com.dappslocker.bakingapp.repository.DataSource;

public interface RecipesDataSource {
    void getRecipes();
    void refreshRecipes();
    void setLoadRecipeCallBack(@NonNull DataSource.LoadRecipeCallback callBackallback);
}