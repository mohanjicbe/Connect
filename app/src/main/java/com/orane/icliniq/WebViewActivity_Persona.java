package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class WebViewActivity_Persona extends BaseActivity implements ObservableScrollViewCallbacks {

    public String url, qid, type, token;
    LinearLayout bottom_layout;
    ObservableWebView webView;
    ImageView close_button;
    TextView mTitle;
    String inv_id,inv_fee,inv_strfee;
    JSONObject jsonobj_prepinv;
    String labtest_response, code_val;
    public String search_str = "closePage";

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String first_query = "first_query_key";
    public static final String have_free_credit = "have_free_credit";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        FlurryAgent.onPageView();

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");
        qid = intent.getStringExtra("qid");

        System.out.println("url-----" + url);

        if (intent.hasExtra("code")) {
            code_val = intent.getStringExtra("code");
        }

        System.out.println("get Intent url-------------" + url);
        System.out.println("get Intent type-------------" + type);
        System.out.println("get Intent code_val-------------" + code_val);

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

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        if (url != null && !url.isEmpty() && !url.equals("null") && !url.equals("")) {

            try {

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


                    }

                    @Override
                    public void onPageFinished(final WebView view, final String url) {
                        progress.setVisibility(View.GONE);

                        System.out.println("Finish_url-----" + url);

                        try {

                            Uri uri = Uri.parse(url);
                            token = uri.getLastPathSegment();



                            if (token.equals(search_str)) {
                                // pay_success();
                                try {

                                    search_str = "Changed";
                                 //   ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                                    //---------------------------------------
                                    String prep_url = (Model.BASE_URL) + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=query&item_id=" + qid + "&token=" + Model.token + "&enc=1";
                                    System.out.println("Query2 Prepare Invoice url-------------" + prep_url);
                                    new JSON_Prepare_inv().execute(prep_url);
                                    //---------------------------------------



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception r) {
                            r.printStackTrace();
                        }



                        super.onPageFinished(view, url);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

/*                new AlertDialog.Builder(WebViewActivity.this)
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


    private class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(WebViewActivity_Persona.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_prepinv = jParser.getJSONFromUrl(urls[0]);

                System.out.println("jsonobj--------" + jsonobj_prepinv.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            dialog.cancel();
            Model.query_launch = "Askquery2";

            try {

                inv_id = jsonobj_prepinv.getString("id");
                inv_fee = jsonobj_prepinv.getString("fee");
                inv_strfee = jsonobj_prepinv.getString("str_fee");

                System.out.println("inv_id--------" + inv_id);
                System.out.println("inv_fee--------" + (inv_fee));
                System.out.println("inv_strfee--------" + inv_strfee);


                if (!(inv_id).equals("0")) {

                    Model.have_free_credit = "0";

                    //----------------- Kissmetrics ----------------------------------
                    Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                    Model.kiss.record("android.patient.Query_Submit_Success");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("Query_id:", qid);
                    properties.put("Invoice_id:", inv_id);
                    properties.put("Invoice_fee:", inv_strfee);
                    Model.kiss.set(properties);
                    //----------------- Kissmetrics ----------------------------------

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("Query_id:", qid);
                    articleParams.put("Invoice_id:", inv_id);
                    articleParams.put("Invoice_fee:", inv_strfee);
                    FlurryAgent.logEvent("android.patient.Query_Submit_Success", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    params.putString("Query_id", qid);
                    params.putString("Invoice_id", inv_id);
                    params.putString("Invoice_fee", inv_strfee);
                    Model.mFirebaseAnalytics.logEvent("Query_Submit_Success", params);
                    //------------ Google firebase Analitics--------------------

                    //((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent intent = new Intent(WebViewActivity_Persona.this, Invoice_Page_New.class);
                    intent.putExtra("qid", qid);
                    intent.putExtra("inv_id", inv_id);
                    intent.putExtra("inv_strfee", inv_strfee);
                    intent.putExtra("type", "query");
                    startActivity(intent);
                    finish();

                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                } else {

                    Model.have_free_credit = "0";

                    Toast.makeText(getApplicationContext(), "Your query has been successfully posted", Toast.LENGTH_SHORT).show();

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(have_free_credit, "0");
                    editor.apply();
                    //============================================================

                    //----------------- Kissmetrics ----------------------------------
                    Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                    Model.kiss.record("android.patient.Query_Submit_Success");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("Query_id:", qid);
                    properties.put("Invoice_id:", inv_id);
                    properties.put("Invoice_fee:", inv_strfee);
                    Model.kiss.set(properties);
                    //----------------- Kissmetrics ----------------------------------

                    System.out.println("query_id--------------" + qid);

                    ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent i = new Intent(WebViewActivity_Persona.this, QueryViewActivity.class);
                    i.putExtra("qid", qid);
                    startActivity(i);
                    finish();
                }

                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}