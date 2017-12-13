package vald3nir.programming_challenge.views;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.control.VideoDownloadCallback;
import vald3nir.programming_challenge.control.VideoDownloader;
import vald3nir.programming_challenge.models.Multimedia;

@SuppressLint("Registered")
@EActivity(R.layout.activity_multimedia)
public class MultimediaActivity extends AppCompatActivity implements VideoDownloadCallback {

    /*Note: Variables noted with @Extra and @ViewById can not be private*/

    @Extra
    Multimedia multimedia; // get multimedia from list (Intent)

    @Extra
    String baseUrl;

    @ViewById
    Toolbar toolbar;

    @ViewById
    VideoView videoView;

    @ViewById
    TextView timeCurrentTextview, timeTotalTextview;

    @ViewById
    SeekBar seekBar;

    //    ==========================================================================================


    private MediaPlayer mediaPlayerAudio = new MediaPlayer();
    private MediaPlayer mediaPlayerVideo = new MediaPlayer();

    private Handler handlerUpdateProgressAudio = new Handler();

    private int currentRuntime = 0;
    private String pathFileVideo;

    //    ==========================================================================================

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        currentRuntime = mediaPlayerAudio.getCurrentPosition();
        System.out.println();
    }

    //    ==========================================================================================

    @Override
    public void runMultimedia() {
        playAudio();
        playVideo();
    }

    //    ==========================================================================================

    @Override
    public void notifyDownloadCanceled() {
        onBackPressed();
    }

    //    ==========================================================================================

    @AfterViews
    public void afterViews() {
        actionBarConfiguration();
        pathFileVideo = Environment.getExternalStorageDirectory().getPath() + "/" + multimedia.getVideo();
        handlerUpdateProgressAudio.postDelayed(UpdateSongTime, 100);
    }

    //    ==========================================================================================

    private void seekbarConfiguration() {

        int duration = mediaPlayerAudio.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) duration);
        long secords = TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                TimeUnit.MINUTES.toSeconds(minutes);

        timeTotalTextview.setText(String.format("%d:%d", minutes, secords));
        seekBar.setMax(duration);
        seekBar.setProgress(currentRuntime);
    }

    //    ==========================================================================================

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

    //    ==========================================================================================

    @SuppressLint("RestrictedApi")
    private void actionBarConfiguration() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);

            if (multimedia != null && multimedia.getName() != null) {
                actionBar.setTitle(multimedia.getName());
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //    ==========================================================================================

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayerAudio.isPlaying()) {
            mediaPlayerAudio.stop();
            mediaPlayerAudio.release();
            mediaPlayerAudio = new MediaPlayer();
        }
    }

    //    ==========================================================================================

    @Override
    protected void onResume() {
        super.onResume();
        checkVideoDownload();
    }

    //    ==========================================================================================

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

    //    ==========================================================================================

    @UiThread
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

    //    ==========================================================================================

    @UiThread
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
