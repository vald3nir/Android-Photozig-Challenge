package vald3nir.programming_challenge.views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.app.Constants;
import vald3nir.programming_challenge.app.services.RetrofitServices;
import vald3nir.programming_challenge.models.DataAssets;
import vald3nir.programming_challenge.models.Multimedia;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private RetrofitServices retrofitService;

    @Bean
    MultimediaAdpter adpter;

    static DataAssets dataAssets;

    @ViewById
    ListView multimediaListview;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.retrofitService = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(RetrofitServices.class);
    }

    @AfterViews
    public void afterViews() {

        multimediaListview.setAdapter(adpter);
        multimediaListview.setOnItemClickListener(this);

        this.retrofitService.getData().enqueue(new Callback<DataAssets>() {
            @Override
            public void onResponse(Call<DataAssets> call, Response<DataAssets> response) {
                dataAssets = response.body();
                adpter.bind(dataAssets.getMultimedia());
            }

            @Override
            public void onFailure(Call<DataAssets> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    ProgressDialog mProgressDialog;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final Multimedia multimedia = adpter.getItem(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("");

        // set dialog message
        alertDialogBuilder.setMessage("Select an option:").setCancelable(false)

                .setPositiveButton("Donwload Video", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        runDownloadMultimedia(multimedia);
                    }
                })

                .setNegativeButton("Go to next page", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        MultimediaActivity_.intent(MainActivity.this).multimedia(multimedia).start();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void runDownloadMultimedia(Multimedia multimedia) {
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(this, multimedia.getVideo());
        downloadTask.execute(dataAssets.getAssetsLocation() + "/" + multimedia.getVideo());

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }


    public class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;

        private PowerManager.WakeLock mWakeLock;

        private String fileName;

        public DownloadTask(Context context, String fileName) {
            this.context = context;
            this.fileName = fileName;
        }

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
                output = new FileOutputStream("/sdcard/" + fileName);

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
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }

    }
}
