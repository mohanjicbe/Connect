package com.orane.icliniq;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;

import java.util.HashMap;

public class HotlineHome extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    LinearLayout myqueries_layout,nolayout, netcheck_layout, hl_view_hotline_doctors, invite_doc_layout;
    RelativeLayout LinearLayout1;
    String params;
    Button btn_get;
    ProgressBar progressBar;
    Typeface font_reg, font_bold;
    TextView tv_tooltit, tv_tooldesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotline_home);

        FlurryAgent.onPageView();

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        //---------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Hotline Chat");
            getSupportActionBar().setSubtitle("Instant chat with your doctors");

            tv_tooltit = (TextView) toolbar.findViewById(R.id.tv_tooltit);
            tv_tooldesc = (TextView) toolbar.findViewById(R.id.tv_tooldesc);

            tv_tooltit.setText("Hotline Chat");
            tv_tooldesc.setText("Instant chat with your doctors");

            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
            tv_tooltit.setTypeface(khandBold);
            tv_tooldesc.setTypeface(khandBold);

        }
        //---------------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        LinearLayout1 = (RelativeLayout) findViewById(R.id.LinearLayout1);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        hl_view_hotline_doctors = (LinearLayout) findViewById(R.id.hl_view_hotline_doctors);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        invite_doc_layout = (LinearLayout) findViewById(R.id.invite_doc_layout);
        myqueries_layout = (LinearLayout) findViewById(R.id.myqueries_layout);
        btn_get = (Button) findViewById(R.id.btn_get);

        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tvhltit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvhltit_desc)).setTypeface(font_reg);

        ((TextView) findViewById(R.id.tv_tit1)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_tit2)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_tit3)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_tit4)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_tit5)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_tit6)).setTypeface(font_reg);

        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Hotline_Home");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("android.patient.user_id:", Model.id);
        Model.kiss.set(properties);
        //----------------- Kissmetrics ----------------------------------

        //---------------- Animation -------------------
        Animation animSlideDown = AnimationUtils.loadAnimation(HotlineHome.this, R.anim.bounce1);
        Animation animSlideDown2 = AnimationUtils.loadAnimation(HotlineHome.this, R.anim.bounce2);
        Animation animSlideDown3 = AnimationUtils.loadAnimation(HotlineHome.this, R.anim.bounce3);


        animSlideDown.setStartOffset(50);
        hl_view_hotline_doctors.startAnimation(animSlideDown);

        animSlideDown2.setStartOffset(150);
        invite_doc_layout.startAnimation(animSlideDown2);

        animSlideDown3.setStartOffset(250);
        myqueries_layout.startAnimation(animSlideDown3);



        hl_view_hotline_doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotlineHome.this, HotlineDoctorsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        invite_doc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotlineHome.this, Invite_doctors.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotlineHome.this, Instant_Chat.class);
                intent.putExtra("doctor_id", "0");
                intent.putExtra("plan_id", "");
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        myqueries_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotlineHome.this, QueryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        //------------------ Button Animation -------------------------
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanim);
        btn_get.startAnimation(shake);
        //------------------ Button Animation -------------------------

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.ask_menu, menu);
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

    @Override
    protected void onResume() {
        super.onResume();

    }

}
