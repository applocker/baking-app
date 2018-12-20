package com.dappslocker.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dappslocker.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class IngredientsAdapter extends ArrayAdapter<Ingredient> {
    private final Context mContext;
    private final List<Ingredient> mIngredients;
    public IngredientsAdapter(@NonNull Context context, @NonNull ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
        mContext = context;
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_ingridents,parent,false);

        Ingredient ingredient = mIngredients.get(position);

        TextView textViewQuantity = listItem.findViewById(R.id.textViewQuantity);
        textViewQuantity.setText(String.format(Locale.getDefault(),"%.2f",ingredient.getQuantity()));

        TextView textViewMeasure = listItem.findViewById(R.id.textViewMeasure);
        textViewMeasure.setText(ingredient.getMeasure());

        TextView textViewIngredient = listItem.findViewById(R.id.textViewIngridentsTitle);
        textViewIngredient.setText(ingredient.getIngredient());

        return listItem;
    }
}
