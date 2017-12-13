package vald3nir.programming_challenge.views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vald3nir on 13/12/17
 */

public class VideoDownloader extends AsyncTask<String, Integer, String> implements DialogInterface.OnCancelListener {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    private PowerManager.WakeLock wakeLock;

    private String fileName;

    private ProgressDialog progressDialog;

    private VideoDownloadCallback callback;

    //    ==========================================================================================

    public VideoDownloader(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public VideoDownloader(Context context, String fileName, VideoDownloadCallback callback) {
        this.context = context;
        this.fileName = fileName;
        this.callback = callback;
    }

    //    ==========================================================================================

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {

            java.net.URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the fileName
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the fileName
            input = connection.getInputStream();
            output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);

            byte data[] = new byte[4096];
            long total = 0;
            int count;

            while ((count = input.read(data)) != -1) {
                // allow canceling with back button

                if (isCancelled()) {
                    input.close();
                    return null;
                }

                total += count;
                // publishing the progress....

                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));

                output.write(data, 0, count);
            }

        } catch (Exception e) {
            return e.toString();

        } finally {

            try {

                if (output != null) output.close();
                if (input != null) input.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (connection != null)
                connection.disconnect();
        }

        return null;
    }

    //    ==========================================================================================

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // take CPU lock to prevent CPU from going off if the user presses the power button during download
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading " + fileName);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(this);
            progressDialog.show();
        }

    }

    //    ==========================================================================================

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(progress[0]);
    }

    //    ==========================================================================================

    @Override
    protected void onPostExecute(String result) {
        wakeLock.release();
        progressDialog.dismiss();

        if (result == null) {

            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();

            if (callback != null) {
                callback.runMultimedia();
            }

        } else {
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        }
    }

    //    ==========================================================================================

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);

        String pathFileVideo = Environment.getExternalStorageDirectory().getPath() + "/" + fileName;
        File file = new File(pathFileVideo);

        if (file.exists()) {
            file.delete();
        }

        if (callback != null) {
            callback.notifyDownloadCanceled();
        }
    }
}
