package com.dappslocker.bakingapp.datasource;

import com.dappslocker.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by Tiwuya on 10,October,2018
 */
public interface RecipeDataSource {
    List<Recipe> getRecipes();
}
