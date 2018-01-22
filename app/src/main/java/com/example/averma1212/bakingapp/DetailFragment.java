package com.example.averma1212.bakingapp;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.averma1212.bakingapp.data.Steps;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
 * Created by HP on 23-12-2017.
 **/



public class DetailFragment extends Fragment {
    private static final java.lang.String CUR_POS = "position";
    private static Steps step;
    private static String ingredients;
    private static long curPos;

    @BindView(R.id.simple_exoplayer_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.detail_topic)
    TextView topic;
    @BindView(R.id.detail_desc)
    TextView description;
    @BindView(R.id.step_img)
    ImageView step_image;
    @BindView(R.id.no_step_img_tv)
    TextView no_step_img;

    private SimpleExoPlayer mExoPlayer;

    private Unbinder unbinder;

    public DetailFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_fragment,container,false);
        unbinder = ButterKnife.bind(this,rootView);

        if(step==null){
            topic.setText(R.string.ingredients);
            mPlayerView.setVisibility(View.GONE);
            description.setText(ingredients);
            return rootView;
        }
        topic.setText(step.getShortDesc());
        description.setText(step.getDesc());
        String imgUrl = step.getImageURL();
        if(imgUrl.length()>0){
            step_image.setVisibility(View.VISIBLE);
            Picasso.with(step_image.getContext()).load(imgUrl).into(step_image);
        } else {
            no_step_img.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        if(step!=null){
            initializePlayerOrSetGone();
        }
        super.onResume();
            if(mExoPlayer!=null){
                mExoPlayer.seekTo(curPos);
            }
    }

    private void initializePlayerOrSetGone() {
        String mediaUri = step.getVideoURL();
        if(mediaUri.length()==0){
            mPlayerView.setVisibility(View.GONE);
        }
        else{
            initializePlayer(Uri.parse(mediaUri));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
            /*if(mExoPlayer!=null)
                curPos = mExoPlayer.getCurrentPosition();*/

    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    public void setSteps(Steps steps) {
        this.step = steps;
        this.ingredients = null;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
        this.step = null;
    }


    public void initializePlayer(Uri mediaUri){
        TrackSelector trackSelector = new DefaultTrackSelector();
        if(mExoPlayer==null){
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector);
        }
        mPlayerView.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getActivity(),"string");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }



    //Release Player
    public void releasePlayer(){
        if(mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mExoPlayer!=null){
            outState.putLong(CUR_POS,mExoPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            curPos = savedInstanceState.getLong(CUR_POS);
        }

    }
}
