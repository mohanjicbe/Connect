package com.orane.icliniq;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

public class Manage_Subscriptions extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String update_status = "update_status_key";
    public static final String update_alert_time = "update_alert_time_key";
    SharedPreferences sharedpreferences;
    LinearLayout full_layout, nolayout;
    TextView subs_title, tv_cancel_url, tv_renew_url, tv_started_at, tv_enddate, tv_exp_in, tv_autorenewal, tv_next_renewable, tv_status;
    Button btn_submit, btn_cancel, btn_viewplans;
    JSONArray jsonarray;
    String str_response, title, cancelRenewableUrl_val;
    String urlRenewal_val;
    JSONObject jsononjsec, jsonobj1;
    ProgressBar progressBar;
    View vi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_subscription);

        //------------ Object Creations -----------------------------------------
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations ----------------------------------------------

        try {
            Model.kiss.record("android.Patient.ManageSubscription");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        //--------------------- Fonts Sett -------------------------------------------
        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);
        //--------------------- Fonts Sett -------------------------------------------

        full_layout = (LinearLayout) findViewById(R.id.full_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        btn_viewplans = (Button) findViewById(R.id.btn_viewplans);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //http://localhost/projects/icliniq/web/index.php/sapp/patientCurrentSubscriptionDet?user_id=58423&page=1&token=9bb798005b85d8c73ac4c821a729c33e-1f32aa4c9a1d2ea010adcf2348166a04

        //--------------------------------------------------
        String full_url = Model.BASE_URL + "sapp/patientCurrentSubscriptionDet?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("full_url-------------" + full_url);
        new JSON_offers_server().execute(full_url);
        //--------------------------------------------------

        btn_viewplans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manage_Subscriptions.this, SubscriptionPackActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class JSON_offers_server extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            full_layout.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("Server response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                jsonarray = new JSONArray(str_response);
                apply_offers(jsonarray);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void apply_offers(JSONArray jsonarray) {

        try {

            full_layout.removeAllViews();


            System.out.println("jsonarray.length()-----" + jsonarray.length());
            System.out.println("jsonarray-----" + jsonarray.toString());

            if (jsonarray.length() > 0) {

                for (int i = 0; i < jsonarray.length(); i++) {
                    jsononjsec = jsonarray.getJSONObject(i);

                    System.out.println("jsonobj_first-----" + jsononjsec.toString());

                    String title_val = jsononjsec.getString("title");
                    String startedAt_val = jsononjsec.getString("startedAt");
                    String status_val = jsononjsec.getString("status");
                    String autoRenewable_val = jsononjsec.getString("autoRenewable");
                    cancelRenewableUrl_val = jsononjsec.getString("cancelRenewableUrl");
                    String nextRenewal_val = jsononjsec.getString("nextRenewal");
                    String expiresIn_val = jsononjsec.getString("expiresIn");
                    String endDate_val = jsononjsec.getString("endDate");
                    String renewalTxt_val = jsononjsec.getString("renewalTxt");
                    urlRenewal_val = jsononjsec.getString("urlRenewal");

                    vi = getLayoutInflater().inflate(R.layout.subscriptions_row, null);
                    subs_title = (TextView) vi.findViewById(R.id.subs_title);
                    tv_started_at = (TextView) vi.findViewById(R.id.tv_started_at);
                    tv_next_renewable = (TextView) vi.findViewById(R.id.tv_next_renewable);
                    tv_enddate = (TextView) vi.findViewById(R.id.tv_enddate);
                    tv_exp_in = (TextView) vi.findViewById(R.id.tv_exp_in);
                    tv_autorenewal = (TextView) vi.findViewById(R.id.tv_autorenewal);
                    tv_status = (TextView) vi.findViewById(R.id.tv_status);
                    btn_submit = (Button) vi.findViewById(R.id.btn_submit);
                    btn_cancel = (Button) vi.findViewById(R.id.btn_cancel);
                    tv_cancel_url = (TextView) vi.findViewById(R.id.tv_cancel_url);
                    tv_renew_url = (TextView) vi.findViewById(R.id.tv_renew_url);

/*
                            //---------------- Custom default Font --------------------
                            Typeface robo_regular = Typeface.createFromAsset(getAssets(), Model.font_name);
                            Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                            offer_title.setTypeface(robo_bold);
                            off_desc.setTypeface(robo_regular);
                            //---------------- Custom default Font --------------------
*/

                    tv_cancel_url.setText(cancelRenewableUrl_val);
                    tv_renew_url.setText(urlRenewal_val);

                    //------------------------------------------
                    if (title_val != null && !title_val.isEmpty() && !title_val.equals("null") && !title_val.equals(""))
                        subs_title.setText(title_val);
                    else
                        subs_title.setVisibility(View.GONE);
                    //------------------------------------------

                    //------------------------------------------
                    if (startedAt_val != null && !startedAt_val.isEmpty() && !startedAt_val.equals("null") && !startedAt_val.equals("")) {
                        tv_started_at.setText(startedAt_val);
                        ((LinearLayout) vi.findViewById(R.id.tv_started_at_layout)).setVisibility(View.VISIBLE);
                    } else {
                        tv_started_at.setVisibility(View.GONE);
                        ((LinearLayout) vi.findViewById(R.id.tv_started_at_layout)).setVisibility(View.GONE);
                    }
                    //------------------------------------------

                    //------------------------------------------
                    if (nextRenewal_val != null && !nextRenewal_val.isEmpty() && !nextRenewal_val.equals("null") && !nextRenewal_val.equals("")) {
                        tv_next_renewable.setText(nextRenewal_val);
                    } else {
                        tv_next_renewable.setVisibility(View.GONE);
                        ((LinearLayout) vi.findViewById(R.id.tv_next_renewable_lay)).setVisibility(View.GONE);
                    }
                    //------------------------------------------

                    //------------------------------------------
                    if (endDate_val != null && !endDate_val.isEmpty() && !endDate_val.equals("null") && !endDate_val.equals("")) {
                        tv_enddate.setText(endDate_val);
                    } else {
                        tv_enddate.setVisibility(View.GONE);
                        ((LinearLayout) vi.findViewById(R.id.tv_enddate_lay)).setVisibility(View.GONE);
                    }
                    //------------------------------------------

                    //------------------------------------------
                    if (expiresIn_val != null && !expiresIn_val.isEmpty() && !expiresIn_val.equals("null") && !expiresIn_val.equals("")) {
                        tv_exp_in.setText(expiresIn_val);
                    } else {
                        tv_exp_in.setVisibility(View.GONE);
                        ((LinearLayout) vi.findViewById(R.id.tv_exp_lay)).setVisibility(View.GONE);
                    }
                    //------------------------------------------

                    //------------------------------------------
                    if (autoRenewable_val != null && !autoRenewable_val.isEmpty() && !autoRenewable_val.equals("null") && !autoRenewable_val.equals("")) {
                        tv_autorenewal.setText(autoRenewable_val);
                    } else {
                        tv_autorenewal.setVisibility(View.GONE);
                        ((LinearLayout) vi.findViewById(R.id.tv_autorenewal_lay)).setVisibility(View.GONE);
                    }
                    //------------------------------------------

                    //------------------------------------------
                    if (status_val != null && !status_val.isEmpty() && !status_val.equals("null") && !status_val.equals("")) {
                        tv_status.setText(status_val);
                        tv_status.setVisibility(View.VISIBLE);

                        if (status_val.equals("Active")) {
                            tv_status.setTextColor(getResources().getColor(R.color.green_800));
                        } else {
                            tv_status.setTextColor(getResources().getColor(R.color.red_700));
                        }

                    } else {
                        tv_status.setVisibility(View.GONE);
                    }
                    //------------------------------------------

                    //------------------------------------------
                    if (cancelRenewableUrl_val != null && !cancelRenewableUrl_val.isEmpty() && !cancelRenewableUrl_val.equals("null") && !cancelRenewableUrl_val.equals(""))
                        btn_cancel.setVisibility(View.VISIBLE);
                    else
                        btn_cancel.setVisibility(View.GONE);
                    //------------------------------------------

                    //------------------------------------------
                    if (urlRenewal_val != null && !urlRenewal_val.isEmpty() && !urlRenewal_val.equals("null") && !urlRenewal_val.equals(""))
                        btn_submit.setVisibility(View.VISIBLE);
                    else
                        btn_submit.setVisibility(View.GONE);
                    //------------------------------------------

                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent_fav = (View) v.getParent();
                            TextView tv_renew_url = (TextView) parent_fav.findViewById(R.id.tv_renew_url);
                            String renew_url = tv_renew_url.getText().toString();

                            //------------------------------
                            System.out.println("Get tv_renew_url URL: " + renew_url);
                            new JSON_proce_url().execute(renew_url);
                            //------------------------------

                            Intent intent = new Intent(Manage_Subscriptions.this, SubscriptionPackActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent_fav = (View) v.getParent();
                            TextView tv_cancel_url = (TextView) parent_fav.findViewById(R.id.tv_cancel_url);
                            String cancel_url = tv_cancel_url.getText().toString();

                            //--------------------------------------
                            System.out.println("Get Cancel URL: " + cancel_url);
                            new JSON_proce_url().execute(cancel_url);
                            //--------------------------------------

                            //--------------------------------------------------
                            String full_url = Model.BASE_URL + "sapp/patientCurrentSubscriptionDet?user_id=" + Model.id + "&token=" + Model.token;
                            System.out.println("full_url-------------" + full_url);
                            new JSON_offers_server().execute(full_url);
                            //--------------------------------------------------

                        }
                    });


                    full_layout.addView(vi);
                }

                nolayout.setVisibility(View.GONE);
                full_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            } else {
                nolayout.setVisibility(View.VISIBLE);
                full_layout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_proce_url extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("Server response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
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
