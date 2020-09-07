package com.orane.icliniq;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.orane.icliniq.Model.Model;


public class Prescription_WebViewActivity extends AppCompatActivity {

    public String url,pdf_url_text,str_html_text, type;
    ObservableWebView webView;
    ImageView download_button;
    private static final String TAG = Prescription_WebViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_webview);

        final ProgressBar progress = findViewById(R.id.progress);

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        str_html_text = intent.getStringExtra("str_html");
        pdf_url_text = intent.getStringExtra("pdf_url");
        //type = intent.getStringExtra("type");
        System.out.println("Webview str_html_text-----" + str_html_text);
        //------ getting Values ---------------------------

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
            download_button = toolbar.findViewById(R.id.download_button);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
            //mTitle.setText(type);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------

        webView = (ObservableWebView) findViewById(R.id.webview);

        try {

            webView.setVisibility(View.VISIBLE);


            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setInitialScale(0);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);

            webView.loadDataWithBaseURL("", str_html_text, "text/html", "UTF-8", "");
            webView.setLongClickable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }


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

            @Override
            public void onPageFinished(final WebView view, final String url) {
                progress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });


        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(pdf_url_text));
                startActivity(i);
            }
        });

       // webView.loadUrl(url);
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
            if (webView.canGoBack()) {
                webView.goBack();
            } else {

                finish();

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

            finish();


        }
    }


}