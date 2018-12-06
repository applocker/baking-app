package com.dappslocker.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.linearLayoutContainerNoMedia)
    ConstraintLayout mNoMedia;



    private Recipe mRecipe;
    private static final String KEY_RECIPE = "recipe";
    private static final String KEY_POSITION = "position";
    private static final String KEY_ZERO_INDEX = "zero_index";
    private int position;
    private int zeroIndexPosition;
    private Step mStep;
    private int mTotalSteps;
    private String videoUrl;
    private Context context;
    public StepDetailFragment(){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RecipeDetailActivity) {
           this.context = context;
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
        if(savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelable(KEY_RECIPE);
            mTotalSteps = mRecipe.getListOfSteps().size();
            position =  savedInstanceState.getInt(KEY_POSITION);
            zeroIndexPosition = savedInstanceState.getInt(KEY_ZERO_INDEX);
        }else{
            Bundle bundle = getArguments();
            mRecipe = bundle.getParcelable(KEY_RECIPE);
            mTotalSteps = mRecipe.getListOfSteps().size();
            position =  bundle.getInt(KEY_POSITION);
            if(position > 0){
                zeroIndexPosition = position - 1;
            }
        }
        updateViews();
        mImageButtonMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mStepDetailClickedListener.onMediaPlayerClicked();

            }
        });

        mImageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeroIndexPosition = --zeroIndexPosition <= 0 ? 0:zeroIndexPosition;
                updateViews();
            }
        });

         mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeroIndexPosition = (++zeroIndexPosition >= mTotalSteps - 1)? mTotalSteps - 1:zeroIndexPosition;
                updateViews();
            }
        });

        return rootView;
    }

    private void updateViews() {
        if(zeroIndexPosition == 0){
            mImageButtonPrevious.setVisibility(View.INVISIBLE);
        }
        else if(zeroIndexPosition == (mTotalSteps-1)){
            mImageButtonNext.setVisibility(View.INVISIBLE);
        }
        else{
            mImageButtonPrevious.setVisibility(View.VISIBLE);
            mImageButtonNext.setVisibility(View.VISIBLE);
        }

        mStep = mRecipe.getListOfSteps().get(zeroIndexPosition);
        mTextViewStep.setText(getResources().getString(R.string.step) + " " + mStep.getId());
        mTextViewStepDescription.setText(mStep.getDescription());
        videoUrl = mStep.getVideoURL();
        if(videoUrl.isEmpty()){
            mImageButtonMediaPlayer.setVisibility(View.GONE);
            mNoMedia.setVisibility(View.VISIBLE);
        }
        else{
            mNoMedia.setVisibility(View.GONE);
            mImageButtonMediaPlayer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        setRetainInstance(true);
        outState.putParcelable(KEY_RECIPE,mRecipe);
        outState.putInt(KEY_POSITION,position);
        outState.putInt(KEY_ZERO_INDEX,zeroIndexPosition);
    }
}
