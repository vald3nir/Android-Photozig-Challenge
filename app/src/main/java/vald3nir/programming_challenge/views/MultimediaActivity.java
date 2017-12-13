package vald3nir.programming_challenge.views;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.VideoView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.models.Multimedia;

@SuppressLint("Registered")
@EActivity(R.layout.activity_multimedia)
public class MultimediaActivity extends AppCompatActivity implements VideoDownloadCallback {

    @Extra
    Multimedia multimedia;

    @Extra
    String baseUrl;

    @ViewById
    public Toolbar toolbar;

    @ViewById
    VideoView videoViewRelative;

    MediaPlayer mediaPlayerAudio = new MediaPlayer();
    MediaPlayer mediaPlayerVideo = new MediaPlayer();

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
    public void onDownloadComplete() {
        playAudio();
        playVideo();
    }

    //    ==========================================================================================

    @AfterViews
    public void afterViews() {
        actionBarConfiguration();
    }

    //    ==========================================================================================

    @SuppressLint("RestrictedApi")
    private void actionBarConfiguration() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
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

        pathFileVideo = Environment.getExternalStorageDirectory().getPath() + "/" + multimedia.getVideo();
        File file = new File(pathFileVideo);

        if (file.exists()) {

            playAudio();
            playVideo();

        } else {

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

        videoViewRelative.setVideoURI(Uri.parse(pathFileVideo));
        videoViewRelative.requestFocus();
//        videoViewRelative.setMediaController(new MediaController(this));
        videoViewRelative.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayerVideo = mediaPlayer;
                mediaPlayerVideo.setLooping(true);
            }
        });
        videoViewRelative.start();
    }


}
