package com.zhuinden.movierandomizerclient.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.zhuinden.movierandomizerclient.application.CustomApplication;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class Toaster {
    private Toaster() {
    }

    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void showToast(String text) {
        if(Looper.getMainLooper() == Looper.myLooper()) {
            showText(text);
        } else {
            handler.post(() -> showText(text));
        }
    }

    private static void showText(String text) {
        Toast.makeText(CustomApplication.get(), text, Toast.LENGTH_LONG).show();
    }
}
