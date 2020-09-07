package com.orane.icliniq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.adapter.Presription_ListAdapter;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Prescriptions_Activity extends AppCompatActivity {

    Toolbar toolbar;
    RelativeLayout LinearLayout1;
    Button btn_reload;
    Presription_ListAdapter objAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Item objItem;
    List<Item> listArray;
    List<Item> arrayOfList;
    ListView listView;
    TextView empty_msgmsg, tv_info;
    JSONObject object, json_response_obj, jsonobj_canisnaswer, json_err_feedback;
    public String tv_id_val, patient_id, cur_qid, drugs_text, str_response, Log_Status, pat_location, str_price, params, prio_text, followcode_text;
    private ProgressBar bar;
    LinearLayout nolayout, netcheck_layout;
    ProgressBar progressBar, progressBar_bottom;
    Intent intent;
    long startTime;
    ImageView imgapp;
    TextView tvtips1, tvtips2, tvtips3;
    LinearLayout prescribe_layout;

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
    public static final String sp_share_link = "sp_share_link";
    public static final String token = "token_key";

    Double floor_val;
    Integer int_floor;
    public boolean pagination = true;
    TextView tv_noqueries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescriptions_list);


        Model.query_launch = "";

        //------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---------------------------------------------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //---------------------------------------------------

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        Model.fee_q = sharedpreferences.getString(fee_q, "");
        Model.fee_consult = sharedpreferences.getString(fee_consult, "");
        Model.currency_label = sharedpreferences.getString(currency_label, "");
        Model.currency_symbol = sharedpreferences.getString(currency_symbol, "");
        Model.have_free_credit = sharedpreferences.getString(have_free_credit, "");
        Model.photo_url = sharedpreferences.getString(photo_url, "");
        Model.kmid = sharedpreferences.getString(sp_km_id, "");
        //============================================================

        //Model.id = "59700";

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        btn_reload = (Button) findViewById(R.id.btn_reload);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        empty_msgmsg = (TextView) findViewById(R.id.empty_msgmsg);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        listView = (ListView) findViewById(R.id.listview);
        tv_noqueries = (TextView) findViewById(R.id.tv_noqueries);

       /* Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
        tv_noqueries.setTypeface(khandBold);*/


        try {
            Intent intent = getIntent();
            cur_qid = intent.getStringExtra("qid");
            patient_id = intent.getStringExtra("patient_id");

            System.out.println("cur_qid-------------" + cur_qid);
            System.out.println("P List patient_id-------------" + patient_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //---------------------------
        if (Log_Status.equals("1")) {
            full_process();
        } else {
            finishAffinity();
            intent = new Intent(Prescriptions_Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        //---------------------------


        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    full_process();

                } else {
                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);

                    objAdapter.setSelection(position);

                    TextView tv_id = (TextView) view.findViewById(R.id.tv_id);
                    TextView tvquery = (TextView) view.findViewById(R.id.tvquery);
                    TextView tvspeciality = (TextView) view.findViewById(R.id.tvspeciality);
                    TextView tvprice = (TextView) view.findViewById(R.id.tvprice);
                    TextView tv_drug_type = (TextView) view.findViewById(R.id.tv_drug_type);
                    TextView tv_when_to_take = (TextView) view.findViewById(R.id.tv_when_to_take);
                    TextView tv_how_to_take = (TextView) view.findViewById(R.id.tv_how_to_take);
                    TextView tv_days = (TextView) view.findViewById(R.id.tv_days);

                    tv_id_val = tv_id.getText().toString();
                    String drug_name = tvquery.getText().toString();
                    String dose_text = tvspeciality.getText().toString();
                    String qty_val = tvprice.getText().toString();
                    String how_to_take = tv_how_to_take.getText().toString();
                    String days_val = tv_days.getText().toString();
                    String drug_type_val = tv_drug_type.getText().toString();
                    String when_to_take = tv_when_to_take.getText().toString();

                    System.out.println("drug_type_val------------------" + drug_type_val);

                    intent = new Intent(Prescriptions_Activity.this, Prescription_Entry_Activity.class);
                    intent.putExtra("add_type", "update");
                    intent.putExtra("cur_qid", cur_qid);
                    intent.putExtra("pat_id", patient_id);
                    intent.putExtra("prescription_id", tv_id_val);
                    intent.putExtra("drug_name", drug_name);
                    intent.putExtra("dose_text", dose_text);
                    intent.putExtra("qty_val", qty_val);
                    intent.putExtra("how_to_take", how_to_take);
                    intent.putExtra("days_val", days_val);
                    intent.putExtra("drug_type", drug_type_val);
                    intent.putExtra("when_to_take", when_to_take);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    if (isInternetOn()) {
                        pagination = true;

                        //----------------------- Doctor Inbox -------------------------------
                        params = Model.BASE_URL + "sapp/viewPrescription?os_type=android&questionId=" + cur_qid + "&user_id=" + (Model.id) + "&page=1&token=" + Model.token;
                        System.out.println("params-----------" + params);
                        new MyTask_server().execute(params);
                        //----------------------- Doctor Inbox -------------------------------

                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void full_process() {

        if (isInternetOn()) {
            try {
                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                    //----------------------- Doctor Inbox ------------------------------
                    params = Model.BASE_URL + "sapp/viewPrescription?os_type=android&questionId=" + cur_qid + "&user_id=" + (Model.id) + "&page=1&token=" + Model.token;
                    //params = Model.BASE_URL + "sapp/qinboxDoc?user_id=68966&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token;
                    System.out.println("params-----------" + params);
                    new MyTask_server().execute(params);
                    //----------------------- Doctor Inbox -------------------------------

                } else {
                    go_back_msg();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
        }
    }

    private class MyTask_server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);

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

                if (str_response != null && !str_response.isEmpty() && !str_response.equals("null") && !str_response.equals("")) {

                    JSONObject jobj = new JSONObject(str_response);

                    if (jobj.has("patientId")) {
                        patient_id = jobj.getString("patientId");
                        System.out.println("patient_id---------------" + patient_id);
                    }

                    if (jobj.has("drugs")) {
                        drugs_text = jobj.getString("drugs");


                        JSONArray jsonarr = new JSONArray(drugs_text);
                        listArray = new ArrayList<Item>();

                        for (int i = 0; i < jsonarr.length(); i++) {

                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                            objItem = new Item();
                            objItem.setId(jsonobj1.getString("id"));
                            objItem.setName(jsonobj1.getString("drugName"));
                            objItem.setTy(jsonobj1.getString("drugType"));
                            objItem.setPri(jsonobj1.getString("drugDose"));
                            objItem.setAmt(jsonobj1.getString("quantity"));
                            objItem.setSpec(jsonobj1.getString("forDays"));
                            objItem.setDes(jsonobj1.getString("whenToTake"));
                            objItem.setWamt(jsonobj1.getString("howToTake"));

                            listArray.add(objItem);
                        }


                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            nolayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);

                        } else {

                            /*if (arrayOfList.size() < 10) {
                                pagination = false;
                            }*/

                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);

                            setAdapterToListview();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                    }

                    //----------------------------------------------------------
                 /*   long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total Elapsed of AsyncTask (in milliseconds)--- " + elapsedTime);
                    System.out.println("arrayOfList.size()---------" + arrayOfList.size());*/
                    //----------------------------------------------------------
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

            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);

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

                JSONObject jobj = new JSONObject(str_response);
                drugs_text = jobj.getString("drugs");

                JSONArray jsonarr = new JSONArray(drugs_text);
                listArray = new ArrayList<Item>();

                for (int i = 0; i < jsonarr.length(); i++) {

                    JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                    objItem = new Item();
                    objItem.setId(jsonobj1.getString("id"));
                    objItem.setName(jsonobj1.getString("drugName"));
                    objItem.setTy(jsonobj1.getString("drugType"));
                    objItem.setPri(jsonobj1.getString("drugDose"));
                    objItem.setAmt(jsonobj1.getString("quantity"));
                    objItem.setSpec(jsonobj1.getString("forDays"));
                    objItem.setDes(jsonobj1.getString("whenToTake"));
                    objItem.setWamt(jsonobj1.getString("howToTake"));

                    listArray.add(objItem);
                }

                arrayOfList = listArray;

                if (null == arrayOfList || arrayOfList.size() == 0) {

                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);

                } else {
                       /* if (arrayOfList.size() < 10) {
                            pagination = false;
                        }*/

                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);

                    add_page_AdapterToListview();
                }

                //----------------------------------------------------------
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Total Elapsed of AsyncTask (in milliseconds)--- " + elapsedTime);
                System.out.println("arrayOfList.size()---------" + arrayOfList.size());
                //----------------------------------------------------------

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.Prescription_List_Count", "" + arrayOfList.size());
                articleParams.put("android.doc.ElapsedTime", "" + elapsedTime);
                FlurryAgent.logEvent("android.doc.Prescription_List", articleParams);
                //----------- Flurry -------------------------------------------------


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void setAdapterToListview() {
        try {
            objAdapter = new Presription_ListAdapter(Prescriptions_Activity.this, R.layout.prescription_row, arrayOfList);
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


    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public void go_back_msg() {

        Toast.makeText(this, "Oops.! Something went wrong. Please go back and Try again.", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.new_query_menu, menu);
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

        if (Model.query_launch.equals("writePrescription")) {
            Model.query_launch = "";
            full_process();
        }
    }

}
