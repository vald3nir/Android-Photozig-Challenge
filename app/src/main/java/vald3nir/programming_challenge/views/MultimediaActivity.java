package vald3nir.programming_challenge.views;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.widget.Toast;
import android.widget.VideoView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.models.Multimedia;

import static vald3nir.programming_challenge.views.MainActivity.dataAssets;

@SuppressLint("Registered")
@EActivity(R.layout.activity_multimedia)
public class MultimediaActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    @Extra
    Multimedia multimedia;

    @Extra
    String baseUrl;

    @ViewById
    VideoView videoViewRelative;

//    @ViewById
//    SurfaceView surfaceview;


    MediaPlayer mediaPlayer = new MediaPlayer();
    MediaPlayer mediaPlayerVideo = new MediaPlayer();

    @Override
    protected void onPause() {
        super.onPause();
        stopMultimedia();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMultimedia();
    }

    private void stopMultimedia() {
        mediaPlayer.stop();
        mediaPlayerVideo.stop();
    }

    @AfterViews
    public void afterViews() {

//        surfaceHolder = surfaceview.getHolder();
//        surfaceHolder.addCallback(this);


        playAudio();
        playVideo();
    }

    private SurfaceHolder surfaceHolder;

//    private MediaController mediaController;
//    private Handler handler = new Handler();


    @UiThread
    public void playAudio() {

//        try {
//            mediaPlayer.setDataSource(dataAssets.getAssetsLocation() + "/" + multimedia.getAudio());
//            mediaPlayer.prepare();
//            mediaPlayer.setLooping(false);
//            mediaPlayer.setDisplay(surfaceHolder);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mediaPlayerVideo.stop();
//                }
//            });
//
//
//            mediaPlayer.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @UiThread
    public void playVideo() {

        String filePath = "/sdcard/" + multimedia.getVideo();
        File file = new File(filePath);

        if (file.exists()) {

            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();


            videoViewRelative.setVideoURI(Uri.parse(filePath));
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
        } else {
            Toast.makeText(this, "Nao", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setDisplay(surfaceHolder);
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            mediaPlayer.prepare();
//            mediaPlayer.setDataSource(videoSource);
//            mediaPlayer.prepare();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
