package com.orane.icliniq.zoom;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.network.JSONParser;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingEvent;
import us.zoom.sdk.MeetingOptions;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.StartMeetingParams4APIUser;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class Video_Calling_Activity extends AppCompatActivity implements Constants, ZoomSDKInitializeListener, MeetingServiceListener {


    public boolean active = true;

    private Window wind;
    PendingIntent pIntent;
    Ringtone ringtone;
    Uri notification;
    LinearLayout waiting_layout, call_join_layout, decline_layout;
    RelativeLayout call_layout;
    Intent intent;
    MediaPlayer player;

    CircleImageView img_doc_photo;

    private final static String TAG = "Zoom SDK Example";
    private EditText mEdtMeetingNo;
    private EditText mEdtMeetingPassword;

    private final static int STYPE = MeetingService.USER_TYPE_ZOOM;

    LinearLayout full_layout;
    TextView tv_docsp, tv_docname, call_warn_text, tvdocname, tvedu, tvspec, tvdate, tvtime, tvnotes, tvquery;
    public String doc_sp, doc_photo, cons_id;
    JSONObject jsonobj, get_meetingid_jsonobj;
    public String DISPLAY_NAME,str_response, Log_Status, screen_status, z_status, appt_id, uname, docname, pass, z_meeting_id, cid, cquery, docter_txt, cschedule, cmode, notes, strstatus, doc_name, doc_spec, doc_url, cdate, ctime;
    ImageView cons_image;
    CircleImageView imageview_poster;
    RippleBackground content;
    CardView card_view_join, card_view;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_calling_screen);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
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

        FlurryAgent.onPageView();

        /*Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ringtone = RingtoneManager.getRingtone(Video_Calling_Activity.this, notification);

        ringtone.play();
*/


        try{

            player = MediaPlayer.create(Video_Calling_Activity.this,
                    Settings.System.DEFAULT_RINGTONE_URI);
            player.setLooping(true);
            player.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
/*
        //--------------- Alert Service -------------------------------------------------------
        if (checkRingerIsOn(Video_Calling_Activity.this)) {
            player = MediaPlayer.create(Video_Calling_Activity.this,
                    Settings.System.DEFAULT_RINGTONE_URI);
            player.setLooping(true);
            player.start();
        } else {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
        }
        //--------------- Alert Service -------------------------------------------------------
*/






        mEdtMeetingNo = (EditText) findViewById(R.id.edtMeetingNo);
        mEdtMeetingPassword = (EditText) findViewById(R.id.edtMeetingPassword);
/*
        if (savedInstanceState == null) {
            ZoomSDK sdk = ZoomSDK.getInstance();
            sdk.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, this);
            // sdk.setDropBoxAppKeyPair(this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
            // sdk.setOneDriveClientId(this, ONEDRIVE_CLIENT_ID);
            //sdk.setGoogleDriveClientId(this, GOOGLE_DRIVE_CLIENT_ID);
        } else {
            registerMeetingServiceListener();
        }*/


        try {


            DISPLAY_NAME = Model.name;

            ZoomSDK zoomSDK = ZoomSDK.getInstance();

            if(savedInstanceState == null) {
                zoomSDK.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, this);
            }

            if(zoomSDK.isInitialized()) {
                registerMeetingServiceListener();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }




        img_doc_photo = (CircleImageView) findViewById(R.id.img_doc_photo);
        call_layout = (RelativeLayout) findViewById(R.id.call_layout);
        waiting_layout = (LinearLayout) findViewById(R.id.waiting_layout);
        decline_layout = (LinearLayout) findViewById(R.id.decline_layout);
        call_join_layout = (LinearLayout) findViewById(R.id.call_join_layout);
        //btn_joinvideocons = (Button) findViewById(R.id.btn_joinvideocons);
        card_view_join = (CardView) findViewById(R.id.card_view_join);
        card_view = (CardView) findViewById(R.id.card_view);

        tv_docsp = (TextView) findViewById(R.id.tv_docsp);
        tv_docname = (TextView) findViewById(R.id.tv_docname);
        call_warn_text = (TextView) findViewById(R.id.call_warn_text);
        full_layout = (LinearLayout) findViewById(R.id.full_layout);
        imageview_poster = (CircleImageView) findViewById(R.id.imageview_poster);
        cons_image = (ImageView) findViewById(R.id.cons_image);
        tvdocname = (TextView) findViewById(R.id.tvdocname);
        tvedu = (TextView) findViewById(R.id.tvedu);
        tvspec = (TextView) findViewById(R.id.tvspec);
        tvquery = (TextView) findViewById(R.id.tvquery);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvtime = (TextView) findViewById(R.id.tvtime);
        tvnotes = (TextView) findViewById(R.id.tvnotes);

        try {

            Intent intent = getIntent();
            cons_id = intent.getStringExtra("tv_cons_id");
            doc_photo = intent.getStringExtra("doc_photo");
            doc_name = intent.getStringExtra("doc_name");
            doc_sp = intent.getStringExtra("doc_sp");

            tv_docname.setText(doc_name);
            tv_docsp.setText(doc_sp);
            Picasso.with(getApplicationContext()).load(doc_photo).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(img_doc_photo);

            System.out.println("Getting cons_id----- " + cons_id);
            System.out.println("Getting doc_photo----- " + doc_photo);
            System.out.println("Getting doc_name----- " + doc_name);
            System.out.println("Getting doc_sp----- " + doc_sp);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // getWindow().setBackgroundDrawable(null);

        call_warn_text.setText(Html.fromHtml("<b>Note:</b> If your mobile internet is too slow, please join video consultation from your computer. Check for a video link in your email."));
        full_layout.setVisibility(View.GONE);

        active = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    System.out.println("active----" + active);
                    if (active) {
                        Notification();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 60000);


        full_process();

        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

       /* //------------------------------------------------
        PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();
        screenLock.release();

        KeyguardManager manager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("abc");
        lock.disableKeyguard();

        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        //------------------------------------------------
*/



        decline_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    Model.mFirebaseAnalytics.logEvent("VideoCalling_Call_decline", params);
                    //------------ Google firebase Analitics--------------------

                    active = false;

                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());
                    System.out.println("currentDateandTime---------" + currentDateandTime);
/*
                    //----------------- Kissmetrics ----------------------------------
                    Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                    Model.kiss.record("android.patient.Video_Calling_Decline");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.patient.Cons_id", cons_id);
                    properties.put("android.patient.Time", currentDateandTime);
                    Model.kiss.set(properties);
                    //----------------- Kissmetrics ----------------------------------*/
                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.Cons_id", cons_id);
                    articleParams.put("android.patient.Time", currentDateandTime);
                    FlurryAgent.logEvent("android.patient.Video_Calling_Decline", articleParams);
                    //----------- Flurry -------------------------------------------------

/*                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);*/
                    //ringtone.play();

                    Intent intent = new Intent(Video_Calling_Activity.this, Consultation_View.class);
                    intent.putExtra("tv_cons_id", cons_id);
                    System.out.println("Model.appt_id-------" + cons_id);
                    startActivity(intent);

                    finish();

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
    protected void onStop() {
        super.onStop();

        //active = false;
        try{

            player.stop();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public void onPause() {
        super.onPause();

        //active = false;
        try{

            player.stop();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        //Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);


        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            //Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
            System.out.println("Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode);
        } else {
            //Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();
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
        //String meetingNo = mEdtMeetingNo.getText().toString().trim();
        //String meetingPassword = mEdtMeetingPassword.getText().toString().trim();
        try {

            active = false;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id);
            Model.mFirebaseAnalytics.logEvent("VideoCalling_Accept", params);
            //------------ Google firebase Analitics--------------------

            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Video_Calling_Accept");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("android.patient.Cons_id", cons_id);
            properties.put("android.patient.Time", currentDateandTime);
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------
            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.Cons_id", cons_id);
            articleParams.put("android.patient.Time", currentDateandTime);
            FlurryAgent.logEvent("android.patient.Video_Calling_Accept", articleParams);
            //----------- Flurry -------------------------------------------------


            try{

                player.stop();
            }
            catch (Exception e){
                e.printStackTrace();
            }



            if (isInternetOn()) {
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
                Toast.makeText(getApplicationContext(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class Get_Meeting_id extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waiting_layout.setVisibility(View.VISIBLE);
            call_layout.setVisibility(View.GONE);
            full_layout.setVisibility(View.GONE);

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

/*                JSONParser jParser = new JSONParser();
                get_meetingid_jsonobj = jParser.getJSONFromUrl(urls[0]);
                System.out.println("urls[0]---------------" + urls[0]);*/

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
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {


                    //-------------------------------------------------------
                    if (get_meetingid_jsonobj.has("z_meeting_id")) {
                        z_meeting_id = get_meetingid_jsonobj.getString("z_meeting_id");
                    }
                    if (get_meetingid_jsonobj.has("z_status")) {
                        z_status = get_meetingid_jsonobj.getString("z_status");
                    }
                    //-------------------------------------------------------
                    System.out.println("get z_meeting_id----------" + z_meeting_id);
                    System.out.println("get z_status----------" + z_status);


                    if (z_meeting_id != null && !z_meeting_id.isEmpty() && !z_meeting_id.equals("null") && !z_meeting_id.equals("")) {
                        join_video_cons();
                    } else {
                        notify_doc_left();
                    }

/*                if (z_status.equals("1")) {

                } else {
                    //Toast.makeText(getApplicationContext(), "Sorry, Doctor has left from the consultation room. Please wait for Another notification or contact icliniq Customer support.", Toast.LENGTH_LONG).show();
                    //finish();

                    notify_doc_left();
                }*/


/*                waiting_layout.setVisibility(View.GONE);
                call_layout.setVisibility(View.GONE);
                full_layout.setVisibility(View.GONE);*/

                    //finish();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class JSON_View_Consult extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

/*                JSONParser jParser = new JSONParser();
                jsonobj = jParser.getJSONFromUrl(urls[0]);
                System.out.println("urls[0]---------------" + urls[0]);*/

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

                    //------------------z_meeting_id------------------------------------
                    if (jsonobj.has("z_meeting_id")) {
                        z_meeting_id = jsonobj.getString("z_meeting_id");
                        card_view.setVisibility(View.VISIBLE);
                        card_view_join.setVisibility(View.VISIBLE);

                        System.out.println("z_meeting_id------------" + z_meeting_id);
                    } else {
                        card_view.setVisibility(View.VISIBLE);
                        card_view_join.setVisibility(View.GONE);
                        z_meeting_id = "nil";
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

                    System.out.println("cid--------" + cons_id);
                    System.out.println("cquery--------" + cquery);
                    System.out.println("cmode--------" + cmode);
                    System.out.println("notes--------" + notes);
                    System.out.println("strstatus--------" + strstatus);
                    System.out.println("doc_name--------" + doc_name);
                    System.out.println("doc_spec--------" + doc_spec);
                    System.out.println("doc_url--------" + doc_url);
                    System.out.println("cdate--------" + cdate);
                    System.out.println("ctime--------" + ctime);

                    tvdocname.setText(doc_name);
                    tvspec.setText(Html.fromHtml(doc_spec));
                    tvdate.setText(cdate);
                    tvtime.setText(ctime);
                    tvquery.setText(Html.fromHtml(cquery));
                    tvnotes.setText(Html.fromHtml(notes));

                    if (cmode.equals("Phone")) {
                        cons_image.setBackgroundResource(R.mipmap.phone_cons_ico_color);
                    } else {
                        cons_image.setBackgroundResource(R.mipmap.video_cons_ico_color);
                    }

                    Picasso.with(getApplicationContext()).load(doc_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(imageview_poster);

                    //dialog.cancel();
                    //----------------- Kissmetrics ----------------------------------
                    Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                    Model.kiss.record("android.patient.Video_Calling_Screen");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.patient.appt_id", appt_id);
                    properties.put("android.patient.query", cquery);
                    properties.put("android.patient.Mode", cmode);
                    properties.put("android.patient.Status", strstatus);
                    properties.put("android.patient.z_meeting_id", z_meeting_id);
                    properties.put("android.patient.doc_name", doc_name);
                    properties.put("android.patient.doc_spec", doc_spec);
                    properties.put("android.patient.cdate", cdate);
                    properties.put("android.patient.ctime", ctime);
                    Model.kiss.set(properties);
                    //----------------- Kissmetrics ----------------------------------
                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.appt_id", appt_id);
                    articleParams.put("android.patient.query", cquery);
                    articleParams.put("android.patient.Mode", cmode);
                    articleParams.put("android.patient.Status", strstatus);
                    articleParams.put("android.patient.z_meeting_id", z_meeting_id);
                    articleParams.put("android.patient.doc_name", doc_name);
                    articleParams.put("android.patient.doc_spec", doc_spec);
                    articleParams.put("android.patient.cdate", cdate);
                    articleParams.put("android.patient.ctime", ctime);
                    FlurryAgent.logEvent("android.patient.Video_Calling_Screen", articleParams);
                    //----------- Flurry -------------------------------------------------

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void full_process() {

        try {
            //--------------------------------------------------------------
            String view_url = Model.BASE_URL + "/sapp/viewAppt?id=" + cons_id + "&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&format=json&token=" + Model.token;
            System.out.println("URL--------" + view_url);
            new JSON_View_Consult().execute(view_url);
            //--------------------------------------------------------------
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

        finish();




      /*
        String meetingNo = z_meeting_id;
        String meetingPassword = "";

        System.out.println("Getting meetingNo----- " + meetingNo);

        if (meetingNo.length() == 0) {
            Toast.makeText(this, "You need to enter a meeting number which you want to join.", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        MeetingOptions opts = new MeetingOptions();

        opts.no_bottom_toolbar = false;


        System.out.println("Getting meetingNo----- " + meetingNo);
        System.out.println("Getting Model.name----- " + (Model.name));
        System.out.println("Getting meetingPassword----- " + meetingPassword);

        int ret = meetingService.joinMeeting(this, meetingNo, (Model.name), meetingPassword, opts);

        System.out.println("Getting ret----- " + ret);

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


    public void Notification() {

        intent = new Intent(this, Consultation_View.class);
        intent.putExtra("tv_cons_id", cons_id);
        Model.query_launch = "Video_Calling_Activity";

        try {
            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Call_set_Notification");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("android.patient.Cons_id", cons_id);
            properties.put("android.patient.User_ID", (Model.id));
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------
            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.Cons_id", cons_id);
            articleParams.put("android.patient.User_ID", (Model.id));
            FlurryAgent.logEvent("android.patient.Call_set_Notification", articleParams);
            //----------- Flurry -------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);


        //ringtone.play();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.missed_call_white)
                .setTicker("icliniq video call missed")
                .setContentTitle("icliniq Video Consultation")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.missed_call_white))
                .setDefaults(new NotificationCompat().DEFAULT_SOUND)
                .setContentText("Join icliniq video consultation")
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());

        active = false;
    }

    public void notify_doc_left() {

        final MaterialDialog alert = new MaterialDialog(Video_Calling_Activity.this);
        alert.setTitle("Notification");
        alert.setMessage("Sorry.! Doctor has left the Consultation room. We will notify once doctor arrives");
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
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode,
                                       int internalErrorCode) {

        if (meetingStatus == meetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        }

        if (meetingStatus == MeetingStatus.MEETING_STATUS_IDLE || meetingStatus == MeetingStatus.MEETING_STATUS_FAILED) {
            // selectTab(TAB_WELCOME);
        }
    }

    public static boolean checkRingerIsOn(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

}
