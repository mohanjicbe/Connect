package com.orane.icliniq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.orane.icliniq.Model.Model;


public class Webview_Facebook_Activity extends AppCompatActivity {

    private WebView webView;
    public String inv_id, fb_url;
    LinearLayout bottom_layout, toplayout;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String app_first_open = "app_first_open_key";
    public static final String token = "token_key";
    public static final String browser_country = "browser_country";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_webview);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Facebook Tips");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------


        //------ getting Values ---------------------------
        Intent intent = getIntent();
        fb_url = intent.getStringExtra("fb_url");
        System.out.println("Get Intent fb_url-----" + fb_url);
        //------ getting Values ---------------------------
        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================


        toplayout = (LinearLayout) findViewById(R.id.toplayout);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        webView = (WebView) findViewById(R.id.webView1);

/*        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);*/

        try {
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        System.out.println("height----------" + height);
        System.out.println("width----------" + width);


        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        String html = "<iframe src=\"" + fb_url + "&width=500&show_text=true&height=584&appId\" width=\"" + (width / 2) + "\" height=\"500\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\"></iframe>";
        //String html = "<iframe src=\"https://www.facebook.com/plugins/post.php?href=https%3A%2F%2Fwww.facebook.com%2Ficliniq%2Fposts%2F1281005855302759%3A0&width=500&show_text=true&height=584&appId\" width=\"" + (width / 2) + "\" height=\"500\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\"></iframe>";
        webView.loadData(html, "text/html", null);

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                toplayout.setVisibility(View.GONE);
                bottom_layout.setVisibility(View.GONE);
                return false;
            }
        });



/*      if (new NetCheck().netcheck(Webview_Facebook_Activity.this)) {
            startWebView("<iframe src=\"https://www.facebook.com/plugins/post.php?href=https%3A%2F%2Fwww.facebook.com%2Ficliniq%2Fposts%2F1281005855302759%3A0&width=500\" width=\"500\" height=\"589\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\"></iframe>");
        } else {
            Toast.makeText(Webview_Facebook_Activity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }*/

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


  /*  private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient() {

            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {

                try {
                    String webUrl = webView.getUrl();

                    Uri uri = Uri.parse(webUrl);
                    token = uri.getLastPathSegment();

                } catch (Exception r) {
                    r.printStackTrace();
                }

            }

            public void onPageFinished(WebView view, String url) {
                try {
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        if (new NetCheck().netcheck(Webview_Facebook_Activity.this)) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        } else {
            Toast.makeText(Webview_Facebook_Activity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {

            if (bottom_layout.getVisibility() == View.VISIBLE) {
                super.onBackPressed();
            } else {
                toplayout.setVisibility(View.VISIBLE);
                bottom_layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
