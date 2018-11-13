package com.dappslocker.bakingapp;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.model.Step;


public class RecipeDetailAdapter  extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailAdapterViewHolder> {
    private final static String TAG = "RecipeDetailAdapter";
    private static Recipe mRecipe;
    RecipeDetailAdapter(Recipe recipe){
        mRecipe = recipe;
    }

    @Override
    public RecipeDetailAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe_detail, parent, false);
        return  new RecipeDetailAdapter.RecipeDetailAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeDetailAdapterViewHolder holder, int position) {
        //set the visibility of the views depending on position
        if(mRecipe == null){
            return;
        }
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        if (mRecipe == null){
            Log.d(TAG,"inside getItemCount(): mRecipe = null");
            return 0;
        }
        //list of ingridents will occupy the first position for the reccyclerview
        return mRecipe.getListOfSteps().size()+1;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        notifyDataSetChanged();
    }

    public class RecipeDetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        final Button buttonViewIngredientsCount;
        final ConstraintLayout constraintLayout;
        final TextView textViewStepCount;
        final TextView textViewStepDescription;
        public RecipeDetailAdapterViewHolder(View view) {
            super(view);
            buttonViewIngredientsCount = view.findViewById(R.id.item_recipe_ingredients);
            constraintLayout = view.findViewById(R.id.item_recipe_steps_container);
            textViewStepCount = view.findViewById(R.id.textViewStep);
            textViewStepDescription = view.findViewById(R.id.textViewStepDescription);
        }

        @Override
        public void onClick(View v) {
            //todo: implement click
        }

        public void bindView(int position) {
            if(position == 0){
                //set visibility
                constraintLayout.setVisibility(View.GONE);
                buttonViewIngredientsCount.setVisibility(View.VISIBLE);
                buttonViewIngredientsCount.setText("Number of Ingridents = " + mRecipe.getListOfIngredients().size());
            }
            else{
                Step step = mRecipe.getListOfSteps().get(position-1);
                buttonViewIngredientsCount.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
                textViewStepCount.setText(Integer.valueOf(step.getId()+1).toString());
                textViewStepDescription.setText(step.getShortDescription());
            }
        }
    }
}
