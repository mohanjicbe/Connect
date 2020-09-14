package com.orane.icliniq.Labtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.orane.icliniq.Agencies_List_Activity;
import com.orane.icliniq.Invoice_Page_New;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.network.JSONParser;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Labtest_Beneficiary extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    EditText edt_email, edt_address, edt_pincode, edt_mobno, edt_bname;
    RadioGroup radio_group;
    String gender_val = "1", lalapath_agency_val, thyro_agency_val, amt_pay, thyrocare_obj, lalpath_obj, dob_for_show, dob_for_server, invoiceId_val, orderId_val, status_val, date_for_derver, time_noon, date_for_show, time_mins, time_hours;
    JSONObject json_benef, json_agencies_response, json_invoice, json_response_obj;
    RadioButton rad_male, rad_female;
    Button btn_thyrocare_agency, btn_lalpath_agency, btn_date, btn_time, btn_continue, btn_dob;
    TextView tv_agencies_name, tv_agencies_name_thyro;
    Toolbar toolbar;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;
    SimpleDateFormat simpleDateOnlyFormat;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder;

    TextView tv_cat_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labtest_beneficiary);

        //------------------- Toolbar -----------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------------- Toolbar -----------------------------------

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        amt_pay = intent.getStringExtra("amt_pay");
        System.out.println("Get Intent amt_pay-----" + amt_pay);
        //------ getting Values ---------------------------


        tv_cat_amt = (TextView) findViewById(R.id.tv_cat_amt);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobno = (EditText) findViewById(R.id.edt_mobno);
        edt_bname = (EditText) findViewById(R.id.edt_bname);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        rad_male = (RadioButton) findViewById(R.id.rad_male);
        rad_female = (RadioButton) findViewById(R.id.rad_female);
        edt_pincode = (EditText) findViewById(R.id.edt_pincode);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_thyrocare_agency = (Button) findViewById(R.id.btn_thyrocare_agency);
        btn_lalpath_agency = (Button) findViewById(R.id.btn_lalpath_agency);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_dob = (Button) findViewById(R.id.btn_dob);

        tv_agencies_name = (TextView) findViewById(R.id.tv_agencies_name);
        tv_agencies_name_thyro = (TextView) findViewById(R.id.tv_agencies_name_thyro);

        tv_cat_amt.setText("Rs. " + amt_pay);

        btn_thyrocare_agency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Labtest_Beneficiary.this, Agencies_List_Activity.class);
                intent.putExtra("agency_type", thyrocare_obj);
                Model.vendor_name = "thyrocare";
                startActivity(intent);
            }
        });

        btn_lalpath_agency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Labtest_Beneficiary.this, Agencies_List_Activity.class);
                intent.putExtra("agency_type", lalpath_obj);
                Model.vendor_name = "lalpath";
                startActivity(intent);
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 4); // 4. Feb. 2018
                calendar.set(Calendar.MONTH, 1);
                calendar.set(Calendar.YEAR, 2018);
                calendar.set(Calendar.HOUR_OF_DAY, 11);
                calendar.set(Calendar.MINUTE, 13);

                final Date defaultDate = calendar.getTime();

                singleBuilder = new SingleDateAndTimePickerDialog.Builder(Labtest_Beneficiary.this)
                        //.bottomSheet()
                        //.curved()

                        //.backgroundColor(Color.BLACK)
                        //.mainColor(Color.GREEN)

                        .displayHours(false)
                        .displayMinutes(false)
                        .displayDays(false)
                        .displayMonth(true)
                        .displayDaysOfMonth(true)
                        .displayYears(true)
                        .defaultDate(defaultDate)
                        .displayMonthNumbers(true)

                        //.mustBeOnFuture()
                        //.minutesStep(15)
                        //.mustBeOnFuture()
                        .defaultDate(defaultDate)
                        //.minDateRange(minDate)
                        //.maxDateRange(maxDate)

                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {

                            }
                        })

                        .title("Select Date")
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {

                                date_for_show = new SimpleDateFormat("dd-MM-yyyy").format(date);
                                date_for_derver = new SimpleDateFormat("yyyy-MM-dd").format(date);

                                System.out.println("date_for_show--------------" + date_for_show);
                                System.out.println("date_for_derver--------------" + date_for_derver);

                                btn_date.setText(date_for_show);
                            }
                        });

                singleBuilder.display();
            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String email_text = edt_email.getText().toString();
                    String mob_text = edt_mobno.getText().toString();
                    String pincode_text = edt_pincode.getText().toString();
                    String address_text = edt_address.getText().toString();
                    String bname_text = edt_bname.getText().toString();

                    //-----------------------------
                    if (rad_male.isSelected()) {
                        gender_val = "1";
                    }
                    if (rad_female.isSelected()) {
                        gender_val = "2";
                    }
                    //-----------------------------

                    System.out.println("gender_val--------------- " + gender_val);

                    String timee = time_hours + ":" + time_mins + ":00";

                    json_benef = new JSONObject();
                    json_benef.put("user_id", (Model.id));
                    json_benef.put("token", Model.token);
                    json_benef.put("email", email_text);
                    json_benef.put("mobile", mob_text);
                    json_benef.put("pincode", pincode_text);
                    json_benef.put("address", address_text);
                    json_benef.put("tsp", thyro_agency_val);
                    json_benef.put("tsp_lal", lalapath_agency_val);
                    json_benef.put("ben_name", bname_text);
                    json_benef.put("ben_dob", dob_for_server);
                    json_benef.put("ben_gender", gender_val);
                    json_benef.put("appt_date", date_for_derver);
