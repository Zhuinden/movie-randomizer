package com.zhuinden.movierandomizerclient.application.injection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.zhuinden.movierandomizerclient.application.AppConfig;
import com.zhuinden.movierandomizerclient.data.api.MovieApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zhuinden on 2017.12.28..
 */

@Module
public class ServiceModule {
    @Provides
        // NOT SINGLETON THIS TIME!
    Retrofit retrofit(AppConfig appConfig) {
        return new Retrofit.Builder()
                .baseUrl(appConfig.getServerUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
        // NOT SINGLETON THIS TIME!
    MovieApi movieApi(Retrofit retrofit) {
        return retrofit.create(MovieApi.class);
    }

    @Provides
    @Singleton
    Gson gson() {
        return new GsonBuilder() //
                .setLenient() //
                .create();
    }
}
