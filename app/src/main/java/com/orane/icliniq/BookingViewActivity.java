package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.easing.linear.Linear;
import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.expand.ExpandableLayout;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingViewActivity extends AppCompatActivity {


    TextView tv_notify, tvdate, tvtime, tvquery, tvtz, tvlang, tvstatus;
    JSONObject jsonobj;
    public String isAttachMore_val, strHtmlData_text, file_fileUrl_text, family_list, fileCount_val, str_response, booking_id, bid, bquery, bcdate, btime, btz, bctype, blang, bstatus;
    CircleImageView imageview_poster;
    ScrollView main_layout;
    JSONObject jsonobj1;
    ObservableWebView webview;
    Button btn_attach;
    LinearLayout expand_layout;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_view);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Booking View");
        }
        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }


        webview = (ObservableWebView) findViewById(R.id.webview);
        imageview_poster = (CircleImageView) findViewById(R.id.imageview_poster);
        tv_notify = (TextView) findViewById(R.id.tv_notify);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvtime = (TextView) findViewById(R.id.tvtime);
        tvquery = (TextView) findViewById(R.id.tvquery);
        tvtz = (TextView) findViewById(R.id.tvtz);
        tvlang = (TextView) findViewById(R.id.tvlang);
        tvstatus = (TextView) findViewById(R.id.tvstatus);
        main_layout = (ScrollView) findViewById(R.id.main_layout);
        btn_attach = (Button) findViewById(R.id.btn_attach);
        expand_layout = (LinearLayout) findViewById(R.id.expand_layout);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tvdate.setTypeface(font_bold);
        tvtime.setTypeface(font_bold);
        tvtz.setTypeface(font_bold);
        tvlang.setTypeface(font_bold);
        tvquery.setTypeface(font_reg);

        ((TextView) findViewById(R.id.tv_date_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_time_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_schedule)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvstatus)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_timezone_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_lang_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_query_lab)).setTypeface(font_bold);

        try {
            FlurryAgent.onPageView();
        } catch (Exception ee) {
            System.out.println("Exception-----------" + ee.toString());
            ee.printStackTrace();
        }

        try {
            Intent intent = getIntent();
            booking_id = intent.getStringExtra("tv_booking_id");
        } catch (Exception e) {
            System.out.println("Exception--Getting--Intent--" + e.toString());
            e.printStackTrace();
        }

        full_process();



        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BookingViewActivity.this, Attachment_Screen_Activity.class);
                intent.putExtra("item_id", booking_id);
                intent.putExtra("item_type", "booking");
                startActivity(intent);

            }
        });
    }

    public void full_process() {

        try {
            if (new NetCheck().netcheck(BookingViewActivity.this)) {
                //----------------------------------------------------
                String url = Model.BASE_URL + "/sapp/viewBooking?user_id=" + (Model.id) + "&id=" + booking_id + "&format=json&token=" + Model.token + "&enc=1";
                System.out.println("url-------------" + url);
                new JSON_View_Booking().execute(url);
                //----------------------------------------------------
            } else {
                System.out.println("Internet is not Available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_View_Booking extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(BookingViewActivity.this);
            dialog.setMessage("please wait");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);

            main_layout.setVisibility(View.GONE);
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
                        Intent intent = new Intent(BookingViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    bid = jsonobj.getString("id");
                    bquery = jsonobj.getString("query");
                    bcdate = jsonobj.getString("consult_date");
                    btime = jsonobj.getString("str_time_range");
                    btz = jsonobj.getString("timezone");
                    bctype = jsonobj.getString("strConsultType");
                    blang = jsonobj.getString("language");
                    bstatus = jsonobj.getString("strStatus");
                    fileCount_val = jsonobj.getString("fileCount");
                    strHtmlData_text = jsonobj.getString("strHtmlData");
                    isAttachMore_val = jsonobj.getString("isAttachMore");

                    if (bstatus != null && !bstatus.isEmpty() && !bstatus.equals("null") && !bstatus.equals("")) {
                        tvstatus.setText(bstatus);
                    } else {
                        tvstatus.setVisibility(View.GONE);
                    }


                    if (isAttachMore_val.equals("1")) {
                        btn_attach.setVisibility(View.VISIBLE);
                    } else {
                        btn_attach.setVisibility(View.GONE);
                    }


                    tvdate.setText(bcdate);
                    tvtime.setText(btime);
                    //tvquery.setText(Html.fromHtml(bquery));

                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadDataWithBaseURL("", strHtmlData_text, "text/html", "UTF-8", "");
                    webview.setLongClickable(false);

                    tvtz.setText(btz);
                    tvlang.setText(blang);

                    //-------------------------------------------
                    if (bctype.equals("Phone")) {
                        imageview_poster.setBackgroundResource(R.mipmap.phone_cons_ico_color);
                    } else if (bctype.equals("Direct Visit")) {
                        imageview_poster.setBackgroundResource(R.mipmap.direct_walk);
                    } else {
                        imageview_poster.setBackgroundResource(R.mipmap.video_cons_ico_color);
                    }
                    //-------------------------------------------

                    //---------------------------------------------------------
                    if (bstatus.equals("Doctor not assigned yet.")) {
                        tv_notify.setVisibility(View.VISIBLE);
                    } else {
                        tv_notify.setVisibility(View.GONE);
                    }
                    //---------------------------------------------------------

                    main_layout.setVisibility(View.VISIBLE);
                    dialog.cancel();

                    //-------------------------------------------------------------------
                    String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + booking_id + "&item_type=booking&user_id=" + Model.id + "&token=" + Model.token;
                    System.out.println("get_family_url---------" + get_family_url);
                    new JSON_getFileList().execute(get_family_url);
                    //-------------------------------------------------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    @Override
    protected void onResume() {

        super.onResume();
        System.out.println("Model.upload_files--------------" + Model.upload_files);
        //file_list.removeAllViews();

        if ((Model.query_launch).equals("attached_file")) {

            //-------------------------------------------------------------------
            String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + booking_id + "&item_type=booking&user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("get_family_url---------" + get_family_url);
            new JSON_getFileList().execute(get_family_url);
            //-------------------------------------------------------------------
        }

        Model.upload_files = "";
    }

    private class JSON_getFileList extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(BookingViewActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                family_list = jParser.getJSONString(urls[0]);

                System.out.println("Family URL---------------" + urls[0]);

                return family_list;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            //apply_relaships_radio(family_list);

            try {

                System.out.println("Attached_Files_List----" + family_list);

                JSONObject jsonFileList = new JSONObject(family_list);

                String det_text = jsonFileList.getString("det");

                JSONArray det_jarray = new JSONArray(det_text);

                expand_layout.removeAllViews();


                for (int i = 0; i < det_jarray.length(); i++) {
                    jsonobj1 = det_jarray.getJSONObject(i);
                    System.out.println("jsonobj_first-----" + jsonobj1.toString());

                    String reportsLabel_text = jsonobj1.getString("reportsLabel");
                    String data_text = jsonobj1.getString("data");

                    JSONArray data_jarray = new JSONArray(data_text);

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.attachment_expand_view, null);

                    final LinearLayout expand_inner_view = (LinearLayout) addView.findViewById(R.id.expand_inner_view);
                    TextView tv_att_title = (TextView) addView.findViewById(R.id.tv_att_title);
                    final ImageView img_right_arrow = (ImageView) addView.findViewById(R.id.img_right_arrow);

                    //img_right_arrow.setImageResource(R.mipmap.down_icon);
                    tv_att_title.setText(reportsLabel_text);

                    ExpandableLayout expandableLayout = (ExpandableLayout) addView.findViewById(R.id.expandable_layout);

/*                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    expandableLayout.setBackgroundColor(color);*/


                    expandableLayout.setOnExpandListener(new ExpandableLayout.OnExpandListener() {
                        @Override
                        public void onExpand(boolean expanded) {
                            //Toast.makeText(AskQuery2.this, "expand?" + expanded, Toast.LENGTH_SHORT).show();

                            if (expanded) {
                                img_right_arrow.setImageResource(R.mipmap.up_icon);
                            } else {
                                img_right_arrow.setImageResource(R.mipmap.down_icon);
                            }

                        }
                    });

                    for (int j = 0; j < data_jarray.length(); j++) {
                        JSONObject file_jobj = data_jarray.getJSONObject(j);
                        System.out.println("jsonobj_first-----" + file_jobj.toString());

                        final String attach_id_text = file_jobj.getString("attach_id");
                        String reportDate_text = file_jobj.getString("reportDate");
                        final String fileUrl_text = file_jobj.getString("fileUrl");
                        String isDelete_text = file_jobj.getString("isDelete");
                        String reportDesc = file_jobj.getString("reportDesc");

                        //------------------------------------
                        LayoutInflater layoutInflater2 = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView2 = layoutInflater2.inflate(R.layout.attachment_expand_view2, null);

                        TextView tv_summ_title2 = (TextView) addView2.findViewById(R.id.tv_summ_title2);
                        TextView tv_desc_summary_text = (TextView) addView2.findViewById(R.id.tv_desc_summary_text);
                        Button btn_open = (Button) addView2.findViewById(R.id.btn_open);
                        Button btn_remove = (Button) addView2.findViewById(R.id.btn_remove);


                        tv_summ_title2.setText("File : " + (j + 1) + " Dated on " + reportDate_text);
                        tv_desc_summary_text.setText(reportDesc);
                        img_right_arrow.setImageBitmap(BitmapFactory.decodeFile(fileUrl_text));


                        System.out.println("attach_id_text-----------" + (attach_id_text));
                        System.out.println("reportDate_text-----------" + (reportDate_text));
                        System.out.println("isDelete_text-----------" + (isDelete_text));
                        System.out.println("reportDesc-----------" + (reportDesc));

                        //------------------------------------------
                        if (isDelete_text.equals("1")) {
                            btn_remove.setVisibility(View.VISIBLE);
                        } else {
                            btn_remove.setVisibility(View.GONE);
                        }
                        //------------------------------------------


                        btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            /*    View parent = (View) v.getParent();
                                //View grand_parent = (View)parent.getParent();

                                tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                                String attid = tv_attach_id.getText().toString();*/

                                //---------------------------
                                String url = Model.BASE_URL + "/sapp/removeHReportData?item_id=" + booking_id + "&item_type=booking&attach_id=" + attach_id_text + "&user_id=" + (Model.id) + "&attach_id=" + attach_id_text + "&token=" + Model.token;
                                System.out.println("Remover Attach url-------------" + url);
                                new JSON_remove_file().execute(url);
                                //---------------------------

                                // System.out.println("Removed attach_id-----------" + attid);

                                // ((LinearLayout) addView.getParent()).removeView(addView);
                            }
                        });

                        btn_open.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if ("?".contains(fileUrl_text)) {
                                    file_fileUrl_text = fileUrl_text + "&user_id=" + Model.id + "&token=" + Model.token + "&enc=1";
                                } else {
                                    file_fileUrl_text = fileUrl_text + "?user_id=" + Model.id + "&token=" + Model.token + "&enc=1";
                                }


    /*                            Intent intent = new Intent(BookingViewActivity.this, WebViewActivity.class);
                                intent.putExtra("url", file_fileUrl_text);
                                intent.putExtra("type", "attachment");
                                startActivity(intent);*/


                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(file_fileUrl_text));
                                startActivity(i);
                            }
                        });

                        expand_inner_view.addView(addView2);
                        //-----------------------------------

                    }

                    expand_layout.addView(addView);


                    Model.query_launch = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(BookingViewActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                (new JSONParser()).getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            //-------------------------------------------------------------------
            String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + booking_id + "&item_type=booking&user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("get_family_url---------" + get_family_url);
            new JSON_getFileList().execute(get_family_url);
            //-------------------------------------------------------------------

            dialog.dismiss();

        }
    }
}
