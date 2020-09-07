package com.orane.icliniq.Deeplinks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.zoom.Consultation_View;

import java.util.HashMap;

public class DeepLinkActivityVideoCons extends Activity {

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
            //https://www.icliniq.com/meeting/zlink?id=36307&u=user
            Uri data = intent.getData();

            System.out.println("uri---------------" + data.toString());

            full_url = "" + data;

            String domain = "www.icliniq.com";
            boolean domain_check = full_url.toLowerCase().contains(domain.toLowerCase());

            if (domain_check) {
                String sub_str_msg = "zlink";
                title_check = full_url.toLowerCase().contains(sub_str_msg.toLowerCase());

                if (title_check) {

                    System.out.println("title_check-----------------TRUE");

                    String full_url = data.toString();
                    System.out.println("full_url-----------------" + full_url);

                    Uri uri = Uri.parse(full_url);
                    idval = uri.getQueryParameter("id");
                    System.out.println("idval------------" + idval);

                    if (Log_Status.equals("1")) {
                        try {
                            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                                //--------------------Kissmetrics--------------------------
                                HashMap<String, String> properties = new HashMap<String, String>();
                                Model.kiss.record("android.patient.DeepLink_Consultation_View");
                                properties.put("android.patient.Cons_id", idval);
                                Model.kiss.set(properties);
                                //--------------------Kissmetrics--------------------------

                                intent = new Intent(this, Consultation_View.class);
                                intent.putExtra("tv_cons_id", idval);
                                Model.query_launch = "PushNotificationService";
                                startActivity(intent);

                            } else {
                                System.out.println("Please Login the App------");
                                intent = new Intent(this, LoginActivity.class);
                                startActivity(intent);
                            }

                        } catch (Exception e2) {
                            e2.printStackTrace();
                            System.out.println("Exception--Video Call Satus------" + e2.toString());
                            intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        System.out.println("Please Login the App------");
                    }
                }
                else{
                    System.out.println("Exception--No LInk------");

                    finishAffinity();
                    System.out.println("data.toString()------------" + data.toString());
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(data.toString()));
                    startActivity(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}