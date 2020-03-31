package com.bash.backingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.bash.backingapp.models.Step;

public class StepDetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private static final String KEY_EXO_POSITION = "exopostionkey";
    private static final String KEY_EXO_PLAYREADY = "exoplayreadykey";

    private Step mStep;
    private TextView tv_description;
    private ImageButton nav_prev;
    private ImageButton nav_next;

    private ImageView iv_exo;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    private long exoPosition = 0;
    private boolean exoPlayWhenReady = true;

    StepDetailsFragment.OnNavClickListener mCallback;

    public interface OnNavClickListener {
        void onNavigate(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (StepDetailsFragment.OnNavClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnNavClickListener");
        }
    }

    public StepDetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public StepDetailsFragment(Step step) {
        mStep = step;
    }

    public void setStep(Step step)  {
        mStep = step;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.fragment_stepdetail, container, false);
        if(savedInstanceState != null) {
            exoPosition = savedInstanceState.getLong(KEY_EXO_POSITION);
            exoPlayWhenReady = savedInstanceState.getBoolean(KEY_EXO_PLAYREADY);
        }

        iv_exo = (ImageView) rootView.findViewById(R.id.iv_exoplayer);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.v_exoplayer);
        tv_description = rootView.findViewById(R.id.tv_step_description);

        if(rootView.findViewById(R.id.nav_next)!=null) {
            nav_prev = rootView.findViewById(R.id.nav_prev);
            nav_next = rootView.findViewById(R.id.nav_next);

            nav_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mCallback.onNavigate(mStep.getId() - 1);
                }
            });
            nav_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mCallback.onNavigate(mStep.getId() + 1);
                }
            });
        }

        if(mStep != null) {
            if(tv_description != null) {
                tv_description.setText(mStep.getDescription());
            } else {
            }
        }
        return rootView;

    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(exoPosition);
            mExoPlayer.setPlayWhenReady(exoPlayWhenReady);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_EXO_POSITION,exoPosition);
        outState.putBoolean(KEY_EXO_PLAYREADY,exoPlayWhenReady);
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        if(mExoPlayer!=null) {
            exoPosition = mExoPlayer.getCurrentPosition() ;
            exoPlayWhenReady = mExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mStep.getVideoURL().length()>0){
            iv_exo.setVisibility(View.GONE);
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        }
    }

    @Override
    public void onClick(View view) {}
}
