package com.example.fong.demo;

import android.app.Application;

/**
 * Created by Fong on 17/2/5.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
