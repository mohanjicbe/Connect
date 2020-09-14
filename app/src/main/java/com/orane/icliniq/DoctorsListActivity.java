package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.adapter.DoctorsRowAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.orane.icliniq.utils.RetrofitService;
import com.orane.icliniq.utils.SpecialistListActivity;
import com.orane.icliniq.utils.walletModel.SearchDoctorModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoctorsListActivity extends AppCompatActivity {

    ArrayAdapter<String> dataAdapter = null;
    Item objItem;
    public List<Item> listArray;
    public List<Item> arrayOfList;
    public List<Item> arrayOfSearch=new ArrayList<>();
    public List<SearchDoctorModel> searchList;
    public File imageFile;
    JSONObject jsonobj_makefav, fav_jsonobj;

    TextView tvid, tv_showall_text, spec_text;
    ProgressBar progressBar, progressBar_bottom;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    LinearLayout spec_layout, nolayout, netcheck_layout;
    String params, str_response;
    //ImageView leftback;
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
    public boolean pagination = true;
    LinearLayout bg_layout;
    Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_list);

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
            getSupportActionBar().setTitle("Doctors");

            spec_layout = (LinearLayout) toolbar.findViewById(R.id.spec_layout);
            tv_showall_text = (TextView) toolbar.findViewById(R.id.tv_showall_text);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        FlurryAgent.onPageView();

        //leftback = (ImageView) findViewById(R.id.leftback);

        spec_text = (TextView) findViewById(R.id.spec_text);
        listView = (ListView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        bg_layout = (LinearLayout) findViewById(R.id.bg_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        btn_search=findViewById(R.id.btn_search);

        is_fav = "1";

        full_process();
        btn_search.setOnClickListener(v -> AlertBoxMethod());
        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Doctorlist");
        //----------------- Kissmetrics ----------------------------------

        tv_showall_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spec_text.setText("All Specialities");
                tv_showall_text.setVisibility(View.GONE);
                Model.select_spec_val = "0";

                //----------------------------------------------
                String Resume_url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&page=1&sp_id=" + Model.select_spec_val + "&token=" + Model.token + "&enc=1";
                System.out.println("ShowAll_url----" + Resume_url);
                new MyTask_server().execute(Resume_url);
                //----------------------------------------------
            }
        });

