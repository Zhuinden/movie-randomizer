package com.zhuinden.movierandomizerclient.application.injection;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Zhuinden on 2017.12.28..
 */

@Module
public class JobQueueModule {
    @Provides
    @Singleton
    public JobManager jobManager(Context appContext) {
        return new JobManager(new Configuration.Builder(appContext).build());
    }
}
