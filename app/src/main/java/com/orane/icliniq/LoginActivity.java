package com.orane.icliniq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {

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
    public StringBuffer json_response = new StringBuffer();
    public String str_response, country, code_val, appsFlyerId, isvalid, userid, url, sub_url = "mobileajax/loginSubmit";
    Button btnlogin;
    MaterialEditText edtemail;
    ShowHidePasswordEditText edtpassword;
    TextView tvforgotpw;
    BufferedReader reader = null;
    JSONObject login_json, json;
    JSONObject login_jsonobj, jsonobj;
    LinearLayout btn_loginmobno, TextView;
    TextView requestreg;
    ImageView hosp_logo;
    SharedPreferences sharedpreferences;

    public static final String hospital_name = "hospital_name_key";
    public static final String hospital_domain = "hospital_domain_key";
    public static final String hospital_logo = "hospital_domain_key";
    public static final String base_url = "base_url_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FlurryAgent.onPageView();
        //------------ Initialization ---------------------------------------

        //------------------------------------------------------------------------------------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String hospital_name_text = sharedpreferences.getString(hospital_name, "");
        String hospital_domain_text = sharedpreferences.getString(hospital_domain, "");
        String hospital_logo_text = sharedpreferences.getString(hospital_logo, "");
        String base_url_text = sharedpreferences.getString(base_url, "");

        Model.hosp_name = hospital_name_text;
        Model.hosp_domain = hospital_domain_text;
        Model.hosp_logo = hospital_logo_text;
        Model.BASE_URL = base_url_text;
        //------------------------------------------------------------------------------------------

        hosp_logo = findViewById(R.id.hosp_logo);
        btnlogin = findViewById(R.id.btnlogin);
        edtemail = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassword);
        tvforgotpw = findViewById(R.id.tvforgotpw);
        requestreg = findViewById(R.id.requestreg);
        btn_loginmobno = findViewById(R.id.btn_loginmobno);
        //------------ Initialization ---------------------------------------

        try {
            Picasso.with(LoginActivity.this).load(Model.hosp_logo).placeholder(R.mipmap.hospital_logo).error(R.mipmap.hospital_logo).into(hosp_logo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Typeface font_bold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        Typeface font_normal = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);

        btnlogin.setTypeface(font_normal);
        edtemail.setTypeface(font_normal);
        edtpassword.setTypeface(font_normal);
        tvforgotpw.setTypeface(font_normal);

        ((TextView) findViewById(R.id.tv_otptext)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_forgottext)).setTypeface(font_normal);
        ((TextView) findViewById(R.id.requestreg)).setTypeface(font_normal);

        tvforgotpw.setText(Html.fromHtml(getResources().getString(R.string.forgotpw)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        try {
            //-------------------------------------------------------------------------
            String url = Model.BASE_URL + "sapp/country?token=" + Model.token + "&enc=1";
            System.out.println("url-------------" + url);
            new JSON_getCountry().execute(url);
            //-------------------------------------------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        edtpassword.setOnEditorActionListener(new MaterialEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnlogin.performClick();
                    return true;
                }
                return false;
            }
        });
*/
        btn_loginmobno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        LoginActivity.this.finish();
                    }
                });
                startActivityForResult(intent, 1);
            }
        });

        requestreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        LoginActivity.this.finish();
                    }
                });
                startActivityForResult(intent, 1);


