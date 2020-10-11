package com.zhuinden.movierandomizerclient.features.movies;

import com.google.auto.value.AutoValue;

import com.zhuinden.movierandomizerclient.utils.navigation.BaseFragment;
import com.zhuinden.movierandomizerclient.utils.navigation.BaseKey;

/**
 * Created by Zhuinden on 2017.12.28..
 */

@AutoValue
public abstract class MoviesKey extends BaseKey {
    @Override
    protected BaseFragment createFragment() {
        return new MoviesFragment();
    }

    public static MoviesKey create() {
        return new AutoValue_MoviesKey();
    }
}
