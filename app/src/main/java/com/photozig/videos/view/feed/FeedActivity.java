package com.photozig.videos.view.feed;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photozig.videos.R;
import com.photozig.videos.interfaces.IAlertDialogListener;
import com.photozig.videos.interfaces.ISelectVideoListener;
import com.photozig.videos.model.Video;
import com.photozig.videos.view.custom.CustomActivity;

import java.util.Collection;

public class FeedActivity extends CustomActivity implements ISelectVideoListener {

    private final FeedAdapter feedAdapter = new FeedAdapter(this);
    private final FeedDelegate delegate = new FeedDelegate(this);
    private ProgressBar loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setupActionBar(getString(R.string.feed));
        setupFeed();
    }

    private void setupFeed() {
        loading = findViewById(R.id.pb_loading);
        RecyclerView recyclerView = findViewById(R.id.rv_videos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(feedAdapter);
        delegate.loadFeed();
    }

    public void showLoading(boolean show) {
        loading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSelectVideo(Video video) {
        showAlertDialog(
                getString(R.string.select_an_option),
                getString(R.string.see_video),
                getString(R.string.download_video),
                new IAlertDialogListener() {
                    @Override
                    public void onClickPositiveButton() {
                        delegate.showVideo(video);
                    }

                    @Override
                    public void onClickNegativeButton() {
                        delegate.downloadVideo(video);
                    }
                }
        );
    }

    public void bindAdapter(String assetsLocation, Collection<Video> videos) {
        feedAdapter.bind(assetsLocation, videos);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        delegate.onRequestPermissionsResult(requestCode);
    }
}
