package com.dzm.mypoker;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getApp() {
        return sInstance;
    }
}
