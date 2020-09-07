package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.adapter.FamilyListAdaptor;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

public class FamilyProfileListActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, AAH_FabulousFragment.Callbacks {

    ArrayAdapter<String> dataAdapter = null;
    Item objItem;
    TextView tvdocname, weight_title, height_title, tv_forname, tv_soid, tvqfee, tvtit, tvfquery, tvprice;
    public List<Item> listArray;
    public List<Item> arrayOfList;
    public File imageFile;
    JSONObject json_family_new;
    JSONObject jsonobj_makefav, fav_jsonobj;
    public String qfee_text, rel_val, soid_val;
    TextView tvid;
    Spinner spinner_height, spinner_relationship, spinner_weight, spinner_title;
    TextView tv_tooltit, tv_fee11, tv_fee1, tv_name_title, relation_title, tv_spec_name;
    RadioButton radio_male, radio_thirdgender, radio_female;
    LinearLayout famidets_layout, someone_layout, parent_layout, select_layout, family_inner_layout;
    public String rel_name;
    String fee_str_text, icq100_id_val, inf_for, Log_Status, mem_name, wt_name, height_name, tit_id, dob_val,age_val, gender_val, height_val, weight_val, relation_type_val, radio_id, myself_id, famDets_text, fam_response, relation_value, tit_name, tit_val;
    ProgressBar progressBar, progressBar_bottom;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Map<String, String> spec_map = new HashMap<String, String>();
    Map<String, String> ht_map = new HashMap<String, String>();
    Map<String, String> wt_map = new HashMap<String, String>();
    Map<String, String> tit_map = new HashMap<String, String>();
    Map<String, String> gen_map = new HashMap<String, String>();

    Map<String, String> family_map = new HashMap<String, String>();
    LinearLayout spec_layout, nolayout, netcheck_layout;
    String params, profile_id, family_list, str_response;
    FamilyListAdaptor objAdapter;
    public String is_fav, Doc_id, spec_val = "0", fav_url;
    long startTime;

    ObservableGridView listview;

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

