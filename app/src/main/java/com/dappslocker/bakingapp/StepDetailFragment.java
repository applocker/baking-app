package com.dappslocker.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.linearLayoutContainerVideoPlayer)
    LinearLayout mPlayVideoOrShowNoMediaContainer;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.exoPlayerVideoViewContainer)
    FrameLayout mExoPlayerVideoViewContainer;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.exoPlayerVideoView)
    PlayerView  mExoPlayerView;

    @Nullable
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.constraintLayoutLandStepDescriptionContainer)
    ConstraintLayout  mStepDescriptionContainer;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.guidelineVerticalPrevNext)
    Guideline mGuidelineVertical;

    private Recipe mRecipe;
    private static final String KEY_RECIPE = "recipe";
    private static final String KEY_POSITION = "position";
    private static final String KEY_ZERO_INDEX = "zero_index";
    private static final String KEY_VIDEO_INPLAY = "video_inplay";
    private int position;
    private int zeroIndexPosition;
    private Step mStep;
    private int mTotalSteps;
    private String videoUrl;
    private boolean isVideoInplay;
    private Context context;
    private SimpleExoPlayer player;
    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0;

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
            isVideoInplay = savedInstanceState.getBoolean(KEY_VIDEO_INPLAY);
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
                PlayVideo();
            }
        });

        mImageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeroIndexPosition = --zeroIndexPosition <= 0 ? 0:zeroIndexPosition;
                ShowVideoPlayButton();
                updateViews();
                if(isVideoInplay){
                    releasePlayer();
                }
            }
        });

         mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeroIndexPosition = (++zeroIndexPosition >= mTotalSteps - 1)? mTotalSteps - 1:zeroIndexPosition;
                ShowVideoPlayButton();
                updateViews();
                if(isVideoInplay){
                    releasePlayer();
                }
            }
        });

        return rootView;
    }

    private void PlayVideo() {
        // hide the video player container
        mPlayVideoOrShowNoMediaContainer.setVisibility(View.GONE);
        //show the exoplayer container
        mExoPlayerVideoViewContainer.setVisibility(View.VISIBLE);
        //begin playig the video
        isVideoInplay = true;
        initializePlayer();

    }

    private void ShowVideoPlayButton() {
        // hide the video player container
        mPlayVideoOrShowNoMediaContainer.setVisibility(View.VISIBLE);
        //show the exoplayer container
        mExoPlayerVideoViewContainer.setVisibility(View.GONE);
        //begin playig the video
        isVideoInplay = false;
    }

    private void updateViews() {
        int deviceOrientation = getResources().getConfiguration().orientation;
        if (deviceOrientation == Configuration.ORIENTATION_LANDSCAPE && isVideoInplay == true) {
            showVideoInFullScreen();
            //get the video url
            mStep = mRecipe.getListOfSteps().get(zeroIndexPosition);
            videoUrl = mStep.getVideoURL();
            //restore play video container
            if(isVideoInplay){
                PlayVideo();
            }            
        } else {
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
            //update view steps
            mStep = mRecipe.getListOfSteps().get(zeroIndexPosition);
            mTextViewStep.setText(getResources().getString(R.string.step) + " " + mStep.getId());
            mTextViewStepDescription.setText(mStep.getDescription());
            //restore play video container
            if(isVideoInplay){
                PlayVideo();
            }else{
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

        }
    }

    private void showVideoInFullScreen() {
        mStepDescriptionContainer.setVisibility(View.GONE);
        mGuidelineVertical.setGuidelinePercent(1.0f);
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        setRetainInstance(true);
        outState.putParcelable(KEY_RECIPE,mRecipe);
        outState.putBoolean(KEY_VIDEO_INPLAY,isVideoInplay);
        outState.putInt(KEY_POSITION,position);
        outState.putInt(KEY_ZERO_INDEX,zeroIndexPosition);
    }

    private void initializePlayer() {
        if(!isVideoInplay){
            return;
        }
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mExoPlayerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        //player is waiting for a media
        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        //we now have a source of media
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
}
