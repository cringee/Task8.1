package com.example.task81;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private EditText editTextYouTubeUrl;
    private Button buttonPlay;
    private Button buttonAddToPlaylist;
    private Button buttonMyPlaylist;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.open();

        // Initialize the views
        editTextYouTubeUrl = findViewById(R.id.editTextYouTubeURL);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonAddToPlaylist = findViewById(R.id.buttonAddToPlaylist);
        buttonMyPlaylist = findViewById(R.id.buttonMyPlaylist);

        // Set OnClickListener for the Play button
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeUrl = editTextYouTubeUrl.getText().toString();
                String videoId = extractVideoId(youtubeUrl);
                navigateToVideoActivity(videoId);
            }
        });

        // Set OnClickListener for the Add to Playlist button
        buttonAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeUrl = editTextYouTubeUrl.getText().toString();
                addToPlaylist(youtubeUrl);
            }
        });

        // Set OnClickListener for the My Playlist button
        buttonMyPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPlaylistActivity();
            }
        });
    }

    // Method to extract the video ID from the YouTube URL
    private String extractVideoId(String youtubeUrl) {
        String videoId = null;
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0) {
            String videoUrl = youtubeUrl.trim();
            if (videoUrl.contains("youtube.com")) {
                Uri uri = Uri.parse(videoUrl);
                String query = uri.getQuery();
                if (query != null && query.length() > 0) {
                    String[] queryParams = query.split("&");
                    for (String param : queryParams) {
                        if (param.startsWith("v=")) {
                            videoId = param.substring(2);
                            break;
                        }
                    }
                }
            } else if (videoUrl.contains("youtu.be")) {
                String[] segments = videoUrl.split("/");
                videoId = segments[segments.length - 1];
            }
        }
        return videoId;
    }

    // Method to navigate to the VideoActivity
    private void navigateToVideoActivity(String videoId) {
        Intent intent = new Intent(HomeActivity.this, VideoActivity.class);
        intent.putExtra("videoId", videoId);
        startActivity(intent);
    }

    // Method to add the video to the playlist
    private void addToPlaylist(String youtubeUrl) {
        String videoId = extractVideoId(youtubeUrl);
        if (videoId != null) {
            String embeddedUrl = "https://www.youtube.com/embed/" + videoId;
            databaseHelper.addVideoToPlaylist(embeddedUrl);
        }
    }

    // Method to navigate to the PlaylistActivity
    private void navigateToPlaylistActivity() {
        Intent intent = new Intent(HomeActivity.this, PlaylistActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed
        databaseHelper.close();
    }
}
