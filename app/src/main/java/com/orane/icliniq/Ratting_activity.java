package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

import me.drakeet.materialdialog.MaterialDialog;

public class Ratting_activity extends AppCompatActivity {

    public View vi;
    private FrameLayout li;
    JSONObject prepay_jobject, jsonobj, jsonobj1, jsonobj_offers;
    JSONArray jsonarray;
    public String msg_date, msg_sender, msg_to, msg_subject, msg_description, prepack_text, str_response, Log_Status, status_val, full_url, off_id, prep_inv_id, prep_inv_fee, prep_inv_strfee, off_label, strfee, strdesc1, strdesc2;
    LinearLayout parent_offer_layout;
    RelativeLayout full_layout;
    LinearLayout nolayout, offer_layout, netcheck_layout;
    ProgressBar progressBar;
    EditText edt_feedback;
    TextView tv_title, tv_desc, offer_title, off_desc, offid, tv_subject, tv_sender, tv_toaddress, tv_date;
    String qid, ans_id_val, doctor_id, ratting_val;
    Long startTime;
    JSONObject jsonobj_ratting;

    private RadioGroup radioGroup;
    private CheckBox headerCheckBox;
    private CheckBox footerCheckBox;
    private CheckBox expandedCheckBox;
    public File imageFile;
    ObservableScrollView scrollview;
    Typeface noto_reg, noto_bold;

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
    public static final String prepack = "prepack_key";

    RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4;
    Button btn_submit;
    LinearLayout ratting_layout;
    TextView text_ratting1, text_ratting2, text_ratting3, text_ratting4;
    TextView text_ratting1_key, text_ratting2_key, text_ratting3_key, text_ratting4_key;
    JSONObject json_inner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratting_view);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);

        }
        //------------ Object Creations -------------------------------

        try {

            Intent intent = getIntent();
            qid = intent.getStringExtra("qid");
            doctor_id = intent.getStringExtra("doctor_id");
            ratting_val = intent.getStringExtra("ratting_val");
            ans_id_val = intent.getStringExtra("ans_id_val");

            System.out.println("qid------------->" + qid);
            System.out.println("doctor_id------------->" + doctor_id);
            System.out.println("ratting_val------------->" + ratting_val);
            System.out.println("ans_id_val------------->" + ans_id_val);

        } catch (Exception e) {
            e.printStackTrace();
        }


        ratingBar1 = findViewById(R.id.ratingBar1);
        ratingBar2 = findViewById(R.id.ratingBar2);
        ratingBar3 = findViewById(R.id.ratingBar3);
        ratingBar4 = findViewById(R.id.ratingBar4);

        edt_feedback = findViewById(R.id.edt_feedback);

        text_ratting1_key = findViewById(R.id.text_ratting1_key);
        text_ratting2_key = findViewById(R.id.text_ratting2_key);
        text_ratting3_key = findViewById(R.id.text_ratting3_key);
        text_ratting4_key = findViewById(R.id.text_ratting4_key);

        text_ratting1 = findViewById(R.id.text_ratting1);
        text_ratting2 = findViewById(R.id.text_ratting2);
        text_ratting3 = findViewById(R.id.text_ratting3);
        text_ratting4 = findViewById(R.id.text_ratting4);

        btn_submit = findViewById(R.id.btn_submit);
        ratting_layout = findViewById(R.id.ratting_layout);


        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Toast.makeText(Ratting_activity.this,
                        "Rating changed, current rating " + ratingBar.getRating(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Toast.makeText(Ratting_activity.this,
                        "Rating changed, current rating " + ratingBar.getRating(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        ratingBar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Toast.makeText(Ratting_activity.this,
                        "Rating changed, current rating " + ratingBar.getRating(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        ratingBar4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Toast.makeText(Ratting_activity.this,
                        "Rating changed, current rating " + ratingBar.getRating(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //------------- post --------------------------------------
                try {
                    JSONObject json_ratting = new JSONObject();

                    String feedback_text = edt_feedback.getText().toString();

                    json_ratting.put("user_id", (Model.id));
                    json_ratting.put("feedback", feedback_text);
                    json_ratting.put("qid", qid);
                    json_ratting.put("answer_id", ans_id_val);
                    json_ratting.put("user_2_rate", doctor_id);
                    json_ratting.put("rating", ratting_val);

                    //--------------------------
                    json_inner = new JSONObject();

                    String key1 = text_ratting1_key.getText().toString();
                    String key2 = text_ratting2_key.getText().toString();
                    String key3 = text_ratting3_key.getText().toString();
                    String key4 = text_ratting4_key.getText().toString();

                    String rate1 = "" + ratingBar1.getRating();
                    String rate2 = "" + ratingBar2.getRating();
                    String rate3 = "" + ratingBar3.getRating();
                    String rate4 = "" + ratingBar4.getRating();


                    json_inner.put(key1, rate1);
                    json_inner.put(key2, rate2);
                    json_inner.put(key3, rate3);
                    json_inner.put(key4, rate4);
                    //--------------------------

                    json_ratting.put("star_rating", json_inner);

                    System.out.println("json_ratting---" + json_ratting.toString());

                    new JSON_post_ratting().execute(json_ratting);

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                //------------- ---------------------------------------------

            }
        });


        //----------------------------------------------------
        full_url = Model.BASE_URL + "sapp/starRatingType?user_id=" + Model.id + "&token=" + Model.token + "&os_type=android";
        System.out.println("full_url-------------" + full_url);
        new JSON_offers_server().execute(full_url);
        //----------------------------------------------------

    }


    private class JSON_offers_server extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Ratting_activity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                /*str_response = "\n" +
                        "{\"resp_time\":\"How fast the doctor responded\",\"empathy\":\"Empathy\",\"det_answer\":\"Detailed Answer\",\"lang_fluency\":\"Language Fluency\"}";
*/
                System.out.println("Server response--------------" + str_response);


                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                JSONObject jsononjsec = new JSONObject(str_response);

                Integer i = 1;

                //-----------------------------------------------
                Iterator<String> iter = jsononjsec.keys();
                while (iter.hasNext()) {
                    try {

                        String key = iter.next();
                        Object value = jsononjsec.get(key);

                        System.out.println("Key-----" + key);
                        System.out.println("value-----" + value);

                        if (i == 1) {
                            text_ratting1.setText(value.toString());
                            text_ratting1_key.setText(key);
                        }
                        if (i == 2) {
                            text_ratting2.setText(value.toString());
                            text_ratting2_key.setText(key);
                        }
                        if (i == 3) {
                            text_ratting3.setText(value.toString());
                            text_ratting3_key.setText(key);
                        }
                        if (i == 4) {
                            text_ratting4.setText(value.toString());
                            text_ratting4_key.setText(key);
                        }
                        i++;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //-------------------------------------------------


                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class JSON_post_ratting extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Ratting_activity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj_ratting = jParser.JSON_POST(urls[0], "post_ratting");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.print("jsonobj_ratting---------------" + jsonobj_ratting.toString());

                if (jsonobj_ratting.has("status")) {
                    String status_val = jsonobj_ratting.getString("status");
                    String msg_val = jsonobj_ratting.getString("msg");
                    //Toast.makeText(Ratting_activity.this, msg_val, Toast.LENGTH_SHORT).show();
                    //finish();
                    say_success(msg_val);
                }
                else{
                    //Toast.makeText(Ratting_activity.this, "Feeback Submission failed..", Toast.LENGTH_SHORT).show();
                    say_success("Feeback Submission failed..");
                }

                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.prepack_menu, menu);
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
    public void onBackPressed() {
        finish();
    }

    public void say_success(String msg) {

        final MaterialDialog alert = new MaterialDialog(Ratting_activity.this);
        //alert.setTitle("Thankyou.!");
        alert.setMessage(msg);
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Model.query_launch="ratting_activity";
                finish();
            }
        });
        alert.show();

    }
}
