package com.orane.icliniq.Labtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Labtest_WebViewActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.adapter.LabtestListAdapter;
import com.orane.icliniq.adapter.ViewPagerAdapter;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONObject;


public class Labtest_tabs_Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    String str_response, cart_val;
    private ViewPager viewPager;
    JSONObject json_addcart, json_response_tests, json_viewtest, json_response_obj;
    FrameLayout menu_backet;
    All_Labs_List upcomingFragment;
    Thyrocare_Fragment unconfirmedFragment;
    Dr_Lalpaths_Fragment historyFragment;


    private Toolbar toolbar;
    //private SlidingTabLayout SlidingTabLayout;
    private LabtestListAdapter viewPagerAdapter;

    TextView mTitle, tv_cart_count;
    Menu menu_new;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String token = "token_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labtest_tabs_activity);

        //------- Object Creation ----------------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mTitle = toolbar.findViewById(R.id.toolbar_title);
        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        mTitle.setTypeface(khandBold);
        //------- Object Creation ----------------------------------

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        Setup_viewPager();

        menu_backet = findViewById(R.id.menu_backet);
        tv_cart_count = findViewById(R.id.tv_cart_count);

        menu_backet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Labtest_tabs_Activity.this, Labtest_CartViewActivity.class);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        Labtest_tabs_Activity.this.finish();
                    }
                });
                startActivityForResult(intent, 1);

            }
        });

        //--------- Listings ---------------------
        String full_url = Model.BASE_URL + "sapp/labTestCartCount?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("full_url-------------" + full_url);
        new JSON_cart_count().execute(full_url);
        //--------- Listings ---------------------


    }


    @Override
    public void onResume() {
        super.onResume();

        System.out.println("Resume Model.mNotificationCounter-=====-----" + Model.mNotificationCounter);


        if (Model.mNotificationCounter < 0) {
            Model.mNotificationCounter = 0;
        }

        //--------- Listings ---------------------
        String full_url = Model.BASE_URL + "sapp/labTestCartCount?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("full_url-------------" + full_url);
        new JSON_cart_count().execute(full_url);
        //--------- Listings ---------------------

/*        if (Model.query_launch.equals("cart")) {
            System.out.println("Cart Resume--------------");
            if (Model.mNotificationCounter > 0) {
                tv_cart_count.setVisibility(View.VISIBLE);
                tv_cart_count.setText("" + Model.mNotificationCounter);
            } else {
                tv_cart_count.setVisibility(View.GONE);
            }
        }*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* menu_new = menu;
        getMenuInflater().inflate(R.menu.labtest_menu, menu_new);

        if (Model.mNotificationCounter < 0) {
            Model.mNotificationCounter = 0;
        }

        BadgeCounter.update(this,
                menu_new.findItem(R.id.nav_basket),
                R.mipmap.basket_icon,
                BadgeCounter.BadgeColor.RED,
                Model.mNotificationCounter);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
/*
        if (id == R.id.nav_basket) {


          */
/*  Intent intent = new Intent(getApplicationContext(), Labtest_CartViewActivity.class);
            startActivity(intent);
            finish();*//*

            return true;
        }
*/

        return super.onOptionsItemSelected(item);
    }

    public void Setup_viewPager() {

        // --------------------- Initializing viewPager -----------------------------
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        upcomingFragment = new All_Labs_List();
        unconfirmedFragment = new Thyrocare_Fragment();
        historyFragment = new Dr_Lalpaths_Fragment();

        adapter.addFragment(upcomingFragment, "ALL");
        adapter.addFragment(unconfirmedFragment, "Thyrocare");
        adapter.addFragment(historyFragment, "Dr.Lal PathLabs");

        viewPager.setAdapter(adapter);
        // --------------------- Initializing viewPager -----------------------------

    }


    public void onClickCartText(View v) {

        Intent intent = new Intent(Labtest_tabs_Activity.this, Labtest_CartViewActivity.class);
        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                finish();
            }
        });
        startActivityForResult(intent, 1);
    }

    public void onClickCart(View v) {

        View parent = (View) v.getParent();
        View grand_parent = (View) parent.getParent();

        //Button btn_addtocart = (Button) grand_parent.findViewById(R.id.btn_addtocart);

        TextView tv_id = grand_parent.findViewById(R.id.tv_id);
        TextView tv_cartText = grand_parent.findViewById(R.id.tv_cartText);

        tv_cartText.setVisibility(View.VISIBLE);

        v.setVisibility(View.GONE);

        String tvid = tv_id.getText().toString();

        System.out.println("tvid--------------" + tvid);
        System.out.println("Carting Button--------------");


        try {
            json_addcart = new JSONObject();
            json_addcart.put("user_id", (Model.id));
            json_addcart.put("token", Model.token);
            json_addcart.put("id", tvid);

            System.out.println("json_addcart---" + json_addcart.toString());

            new json_addcart().execute(json_addcart);

        } catch (Exception e2) {
            e2.printStackTrace();
        }


    }

    private class json_addcart extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Labtest_tabs_Activity.this);
            dialog.setMessage("Please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "addtocart");


                System.out.println("json_response_obj URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("json_response_obj-------------" + json_response_obj.toString());

                String count = json_response_obj.getString("item_count");
                System.out.println("count--------------" + count);


             /*   BadgeCounter.update(Labtest_tabs_Activity.this,
                        menu_new.findItem(R.id.nav_basket),
                        R.mipmap.basket_icon,
                        BadgeCounter.BadgeColor.RED,
                        count);*/

                Model.mNotificationCounter = Integer.parseInt(count);

                if (Model.mNotificationCounter > 0) {
                    tv_cart_count.setVisibility(View.VISIBLE);
                    tv_cart_count.setText(count);
                } else {
                    tv_cart_count.setVisibility(View.GONE);
                }


                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class JSON_cart_count extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("Cart List response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("str_response--------------" + str_response);
                Model.mNotificationCounter = Integer.parseInt((new JSONObject(str_response)).getString("count"));

                System.out.println("cart_size--------------" + Model.mNotificationCounter);

                if (Model.mNotificationCounter > 0) {
                    tv_cart_count.setVisibility(View.VISIBLE);
                    tv_cart_count.setText("" + Model.mNotificationCounter);
                } else {
                    tv_cart_count.setVisibility(View.GONE);
                }


/*                BadgeCounter.update(Labtest_tabs_Activity.this,
                        menu_new.findItem(R.id.nav_basket),
                        R.mipmap.basket_icon,
                        BadgeCounter.BadgeColor.RED,
                        Model.mNotificationCounter);*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickViewTests(View v) {

        View parent = (View) v.getParent();
        View grand_parent = (View) parent.getParent();

        TextView btn_viewtests = parent.findViewById(R.id.btn_viewtests);
        TextView tv_id = grand_parent.findViewById(R.id.tv_id);

        String tv_id_val = tv_id.getText().toString();

        System.out.println("tv_id_val--------------" + tv_id_val);

        Intent intent = new Intent(Labtest_tabs_Activity.this, TestListActivity.class);
        intent.putExtra("test_id", tv_id_val);
        startActivity(intent);

/*        Intent i = new Intent(Labtest_tabs_Activity.this, Labtest_WebViewActivity.class);
        i.putExtra("url", "");
        i.putExtra("type", "Labtest");
        i.putExtra("code", tv_code_val);
        i.putExtra("test_id", tv_id_val);
        startActivity(i);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

    }

    public void onClickViewWebview(View v) {


        System.out.println("Main Layout click----------------");

        TextView tv_code = v.findViewById(R.id.tv_code);
        TextView tv_id = v.findViewById(R.id.tv_id);
        TextView tv_cartText = v.findViewById(R.id.tv_cartText);

        String tv_code_val = tv_code.getText().toString();
        String tv_id_val = tv_id.getText().toString();

        System.out.println("tv_code_val----------------" + tv_code_val);
        System.out.println("tv_id_val----------------" + tv_id_val);

        if (tv_cartText.getVisibility() == View.VISIBLE) {
            cart_val = "1";
        } else {
            cart_val = "0";
        }


        Intent i = new Intent(getApplicationContext(), Labtest_WebViewActivity.class);
        i.putExtra("url", "");
        i.putExtra("type", "Labtest");
        i.putExtra("code", tv_code_val);
        i.putExtra("test_id", tv_id_val);
        i.putExtra("is_cart_added", cart_val);
        startActivity(i);

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }


}
