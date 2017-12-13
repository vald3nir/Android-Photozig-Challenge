package vald3nir.programming_challenge.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Multimedia multimedia = adpter.getItem(position);
        showDialog(multimedia);
    }

    private void showDialog(final Multimedia multimedia) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Select an option:").setCancelable(false)

                .setPositiveButton("Go to next page", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        MultimediaActivity_.intent(MainActivity.this).multimedia(multimedia).start();
                    }
                })
                .setNegativeButton("Donwload Video", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        runDownloadMultimedia(multimedia);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void runDownloadMultimedia(Multimedia multimedia) {
        final VideoDownloader downloadTask = new VideoDownloader(this, multimedia.getVideo());
        downloadTask.execute(dataAssets.getAssetsLocation() + "/" + multimedia.getVideo());
    }


}
