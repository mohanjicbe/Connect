package com.orane.icliniq;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Model;


public class Attachment_WebViewActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    public String url, type, token;
    LinearLayout bottom_layout;
    ObservableWebView webView;
    ImageView close_button;
    TextView mTitle;
    public String search_str = "guestpaythank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        FlurryAgent.onPageView();

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");

        System.out.println("url-----" + url);
        //------ getting Values ---------------------------

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        //------------------ Title Bar ------------------------------------------------
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        close_button = (ImageView) toolbar.findViewById(R.id.close_button);

        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        mTitle.setTypeface(khandBold);
        mTitle.setText(type);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------

        webView = (ObservableWebView) findViewById(R.id.webview);

        try {

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webSettings.setBuiltInZoomControls(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                    return false;
                }

                @Override
                public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                    progress.setVisibility(View.VISIBLE);
                    super.onPageStarted(view, url, favicon);
                }

                public void onLoadResource(WebView view, String url) {

                    try {
                        String webUrl = webView.getUrl();

                        Uri uri = Uri.parse(webUrl);
                        token = uri.getLastPathSegment();

                        if (token.equals(search_str)) {
                            // pay_success();
                            try {

                                search_str = "Changed";
                                ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                                // Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
                                Model.query_launch = "Invoice";
                                Intent intent = new Intent(Attachment_WebViewActivity.this, ThankyouActivity.class);
                                intent.putExtra("type", type);
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception r) {
                        r.printStackTrace();
                    }
                }

                @Override
                public void onPageFinished(final WebView view, final String url) {
                    progress.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
/*
                new AlertDialog.Builder(Attachment_WebViewActivity.this)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit!")
                        .setMessage("Are you sure you want to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();*/
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

        //getMenuInflater().inflate(R.menu.video_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit!")
                        .setMessage("Are you sure you want to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit!")
                    .setMessage("Are you sure you want to close?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }


}