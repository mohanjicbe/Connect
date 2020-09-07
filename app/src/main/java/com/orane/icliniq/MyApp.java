package com.orane.icliniq;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.flurry.android.FlurryAgent;
import com.orane.icliniq.Model.Model;

public class MyApp extends Application {


    private static MyApp singleton;

    private static final String FLURRY_APIKEY = "6428FWRMHQXV4BXK43T3";
    public static final String LOG_TAG = MyApp.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;


        //-----------------Init Flurry --------------------------
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogLevel(Log.INFO);
        FlurryAgent.setVersionName(Model.App_ver);
        FlurryAgent.init(this, FLURRY_APIKEY);
        Log.i(LOG_TAG, "Initialized Flurry Agent");
        //-----------------Init Flurry --------------------------

    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
