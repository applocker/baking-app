/**
 * Created by Tiwuya on 22,August,2018
 */
/***************************************************************************************
 *    Referenced code samples from : http://square.github.io/retrofit/
 ***************************************************************************************/
package com.dappslocker.bakingapp.datasource.network;

import com.dappslocker.bakingapp.utility.NetworkUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(getRecipeResource())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    private static String getRecipeResource(){
        return NetworkUtils.getRecipeUrl();
    }
}
