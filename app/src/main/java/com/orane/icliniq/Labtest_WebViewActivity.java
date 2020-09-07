package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.easing.linear.Linear;
import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Labtest.Labtest_CartViewActivity;
import com.orane.icliniq.Labtest.Labtest_tabs_Activity;
import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONObject;


public class Labtest_WebViewActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    public String url, type, token;
    LinearLayout bottom_layout;
    ObservableWebView webView;
    TextView mTitle;
    String test_id, str_response;
    String labtest_response, code_val;
    public String is_cart_added_val, search_str = "guestpaythank";
    TextView tv_test_id, tv_add_cart, tv_cart_count;
    FrameLayout menu_backet;
    JSONObject jsonobj_addcart, json_response_obj;
    LinearLayout cart_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labtest_webview);

        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        FlurryAgent.onPageView();


        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");
        test_id = intent.getStringExtra("test_id");
        is_cart_added_val = intent.getStringExtra("is_cart_added");

        if (intent.hasExtra("code")) {
            code_val = intent.getStringExtra("code");
        }

        System.out.println("url-----" + url);
        System.out.println("test_id-----" + test_id);
        System.out.println("get Intent url-------------" + url);
        System.out.println("get Intent type-------------" + type);
        System.out.println("get Intent code_val-------------" + code_val);
        System.out.println("get Intent is_cart_added_val-------------" + is_cart_added_val);


        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        //------ getting Values ---------------------------

        //------------------ Title Bar ------------------------------------------------
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        mTitle.setTypeface(khandBold);
        mTitle.setText(type);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------


        webView = (ObservableWebView) findViewById(R.id.webview);
        tv_test_id = (TextView) findViewById(R.id.tv_test_id);
        tv_cart_count = (TextView) findViewById(R.id.tv_cart_count);
        tv_add_cart = (TextView) findViewById(R.id.tv_add_cart);
        menu_backet = (FrameLayout) findViewById(R.id.menu_backet);
        cart_layout = (LinearLayout) findViewById(R.id.cart_layout);

        tv_test_id.setText(test_id);

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
                                    Intent intent = new Intent(Labtest_WebViewActivity.this, ThankyouActivity.class);
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
        } else {

            //-------------------------------------------------------------------
            String get_Labtest = Model.BASE_URL + "sapp/viewLabTest?code=" + code_val + "&user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("get_Labtest---------" + get_Labtest);
            new JSON_getTestView().execute(get_Labtest);
            //-------------------------------------------------------------------
        }


        //--------- Listings ---------------------
        String full_url = Model.BASE_URL + "sapp/labTestCartCount?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("full_url-------------" + full_url);
        new JSON_cart_count().execute(full_url);
        //--------- Listings ---------------------


        webView.loadUrl(url);
        webView.setScrollViewCallbacks(this);


        cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Carting Button--------------");

               /* View parent = (View) v.getParent();
                View grand_parent = (View) parent.getParent();

                //Button btn_addtocart = (Button) grand_parent.findViewById(R.id.btn_addtocart);

                TextView tv_id = (TextView) grand_parent.findViewById(R.id.tv_id);

                String tvid = tv_id.getText().toString();

                System.out.println("tvid--------------" + tvid);*/


            }
        });

        tv_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Carting Button--------------");

                try {
                    jsonobj_addcart = new JSONObject();
                    jsonobj_addcart.put("user_id", (Model.id));
                    jsonobj_addcart.put("token", Model.token);
                    jsonobj_addcart.put("id", test_id);

                    System.out.println("jsonobj_addcart---" + jsonobj_addcart.toString());

                    new json_addcart().execute(jsonobj_addcart);

                } catch (Exception e2) {
                    e2.printStackTrace();
                }


            }
        });


        menu_backet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Labtest_WebViewActivity.this, Labtest_CartViewActivity.class);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        Labtest_WebViewActivity.this.finish();
                    }
                });
                startActivityForResult(intent, 1);

            }
        });

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

    private class JSON_getTestView extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Labtest_WebViewActivity.this);
            dialog.setMessage("Loading..");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                labtest_response = jParser.getJSONString(urls[0]);
                System.out.println("Family URL---------------" + urls[0]);

                return labtest_response;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            try {
                System.out.println("labtest_response------------------" + labtest_response);

                JSONObject jsonresp = new JSONObject(labtest_response);

                if (jsonresp.has("status")) {
                    String status_text = jsonresp.getString("status");

                    if (status_text.equals("1")) {

                    } else {
                        if (jsonresp.has("msg")) {
                            String msg_text = jsonresp.getString("msg");

                            Toast.makeText(Labtest_WebViewActivity.this, msg_text, Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                String strTest_text = jsonresp.getString("strTest");
                String isAddedCart_text = jsonresp.getString("isAddedCart");

                System.out.println("strTest_text------------" + strTest_text);
                System.out.println("isAddedCart_text------------" + isAddedCart_text);

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.setInitialScale(0);
                webView.getSettings().setDisplayZoomControls(true);

                strTest_text = strTest_text.replace("\\", "/");

                //strTest_text = "<link rel=\"stylesheet\" type=\"text/css\" ><span style=\"font-family:;\">" + strTest_text + "</span>";

                webView.loadDataWithBaseURL("", strTest_text, "text/html", "UTF-8", "");
                webView.setLongClickable(false);
                //----------------------------------------------------------------

                if (is_cart_added_val.equals("1")) {
                    tv_add_cart.setVisibility(View.GONE);
                } else {
                    tv_add_cart.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            dialog.cancel();
        }
    }

    private class JSON_cart_count extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("Cart List response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("str_response--------------" + str_response);
                Model.mNotificationCounter = Integer.parseInt((new JSONObject(str_response)).getString("count"));
                System.out.println("cart_size--------------" + Model.mNotificationCounter);

                if (Model.mNotificationCounter > 0) {
                    tv_cart_count.setVisibility(View.VISIBLE);
                    tv_cart_count.setText("" + Model.mNotificationCounter);
                } else {
                    tv_cart_count.setVisibility(View.GONE);
                }


/*                BadgeCounter.update(Labtest_tabs_Activity.this,
                        menu_new.findItem(R.id.nav_basket),
                        R.mipmap.basket_icon,
                        BadgeCounter.BadgeColor.RED,
                        Model.mNotificationCounter);*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class json_addcart extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Labtest_WebViewActivity.this);
            dialog.setMessage("Please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "addtocart");

                System.out.println("json_response_obj URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("json_response_obj-------------" + json_response_obj.toString());
                String count = json_response_obj.getString("item_count");
                tv_add_cart.setVisibility(View.GONE);

                System.out.println("count--------------" + count);

             /*   BadgeCounter.update(Labtest_tabs_Activity.this,
                        menu_new.findItem(R.id.nav_basket),
                        R.mipmap.basket_icon,
                        BadgeCounter.BadgeColor.RED,
                        count);*/

                Model.mNotificationCounter = Integer.parseInt(count);

                if (Model.mNotificationCounter > 0) {
                    tv_cart_count.setVisibility(View.VISIBLE);
                    tv_cart_count.setText(count);
                } else {
                    tv_cart_count.setVisibility(View.GONE);
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}