/*                if (country.equals("IN")) {

                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            LoginActivity.this.finish();
                        }
                    });
                    startActivityForResult(intent, 1);
                } else {
                    //------ Signup Section--------------------------
                    ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent i = new Intent(LoginActivity.this, OTPSignupActivity.class);
                    i.putExtra("mobile", "");
                    i.putExtra("ccode", "");
                    startActivity(i);
                    finish();
                    //------ Signup Section--------------------------
                }*/
            }
        });
        tvforgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        LoginActivity.this.finish();
                    }
                });
                startActivityForResult(intent, 1);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = edtemail.getText().toString();
                String password = edtpassword.getText().toString();

                try {
                    if (uname.equals(""))
                        edtemail.setError("Please enter your mail id or mobile number");
                    else if (password.equals(""))
                        edtpassword.setError("Please enter your password");
                    else {

                        login_json = new JSONObject();
                        login_json.put("username", uname);
                        login_json.put("pwd", password);
                        login_json.put("user_type", "1");

                        if (new NetCheck().netcheck(LoginActivity.this)) {
                            new JSONAsyncTask_LoginSubmit().execute(login_json);
                        } else {
                            Toast.makeText(LoginActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
    }

    private class JSONAsyncTask_LoginSubmit extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Logging in, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "getLogin");

                System.out.println("Response Login---" + login_jsonobj.toString());
                System.out.println("URL----------" + urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                isvalid = login_jsonobj.getString("isValid");
                System.out.println("isvalid----------" + isvalid);

                if (isvalid.equals("true")) {

                    Model.isValid = "1";
                    Model.kmid = login_jsonobj.getString("kmid");
                    Model.name = login_jsonobj.getString("name");
                    Model.mcode = login_jsonobj.getString("m_code");
                    Model.mnum = login_jsonobj.getString("m_num");
                    Model.id = login_jsonobj.getString("id");
                    userid = login_jsonobj.getString("id");
                    Model.browser_country = login_jsonobj.getString("browser_country");
                    Model.email = login_jsonobj.getString("email");
                    Model.fee_q = login_jsonobj.getString("fee_q");
                    Model.fee_consult = login_jsonobj.getString("fee_consult");
                    Model.fee_q_inr = login_jsonobj.getString("fee_q_inr");
                    Model.fee_consult_inr = login_jsonobj.getString("fee_consult_inr");
                    Model.currency_symbol = login_jsonobj.getString("currency_symbol");
                    Model.currency_label = login_jsonobj.getString("currency_label");
                    Model.have_free_credit = login_jsonobj.getString("have_free_credit");
                    Model.photo_url = login_jsonobj.getString("photo_url");
                    Model.token = login_jsonobj.getString("token");
                    Model.refcode = login_jsonobj.getString("ref_code");

/*
                    Model.id = "6928";
                    userid = "6928";
                    Model.token = "20c1945eae4b9868cbbfd09675f7d76e-52943760576796b8ceabdba1e87bebd2";*/

                    if (login_jsonobj.has("has_free_follow")) {
                        Model.has_free_follow = login_jsonobj.getString("has_free_follow");
                    }

                    System.out.println("Model.browser_country----" + Model.browser_country);

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "1");
                    editor.putString(isValid, Model.isValid);
                    editor.putString(user_name, Model.name);
                    editor.putString(Name, Model.name);
                    editor.putString(id, userid);
                    editor.putString(browser_country, Model.browser_country);
                    editor.putString(email, Model.email);
                    editor.putString(fee_q, Model.fee_q);
                    editor.putString(fee_consult, Model.fee_consult);
                    editor.putString(fee_q_inr, Model.fee_q_inr);
                    editor.putString(fee_consult_inr, Model.fee_consult_inr);
                    editor.putString(currency_symbol, Model.currency_symbol);
                    editor.putString(currency_label, Model.currency_label);
                    editor.putString(have_free_credit, Model.have_free_credit);
                    editor.putString(photo_url, Model.photo_url);
                    editor.putString(sp_km_id, Model.kmid);
                    editor.putString(sp_mcode, Model.mcode);
                    editor.putString(sp_mnum, Model.mnum);
                    editor.putString(sp_has_free_follow, Model.has_free_follow);
                    editor.putString(token, Model.token);
                    editor.putString(chat_tip, "on");
                    editor.putString(ref_code, Model.refcode);
                    editor.apply();
                    //============================================================

                    try {
                        System.out.println("Model.mnum---------" + Model.mnum);

                        //----------- Flurry -------------------------------------------------
                        FlurryAgent.setUserId(userid);
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.patient.Country", Model.browser_country);
                        articleParams.put("android.patient.App_Version", (Model.App_ver));
                        articleParams.put("android.patient.token", Model.token);
                        articleParams.put("android.patient.Logwith", (edtemail.getText().toString() + "/" + (edtpassword.getText().toString())));
                        FlurryAgent.logEvent("android.patient.Login_Access_Success", articleParams);
                        //----------- Flurry -------------------------------------------------

                        //------------ Google firebase Analitics-----------------------------------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("Logwith", (edtemail.getText().toString() + "/" + (edtpassword.getText().toString())));
                        Model.mFirebaseAnalytics.logEvent("Login_Access_Success", params);
                        //------------ Google firebase Analitics---------------------------------------------

                    } catch (Exception t) {
                        t.printStackTrace();
                    }

                    Intent i = new Intent(LoginActivity.this, Connect_Home_Actvity.class);
                    startActivity(i);
                    finish();

                } else {

                    Toast.makeText(getApplicationContext(), "Incorrect Username or Password. Please enter your valid Username and Password", Toast.LENGTH_LONG).show();

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.App_Version", (Model.App_ver));
                    articleParams.put("android.patient.token", Model.token);
                    articleParams.put("android.patient.Logwith", (edtemail.getText().toString() + "/" + (edtpassword.getText().toString())));
                    FlurryAgent.logEvent("android.patient.Login_Access_Failed", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("Logwith", (edtemail.getText().toString() + "/" + (edtpassword.getText().toString())));
                    Model.mFirebaseAnalytics.logEvent("Login_Access_Failed", params);
                    //------------ Google firebase Analitics---------------------------------------------

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }


    private class JSON_getCountry extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*          dialog = new ProgressDialog(LoginActivity.this);
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

                    country = jsonobj.getString("country");
                    code_val = jsonobj.getString("code");

                    System.out.println("country----------" + country);
                    System.out.println("code_val----------" + code_val);

                    Model.browser_country = country;
                    Model.pat_country = country;
                    Model.country_code = code_val;
                }

            } catch (Exception e) {
                country = "";
                e.printStackTrace();
            }

            //dialog.cancel();

        }
    }


}