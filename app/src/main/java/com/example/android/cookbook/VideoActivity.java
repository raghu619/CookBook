package com.example.android.cookbook;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.cookbook.fragments.VideoFragment;
import com.example.android.cookbook.globaldata.Global_Constants;
import com.example.android.cookbook.globaldata.Global_Data;

public class VideoActivity extends AppCompatActivity implements VideoFragment.FragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(Global_Constants.ARG_VIDEO_STEP,
                    getIntent().getIntExtra(Global_Constants.ARG_VIDEO_STEP, -1));

            setTitle(getIntent().getStringExtra(Global_Constants.ARG_RECIPE_NAME));
            VideoFragment videoFragment=new VideoFragment();
            videoFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container,videoFragment)
                    .commit();

        }
    }

    @Override
    public void set_fragment_data(long seekTo, boolean isPlaying) {

        Bundle bundle = new Bundle();
        bundle.putLong(Global_Constants.VID_SEEKTO, seekTo);
        bundle.putBoolean(Global_Constants.VID_IS_PLAYING, isPlaying);
        Global_Data.getInstance().setBundle(bundle);
    }
}
