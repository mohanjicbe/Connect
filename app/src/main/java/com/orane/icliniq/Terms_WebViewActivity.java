package com.orane.icliniq;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Model;


public class Terms_WebViewActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    public String url, type;
    LinearLayout bottom_layout;
    TextView menu_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_webview);

        Model.terms_isagree = "false";

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        menu_title = toolbar.findViewById(R.id.menu_title);

        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        mTitle.setTypeface(khandBold);


        //menu_title.setText("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------


        bottom_layout = findViewById(R.id.bottom_layout);


        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        final ProgressBar progress = findViewById(R.id.progress);
        FlurryAgent.onPageView();

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");
        System.out.println("url-----" + url);
        //------ getting Values ---------------------------

        mTitle.setText(type);

        ObservableWebView webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                return true;
            }

            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                progress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                progress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

/*        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/


 /*       bottom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Model.query_launch = "WebView";
                    Intent intent = new Intent(Terms_WebViewActivity.this, AskQuery1.class);

                    startActivity(intent);
                    //finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //------------ Google firebase Analitics-----------------------------------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("user_id", Model.id);
                Model.mFirebaseAnalytics.logEvent("Healthtools", params);
                //------------ Google firebase Analitics---------------------------------------------
            }
        });
*/

        menu_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.terms_isagree = "true";
                finish();
            }
        });

        webView.loadUrl(url);
        webView.setScrollViewCallbacks(this);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        System.out.println("Scrolling----------------------" + scrollState);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            Model.terms_isagree = "false";
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}