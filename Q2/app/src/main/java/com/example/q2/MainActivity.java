package com.example.q2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private VideoView videoView;
    private Uri photoUri;
    private Uri videoUri;

    private final ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    imageView.setImageURI(photoUri);
                    imageView.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private final ActivityResultLauncher<Uri> captureVideoLauncher = registerForActivityResult(
            new ActivityResultContracts.CaptureVideo(),
            result -> {
                if (result) {
                    videoView.setVideoURI(videoUri);
                    videoView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    videoView.start();
                } else {
                    Toast.makeText(this, "Failed to record video", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.image_view);
        videoView = findViewById(R.id.video_view);
        Button btnCaptureImage = findViewById(R.id.btn_capture_image);
        Button btnRecordVideo = findViewById(R.id.btn_record_video);

        btnCaptureImage.setOnClickListener(v -> {
            try {
                File photoFile = createImageFile();
                photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                takePictureLauncher.launch(photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnRecordVideo.setOnClickListener(v -> {
            try {
                File videoFile = createVideoFile();
                videoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", videoFile);
                captureVideoLauncher.launch(videoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        return File.createTempFile(videoFileName, ".mp4", storageDir);
    }
}