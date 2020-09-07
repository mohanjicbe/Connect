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

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class Inbox_view extends AppCompatActivity {

    public View vi;
    private FrameLayout li;
    JSONObject prepay_jobject, jsonobj, jsonobj1, jsonobj_offers;
    JSONArray jsonarray;
    public String msg_date, msg_sender, msg_to, msg_subject, msg_description, prepack_text, str_response, Log_Status, status_val, full_url, off_id, prep_inv_id, prep_inv_fee, prep_inv_strfee, off_label, strfee, strdesc1, strdesc2;
    LinearLayout parent_offer_layout;
    RelativeLayout full_layout;
    LinearLayout nolayout, offer_layout, netcheck_layout;
    ProgressBar progressBar;
    TextView tv_title, tv_desc, offer_title, off_desc, offid, tv_subject, tv_sender, tv_toaddress, tv_date;
    String qid;
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
        setContentView(R.layout.inbox_msg_view);


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

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);

        }

        FlurryAgent.onPageView();
        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Inbox_Msg_View");
        //----------------- Kissmetrics ----------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        try {
            Intent intent = getIntent();
            qid = intent.getStringExtra("qid");
            System.out.println("qid------------->" + qid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_subject = (TextView) findViewById(R.id.tv_subject);
        tv_sender = (TextView) findViewById(R.id.tv_sender);
        tv_toaddress = (TextView) findViewById(R.id.tv_toaddress);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

        //---------------- Custom default Font --------------------
        noto_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        noto_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tv_subject.setTypeface(noto_bold);
        tv_sender.setTypeface(noto_reg);
        tv_toaddress.setTypeface(noto_reg);
        tv_date.setTypeface(noto_reg);
        tv_desc.setTypeface(noto_reg);
        //---------------- Custom default Font --------------------

        //----------------------------------------------------
        full_url = Model.BASE_URL + "sapp/messageView?id=" + qid + "&user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("full_url-------------" + full_url);
        new JSON_offers_server().execute(full_url);
        //----------------------------------------------------

    }


    private class JSON_offers_server extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Inbox_view.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

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

                JSONObject jsononjsec = new JSONObject(str_response);

                msg_date = jsononjsec.getString("msg_date");
                msg_sender = jsononjsec.getString("msg_sender");
                msg_to = jsononjsec.getString("msg_to");
                msg_subject = jsononjsec.getString("msg_subject");
                msg_description = jsononjsec.getString("msg_description");

                System.out.println("msg_date-----" + msg_date);
                System.out.println("msg_sender-----" + msg_sender);
                System.out.println("msg_to-----" + msg_to);
                System.out.println("msg_subject-----" + msg_subject);
                System.out.println("msg_description-----" + msg_description);

                tv_subject.setText(msg_subject);
                tv_sender.setText(msg_sender);
                tv_toaddress.setText(msg_to);
                tv_date.setText(msg_date);

                tv_desc.setText(Html.fromHtml(msg_description));

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

            if ((Model.query_launch).equals("InboxMessage")) {
                Model.query_launch = "";
                Intent intent = new Intent(Inbox_view.this, InboxActivity.class);
                startActivity(intent);
                finish();
            } else {
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((Model.query_launch).equals("InboxMessage")) {
            Model.query_launch = "";
            Intent intent = new Intent(Inbox_view.this, InboxActivity.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }

    }

}
