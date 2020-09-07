package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.zoom.Video_Calling_Activity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import me.drakeet.materialdialog.MaterialDialog;

public class Connect_Hosp_Select extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;

    JSONObject jsonobj;
    public static Connect_Hosp_Select otpinst;

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
    public static final String first_query = "first_query_key";
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_has_free_follow = "sp_has_free_follow_key";
    public static final String token = "token_key";
    public static final String chat_tip = "chat_tip_key";

    public static final String hospital_name = "hospital_name_key";
    public static final String hospital_domain = "hospital_domain_key";
    public static final String hospital_logo = "hospital_domain_key";
    public static final String base_url = "base_url_key";

    public static Connect_Hosp_Select instance() {
        return otpinst;
    }

    EditText edt_hosp_code;
    Button btn_submit;
    String hospiral_domain_text, str_response, Log_Status;


    @Override
    public void onStart() {
        super.onStart();
        otpinst = this;
    }

    Button btn_scan_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_hosp_select);

        FlurryAgent.onPageView();

        //------------------------------------------------------------------------------------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        String hospital_name_text = sharedpreferences.getString(hospital_name, "");
        String hospital_domain_text = sharedpreferences.getString(hospital_domain, "");
        String hospital_logo_text = sharedpreferences.getString(hospital_logo, "");
        String base_url_text = sharedpreferences.getString(base_url, "");

        Model.hosp_name = hospital_name_text;
        Model.hosp_domain = hospital_domain_text;
        Model.hosp_logo = hospital_logo_text;
        Model.BASE_URL = base_url_text;
        //------------------------------------------------------------------------------------------

        btn_scan_qr_code = (Button) findViewById(R.id.btn_scan_qr_code);
        edt_hosp_code = (EditText) findViewById(R.id.edt_hosp_code);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        //--------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Mobile Verification");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //--------------------------------------------------------------------

        btn_scan_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    IntentIntegrator integrator = new IntentIntegrator(Connect_Hosp_Select.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String hcode = edt_hosp_code.getText().toString();
                    if (hcode != null && !hcode.isEmpty() && !hcode.equals("null") && !hcode.equals("")) {

                        //------------------------- Url Pass -------------------------
                        //String get_hosp_url = "http://192.168.0.114/icliniq-git/web/index.php/mobileajax/hospitalInfo?slug=" + hcode;
                        String get_hosp_url = Model.Basic_server + "/mobileajax/hospitalInfo?slug=" + hcode;
                        System.out.println("get_hosp_url-----" + get_hosp_url);
                        new JSON_getHosp().execute(get_hosp_url);
                        //------------------------- Url Pass -------------------------

                    } else {
                        //Toast.makeText(Connect_Hosp_Select.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                        edt_hosp_code.requestFocus();
                        edt_hosp_code.setError("Enter valid code");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");

                System.out.println("Results---------" + result.getContents());
                String hcode = result.getContents();

                //show_hosp(hcode);
                //show_hosp("mnp");

                //------------------------- Url Pass -------------------------
                //String get_hosp_url = "http://192.168.0.114/icliniq-git/web/index.php/mobileajax/hospitalInfo?slug=" + hcode;
                String get_hosp_url = Model.Basic_server+  "mobileajax/hospitalInfo?slug=" + hcode;
                System.out.println("get_hosp_url-----" + get_hosp_url);
                new JSON_getHosp().execute(get_hosp_url);
                //------------------------- Url Pass -------------------------

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
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


    public void show_hosp() {

        try {

            final MaterialDialog alert = new MaterialDialog(Connect_Hosp_Select.this);
            View view = LayoutInflater.from(Connect_Hosp_Select.this).inflate(R.layout.connect_scanned_view, null);
            alert.setView(view);
            alert.setTitle("");
            alert.setCanceledOnTouchOutside(false);

            ImageView imgapp = (ImageView) view.findViewById(R.id.hosp_logo);
            TextView tv_hosp_name = (TextView) view.findViewById(R.id.hosp_name);
            TextView hosp_desc = (TextView) view.findViewById(R.id.hosp_desc);

            Picasso.with(getApplicationContext()).load(Model.hosp_logo).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(imgapp);
            tv_hosp_name.setText(Model.hosp_name);

            alert.setPositiveButton("Continue", new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (Log_Status.equals("1")) {

                        Intent i = new Intent(Connect_Hosp_Select.this, Connect_Home_Actvity.class);
                        i.putExtra("aff_id", Model.hosp_domain);
                        i.putExtra("id", Model.id);
                        i.putExtra("token", Model.token);
                        startActivity(i);
                        finishAffinity();

                    } else {

                        Intent i = new Intent(Connect_Hosp_Select.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                    alert.dismiss();
                }
            });

            alert.setNegativeButton("Cancel", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_getHosp extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Connect_Hosp_Select.this);
            dialog.setMessage("Please wait");
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

                if (jsonobj.has("status")) {
                    String status_val = jsonobj.getString("status");

                    if (status_val.equals("1")) {

                        String hosp_name = jsonobj.getString("name");
                        String hosp_logo = jsonobj.getString("logo");
                        String hosp_subdomain = jsonobj.getString("subdomain");

/*
                        String hospital_name_text = sharedpreferences.getString(hospital_name, "");
                        String hospital_domain_text = sharedpreferences.getString(hospital_domain, "");
                        String hospital_logo_text = sharedpreferences.getString(hospital_logo, "");
*/

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(hospital_name, hosp_name);
                        editor.putString(hospital_domain, hosp_subdomain);
                        editor.putString(hospital_logo, hosp_logo);

                        String BASE_URL = "https://" + hosp_subdomain + ".icliniq.com/";

                        editor.putString(base_url, BASE_URL);
                        editor.apply();
                        //============================================================

                        Model.hosp_name = hosp_name;
                        Model.hosp_domain = hosp_subdomain;
                        Model.hosp_logo = hosp_logo;
                        Model.BASE_URL = BASE_URL;

                        show_hosp();

                    } else {
                        edt_hosp_code.setError("Invalid hospital code");
                        edt_hosp_code.requestFocus();
                    }
                }

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //dialog.cancel();

        }
    }


}
