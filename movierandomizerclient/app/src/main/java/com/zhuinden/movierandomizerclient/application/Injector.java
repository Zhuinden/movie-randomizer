package com.zhuinden.movierandomizerclient.application;

import com.zhuinden.movierandomizerclient.application.injection.ApplicationComponent;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class Injector {
    private Injector() {
    }

    public static ApplicationComponent get() {
        return CustomApplication.get().applicationComponent;
    }
}
