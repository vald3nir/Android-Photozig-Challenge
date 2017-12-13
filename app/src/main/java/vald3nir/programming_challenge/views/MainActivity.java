package vald3nir.programming_challenge.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.control.VideoDownloader;
import vald3nir.programming_challenge.models.DataAssets;
import vald3nir.programming_challenge.models.Multimedia;
import vald3nir.programming_challenge.services.RetrofitServices;
import vald3nir.programming_challenge.views.adapters.MultimediaAdpter;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DataAssets dataAssets;

    private RetrofitServices retrofitService;

    public final String URL_SERVER = "http://pbmedia.pepblast.com/pz_challenge/";

    //    ==========================================================================================
    /* Note: Variables noted with @Bean and @ViewById can not be private */

    @Bean
    MultimediaAdpter adpter;

    @ViewById
    ListView multimediaListview;

    @ViewById
    Toolbar toolbar;

    //    ==========================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.retrofitService = new Retrofit.Builder()
                .baseUrl(URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(RetrofitServices.class);

    }

    //    ==========================================================================================

    @AfterViews
    public void afterViews() {
        actionBarConfiguration();
        listMultimediaConfiguration();
    }

    //    ==========================================================================================

    @SuppressLint("RestrictedApi")
    private void actionBarConfiguration() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
        }

    }

    //    ==========================================================================================

    private void listMultimediaConfiguration() {

        multimediaListview.setAdapter(adpter);
        multimediaListview.setOnItemClickListener(this);

        this.retrofitService.getData().enqueue(new Callback<DataAssets>() {
            @Override
            public void onResponse(Call<DataAssets> call, Response<DataAssets> response) {
                dataAssets = response.body();
                adpter.bind(dataAssets.getAssetsLocation(), dataAssets.getMultimedia());
            }

            @Override
            public void onFailure(Call<DataAssets> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //    ==========================================================================================

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Multimedia multimedia = adpter.getItem(position);
        showDialog(multimedia);
    }

    //    ==========================================================================================

    private void showDialog(final Multimedia multimedia) {

        AlertDialog alertDialog = new AlertDialog.Builder(this)

                .setMessage("Select an option:").setCancelable(false)

                .setPositiveButton("Go to next page", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        // call new activity
                        MultimediaActivity_.intent(MainActivity.this)
                                // pass parameter to new activity (@Extra)
                                .multimedia(multimedia)
                                .baseUrl(dataAssets.getAssetsLocation())
                                .start();
                    }
                })

                .setNegativeButton("Donwload Video", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        runDownloadMultimedia(multimedia);
                    }
                })

                .create();

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    //    ==========================================================================================

    private void runDownloadMultimedia(Multimedia multimedia) {
        final VideoDownloader videoDownloader = new VideoDownloader(this, multimedia.getVideo());
        videoDownloader.execute(dataAssets.getAssetsLocation() + "/" + multimedia.getVideo());
    }


}
