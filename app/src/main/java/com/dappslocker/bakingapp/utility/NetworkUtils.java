package com.dappslocker.bakingapp.utility;


/**
 * Utility method for the baking app
 */
public final class NetworkUtils {

    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/";
    /***
     * @return return the resource locator for the recipie
     */
    public static String getRecipeUrl() {
        return RECIPE_URL;
    }

    private NetworkUtils() { }
}