package com.orane.icliniq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.adapter.ConsRowAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.orane.icliniq.zoom.Consultation_View;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class ConsultationListActivity extends AppCompatActivity {

    ArrayAdapter<String> dataAdapter = null;
    Item objItem;
    public List<Item> listArray;
    ProgressBar progressBar, progressBar_bottom;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    LinearLayout nolayout, netcheck_layout;
    String params;
    public List<Item> arrayOfList;
    ConsRowAdapter objAdapter;
    TextView tv_placeholder_text;
    Button btn_book_copns;

    public String str_response, docstr, url, sub_url = "sapp/listConsult4Pat";
    public JSONObject doc_obj;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    Intent intent;
    public boolean pagination = true;
    LinearLayout bg_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation_list);

        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.ConsultationList");
        //----------------- Kissmetrics ----------------------------------

        FlurryAgent.onPageView();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        tv_placeholder_text = (TextView) findViewById(R.id.tv_placeholder_text);
        btn_book_copns = (Button) findViewById(R.id.btn_book_copns);
        bg_layout = (LinearLayout) findViewById(R.id.bg_layout);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        //-----------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            if (!(Model.browser_country).equals("IN")) {
                getSupportActionBar().setTitle("My Calls");
            } else {
                getSupportActionBar().setTitle("");
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------------------------------------------------------------------

        if (!(Model.browser_country).equals("IN")) {

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(Model.MyConsultation_Toolbar_title);
            }

            tv_placeholder_text.setText(Model.MyConsultation_nolist_placeholder);
            ((TextView) findViewById(R.id.tv_placeholder_text2)).setText(Model.MyConsultation_nolist_desc);
            btn_book_copns.setText(Model.MyConsultation_button);

        } else {
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
            mTitle.setText("My consultations");
        }

        full_process();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("position-----" + position);

                TextView tvid = (TextView) view.findViewById(R.id.tvid);

                Intent intent = new Intent(ConsultationListActivity.this, Consultation_View.class);
                intent.putExtra("tv_cons_id", (tvid.getText().toString()));
                startActivity(intent);
                //finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                //------------ Google firebase Analitics-----------------------------------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                params.putString("cons_id", tvid.getText().toString());
                Model.mFirebaseAnalytics.logEvent("ConsultationList", params);
                //------------ Google firebase Analitics-----------------------------------------------

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
                            //Toast.makeText(getApplicationContext(),"No more to queries load",Toast.LENGTH_LONG).show();
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

                        System.out.println("pagination----------" + pagination);

                        if (int_floor != 0 && (pagination)) {
                            //---------------------------------------------------
                            String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&format=json&page=" + int_floor + "&token=" + Model.token + "&enc=1";
                            System.out.println("params----------" + url);
                            new MyTask_Pagination().execute(url);
                            //---------------------------------------------------
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

                if (new NetCheck().netcheck(ConsultationListActivity.this)) {
                    pagination = true;
                    //----------------------------------------
                    String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&format=json&page=1&token=" + Model.token + "&enc=1";
                    System.out.println(url);
                    new MyTask_refresh().execute(url);
                    //----------------------------------------

                    mSwipeRefreshLayout.setRefreshing(false);

                } else {

                    try {
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);

                        progressBar_bottom.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(ConsultationListActivity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_book_copns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(ConsultationListActivity.this, DoctorsListActivity.class);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        ConsultationListActivity.this.finish();
                    }
                });
                startActivityForResult(intent, 1);
            }
        });
    }

    public void full_process() {

        try {
            //------------------------------------------------
            String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&format=json&page=1&token=" + Model.token + "&enc=1";
            System.out.println("url-----------" + url);
            new MyTask_server().execute(url);
            //------------------------------------------------

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
            bg_layout.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);
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
                if (str_response != null && !str_response.isEmpty() && !str_response.equals("null") && !str_response.equals("")) {

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
                                Intent intent = new Intent(ConsultationListActivity.this, LoginActivity.class);
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

                                objItem = new Item();
                                objItem.setDocid(jsonobj1.getString("id"));
                                objItem.setCdate(jsonobj1.getString("date"));
                                objItem.setCtime(jsonobj1.getString("time"));
                                objItem.setCtz(jsonobj1.getString("str_timezone"));
                                objItem.setCnotes(jsonobj1.getString("notes"));

                                //--------------------------------------------------------------
                                if (jsonobj1.has("consult_type")) {
                                    objItem.setTy(jsonobj1.getString("consult_type"));
                                } else {
                                    objItem.setTy("none");
                                }
                                //--------------------------------------------------------------

                                //----------- Doctor Details ------------------------
                                docstr = jsonobj1.getString("doctor");
                                doc_obj = new JSONObject(docstr);
                                objItem.setCdname(doc_obj.getString("name"));
                                objItem.setDocimage(doc_obj.getString("photo_url"));
                                //----------- Doctor Details ------------------------

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

                                if (arrayOfList.size() < 5) {
                                    pagination = false;
                                }

                                nolayout.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                netcheck_layout.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                bg_layout.setVisibility(View.GONE);

                                progressBar_bottom.setVisibility(View.GONE);

                                setAdapterToListview();
                            }

                        } else {
                            nolayout.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            bg_layout.setVisibility(View.GONE);

                            progressBar_bottom.setVisibility(View.GONE);
                        }


                    } else {
                        nolayout.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);

                        progressBar_bottom.setVisibility(View.GONE);
                    }
                    //----------------------------------------------------------

                } else {
                    nolayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    netcheck_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    bg_layout.setVisibility(View.GONE);

                    progressBar_bottom.setVisibility(View.GONE);
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
            progressBar.setVisibility(View.GONE);
            bg_layout.setVisibility(View.GONE);

            progressBar_bottom.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                str_response = new JSONParser().getJSONString(params[0]);
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
                System.out.println("str_response--------------" + str_response);

                if (str_response != null && !str_response.isEmpty() && !str_response.equals("null") && !str_response.equals("")) {
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
                                Intent intent = new Intent(ConsultationListActivity.this, LoginActivity.class);
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
                            objItem.setCdate(jsonobj1.getString("date"));
                            objItem.setCtime(jsonobj1.getString("time"));
                            objItem.setCtz(jsonobj1.getString("str_timezone"));
                            objItem.setCnotes(jsonobj1.getString("notes"));

                            //--------------------------------------------------------------
                            if (jsonobj1.has("consult_type")) {
                                objItem.setTy(jsonobj1.getString("consult_type"));
                            } else {
                                objItem.setTy("none");
                            }
                            //--------------------------------------------------------------

                            //----------- Doctor Details ------------------------
                            docstr = jsonobj1.getString("doctor");
                            doc_obj = new JSONObject(docstr);
                            objItem.setCdname(doc_obj.getString("name"));
                            objItem.setDocimage(doc_obj.getString("photo_url"));
                            //----------- Doctor Details ------------------------

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

                            if (arrayOfList.size() < 5) {
                                pagination = false;
                            }

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            bg_layout.setVisibility(View.GONE);

                            progressBar_bottom.setVisibility(View.GONE);

                            add_page_AdapterToListview();
                        }
                    }
                    //----------------------------------------------------------

                } else {
                    nolayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    netcheck_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    bg_layout.setVisibility(View.GONE);

                    progressBar_bottom.setVisibility(View.GONE);
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
            bg_layout.setVisibility(View.GONE);

            progressBar_bottom.setVisibility(View.GONE);

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
                            Intent intent = new Intent(ConsultationListActivity.this, LoginActivity.class);
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
                        objItem.setCdate(jsonobj1.getString("date"));
                        objItem.setCtime(jsonobj1.getString("time"));
                        objItem.setCtz(jsonobj1.getString("str_timezone"));
                        objItem.setCnotes(jsonobj1.getString("notes"));

                        //--------------------------------------------------------------
                        if (jsonobj1.has("consult_type")) {
                            objItem.setTy(jsonobj1.getString("consult_type"));
                        } else {
                            objItem.setTy("none");
                        }
                        //--------------------------------------------------------------

                        //----------- Doctor Details ------------------------
                        docstr = jsonobj1.getString("doctor");
                        doc_obj = new JSONObject(docstr);
                        objItem.setCdname(doc_obj.getString("name"));
                        objItem.setDocimage(doc_obj.getString("photo_url"));
                        //----------- Doctor Details ------------------------

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

        objAdapter = new ConsRowAdapter(ConsultationListActivity.this, R.layout.consultation_row, arrayOfList);
        listView.setAdapter(objAdapter);
        objAdapter.notifyDataSetChanged();
    }

    public void add_page_AdapterToListview() {

        objAdapter.addAll(arrayOfList);
        listView.setSelection(objAdapter.getCount() - (arrayOfList.size()));
        objAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.consultation_menu, menu);
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
            String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&format=json&page=1&token=" + Model.token + "&enc=1";
            new MyTask_server().execute(url);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
