package com.zhuinden.movierandomizerclient.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public interface MovieApi {
    @GET("api/movies")
    Call<List<MovieDO>> getMovies();
}
