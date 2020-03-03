package vald3nir.programming_challenge.services;

import retrofit2.Call;
import retrofit2.http.GET;
import vald3nir.programming_challenge.models.DataAssets;

public interface RetrofitServices {

    @GET("assets.json")
    Call<DataAssets> getData();

}