package com.example.task81;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaylistActivity extends AppCompatActivity {

    private LinearLayout playlistContainer;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.open();

        // Get reference to the playlistContainer LinearLayout
        playlistContainer = findViewById(R.id.playlistContainer);

        // Display the playlist items
        displayPlaylist();
    }

    private void displayPlaylist() {
        // Retrieve the playlist from the database
        List<String> playlist = databaseHelper.getPlaylist();

        // Iterate through each video URL in the playlist
        for (final String videoUrl : playlist) {
            // Create a new LinearLayout for each playlist item
            LinearLayout playlistItemLayout = new LinearLayout(this);
            playlistItemLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Create the delete button for removing the video from the playlist
            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeVideoFromPlaylist(videoUrl);
                }
            });

            // Create the text view for displaying the video URL
            TextView textViewVideo = new TextView(this);
            textViewVideo.setText(videoUrl);
            textViewVideo.setPadding(16, 16, 16, 16);
            textViewVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToVideoActivity(videoUrl);
                }
            });

            // Add the delete button and text view to the playlist item layout
            playlistItemLayout.addView(deleteButton);
            playlistItemLayout.addView(textViewVideo);

            // Add the playlist item layout to the playlist container
            playlistContainer.addView(playlistItemLayout);
        }
    }

    private void removeVideoFromPlaylist(String videoUrl) {
        // Remove the video from the playlist in the database
        databaseHelper.removeVideoFromPlaylist(videoUrl);

        // Remove the playlist item from the playlistContainer visually
        int childCount = playlistContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            LinearLayout playlistItemLayout = (LinearLayout) playlistContainer.getChildAt(i);
            TextView textViewVideo = (TextView) playlistItemLayout.getChildAt(1);
            if (textViewVideo.getText().toString().equals(videoUrl)) {
                playlistContainer.removeViewAt(i);
                break;
            }
        }
    }

    private void navigateToVideoActivity(String videoUrl) {
        // Extract the video ID from the video URL
        String videoId = extractVideoId(videoUrl);

        // Display a toast message with the video ID (for testing purposes)
        Toast.makeText(this, videoId, Toast.LENGTH_SHORT).show();

        // Navigate to the VideoActivity passing the video ID as an intent extra
        Intent intent = new Intent(PlaylistActivity.this, VideoActivity.class);
        intent.putExtra("videoId", videoId);
        startActivity(intent);
    }

    private String extractVideoId(String youtubeUrl) {
        String videoId = null;
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0) {
            String videoUrl = youtubeUrl.trim();
            Pattern pattern = Pattern.compile("(?<=youtu.be/|watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|v=|e\\/|watch\\?v=|embed\\?video_id=|%2Fvideos%2F|embed\\?v=)([^#\\&\\?\\n]*)");
            Matcher matcher = pattern.matcher(videoUrl);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed
        databaseHelper.close();
    }
}
