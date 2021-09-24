package com.photozig.videos.rest;

import static com.photozig.videos.AppConfig.SERVER_URL;

import androidx.annotation.NonNull;

import com.photozig.videos.interfaces.IFeedLoaderCallback;
import com.photozig.videos.model.Feed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitClient {

    private interface RetrofitServices {
        @GET("assets.json")
        Call<Feed> getData();
    }

    private final RetrofitServices retrofitService;

    public RetrofitClient() {
        this.retrofitService = new Retrofit
                .Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitServices.class);
    }

    public void getFeed(IFeedLoaderCallback callback) {
        this.retrofitService.getData().enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(@NonNull Call<Feed> call, @NonNull Response<Feed> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Feed> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
