package com.orane.icliniq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class Consultation2 extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private TextView timeTextView;
    private TextView dateTextView;
    private CheckBox mode24Hours;
    private CheckBox modeDarkTime;
    private CheckBox modeDarkDate;
    private CheckBox modeCustomAccentTime;
    private CheckBox modeCustomAccentDate;
    private CheckBox vibrateTime;
    private CheckBox vibrateDate;
    private CheckBox dismissTime;
    private CheckBox dismissDate;
    private CheckBox titleTime;
    private CheckBox titleDate;
    private CheckBox showYearFirst;
    private CheckBox enableSeconds;
    private CheckBox limitTimes;
    private CheckBox limitDates;
    private CheckBox highlightDates;

    private int i = 1;
    private final static String CLICK = "click";
    private final static String NEXT_DATA = "next";
    RadioButton time_band1, time_band2, time_band3;
    LinearLayout track1, track2, track3, ftrack_layout;
    Button btn_date, btn_submit;
    Spinner spinner_timerange, spinner_timezone;
    Map<String, String> timerange_map = new HashMap<String, String>();
    public String timerange_text, token_status, date_text, fee_cp, cons_select_date, fee_hp, fee_lp, cons_phone, cons_type_text, cons_type, ftrack_show, consult_id, cons_phno, Query, spec_val, lang_val, cccode, sel_timerange_code, sel_timerange, sel_timezone, content_str, timezone_str, times_values;
    JSONObject cons_booking_jsonobj, post_json, jsonobj, jsonobj1c;
    TextView tv_fee_cp, tv_changetimezone, tv_tz_present, tv_fee_hp, tv_fee_lp;
    public static Activity fa;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation2);

        //------------------------ Initialization --------------------------------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.upload_files = "";
        //------------------------ Initialization --------------------------------------

        FlurryAgent.onPageView();

        //------------------------ Object Initialization --------------------------------------
        tv_fee_cp = (TextView) findViewById(R.id.tv_fee_cp);
        timeTextView = (TextView) findViewById(R.id.time_textview);
        dateTextView = (TextView) findViewById(R.id.date_textview);
