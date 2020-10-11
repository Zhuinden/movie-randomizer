package com.zhuinden.movierandomizerclient.application.injection;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import com.zhuinden.movierandomizerclient.application.CustomApplication;

/**
 * Created by Zhuinden on 2017.12.28..
 */

@Module
public class ContextModule {
    @Provides
    Context context() {
        return CustomApplication.get();
    }
}
