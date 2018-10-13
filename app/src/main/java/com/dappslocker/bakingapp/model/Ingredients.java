package com.dappslocker.bakingapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tiwuya on 10,October,2018
 */
class Ingredients {
    @SerializedName("quantity")
    float quantity;
    @SerializedName("measure")
    String measure;
    @SerializedName("ingredient")
    String ingredient;

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
}