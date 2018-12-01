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
    private static final String RECIPE = "recipe";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();

        //ingridentsFrament.setRecipe(recipe);
        bundle = getArguments();
        //mRecipe = (Recipe) bundle.getParcelableArray(RECIPE);
        ArrayList<Ingredient> ingredients =mRecipe.getListOfIngredients();
        IngredientsAdapter ingridentsAdapter = new IngredientsAdapter(getActivity(),ingredients);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)
                .setAdapter(ingridentsAdapter, null)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dismiss the dialog
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