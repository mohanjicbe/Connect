package com.orane.icliniq;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.attachment_view.GridViewActivity;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class QueryViewActivity extends BaseActivity implements ObservableScrollViewCallbacks, View.OnClickListener, TextLinkClickListener {

    Animation shake;
    private Menu menu;
    public String is_star_val, str_drug_dets,qid_in_ans, has_prescription_val, strHtmlQus_text, strHtmlAns_text, spec_val, inf_for, fee_str_text, action, gender_val, age_val, age_gender_text, consulted_for_text, rating_text, pat_feedback_text, reply_text, arr_feedback_text, attach_file_text, file_url, str_response, is_hline, answer_created_at, doctor_str, answer_ext, query_str, doc_name, doc_photo_json, speciality_json, pqid, qtype, query_draft_text, serverResponseMessage, selectedPath, status_postquery, sel_filename, last_upload_file, attach_status, attach_file_url, attach_filename, local_url, contentAsString, upLoadServerUri, attach_id, attach_qid, upload_response, image_path, selectedfilename;
    View addView;
    InputStream is = null;
    Typeface font_reg, font_bold;
    RelativeLayout parlayout;
    android.widget.TextView tv_ext_desc;
    android.widget.TextView  tv_ans_filename, tv_replytext, tv_fee11, tvqfee, tv_patfeedback, new_text, tvaskquery, edt_title;
    ProgressBar progressBar;
    LinearLayout query_display_layout, Submit_layout, like_layout_par, doc_layout, extra_layout;
    Bitmap bitmap;
    ObservableWebView query_webview, webview_answer;
    CircleImageView doc_photo;
    EditText edt_query;
    JSONArray jarray_files;
    View vi_ext, vi_ans_ext;
    LinearLayout answer_layout_attachfile, answer_files_layout, myLayout, file_list, doctor_reply_section, ans_extra_layout;
    LinearLayout feedback_section, nolayout, netcheck_layout, full_layout, files_layout, layout_attachfile, str_status_layout, edt_query_layout, note_layout, ftrack_layout, recc_layout, quest_layout, qns_layout, pay_layout;
    View vi_ans, vi, vi_files;
    JSONObject jsonobj_icq50, json_followup, jsonobj_files, json, feedback_json, feedback_jsonobj, jsonobj_prep_inv;
    android.widget.TextView ftrack_text, tv_cons_for, btn_paynow, qtitle;
    Button btn_icq50, btn_paytm;
    LinearLayout like_layout, dislike_layout, btn_pay;
    Button btn_askfollup;
    public String doctor_id, file_user_id, file_doctype, file_file, file_ext, q_files_text, ratting_val, cur_answer_id, answerval_id, pass, uname, str_status, unpaid_fee, unpaid_invid, unpaid_json_text, str_follow_fee, ftrack_str_status_val, ftrack_str_status, ftrack_fee, ftrack_str, qid, qastatus, more_comp, prev_hist, cur_medi, past_medi, lab_tests, prob_caus, inv_done, diff_diag, prob_diag, treat_plan, prev_measu, reg_folup;
    ScrollView scrollview;
    public String a_files_text, feedback_text, inv_id, inv_fee, inv_strfee, feedback_status, docname, query_txt, answer, answer_text, status, question_ext, created_at, question;
    HashMap<String, String> reccom_list = new HashMap<String, String>();
    android.widget.TextView tv_patq, tv_qid_value, tv_filename, tv_ext, tv_userid, TextView, ftrack_desc;
    Button btn_ftrack;
    HashMap<String, String> docname_hash = new HashMap<String, String>();

    Map<String, String> extra_ans_map = new HashMap<String, String>();
    Map<String, String> extra_query_map = new HashMap<String, String>();

    public JSONObject jsononjsec, ansjsonobject, jsonobj, jsonobj1, jsonobj_followup;
    public JSONArray jsonarray, ansjsonarray;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
    public static final String bcountry = "bcountry_key";
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
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_qid = "sp_qid_key";
    public static final String token = "token_key";

    Button btn_cancel1;
    private LinkEnabledTextView check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_view_detail);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);

        FlurryAgent.onPageView();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Query View");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        check = new LinkEnabledTextView(QueryViewActivity.this, null);
        check.setOnTextLinkClickListener(QueryViewActivity.this);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String Log_Status = sharedpreferences.getString(Login_Status, "");
        uname = sharedpreferences.getString(user_name, "");
        docname = sharedpreferences.getString(Name, "");
        Model.name = sharedpreferences.getString(Name, "");
        pass = sharedpreferences.getString(password, "");
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

        file_list = findViewById(R.id.file_list);
        edt_title = findViewById(R.id.edt_title);
        btn_cancel1 = findViewById(R.id.btn_cancel1);
        btn_icq50 = findViewById(R.id.btn_icq50);
        netcheck_layout = findViewById(R.id.netcheck_layout);
        nolayout = findViewById(R.id.nolayout);
        edt_query_layout = findViewById(R.id.edt_query_layout);
        full_layout = findViewById(R.id.full_layout);
        ftrack_text = findViewById(R.id.ftrack_text);
        tvaskquery = findViewById(R.id.tvaskquery);
        btn_ftrack = findViewById(R.id.btn_ftrack);
        parlayout = findViewById(R.id.parlayout);
        new_text = findViewById(R.id.new_text);
        btn_paynow = findViewById(R.id.btn_paynow);
        str_status_layout = findViewById(R.id.str_status_layout);
        note_layout = findViewById(R.id.note_layout);
        ftrack_layout = findViewById(R.id.ftrack_layout);
        ftrack_desc = findViewById(R.id.ftrack_desc);
        btn_pay = findViewById(R.id.btn_pay);
        btn_paytm = findViewById(R.id.btn_paytm);
        edt_query = findViewById(R.id.edt_query);
        scrollview = findViewById(R.id.scrollview);
        myLayout = findViewById(R.id.parent_qalayout);
        recc_layout = findViewById(R.id.recc_layout);
        qtitle = findViewById(R.id.qtitle);
        tvqfee = findViewById(R.id.tvqfee);
        tv_fee11 = findViewById(R.id.tv_fee11);
        btn_askfollup = findViewById(R.id.btn_askfollup);
        pay_layout = findViewById(R.id.pay_layout);
        Submit_layout = findViewById(R.id.Submit_layout);

        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tvaskquery.setTypeface(font_bold);
        ftrack_desc.setTypeface(font_reg);

        ((android.widget.TextView) findViewById(R.id.tv_cantwait)).setTypeface(font_bold);
        ((android.widget.TextView) findViewById(R.id.tv_nottext)).setTypeface(font_reg);
        ((android.widget.TextView) findViewById(R.id.edt_title)).setTypeface(font_bold);

        btn_ftrack.setTypeface(font_bold);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        fullprocess();

        edt_query.setFocusable(true);
        scrollview.fullScroll(ScrollView.FOCUS_DOWN);

        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_ftrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----------------------------------------------------
                Model.query_launch = "Query_View";
                String url = Model.BASE_URL + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=ftrack&item_id=" + (qid) + "&token=" + Model.token + "&enc=1";
                System.out.println("Prep inv===========" + url);
                new JSON_Prepare_inv().execute(url);
                //---------------------------------------------------
            }
        });

        btn_icq50.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (new NetCheck().netcheck(QueryViewActivity.this)) {

                    Model.query_cache = edt_query.getText().toString();

                    try {
                        String query_str = edt_query.getText().toString();

                        if ((query_str.length()) > 0) {

                                   /* String spintext = spinner_speciality.getSelectedItem().toString();
                                    spec_val = spec_map.get(spintext); */

                            spec_val = Model.select_spec_val;

                            query_txt = URLEncoder.encode((edt_query.getText().toString()), "UTF-8");
                            json = new JSONObject();
                            json.put("query", (edt_query.getText().toString()));

                                   /*//---------------------------------------------------------
                                    if (spintext.equals("Choose speciality (optional)"))
                                        spec_val = "0";
                                    if (spintext.length() <= 0)
                                        spec_val = "0";
                                    //---------------------------------------------------------
*/
                            System.out.println("spec_val-------------------" + spec_val);
                            json.put("is_icq100", "2");
                            json.put("speciality", spec_val);
                            json.put("doctor_id", doctor_id);
                            json.put("pqid", "0");
                            json.put("qid", qid);

                            inf_for = "icq50";

                            new JSONPostQuery().execute(json);

                            //------------ Google firebase Analitics-----------------------------------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("user_id", Model.id);
                            Model.mFirebaseAnalytics.logEvent("icq50hrs_Query_Post", params);
                            //------------ Google firebase Analitics----------------------------------------------

                        } else
                            edt_query.setError("Please enter your query");
                        edt_query.requestFocus();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(QueryViewActivity.this, "Please check your Internet Connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if ((unpaid_json_text.length()) > 2) {
                        //Intent intent = new Intent(QueryViewActivity.this, Invoice_Page_New.class);
                        Intent intent = new Intent(QueryViewActivity.this, Invoice_Page_New.class);
                        intent.putExtra("qid", qid);
                        intent.putExtra("inv_id", inv_id);
                        intent.putExtra("inv_strfee", inv_strfee);
                        intent.putExtra("type", "query");

                        System.out.println("Pay inv_id----" + inv_id);
                        System.out.println("Pay inv_strfee----" + inv_strfee);

                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    } else {
                        Model.query_launch = "Query_View";
                        //--------------------------------------------
                        String url = Model.BASE_URL + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=query&item_id=" + qid + "&token=" + Model.token;
                        System.out.println("Prep inv------------" + url);
                        new JSON_Prepare_inv().execute(url);
                        //--------------------------------------------
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        btn_askfollup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    submit_query();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void fullprocess() {

        if (new NetCheck().netcheck(QueryViewActivity.this)) {
            try {

                //------------ Object Creations -------------------------------
                try {
                    Intent intent = getIntent();
                    qid = intent.getStringExtra("qid");
                    qtype = intent.getStringExtra("qtype");

                    System.out.println("Get Intent qid-----" + qid);
                    System.out.println("Get Intent qtype-----" + qtype);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {

                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("Query_id:", qid);
                    FlurryAgent.logEvent("android.patient.Query_View", articleParams);
                    //----------- Flurry -------------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("View_Query_id_Start------------" + qid);

                try {

                    if (qid != null && !qid.isEmpty() && !qid.equals("null") && !qid.equals("")) {
                        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                            //--------------------------
                            String full_url = Model.BASE_URL + "sapp/viewq?id=" + qid + "&user_id=" + (Model.id) + "&format=json&token=" + Model.token + "&enc=1&isAFiles=1";
                            System.out.println("Viewq url-------------" + full_url);
                            new Query_View_AsyncTask().execute(full_url);
                            //--------------------------

                            //---------------- Auto Scroll Bottom -----------------------
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            };
                            scrollview.post(runnable);
                            //---------------- Auto Scroll Bottom -----------------------

                        } else {
                            Toast.makeText(getApplicationContext(), "Sorry. Something went wrong. Please Logout and Login again.", Toast.LENGTH_LONG).show();
                            force_logout();
                        }

                    } else {

                        Toast.makeText(getApplicationContext(), "Sorry. Something went wrong. Please Go back and Try again.", Toast.LENGTH_LONG).show();
                        force_logout();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            scrollview.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

    }


    private class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj_followup = jParser.JSON_POST(urls[0], "PostQuery");

                System.out.println("urls[0]---------------" + urls[0]);
                System.out.println("Response jsonobj_followup---------------" + jsonobj_followup.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                if (jsonobj_followup.has("token_status")) {
                    String token_status = jsonobj_followup.getString("token_status");
                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(QueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    if (jsonobj_followup.has("qid")) {
                        qid = jsonobj_followup.getString("qid");
                        System.out.println("Return Followup qid--------" + qid);
                    }

                    if (jsonobj_followup.has("icq100_id")) {

                        String icq100_id_val = jsonobj_followup.getString("icq100_id");

                        //------------ Prepare Invoice------------------------------------
                        String url = (Model.BASE_URL) + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=" + inf_for + "&item_id=" + icq100_id_val + "&token=" + Model.token + "&enc=1";
                        System.out.println("Query2 Prepare Invoice url-------------" + url);
                        new JSON_Prepare_inv().execute(url);
                        //------------ Prepare Invoice------------------------------------

                    } else {

                        Intent intent = new Intent(QueryViewActivity.this, File_Upload_Screen.class);
                        intent.putExtra("qid", qid);
                        startActivity(intent);
                        finish();
                    }

                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    progressBar.setVisibility(View.GONE);
                    scrollview.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(QueryViewActivity.this);
            dialog.setMessage("Preparing Invoice. Please wait..");
            dialog.show();
            dialog.setCancelable(false);
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

                jsonobj_prep_inv = new JSONObject(str_response);

                if (jsonobj_prep_inv.has("token_status")) {
                    String token_status = jsonobj_prep_inv.getString("token_status");

                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    inv_id = jsonobj_prep_inv.getString("id");
                    inv_fee = jsonobj_prep_inv.getString("fee");
                    inv_strfee = jsonobj_prep_inv.getString("str_fee");

                    Intent intent = new Intent(QueryViewActivity.this, Invoice_Page_New.class);
                    intent.putExtra("qid", qid);
                    intent.putExtra("inv_id", inv_id);
                    intent.putExtra("inv_strfee", inv_strfee);
                    intent.putExtra("type", "query");
                    startActivity(intent);

                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // getMenuInflater().inflate(R.menu.ask_menu, menu);
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


/*    private void show_send() {
        MenuItem item1 = menu.findItem(R.id.nav_send);
        item1.setVisible(true);
        MenuItem item2 = menu.findItem(R.id.nav_doctors);
        item2.setVisible(false);
        MenuItem item3 = menu.findItem(R.id.nav_hotline);
        item3.setVisible(false);
        MenuItem item4 = menu.findItem(R.id.nav_mydocts);
        item4.setVisible(false);
        MenuItem item5 = menu.findItem(R.id.nav_howworks);
        item5.setVisible(false);
        MenuItem item6 = menu.findItem(R.id.nav_newquery);
        item6.setVisible(true);
    }

    private void hide_send() {
        MenuItem item1 = menu.findItem(R.id.nav_send);
        item1.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.nav_doctors);
        item2.setVisible(true);
        MenuItem item3 = menu.findItem(R.id.nav_hotline);
        item3.setVisible(true);
        MenuItem item4 = menu.findItem(R.id.nav_mydocts);
        item4.setVisible(true);
        MenuItem item5 = menu.findItem(R.id.nav_howworks);
        item5.setVisible(true);
        MenuItem item6 = menu.findItem(R.id.nav_newquery);
        item6.setVisible(true);
    }*/


    private class Query_View_AsyncTask extends AsyncTask<String, Void, Boolean> {

        AlertDialog dialog;

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            scrollview.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
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

            myLayout.removeAllViews();

            try {

                jsonobj = new JSONObject(str_response);

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    jsonarray = new JSONArray();
                    jsonarray.put(jsonobj);

                    System.out.println("jsonarray.length()-----" + jsonarray.length());
                    System.out.println("jsonarray-----" + jsonarray.toString());

                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobj1 = jsonarray.getJSONObject(i);
                        System.out.println("jsonobj_first-----" + jsonobj1.toString());

                        for (int j = 1; j <= 60; j++) {

                            more_comp = "";
                            prev_hist = "";
                            cur_medi = "";
                            past_medi = "";
                            lab_tests = "";
                            question = "";

                            String s = "" + j;
                            if (jsonobj1.has("" + s)) {

                                String thread = jsonobj1.getString("" + s);
                                System.out.println("thread-----" + thread);

                                jsononjsec = new JSONObject(thread);

                                if (jsononjsec.has("ftrack")) {
                                    ftrack_str = jsononjsec.getString("ftrack");
                                } else {
                                    ftrack_str = "";
                                }

                                qid = jsononjsec.getString("id");
                                pqid = jsononjsec.getString("pqid");
                                question = jsononjsec.getString("question");

                                //--------------------------------------------------
                                if (jsononjsec.has("consulted_for")) {
                                    consulted_for_text = jsononjsec.getString("consulted_for");
                                } else {
                                    consulted_for_text = "";
                                }
                                //--------------------------------------------------

                                created_at = jsononjsec.getString("created_at");
                                question_ext = jsononjsec.getString("question_ext");
                                q_files_text = jsononjsec.getString("q_files");
                                age_gender_text = jsononjsec.getString("age_gender");
                                answer = jsononjsec.getString("answer");
                                is_hline = jsononjsec.getString("is_hline");


                                if (age_gender_text.length() > 2) {
                                    //----------- get Gender age ----------------------
                                    JSONObject age_json = new JSONObject(age_gender_text);
                                    System.out.println("age_json-------------- " + age_json);

                                    if (age_json.has("age")) {
                                        age_val = age_json.getString("age");
                                    } else {
                                        age_val = "";
                                    }

                                    if (age_json.has("gender")) {
                                        gender_val = age_json.getString("gender");
                                    } else {
                                        gender_val = "";
                                    }

                                    //----------- get Gender age ----------------------
                                } else {
                                    age_val = "";
                                    gender_val = "";
                                }

                                //------------ Get Array -----------------------------------
                                String array_text = jsononjsec.getString("ans_ext_expansion");
                                String array_text_query = jsononjsec.getString("query_ext_expansion");

                                System.out.println("array_text_ans-----" + array_text);
                                System.out.println("array_text_query-----" + array_text_query);

                                //-------------- query Key -----------------------------
                                JSONObject query_arrayJSONObj = new JSONObject(array_text_query);
                                Iterator<String> query_arrayiterator = query_arrayJSONObj.keys();
                                while (query_arrayiterator.hasNext()) {
                                    String query_key = query_arrayiterator.next();
                                    String value_of_key = query_arrayJSONObj.optString(query_key);

                                    System.out.println("query_key------------------" + query_key);
                                    System.out.println("value_of_key------------------" + value_of_key);

                                    extra_query_map.put(query_key, value_of_key);
                                }
                                //-------------- query Key -----------------------------

                                //-------------- Ans Key -----------------------------
                                JSONObject ans_arrayJSONObj = new JSONObject(array_text);
                                Iterator<String> ans_arrayiterator = ans_arrayJSONObj.keys();
                                while (ans_arrayiterator.hasNext()) {
                                    String ans_key = ans_arrayiterator.next();
                                    String ans_value_of_key = ans_arrayJSONObj.optString(ans_key);

                                    System.out.println("ans_key------------------" + ans_key);
                                    System.out.println("ans_value_of_key------------------" + ans_value_of_key);

                                    extra_ans_map.put(ans_key, ans_value_of_key);
                                }
                                //-------------- Ans Key -----------------------------

                                //------------ Get Array -----------------------------------
                                System.out.println("ftrack_str------------" + ftrack_str);
                                System.out.println("qid----" + qid);
                                System.out.println("pqid----" + pqid);
                                System.out.println("question------------" + question);
                                System.out.println("created_at------------" + created_at);
                                System.out.println("question_ext------------" + question_ext);
                                System.out.println("q_files_text------------" + q_files_text);
                                System.out.println("answer------------" + answer);

                                //------------------------------------------------
                                if (jsononjsec.has("str_follow_fee")) {
                                    str_follow_fee = jsononjsec.getString("str_follow_fee");
                                    //tvaskquery.setText("Submit your Query (" + str_follow_fee + ")");
                                    tvqfee.setText("(" + str_follow_fee + ")");

                                    System.out.println("str_follow_fee----" + str_follow_fee);
                                } else {
                                    System.out.println("No str_follow_fee----" + str_follow_fee);
                                    // str_follow_fee = "";
                                    //tvaskquery.setText("");ftrack_desc
                                }
                                //------------------------------------------------

                                //---------------------------------------------------------
                                if (jsononjsec.has("str_status")) {
                                    ftrack_str_status = jsononjsec.getString("str_status");
                                    if (!ftrack_str_status.equals("")) {
                                        Model.ftrack_str_status = ftrack_str_status;
                                        str_status_layout.setVisibility(View.VISIBLE);
                                        ftrack_text.setText(Html.fromHtml(ftrack_str_status));
                                    } else {
                                        ftrack_str_status = "";
                                        str_status_layout.setVisibility(View.GONE);
                                    }

                                    System.out.println("ftrack_str_status----" + ftrack_str_status);

                                } else {
                                    ftrack_str_status = "";
                                    str_status_layout.setVisibility(View.GONE);

                                    System.out.println("ftrack_str_status----" + ftrack_str_status);
                                }
                                //---------------------------------------------------------

                                //---------------------------------------------------------
                                if (jsononjsec.has("status")) {
                                    qastatus = jsononjsec.getString("status");
                                    Model.querystatus = qastatus;

                                    System.out.println("qastatus------------------" + qastatus);

                                } else {
                                    qastatus = "";
                                }
                                //---------------------------------------------------------

                                //---------------------------------------------------------
                                if (jsononjsec.has("unpaid")) {
                                    unpaid_json_text = jsononjsec.getString("unpaid");
                                    System.out.println("unpaid_json_text-----" + unpaid_json_text);

                                    //---------------------------------------------------------
                                    if ((unpaid_json_text.length()) > 2) {
                                        JSONObject unpaid_qext_json = new JSONObject(unpaid_json_text);
                                        System.out.println("unpaid_qext_json-----" + unpaid_qext_json.toString());
                                        unpaid_fee = unpaid_qext_json.getString("fee");
                                        inv_strfee = unpaid_qext_json.getString("fee");
                                        unpaid_invid = unpaid_qext_json.getString("inv_id");
                                        inv_id = unpaid_qext_json.getString("inv_id");
                                        btn_paynow.setText("Pay Now (" + unpaid_fee + ")");

                                        System.out.println("inv_strfee----------" + inv_strfee);
                                        System.out.println("inv_id----------" + inv_id);
                                    }
                                }

                                //---------------------------------------------------------
                                vi = getLayoutInflater().inflate(R.layout.thread_view_query, null);

                                query_display_layout = vi.findViewById(R.id.query_display_layout);
                                query_webview = vi.findViewById(R.id.query_webview);

                                query_webview.setVerticalScrollBarEnabled(false);


                                files_layout = vi.findViewById(R.id.files_layout);
                                layout_attachfile = vi.findViewById(R.id.layout_attachfile);
                                quest_layout = vi.findViewById(R.id.quest_layout);
                                qns_layout = vi.findViewById(R.id.qns_layout);
                                extra_layout = vi.findViewById(R.id.extra_layout);
                                //qns_layout = (LinearLayout) vi.findViewById(R.id.qns_layout);
                                tv_patq = vi.findViewById(R.id.pq);
                                tv_cons_for = vi.findViewById(R.id.tv_cons_for);
                                tv_filename = vi.findViewById(R.id.tv_filename);
                                tv_qid_value = vi.findViewById(R.id.tv_qid_value);

                                android.widget.TextView attachfile = vi.findViewById(R.id.attachfile);
                                android.widget.TextView tv_datetime = vi.findViewById(R.id.tv_datetime);

                                tv_patq.setTypeface(font_reg);
                                tv_cons_for.setTypeface(font_reg);

                                tv_datetime.setTypeface(font_reg);
                                attachfile.setTypeface(font_bold);


                                if (jsononjsec.has("strHtmlQus")) {
                                    strHtmlQus_text = jsononjsec.getString("strHtmlQus");

                                    query_display_layout.setVisibility(View.GONE);
                                    query_webview.setVisibility(View.VISIBLE);

                                    query_webview.getSettings().setJavaScriptEnabled(true);
                                    query_webview.setBackgroundColor(Color.TRANSPARENT);
                                    query_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                    query_webview.loadDataWithBaseURL("", strHtmlQus_text, "text/html", "UTF-8", "");
                                    query_webview.setLongClickable(false);


                                } else {

                                    query_display_layout.setVisibility(View.VISIBLE);
                                    query_webview.setVisibility(View.GONE);
                                }

                                question = question.replaceAll("\r\n", "");
                                //tv_patq.setAutoLinkMask(Linkify.ALL);

                                //----------------consulted_for--------------------------------
                                if (consulted_for_text != null && !consulted_for_text.isEmpty() && !consulted_for_text.equals("null") && !consulted_for_text.equals("")) {
                                    tv_cons_for.setText("Consulted for : " + consulted_for_text + ", " + age_val + ", " + gender_val);
                                    tv_cons_for.setVisibility(View.VISIBLE);
                                } else {
                                    tv_cons_for.setVisibility(View.GONE);
                                }
                                //----------------consulted_for--------------------------------

                                tv_patq.setText(question);
                                tv_patq.setMovementMethod(LinkMovementMethod.getInstance());
                                tv_datetime.setText(created_at);


                                //---------------- Files ---------------------------------------
                                if ((q_files_text.length()) > 2) {

                                    layout_attachfile.setVisibility(View.VISIBLE);

                                    System.out.println("files_text------" + q_files_text);
                                    jarray_files = jsononjsec.getJSONArray("q_files");

                                    JSONArray jarray_files_count = jsononjsec.getJSONArray("q_files");

                                    System.out.println("jsonobj_items------" + jarray_files.toString());
                                    System.out.println("jarray_files.length()------" + jarray_files.length());

                                    Integer cnt = jarray_files_count.length();
                                    System.out.println("cnt---------" + cnt);

                                    attachfile.setText("Attached  " + jarray_files_count.length() + " File(s)");
                                    System.out.println("Count--------" + jarray_files_count.length());

                                    attach_file_text = "";

                                    for (int m = 0; m < jarray_files.length(); m++) {
                                        jsonobj_files = jarray_files.getJSONObject(m);

                                        System.out.println("jsonobj_files--" + m + " ----" + jsonobj_files.toString());

                                        file_user_id = jsonobj_files.getString("user_id");
                                        file_doctype = jsonobj_files.getString("doctype");
                                        file_file = jsonobj_files.getString("file");
                                        file_ext = jsonobj_files.getString("ext");
                                        file_url = jsonobj_files.getString("file_url");

                                        //------------------------ File Attached Text --------------------------------
                                        if (attach_file_text.equals("")) {
                                            attach_file_text = file_url;
                                            System.out.println("attach_file_text-------" + attach_file_text);
                                        } else {
                                            attach_file_text = attach_file_text + "###" + file_url;
                                            System.out.println("attach_file_text-------" + attach_file_text);
                                        }
                                        //------------------------ File Attached Text --------------------------------

                                        System.out.println("file_user_id--------" + file_user_id);
                                        System.out.println("file_doctype--------" + file_doctype);
                                        System.out.println("filename--------" + file_file);
                                        System.out.println("file_ext--------" + file_ext);
                                        System.out.println("file_url--------" + file_url);


                                        vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                                        ImageView file_image = vi_files.findViewById(R.id.file_image);
                                        //tv_filename = (TextView) vi_files.findViewById(R.id.tv_filename);
                                        tv_ext = vi_files.findViewById(R.id.tv_ext);
                                        tv_userid = vi_files.findViewById(R.id.tv_userid);

                                        //tv_filename.setText(Html.fromHtml(file_url));
                                        //tv_filename.setText(attach_file_text);

                                        System.out.println("Final attach_file_text-------" + attach_file_text);

                                        tv_ext.setText(file_ext);
                                        tv_userid.setText(file_user_id);

                                        files_layout.addView(vi_files);
                                    }

                                    tv_filename.setText(q_files_text);
                                    tv_qid_value.setText(qid);

                                    files_layout.setVisibility(View.GONE);

                                } else {
                                    layout_attachfile.setVisibility(View.GONE);
                                }
                                //---------------- Files---------------------------------------


                                if ((Model.querystatus).equals("answered")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));

                                    btn_askfollup.setVisibility(View.VISIBLE);
                                    Submit_layout.setVisibility(View.VISIBLE);
                                    ////btn_cancel2.setVisibility(View.VISIBLE);
                                    pay_layout.setVisibility(View.GONE);
                                    ////btn_cancel1.setVisibility(View.GONE);
                                    ftrack_layout.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    edt_query_layout.setVisibility(View.VISIBLE);
                                    // hide_send();

                                } else if ((Model.querystatus).equals("edit")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.GONE);
                                    Submit_layout.setVisibility(View.GONE);
                                    ////btn_cancel2.setVisibility(View.GONE);
                                    pay_layout.setVisibility(View.VISIBLE);
                                    ////btn_cancel1.setVisibility(View.VISIBLE);
                                    ftrack_layout.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    edt_query_layout.setVisibility(View.GONE);

                                    //----- Edit text focus ------------------------------
                                    edt_query.requestFocus();
                                    edt_query.setSelection(edt_query.getText().length());
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(edt_query, InputMethodManager.SHOW_IMPLICIT);
                                    //----- Edit text focus ------------------------------
                                    //show_send();

                                } else if ((Model.querystatus).equals("unpaid")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.GONE);
                                    Submit_layout.setVisibility(View.GONE);
                                    ////btn_cancel2.setVisibility(View.GONE);
                                    pay_layout.setVisibility(View.VISIBLE);
                                    ////btn_cancel1.setVisibility(View.VISIBLE);
                                    ftrack_layout.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    edt_query_layout.setVisibility(View.GONE);
                                    //hide_send();

                                } else if ((Model.querystatus).equals("pending_review")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.GONE);
                                    Submit_layout.setVisibility(View.GONE);
                                    //btn_cancel2.setVisibility(View.GONE);
                                    pay_layout.setVisibility(View.GONE);
                                    //btn_cancel1.setVisibility(View.GONE);
                                    ftrack_layout.setVisibility(View.VISIBLE);
                                    note_layout.setVisibility(View.GONE);
                                    edt_query_layout.setVisibility(View.GONE);
                                    //hide_send();

                                } else if ((Model.querystatus).equals("new")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.GONE);
                                    Submit_layout.setVisibility(View.GONE);
                                    //btn_cancel2.setVisibility(View.GONE);
                                    pay_layout.setVisibility(View.GONE);
                                    //btn_cancel1.setVisibility(View.GONE);
                                    ftrack_layout.setVisibility(View.VISIBLE);
                                    note_layout.setVisibility(View.GONE);
                                    edt_query_layout.setVisibility(View.GONE);
                                    //hide_send();

                                } else if ((Model.querystatus).equals("spam")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.VISIBLE);
                                    Submit_layout.setVisibility(View.VISIBLE);

                                    //btn_cancel2.setVisibility(View.VISIBLE);
                                    ftrack_layout.setVisibility(View.GONE);
                                    pay_layout.setVisibility(View.GONE);
                                    //btn_cancel1.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    edt_query_layout.setVisibility(View.GONE);
                                    //hide_send();

                                } else if ((Model.querystatus).equals("8")) {

                                    System.out.println("Model.querystatus----------" + (Model.querystatus));

                                    //btn_cancel2.setVisibility(View.VISIBLE);
                                    btn_askfollup.setVisibility(View.VISIBLE);
                                    Submit_layout.setVisibility(View.VISIBLE);

                                    ftrack_layout.setVisibility(View.GONE);
                                    pay_layout.setVisibility(View.GONE);
                                    //btn_cancel1.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    edt_query_layout.setVisibility(View.VISIBLE);

                                    //-----------------------------------------------------
                                    try {
                                        getSupportActionBar().setTitle("Query in Draft");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //-----------------------------------------------------

                                    if (qid != null && !qid.isEmpty() && !qid.equals("") && !qid.equals("null") && !qid.equals("")) {

                                        //--------------------------------------------------------
                                        String url = Model.BASE_URL + "sapp/loadQbyId" + "?qid=" + qid + "&user_id=" + (Model.id) + "&token=" + Model.token + "&enc=1";
                                        System.out.println("loadQbyIdurl---" + url);
                                        new JSON_get_Query().execute(url);
                                        //--------------------------------------------------------

                                    } else {
                                        edt_query.setText("");
                                    }

                                    //----- Edit text focus ------------------------------
                                    edt_query.requestFocus();
                                    edt_query.setSelection(edt_query.getText().length());
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(edt_query, InputMethodManager.SHOW_IMPLICIT);
                                    //show_send();
                                    //----- Edit text focus ------------------------------


                                } else if ((Model.querystatus).equals("report_request")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.VISIBLE);
                                    Submit_layout.setVisibility(View.VISIBLE);

                                    //btn_cancel2.setVisibility(View.VISIBLE);
                                    pay_layout.setVisibility(View.GONE);
                                    //btn_cancel1.setVisibility(View.GONE);
                                    ftrack_layout.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    //hide_send();

                                } else if ((Model.querystatus).equals("6")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.VISIBLE);
                                    //btn_cancel2.setVisibility(View.VISIBLE);
                                    pay_layout.setVisibility(View.GONE);
                                    //btn_cancel1.setVisibility(View.GONE);
                                    ftrack_layout.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    //hide_send();

                                } else if ((Model.querystatus).equals("rejected")) {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.VISIBLE);
                                    //btn_cancel2.setVisibility(View.VISIBLE);
                                    pay_layout.setVisibility(View.GONE);
                                    //btn_cancel1.setVisibility(View.GONE);
                                    ftrack_layout.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    //hide_send();
                                } else {
                                    System.out.println("Model.querystatus----------" + (Model.querystatus));
                                    btn_askfollup.setVisibility(View.VISIBLE);
                                    //btn_cancel2.setVisibility(View.VISIBLE);
                                    pay_layout.setVisibility(View.GONE);
                                    //btn_cancel1.setVisibility(View.GONE);
                                    ftrack_layout.setVisibility(View.GONE);
                                    note_layout.setVisibility(View.GONE);
                                    //hide_send();
                                }

                                if ((ftrack_str.length()) > 2) {

                                    ftrack_layout.setVisibility(View.VISIBLE);
                                    JSONObject ftrack_str_json = new JSONObject(ftrack_str);
                                    System.out.println("ftrack_str-----" + ftrack_str_json.toString());

                                    if (ftrack_str_json.has("str_fee")) {
                                        ftrack_fee = ftrack_str_json.getString("str_fee");

                                        String first = "Pay ";
                                        String second = " <font color='#BF2B0B'><b>" + ftrack_fee + "</b></font>";
                                        String third = " to ";
                                        String fourth = "<b>Fast Track</b> ";
                                        String fifth = "Your Query and get a Detailed Answer in <b>4</b> to <b>8</b> hours.";
                                        ftrack_desc.setText(Html.fromHtml(first + second + third + fourth + fifth));
                                    }

                                } else {
                                    ftrack_layout.setVisibility(View.GONE);
                                }
                                //---------- Fast Track -------------------------------------------------------------

                                extra_layout.removeAllViews();

                                if ((question_ext.length()) > 2) {


                                    //-------------- Dynamic Key -----------------------------
                                    JSONObject categoryJSONObj = new JSONObject(question_ext);
                                    System.out.println("question_ext-----" + question_ext);

                                    Iterator<String> iterator = categoryJSONObj.keys();
                                    while (iterator.hasNext()) {
                                        String key = iterator.next();
                                        String value_of_key = categoryJSONObj.optString(key);

                                        //Log.i("TAG", "key:" + key + "--Value::" + categoryJSONObj.optString(key));
                                        System.out.println("Key------------------" + key);
                                        System.out.println("Value------------------" + value_of_key);

                                        if (!value_of_key.equals("")) {

                                            vi_ext = getLayoutInflater().inflate(R.layout.extra_answer_row, null);
                                            android.widget.TextView tv_ext_title = vi_ext.findViewById(R.id.tv_ext_title);
                                            tv_ext_desc = vi_ext.findViewById(R.id.tv_ext_desc);


                                            String GET_KEY = extra_query_map.get(key);
                                            System.out.println("keykeykey---------------" + key);
                                            System.out.println("GET_KEY---------------" + GET_KEY);


                                            tv_ext_title.setText(GET_KEY);

                                            tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));
                                            tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());

                                            extra_layout.addView(vi_ext);
                                        }


                                    }
                                    //-------------- Dynamic Key -----------------------------


                                    quest_layout.setVisibility(View.VISIBLE);
                                    qns_layout.setVisibility(View.GONE);

                                    myLayout.addView(vi);

                                } else {

                                    quest_layout.setVisibility(View.VISIBLE);
                                    qns_layout.setVisibility(View.GONE);

                                    if (qastatus.equals("8")) {
                                        // btn_askfollup.setVisibility(View.VISIBLE);

                                    } else {
                                        // btn_askfollup.setVisibility(View.GONE);
                                        myLayout.addView(vi);
                                    }
                                }

                                if ((answer.length()) > 2) {

                                    ansjsonarray = jsononjsec.getJSONArray("answer");

                                    for (int k = 0; k < ansjsonarray.length(); k++) {

                                        prob_caus = "";
                                        inv_done = "";
                                        diff_diag = "";
                                        prob_diag = "";
                                        treat_plan = "";
                                        prev_measu = "";
                                        reg_folup = "";
                                        answer_text = "";

                                        vi_ans = getLayoutInflater().inflate(R.layout.thread_view_answer, null);

                                        webview_answer = vi_ans.findViewById(R.id.webview_answer);

                                        webview_answer.setVerticalScrollBarEnabled(false);

                                        answer_layout_attachfile = vi_ans.findViewById(R.id.answer_layout_attachfile);
                                        answer_files_layout = vi_ans.findViewById(R.id.answer_files_layout);

                                        doc_layout = vi_ans.findViewById(R.id.doc_layout);
                                        ans_extra_layout = vi_ans.findViewById(R.id.ans_extra_layout);
                                        android.widget.TextView answer_attachfile = vi_ans.findViewById(R.id.answer_attachfile);
                                        android.widget.TextView tvt_query_id = vi_ans.findViewById(R.id.tvt_query_id);
                                        android.widget.TextView tvt_answer_id = vi_ans.findViewById(R.id.tvt_answer_id);
                                        android.widget.TextView tvt_query_id2 = vi_ans.findViewById(R.id.tvt_query_id2);
                                        android.widget.TextView tvt_answer_id2 = vi_ans.findViewById(R.id.tvt_answer_id2);
                                        android.widget.TextView tv_docans = vi_ans.findViewById(R.id.docans);
                                        android.widget.TextView tv_datetimeans = vi_ans.findViewById(R.id.tv_datetime);
                                        android.widget.TextView tvdocspec = vi_ans.findViewById(R.id.tvdocspec);
                                        final android.widget.TextView tvdocname = vi_ans.findViewById(R.id.tvdocname);
                                        android.widget.TextView tvdocid = vi_ans.findViewById(R.id.tvdocid);
                                        LinearLayout ratting_layout = vi_ans.findViewById(R.id.ratting_layout);

                                        Button btn_prescription = vi_ans.findViewById(R.id.btn_prescription);
                                        TextView tv_cuurent_query_id = (TextView) vi_ans.findViewById(R.id.tv_cuurent_query_id);
                                        LinearLayout prescribe_view_layout = (LinearLayout) vi_ans.findViewById(R.id.prescribe_view_layout);




                                        RatingBar ratingBar1 = vi_ans.findViewById(R.id.ratingBar1);
                                        RatingBar ratingBar2 = vi_ans.findViewById(R.id.ratingBar2);
                                        RatingBar ratingBar3 = vi_ans.findViewById(R.id.ratingBar3);
                                        RatingBar ratingBar4 = vi_ans.findViewById(R.id.ratingBar4);

                                        android.widget.TextView text_ratting1 = vi_ans.findViewById(R.id.text_ratting1);
                                        android.widget.TextView text_ratting2 = vi_ans.findViewById(R.id.text_ratting2);
                                        android.widget.TextView text_ratting3 = vi_ans.findViewById(R.id.text_ratting3);
                                        android.widget.TextView text_ratting4 = vi_ans.findViewById(R.id.text_ratting4);


                                        doc_photo = vi_ans.findViewById(R.id.doc_photo);
                                        Button btnaskquery = vi_ans.findViewById(R.id.btnaskquery);
                                        like_layout = vi_ans.findViewById(R.id.like_layout);
                                        dislike_layout = vi_ans.findViewById(R.id.dislike_layout);
                                        feedback_section = vi_ans.findViewById(R.id.feedback_section);
                                        tv_patfeedback = vi_ans.findViewById(R.id.tv_patfeedback);
                                        tv_replytext = vi_ans.findViewById(R.id.tv_replytext);
                                        doctor_reply_section = vi_ans.findViewById(R.id.doctor_reply_section);
                                        like_layout_par = vi_ans.findViewById(R.id.like_layout_par);
                                        tv_ans_filename = vi_ans.findViewById(R.id.tv_filename);

                                        tv_qid_value = vi_ans.findViewById(R.id.tv_qid_value);

                                        System.out.println("Answer_Section-----");

                                        tv_docans.setTypeface(font_reg);
                                        tv_datetimeans.setTypeface(font_reg);

                                        ansjsonobject = ansjsonarray.getJSONObject(k);
                                        System.out.println("ansjsonobject---------" + ansjsonobject.toString());

                                        answer_text = ansjsonobject.getString("answer");
                                        answerval_id = ansjsonobject.getString("id");
                                        final String qid_in_ans = ansjsonobject.getString("qid");
                                        answer_created_at = ansjsonobject.getString("created_at");
                                        doctor_str = ansjsonobject.getString("doctor");
                                        answer_ext = ansjsonobject.getString("answer_ext");
                                        String a_files_text = ansjsonobject.getString("a_files");
                                        has_prescription_val = ansjsonobject.getString("has_prescription");

                                        tv_cuurent_query_id.setText(qid_in_ans);

                                        //----------------prescription ---------------------------------------
                                        if (has_prescription_val.equals("1")) {
                                            prescribe_view_layout.setVisibility(View.VISIBLE);
                                        } else {
                                            prescribe_view_layout.setVisibility(View.GONE);
                                        }
                                        //----------------prescription ---------------------------------------

/*                                        btn_prescription.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent i = new Intent(QueryViewActivity.this, Prescriptions_Activity.class);
                                                i.putExtra("qid", qid_in_ans);
                                                startActivity(i);
                                            }
                                        });*/

                                        //---------------- Files ---------------------------------------
                                        if ((a_files_text.length()) > 5) {

                                            answer_layout_attachfile.setVisibility(View.VISIBLE);

                                            System.out.println("files_text------" + a_files_text);
                                            //jarray_files = jsononjsec.getJSONArray(a_files_text);
                                            jarray_files = new JSONArray(a_files_text);

                                            System.out.println("jsonobj_items------" + jarray_files.toString());
                                            System.out.println("jarray_files.length()------" + jarray_files.length());

                                            answer_attachfile.setText("Attached " + jarray_files.length() + " File(s)");

                                            attach_file_text = "";

                                            for (int m = 0; m < jarray_files.length(); m++) {
                                                jsonobj_files = jarray_files.getJSONObject(m);

                                                System.out.println("jsonobj_files--" + m + " ----" + jsonobj_files.toString());

                                                file_user_id = jsonobj_files.getString("user_id");
                                                file_doctype = jsonobj_files.getString("doctype");
                                                file_file = jsonobj_files.getString("file");
                                                file_ext = jsonobj_files.getString("ext");
                                                file_url = jsonobj_files.getString("file_url");

                                                //------------------------ File Attached Text --------------------------------
                                                if (attach_file_text.equals("")) {
                                                    attach_file_text = file_url;
                                                    System.out.println("attach_file_text-------" + attach_file_text);
                                                } else {
                                                    attach_file_text = attach_file_text + "###" + file_url;
                                                    System.out.println("attach_file_text-------" + attach_file_text);
                                                }
                                                //------------------------ File Attached Text --------------------------------

                                                System.out.println("file_user_id--------" + file_user_id);
                                                System.out.println("file_doctype--------" + file_doctype);
                                                System.out.println("filename--------" + file_file);
                                                System.out.println("file_ext--------" + file_ext);
                                                System.out.println("file_url--------" + file_url);


                                                vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                                                ImageView file_image = vi_files.findViewById(R.id.file_image);
                                                //tv_ans_filename = (TextView) vi_files.findViewById(R.id.tv_ans_filename);
                                                tv_ext = vi_files.findViewById(R.id.tv_ext);
                                                tv_userid = vi_files.findViewById(R.id.tv_userid);

                                                //tv_ans_filename.setText(Html.fromHtml(file_url));
                                                /*

                                                 * */
                                                //tv_ans_filename.setText(attach_file_text);

                                                System.out.println("Final attach_file_text-------" + attach_file_text);

                                                tv_ext.setText(file_ext);
                                                tv_userid.setText(file_user_id);

                                                answer_files_layout.addView(vi_files);
                                            }

                                            tv_ans_filename.setText(a_files_text);
                                            tv_qid_value.setText(qid_in_ans);

                                            answer_files_layout.setVisibility(View.GONE);

                                        } else {
                                            answer_layout_attachfile.setVisibility(View.GONE);
                                        }
                                        //---------------- Files---------------------------------------


                                        //-------- Webview ---------------------------------------------------
                                        if (ansjsonobject.has("strHtmlAns")) {
                                            strHtmlAns_text = ansjsonobject.getString("strHtmlAns");

                                            ans_extra_layout.setVisibility(View.GONE);
                                            tv_docans.setVisibility(View.GONE);
                                            webview_answer.setVisibility(View.VISIBLE);

                                            webview_answer.getSettings().setJavaScriptEnabled(true);
                                            webview_answer.setBackgroundColor(Color.TRANSPARENT);
                                            webview_answer.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                            webview_answer.loadDataWithBaseURL("", strHtmlAns_text, "text/html", "UTF-8", "");
                                            webview_answer.setLongClickable(false);

                                        } else {

                                            webview_answer.setVisibility(View.GONE);
                                            ans_extra_layout.setVisibility(View.VISIBLE);
                                            tv_docans.setVisibility(View.VISIBLE);
                                        }


                                        //--------------- patient Feedback ------------------------------
                                        if (ansjsonobject.has("arr_feedback")) {
                                            arr_feedback_text = ansjsonobject.getString("arr_feedback");

                                            System.out.println("arr_feedback_text.length()----------------" + arr_feedback_text.length());

                                            if (arr_feedback_text.length() > 2) {

                                                JSONObject jobject = new JSONObject(arr_feedback_text);
                                                System.out.println("arr_feedback_text---------" + arr_feedback_text);

                                                rating_text = jobject.getString("rating");
                                                pat_feedback_text = jobject.getString("feedback");
                                                reply_text = jobject.getString("reply");

                                                if (jobject.has("is_star")) {

                                                    is_star_val = jobject.getString("is_star");

                                                    if (is_star_val.equals("1")) {

                                                        ratting_layout.setVisibility(View.VISIBLE);

                                                        //---------- Ratting Star Text---------------------------
                                                        String star1_text = jobject.getString("star1");

                                                        if (star1_text != null && !star1_text.isEmpty() && !star1_text.equals("null") && !star1_text.equals("")) {

                                                            text_ratting1.setVisibility(View.VISIBLE);
                                                            ratingBar1.setVisibility(View.VISIBLE);

                                                            JSONObject json_star1 = new JSONObject(star1_text);
                                                            String star_title = json_star1.getString("title");
                                                            String star_value = json_star1.getString("value");

                                                            ratingBar1.setRating(Float.parseFloat(star_value));
                                                            text_ratting1.setText(star_title);
                                                        } else {
                                                            text_ratting1.setVisibility(View.GONE);
                                                            ratingBar1.setVisibility(View.GONE);
                                                        }
                                                        //---------------------------------------------------

                                                        //---------- Ratting Star Text---------------------------
                                                        String star2_text = jobject.getString("star2");

                                                        if (star2_text != null && !star2_text.isEmpty() && !star2_text.equals("null") && !star2_text.equals("")) {

                                                            text_ratting2.setVisibility(View.VISIBLE);
                                                            ratingBar2.setVisibility(View.VISIBLE);

                                                            JSONObject json_star2 = new JSONObject(star2_text);
                                                            String star_title = json_star2.getString("title");
                                                            String star_value = json_star2.getString("value");

                                                            ratingBar2.setRating(Float.parseFloat(star_value));
                                                            text_ratting2.setText(star_title);
                                                        } else {
                                                            text_ratting2.setVisibility(View.GONE);
                                                            ratingBar2.setVisibility(View.GONE);
                                                        }
                                                        //---------------------------------------------------

                                                        //---------- Ratting Star Text---------------------------
                                                        String star3_text = jobject.getString("star3");

                                                        if (star3_text != null && !star3_text.isEmpty() && !star3_text.equals("null") && !star3_text.equals("")) {

                                                            text_ratting3.setVisibility(View.VISIBLE);
                                                            ratingBar3.setVisibility(View.VISIBLE);

                                                            JSONObject json_star3 = new JSONObject(star3_text);
                                                            String star_title = json_star3.getString("title");
                                                            String star_value = json_star3.getString("value");

                                                            ratingBar3.setRating(Float.parseFloat(star_value));
                                                            text_ratting3.setText(star_title);
                                                        } else {
                                                            text_ratting3.setVisibility(View.GONE);
                                                            ratingBar3.setVisibility(View.GONE);
                                                        }
                                                        //---------------------------------------------------

                                                        //---------- Ratting Star Text---------------------------
                                                        String star4_text = jobject.getString("star4");

                                                        if (star4_text != null && !star4_text.isEmpty() && !star4_text.equals("null") && !star4_text.equals("") && star4_text.length() > 2) {

                                                            text_ratting4.setVisibility(View.VISIBLE);
                                                            ratingBar4.setVisibility(View.VISIBLE);

                                                            JSONObject json_star4 = new JSONObject(star4_text);
                                                            String star_title = json_star4.getString("title");
                                                            String star_value = json_star4.getString("value");

                                                            ratingBar4.setRating(Float.parseFloat(star_value));
                                                            text_ratting4.setText(star_title);
                                                        } else {
                                                            text_ratting4.setVisibility(View.GONE);
                                                            ratingBar4.setVisibility(View.GONE);
                                                        }
                                                        //---------------------------------------------------


                                                    } else {
                                                        ratting_layout.setVisibility(View.GONE);
                                                    }

                                                } else {
                                                    ratting_layout.setVisibility(View.GONE);
                                                }

                                                feedback_section.setVisibility(View.VISIBLE);
                                                like_layout.setVisibility(View.GONE);
                                                dislike_layout.setVisibility(View.GONE);

                                                tv_patfeedback.setText(pat_feedback_text);

                                                if (reply_text != null && !reply_text.isEmpty() && !reply_text.equals("null") && !reply_text.equals("")) {
                                                    tv_replytext.setText(reply_text);
                                                    doctor_reply_section.setVisibility(View.VISIBLE);
                                                } else {
                                                    doctor_reply_section.setVisibility(View.GONE);
                                                }
                                            } else {
                                                feedback_section.setVisibility(View.GONE);
                                                like_layout_par.setVisibility(View.VISIBLE);
                                                System.out.println("arr_feedback_text.length()----------------" + arr_feedback_text.length());
                                            }

/*

                                            //-------- Feedback Webview ---------------------------------------------------
                                            if (ansjsonobject.has("strFeedback")) {
                                                String strFeedback_text = ansjsonobject.getString("strFeedback");


                                            } else {

                                            }
                                            //-------- Feedback Webview ---------------------------------------------------
*/


                                        } else {
                                            feedback_section.setVisibility(View.GONE);
                                            like_layout_par.setVisibility(View.VISIBLE);
                                        }
                                        //--------------- patient Feedback ------------------------------


                                        answer_text = answer_text.replaceAll("\r\n", "");

                                        System.out.println("answer_text-----" + answer_text);
                                        System.out.println("answer_created_at-----" + answer_created_at);
                                        System.out.println("doctor_str-----" + doctor_str);
                                        System.out.println("doctor_str-----" + doctor_str);
                                        System.out.println("answer_ext-----" + answer_ext);
                                        System.out.println("arr_feedback_text-----" + arr_feedback_text);

                                        tvt_answer_id.setText(answerval_id);
                                        tvt_answer_id2.setText(answerval_id);
                                        tv_docans.setText(answer_text);
                                        tv_docans.setMovementMethod(LinkMovementMethod.getInstance());

                                        tv_datetimeans.setText(answer_created_at);


                                        //--------------- Doctor---------------------------------------------------------------------------
                                        if ((doctor_str.length()) > 2) {
                                            //--------- Doctor --------------------------
                                            JSONObject doctor_dets_json = new JSONObject(doctor_str);

                                            doc_name = doctor_dets_json.getString("name");
                                            doctor_id = doctor_dets_json.getString("id");
                                            doc_photo_json = doctor_dets_json.getString("photo");
                                            speciality_json = doctor_dets_json.getString("speciality");

                                            System.out.println("doc_name-----" + doc_name);
                                            System.out.println("doctor_id-----" + doctor_id);
                                            System.out.println("doc_photo_json-----" + doc_photo_json);
                                            System.out.println("speciality_json-----" + speciality_json);


                                            if (speciality_json != null && !speciality_json.isEmpty() && !speciality_json.equals("null") && !speciality_json.equals("")) {
                                                tvdocspec.setText(speciality_json);
                                            } else {
                                                tvdocspec.setText("");
                                                tvdocspec.setVisibility(View.GONE);
                                            }

                                            tvt_query_id.setText(qid);
                                            tvt_query_id2.setText(qid);
                                            Picasso.with(getApplicationContext()).load(doc_photo_json).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(doc_photo);

                                            //------font -----------------------------
                                            tvdocname.setTypeface(font_bold);
                                            tvdocspec.setTypeface(font_reg);
                                            //------font -----------------------------

                                            //------------------------------------
                                            docname_hash.put(doc_name, doctor_id);
                                            tvdocname.setText(doc_name);
                                            //------------------------------------

                                           /* doc_layout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    String docid = docname_hash.get(tvdocname.getText().toString());
                                                    System.out.println("docname----------" + (tvdocname.getText().toString()));
                                                    System.out.println("docid----------" + docid);

                                                    Intent intent = new Intent(QueryViewActivity.this, DoctorProfileActivity.class);
                                                    intent.putExtra("tv_doc_id", docid);
                                                    startActivity(intent);

                                                }
                                            });*/
                                        }
                                        //--------------- Doctor END---------------------------------------------------------------------


                                        //--------------- Answer Extra ---------------------------------------------------------------------------

                                        if ((answer_ext.length()) > 2) {

                                            ans_extra_layout.removeAllViews();
                                            //-------------- Dynamic Key -----------------------------
                                            JSONObject categoryJSONObj = new JSONObject(answer_ext);
                                            Iterator<String> iterator = categoryJSONObj.keys();
                                            while (iterator.hasNext()) {
                                                String key = iterator.next();
                                                String value_of_key = categoryJSONObj.optString(key);

                                                System.out.println("Key------------------" + key);
                                                System.out.println("Value------------------" + value_of_key);

                                                if (!value_of_key.equals("")) {

                                                    vi_ans_ext = getLayoutInflater().inflate(R.layout.extra_answer_row, null);
                                                    android.widget.TextView tv_ext_title = vi_ans_ext.findViewById(R.id.tv_ext_title);
                                                    tv_ext_desc = vi_ans_ext.findViewById(R.id.tv_ext_desc);

                                                    tv_ext_title.setText(extra_ans_map.get(key));
                                                    tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));
                                                    tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());

                                                    ans_extra_layout.addView(vi_ans_ext);
                                                }
                                            }
                                            //-------------- Dynamic Key -----------------------------
                                        }

                                        like_layout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                android.widget.TextView tvt_answer_id = v.findViewById(R.id.tvt_answer_id);
                                                String ans_id_val = tvt_answer_id.getText().toString();
                                                cur_answer_id = tvt_answer_id.getText().toString();

                                                ratting_val = "5";

                                                System.out.println("ans_id_val-----------" + ans_id_val);
                                                System.out.println("qid-----------" + qid);
                                                System.out.println("Doctor ID-----------" + doctor_id);

                                                //showFeedbackDialog();

                                                Intent intent = new Intent(QueryViewActivity.this, Ratting_activity.class);
                                                intent.putExtra("qid", qid);
                                                intent.putExtra("doctor_id", doctor_id);
                                                intent.putExtra("ratting_val", ratting_val);
                                                intent.putExtra("ans_id_val", ans_id_val);
                                                startActivity(intent);

                                            }
                                        });


                                        dislike_layout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                android.widget.TextView tvt_answer_id2 = v.findViewById(R.id.tvt_answer_id2);
                                                String ans_id_val = tvt_answer_id2.getText().toString();
                                                cur_answer_id = tvt_answer_id2.getText().toString();

                                                ratting_val = "1";

                                                System.out.println("ans_id_val-----------" + ans_id_val);
                                                System.out.println("qid-----------" + qid);
                                                System.out.println("Doctor ID-----------" + doctor_id);

                                                //showFeedbackDialog();
                                                Intent intent = new Intent(QueryViewActivity.this, Ratting_activity.class);
                                                intent.putExtra("qid", qid);
                                                intent.putExtra("doctor_id", doctor_id);
                                                intent.putExtra("ratting_val", ratting_val);
                                                intent.putExtra("ans_id_val", ans_id_val);
                                                startActivity(intent);
                                            }
                                        });


                                        myLayout.addView(vi_ans);
                                    }
                                }
                            }

                        }

                    }

                    scrollview.setVisibility(View.VISIBLE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);


                    if (doctor_id != null && !doctor_id.isEmpty() && !doctor_id.equals("") && !doctor_id.equals("null") && !doctor_id.equals("")) {

                        //------------- Get ICQ50 Fees ---------------------------------------------
                        System.out.println("doctor_id---------------" + doctor_id);

                        try {

                            JSONObject json_get_icq50_fee = new JSONObject();
                            json_get_icq50_fee.put("user_id", (Model.id));
                            json_get_icq50_fee.put("doctor_id", doctor_id);
                            json_get_icq50_fee.put("item_type", "icq50");
                            json_get_icq50_fee.put("all", "0");

                            System.out.println("JSON_getFee_50---" + json_get_icq50_fee.toString());

                            new JSON_getFee_50().execute(json_get_icq50_fee);

                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        //------------- Get ICQ50 Fees ---------------------------------------------
                    } else {
                        //------------- Get ICQ50 Fees ---------------------------------------------
                        System.out.println("doctor_id---------------" + doctor_id);

                        try {

                            JSONObject json_get_icq50_fee = new JSONObject();
                            json_get_icq50_fee.put("user_id", (Model.id));
                            json_get_icq50_fee.put("item_type", "icq50");
                            json_get_icq50_fee.put("all", "1");

                            System.out.println("JSON_getFee_50---" + json_get_icq50_fee.toString());

                            new JSON_getFee_50().execute(json_get_icq50_fee);

                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        //------------- Get ICQ50 Fees ---------------------------------------------
                    }


                    //---------------- Auto Scroll Bottom -----------------------
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    };
                    scrollview.post(runnable);
                    //---------------- Auto Scroll Bottom -----------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void onClick(View v) {

        try {
            android.widget.TextView tv_filename = v.findViewById(R.id.tv_filename);
            android.widget.TextView tv_qid_value = v.findViewById(R.id.tv_qid_value);

            String file_name = tv_filename.getText().toString();
            String qid_text = tv_qid_value.getText().toString();

            String file_ext = tv_ext.getText().toString();
            String file_userid = tv_userid.getText().toString();

            System.out.println("str_filename-------" + file_name);
            System.out.println("qid_text-------" + qid_text);


/*            String url = Model.BASE_URL + "tools/downloadFile/user_id/" + (file_userid) + "/doctype/query_attachment?file=" + file_name + "&ext=" + file_ext + "&isapp=1";
            System.out.println("File url-------------" + url);*/
/*            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(file_name));
            startActivity(i);*/

/*            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);524736
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

            Intent i = new Intent(QueryViewActivity.this, ExpandableActivity.class);
            i.putExtra("item_id", qid_text);
            i.putExtra("item_type", "query");
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doc_prof(View v) {

        try {
            android.widget.TextView tvdocid = v.findViewById(R.id.tvdocid);
            String tvdocid_val = tvdocid.getText().toString();

            System.out.println("tvdocid_val-------" + tvdocid_val);

         /*   Intent intent = new Intent(QueryViewActivity.this, DoctorProfileActivity.class);
            intent.putExtra("tv_doc_id", tvdocid_val);
            startActivity(intent);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }

    public void showFeedbackDialog() {

        try {
            final MaterialDialog alert = new MaterialDialog(QueryViewActivity.this);
            View view = LayoutInflater.from(QueryViewActivity.this).inflate(R.layout.ask_feedback, null);
            alert.setView(view);

            alert.setTitle("Feedback");

            final EditText edt_coupon = view.findViewById(R.id.edt_coupon);

            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    feedback_text = edt_coupon.getText().toString();

                    if (!(feedback_text.equals(""))) {
                        try {
                            feedback_json = new JSONObject();
                            feedback_json.put("feedback", feedback_text);
                            feedback_json.put("user_id", (Model.id));
                            feedback_json.put("qid", qid);
                            feedback_json.put("answer_id", answerval_id);
                            feedback_json.put("user_2_rate", (doctor_id));
                            feedback_json.put("rating", ratting_val);

                            System.out.println("feedback_json----------" + feedback_json.toString());

                            new JSON_like_feedback().execute(feedback_json);

                            alert.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        edt_coupon.setError("Please enter the feedback");
                        edt_coupon.requestFocus();
                    }
                }
            });

            alert.setNegativeButton("CANCEL", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_like_feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(QueryViewActivity.this);
            dialog.setMessage("Sending feedback.., please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                feedback_jsonobj = jParser.JSON_POST(urls[0], "postFeedback");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (feedback_jsonobj.has("token_status")) {
                    String token_status = feedback_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(QueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    feedback_status = feedback_jsonobj.getString("status");
                    System.out.println("feedback_status----------" + feedback_status);


                    like_layout_par.setVisibility(View.GONE);
                    feedback_section.setVisibility(View.VISIBLE);
                    doctor_reply_section.setVisibility(View.GONE);

                    tv_patfeedback.setText(pat_feedback_text);


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.feedback", feedback_text);
                    articleParams.put("android.patient.patient_id", (Model.id));
                    articleParams.put("android.patient.qid", qid);
                    articleParams.put("android.patient.answer_id", cur_answer_id);
                    articleParams.put("android.patient.doctor_id", (doctor_id));
                    articleParams.put("android.patient.ratting", ratting_val);
                    FlurryAgent.logEvent("android.patient.Answer_Feedback", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("qid", qid);
                    params.putString("doctor_id", doctor_id);
                    params.putString("ratting", ratting_val);
                    Model.mFirebaseAnalytics.logEvent("Answer_Feedback", params);
                    //------------ Google firebase Analitics---------------------------------------------

                    //--------------------------
                    String full_url = Model.BASE_URL + "sapp/viewq?id=" + qid + "&user_id=" + (Model.id) + "&format=json&token=" + Model.token + "&enc=1&isAFiles=1";
                    System.out.println("Viewq url-------------" + full_url);
                    new Query_View_AsyncTask().execute(full_url);
                    //--------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

    class JSON_get_Query extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(QueryViewActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj = jParser.getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj.has("query")) {
                    query_draft_text = jsonobj.getString("query");
                    System.out.println("Load Draft query_draft_text----" + query_draft_text);

                    if (query_draft_text != null && !query_draft_text.isEmpty() && !query_draft_text.equals("") && !query_draft_text.equals("null")) {
                        edt_query.setText(Html.fromHtml(query_draft_text));
                        edt_title.setText("Edit and Send Your Reply");

                        System.out.println("Load Draft-----");

                        btn_askfollup.setVisibility(View.VISIBLE);
                        ftrack_layout.setVisibility(View.GONE);
                        pay_layout.setVisibility(View.GONE);
                        note_layout.setVisibility(View.GONE);
                        edt_query_layout.setVisibility(View.VISIBLE);
                    }
                }

            } catch (Exception e) {
                edt_query.setText("");
                System.out.println("Exception query_draft_text----");
                e.printStackTrace();
            }

            dialog.cancel();
        }

    }

    public void force_logout() {


        //----------- Flurry -------------------------------------------------
        Map<String, String> articleParams = new HashMap<String, String>();
        articleParams.put("android.patient.country:", (Model.browser_country));
        FlurryAgent.logEvent("android.patient.Force_Logout", articleParams);
        //----------- Flurry -------------------------------------------------

        final MaterialDialog alert = new MaterialDialog(QueryViewActivity.this);
        alert.setTitle("Please Re-Login the App..!");
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
                Intent i = new Intent(QueryViewActivity.this, LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });
        alert.show();
    }

    public void submit_query() {


        try {
            query_str = edt_query.getText().toString();

            System.out.println("query_txt-------------" + query_str);
            System.out.println("doctor_id-------------" + doctor_id);
            System.out.println("pqid-------------" + pqid);
            System.out.println("qid-------------" + qid);
            System.out.println("qtype-------------" + qtype);

            System.out.println("query_str Length-------------" + query_str.length());

            if ((query_str.length()) > 0) {
                query_txt = URLEncoder.encode((edt_query.getText().toString()), "UTF-8");

                json_followup = new JSONObject();
                json_followup.put("query", query_txt);
                json_followup.put("speciality", "0");
                json_followup.put("doctor_id", doctor_id);
                json_followup.put("pqid", pqid); //Previous Query ID

                if (qtype.equals("draft")) {
                    json_followup.put("qid", qid);
                    System.out.println("Draft Followup json-------------" + json_followup.toString());
                } else {
                    json_followup.put("qid", "0");
                    System.out.println("Normal Followup json-------------" + json_followup.toString());
                }

                System.out.println("Followup-------------");
                new JSONPostQuery().execute(json_followup);

            } else
                edt_query.setError("Please enter your query");
            edt_query.requestFocus();

        } catch (Exception e) {
            System.out.println("Exception Followup ---- " + e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //------------------ Button Animation -------------------------
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanim);
        btn_pay.startAnimation(shake);
        btn_ftrack.startAnimation(shake);
        //------------------ Button Animation -------------------------
    }

/*    public void onClickFile(View v) {

        try {
            TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();
            System.out.println("str_filename-------" + file_name);

            //---------------------------
            //Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            Intent i = new Intent(getApplicationContext(), Attached_List_Activity.class);
            i.putExtra("filetxt", jarray_files.toString());
            i.putExtra("qid", qid);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            //---------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void onClickFile(View v) {

        try {
            android.widget.TextView tv_filename = v.findViewById(R.id.tv_filename);
            android.widget.TextView tv_qid_value = v.findViewById(R.id.tv_qid_value);

            String file_name = tv_filename.getText().toString();
            String qid_text = tv_qid_value.getText().toString();

            String file_ext = tv_ext.getText().toString();
            String file_userid = tv_userid.getText().toString();

            System.out.println("str_filename-------" + file_name);
            System.out.println("qid_text-------" + qid_text);

            Intent i = new Intent(QueryViewActivity.this, ExpandableActivity.class);
            i.putExtra("item_id", qid_text);
            i.putExtra("item_type", "query");
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void onClickFile_answer(View v) {

        try {

            android.widget.TextView tv_filename = v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();
            System.out.println("str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    public void onTextLinkClick(View textView, String clickedString) {
        //android.util.Log.v('Hyperlink clicked is :: ' + clickedString, 'Hyperlink clicked is :: ' + clickedString);
        System.out.println("clickedString--------" + clickedString);
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        System.out.println("Scrolling----------------------" + scrollState);
    }


    private class JSON_getFee_50 extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(QueryViewActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }


        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj_icq50 = jParser.JSON_POST(urls[0], "getFee");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.print("docprof_jsonobj---------------" + jsonobj_icq50.toString());
                if (jsonobj_icq50.has("str_fee")) {
                    fee_str_text = jsonobj_icq50.getString("str_fee");
                    tv_fee11.setText("(" + fee_str_text + ")");
                }


                if (jsonobj_icq50.has("icq50")) {

                    String icq50_text = jsonobj_icq50.getString("icq50");
                    String icq50_fees = new JSONObject(icq50_text).getString("str_fee");

                    tv_fee11.setText("(" + icq50_fees + ")");
                }

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void onclick_viewpres(View v) {

        try {
            View parent = (View) v.getParent();

            TextView cqid = (TextView) parent.findViewById(R.id.tv_cuurent_query_id);
            String cqid_val = cqid.getText().toString();
            System.out.println("cqid_val---------" + cqid_val);

            String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=query&item_id=" + cqid_val;
            System.out.println("Pressed Prescription-----------" + params);
            new list_drugs().execute(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class list_drugs extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(QueryViewActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                str_drug_dets = new JSONParser().getJSONString(params[0]);
                System.out.println("str_drug_dets--------------" + str_drug_dets);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                if (str_drug_dets != null && !str_drug_dets.isEmpty() && !str_drug_dets.equals("null") && !str_drug_dets.equals("")) {

                    JSONObject jobj = new JSONObject(str_drug_dets);

                    String status_text = jobj.getString("status");

                    if (status_text.equals("1")) {

                        if (jobj.has("strHtmlData")) {

                            String strHtmlData = jobj.getString("strHtmlData");
                            String prescPdfUrl_text = jobj.getString("prescPdfUrl");

                            System.out.println("Final_strHtmlData-----" + strHtmlData);

                            try {
                                Intent i = new Intent(getApplicationContext(), Prescription_WebViewActivity.class);
                                i.putExtra("str_html", strHtmlData);
                                i.putExtra("pdf_url", prescPdfUrl_text);
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            String msg_text = jobj.getString("msg");
                            Toast.makeText(QueryViewActivity.this, msg_text, Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





}