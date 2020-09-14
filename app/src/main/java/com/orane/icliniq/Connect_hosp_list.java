package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import me.drakeet.materialdialog.MaterialDialog;

public class Connect_hosp_list extends AppCompatActivity {

    Button btn_started;
    String str_response;
    LinearLayout hosp_list_layout;
    JSONObject login_json, login_jsonobj, jsonobj;
    public MaterialDialog alert;

    public static Connect_hosp_list otpinst;

    public static Connect_hosp_list instance() {
        return otpinst;
    }

    @Override
    public void onStart() {
        super.onStart();
        otpinst = this;
    }

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

    ImageView img_add_icon, hosp_logo;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_hospital_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //--------------------------------------------------
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        mTitle.setTypeface(khandBold);
        //--------------------------------------------------

        //---------Tool bar-----------------------------------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //---------Tool bar-----------------------------------------


        //------------------------------------------------------------------------------------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String hospital_name_text = sharedpreferences.getString(hospital_name, "");
        String hospital_domain_text = sharedpreferences.getString(hospital_domain, "");
        String hospital_logo_text = sharedpreferences.getString(hospital_logo, "");
        String base_url_text = sharedpreferences.getString(base_url, "");

        System.out.println("hospital_name_text--------" + hospital_name_text);
        System.out.println("hospital_domain_text--------" + hospital_domain_text);
        System.out.println("hospital_logo_text--------" + hospital_logo_text);
        System.out.println("base_url_text--------" + base_url_text);

        img_add_icon = (ImageView) findViewById(R.id.img_add_icon);
        hosp_logo = (ImageView) findViewById(R.id.hosp_logo);
        hosp_list_layout = (LinearLayout) findViewById(R.id.hosp_list_layout);

        //------------------------------------------
        String url = Model.BASE_URL + "/sapp/myHospitals?user_id=" + (Model.id) + "&token=" + (Model.token) + "&enc=1";
        System.out.println("url---" + url);
        new Hosp_list_Async().execute(url);
        //-----------------------------------------

        Picasso.with(Connect_hosp_list.this).load(hospital_logo_text).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(hosp_logo);


        hosp_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    alert = new MaterialDialog(Connect_hosp_list.this);
                    View customLayout = LayoutInflater.from(Connect_hosp_list.this).inflate(R.layout.connect_hosp_scan, null);
                    alert.setView(customLayout);
                    //alert.setTitle("Add a hospital");
                    alert.setCanceledOnTouchOutside(false);

                    EditText edt_hosp_code = (EditText) customLayout.findViewById(R.id.edt_hosp_code);
                    ImageView img_qrcode = (ImageView) customLayout.findViewById(R.id.img_qrcode);


                    img_qrcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                IntentIntegrator integrator = new IntentIntegrator(Connect_hosp_list.this);
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

                    alert.setPositiveButton("Submit", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String hosp_domain_text = edt_hosp_code.getText().toString();

                            try {
                                login_json = new JSONObject();
                                login_json.put("subdomain", hosp_domain_text);

                                new AsyncTask_Post_Hosp().execute(login_json);

                            } catch (Exception e) {

                                e.printStackTrace();
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
        });


    }


    class Hosp_list_Async extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Connect_hosp_list.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                str_response = new JSONParser().getJSONString(params[0]);
                System.out.println("str_response--------------" + str_response);

                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                //----------------------------------------------------------
                Object json = new JSONTokener(str_response).nextValue();
                if (json instanceof JSONObject) {
                    System.out.println("This is JSON OBJECT---------------" + str_response);

                    JSONObject jobject = new JSONObject(str_response);

                    if (jobject.has("token_status")) {
                        String token_status = jobject.getString("token_status");
                        if (token_status.equals("0")) {

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //============================================================

                            finishAffinity();
                            Intent intent = new Intent(Connect_hosp_list.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);

                    hosp_list_layout.removeAllViews();

                    for (int i = 0; i < jsonarr.length(); i++) {

                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        System.out.println("jsonobj1------------" + jsonobj1.toString());

                        String hosp_name = jsonobj1.getString("name");
                        String logo_url_text = jsonobj1.getString("logo_url");
                        String subdomain_text = jsonobj1.getString("subdomain");
                        String status_val = jsonobj1.getString("status");

                        View recc_vi = getLayoutInflater().inflate(R.layout.connect_hospital_row, null);

                        ImageView hosp_logo = recc_vi.findViewById(R.id.hosp_logo);
                        TextView hosp_domain = recc_vi.findViewById(R.id.hosp_domain);

                        hosp_domain.setText(subdomain_text);

                        try {
                            Picasso.with(Connect_hosp_list.this).load(logo_url_text).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(hosp_logo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        hosp_list_layout.addView(recc_vi);
                    }

                }
                //----------------------------------------------------------

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hosp_select(View v) {

        try {


            TextView hosp_domain = v.findViewById(R.id.hosp_domain);
            String hosp_domain_text = hosp_domain.getText().toString();

            System.out.println("get hosp_domain_text-------- " + hosp_domain_text);

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("hosp_domain_text", hosp_domain_text);
            Model.mFirebaseAnalytics.logEvent("hosp_domain_text", params);
            //------------ Google firebase Analitics--------------------

            if (hosp_domain_text != null && !hosp_domain_text.isEmpty() && !hosp_domain_text.equals("null") && !hosp_domain_text.equals("")) {

                //------------------------- Url Pass -------------------------
                String get_hosp_url = Model.Basic_server + "mobileajax/hospitalInfo?slug=" + hosp_domain_text;
                System.out.println("get_hosp_url-----" + get_hosp_url);
                new JSON_getHosp().execute(get_hosp_url);
                //------------------------- Url Pass -------------------------

            } else {
                //Toast.makeText(Connect_Home_Actvity.this, "Sorry, Something went wrong. GO Back and Try again..!", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {

                String hcode = result.getContents();

                try {
                    login_json = new JSONObject();
                    login_json.put("subdomain", hcode);

                    new AsyncTask_Post_Hosp().execute(login_json);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class AsyncTask_Post_Hosp extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Connect_hosp_list.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "submit_hosp");

                System.out.println("Response ---" + login_jsonobj.toString());
                System.out.println("URL----------" + urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {


            try {
                String status_val = login_jsonobj.getString("status");

                if (status_val.equals("1")) {
                    alert.dismiss();

                    Toast.makeText(Connect_hosp_list.this, "New hospital has been added successfully", Toast.LENGTH_SHORT).show();

                    //------------------------------------------
                    String url = Model.BASE_URL + "/sapp/myHospitals?user_id=" + (Model.id) + "&token=" + (Model.token) + "&enc=1";
                    System.out.println("url---" + url);
                    new Hosp_list_Async().execute(url);
                    //-----------------------------------------

                } else {

                    Toast.makeText(Connect_hosp_list.this, "You are entered invalid hospital code.", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            dialog.dismiss();
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


    private class JSON_getHosp extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Connect_hosp_list.this);
            dialog.setMessage("Applying. Please wait");
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

                        Intent i = new Intent(Connect_hosp_list.this, Connect_Home_Actvity.class);
                        i.putExtra("aff_id", Model.hosp_domain);
                        i.putExtra("id", Model.id);
                        i.putExtra("token", Model.token);
                        startActivity(i);
                        finishAffinity();

                        //show_hosp();

                    } else {
                        Toast.makeText(Connect_hosp_list.this, "Invalid hospital code", Toast.LENGTH_SHORT).show();
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
