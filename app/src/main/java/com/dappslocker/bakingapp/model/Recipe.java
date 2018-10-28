package com.dappslocker.bakingapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "ingredients")
    @SerializedName("ingredients")
    private ArrayList<Ingredients> listOfIngredients;

    @ColumnInfo(name = "steps")
    @SerializedName("steps")
    private ArrayList<Step> listOfSteps;

    @SerializedName("servings")
    private Integer servings;

    @SerializedName("image")
    private String image;

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
