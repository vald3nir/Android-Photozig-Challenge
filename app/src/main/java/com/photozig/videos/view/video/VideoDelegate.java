package com.photozig.videos.view.video;

import static com.photozig.videos.view.video.VideoActivity.ASSERTS_LOCATION;
import static com.photozig.videos.view.video.VideoActivity.VIDEO_PARAM;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.photozig.videos.model.Video;

public class VideoDelegate {

    private final VideoActivity view;
    private final MediaPlayer audioPlayer = new MediaPlayer();
    private SimpleExoPlayer videoPlayer;
    private Video video;
    private String baseUrl;

    public VideoDelegate(VideoActivity view) {
        this.view = view;
    }

    public void loadData(Bundle extras) {
        video = (Video) extras.getSerializable(VIDEO_PARAM);
        baseUrl = extras.getString(ASSERTS_LOCATION);
    }

    public Player createVideoPlayer() {

        videoPlayer = new SimpleExoPlayer.Builder(view).build();
        videoPlayer.setPlayWhenReady(true);
        videoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {

                if (playbackState == Player.STATE_READY) {
                    if (!audioPlayer.isPlaying()) {
                        playAudio();
                    }

                } else if (playbackState == Player.STATE_ENDED) {
                    videoPlayer.seekTo(0);
                }
            }
        });
        videoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(baseUrl + "/" + video.getVideoPath())));
        videoPlayer.prepare();
        return videoPlayer;
    }

    public void playAudio() {
        try {

            audioPlayer.setDataSource(view, Uri.parse(baseUrl + "/" + video.getAudioPath()));
            audioPlayer.prepare();
            audioPlayer.seekTo(0);
            audioPlayer.setLooping(false);
            audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioPlayer.setOnCompletionListener(mp -> {
                if (videoPlayer.isPlaying()) {
                    videoPlayer.stop();
                    videoPlayer.release();
                }
            });
            audioPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlayer() {
        if (videoPlayer.isPlaying()) {
            videoPlayer.stop();
            videoPlayer.release();
        }
        if (audioPlayer.isPlaying()) {
            audioPlayer.stop();
            audioPlayer.release();
        }
    }
}
