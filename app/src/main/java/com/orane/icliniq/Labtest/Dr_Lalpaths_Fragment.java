package com.orane.icliniq.Labtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.BadgeCounter;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.adapter.LabtestListAdapter;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dr_Lalpaths_Fragment extends Fragment implements ObservableScrollViewCallbacks {


    FloatingActionButton fab;
    Item objItem;
    public List<Item> listArray;
    ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Button btn_reload, btn_qview, btn_qedit, btn_addtocart;
    LabtestListAdapter objAdapter;
    TextView btn_viewtests, tv_id;
    List<Item> arrayOfList;
    Menu menu_new;
    ObservableListView listView;
    TextView empty_msgmsg;
    JSONObject object, json_addcart;
    public String qid, params;
    private ProgressBar bar;
    Toolbar toolbar;
    JSONArray jsonarray;
    LinearLayout nolayout, netcheck_layout;
    ProgressBar progressBar_bottom;
    CircleImageView imageview_poster;
    public String Log_Status, str_response, query_status, hlstatus, doc_url, edit_query;
    Intent intent;
    JSONObject json_viewtests, json_response_obj;

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


    public Dr_Lalpaths_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.labtest_list, container, false);


        //================ Shared Pref ======================
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        //============================================================

        listView = (ObservableListView) rootView.findViewById(R.id.listview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) rootView.findViewById(R.id.progressBar_bottom);
        netcheck_layout = (LinearLayout) rootView.findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) rootView.findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_query_new);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        full_process();

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
                            try {
                                json_viewtests = new JSONObject();
                                json_viewtests.put("user_id", (Model.id));
                                json_viewtests.put("token", Model.token);
                                json_viewtests.put("disease", "");
                                json_viewtests.put("q", "");
                                json_viewtests.put("page", int_floor);
                                json_viewtests.put("vendor", "2");
                                //json_viewtests.put("list_type", "profile");

                                System.out.println("Async_TestList---" + json_viewtests.toString());

                                new MyTask_Pagination().execute(json_viewtests);

                                //say_success();

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
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

                try {
                    json_viewtests = new JSONObject();
                    json_viewtests.put("user_id", (Model.id));
                    json_viewtests.put("token", Model.token);
                    json_viewtests.put("disease", "");
                    json_viewtests.put("q", "");
                    json_viewtests.put("page", "1");
                    //json_viewtests.put("list_type", "profile");
                    json_viewtests.put("vendor", "2");

                    System.out.println("Async_TestList---" + json_viewtests.toString());

                    new MyTask_server().execute(json_viewtests);

                    //say_success();

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });

        listView.setScrollViewCallbacks(this);

        return rootView;
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        System.out.println("Scrolling---2--2--2--2--4--2--1---------" + scrollState);

