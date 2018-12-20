package com.dappslocker.bakingapp.datasource.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dappslocker.bakingapp.model.Recipe;


import java.util.List;

/**
 * Data access object for Recipes
 */
@Dao
@SuppressWarnings("unused")
public interface RecipesDao {
    /**
     * Select all recipes from the Recipies table.
     *
     * @return all Recipies.
     */
    @Query("SELECT * FROM recipes")
    List<Recipe> getRecipes();

    /**
     * Insert a recipe in the database. If the recipe already exists, replace it.
     *
     * @param recipe the recipe to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);

    /**
     * Update a Recipe.
     *
     * @param recipe Recipe to be updated
     * @return the number of Recipes updated. This should always be 1.
     */
    @Update
    int updateRecipe(Recipe recipe);


    /**
     * Delete a Recipe by id.
     *
     * @return the number of Recipes deleted. This should always be 1.
     */
    @Query("DELETE FROM recipes WHERE id = :recipeId")
    int deleteRecipeById(int recipeId);

    /**
     * Delete all Recipes.
     */
    @Query("DELETE FROM recipes")
    void deleteRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipe getRecipe(int id);
}
