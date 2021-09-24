package com.photozig.videos.view.video;

import static com.google.android.exoplayer2.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.photozig.videos.R;
import com.photozig.videos.model.Video;
import com.photozig.videos.view.custom.CustomActivity;

public class VideoActivity extends CustomActivity {

    private final VideoDelegate delegate = new VideoDelegate(this);
    private StyledPlayerView playerView;
    static final String VIDEO_PARAM = "VIDEO_PARAM";
    static final String ASSERTS_LOCATION = "ASSERTS_LOCATION";

    public static Intent createIntent(Activity activity, Video video, String assetsLocation) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra(VIDEO_PARAM, video);
        intent.putExtra(ASSERTS_LOCATION, assetsLocation);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        delegate.loadData(getIntent().getExtras());
        setupPlayerView();
    }

    private void setupPlayerView() {
        playerView = findViewById(R.id.player_view);
        playerView.setRepeatToggleModes(REPEAT_TOGGLE_MODE_ALL);
        playerView.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerView.setPlayer(delegate.createVideoPlayer());
    }

    @Override
    protected void onStop() {
        super.onStop();
        delegate.stopPlayer();
    }
}
