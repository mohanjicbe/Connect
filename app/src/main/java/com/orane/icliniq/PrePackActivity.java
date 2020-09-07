package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.orane.icliniq.network.ShareIntent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class PrePackActivity extends AppCompatActivity implements View.OnClickListener {

    public View vi;
    private FrameLayout li;
    JSONObject prepay_jobject, jsonobj, jsonobj1, jsonobj_offers;
    JSONArray jsonarray;
    public String prepack_text, str_response, Log_Status, status_val, full_url, off_id, prep_inv_id, prep_inv_fee, prep_inv_strfee, off_label, strfee, strdesc1, strdesc2;
    LinearLayout parent_offer_layout;
    RelativeLayout full_layout;
    LinearLayout nolayout, offer_layout, netcheck_layout;
    ProgressBar progressBar;
    TextView tv_title, tv_desc, offer_title, off_desc, offid;
    Long startTime;

    private RadioGroup radioGroup;
    private CheckBox headerCheckBox;
    private CheckBox footerCheckBox;
    private CheckBox expandedCheckBox;
    public File imageFile;
    ObservableScrollView scrollview;
    Typeface noto_reg, noto_bold;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prepaid_packages);


        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);

        }

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        prepack_text = sharedpreferences.getString(prepack, "");
        //============================================================

        noto_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        noto_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_desc);


        scrollview = (ObservableScrollView) findViewById(R.id.scrollview);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        parent_offer_layout = (LinearLayout) findViewById(R.id.parent_offer_layout);
        full_layout = (RelativeLayout) findViewById(R.id.full_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);



        //------------ Object Creations -------------------------------.
        FlurryAgent.onPageView();
        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Prepaid_Packages");
        //----------------- Kissmetrics ----------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //---------------- Custom default Font --------------------
        Typeface robo_regular = Typeface.createFromAsset(getAssets(), Model.font_name);
        tv_title.setTypeface(robo_regular);
        tv_desc.setTypeface(robo_regular);
        //---------------- Custom default Font --------------------


        if (new NetCheck().netcheck(PrePackActivity.this)) {

            if (prepack_text != null && !prepack_text.isEmpty() && !prepack_text.equals("null") && !prepack_text.equals("")) {
                try {
                    str_response = prepack_text;
                    System.out.println("Local response--------------" + str_response);
                    prepay_jobject = new JSONObject(str_response);
                    apply_offers(prepay_jobject);

                    //------------------------------------------
                    full_url = Model.BASE_URL + "sapp/listPrepaid?user_id=" + Model.id + "&token=" + Model.token;
                    System.out.println("full_url-------------" + full_url);
                    new JSON_offers_server().execute(full_url);
                    //------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                //----------------------------------------------------------
                full_url = Model.BASE_URL + "sapp/listPrepaid?user_id=" + Model.id + "&token=" + Model.token;
                System.out.println("full_url-------------" + full_url);
                new JSON_offers_server().execute(full_url);
                //----------------------------------------------------------
            }


        } else {

            progressBar.setVisibility(View.GONE);
            full_layout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);

            Toast.makeText(PrePackActivity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
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
                System.out.println("Server response--------------" + str_response);

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(prepack, str_response);
                editor.apply();
                //============================================================

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {

        try {
            View parent = (View) v.getParent();

            TextView tvid = (TextView) parent.findViewById(R.id.tvoid);
            String tvidval = tvid.getText().toString();
            System.out.println("tvidval-----------------------------------" + tvidval);

            //--------------- Prepare Invoice ------------------------
            String url = Model.BASE_URL + "sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=prepay&item_id=" + tvidval + "&token=" + Model.token;
            System.out.println("Prep_Inv_url-----------" + url);
            new JSON_Prepare_inv().execute(url);
            //--------------- Prepare Invoice ------------------------

            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Prepaid_Invoice_View");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("Packid:", tvidval);
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------

   /*         //------------ Google firebase Analitics-----------------------------------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("Packid", tvidval);
            Model.mFirebaseAnalytics.logEvent("Prepaid_Invoice_View", params);
            //------------ Google firebase Analitics---------------------------------------------*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(PrePackActivity.this);
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

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(PrePackActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    if (jsonobj.has("status")) {
                        status_val = jsonobj.getString("status");
                        if (status_val.equals("0")) {

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //============================================================

                            dialog.dismiss();

                            finishAffinity();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {

                        prep_inv_id = jsonobj.getString("id");
                        prep_inv_fee = jsonobj.getString("fee");
                        prep_inv_strfee = jsonobj.getString("str_fee");

                        System.out.println("Model.prep_inv_id---------------" + prep_inv_id);
                        System.out.println("Model.prep_inv_fee---------------" + prep_inv_fee);
                        System.out.println("Model.prep_inv_strfee---------------" + prep_inv_strfee);

                        Model.query_launch = "prepack";

                        Intent intent = new Intent(PrePackActivity.this, Invoice_Page_New.class);
                        intent.putExtra("qid", "0");
                        intent.putExtra("inv_id", prep_inv_id);
                        intent.putExtra("inv_strfee", prep_inv_strfee);
                        intent.putExtra("type", "prepaid");
                        startActivity(intent);

                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }
                }

                dialog.dismiss();

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
                sintent.ShareApp(PrePackActivity.this, "PrePackActivity");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
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
                            off_label = jsononjsec.getString("label");
                            strfee = jsononjsec.getString("str_fee");
                            strdesc1 = jsononjsec.getString("description_1");
                            strdesc2 = jsononjsec.getString("description_2");

                            System.out.println("off_id-----" + off_id);
                            System.out.println("off_label-----" + off_label);
                            System.out.println("strfee-----" + strfee);
                            System.out.println("desc1-----" + strdesc1);
                            System.out.println("desc2-----" + strdesc2);

                            vi = getLayoutInflater().inflate(R.layout.offers_list, null);
                            offer_title = (TextView) vi.findViewById(R.id.offer_title);
                            off_desc = (TextView) vi.findViewById(R.id.offer_desc);
                            offid = (TextView) vi.findViewById(R.id.tvoid);
                            offer_layout = (LinearLayout) vi.findViewById(R.id.offer_layout);

                            //---------------- Custom default Font --------------------
                            Typeface robo_regular = Typeface.createFromAsset(getAssets(), Model.font_name);
                            Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                            offer_title.setTypeface(robo_bold);
                            off_desc.setTypeface(robo_regular);
                            //---------------- Custom default Font --------------------

                            offer_title.setText(off_label);
                            off_desc.setText(Html.fromHtml("<strong><font color=\"#FA431C\">Add " + strfee + "</font></strong> get <b><font color=\"#CB04BC\">" + strdesc1 + "</font></b> " + strdesc2));
                            offid.setText(off_id);

                            offer_layout.setOnClickListener(PrePackActivity.this);

                            parent_offer_layout.addView(vi);

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
