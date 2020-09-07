package com.orane.icliniq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;

import java.util.HashMap;

public class NotificationViewActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    Button btn_ok, btn_openapp;
    TextView tv_title, tv_desc;
    public String push_title, push_type, push_msg;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationview);

        FlurryAgent.onPageView();
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Notification");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------
        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================



        Intent intent = getIntent();
        push_msg = intent.getStringExtra("push_msg");
        push_type = intent.getStringExtra("push_type");
        push_title = intent.getStringExtra("push_title");

        btn_openapp = (Button) findViewById(R.id.btn_openapp);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Notification_View");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("android.patient.Pushmsg_View_msg", push_msg);
        properties.put("android.patient.Pushmsg_Type", push_type);
        properties.put("android.patient.Pushmsg_View_Title", push_title);
        Model.kiss.set(properties);
        //----------------- Kissmetrics ----------------------------------

        try {
            tv_desc.setText(Html.fromHtml(push_msg));
            tv_title.setText(Html.fromHtml(push_title));
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        btn_openapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NotificationViewActivity.this, Connect_Home_Actvity.class);
                startActivity(i);
                finish();

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

            if (doubleBackToExitPressedOnce) {
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
