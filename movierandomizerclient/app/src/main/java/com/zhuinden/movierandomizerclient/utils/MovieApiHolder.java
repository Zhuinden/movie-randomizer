package com.zhuinden.movierandomizerclient.utils;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.zhuinden.movierandomizerclient.data.api.MovieApi;

/**
 * Created by Zhuinden on 2017.12.28..
 */

@Singleton
public class MovieApiHolder {
    private AtomicReference<MovieApi> movieApi = new AtomicReference<>();

    @Inject
    public MovieApiHolder() {
    }

    public MovieApi getMovieApi() {
        return movieApi.get();
    }

    public void setMovieApi(MovieApi movieApi) {
        this.movieApi.set(movieApi);
    }
}
