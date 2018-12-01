package com.dappslocker.bakingapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "ingredients")
    @SerializedName("ingredients")
    private ArrayList<Ingredient> listOfIngredients;

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

    public ArrayList<Ingredient> getListOfIngredients() {
        return listOfIngredients;
    }

    public void setListOfIngredients(ArrayList<Ingredient> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    public ArrayList<Step> getListOfSteps() {
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

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe() {
        //Empty constructor for room
    }

    @Ignore
    private Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        in.readTypedList(this.listOfIngredients,Ingredient.CREATOR);
        in.readTypedList(this.listOfSteps,Step.CREATOR);
        this.servings=in.readInt();
        this.image = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(listOfIngredients);
        dest.writeTypedList(listOfSteps);
        dest.writeInt(servings);
        dest.writeString(image);
    }
}
