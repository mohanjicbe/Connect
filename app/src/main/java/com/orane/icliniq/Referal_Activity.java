package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class Referal_Activity extends AppCompatActivity {

    ArrayAdapter<String> dataAdapter = null;
    Map<String, String> tz_map = new HashMap<String, String>();
    public String params, promo_code_response, status_val, err_val, tz_val;
    Toolbar toolbar;
    ListView listView;
    List<String> categories;
    TextView tv_ref_code;
    Button btn_submit;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
    public static final String bcountry = "bcountry_key";
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
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_has_free_follow = "sp_has_free_follow_key";
    public static final String token = "token_key";
    public static final String chat_tip = "chat_tip_key";
    public static final String ref_code = "ref_code_key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_layout);

        //------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        btn_submit = (Button) findViewById(R.id.btn_submit);
        tv_ref_code = (TextView) findViewById(R.id.tv_ref_code);

        System.out.println(" Model.refcode--------------" + Model.refcode);

        //================ Shared Pref ===============================
        sharedpreferences = Referal_Activity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.refcode = sharedpreferences.getString(ref_code, "");
        //============================================================

        Model.query_launch = "";


        if ((Model.refcode) != null && !(Model.refcode).isEmpty() && !(Model.refcode).equals("null") && !(Model.refcode).equals("")) {

            tv_ref_code.setText(Model.refcode);
            btn_submit.setText("Share Promo Code");

        } else {
            tv_ref_code.setText("XXXXX");
            btn_submit.setText("Get Promo Code");

            //-------------------------------------------------------------------
            String get_family_url = Model.BASE_URL + "sapp/getPromoCode?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("get_family_url---------" + get_family_url);
            new JSON_getPromo().execute(get_family_url);
            //-------------------------------------------------------------------
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((Model.refcode) != null && !(Model.refcode).isEmpty() && !(Model.refcode).equals("null") && !(Model.refcode).equals("")) {
                    String share_text = "Rs.50 OFF on ur first #icliniq online doctor advice. Use my promo code " + Model.refcode + ". Redeem it now @icliniq";
                    Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                    txtIntent.setType("text/plain");
                    txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, share_text);
                    startActivity(Intent.createChooser(txtIntent, "Share"));

                } else {

                    final MaterialDialog alert = new MaterialDialog(Referal_Activity.this);
                    alert.setTitle("Get Promo code");
                    alert.setMessage(err_val);
                    alert.setCanceledOnTouchOutside(false);
                    alert.setPositiveButton("Yes, Proceed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(Referal_Activity.this, OTPActivity_PromoCode.class);
                            startActivity(i);

                            alert.dismiss();
                        }
                    });


                    alert.setNegativeButton("Not Now", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });

                    alert.show();

                }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return true;
    }


    @Override
    public void onBackPressed() {

        finish();
    }


    private class JSON_getPromo extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Referal_Activity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                promo_code_response = jParser.getJSONString(urls[0]);

                System.out.println("Family URL---------------" + urls[0]);

                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            System.out.println("promo_code_response---------------" + promo_code_response);

            try {
                JSONObject jobj = new JSONObject(promo_code_response);

                status_val = jobj.getString("status");

                if (status_val.equals("0")) {
                    err_val = jobj.getString("err");
                } else {

                    String code_val = jobj.getString("code");

                    tv_ref_code.setText(code_val);
                    btn_submit.setText("Share Promo Code");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            dialog.cancel();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Model.query_launch.equals("otp_success")) {
            //-------------------------------------------------------------------
            String get_family_url = Model.BASE_URL + "sapp/getPromoCode?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("get_family_url---------" + get_family_url);
            new JSON_getPromo().execute(get_family_url);
            //-------------------------------------------------------------------
        }
    }

}
