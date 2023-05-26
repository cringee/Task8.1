package com.example.task81;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private WebView webViewVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        // Initialize the WebView
        webViewVideo = findViewById(R.id.webViewVideo);

        // Enable JavaScript in WebView
        WebSettings webSettings = webViewVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Get the video ID from the intent extras
        String videoId = getIntent().getStringExtra("videoId");

        // Load the YouTube video using the video ID
        loadYouTubeVideo(videoId);
    }

    private void loadYouTubeVideo(String videoId) {
        // Construct the URL for embedding the YouTube video
        String iframeUrl = "https://www.youtube.com/embed/" + videoId;

        // Clear the WebView cache
        webViewVideo.clearCache(true);

        // Construct the HTML code to embed the YouTube video
        String html = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + iframeUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        // Load the HTML content into the WebView
        webViewVideo.loadData(html, "text/html", "utf-8");
    }
}