/*        if (scrollState == ScrollState.UP) {
            //mFabToolbar.slideOutFab();
            System.out.println("Scrolling UP---------------------------");
            fab.hide();
        } else if (scrollState == ScrollState.DOWN) {
            //mFabToolbar.slideInFab();
            System.out.println("Scrolling Down---------------------------");
            fab.show();
        }*/
    }

    public void full_process() {

        try {
            json_viewtests = new JSONObject();
            json_viewtests.put("user_id", (Model.id));
            json_viewtests.put("token", Model.token);
            json_viewtests.put("disease", "");
            json_viewtests.put("q", "");
            json_viewtests.put("page", "1");
            json_viewtests.put("vendor", "2");
            //json_viewtests.put("list_type", "profile");

            System.out.println("Async_TestList---" + json_viewtests.toString());

            new MyTask_server().execute(json_viewtests);

            //say_success();

        } catch (Exception e2) {
            e2.printStackTrace();
        }


/*        //--------- Listings ---------------------
        String full_url = Model.BASE_URL + "sapp/myCart?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("full_url-------------" + full_url);
        new JSON_cart_list().execute(full_url);
        //--------- Listings ---------------------*/

    }

    class MyTask_server extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(JSONObject... params) {

            try {
                str_response = new JSONParser().JSON_STR_POST(params[0], "labtest");
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

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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
                            objItem.setTestid(jsonobj1.getString("id"));
                            objItem.setTestname(jsonobj1.getString("test_name"));
                            objItem.setDisease(jsonobj1.getString("diseaseGroup"));
                            objItem.setTprice(jsonobj1.getString("price"));
                            objItem.setNormalValue(jsonobj1.getString("normalLabValue"));
                            objItem.setTestCount(jsonobj1.getString("labViewTestCount"));
                            //objItem.setBctype(jsonobj1.getString("strSubTests"));
                            objItem.setDhash(jsonobj1.getString("disease_hash"));
                            objItem.setNhash(jsonobj1.getString("name_hash"));
                            //objItem.setBctype(jsonobj1.getString("code"));
                            objItem.setIsadded(jsonobj1.getString("isAdded"));

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
                    } else {
                        nolayout.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }

                    mSwipeRefreshLayout.setRefreshing(false);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setAdapterToListview() {

        try {

            objAdapter = new LabtestListAdapter(getActivity(), R.layout.labtest_row, arrayOfList);
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


    class MyTask_Pagination extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(JSONObject... params) {
            try {
                str_response = new JSONParser().JSON_STR_POST(params[0], "labtest");
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

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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
                            objItem.setTestid(jsonobj1.getString("id"));
                            objItem.setTestname(jsonobj1.getString("test_name"));
                            objItem.setDisease(jsonobj1.getString("diseaseGroup"));
                            objItem.setTprice(jsonobj1.getString("price"));
                            objItem.setNormalValue(jsonobj1.getString("normalLabValue"));
                            objItem.setTestCount(jsonobj1.getString("labViewTestCount"));
                            //objItem.setBctype(jsonobj1.getString("strSubTests"));
                            objItem.setDhash(jsonobj1.getString("disease_hash"));
                            objItem.setNhash(jsonobj1.getString("name_hash"));
                            //objItem.setBctype(jsonobj1.getString("code"));
                            objItem.setIsadded(jsonobj1.getString("isAdded"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
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

                            add_page_AdapterToListview();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

/*
    public void onClickViewTests(View v) {

        View parent = (View) v.getParent();
        //View grand_parent = (View)parent.getParent();
        btn_viewtests = (TextView) parent.findViewById(R.id.btn_viewtests);

        System.out.println("btn_viewtests--------------");
    }
*/

/*    public void onClickCartText(View v) {

        Intent intent = new Intent(getActivity(), Labtest_CartViewActivity.class);
        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                getActivity().finish();
            }
        });
        startActivityForResult(intent, 1);
    }*/

   /* public void onClickCart(View v) {

        View parent = (View) v.getParent();
        View grand_parent = (View) parent.getParent();

        btn_addtocart = (Button) grand_parent.findViewById(R.id.btn_addtocart);
        tv_id = (TextView) grand_parent.findViewById(R.id.tv_id);

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
*/

    private class json_addcart extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Submitting..., please wait..");
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

                String count = json_response_obj.getString("count");

/*                BadgeCounter.update(getActivity(),
                        menu_new.findItem(R.id.nav_basket),
                        R.mipmap.basket_icon,
                        BadgeCounter.BadgeColor.RED,
                        count);*/

                System.out.println("count-------------" + count);

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_cart_list extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

                JSONObject jobj = new JSONObject(str_response);

                String cartlist_text = jobj.getString("cartlist");
                JSONArray jarray = new JSONArray(cartlist_text);

                Integer cart_size = jarray.length();
                Model.mNotificationCounter = cart_size;

                System.out.println("cart_size--------------" + cart_size);



/*                BadgeCounter.update(getActivity(),
                        menu_new.findItem(R.id.nav_basket),
                        R.mipmap.basket_icon,
                        BadgeCounter.BadgeColor.RED,
                        Model.mNotificationCounter);*/

                System.out.println();

                //produce_list(str_response);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Model.mNotificationCounter < 0) {
            Model.mNotificationCounter = 0;
        }

        System.out.println("Model.mNotificationCounter-----------------" + Model.mNotificationCounter);

/*        BadgeCounter.update(getActivity(),
                menu_new.findItem(R.id.nav_basket),
                R.mipmap.basket_icon,
                BadgeCounter.BadgeColor.RED,
                Model.mNotificationCounter);*/
    }


}
