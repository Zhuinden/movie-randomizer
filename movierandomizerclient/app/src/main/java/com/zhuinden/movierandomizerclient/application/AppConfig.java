package com.zhuinden.movierandomizerclient.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;

/**
 * Created by Zhuinden on 2017.12.28..
 */

@Singleton
public class AppConfig {
    private static final String DEFAULT_URL = "http://10.0.2.2:8080/";

    private final Context context;

    @Inject
    public AppConfig(Context appContext) {
        this.context = appContext;
    }

    public String getServerUrl() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("SERVER_URL", DEFAULT_URL);
    }

    @SuppressLint("ApplySharedPref")
    public boolean setServerUrl(String serverUrl) {
        try {
            HttpUrl url = HttpUrl.parse(serverUrl);
            if(url == null) {
                throw new IllegalArgumentException("Invalid URL: " + serverUrl);
            }
        } catch(IllegalArgumentException e) {
            return false;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SERVER_URL", serverUrl);
        editor.commit();
        return true;
    }
}
