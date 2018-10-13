package com.dappslocker.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiwuya on 10,October,2018
 */
public class Recipe {
    @SerializedName("id")
    Integer id;
    @SerializedName("name")
    String name;
    @SerializedName("ingredients")
    ArrayList<Ingredients> listOfIngredients;
    @SerializedName("steps")
    ArrayList<Step> listOfSteps;
    @SerializedName("servings")
    Integer servings;
    @SerializedName("image")
    String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredients> getListOfIngredients() {
        return listOfIngredients;
    }

    public void setListOfIngredients(ArrayList<Ingredients> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    public List<Step> getListOfSteps() {
        return listOfSteps;
    }

    public void setListOfSteps(ArrayList<Step> listOfSteps) {
        this.listOfSteps = listOfSteps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
