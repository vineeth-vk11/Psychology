package com.psychology.AccountUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.psychology.R;

public class KnowMoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        YouTubePlayerView youTubePlayerView1 = findViewById(R.id.youtube_player_view1);
        YouTubePlayerView youTubePlayerView2 = findViewById(R.id.youtube_player_view2);

        getLifecycle().addObserver(youTubePlayerView);
        getLifecycle().addObserver(youTubePlayerView1);
        getLifecycle().addObserver(youTubePlayerView2);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "w3-1_OyqdDs";
                youTubePlayer.loadVideo(videoId, 0);
                youTubePlayer.pause();
            }
        });

        youTubePlayerView1.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "w3-1_OyqdDs";
                youTubePlayer.loadVideo(videoId, 0);
                youTubePlayer.pause();
            }
        });

        youTubePlayerView2.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "w3-1_OyqdDs";
                youTubePlayer.loadVideo(videoId, 0);
                youTubePlayer.pause();
            }
        });


    }
}