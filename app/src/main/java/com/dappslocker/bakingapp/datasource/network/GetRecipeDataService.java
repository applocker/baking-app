/*
 Referenced code samples from : http://square.github.io/retrofit/
 */
package com.dappslocker.bakingapp.datasource.network;

import com.dappslocker.bakingapp.model.Recipe;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * This interface define the end points to retreive data from the movie database
 */
public interface GetRecipeDataService {
    @GET("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipies();
}
