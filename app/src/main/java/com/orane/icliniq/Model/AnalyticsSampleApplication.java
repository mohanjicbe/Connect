package com.orane.icliniq.Model;

import android.app.Application;
import android.util.Log;

import com.flurry.android.FlurryAgent;

public class AnalyticsSampleApplication extends Application {

    private static final String FLURRY_APIKEY = "JQVT87W7TGN5W7SWY2FH";
    public static final String LOG_TAG = AnalyticsSampleApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        // Init Flurry
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogLevel(Log.INFO);

        FlurryAgent.setVersionName("1.0");
        FlurryAgent.init(this, FLURRY_APIKEY);

        Log.i(LOG_TAG, "Initialized FLurry Agent");
    }
}
