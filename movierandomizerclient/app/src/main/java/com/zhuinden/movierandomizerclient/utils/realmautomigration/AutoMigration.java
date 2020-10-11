package com.zhuinden.movierandomizerclient.utils.realmautomigration;

import android.support.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class AutoMigration implements RealmMigration {
    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
        RealmAutoMigration.migrate(realm);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof AutoMigration;
    }

    @Override
    public int hashCode() {
        return AutoMigration.class.hashCode();
    }
}