/*
        leftback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/

//        spec_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DoctorsListActivity.this, SpecialityListActivity.class);
//                startActivity(intent);
//            }
//        });

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

                if (!Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                    //Intent intent = new Intent(DoctorsListActivity.this, DoctorProfileActivity.class);
                    Intent intent = new Intent(DoctorsListActivity.this, ParallexMainActivity.class);
                    intent.putExtra("tv_doc_id", Doc_id);
                    startActivity(intent);
                    //finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry, Something went wrong. GO Back and Try again..!", Toast.LENGTH_LONG).show();
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
                            //----------------------------------------------------------------
                            String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&page=" + int_floor + "&sp_id=" + spec_val + "&token=" + Model.token + "&enc=1";
                            System.out.println("url----------" + url);
                            new MyTask_Pagination().execute(url);
                            //----------------------------------------------------------------
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

                if (new NetCheck().netcheck(DoctorsListActivity.this)) {
                    //----------------------------------------------------------------
                    String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&page=1&sp_id=" + spec_val + "&token=" + Model.token + "&enc=1";
                    System.out.println("url----------" + url);
                    new MyTask_refresh().execute(url);
                    //----------------------------------------------------------------

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
    }

    private void AlertBoxMethod() {
        searchList=new ArrayList<>();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.searchdoctor_alert, null);
        dialogBuilder.setView(dialogView);
        EditText txt_name=dialogView.findViewById(R.id.edt_name);
        Button btn_cancel=dialogView.findViewById(R.id.btnCancel);
        Button btnSearch=dialogView.findViewById(R.id.btnSearch);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (txt_name.getText().toString().isEmpty()){
                    Toast.makeText(DoctorsListActivity.this, "Please enter Name ", Toast.LENGTH_SHORT).show();
                }else
                 alertDialog.dismiss();
                  SearchDoctor(txt_name.getText().toString());
            }
        });
    }

    private void SearchDoctor(String name) {
        arrayOfSearch.clear();
        searchList.clear();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        Model.select_spec_val="0";
//       "?user_id=" + (Model.id) + "&page=1&sp_id=" + Model.select_spec_val + "&token=" + Model.token + "&enc=1"+"doc_name"+name;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Model.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        service.searchDoctorDetails(Model.id,"1 ","0",Model.token,"1",name).enqueue(new Callback<List<SearchDoctorModel>>() {
            @Override
            public void onResponse(Call<List<SearchDoctorModel>> call, Response<List<SearchDoctorModel>> response) {
                progressDialog.dismiss();
                if (response.code()==200){
                    Log.e("response",response.code()+" ");
                    if (response.body() != null) {
                        searchList.addAll(response.body());
                        if(searchList!=null&&searchList.size()>0){
                            Log.e("search name",searchList.get(0).getName()+"  ");
                            Log.e("search name",searchList.get(0).getSpeciality()+"  ");
                            for (int i=0;i<searchList.size();i++){
                                Item item=new Item();
                                item.setName(searchList.get(i).getName());
//                                item.setName();
                                item.setAmt (searchList.get(i).getAvgRating());
                                item.setCfee(searchList.get(i).getCfee());
                                item.setQfee(searchList.get(i).getQfee());
                                item.setDocedu(searchList.get(i).getEdu());
                                item.setDocurl(searchList.get(i).getDoctorUrl());
                                item.setDocimage(searchList.get(i).getPhotoUrl());
                                item.setArtTitle (searchList.get(i).getIsStar());
                                item.setDocname(searchList.get(i).getName());
                                item.setDocspec(searchList.get(i).getSpeciality());
                                item.setDocid(searchList.get(i).getId());
                                Log.e("arraysearch",searchList.get(i).getSpeciality()+" ");
                                Log.e("arraysearch",searchList.get(i).getName()+" ");
                                Log.e("arraysearch",searchList.get(i).getSpeciality()+" ");
                                arrayOfSearch.add(item);
                            }
                                Log.e("arraysearch",arrayOfSearch.size()+" ");

                            try {
                                objAdapter = new DoctorsRowAdapter(DoctorsListActivity.this, R.layout.doctors_row, arrayOfSearch);
                                listView.setAdapter(objAdapter);
//                                objAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(DoctorsListActivity.this, "please enter correct name", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<SearchDoctorModel>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DoctorsListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void full_process() {

        Model.select_spec_val = "0";
        tv_showall_text.setVisibility(View.GONE);

        if (isInternetOn()) {

            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                //--------------------------------------
                String url = Model.BASE_URL + "sapp/doctors?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + (Model.token) + "&enc=1";
                System.out.println("url---" + url);
                new MyTask_server().execute(url);
                //--------------------------------------
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
                            Intent intent = new Intent(DoctorsListActivity.this, LoginActivity.class);
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
                        objItem.setDocname(jsonobj1.getString("name"));
                        objItem.setDocedu(jsonobj1.getString("edu"));
                        objItem.setDocspec(jsonobj1.getString("speciality"));
                        objItem.setDocimage(jsonobj1.getString("photo_url"));
                        objItem.setDocurl(jsonobj1.getString("doctor_url"));
                        objItem.setCfee(jsonobj1.getString("cfee"));
                        objItem.setQfee(jsonobj1.getString("qfee"));


                        System.out.println("Geetying Ratting Value----------" + jsonobj1.getString("avg_rating"));

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
                            Intent intent = new Intent(DoctorsListActivity.this, LoginActivity.class);
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
                        objItem.setDocname(jsonobj1.getString("name"));
                        objItem.setDocedu(jsonobj1.getString("edu"));
                        objItem.setDocspec(jsonobj1.getString("speciality"));
                        objItem.setDocimage(jsonobj1.getString("photo_url"));
                        objItem.setCfee(jsonobj1.getString("cfee"));
                        objItem.setQfee(jsonobj1.getString("qfee"));
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
                            Intent intent = new Intent(DoctorsListActivity.this, LoginActivity.class);
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
                        objItem.setDocname(jsonobj1.getString("name"));
                        objItem.setDocedu(jsonobj1.getString("edu"));
                        objItem.setDocspec(jsonobj1.getString("speciality"));
                        objItem.setDocimage(jsonobj1.getString("photo_url"));
                        objItem.setCfee(jsonobj1.getString("cfee"));
                        objItem.setQfee(jsonobj1.getString("qfee"));
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
            objAdapter = new DoctorsRowAdapter(DoctorsListActivity.this, R.layout.doctors_row, arrayOfList);
            listView.setAdapter(objAdapter);
            objAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.doclist_menu, menu);
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

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("All Doctors");
            }

            //--------------------------------------
            spec_val = "0";
            String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + Model.token + "&enc=1";
            System.out.println("url----------" + url);
            new MyTask_server().execute(url);
            //--------------------------------------

            return true;
        }


        if (id == R.id.nav_search) {

        AlertBoxMethod();

            return true;
        }

        if (id == R.id.nav_specfilter) {

            //---------------------------------------------
//            Intent intent = new Intent(DoctorsListActivity.this, SpecialityListActivity.class);
//            startActivity(intent);
//
            Intent intent = new Intent(DoctorsListActivity.this, SpecialistListActivity.class);
            startActivity(intent);

            //finish();
            //---------------------------------------------
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Model.query_launch).equals("SpecialistListActivity")) {

            Model.query_launch = "";

            if(Model.select_spec_val.equals("0")){
                spec_layout.setVisibility(View.VISIBLE);
                spec_text.setText("All Specialities");
            }
            else{
                spec_layout.setVisibility(View.VISIBLE);
                spec_text.setText(Model.select_specname);
            }

            tv_showall_text.setVisibility(View.GONE);

            //---------------------------------------------
            String url = Model.BASE_URL + sub_url + "?user_id=" + (Model.id) + "&page=1&sp_id=" + Model.select_spec_val + "&token=" + (Model.token);
            System.out.println("url-----" + url);
            new MyTask_server().execute(url);
            //---------------------------------------------
        }
    }

    public void force_logout() {

        try {
            final MaterialDialog alert = new MaterialDialog(DoctorsListActivity.this);
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
                    Intent i = new Intent(DoctorsListActivity.this, LoginActivity.class);
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

                    Intent intent = new Intent(DoctorsListActivity.this, ParallexMainActivity.class);
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

                    Intent i = new Intent(DoctorsListActivity.this, HotlinePackagesActivity.class);
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
                            fav_url = Model.BASE_URL + "/sapp/add2fav?user_id=" + (Model.id) + "&item_type_id=1&remove=1&item_id=" + Doc_id;
                            System.out.println("Remove Favoueite url-------------" + fav_url);
                            is_fav = "0";
                            //------------------------------------------

                        } else {

                            View parent_1 = (View) v.getParent();
                            ImageView img_fav = (ImageView) parent_1.findViewById(R.id.img_fav);
                            TextView tvid = (TextView) parent_1.findViewById(R.id.tvid);

                            Doc_id = tvid.getText().toString();
                            img_fav.setImageResource(R.mipmap.love_grey_full);
                            fav_url = Model.BASE_URL + "/sapp/add2fav?user_id=" + (Model.id) + "&item_type_id=1&item_id=" + Doc_id;
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
                        Intent intent = new Intent(DoctorsListActivity.this, LoginActivity.class);
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


    public void onDoctorShare(View v) {

        try {

            switch (v.getId()) {

                case R.id.sharedoc_layout:

                    //View parent = (View) v.getParent();
                    TextView tv_doclink = v.findViewById(R.id.tv_doclink);
                    TextView tvdocname_new = v.findViewById(R.id.tvdocname_new);
                    TextView tv_spec_new = v.findViewById(R.id.tv_spec_new);

                    String doclink = tv_doclink.getText().toString();
                    String docname_share_val = tvdocname_new.getText().toString();
                    String spec_new_val = tv_spec_new.getText().toString();

                    System.out.println("doclink-----------" + doclink);
                    System.out.println("docname_share_val-----------" + docname_share_val);
                    System.out.println("spec_new_val-----------" + spec_new_val);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    //i.putExtra(Intent.EXTRA_SUBJECT, ); "\n\n
                    String sAux = "It is quick to consult doctor " + docname_share_val + " " + spec_new_val + " online at  iCliniq Connect. \n\n " + doclink;

                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onClick_docs(View v) {

        try {
            View parent = (View) v.getParent();

            TextView tvid = parent.findViewById(R.id.tvid);

            String Doc_id = tvid.getText().toString();

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("doctor_id", tvid.getText().toString());
            Model.mFirebaseAnalytics.logEvent("doctor_select", params);
            //------------ Google firebase Analitics--------------------

            if (Doc_id != null && !Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                Intent intent = new Intent(DoctorsListActivity.this, ParallexMainActivity.class);
                intent.putExtra("tv_doc_id", Doc_id);
                startActivity(intent);
            } else {
                Toast.makeText(DoctorsListActivity.this, "Sorry, Something went wrong. Go Back and Try again..!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_book(View v) {

        try {

            View parent = (View) v.getParent();
            View grand_parent = (View) parent.getParent();

            TextView tvid = grand_parent.findViewById(R.id.tvid);

            String Doc_id = tvid.getText().toString();

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("doctor_id", tvid.getText().toString());
            Model.mFirebaseAnalytics.logEvent("doctor_select", params);
            //------------ Google firebase Analitics--------------------

            if (Doc_id != null && !Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                Intent intent = new Intent(DoctorsListActivity.this, ParallexMainActivity.class);
                intent.putExtra("tv_doc_id", Doc_id);
                startActivity(intent);
            } else {
                Toast.makeText(DoctorsListActivity.this, "Sorry, Something went wrong. Go Back and Try again..!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
