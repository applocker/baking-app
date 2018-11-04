package com.dappslocker.bakingapp.datasource.database;

import android.arch.persistence.room.TypeConverter;

import com.dappslocker.bakingapp.model.Ingredients;
import com.dappslocker.bakingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Converters {
    @TypeConverter
    public static ArrayList<Ingredients> fromJsonStringToListOfIngredients(String data) {
        if (data == null) {
            return (ArrayList)Collections.emptyList();
        }
        Type listType = new TypeToken<ArrayList<Ingredients>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String fromListOfIngredientsToJson(ArrayList<Ingredients> ingredientsArrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(ingredientsArrayList);
        return json;
    }

    @TypeConverter
    public static ArrayList<Step> fromJsonStringToListOfSteps(String data) {
        if (data == null) {
            return (ArrayList)Collections.emptyList();
        }
        Type listType = new TypeToken<ArrayList<Step>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String fromListOfStepsToJson(ArrayList<Step> StepsArrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(StepsArrayList);
        return json;
    }
}  