    public String sub_url = "sapp/doctors";
    public boolean pagination = true;
    LinearLayout bg_layout;
    FloatingActionButton fab;
    TextView tv_add_member;
    RelativeLayout fab_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_list);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        //============================================================

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("My family profile list");

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        FlurryAgent.onPageView();

        fab_layout = (RelativeLayout) findViewById(R.id.fab_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tv_add_member = (TextView) findViewById(R.id.tv_add_member);
        famidets_layout = (LinearLayout) findViewById(R.id.famidets_layout);
        family_inner_layout = (LinearLayout) findViewById(R.id.family_inner_layout);
        listview = (ObservableGridView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        bg_layout = (LinearLayout) findViewById(R.id.bg_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);

        is_fav = "1";


        full_process();

        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Doctorlist");
        //----------------- Kissmetrics ----------------------------------

        hash_map_title();
        hashmap_relation();
        hashmap_height();
        hashmap_weight();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ask_someone("someone");

                Intent intent = new Intent(FamilyProfileListActivity.this, SomeoneEdit_Dialog.class);
                intent.putExtra("add_type", "someone");
                intent.putExtra("profile_id", "0");
                startActivity(intent);


            }
        });

        fab_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ask_someone("someone");

                Intent intent = new Intent(FamilyProfileListActivity.this, SomeoneEdit_Dialog.class);
                intent.putExtra("add_type", "someone");
                intent.putExtra("profile_id", "0");
                startActivity(intent);



            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("position-----" + position);

                TextView tv_id = (TextView) view.findViewById(R.id.tv_id);

                profile_id = tv_id.getText().toString();

                System.out.println("profile_id----------------" + profile_id);

                //-------------------------------------------------------------------
                String get_family_profile = Model.BASE_URL + "sapp/getFamilyProfile?user_id=" + Model.id + "&id=" + profile_id + "&isView=0&isApp=0&token=" + Model.token;
                System.out.println("get_family_profile---------" + get_family_profile);
                new JSON_EditFamDetails().execute(get_family_profile);
                //---------------------------------------------------------------

             /*   if (!Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                    //Intent intent = new Intent(DoctorsListActivity.this, DoctorProfileActivity.class);
                    Intent intent = new Intent(FamilyProfileListActivity.this, ParallexMainActivity.class);
                    intent.putExtra("tv_doc_id", Doc_id);
                    startActivity(intent);
                    //finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry, Something went wrong. GO Back and Try again..!", Toast.LENGTH_LONG).show();
                }*/
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new NetCheck().netcheck(FamilyProfileListActivity.this)) {
                    //----------------------------------------------------------------
                    //-------------------------------------------------------------------
                    String get_family_url = Model.BASE_URL + "sapp/getFamilyProfile?user_id=" + Model.id + "&token=" + Model.token;
                    System.out.println("get_family_url---------" + get_family_url);
                    new MyTask_server().execute(get_family_url);
                    //-------------------------------------------------------------------
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    bg_layout.setVisibility(View.GONE);
                }
            }
        });


        listview.setScrollViewCallbacks(this);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        System.out.println("scrollY---------" + scrollY);
        System.out.println("firstScroll---------" + firstScroll);
        System.out.println("dragging---------" + dragging);
    }

    @Override
    public void onDownMotionEvent() {

        System.out.println("Scrolling Down---------");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (scrollState == ScrollState.UP) {
            System.out.println("scrollDir-----UP---" + scrollState);
            //fab_layout.hide();
            slideDown(fab_layout);

        } else if (scrollState == ScrollState.DOWN) {
            System.out.println("scrollDir-----Down---" + scrollState);
            //fab_layout.show();
            slideUp(fab_layout);
        }
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    public void full_process() {

        Model.select_spec_val = "0";

        if (isInternetOn()) {

            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                //-------------------------------------------------------------------
                String get_family_url = Model.BASE_URL + "sapp/getFamilyProfile?user_id=" + Model.id + "&token=" + Model.token;
                System.out.println("get_family_url---------" + get_family_url);
                new MyTask_server().execute(get_family_url);
                //-------------------------------------------------------------------
/*
                //--------------------------------------
                String url = Model.BASE_URL + "sapp/doctors?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + (Model.token) + "&enc=1";
                System.out.println("url---" + url);
                new MyTask_server().execute(url);
                //--------------------------------------*/
            } else {
                force_logout();
            }

        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            bg_layout.setVisibility(View.GONE);
        }
    }

    class MyTask_server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            bg_layout.setVisibility(View.VISIBLE);

            progressBar_bottom.setVisibility(View.GONE);


            startTime = System.currentTimeMillis();
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
                            Intent intent = new Intent(FamilyProfileListActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);

                    listArray = new ArrayList<Item>();
                    for (int i = 0; i < jsonarr.length(); i++) {

                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        System.out.println("jsonobj1------------" + jsonobj1.toString());

                        objItem = new Item();
                        objItem.setDocid(jsonobj1.getString("id"));
                        objItem.setDocname(jsonobj1.getString("relation_type"));
                        objItem.setDocedu(jsonobj1.getString("name"));
                        objItem.setDocspec(jsonobj1.getString("gender"));

                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;
                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        nolayout.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);

                    } else {

                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);

                        long elapsedTime = System.currentTimeMillis() - startTime;
                        System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

                        setAdapterToListview();
                    }
                }
                //----------------------------------------------------------

                bg_layout.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class MyTask_Pagination extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            bg_layout.setVisibility(View.GONE);

            progressBar_bottom.setVisibility(View.VISIBLE);

            startTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                str_response = new JSONParser().getJSONString(params[0]);
                System.out.println("str_response--------------" + str_response);

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
                            Intent intent = new Intent(FamilyProfileListActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {

                    System.out.println("str_response----" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();
                    for (int i = 0; i < jsonarr.length(); i++) {

                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        objItem = new Item();
                        objItem.setDocid(jsonobj1.getString("id"));
                        objItem.setDocname(jsonobj1.getString("relation_type"));
                        objItem.setDocedu(jsonobj1.getString("name"));
                        objItem.setDocspec(jsonobj1.getString("gender"));


                        listArray.add(objItem);

                    }
                    arrayOfList = listArray;
                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);

                        progressBar_bottom.setVisibility(View.GONE);

                    } else {
                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);

                        progressBar_bottom.setVisibility(View.GONE);

                        long elapsedTime = System.currentTimeMillis() - startTime;
                        System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

                        add_page_AdapterToListview();
                    }
                }
                //----------------------------------------------------------

                bg_layout.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class MyTask_refresh extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            bg_layout.setVisibility(View.GONE);

            progressBar_bottom.setVisibility(View.GONE);

            startTime = System.currentTimeMillis();
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
                            Intent intent = new Intent(FamilyProfileListActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();
                    for (int i = 0; i < jsonarr.length(); i++) {

                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        objItem = new Item();
                        objItem.setDocid(jsonobj1.getString("id"));
                        objItem.setDocname(jsonobj1.getString("relation_type"));
                        objItem.setDocedu(jsonobj1.getString("name"));
                        objItem.setDocspec(jsonobj1.getString("gender"));

                        listArray.add(objItem);

                    }

                    arrayOfList = listArray;
                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);

                        progressBar_bottom.setVisibility(View.GONE);

                    } else {
                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);

                        progressBar_bottom.setVisibility(View.GONE);

                        long elapsedTime = System.currentTimeMillis() - startTime;
                        System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

                        setAdapterToListview();
                    }
                }
                //----------------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setAdapterToListview() {

        try {
            objAdapter = new FamilyListAdaptor(FamilyProfileListActivity.this, R.layout.familyprofile_row, arrayOfList);
            listview.setAdapter(objAdapter);
            objAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_page_AdapterToListview() {
        try {
            objAdapter.addAll(arrayOfList);
            listview.setSelection(objAdapter.getCount() - (arrayOfList.size()));
            objAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final boolean isInternetOn() {

        try {
            ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
                return true;
            } else if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.doclist_menu, menu);
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
    public void onResume() {
        super.onResume();

        if ((Model.query_launch).equals("SpecialityListActivity")) {

            Model.query_launch = "";

            //---------------------------------------------
            String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&page=1&sp_id=" + Model.select_spec_val + "&token=" + (Model.token);
            System.out.println("url-----" + url);
            new MyTask_server().execute(url);
            //---------------------------------------------
        }
    }

    public void force_logout() {

        try {
            final MaterialDialog alert = new MaterialDialog(FamilyProfileListActivity.this);
            alert.setTitle("Oops..!");
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

                    finishAffinity();
                    Intent i = new Intent(FamilyProfileListActivity.this, LoginActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    finish();
                }
            });
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {

        try {

            switch (v.getId()) {

                case R.id.img_sharedoc:
/*
                    System.out.println("qtitle_textval-------------" + Docname);
                    System.out.println("qaurl-------------" + docurl);

                    TakeScreenshot_Share();*/

                    break;

                case R.id.img_fav:

                    //----------- get Doc Id ---------------------------------------
                    View parent_fav = (View) v.getParent();
                    tvid = (TextView) parent_fav.findViewById(R.id.tvid);
                    String did = tvid.getText().toString();
                    //----------- get Doc Id ---------------------------------------

                    //----------------------------------------
                    fav_url = Model.BASE_URL + "/sapp/add2fav?user_id=" + (Model.id) + "&item_type_id=1&item_id=" + did + "&token=" + Model.token + "&enc=1";
                    System.out.println("Favoueite url-------------" + fav_url);
                    is_fav = "1";
                    //----------------------------------------

                    break;

                case R.id.btn_viewprofile:

                    View parent = (View) v.getParent();
                    //View grand_parent = (View)parent.getParent();

                    tvid = (TextView) parent.findViewById(R.id.tvid);
                    //tv_docurl = (TextView) parent.findViewById(R.id.tv_docurl);

                    String docid = tvid.getText().toString();
                    //docurl = tv_docurl.getText().toString();

                    System.out.println("docid----" + docid);
                    //System.out.println("docurl----" + docurl);

                    Intent intent = new Intent(FamilyProfileListActivity.this, ParallexMainActivity.class);
                    intent.putExtra("tv_doc_id", docid);
                    startActivity(intent);

                    break;

                case R.id.btn_hotlineplans:

                    View parent2 = (View) v.getParent();
                    //View grand_parent = (View)parent.getParent();

                    tvid = (TextView) parent2.findViewById(R.id.tvid);
                    //tv_docurl = (TextView) parent2.findViewById(R.id.tv_docurl);

                    docid = tvid.getText().toString();
                    //docurl = tv_docurl.getText().toString();

                    System.out.println("docid----" + docid);
                    //System.out.println("docurl----" + docurl);

                    Intent i = new Intent(FamilyProfileListActivity.this, HotlinePackagesActivity.class);
                    i.putExtra("Doctor_id", docid);
                    i.putExtra("Doctor_name", "");
                    i.putExtra("tv_docurl", "");
                    startActivity(i);

                    break;

                case R.id.img_share:

/*                    System.out.println("qtitle_textval-------------" + Docname);
                    System.out.println("qaurl-------------" + docurl);*/

                    //TakeScreenshot_Share();

                    break;

                case R.id.fav_layout:
                    System.out.println("is_fav----" + is_fav);

                    if (is_fav != null && !is_fav.isEmpty() && !is_fav.equals("null") && !is_fav.equals("")) {
                        if (is_fav.equals("1")) {


                            View parent_1 = (View) v.getParent();
                            ImageView img_fav = (ImageView) parent_1.findViewById(R.id.img_fav);

                            img_fav.setImageResource(R.mipmap.love_grey);
                            TextView tvid = (TextView) parent_1.findViewById(R.id.tvid);
                            Doc_id = tvid.getText().toString();

                            //------------------------------------------
                            fav_url = Model.BASE_URL + "/sapp/add2fav?user_id=" + (Model.id) + "&item_type_id=1&remove=1&item_id=" + Doc_id + "&token=" + Model.token;
                            System.out.println("Remove Favoueite url-------------" + fav_url);
                            is_fav = "0";
                            //------------------------------------------

                        } else {

                            View parent_1 = (View) v.getParent();
                            ImageView img_fav = (ImageView) parent_1.findViewById(R.id.img_fav);
                            TextView tvid = (TextView) parent_1.findViewById(R.id.tvid);

                            Doc_id = tvid.getText().toString();
                            img_fav.setImageResource(R.mipmap.love_grey_full);
                            fav_url = Model.BASE_URL + "/sapp/add2fav?user_id=" + (Model.id) + "&item_type_id=1&item_id=" + Doc_id + "&token=" + Model.token;
                            System.out.println("Favoueite url-------------" + fav_url);
                            is_fav = "1";
                        }

                        new JSON_make_favupdate().execute(fav_url);
                    }

                    break;
            }

            System.out.println("onClick-------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_make_favupdate extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                jsonobj_makefav = new JSONObject(str_response);

                if (jsonobj_makefav.has("token_status")) {
                    String token_status = jsonobj_makefav.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(FamilyProfileListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    if (jsonobj_makefav.has("status")) {
                        String Status_val = jsonobj_makefav.getString("status");
                        System.out.println("Status_val----" + Status_val);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_EditFamDetails extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(FamilyProfileListActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                famDets_text = jParser.getJSONString(urls[0]);

                System.out.println("Family URL- Dets--------------" + urls[0]);
                System.out.println("Family URL- famDets_text--------------" + famDets_text);

                return famDets_text;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            try {
                JSONObject fam_obj = new JSONObject(family_list);

                System.out.println("fam_obj--------------" + fam_obj.toString());

                rel_val = fam_obj.getString("relation_type");
                tit_id = fam_obj.getString("title_id");
                mem_name = fam_obj.getString("name");
                age_val = fam_obj.getString("age");
                dob_val = fam_obj.getString("dob");
                gender_val = fam_obj.getString("gender");
                height_val = fam_obj.getString("height");
                weight_val = fam_obj.getString("weight");

                tit_val = tit_id;

                System.out.println("tit_id-1--------------" + tit_id);
                System.out.println("mem_name 1---------------" + mem_name);
                System.out.println("rel_type 1---------------" + rel_val);
                System.out.println("age_val 1---------------" + age_val);
                System.out.println("dob_val 1---------------" + dob_val);
                System.out.println("gender_val- 1--------------" + gender_val);
                System.out.println("height_val-1--------------" + height_val);
                System.out.println("weight_val---------------" + weight_val);


                Intent intent = new Intent(FamilyProfileListActivity.this, SomeoneEdit_Dialog.class);
                intent.putExtra("add_type", "edit");
                intent.putExtra("profile_id", profile_id);
                intent.putExtra("tit_id", tit_id);
                intent.putExtra("mem_name", mem_name);
                intent.putExtra("rel_val", rel_val);
                intent.putExtra("dob_val", dob_val);
                intent.putExtra("age_val", age_val);
                intent.putExtra("gender_val", gender_val);
                intent.putExtra("height_val", height_val);
                intent.putExtra("weight_val", weight_val);
                startActivity(intent);


                //ask_someone("edit");

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public void ask_someone(String add_type) {

        //gender_val = "1";
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.someone_edit, null);

        /*RelativeLayout title_layout = (RelativeLayout) alertLayout.findViewById(R.id.title_layout);
        final TextView tv_title_code = (TextView) alertLayout.findViewById(R.id.tv_title_code);*/

        final EditText edt_name = (EditText) alertLayout.findViewById(R.id.edt_name);
        final MaterialEditText edt_age = (MaterialEditText) alertLayout.findViewById(R.id.edt_age);
        final RadioGroup radgrp_gender = (RadioGroup) alertLayout.findViewById(R.id.radgrp_gender);
        radio_male = (RadioButton) alertLayout.findViewById(R.id.radio_male);
        radio_female = (RadioButton) alertLayout.findViewById(R.id.radio_female);
        radio_thirdgender = (RadioButton) alertLayout.findViewById(R.id.radio_thirdgender);

        final RelativeLayout name_title_layout = (RelativeLayout) alertLayout.findViewById(R.id.name_title_layout);
        final RelativeLayout relation_layout = (RelativeLayout) alertLayout.findViewById(R.id.relation_layout);
        final RelativeLayout height_layout = (RelativeLayout) alertLayout.findViewById(R.id.height_layout);
        final RelativeLayout weight_layout = (RelativeLayout) alertLayout.findViewById(R.id.weight_layout);

        tv_name_title = (TextView) alertLayout.findViewById(R.id.tv_name_title);
        height_title = (TextView) alertLayout.findViewById(R.id.height_title);
        weight_title = (TextView) alertLayout.findViewById(R.id.weight_title);
        relation_title = (TextView) alertLayout.findViewById(R.id.relation_title);

        spinner_height = (Spinner) alertLayout.findViewById(R.id.spinner_height);
        spinner_weight = (Spinner) alertLayout.findViewById(R.id.spinner_weight);
        spinner_title = (Spinner) alertLayout.findViewById(R.id.spinner_title);
        spinner_relationship = (Spinner) alertLayout.findViewById(R.id.spinner_relationship);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if (add_type.equals("myself")) {
            alert.setTitle("Update your profile");
            edt_name.setText(Model.name);
            rel_val = "1";
            spinner_relationship.setVisibility(View.GONE);
        } else if (add_type.equals("someone")) {
            alert.setTitle("Add new member");
            profile_id = "0";
            rel_val = "";
        } else if (add_type.equals("edit")) {
            alert.setTitle("Edit a member");
            //rel_val = "";

            System.out.println("# tit_name----------" + getKeyFromValue(tit_map, tit_id));
            System.out.println("# rel_name----------" + getKeyFromValue(gen_map, rel_val));
            System.out.println("# ht_name----------" + getKeyFromValue(ht_map, height_val));
            System.out.println("# wt_name----------" + getKeyFromValue(wt_map, weight_val));

            tv_name_title.setText("" + getKeyFromValue(tit_map, tit_id));
            relation_title.setText("" + getKeyFromValue(gen_map, rel_val));
            height_title.setText("" + getKeyFromValue(ht_map, height_val));
            weight_title.setText("" + getKeyFromValue(wt_map, weight_val));

            System.out.println("tit_name--------------" + tit_name);
            System.out.println("mem_name--------------" + mem_name);
            System.out.println("rel_name--------------" + rel_name);
            System.out.println("age_val--------------" + age_val);
            System.out.println("gender_val--------------" + gender_val);
            System.out.println("ht_name--------------" + height_name);
            System.out.println("wt_name--------------" + wt_name);

            edt_name.setText(mem_name);
            edt_age.setText(age_val);

            System.out.println("gender_val=================" + gender_val);

            //-----------------------------------
            if (gender_val.equals("1")) {
                System.out.println("radio_male Selected------------------");
                radio_male.setChecked(true);
                radio_female.setChecked(false);
                radio_thirdgender.setChecked(false);
            } else if (gender_val.equals("2")) {
                System.out.println("Female Selected------------------");
                radio_male.setChecked(false);
                radio_female.setChecked(true);
                radio_thirdgender.setChecked(false);
            } else if (gender_val.equals("3")) {
                System.out.println("Female Selected------------------");
                radio_male.setChecked(false);
                radio_female.setChecked(false);
                radio_thirdgender.setChecked(true);
            }

            //-----------------------------------
        }

        //apply_name_title();
        //apply_relationship();

        //setup_title();
        //setup_relation();
        //setup_height();
        //setup_weight();


        height_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply_height();
            }
        });

        weight_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply_weight();
            }
        });


        name_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply_name_title();
            }
        });

        relation_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply_relationship();
            }
        });


        radgrp_gender.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);

                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {

                            String check_string = checkedRadioButton.getText().toString();
                            System.out.println("check_string------------" + check_string);

                            if (check_string.equals("Male")) {
                                gender_val = "1";
                            } else if (check_string.equals("Female")) {
                                gender_val = "2";
                            } else {
                                gender_val = "3";
                            }

                            System.out.println("gen_val------------" + gender_val);
                        }
                    }
                });


        alert.setView(alertLayout);
        alert.setCancelable(false);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                //famidets_layout.setVisibility(View.GONE);
            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getBaseContext(), "Done clicked", Toast.LENGTH_SHORT).show();

                String name_val = edt_name.getText().toString();
                String age_val = edt_age.getText().toString();
                String rel_text = relation_title.getText().toString();

               /* if (tit_val != null && !tit_val.isEmpty() && !tit_val.equals("null") && !tit_val.equals("")) {
                    tit_val = "1";
                }*/

                System.out.println("profile_id ---------" + profile_id);
                System.out.println("age_val ---------" + age_val);
                System.out.println("gender_val ---------" + gender_val);
                System.out.println("tit_val ---------" + tit_val);
                System.out.println("name_val ---------" + name_val);
                System.out.println("rel_val ---------" + rel_val);
                System.out.println("height_val ---------" + height_val);
                System.out.println("weight_val ---------" + weight_val);

                if (!name_val.equals("")) {
                    if (!age_val.equals("")) {
                        if (!rel_text.equals("Select relationship")) {
                            try {
                                json_family_new = new JSONObject();
                                json_family_new.put("fpId", profile_id);
                                json_family_new.put("save_type", "edit");
                                json_family_new.put("age", age_val);
                                json_family_new.put("gender", gender_val);
                                json_family_new.put("title", tit_val);
                                json_family_new.put("name", name_val);
                                json_family_new.put("relationType", rel_val);
                                json_family_new.put("height", height_val);
                                json_family_new.put("weight", weight_val);

                                System.out.println("json_family_new---" + json_family_new.toString());

                                new JSON_NewFamily().execute(json_family_new);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Select relation ship", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        edt_age.setError("Please enter your age");
                        edt_age.requestFocus();
                    }
                } else {
                    edt_name.setError("Please enter the name");
                    edt_name.requestFocus();
                }

            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    public void hash_map_title() {
        tit_map.put("Mr.", "1");
        tit_map.put("Miss.", "2");
        tit_map.put("Mrs.", "3");
        tit_map.put("Baby", "5");
    }

    public void hashmap_relation() {
        gen_map.put("Myself", "1");
        gen_map.put("Father", "2");
        gen_map.put("Mother", "3");
        gen_map.put("Brother", "4");
        gen_map.put("Sister", "5");
        gen_map.put("Husband", "6");
        gen_map.put("Wife", "7");
        gen_map.put("Son", "8");
        gen_map.put("Daughter", "9");
        gen_map.put("Cousin", "10");
        gen_map.put("Grand Father", "11");
        gen_map.put("Grand Mother", "12");
        gen_map.put("Friend", "13");
        gen_map.put("Others", "14");
    }

    public void hashmap_height() {

        final ArrayAdapter<String> height_categories = new ArrayAdapter<String>(FamilyProfileListActivity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        height_categories.add("1' 0\" (30.48 cm)");
        ht_map.put("1' 0\" (30.48 cm)", "1");

        height_categories.add("1' 1\" (33.02 cm)");
        ht_map.put("1' 1\" (33.02 cm)", "2");

        height_categories.add("1' 2\" (35.56 cm)");
        ht_map.put("1' 2\" (35.56 cm)", "3");
        height_categories.add("1' 3\" (38.1 cm)");
        ht_map.put("1' 3\" (38.1 cm)", "4");
        height_categories.add("1' 4\" (40.64 cm)");
        ht_map.put("1' 4\" (40.64 cm)", "5");
        height_categories.add("1' 5\" (43.18 cm)");
        ht_map.put("1' 5\" (43.18 cm)", "6");
        height_categories.add("1' 6\" (45.72 cm)");
        ht_map.put("1' 6\" (45.72 cm)", "7");
        height_categories.add("1' 7\" (48.26 cm)");
        ht_map.put("1' 7\" (48.26 cm)", "8");
        height_categories.add("1' 8\" (50.8 cm)");
        ht_map.put("1' 8\" (50.8 cm)", "9");
        height_categories.add("1' 9\" (53.34 cm)");
        ht_map.put("1' 9\" (53.34 cm)", "10");
        height_categories.add("1' 10\" (55.88 cm)");
        ht_map.put("1' 10\" (55.88 cm)", "11");
        height_categories.add("1' 11\" (58.42 cm)");
        ht_map.put("1' 11\" (58.42 cm)", "12");
        height_categories.add("2' 0\" (60.96 cm)");
        ht_map.put("2' 0\" (60.96 cm)", "13");
        height_categories.add("2' 1\" (63.5 cm)");
        ht_map.put("2' 1\" (63.5 cm)", "14");
        height_categories.add("2' 2\" (66.04 cm)");
        ht_map.put("2' 2\" (66.04 cm)", "15");
        height_categories.add("2' 3\" (68.58 cm)");
        ht_map.put("2' 3\" (68.58 cm)", "16");
        height_categories.add("2' 4\" (71.12 cm)");
        ht_map.put("2' 4\" (71.12 cm)", "17");
        height_categories.add("2' 5\" (73.66 cm)");
        ht_map.put("2' 5\" (73.66 cm)", "18");
        height_categories.add("2' 6\" (76.2 cm)");
        ht_map.put("2' 6\" (76.2 cm)", "19");
        height_categories.add("2' 7\" (78.74 cm)");
        ht_map.put("2' 7\" (78.74 cm)", "20");
        height_categories.add("2' 8\" (81.28 cm)");
        ht_map.put("2' 8\" (81.28 cm)", "21");
        height_categories.add("2' 9\" (83.82 cm)");
        ht_map.put("2' 9\" (83.82 cm)", "22");
        height_categories.add("2' 10\" (86.36 cm)");
        ht_map.put("2' 10\" (86.36 cm)", "23");
        height_categories.add("2' 11\" (88.9 cm)");
        ht_map.put("2' 11\" (88.9 cm)", "24");
        height_categories.add("3' 0\" (91.44 cm)");
        ht_map.put("3' 0\" (91.44 cm)", "25");
        height_categories.add("3' 1\" (93.98 cm)");
        ht_map.put("3' 1\" (93.98 cm)", "26");
        height_categories.add("3' 2\" (96.52 cm)");
        ht_map.put("3' 2\" (96.52 cm)", "27");
        height_categories.add("3' 3\" (99.06 cm)");
        ht_map.put("3' 3\" (99.06 cm)", "28");
        height_categories.add("3' 4\" (101.6 cm)");
        ht_map.put("3' 4\" (101.6 cm)", "29");
        height_categories.add("3' 5\" (104.14 cm)");
        ht_map.put("3' 5\" (104.14 cm)", "30");
        height_categories.add("3' 6\" (106.68 cm)");
        ht_map.put("3' 6\" (106.68 cm)", "31");
        height_categories.add("3' 7\" (109.22 cm)");
        ht_map.put("3' 7\" (109.22 cm)", "32");
        height_categories.add("3' 8\" (111.76 cm)");
        ht_map.put("3' 8\" (111.76 cm)", "33");
        height_categories.add("3' 9\" (114.3 cm)");
        ht_map.put("3' 9\" (114.3 cm)", "34");
        height_categories.add("3' 10\" (116.84 cm)");
        ht_map.put("3' 10\" (116.84 cm)", "35");
        height_categories.add("3' 11\" (119.38 cm)");
        ht_map.put("3' 11\" (119.38 cm)", "36");
        height_categories.add("4' 0\" (121.92 cm)");
        ht_map.put("4' 0\" (121.92 cm)", "37");
        height_categories.add("4' 1\" (124.46 cm)");
        ht_map.put("4' 1\" (124.46 cm)", "38");
        height_categories.add("4' 2\" (127 cm)");
        ht_map.put("4' 2\" (127 cm)", "39");
        height_categories.add("4' 3\" (129.54 cm)");
        ht_map.put("4' 3\" (129.54 cm)", "40");
        height_categories.add("4' 4\" (132.08 cm)");
        ht_map.put("4' 4\" (132.08 cm)", "41");
        height_categories.add("4' 5\" (134.62 cm)");
        ht_map.put("4' 5\" (134.62 cm)", "42");
        height_categories.add("4' 6\" (137.16 cm)");
        ht_map.put("4' 6\" (137.16 cm)", "43");
        height_categories.add("4' 7\" (139.7 cm)");
        ht_map.put("4' 7\" (139.7 cm)", "44");
        height_categories.add("4' 8\" (142.24 cm)");
        ht_map.put("4' 8\" (142.24 cm)", "45");
        height_categories.add("4' 9\" (144.78 cm)");
        ht_map.put("4' 9\" (144.78 cm)", "46");
        height_categories.add("4' 10\" (147.32 cm)");
        ht_map.put("4' 10\" (147.32 cm)", "47");
        height_categories.add("4' 11\" (149.86 cm)");
        ht_map.put("4' 11\" (149.86 cm)", "48");
        height_categories.add("5' 0\" (152.4 cm)");
        ht_map.put("5' 0\" (152.4 cm)", "49");
        height_categories.add("5' 1\" (154.94 cm)");
        ht_map.put("5' 1\" (154.94 cm)", "50");
        height_categories.add("5' 2\" (157.48 cm)");
        ht_map.put("5' 2\" (157.48 cm)", "51");
        height_categories.add("5' 3\" (160.02 cm)");
        ht_map.put("5' 3\" (160.02 cm)", "52");
        height_categories.add("5' 4\" (162.56 cm)");
        ht_map.put("5' 4\" (162.56 cm)", "53");
        height_categories.add("5' 5\" (165.1 cm)");
        ht_map.put("5' 5\" (165.1 cm)", "54");
        height_categories.add("5' 6\" (167.64 cm)");
        ht_map.put("5' 6\" (167.64 cm)", "55");
        height_categories.add("5' 7\" (170.18 cm)");
        ht_map.put("5' 7\" (170.18 cm)", "56");
        height_categories.add("5' 8\" (172.72 cm)");
        ht_map.put("5' 8\" (172.72 cm)", "57");
        height_categories.add("5' 9\" (175.26 cm)");
        ht_map.put("5' 9\" (175.26 cm)", "58");
        height_categories.add("5' 10\" (177.8 cm)");
        ht_map.put("5' 10\" (177.8 cm)", "59");
        height_categories.add("5' 11\" (180.34 cm)");
        ht_map.put("5' 11\" (180.34 cm)", "60");
        height_categories.add("6' 0\" (182.88 cm)");
        ht_map.put("6' 0\" (182.88 cm)", "61");
        height_categories.add("6' 1\" (185.42 cm)");
        ht_map.put("6' 1\" (185.42 cm)", "62");
        height_categories.add("6' 2\" (187.96 cm)");
        ht_map.put("6' 2\" (187.96 cm)", "63");
        height_categories.add("6' 3\" (190.5 cm)");
        ht_map.put("6' 3\" (190.5 cm)", "64");
        height_categories.add("6' 4\" (193.04 cm)");
        ht_map.put("6' 4\" (193.04 cm)", "65");
        height_categories.add("6' 5\" (195.58 cm)");
        ht_map.put("6' 5\" (195.58 cm)", "66");
        height_categories.add("6' 6\" (198.12 cm)");
        ht_map.put("6' 6\" (198.12 cm)", "67");
        height_categories.add("6' 7\" (200.66 cm)");
        ht_map.put("6' 7\" (200.66 cm)", "68");
        height_categories.add("6' 8\" (203.2 cm)");
        ht_map.put("6' 8\" (203.2 cm)", "69");
        height_categories.add("6' 9\" (205.74 cm)");
        ht_map.put("6' 9\" (205.74 cm)", "70");
        height_categories.add("6' 10\" (208.28 cm)");
        ht_map.put("6' 10\" (208.28 cm)", "71");
        height_categories.add("6' 11\" (210.82 cm)");
        ht_map.put("6' 11\" (210.82 cm)", "72");
        height_categories.add("7' 0\" (213.36 cm)");
        ht_map.put("7' 0\" (213.36 cm)", "73");
        height_categories.add("7' 1\" (215.9 cm)");
        ht_map.put("7' 1\" (215.9 cm)", "74");
        height_categories.add("7' 2\" (218.44 cm)");
        ht_map.put("7' 2\" (218.44 cm)", "75");
        height_categories.add("7' 3\" (220.98 cm)");
        ht_map.put("7' 3\" (220.98 cm)", "76");
        height_categories.add("7' 4\" (223.52 cm)");
        ht_map.put("7' 4\" (223.52 cm)", "77");
        height_categories.add("7' 5\" (226.06 cm)");
        ht_map.put("7' 5\" (226.06 cm)", "78");
        height_categories.add("7' 6\" (228.6 cm)");
        ht_map.put("7' 6\" (228.6 cm)", "79");
        height_categories.add("7' 7\" (231.14 cm)");
        ht_map.put("7' 7\" (231.14 cm)", "80");
        height_categories.add("7' 8\" (233.68 cm)");
        ht_map.put("7' 8\" (233.68 cm)", "81");
        height_categories.add("7' 9\" (236.22 cm)");
        ht_map.put("7' 9\" (236.22 cm)", "82");
        height_categories.add("7' 10\" (238.76 cm)");
        ht_map.put("7' 10\" (238.76 cm)", "83");
        height_categories.add("7' 11\" (241.3 cm)");
        ht_map.put("7' 11\" (241.3 cm)", "84");
        height_categories.add("8' 0\" (243.84 cm)");
        ht_map.put("8' 0\" (243.84 cm)", "85");
        height_categories.add("8' 1\" (246.38 cm)");
        ht_map.put("8' 1\" (246.38 cm)", "86");
        height_categories.add("8' 2\" (248.92 cm)");
        ht_map.put("8' 2\" (248.92 cm)", "87");
        height_categories.add("8' 3\" (251.46 cm)");
        ht_map.put("8' 3\" (251.46 cm)", "88");
        height_categories.add("8' 4\" (254 cm)");
        ht_map.put("8' 4\" (254 cm)", "89");
        height_categories.add("8' 5\" (256.54 cm)");
        ht_map.put("8' 5\" (256.54 cm)", "90");
        height_categories.add("8' 6\" (259.08 cm)");
        ht_map.put("8' 6\" (259.08 cm)", "91");
        height_categories.add("8' 7\" (261.62 cm)");
        ht_map.put("8' 7\" (261.62 cm)", "92");
        height_categories.add("8' 8\" (264.16 cm)");
        ht_map.put("8' 8\" (264.16 cm)", "93");
        height_categories.add("8' 9\" (266.7 cm)");
        ht_map.put("8' 9\" (266.7 cm)", "94");
        height_categories.add("8' 10\" (269.24 cm)");
        ht_map.put("8' 10\" (269.24 cm)", "95");
        height_categories.add("8' 11\" (271.78 cm)");
        ht_map.put("8' 11\" (271.78 cm)", "96");


    }

    public void hashmap_weight() {

        final ArrayAdapter<String> weight_categories = new ArrayAdapter<String>(FamilyProfileListActivity.this, R.layout.dialog_list_textview);

        weight_categories.add("1 lbs (0.45 kg)");
        wt_map.put("1 lbs (0.45 kg)", "1");
        weight_categories.add("2 lbs (0.91 kg)");
        wt_map.put("2 lbs (0.91 kg)", "2");
        weight_categories.add("3 lbs (1.36 kg)");
        wt_map.put("3 lbs (1.36 kg)", "3");
        weight_categories.add("4 lbs (1.81 kg)");
        wt_map.put("4 lbs (1.81 kg)", "4");
        weight_categories.add("5 lbs (2.27 kg)");
        wt_map.put("5 lbs (2.27 kg)", "5");
        weight_categories.add("6 lbs (2.72 kg)");
        wt_map.put("6 lbs (2.72 kg)", "6");
        weight_categories.add("7 lbs (3.18 kg)");
        wt_map.put("7 lbs (3.18 kg)", "7");
        weight_categories.add("8 lbs (3.63 kg)");
        wt_map.put("8 lbs (3.63 kg)", "8");
        weight_categories.add("9 lbs (4.08 kg)");
        wt_map.put("9 lbs (4.08 kg)", "9");
        weight_categories.add("10 lbs (4.54 kg)");
        wt_map.put("10 lbs (4.54 kg)", "10");
        weight_categories.add("11 lbs (4.99 kg)");
        wt_map.put("11 lbs (4.99 kg)", "11");
        weight_categories.add("12 lbs (5.44 kg)");
        wt_map.put("12 lbs (5.44 kg)", "12");
        weight_categories.add("13 lbs (5.9 kg)");
        wt_map.put("13 lbs (5.9 kg)", "13");
        weight_categories.add("14 lbs (6.35 kg)");
        wt_map.put("14 lbs (6.35 kg)", "14");
        weight_categories.add("15 lbs (6.8 kg)");
        wt_map.put("15 lbs (6.8 kg)", "15");
        weight_categories.add("16 lbs (7.26 kg)");
        wt_map.put("16 lbs (7.26 kg)", "16");
        weight_categories.add("17 lbs (7.71 kg)");
        wt_map.put("17 lbs (7.71 kg)", "17");
        weight_categories.add("18 lbs (8.16 kg)");
        wt_map.put("18 lbs (8.16 kg)", "18");
        weight_categories.add("19 lbs (8.62 kg)");
        wt_map.put("19 lbs (8.62 kg)", "19");
        weight_categories.add("20 lbs (9.07 kg)");
        wt_map.put("20 lbs (9.07 kg)", "20");
        weight_categories.add("21 lbs (9.53 kg)");
        wt_map.put("21 lbs (9.53 kg)", "21");
        weight_categories.add("22 lbs (9.98 kg)");
        wt_map.put("22 lbs (9.98 kg)", "22");
        weight_categories.add("23 lbs (10.43 kg)");
        wt_map.put("23 lbs (10.43 kg)", "23");
        weight_categories.add("24 lbs (10.89 kg)");
        wt_map.put("24 lbs (10.89 kg)", "24");
        weight_categories.add("25 lbs (11.34 kg)");
        wt_map.put("25 lbs (11.34 kg)", "25");
        weight_categories.add("26 lbs (11.79 kg)");
        wt_map.put("26 lbs (11.79 kg)", "26");
        weight_categories.add("27 lbs (12.25 kg)");
        wt_map.put("27 lbs (12.25 kg)", "27");
        weight_categories.add("28 lbs (12.7 kg)");
        wt_map.put("28 lbs (12.7 kg)", "28");
        weight_categories.add("29 lbs (13.15 kg)");
        wt_map.put("29 lbs (13.15 kg)", "29");
        weight_categories.add("30 lbs (13.61 kg)");
        wt_map.put("30 lbs (13.61 kg)", "30");
        weight_categories.add("31 lbs (14.06 kg)");
        wt_map.put("31 lbs (14.06 kg)", "31");
        weight_categories.add("32 lbs (14.51 kg)");
        wt_map.put("32 lbs (14.51 kg)", "32");
        weight_categories.add("33 lbs (14.97 kg)");
        wt_map.put("33 lbs (14.97 kg)", "33");
        weight_categories.add("34 lbs (15.42 kg)");
        wt_map.put("34 lbs (15.42 kg)", "34");
        weight_categories.add("35 lbs (15.88 kg)");
        wt_map.put("35 lbs (15.88 kg)", "35");
        weight_categories.add("36 lbs (16.33 kg)");
        wt_map.put("36 lbs (16.33 kg)", "36");
        weight_categories.add("37 lbs (16.78 kg)");
        wt_map.put("37 lbs (16.78 kg)", "37");
        weight_categories.add("38 lbs (17.24 kg)");
        wt_map.put("38 lbs (17.24 kg)", "38");
        weight_categories.add("39 lbs (17.69 kg)");
        wt_map.put("39 lbs (17.69 kg)", "39");
        weight_categories.add("40 lbs (18.14 kg)");
        wt_map.put("40 lbs (18.14 kg)", "40");
        weight_categories.add("41 lbs (18.6 kg)");
        wt_map.put("41 lbs (18.6 kg)", "41");
        weight_categories.add("42 lbs (19.05 kg)");
        wt_map.put("42 lbs (19.05 kg)", "42");
        weight_categories.add("43 lbs (19.5 kg)");
        wt_map.put("43 lbs (19.5 kg)", "43");
        weight_categories.add("44 lbs (19.96 kg)");
        wt_map.put("44 lbs (19.96 kg)", "44");
        weight_categories.add("45 lbs (20.41 kg)");
        wt_map.put("45 lbs (20.41 kg)", "45");
        weight_categories.add("46 lbs (20.87 kg)");
        wt_map.put("46 lbs (20.87 kg)", "46");
        weight_categories.add("47 lbs (21.32 kg)");
        wt_map.put("47 lbs (21.32 kg)", "47");
        weight_categories.add("48 lbs (21.77 kg)");
        wt_map.put("48 lbs (21.77 kg)", "48");
        weight_categories.add("49 lbs (22.23 kg)");
        wt_map.put("49 lbs (22.23 kg)", "49");
        weight_categories.add("50 lbs (22.68 kg)");
        wt_map.put("50 lbs (22.68 kg)", "50");
        weight_categories.add("51 lbs (23.13 kg)");
        wt_map.put("51 lbs (23.13 kg)", "51");
        weight_categories.add("52 lbs (23.59 kg)");
        wt_map.put("52 lbs (23.59 kg)", "52");
        weight_categories.add("53 lbs (24.04 kg)");
        wt_map.put("53 lbs (24.04 kg)", "53");
        weight_categories.add("54 lbs (24.49 kg)");
        wt_map.put("54 lbs (24.49 kg)", "54");
        weight_categories.add("55 lbs (24.95 kg)");
        wt_map.put("55 lbs (24.95 kg)", "55");
        weight_categories.add("56 lbs (25.4 kg)");
        wt_map.put("56 lbs (25.4 kg)", "56");
        weight_categories.add("57 lbs (25.85 kg)");
        wt_map.put("57 lbs (25.85 kg)", "57");
        weight_categories.add("58 lbs (26.31 kg)");
        wt_map.put("58 lbs (26.31 kg)", "58");
        weight_categories.add("59 lbs (26.76 kg)");
        wt_map.put("59 lbs (26.76 kg)", "59");
        weight_categories.add("60 lbs (27.22 kg)");
        wt_map.put("60 lbs (27.22 kg)", "60");
        weight_categories.add("61 lbs (27.67 kg)");
        wt_map.put("61 lbs (27.67 kg)", "61");
        weight_categories.add("62 lbs (28.12 kg)");
        wt_map.put("62 lbs (28.12 kg)", "62");
        weight_categories.add("63 lbs (28.58 kg)");
        wt_map.put("63 lbs (28.58 kg)", "63");
        weight_categories.add("64 lbs (29.03 kg)");
        wt_map.put("64 lbs (29.03 kg)", "64");
        weight_categories.add("65 lbs (29.48 kg)");
        wt_map.put("65 lbs (29.48 kg)", "65");
        weight_categories.add("66 lbs (29.94 kg)");
        wt_map.put("66 lbs (29.94 kg)", "66");
        weight_categories.add("67 lbs (30.39 kg)");
        wt_map.put("67 lbs (30.39 kg)", "67");
        weight_categories.add("68 lbs (30.84 kg)");
        wt_map.put("68 lbs (30.84 kg)", "68");
        weight_categories.add("69 lbs (31.3 kg)");
        wt_map.put("69 lbs (31.3 kg)", "69");
        weight_categories.add("70 lbs (31.75 kg)");
        wt_map.put("70 lbs (31.75 kg)", "70");
        weight_categories.add("71 lbs (32.21 kg)");
        wt_map.put("71 lbs (32.21 kg)", "71");
        weight_categories.add("72 lbs (32.66 kg)");
        wt_map.put("72 lbs (32.66 kg)", "72");
        weight_categories.add("73 lbs (33.11 kg)");
        wt_map.put("73 lbs (33.11 kg)", "73");
        weight_categories.add("74 lbs (33.57 kg)");
        wt_map.put("74 lbs (33.57 kg)", "74");
        weight_categories.add("75 lbs (34.02 kg)");
        wt_map.put("75 lbs (34.02 kg)", "75");
        weight_categories.add("76 lbs (34.47 kg)");
        wt_map.put("76 lbs (34.47 kg)", "76");
        weight_categories.add("77 lbs (34.93 kg)");
        wt_map.put("77 lbs (34.93 kg)", "77");
        weight_categories.add("78 lbs (35.38 kg)");
        wt_map.put("78 lbs (35.38 kg)", "78");
        weight_categories.add("79 lbs (35.83 kg)");
        wt_map.put("79 lbs (35.83 kg)", "79");
        weight_categories.add("80 lbs (36.29 kg)");
        wt_map.put("80 lbs (36.29 kg)", "80");
        weight_categories.add("81 lbs (36.74 kg)");
        wt_map.put("81 lbs (36.74 kg)", "81");
        weight_categories.add("82 lbs (37.19 kg)");
        wt_map.put("82 lbs (37.19 kg)", "82");
        weight_categories.add("83 lbs (37.65 kg)");
        wt_map.put("83 lbs (37.65 kg)", "83");
        weight_categories.add("84 lbs (38.1 kg)");
        wt_map.put("84 lbs (38.1 kg)", "84");
        weight_categories.add("85 lbs (38.56 kg)");
        wt_map.put("85 lbs (38.56 kg)", "85");
        weight_categories.add("86 lbs (39.01 kg)");
        wt_map.put("86 lbs (39.01 kg)", "86");
        weight_categories.add("87 lbs (39.46 kg)");
        wt_map.put("87 lbs (39.46 kg)", "87");
        weight_categories.add("88 lbs (39.92 kg)");
        wt_map.put("88 lbs (39.92 kg)", "88");
        weight_categories.add("89 lbs (40.37 kg)");
        wt_map.put("89 lbs (40.37 kg)", "89");
        weight_categories.add("90 lbs (40.82 kg)");
        wt_map.put("90 lbs (40.82 kg)", "90");
        weight_categories.add("91 lbs (41.28 kg)");
        wt_map.put("91 lbs (41.28 kg)", "91");
        weight_categories.add("92 lbs (41.73 kg)");
        wt_map.put("92 lbs (41.73 kg)", "92");
        weight_categories.add("93 lbs (42.18 kg)");
        wt_map.put("93 lbs (42.18 kg)", "93");
        weight_categories.add("94 lbs (42.64 kg)");
        wt_map.put("94 lbs (42.64 kg)", "94");
        weight_categories.add("95 lbs (43.09 kg)");
        wt_map.put("95 lbs (43.09 kg)", "95");
        weight_categories.add("96 lbs (43.54 kg)");
        wt_map.put("96 lbs (43.54 kg)", "96");
        weight_categories.add("97 lbs (44 kg)");
        wt_map.put("97 lbs (44 kg)", "97");
        weight_categories.add("98 lbs (44.45 kg)");
        wt_map.put("98 lbs (44.45 kg)", "98");
        weight_categories.add("99 lbs (44.91 kg)");
        wt_map.put("99 lbs (44.91 kg)", "99");
        weight_categories.add("100 lbs (45.36 kg)");
        wt_map.put("100 lbs (45.36 kg)", "100");
        weight_categories.add("101 lbs (45.81 kg)");
        wt_map.put("101 lbs (45.81 kg)", "101");
        weight_categories.add("102 lbs (46.27 kg)");
        wt_map.put("102 lbs (46.27 kg)", "102");
        weight_categories.add("103 lbs (46.72 kg)");
        wt_map.put("103 lbs (46.72 kg)", "103");
        weight_categories.add("104 lbs (47.17 kg)");
        wt_map.put("104 lbs (47.17 kg)", "104");
        weight_categories.add("105 lbs (47.63 kg)");
        wt_map.put("105 lbs (47.63 kg)", "105");
        weight_categories.add("106 lbs (48.08 kg)");
        wt_map.put("106 lbs (48.08 kg)", "106");
        weight_categories.add("107 lbs (48.53 kg)");
        wt_map.put("107 lbs (48.53 kg)", "107");
        weight_categories.add("108 lbs (48.99 kg)");
        wt_map.put("108 lbs (48.99 kg)", "108");
        weight_categories.add("109 lbs (49.44 kg)");
        wt_map.put("109 lbs (49.44 kg)", "109");
        weight_categories.add("110 lbs (49.9 kg)");
        wt_map.put("110 lbs (49.9 kg)", "110");
        weight_categories.add("111 lbs (50.35 kg)");
        wt_map.put("111 lbs (50.35 kg)", "111");
        weight_categories.add("112 lbs (50.8 kg)");
        wt_map.put("112 lbs (50.8 kg)", "112");
        weight_categories.add("113 lbs (51.26 kg)");
        wt_map.put("113 lbs (51.26 kg)", "113");
        weight_categories.add("114 lbs (51.71 kg)");
        wt_map.put("114 lbs (51.71 kg)", "114");
        weight_categories.add("115 lbs (52.16 kg)");
        wt_map.put("115 lbs (52.16 kg)", "115");
        weight_categories.add("116 lbs (52.62 kg)");
        wt_map.put("116 lbs (52.62 kg)", "116");
        weight_categories.add("117 lbs (53.07 kg)");
        wt_map.put("117 lbs (53.07 kg)", "117");
        weight_categories.add("118 lbs (53.52 kg)");
        wt_map.put("118 lbs (53.52 kg)", "118");
        weight_categories.add("119 lbs (53.98 kg)");
        wt_map.put("119 lbs (53.98 kg)", "119");
        weight_categories.add("120 lbs (54.43 kg)");
        wt_map.put("120 lbs (54.43 kg)", "120");
        weight_categories.add("121 lbs (54.88 kg)");
        wt_map.put("121 lbs (54.88 kg)", "121");
        weight_categories.add("122 lbs (55.34 kg)");
        wt_map.put("122 lbs (55.34 kg)", "122");
        weight_categories.add("123 lbs (55.79 kg)");
        wt_map.put("123 lbs (55.79 kg)", "123");
        weight_categories.add("124 lbs (56.25 kg)");
        wt_map.put("124 lbs (56.25 kg)", "124");
        weight_categories.add("125 lbs (56.7 kg)");
        wt_map.put("125 lbs (56.7 kg)", "125");
        weight_categories.add("126 lbs (57.15 kg)");
        wt_map.put("126 lbs (57.15 kg)", "126");
        weight_categories.add("127 lbs (57.61 kg)");
        wt_map.put("127 lbs (57.61 kg)", "127");
        weight_categories.add("128 lbs (58.06 kg)");
        wt_map.put("128 lbs (58.06 kg)", "128");
        weight_categories.add("129 lbs (58.51 kg)");
        wt_map.put("129 lbs (58.51 kg)", "129");
        weight_categories.add("130 lbs (58.97 kg)");
        wt_map.put("130 lbs (58.97 kg)", "130");
        weight_categories.add("131 lbs (59.42 kg)");
        wt_map.put("131 lbs (59.42 kg)", "131");
        weight_categories.add("132 lbs (59.87 kg)");
        wt_map.put("132 lbs (59.87 kg)", "132");
        weight_categories.add("133 lbs (60.33 kg)");
        wt_map.put("133 lbs (60.33 kg)", "133");
        weight_categories.add("134 lbs (60.78 kg)");
        wt_map.put("134 lbs (60.78 kg)", "134");
        weight_categories.add("135 lbs (61.23 kg)");
        wt_map.put("135 lbs (61.23 kg)", "135");
        weight_categories.add("136 lbs (61.69 kg)");
        wt_map.put("136 lbs (61.69 kg)", "136");
        weight_categories.add("137 lbs (62.14 kg)");
        wt_map.put("137 lbs (62.14 kg)", "137");
        weight_categories.add("138 lbs (62.6 kg)");
        wt_map.put("138 lbs (62.6 kg)", "138");
        weight_categories.add("139 lbs (63.05 kg)");
        wt_map.put("139 lbs (63.05 kg)", "139");
        weight_categories.add("140 lbs (63.5 kg)");
        wt_map.put("140 lbs (63.5 kg)", "140");
        weight_categories.add("141 lbs (63.96 kg)");
        wt_map.put("141 lbs (63.96 kg)", "141");
        weight_categories.add("142 lbs (64.41 kg)");
        wt_map.put("142 lbs (64.41 kg)", "142");
        weight_categories.add("143 lbs (64.86 kg)");
        wt_map.put("143 lbs (64.86 kg)", "143");
        weight_categories.add("144 lbs (65.32 kg)");
        wt_map.put("144 lbs (65.32 kg)", "144");
        weight_categories.add("145 lbs (65.77 kg)");
        wt_map.put("145 lbs (65.77 kg)", "145");
        weight_categories.add("146 lbs (66.22 kg)");
        wt_map.put("146 lbs (66.22 kg)", "146");
        weight_categories.add("147 lbs (66.68 kg)");
        wt_map.put("147 lbs (66.68 kg)", "147");
        weight_categories.add("148 lbs (67.13 kg)");
        wt_map.put("148 lbs (67.13 kg)", "148");
        weight_categories.add("149 lbs (67.59 kg)");
        wt_map.put("149 lbs (67.59 kg)", "149");
        weight_categories.add("150 lbs (68.04 kg)");
        wt_map.put("150 lbs (68.04 kg)", "150");
        weight_categories.add("151 lbs (68.49 kg)");
        wt_map.put("151 lbs (68.49 kg)", "151");
        weight_categories.add("152 lbs (68.95 kg)");
        wt_map.put("152 lbs (68.95 kg)", "152");
        weight_categories.add("153 lbs (69.4 kg)");
        wt_map.put("153 lbs (69.4 kg)", "153");
        weight_categories.add("154 lbs (69.85 kg)");
        wt_map.put("154 lbs (69.85 kg)", "154");
        weight_categories.add("155 lbs (70.31 kg)");
        wt_map.put("155 lbs (70.31 kg)", "155");
        weight_categories.add("156 lbs (70.76 kg)");
        wt_map.put("156 lbs (70.76 kg)", "156");
        weight_categories.add("157 lbs (71.21 kg)");
        wt_map.put("157 lbs (71.21 kg)", "157");
        weight_categories.add("158 lbs (71.67 kg)");
        wt_map.put("158 lbs (71.67 kg)", "158");
        weight_categories.add("159 lbs (72.12 kg)");
        wt_map.put("159 lbs (72.12 kg)", "159");
        weight_categories.add("160 lbs (72.57 kg)");
        wt_map.put("160 lbs (72.57 kg)", "160");
        weight_categories.add("161 lbs (73.03 kg)");
        wt_map.put("161 lbs (73.03 kg)", "161");
        weight_categories.add("162 lbs (73.48 kg)");
        wt_map.put("162 lbs (73.48 kg)", "162");
        weight_categories.add("163 lbs (73.94 kg)");
        wt_map.put("163 lbs (73.94 kg)", "163");
        weight_categories.add("164 lbs (74.39 kg)");
        wt_map.put("164 lbs (74.39 kg)", "164");
        weight_categories.add("165 lbs (74.84 kg)");
        wt_map.put("165 lbs (74.84 kg)", "165");
        weight_categories.add("166 lbs (75.3 kg)");
        wt_map.put("166 lbs (75.3 kg)", "166");
        weight_categories.add("167 lbs (75.75 kg)");
        wt_map.put("167 lbs (75.75 kg)", "167");
        weight_categories.add("168 lbs (76.2 kg)");
        wt_map.put("168 lbs (76.2 kg)", "168");
        weight_categories.add("169 lbs (76.66 kg)");
        wt_map.put("169 lbs (76.66 kg)", "169");
        weight_categories.add("170 lbs (77.11 kg)");
        wt_map.put("170 lbs (77.11 kg)", "170");
        weight_categories.add("171 lbs (77.56 kg)");
        wt_map.put("171 lbs (77.56 kg)", "171");
        weight_categories.add("172 lbs (78.02 kg)");
        wt_map.put("172 lbs (78.02 kg)", "172");
        weight_categories.add("173 lbs (78.47 kg)");
        wt_map.put("173 lbs (78.47 kg)", "173");
        weight_categories.add("174 lbs (78.93 kg)");
        wt_map.put("174 lbs (78.93 kg)", "174");
        weight_categories.add("175 lbs (79.38 kg)");
        wt_map.put("175 lbs (79.38 kg)", "175");
        weight_categories.add("176 lbs (79.83 kg)");
        wt_map.put("176 lbs (79.83 kg)", "176");
        weight_categories.add("177 lbs (80.29 kg)");
        wt_map.put("177 lbs (80.29 kg)", "177");
        weight_categories.add("178 lbs (80.74 kg)");
        wt_map.put("178 lbs (80.74 kg)", "178");
        weight_categories.add("179 lbs (81.19 kg)");
        wt_map.put("179 lbs (81.19 kg)", "179");
        weight_categories.add("180 lbs (81.65 kg)");
        wt_map.put("180 lbs (81.65 kg)", "180");
        weight_categories.add("181 lbs (82.1 kg)");
        wt_map.put("181 lbs (82.1 kg)", "181");
        weight_categories.add("182 lbs (82.55 kg)");
        wt_map.put("182 lbs (82.55 kg)", "182");
        weight_categories.add("183 lbs (83.01 kg)");
        wt_map.put("183 lbs (83.01 kg)", "183");
        weight_categories.add("184 lbs (83.46 kg)");
        wt_map.put("184 lbs (83.46 kg)", "184");
        weight_categories.add("185 lbs (83.91 kg)");
        wt_map.put("185 lbs (83.91 kg)", "185");
        weight_categories.add("186 lbs (84.37 kg)");
        wt_map.put("186 lbs (84.37 kg)", "186");
        weight_categories.add("187 lbs (84.82 kg)");
        wt_map.put("187 lbs (84.82 kg)", "187");
        weight_categories.add("188 lbs (85.28 kg)");
        wt_map.put("188 lbs (85.28 kg)", "188");
        weight_categories.add("189 lbs (85.73 kg)");
        wt_map.put("189 lbs (85.73 kg)", "189");
        weight_categories.add("190 lbs (86.18 kg)");
        wt_map.put("190 lbs (86.18 kg)", "190");
        weight_categories.add("191 lbs (86.64 kg)");
        wt_map.put("191 lbs (86.64 kg)", "191");
        weight_categories.add("192 lbs (87.09 kg)");
        wt_map.put("192 lbs (87.09 kg)", "192");
        weight_categories.add("193 lbs (87.54 kg)");
        wt_map.put("193 lbs (87.54 kg)", "193");
        weight_categories.add("194 lbs (88 kg)");
        wt_map.put("194 lbs (88 kg)", "194");
        weight_categories.add("195 lbs (88.45 kg)");
        wt_map.put("195 lbs (88.45 kg)", "195");
        weight_categories.add("196 lbs (88.9 kg)");
        wt_map.put("196 lbs (88.9 kg)", "196");
        weight_categories.add("197 lbs (89.36 kg)");
        wt_map.put("197 lbs (89.36 kg)", "197");
        weight_categories.add("198 lbs (89.81 kg)");
        wt_map.put("198 lbs (89.81 kg)", "198");
        weight_categories.add("199 lbs (90.26 kg)");
        wt_map.put("199 lbs (90.26 kg)", "199");
        weight_categories.add("200 lbs (90.72 kg)");
        wt_map.put("200 lbs (90.72 kg)", "200");
        weight_categories.add("201 lbs (91.17 kg)");
        wt_map.put("201 lbs (91.17 kg)", "201");
        weight_categories.add("202 lbs (91.63 kg)");
        wt_map.put("202 lbs (91.63 kg)", "202");
        weight_categories.add("203 lbs (92.08 kg)");
        wt_map.put("203 lbs (92.08 kg)", "203");
        weight_categories.add("204 lbs (92.53 kg)");
        wt_map.put("204 lbs (92.53 kg)", "204");
        weight_categories.add("205 lbs (92.99 kg)");
        wt_map.put("205 lbs (92.99 kg)", "205");
        weight_categories.add("206 lbs (93.44 kg)");
        wt_map.put("206 lbs (93.44 kg)", "206");
        weight_categories.add("207 lbs (93.89 kg)");
        wt_map.put("207 lbs (93.89 kg)", "207");
        weight_categories.add("208 lbs (94.35 kg)");
        wt_map.put("208 lbs (94.35 kg)", "208");
        weight_categories.add("209 lbs (94.8 kg)");
        wt_map.put("209 lbs (94.8 kg)", "209");
        weight_categories.add("210 lbs (95.25 kg)");
        wt_map.put("210 lbs (95.25 kg)", "210");
        weight_categories.add("211 lbs (95.71 kg)");
        wt_map.put("211 lbs (95.71 kg)", "211");
        weight_categories.add("212 lbs (96.16 kg)");
        wt_map.put("212 lbs (96.16 kg)", "212");
        weight_categories.add("213 lbs (96.62 kg)");
        wt_map.put("213 lbs (96.62 kg)", "213");
        weight_categories.add("214 lbs (97.07 kg)");
        wt_map.put("214 lbs (97.07 kg)", "214");
        weight_categories.add("215 lbs (97.52 kg)");
        wt_map.put("215 lbs (97.52 kg)", "215");
        weight_categories.add("216 lbs (97.98 kg)");
        wt_map.put("216 lbs (97.98 kg)", "216");
        weight_categories.add("217 lbs (98.43 kg)");
        wt_map.put("217 lbs (98.43 kg)", "217");
        weight_categories.add("218 lbs (98.88 kg)");
        wt_map.put("218 lbs (98.88 kg)", "218");
        weight_categories.add("219 lbs (99.34 kg)");
        wt_map.put("219 lbs (99.34 kg)", "219");
        weight_categories.add("220 lbs (99.79 kg)");
        wt_map.put("220 lbs (99.79 kg)", "220");
        weight_categories.add("221 lbs (100.24 kg)");
        wt_map.put("221 lbs (100.24 kg)", "221");
        weight_categories.add("222 lbs (100.7 kg)");
        wt_map.put("222 lbs (100.7 kg)", "222");
        weight_categories.add("223 lbs (101.15 kg)");
        wt_map.put("223 lbs (101.15 kg)", "223");
        weight_categories.add("224 lbs (101.6 kg)");
        wt_map.put("224 lbs (101.6 kg)", "224");
        weight_categories.add("225 lbs (102.06 kg)");
        wt_map.put("225 lbs (102.06 kg)", "225");
        weight_categories.add("226 lbs (102.51 kg)");
        wt_map.put("226 lbs (102.51 kg)", "226");
        weight_categories.add("227 lbs (102.97 kg)");
        wt_map.put("227 lbs (102.97 kg)", "227");
        weight_categories.add("228 lbs (103.42 kg)");
        wt_map.put("228 lbs (103.42 kg)", "228");
        weight_categories.add("229 lbs (103.87 kg)");
        wt_map.put("229 lbs (103.87 kg)", "229");
        weight_categories.add("230 lbs (104.33 kg)");
        wt_map.put("230 lbs (104.33 kg)", "230");
        weight_categories.add("231 lbs (104.78 kg)");
        wt_map.put("231 lbs (104.78 kg)", "231");
        weight_categories.add("232 lbs (105.23 kg)");
        wt_map.put("232 lbs (105.23 kg)", "232");
        weight_categories.add("233 lbs (105.69 kg)");
        wt_map.put("233 lbs (105.69 kg)", "233");
        weight_categories.add("234 lbs (106.14 kg)");
        wt_map.put("234 lbs (106.14 kg)", "234");
        weight_categories.add("235 lbs (106.59 kg)");
        wt_map.put("235 lbs (106.59 kg)", "235");
        weight_categories.add("236 lbs (107.05 kg)");
        wt_map.put("236 lbs (107.05 kg)", "236");
        weight_categories.add("237 lbs (107.5 kg)");
        wt_map.put("237 lbs (107.5 kg)", "237");
        weight_categories.add("238 lbs (107.95 kg)");
        wt_map.put("238 lbs (107.95 kg)", "238");
        weight_categories.add("239 lbs (108.41 kg)");
        wt_map.put("239 lbs (108.41 kg)", "239");
        weight_categories.add("240 lbs (108.86 kg)");
        wt_map.put("240 lbs (108.86 kg)", "240");
        weight_categories.add("241 lbs (109.32 kg)");
        wt_map.put("241 lbs (109.32 kg)", "241");
        weight_categories.add("242 lbs (109.77 kg)");
        wt_map.put("242 lbs (109.77 kg)", "242");
        weight_categories.add("243 lbs (110.22 kg)");
        wt_map.put("243 lbs (110.22 kg)", "243");
        weight_categories.add("244 lbs (110.68 kg)");
        wt_map.put("244 lbs (110.68 kg)", "244");
        weight_categories.add("245 lbs (111.13 kg)");
        wt_map.put("245 lbs (111.13 kg)", "245");
        weight_categories.add("246 lbs (111.58 kg)");
        wt_map.put("246 lbs (111.58 kg)", "246");
        weight_categories.add("247 lbs (112.04 kg)");
        wt_map.put("247 lbs (112.04 kg)", "247");
        weight_categories.add("248 lbs (112.49 kg)");
        wt_map.put("248 lbs (112.49 kg)", "248");
        weight_categories.add("249 lbs (112.94 kg)");
        wt_map.put("249 lbs (112.94 kg)", "249");
        weight_categories.add("250 lbs (113.4 kg)");
        wt_map.put("250 lbs (113.4 kg)", "250");
        weight_categories.add("251 lbs (113.85 kg)");
        wt_map.put("251 lbs (113.85 kg)", "251");
        weight_categories.add("252 lbs (114.31 kg)");
        wt_map.put("252 lbs (114.31 kg)", "252");
        weight_categories.add("253 lbs (114.76 kg)");
        wt_map.put("253 lbs (114.76 kg)", "253");
        weight_categories.add("254 lbs (115.21 kg)");
        wt_map.put("254 lbs (115.21 kg)", "254");
        weight_categories.add("255 lbs (115.67 kg)");
        wt_map.put("255 lbs (115.67 kg)", "255");
        weight_categories.add("256 lbs (116.12 kg)");
        wt_map.put("256 lbs (116.12 kg)", "256");
        weight_categories.add("257 lbs (116.57 kg)");
        wt_map.put("257 lbs (116.57 kg)", "257");
        weight_categories.add("258 lbs (117.03 kg)");
        wt_map.put("258 lbs (117.03 kg)", "258");
        weight_categories.add("259 lbs (117.48 kg)");
        wt_map.put("259 lbs (117.48 kg)", "259");
        weight_categories.add("260 lbs (117.93 kg)");
        wt_map.put("260 lbs (117.93 kg)", "260");
        weight_categories.add("261 lbs (118.39 kg)");
        wt_map.put("261 lbs (118.39 kg)", "261");
        weight_categories.add("262 lbs (118.84 kg)");
        wt_map.put("262 lbs (118.84 kg)", "262");
        weight_categories.add("263 lbs (119.29 kg)");
        wt_map.put("263 lbs (119.29 kg)", "263");
        weight_categories.add("264 lbs (119.75 kg)");
        wt_map.put("264 lbs (119.75 kg)", "264");
        weight_categories.add("265 lbs (120.2 kg)");
        wt_map.put("265 lbs (120.2 kg)", "265");
        weight_categories.add("266 lbs (120.66 kg)");
        wt_map.put("266 lbs (120.66 kg)", "266");
        weight_categories.add("267 lbs (121.11 kg)");
        wt_map.put("267 lbs (121.11 kg)", "267");
        weight_categories.add("268 lbs (121.56 kg)");
        wt_map.put("268 lbs (121.56 kg)", "268");
        weight_categories.add("269 lbs (122.02 kg)");
        wt_map.put("269 lbs (122.02 kg)", "269");
        weight_categories.add("270 lbs (122.47 kg)");
        wt_map.put("270 lbs (122.47 kg)", "270");
        weight_categories.add("271 lbs (122.92 kg)");
        wt_map.put("271 lbs (122.92 kg)", "271");
        weight_categories.add("272 lbs (123.38 kg)");
        wt_map.put("272 lbs (123.38 kg)", "272");
        weight_categories.add("273 lbs (123.83 kg)");
        wt_map.put("273 lbs (123.83 kg)", "273");
        weight_categories.add("274 lbs (124.28 kg)");
        wt_map.put("274 lbs (124.28 kg)", "274");
        weight_categories.add("275 lbs (124.74 kg)");
        wt_map.put("275 lbs (124.74 kg)", "275");
        weight_categories.add("276 lbs (125.19 kg)");
        wt_map.put("276 lbs (125.19 kg)", "276");
        weight_categories.add("277 lbs (125.65 kg)");
        wt_map.put("277 lbs (125.65 kg)", "277");
        weight_categories.add("278 lbs (126.1 kg)");
        wt_map.put("278 lbs (126.1 kg)", "278");
        weight_categories.add("279 lbs (126.55 kg)");
        wt_map.put("279 lbs (126.55 kg)", "279");
        weight_categories.add("280 lbs (127.01 kg)");
        wt_map.put("280 lbs (127.01 kg)", "280");
        weight_categories.add("281 lbs (127.46 kg)");
        wt_map.put("281 lbs (127.46 kg)", "281");
        weight_categories.add("282 lbs (127.91 kg)");
        wt_map.put("282 lbs (127.91 kg)", "282");
        weight_categories.add("283 lbs (128.37 kg)");
        wt_map.put("283 lbs (128.37 kg)", "283");
        weight_categories.add("284 lbs (128.82 kg)");
        wt_map.put("284 lbs (128.82 kg)", "284");
        weight_categories.add("285 lbs (129.27 kg)");
        wt_map.put("285 lbs (129.27 kg)", "285");
        weight_categories.add("286 lbs (129.73 kg)");
        wt_map.put("286 lbs (129.73 kg)", "286");
        weight_categories.add("287 lbs (130.18 kg)");
        wt_map.put("287 lbs (130.18 kg)", "287");
        weight_categories.add("288 lbs (130.63 kg)");
        wt_map.put("288 lbs (130.63 kg)", "288");
        weight_categories.add("289 lbs (131.09 kg)");
        wt_map.put("289 lbs (131.09 kg)", "289");
        weight_categories.add("290 lbs (131.54 kg)");
        wt_map.put("290 lbs (131.54 kg)", "290");
        weight_categories.add("291 lbs (132 kg)");
        wt_map.put("291 lbs (132 kg)", "291");
        weight_categories.add("292 lbs (132.45 kg)");
        wt_map.put("292 lbs (132.45 kg)", "292");
        weight_categories.add("293 lbs (132.9 kg)");
        wt_map.put("293 lbs (132.9 kg)", "293");
        weight_categories.add("294 lbs (133.36 kg)");
        wt_map.put("294 lbs (133.36 kg)", "294");
        weight_categories.add("295 lbs (133.81 kg)");
        wt_map.put("295 lbs (133.81 kg)", "295");
        weight_categories.add("296 lbs (134.26 kg)");
        wt_map.put("296 lbs (134.26 kg)", "296");
        weight_categories.add("297 lbs (134.72 kg)");
        wt_map.put("297 lbs (134.72 kg)", "297");
        weight_categories.add("298 lbs (135.17 kg)");
        wt_map.put("298 lbs (135.17 kg)", "298");
        weight_categories.add("299 lbs (135.62 kg)");
        wt_map.put("299 lbs (135.62 kg)", "299");
        weight_categories.add("300 lbs (136.08 kg)");
        wt_map.put("300 lbs (136.08 kg)", "300");
        weight_categories.add("301 lbs (136.53 kg)");
        wt_map.put("301 lbs (136.53 kg)", "301");
        weight_categories.add("302 lbs (136.98 kg)");
        wt_map.put("302 lbs (136.98 kg)", "302");
        weight_categories.add("303 lbs (137.44 kg)");
        wt_map.put("303 lbs (137.44 kg)", "303");
        weight_categories.add("304 lbs (137.89 kg)");
        wt_map.put("304 lbs (137.89 kg)", "304");
        weight_categories.add("305 lbs (138.35 kg)");
        wt_map.put("305 lbs (138.35 kg)", "305");
        weight_categories.add("306 lbs (138.8 kg)");
        wt_map.put("306 lbs (138.8 kg)", "306");
        weight_categories.add("307 lbs (139.25 kg)");
        wt_map.put("307 lbs (139.25 kg)", "307");
        weight_categories.add("308 lbs (139.71 kg)");
        wt_map.put("308 lbs (139.71 kg)", "308");
        weight_categories.add("309 lbs (140.16 kg)");
        wt_map.put("309 lbs (140.16 kg)", "309");
        weight_categories.add("310 lbs (140.61 kg)");
        wt_map.put("310 lbs (140.61 kg)", "310");
        weight_categories.add("311 lbs (141.07 kg)");
        wt_map.put("311 lbs (141.07 kg)", "311");
        weight_categories.add("312 lbs (141.52 kg)");
        wt_map.put("312 lbs (141.52 kg)", "312");
        weight_categories.add("313 lbs (141.97 kg)");
        wt_map.put("313 lbs (141.97 kg)", "313");
        weight_categories.add("314 lbs (142.43 kg)");
        wt_map.put("314 lbs (142.43 kg)", "314");
        weight_categories.add("315 lbs (142.88 kg)");
        wt_map.put("315 lbs (142.88 kg)", "315");
        weight_categories.add("316 lbs (143.34 kg)");
        wt_map.put("316 lbs (143.34 kg)", "316");
        weight_categories.add("317 lbs (143.79 kg)");
        wt_map.put("317 lbs (143.79 kg)", "317");
        weight_categories.add("318 lbs (144.24 kg)");
        wt_map.put("318 lbs (144.24 kg)", "318");
        weight_categories.add("319 lbs (144.7 kg)");
        wt_map.put("319 lbs (144.7 kg)", "319");
        weight_categories.add("320 lbs (145.15 kg)");
        wt_map.put("320 lbs (145.15 kg)", "320");
        weight_categories.add("321 lbs (145.6 kg)");
        wt_map.put("321 lbs (145.6 kg)", "321");
        weight_categories.add("322 lbs (146.06 kg)");
        wt_map.put("322 lbs (146.06 kg)", "322");
        weight_categories.add("323 lbs (146.51 kg)");
        wt_map.put("323 lbs (146.51 kg)", "323");
        weight_categories.add("324 lbs (146.96 kg)");
        wt_map.put("324 lbs (146.96 kg)", "324");
        weight_categories.add("325 lbs (147.42 kg)");
        wt_map.put("325 lbs (147.42 kg)", "325");
        weight_categories.add("326 lbs (147.87 kg)");
        wt_map.put("326 lbs (147.87 kg)", "326");
        weight_categories.add("327 lbs (148.32 kg)");
        wt_map.put("327 lbs (148.32 kg)", "327");
        weight_categories.add("328 lbs (148.78 kg)");
        wt_map.put("328 lbs (148.78 kg)", "328");
        weight_categories.add("329 lbs (149.23 kg)");
        wt_map.put("329 lbs (149.23 kg)", "329");
        weight_categories.add("330 lbs (149.69 kg)");
        wt_map.put("330 lbs (149.69 kg)", "330");
    }


    public void apply_height() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(FamilyProfileListActivity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select height");

        final ArrayAdapter<String> height_categories = new ArrayAdapter<String>(FamilyProfileListActivity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        height_categories.add("1' 0\" (30.48 cm)");
        ht_map.put("1' 0\" (30.48 cm)", "1");

        height_categories.add("1' 1\" (33.02 cm)");
        ht_map.put("1' 1\" (33.02 cm)", "2");

        height_categories.add("1' 2\" (35.56 cm)");
        ht_map.put("1' 2\" (35.56 cm)", "3");

        height_categories.add("1' 3\" (38.1 cm)");
        ht_map.put("1' 3\" (38.1 cm)", "4");

        height_categories.add("1' 4\" (40.64 cm)");
        ht_map.put("1' 4\" (40.64 cm)", "5");

        height_categories.add("1' 5\" (43.18 cm)");
        ht_map.put("1' 5\" (43.18 cm)", "6");

        height_categories.add("1' 6\" (45.72 cm)");
        ht_map.put("1' 6\" (45.72 cm)", "7");

        height_categories.add("1' 7\" (48.26 cm)");
        ht_map.put("1' 7\" (48.26 cm)", "8");

        height_categories.add("1' 8\" (50.8 cm)");
        ht_map.put("1' 8\" (50.8 cm)", "9");

        height_categories.add("1' 9\" (53.34 cm)");
        ht_map.put("1' 9\" (53.34 cm)", "10");

        height_categories.add("1' 10\" (55.88 cm)");
        ht_map.put("1' 10\" (55.88 cm)", "11");

        height_categories.add("1' 11\" (58.42 cm)");
        ht_map.put("1' 11\" (58.42 cm)", "12");

        height_categories.add("2' 0\" (60.96 cm)");
        ht_map.put("2' 0\" (60.96 cm)", "13");

        height_categories.add("2' 1\" (63.5 cm)");
        ht_map.put("2' 1\" (63.5 cm)", "14");
        height_categories.add("2' 2\" (66.04 cm)");
        ht_map.put("2' 2\" (66.04 cm)", "15");
        height_categories.add("2' 3\" (68.58 cm)");
        ht_map.put("2' 3\" (68.58 cm)", "16");
        height_categories.add("2' 4\" (71.12 cm)");
        ht_map.put("2' 4\" (71.12 cm)", "17");
        height_categories.add("2' 5\" (73.66 cm)");
        ht_map.put("2' 5\" (73.66 cm)", "18");
        height_categories.add("2' 6\" (76.2 cm)");
        ht_map.put("2' 6\" (76.2 cm)", "19");
        height_categories.add("2' 7\" (78.74 cm)");
        ht_map.put("2' 7\" (78.74 cm)", "20");
        height_categories.add("2' 8\" (81.28 cm)");
        ht_map.put("2' 8\" (81.28 cm)", "21");
        height_categories.add("2' 9\" (83.82 cm)");
        ht_map.put("2' 9\" (83.82 cm)", "22");
        height_categories.add("2' 10\" (86.36 cm)");
        ht_map.put("2' 10\" (86.36 cm)", "23");
        height_categories.add("2' 11\" (88.9 cm)");
        ht_map.put("2' 11\" (88.9 cm)", "24");
        height_categories.add("3' 0\" (91.44 cm)");
        ht_map.put("3' 0\" (91.44 cm)", "25");
        height_categories.add("3' 1\" (93.98 cm)");
        ht_map.put("3' 1\" (93.98 cm)", "26");
        height_categories.add("3' 2\" (96.52 cm)");
        ht_map.put("3' 2\" (96.52 cm)", "27");
        height_categories.add("3' 3\" (99.06 cm)");
        ht_map.put("3' 3\" (99.06 cm)", "28");
        height_categories.add("3' 4\" (101.6 cm)");
        ht_map.put("3' 4\" (101.6 cm)", "29");
        height_categories.add("3' 5\" (104.14 cm)");
        ht_map.put("3' 5\" (104.14 cm)", "30");
        height_categories.add("3' 6\" (106.68 cm)");
        ht_map.put("3' 6\" (106.68 cm)", "31");
        height_categories.add("3' 7\" (109.22 cm)");
        ht_map.put("3' 7\" (109.22 cm)", "32");
        height_categories.add("3' 8\" (111.76 cm)");
        ht_map.put("3' 8\" (111.76 cm)", "33");
        height_categories.add("3' 9\" (114.3 cm)");
        ht_map.put("3' 9\" (114.3 cm)", "34");
        height_categories.add("3' 10\" (116.84 cm)");
        ht_map.put("3' 10\" (116.84 cm)", "35");
        height_categories.add("3' 11\" (119.38 cm)");
        ht_map.put("3' 11\" (119.38 cm)", "36");
        height_categories.add("4' 0\" (121.92 cm)");
        ht_map.put("4' 0\" (121.92 cm)", "37");
        height_categories.add("4' 1\" (124.46 cm)");
        ht_map.put("4' 1\" (124.46 cm)", "38");
        height_categories.add("4' 2\" (127 cm)");
        ht_map.put("4' 2\" (127 cm)", "39");
        height_categories.add("4' 3\" (129.54 cm)");
        ht_map.put("4' 3\" (129.54 cm)", "40");
        height_categories.add("4' 4\" (132.08 cm)");
        ht_map.put("4' 4\" (132.08 cm)", "41");
        height_categories.add("4' 5\" (134.62 cm)");
        ht_map.put("4' 5\" (134.62 cm)", "42");
        height_categories.add("4' 6\" (137.16 cm)");
        ht_map.put("4' 6\" (137.16 cm)", "43");
        height_categories.add("4' 7\" (139.7 cm)");
        ht_map.put("4' 7\" (139.7 cm)", "44");
        height_categories.add("4' 8\" (142.24 cm)");
        ht_map.put("4' 8\" (142.24 cm)", "45");
        height_categories.add("4' 9\" (144.78 cm)");
        ht_map.put("4' 9\" (144.78 cm)", "46");
        height_categories.add("4' 10\" (147.32 cm)");
        ht_map.put("4' 10\" (147.32 cm)", "47");
        height_categories.add("4' 11\" (149.86 cm)");
        ht_map.put("4' 11\" (149.86 cm)", "48");
        height_categories.add("5' 0\" (152.4 cm)");
        ht_map.put("5' 0\" (152.4 cm)", "49");
        height_categories.add("5' 1\" (154.94 cm)");
        ht_map.put("5' 1\" (154.94 cm)", "50");
        height_categories.add("5' 2\" (157.48 cm)");
        ht_map.put("5' 2\" (157.48 cm)", "51");
        height_categories.add("5' 3\" (160.02 cm)");
        ht_map.put("5' 3\" (160.02 cm)", "52");
        height_categories.add("5' 4\" (162.56 cm)");
        ht_map.put("5' 4\" (162.56 cm)", "53");
        height_categories.add("5' 5\" (165.1 cm)");
        ht_map.put("5' 5\" (165.1 cm)", "54");
        height_categories.add("5' 6\" (167.64 cm)");
        ht_map.put("5' 6\" (167.64 cm)", "55");
        height_categories.add("5' 7\" (170.18 cm)");
        ht_map.put("5' 7\" (170.18 cm)", "56");
        height_categories.add("5' 8\" (172.72 cm)");
        ht_map.put("5' 8\" (172.72 cm)", "57");
        height_categories.add("5' 9\" (175.26 cm)");
        ht_map.put("5' 9\" (175.26 cm)", "58");
        height_categories.add("5' 10\" (177.8 cm)");
        ht_map.put("5' 10\" (177.8 cm)", "59");
        height_categories.add("5' 11\" (180.34 cm)");
        ht_map.put("5' 11\" (180.34 cm)", "60");
        height_categories.add("6' 0\" (182.88 cm)");
        ht_map.put("6' 0\" (182.88 cm)", "61");
        height_categories.add("6' 1\" (185.42 cm)");
        ht_map.put("6' 1\" (185.42 cm)", "62");
        height_categories.add("6' 2\" (187.96 cm)");
        ht_map.put("6' 2\" (187.96 cm)", "63");
        height_categories.add("6' 3\" (190.5 cm)");
        ht_map.put("6' 3\" (190.5 cm)", "64");
        height_categories.add("6' 4\" (193.04 cm)");
        ht_map.put("6' 4\" (193.04 cm)", "65");
        height_categories.add("6' 5\" (195.58 cm)");
        ht_map.put("6' 5\" (195.58 cm)", "66");
        height_categories.add("6' 6\" (198.12 cm)");
        ht_map.put("6' 6\" (198.12 cm)", "67");
        height_categories.add("6' 7\" (200.66 cm)");
        ht_map.put("6' 7\" (200.66 cm)", "68");
        height_categories.add("6' 8\" (203.2 cm)");
        ht_map.put("6' 8\" (203.2 cm)", "69");
        height_categories.add("6' 9\" (205.74 cm)");
        ht_map.put("6' 9\" (205.74 cm)", "70");
        height_categories.add("6' 10\" (208.28 cm)");
        ht_map.put("6' 10\" (208.28 cm)", "71");
        height_categories.add("6' 11\" (210.82 cm)");
        ht_map.put("6' 11\" (210.82 cm)", "72");
        height_categories.add("7' 0\" (213.36 cm)");
        ht_map.put("7' 0\" (213.36 cm)", "73");
        height_categories.add("7' 1\" (215.9 cm)");
        ht_map.put("7' 1\" (215.9 cm)", "74");
        height_categories.add("7' 2\" (218.44 cm)");
        ht_map.put("7' 2\" (218.44 cm)", "75");
        height_categories.add("7' 3\" (220.98 cm)");
        ht_map.put("7' 3\" (220.98 cm)", "76");
        height_categories.add("7' 4\" (223.52 cm)");
        ht_map.put("7' 4\" (223.52 cm)", "77");
        height_categories.add("7' 5\" (226.06 cm)");
        ht_map.put("7' 5\" (226.06 cm)", "78");
        height_categories.add("7' 6\" (228.6 cm)");
        ht_map.put("7' 6\" (228.6 cm)", "79");
        height_categories.add("7' 7\" (231.14 cm)");
        ht_map.put("7' 7\" (231.14 cm)", "80");
        height_categories.add("7' 8\" (233.68 cm)");
        ht_map.put("7' 8\" (233.68 cm)", "81");
        height_categories.add("7' 9\" (236.22 cm)");
        ht_map.put("7' 9\" (236.22 cm)", "82");
        height_categories.add("7' 10\" (238.76 cm)");
        ht_map.put("7' 10\" (238.76 cm)", "83");
        height_categories.add("7' 11\" (241.3 cm)");
        ht_map.put("7' 11\" (241.3 cm)", "84");
        height_categories.add("8' 0\" (243.84 cm)");
        ht_map.put("8' 0\" (243.84 cm)", "85");
        height_categories.add("8' 1\" (246.38 cm)");
        ht_map.put("8' 1\" (246.38 cm)", "86");
        height_categories.add("8' 2\" (248.92 cm)");
        ht_map.put("8' 2\" (248.92 cm)", "87");
        height_categories.add("8' 3\" (251.46 cm)");
        ht_map.put("8' 3\" (251.46 cm)", "88");
        height_categories.add("8' 4\" (254 cm)");
        ht_map.put("8' 4\" (254 cm)", "89");
        height_categories.add("8' 5\" (256.54 cm)");
        ht_map.put("8' 5\" (256.54 cm)", "90");
        height_categories.add("8' 6\" (259.08 cm)");
        ht_map.put("8' 6\" (259.08 cm)", "91");
        height_categories.add("8' 7\" (261.62 cm)");
        ht_map.put("8' 7\" (261.62 cm)", "92");
        height_categories.add("8' 8\" (264.16 cm)");
        ht_map.put("8' 8\" (264.16 cm)", "93");
        height_categories.add("8' 9\" (266.7 cm)");
        ht_map.put("8' 9\" (266.7 cm)", "94");
        height_categories.add("8' 10\" (269.24 cm)");
        ht_map.put("8' 10\" (269.24 cm)", "95");
        height_categories.add("8' 11\" (271.78 cm)");
        ht_map.put("8' 11\" (271.78 cm)", "96");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(height_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = height_categories.getItem(which);
                String select_value = (ht_map).get(height_categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                height_val = select_value;
                height_name = select_text;

                height_title.setText(height_name);
            }
        });
        builderSingle.show();
    }


    public void apply_weight() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(FamilyProfileListActivity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select weight");

        final ArrayAdapter<String> weight_categories = new ArrayAdapter<String>(FamilyProfileListActivity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        weight_categories.add("1 lbs (0.45 kg)");
        wt_map.put("1 lbs (0.45 kg)", "1");
        weight_categories.add("2 lbs (0.91 kg)");
        wt_map.put("2 lbs (0.91 kg)", "2");
        weight_categories.add("3 lbs (1.36 kg)");
        wt_map.put("3 lbs (1.36 kg)", "3");
        weight_categories.add("4 lbs (1.81 kg)");
        wt_map.put("4 lbs (1.81 kg)", "4");
        weight_categories.add("5 lbs (2.27 kg)");
        wt_map.put("5 lbs (2.27 kg)", "5");
        weight_categories.add("6 lbs (2.72 kg)");
        wt_map.put("6 lbs (2.72 kg)", "6");
        weight_categories.add("7 lbs (3.18 kg)");
        wt_map.put("7 lbs (3.18 kg)", "7");
        weight_categories.add("8 lbs (3.63 kg)");
        wt_map.put("8 lbs (3.63 kg)", "8");
        weight_categories.add("9 lbs (4.08 kg)");
        wt_map.put("9 lbs (4.08 kg)", "9");
        weight_categories.add("10 lbs (4.54 kg)");
        wt_map.put("10 lbs (4.54 kg)", "10");
        weight_categories.add("11 lbs (4.99 kg)");
        wt_map.put("11 lbs (4.99 kg)", "11");
        weight_categories.add("12 lbs (5.44 kg)");
        wt_map.put("12 lbs (5.44 kg)", "12");
        weight_categories.add("13 lbs (5.9 kg)");
        wt_map.put("13 lbs (5.9 kg)", "13");
        weight_categories.add("14 lbs (6.35 kg)");
        wt_map.put("14 lbs (6.35 kg)", "14");
        weight_categories.add("15 lbs (6.8 kg)");
        wt_map.put("15 lbs (6.8 kg)", "15");
        weight_categories.add("16 lbs (7.26 kg)");
        wt_map.put("16 lbs (7.26 kg)", "16");
        weight_categories.add("17 lbs (7.71 kg)");
        wt_map.put("17 lbs (7.71 kg)", "17");
        weight_categories.add("18 lbs (8.16 kg)");
        wt_map.put("18 lbs (8.16 kg)", "18");
        weight_categories.add("19 lbs (8.62 kg)");
        wt_map.put("19 lbs (8.62 kg)", "19");
        weight_categories.add("20 lbs (9.07 kg)");
        wt_map.put("20 lbs (9.07 kg)", "20");
        weight_categories.add("21 lbs (9.53 kg)");
        wt_map.put("21 lbs (9.53 kg)", "21");
        weight_categories.add("22 lbs (9.98 kg)");
        wt_map.put("22 lbs (9.98 kg)", "22");
        weight_categories.add("23 lbs (10.43 kg)");
        wt_map.put("23 lbs (10.43 kg)", "23");
        weight_categories.add("24 lbs (10.89 kg)");
        wt_map.put("24 lbs (10.89 kg)", "24");
        weight_categories.add("25 lbs (11.34 kg)");
        wt_map.put("25 lbs (11.34 kg)", "25");
        weight_categories.add("26 lbs (11.79 kg)");
        wt_map.put("26 lbs (11.79 kg)", "26");
        weight_categories.add("27 lbs (12.25 kg)");
        wt_map.put("27 lbs (12.25 kg)", "27");
        weight_categories.add("28 lbs (12.7 kg)");
        wt_map.put("28 lbs (12.7 kg)", "28");
        weight_categories.add("29 lbs (13.15 kg)");
        wt_map.put("29 lbs (13.15 kg)", "29");
        weight_categories.add("30 lbs (13.61 kg)");
        wt_map.put("30 lbs (13.61 kg)", "30");
        weight_categories.add("31 lbs (14.06 kg)");
        wt_map.put("31 lbs (14.06 kg)", "31");
        weight_categories.add("32 lbs (14.51 kg)");
        wt_map.put("32 lbs (14.51 kg)", "32");
        weight_categories.add("33 lbs (14.97 kg)");
        wt_map.put("33 lbs (14.97 kg)", "33");
        weight_categories.add("34 lbs (15.42 kg)");
        wt_map.put("34 lbs (15.42 kg)", "34");
        weight_categories.add("35 lbs (15.88 kg)");
        wt_map.put("35 lbs (15.88 kg)", "35");
        weight_categories.add("36 lbs (16.33 kg)");
        wt_map.put("36 lbs (16.33 kg)", "36");
        weight_categories.add("37 lbs (16.78 kg)");
        wt_map.put("37 lbs (16.78 kg)", "37");
        weight_categories.add("38 lbs (17.24 kg)");
        wt_map.put("38 lbs (17.24 kg)", "38");
        weight_categories.add("39 lbs (17.69 kg)");
        wt_map.put("39 lbs (17.69 kg)", "39");
        weight_categories.add("40 lbs (18.14 kg)");
        wt_map.put("40 lbs (18.14 kg)", "40");
        weight_categories.add("41 lbs (18.6 kg)");
        wt_map.put("41 lbs (18.6 kg)", "41");
        weight_categories.add("42 lbs (19.05 kg)");
        wt_map.put("42 lbs (19.05 kg)", "42");
        weight_categories.add("43 lbs (19.5 kg)");
        wt_map.put("43 lbs (19.5 kg)", "43");
        weight_categories.add("44 lbs (19.96 kg)");
        wt_map.put("44 lbs (19.96 kg)", "44");
        weight_categories.add("45 lbs (20.41 kg)");
        wt_map.put("45 lbs (20.41 kg)", "45");
        weight_categories.add("46 lbs (20.87 kg)");
        wt_map.put("46 lbs (20.87 kg)", "46");
        weight_categories.add("47 lbs (21.32 kg)");
        wt_map.put("47 lbs (21.32 kg)", "47");
        weight_categories.add("48 lbs (21.77 kg)");
        wt_map.put("48 lbs (21.77 kg)", "48");
        weight_categories.add("49 lbs (22.23 kg)");
        wt_map.put("49 lbs (22.23 kg)", "49");
        weight_categories.add("50 lbs (22.68 kg)");
        wt_map.put("50 lbs (22.68 kg)", "50");
        weight_categories.add("51 lbs (23.13 kg)");
        wt_map.put("51 lbs (23.13 kg)", "51");
        weight_categories.add("52 lbs (23.59 kg)");
        wt_map.put("52 lbs (23.59 kg)", "52");
        weight_categories.add("53 lbs (24.04 kg)");
        wt_map.put("53 lbs (24.04 kg)", "53");
        weight_categories.add("54 lbs (24.49 kg)");
        wt_map.put("54 lbs (24.49 kg)", "54");
        weight_categories.add("55 lbs (24.95 kg)");
        wt_map.put("55 lbs (24.95 kg)", "55");
        weight_categories.add("56 lbs (25.4 kg)");
        wt_map.put("56 lbs (25.4 kg)", "56");
        weight_categories.add("57 lbs (25.85 kg)");
        wt_map.put("57 lbs (25.85 kg)", "57");
        weight_categories.add("58 lbs (26.31 kg)");
        wt_map.put("58 lbs (26.31 kg)", "58");
        weight_categories.add("59 lbs (26.76 kg)");
        wt_map.put("59 lbs (26.76 kg)", "59");
        weight_categories.add("60 lbs (27.22 kg)");
        wt_map.put("60 lbs (27.22 kg)", "60");
        weight_categories.add("61 lbs (27.67 kg)");
        wt_map.put("61 lbs (27.67 kg)", "61");
        weight_categories.add("62 lbs (28.12 kg)");
        wt_map.put("62 lbs (28.12 kg)", "62");
        weight_categories.add("63 lbs (28.58 kg)");
        wt_map.put("63 lbs (28.58 kg)", "63");
        weight_categories.add("64 lbs (29.03 kg)");
        wt_map.put("64 lbs (29.03 kg)", "64");
        weight_categories.add("65 lbs (29.48 kg)");
        wt_map.put("65 lbs (29.48 kg)", "65");
        weight_categories.add("66 lbs (29.94 kg)");
        wt_map.put("66 lbs (29.94 kg)", "66");
        weight_categories.add("67 lbs (30.39 kg)");
        wt_map.put("67 lbs (30.39 kg)", "67");
        weight_categories.add("68 lbs (30.84 kg)");
        wt_map.put("68 lbs (30.84 kg)", "68");
        weight_categories.add("69 lbs (31.3 kg)");
        wt_map.put("69 lbs (31.3 kg)", "69");
        weight_categories.add("70 lbs (31.75 kg)");
        wt_map.put("70 lbs (31.75 kg)", "70");
        weight_categories.add("71 lbs (32.21 kg)");
        wt_map.put("71 lbs (32.21 kg)", "71");
        weight_categories.add("72 lbs (32.66 kg)");
        wt_map.put("72 lbs (32.66 kg)", "72");
        weight_categories.add("73 lbs (33.11 kg)");
        wt_map.put("73 lbs (33.11 kg)", "73");
        weight_categories.add("74 lbs (33.57 kg)");
        wt_map.put("74 lbs (33.57 kg)", "74");
        weight_categories.add("75 lbs (34.02 kg)");
        wt_map.put("75 lbs (34.02 kg)", "75");
        weight_categories.add("76 lbs (34.47 kg)");
        wt_map.put("76 lbs (34.47 kg)", "76");
        weight_categories.add("77 lbs (34.93 kg)");
        wt_map.put("77 lbs (34.93 kg)", "77");
        weight_categories.add("78 lbs (35.38 kg)");
        wt_map.put("78 lbs (35.38 kg)", "78");
        weight_categories.add("79 lbs (35.83 kg)");
        wt_map.put("79 lbs (35.83 kg)", "79");
        weight_categories.add("80 lbs (36.29 kg)");
        wt_map.put("80 lbs (36.29 kg)", "80");
        weight_categories.add("81 lbs (36.74 kg)");
        wt_map.put("81 lbs (36.74 kg)", "81");
        weight_categories.add("82 lbs (37.19 kg)");
        wt_map.put("82 lbs (37.19 kg)", "82");
        weight_categories.add("83 lbs (37.65 kg)");
        wt_map.put("83 lbs (37.65 kg)", "83");
        weight_categories.add("84 lbs (38.1 kg)");
        wt_map.put("84 lbs (38.1 kg)", "84");
        weight_categories.add("85 lbs (38.56 kg)");
        wt_map.put("85 lbs (38.56 kg)", "85");
        weight_categories.add("86 lbs (39.01 kg)");
        wt_map.put("86 lbs (39.01 kg)", "86");
        weight_categories.add("87 lbs (39.46 kg)");
        wt_map.put("87 lbs (39.46 kg)", "87");
        weight_categories.add("88 lbs (39.92 kg)");
        wt_map.put("88 lbs (39.92 kg)", "88");
        weight_categories.add("89 lbs (40.37 kg)");
        wt_map.put("89 lbs (40.37 kg)", "89");
        weight_categories.add("90 lbs (40.82 kg)");
        wt_map.put("90 lbs (40.82 kg)", "90");
        weight_categories.add("91 lbs (41.28 kg)");
        wt_map.put("91 lbs (41.28 kg)", "91");
        weight_categories.add("92 lbs (41.73 kg)");
        wt_map.put("92 lbs (41.73 kg)", "92");
        weight_categories.add("93 lbs (42.18 kg)");
        wt_map.put("93 lbs (42.18 kg)", "93");
        weight_categories.add("94 lbs (42.64 kg)");
        wt_map.put("94 lbs (42.64 kg)", "94");
        weight_categories.add("95 lbs (43.09 kg)");
        wt_map.put("95 lbs (43.09 kg)", "95");
        weight_categories.add("96 lbs (43.54 kg)");
        wt_map.put("96 lbs (43.54 kg)", "96");
        weight_categories.add("97 lbs (44 kg)");
        wt_map.put("97 lbs (44 kg)", "97");
        weight_categories.add("98 lbs (44.45 kg)");
        wt_map.put("98 lbs (44.45 kg)", "98");
        weight_categories.add("99 lbs (44.91 kg)");
        wt_map.put("99 lbs (44.91 kg)", "99");
        weight_categories.add("100 lbs (45.36 kg)");
        wt_map.put("100 lbs (45.36 kg)", "100");
        weight_categories.add("101 lbs (45.81 kg)");
        wt_map.put("101 lbs (45.81 kg)", "101");
        weight_categories.add("102 lbs (46.27 kg)");
        wt_map.put("102 lbs (46.27 kg)", "102");
        weight_categories.add("103 lbs (46.72 kg)");
        wt_map.put("103 lbs (46.72 kg)", "103");
        weight_categories.add("104 lbs (47.17 kg)");
        wt_map.put("104 lbs (47.17 kg)", "104");
        weight_categories.add("105 lbs (47.63 kg)");
        wt_map.put("105 lbs (47.63 kg)", "105");
        weight_categories.add("106 lbs (48.08 kg)");
        wt_map.put("106 lbs (48.08 kg)", "106");
        weight_categories.add("107 lbs (48.53 kg)");
        wt_map.put("107 lbs (48.53 kg)", "107");
        weight_categories.add("108 lbs (48.99 kg)");
        wt_map.put("108 lbs (48.99 kg)", "108");
        weight_categories.add("109 lbs (49.44 kg)");
        wt_map.put("109 lbs (49.44 kg)", "109");
        weight_categories.add("110 lbs (49.9 kg)");
        wt_map.put("110 lbs (49.9 kg)", "110");
        weight_categories.add("111 lbs (50.35 kg)");
        wt_map.put("111 lbs (50.35 kg)", "111");
        weight_categories.add("112 lbs (50.8 kg)");
        wt_map.put("112 lbs (50.8 kg)", "112");
        weight_categories.add("113 lbs (51.26 kg)");
        wt_map.put("113 lbs (51.26 kg)", "113");
        weight_categories.add("114 lbs (51.71 kg)");
        wt_map.put("114 lbs (51.71 kg)", "114");
        weight_categories.add("115 lbs (52.16 kg)");
        wt_map.put("115 lbs (52.16 kg)", "115");
        weight_categories.add("116 lbs (52.62 kg)");
        wt_map.put("116 lbs (52.62 kg)", "116");
        weight_categories.add("117 lbs (53.07 kg)");
        wt_map.put("117 lbs (53.07 kg)", "117");
        weight_categories.add("118 lbs (53.52 kg)");
        wt_map.put("118 lbs (53.52 kg)", "118");
        weight_categories.add("119 lbs (53.98 kg)");
        wt_map.put("119 lbs (53.98 kg)", "119");
        weight_categories.add("120 lbs (54.43 kg)");
        wt_map.put("120 lbs (54.43 kg)", "120");
        weight_categories.add("121 lbs (54.88 kg)");
        wt_map.put("121 lbs (54.88 kg)", "121");
        weight_categories.add("122 lbs (55.34 kg)");
        wt_map.put("122 lbs (55.34 kg)", "122");
        weight_categories.add("123 lbs (55.79 kg)");
        wt_map.put("123 lbs (55.79 kg)", "123");
        weight_categories.add("124 lbs (56.25 kg)");
        wt_map.put("124 lbs (56.25 kg)", "124");
        weight_categories.add("125 lbs (56.7 kg)");
        wt_map.put("125 lbs (56.7 kg)", "125");
        weight_categories.add("126 lbs (57.15 kg)");
        wt_map.put("126 lbs (57.15 kg)", "126");
        weight_categories.add("127 lbs (57.61 kg)");
        wt_map.put("127 lbs (57.61 kg)", "127");
        weight_categories.add("128 lbs (58.06 kg)");
        wt_map.put("128 lbs (58.06 kg)", "128");
        weight_categories.add("129 lbs (58.51 kg)");
        wt_map.put("129 lbs (58.51 kg)", "129");
        weight_categories.add("130 lbs (58.97 kg)");
        wt_map.put("130 lbs (58.97 kg)", "130");
        weight_categories.add("131 lbs (59.42 kg)");
        wt_map.put("131 lbs (59.42 kg)", "131");
        weight_categories.add("132 lbs (59.87 kg)");
        wt_map.put("132 lbs (59.87 kg)", "132");
        weight_categories.add("133 lbs (60.33 kg)");
        wt_map.put("133 lbs (60.33 kg)", "133");
        weight_categories.add("134 lbs (60.78 kg)");
        wt_map.put("134 lbs (60.78 kg)", "134");
        weight_categories.add("135 lbs (61.23 kg)");
        wt_map.put("135 lbs (61.23 kg)", "135");
        weight_categories.add("136 lbs (61.69 kg)");
        wt_map.put("136 lbs (61.69 kg)", "136");
        weight_categories.add("137 lbs (62.14 kg)");
        wt_map.put("137 lbs (62.14 kg)", "137");
        weight_categories.add("138 lbs (62.6 kg)");
        wt_map.put("138 lbs (62.6 kg)", "138");
        weight_categories.add("139 lbs (63.05 kg)");
        wt_map.put("139 lbs (63.05 kg)", "139");
        weight_categories.add("140 lbs (63.5 kg)");
        wt_map.put("140 lbs (63.5 kg)", "140");
        weight_categories.add("141 lbs (63.96 kg)");
        wt_map.put("141 lbs (63.96 kg)", "141");
        weight_categories.add("142 lbs (64.41 kg)");
        wt_map.put("142 lbs (64.41 kg)", "142");
        weight_categories.add("143 lbs (64.86 kg)");
        wt_map.put("143 lbs (64.86 kg)", "143");
        weight_categories.add("144 lbs (65.32 kg)");
        wt_map.put("144 lbs (65.32 kg)", "144");
        weight_categories.add("145 lbs (65.77 kg)");
        wt_map.put("145 lbs (65.77 kg)", "145");
        weight_categories.add("146 lbs (66.22 kg)");
        wt_map.put("146 lbs (66.22 kg)", "146");
        weight_categories.add("147 lbs (66.68 kg)");
        wt_map.put("147 lbs (66.68 kg)", "147");
        weight_categories.add("148 lbs (67.13 kg)");
        wt_map.put("148 lbs (67.13 kg)", "148");
        weight_categories.add("149 lbs (67.59 kg)");
        wt_map.put("149 lbs (67.59 kg)", "149");
        weight_categories.add("150 lbs (68.04 kg)");
        wt_map.put("150 lbs (68.04 kg)", "150");
        weight_categories.add("151 lbs (68.49 kg)");
        wt_map.put("151 lbs (68.49 kg)", "151");
        weight_categories.add("152 lbs (68.95 kg)");
        wt_map.put("152 lbs (68.95 kg)", "152");
        weight_categories.add("153 lbs (69.4 kg)");
        wt_map.put("153 lbs (69.4 kg)", "153");
        weight_categories.add("154 lbs (69.85 kg)");
        wt_map.put("154 lbs (69.85 kg)", "154");
        weight_categories.add("155 lbs (70.31 kg)");
        wt_map.put("155 lbs (70.31 kg)", "155");
        weight_categories.add("156 lbs (70.76 kg)");
        wt_map.put("156 lbs (70.76 kg)", "156");
        weight_categories.add("157 lbs (71.21 kg)");
        wt_map.put("157 lbs (71.21 kg)", "157");
        weight_categories.add("158 lbs (71.67 kg)");
        wt_map.put("158 lbs (71.67 kg)", "158");
        weight_categories.add("159 lbs (72.12 kg)");
        wt_map.put("159 lbs (72.12 kg)", "159");
        weight_categories.add("160 lbs (72.57 kg)");
        wt_map.put("160 lbs (72.57 kg)", "160");
        weight_categories.add("161 lbs (73.03 kg)");
        wt_map.put("161 lbs (73.03 kg)", "161");
        weight_categories.add("162 lbs (73.48 kg)");
        wt_map.put("162 lbs (73.48 kg)", "162");
        weight_categories.add("163 lbs (73.94 kg)");
        wt_map.put("163 lbs (73.94 kg)", "163");
        weight_categories.add("164 lbs (74.39 kg)");
        wt_map.put("164 lbs (74.39 kg)", "164");
        weight_categories.add("165 lbs (74.84 kg)");
        wt_map.put("165 lbs (74.84 kg)", "165");
        weight_categories.add("166 lbs (75.3 kg)");
        wt_map.put("166 lbs (75.3 kg)", "166");
        weight_categories.add("167 lbs (75.75 kg)");
        wt_map.put("167 lbs (75.75 kg)", "167");
        weight_categories.add("168 lbs (76.2 kg)");
        wt_map.put("168 lbs (76.2 kg)", "168");
        weight_categories.add("169 lbs (76.66 kg)");
        wt_map.put("169 lbs (76.66 kg)", "169");
        weight_categories.add("170 lbs (77.11 kg)");
        wt_map.put("170 lbs (77.11 kg)", "170");
        weight_categories.add("171 lbs (77.56 kg)");
        wt_map.put("171 lbs (77.56 kg)", "171");
        weight_categories.add("172 lbs (78.02 kg)");
        wt_map.put("172 lbs (78.02 kg)", "172");
        weight_categories.add("173 lbs (78.47 kg)");
        wt_map.put("173 lbs (78.47 kg)", "173");
        weight_categories.add("174 lbs (78.93 kg)");
        wt_map.put("174 lbs (78.93 kg)", "174");
        weight_categories.add("175 lbs (79.38 kg)");
        wt_map.put("175 lbs (79.38 kg)", "175");
        weight_categories.add("176 lbs (79.83 kg)");
        wt_map.put("176 lbs (79.83 kg)", "176");
        weight_categories.add("177 lbs (80.29 kg)");
        wt_map.put("177 lbs (80.29 kg)", "177");
        weight_categories.add("178 lbs (80.74 kg)");
        wt_map.put("178 lbs (80.74 kg)", "178");
        weight_categories.add("179 lbs (81.19 kg)");
        wt_map.put("179 lbs (81.19 kg)", "179");
        weight_categories.add("180 lbs (81.65 kg)");
        wt_map.put("180 lbs (81.65 kg)", "180");
        weight_categories.add("181 lbs (82.1 kg)");
        wt_map.put("181 lbs (82.1 kg)", "181");
        weight_categories.add("182 lbs (82.55 kg)");
        wt_map.put("182 lbs (82.55 kg)", "182");
        weight_categories.add("183 lbs (83.01 kg)");
        wt_map.put("183 lbs (83.01 kg)", "183");
        weight_categories.add("184 lbs (83.46 kg)");
        wt_map.put("184 lbs (83.46 kg)", "184");
        weight_categories.add("185 lbs (83.91 kg)");
        wt_map.put("185 lbs (83.91 kg)", "185");
        weight_categories.add("186 lbs (84.37 kg)");
        wt_map.put("186 lbs (84.37 kg)", "186");
        weight_categories.add("187 lbs (84.82 kg)");
        wt_map.put("187 lbs (84.82 kg)", "187");
        weight_categories.add("188 lbs (85.28 kg)");
        wt_map.put("188 lbs (85.28 kg)", "188");
        weight_categories.add("189 lbs (85.73 kg)");
        wt_map.put("189 lbs (85.73 kg)", "189");
        weight_categories.add("190 lbs (86.18 kg)");
        wt_map.put("190 lbs (86.18 kg)", "190");
        weight_categories.add("191 lbs (86.64 kg)");
        wt_map.put("191 lbs (86.64 kg)", "191");
        weight_categories.add("192 lbs (87.09 kg)");
        wt_map.put("192 lbs (87.09 kg)", "192");
        weight_categories.add("193 lbs (87.54 kg)");
        wt_map.put("193 lbs (87.54 kg)", "193");
        weight_categories.add("194 lbs (88 kg)");
        wt_map.put("194 lbs (88 kg)", "194");
        weight_categories.add("195 lbs (88.45 kg)");
        wt_map.put("195 lbs (88.45 kg)", "195");
        weight_categories.add("196 lbs (88.9 kg)");
        wt_map.put("196 lbs (88.9 kg)", "196");
        weight_categories.add("197 lbs (89.36 kg)");
        wt_map.put("197 lbs (89.36 kg)", "197");
        weight_categories.add("198 lbs (89.81 kg)");
        wt_map.put("198 lbs (89.81 kg)", "198");
        weight_categories.add("199 lbs (90.26 kg)");
        wt_map.put("199 lbs (90.26 kg)", "199");
        weight_categories.add("200 lbs (90.72 kg)");
        wt_map.put("200 lbs (90.72 kg)", "200");
        weight_categories.add("201 lbs (91.17 kg)");
        wt_map.put("201 lbs (91.17 kg)", "201");
        weight_categories.add("202 lbs (91.63 kg)");
        wt_map.put("202 lbs (91.63 kg)", "202");
        weight_categories.add("203 lbs (92.08 kg)");
        wt_map.put("203 lbs (92.08 kg)", "203");
        weight_categories.add("204 lbs (92.53 kg)");
        wt_map.put("204 lbs (92.53 kg)", "204");
        weight_categories.add("205 lbs (92.99 kg)");
        wt_map.put("205 lbs (92.99 kg)", "205");
        weight_categories.add("206 lbs (93.44 kg)");
        wt_map.put("206 lbs (93.44 kg)", "206");
        weight_categories.add("207 lbs (93.89 kg)");
        wt_map.put("207 lbs (93.89 kg)", "207");
        weight_categories.add("208 lbs (94.35 kg)");
        wt_map.put("208 lbs (94.35 kg)", "208");
        weight_categories.add("209 lbs (94.8 kg)");
        wt_map.put("209 lbs (94.8 kg)", "209");
        weight_categories.add("210 lbs (95.25 kg)");
        wt_map.put("210 lbs (95.25 kg)", "210");
        weight_categories.add("211 lbs (95.71 kg)");
        wt_map.put("211 lbs (95.71 kg)", "211");
        weight_categories.add("212 lbs (96.16 kg)");
        wt_map.put("212 lbs (96.16 kg)", "212");
        weight_categories.add("213 lbs (96.62 kg)");
        wt_map.put("213 lbs (96.62 kg)", "213");
        weight_categories.add("214 lbs (97.07 kg)");
        wt_map.put("214 lbs (97.07 kg)", "214");
        weight_categories.add("215 lbs (97.52 kg)");
        wt_map.put("215 lbs (97.52 kg)", "215");
        weight_categories.add("216 lbs (97.98 kg)");
        wt_map.put("216 lbs (97.98 kg)", "216");
        weight_categories.add("217 lbs (98.43 kg)");
        wt_map.put("217 lbs (98.43 kg)", "217");
        weight_categories.add("218 lbs (98.88 kg)");
        wt_map.put("218 lbs (98.88 kg)", "218");
        weight_categories.add("219 lbs (99.34 kg)");
        wt_map.put("219 lbs (99.34 kg)", "219");
        weight_categories.add("220 lbs (99.79 kg)");
        wt_map.put("220 lbs (99.79 kg)", "220");
        weight_categories.add("221 lbs (100.24 kg)");
        wt_map.put("221 lbs (100.24 kg)", "221");
        weight_categories.add("222 lbs (100.7 kg)");
        wt_map.put("222 lbs (100.7 kg)", "222");
        weight_categories.add("223 lbs (101.15 kg)");
        wt_map.put("223 lbs (101.15 kg)", "223");
        weight_categories.add("224 lbs (101.6 kg)");
        wt_map.put("224 lbs (101.6 kg)", "224");
        weight_categories.add("225 lbs (102.06 kg)");
        wt_map.put("225 lbs (102.06 kg)", "225");
        weight_categories.add("226 lbs (102.51 kg)");
        wt_map.put("226 lbs (102.51 kg)", "226");
        weight_categories.add("227 lbs (102.97 kg)");
        wt_map.put("227 lbs (102.97 kg)", "227");
        weight_categories.add("228 lbs (103.42 kg)");
        wt_map.put("228 lbs (103.42 kg)", "228");
        weight_categories.add("229 lbs (103.87 kg)");
        wt_map.put("229 lbs (103.87 kg)", "229");
        weight_categories.add("230 lbs (104.33 kg)");
        wt_map.put("230 lbs (104.33 kg)", "230");
        weight_categories.add("231 lbs (104.78 kg)");
        wt_map.put("231 lbs (104.78 kg)", "231");
        weight_categories.add("232 lbs (105.23 kg)");
        wt_map.put("232 lbs (105.23 kg)", "232");
        weight_categories.add("233 lbs (105.69 kg)");
        wt_map.put("233 lbs (105.69 kg)", "233");
        weight_categories.add("234 lbs (106.14 kg)");
        wt_map.put("234 lbs (106.14 kg)", "234");
        weight_categories.add("235 lbs (106.59 kg)");
        wt_map.put("235 lbs (106.59 kg)", "235");
        weight_categories.add("236 lbs (107.05 kg)");
        wt_map.put("236 lbs (107.05 kg)", "236");
        weight_categories.add("237 lbs (107.5 kg)");
        wt_map.put("237 lbs (107.5 kg)", "237");
        weight_categories.add("238 lbs (107.95 kg)");
        wt_map.put("238 lbs (107.95 kg)", "238");
        weight_categories.add("239 lbs (108.41 kg)");
        wt_map.put("239 lbs (108.41 kg)", "239");
        weight_categories.add("240 lbs (108.86 kg)");
        wt_map.put("240 lbs (108.86 kg)", "240");
        weight_categories.add("241 lbs (109.32 kg)");
        wt_map.put("241 lbs (109.32 kg)", "241");
        weight_categories.add("242 lbs (109.77 kg)");
        wt_map.put("242 lbs (109.77 kg)", "242");
        weight_categories.add("243 lbs (110.22 kg)");
        wt_map.put("243 lbs (110.22 kg)", "243");
        weight_categories.add("244 lbs (110.68 kg)");
        wt_map.put("244 lbs (110.68 kg)", "244");
        weight_categories.add("245 lbs (111.13 kg)");
        wt_map.put("245 lbs (111.13 kg)", "245");
        weight_categories.add("246 lbs (111.58 kg)");
        wt_map.put("246 lbs (111.58 kg)", "246");
        weight_categories.add("247 lbs (112.04 kg)");
        wt_map.put("247 lbs (112.04 kg)", "247");
        weight_categories.add("248 lbs (112.49 kg)");
        wt_map.put("248 lbs (112.49 kg)", "248");
        weight_categories.add("249 lbs (112.94 kg)");
        wt_map.put("249 lbs (112.94 kg)", "249");
        weight_categories.add("250 lbs (113.4 kg)");
        wt_map.put("250 lbs (113.4 kg)", "250");
        weight_categories.add("251 lbs (113.85 kg)");
        wt_map.put("251 lbs (113.85 kg)", "251");
        weight_categories.add("252 lbs (114.31 kg)");
        wt_map.put("252 lbs (114.31 kg)", "252");
        weight_categories.add("253 lbs (114.76 kg)");
        wt_map.put("253 lbs (114.76 kg)", "253");
        weight_categories.add("254 lbs (115.21 kg)");
        wt_map.put("254 lbs (115.21 kg)", "254");
        weight_categories.add("255 lbs (115.67 kg)");
        wt_map.put("255 lbs (115.67 kg)", "255");
        weight_categories.add("256 lbs (116.12 kg)");
        wt_map.put("256 lbs (116.12 kg)", "256");
        weight_categories.add("257 lbs (116.57 kg)");
        wt_map.put("257 lbs (116.57 kg)", "257");
        weight_categories.add("258 lbs (117.03 kg)");
        wt_map.put("258 lbs (117.03 kg)", "258");
        weight_categories.add("259 lbs (117.48 kg)");
        wt_map.put("259 lbs (117.48 kg)", "259");
        weight_categories.add("260 lbs (117.93 kg)");
        wt_map.put("260 lbs (117.93 kg)", "260");
        weight_categories.add("261 lbs (118.39 kg)");
        wt_map.put("261 lbs (118.39 kg)", "261");
        weight_categories.add("262 lbs (118.84 kg)");
        wt_map.put("262 lbs (118.84 kg)", "262");
        weight_categories.add("263 lbs (119.29 kg)");
        wt_map.put("263 lbs (119.29 kg)", "263");
        weight_categories.add("264 lbs (119.75 kg)");
        wt_map.put("264 lbs (119.75 kg)", "264");
        weight_categories.add("265 lbs (120.2 kg)");
        wt_map.put("265 lbs (120.2 kg)", "265");
        weight_categories.add("266 lbs (120.66 kg)");
        wt_map.put("266 lbs (120.66 kg)", "266");
        weight_categories.add("267 lbs (121.11 kg)");
        wt_map.put("267 lbs (121.11 kg)", "267");
        weight_categories.add("268 lbs (121.56 kg)");
        wt_map.put("268 lbs (121.56 kg)", "268");
        weight_categories.add("269 lbs (122.02 kg)");
        wt_map.put("269 lbs (122.02 kg)", "269");
        weight_categories.add("270 lbs (122.47 kg)");
        wt_map.put("270 lbs (122.47 kg)", "270");
        weight_categories.add("271 lbs (122.92 kg)");
        wt_map.put("271 lbs (122.92 kg)", "271");
        weight_categories.add("272 lbs (123.38 kg)");
        wt_map.put("272 lbs (123.38 kg)", "272");
        weight_categories.add("273 lbs (123.83 kg)");
        wt_map.put("273 lbs (123.83 kg)", "273");
        weight_categories.add("274 lbs (124.28 kg)");
        wt_map.put("274 lbs (124.28 kg)", "274");
        weight_categories.add("275 lbs (124.74 kg)");
        wt_map.put("275 lbs (124.74 kg)", "275");
        weight_categories.add("276 lbs (125.19 kg)");
        wt_map.put("276 lbs (125.19 kg)", "276");
        weight_categories.add("277 lbs (125.65 kg)");
        wt_map.put("277 lbs (125.65 kg)", "277");
        weight_categories.add("278 lbs (126.1 kg)");
        wt_map.put("278 lbs (126.1 kg)", "278");
        weight_categories.add("279 lbs (126.55 kg)");
        wt_map.put("279 lbs (126.55 kg)", "279");
        weight_categories.add("280 lbs (127.01 kg)");
        wt_map.put("280 lbs (127.01 kg)", "280");
        weight_categories.add("281 lbs (127.46 kg)");
        wt_map.put("281 lbs (127.46 kg)", "281");
        weight_categories.add("282 lbs (127.91 kg)");
        wt_map.put("282 lbs (127.91 kg)", "282");
        weight_categories.add("283 lbs (128.37 kg)");
        wt_map.put("283 lbs (128.37 kg)", "283");
        weight_categories.add("284 lbs (128.82 kg)");
        wt_map.put("284 lbs (128.82 kg)", "284");
        weight_categories.add("285 lbs (129.27 kg)");
        wt_map.put("285 lbs (129.27 kg)", "285");
        weight_categories.add("286 lbs (129.73 kg)");
        wt_map.put("286 lbs (129.73 kg)", "286");
        weight_categories.add("287 lbs (130.18 kg)");
        wt_map.put("287 lbs (130.18 kg)", "287");
        weight_categories.add("288 lbs (130.63 kg)");
        wt_map.put("288 lbs (130.63 kg)", "288");
        weight_categories.add("289 lbs (131.09 kg)");
        wt_map.put("289 lbs (131.09 kg)", "289");
        weight_categories.add("290 lbs (131.54 kg)");
        wt_map.put("290 lbs (131.54 kg)", "290");
        weight_categories.add("291 lbs (132 kg)");
        wt_map.put("291 lbs (132 kg)", "291");
        weight_categories.add("292 lbs (132.45 kg)");
        wt_map.put("292 lbs (132.45 kg)", "292");
        weight_categories.add("293 lbs (132.9 kg)");
        wt_map.put("293 lbs (132.9 kg)", "293");
        weight_categories.add("294 lbs (133.36 kg)");
        wt_map.put("294 lbs (133.36 kg)", "294");
        weight_categories.add("295 lbs (133.81 kg)");
        wt_map.put("295 lbs (133.81 kg)", "295");
        weight_categories.add("296 lbs (134.26 kg)");
        wt_map.put("296 lbs (134.26 kg)", "296");
        weight_categories.add("297 lbs (134.72 kg)");
        wt_map.put("297 lbs (134.72 kg)", "297");
        weight_categories.add("298 lbs (135.17 kg)");
        wt_map.put("298 lbs (135.17 kg)", "298");
        weight_categories.add("299 lbs (135.62 kg)");
        wt_map.put("299 lbs (135.62 kg)", "299");
        weight_categories.add("300 lbs (136.08 kg)");
        wt_map.put("300 lbs (136.08 kg)", "300");
        weight_categories.add("301 lbs (136.53 kg)");
        wt_map.put("301 lbs (136.53 kg)", "301");
        weight_categories.add("302 lbs (136.98 kg)");
        wt_map.put("302 lbs (136.98 kg)", "302");
        weight_categories.add("303 lbs (137.44 kg)");
        wt_map.put("303 lbs (137.44 kg)", "303");
        weight_categories.add("304 lbs (137.89 kg)");
        wt_map.put("304 lbs (137.89 kg)", "304");
        weight_categories.add("305 lbs (138.35 kg)");
        wt_map.put("305 lbs (138.35 kg)", "305");
        weight_categories.add("306 lbs (138.8 kg)");
        wt_map.put("306 lbs (138.8 kg)", "306");
        weight_categories.add("307 lbs (139.25 kg)");
        wt_map.put("307 lbs (139.25 kg)", "307");
        weight_categories.add("308 lbs (139.71 kg)");
        wt_map.put("308 lbs (139.71 kg)", "308");
        weight_categories.add("309 lbs (140.16 kg)");
        wt_map.put("309 lbs (140.16 kg)", "309");
        weight_categories.add("310 lbs (140.61 kg)");
        wt_map.put("310 lbs (140.61 kg)", "310");
        weight_categories.add("311 lbs (141.07 kg)");
        wt_map.put("311 lbs (141.07 kg)", "311");
        weight_categories.add("312 lbs (141.52 kg)");
        wt_map.put("312 lbs (141.52 kg)", "312");
        weight_categories.add("313 lbs (141.97 kg)");
        wt_map.put("313 lbs (141.97 kg)", "313");
        weight_categories.add("314 lbs (142.43 kg)");
        wt_map.put("314 lbs (142.43 kg)", "314");
        weight_categories.add("315 lbs (142.88 kg)");
        wt_map.put("315 lbs (142.88 kg)", "315");
        weight_categories.add("316 lbs (143.34 kg)");
        wt_map.put("316 lbs (143.34 kg)", "316");
        weight_categories.add("317 lbs (143.79 kg)");
        wt_map.put("317 lbs (143.79 kg)", "317");
        weight_categories.add("318 lbs (144.24 kg)");
        wt_map.put("318 lbs (144.24 kg)", "318");
        weight_categories.add("319 lbs (144.7 kg)");
        wt_map.put("319 lbs (144.7 kg)", "319");
        weight_categories.add("320 lbs (145.15 kg)");
        wt_map.put("320 lbs (145.15 kg)", "320");
        weight_categories.add("321 lbs (145.6 kg)");
        wt_map.put("321 lbs (145.6 kg)", "321");
        weight_categories.add("322 lbs (146.06 kg)");
        wt_map.put("322 lbs (146.06 kg)", "322");
        weight_categories.add("323 lbs (146.51 kg)");
        wt_map.put("323 lbs (146.51 kg)", "323");
        weight_categories.add("324 lbs (146.96 kg)");
        wt_map.put("324 lbs (146.96 kg)", "324");
        weight_categories.add("325 lbs (147.42 kg)");
        wt_map.put("325 lbs (147.42 kg)", "325");
        weight_categories.add("326 lbs (147.87 kg)");
        wt_map.put("326 lbs (147.87 kg)", "326");
        weight_categories.add("327 lbs (148.32 kg)");
        wt_map.put("327 lbs (148.32 kg)", "327");
        weight_categories.add("328 lbs (148.78 kg)");
        wt_map.put("328 lbs (148.78 kg)", "328");
        weight_categories.add("329 lbs (149.23 kg)");
        wt_map.put("329 lbs (149.23 kg)", "329");
        weight_categories.add("330 lbs (149.69 kg)");
        wt_map.put("330 lbs (149.69 kg)", "330");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(weight_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = weight_categories.getItem(which);
                String select_value = (wt_map).get(weight_categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                weight_val = select_value;
                wt_name = select_text;

                weight_title.setText(wt_name);
            }
        });
        builderSingle.show();
    }


    public void apply_name_title() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(FamilyProfileListActivity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select title");

        final ArrayAdapter<String> categories = new ArrayAdapter<String>(FamilyProfileListActivity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        categories.add("Mr.");
        tit_map.put("Mr.", "1");

        categories.add("Miss.");
        tit_map.put("Miss.", "2");

        categories.add("Mrs.");
        tit_map.put("Mrs.", "3");

        categories.add("Baby");
        tit_map.put("Baby", "5");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = categories.getItem(which);
                String select_value = (tit_map).get(categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                tit_val = select_value;
                tit_name = select_text;

                tv_name_title.setText(tit_name);
            }
        });

        builderSingle.show();
    }


    public void apply_relationship() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(FamilyProfileListActivity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select relationship");

        final ArrayAdapter<String> categories = new ArrayAdapter<String>(FamilyProfileListActivity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        categories.add("Myself");
        gen_map.put("Myself", "1");

        categories.add("Father");
        gen_map.put("Father", "2");

        categories.add("Mother");
        gen_map.put("Mother", "3");

        categories.add("Brother");
        gen_map.put("Brother", "4");

        categories.add("Sister");
        gen_map.put("Sister", "5");

        categories.add("Husband");
        gen_map.put("Husband", "6");

        categories.add("Wife");
        gen_map.put("Wife", "7");

        categories.add("Son");
        gen_map.put("Son", "8");

        categories.add("Daughter");
        gen_map.put("Daughter", "9");

        categories.add("Cousin");
        gen_map.put("Cousin", "10");

        categories.add("Grand Father");
        gen_map.put("Grand Father", "11");

        categories.add("Grand Mother");
        gen_map.put("Grand Mother", "12");

        categories.add("Friend");
        gen_map.put("Friend", "13");

        categories.add("Others");
        gen_map.put("Others", "14");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = categories.getItem(which);
                String select_value = (gen_map).get(categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                rel_val = select_value;
                rel_name = select_text;

                relation_title.setText(rel_name);
            }
        });
        builderSingle.show();
    }

    private class JSON_NewFamily extends AsyncTask<JSONObject, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(FamilyProfileListActivity.this);
            dialog.setMessage("Submitting..., please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                family_list = jParser.JSON_String_POST(urls[0], "newFamily");

                System.out.println("newFamily URL---------------" + urls[0]);


                return family_list;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            try {

                System.out.println("family_list----------" + family_list);

                full_process();

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onResult(Object result) {
        Log.d("k9res", "onResult: " + result.toString());
        if (result.toString().equalsIgnoreCase("swiped_down")) {
            //do something or nothing
        } else {
            //handle result
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
