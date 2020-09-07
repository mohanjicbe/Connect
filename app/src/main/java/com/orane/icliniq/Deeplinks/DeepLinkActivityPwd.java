package com.orane.icliniq.Deeplinks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.orane.icliniq.ForgotPwdActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.SplashActivity;


public class DeepLinkActivityPwd extends Activity {

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
    public static final String update_status = "update_status_key";
    public static final String update_alert_time = "update_alert_time_key";

    public String full_url, Log_Status_val, uname, docname, pass, idval, Log_Status;
    public boolean pwd_str_check, title_check;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        uname = sharedpreferences.getString(user_name, "");
        docname = sharedpreferences.getString(Name, "");
        Model.name = sharedpreferences.getString(Name, "");
        pass = sharedpreferences.getString(password, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        Model.fee_q = sharedpreferences.getString(fee_q, "");
        Model.fee_consult = sharedpreferences.getString(fee_consult, "");
        Model.currency_label = sharedpreferences.getString(currency_label, "");
        Model.currency_symbol = sharedpreferences.getString(currency_symbol, "");
        Model.have_free_credit = sharedpreferences.getString(have_free_credit, "");
        Model.photo_url = sharedpreferences.getString(photo_url, "");
        Model.kmid = sharedpreferences.getString(sp_km_id, "");
        Model.first_query = sharedpreferences.getString(first_query, "no");
        //============================================================

        try {

            Intent intent = getIntent();
            //String action = intent.getAction();
            Uri data = intent.getData();

            System.out.println("uri---------------" + data.toString());

            full_url = "" + data;

            String domain = "www.icliniq.com";
            boolean domain_check = full_url.toLowerCase().contains(domain.toLowerCase());

            if (domain_check) {

                String pwd_str_msg = "resetpassword";
                pwd_str_check = full_url.toLowerCase().contains(pwd_str_msg.toLowerCase());

                if (pwd_str_check) {

                    System.out.println("pwd_str_check-----------------TRUE");

                    Intent i = new Intent(DeepLinkActivityPwd.this, ForgotPwdActivity.class);
                    i.putExtra("finisher", new ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            DeepLinkActivityPwd.this.finish();
                        }
                    });
                    startActivityForResult(i, 1);

                } else {
                    Intent i = new Intent(DeepLinkActivityPwd.this, SplashActivity.class);
                    startActivity(i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}