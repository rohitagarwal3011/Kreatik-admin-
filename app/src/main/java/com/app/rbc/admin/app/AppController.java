package com.app.rbc.admin.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by rohit on 21/6/17.
 */

public class AppController extends Application {
    private static final AppController ourInstance = new AppController();

    static AppController getInstance() {
        return ourInstance;
    }

    public AppController() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
