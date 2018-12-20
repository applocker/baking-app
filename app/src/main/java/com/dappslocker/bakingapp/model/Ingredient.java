package com.dappslocker.bakingapp.model;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Ingredient implements Parcelable {
    @SerializedName("quantity")
    private
    float quantity;
    @SerializedName("measure")
    private
    String measure;
    @SerializedName("ingredient")
    private
    String ingredient;


    public static final Parcelable.Creator<Ingredient> CREATOR
            = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };


    public Ingredient() {
        //Empty constructor for room
    }

    @Ignore
    private Ingredient(Parcel in) {
        this.quantity = in.readInt();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}