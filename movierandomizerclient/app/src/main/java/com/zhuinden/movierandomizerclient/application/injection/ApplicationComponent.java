package com.zhuinden.movierandomizerclient.application.injection;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.JobQueue;

import javax.inject.Singleton;

import dagger.Component;
import com.zhuinden.movierandomizerclient.application.AppConfig;
import com.zhuinden.movierandomizerclient.data.api.MovieApi;
import com.zhuinden.movierandomizerclient.data.db.MovieDao;
import com.zhuinden.movierandomizerclient.utils.MovieApiHolder;

/**
 * Created by Zhuinden on 2017.12.28..
 */

@Singleton
@Component(modules = {ContextModule.class, JobQueueModule.class, ServiceModule.class})
public interface ApplicationComponent {
    Context appContext();

    JobManager jobManager();

    MovieApiHolder movieApiHolder();

    MovieDao movieDao();

    MovieApi movieApi();

    AppConfig appConfig();
}