/*      Button timeButton = (Button) findViewById(R.id.time_button);
        Button dateButton = (Button) findViewById(R.id.date_button);*/
        mode24Hours = (CheckBox) findViewById(R.id.mode_24_hours);
        modeDarkTime = (CheckBox) findViewById(R.id.mode_dark_time);
        modeDarkDate = (CheckBox) findViewById(R.id.mode_dark_date);
        modeCustomAccentTime = (CheckBox) findViewById(R.id.mode_custom_accent_time);
        modeCustomAccentDate = (CheckBox) findViewById(R.id.mode_custom_accent_date);
        vibrateTime = (CheckBox) findViewById(R.id.vibrate_time);
        vibrateDate = (CheckBox) findViewById(R.id.vibrate_date);
        dismissTime = (CheckBox) findViewById(R.id.dismiss_time);
        dismissDate = (CheckBox) findViewById(R.id.dismiss_date);
        titleTime = (CheckBox) findViewById(R.id.title_time);
        titleDate = (CheckBox) findViewById(R.id.title_date);
        showYearFirst = (CheckBox) findViewById(R.id.show_year_first);
        enableSeconds = (CheckBox) findViewById(R.id.enable_seconds);
        limitTimes = (CheckBox) findViewById(R.id.limit_times);
        limitDates = (CheckBox) findViewById(R.id.limit_dates);
        highlightDates = (CheckBox) findViewById(R.id.highlight_dates);

        modeDarkTime.setChecked(Utils.isDarkTheme(this, modeDarkTime.isChecked()));
        modeDarkDate.setChecked(Utils.isDarkTheme(this, modeDarkDate.isChecked()));
        //------------------------ Object Initialization --------------------------------------

        //------------ Toolbar Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Schedule the consultation");
        }
        //------------ Toolbar Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        tv_tz_present = (TextView) findViewById(R.id.tv_tz_present);
        tv_fee_hp = (TextView) findViewById(R.id.tv_fee_hp);
        tv_fee_lp = (TextView) findViewById(R.id.tv_fee_lp);

        tv_changetimezone = (TextView) findViewById(R.id.tv_changetimezone);
        spinner_timezone = (Spinner) findViewById(R.id.spinner_timezone);
        spinner_timerange = (Spinner) findViewById(R.id.spinner_timerange);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        time_band1 = (RadioButton) findViewById(R.id.time_band1);
        time_band2 = (RadioButton) findViewById(R.id.time_band2);
        time_band3 = (RadioButton) findViewById(R.id.time_band3);
        track1 = (LinearLayout) findViewById(R.id.track1);
        track2 = (LinearLayout) findViewById(R.id.track2);
        track3 = (LinearLayout) findViewById(R.id.track3);
        ftrack_layout = (LinearLayout) findViewById(R.id.ftrack_layout);


        //Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_tz_present)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_choose)).setTypeface(font_bold);
        btn_submit.setTypeface(font_bold);


        try {
            //------ getting Values ---------------------------
            Intent intent = getIntent();
            Query = intent.getStringExtra("Query");
            spec_val = intent.getStringExtra("spec_val");
            lang_val = intent.getStringExtra("lang_val");
            cccode = intent.getStringExtra("cccode");
            cons_phno = intent.getStringExtra("cons_phno");
            ftrack_show = intent.getStringExtra("ftrack_show");
            cons_type = intent.getStringExtra("cons_type");
            fee_hp = intent.getStringExtra("fee_hp");
            fee_lp = intent.getStringExtra("fee_lp");
            fee_cp = intent.getStringExtra("fee_cp");
            //------ getting Values ---------------------------

            System.out.println("Query---------" + Query);
            System.out.println("spec_val---------" + spec_val);
            System.out.println("lang_val---------" + lang_val);
            System.out.println("cccode---------" + cccode);
            System.out.println("cons_phno---------" + cons_phno);
            System.out.println("ftrack_show---------" + ftrack_show);
            System.out.println("cons_type---------" + cons_type);
            System.out.println("fee_hp---------" + fee_hp);
            System.out.println("fee_lp---------" + fee_lp);
            System.out.println("fee_cp---------" + fee_cp);

            //---- getting fees ---------------
            tv_fee_hp.setText(fee_hp);
            tv_fee_lp.setText(fee_lp);
            tv_fee_cp.setText(fee_cp);
            //---- getting fees ---------------

            //------ Ftrack Showing---------------------------
            if (ftrack_show.equals("true")) {
                ftrack_layout.setVisibility(View.VISIBLE);
            } else if (ftrack_show.equals("false")) {
                ftrack_layout.setVisibility(View.GONE);
            }
            //------ Ftrack Showing---------------------------

            cons_select_date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                  /*  System.out.println("Model.id-----" + Model.id);
                    System.out.println("Model.const_type-----" + cons_type);
                    System.out.println("Model.cons_query-----" + Query);
                    System.out.println("Model.time_range-----" + Model.sel_timerange_code);
                    System.out.println("Model.cons_select_date-----" + cons_select_date);
                    System.out.println("Model.cons_lang-----" + lang_val);
                    System.out.println("Model.cons_ccode-----" + Model.sel_country_code);
                    System.out.println("Model.cons_number-----" + cons_phno);
                    System.out.println("Model.cons_timezone-----" + Model.cons_timezone);
                    System.out.println("Model.spec_val-----" + spec_val);*/

                    try {

                        post_json = new JSONObject();
                        post_json.put("user_id", (Model.id));
                        post_json.put("doctor_id", "0");
                        post_json.put("consult_type", cons_type);
                        post_json.put("query", Query);
                        post_json.put("time_range", (Model.sel_timerange_code));
                        post_json.put("consult_date", cons_select_date);
                        post_json.put("lang", lang_val);
                        post_json.put("ccode", cccode);
                        post_json.put("number", cons_phno);
                        post_json.put("timezone", (Model.cons_timezone));
                        post_json.put("speciality", spec_val);

                        System.out.println("post_json-------------" + post_json.toString());

                        //----------------------------------------------------------
                        if ((sel_timerange_code) != null && !(sel_timerange_code).isEmpty() && !(sel_timerange_code).equals("null") && !(sel_timerange_code).equals("") && (sel_timerange_code).equals("1")) {
                            date_text = btn_date.getText().toString();
                            timerange_text = spinner_timerange.getSelectedItem().toString();

                        } else {
                            date_text = "";
                            timerange_text = "";
                        }

                        System.out.println("sel_timerange_code-----" + sel_timerange_code);
                        System.out.println("date_text-----" + date_text);
                        System.out.println("timerange_text-----" + timerange_text);

                        //----------------------------------------------------------

                        if (time_band3.isChecked()) {

                            if ((date_text) != null && !(date_text).isEmpty() && !(date_text).equals("null") && !(date_text).equals("Select Date") && !(date_text).equals("")) {
                                if ((timerange_text) != null && !(timerange_text).isEmpty() && !(timerange_text).equals("null") && !(timerange_text).equals("Select Time Range") && !(timerange_text).equals("")) {
                                    if (new NetCheck().netcheck(Consultation2.this)) {

                                        new JSONPostQuery().execute(post_json);

                                    } else {
                                        Toast.makeText(Consultation2.this, "Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                                    }
                                } else
                                    Toast.makeText(Consultation2.this, "Select Time Range", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(Consultation2.this, "Select Consultation Date", Toast.LENGTH_LONG).show();
                        } else {
                            new JSONPostQuery().execute(post_json);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    force_logout();
                }
            }
        });


        track1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_band1.setChecked(true);
                sel_timerange_code = "0";
                Model.sel_timerange_code = "0";
                System.out.println("sel_timerange_code----" + sel_timerange_code);
            }
        });
        track2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_band2.setChecked(true);
                sel_timerange_code = "-1";
                Model.sel_timerange_code = "-1";
                System.out.println("sel_timerange_code----" + sel_timerange_code);
            }
        });
        track3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_band3.setChecked(true);
                sel_timerange_code = "1";
                Model.sel_timerange_code = "1";
                System.out.println("sel_timerange_code----" + sel_timerange_code);
            }
        });

        try {
            //--------------------------------
            String url = Model.BASE_URL + "/sapp/getTimeRange?format=json_new";
            System.out.println("url----------" + url);
            new JSON_Time_Range().execute(url);
            //--------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

        //------------------------------------------------------------------------------------
        spinner_timezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {


                String spintz = spinner_timezone.getSelectedItem().toString();
                System.out.println("spintz---------" + spintz);

/*

                String tz_value = time_display_map.get(spintz);
                System.out.println("tz_value---------" + tz_value);

*/

                if (spintz.equals("Choose another timezone")) {

                    Intent intent = new Intent(Consultation2.this, TimeZoneActivity.class);
                    startActivity(intent);
                }

                /*String spintz_value = time_display_map.get(spintz);
                System.out.println("spintz_value---------" + spintz_value);

                if (spintz_value != null && !spintz_value.isEmpty() && !spintz_value.equals("null") && !spintz_value.equals("")) {
                    if (spintz_value.equals("Choose another timezone")) {

                        Intent intent = new Intent(Consultation2.this, TimeZoneActivity.class);
                        startActivity(intent);
                    }
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------
        spinner_timerange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                time_band3.setChecked(true);
                sel_timerange = spinner_timerange.getSelectedItem().toString();
                sel_timerange_code = timerange_map.get(sel_timerange);
                Model.sel_timerange_code = timerange_map.get(sel_timerange);

                Model.time_range = sel_timerange;
                System.out.println("Model.time_range------" + Model.time_range);
                System.out.println("Sel_timerange_code------" + Model.sel_timerange_code);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //------------------------------------------------------------------------------------

        tv_changetimezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consultation2.this, TimeZoneActivity.class);
                startActivity(intent);
            }
        });


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_band3.setChecked(true);


                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Consultation2.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);


                if (modeCustomAccentDate.isChecked()) {
                    dpd.setAccentColor(Color.parseColor("#9C27B0"));
                }

                if (titleDate.isChecked()) {
                    dpd.setTitle("Select Date");
                }

                if (limitDates.isChecked()) {
                    Calendar[] dates = new Calendar[300];
                    for (int i = 1; i <= 300; i++) {
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.DATE, i);
                        dates[i - 1] = date;
                    }

                    dpd.setSelectableDays(dates);
                }

                if (highlightDates.isChecked()) {
                    Calendar[] dates = new Calendar[13];
                    for (int i = -6; i <= 6; i++) {
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.WEEK_OF_YEAR, i);
                        dates[i + 6] = date;
                    }
                    dpd.setHighlightedDays(dates);
                }

                // calendar.add(Calendar.DATE, 2);

                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });


        time_band1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //---------------------------------------------
                if (time_band1.isChecked()) {
                    time_band2.setChecked(false);
                    time_band3.setChecked(false);

                    sel_timerange_code = "0";
                    Model.sel_timerange_code = "0";
                    System.out.println("sel_timerange_code----" + sel_timerange_code);
                }
                //---------------------------------------------
            }
        });

        time_band2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //---------------------------------------------
                if (time_band2.isChecked()) {
                    time_band1.setChecked(false);
                    time_band3.setChecked(false);

                    sel_timerange_code = "-1";
                    Model.sel_timerange_code = "-1";
                    System.out.println("sel_timerange_code----" + sel_timerange_code);
                }
                //---------------------------------------------
            }
        });

        time_band3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //---------------------------------------------
                if (time_band3.isChecked()) {
                    time_band1.setChecked(false);
                    time_band2.setChecked(false);

                    sel_timerange_code = "1";
                    Model.sel_timerange_code = "1";
                    System.out.println("sel_timerange_code----" + sel_timerange_code);
                }
                //---------------------------------------------
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
            DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

            if (tpd != null) tpd.setOnTimeSetListener(this);
            if (dpd != null) dpd.setOnDateSetListener(this);

            //--------------------------------------------------
            if ((Model.cons_timezone_val) != null && !(Model.cons_timezone_val).isEmpty() && !(Model.cons_timezone_val).equals("null") && !(Model.cons_timezone_val).equals("")) {
                try {
                    //----------------------------------------------------------------
                    String url = Model.BASE_URL + "/sapp/getTimeRange?format=json_new&tz_name=" + (Model.cons_timezone_val) + "&token=" + Model.token + "&enc=1";
                    System.out.println("getTimerange url--------" + url);
                    new JSON_Time_Range().execute(url);
                    //-------------------------------------------------
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //--------------------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        try {
            String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
            String minuteString = minute < 10 ? "0" + minute : "" + minute;
            String secondString = second < 10 ? "0" + second : "" + second;
            String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
            timeTextView.setText(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        try {
            //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            cons_select_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            System.out.println("Cal Date------" + cons_select_date);

            //--------- for System -------------------
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
            Date dateObj = curFormater.parse(cons_select_date);
            String newDateStr = curFormater.format(dateObj);
            System.out.println("For System select_date---------" + newDateStr);
            //--------------------------------

            btn_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            System.out.println("cons_select_date---------" + cons_select_date);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Consultation2.this);
            dialog.setMessage("Submitting, please wait");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                cons_booking_jsonobj = jParser.JSON_POST(urls[0], "Submit_consultation");

                System.out.println("cons_booking_jsonobj---------------" + cons_booking_jsonobj.toString());
                System.out.println("Cons_Booking JSON--------------" + urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                if (cons_booking_jsonobj.has("token_status")) {
                    String token_status = cons_booking_jsonobj.getString("token_status");
                    System.out.println("token_status-----------" + token_status);

                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(Consultation2.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    consult_id = cons_booking_jsonobj.getString("id");
                    System.out.println("consult_id-----------" + consult_id);


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.consult_type", cons_type);
                    articleParams.put("android.patient.Query", Query);
                    articleParams.put("android.patient.Time_Range", (Model.sel_timerange_code));
                    articleParams.put("android.patient.Consult_date", (cons_select_date));
                    articleParams.put("android.patient.Country_code", cccode);
                    articleParams.put("android.patient.Ph_number", cons_phno);
                    articleParams.put("android.patient.timezone", (Model.cons_timezone));
                    articleParams.put("android.patient.speciality", spec_val);
                    FlurryAgent.logEvent("android.patient.Consultation_Post", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    params.putString("consult_type", cons_type);
                    params.putString("Query", Query);
                    Model.mFirebaseAnalytics.logEvent("Consultation_Post", params);
                    //------------ Google firebase Analitics--------------------

                    Model.query_launch = "Consultation2";

                    ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent intent = new Intent(Consultation2.this, Consultation3.class);
                    intent.putExtra("consult_id", consult_id);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            Consultation2.this.finish();
                        }
                    });
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_Time_Range extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation2.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.getJSONFromUrl(urls[0]);
                System.out.println("Time_Range jsonobj-----" + jsonobj.toString());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            final List<String> categories = new ArrayList<String>();

            try {

                content_str = jsonobj.getString("content");
                timezone_str = jsonobj.getString("timezone");

                Model.cons_timezone = timezone_str;

                System.out.println("content_str--------" + content_str);
                System.out.println("timezone_str--------" + timezone_str);
                System.out.println("Model.cons_timezone--------" + Model.cons_timezone);

                JSONObject content_obj = new JSONObject(content_str);
                String fone = content_obj.getString("1");
                System.out.println("fone--------" + fone);

                JSONArray jsonarray = new JSONArray();
                jsonarray.put(content_obj);

                //======================================================================
                categories.add("Select Time Range");
                timerange_map.put(times_values, "0");
                //=========================================================


                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobj1c = jsonarray.getJSONObject(i);
                    System.out.println("jsonobj1c-----" + jsonobj1c.toString());

                    for (int j = 1; j <= 24; j++) {

                        if (jsonobj1c.has("" + j)) {
                            times_values = jsonobj1c.getString("" + j);
                            //======================================================================
                            categories.add(times_values);
                            timerange_map.put(times_values, "" + j);
                            //=========================================================
                        }
                    }
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Consultation2.this, android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_timerange.setAdapter(dataAdapter);

                //tv_tz_present.setText("Your Timezone: " + timezone_str);

                //----------- Time Zone ------------------------
                final List<String> categories2 = new ArrayList<String>();
                categories2.add(timezone_str);

/*                if ((Model.cons_timezone_name) != null && !(Model.cons_timezone_name).isEmpty() && !(Model.cons_timezone_name).equals("null") && !(Model.cons_timezone_name).equals("")) {
                    categories2.add(Model.cons_timezone_name);
                } else {
                    categories2.add(timezone_str);
                }*/


                System.out.println("Model.cons_timezone_name---------" + Model.cons_timezone_name);
                System.out.println("timezone_str--------" + timezone_str);

                //time_display_map.put(Model.cons_timezone_name, timezone_str);

                categories2.add("Choose another timezone");
                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Consultation2.this, android.R.layout.simple_spinner_item, categories2);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_timezone.setAdapter(dataAdapter2);
                //----------- Time Zone ------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

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

    public void force_logout() {

        try {

            final MaterialDialog alert = new MaterialDialog(Consultation2.this);
            alert.setTitle("Oops..!");
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

                    finishAffinity();
                    Intent i = new Intent(Consultation2.this, LoginActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    finish();
                }
            });
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
