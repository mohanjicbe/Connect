package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

public class ForgotPwdActivity extends AppCompatActivity {

    Button btn_submit, btn_pwdsubmit, btn_code_submit;
    MaterialEditText edtemail, edt_confirmpwd;
    ShowHidePasswordEditText edt_newpwd;
    public String userid, status_text, code_txt, name_txt, email_text, status_txt, err_txt, newpwd_text, confirmpwd_text, user_id_txt, isvalid, pin_txt, edt_otp_text;
    JSONObject post_json, login_json, login_jsonobj, jsonobj_forgotpwd, jsonobj_pwd_submit, jsonobj_pinsubmit;
    LinearLayout AskMail_Layout, verify_layout, enterpwd_Layout;
    EditText edt_otp;
    TextView tv_mobno, tv_resend, tv_text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pwd);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //------- Initialize ------------------==========================================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Forgot Password");
        }
        //------------ Object Creations -------------------------------

        FlurryAgent.onPageView();

        //=========================================================
        Model.kiss.record("android.patient.ForgotPwd_Screen");
        //----------------------------------------------------------------------------

        //------- Initialize ------------------------------------------------------
        btn_submit = findViewById(R.id.btn_submit);
        btn_pwdsubmit = findViewById(R.id.btn_pwdsubmit);
        btn_code_submit = findViewById(R.id.btn_otp_submit);

        tv_mobno = findViewById(R.id.tv_mobno);
        tv_text = findViewById(R.id.tv_text);
        tv_resend = findViewById(R.id.tv_resend);
        edt_otp = findViewById(R.id.edt_otp);
        edtemail = findViewById(R.id.edtemail);
        edt_newpwd = findViewById(R.id.edt_newpwd);
        edt_confirmpwd = findViewById(R.id.edt_confirmpwd);
        AskMail_Layout = findViewById(R.id.AskMail_Layout);
        enterpwd_Layout = findViewById(R.id.enterpwd_Layout);
        verify_layout = findViewById(R.id.verify_layout);


        //-------------- Font --------------------
        Typeface font_bold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        Typeface font_normal = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);

        ((TextView) findViewById(R.id.tv_cpw1)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_resend)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_mobno)).setTypeface(font_bold);
        ((Button) findViewById(R.id.btn_pwdsubmit)).setTypeface(font_bold);
        ((Button) findViewById(R.id.btn_submit)).setTypeface(font_bold);
        ((Button) findViewById(R.id.btn_otp_submit)).setTypeface(font_bold);
        ((EditText) findViewById(R.id.edt_otp)).setTypeface(font_bold);
        ((EditText) findViewById(R.id.edt_newpwd)).setTypeface(font_bold);
        ((EditText) findViewById(R.id.edt_confirmpwd)).setTypeface(font_bold);
        ((EditText) findViewById(R.id.edtemail)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_tit1)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_tit2)).setTypeface(font_normal);
        ((TextView) findViewById(R.id.tv_text)).setTypeface(font_normal);
        ((TextView) findViewById(R.id.tv_pwd2)).setTypeface(font_normal);
        //-------------- Font --------------------

        AskMail_Layout.setVisibility(View.VISIBLE);
        enterpwd_Layout.setVisibility(View.GONE);
        verify_layout.setVisibility(View.GONE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_text = edtemail.getText().toString();
                System.out.println("forgot_email_text----" + email_text);

                if (Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {
                    try {

                        //------------ Google firebase Analitics-----------------------------------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", user_id_txt);
                        params.putString("email_text", email_text);
                        Model.mFirebaseAnalytics.logEvent("pwd_email_submit", params);
                        //------------ Google firebase Analitics-----------------------------------------------

                        post_json = new JSONObject();
                        post_json.put("email", email_text);
                        post_json.put("user_type", "1");

                        System.out.println("Forgot pwd json------------" + post_json.toString());

                        new Async_requestCode().execute(post_json);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    edtemail.setError("Please enter the valid email id");
                    edtemail.requestFocus();
                }

            }
        });


        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", user_id_txt);
                    params.putString("email_text", email_text);
                    Model.mFirebaseAnalytics.logEvent("resend_code", params);
                    //------------ Google firebase Analitics-----------------------------------------------

                    email_text = edtemail.getText().toString();
                    System.out.println("forgot_email_text----" + email_text);

                    if (Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {

                        try {
                            post_json = new JSONObject();
                            post_json.put("email", email_text);
                            post_json.put("user_type", "1");

                            System.out.println("Forgot pwd json------------" + post_json.toString());

                            new Async_requestCode().execute(post_json);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

/*
        edtemail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    btn_submit.performClick();

                    return true;
                }
                return false;
            }
        });
*/


        btn_code_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    edt_otp_text = edt_otp.getText().toString();

                    if (code_txt.equals(edt_otp_text)) {

                        System.out.println("Code is Matched-----------");

                        AskMail_Layout.setVisibility(View.GONE);
                        verify_layout.setVisibility(View.GONE);
                        enterpwd_Layout.setVisibility(View.VISIBLE);

                        try {

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.enter_code", edt_otp_text);
                            articleParams.put("android.patient.api_code", code_txt);
                            articleParams.put("android.patient.status", "matched");
                            FlurryAgent.logEvent("android.patient.pwd_code_submit", articleParams);
                            //----------- Flurry -------------------------------------------------

                            //------------ Google firebase Analitics-----------------------------------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("enter_code", edt_otp_text);
                            params.putString("api_code", code_txt);
                            params.putString("status", "matched");
                            Model.mFirebaseAnalytics.logEvent("pwd_code_submit", params);
                            //------------ Google firebase Analytics-----------------------------------------------

                        } catch (Exception t) {
                            t.printStackTrace();
                        }


                    } else {

                        Toast.makeText(ForgotPwdActivity.this, "Verification Code is incorrect", Toast.LENGTH_SHORT).show();

                        try {
                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.enter_code", edt_otp_text);
                            articleParams.put("android.patient.api_code", code_txt);
                            articleParams.put("android.patient.status", "Not-matched");
                            FlurryAgent.logEvent("android.patient.pwd_code_submit", articleParams);
                            //----------- Flurry -------------------------------------------------


                            //------------ Google firebase Analitics-----------------------------------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("enter_code", edt_otp_text);
                            params.putString("api_code", code_txt);
                            params.putString("status", "Not-matched");
                            Model.mFirebaseAnalytics.logEvent("pwd_code_submit", params);
                            //------------ Google firebase Analitics-----------------------------------------------

                        } catch (Exception t) {
                            t.printStackTrace();
                        }

                        System.out.println("The OTP you have entered is incorrect. Please enter a valid OTP.");
                        System.out.println("The OTP you have entered is incorrect. Please enter a valid OTP." + edt_otp_text);
                        System.out.println("code_txt-----------" + code_txt);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        edt_otp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_code_submit.performClick();
                    return true;
                }
                return false;
            }
        });


        btn_pwdsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newpwd_text = edt_newpwd.getText().toString();
                confirmpwd_text = edt_confirmpwd.getText().toString();

                System.out.println("newpwd_text----" + newpwd_text);
                System.out.println("confirmpwd_text----" + confirmpwd_text);

                if (newpwd_text.length() < 8 || !isValidPassword(newpwd_text)) {
                    System.out.println("Pwd is Not Valid");
                    edt_newpwd.requestFocus();
                    new AlertDialog.Builder(ForgotPwdActivity.this)
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            //.setTitle("InValid Password")
                            .setMessage("Please enter your Valid Password.Minimum 8 characters at least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character(@#$%^&*)")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            //.setNegativeButton("No", null)
                            .show();
                } else {

                    try {

                        if (newpwd_text.equals(confirmpwd_text)) {

                            post_json = new JSONObject();
                            post_json.put("pwd", newpwd_text);
                            post_json.put("user_id", user_id_txt);

                            System.out.println("Submit_pwd_json------------" + post_json.toString());

                            new Async_Submitpwd().execute(post_json);

                        } else {
                            System.out.println("Password is Entered passwords do not match------");
                            Toast.makeText(ForgotPwdActivity.this, "Those passwords don't match", Toast.LENGTH_SHORT).show();
                            edt_confirmpwd.setError("Entered passwords do not match");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        edt_confirmpwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_pwdsubmit.performClick();
                    return true;
                }
                return false;
            }
        });
    }


    private class Async_requestCode extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ForgotPwdActivity.this);
            dialog.setMessage("Sending verification code. Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_forgotpwd = jParser.JSON_POST(urls[0], "forgotpwd_request");

                System.out.println("jsonobj_forgotpwd_resoponse----------" + jsonobj_forgotpwd.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj_forgotpwd.has("token_status")) {
                    String token_status = jsonobj_forgotpwd.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(ForgotPwdActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    if (!jsonobj_forgotpwd.has("err")) {

                        Toast.makeText(getApplicationContext(), "Verification Code has been sent to your mail...", Toast.LENGTH_LONG).show();

                        name_txt = jsonobj_forgotpwd.getString("name");
                        user_id_txt = jsonobj_forgotpwd.getString("user_id");
                        code_txt = jsonobj_forgotpwd.getString("code");

                        tv_mobno.setText(email_text);

                        AskMail_Layout.setVisibility(View.GONE);
                        verify_layout.setVisibility(View.VISIBLE);
                        enterpwd_Layout.setVisibility(View.GONE);

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    } else {
                        err_txt = jsonobj_forgotpwd.getString("err");
                        email_notvalid(err_txt);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }


    private class Async_Submitpwd extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ForgotPwdActivity.this);
            dialog.setMessage("Resetting password. Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj_pwd_submit = jParser.JSON_POST(urls[0], "ConfirmPassword");

                System.out.println("jsonobj_Submit_pwd_resoponse----------" + jsonobj_pwd_submit.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj_pwd_submit.has("token_status")) {
                    String token_status = jsonobj_pwd_submit.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(ForgotPwdActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    status_text = jsonobj_pwd_submit.getString("status");
                    System.out.println("Status_text----------" + status_text);

                    if (status_text.equals("1")) {

                        try {
                            Model.kiss.record("android.patient.Password_Change_Success");
                            HashMap<String, String> properties = new HashMap<String, String>();
                            properties.put("android.patient.user_id", user_id_txt);
                            properties.put("android.patient.pwd", newpwd_text);
                            Model.kiss.set(properties);
                            //----------------------------------------------------------------------------
                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.user_id", user_id_txt);
                            articleParams.put("android.patient.pwd", newpwd_text);
                            FlurryAgent.logEvent("android.patient.Password_Change_Success", articleParams);
                            //----------- Flurry -------------------------------------------------

                        } catch (Exception t) {
                            System.out.println("Exception-first--" + t.toString());
                            t.printStackTrace();
                        }

                        success();

                    } else {

                        Toast.makeText(getApplicationContext(), "Password Submit failed. Try Again...", Toast.LENGTH_LONG).show();

                        try {
                            Model.kiss.record("android.patient.Password_Change_Failed");
                            HashMap<String, String> properties = new HashMap<String, String>();
                            properties.put("android.patient.user_id", user_id_txt);
                            properties.put("android.patient.pwd", newpwd_text);
                            Model.kiss.set(properties);
                            //----------------------------------------------------------------------------

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.user_id", user_id_txt);
                            articleParams.put("android.patient.pwd", newpwd_text);
                            FlurryAgent.logEvent("android.patient.Password_Change_Failed", articleParams);
                            //----------- Flurry -------------------------------------------------


                        } catch (Exception t) {
                            System.out.println("Exception-first--" + t.toString());
                            t.printStackTrace();
                        }

                    }
                }

            } catch (Exception e) {
                System.out.println("Exception--Forgot pwd----" + e.toString());
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }

    public void success() {

        final MaterialDialog alert = new MaterialDialog(ForgotPwdActivity.this);
        alert.setTitle("Success..!");
        alert.setMessage("Password reset successful");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    login_json = new JSONObject();
                    login_json.put("username", user_id_txt);
                    login_json.put("pwd", confirmpwd_text);
                    login_json.put("user_type", "1");

                    new JSON_Login().execute(login_json);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                alert.dismiss();
            }
        });

        alert.show();
    }

    public void email_notvalid(String err) {

        final MaterialDialog alert = new MaterialDialog(ForgotPwdActivity.this);
        alert.setTitle("Oops..!");
        alert.setMessage(err);
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
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

    private class JSON_Login extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ForgotPwdActivity.this);
            dialog.setMessage("Logging in, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "getLogin");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("login_jsonobj---------" + login_jsonobj.toString());

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

                    if (login_jsonobj.has("token")) {
                        Model.token = login_jsonobj.getString("token");
                    }

                    if (login_jsonobj.has("has_free_follow")) {
                        Model.has_free_follow = login_jsonobj.getString("has_free_follow");
                    }

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "1");
                    editor.putString(isValid, Model.isValid);
                    editor.putString(user_name, Model.email);
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
                    editor.apply();
                    //============================================================

                    try {
                        System.out.println("Model.mnum---------" + Model.mnum);

                        Model.kiss.record("android.patient.pwd_Login_Access_Success");
                        //Model.kiss.identify(Model.kmid);om
                        Model.kiss.identify(Model.kmid);
                        HashMap<String, String> properties = new HashMap<String, String>();
                        properties.put("android.patient.Country", Model.browser_country);
                        properties.put("android.patient.App_Version", (Model.App_ver));
                        properties.put("android.patient.Logwith", user_id_txt + "/" + confirmpwd_text);
                        Model.kiss.set(properties);
                        //----------------------------------------------------------------------------

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.patient.Country", Model.browser_country);
                        articleParams.put("android.patient.App_Version", (Model.App_ver));
                        articleParams.put("android.patient.Logwith", user_id_txt + "/" + confirmpwd_text);
                        FlurryAgent.logEvent("android.patient.pwd_Login_Access_Success", articleParams);
                        //----------- Flurry -------------------------------------------------

                        //------------ Google firebase Analitics-----------------------------------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("Logwith", user_id_txt + "/" + confirmpwd_text);
                        Model.mFirebaseAnalytics.logEvent("pwd_Login_Access_Success", params);
                        //------------ Google firebase Analitics-----------------------------------------------

                    } catch (Exception t) {
                        System.out.println("Kissmetrics Exception-first--" + t.toString());
                        t.printStackTrace();
                    }

                    ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());
                    Intent i = new Intent(ForgotPwdActivity.this, Connect_Home_Actvity.class);
                    startActivity(i);
                    finish();

                } else {

                    Toast.makeText(getApplicationContext(), "Login failed. Please try again.", Toast.LENGTH_LONG).show();

                    //============================================================================

                    Model.kiss.record("android.patient.pwd_Login_Access_Failed");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.patient.App_Version", (Model.App_ver));
                    properties.put("android.patient.Logwith", user_id_txt + "/" + confirmpwd_text);
                    Model.kiss.set(properties);
                    //----------------------------------------------------------------------------

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.App_Version", (Model.App_ver));
                    articleParams.put("android.patient.Logwith", user_id_txt + "/" + confirmpwd_text);
                    FlurryAgent.logEvent("android.patient.pwd_Login_Access_Failed", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("Logwith", user_id_txt + "/" + confirmpwd_text);
                    Model.mFirebaseAnalytics.logEvent("pwd_Login_Access_Failed", params);
                    //------------ Google firebase Analitics-----------------------------------------------
                }

            } catch (Exception e) {
                System.out.println("Exception--Login--------" + e.toString());
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


}
