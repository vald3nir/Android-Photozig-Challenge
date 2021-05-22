package vald3nir.movies.control;

import retrofit2.Call;
import retrofit2.http.GET;
import vald3nir.movies.model.DataAssets;

public interface RetrofitServices {

    @GET("assets.json")
    Call<DataAssets> getData();

}