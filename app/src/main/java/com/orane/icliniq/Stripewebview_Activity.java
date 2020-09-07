package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.NetCheck;

import me.drakeet.materialdialog.MaterialDialog;


public class Stripewebview_Activity extends AppCompatActivity {

    private WebView webView;
    public String type_val, inv_id, inv_title_txt, url, token, fee_val, cur_val;
    public String search_str = "guestpaythank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stripe_webview);

        //------------ Object Creations -----------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.tv_tooltit);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations ----------------------------------------------

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        webView = (WebView) findViewById(R.id.webView1);

        try {

            Intent intent = getIntent();
            url = intent.getStringExtra("url");
            inv_title_txt = intent.getStringExtra("title");
            type_val = intent.getStringExtra("type");

            System.out.println("Get url---------" + url);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (new NetCheck().netcheck(Stripewebview_Activity.this)) {
            //----------------------------------------------------------
            startWebView(url);
            //----------------------------------------------------------
        } else {
            Toast.makeText(Stripewebview_Activity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient() {

            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {

                try {
                    String webUrl = webView.getUrl();

                    System.out.println("Loaded webUrl---------" + webUrl);

                    Uri uri = Uri.parse(webUrl);
                    token = uri.getLastPathSegment();

                    if (webUrl.contains("error_message")) {
                        pay_failure();
                    } else {
                        if (token.equals(search_str)) {

                            //pay_success();

                            try {
                                search_str = "Changed";
                                ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                                //Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
                                Model.query_launch = "Askquery2";
                                Intent intent = new Intent(Stripewebview_Activity.this, ThankyouActivity.class);
                                intent.putExtra("type", type_val);
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                } catch (Exception r) {
                    r.printStackTrace();
                }
            }

            public void onPageFinished(WebView view, String url) {
            }
        });

        if (new NetCheck().netcheck(Stripewebview_Activity.this)) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        } else {
            Toast.makeText(Stripewebview_Activity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void pay_success() {


        final MaterialDialog alert = new MaterialDialog(Stripewebview_Activity.this);
        alert.setTitle("Payment Success..!");
        alert.setMessage("");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
                finish();
            }
        });
        alert.show();
    }


    public void pay_failure() {

        final MaterialDialog alert = new MaterialDialog(Stripewebview_Activity.this);
        //alert.setTitle("Payment Success..!");
        alert.setMessage("Payment has been stopped");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Ok.", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();
            }
        });
        alert.show();
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
}
