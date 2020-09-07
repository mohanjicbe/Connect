package com.orane.icliniq.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.AskQuery1;
import com.orane.icliniq.DoctorProfileActivity;
import com.orane.icliniq.HotlinePackagesActivity;
import com.orane.icliniq.IntroScreenActivity;
import com.orane.icliniq.Invite_doctors;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.R;
import com.orane.icliniq.SpecialityListActivity;
import com.orane.icliniq.adapter.MyDoctorsRowAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDoctorsActivity extends BaseActivity implements ObservableScrollViewCallbacks, AAH_FabulousFragment.Callbacks {

    InviteDoctorFragment dialogFrag;
    public Item objItem;
    List<Item> listArray;
    ArrayAdapter<String> dataAdapter = null;
    public List<Item> arrayOfList;

    public File imageFile;
    ImageView leftback;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ObservableListView listView;
    LinearLayout spec_layout, nolayout, netcheck_layout;
    RelativeLayout LinearLayout1;
    public String str_response, doct_name, params, url_txt;
    MyDoctorsRowAdapter objAdapter;
    ProgressBar progressBar_bottom, progressBar;
    long startTime;
    TextView tvid, tv_showall_text, spec_text;
    Button btn_adddoc;
    FloatingActionButton fab;
    public boolean pagination = true;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydoctors);

        FlurryAgent.onPageView();

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        //============================================================

        FlurryAgent.onPageView();

        //------------ Google firebase Analitics--------------------
        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("user_id", (Model.id));
        Model.mFirebaseAnalytics.logEvent("MyDoctorsList", params);
        //------------ Google firebase Analitics--------------------

        //----------------- Toolbar Initialization----------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //----------------- Toolbar Initialization----------------------------------

        fab = (FloatingActionButton) findViewById(R.id.fab);
        btn_adddoc = (Button) findViewById(R.id.btn_adddoc);
        leftback = (ImageView) findViewById(R.id.leftback);
        spec_layout = (LinearLayout) findViewById(R.id.spec_layout);
        tv_showall_text = (TextView) findViewById(R.id.tv_showall_text);
        spec_text = (TextView) findViewById(R.id.spec_text);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        LinearLayout1 = (RelativeLayout) findViewById(R.id.LinearLayout1);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        listView = (ObservableListView) findViewById(R.id.listview);

        full_process();

        tv_showall_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spec_text.setText("All Specialities");
                tv_showall_text.setVisibility(View.GONE);
                Model.select_spec_val = "0";

                //---------------------------------------------
                /*String Resume_url = "?user_id=" + (Model.id) + "&page=1&speciality=" + (Model.select_spec_val);
                System.out.println("ShowAll_url----" + Resume_url);*/
                String mydoc_url = Model.BASE_URL + "sapp/myDoc?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + Model.token;
                System.out.println("params---------------" + mydoc_url);
                new MyTask_server().execute(mydoc_url);
                //---------------------------------------------

            }
        });

        leftback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

