package com.orane.icliniq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;

import me.drakeet.materialdialog.MaterialDialog;


public class Consultation_Home extends AppCompatActivity {

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

    public Intent intent;
    public String Log_Status_val;
    Button btn_bookcons;
    TextView tv_tit, tv_tit1, tv_tit2, tv_tit3, tv_tit4, tv_tit5, tv_tit6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation_home);

        FlurryAgent.onPageView();

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status_val = sharedpreferences.getString(Login_Status, "");
        Model.id = sharedpreferences.getString(id, "");
        //============================================================

        tv_tit = (TextView) findViewById(R.id.tv_tit);
        tv_tit1 = (TextView) findViewById(R.id.tv_tit1);
        tv_tit2 = (TextView) findViewById(R.id.tv_tit2);
        tv_tit3 = (TextView) findViewById(R.id.tv_tit3);
        tv_tit4 = (TextView) findViewById(R.id.tv_tit4);
        tv_tit5 = (TextView) findViewById(R.id.tv_tit5);
        tv_tit6 = (TextView) findViewById(R.id.tv_tit6);

        btn_bookcons = (Button) findViewById(R.id.btn_bookcons);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Consult with your doctor");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------


        //------------------- Font refresh --------------------------------
        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tv_tit.setTypeface(font_bold);
        tv_tit1.setTypeface(font_reg);
        tv_tit2.setTypeface(font_reg);
        tv_tit3.setTypeface(font_reg);
        tv_tit4.setTypeface(font_reg);
        tv_tit5.setTypeface(font_reg);
        tv_tit6.setTypeface(font_reg);
        btn_bookcons.setTypeface(font_bold);
        //------------------- Font refresh --------------------------------


        if (!(Model.browser_country).equals("IN")) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(Model.MyConsultation_Toolbar_title);
            }

            tv_tit.setText(Model.Main_Activity_title1);
            tv_tit1.setText(Model.Main_Activity_how_desc);
            tv_tit2.setText(Model.Main_Activity_list1);
            tv_tit3.setText(Model.Main_Activity_list2);
            tv_tit4.setText(Model.Main_Activity_list3);
            tv_tit5.setText(Model.Main_Activity_list4);
            tv_tit6.setText(Model.Main_Activity_bott);

            btn_bookcons.setText(Model.MyConsultation_button);
        }


        btn_bookcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {
                    if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                        Model.query_launch = "MainActivity";

                        intent = new Intent(Consultation_Home.this, Consultation1.class);
                        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                            @Override
                            protected void onReceiveResult(int resultCode, Bundle resultData) {
                                Consultation_Home.this.finish();
                            }
                        });
                        startActivityForResult(intent, 1);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else {
                        force_logout();
                    }
                } else {
                    Toast.makeText(Consultation_Home.this, "Internet connection is not available", Toast.LENGTH_LONG).show();
                }
            }
        });
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

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public void force_logout() {

        final MaterialDialog alert = new MaterialDialog(Consultation_Home.this);
        alert.setTitle("Please re-login the App..!");
        alert.setMessage("Something went wrong. Please go back and try again..!e");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================

                Intent i = new Intent(Consultation_Home.this, LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //------------------ Button Animation -------------------------
            Animation shake;
            shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanim);
            btn_bookcons.startAnimation(shake);
            //------------------ Button Animation -------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
