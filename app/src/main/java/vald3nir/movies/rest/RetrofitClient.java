package vald3nir.movies.rest;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vald3nir.movies.model.DataAssets;

/**
 * Created by vald3nir on 23/05/21
 */

public class RetrofitClient {

    private final String URL = "http://pbmedia.pepblast.com/pz_challenge/";
    private RetrofitServices retrofitService;

    public RetrofitClient() {
        this.retrofitService = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(RetrofitServices.class);
    }

    public void listMultimedias(IMultimediasLoaderCallback callback) {
        this.retrofitService.getData().enqueue(new Callback<DataAssets>() {
            @Override
            public void onResponse(@NonNull Call<DataAssets> call, @NonNull Response<DataAssets> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DataAssets> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface IMultimediasLoaderCallback {
        public void onSuccess(DataAssets dataAssets);
    }
}
