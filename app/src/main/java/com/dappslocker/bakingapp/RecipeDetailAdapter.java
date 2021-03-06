package com.dappslocker.bakingapp;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.model.Step;

import java.util.Locale;


public class RecipeDetailAdapter  extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailAdapterViewHolder> {
    private final static String TAG = "RecipeDetailAdapter";
    private Recipe mRecipe;
    private int selectedPos = RecyclerView.NO_POSITION;
    private final RecipeDetailAdapterOnClickHandler mClickHandler;

    RecipeDetailAdapter(RecipeDetailAdapterOnClickHandler clickHandler){
        mRecipe = null;
        mClickHandler = clickHandler;
    }

    public interface RecipeDetailAdapterOnClickHandler {
        void onClick(int position);
    }

    @NonNull
    @Override
    public RecipeDetailAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe_detail, parent, false);
        return  new RecipeDetailAdapter.RecipeDetailAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailAdapterViewHolder holder, int position) {
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
        //list of ingredents will occupy the first position for the recyclerview
        return mRecipe.getListOfSteps().size()+1;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        notifyDataSetChanged();
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void prevNextClicked( int position){
        if(position > 0){
            notifyItemChanged(selectedPos);
            selectedPos = position;
            notifyItemChanged(selectedPos);
        }
    }

    public class RecipeDetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        final TextView textViewIngredients;
        final ConstraintLayout constraintLayout;
        final TextView textViewStepCount;
        final TextView textViewStepDescription;
        final LinearLayout linearLayoutIngridentsContainer;

        RecipeDetailAdapterViewHolder(View view) {
            super(view);
            textViewIngredients = view.findViewById(R.id.item_recipe_ingredients);
            constraintLayout = view.findViewById(R.id.item_recipe_steps_container);
            textViewStepCount = view.findViewById(R.id.textViewStep);
            textViewStepDescription = view.findViewById(R.id.textViewStepShortDescription);
            linearLayoutIngridentsContainer = view.findViewById(R.id.item_recipe_ingredients_container);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
             int position = getAdapterPosition();
            if(position > 0){
                notifyItemChanged(selectedPos);
                selectedPos = getAdapterPosition();
                notifyItemChanged(selectedPos);
            }
            mClickHandler.onClick(position);

        }

        void bindView(int position) {
            if(position == 0){
                //set visibility
                constraintLayout.setVisibility(View.GONE);
                linearLayoutIngridentsContainer.setVisibility(View.VISIBLE);
                textViewIngredients.setText(R.string.ingridients_title);
            }
            else{
                Step step = mRecipe.getListOfSteps().get(position-1);
                linearLayoutIngridentsContainer.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
                textViewStepCount.setText(String.format(Locale.getDefault(),"%d",(step.getId()+1)));
                textViewStepDescription.setText(step.getShortDescription());
                this.itemView.setSelected(selectedPos == position);
            }
        }
    }
}
