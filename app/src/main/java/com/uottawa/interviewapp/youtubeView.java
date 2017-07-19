package com.uottawa.interviewapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class youtubeView extends YouTubePlayerSupportFragment implements
        YouTubePlayer.OnInitializedListener {
    String url;

    public youtubeView(String url) {
        this.url = url;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.initialize("AIzaSyANdim4n0IWvHMlxux864r43PreWHbPhMQ", this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if(!b){
            YouTubePlayer.PlayerStyle style = YouTubePlayer.PlayerStyle.DEFAULT;
            youTubePlayer.setShowFullscreenButton(false);
            youTubePlayer.setPlayerStyle(style);
            youTubePlayer.cueVideo(url);

        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }



}