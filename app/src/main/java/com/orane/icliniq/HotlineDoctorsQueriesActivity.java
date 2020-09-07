package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.adapter.MyQueriesHotlineAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HotlineDoctorsQueriesActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    FloatingActionButton fab;
    Item objItem;
    List<Item> listArray;

    ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Button btn_reload, btn_qview, btn_qedit;
    MyQueriesHotlineAdapter objAdapter;
    List<Item> arrayOfList;
    ObservableListView listView;

    TextView empty_msgmsg;
    JSONObject object;
    public String url, Log_Status, str_response, docurl, Doctor_name, query_id, params, Doctor_id;
    Toolbar toolbar;
    LinearLayout nolayout, netcheck_layout;

    public static final String query_lisr_array = "query_lisr_array_key";

    TextView tv_tooltit, tv_tooldesc;
    ProgressBar progressBar_bottom;
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
        setContentView(R.layout.hotline_myquery);


        FlurryAgent.onPageView();

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        //============================================================

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        btn_reload = (Button) findViewById(R.id.btn_reload);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        empty_msgmsg = (TextView) findViewById(R.id.empty_msgmsg);
        listView = (ObservableListView) findViewById(R.id.listview);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        listView.setClickable(true);

        try {
            Intent intent = getIntent();
            Doctor_id = intent.getStringExtra("Doctor_id");
            Doctor_name = intent.getStringExtra("Doctor_name");
            docurl = intent.getStringExtra("tv_docurl");

            if (getSupportActionBar() != null) {

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(Doctor_name);

                tv_tooltit = (TextView) toolbar.findViewById(R.id.tv_tooltit);
                tv_tooldesc = (TextView) toolbar.findViewById(R.id.tv_tooldesc);

                tv_tooltit.setText("Hotline Chat");
                tv_tooldesc.setText("Chat Queries list");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
            }

            System.out.println("selecting_Doctor_id---------" + Doctor_id);

        } catch (Exception e) {
            System.out.println("selecting_Doctor_id Exception---------" + e.toString());
            e.printStackTrace();
        }

        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Hotline_Query_List");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("android.patient.Doctor_id:", Doctor_id);
        properties.put("android.patient.Doctor_name:", Doctor_name);
        Model.kiss.set(properties);
        //----------------- Kissmetrics ----------------------------------

        full_process();


        btn_reload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                full_process();
            }
        });


        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotlineDoctorsQueriesActivity.this, ParallexMainActivity.class);
                intent.putExtra("tv_doc_id", Doctor_id);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("position-----" + position);

                final TextView qidval = (TextView) view.findViewById(R.id.tv_qid);
                TextView tvstatus = (TextView) view.findViewById(R.id.tvstatus);

                String query_status = tvstatus.getText().toString();
                query_id = qidval.getText().toString();
                System.out.println("query_id--------------" + query_id);

/*                Intent i = new Intent(MyQueriesHotlineActivity.this, ChatActivity.class);
                i.putExtra("selqid", query_id);
                startActivity(i);
                finish();*/

                Model.qid = query_id;

                Intent i = new Intent(HotlineDoctorsQueriesActivity.this, HotlineChatViewActivity.class);
                i.putExtra("Doctor_id", Doctor_id);
                i.putExtra("selqid", query_id);
                i.putExtra("docurl", docurl);
                i.putExtra("fcode", "");
                startActivity(i);
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

                        double cur_page = (listView.getAdapter().getCount()) / 5;
                        System.out.println("cur_page 1----" + cur_page);

                        if (count < 5) {
                            System.out.println("No more to Load");
                            //Toast.makeText(getApplicationContext(), "No more queries to load", Toast.LENGTH_LONG).show();
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
                            url = Model.BASE_URL + "sapp/myQueries?user_id=" + (Model.id) + "&doctor_id=" + Doctor_id + "&format=json&page=" + int_floor + "&hline=1&token=" + Model.token;
                            System.out.println("url-------------" + url);
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

                if (new NetCheck().netcheck(HotlineDoctorsQueriesActivity.this)) {

                    pagination = true;

                    url = Model.BASE_URL + "sapp/myQueries?user_id=" + (Model.id) + "&doctor_id=" + Doctor_id + "&format=json&page=1&hline=1&token=" + Model.token;
                    System.out.println("url-------------" + url);
                    new Json_Async_Server().execute(url);

                    mSwipeRefreshLayout.setRefreshing(false);

                } else {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(HotlineDoctorsQueriesActivity.this, "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setScrollViewCallbacks(this);
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        System.out.println("Scrolling----------------------" + scrollState);

        if (scrollState == ScrollState.UP) {
            //mFabToolbar.slideOutFab();
            System.out.println("Scrolling UP---------------------------");
            fab.hide();
        } else if (scrollState == ScrollState.DOWN) {
            //mFabToolbar.slideInFab();
            System.out.println("Scrolling Down---------------------------");
            fab.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.query_list_menu, menu);
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

    public void full_process() {

        if (new NetCheck().netcheck(HotlineDoctorsQueriesActivity.this)) {
            try {

                url = Model.BASE_URL + "sapp/myQueries?user_id=" + (Model.id) + "&doctor_id=" + Doctor_id + "&format=json&page=1&hline=1&token=" + Model.token;
                System.out.println("My Queries Hotline url params----------" + url);
                new Json_Async_Server().execute(url);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(HotlineDoctorsQueriesActivity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }


    private class Json_Async_Server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
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
                    System.out.println("Doctors List--This is JSON OBJECT---------------" + str_response);

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

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        objItem = new Item();
                        objItem.setQid(jsonobj1.getString("id"));
                        objItem.setQuestion(jsonobj1.getString("question"));
                        objItem.setStr_status(jsonobj1.getString("str_status"));
                        objItem.setStatus(jsonobj1.getString("status"));
                        objItem.setDatetime(jsonobj1.getString("datetime"));

                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.VISIBLE);
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    } else {
                        if (arrayOfList.size() < 5) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
        }
    }

    class MyTask_Pagination extends AsyncTask<String, Void, Void> {

        ProgressDialog pDialog;

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
                    System.out.println("Doctors List--This is JSON OBJECT---------------" + str_response);

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

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        objItem = new Item();
                        objItem.setQid(jsonobj1.getString("id"));
                        objItem.setQuestion(jsonobj1.getString("question"));
                        objItem.setStr_status(jsonobj1.getString("str_status"));
                        objItem.setStatus(jsonobj1.getString("status"));
                        objItem.setDatetime(jsonobj1.getString("datetime"));

                        listArray.add(objItem);

                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    } else {
                        if (arrayOfList.size() < 5) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setAdapterToListview() {

        try {
            objAdapter = new MyQueriesHotlineAdapter(HotlineDoctorsQueriesActivity.this, R.layout.hotline_myqueries_row, arrayOfList);
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
}
