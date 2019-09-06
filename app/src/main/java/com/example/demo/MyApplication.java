package com.example.demo;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * author：YZQ
 * Date:2019-08-29
 * DIRECTION:
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);

        super.attachBaseContext(base);

    }
}
