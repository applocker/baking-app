package com.dappslocker.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {
    //imageButtonMediaPlayer,  textViewStepHeading, textViewStepDescription, imageButtonPrevious, imageButtonNext
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.imageButtonMediaPlayer)
    ImageButton mImageButtonMediaPlayer;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.textViewStepHeading)
    TextView mTextViewStep;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.textViewStepDescription)
    TextView mTextViewStepDescription;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.imageButtonPrevious)
    ImageButton mImageButtonPrevious;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.imageButtonNext)
    ImageButton mImageButtonNext;

    private OnStepDetailClickedListener mStepDetailClickedListener;
    private Recipe mRecipe;
    private static final String KEY_RECIPE = "recipe";
    private static final String KEY_POSITION = "position";
    private int position;
    private Step mStep;
    public StepDetailFragment(){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RecipeDetailActivity) {
            if (context instanceof OnStepDetailClickedListener) {
                mStepDetailClickedListener = (OnStepDetailClickedListener)context;
            }
        }
        else {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this,rootView);
        Bundle bundle = getArguments();
        mRecipe = bundle.getParcelable(KEY_RECIPE);
        position =  bundle.getInt(KEY_POSITION);
        if(position > 0){
            position = position - 1;
        }else{
            position = -1;
        }
        mStep = mRecipe.getListOfSteps().get(position);
        mTextViewStep.setText(getResources().getString(R.string.step) + " " + mStep.getId());
        mTextViewStepDescription.setText(mStep.getDescription());
        mImageButtonMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepDetailClickedListener.onMediaPlayerClicked();
            }
        });

        mImageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepDetailClickedListener.onPreviousStepClicked();
            }
        });

         mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepDetailClickedListener.onNextStepClicked();
            }
        });

        return rootView;
    }



    public interface OnStepDetailClickedListener {
        void onMediaPlayerClicked();
        void onPreviousStepClicked();
        void onNextStepClicked();
    }
}
