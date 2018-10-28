package com.example.bogdan.feastfordriver;

import android.app.Application;

public class FdApp extends Application {

    private static FdApp app;

    public static FdApp get() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
