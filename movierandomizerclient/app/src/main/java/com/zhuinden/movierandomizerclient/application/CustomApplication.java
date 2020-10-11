package com.zhuinden.movierandomizerclient.application;

import android.app.Application;

import com.zhuinden.movierandomizerclient.application.injection.ApplicationComponent;
import com.zhuinden.movierandomizerclient.application.injection.DaggerApplicationComponent;
import com.zhuinden.movierandomizerclient.utils.realmautomigration.AutoMigration;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class CustomApplication
        extends Application {
    private static CustomApplication INSTANCE;

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder() //
                                              .migration(new AutoMigration()) //
                                              .schemaVersion(2) //
                                              .build());
        applicationComponent = DaggerApplicationComponent.create();
    }

    public static CustomApplication get() {
        return INSTANCE;
    }
}
