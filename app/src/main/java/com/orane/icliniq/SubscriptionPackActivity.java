package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.ShareIntent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionPackActivity extends AppCompatActivity implements View.OnClickListener {

    public View vi;
    private FrameLayout li;
    JSONObject prepay_jobject, jsonobj, jsonobj1, jsonobj_offers;
    JSONArray jsonarray;
    public String prepack_text, Log_Status, str_response, plan_name, per_month_val, billed_for, val_currency, val_for, full_url, off_id, prep_inv_id, prep_inv_fee, prep_inv_strfee, off_label, strfee, strdesc1, strdesc2;
    LinearLayout parent_offer_layout;
    RelativeLayout full_layout;
    LinearLayout nolayout, netcheck_layout;
    ProgressBar progressBar;
    RelativeLayout offer_layout;
    TextView offer_title, tv_price, off_desc, offid, tvtit1, tvtit2, tvtit3;
    Long startTime;

    private RadioGroup radioGroup;
    private CheckBox headerCheckBox;
    private CheckBox footerCheckBox;
    private CheckBox expandedCheckBox;
    public File imageFile;
    ScrollView scrollview;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
    public static final String isValid = "isValid";
    public static final String id = "id";
    public static final String browser_country = "browser_country";
    public static final String email = "email";
    public static final String fee_q = "fee_q";
    public static final String fee_consult = "fee_consult";
    public static final String fee_q_inr = "fee_q_inr";
    public static final String fee_consult_inr = "fee_consult_inr";
    public static final String currency_symbol = "currency_symbol";
    public static final String currency_label = "currency_label";
    public static final String have_free_credit = "have_free_credit";
    public static final String photo_url = "photo_url";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String first_query = "first_query_key";
    public static final String prepack = "prepack_key";

    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcription_packages);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        prepack_text = sharedpreferences.getString(prepack, "");
        //============================================================


        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //------------ Object Creations -------------------------------.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        parent_offer_layout = (LinearLayout) findViewById(R.id.parent_offer_layout);
        full_layout = (RelativeLayout) findViewById(R.id.full_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);

        tvtit1 = (TextView) findViewById(R.id.tvtit1);
        tvtit2 = (TextView) findViewById(R.id.tvtit2);
        tvtit3 = (TextView) findViewById(R.id.tvtit3);

        tvtit1.setText(Html.fromHtml("&bull Unlimited medical advice."));
        tvtit2.setText(Html.fromHtml("&bull Expert doctors from over 80 specialities."));
        tvtit3.setText(Html.fromHtml("&bull Save money. (A year of icliniq is <b>cheaper</b> than just a few queries)"));

        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Subscription_Packages");
        //----------------- Kissmetrics ----------------------------------





        if (isInternetOn()) {

            if (prepack_text != null && !prepack_text.isEmpty() && !prepack_text.equals("null") && !prepack_text.equals("")) {
                try {
                    str_response = prepack_text;
                    System.out.println("Local response--------------" + str_response);
                    prepay_jobject = new JSONObject(str_response);
                    apply_offers(prepay_jobject);


                    full_url = Model.BASE_URL + "sapp/listSubscription?user_id=" + Model.id + "&token=" + Model.token + "&enc=1";
                    System.out.println("full_url-------------" + full_url);
                    new JSON_offers_server().execute(full_url);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                //-------------------------------------------------------
                full_url = Model.BASE_URL + "sapp/listSubscription?user_id=" + Model.id + "&token=" + Model.token + "&enc=1";
                System.out.println("full_url-------------" + full_url);
                new JSON_offers_server().execute(full_url);
                //-------------------------------------------------------
            }

/*            full_url = Model.BASE_URL + "sapp/listSubscription?user_id=" + Model.id + "&token=" + Model.token + "&enc=1";
            new JSON_offers_server().execute(full_url);

            //new JSON_offers_local().execute(full_url);*/
        } else {

            progressBar.setVisibility(View.GONE);
            full_layout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(), "Internet is not connected", Toast.LENGTH_LONG).show();
        }
    }


    private class JSON_offers_server extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(prepack, str_response);
                editor.apply();
                //============================================================

