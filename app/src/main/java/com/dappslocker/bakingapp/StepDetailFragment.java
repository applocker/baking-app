package com.dappslocker.bakingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.model.Step;
import com.dappslocker.bakingapp.utility.BakingAppUtils;
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

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

@SuppressWarnings("ConstantConditions")
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
    private int position;
    private int zeroIndexPosition;
    private int mTotalSteps;
    private String videoUrl;
    private boolean isVideoInplay;
    private Context context;
    private SimpleExoPlayer player;
    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private static boolean exoPlayerLaunced = false;
    private PlayVideoLandCliCkListener playVideoLandCliCkListener;
    private ActionBarListener actionBarListener;
    private PrevNextClickListener twoPanePrevNextClickListener;
    private boolean isTwoPaneLayout = false;
    public StepDetailFragment(){

    }

     interface PlayVideoLandCliCkListener{
            void onPLayVideoLand(String videoUrl);
     }
     interface ActionBarListener{
            void hideActionBar();
     }
     interface PrevNextClickListener{
            void prevNextClicked(int position);
     }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RecipeDetailActivity) {
           this.context = context;
            playVideoLandCliCkListener = (PlayVideoLandCliCkListener) context;
            actionBarListener = (ActionBarListener) context;
            twoPanePrevNextClickListener = (PrevNextClickListener) context;
        }
        else {
            throw new ClassCastException(context.toString()
                    + " must implement PlayVideoLandCliCkListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this,rootView);
        isTwoPaneLayout = getResources().getBoolean(R.bool.isTablet);
        if(savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelable(BakingAppUtils.KEY_RECIPE);
            mTotalSteps = mRecipe.getListOfSteps().size();
            position =  savedInstanceState.getInt(BakingAppUtils.KEY_POSITION);
            zeroIndexPosition = savedInstanceState.getInt(BakingAppUtils.KEY_ZERO_INDEX);
            isVideoInplay = savedInstanceState.getBoolean(BakingAppUtils.KEY_VIDEO_INPLAY);
        }
        else{
            //noinspection PointlessBooleanExpression
            if (exoPlayerLaunced != true){
                Bundle bundle = getArguments();
                mRecipe = bundle.getParcelable(BakingAppUtils.KEY_RECIPE);
                mTotalSteps = mRecipe.getListOfSteps().size();
                position =  bundle.getInt(BakingAppUtils.KEY_POSITION);
                if(position > 0){
                    zeroIndexPosition = position - 1;
                }
            }
        }
        updateViews();
        mImageButtonMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isTwoPaneLayout && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    //start exoplayer in full screen mode on phone screens
                   if(urlValid()){
                       exoPlayerLaunced = true;
                       actionBarListener.hideActionBar();
                       playVideoLandCliCkListener.onPLayVideoLand(videoUrl);
                   }
                }else{
                    if(urlValid()){
                        PlayVideo();
                        exoPlayerLaunced = true;
                    }
                }

            }
        });

        mImageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                zeroIndexPosition = --zeroIndexPosition <= 0 ? 0:zeroIndexPosition;
                ShowVideoPlayButton();
                updateViews();
                if(isTwoPaneLayout){
                    twoPanePrevNextClickListener.prevNextClicked(zeroIndexPosition);
                }
                if(isVideoInplay){
                    releasePlayer();
                }

            }
        });

         mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                zeroIndexPosition = (++zeroIndexPosition >= mTotalSteps - 1)? mTotalSteps - 1:zeroIndexPosition;
                ShowVideoPlayButton();
                updateViews();
                if(isTwoPaneLayout){
                    twoPanePrevNextClickListener.prevNextClicked(zeroIndexPosition);
                }
                if(isVideoInplay){
                    releasePlayer();
                }
            }
        });

        return rootView;
    }

    private boolean urlValid() {
        boolean isUrlValid = !videoUrl.equals("");
        if(!isUrlValid){
            showErrorUrlDialog();
        }
        return isUrlValid;
    }

    private void showErrorUrlDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(getResources().getString(R.string.invalid_url_error_title))
                .setMessage(getResources().getString(R.string.invalid_url_error_message))
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void PlayVideo() {
        // hide the video player container
        mPlayVideoOrShowNoMediaContainer.setVisibility(View.GONE);
        //show the exoplayer container
        mExoPlayerVideoViewContainer.setVisibility(View.VISIBLE);
        //begin playing the video
        isVideoInplay = true;
        initializePlayer();

    }

    private void ShowVideoPlayButton() {
        // hide the video player container
        mPlayVideoOrShowNoMediaContainer.setVisibility(View.VISIBLE);
        //show the exoplayer container
        mExoPlayerVideoViewContainer.setVisibility(View.GONE);
        isVideoInplay = false;
        exoPlayerLaunced = false;
    }



    private void updateViews() {
        int deviceOrientation = getResources().getConfiguration().orientation;
        Step step;
        if (deviceOrientation == Configuration.ORIENTATION_LANDSCAPE && isVideoInplay) {
            if(!isTwoPaneLayout){
                showVideoInFullScreen();
                actionBarListener.hideActionBar();
            }
            //get the video url
            step = mRecipe.getListOfSteps().get(zeroIndexPosition);
            videoUrl = step.getVideoURL();
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
            step = mRecipe.getListOfSteps().get(zeroIndexPosition);
            CharSequence text = getResources().getString(R.string.step) + " " + step.getId();
            mTextViewStep.setText(text);
            mTextViewStepDescription.setText(step.getDescription());
            //restore play video container
            if(isVideoInplay){
                PlayVideo();
            }
            else{
                videoUrl = step.getVideoURL();
                if(videoUrl.isEmpty()){
                    //show thumbnail if one exists for the video else we display the play button
                    String thumbnailURL= step.getThumbnailURL();
                    if(!thumbnailURL.equals("")){
                        mNoMedia.setVisibility(View.GONE);
                        displayVideoThumbnail(thumbnailURL);
                    }else{
                        mImageButtonMediaPlayer.setVisibility(View.GONE);
                        mNoMedia.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    displayPlayButtonImage();
                }
            }

        }
    }

    private void displayPlayButtonImage() {
        mNoMedia.setVisibility(View.GONE);
        mImageButtonMediaPlayer.setVisibility(View.VISIBLE);
        mImageButtonMediaPlayer.setBackground(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        mImageButtonMediaPlayer.setImageResource(R.mipmap.ic_play_arrow_black_24dp);
    }

    private void displayVideoThumbnail(String thumbnailURL) {
        float width = getResources().getDimension(R.dimen.play_button_width);
        float height =  getResources().getDimension(R.dimen.play_button_height);

        @SuppressWarnings({"unused", "unchecked"}) Target target =
                Glide.with(getActivity())
                        .asBitmap()
                        .load(thumbnailURL)
                        .apply(fitCenterTransform())
                        .into(new SimpleTarget((int)width, (int)height) {

                            @Override
                            public void onResourceReady(@NonNull Object resource, @Nullable Transition transition) {
                                Drawable drawable = new BitmapDrawable(context.getResources(), (Bitmap) resource);
                                mImageButtonMediaPlayer.setBackground(drawable);
                                mImageButtonMediaPlayer.setImageResource(R.mipmap.ic_play_arrow_black_24dp);
                            }
                        });
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
        outState.putParcelable(BakingAppUtils.KEY_RECIPE,mRecipe);
        outState.putBoolean(BakingAppUtils.KEY_VIDEO_INPLAY,isVideoInplay);
        outState.putInt(BakingAppUtils.KEY_POSITION,position);
        outState.putInt(BakingAppUtils.KEY_ZERO_INDEX,zeroIndexPosition);
    }

    public int getPosition() {
        return position;
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
        player.prepare(mediaSource, false, false);
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
