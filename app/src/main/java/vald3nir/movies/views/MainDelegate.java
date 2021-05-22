package vald3nir.movies.views;

import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vald3nir.movies.control.VideoDownloader;
import vald3nir.movies.model.DataAssets;
import vald3nir.movies.model.Multimedia;
import vald3nir.movies.control.RetrofitServices;

public class MainDelegate {

    private RetrofitServices retrofitService;
    private MainActivity mainActivity;
    private DataAssets dataAssets;

    MainDelegate(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.retrofitService = new Retrofit.Builder()
                .baseUrl("http://pbmedia.pepblast.com/pz_challenge/")
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(RetrofitServices.class);
    }

    public void showDialog(Multimedia multimedia) {

        AlertDialog alertDialog = new AlertDialog.Builder(mainActivity)

                .setMessage("Select an option:").setCancelable(false)

                .setPositiveButton("Go to next page", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // call new activity
                        MultimediaActivity.startActivity(mainActivity, multimedia, dataAssets.getAssetsLocation());
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

    public void listMultimediaConfiguration() {


        this.retrofitService.getData().enqueue(new Callback<DataAssets>() {
            @Override
            public void onResponse(@NonNull Call<DataAssets> call, @NonNull Response<DataAssets> response) {
                dataAssets = response.body();
                if (dataAssets != null) {
                    mainActivity.bind(dataAssets.getAssetsLocation(), dataAssets.getMultimedia());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataAssets> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void runDownloadMultimedia(Multimedia multimedia) {
        final VideoDownloader videoDownloader = new VideoDownloader(mainActivity, multimedia.getVideo());
        videoDownloader.execute(dataAssets.getAssetsLocation() + "/" + multimedia.getVideo());
    }
}
