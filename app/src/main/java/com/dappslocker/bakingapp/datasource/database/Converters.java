package com.dappslocker.bakingapp.datasource.database;

import android.arch.persistence.room.TypeConverter;

import com.dappslocker.bakingapp.model.Ingredient;
import com.dappslocker.bakingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;


@SuppressWarnings("unused")
class Converters {
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static ArrayList<Ingredient> fromJsonStringToListOfIngredients(String data) {
        if (data == null) {
            return (ArrayList)Collections.emptyList();
        }
        Type listType = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String fromListOfIngredientsToJson(ArrayList<Ingredient> ingredientsArrayList) {
        Gson gson = new Gson();
        return gson.toJson(ingredientsArrayList);
    }

    @SuppressWarnings("unchecked")
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
        return gson.toJson(StepsArrayList);
    }
}  
