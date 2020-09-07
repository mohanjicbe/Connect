package com.orane.icliniq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.adapter.HotlineDoctorsRowAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class HotlineDoctorsActivity extends AppCompatActivity {

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
    public static final String first_hotline = "first_hotline_key";

    ArrayAdapter<String> dataAdapter = null;
    Item objItem;
    public List<Item> listArray;
    ImageView img_menu, side_ribbon;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    LinearLayout nolayout, netcheck_layout, full_layout, spec_layout;
    RelativeLayout plan_layout;
    RelativeLayout LinearLayout1;
    String params;
    ProgressBar progressBar_bottom, progressBar;
    List<Item> arrayOfList;
    HotlineDoctorsRowAdapter objAdapter;
    long startTime;
    TextView plan_amt, tvid, tv_docurl;
    Button btn_viewprofile, btn_hotlineplans;
    public String str_response, docid, docurl, first_hotline_val, url, sub_url, doc_id, doc_url, doc_name;
    RelativeLayout filter_layout;
    FloatingActionButton fab;
    ImageView leftback;
    TextView spec_text, tv_showall_text;
    public boolean pagination = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotline_doctors);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.query_launch = "";

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        first_hotline_val = sharedpreferences.getString(first_hotline, "yes");
        //================ Shared Pref ======================

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("All Hotline Doctors");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listview);
        LinearLayout1 = (RelativeLayout) findViewById(R.id.LinearLayout1);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        listView = (ListView) findViewById(R.id.listview);
        filter_layout = (RelativeLayout) findViewById(R.id.filter_layout);
        spec_text = (TextView) findViewById(R.id.spec_text);
        tv_showall_text = (TextView) findViewById(R.id.tv_showall_text);
        leftback = (ImageView) findViewById(R.id.leftback);
        spec_layout = (LinearLayout) findViewById(R.id.spec_layout);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name);

        spec_text.setTypeface(font_reg);
        tv_showall_text.setTypeface(font_bold);

        FlurryAgent.onPageView();

        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Hotline_Doctors_List");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("android.patient.User_id:", (Model.id));
        Model.kiss.set(properties);
        //----------------- Kissmetrics ----------------------------------

        System.out.println("Page Model.id --------------- " + Model.id);
        System.out.println("first_hotline_val --------------- " + first_hotline_val);

        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
            full_process();
        } else {
            force_logout();
        }

        filter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);

                    side_ribbon = (ImageView) view.findViewById(R.id.side_ribbon);
                    full_layout = (LinearLayout) view.findViewById(R.id.full_layout);
                    plan_layout = (RelativeLayout) view.findViewById(R.id.plan_layout);
                    TextView plan_text = (TextView) view.findViewById(R.id.plan_text);
                    TextView tvid = (TextView) view.findViewById(R.id.tvid);
                    TextView tv_docurl = (TextView) view.findViewById(R.id.tv_docurl);
                    TextView tvdocname = (TextView) view.findViewById(R.id.tvdocname);
                    TextView tvedu = (TextView) view.findViewById(R.id.tvedu);
                    TextView tvspec = (TextView) view.findViewById(R.id.tvspec);
                    CircleImageView imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);
                    btn_viewprofile = (Button) view.findViewById(R.id.btn_viewprofile);
                    btn_hotlineplans = (Button) view.findViewById(R.id.btn_hotlineplans);

                    doc_id = tvid.getText().toString();
                    doc_url = tv_docurl.getText().toString();
                    doc_name = tvdocname.getText().toString();

                    System.out.println("doc_id-----" + doc_id);
                    System.out.println("Side Ribbon-----" + side_ribbon.getVisibility());


                    if (side_ribbon.getVisibility() == View.VISIBLE) {

                        Intent intent = new Intent(HotlineDoctorsActivity.this, HotlineDoctorsQueriesActivity.class);
                        intent.putExtra("Doctor_id", doc_id);
                        intent.putExtra("Doctor_name", doc_name);
                        intent.putExtra("tv_docurl", doc_url);
                        startActivity(intent);

                    }

                    btn_hotlineplans.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(HotlineDoctorsActivity.this, HotlinePackagesActivity.class);
                            intent.putExtra("Doctor_id", doc_id);
                            intent.putExtra("Doctor_name", doc_name);
                            intent.putExtra("tv_docurl", doc_url);
                            startActivity(intent);

                            View parent = (View) v.getParent();

                            Button btn = (Button) parent.findViewById(R.id.btn_hotlineplans);
                            System.out.println("btn----------" + btn.getText().toString());
                        }
                    });

                    btn_viewprofile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(HotlineDoctorsActivity.this, ParallexMainActivity.class);
                            intent.putExtra("tv_doc_id", doc_id);
                            startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Double floor_val;
                Integer int_floor = 0;

                int threshold = 1;
                int count = listView.getCount();
                System.out.println("count----- " + count);

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold) {

                        double cur_page = (listView.getAdapter().getCount()) / 10;
                        System.out.println("cur_page 1----" + cur_page);

                        if (count < 10) {
                            System.out.println("No more to Load");
                            int_floor = 0;
                        } else if (count == 10) {
                            floor_val = cur_page + 1;

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();

                        } else {
                            floor_val = Math.floor(cur_page);
                            Double diff = cur_page - floor_val;

                            if (diff == 0) {
                                floor_val = floor_val + 1;
                            } else if (diff > 0) {
                                floor_val = floor_val + 2;
                            }

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();

                        }
                        if (int_floor != 0 && (pagination)) {
                            //-------------------------------------------------
                            String url = Model.BASE_URL + "sapp/hotlineDoc?user_id=" + (Model.id) + "&page=" + int_floor + "&speciality=" + Model.select_spec_val + "&token=" + Model.token;
                            System.out.println("url---" + url);
                            new MyTask_Pagination().execute(url);
                            //-------------------------------------------------
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HotlineDoctorsActivity.this, SpecialityListActivity.class);
                startActivity(intent);
            }
        });

        tv_showall_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spec_text.setText("All Specialities");
                Model.select_spec_val = "0";
                tv_showall_text.setVisibility(View.GONE);

                String url = Model.BASE_URL + "sapp/hotlineDoc?user_id=" + (Model.id) + "&page=1&speciality=" + Model.select_spec_val + "&token=" + Model.token;
                System.out.println("ShowAll_url----" + url);
                new MyTask_server().execute(url);
            }
        });

        leftback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spec_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotlineDoctorsActivity.this, SpecialityListActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new NetCheck().netcheck(HotlineDoctorsActivity.this)) {

                    pagination = true;

                    String url = Model.BASE_URL + "sapp/hotlineDoc?user_id=" + (Model.id) + "&page=1&speciality=" + Model.select_spec_val + "&token=" + Model.token;
                    System.out.println("Refresh url----------" + url);
                    new MyTask_refresh().execute(url);

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
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Model.query_launch).equals("SpecialityListActivity")) {

            Model.query_launch = "";
            spec_text.setText(Model.select_specname);
            tv_showall_text.setVisibility(View.VISIBLE);

            String url = Model.BASE_URL + "sapp/hotlineDoc?user_id=" + (Model.id) + "&page=1&speciality=" + Model.select_spec_val + "&token=" + Model.token;
            System.out.println("Resume_url----" + url);

            new MyTask_server().execute(url);
        }
    }

    public void full_process() {
        try {
            if (new NetCheck().netcheck(HotlineDoctorsActivity.this)) {
                Model.select_spec_val = "0";
                tv_showall_text.setVisibility(View.GONE);

                String url = Model.BASE_URL + "sapp/hotlineDoc?user_id=" + (Model.id) + "&page=1&speciality=" + Model.select_spec_val + "&token=" + Model.token;
                System.out.println("Hotline Doctor url--------" + url);
                new MyTask_server().execute(url);

            } else {
                mSwipeRefreshLayout.setVisibility(View.GONE);
                nolayout.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.VISIBLE);
                progressBar_bottom.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                Toast.makeText(HotlineDoctorsActivity.this, "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyTask_server extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);

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
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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

                            System.out.println("jsonobj1 Object------------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setHlid(jsonobj1.getString("id"));
                            objItem.setHlname(jsonobj1.getString("name"));
                            objItem.setHlphoto_url(jsonobj1.getString("photo_url"));
                            objItem.setHledu(jsonobj1.getString("edu"));
                            objItem.setHlsp(jsonobj1.getString("sp"));
                            objItem.setHlfee3(jsonobj1.getString("fee3"));
                            objItem.setHlis_subscribed(jsonobj1.getString("is_subscribed"));
                            objItem.setCurrlable(jsonobj1.getString("currency_label"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            nolayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                        } else {
                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

                            setAdapterToListview();
                        }
                    }
                }


            } catch (Exception e) {
                System.out.println("Exception post1----" + e.toString());
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
            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);

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
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    listArray = new ArrayList<Item>();
                    JSONArray jsonarr = new JSONArray(str_response);


                    if (str_response.length() > 2) {

                        listArray = new ArrayList<Item>();

                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            System.out.println("jsonobj1 Object------------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setHlid(jsonobj1.getString("id"));
                            objItem.setHlname(jsonobj1.getString("name"));
                            objItem.setHlphoto_url(jsonobj1.getString("photo_url"));
                            objItem.setHledu(jsonobj1.getString("edu"));
                            objItem.setHlsp(jsonobj1.getString("sp"));
                            objItem.setHlfee3(jsonobj1.getString("fee3"));
                            objItem.setHlis_subscribed(jsonobj1.getString("is_subscribed"));
                            objItem.setCurrlable(jsonobj1.getString("currency_label"));


                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                        } else {
                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

                            add_page_AdapterToListview();
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class MyTask_refresh extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);

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
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    listArray = new ArrayList<Item>();
                    JSONArray jsonarr = new JSONArray(str_response);


                    if (str_response.length() > 2) {
                        listArray = new ArrayList<Item>();
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            System.out.println("jsonobj1 Object------------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setHlid(jsonobj1.getString("id"));
                            objItem.setHlname(jsonobj1.getString("name"));
                            objItem.setHlphoto_url(jsonobj1.getString("photo_url"));
                            objItem.setHledu(jsonobj1.getString("edu"));
                            objItem.setHlsp(jsonobj1.getString("sp"));
                            objItem.setHlfee3(jsonobj1.getString("fee3"));
                            objItem.setHlis_subscribed(jsonobj1.getString("is_subscribed"));
                            objItem.setCurrlable(jsonobj1.getString("currency_label"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            nolayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                        } else {
                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

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

        objAdapter = new HotlineDoctorsRowAdapter(HotlineDoctorsActivity.this, R.layout.hotline_doctors_row, arrayOfList);
        listView.setAdapter(objAdapter);
    }

    public void add_page_AdapterToListview() {

        objAdapter.addAll(arrayOfList);
        listView.setSelection(objAdapter.getCount() - (arrayOfList.size()));
        objAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hotlinedoctors_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_showall) {

            //----------------------------------------------
            String url = Model.BASE_URL + "sapp/hotlineDoc?user_id=" + (Model.id) + "&page=1&speciality=" + Model.select_spec_val + "&token=" + Model.token;
            System.out.println("url---" + url);
            new MyTask_server().execute(url);
            //--------------------------------------

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void force_logout() {

        final MaterialDialog alert = new MaterialDialog(HotlineDoctorsActivity.this);
        alert.setTitle("Please Re-Login the App..!");
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
                Intent i = new Intent(HotlineDoctorsActivity.this, LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });
        alert.show();
    }

    public void onClick(View v) {

        try {

            switch (v.getId()) {

                case R.id.btn_viewprofile:
                    View parent = (View) v.getParent();
                    //View grand_parent = (View)parent.getParent();

                    tvid = (TextView) parent.findViewById(R.id.tvid);
                    tv_docurl = (TextView) parent.findViewById(R.id.tv_docurl);

                    docid = tvid.getText().toString();
                    docurl = tv_docurl.getText().toString();

                    System.out.println("docid----" + docid);
                    System.out.println("docurl----" + docurl);

                    Intent intent = new Intent(HotlineDoctorsActivity.this, ParallexMainActivity.class);
                    intent.putExtra("tv_doc_id", docid);
                    startActivity(intent);

                    break;

                case R.id.btn_hotlineplans:

                    View parent2 = (View) v.getParent();
                    //View grand_parent = (View)parent.getParent();

                    tvid = (TextView) parent2.findViewById(R.id.tvid);
                    tv_docurl = (TextView) parent2.findViewById(R.id.tv_docurl);

                    docid = tvid.getText().toString();
                    docurl = tv_docurl.getText().toString();

                    System.out.println("docid----" + docid);
                    System.out.println("docurl----" + docurl);

                    Intent i = new Intent(HotlineDoctorsActivity.this, HotlinePackagesActivity.class);
                    i.putExtra("Doctor_id", docid);
                    i.putExtra("Doctor_name", "");
                    i.putExtra("tv_docurl", docurl);
                    startActivity(i);

                    break;
            }

            System.out.println("onClick-------");

        } catch (Exception e) {
            System.out.println("Exception-------" + e.toString());
            e.printStackTrace();
        }
    }


}