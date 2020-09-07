package com.orane.icliniq;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;


public class FeedbackActivity extends AppCompatActivity {

    Button btn_submit, unmute;
    EditText edt_feedback;
    public String str_response, feedback_val, report_response;
    JSONObject json_feedback, json_response_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_ideas);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Feedback");
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        edt_feedback = (EditText) findViewById(R.id.edt_feedback);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String s = edt_feedback.getText().toString();
                    feedback_val = s.replaceAll("\"", "");
                    feedback_val = URLEncoder.encode(s, "utf-8");

                    if (feedback_val.equals("")) {
                        edt_feedback.setError("Please enter the feedback");
                    } else {

                        try {

                            json_feedback = new JSONObject();
                            json_feedback.put("user_id", (Model.id));
                            json_feedback.put("suggestion", feedback_val);


                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("user_id", Model.id);
                            params.putString("feedback", feedback_val);
                            Model.mFirebaseAnalytics.logEvent("suggestion", params);
                            //------------ Google firebase Analitics--------------------

                            //------------- Flurry -------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("user_id", Model.id + "/" + Model.name);
                            articleParams.put("suggestion", feedback_val);
                            FlurryAgent.logEvent("suggestion", articleParams);
                            //------------- Flurry -------------------------------

                            new JSON_Feedback().execute(json_feedback);

                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void say_success() {

        final MaterialDialog alert = new MaterialDialog(FeedbackActivity.this);
        alert.setTitle("Thank you.!");
        alert.setMessage("Your suggestion has been Submitted");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();
            }
        });
        alert.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main, menu);
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


    class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(FeedbackActivity.this);
            dialog.setMessage("please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "suggestion");

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                if (json_response_obj.has("status")) {
                    report_response = json_response_obj.getString("status");
                    System.out.println("report_response--------------" + report_response);

                    if (report_response.equals("1")) {
                        say_success();
                    } else {
                        Toast.makeText(getApplicationContext(), "Feedback failed. Please try again", Toast.LENGTH_LONG).show();
                    }
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