/*                    json_benef.put("hours", time_hours);
                    json_benef.put("minutes", time_mins);
                    json_benef.put("noon", time_noon);*/
                    json_benef.put("appt_time", timee);

                    System.out.println("json_benef---" + json_benef.toString());

                    if (dob_for_server != null && !dob_for_server.isEmpty() && !dob_for_server.equals("null") && !dob_for_server.equals("")) {

                        if (date_for_derver != null && !date_for_derver.isEmpty() && !date_for_derver.equals("null") && !date_for_derver.equals("")) {

                            if ((Model.agency_val) != null && !(Model.agency_val).isEmpty() && !(Model.agency_val).equals("null") && !(Model.agency_val).equals("")) {

                                if (address_text != null && !address_text.isEmpty() && !address_text.equals("null") && address_text.length() > 20) {

                                    if (mob_text != null && !mob_text.isEmpty() && !mob_text.equals("null") && !mob_text.equals("")) {

                                        new JSON_POST_BENEF().execute(json_benef);

                                    } else {
                                        edt_mobno.setError("Please enter your mobile number");
                                        Toast.makeText(getApplicationContext(), "Mobile number is mandatory", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Address must contain at least 20 letters", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Please select nearest agency", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select your appointment date", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select your Date of Birth", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });


        btn_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Labtest_Beneficiary.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setAccentColor(Color.parseColor("#9C27B0"));
                dpd.setTitle("Select Date");


          /*      //----------__Date Limit ------------------------
                Calendar[] dates = new Calendar[300];
                for (int i = 1; i <= 300; i++) {
                    Calendar date = Calendar.getInstance();
                    date.add(Calendar.DATE, i);
                    dates[i - 1] = date;
                }
                dpd.setSelectableDays(dates);
                //----------__Date Limit ------------------------

                //----------Highlight Date------------------------
                Calendar[] dates2 = new Calendar[13];
                for (int i = -6; i <= 6; i++) {
                    Calendar date = Calendar.getInstance();
                    date.add(Calendar.WEEK_OF_YEAR, i);
                    dates2[i + 6] = date;
                }
                dpd.setHighlightedDays(dates2);
                //----------Highlight Date------------------------*/

                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });


        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        Labtest_Beneficiary.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(false);
                dpd.setAccentColor(Color.parseColor("#9C27B0"));
                dpd.setTitle("Select Time");

                dpd.show(getFragmentManager(), "TimePicker");
            }
        });


        //--------- Listings ---------------------
        String full_url = Model.BASE_URL + "sapp/labAgencies?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("full_url-------------" + full_url);
        new JSON_agencies().execute(full_url);
        //--------- Listings ---------------------

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    class JSON_agencies extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Labtest_Beneficiary.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                json_agencies_response = jParser.getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("json_agencies_response------" + json_agencies_response.toString());

                lalpath_obj = json_agencies_response.getString("lalpath");
                thyrocare_obj = json_agencies_response.getString("thyrocare");

                //--------------------------------------
                if (lalpath_obj != null && !lalpath_obj.isEmpty() && !lalpath_obj.equals("null") && !lalpath_obj.equals("")) {
                    btn_lalpath_agency.setVisibility(View.VISIBLE);
                } else {
                    btn_lalpath_agency.setVisibility(View.GONE);
                }
                //--------------------------------------

                //--------------------------------------
                if (thyrocare_obj != null && !thyrocare_obj.isEmpty() && !thyrocare_obj.equals("null") && !thyrocare_obj.equals("")) {
                    btn_thyrocare_agency.setVisibility(View.VISIBLE);
                } else {
                    btn_thyrocare_agency.setVisibility(View.GONE);
                }
                //--------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        try {
            String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
            String minuteString = minute < 10 ? "0" + minute : "" + minute;
            String secondString = second < 10 ? "0" + second : "" + second;

            String time = hourString + "h" + minuteString + "m" + secondString + "s";
            System.out.println("time----------" + time);

            time_hours = hourString;
            time_mins = minuteString;
            time_noon = "";

            //---------------------------------
            if (hourOfDay > 11) {
                time_noon = "PM";
            } else {
                time_noon = "AM";
            }
            //---------------------------------

            btn_time.setText(hourString + ":" + minuteString + " " + time_noon);

            System.out.println("Time -------------- " + hourString + ":" + minuteString + " " + time_noon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        try {
            //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            //String cons_select_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

            dob_for_show = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            System.out.println("dob_for_show------" + dob_for_show);

            dob_for_server = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            System.out.println("dob_for_server------" + dob_for_server);

            btn_dob.setText(dob_for_show);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_POST_BENEF extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Labtest_Beneficiary.this);
            dialog.setMessage("Please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "labTestBookOrder");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("report_response--------------" + json_response_obj.toString());

                if (json_response_obj.has("status")) {
                    status_val = json_response_obj.getString("status");

                    System.out.println("status_val---------------------" + status_val);
                }

                if (status_val.equals("1")) {

                    orderId_val = json_response_obj.getString("orderId");
                    invoiceId_val = json_response_obj.getString("invoiceId");

                    Intent intent = new Intent(Labtest_Beneficiary.this, Invoice_Page_New.class);
                    intent.putExtra("qid", "0");
                    intent.putExtra("inv_id", invoiceId_val);
                    intent.putExtra("inv_strfee", "");
                    intent.putExtra("type", "labtest");
                    startActivity(intent);
                    finish();

                } else {
                    if (json_response_obj.has("message")) {
                        String message_val = json_response_obj.getString("message");
                        Toast.makeText(getApplicationContext(), message_val, Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {

            if ((Model.query_launch).equals("agencies")) {

                if (Model.vendor_name.equals("thyrocare")) {
                    tv_agencies_name_thyro.setText(Model.agency_name);
                    tv_agencies_name_thyro.setVisibility(View.VISIBLE);
                    //tv_agencies_name.setVisibility(View.GONE);

                    thyro_agency_val = Model.agency_val;

                } else if (Model.vendor_name.equals("lalpath")) {
                    tv_agencies_name.setText(Model.agency_name);
                    tv_agencies_name.setVisibility(View.VISIBLE);
                    //tv_agencies_name_thyro.setVisibility(View.GONE);
                    lalapath_agency_val = Model.agency_val;
                }
            }

            System.out.println("Resume agency_name-----" + Model.agency_name);
            System.out.println("Resume agency_val----" + Model.agency_val);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.ask_menu, menu);
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
}
