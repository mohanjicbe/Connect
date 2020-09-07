package com.orane.icliniq.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.Parallex.slidingTab.SlidingTabLayout;
import com.orane.icliniq.R;
import com.orane.icliniq.SpecialityListActivity;
import com.orane.icliniq.adapter.DoctorsRowAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorsFragment extends Fragment {


    ArrayAdapter<String> dataAdapter = null;
    Item objItem;
    public List<Item> listArray;
    public List<Item> arrayOfList;
    public File imageFile;
    JSONObject jsonobj_makefav;
    ViewPager vpPager;
    FloatingActionButton fab;

    ProgressBar progressBar, progressBar_bottom;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    LinearLayout nolayout, netcheck_layout;
    String params, str_response;
    SlidingTabLayout slide1;
    public boolean pagination = true;

    DoctorsRowAdapter objAdapter;
    Map<String, String> spec_map = new HashMap<String, String>();
    public String is_fav, Doc_id, spec_val = "0", fav_url;
    long startTime;


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
    public String Log_Status;
    LinearLayout appBarLayout;
    boolean _isFirsTtime = true;

    public static DoctorsFragment newInstance(int pageIndex) {
        DoctorsFragment contentFragment = new DoctorsFragment();
        Bundle args = new Bundle();
        args.putInt("pageIndex", pageIndex);
        contentFragment.setArguments(args);
        return contentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("OnCreate Fragment-------------");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.doctors_list, container, false);
        System.out.println("OnCreateVIEW Fragment-------------");

        //================ Shared Pref ======================
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        //============================================================

        Model.select_spec_val = "0";

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        appBarLayout = (LinearLayout) view.findViewById(R.id.appBarLayout);
        listView = (ListView) view.findViewById(R.id.listview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) view.findViewById(R.id.progressBar_bottom);
        netcheck_layout = (LinearLayout) view.findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) view.findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_query_new);

        is_fav = "1";

        appBarLayout.setVisibility(View.GONE);

        full_process();

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
/*                TextView tvcfee = (TextView) view.findViewById(R.id.tvcfee);
                TextView tvqfee = (TextView) view.findViewById(R.id.tvqfee);
                TextView tvdocname = (TextView) view.findViewById(R.id.tvdocname);
                TextView tvedu = (TextView) view.findViewById(R.id.tvedu);
                TextView tvspec = (TextView) view.findViewById(R.id.tvspec);
                CircleImageView imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);*/

                Doc_id = tvid.getText().toString();

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("doctor_id", tvid.getText().toString());
                Model.mFirebaseAnalytics.logEvent("doctor_select", params);
                //------------ Google firebase Analitics--------------------


                if (Doc_id != null && !Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                    Intent intent = new Intent(getActivity(), ParallexMainActivity.class);
                    intent.putExtra("tv_doc_id", Doc_id);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Sorry, Something went wrong. GO Back and Try again..!", Toast.LENGTH_LONG).show();
                }
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
                            String url = Model.BASE_URL + "sapp/doctors?user_id=" + (Model.id) + "&page=" + int_floor + "&sp_id=" + (Model.select_spec_val) + "&token=" + Model.token;
                            System.out.println("Pagination url------------" + url);
                            new MyTask_Pagination().execute(url);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        listView.setOnTouchListener(new View.OnTouchListener() {
            float initialY, finalY;
            boolean isScrollingUp;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        initialY = event.getY();
                    case (MotionEvent.ACTION_UP):

                        finalY = event.getY();

                        if (initialY < finalY) {
                            System.out.println("Scrolling up-----");
                            isScrollingUp = true;
                        } else if (initialY > finalY) {
                            System.out.println("Scrolling Down-----");
                            isScrollingUp = false;
                        }
                    default:
                }

                if (isScrollingUp) {
                    System.out.println("Scrolling UP----------");
                    fab.show();
                    //((AppCompatActivity) getActivity()).getSupportActionBar().show();

                } else {
                    System.out.println("Scrolling DOWN----------");
                    fab.hide();
                    //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                }

                return false;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new NetCheck().netcheck(getActivity())) {
                    pagination = true;
                    String url = Model.BASE_URL + "sapp/doctors?user_id=" + (Model.id) + "&page=1&sp_id=" + (Model.select_spec_val) + "&token=" + Model.token;
                    System.out.println("Swipe url------------" + url);
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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SpecialityListActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            if ((Model.query_launch).equals("SpecialityListActivity")) {
                Model.query_launch = "";
                String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&page=1&sp_id=" + (Model.select_spec_val) + "&token=" + (Model.token);
                System.out.println("Resume url--------------" + url);
                new MyTask_server().execute(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void full_process() {

        try {
            Model.select_spec_val = "0";
            if (isInternetOn()) {

                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                    String DoctorList_url = Model.BASE_URL + "sapp/doctors?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + Model.token;
                    System.out.println("DoctorList_url------------" + DoctorList_url);
                    new MyTask_server().execute(DoctorList_url);
                }

            } else {
                mSwipeRefreshLayout.setVisibility(View.GONE);
                nolayout.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.VISIBLE);
                progressBar_bottom.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }

                } else if (json instanceof JSONArray) {

                    try {
                        JSONArray jsonarr = new JSONArray(str_response);
                        listArray = new ArrayList<Item>();

                        for (int i = 0; i < jsonarr.length(); i++) {

                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            objItem = new Item();
                            objItem.setDocid(jsonobj1.getString("id"));
                            objItem.setDocname(jsonobj1.getString("name"));
                            objItem.setDocedu(jsonobj1.getString("edu"));
                            objItem.setDocspec(jsonobj1.getString("speciality"));
                            objItem.setDocimage(jsonobj1.getString("photo_url"));
                            objItem.setDocurl(jsonobj1.getString("doctor_url"));
                            objItem.setCfee(jsonobj1.getString("cfee"));
                            objItem.setQfee(jsonobj1.getString("qfee"));
                            objItem.setFav(jsonobj1.getString("is_fav"));
                            objItem.setAmt(jsonobj1.getString("avg_rating"));
                            objItem.setArtTitle(jsonobj1.getString("rating_lbl"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;
                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            nolayout.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                        } else {

                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

                            setAdapterToListview();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //----------------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class MyTask_Pagination extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
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

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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

                            objItem = new Item();
                            objItem.setDocid(jsonobj1.getString("id"));
                            objItem.setDocname(jsonobj1.getString("name"));
                            objItem.setDocedu(jsonobj1.getString("edu"));
                            objItem.setDocspec(jsonobj1.getString("speciality"));
                            objItem.setDocimage(jsonobj1.getString("photo_url"));
                            objItem.setCfee(jsonobj1.getString("cfee"));
                            objItem.setQfee(jsonobj1.getString("qfee"));
                            objItem.setFav(jsonobj1.getString("is_fav"));
                            objItem.setAmt(jsonobj1.getString("avg_rating"));
                            objItem.setArtTitle(jsonobj1.getString("rating_lbl"));


                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;
                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                        } else {

                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

                            add_page_AdapterToListview();
                        }
                    }
                }
                //----------------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class MyTask_refresh extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                nolayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                netcheck_layout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                progressBar_bottom.setVisibility(View.GONE);

                startTime = System.currentTimeMillis();

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
                        System.out.println("token_status--------" + token_status);
                        if (token_status.equals("0")) {

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //============================================================

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);

                    listArray = new ArrayList<Item>();

                    if (str_response.length() > 2) {
                        listArray = new ArrayList<Item>();
                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            objItem = new Item();
                            objItem.setDocid(jsonobj1.getString("id"));
                            objItem.setDocname(jsonobj1.getString("name"));
                            objItem.setDocedu(jsonobj1.getString("edu"));
                            objItem.setDocspec(jsonobj1.getString("speciality"));
                            objItem.setDocimage(jsonobj1.getString("photo_url"));
                            objItem.setCfee(jsonobj1.getString("cfee"));
                            objItem.setQfee(jsonobj1.getString("qfee"));
                            objItem.setFav(jsonobj1.getString("is_fav"));
                            objItem.setAmt(jsonobj1.getString("avg_rating"));
                            objItem.setArtTitle(jsonobj1.getString("rating_lbl"));


                            listArray.add(objItem);

                        }
                        arrayOfList = listArray;
                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                        } else {
                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total Elapsed Doctor List SERVER time in milliseconds: " + elapsedTime);

                            setAdapterToListview();
                        }
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
            System.out.println("Start Page count--------------" + listView.getCount());

            objAdapter = new DoctorsRowAdapter(getActivity(), R.layout.doctors_row, arrayOfList);
            listView.setAdapter(objAdapter);
            objAdapter.notifyDataSetChanged();
            System.out.println("End Page count--------------" + listView.getCount());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_page_AdapterToListview() {

        try {
            System.out.println("Start Page count--------------" + listView.getCount());
            System.out.println("arrayOfList-------------" + arrayOfList.toString());

            objAdapter.addAll(arrayOfList);
            listView.setSelection(objAdapter.getCount() - (arrayOfList.size()));
            objAdapter.notifyDataSetChanged();

            System.out.println("End Page count--------------" + listView.getCount());
            System.out.println("arrayOfList-------------" + arrayOfList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final boolean isInternetOn() {

        try {
            ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(slide1) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(slide1) == -slide1.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-slide1.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(slide1) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(slide1), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(slide1, translationY);
                ViewHelper.setTranslationY(vpPager, translationY);

/*                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((View) vpPager).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;*/
                //((View) vpPager).requestLayout();
            }
        });
        animator.start();
    }

    protected int getScreenHeight() {
        return getActivity().findViewById(android.R.id.content).getHeight();
    }


}
