package com.dappslocker.bakingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.dappslocker.bakingapp.model.Ingredient;
import com.dappslocker.bakingapp.model.Recipe;

import java.util.ArrayList;

public class IngredientsFrament extends DialogFragment {
    private Recipe mRecipe;
    private static final String KEY_RECIPE = "recipe";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mRecipe = bundle.getParcelable(KEY_RECIPE);
        ArrayList<Ingredient> ingredients =mRecipe.getListOfIngredients();
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getActivity(),ingredients);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)
                .setAdapter(ingredientsAdapter, null)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    dismiss(); // dismiss the dialog
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }
}