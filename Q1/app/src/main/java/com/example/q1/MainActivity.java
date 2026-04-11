package com.example.q1;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import android.widget.MediaController;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private Button btnPlay, btnPause, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);

        // Set a sample video URI. 
        // Note: You can replace this with a local resource like:
        // Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample_video)
        // For now, using a sample online video for demonstration.
        String videoPath = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Optional: Add default media controller for seekbar etc.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    videoView.start();
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.stopPlayback();
                videoView.resume(); // Reset to beginning
            }
        });
    }
}
