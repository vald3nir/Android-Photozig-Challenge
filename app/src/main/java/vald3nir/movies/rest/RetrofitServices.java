package vald3nir.movies.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import vald3nir.movies.model.DataAssets;

/**
 * Created by vald3nir on 13/12/17
 */

public interface RetrofitServices {

    @GET("assets.json")
    Call<DataAssets> getData();

}