/*              JSONParser jParser = new JSONParser();
                jsonobj_offers = jParser.getPrePack(urls[0]);

                Prepack_file.saveData(getApplicationContext(), jsonobj_offers.toString());

                System.out.println("URL------------------------ " + urls[0]);
                System.out.println("jsonobj_offers------------------------ " + jsonobj_offers.toString());*/

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {


                prepay_jobject = new JSONObject(str_response);

                if (prepay_jobject.has("token_status")) {
                    String token_status = prepay_jobject.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    apply_offers(prepay_jobject);
                }





              /*  if (jsonobj_offers.has("token_status")) {

                    String token_status = jsonobj_offers.getString("token_status");
                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(SubscriptionPackActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    if (jsonobj_offers != null && !jsonobj_offers.equals("null") && !jsonobj_offers.equals("")) {

                        apply_offers(jsonobj_offers);

                    } else {
                        System.out.println("jsonobj_offers-is--null");

                        progressBar.setVisibility(View.GONE);
                        full_layout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {

        try {
            View parent = (View) v.getParent();

            TextView tvid = (TextView) v.findViewById(R.id.tvoid);
            String tvidval = tvid.getText().toString();
            System.out.println("tvidval-----------------------------------" + tvidval);

            String url = Model.BASE_URL + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=subscribe&item_id=" + tvidval + "&token=" + Model.token + "&enc=1";
            System.out.println("Prep_Inv_url-----------" + url);
            new JSON_Prepare_inv().execute(url);

            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Subscribe_Invoice_View");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("Packid:", tvidval);
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------
            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("Packid:", tvidval);
            FlurryAgent.logEvent("android.patient.Subscribe_Invoice_View", articleParams);
            //----------- Flurry -------------------------------------------------

            //------------ Google firebase Analitics-----------------------------------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("Packid", tvidval);
            Model.mFirebaseAnalytics.logEvent("Subscribe_Invoice_View", params);
            //------------ Google firebase Analitics---------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(SubscriptionPackActivity.this);
            dialog.setMessage("Preparing Invoice. Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                jsonobj = new JSONObject(str_response);
                System.out.println("jsonobj---------------" + jsonobj.toString());


                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    prep_inv_id = jsonobj.getString("id");
                    prep_inv_fee = jsonobj.getString("fee");
                    prep_inv_strfee = jsonobj.getString("str_fee");

                    Model.query_launch = "prepack";

                    Intent intent = new Intent(SubscriptionPackActivity.this, Invoice_Page_New.class);
                    intent.putExtra("qid", "0");
                    intent.putExtra("inv_id", prep_inv_id);
                    intent.putExtra("inv_strfee", prep_inv_strfee);
                    intent.putExtra("type", "subscription");

                    startActivity(intent);

                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    dialog.cancel();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.prepack_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.nav_share) {
            //TakeScreenshot_Share();
            try {
                ShareIntent sintent = new ShareIntent();
                sintent.ShareApp(SubscriptionPackActivity.this, "SubscriptionPackActivity");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public final boolean isInternetOn() {


        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public void apply_offers(JSONObject jsonobj_offers) {

        try {
            parent_offer_layout.removeAllViews();

            jsonarray = new JSONArray();
            jsonarray.put(jsonobj_offers);

            System.out.println("jsonarray.length()-----" + jsonarray.length());
            System.out.println("jsonarray-----" + jsonarray.toString());

            if (jsonarray.length() > 0) {

                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobj1 = jsonarray.getJSONObject(i);
                    System.out.println("jsonobj_first-----" + jsonobj1.toString());

                    for (int j = 1; j <= 10; j++) {

                        String s = "" + j;
                        if (jsonobj1.has("" + s)) {

                            String thread = jsonobj1.getString("" + s);
                            System.out.println("thread-----" + thread);

                            JSONObject jsononjsec = new JSONObject(thread);

                            off_id = jsononjsec.getString("id");
                            plan_name = jsononjsec.getString("plan_name");
                            per_month_val = jsononjsec.getString("per_month_val");
                            billed_for = jsononjsec.getString("billed_for");


                            per_month_val = per_month_val.replace("$", "");

                            System.out.println("per_month_val-----" + per_month_val);
                            System.out.println("off_id-----" + off_id);
                            System.out.println("plan_name-----" + plan_name);
                            System.out.println("billed_for-----" + billed_for);

                            vi = getLayoutInflater().inflate(R.layout.subscription_offers_list, null);
                            tv_price = (TextView) vi.findViewById(R.id.tv_price);
                            TextView tv_month = (TextView) vi.findViewById(R.id.tv_month);
                            TextView tv_deci = (TextView) vi.findViewById(R.id.tv_deci);

                            offer_title = (TextView) vi.findViewById(R.id.offer_title);
                            off_desc = (TextView) vi.findViewById(R.id.offer_desc);
                            offid = (TextView) vi.findViewById(R.id.tvoid);
                            offer_layout = (RelativeLayout) vi.findViewById(R.id.offer_layout);

                            offer_title.setText(plan_name);
                            offid.setText(off_id);


                            //---------------------------------------

                            boolean isFound = per_month_val.contains(".");

                            System.out.println("per_month_val-----------" + per_month_val);
                            System.out.println("isFound-----------" + isFound);

                            if (isFound) {
                                String[] separated = per_month_val.split("\\.");
                                String int_val = separated[0]; // this will contain "Fruit"
                                String deci_val = separated[1]; // this will contain " they taste good"

                                System.out.println("int_val-----------" + int_val);
                                System.out.println("deci_val-----------" + deci_val);

                                tv_price.setText(int_val);
                                tv_deci.setText(deci_val);

                            } else {
                                tv_deci.setVisibility(View.GONE);
                                tv_price.setText(per_month_val);
                            }
                            //---------------------------------------

                            if (off_id.equals("1")) {
                                off_desc.setText(Html.fromHtml("Get unlimited queries on billed <b>" + billed_for + "</b>"));
                            } else if (off_id.equals("3")) {
                                off_desc.setText(Html.fromHtml("Get unlimited queries on billed <b>" + billed_for + "</b>"));
                            } else if (off_id.equals("4")) {
                                off_desc.setText(Html.fromHtml("Get unlimited queries on billed <b>" + billed_for + "</b>"));
                            }

                            offer_layout.setOnClickListener(SubscriptionPackActivity.this);

                            parent_offer_layout.addView(vi);

                        }
                    }
                }
               /* progressBar.setVisibility(View.GONE);
                full_layout.setVisibility(View.VISIBLE);
                nolayout.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.GONE);*/
            } else {
                /*progressBar.setVisibility(View.GONE);
                full_layout.setVisibility(View.GONE);
                nolayout.setVisibility(View.VISIBLE);
                netcheck_layout.setVisibility(View.GONE);*/
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
