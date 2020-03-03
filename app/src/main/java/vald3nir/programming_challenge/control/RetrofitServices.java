package vald3nir.programming_challenge.control;

import retrofit2.Call;
import retrofit2.http.GET;
import vald3nir.programming_challenge.model.DataAssets;

public interface RetrofitServices {

    @GET("assets.json")
    Call<DataAssets> getData();

}