package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;

import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONObject;


public class SplashActivity extends FragmentActivity {

    public StringBuffer json_response = new StringBuffer();
    public String url;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String app_first_open = "app_first_open_key";
    public static final String token = "token_key";
    public static final String browser_country = "browser_country";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String isValid = "isValid";
    public static final String id = "id";
    public static final String email = "email";
    public static final String fee_q = "fee_q";
    public static final String fee_consult = "fee_consult";
    public static final String fee_q_inr = "fee_q_inr";
    public static final String fee_consult_inr = "fee_consult_inr";
    public static final String currency_symbol = "currency_symbol";
    public static final String currency_label = "currency_label";
    public static final String have_free_credit = "have_free_credit";
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_has_free_follow = "sp_has_free_follow_key";
    public static final String chat_tip = "chat_tip_key";

    public static final String affiliated_id = "affiliated_id_key";
    public static final String base_url = "base_url_key";
    public static final String hospital_name = "hospital_name_key";
    public static final String hospital_domain = "hospital_domain_key";
    public static final String hospital_logo = "hospital_domain_key";


    public String uname, country_code_no, country, country_url, str_response, user_id_val, pass, Log_Status;
    Boolean isInternetPresent = false;
    JSONObject jsonobj;
    public String app_first_open_status;
    private static final String SHOWCASE_ID = "simple example";
    //ParticleView mPvGithub;

    LinearLayout splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((Model.app_vendor_type).equals("SaaS")) {
            setContentView(R.layout.splash);
        } else {
            setContentView(R.layout.splash);
        }

        getWindow().setBackgroundDrawable(null);


        //-------- Initialization -----------------------------------------------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        app_first_open_status = sharedpreferences.getString(app_first_open, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.token = sharedpreferences.getString(token, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.name = sharedpreferences.getString(Name, "");
        //------------------------------------------------------------------------------------------

        String hospital_name_text = sharedpreferences.getString(hospital_name, "");
        String hospital_logo_text = sharedpreferences.getString(hospital_logo, "");
        String hospital_domain_text = sharedpreferences.getString(hospital_domain, "");
        Model.affiliated_id = hospital_domain_text;
        Model.BASE_URL = sharedpreferences.getString(base_url, "");



        System.out.println("MModel.id---------------------" + Model.id);
        System.out.println("MModel.name---------------------" + Model.name);
        System.out.println("Model.email------------------" + Model.email);
        System.out.println("Model.token------------------" + Model.token);
        System.out.println("Model.affiliated_id------------------" + Model.affiliated_id);

        //Model.first_time = "Yes";
        //-------- Initialization -----------------------------------------------------

        //FirebaseApp.initializeApp(getApplicationContext());


    /*    try {

            if (user_id_val != null && !user_id_val.isEmpty() && !user_id_val.equals("null") && !user_id_val.equals("")) {
                country_url = Model.BASE_URL + "sapp/country?track=true&user_id=" + user_id_val + "&token=" + Model.token;
            } else {
                country_url = Model.BASE_URL + "sapp/country?track=true&token=" + Model.token + "&user_id=";
            }

            //---------------------------------------------------------
            System.out.println("country_url-------------" + country_url);
            new JSON_getCountry().execute(country_url);
            //---------------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        full_process();

    }

    public void process_code() {

        try {
            if (Log_Status.equals("1")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //new GetVersionCode().execute();

                        Intent i = new Intent(SplashActivity.this, Connect_Home_Actvity.class);
                        startActivity(i);
                        finish();


                    }
                }, 3000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (!app_first_open_status.equals("no")) {

                                System.out.println("app_first_open_status--------------------" + app_first_open_status);

                                Intent i = new Intent(SplashActivity.this, IntroScreenActivity.class);
                                i.putExtra("screen_launch", "splash");
                                startActivity(i);
                                finish();

                            } else {

                                //============================================================
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(app_first_open, "no");
                                editor.apply();
                                //============================================================

                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void full_process() {

        try {
            Log_Status = sharedpreferences.getString(Login_Status, "");
            uname = sharedpreferences.getString(user_name, "");
            pass = sharedpreferences.getString(password, "");
            Model.browser_country = sharedpreferences.getString(browser_country, "");
            //Model.affiliated_id = sharedpreferences.getString(affiliated_id, "");

            System.out.println("Log_Status-------------" + Log_Status);
            System.out.println("uname-------------" + uname);
            System.out.println("pass-------------" + pass);
            System.out.println(" Model.browser_country-------------" + Model.browser_country);
            System.out.println(" Model.affiliated_id-------------" + Model.affiliated_id);


            System.out.println("Model.app_vendor_type------------" + Model.app_vendor_type);

            if ((Model.app_vendor_type).equals("SaaS")) {
                sass_process_code();
            } else if ((Model.app_vendor_type).equals("icliniq")) {
                process_code();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sass_process_code() {

        System.out.println("affiliated_id-----" + Model.affiliated_id);
        String aff_id = Model.affiliated_id;


        System.out.println("Log_Status-----------" + Log_Status);

        try {

            if (Log_Status.equals("1")) {

                if (aff_id != null && !aff_id.isEmpty() && !aff_id.equals("null") && !aff_id.equals("")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent i = new Intent(SplashActivity.this, Connect_Home_Actvity.class);
                            i.putExtra("aff_id", aff_id);
                            i.putExtra("id", Model.id);
                            i.putExtra("token", Model.token);
                            startActivity(i);
                            finish();

                        }
                    }, 3000);
                } else {

                    Intent i = new Intent(SplashActivity.this, Connect_Hosp_Select.class);
                    i.putExtra("aff_id", "");
                    i.putExtra("id", Model.id);
                    i.putExtra("token", Model.token);
                    startActivity(i);
                    finish();
                }

            } else {

                Intent i = new Intent(SplashActivity.this, Connect_get_started.class);
                i.putExtra("screen_launch", "splash");
                startActivity(i);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_getCountry extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            dialog = new ProgressDialog(OTPActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);*/
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

                country = jsonobj.getString("country");
                country_code_no = jsonobj.getString("code");

                System.out.println("country----------" + country);
                System.out.println("country_code_no----------" + country_code_no);

                Model.pat_country = country;
                Model.browser_country = country;

            } catch (Exception e) {
                e.printStackTrace();
            }
            //dialog.cancel();

        }
    }


   /* //********* Check for Updated Version of APP
    private class GetVersionCode extends AsyncTask<Void, String, String> {
        String currentVersion = null;

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".xyOfqd .hAyfc:nth-child(4) .htlgb span")
                        .get(0)
                        .ownText();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("newVersion---- " + newVersion);
            return newVersion;
        }

        @Override
        protected void onPostExecute(String onlineVersion) {

            System.out.println("onlineVersion---- " + onlineVersion);
            System.out.println("Current Version ---- " + Model.App_ver);

            if (!onlineVersion.equals(Model.App_ver)) {

                final MaterialDialog alert = new MaterialDialog(SplashActivity.this);
                alert.setTitle("Update is available..!");
                alert.setMessage("Latest update (" + onlineVersion + ") is available. Do you want to update right now?");
                alert.setCanceledOnTouchOutside(false);
                alert.setPositiveButton("Update", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String url = "https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en_US";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        finishAffinity();
                    }
                });

                alert.setNegativeButton("Later", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(SplashActivity.this, Connect_Home_Actvity.class);
                        startActivity(i);
                        finish();

                        alert.dismiss();
                    }
                });

                alert.show();
            }
            else{
                Intent i = new Intent(SplashActivity.this, Connect_Home_Actvity.class);
                startActivity(i);
                finish();
            }

        }
    }*/
}
