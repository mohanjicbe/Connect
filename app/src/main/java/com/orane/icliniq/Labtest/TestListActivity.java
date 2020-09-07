package com.orane.icliniq.Labtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestListActivity extends AppCompatActivity {

    ArrayAdapter<String> dataAdapter = null;
    Map<String, String> spec_map = new HashMap<String, String>();
    public String id_val,json_response_tests,name_test,code_val,test_id,params, spec_val, select_Speciality;
    Toolbar toolbar;
    LinearLayout icon_layout, search_box, b1_layout, b2_layout, b3_layout, b4_layout, b5_layout, b6_layout, b7_layout, b8_layout, b9_layout;
    Button btn_viewall;
    JSONObject json_viewtest;
    TextView toolbar_title,toolbar_menu;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speciality_search);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Test List");
        toolbar_menu = (TextView) toolbar.findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);



        //----------- Flurry -------------------------------------------------
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("User_ID", Model.id);
        FlurryAgent.logEvent("android.patient.TestList", properties);
        //----------- Flurry -------------------------------------------------

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        btn_viewall = (Button) findViewById(R.id.btn_viewall);
        icon_layout = (LinearLayout) findViewById(R.id.icon_layout);
        search_box = (LinearLayout) findViewById(R.id.search_box);
        listView = (ListView) findViewById(R.id.listView1);


        try {

            //------ getting Values ---------------------------
            Intent intent = getIntent();
            test_id = intent.getStringExtra("test_id");
            System.out.println("Get Intent test_id-----" + test_id);
            //------ getting Values ---------------------------

            json_viewtest = new JSONObject();
            json_viewtest.put("listId", test_id);

            System.out.println("json_viewtest---" + json_viewtest.toString());

            new json_viewTest().execute(json_viewtest);

        } catch (Exception e2) {
            e2.printStackTrace();
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar_menu.setVisibility(View.GONE);
        icon_layout.setVisibility(View.GONE);
        search_box.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.spec_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Model.query_launch = "";
            finish();
            return true;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        Model.query_launch = "";
        finish();
    }

    private class json_viewTest extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(TestListActivity.this);
            dialog.setMessage("Please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_tests = jParser.JSON_String_POST(urls[0], "view_test");

                System.out.println("json_response_obj URL---------------" + urls[0]);
                System.out.println("json_response_tests-----------" + json_response_tests);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("json_response_obj-------------" + json_response_tests);

                JSONArray reccom_qa = new JSONArray(json_response_tests);


                List<String> categories = new ArrayList<String>();

                for (int i = 0; i < reccom_qa.length(); i++) {
                    JSONObject jsonobj3 = reccom_qa.getJSONObject(i);

                    id_val = jsonobj3.getString("id");
                    name_test = jsonobj3.getString("name");
                    code_val = jsonobj3.getString("code");

                    categories.add(name_test);
                }


                dataAdapter = new ArrayAdapter<String>(TestListActivity.this, R.layout.speciality_list_row, categories);
                listView.setAdapter(dataAdapter);
                listView.setTextFilterEnabled(true);
                //-------------------------------------------------------------------

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
