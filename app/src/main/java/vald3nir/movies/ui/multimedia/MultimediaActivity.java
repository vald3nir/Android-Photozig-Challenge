package vald3nir.movies.ui.multimedia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import vald3nir.movies.R;
import vald3nir.movies.rest.VideoDownloadCallback;
import vald3nir.movies.rest.VideoDownloader;
import vald3nir.movies.model.Multimedia;
import vald3nir.movies.ui.home.HomeActivity;

public class MultimediaActivity extends AppCompatActivity implements VideoDownloadCallback {

    Multimedia multimedia; // get multimedia from list (Intent)
    String baseUrl;
    VideoView videoView;
    TextView timeCurrentTextview, timeTotalTextview;
    SeekBar seekBar;

    private MediaPlayer mediaPlayerAudio = new MediaPlayer();
    private MediaPlayer mediaPlayerVideo = new MediaPlayer();

    private Handler handlerUpdateProgressAudio = new Handler();

    private int currentRuntime = 0;
    private String pathFileVideo;

    public static void startActivity(HomeActivity homeActivity, Multimedia multimedia, String assetsLocation) {
        Intent intent = new Intent(homeActivity, MultimediaActivity.class);
        intent.putExtra("multimedia", multimedia);
        intent.putExtra("assetsLocation", assetsLocation);
        homeActivity.startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        currentRuntime = mediaPlayerAudio.getCurrentPosition();
    }

    @Override
    public void runMultimedia() {
        playAudio();
        playVideo();
    }

    @Override
    public void notifyDownloadCanceled() {
        onBackPressed();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);
        actionBarConfiguration();

        videoView = findViewById(R.id.videoView);
        timeCurrentTextview = findViewById(R.id.time_current_textview);
        timeTotalTextview = findViewById(R.id.time_total_textview);
        seekBar = findViewById(R.id.seekBar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            multimedia = (Multimedia) extras.getSerializable("multimedia");
            baseUrl = extras.getString("assetsLocation");
        }

        pathFileVideo = Environment.getExternalStorageDirectory().getPath() + "/" + multimedia.getVideo();
        handlerUpdateProgressAudio.postDelayed(UpdateSongTime, 100);
    }

    @SuppressLint("DefaultLocale")
    private void seekbarConfiguration() {

        int duration = mediaPlayerAudio.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) duration);
        long secords = TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                TimeUnit.MINUTES.toSeconds(minutes);

        timeTotalTextview.setText(String.format("%d:%d", minutes, secords));
        seekBar.setMax(duration);
        seekBar.setProgress(currentRuntime);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {
            currentRuntime = mediaPlayerAudio.getCurrentPosition();

            long minutes = TimeUnit.MILLISECONDS.toMinutes((long) currentRuntime);
            long secords = TimeUnit.MILLISECONDS.toSeconds((long) currentRuntime) -
                    TimeUnit.MINUTES.toSeconds(minutes);

            timeCurrentTextview.setText(String.format("%d:%d", minutes, secords));

            seekbarConfiguration();
            handlerUpdateProgressAudio.postDelayed(this, 100);
        }
    };

    @SuppressLint("RestrictedApi")
    private void actionBarConfiguration() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);

            if (multimedia != null && multimedia.getName() != null) {
                actionBar.setTitle(multimedia.getName());
            }
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayerAudio.isPlaying()) {
            mediaPlayerAudio.stop();
            mediaPlayerAudio.release();
            mediaPlayerAudio = new MediaPlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVideoDownload();
    }

    private void checkVideoDownload() {
        File file = new File(pathFileVideo);

        if (file.exists()) {
            runMultimedia();

        } else {

            /* run download the video*/
            final VideoDownloader downloadTask = new VideoDownloader(this, multimedia.getVideo(), this);
            downloadTask.execute(baseUrl + "/" + multimedia.getVideo());
        }
    }

    public void playAudio() {

        try {

            try {

                mediaPlayerAudio.setDataSource(baseUrl + "/" + multimedia.getAudio());
                mediaPlayerAudio.prepare();
                mediaPlayerAudio.seekTo(currentRuntime);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Audio not available!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            mediaPlayerAudio.setLooping(false);
            mediaPlayerAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayerAudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayerVideo.isPlaying()) {
                        mediaPlayerVideo.stop();
                        mediaPlayerVideo.release();
                        mediaPlayerVideo = new MediaPlayer();
                    }
                }
            });
            mediaPlayerAudio.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playVideo() {
        videoView.setVideoURI(Uri.parse(pathFileVideo));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayerVideo = mediaPlayer;
                mediaPlayerVideo.setLooping(true);
            }
        });
        videoView.start();
    }


}
