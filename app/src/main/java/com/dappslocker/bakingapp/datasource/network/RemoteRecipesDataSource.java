package com.dappslocker.bakingapp.datasource.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dappslocker.bakingapp.repository.DataSource.LoadRecipeCallback;
import com.dappslocker.bakingapp.repository.RecipesDataSource;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.DataSourceUtils.DataSourceIdentifers;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RemoteRecipesDataSource implements RecipesDataSource {
    private volatile static RemoteRecipesDataSource INSTANCE = null;
    private static final String TAG = "RemoteRecipesDataSource";
    private LoadRecipeCallback mCallback;
    private GetRecipeDataService service;
    private  Call<ArrayList<Recipe>> call;

    RemoteRecipesDataSource(){
        service = getService();
    }

    public static RemoteRecipesDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteRecipesDataSource();
        }
        return INSTANCE;
    }

    public GetRecipeDataService getService() {
        return RetrofitClient.getRetrofitInstance().create(GetRecipeDataService.class);
    }

    public void setService(@NonNull GetRecipeDataService service) {
        this.service = service;
    }

    @Override
    public void getRecipes() {
        loadFromNetwork();
    }
    @Override
    public void refreshRecipes() {
        getRecipes();
    }

    @Override
    public void setLoadRecipeCallBack(@NonNull LoadRecipeCallback callBack) {
        this.mCallback = callBack;
    }

    /**
     * Make a retrofit call to read recipies from a network and notify the invoker via callback
     */
    private void loadFromNetwork() {
        call = service.getRecipies();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                if(response.isSuccessful()){
                    handleResponse(response.body());
                }
                else{
                    handleResponse(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                handleResponse(null);
            }

            private void handleResponse(ArrayList<Recipe> recipes) {
                if (recipes != null) {
                    mCallback.onRecipeLoaded(recipes, DataSourceIdentifers.NETWORK);

                    Log.d(TAG, "retrieving movie from the network was sucessfull");
                } else {
                    mCallback.onDataNotAvailable(DataSourceIdentifers.NETWORK);
                    Log.d(TAG, "retrieving movie from the network was not sucessfull");
                }
            }
        });
    }

    public static boolean isTestSetup(String testString){
        return true;
    }

}