/*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDoctorsActivity.this, Invite_doctors.class);
                startActivity(intent);
                //finish();
            }
        });
*/


        dialogFrag = InviteDoctorFragment.newInstance();
        dialogFrag.setParentFab(fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });


        btn_adddoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDoctorsActivity.this, Invite_doctors.class);
                startActivity(intent);
                //finish();
            }
        });

        spec_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDoctorsActivity.this, SpecialityListActivity.class);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("position-----" + position);

                Model.upload_files = "";
                Model.compmore = "";
                Model.prevhist = "";
                Model.curmedi = "";
                Model.pastmedi = "";
                Model.labtest = "";

                TextView tvid = (TextView) view.findViewById(R.id.tvid);
                TextView tvcfee = (TextView) view.findViewById(R.id.tvcfee);
                TextView tvqfee = (TextView) view.findViewById(R.id.tvqfee);
                TextView tvdocname = (TextView) view.findViewById(R.id.tvdocname);
                TextView tvedu = (TextView) view.findViewById(R.id.tvedu);
                TextView tvspec = (TextView) view.findViewById(R.id.tvspec);
                CircleImageView imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);

                Intent intent = new Intent(MyDoctorsActivity.this, ParallexMainActivity.class);
                intent.putExtra("tv_doc_id", tvid.getText().toString());
                //---------- Image Send -----------------------

                imageview_poster.buildDrawingCache();
                Bitmap image = imageview_poster.getDrawingCache();
                Bundle extras = new Bundle();

                extras.putParcelable("imagebitmap", image);
                System.out.println("image---" + image.toString());

                Model.doctor_id = tvid.getText().toString();
                extras.putString("Doc_id", tvid.getText().toString());
                extras.putString("Doc_name", tvdocname.getText().toString());
                extras.putString("Doc_edu", tvedu.getText().toString());
                extras.putString("Doc_spec", tvspec.getText().toString());
                extras.putString("cfee", tvcfee.getText().toString());
                extras.putString("qfee", tvqfee.getText().toString());
                intent.putExtras(extras);
                //---------- Image Send -----------------------

                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Double floor_val = 0.0;
                Integer int_floor = 0;

                int threshold = 1;
                int count = listView.getCount();
                System.out.println("count----- " + count);

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold) {

                        double cur_page = (listView.getAdapter().getCount()) / 10;
                        System.out.println("cur_page 1----" + cur_page);

                        if (count < 10) {
                            System.out.println("No more doctors to load");
                            //Toast.makeText(getApplicationContext(), "No more to queries load", Toast.LENGTH_LONG).show();
                            int_floor = 0;
                        } else if (count == 10) {
                            floor_val = cur_page + 1;

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();

                        } else {
                            floor_val = Math.floor(cur_page);
                            Double diff = cur_page - floor_val;

                            System.out.println("cur_page 2----" + cur_page);
                            System.out.println("floor_val 2----" + floor_val);
                            System.out.println("diff 2----" + diff);

                            if (diff == 0) {
                                floor_val = floor_val + 1;
                            } else if (diff > 0) {
                                floor_val = floor_val + 2;
                            }

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();

                        }


                        if (int_floor != 0 && (pagination)) {
                            String url = Model.BASE_URL + "sapp/myDoc?user_id=" + (Model.id) + "&page=" + int_floor + "&sp_id=0&token=" + Model.token;
                            System.out.println("params---------------" + url);

                            new MyTask_Pagination().execute(url);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new NetCheck().netcheck(MyDoctorsActivity.this)) {
                    pagination = true;

                    //------------------------------------------------------
                    String url = Model.BASE_URL + "sapp/myDoc?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + Model.token;
                    System.out.println("url---------------" + url);
                    new MyTask_refresh().execute(url);
                    //------------------------------------------------------

                    mSwipeRefreshLayout.setRefreshing(false);

                } else {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        listView.setScrollViewCallbacks(this);
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
            fab.hide();

        } else if (scrollState == ScrollState.DOWN) {
            System.out.println("scrollDir-----Down---" + scrollState);
            fab.show();
        }
    }

    public void full_process() {

        if (new NetCheck().netcheck(MyDoctorsActivity.this)) {

            try {
                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                    //-------------------------------------
                    String url = Model.BASE_URL + "sapp/myDoc?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + Model.token;
                    System.out.println("params---------------" + url);
                    new MyTask_server().execute(url);
                    //-------------------------------------

                } else {
                    Toast.makeText(getApplicationContext(), "Please Logout and Login Again to proceed.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(MyDoctorsActivity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }


    class MyTask_server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                mSwipeRefreshLayout.setVisibility(View.GONE);
                nolayout.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.GONE);
                progressBar_bottom.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    System.out.println("str_response-------------" + str_response);

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
                            Intent intent = new Intent(MyDoctorsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    System.out.println("jsonarr.length()---------------" + jsonarr.length());

                    if (str_response.length() > 2) {

                        listArray = new ArrayList<Item>();
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            System.out.println("jsonobj1-----------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setDocid(jsonobj1.getString("id"));
                            objItem.setDocname(jsonobj1.getString("name"));
                            objItem.setDocedu(jsonobj1.getString("edu"));
                            objItem.setDocspec(jsonobj1.getString("speciality"));
                            objItem.setDocimage(jsonobj1.getString("photo_url"));
                            objItem.setCfee(jsonobj1.getString("cfee"));
                            objItem.setQfee(jsonobj1.getString("qfee"));

                            listArray.add(objItem);

                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            nolayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            fab.setVisibility(View.GONE);

                        } else {

                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                            setAdapterToListview();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MyTask_Pagination extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

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
                    System.out.println("str_response-------------" + str_response);

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
                            Intent intent = new Intent(MyDoctorsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);

                    if (str_response.length() > 2) {
                        listArray = new ArrayList<Item>();
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            System.out.println("jsonobj1-----------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setDocid(jsonobj1.getString("id"));
                            objItem.setDocname(jsonobj1.getString("name"));
                            objItem.setDocedu(jsonobj1.getString("edu"));
                            objItem.setDocspec(jsonobj1.getString("speciality"));
                            objItem.setDocimage(jsonobj1.getString("photo_url"));
                            objItem.setCfee(jsonobj1.getString("cfee"));
                            objItem.setQfee(jsonobj1.getString("qfee"));

                            listArray.add(objItem);

                        }
                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            //Toast.makeText(getApplicationContext(), "No more doctors to load", Toast.LENGTH_LONG).show();

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);


                        } else {

                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                            add_page_AdapterToListview();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class MyTask_refresh extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
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
                    System.out.println("str_response-------------" + str_response);

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
                            Intent intent = new Intent(MyDoctorsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);

                    if (str_response.length() > 2) {
                        listArray = new ArrayList<Item>();
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            System.out.println("jsonobj1-----------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setDocid(jsonobj1.getString("id"));
                            objItem.setDocname(jsonobj1.getString("name"));
                            objItem.setDocedu(jsonobj1.getString("edu"));
                            objItem.setDocspec(jsonobj1.getString("speciality"));
                            objItem.setDocimage(jsonobj1.getString("photo_url"));
                            objItem.setCfee(jsonobj1.getString("cfee"));
                            objItem.setQfee(jsonobj1.getString("qfee"));

                            listArray.add(objItem);

                        }

                        arrayOfList = listArray;
                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            nolayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                        } else {

                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                            setAdapterToListview();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapterToListview() {

        try {
            objAdapter = new MyDoctorsRowAdapter(MyDoctorsActivity.this, R.layout.my_doctors_row, arrayOfList);
            listView.setAdapter(objAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_page_AdapterToListview() {
        try {
            objAdapter.addAll(arrayOfList);
            listView.setSelection(objAdapter.getCount() - (arrayOfList.size()));
            objAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mydoctors_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_refresh) {

            try {

                String url = Model.BASE_URL + "sapp/myDoc?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + Model.token;
                System.out.println("params---------------" + url);
                new MyTask_server().execute(url);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }


      /*  if (id == R.id.nav_newquery) {

            Model.doctor_id = "0";
            Model.qid = "";
            Model.dqid = "";
            Model.upload_files = "";
            Model.compmore = "";
            Model.prevhist = "";
            Model.curmedi = "";
            Model.pastmedi = "";
            Model.labtest = "";
            Model.query_cache = "";

            Intent intent = new Intent(MyDoctorsActivity.this, AskQuery1.class);
            startActivity(intent);
            finish();


            return true;
        }


        if (id == R.id.nav_howworks) {

            Intent intent = new Intent(MyDoctorsActivity.this, IntroScreenActivity.class);
            intent.putExtra("screen_launch", "mydoctors");
            startActivity(intent);

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if ((Model.query_launch).equals("SpecialityListActivity")) {

            Model.query_launch = "";

            spec_text.setText(Model.select_specname);
            tv_showall_text.setVisibility(View.VISIBLE);

            //----------------------------
            String url = Model.BASE_URL + "sapp/myDoc?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + Model.token;
            System.out.println("params---------------" + url);
            new MyTask_server().execute(url);
            //----------------------------
        }
    }


    public void onClick(View v) {

        try {

            switch (v.getId()) {

                case R.id.btn_viewprofile:
                    View parent = (View) v.getParent();
                    //View grand_parent = (View)parent.getParent();

                    tvid = (TextView) parent.findViewById(R.id.tvid);
                    //tv_docurl = (TextView) parent.findViewById(R.id.tv_docurl);
                    String docid = tvid.getText().toString();
                    //docurl = tv_docurl.getText().toString();
                    System.out.println("docid----" + docid);
                    //System.out.println("docurl----" + docurl);
                    Intent intent = new Intent(MyDoctorsActivity.this, DoctorProfileActivity.class);
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

                    Intent i = new Intent(MyDoctorsActivity.this, HotlinePackagesActivity.class);
                    i.putExtra("Doctor_id", docid);
                    i.putExtra("Doctor_name", "");
                    i.putExtra("tv_docurl", "");
                    startActivity(i);

                    break;

                case R.id.img_share:

                    View parent3 = (View) v.getParent();
                    //View grand_parent = (View)parent.getParent();

                    TextView tv_url = (TextView) parent3.findViewById(R.id.tv_url);
                    //TextView tvdocname = (TextView) parent3.findViewById(R.id.tvdocname);


                    url_txt = tv_url.getText().toString();
                    //doct_name = tvdocname.getText().toString();

                    System.out.println("url_txt----" + url_txt);
                    //TakeScreenshot_Share();
                    break;

            }

        } catch (Exception e) {
            System.out.println("Exception-------" + e.toString());
            e.printStackTrace();
        }
    }


    @Override
    public void onResult(Object result) {

        if (result.toString().equalsIgnoreCase("swiped_down")) {
            //do something or nothing
        } else {
            //handle result
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dialogFrag.isAdded()) {
            dialogFrag.dismiss();
            dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
        }

    }
}
