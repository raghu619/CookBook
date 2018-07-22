package com.example.android.cookbook.fragments;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.globaldata.Global_Constants;
import com.example.android.cookbook.globaldata.Global_Data;
import com.example.android.cookbook.models.VideosModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by raghvendra on 18/7/18.
 */

public  class VideoFragment extends Fragment{

    private VideosModel model;
    private SimpleExoPlayer mExoPlayer;
    Unbinder unbinder;


    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.player)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.txt_no_video)
    TextView txt_no_video;
    @BindView(R.id.no_video_img)
    ImageView no_video_img;
    private long seekTo = 0;
    private boolean isPlaying = false;

    public interface FragmentListener{

        void  set_fragment_data(long seekTo,boolean isPlaying);
    }

private FragmentListener fragmentListener;
 private Global_Data global_data;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener=(FragmentListener)context;
        global_data=Global_Data.getInstance();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pos = getArguments().getInt(Global_Constants.ARG_VIDEO_STEP, -1);
        if (pos != -1) {
            model = Global_Data.getInstance().getRecipeModel().getSteps().get(pos);
        }
        if ( global_data.getBundle() != null ) {
            seekTo = global_data.getBundle().getLong(Global_Constants.VID_SEEKTO, 0);
            isPlaying = global_data.getBundle().getBoolean(Global_Constants.VID_IS_PLAYING, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_fragment, container, false);
        unbinder= ButterKnife.bind(this, rootView);
        txtTitle.setText(model.getShortDescription());
        txtDesc.setText(model.getDescription());
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_video));

        if (model.getThumbnailURL().equals(Global_Constants.NO_DATA)
                && model.getVideoURL().equals(Global_Constants.NO_DATA)) {
            no_video_img.setVisibility(View.VISIBLE);
            txt_no_video.setVisibility(View.VISIBLE);
        }
       return  rootView;


    }

    private void initializePlayer(Uri uri) {

        if (mExoPlayer == null) {


            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getContext(), userAgent)
                    , new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(isPlaying);
            mExoPlayer.seekTo(seekTo);


        }
    }

        private void saveCurrentVideoState()
        {
            seekTo = mExoPlayer.getContentPosition();
            isPlaying = mExoPlayer.getPlayWhenReady();
            fragmentListener.set_fragment_data(seekTo, isPlaying);
        }
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void pausePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer(Uri.parse(model.getVideoURL()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= Build.VERSION_CODES.M || mExoPlayer == null)) {
            initializePlayer(Uri.parse(model.getVideoURL()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveCurrentVideoState();
        pausePlayer();
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}




