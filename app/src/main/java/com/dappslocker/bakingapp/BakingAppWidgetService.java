package com.dappslocker.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;


public class BakingAppWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}


@SuppressWarnings("unused")
class BakingAppRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private  ArrayList<String> listOfIngredients;
    private final Context mContext;

    BakingAppRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
    }
    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        ArrayList<String> list = BakingAppWidgetProvider.getListOfIngredients();
        if(!(list == null)){
            listOfIngredients = new ArrayList<>();
            listOfIngredients.clear();
            listOfIngredients.addAll(list);
        }
    }

    @Override
    public void onDestroy() {
        listOfIngredients = null;
    }

    @Override
    public int getCount() {
        if(!(listOfIngredients == null)){
            return listOfIngredients.size();
        }
        else {
            return 0;
        }
    }


    @Override
    public RemoteViews getViewAt(int position) {
        if(position == AdapterView.INVALID_POSITION ||listOfIngredients == null){
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingridient_list_item);
        remoteViews.setTextViewText(R.id.appwidget_text_ingridient_item,listOfIngredients.get(position));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
