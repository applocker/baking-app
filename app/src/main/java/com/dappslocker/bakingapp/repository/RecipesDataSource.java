package com.dappslocker.bakingapp.repository;

import android.support.annotation.NonNull;

import com.dappslocker.bakingapp.repository.DataSource;
@SuppressWarnings("unused")
public interface RecipesDataSource {
    void getRecipes();
    void refreshRecipes();
    void setLoadRecipeCallBack(@NonNull DataSource.LoadRecipeCallback callBackallback);
}