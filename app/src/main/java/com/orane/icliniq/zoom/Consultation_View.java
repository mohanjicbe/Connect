package com.orane.icliniq.zoom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.orane.icliniq.ExpandableActivity;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Prescription_Entry_Activity;
import com.orane.icliniq.Prescription_WebViewActivity;
import com.orane.icliniq.R;
import com.orane.icliniq.Voxeet;
import com.orane.icliniq.chime.MeetingHomeActivity;
import com.orane.icliniq.network.JSONParser;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.StartMeetingParams4APIUser;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class Consultation_View extends AppCompatActivity implements Constants, ZoomSDKInitializeListener, MeetingServiceListener {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    //private final static String DISPLAY_NAME = "ZoomUS SDK";
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
    public static final String currenccalling_layouty_label = "currency_label";
    public static final String have_free_credit = "have_free_credit";
    public static final String photo_url = "photo_url";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String first_query = "first_query_key";
    public static final String token = "token_key";
    private final static String TAG = "Zoom SDK Example";
    private final static int STYPE = MeetingService.USER_TYPE_ZOOM;
    public String cons_id, str_drug_dets;
    public String vendor_text,conf_id_text,conf_name_text,chime_url_text,appt_status,err_status, open_url_text, strHtmlData_text, files_text, DISPLAY_NAME, str_response, z_status, appt_id, uname, docname, pass, z_meeting_id, cid, cquery, docter_txt, cschedule, cmode, notes, strstatus, doc_name, doc_spec, doc_url, cdate, ctime;
    LinearLayout calling_layout, files_full_layout, waiting_layout, full_layout;
    TextView tv_jointips, tv_status, tv_filename, tvdocname, tvedu, tvspec, tvdate, tvtime, tvnotes, tvquery;
    JSONObject jsonobj, get_meetingid_jsonobj;
    ImageView cons_image;
    CircleImageView imageview_poster;
    RippleBackground content;
    CardView card_view_join, card_view;
    Button btn_joinvideocons;
    SharedPreferences sharedpreferences;
    TextView tvattached;
    Button btn_prescription;
    ObservableWebView webview;
    Typeface font_reg, font_bold;
    private EditText mEdtMeetingNo;
    private EditText mEdtMeetingPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mEdtMeetingNo = findViewById(R.id.edtMeetingNo);
        mEdtMeetingPassword = findViewById(R.id.edtMeetingPassword);

        try {


            DISPLAY_NAME = Model.name;

            ZoomSDK zoomSDK = ZoomSDK.getInstance();

            if (savedInstanceState == null) {
                zoomSDK.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, this);
            }

            if (zoomSDK.isInitialized()) {
                registerMeetingServiceListener();
            }


        /*
            if (savedInstanceState == null) {
                ZoomSDK sdk = ZoomSDK.getInstance();
                sdk.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, this);
                sdk.setDropBoxAppKeyPair(this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
                sdk.setOneDriveClientId(this, ONEDRIVE_CLIENT_ID);
                //sdk.setGoogleDriveClientId(this, GOOGLE_DRIVE_CLIENT_ID);
            } else {
                registerMeetingServiceListener();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

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
        Model.currency_symbol = sharedpreferences.getString(currency_symbol, "");
        Model.have_free_credit = sharedpreferences.getString(have_free_credit, "");
        Model.photo_url = sharedpreferences.getString(photo_url, "");
        Model.kmid = sharedpreferences.getString(sp_km_id, "");
        Model.first_query = sharedpreferences.getString(first_query, "no");
        //============================================================

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================


        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Consultation");
        }
        //------------ Object Creations -------------------------------

        //-------------------------------------------
        FlurryAgent.onPageView();
        //-------------------------------------------

        //btn_joinvideocons = (Button) findViewById(R.id.btn_joinvideocons);
        card_view_join = findViewById(R.id.card_view_join);
        card_view = findViewById(R.id.card_view);

        webview = findViewById(R.id.webview);

        btn_prescription = findViewById(R.id.btn_prescription);
        files_full_layout = findViewById(R.id.files_full_layout);
        calling_layout = findViewById(R.id.calling_layout);
        waiting_layout = findViewById(R.id.waiting_layout);
        full_layout = findViewById(R.id.full_layout);
        imageview_poster = findViewById(R.id.imageview_poster);
        cons_image = findViewById(R.id.cons_image);
        tvdocname = findViewById(R.id.tvdocname);
        tvedu = findViewById(R.id.tvedu);
        tvspec = findViewById(R.id.tvspec);
        tv_jointips = findViewById(R.id.tv_jointips);
        tvattached = findViewById(R.id.tvattached);

        tv_filename = findViewById(R.id.tv_filename);
        tvquery = findViewById(R.id.tvquery);
        tvdate = findViewById(R.id.tvdate);
        tvtime = findViewById(R.id.tvtime);
        tvnotes = findViewById(R.id.tvnotes);
        tv_status = findViewById(R.id.tv_status);

        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tvdocname.setTypeface(font_bold);
        tvspec.setTypeface(font_reg);
        tv_jointips.setTypeface(font_reg);

        ((TextView) findViewById(R.id.tv_schedule)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_date)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvdate)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_time)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvtime)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_query_head)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_notes_head)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_query_head)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_query_head)).setTypeface(font_bold);

        tvquery.setTypeface(font_reg);
        tvnotes.setTypeface(font_reg);
        tvspec.setTypeface(font_reg);

        try {
            Intent intent = getIntent();
            cons_id = intent.getStringExtra("tv_cons_id");
            System.out.println("Getting cons_id----- " + cons_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        full_process();

		/*btn_joinvideocons.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, com.orane.icliniq.zoom.Connect_Home_Actvity.class);
				startActivity(intent);
			}
		});*/

        final RippleBackground rippleBackground = findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        btn_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=consult&item_id=" + cons_id;
                    System.out.println("Pressed Prescription-----------" + params);
                    new list_drugs().execute(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void registerMeetingServiceListener() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        //Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            System.out.println("Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode);
        } else {
            System.out.println("Initialize Zoom SDK successfully.");

            registerMeetingServiceListener();
        }
    }

    @Override
    protected void onDestroy() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (zoomSDK.isInitialized()) {
            MeetingService meetingService = zoomSDK.getMeetingService();
            meetingService.removeListener(this);
        }

        super.onDestroy();
    }

    public void onClickBtnJoinMeeting(View view) {


/*        Intent i = new Intent(Consultation_View.this, MeetingHomeActivity.class);
        i.putExtra("cons_user_name", "Mohan");
        i.putExtra("conf_name", "meeting130");
        startActivity(i);*/



        //String meetingNo = mEdtMeetingNo.getText().toString().trim();
        //String meetingPassword = mEdtMeetingPassword.getText().toString().trim();

      /*  if (isInternetOn()) {
            try {
                //------------------------- Url Pass ------------------------- open_zoom();
                String get_meet_url = Model.BASE_URL + "sapp/getzid?appt_id=" + appt_id + "&user_id=" + (Model.id) + "&token=" + Model.token;
                System.out.println("Cond get_meet_url------" + get_meet_url);
                new Get_Meeting_id().execute(get_meet_url);
                //------------------------- Url Pass -------------------------
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Internet in not available", Toast.LENGTH_LONG).show();
        }*/


        try {
            //------------------------- Url Pass ------------------------- open_zoom();
            String get_meet_url = Model.BASE_URL + "sapp/getzidV2?appt_id=" + appt_id + "&user_id=" + (Model.id) + "&token=" + Model.token;
            System.out.println("Cond get_meet_url------" + get_meet_url);
            new Get_Meeting_id_V2().execute(get_meet_url);
            //------------------------- Url Pass -------------------------
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


/*
    @Override
    public void onMeetingEvent(int meetingEvent, int errorCode,
                               int internalErrorCode) {

        if (meetingEvent == MeetingEvent.MEETING_CONNECT_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            //Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        }
    }
*/

    public void full_process() {

        try {
            //---------------------------------------------------------------
            String view_url = Model.BASE_URL + "/sapp/viewAppt?id=" + cons_id + "&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&format=json&token=" + Model.token;
            System.out.println("URL--------" + view_url);
            new JSON_View_Consult().execute(view_url);
            //---------------------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void join_video_cons() {


        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        if (z_meeting_id == null) {
            Toast.makeText(this, "MEETING_ID in Constants can not be NULL", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        StartMeetingOptions opts = new StartMeetingOptions();
        opts.no_driving_mode = false;
//		opts.no_meeting_end_message = true;
        opts.no_titlebar = false;
        opts.no_bottom_toolbar = false;
        opts.no_invite = true;
        opts.custom_meeting_id = z_meeting_id;


        StartMeetingParams4APIUser params = new StartMeetingParams4APIUser();
        params.userId = USER_ID;
        params.zoomToken = ZOOM_TOKEN;
        params.meetingNo = z_meeting_id;
        params.displayName = DISPLAY_NAME;

        int ret = meetingService.startMeetingWithParams(this, params, opts);

        System.out.println("Getting ret----- " + ret);

        Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);

        waiting_layout.setVisibility(View.GONE);
        calling_layout.setVisibility(View.VISIBLE);
        finish();






     /*   String meetingNo = z_meeting_id;
        String meetingPassword = "";

        System.out.println("Getting meetingNo----- " + meetingNo);


        if (meetingNo.length() == 0) {
            //Toast.makeText(this, "You need to enter a meeting number which you want to join.", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Something went wrong. Please contact icliniq customer support.", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            //Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Something went wrong. Please contact icliniq customer support.", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        MeetingOptions opts = new MeetingOptions();

        System.out.println("Getting meetingNo----- " + meetingNo);
        System.out.println("Getting Model.name----- " + (Model.name));
        System.out.println("Getting meetingPassword----- " + meetingPassword);

        int ret = meetingService.joinMeeting(this, meetingNo, (Model.name), meetingPassword, opts);

        System.out.println("Getting ret----- " + ret);

        waiting_layout.setVisibility(View.GONE);
        calling_layout.setVisibility(View.VISIBLE);
        finish();*/
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public void onClick(View v) {

       /* try {
            TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();
            System.out.println("str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);

            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {

            TextView tv_filename = v.findViewById(R.id.tv_filename);

            String file_name = tv_filename.getText().toString();

            System.out.println("str_filename-------" + file_name);
            System.out.println("cons_id-------" + cons_id);

                /*Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
                //Intent i = new Intent(getApplicationContext(), Attachment_List_Activity.class);
                //Intent i = new Intent(getApplicationContext(), Attached_List_Activity.class);
                //i.putExtra("filetxt", files_text);
                i.putExtra("filetxt", file_name);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

            Intent i = new Intent(Consultation_View.this, ExpandableActivity.class);
            i.putExtra("item_id", cons_id);
            i.putExtra("item_type", "appointment");
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode,
                                       int internalErrorCode) {

        if (meetingStatus == MeetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        }

        if (meetingStatus == MeetingStatus.MEETING_STATUS_IDLE || meetingStatus == MeetingStatus.MEETING_STATUS_FAILED) {

        }
    }

    class Get_Meeting_id extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation_View.this);
            dialog.setMessage("please wait");
            dialog.show();
            dialog.setCancelable(false);

            waiting_layout.setVisibility(View.VISIBLE);
            calling_layout.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                JSONParser jParser = new JSONParser();
                get_meetingid_jsonobj = jParser.getJSONFromUrl(urls[0]);
                System.out.println("urls[0]---------------" + urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                //-------------------------------------------------------
                if (get_meetingid_jsonobj.has("open_url")) {
                    open_url_text = get_meetingid_jsonobj.getString("open_url");
                } else {
                    open_url_text = "";
                }
                if (get_meetingid_jsonobj.has("z_meeting_id")) {
                    z_meeting_id = get_meetingid_jsonobj.getString("z_meeting_id");
                }
                if (get_meetingid_jsonobj.has("z_status")) {
                    z_status = get_meetingid_jsonobj.getString("z_status");
                }
                //-------------------------------------------------------

                System.out.println("get z_meeting_id----------" + z_meeting_id);
                System.out.println("get z_status----------" + z_status);
                System.out.println("get open_url_text----------" + open_url_text);

                if (z_status.equals("1")) {

                    if (open_url_text != null && !open_url_text.isEmpty() && !open_url_text.equals("null") && !open_url_text.equals("")) {

                        System.out.println("Open with Web Browser------------" + open_url_text);

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(open_url_text));
                        startActivity(i);

                    } else {
                        if (z_meeting_id != null && !z_meeting_id.isEmpty() && !z_meeting_id.equals("null") && !z_meeting_id.equals("null")) {
                            System.out.println("Open with Zoom ------------" + z_meeting_id);

                            join_video_cons();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Sorry, The Consultation is not started yet, Please wait for another Notification to come", Toast.LENGTH_LONG).show();
                }


                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class JSON_View_Consult extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Consultation_View.this);
            dialog.setMessage("please wait");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);

            full_layout.setVisibility(View.GONE);
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
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    appt_id = jsonobj.getString("id");
                    cquery = jsonobj.getString("query");
                    cmode = jsonobj.getString("consultation_mode");
                    notes = jsonobj.getString("notes_from_doctor");
                    strstatus = jsonobj.getString("str_status");
                    files_text = jsonobj.getString("files");
                    strHtmlData_text = jsonobj.getString("strHtmlData");
                    String has_prescription = jsonobj.getString("has_prescription");

                    //------------------has_prescription------------------------------------
                    if (has_prescription != null && !has_prescription.isEmpty() && !has_prescription.equals("null") && !has_prescription.equals("")) {
                        if (has_prescription.equals("1")) {
                            btn_prescription.setVisibility(View.VISIBLE);
                        } else {
                            btn_prescription.setVisibility(View.GONE);
                        }
                    }
                    else{
                        btn_prescription.setVisibility(View.GONE);
                    }
                    //------------------has_prescription------------------------------------


                    //------------------File------------------------------------
                    if (files_text.length() > 2) {
                        tv_filename.setText(files_text);

                        JSONArray attch_aaray = new JSONArray(files_text);
                        tvattached.setText(attch_aaray.length() + " file(s) attached");

                        files_full_layout.setVisibility(View.VISIBLE);
                    } else {
                        tv_filename.setText("");
                        files_full_layout.setVisibility(View.GONE);
                    }
                    //------------------File------------------------------------


                    //------------------z_meeting_id------------------------------------
                    if (jsonobj.has("z_meeting_id")) {
                        z_meeting_id = jsonobj.getString("z_meeting_id");
                        card_view.setVisibility(View.VISIBLE);
                        card_view_join.setVisibility(View.VISIBLE);

                        System.out.println("z_meeting_id------------" + z_meeting_id);
                    } else {
                        card_view.setVisibility(View.VISIBLE);
                        card_view_join.setVisibility(View.GONE);
                        System.out.println("z_meeting_id------------ No");
                    }
                    //-------------------z_meeting_id-----------------------------------

                    //----------------------------------------------
                    docter_txt = jsonobj.getString("doctor");
                    JSONObject jsondoctxt = new JSONObject(docter_txt);
                    doc_name = jsondoctxt.getString("name");
                    doc_spec = jsondoctxt.getString("speciality");
                    doc_url = jsondoctxt.getString("photo_url");
                    //----------------------------------------------

                    //----------------------------------------------
                    cschedule = jsonobj.getString("schedule");
                    JSONObject jsonschtxt = new JSONObject(cschedule);
                    cdate = jsonschtxt.getString("date");
                    ctime = jsonschtxt.getString("time");
                    //----------------------------------------------
/*
                    System.out.println("cid--------" + cid);
                    System.out.println("cquery--------" + cquery);
                    System.out.println("cmode--------" + cmode);
                    System.out.println("notes--------" + notes);
                    System.out.println("strstatus--------" + strstatus);
                    System.out.println("doc_name--------" + doc_name);
                    System.out.println("doc_spec--------" + doc_spec);
                    System.out.println("doc_url--------" + doc_url);
                    System.out.println("cdate--------" + cdate);
                    System.out.println("ctime--------" + ctime);
*/

                    tvdocname.setText(doc_name);
                    tvspec.setText(Html.fromHtml(doc_spec));
                    tvdate.setText(cdate);
                    tvtime.setText(ctime);

                    //tvquery.setText(Html.fromHtml(cquery));

                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadDataWithBaseURL("", strHtmlData_text, "text/html", "UTF-8", "");
                    webview.setLongClickable(false);

                    tvnotes.setText(Html.fromHtml(notes));

                    if (strstatus != null && !strstatus.isEmpty() && !strstatus.equals("null") && !strstatus.equals("")) {
                        tv_status.setText(Html.fromHtml(strstatus));
                    } else {
                        tv_status.setText("");
                    }


                    //--------- Selecting Mode --------------------------------
                    if (cmode.equals("Phone")) {
                        cons_image.setBackgroundResource(R.mipmap.phone_cons_ico_color);
                        card_view_join.setVisibility(View.GONE);
                    } else if (cmode.equals("Direct Visit")) {
                        cons_image.setBackgroundResource(R.mipmap.direct_walk);
                    } else {
                        cons_image.setBackgroundResource(R.mipmap.video_cons_ico_color);
                    }
                    //--------- Selecting Mode --------------------------------

                    Picasso.with(getApplicationContext()).load(doc_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(imageview_poster);

                    full_layout.setVisibility(View.VISIBLE);

                    dialog.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class list_drugs extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation_View.this);
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
                            Toast.makeText(Consultation_View.this, msg_text, Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class Get_Meeting_id_V2 extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation_View.this);
            dialog.setMessage("Connecting..., please wait");
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

                get_meetingid_jsonobj = new JSONObject(str_response);

                if (get_meetingid_jsonobj.has("token_status")) {
                    String token_status = get_meetingid_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(Consultation_View.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {


                    //-------------------------------------------------------
                    if (get_meetingid_jsonobj.has("open_url")) {
                        open_url_text = get_meetingid_jsonobj.getString("open_url");
                        System.out.print("open_url_text------------" + open_url_text);
                    } else {
                        open_url_text = "";
                    }


                    if (get_meetingid_jsonobj.has("chime_url")) {
                        chime_url_text = get_meetingid_jsonobj.getString("chime_url");
                        System.out.print("open_url_text------------" + open_url_text);
                    } else {
                        chime_url_text = "";
                    }

                    if (get_meetingid_jsonobj.has("vendor")) {
                        vendor_text = get_meetingid_jsonobj.getString("vendor");
                        System.out.print("vendor_text------------" + vendor_text);
                    } else {
                        vendor_text = "";
                    }

                    if (get_meetingid_jsonobj.has("conf_id")) {
                        conf_id_text = get_meetingid_jsonobj.getString("conf_id");
                        System.out.print("conf_id_text------------" + conf_id_text);
                    } else {
                        conf_id_text = "";
                    }
                    if (get_meetingid_jsonobj.has("name")) {
                        conf_name_text = get_meetingid_jsonobj.getString("name");
                        System.out.print("conf_name_text------------" + conf_name_text);
                    } else {
                        conf_name_text = "";
                    }

                    if (get_meetingid_jsonobj.has("z_meeting_id")) {
                        z_meeting_id = get_meetingid_jsonobj.getString("z_meeting_id");
                        System.out.println("get z_meeting_id----------" + z_meeting_id);
                    }
                    if (get_meetingid_jsonobj.has("z_status")) {
                        z_status = get_meetingid_jsonobj.getString("z_status");
                        System.out.println("get z_status----------" + z_status);
                    }
                    //-------------------------------------------------------


                    if (get_meetingid_jsonobj.has("appt_status")) {
                        appt_status = get_meetingid_jsonobj.getString("appt_status");
                        System.out.println("get appt_status----------" + appt_status);
                    } else {
                        appt_status = "";
                    }

                    if (get_meetingid_jsonobj.has("status")) {
                        err_status = get_meetingid_jsonobj.getString("status");
                        System.out.println("get err_status----------" + err_status);
                    } else {
                        err_status = "";
                    }


                    if (vendor_text.equals("1")) {
                       start_voxeet_cons(conf_name_text, conf_id_text);
                    } else if (vendor_text.equals("2")) {

                        Intent i = new Intent(Consultation_View.this, MeetingHomeActivity.class);
                        i.putExtra("cons_user_name",conf_name_text);
                        i.putExtra("conf_name", conf_id_text);
                        i.putExtra("chime_url", chime_url_text);

                        startActivity(i);

                    } else if (vendor_text.equals("3")) {

/*                        if (z_meeting_id != null && !z_meeting_id.isEmpty() && !z_meeting_id.equals("null") && !z_meeting_id.equals("")) {
                            System.out.println("Open with Zoom ------------" + z_meeting_id);
                            join_video_cons();
                            //start_consultation();
                        } else {
                            start_cons_layout.setVisibility(View.GONE);
                        }*/

                    } else if (vendor_text.equals("4")) {
                        if (open_url_text != null && !open_url_text.isEmpty() && !open_url_text.equals("null") && !open_url_text.equals("")) {
                            System.out.println("Open with WebRtc SDK------------" + open_url_text);
                            try {

                                Uri uri = Uri.parse("googlechrome://navigate?url=" + open_url_text);
                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                if (i.resolveActivity(getPackageManager()) == null) {
                                    i.setData(Uri.parse(open_url_text));
                                }

                                startActivity(i);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Consultation_View.this, "Please install chrome browser to open this video meeting..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (open_url_text != null && !open_url_text.isEmpty() && !open_url_text.equals("null") && !open_url_text.equals("")) {

                            System.out.println("Open with WebRtc SDK------------" + open_url_text);

                            try {

                                Uri uri = Uri.parse("googlechrome://navigate?url=" + open_url_text);
                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                if (i.resolveActivity(getPackageManager()) == null) {
                                    i.setData(Uri.parse(open_url_text));
                                }

                                startActivity(i);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Consultation_View.this, "Please install chrome browser to open this video meeting..", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Consultation_View.this, "This consultation is not Started yet, Try after some time", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println("Call Error....." + e.toString());
            }
            dialog.cancel();

        }
    }

    public void start_voxeet_cons(String conf_name_text, String conf_id_text) {

        Intent i = new Intent(Consultation_View.this, Voxeet.class);
        i.putExtra("cons_user_name", conf_name_text);
        i.putExtra("conf_name", conf_id_text);
        startActivity(i);

    }

}