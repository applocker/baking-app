package com.dappslocker.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dappslocker.bakingapp.model.Ingredient;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.BakingAppUtils;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {
    private static Recipe mRecipe;
    private static ArrayList<String> listOfIngredients;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        //clear any previous text
        views.setTextViewText(R.id.appwidget_text_recipe_title,"");
        PendingIntent pendingIntent;
         if (mRecipe != null){
             //set the title
             views.setTextViewText(R.id.appwidget_text_recipe_title,mRecipe.getName());
             listOfIngredients =  new ArrayList<>();
             listOfIngredients.clear();
             for(Ingredient ingredient: mRecipe.getListOfIngredients()){
                 listOfIngredients.add(createViewForStep(ingredient));
             }
             //create intent for the service
             Intent intentBakingAppService = new Intent(context, BakingAppWidgetService.class);
             views.setRemoteAdapter(R.id.listViewWidgetIngredientsItemsContainer, intentBakingAppService);

             Intent intent = new Intent(context, RecipeDetailActivity.class);
             intent.putExtra(BakingAppUtils.RECIPE_ID,mRecipe.getId());
             intent.putExtra(BakingAppUtils.RECIPE_NAME,mRecipe.getName());
             intent.putExtra(BakingAppUtils.RECIPE_LAUNCED_FROM_WIDGET, true);
             pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         }
         else{
             Intent intent = new Intent(context, RecipeActivity.class);
             pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         }
        views.setOnClickPendingIntent(R.id.linearLayoutWidgetStepsTitleContainer,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        if (mRecipe != null){
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.listViewWidgetIngredientsItemsContainer);
        }

    }

    private static String createViewForStep( Ingredient ingredient) {
        return ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void setRecipe(Recipe recipe, Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if(BakingAppWidgetProvider.mRecipe == null){
            BakingAppWidgetProvider.mRecipe = new Recipe();
        }
        BakingAppWidgetProvider.mRecipe = recipe;
        RecipeDetailActivity.setWidgetRecipe(recipe);
         for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static ArrayList<String> getListOfIngredients() {
        return listOfIngredients;
    }
}

