package com.dappslocker.bakingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dappslocker.bakingapp.model.Recipe;

import java.util.List;


class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private final static String TAG = RecipeAdapter.class.getSimpleName();
    private static List<Recipe> RecipeList;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(int position);
    }

    RecipeAdapter(List<Recipe> RecipeList, RecipeAdapterOnClickHandler clickHandler ) {
        RecipeAdapter.RecipeList = RecipeList;
        mClickHandler = clickHandler;
    }
    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe, parent, false);
        return  new RecipeAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        holder.loadRecipe(position);
    }

    @Override
    public int getItemCount() {
        if (RecipeList == null){
            Log.d(TAG,"RecipeAdapter.getItemCount(): RecipeList = null");
            return 0;
        }

        return RecipeList.size();
    }

    public void setRecipeList(List<Recipe> listOfRecipes) {
        RecipeAdapter.RecipeList = null;
        RecipeAdapter.RecipeList = listOfRecipes;
        notifyDataSetChanged();
    }

    public  Recipe getRecipeAtPosition(int position) {
        if (RecipeList == null){
            return null;
        }
        else
            return RecipeList.get(position);
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textViewRecipeName;

        RecipeAdapterViewHolder(View view) {
            super(view);
            textViewRecipeName = view.findViewById(R.id.item_recipe_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }

        void loadRecipe(int position) {
            textViewRecipeName.setText(RecipeList.get(position).getName());
        }
    }
}
