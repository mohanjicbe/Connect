package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Agencies_List_Activity extends AppCompatActivity {

    ArrayAdapter<String> dataAdapter = null;
    Map<String, String> tz_map = new HashMap<String, String>();
    public String params, tz_name, tz_val;
    Toolbar toolbar;
    String agency_type_obj;
    ListView listView;
    List<String> categories;
    JSONObject tz_jsonobj, json_response;
    JSONArray tz_jsonarray;
    EditText myFilter;
    public String tz_jsonstr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agencies_search);

        //------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            getSupportActionBar().setTitle("Select nearest agency");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        listView = (ListView) findViewById(R.id.listView1);
        myFilter = (EditText) findViewById(R.id.myFilter);
        categories = new ArrayList<String>();

        try {

            //------ getting Values -------------------------------------
            Intent intent = getIntent();
            agency_type_obj = intent.getStringExtra("agency_type");
            //------ getting Values --------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        //---------------------------------------
        new JSON_Timezone().execute("");

        //---------------------------------------

        dataAdapter = new ArrayAdapter<String>(this, R.layout.agencies_list_row, categories);
        listView.setAdapter(dataAdapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView tvspecname = (TextView) view.findViewById(R.id.tvspecname);

                tz_name = tvspecname.getText().toString();
                tz_val = tz_map.get(tz_name);

                Model.agency_name = tz_name;
                Model.agency_val = tz_val;

                System.out.println("Model.cons_timezone_name--------------" + (Model.cons_timezone_name));
                System.out.println("Model.cons_timezone_val--------------" + (Model.cons_timezone_val));

                Model.query_launch = "agencies";

                finish();
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);

        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return true;
    }


    @Override
    public void onBackPressed() {

        finish();
    }

    class JSON_Timezone extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Agencies_List_Activity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                //json_response = jParser.getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }


        protected void onPostExecute(Boolean result) {

            try {

                json_response = new JSONObject(agency_type_obj);

                tz_jsonarray = new JSONArray();
                tz_jsonarray.put(agency_type_obj);

                System.out.println("tz.length()-----" + tz_jsonarray.length());
                System.out.println("tz jsonarray-----" + tz_jsonarray.toString());

                Iterator<String> iter = json_response.keys();

                while (iter.hasNext()) {

                    String key = iter.next();
                    System.out.println("key-----" + key);

                    try {

                        Object value = json_response.get(key);
                        System.out.println("key_values=======" + value.toString());

                        categories.add(value.toString());
                        tz_map.put(value.toString(), key);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }
}
