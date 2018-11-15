package com.dappslocker.bakingapp.datasource.database;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dappslocker.bakingapp.idlingResource.SimpleIdlingResource;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.repository.DataSource.LoadRecipeCallback;
import com.dappslocker.bakingapp.repository.RecipesDataSource;
import com.dappslocker.bakingapp.utility.AppExecutors;
import com.dappslocker.bakingapp.utility.DataSourceUtils;

import java.util.List;


public class LocalRecipesDataSource implements RecipesDataSource {
    private LoadRecipeCallback mCallback;
    private volatile static LocalRecipesDataSource INSTANCE = null;
    private static final String TAG = "LocalRecipesDataSource";
    private RecipesDao recipesDao;
    private static AppExecutors mAppExecutors;

    private LocalRecipesDataSource(Application application) {
        mAppExecutors = AppExecutors.getInstance();
        //get a reference to the recipes DAO
        recipesDao = RecipeDatabase.getInstance(application).recipesDao();
    }


    public static LocalRecipesDataSource getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new LocalRecipesDataSource(application);
        }
        return INSTANCE;
    }
    @Override
    public void getRecipes() {
        loadFromLocalDatabase();
    }

    public void getRecipe(Integer recipeId, SimpleIdlingResource idlingResource) {
        loadFromLocalDatabase(recipeId,idlingResource);
    }


    /**
     * Make a room call to read recipies from the local database and notify the invoker via callback
     */
    private void loadFromLocalDatabase(final Integer recipeId, final SimpleIdlingResource idlingResource) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(idlingResource != null){
                    idlingResource.setIdleState(false);
                }
                Recipe recipe = recipesDao.getRecipe(recipeId.intValue());
                if( recipe != null){
                    mCallback.onRecipeLoaded(recipe, DataSourceUtils.DataSourceIdentifers.DATABASE);
                    Log.d(TAG, "retrieving recipe from the database was sucessfull");
                }
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
        });
    }
    /**
     * Make a room call to read recipies from the local database and notify the invoker via callback
     */
    private void loadFromLocalDatabase() {
        //
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Recipe> recipeList = recipesDao.getRecipes();
                if( recipeList != null){
                    mCallback.onRecipeLoaded(recipeList, DataSourceUtils.DataSourceIdentifers.DATABASE);
                    Log.d(TAG, "retrieving recipe from the database was sucessfull");
                }else{
                    mCallback.onDataNotAvailable(DataSourceUtils.DataSourceIdentifers.DATABASE);
                    Log.d(TAG, "retrieving recipe from the database was not sucessfull");
                }
            }
        });
    }

    @Override
    public void refreshRecipes() {
        getRecipes();
    }

    @Override
    public void setLoadRecipeCallBack(@NonNull LoadRecipeCallback callBack) {
        this.mCallback = callBack;
    }

    public void deleteAllAndInsert(final List<Recipe> recipeList){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipesDao.deleteRecipes();
                Log.d(TAG, "deleted all recipes from the database");
                for (Recipe recipe : recipeList) {
                    recipesDao.insertRecipe(recipe);
                }
                Log.d(TAG, "inserted " + recipeList.size() + "recipe(s) into the database");
            }
        });
    }

}
