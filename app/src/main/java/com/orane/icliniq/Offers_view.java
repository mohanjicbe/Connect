package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class Offers_view extends AppCompatActivity {

    Toolbar toolbar;
    String offers_details, offer_id_val, inv_id, inv_fee, inv_strfee, orderId;
    LinearLayout inner_offers_list, userfeedback_layout;
    ObservableWebView webview_top;
    TextView tv_viewall;
    JSONObject jsonobj_prepinv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }


        //----------------Mohan----------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Offers View");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //----------- initialize --------------------------------------

        tv_viewall = findViewById(R.id.tv_viewall);
        userfeedback_layout = findViewById(R.id.userfeedback_layout);
        inner_offers_list = findViewById(R.id.inner_offers_list);
        webview_top = findViewById(R.id.webview_top);

        Intent intent = getIntent();
        offer_id_val = intent.getStringExtra("offer_id");

        //----------- Deals and Offers--------------------------------------------------------
        String get_family_url = Model.BASE_URL + "/sapp/viewDealsAndOffers?id=" + offer_id_val + "&user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("get_family_url---------" + get_family_url);
        new JSON_deals_offers().execute(get_family_url);
        //------------Deals and Offers-------------------------------------------------------

        tv_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Offers_view.this, Comments_ListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main_query, menu);
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


    private class JSON_deals_offers extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Offers_view.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                offers_details = jParser.getJSONString(urls[0]);

                System.out.println("Family URL---------------" + urls[0]);

                return offers_details;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            try {

                System.out.println("offers_details-------------- " + offers_details);

                JSONObject jobject_offers = new JSONObject(offers_details);

                String strhtml_data = jobject_offers.getString("strHtmlData");

                webview_top.getSettings().setJavaScriptEnabled(true);
                webview_top.loadDataWithBaseURL("", strhtml_data, "text/html", "UTF-8", "");
                webview_top.setLongClickable(false);


                String plans_text = jobject_offers.getString("plans");

                JSONArray data_text_array = new JSONArray(plans_text);

                System.out.println("data_text_array------" + data_text_array.toString());

                inner_offers_list.removeAllViews();

                for (int i = 0; i < data_text_array.length(); i++) {
                    JSONObject jsonobj_files = data_text_array.getJSONObject(i);

                    String offers_id = jsonobj_files.getString("id");
                    String title_text = jsonobj_files.getString("name");
                    String str_plan_html = jsonobj_files.getString("strPlanDet");

                    View recc_vi = getLayoutInflater().inflate(R.layout.offers_items, null);

                    LinearLayout full_layout = recc_vi.findViewById(R.id.full_layout);
                    TextView tv_offer_details = recc_vi.findViewById(R.id.tv_offer_details);
                    TextView tv_offer_id = recc_vi.findViewById(R.id.tv_offer_id);
                    TextView tv_offer_title = recc_vi.findViewById(R.id.tv_offer_title);
                    ImageView offer_img = recc_vi.findViewById(R.id.offer_img);
                    Button btn_submit = recc_vi.findViewById(R.id.btn_submit);

                    tv_offer_id.setText(Html.fromHtml(offers_id));
                    tv_offer_title.setText(Html.fromHtml(title_text));


                    tv_offer_details.setText(Html.fromHtml(str_plan_html));


                    // Picasso.with(getActivity()).load(bg_img_text).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(deal_bg);

                    boolean isEven = i % 2 == 0;

                    System.out.println("isEven-----------" + isEven);

                    if (isEven) {
                        full_layout.setBackgroundResource(R.mipmap.offer_blue_bg);
                        btn_submit.setBackgroundColor(getResources().getColor(R.color.white));
                        btn_submit.setTextColor(getResources().getColor(R.color.app_color));
                        tv_offer_title.setTextColor(getResources().getColor(R.color.white));
                        tv_offer_details.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        full_layout.setBackgroundResource(R.mipmap.offer_white_bg);
                        btn_submit.setBackgroundColor(getResources().getColor(R.color.app_color));
                        btn_submit.setTextColor(getResources().getColor(R.color.white));
                        tv_offer_title.setTextColor(getResources().getColor(R.color.black));
                        tv_offer_details.setTextColor(getResources().getColor(R.color.black));
                    }

                    inner_offers_list.addView(recc_vi);

                }


                String feedback_text = jobject_offers.getString("feedback");

                JSONArray feedback_array = new JSONArray(feedback_text);

                System.out.println("feedback_array------" + feedback_array.toString());

                userfeedback_layout.removeAllViews();


                for (int i = 1; i <= feedback_array.length(); i++) {
                    JSONObject jsonobj_feedback_files = feedback_array.getJSONObject(i - 1);

                    String feed_text = jsonobj_feedback_files.getString("feed");
                    String doc_id = jsonobj_feedback_files.getString("doc_id");
                    String doc_name = jsonobj_feedback_files.getString("doc_name");
                    String doc_img_url = jsonobj_feedback_files.getString("doc_img_url");
                    String pat_name = jsonobj_feedback_files.getString("pat_name");
                    String pat_loc = jsonobj_feedback_files.getString("pat_loc");
                    String doc_edu = jsonobj_feedback_files.getString("doc_edu");

                    View recc_vi = getLayoutInflater().inflate(R.layout.offers_feedback_row, null);

                    CircleImageView doc_img = recc_vi.findViewById(R.id.doc_img);
                    LinearLayout doc_layout = recc_vi.findViewById(R.id.doc_layout);
                    LinearLayout full_layout = recc_vi.findViewById(R.id.feedback_layout);
                    TextView tv_name = recc_vi.findViewById(R.id.tv_name);
                    TextView tv_edu = recc_vi.findViewById(R.id.tv_edu);
                    TextView tv_id = recc_vi.findViewById(R.id.tv_id);
                    TextView tv_pat_name = recc_vi.findViewById(R.id.tv_pat_name);
                    TextView tv_feedback = recc_vi.findViewById(R.id.tv_feedback);
                    TextView tv_doc_id = recc_vi.findViewById(R.id.tv_doc_id);

                    tv_name.setText(Html.fromHtml(doc_name));
                    tv_edu.setText(Html.fromHtml(doc_edu));
                    tv_pat_name.setText(Html.fromHtml(pat_name + ", " + pat_loc));
                    tv_feedback.setText(Html.fromHtml(feed_text));
                    tv_doc_id.setText(Html.fromHtml(doc_id));

                    Picasso.with(Offers_view.this).load(doc_img_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(doc_img);

                    userfeedback_layout.addView(recc_vi);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

    public void offer_submit_press(View v) {

        try {

            View parent = (View) v.getParent();
            View grand_parent = (View) parent.getParent();

            TextView tv_offer_id = grand_parent.findViewById(R.id.tv_offer_id);
            String tv_offer_id_val = tv_offer_id.getText().toString();

            System.out.println("tv_offer_id_val-------" + tv_offer_id_val);
            System.out.println("offer_id_val-------" + offer_id_val);

            //---------------------------------------
            String prep_url = (Model.BASE_URL) + "/sapp/prepareInv?user_id=" + Model.id + "&inv_for=campaign&item_id=" + tv_offer_id_val + "&campaign_id=" + offer_id_val + "&token=" + Model.token;
            System.out.println("Query2 Prepare Invoice url-------------" + prep_url);
            new JSON_Prepare_inv().execute(prep_url);
            //---------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Offers_view.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_prepinv = jParser.getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            dialog.cancel();

            System.out.println("jsonobj_prepinv--------" + jsonobj_prepinv.toString());

            Model.query_launch = "Askquery2";

            try {

                inv_id = jsonobj_prepinv.getString("id");
                inv_fee = jsonobj_prepinv.getString("fee");
                inv_strfee = jsonobj_prepinv.getString("str_fee");
                orderId = jsonobj_prepinv.getString("orderId");

                System.out.println("inv_id--------" + inv_id);
                System.out.println("inv_fee--------" + (inv_fee));
                System.out.println("inv_strfee--------" + inv_strfee);


                if (!(inv_id).equals("0")) {

                    Model.have_free_credit = "0";

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    params.putString("offer_id", offer_id_val);
                    params.putString("Invoice_id", inv_id);
                    params.putString("Invoice_fee", inv_strfee);
                    Model.mFirebaseAnalytics.logEvent("Offer_select", params);
                    //------------ Google firebase Analitics--------------------

                    //((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent intent = new Intent(Offers_view.this, Invoice_Page_New.class);
                    intent.putExtra("qid", orderId);
                    intent.putExtra("inv_id", inv_id);
                    intent.putExtra("inv_strfee", inv_strfee);
                    intent.putExtra("type", "offer");
                    startActivity(intent);
                    // finish();

                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                }

                //  finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
