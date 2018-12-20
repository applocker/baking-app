package com.dappslocker.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.utility.BakingAppUtils;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {
    private static Recipe mRecipe;
    private static Boolean isLaunchedFromWidget = false;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        //clear any previous text
        views.setTextViewText(R.id.appwidget_text,"");
        PendingIntent pendingIntent;
         if (mRecipe != null){
             views.setTextViewText(R.id.appwidget_text,mRecipe.getName());
             Intent intent = new Intent(context, RecipeDetailActivity.class);
             isLaunchedFromWidget = true;
             intent.putExtra(BakingAppUtils.RECIPE_ID,mRecipe.getId());
             intent.putExtra(BakingAppUtils.RECIPE_NAME,mRecipe.getName());
             intent.putExtra(BakingAppUtils.RECIPE_LAUNCED_FROM_WIDGET,isLaunchedFromWidget);
             pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         }
         else{
             Intent intent = new Intent(context, RecipeActivity.class);
             pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

         }
        views.setOnClickPendingIntent(R.id.imageViewWidgetChefHat,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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
}

