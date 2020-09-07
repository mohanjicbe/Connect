package com.orane.icliniq;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.adapter.Comments_ListAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Comments_ListActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    //bKqFrYxltco

    Item objItem;
    public List<Item> listArray;
    ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Button btn_reload, btn_qview, btn_qedit;
    Comments_ListAdapter objAdapter;
    Button btn_askquery;
    List<Item> arrayOfList;
    ObservableListView listView;
    TextView empty_msgmsg;
    JSONObject object;
    public String qid, params;
    Toolbar toolbar;
    JSONArray jsonarray;
    LinearLayout nolayout, netcheck_layout;
    ProgressBar progressBar_bottom;
    CircleImageView imageview_poster;
    public String Log_Status, str_response, query_status, hlstatus, doc_url, edit_query;
    Intent intent;

    public static final String query_list_array = "query_list_array_key";
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
    public static final String query_reponse = "query_reponse_key";

    LinearLayout bg_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_list);

        FlurryAgent.onPageView();

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.query_reponse = sharedpreferences.getString(query_reponse, "");
        //============================================================

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //--------------------------------------------------
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
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

        progressBar = findViewById(R.id.progressBar);
        progressBar_bottom = findViewById(R.id.progressBar_bottom);
        mSwipeRefreshLayout = findViewById(R.id.swipe_query_new);
        //bar = (ProgressBar) findViewById(R.id.progressBar);
        btn_reload = findViewById(R.id.btn_reload);
        netcheck_layout = findViewById(R.id.netcheck_layout);
        nolayout = findViewById(R.id.nolayout);
        empty_msgmsg = findViewById(R.id.empty_msgmsg);
        listView = findViewById(R.id.listview);
        btn_askquery = findViewById(R.id.btn_askquery);
        bg_layout = findViewById(R.id.bg_layout);

        full_process();

        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                full_process();
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
                            System.out.println("No more to Load");
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
                            //-----------------------------------------------------------
                            params = Model.BASE_URL + "sapp/getTesti?user_id=" + (Model.id) + "&page=" + int_floor + "&token=" + Model.token + "&limit=10";
                            System.out.println("params--full_process--" + params);
                            new MyTask_Pagination().execute(params);
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

                if (new NetCheck().netcheck(Comments_ListActivity.this)) {
                    pagination = true;

                    //------------------------------------------------------------
                    params = Model.BASE_URL + "sapp/getTesti?user_id=" + (Model.id) + "&page=1&token=" + Model.token + "&limit=10";
                    System.out.println("params--full_process--" + params);
                    new MyTask_refresh().execute(params);
                    //------------------------------------------------------------

                    mSwipeRefreshLayout.setRefreshing(false);

                } else {

                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    bg_layout.setVisibility(View.GONE);

                    progressBar_bottom.setVisibility(View.GONE);

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

        System.out.println("Scrolling-----------" + scrollState);

        if (scrollState == ScrollState.UP) {
            //mFabToolbar.slideOutFab();
            System.out.println("Scrolling UP---------------------------");
            //fab.hide();
            hideBottomBar();
        } else if (scrollState == ScrollState.DOWN) {
            //mFabToolbar.slideInFab();
            System.out.println("Scrolling Down---------------------------");
            //fab.show();
            showBottomBar();
        }
    }


    private void showBottomBar() {
        moveBottomBar(0);
    }

    private void hideBottomBar() {
       // moveBottomBar(fab.getHeight() + 15);
    }

    private void moveBottomBar(float toTranslationY) {
       /* if (ViewHelper.getTranslationY(fab) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(fab), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(fab, translationY);
*//*                ViewHelper.setTranslationY((View) mScrollable, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) mScrollable).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;*//*
                listView.requestLayout();
            }
        });
        animator.start();*/
    }
    //------------ Toolbar Hide ----------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.query_list_menu, menu);
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
            //------------------------------------------------------------
            params = Model.BASE_URL + "sapp/myQueries?user_id=" + (Model.id) + "&format=json&page=1&token=" + Model.token + "&enc=1";
            System.out.println("params--full_process--" + params);
            new MyTask_refresh().execute(params);
            //------------------------------------------------------------
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

  /*  @Override
    public void onResume() {
        super.onResume();

        System.out.println("Resume Model.query_reponse-----------" + Model.query_reponse);

        if ((Model.query_reponse) != null && !(Model.query_reponse).isEmpty() && !(Model.query_reponse).equals("null") && !(Model.query_reponse).equals("")) {
            try {
                JSONArray jsonarr = new JSONArray(Model.query_reponse);

                if (Model.query_reponse.length() > 2) {

                    listArray = new ArrayList<Item>();

                    for (int i = 0; i < jsonarr.length(); i++) { rajendrachozhankallanai1899
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        System.out.println("jsonobj1-----------" + jsonobj1.toString());

                        objItem = new Item();
                        objItem.setQid(jsonobj1.getString("id"));
                        objItem.setQuestion(jsonobj1.getString("question"));
                        objItem.setStr_status(jsonobj1.getString("str_status"));
                        objItem.setStatus(jsonobj1.getString("status"));
                        objItem.setDatetime(jsonobj1.getString("datetime"));
                        objItem.setDocimage(jsonobj1.getString("doc_photo"));
                        objItem.setBctype(jsonobj1.getString("is_hline"));

                        if (jsonobj1.has("doc_name")) {
                            objItem.setDocname(jsonobj1.getString("doc_name"));
                        }
                        objItem.setHotline_status(jsonobj1.getString("is_hline"));

                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        nolayout.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                        setAdapterToListview();
                    }
                }

                full_process();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            full_process();
        }

    }*/

    public void full_process() {


        try {
            //----------------------------------------------------------------------------
            params = Model.BASE_URL + "sapp/getTesti?user_id=" + (Model.id) + "&page=1&token=" + Model.token + "&limit=10";
            System.out.println("params--full_process--" + params);
            new MyTask_server().execute(params);
            //----------------------------------------------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

/*        if (new NetCheck().netcheck(QueryActivity.this)) {

        } else {
            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }*/
    }

    class MyTask_server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            bg_layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                str_response = new JSONParser().getJSONString(params[0]);
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
                            Intent intent = new Intent(Comments_ListActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(query_reponse, str_response);
                    editor.apply();
                    //============================================================

                    JSONArray jsonarr = new JSONArray(str_response);
                    if (str_response.length() > 2) {

                        listArray = new ArrayList<Item>();

                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            System.out.println("jsonobj1-----------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setFeed(jsonobj1.getString("feed"));
                            objItem.setDocid(jsonobj1.getString("doc_id"));
                            objItem.setDocname(jsonobj1.getString("doc_name"));
                            objItem.setDocurl(jsonobj1.getString("doc_img_url"));
                            objItem.setName(jsonobj1.getString("pat_name"));
                            objItem.setArtDocname(jsonobj1.getString("pat_loc"));
                            objItem.setDocedu(jsonobj1.getString("doc_edu"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            nolayout.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            bg_layout.setVisibility(View.GONE);


                        } else {

                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            bg_layout.setVisibility(View.GONE);


                            setAdapterToListview();
                        }
                    } else {
                        nolayout.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);

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

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            bg_layout.setVisibility(View.GONE);
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
                            Intent intent = new Intent(Comments_ListActivity.this, LoginActivity.class);
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
                            objItem.setFeed(jsonobj1.getString("feed"));
                            objItem.setDocid(jsonobj1.getString("doc_id"));
                            objItem.setDocname(jsonobj1.getString("doc_name"));
                            objItem.setDocurl(jsonobj1.getString("doc_img_url"));
                            objItem.setName(jsonobj1.getString("pat_name"));
                            objItem.setArtDocname(jsonobj1.getString("pat_loc"));
                            objItem.setDocedu(jsonobj1.getString("doc_edu"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            nolayout.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            bg_layout.setVisibility(View.GONE);

                        } else {

                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            bg_layout.setVisibility(View.GONE);

                            setAdapterToListview();
                        }
                    } else {
                        nolayout.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);
                    }
                }

                mSwipeRefreshLayout.setRefreshing(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class MyTask_Pagination extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                nolayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                netcheck_layout.setVisibility(View.GONE);
                progressBar_bottom.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                bg_layout.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                            Intent intent = new Intent(Comments_ListActivity.this, LoginActivity.class);
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
                            objItem.setFeed(jsonobj1.getString("feed"));
                            objItem.setDocid(jsonobj1.getString("doc_id"));
                            objItem.setDocname(jsonobj1.getString("doc_name"));
                            objItem.setDocurl(jsonobj1.getString("doc_img_url"));
                            objItem.setName(jsonobj1.getString("pat_name"));
                            objItem.setArtDocname(jsonobj1.getString("pat_loc"));
                            objItem.setDocedu(jsonobj1.getString("doc_edu"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            bg_layout.setVisibility(View.GONE);

                        } else {

                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            bg_layout.setVisibility(View.GONE);
                            add_page_AdapterToListview();
                        }
                    } else {
                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapterToListview() {

        try {
            objAdapter = new Comments_ListAdapter(Comments_ListActivity.this, R.layout.offers_feedback_row, arrayOfList);
            listView.setAdapter(objAdapter);

            int count = listView.getCount();
            System.out.println("After Set count----- " + count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_page_AdapterToListview() {

        try {
            objAdapter.addAll(arrayOfList);

            listView.setSelection(objAdapter.getCount() - (arrayOfList.size()));
            objAdapter.notifyDataSetChanged();

            int count = listView.getCount();
            System.out.println("After Pagination----- " + count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
