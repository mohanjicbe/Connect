package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.hbb20.CountryCodePicker;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class Patient_Profile_Activity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    Button btn_cancel;
    ScrollView scrollview;
    JSONObject jsonobj;
    JSONObject post_json, login_jsonobj, json, json_validate;
    CheckBox checkterms;
    Spinner spinner_speciality;
    ProgressBar progressBar;
    Button btn_date, btn_submit;

    TextView weight_title, height_title, tv_name_title;
    LinearLayout signup_layout;
    EditText edt_phoneno, edt_email, edt_name, edt_age;
    EditText edtname, edtemail, edtpassword;
    public String height_name, cons_select_date, dob_val, wt_name, blood_group_val, blood_group_name, pin_val, tit_val, tit_name, err_val, created_at_val, name_val, email_val, mobile_val, age_val, gender_val, height_id_val, weight_id_val, isValid_val, country_code_val, selected_cc_value, selected_cc_text, str_response, userId, mobno = "", fname, emailid, pwd, country;
    LinearLayout mob_layout, otp_layout;
    TextView tv_mobno, tv_timertext;
    public String err_text, isvalid, userid;
    TextView tvterms, tv_ccode, tv_resend;
    LinearLayout timer_layout;
    RelativeLayout weight_layout, height_layout, ccode_layout;
    MyCountDownTimer myCountDownTimer;
    Map<String, String> cc_map = new HashMap<String, String>();
    CountryCodePicker countryCodePicker;

    Map<String, String> spec_map = new HashMap<String, String>();
    Map<String, String> ht_map = new HashMap<String, String>();
    Map<String, String> wt_map = new HashMap<String, String>();
    Map<String, String> tit_map = new HashMap<String, String>();
    Map<String, String> gen_map = new HashMap<String, String>();
    Map<String, String> family_map = new HashMap<String, String>();
    Map<String, String> bg_map = new HashMap<String, String>();

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
    public static final String first_query = "first_query_key";
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_has_free_follow = "sp_has_free_follow_key";
    public static final String token = "token_key";

    SharedPreferences sharedpreferences;
    private boolean hasStarted = false;
    RadioButton check_thirdgender, check_male, check_female;
    TextView bg_height_title, ccode_height_title;

    RelativeLayout bgroup_layout, name_title_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile);

        FlurryAgent.onPageView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        Model.terms_isagree = "false";
        Model.mobvalidate = "0";
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }
        //------------ Object Creations -------------------------------

        btn_date = findViewById(R.id.btn_date);
        btn_submit = findViewById(R.id.btn_submit);
        tv_name_title = findViewById(R.id.tv_name_title);
        height_title = findViewById(R.id.height_title);
        weight_title = findViewById(R.id.weight_title);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_age = findViewById(R.id.edt_age);
        edt_phoneno = findViewById(R.id.edt_phoneno);
        check_female = findViewById(R.id.check_female);
        check_male = findViewById(R.id.check_male);
        check_thirdgender = findViewById(R.id.check_thirdgender);
        ccode_height_title = findViewById(R.id.ccode_height_title);
        bg_height_title = findViewById(R.id.bg_height_title);

        name_title_layout = findViewById(R.id.name_title_layout);
        bgroup_layout = findViewById(R.id.bgroup_layout);
        ccode_layout = findViewById(R.id.ccode_layout);
        height_layout = findViewById(R.id.height_layout);
        weight_layout = findViewById(R.id.weight_layout);


        hash_map_title();
        hashmap_relation();
        hashmap_height();
        hashmap_weight();
        hashmap_bg();


        name_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_name_title();
            }
        });

        bgroup_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_blood_group();
            }
        });

        ccode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_ccode();
            }
        });

        height_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_height();
            }
        });

        weight_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_weight();
            }
        });


        //tv_ccode.setText("+" + country_code_val);
        selected_cc_value = Model.country_code;
        selected_cc_text = Model.pat_country;

        full_process();

        //-------------------------------------------------------------------------------
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String edt_name_text = edt_name.getText().toString();
                String edt_email_text = edt_email.getText().toString();
                String edt_phoneno_text = edt_phoneno.getText().toString();
                String edt_age_text = edt_age.getText().toString();

                //---------------------------------------
                if (check_male.isChecked()) {
                    gender_val = "1";
                } else if (check_female.isChecked()) {
                    gender_val = "2";
                } else {
                    gender_val = "3";
                }
                //---------------------------------------


                System.out.println("tit_val------------" + tit_val);
                System.out.println("edt_name_text------------" + edt_name_text);
                System.out.println("edt_email_text------------" + edt_email_text);
                System.out.println("selected_cc_value------------" + selected_cc_value);
                System.out.println("edt_phoneno_text------------" + edt_phoneno_text);
                System.out.println("blood_group_val------------" + blood_group_val);
                System.out.println("gender_val------------" + gender_val);
                System.out.println("edt_age_text------------" + edt_age_text);
                System.out.println("height_id_val------------" + height_id_val);
                System.out.println("weight_id_val------------" + weight_id_val);


                if ((edt_name_text.length() > 0)) {
                    if ((edt_email_text.length() > 0)) {
                        if ((edt_phoneno_text.length() > 0)) {

                            try {

                                json_validate = new JSONObject();
                                json_validate.put("user_id", Model.id);
                                json_validate.put("token", Model.token);
                                json_validate.put("title", tit_val);
                                json_validate.put("name", edt_name_text);
                                json_validate.put("email", edt_email_text);
                                json_validate.put("country_code_mobile", selected_cc_value);
                                json_validate.put("mobile", edt_phoneno_text);
                                json_validate.put("blood_group", blood_group_val);
                                json_validate.put("gender", gender_val);
                                json_validate.put("dob", cons_select_date);
                                json_validate.put("height_id", height_id_val);
                                json_validate.put("weight_id", weight_id_val);

                                System.out.println("json_validate----" + json_validate.toString());

                                new Async_PostPatProfile().execute(json_validate);

                                //--------------------------------------------------
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            edt_phoneno.setError("Mobile number is mandatory");
                        }
                    } else {
                        edtemail.setError("Please enter your valid Email address");
                    }
                } else {
                    edtname.setError("Please enter the name");
                }


                /*
                 *
                 * */

             /*   String edt_phoneno_text = edt_phoneno.getText().toString();

                try {
                    edt_phoneno_text = edt_phoneno.getText().toString();

                    if (!edt_phoneno_text.equals("")) {

                        json = new JSONObject();
                        json.put("mobile", edt_phoneno_text);
                        json.put("ccode", selected_cc_value);
                        ** Room Setup ->

                        System.out.println("json----" + json.toString());

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        new Async_CheckMobnoExist().execute(json);

                    } else {
                        edt_phoneno.setError("Enter Mobile number");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
*/

            }
        });


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Patient_Profile_Activity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);

                dpd.setAccentColor(Color.parseColor("#9C27B0"));
                dpd.setTitle("Please select your DOB");

                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private class JSON_get_Patient_Details extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Patient_Profile_Activity.this);
            dialog.setMessage("Please wait");
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

                    System.out.println("jsonobj-----------" + jsonobj.toString());

                    tit_val = jsonobj.getString("title");
                    name_val = jsonobj.getString("name");
                    email_val = jsonobj.getString("email");
                    selected_cc_value = jsonobj.getString("country_code_mobile");
                    mobile_val = jsonobj.getString("mobile");
                    blood_group_val = jsonobj.getString("blood_group");
                    gender_val = jsonobj.getString("gender");
                    age_val = jsonobj.getString("age");
                    dob_val = jsonobj.getString("dob");
                    height_id_val = jsonobj.getString("height_id");
                    weight_id_val = jsonobj.getString("weight_id");
                    created_at_val = jsonobj.getString("created_at");


                    System.out.println("# tit_name----------" + getKeyFromValue(tit_map, tit_val));
                    System.out.println("# rel_name----------" + getKeyFromValue(gen_map, gender_val));
                    System.out.println("# ht_name----------" + getKeyFromValue(ht_map, height_id_val));
                    System.out.println("# wt_name----------" + getKeyFromValue(wt_map, weight_id_val));

                    //--------------------------------------
                    if (dob_val != null && !dob_val.isEmpty() && !dob_val.equals("null") && !dob_val.equals("")) {
                        btn_date.setText(dob_val);
                    } else {
                        btn_date.setText("Please select your DOB");
                    }
                    //--------------------------------------


                    //--------- ------------------------------------------------
                    String map_val = "" + getKeyFromValue(tit_map, tit_val);
                    if (map_val != null && !map_val.isEmpty() && !map_val.equals("null") && !map_val.equals("")) {
                        tv_name_title.setText(map_val);
                    } else {
                        tv_name_title.setText("");
                    }
                    //--------- ------------------------------------------------

                    //--------- ------------------------------------------------
                    String ht_value = "" + getKeyFromValue(ht_map, height_id_val);
                    if (ht_value != null && !ht_value.isEmpty() && !ht_value.equals("null") && !ht_value.equals("")) {
                        height_title.setText(ht_value);
                    } else {
                        height_title.setText("");
                    }
                    //--------- ------------------------------------------------
                    //--------- ------------------------------------------------
                    String wt_value = "" + getKeyFromValue(wt_map, weight_id_val);
                    if (wt_value != null && !wt_value.isEmpty() && !wt_value.equals("null") && !wt_value.equals("")) {
                        weight_title.setText(wt_value);
                    } else {
                        weight_title.setText("");
                    }
                    //--------- ------------------------------------------------

                    //------------------------------------------------------------------
                    if (name_val != null && !name_val.isEmpty() && !name_val.equals("null") && !name_val.equals("")) {
                        edt_name.setText(name_val);
                    } else {
                        edt_name.setText("");
                    }
                    //------------------------------------------------------------------

                    //------------------------------------------------------------------
                    if (email_val != null && !email_val.isEmpty() && !email_val.equals("null") && !email_val.equals("")) {
                        edt_email.setText(email_val);
                    } else {
                        edt_email.setText("");
                    }
                    //------------------------------------------------------------------

                    //-----------------------------------
                    if (gender_val.equals("1")) {
                        check_male.setChecked(true);
                        check_female.setChecked(false);
                        check_thirdgender.setChecked(false);

                    } else if (gender_val.equals("2")) {
                        check_male.setChecked(false);
                        check_female.setChecked(true);
                        check_thirdgender.setChecked(false);
                    } else if (gender_val.equals("3")) {
                        check_male.setChecked(false);
                        check_female.setChecked(false);
                        check_thirdgender.setChecked(true);
                    } else if (gender_val.equals("0")) {
                        check_male.setChecked(false);
                        check_female.setChecked(false);
                        check_thirdgender.setChecked(false);
                    }

                    //-----------------------------------

                    //------------------------------------------------------------------
                    if (age_val != null && !age_val.isEmpty() && !age_val.equals("null") && !age_val.equals("")) {
                        edt_age.setText(age_val);
                    } else {
                        edt_age.setText("");
                    }
                    //------------------------------------------------------------------

                    //------------------------------------------------------------------
                    if (selected_cc_value != null && !selected_cc_value.isEmpty() && !selected_cc_value.equals("null") && !selected_cc_value.equals("")) {
                        ccode_height_title.setText("+" + selected_cc_value);
                    } else {
                        ccode_height_title.setText("");
                    }
                    //------------------------------------------------------------------

                    //------------------------------------------------------------------
                    if (mobile_val != null && !mobile_val.isEmpty() && !mobile_val.equals("null") && !mobile_val.equals("")) {
                        edt_phoneno.setText(mobile_val);
                    } else {
                        edt_phoneno.setText("");
                    }
                    //------------------------------------------------------------------

                    //------------------------------------------------------------------
                    if (height_id_val != null && !height_id_val.isEmpty() && !height_id_val.equals("null") && !height_id_val.equals("")) {
                        height_title.setText(height_id_val);
                    } else {
                        height_title.setText("");
                    }
                    //------------------------------------------------------------------

                    //------------------------------------------------------------------
                    if (weight_id_val != null && !weight_id_val.isEmpty() && !weight_id_val.equals("null") && !weight_id_val.equals("")) {
                        weight_title.setText(weight_id_val);
                    } else {
                        weight_title.setText("");
                    }
                    //------------------------------------------------------------------


                    //--------- ------------------------------------------------
                    String height_value = "" + getKeyFromValue(ht_map, height_id_val);
                    if (height_value != null && !height_value.isEmpty() && !height_value.equals("null") && !height_value.equals("")) {
                        height_title.setText(height_value);
                    } else {
                        height_title.setText("");
                    }
                    //--------- ------------------------------------------------

                    //--------- ------------------------------------------------
                    String weiht_val = "" + getKeyFromValue(wt_map, weight_id_val);
                    if (weiht_val != null && !weiht_val.isEmpty() && !weiht_val.equals("null") && !weiht_val.equals("")) {
                        weight_title.setText(weiht_val);
                    } else {
                        weight_title.setText("");
                    }
                    //--------- ------------------------------------------------


                    //--------- ------------------------------------------------
                    String bg_valaue = "" + getKeyFromValue(bg_map, blood_group_val);
                    if (bg_valaue != null && !bg_valaue.isEmpty() && !bg_valaue.equals("null") && !bg_valaue.equals("")) {
                        bg_height_title.setText(bg_valaue);
                    } else {
                        bg_height_title.setText("");
                    }
                    //--------- ------------------------------------------------

                }


            } catch (Exception e) {
                country = "";
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
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


    public void process_code() {

        try {

            //-----------------------------------------------------
            String url = Model.BASE_URL + "sapp/patientProfile?user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("url-------------" + url);
            new JSON_get_Patient_Details().execute(url);
            //----------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void full_process() {

        if (isInternetOn()) {
            process_code();
        } else {

            final MaterialDialog alert = new MaterialDialog(Patient_Profile_Activity.this);
            alert.setTitle("Internet is not available..!");
            alert.setMessage("Please check your Internet Connection and try again");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("TRY AGAIN", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    full_process();
                }
            });
            alert.setNegativeButton("OK, EXIT", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();
                }
            });

            alert.show();
        }
    }


    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            // if connected with internet

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void signup_err() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("ERROR");
        dialogBuilder.setMessage(err_text);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setNeutralButton("OK", null);
        dialogBuilder.show();
    }


    class Async_PostPatProfile extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Patient_Profile_Activity.this);
            dialog.setMessage("Submitting.. Please Wait...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                System.out.println("Parameters---------------" + urls[0]);

                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "pat_profile_submit");


                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {
                System.out.println("Patient_profile_json---------------" + login_jsonobj.toString());


                if (login_jsonobj.has("status")) {
                    String status_val = login_jsonobj.getString("status");
                    System.out.println("status_val----------------" + status_val);

                    if (status_val.equals("1")) {
                        Toast.makeText(Patient_Profile_Activity.this, "Profile saved successfully..", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String msg_val = login_jsonobj.getString("msg");
                        Toast.makeText(Patient_Profile_Activity.this, msg_val, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Patient_Profile_Activity.this, "Something went wrong. Please try after sometime.", Toast.LENGTH_SHORT).show();
                }

                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                Model.query_launch = "profile_update";

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished / 1000);
            progressBar.setProgress(progressBar.getMax() - progress);

            int seconds = (int) (millisUntilFinished / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            System.out.println("TIME : " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

            tv_timertext.setText(minutes + " : " + seconds);
        }

        @Override
        public void onFinish() {
            System.out.println("Finish------------");
            hasStarted = false;
            timer_layout.setVisibility(View.GONE);
            //finish();
        }
    }

    private class JSON_Post_Signup extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Patient_Profile_Activity.this);
            dialog.setMessage("Submitting, Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.JSON_POST(urls[0], "Signup");

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj.has("err")) {
                    err_text = jsonobj.getString("err");
                    System.out.println("err_text----------" + err_text);
                    signup_err();

                } else {
                    if (jsonobj.has("isValid")) {

                        isvalid = jsonobj.getString("isValid");
                        System.out.println("isvalid----------" + isvalid);

                        if (isvalid.equals("true")) {

                            userid = jsonobj.getString("id");
                            Model.isValid = jsonobj.getString("isValid");
                            Model.name = jsonobj.getString("name");
                            Model.kmid = jsonobj.getString("kmid");
                            Model.mcode = jsonobj.getString("m_code");
                            Model.mnum = jsonobj.getString("m_num");
                            Model.browser_country = jsonobj.getString("browser_country");
                            Model.id = jsonobj.getString("id");
                            Model.email = jsonobj.getString("email");
                            Model.fee_q = jsonobj.getString("fee_q");
                            Model.fee_consult = jsonobj.getString("fee_consult");
                            Model.fee_q_inr = jsonobj.getString("fee_q_inr");
                            Model.fee_consult_inr = jsonobj.getString("fee_consult_inr");
                            Model.currency_symbol = jsonobj.getString("currency_symbol");
                            Model.currency_label = jsonobj.getString("currency_label");
                            Model.have_free_credit = jsonobj.getString("have_free_credit");
                            Model.token = jsonobj.getString("token");

                            if (jsonobj.has("has_free_follow")) {
                                Model.has_free_follow = jsonobj.getString("has_free_follow");
                            }

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "1");
                            editor.putString(isValid, Model.isValid);
                            editor.putString(user_name, Model.email);
                            editor.putString(Name, Model.name);
                            editor.putString(id, Model.id);
                            editor.putString(browser_country, Model.browser_country);
                            editor.putString(email, Model.email);
                            editor.putString(fee_q, Model.fee_q);
                            editor.putString(fee_consult, Model.fee_consult);
                            editor.putString(fee_q_inr, Model.fee_q_inr);
                            editor.putString(fee_consult_inr, Model.fee_consult_inr);
                            editor.putString(currency_symbol, Model.currency_symbol);
                            editor.putString(currency_label, Model.currency_label);
                            editor.putString(have_free_credit, Model.have_free_credit);
                            editor.putString(photo_url, Model.photo_url);
                            editor.putString(sp_km_id, Model.kmid);
                            editor.putString(sp_mcode, Model.mcode);
                            editor.putString(sp_mnum, Model.mnum);
                            editor.putString(sp_has_free_follow, Model.has_free_follow);
                            editor.putString(token, Model.token);
                            editor.apply();
                            //============================================================

                            //============================================================
                            SharedPreferences.Editor editor2 = sharedpreferences.edit();
                            editor2.putString(first_query, "yes");
                            Model.first_query = "yes";
                            editor.apply();
                            //============================================================

                            System.out.println("userid--------------" + userid);

                            //success();

                            try {
                                //----------- Flurry -------------------------------------------------
                                FlurryAgent.setUserId(Model.kmid);
                                Map<String, String> articleParams = new HashMap<String, String>();
                                articleParams.put("android.patient.name:", edtname.getText().toString());
                                articleParams.put("android.patient.emailid", edtemail.getText().toString());
                                articleParams.put("android.patient.App_Version", Model.App_ver);
                                articleParams.put("android.patient.pwd", edtpassword.getText().toString());
                                articleParams.put("android.patient.mobno", mobno);
                                articleParams.put("android.patient.token", Model.token);
                                articleParams.put("android.patient.country", country);
                                FlurryAgent.logEvent("android.patient.Signup_Success", articleParams);
                                //----------- Flurry -------------------------------------------------

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();

                            //((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                            finishAffinity();
                            Intent i = new Intent(Patient_Profile_Activity.this, Connect_Home_Actvity.class);
                            startActivity(i);
                            finish();
                            //-----------------------------------------------------*/

                        } else {
                            Toast.makeText(getApplicationContext(), "Registration Failed. Try again.!", Toast.LENGTH_SHORT).show();
                            //fail_signup();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Failed. Try again.!", Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class Async_SendOTP extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Patient_Profile_Activity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.JSON_POST(urls[0], "SendOTP");

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("jsonobj-------------" + jsonobj.toString());

                pin_val = jsonobj.getString("pin");

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

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

            //dob_val = cons_select_date;
            btn_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            System.out.println("cons_select_date---------" + cons_select_date);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hash_map_title() {
        tit_map.put("Mr.", "1");
        tit_map.put("Miss.", "2");
        tit_map.put("Mrs.", "3");
        tit_map.put("Baby", "5");
    }


    public void hashmap_bg() {
        bg_map.put("Don't Know", "0");
        bg_map.put("O+", "1");
        bg_map.put("A+", "2");
        bg_map.put("B+", "3");
        bg_map.put("AB+", "4");
        bg_map.put("O-", "5");
        bg_map.put("A-", "6");
        bg_map.put("B-", "7");
        bg_map.put("AB-", "8");
        bg_map.put("Other", "9");
    }


    public void hashmap_relation() {
        gen_map.put("Myself", "1");
        gen_map.put("Father", "2");
        gen_map.put("Mother", "3");
        gen_map.put("Brother", "4");
        gen_map.put("Sister", "5");
        gen_map.put("Husband", "6");
        gen_map.put("Wife", "7");
        gen_map.put("Son", "8");
        gen_map.put("Daughter", "9");
        gen_map.put("Cousin", "10");
        gen_map.put("Grand Father", "11");
        gen_map.put("Grand Mother", "12");
        gen_map.put("Friend", "13");
        gen_map.put("Others", "14");
    }

    public void hashmap_height() {

        final ArrayAdapter<String> height_categories = new ArrayAdapter<String>(Patient_Profile_Activity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        height_categories.add("1' 0\" (30.48 cm)");
        ht_map.put("1' 0\" (30.48 cm)", "1");

        height_categories.add("1' 1\" (33.02 cm)");
        ht_map.put("1' 1\" (33.02 cm)", "2");

        height_categories.add("1' 2\" (35.56 cm)");
        ht_map.put("1' 2\" (35.56 cm)", "3");
        height_categories.add("1' 3\" (38.1 cm)");
        ht_map.put("1' 3\" (38.1 cm)", "4");
        height_categories.add("1' 4\" (40.64 cm)");
        ht_map.put("1' 4\" (40.64 cm)", "5");
        height_categories.add("1' 5\" (43.18 cm)");
        ht_map.put("1' 5\" (43.18 cm)", "6");
        height_categories.add("1' 6\" (45.72 cm)");
        ht_map.put("1' 6\" (45.72 cm)", "7");
        height_categories.add("1' 7\" (48.26 cm)");
        ht_map.put("1' 7\" (48.26 cm)", "8");
        height_categories.add("1' 8\" (50.8 cm)");
        ht_map.put("1' 8\" (50.8 cm)", "9");
        height_categories.add("1' 9\" (53.34 cm)");
        ht_map.put("1' 9\" (53.34 cm)", "10");
        height_categories.add("1' 10\" (55.88 cm)");
        ht_map.put("1' 10\" (55.88 cm)", "11");
        height_categories.add("1' 11\" (58.42 cm)");
        ht_map.put("1' 11\" (58.42 cm)", "12");
        height_categories.add("2' 0\" (60.96 cm)");
        ht_map.put("2' 0\" (60.96 cm)", "13");
        height_categories.add("2' 1\" (63.5 cm)");
        ht_map.put("2' 1\" (63.5 cm)", "14");
        height_categories.add("2' 2\" (66.04 cm)");
        ht_map.put("2' 2\" (66.04 cm)", "15");
        height_categories.add("2' 3\" (68.58 cm)");
        ht_map.put("2' 3\" (68.58 cm)", "16");
        height_categories.add("2' 4\" (71.12 cm)");
        ht_map.put("2' 4\" (71.12 cm)", "17");
        height_categories.add("2' 5\" (73.66 cm)");
        ht_map.put("2' 5\" (73.66 cm)", "18");
        height_categories.add("2' 6\" (76.2 cm)");
        ht_map.put("2' 6\" (76.2 cm)", "19");
        height_categories.add("2' 7\" (78.74 cm)");
        ht_map.put("2' 7\" (78.74 cm)", "20");
        height_categories.add("2' 8\" (81.28 cm)");
        ht_map.put("2' 8\" (81.28 cm)", "21");
        height_categories.add("2' 9\" (83.82 cm)");
        ht_map.put("2' 9\" (83.82 cm)", "22");
        height_categories.add("2' 10\" (86.36 cm)");
        ht_map.put("2' 10\" (86.36 cm)", "23");
        height_categories.add("2' 11\" (88.9 cm)");
        ht_map.put("2' 11\" (88.9 cm)", "24");
        height_categories.add("3' 0\" (91.44 cm)");
        ht_map.put("3' 0\" (91.44 cm)", "25");
        height_categories.add("3' 1\" (93.98 cm)");
        ht_map.put("3' 1\" (93.98 cm)", "26");
        height_categories.add("3' 2\" (96.52 cm)");
        ht_map.put("3' 2\" (96.52 cm)", "27");
        height_categories.add("3' 3\" (99.06 cm)");
        ht_map.put("3' 3\" (99.06 cm)", "28");
        height_categories.add("3' 4\" (101.6 cm)");
        ht_map.put("3' 4\" (101.6 cm)", "29");
        height_categories.add("3' 5\" (104.14 cm)");
        ht_map.put("3' 5\" (104.14 cm)", "30");
        height_categories.add("3' 6\" (106.68 cm)");
        ht_map.put("3' 6\" (106.68 cm)", "31");
        height_categories.add("3' 7\" (109.22 cm)");
        ht_map.put("3' 7\" (109.22 cm)", "32");
        height_categories.add("3' 8\" (111.76 cm)");
        ht_map.put("3' 8\" (111.76 cm)", "33");
        height_categories.add("3' 9\" (114.3 cm)");
        ht_map.put("3' 9\" (114.3 cm)", "34");
        height_categories.add("3' 10\" (116.84 cm)");
        ht_map.put("3' 10\" (116.84 cm)", "35");
        height_categories.add("3' 11\" (119.38 cm)");
        ht_map.put("3' 11\" (119.38 cm)", "36");
        height_categories.add("4' 0\" (121.92 cm)");
        ht_map.put("4' 0\" (121.92 cm)", "37");
        height_categories.add("4' 1\" (124.46 cm)");
        ht_map.put("4' 1\" (124.46 cm)", "38");
        height_categories.add("4' 2\" (127 cm)");
        ht_map.put("4' 2\" (127 cm)", "39");
        height_categories.add("4' 3\" (129.54 cm)");
        ht_map.put("4' 3\" (129.54 cm)", "40");
        height_categories.add("4' 4\" (132.08 cm)");
        ht_map.put("4' 4\" (132.08 cm)", "41");
        height_categories.add("4' 5\" (134.62 cm)");
        ht_map.put("4' 5\" (134.62 cm)", "42");
        height_categories.add("4' 6\" (137.16 cm)");
        ht_map.put("4' 6\" (137.16 cm)", "43");
        height_categories.add("4' 7\" (139.7 cm)");
        ht_map.put("4' 7\" (139.7 cm)", "44");
        height_categories.add("4' 8\" (142.24 cm)");
        ht_map.put("4' 8\" (142.24 cm)", "45");
        height_categories.add("4' 9\" (144.78 cm)");
        ht_map.put("4' 9\" (144.78 cm)", "46");
        height_categories.add("4' 10\" (147.32 cm)");
        ht_map.put("4' 10\" (147.32 cm)", "47");
        height_categories.add("4' 11\" (149.86 cm)");
        ht_map.put("4' 11\" (149.86 cm)", "48");
        height_categories.add("5' 0\" (152.4 cm)");
        ht_map.put("5' 0\" (152.4 cm)", "49");
        height_categories.add("5' 1\" (154.94 cm)");
        ht_map.put("5' 1\" (154.94 cm)", "50");
        height_categories.add("5' 2\" (157.48 cm)");
        ht_map.put("5' 2\" (157.48 cm)", "51");
        height_categories.add("5' 3\" (160.02 cm)");
        ht_map.put("5' 3\" (160.02 cm)", "52");
        height_categories.add("5' 4\" (162.56 cm)");
        ht_map.put("5' 4\" (162.56 cm)", "53");
        height_categories.add("5' 5\" (165.1 cm)");
        ht_map.put("5' 5\" (165.1 cm)", "54");
        height_categories.add("5' 6\" (167.64 cm)");
        ht_map.put("5' 6\" (167.64 cm)", "55");
        height_categories.add("5' 7\" (170.18 cm)");
        ht_map.put("5' 7\" (170.18 cm)", "56");
        height_categories.add("5' 8\" (172.72 cm)");
        ht_map.put("5' 8\" (172.72 cm)", "57");
        height_categories.add("5' 9\" (175.26 cm)");
        ht_map.put("5' 9\" (175.26 cm)", "58");
        height_categories.add("5' 10\" (177.8 cm)");
        ht_map.put("5' 10\" (177.8 cm)", "59");
        height_categories.add("5' 11\" (180.34 cm)");
        ht_map.put("5' 11\" (180.34 cm)", "60");
        height_categories.add("6' 0\" (182.88 cm)");
        ht_map.put("6' 0\" (182.88 cm)", "61");
        height_categories.add("6' 1\" (185.42 cm)");
        ht_map.put("6' 1\" (185.42 cm)", "62");
        height_categories.add("6' 2\" (187.96 cm)");
        ht_map.put("6' 2\" (187.96 cm)", "63");
        height_categories.add("6' 3\" (190.5 cm)");
        ht_map.put("6' 3\" (190.5 cm)", "64");
        height_categories.add("6' 4\" (193.04 cm)");
        ht_map.put("6' 4\" (193.04 cm)", "65");
        height_categories.add("6' 5\" (195.58 cm)");
        ht_map.put("6' 5\" (195.58 cm)", "66");
        height_categories.add("6' 6\" (198.12 cm)");
        ht_map.put("6' 6\" (198.12 cm)", "67");
        height_categories.add("6' 7\" (200.66 cm)");
        ht_map.put("6' 7\" (200.66 cm)", "68");
        height_categories.add("6' 8\" (203.2 cm)");
        ht_map.put("6' 8\" (203.2 cm)", "69");
        height_categories.add("6' 9\" (205.74 cm)");
        ht_map.put("6' 9\" (205.74 cm)", "70");
        height_categories.add("6' 10\" (208.28 cm)");
        ht_map.put("6' 10\" (208.28 cm)", "71");
        height_categories.add("6' 11\" (210.82 cm)");
        ht_map.put("6' 11\" (210.82 cm)", "72");
        height_categories.add("7' 0\" (213.36 cm)");
        ht_map.put("7' 0\" (213.36 cm)", "73");
        height_categories.add("7' 1\" (215.9 cm)");
        ht_map.put("7' 1\" (215.9 cm)", "74");
        height_categories.add("7' 2\" (218.44 cm)");
        ht_map.put("7' 2\" (218.44 cm)", "75");
        height_categories.add("7' 3\" (220.98 cm)");
        ht_map.put("7' 3\" (220.98 cm)", "76");
        height_categories.add("7' 4\" (223.52 cm)");
        ht_map.put("7' 4\" (223.52 cm)", "77");
        height_categories.add("7' 5\" (226.06 cm)");
        ht_map.put("7' 5\" (226.06 cm)", "78");
        height_categories.add("7' 6\" (228.6 cm)");
        ht_map.put("7' 6\" (228.6 cm)", "79");
        height_categories.add("7' 7\" (231.14 cm)");
        ht_map.put("7' 7\" (231.14 cm)", "80");
        height_categories.add("7' 8\" (233.68 cm)");
        ht_map.put("7' 8\" (233.68 cm)", "81");
        height_categories.add("7' 9\" (236.22 cm)");
        ht_map.put("7' 9\" (236.22 cm)", "82");
        height_categories.add("7' 10\" (238.76 cm)");
        ht_map.put("7' 10\" (238.76 cm)", "83");
        height_categories.add("7' 11\" (241.3 cm)");
        ht_map.put("7' 11\" (241.3 cm)", "84");
        height_categories.add("8' 0\" (243.84 cm)");
        ht_map.put("8' 0\" (243.84 cm)", "85");
        height_categories.add("8' 1\" (246.38 cm)");
        ht_map.put("8' 1\" (246.38 cm)", "86");
        height_categories.add("8' 2\" (248.92 cm)");
        ht_map.put("8' 2\" (248.92 cm)", "87");
        height_categories.add("8' 3\" (251.46 cm)");
        ht_map.put("8' 3\" (251.46 cm)", "88");
        height_categories.add("8' 4\" (254 cm)");
        ht_map.put("8' 4\" (254 cm)", "89");
        height_categories.add("8' 5\" (256.54 cm)");
        ht_map.put("8' 5\" (256.54 cm)", "90");
        height_categories.add("8' 6\" (259.08 cm)");
        ht_map.put("8' 6\" (259.08 cm)", "91");
        height_categories.add("8' 7\" (261.62 cm)");
        ht_map.put("8' 7\" (261.62 cm)", "92");
        height_categories.add("8' 8\" (264.16 cm)");
        ht_map.put("8' 8\" (264.16 cm)", "93");
        height_categories.add("8' 9\" (266.7 cm)");
        ht_map.put("8' 9\" (266.7 cm)", "94");
        height_categories.add("8' 10\" (269.24 cm)");
        ht_map.put("8' 10\" (269.24 cm)", "95");
        height_categories.add("8' 11\" (271.78 cm)");
        ht_map.put("8' 11\" (271.78 cm)", "96");


    }

    public void hashmap_weight() {

        final ArrayAdapter<String> weight_categories = new ArrayAdapter<String>(Patient_Profile_Activity.this, R.layout.dialog_list_textview);

        weight_categories.add("1 lbs (0.45 kg)");
        wt_map.put("1 lbs (0.45 kg)", "1");
        weight_categories.add("2 lbs (0.91 kg)");
        wt_map.put("2 lbs (0.91 kg)", "2");
        weight_categories.add("3 lbs (1.36 kg)");
        wt_map.put("3 lbs (1.36 kg)", "3");
        weight_categories.add("4 lbs (1.81 kg)");
        wt_map.put("4 lbs (1.81 kg)", "4");
        weight_categories.add("5 lbs (2.27 kg)");
        wt_map.put("5 lbs (2.27 kg)", "5");
        weight_categories.add("6 lbs (2.72 kg)");
        wt_map.put("6 lbs (2.72 kg)", "6");
        weight_categories.add("7 lbs (3.18 kg)");
        wt_map.put("7 lbs (3.18 kg)", "7");
        weight_categories.add("8 lbs (3.63 kg)");
        wt_map.put("8 lbs (3.63 kg)", "8");
        weight_categories.add("9 lbs (4.08 kg)");
        wt_map.put("9 lbs (4.08 kg)", "9");
        weight_categories.add("10 lbs (4.54 kg)");
        wt_map.put("10 lbs (4.54 kg)", "10");
        weight_categories.add("11 lbs (4.99 kg)");
        wt_map.put("11 lbs (4.99 kg)", "11");
        weight_categories.add("12 lbs (5.44 kg)");
        wt_map.put("12 lbs (5.44 kg)", "12");
        weight_categories.add("13 lbs (5.9 kg)");
        wt_map.put("13 lbs (5.9 kg)", "13");
        weight_categories.add("14 lbs (6.35 kg)");
        wt_map.put("14 lbs (6.35 kg)", "14");
        weight_categories.add("15 lbs (6.8 kg)");
        wt_map.put("15 lbs (6.8 kg)", "15");
        weight_categories.add("16 lbs (7.26 kg)");
        wt_map.put("16 lbs (7.26 kg)", "16");
        weight_categories.add("17 lbs (7.71 kg)");
        wt_map.put("17 lbs (7.71 kg)", "17");
        weight_categories.add("18 lbs (8.16 kg)");
        wt_map.put("18 lbs (8.16 kg)", "18");
        weight_categories.add("19 lbs (8.62 kg)");
        wt_map.put("19 lbs (8.62 kg)", "19");
        weight_categories.add("20 lbs (9.07 kg)");
        wt_map.put("20 lbs (9.07 kg)", "20");
        weight_categories.add("21 lbs (9.53 kg)");
        wt_map.put("21 lbs (9.53 kg)", "21");
        weight_categories.add("22 lbs (9.98 kg)");
        wt_map.put("22 lbs (9.98 kg)", "22");
        weight_categories.add("23 lbs (10.43 kg)");
        wt_map.put("23 lbs (10.43 kg)", "23");
        weight_categories.add("24 lbs (10.89 kg)");
        wt_map.put("24 lbs (10.89 kg)", "24");
        weight_categories.add("25 lbs (11.34 kg)");
        wt_map.put("25 lbs (11.34 kg)", "25");
        weight_categories.add("26 lbs (11.79 kg)");
        wt_map.put("26 lbs (11.79 kg)", "26");
        weight_categories.add("27 lbs (12.25 kg)");
        wt_map.put("27 lbs (12.25 kg)", "27");
        weight_categories.add("28 lbs (12.7 kg)");
        wt_map.put("28 lbs (12.7 kg)", "28");
        weight_categories.add("29 lbs (13.15 kg)");
        wt_map.put("29 lbs (13.15 kg)", "29");
        weight_categories.add("30 lbs (13.61 kg)");
        wt_map.put("30 lbs (13.61 kg)", "30");
        weight_categories.add("31 lbs (14.06 kg)");
        wt_map.put("31 lbs (14.06 kg)", "31");
        weight_categories.add("32 lbs (14.51 kg)");
        wt_map.put("32 lbs (14.51 kg)", "32");
        weight_categories.add("33 lbs (14.97 kg)");
        wt_map.put("33 lbs (14.97 kg)", "33");
        weight_categories.add("34 lbs (15.42 kg)");
        wt_map.put("34 lbs (15.42 kg)", "34");
        weight_categories.add("35 lbs (15.88 kg)");
        wt_map.put("35 lbs (15.88 kg)", "35");
        weight_categories.add("36 lbs (16.33 kg)");
        wt_map.put("36 lbs (16.33 kg)", "36");
        weight_categories.add("37 lbs (16.78 kg)");
        wt_map.put("37 lbs (16.78 kg)", "37");
        weight_categories.add("38 lbs (17.24 kg)");
        wt_map.put("38 lbs (17.24 kg)", "38");
        weight_categories.add("39 lbs (17.69 kg)");
        wt_map.put("39 lbs (17.69 kg)", "39");
        weight_categories.add("40 lbs (18.14 kg)");
        wt_map.put("40 lbs (18.14 kg)", "40");
        weight_categories.add("41 lbs (18.6 kg)");
        wt_map.put("41 lbs (18.6 kg)", "41");
        weight_categories.add("42 lbs (19.05 kg)");
        wt_map.put("42 lbs (19.05 kg)", "42");
        weight_categories.add("43 lbs (19.5 kg)");
        wt_map.put("43 lbs (19.5 kg)", "43");
        weight_categories.add("44 lbs (19.96 kg)");
        wt_map.put("44 lbs (19.96 kg)", "44");
        weight_categories.add("45 lbs (20.41 kg)");
        wt_map.put("45 lbs (20.41 kg)", "45");
        weight_categories.add("46 lbs (20.87 kg)");
        wt_map.put("46 lbs (20.87 kg)", "46");
        weight_categories.add("47 lbs (21.32 kg)");
        wt_map.put("47 lbs (21.32 kg)", "47");
        weight_categories.add("48 lbs (21.77 kg)");
        wt_map.put("48 lbs (21.77 kg)", "48");
        weight_categories.add("49 lbs (22.23 kg)");
        wt_map.put("49 lbs (22.23 kg)", "49");
        weight_categories.add("50 lbs (22.68 kg)");
        wt_map.put("50 lbs (22.68 kg)", "50");
        weight_categories.add("51 lbs (23.13 kg)");
        wt_map.put("51 lbs (23.13 kg)", "51");
        weight_categories.add("52 lbs (23.59 kg)");
        wt_map.put("52 lbs (23.59 kg)", "52");
        weight_categories.add("53 lbs (24.04 kg)");
        wt_map.put("53 lbs (24.04 kg)", "53");
        weight_categories.add("54 lbs (24.49 kg)");
        wt_map.put("54 lbs (24.49 kg)", "54");
        weight_categories.add("55 lbs (24.95 kg)");
        wt_map.put("55 lbs (24.95 kg)", "55");
        weight_categories.add("56 lbs (25.4 kg)");
        wt_map.put("56 lbs (25.4 kg)", "56");
        weight_categories.add("57 lbs (25.85 kg)");
        wt_map.put("57 lbs (25.85 kg)", "57");
        weight_categories.add("58 lbs (26.31 kg)");
        wt_map.put("58 lbs (26.31 kg)", "58");
        weight_categories.add("59 lbs (26.76 kg)");
        wt_map.put("59 lbs (26.76 kg)", "59");
        weight_categories.add("60 lbs (27.22 kg)");
        wt_map.put("60 lbs (27.22 kg)", "60");
        weight_categories.add("61 lbs (27.67 kg)");
        wt_map.put("61 lbs (27.67 kg)", "61");
        weight_categories.add("62 lbs (28.12 kg)");
        wt_map.put("62 lbs (28.12 kg)", "62");
        weight_categories.add("63 lbs (28.58 kg)");
        wt_map.put("63 lbs (28.58 kg)", "63");
        weight_categories.add("64 lbs (29.03 kg)");
        wt_map.put("64 lbs (29.03 kg)", "64");
        weight_categories.add("65 lbs (29.48 kg)");
        wt_map.put("65 lbs (29.48 kg)", "65");
        weight_categories.add("66 lbs (29.94 kg)");
        wt_map.put("66 lbs (29.94 kg)", "66");
        weight_categories.add("67 lbs (30.39 kg)");
        wt_map.put("67 lbs (30.39 kg)", "67");
        weight_categories.add("68 lbs (30.84 kg)");
        wt_map.put("68 lbs (30.84 kg)", "68");
        weight_categories.add("69 lbs (31.3 kg)");
        wt_map.put("69 lbs (31.3 kg)", "69");
        weight_categories.add("70 lbs (31.75 kg)");
        wt_map.put("70 lbs (31.75 kg)", "70");
        weight_categories.add("71 lbs (32.21 kg)");
        wt_map.put("71 lbs (32.21 kg)", "71");
        weight_categories.add("72 lbs (32.66 kg)");
        wt_map.put("72 lbs (32.66 kg)", "72");
        weight_categories.add("73 lbs (33.11 kg)");
        wt_map.put("73 lbs (33.11 kg)", "73");
        weight_categories.add("74 lbs (33.57 kg)");
        wt_map.put("74 lbs (33.57 kg)", "74");
        weight_categories.add("75 lbs (34.02 kg)");
        wt_map.put("75 lbs (34.02 kg)", "75");
        weight_categories.add("76 lbs (34.47 kg)");
        wt_map.put("76 lbs (34.47 kg)", "76");
        weight_categories.add("77 lbs (34.93 kg)");
        wt_map.put("77 lbs (34.93 kg)", "77");
        weight_categories.add("78 lbs (35.38 kg)");
        wt_map.put("78 lbs (35.38 kg)", "78");
        weight_categories.add("79 lbs (35.83 kg)");
        wt_map.put("79 lbs (35.83 kg)", "79");
        weight_categories.add("80 lbs (36.29 kg)");
        wt_map.put("80 lbs (36.29 kg)", "80");
        weight_categories.add("81 lbs (36.74 kg)");
        wt_map.put("81 lbs (36.74 kg)", "81");
        weight_categories.add("82 lbs (37.19 kg)");
        wt_map.put("82 lbs (37.19 kg)", "82");
        weight_categories.add("83 lbs (37.65 kg)");
        wt_map.put("83 lbs (37.65 kg)", "83");
        weight_categories.add("84 lbs (38.1 kg)");
        wt_map.put("84 lbs (38.1 kg)", "84");
        weight_categories.add("85 lbs (38.56 kg)");
        wt_map.put("85 lbs (38.56 kg)", "85");
        weight_categories.add("86 lbs (39.01 kg)");
        wt_map.put("86 lbs (39.01 kg)", "86");
        weight_categories.add("87 lbs (39.46 kg)");
        wt_map.put("87 lbs (39.46 kg)", "87");
        weight_categories.add("88 lbs (39.92 kg)");
        wt_map.put("88 lbs (39.92 kg)", "88");
        weight_categories.add("89 lbs (40.37 kg)");
        wt_map.put("89 lbs (40.37 kg)", "89");
        weight_categories.add("90 lbs (40.82 kg)");
        wt_map.put("90 lbs (40.82 kg)", "90");
        weight_categories.add("91 lbs (41.28 kg)");
        wt_map.put("91 lbs (41.28 kg)", "91");
        weight_categories.add("92 lbs (41.73 kg)");
        wt_map.put("92 lbs (41.73 kg)", "92");
        weight_categories.add("93 lbs (42.18 kg)");
        wt_map.put("93 lbs (42.18 kg)", "93");
        weight_categories.add("94 lbs (42.64 kg)");
        wt_map.put("94 lbs (42.64 kg)", "94");
        weight_categories.add("95 lbs (43.09 kg)");
        wt_map.put("95 lbs (43.09 kg)", "95");
        weight_categories.add("96 lbs (43.54 kg)");
        wt_map.put("96 lbs (43.54 kg)", "96");
        weight_categories.add("97 lbs (44 kg)");
        wt_map.put("97 lbs (44 kg)", "97");
        weight_categories.add("98 lbs (44.45 kg)");
        wt_map.put("98 lbs (44.45 kg)", "98");
        weight_categories.add("99 lbs (44.91 kg)");
        wt_map.put("99 lbs (44.91 kg)", "99");
        weight_categories.add("100 lbs (45.36 kg)");
        wt_map.put("100 lbs (45.36 kg)", "100");
        weight_categories.add("101 lbs (45.81 kg)");
        wt_map.put("101 lbs (45.81 kg)", "101");
        weight_categories.add("102 lbs (46.27 kg)");
        wt_map.put("102 lbs (46.27 kg)", "102");
        weight_categories.add("103 lbs (46.72 kg)");
        wt_map.put("103 lbs (46.72 kg)", "103");
        weight_categories.add("104 lbs (47.17 kg)");
        wt_map.put("104 lbs (47.17 kg)", "104");
        weight_categories.add("105 lbs (47.63 kg)");
        wt_map.put("105 lbs (47.63 kg)", "105");
        weight_categories.add("106 lbs (48.08 kg)");
        wt_map.put("106 lbs (48.08 kg)", "106");
        weight_categories.add("107 lbs (48.53 kg)");
        wt_map.put("107 lbs (48.53 kg)", "107");
        weight_categories.add("108 lbs (48.99 kg)");
        wt_map.put("108 lbs (48.99 kg)", "108");
        weight_categories.add("109 lbs (49.44 kg)");
        wt_map.put("109 lbs (49.44 kg)", "109");
        weight_categories.add("110 lbs (49.9 kg)");
        wt_map.put("110 lbs (49.9 kg)", "110");
        weight_categories.add("111 lbs (50.35 kg)");
        wt_map.put("111 lbs (50.35 kg)", "111");
        weight_categories.add("112 lbs (50.8 kg)");
        wt_map.put("112 lbs (50.8 kg)", "112");
        weight_categories.add("113 lbs (51.26 kg)");
        wt_map.put("113 lbs (51.26 kg)", "113");
        weight_categories.add("114 lbs (51.71 kg)");
        wt_map.put("114 lbs (51.71 kg)", "114");
        weight_categories.add("115 lbs (52.16 kg)");
        wt_map.put("115 lbs (52.16 kg)", "115");
        weight_categories.add("116 lbs (52.62 kg)");
        wt_map.put("116 lbs (52.62 kg)", "116");
        weight_categories.add("117 lbs (53.07 kg)");
        wt_map.put("117 lbs (53.07 kg)", "117");
        weight_categories.add("118 lbs (53.52 kg)");
        wt_map.put("118 lbs (53.52 kg)", "118");
        weight_categories.add("119 lbs (53.98 kg)");
        wt_map.put("119 lbs (53.98 kg)", "119");
        weight_categories.add("120 lbs (54.43 kg)");
        wt_map.put("120 lbs (54.43 kg)", "120");
        weight_categories.add("121 lbs (54.88 kg)");
        wt_map.put("121 lbs (54.88 kg)", "121");
        weight_categories.add("122 lbs (55.34 kg)");
        wt_map.put("122 lbs (55.34 kg)", "122");
        weight_categories.add("123 lbs (55.79 kg)");
        wt_map.put("123 lbs (55.79 kg)", "123");
        weight_categories.add("124 lbs (56.25 kg)");
        wt_map.put("124 lbs (56.25 kg)", "124");
        weight_categories.add("125 lbs (56.7 kg)");
        wt_map.put("125 lbs (56.7 kg)", "125");
        weight_categories.add("126 lbs (57.15 kg)");
        wt_map.put("126 lbs (57.15 kg)", "126");
        weight_categories.add("127 lbs (57.61 kg)");
        wt_map.put("127 lbs (57.61 kg)", "127");
        weight_categories.add("128 lbs (58.06 kg)");
        wt_map.put("128 lbs (58.06 kg)", "128");
        weight_categories.add("129 lbs (58.51 kg)");
        wt_map.put("129 lbs (58.51 kg)", "129");
        weight_categories.add("130 lbs (58.97 kg)");
        wt_map.put("130 lbs (58.97 kg)", "130");
        weight_categories.add("131 lbs (59.42 kg)");
        wt_map.put("131 lbs (59.42 kg)", "131");
        weight_categories.add("132 lbs (59.87 kg)");
        wt_map.put("132 lbs (59.87 kg)", "132");
        weight_categories.add("133 lbs (60.33 kg)");
        wt_map.put("133 lbs (60.33 kg)", "133");
        weight_categories.add("134 lbs (60.78 kg)");
        wt_map.put("134 lbs (60.78 kg)", "134");
        weight_categories.add("135 lbs (61.23 kg)");
        wt_map.put("135 lbs (61.23 kg)", "135");
        weight_categories.add("136 lbs (61.69 kg)");
        wt_map.put("136 lbs (61.69 kg)", "136");
        weight_categories.add("137 lbs (62.14 kg)");
        wt_map.put("137 lbs (62.14 kg)", "137");
        weight_categories.add("138 lbs (62.6 kg)");
        wt_map.put("138 lbs (62.6 kg)", "138");
        weight_categories.add("139 lbs (63.05 kg)");
        wt_map.put("139 lbs (63.05 kg)", "139");
        weight_categories.add("140 lbs (63.5 kg)");
        wt_map.put("140 lbs (63.5 kg)", "140");
        weight_categories.add("141 lbs (63.96 kg)");
        wt_map.put("141 lbs (63.96 kg)", "141");
        weight_categories.add("142 lbs (64.41 kg)");
        wt_map.put("142 lbs (64.41 kg)", "142");
        weight_categories.add("143 lbs (64.86 kg)");
        wt_map.put("143 lbs (64.86 kg)", "143");
        weight_categories.add("144 lbs (65.32 kg)");
        wt_map.put("144 lbs (65.32 kg)", "144");
        weight_categories.add("145 lbs (65.77 kg)");
        wt_map.put("145 lbs (65.77 kg)", "145");
        weight_categories.add("146 lbs (66.22 kg)");
        wt_map.put("146 lbs (66.22 kg)", "146");
        weight_categories.add("147 lbs (66.68 kg)");
        wt_map.put("147 lbs (66.68 kg)", "147");
        weight_categories.add("148 lbs (67.13 kg)");
        wt_map.put("148 lbs (67.13 kg)", "148");
        weight_categories.add("149 lbs (67.59 kg)");
        wt_map.put("149 lbs (67.59 kg)", "149");
        weight_categories.add("150 lbs (68.04 kg)");
        wt_map.put("150 lbs (68.04 kg)", "150");
        weight_categories.add("151 lbs (68.49 kg)");
        wt_map.put("151 lbs (68.49 kg)", "151");
        weight_categories.add("152 lbs (68.95 kg)");
        wt_map.put("152 lbs (68.95 kg)", "152");
        weight_categories.add("153 lbs (69.4 kg)");
        wt_map.put("153 lbs (69.4 kg)", "153");
        weight_categories.add("154 lbs (69.85 kg)");
        wt_map.put("154 lbs (69.85 kg)", "154");
        weight_categories.add("155 lbs (70.31 kg)");
        wt_map.put("155 lbs (70.31 kg)", "155");
        weight_categories.add("156 lbs (70.76 kg)");
        wt_map.put("156 lbs (70.76 kg)", "156");
        weight_categories.add("157 lbs (71.21 kg)");
        wt_map.put("157 lbs (71.21 kg)", "157");
        weight_categories.add("158 lbs (71.67 kg)");
        wt_map.put("158 lbs (71.67 kg)", "158");
        weight_categories.add("159 lbs (72.12 kg)");
        wt_map.put("159 lbs (72.12 kg)", "159");
        weight_categories.add("160 lbs (72.57 kg)");
        wt_map.put("160 lbs (72.57 kg)", "160");
        weight_categories.add("161 lbs (73.03 kg)");
        wt_map.put("161 lbs (73.03 kg)", "161");
        weight_categories.add("162 lbs (73.48 kg)");
        wt_map.put("162 lbs (73.48 kg)", "162");
        weight_categories.add("163 lbs (73.94 kg)");
        wt_map.put("163 lbs (73.94 kg)", "163");
        weight_categories.add("164 lbs (74.39 kg)");
        wt_map.put("164 lbs (74.39 kg)", "164");
        weight_categories.add("165 lbs (74.84 kg)");
        wt_map.put("165 lbs (74.84 kg)", "165");
        weight_categories.add("166 lbs (75.3 kg)");
        wt_map.put("166 lbs (75.3 kg)", "166");
        weight_categories.add("167 lbs (75.75 kg)");
        wt_map.put("167 lbs (75.75 kg)", "167");
        weight_categories.add("168 lbs (76.2 kg)");
        wt_map.put("168 lbs (76.2 kg)", "168");
        weight_categories.add("169 lbs (76.66 kg)");
        wt_map.put("169 lbs (76.66 kg)", "169");
        weight_categories.add("170 lbs (77.11 kg)");
        wt_map.put("170 lbs (77.11 kg)", "170");
        weight_categories.add("171 lbs (77.56 kg)");
        wt_map.put("171 lbs (77.56 kg)", "171");
        weight_categories.add("172 lbs (78.02 kg)");
        wt_map.put("172 lbs (78.02 kg)", "172");
        weight_categories.add("173 lbs (78.47 kg)");
        wt_map.put("173 lbs (78.47 kg)", "173");
        weight_categories.add("174 lbs (78.93 kg)");
        wt_map.put("174 lbs (78.93 kg)", "174");
        weight_categories.add("175 lbs (79.38 kg)");
        wt_map.put("175 lbs (79.38 kg)", "175");
        weight_categories.add("176 lbs (79.83 kg)");
        wt_map.put("176 lbs (79.83 kg)", "176");
        weight_categories.add("177 lbs (80.29 kg)");
        wt_map.put("177 lbs (80.29 kg)", "177");
        weight_categories.add("178 lbs (80.74 kg)");
        wt_map.put("178 lbs (80.74 kg)", "178");
        weight_categories.add("179 lbs (81.19 kg)");
        wt_map.put("179 lbs (81.19 kg)", "179");
        weight_categories.add("180 lbs (81.65 kg)");
        wt_map.put("180 lbs (81.65 kg)", "180");
        weight_categories.add("181 lbs (82.1 kg)");
        wt_map.put("181 lbs (82.1 kg)", "181");
        weight_categories.add("182 lbs (82.55 kg)");
        wt_map.put("182 lbs (82.55 kg)", "182");
        weight_categories.add("183 lbs (83.01 kg)");
        wt_map.put("183 lbs (83.01 kg)", "183");
        weight_categories.add("184 lbs (83.46 kg)");
        wt_map.put("184 lbs (83.46 kg)", "184");
        weight_categories.add("185 lbs (83.91 kg)");
        wt_map.put("185 lbs (83.91 kg)", "185");
        weight_categories.add("186 lbs (84.37 kg)");
        wt_map.put("186 lbs (84.37 kg)", "186");
        weight_categories.add("187 lbs (84.82 kg)");
        wt_map.put("187 lbs (84.82 kg)", "187");
        weight_categories.add("188 lbs (85.28 kg)");
        wt_map.put("188 lbs (85.28 kg)", "188");
        weight_categories.add("189 lbs (85.73 kg)");
        wt_map.put("189 lbs (85.73 kg)", "189");
        weight_categories.add("190 lbs (86.18 kg)");
        wt_map.put("190 lbs (86.18 kg)", "190");
        weight_categories.add("191 lbs (86.64 kg)");
        wt_map.put("191 lbs (86.64 kg)", "191");
        weight_categories.add("192 lbs (87.09 kg)");
        wt_map.put("192 lbs (87.09 kg)", "192");
        weight_categories.add("193 lbs (87.54 kg)");
        wt_map.put("193 lbs (87.54 kg)", "193");
        weight_categories.add("194 lbs (88 kg)");
        wt_map.put("194 lbs (88 kg)", "194");
        weight_categories.add("195 lbs (88.45 kg)");
        wt_map.put("195 lbs (88.45 kg)", "195");
        weight_categories.add("196 lbs (88.9 kg)");
        wt_map.put("196 lbs (88.9 kg)", "196");
        weight_categories.add("197 lbs (89.36 kg)");
        wt_map.put("197 lbs (89.36 kg)", "197");
        weight_categories.add("198 lbs (89.81 kg)");
        wt_map.put("198 lbs (89.81 kg)", "198");
        weight_categories.add("199 lbs (90.26 kg)");
        wt_map.put("199 lbs (90.26 kg)", "199");
        weight_categories.add("200 lbs (90.72 kg)");
        wt_map.put("200 lbs (90.72 kg)", "200");
        weight_categories.add("201 lbs (91.17 kg)");
        wt_map.put("201 lbs (91.17 kg)", "201");
        weight_categories.add("202 lbs (91.63 kg)");
        wt_map.put("202 lbs (91.63 kg)", "202");
        weight_categories.add("203 lbs (92.08 kg)");
        wt_map.put("203 lbs (92.08 kg)", "203");
        weight_categories.add("204 lbs (92.53 kg)");
        wt_map.put("204 lbs (92.53 kg)", "204");
        weight_categories.add("205 lbs (92.99 kg)");
        wt_map.put("205 lbs (92.99 kg)", "205");
        weight_categories.add("206 lbs (93.44 kg)");
        wt_map.put("206 lbs (93.44 kg)", "206");
        weight_categories.add("207 lbs (93.89 kg)");
        wt_map.put("207 lbs (93.89 kg)", "207");
        weight_categories.add("208 lbs (94.35 kg)");
        wt_map.put("208 lbs (94.35 kg)", "208");
        weight_categories.add("209 lbs (94.8 kg)");
        wt_map.put("209 lbs (94.8 kg)", "209");
        weight_categories.add("210 lbs (95.25 kg)");
        wt_map.put("210 lbs (95.25 kg)", "210");
        weight_categories.add("211 lbs (95.71 kg)");
        wt_map.put("211 lbs (95.71 kg)", "211");
        weight_categories.add("212 lbs (96.16 kg)");
        wt_map.put("212 lbs (96.16 kg)", "212");
        weight_categories.add("213 lbs (96.62 kg)");
        wt_map.put("213 lbs (96.62 kg)", "213");
        weight_categories.add("214 lbs (97.07 kg)");
        wt_map.put("214 lbs (97.07 kg)", "214");
        weight_categories.add("215 lbs (97.52 kg)");
        wt_map.put("215 lbs (97.52 kg)", "215");
        weight_categories.add("216 lbs (97.98 kg)");
        wt_map.put("216 lbs (97.98 kg)", "216");
        weight_categories.add("217 lbs (98.43 kg)");
        wt_map.put("217 lbs (98.43 kg)", "217");
        weight_categories.add("218 lbs (98.88 kg)");
        wt_map.put("218 lbs (98.88 kg)", "218");
        weight_categories.add("219 lbs (99.34 kg)");
        wt_map.put("219 lbs (99.34 kg)", "219");
        weight_categories.add("220 lbs (99.79 kg)");
        wt_map.put("220 lbs (99.79 kg)", "220");
        weight_categories.add("221 lbs (100.24 kg)");
        wt_map.put("221 lbs (100.24 kg)", "221");
        weight_categories.add("222 lbs (100.7 kg)");
        wt_map.put("222 lbs (100.7 kg)", "222");
        weight_categories.add("223 lbs (101.15 kg)");
        wt_map.put("223 lbs (101.15 kg)", "223");
        weight_categories.add("224 lbs (101.6 kg)");
        wt_map.put("224 lbs (101.6 kg)", "224");
        weight_categories.add("225 lbs (102.06 kg)");
        wt_map.put("225 lbs (102.06 kg)", "225");
        weight_categories.add("226 lbs (102.51 kg)");
        wt_map.put("226 lbs (102.51 kg)", "226");
        weight_categories.add("227 lbs (102.97 kg)");
        wt_map.put("227 lbs (102.97 kg)", "227");
        weight_categories.add("228 lbs (103.42 kg)");
        wt_map.put("228 lbs (103.42 kg)", "228");
        weight_categories.add("229 lbs (103.87 kg)");
        wt_map.put("229 lbs (103.87 kg)", "229");
        weight_categories.add("230 lbs (104.33 kg)");
        wt_map.put("230 lbs (104.33 kg)", "230");
        weight_categories.add("231 lbs (104.78 kg)");
        wt_map.put("231 lbs (104.78 kg)", "231");
        weight_categories.add("232 lbs (105.23 kg)");
        wt_map.put("232 lbs (105.23 kg)", "232");
        weight_categories.add("233 lbs (105.69 kg)");
        wt_map.put("233 lbs (105.69 kg)", "233");
        weight_categories.add("234 lbs (106.14 kg)");
        wt_map.put("234 lbs (106.14 kg)", "234");
        weight_categories.add("235 lbs (106.59 kg)");
        wt_map.put("235 lbs (106.59 kg)", "235");
        weight_categories.add("236 lbs (107.05 kg)");
        wt_map.put("236 lbs (107.05 kg)", "236");
        weight_categories.add("237 lbs (107.5 kg)");
        wt_map.put("237 lbs (107.5 kg)", "237");
        weight_categories.add("238 lbs (107.95 kg)");
        wt_map.put("238 lbs (107.95 kg)", "238");
        weight_categories.add("239 lbs (108.41 kg)");
        wt_map.put("239 lbs (108.41 kg)", "239");
        weight_categories.add("240 lbs (108.86 kg)");
        wt_map.put("240 lbs (108.86 kg)", "240");
        weight_categories.add("241 lbs (109.32 kg)");
        wt_map.put("241 lbs (109.32 kg)", "241");
        weight_categories.add("242 lbs (109.77 kg)");
        wt_map.put("242 lbs (109.77 kg)", "242");
        weight_categories.add("243 lbs (110.22 kg)");
        wt_map.put("243 lbs (110.22 kg)", "243");
        weight_categories.add("244 lbs (110.68 kg)");
        wt_map.put("244 lbs (110.68 kg)", "244");
        weight_categories.add("245 lbs (111.13 kg)");
        wt_map.put("245 lbs (111.13 kg)", "245");
        weight_categories.add("246 lbs (111.58 kg)");
        wt_map.put("246 lbs (111.58 kg)", "246");
        weight_categories.add("247 lbs (112.04 kg)");
        wt_map.put("247 lbs (112.04 kg)", "247");
        weight_categories.add("248 lbs (112.49 kg)");
        wt_map.put("248 lbs (112.49 kg)", "248");
        weight_categories.add("249 lbs (112.94 kg)");
        wt_map.put("249 lbs (112.94 kg)", "249");
        weight_categories.add("250 lbs (113.4 kg)");
        wt_map.put("250 lbs (113.4 kg)", "250");
        weight_categories.add("251 lbs (113.85 kg)");
        wt_map.put("251 lbs (113.85 kg)", "251");
        weight_categories.add("252 lbs (114.31 kg)");
        wt_map.put("252 lbs (114.31 kg)", "252");
        weight_categories.add("253 lbs (114.76 kg)");
        wt_map.put("253 lbs (114.76 kg)", "253");
        weight_categories.add("254 lbs (115.21 kg)");
        wt_map.put("254 lbs (115.21 kg)", "254");
        weight_categories.add("255 lbs (115.67 kg)");
        wt_map.put("255 lbs (115.67 kg)", "255");
        weight_categories.add("256 lbs (116.12 kg)");
        wt_map.put("256 lbs (116.12 kg)", "256");
        weight_categories.add("257 lbs (116.57 kg)");
        wt_map.put("257 lbs (116.57 kg)", "257");
        weight_categories.add("258 lbs (117.03 kg)");
        wt_map.put("258 lbs (117.03 kg)", "258");
        weight_categories.add("259 lbs (117.48 kg)");
        wt_map.put("259 lbs (117.48 kg)", "259");
        weight_categories.add("260 lbs (117.93 kg)");
        wt_map.put("260 lbs (117.93 kg)", "260");
        weight_categories.add("261 lbs (118.39 kg)");
        wt_map.put("261 lbs (118.39 kg)", "261");
        weight_categories.add("262 lbs (118.84 kg)");
        wt_map.put("262 lbs (118.84 kg)", "262");
        weight_categories.add("263 lbs (119.29 kg)");
        wt_map.put("263 lbs (119.29 kg)", "263");
        weight_categories.add("264 lbs (119.75 kg)");
        wt_map.put("264 lbs (119.75 kg)", "264");
        weight_categories.add("265 lbs (120.2 kg)");
        wt_map.put("265 lbs (120.2 kg)", "265");
        weight_categories.add("266 lbs (120.66 kg)");
        wt_map.put("266 lbs (120.66 kg)", "266");
        weight_categories.add("267 lbs (121.11 kg)");
        wt_map.put("267 lbs (121.11 kg)", "267");
        weight_categories.add("268 lbs (121.56 kg)");
        wt_map.put("268 lbs (121.56 kg)", "268");
        weight_categories.add("269 lbs (122.02 kg)");
        wt_map.put("269 lbs (122.02 kg)", "269");
        weight_categories.add("270 lbs (122.47 kg)");
        wt_map.put("270 lbs (122.47 kg)", "270");
        weight_categories.add("271 lbs (122.92 kg)");
        wt_map.put("271 lbs (122.92 kg)", "271");
        weight_categories.add("272 lbs (123.38 kg)");
        wt_map.put("272 lbs (123.38 kg)", "272");
        weight_categories.add("273 lbs (123.83 kg)");
        wt_map.put("273 lbs (123.83 kg)", "273");
        weight_categories.add("274 lbs (124.28 kg)");
        wt_map.put("274 lbs (124.28 kg)", "274");
        weight_categories.add("275 lbs (124.74 kg)");
        wt_map.put("275 lbs (124.74 kg)", "275");
        weight_categories.add("276 lbs (125.19 kg)");
        wt_map.put("276 lbs (125.19 kg)", "276");
        weight_categories.add("277 lbs (125.65 kg)");
        wt_map.put("277 lbs (125.65 kg)", "277");
        weight_categories.add("278 lbs (126.1 kg)");
        wt_map.put("278 lbs (126.1 kg)", "278");
        weight_categories.add("279 lbs (126.55 kg)");
        wt_map.put("279 lbs (126.55 kg)", "279");
        weight_categories.add("280 lbs (127.01 kg)");
        wt_map.put("280 lbs (127.01 kg)", "280");
        weight_categories.add("281 lbs (127.46 kg)");
        wt_map.put("281 lbs (127.46 kg)", "281");
        weight_categories.add("282 lbs (127.91 kg)");
        wt_map.put("282 lbs (127.91 kg)", "282");
        weight_categories.add("283 lbs (128.37 kg)");
        wt_map.put("283 lbs (128.37 kg)", "283");
        weight_categories.add("284 lbs (128.82 kg)");
        wt_map.put("284 lbs (128.82 kg)", "284");
        weight_categories.add("285 lbs (129.27 kg)");
        wt_map.put("285 lbs (129.27 kg)", "285");
        weight_categories.add("286 lbs (129.73 kg)");
        wt_map.put("286 lbs (129.73 kg)", "286");
        weight_categories.add("287 lbs (130.18 kg)");
        wt_map.put("287 lbs (130.18 kg)", "287");
        weight_categories.add("288 lbs (130.63 kg)");
        wt_map.put("288 lbs (130.63 kg)", "288");
        weight_categories.add("289 lbs (131.09 kg)");
        wt_map.put("289 lbs (131.09 kg)", "289");
        weight_categories.add("290 lbs (131.54 kg)");
        wt_map.put("290 lbs (131.54 kg)", "290");
        weight_categories.add("291 lbs (132 kg)");
        wt_map.put("291 lbs (132 kg)", "291");
        weight_categories.add("292 lbs (132.45 kg)");
        wt_map.put("292 lbs (132.45 kg)", "292");
        weight_categories.add("293 lbs (132.9 kg)");
        wt_map.put("293 lbs (132.9 kg)", "293");
        weight_categories.add("294 lbs (133.36 kg)");
        wt_map.put("294 lbs (133.36 kg)", "294");
        weight_categories.add("295 lbs (133.81 kg)");
        wt_map.put("295 lbs (133.81 kg)", "295");
        weight_categories.add("296 lbs (134.26 kg)");
        wt_map.put("296 lbs (134.26 kg)", "296");
        weight_categories.add("297 lbs (134.72 kg)");
        wt_map.put("297 lbs (134.72 kg)", "297");
        weight_categories.add("298 lbs (135.17 kg)");
        wt_map.put("298 lbs (135.17 kg)", "298");
        weight_categories.add("299 lbs (135.62 kg)");
        wt_map.put("299 lbs (135.62 kg)", "299");
        weight_categories.add("300 lbs (136.08 kg)");
        wt_map.put("300 lbs (136.08 kg)", "300");
        weight_categories.add("301 lbs (136.53 kg)");
        wt_map.put("301 lbs (136.53 kg)", "301");
        weight_categories.add("302 lbs (136.98 kg)");
        wt_map.put("302 lbs (136.98 kg)", "302");
        weight_categories.add("303 lbs (137.44 kg)");
        wt_map.put("303 lbs (137.44 kg)", "303");
        weight_categories.add("304 lbs (137.89 kg)");
        wt_map.put("304 lbs (137.89 kg)", "304");
        weight_categories.add("305 lbs (138.35 kg)");
        wt_map.put("305 lbs (138.35 kg)", "305");
        weight_categories.add("306 lbs (138.8 kg)");
        wt_map.put("306 lbs (138.8 kg)", "306");
        weight_categories.add("307 lbs (139.25 kg)");
        wt_map.put("307 lbs (139.25 kg)", "307");
        weight_categories.add("308 lbs (139.71 kg)");
        wt_map.put("308 lbs (139.71 kg)", "308");
        weight_categories.add("309 lbs (140.16 kg)");
        wt_map.put("309 lbs (140.16 kg)", "309");
        weight_categories.add("310 lbs (140.61 kg)");
        wt_map.put("310 lbs (140.61 kg)", "310");
        weight_categories.add("311 lbs (141.07 kg)");
        wt_map.put("311 lbs (141.07 kg)", "311");
        weight_categories.add("312 lbs (141.52 kg)");
        wt_map.put("312 lbs (141.52 kg)", "312");
        weight_categories.add("313 lbs (141.97 kg)");
        wt_map.put("313 lbs (141.97 kg)", "313");
        weight_categories.add("314 lbs (142.43 kg)");
        wt_map.put("314 lbs (142.43 kg)", "314");
        weight_categories.add("315 lbs (142.88 kg)");
        wt_map.put("315 lbs (142.88 kg)", "315");
        weight_categories.add("316 lbs (143.34 kg)");
        wt_map.put("316 lbs (143.34 kg)", "316");
        weight_categories.add("317 lbs (143.79 kg)");
        wt_map.put("317 lbs (143.79 kg)", "317");
        weight_categories.add("318 lbs (144.24 kg)");
        wt_map.put("318 lbs (144.24 kg)", "318");
        weight_categories.add("319 lbs (144.7 kg)");
        wt_map.put("319 lbs (144.7 kg)", "319");
        weight_categories.add("320 lbs (145.15 kg)");
        wt_map.put("320 lbs (145.15 kg)", "320");
        weight_categories.add("321 lbs (145.6 kg)");
        wt_map.put("321 lbs (145.6 kg)", "321");
        weight_categories.add("322 lbs (146.06 kg)");
        wt_map.put("322 lbs (146.06 kg)", "322");
        weight_categories.add("323 lbs (146.51 kg)");
        wt_map.put("323 lbs (146.51 kg)", "323");
        weight_categories.add("324 lbs (146.96 kg)");
        wt_map.put("324 lbs (146.96 kg)", "324");
        weight_categories.add("325 lbs (147.42 kg)");
        wt_map.put("325 lbs (147.42 kg)", "325");
        weight_categories.add("326 lbs (147.87 kg)");
        wt_map.put("326 lbs (147.87 kg)", "326");
        weight_categories.add("327 lbs (148.32 kg)");
        wt_map.put("327 lbs (148.32 kg)", "327");
        weight_categories.add("328 lbs (148.78 kg)");
        wt_map.put("328 lbs (148.78 kg)", "328");
        weight_categories.add("329 lbs (149.23 kg)");
        wt_map.put("329 lbs (149.23 kg)", "329");
        weight_categories.add("330 lbs (149.69 kg)");
        wt_map.put("330 lbs (149.69 kg)", "330");
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    public void apply_name_title() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Patient_Profile_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select title");

        final ArrayAdapter<String> categories = new ArrayAdapter<String>(Patient_Profile_Activity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

       /* tit_map.put("Mr.", "1");
        tit_map.put("Miss.", "2");
        tit_map.put("Mrs.", "3");
        tit_map.put("Baby", "5");*/

        categories.add("Mr.");
        categories.add("Miss.");
        categories.add("Mrs.");
        categories.add("Baby");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = categories.getItem(which);
                String select_value = (tit_map).get(categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                tit_val = select_value;
                tit_name = select_text;

                tv_name_title.setText(tit_name);
            }
        });

        builderSingle.show();
    }

    public void apply_blood_group() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Patient_Profile_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select blood group");

        final ArrayAdapter<String> categories = new ArrayAdapter<String>(Patient_Profile_Activity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        categories.add("Don't Know");
        categories.add("O+");
        categories.add("A+");
        categories.add("B+");
        categories.add("AB+");
        categories.add("O-");
        categories.add("A-");
        categories.add("AB-");
        categories.add("Other");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = categories.getItem(which);
                String select_value = (bg_map).get(categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                blood_group_val = select_value;
                blood_group_name = select_text;

                bg_height_title.setText(blood_group_name);
            }
        });

        builderSingle.show();
    }


    public void dialog_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Patient_Profile_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select Country code");

        final ArrayAdapter<String> categories = new ArrayAdapter<String>(Patient_Profile_Activity.this, R.layout.dialog_list_textview);

        cc_map = new HashMap<String, String>();

        categories.add("United States (+1), Canada (+1)");
        cc_map.put("United States (+1), Canada (+1)", "1");
        categories.add("United Kingdom (+44)");
        cc_map.put("United Kingdom (+44)", "44");
        categories.add("India (+91)");
        cc_map.put("India (+91)", "91");
        categories.add("Russian Federation (+7), Kazakhstan (+7)");
        cc_map.put("Russian Federation (+7), Kazakhstan (+7)", "7");
        categories.add("Egypt (+20)");
        cc_map.put("Egypt (+20)", "20");
        categories.add("South Africa (+27)");
        cc_map.put("South Africa (+27)", "27");
        categories.add("Greece (+30)");
        cc_map.put("Greece (+30)", "30");
        categories.add("Netherlands (+31)");
        cc_map.put("Netherlands (+31)", "31");
        categories.add("Belgium (+32)");
        cc_map.put("Belgium (+32)", "32");
        categories.add("France (+33)");
        cc_map.put("France (+33)", "33");
        categories.add("Spain (+34)");
        cc_map.put("Spain (+34)", "34");
        categories.add("Hungary (+36)");
        cc_map.put("Hungary (+36)", "36");
        categories.add("Italy (+39)");
        cc_map.put("Italy (+39)", "39");
        categories.add("Romania (+40)");
        cc_map.put("Romania (+40)", "40");
        categories.add("Switzerland (+41)");
        cc_map.put("Switzerland (+41)", "41");
        categories.add("Austria (+43)");
        cc_map.put("Austria (+43)", "43");
        categories.add("Denmark (+45)");
        cc_map.put("Denmark (+45)", "45");
        categories.add("Sweden (+46)");
        cc_map.put("Sweden (+46)", "46");
        categories.add("Norway (+47)");
        cc_map.put("Norway (+47)", "47");
        categories.add("Poland (+48)");
        cc_map.put("Poland (+48)", "48");
        categories.add("Germany (+49)");
        cc_map.put("Germany (+49)", "49");
        categories.add("Peru (+51)");
        cc_map.put("Peru (+51)", "51");
        categories.add("Mexico (+52)");
        cc_map.put("Mexico (+52)", "52");
        categories.add("Cuba (+53)");
        cc_map.put("Cuba (+53)", "53");
        categories.add("Argentina (+54)");
        cc_map.put("Argentina (+54)", "54");
        categories.add("Brazil (+55)");
        cc_map.put("Brazil (+55)", "55");
        categories.add("Chile (+56)");
        cc_map.put("Chile (+56)", "56");
        categories.add("Colombia (+57)");
        cc_map.put("Colombia (+57)", "57");
        categories.add("Venezuela (+58)");
        cc_map.put("Venezuela (+58)", "58");
        categories.add("Malaysia (+60)");
        cc_map.put("Malaysia (+60)", "60");
        categories.add("Australia (+61)");
        cc_map.put("Australia (+61)", "61");
        categories.add("Indonesia (+62)");
        cc_map.put("Indonesia (+62)", "62");
        categories.add("Philippines (+63)");
        cc_map.put("Philippines (+63)", "63");
        categories.add("New Zealand (+64)");
        cc_map.put("New Zealand (+64)", "64");
        categories.add("Singapore (+65)");
        cc_map.put("Singapore (+65)", "65");
        categories.add("Thailand (+66)");
        cc_map.put("Thailand (+66)", "66");
        categories.add("Japan (+81)");
        cc_map.put("Japan (+81)", "81");
        categories.add("Korea, Republic of (+82)");
        cc_map.put("Korea, Republic of (+82)", "82");
        categories.add("Viet Nam (+84)");
        cc_map.put("Viet Nam (+84)", "84");
        categories.add("China (+86)");
        cc_map.put("China (+86)", "86");
        categories.add("Turkey (+90)");
        cc_map.put("Turkey (+90)", "90");
        categories.add("Pakistan (+92)");
        cc_map.put("Pakistan (+92)", "92");
        categories.add("Afghanistan (+93)");
        cc_map.put("Afghanistan (+93)", "93");
        categories.add("Sri Lanka (+94)");
        cc_map.put("Sri Lanka (+94)", "94");
        categories.add("Myanmar (+95)");
        cc_map.put("Myanmar (+95)", "95");
        categories.add("Iran, Islamic Republic of (+98)");
        cc_map.put("Iran, Islamic Republic of (+98)", "98");
        categories.add("South Sudan (+211)");
        cc_map.put("South Sudan (+211)", "211");
        categories.add("Morocco (+212)");
        cc_map.put("Morocco (+212)", "212");
        categories.add("Algeria (+213)");
        cc_map.put("Algeria (+213)", "213");
        categories.add("Tunisia (+216)");
        cc_map.put("Tunisia (+216)", "216");
        categories.add("Libya (+218)");
        cc_map.put("Libya (+218)", "218");
        categories.add("Gambia (+220)");
        cc_map.put("Gambia (+220)", "220");
        categories.add("Senegal (+221)");
        cc_map.put("Senegal (+221)", "221");
        categories.add("Mauritania (+222)");
        cc_map.put("Mauritania (+222)", "222");
        categories.add("Mali (+223)");
        cc_map.put("Mali (+223)", "223");
        categories.add("Guinea (+224)");
        cc_map.put("Guinea (+224)", "224");
        categories.add("Cote d'Ivoire (+225)");
        cc_map.put("Cote d'Ivoire (+225)", "225");
        categories.add("Burkina Faso (+226)");
        cc_map.put("Burkina Faso (+226)", "226");
        categories.add("Niger (+227)");
        cc_map.put("Niger (+227)", "227");
        categories.add("Togo (+228)");
        cc_map.put("Togo (+228)", "228");
        categories.add("Benin (+229)");
        cc_map.put("Benin (+229)", "229");
        categories.add("Mauritius (+230)");
        cc_map.put("Mauritius (+230)", "230");
        categories.add("Liberia (+231)");
        cc_map.put("Liberia (+231)", "231");
        categories.add("Sierra Leone (+232)");
        cc_map.put("Sierra Leone (+232)", "232");
        categories.add("Ghana (+233)");
        cc_map.put("Ghana (+233)", "233");
        categories.add("Nigeria (+234)");
        cc_map.put("Nigeria (+234)", "234");
        categories.add("Chad (+235)");
        cc_map.put("Chad (+235)", "235");
        categories.add("Central African Republic (+236)");
        cc_map.put("Central African Republic (+236)", "236");
        categories.add("Cameroon (+237)");
        cc_map.put("Cameroon (+237)", "237");
        categories.add("Cape Verde (+238)");
        cc_map.put("Cape Verde (+238)", "238");
        categories.add("Sao Tome and Principe (+239)");
        cc_map.put("Sao Tome and Principe (+239)", "239");
        categories.add("Equatorial Guinea (+240)");
        cc_map.put("Equatorial Guinea (+240)", "240");
        categories.add("Gabon (+241)");
        cc_map.put("Gabon (+241)", "241");
        categories.add("Congo (+242)");
        cc_map.put("Congo (+242)", "242");
        categories.add("Democratic Republic of the Congo (+243)");
        cc_map.put("Democratic Republic of the Congo (+243)", "243");
        categories.add("Angola (+244)");
        cc_map.put("Angola (+244)", "244");
        categories.add("GuineaBissau (+245)");
        cc_map.put("GuineaBissau (+245)", "245");
        categories.add("British Indian Ocean Territory (+246)");
        cc_map.put("British Indian Ocean Territory (+246)", "246");
        categories.add("Seychelles (+248)");
        cc_map.put("Seychelles (+248)", "248");
        categories.add("Sudan (+249)");
        cc_map.put("Sudan (+249)", "249");
        categories.add("Rwanda (+250)");
        cc_map.put("Rwanda (+250)", "250");
        categories.add("Ethiopia (+251)");
        cc_map.put("Ethiopia (+251)", "251");
        categories.add("Somalia (+252)");
        cc_map.put("Somalia (+252)", "252");
        categories.add("Djibouti (+253)");
        cc_map.put("Djibouti (+253)", "253");
        categories.add("Kenya (+254)");
        cc_map.put("Kenya (+254)", "254");
        categories.add("United Republic of Tanzania (+255)");
        cc_map.put("United Republic of Tanzania (+255)", "255");
        categories.add("Uganda (+256)");
        cc_map.put("Uganda (+256)", "256");
        categories.add("Burundi (+257)");
        cc_map.put("Burundi (+257)", "257");
        categories.add("Mozambique (+258)");
        cc_map.put("Mozambique (+258)", "258");
        categories.add("Zambia (+260)");
        cc_map.put("Zambia (+260)", "260");
        categories.add("Madagascar (+261)");
        cc_map.put("Madagascar (+261)", "261");
        categories.add("Mayotte (+262)");
        cc_map.put("Mayotte (+262)", "262");
        categories.add("Zimbabwe (+263)");
        cc_map.put("Zimbabwe (+263)", "263");
        categories.add("Namibia (+264)");
        cc_map.put("Namibia (+264)", "264");
        categories.add("Malawi (+265)");
        cc_map.put("Malawi (+265)", "265");
        categories.add("Lesotho (+266)");
        cc_map.put("Lesotho (+266)", "266");
        categories.add("Botswana (+267)");
        cc_map.put("Botswana (+267)", "267");
        categories.add("Swaziland (+268)");
        cc_map.put("Swaziland (+268)", "268");
        categories.add("Comoros (+269)");
        cc_map.put("Comoros (+269)", "269");
        categories.add("Saint Helena (+290)");
        cc_map.put("Saint Helena (+290)", "290");
        categories.add("Eritrea (+291)");
        cc_map.put("Eritrea (+291)", "291");
        categories.add("Aruba (+297)");
        cc_map.put("Aruba (+297)", "297");
        categories.add("Faroe Islands (+298)");
        cc_map.put("Faroe Islands (+298)", "298");
        categories.add("Greenland (+299)");
        cc_map.put("Greenland (+299)", "299");
        categories.add("Gibraltar (+350)");
        cc_map.put("Gibraltar (+350)", "350");
        categories.add("Portugal (+351)");
        cc_map.put("Portugal (+351)", "351");
        categories.add("Luxembourg (+352)");
        cc_map.put("Luxembourg (+352)", "352");
        categories.add("Ireland (+353)");
        cc_map.put("Ireland (+353)", "353");
        categories.add("Iceland (+354)");
        cc_map.put("Iceland (+354)", "354");
        categories.add("Albania (+355)");
        cc_map.put("Albania (+355)", "355");
        categories.add("Malta (+356)");
        cc_map.put("Malta (+356)", "356");
        categories.add("Cyprus (+357)");
        cc_map.put("Cyprus (+357)", "357");
        categories.add("Finland (+358)");
        cc_map.put("Finland (+358)", "358");
        categories.add("Bulgaria (+359)");
        cc_map.put("Bulgaria (+359)", "359");
        categories.add("Lithuania (+370)");
        cc_map.put("Lithuania (+370)", "370");
        categories.add("Latvia (+371)");
        cc_map.put("Latvia (+371)", "371");
        categories.add("Estonia (+372)");
        cc_map.put("Estonia (+372)", "372");
        categories.add("Moldova, Republic of (+373)");
        cc_map.put("Moldova, Republic of (+373)", "373");
        categories.add("Armenia (+374)");
        cc_map.put("Armenia (+374)", "374");
        categories.add("Belarus (+375)");
        cc_map.put("Belarus (+375)", "375");
        categories.add("Andorra (+376)");
        cc_map.put("Andorra (+376)", "376");
        categories.add("Monaco (+377)");
        cc_map.put("Monaco (+377)", "377");
        categories.add("San Marino (+378)");
        cc_map.put("San Marino (+378)", "378");
        categories.add("Holy See (Vatican City State) (+379)");
        cc_map.put("Holy See (Vatican City State) (+379)", "379");
        categories.add("Ukraine (+380)");
        cc_map.put("Ukraine (+380)", "380");
        categories.add("Serbia (+381)");
        cc_map.put("Serbia (+381)", "381");
        categories.add("Montenegro (+382)");
        cc_map.put("Montenegro (+382)", "382");
        categories.add("Croatia (+385)");
        cc_map.put("Croatia (+385)", "385");
        categories.add("Slovenia (+386)");
        cc_map.put("Slovenia (+386)", "386");
        categories.add("Bosnia and Herzegovina (+387)");
        cc_map.put("Bosnia and Herzegovina (+387)", "387");
        categories.add("Macedonia, the Former Yugoslav Republic of (+389)");
        cc_map.put("Macedonia, the Former Yugoslav Republic of (+389)", "389");
        categories.add("Czech Republic (+420)");
        cc_map.put("Czech Republic (+420)", "420");
        categories.add("Slovakia (+421)");
        cc_map.put("Slovakia (+421)", "421");
        categories.add("Liechtenstein (+423)");
        cc_map.put("Liechtenstein (+423)", "423");
        categories.add("Falkland Islands (Malvinas) (+500)");
        cc_map.put("Falkland Islands (Malvinas) (+500)", "500");
        categories.add("Belize (+501)");
        cc_map.put("Belize (+501)", "501");
        categories.add("Guatemala (+502)");
        cc_map.put("Guatemala (+502)", "502");
        categories.add("El Salvador (+503)");
        cc_map.put("El Salvador (+503)", "503");
        categories.add("Honduras (+504)");
        cc_map.put("Honduras (+504)", "504");
        categories.add("Nicaragua (+505)");
        cc_map.put("Nicaragua (+505)", "505");
        categories.add("Costa Rica (+506)");
        cc_map.put("Costa Rica (+506)", "506");
        categories.add("Panama (+507)");
        cc_map.put("Panama (+507)", "507");
        categories.add("Saint Pierre and Miquelon (+508)");
        cc_map.put("Saint Pierre and Miquelon (+508)", "508");
        categories.add("Haiti (+509)");
        cc_map.put("Haiti (+509)", "509");
        categories.add("Saint Barthelemy (+590)");
        cc_map.put("Saint Barthelemy (+590)", "590");
        categories.add("Bolivia (+591)");
        cc_map.put("Bolivia (+591)", "591");
        categories.add("Guyana (+592)");
        cc_map.put("Guyana (+592)", "592");
        categories.add("Ecuador (+593)");
        cc_map.put("Ecuador (+593)", "593");
        categories.add("French Guiana (+594)");
        cc_map.put("French Guiana (+594)", "594");
        categories.add("Paraguay (+595)");
        cc_map.put("Paraguay (+595)", "595");
        categories.add("Martinique (+596)");
        cc_map.put("Martinique (+596)", "596");
        categories.add("Suriname (+597)");
        cc_map.put("Suriname (+597)", "597");
        categories.add("Uruguay (+598)");
        cc_map.put("Uruguay (+598)", "598");
        categories.add("Bonaire (+599), Curacao (+599)");
        cc_map.put("Bonaire (+599), Curacao (+599)", "599");
        categories.add("TimorLeste (+670)");
        cc_map.put("TimorLeste (+670)", "670");
        categories.add("Antarctica (+672)");
        cc_map.put("Antarctica (+672)", "672");
        categories.add("Brunei Darussalam (+673)");
        cc_map.put("Brunei Darussalam (+673)", "673");
        categories.add("Nauru (+674)");
        cc_map.put("Nauru (+674)", "674");
        categories.add("Papua New Guinea (+675)");
        cc_map.put("Papua New Guinea (+675)", "675");
        categories.add("Tonga (+676)");
        cc_map.put("Tonga (+676)", "676");
        categories.add("Solomon Islands (+677)");
        cc_map.put("Solomon Islands (+677)", "677");
        categories.add("Vanuatu (+678)");
        cc_map.put("Vanuatu (+678)", "678");
        categories.add("Fiji (+679)");
        cc_map.put("Fiji (+679)", "679");
        categories.add("Palau (+680)");
        cc_map.put("Palau (+680)", "680");
        categories.add("Wallis and Futuna (+681)");
        cc_map.put("Wallis and Futuna (+681)", "681");
        categories.add("Cook Islands (+682)");
        cc_map.put("Cook Islands (+682)", "682");
        categories.add("Niue (+683)");
        cc_map.put("Niue (+683)", "683");
        categories.add("Samoa (+685)");
        cc_map.put("Samoa (+685)", "685");
        categories.add("Kiribati (+686)");
        cc_map.put("Kiribati (+686)", "686");
        categories.add("New Caledonia (+687)");
        cc_map.put("New Caledonia (+687)", "687");
        categories.add("Tuvalu (+688)");
        cc_map.put("Tuvalu (+688)", "688");
        categories.add("French Polynesia (+689)");
        cc_map.put("French Polynesia (+689)", "689");
        categories.add("Tokelau (+690)");
        cc_map.put("Tokelau (+690)", "690");
        categories.add("Micronesia, Federated States of (+691)");
        cc_map.put("Micronesia, Federated States of (+691)", "691");
        categories.add("Marshall Islands (+692)");
        cc_map.put("Marshall Islands (+692)", "692");
        categories.add("Korea, Democratic People's Republic of (+850)");
        cc_map.put("Korea, Democratic People's Republic of (+850)", "850");
        categories.add("Hong Kong (+852)");
        cc_map.put("Hong Kong (+852)", "852");
        categories.add("Macao (+853)");
        cc_map.put("Macao (+853)", "853");
        categories.add("Cambodia (+855)");
        cc_map.put("Cambodia (+855)", "855");
        categories.add("Lao People's Democratic Republic (+856)");
        cc_map.put("Lao People's Democratic Republic (+856)", "856");
        categories.add("Pitcairn (+870)");
        cc_map.put("Pitcairn (+870)", "870");
        categories.add("Bangladesh (+880)");
        cc_map.put("Bangladesh (+880)", "880");
        categories.add("Taiwan, Province of China (+886)");
        cc_map.put("Taiwan, Province of China (+886)", "886");
        categories.add("Maldives (+960)");
        cc_map.put("Maldives (+960)", "960");
        categories.add("Lebanon (+961)");
        cc_map.put("Lebanon (+961)", "961");
        categories.add("Jordan (+962)");
        cc_map.put("Jordan (+962)", "962");
        categories.add("Syrian Arab Republic (+963)");
        cc_map.put("Syrian Arab Republic (+963)", "963");
        categories.add("Iraq (+964)");
        cc_map.put("Iraq (+964)", "964");
        categories.add("Kuwait (+965)");
        cc_map.put("Kuwait (+965)", "965");
        categories.add("Saudi Arabia (+966)");
        cc_map.put("Saudi Arabia (+966)", "966");
        categories.add("Yemen (+967)");
        cc_map.put("Yemen (+967)", "967");
        categories.add("Oman (+968)");
        cc_map.put("Oman (+968)", "968");
        categories.add("Palestine, State of (+970)");
        cc_map.put("Palestine, State of (+970)", "970");
        categories.add("United Arab Emirates (+971)");
        cc_map.put("United Arab Emirates (+971)", "971");
        categories.add("Israel (+972)");
        cc_map.put("Israel (+972)", "972");
        categories.add("Bahrain (+973)");
        cc_map.put("Bahrain (+973)", "973");
        categories.add("Qatar (+974)");
        cc_map.put("Qatar (+974)", "974");
        categories.add("Bhutan (+975)");
        cc_map.put("Bhutan (+975)", "975");
        categories.add("Mongolia (+976)");
        cc_map.put("Mongolia (+976)", "976");
        categories.add("Nepal (+977)");
        cc_map.put("Nepal (+977)", "977");
        categories.add("Tajikistan (+992)");
        cc_map.put("Tajikistan (+992)", "992");
        categories.add("Turkmenistan (+993)");
        cc_map.put("Turkmenistan (+993)", "993");
        categories.add("Azerbaijan (+994)");
        cc_map.put("Azerbaijan (+994)", "994");
        categories.add("Georgia (+995)");
        cc_map.put("Georgia (+995)", "995");
        categories.add("Kyrgyzstan (+996)");
        cc_map.put("Kyrgyzstan (+996)", "996");
        categories.add("Uzbekistan (+998)");
        cc_map.put("Uzbekistan (+998)", "998");
        categories.add("Bahamas (+1242)");
        cc_map.put("Bahamas (+1242)", "1242");
        categories.add("Barbados (+1246)");
        cc_map.put("Barbados (+1246)", "1246");
        categories.add("Anguilla (+1264)");
        cc_map.put("Anguilla (+1264)", "1264");
        categories.add("Antigua and Barbuda (+1268)");
        cc_map.put("Antigua and Barbuda (+1268)", "1268");
        categories.add("British Virgin Islands (+1284)");
        cc_map.put("British Virgin Islands (+1284)", "1284");
        categories.add("US Virgin Islands (+1340)");
        cc_map.put("US Virgin Islands (+1340)", "1340");
        categories.add("Cayman Islands (+1345)");
        cc_map.put("Cayman Islands (+1345)", "1345");
        categories.add("Bermuda (+1441)");
        cc_map.put("Bermuda (+1441)", "1441");
        categories.add("Grenada (+1473)");
        cc_map.put("Grenada (+1473)", "1473");
        categories.add("Turks and Caicos Islands (+1649)");
        cc_map.put("Turks and Caicos Islands (+1649)", "1649");
        categories.add("Montserrat (+1664)");
        cc_map.put("Montserrat (+1664)", "1664");
        categories.add("Northern Mariana Islands (+1670)");
        cc_map.put("Northern Mariana Islands (+1670)", "1670");
        categories.add("Guam (+1671)");
        cc_map.put("Guam (+1671)", "1671");
        categories.add("American Samoa (+1684)");
        cc_map.put("American Samoa (+1684)", "1684");
        categories.add("Sint Maarten (Dutch part) (+1721)");
        cc_map.put("Sint Maarten (Dutch part) (+1721)", "1721");
        categories.add("Saint Lucia (+1758)");
        cc_map.put("Saint Lucia (+1758)", "1758");
        categories.add("Dominica (+1767)");
        cc_map.put("Dominica (+1767)", "1767");
        categories.add("Saint Vincent and the Grenadines (+1784)");
        cc_map.put("Saint Vincent and the Grenadines (+1784)", "1784");
        categories.add("Dominican Republic (+1809)");
        cc_map.put("Dominican Republic (+1809)", "1809");
        categories.add("Trinidad and Tobago (+1868)");
        cc_map.put("Trinidad and Tobago (+1868)", "1868");
        categories.add("Saint Kitts and Nevis (+1869)");
        cc_map.put("Saint Kitts and Nevis (+1869)", "1869");
        categories.add("Jamaica (+1876)");
        cc_map.put("Jamaica (+1876)", "1876");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = categories.getItem(which);
                String select_value = (cc_map).get(categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                selected_cc_value = select_value;
                selected_cc_text = select_text;

                ccode_height_title.setText("+" + selected_cc_value);
                country_code_val = selected_cc_value;
            }
        });
        builderSingle.show();
    }


    public void apply_height() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Patient_Profile_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select height");

        final ArrayAdapter<String> height_categories = new ArrayAdapter<String>(Patient_Profile_Activity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        height_categories.add("1' 0\" (30.48 cm)");
        ht_map.put("1' 0\" (30.48 cm)", "1");

        height_categories.add("1' 1\" (33.02 cm)");
        ht_map.put("1' 1\" (33.02 cm)", "2");

        height_categories.add("1' 2\" (35.56 cm)");
        ht_map.put("1' 2\" (35.56 cm)", "3");

        height_categories.add("1' 3\" (38.1 cm)");
        ht_map.put("1' 3\" (38.1 cm)", "4");

        height_categories.add("1' 4\" (40.64 cm)");
        ht_map.put("1' 4\" (40.64 cm)", "5");

        height_categories.add("1' 5\" (43.18 cm)");
        ht_map.put("1' 5\" (43.18 cm)", "6");

        height_categories.add("1' 6\" (45.72 cm)");
        ht_map.put("1' 6\" (45.72 cm)", "7");

        height_categories.add("1' 7\" (48.26 cm)");
        ht_map.put("1' 7\" (48.26 cm)", "8");

        height_categories.add("1' 8\" (50.8 cm)");
        ht_map.put("1' 8\" (50.8 cm)", "9");

        height_categories.add("1' 9\" (53.34 cm)");
        ht_map.put("1' 9\" (53.34 cm)", "10");

        height_categories.add("1' 10\" (55.88 cm)");
        ht_map.put("1' 10\" (55.88 cm)", "11");

        height_categories.add("1' 11\" (58.42 cm)");
        ht_map.put("1' 11\" (58.42 cm)", "12");

        height_categories.add("2' 0\" (60.96 cm)");
        ht_map.put("2' 0\" (60.96 cm)", "13");

        height_categories.add("2' 1\" (63.5 cm)");
        ht_map.put("2' 1\" (63.5 cm)", "14");
        height_categories.add("2' 2\" (66.04 cm)");
        ht_map.put("2' 2\" (66.04 cm)", "15");
        height_categories.add("2' 3\" (68.58 cm)");
        ht_map.put("2' 3\" (68.58 cm)", "16");
        height_categories.add("2' 4\" (71.12 cm)");
        ht_map.put("2' 4\" (71.12 cm)", "17");
        height_categories.add("2' 5\" (73.66 cm)");
        ht_map.put("2' 5\" (73.66 cm)", "18");
        height_categories.add("2' 6\" (76.2 cm)");
        ht_map.put("2' 6\" (76.2 cm)", "19");
        height_categories.add("2' 7\" (78.74 cm)");
        ht_map.put("2' 7\" (78.74 cm)", "20");
        height_categories.add("2' 8\" (81.28 cm)");
        ht_map.put("2' 8\" (81.28 cm)", "21");
        height_categories.add("2' 9\" (83.82 cm)");
        ht_map.put("2' 9\" (83.82 cm)", "22");
        height_categories.add("2' 10\" (86.36 cm)");
        ht_map.put("2' 10\" (86.36 cm)", "23");
        height_categories.add("2' 11\" (88.9 cm)");
        ht_map.put("2' 11\" (88.9 cm)", "24");
        height_categories.add("3' 0\" (91.44 cm)");
        ht_map.put("3' 0\" (91.44 cm)", "25");
        height_categories.add("3' 1\" (93.98 cm)");
        ht_map.put("3' 1\" (93.98 cm)", "26");
        height_categories.add("3' 2\" (96.52 cm)");
        ht_map.put("3' 2\" (96.52 cm)", "27");
        height_categories.add("3' 3\" (99.06 cm)");
        ht_map.put("3' 3\" (99.06 cm)", "28");
        height_categories.add("3' 4\" (101.6 cm)");
        ht_map.put("3' 4\" (101.6 cm)", "29");
        height_categories.add("3' 5\" (104.14 cm)");
        ht_map.put("3' 5\" (104.14 cm)", "30");
        height_categories.add("3' 6\" (106.68 cm)");
        ht_map.put("3' 6\" (106.68 cm)", "31");
        height_categories.add("3' 7\" (109.22 cm)");
        ht_map.put("3' 7\" (109.22 cm)", "32");
        height_categories.add("3' 8\" (111.76 cm)");
        ht_map.put("3' 8\" (111.76 cm)", "33");
        height_categories.add("3' 9\" (114.3 cm)");
        ht_map.put("3' 9\" (114.3 cm)", "34");
        height_categories.add("3' 10\" (116.84 cm)");
        ht_map.put("3' 10\" (116.84 cm)", "35");
        height_categories.add("3' 11\" (119.38 cm)");
        ht_map.put("3' 11\" (119.38 cm)", "36");
        height_categories.add("4' 0\" (121.92 cm)");
        ht_map.put("4' 0\" (121.92 cm)", "37");
        height_categories.add("4' 1\" (124.46 cm)");
        ht_map.put("4' 1\" (124.46 cm)", "38");
        height_categories.add("4' 2\" (127 cm)");
        ht_map.put("4' 2\" (127 cm)", "39");
        height_categories.add("4' 3\" (129.54 cm)");
        ht_map.put("4' 3\" (129.54 cm)", "40");
        height_categories.add("4' 4\" (132.08 cm)");
        ht_map.put("4' 4\" (132.08 cm)", "41");
        height_categories.add("4' 5\" (134.62 cm)");
        ht_map.put("4' 5\" (134.62 cm)", "42");
        height_categories.add("4' 6\" (137.16 cm)");
        ht_map.put("4' 6\" (137.16 cm)", "43");
        height_categories.add("4' 7\" (139.7 cm)");
        ht_map.put("4' 7\" (139.7 cm)", "44");
        height_categories.add("4' 8\" (142.24 cm)");
        ht_map.put("4' 8\" (142.24 cm)", "45");
        height_categories.add("4' 9\" (144.78 cm)");
        ht_map.put("4' 9\" (144.78 cm)", "46");
        height_categories.add("4' 10\" (147.32 cm)");
        ht_map.put("4' 10\" (147.32 cm)", "47");
        height_categories.add("4' 11\" (149.86 cm)");
        ht_map.put("4' 11\" (149.86 cm)", "48");
        height_categories.add("5' 0\" (152.4 cm)");
        ht_map.put("5' 0\" (152.4 cm)", "49");
        height_categories.add("5' 1\" (154.94 cm)");
        ht_map.put("5' 1\" (154.94 cm)", "50");
        height_categories.add("5' 2\" (157.48 cm)");
        ht_map.put("5' 2\" (157.48 cm)", "51");
        height_categories.add("5' 3\" (160.02 cm)");
        ht_map.put("5' 3\" (160.02 cm)", "52");
        height_categories.add("5' 4\" (162.56 cm)");
        ht_map.put("5' 4\" (162.56 cm)", "53");
        height_categories.add("5' 5\" (165.1 cm)");
        ht_map.put("5' 5\" (165.1 cm)", "54");
        height_categories.add("5' 6\" (167.64 cm)");
        ht_map.put("5' 6\" (167.64 cm)", "55");
        height_categories.add("5' 7\" (170.18 cm)");
        ht_map.put("5' 7\" (170.18 cm)", "56");
        height_categories.add("5' 8\" (172.72 cm)");
        ht_map.put("5' 8\" (172.72 cm)", "57");
        height_categories.add("5' 9\" (175.26 cm)");
        ht_map.put("5' 9\" (175.26 cm)", "58");
        height_categories.add("5' 10\" (177.8 cm)");
        ht_map.put("5' 10\" (177.8 cm)", "59");
        height_categories.add("5' 11\" (180.34 cm)");
        ht_map.put("5' 11\" (180.34 cm)", "60");
        height_categories.add("6' 0\" (182.88 cm)");
        ht_map.put("6' 0\" (182.88 cm)", "61");
        height_categories.add("6' 1\" (185.42 cm)");
        ht_map.put("6' 1\" (185.42 cm)", "62");
        height_categories.add("6' 2\" (187.96 cm)");
        ht_map.put("6' 2\" (187.96 cm)", "63");
        height_categories.add("6' 3\" (190.5 cm)");
        ht_map.put("6' 3\" (190.5 cm)", "64");
        height_categories.add("6' 4\" (193.04 cm)");
        ht_map.put("6' 4\" (193.04 cm)", "65");
        height_categories.add("6' 5\" (195.58 cm)");
        ht_map.put("6' 5\" (195.58 cm)", "66");
        height_categories.add("6' 6\" (198.12 cm)");
        ht_map.put("6' 6\" (198.12 cm)", "67");
        height_categories.add("6' 7\" (200.66 cm)");
        ht_map.put("6' 7\" (200.66 cm)", "68");
        height_categories.add("6' 8\" (203.2 cm)");
        ht_map.put("6' 8\" (203.2 cm)", "69");
        height_categories.add("6' 9\" (205.74 cm)");
        ht_map.put("6' 9\" (205.74 cm)", "70");
        height_categories.add("6' 10\" (208.28 cm)");
        ht_map.put("6' 10\" (208.28 cm)", "71");
        height_categories.add("6' 11\" (210.82 cm)");
        ht_map.put("6' 11\" (210.82 cm)", "72");
        height_categories.add("7' 0\" (213.36 cm)");
        ht_map.put("7' 0\" (213.36 cm)", "73");
        height_categories.add("7' 1\" (215.9 cm)");
        ht_map.put("7' 1\" (215.9 cm)", "74");
        height_categories.add("7' 2\" (218.44 cm)");
        ht_map.put("7' 2\" (218.44 cm)", "75");
        height_categories.add("7' 3\" (220.98 cm)");
        ht_map.put("7' 3\" (220.98 cm)", "76");
        height_categories.add("7' 4\" (223.52 cm)");
        ht_map.put("7' 4\" (223.52 cm)", "77");
        height_categories.add("7' 5\" (226.06 cm)");
        ht_map.put("7' 5\" (226.06 cm)", "78");
        height_categories.add("7' 6\" (228.6 cm)");
        ht_map.put("7' 6\" (228.6 cm)", "79");
        height_categories.add("7' 7\" (231.14 cm)");
        ht_map.put("7' 7\" (231.14 cm)", "80");
        height_categories.add("7' 8\" (233.68 cm)");
        ht_map.put("7' 8\" (233.68 cm)", "81");
        height_categories.add("7' 9\" (236.22 cm)");
        ht_map.put("7' 9\" (236.22 cm)", "82");
        height_categories.add("7' 10\" (238.76 cm)");
        ht_map.put("7' 10\" (238.76 cm)", "83");
        height_categories.add("7' 11\" (241.3 cm)");
        ht_map.put("7' 11\" (241.3 cm)", "84");
        height_categories.add("8' 0\" (243.84 cm)");
        ht_map.put("8' 0\" (243.84 cm)", "85");
        height_categories.add("8' 1\" (246.38 cm)");
        ht_map.put("8' 1\" (246.38 cm)", "86");
        height_categories.add("8' 2\" (248.92 cm)");
        ht_map.put("8' 2\" (248.92 cm)", "87");
        height_categories.add("8' 3\" (251.46 cm)");
        ht_map.put("8' 3\" (251.46 cm)", "88");
        height_categories.add("8' 4\" (254 cm)");
        ht_map.put("8' 4\" (254 cm)", "89");
        height_categories.add("8' 5\" (256.54 cm)");
        ht_map.put("8' 5\" (256.54 cm)", "90");
        height_categories.add("8' 6\" (259.08 cm)");
        ht_map.put("8' 6\" (259.08 cm)", "91");
        height_categories.add("8' 7\" (261.62 cm)");
        ht_map.put("8' 7\" (261.62 cm)", "92");
        height_categories.add("8' 8\" (264.16 cm)");
        ht_map.put("8' 8\" (264.16 cm)", "93");
        height_categories.add("8' 9\" (266.7 cm)");
        ht_map.put("8' 9\" (266.7 cm)", "94");
        height_categories.add("8' 10\" (269.24 cm)");
        ht_map.put("8' 10\" (269.24 cm)", "95");
        height_categories.add("8' 11\" (271.78 cm)");
        ht_map.put("8' 11\" (271.78 cm)", "96");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(height_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = height_categories.getItem(which);
                String select_value = (ht_map).get(height_categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                height_id_val = select_value;
                height_name = select_text;

                height_title.setText(height_name);
            }
        });
        builderSingle.show();
    }


    public void apply_weight() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Patient_Profile_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select weight");

        final ArrayAdapter<String> weight_categories = new ArrayAdapter<String>(Patient_Profile_Activity.this, R.layout.dialog_list_textview);

        //cc_map = new HashMap<String, String>();

        weight_categories.add("1 lbs (0.45 kg)");
        wt_map.put("1 lbs (0.45 kg)", "1");
        weight_categories.add("2 lbs (0.91 kg)");
        wt_map.put("2 lbs (0.91 kg)", "2");
        weight_categories.add("3 lbs (1.36 kg)");
        wt_map.put("3 lbs (1.36 kg)", "3");
        weight_categories.add("4 lbs (1.81 kg)");
        wt_map.put("4 lbs (1.81 kg)", "4");
        weight_categories.add("5 lbs (2.27 kg)");
        wt_map.put("5 lbs (2.27 kg)", "5");
        weight_categories.add("6 lbs (2.72 kg)");
        wt_map.put("6 lbs (2.72 kg)", "6");
        weight_categories.add("7 lbs (3.18 kg)");
        wt_map.put("7 lbs (3.18 kg)", "7");
        weight_categories.add("8 lbs (3.63 kg)");
        wt_map.put("8 lbs (3.63 kg)", "8");
        weight_categories.add("9 lbs (4.08 kg)");
        wt_map.put("9 lbs (4.08 kg)", "9");
        weight_categories.add("10 lbs (4.54 kg)");
        wt_map.put("10 lbs (4.54 kg)", "10");
        weight_categories.add("11 lbs (4.99 kg)");
        wt_map.put("11 lbs (4.99 kg)", "11");
        weight_categories.add("12 lbs (5.44 kg)");
        wt_map.put("12 lbs (5.44 kg)", "12");
        weight_categories.add("13 lbs (5.9 kg)");
        wt_map.put("13 lbs (5.9 kg)", "13");
        weight_categories.add("14 lbs (6.35 kg)");
        wt_map.put("14 lbs (6.35 kg)", "14");
        weight_categories.add("15 lbs (6.8 kg)");
        wt_map.put("15 lbs (6.8 kg)", "15");
        weight_categories.add("16 lbs (7.26 kg)");
        wt_map.put("16 lbs (7.26 kg)", "16");
        weight_categories.add("17 lbs (7.71 kg)");
        wt_map.put("17 lbs (7.71 kg)", "17");
        weight_categories.add("18 lbs (8.16 kg)");
        wt_map.put("18 lbs (8.16 kg)", "18");
        weight_categories.add("19 lbs (8.62 kg)");
        wt_map.put("19 lbs (8.62 kg)", "19");
        weight_categories.add("20 lbs (9.07 kg)");
        wt_map.put("20 lbs (9.07 kg)", "20");
        weight_categories.add("21 lbs (9.53 kg)");
        wt_map.put("21 lbs (9.53 kg)", "21");
        weight_categories.add("22 lbs (9.98 kg)");
        wt_map.put("22 lbs (9.98 kg)", "22");
        weight_categories.add("23 lbs (10.43 kg)");
        wt_map.put("23 lbs (10.43 kg)", "23");
        weight_categories.add("24 lbs (10.89 kg)");
        wt_map.put("24 lbs (10.89 kg)", "24");
        weight_categories.add("25 lbs (11.34 kg)");
        wt_map.put("25 lbs (11.34 kg)", "25");
        weight_categories.add("26 lbs (11.79 kg)");
        wt_map.put("26 lbs (11.79 kg)", "26");
        weight_categories.add("27 lbs (12.25 kg)");
        wt_map.put("27 lbs (12.25 kg)", "27");
        weight_categories.add("28 lbs (12.7 kg)");
        wt_map.put("28 lbs (12.7 kg)", "28");
        weight_categories.add("29 lbs (13.15 kg)");
        wt_map.put("29 lbs (13.15 kg)", "29");
        weight_categories.add("30 lbs (13.61 kg)");
        wt_map.put("30 lbs (13.61 kg)", "30");
        weight_categories.add("31 lbs (14.06 kg)");
        wt_map.put("31 lbs (14.06 kg)", "31");
        weight_categories.add("32 lbs (14.51 kg)");
        wt_map.put("32 lbs (14.51 kg)", "32");
        weight_categories.add("33 lbs (14.97 kg)");
        wt_map.put("33 lbs (14.97 kg)", "33");
        weight_categories.add("34 lbs (15.42 kg)");
        wt_map.put("34 lbs (15.42 kg)", "34");
        weight_categories.add("35 lbs (15.88 kg)");
        wt_map.put("35 lbs (15.88 kg)", "35");
        weight_categories.add("36 lbs (16.33 kg)");
        wt_map.put("36 lbs (16.33 kg)", "36");
        weight_categories.add("37 lbs (16.78 kg)");
        wt_map.put("37 lbs (16.78 kg)", "37");
        weight_categories.add("38 lbs (17.24 kg)");
        wt_map.put("38 lbs (17.24 kg)", "38");
        weight_categories.add("39 lbs (17.69 kg)");
        wt_map.put("39 lbs (17.69 kg)", "39");
        weight_categories.add("40 lbs (18.14 kg)");
        wt_map.put("40 lbs (18.14 kg)", "40");
        weight_categories.add("41 lbs (18.6 kg)");
        wt_map.put("41 lbs (18.6 kg)", "41");
        weight_categories.add("42 lbs (19.05 kg)");
        wt_map.put("42 lbs (19.05 kg)", "42");
        weight_categories.add("43 lbs (19.5 kg)");
        wt_map.put("43 lbs (19.5 kg)", "43");
        weight_categories.add("44 lbs (19.96 kg)");
        wt_map.put("44 lbs (19.96 kg)", "44");
        weight_categories.add("45 lbs (20.41 kg)");
        wt_map.put("45 lbs (20.41 kg)", "45");
        weight_categories.add("46 lbs (20.87 kg)");
        wt_map.put("46 lbs (20.87 kg)", "46");
        weight_categories.add("47 lbs (21.32 kg)");
        wt_map.put("47 lbs (21.32 kg)", "47");
        weight_categories.add("48 lbs (21.77 kg)");
        wt_map.put("48 lbs (21.77 kg)", "48");
        weight_categories.add("49 lbs (22.23 kg)");
        wt_map.put("49 lbs (22.23 kg)", "49");
        weight_categories.add("50 lbs (22.68 kg)");
        wt_map.put("50 lbs (22.68 kg)", "50");
        weight_categories.add("51 lbs (23.13 kg)");
        wt_map.put("51 lbs (23.13 kg)", "51");
        weight_categories.add("52 lbs (23.59 kg)");
        wt_map.put("52 lbs (23.59 kg)", "52");
        weight_categories.add("53 lbs (24.04 kg)");
        wt_map.put("53 lbs (24.04 kg)", "53");
        weight_categories.add("54 lbs (24.49 kg)");
        wt_map.put("54 lbs (24.49 kg)", "54");
        weight_categories.add("55 lbs (24.95 kg)");
        wt_map.put("55 lbs (24.95 kg)", "55");
        weight_categories.add("56 lbs (25.4 kg)");
        wt_map.put("56 lbs (25.4 kg)", "56");
        weight_categories.add("57 lbs (25.85 kg)");
        wt_map.put("57 lbs (25.85 kg)", "57");
        weight_categories.add("58 lbs (26.31 kg)");
        wt_map.put("58 lbs (26.31 kg)", "58");
        weight_categories.add("59 lbs (26.76 kg)");
        wt_map.put("59 lbs (26.76 kg)", "59");
        weight_categories.add("60 lbs (27.22 kg)");
        wt_map.put("60 lbs (27.22 kg)", "60");
        weight_categories.add("61 lbs (27.67 kg)");
        wt_map.put("61 lbs (27.67 kg)", "61");
        weight_categories.add("62 lbs (28.12 kg)");
        wt_map.put("62 lbs (28.12 kg)", "62");
        weight_categories.add("63 lbs (28.58 kg)");
        wt_map.put("63 lbs (28.58 kg)", "63");
        weight_categories.add("64 lbs (29.03 kg)");
        wt_map.put("64 lbs (29.03 kg)", "64");
        weight_categories.add("65 lbs (29.48 kg)");
        wt_map.put("65 lbs (29.48 kg)", "65");
        weight_categories.add("66 lbs (29.94 kg)");
        wt_map.put("66 lbs (29.94 kg)", "66");
        weight_categories.add("67 lbs (30.39 kg)");
        wt_map.put("67 lbs (30.39 kg)", "67");
        weight_categories.add("68 lbs (30.84 kg)");
        wt_map.put("68 lbs (30.84 kg)", "68");
        weight_categories.add("69 lbs (31.3 kg)");
        wt_map.put("69 lbs (31.3 kg)", "69");
        weight_categories.add("70 lbs (31.75 kg)");
        wt_map.put("70 lbs (31.75 kg)", "70");
        weight_categories.add("71 lbs (32.21 kg)");
        wt_map.put("71 lbs (32.21 kg)", "71");
        weight_categories.add("72 lbs (32.66 kg)");
        wt_map.put("72 lbs (32.66 kg)", "72");
        weight_categories.add("73 lbs (33.11 kg)");
        wt_map.put("73 lbs (33.11 kg)", "73");
        weight_categories.add("74 lbs (33.57 kg)");
        wt_map.put("74 lbs (33.57 kg)", "74");
        weight_categories.add("75 lbs (34.02 kg)");
        wt_map.put("75 lbs (34.02 kg)", "75");
        weight_categories.add("76 lbs (34.47 kg)");
        wt_map.put("76 lbs (34.47 kg)", "76");
        weight_categories.add("77 lbs (34.93 kg)");
        wt_map.put("77 lbs (34.93 kg)", "77");
        weight_categories.add("78 lbs (35.38 kg)");
        wt_map.put("78 lbs (35.38 kg)", "78");
        weight_categories.add("79 lbs (35.83 kg)");
        wt_map.put("79 lbs (35.83 kg)", "79");
        weight_categories.add("80 lbs (36.29 kg)");
        wt_map.put("80 lbs (36.29 kg)", "80");
        weight_categories.add("81 lbs (36.74 kg)");
        wt_map.put("81 lbs (36.74 kg)", "81");
        weight_categories.add("82 lbs (37.19 kg)");
        wt_map.put("82 lbs (37.19 kg)", "82");
        weight_categories.add("83 lbs (37.65 kg)");
        wt_map.put("83 lbs (37.65 kg)", "83");
        weight_categories.add("84 lbs (38.1 kg)");
        wt_map.put("84 lbs (38.1 kg)", "84");
        weight_categories.add("85 lbs (38.56 kg)");
        wt_map.put("85 lbs (38.56 kg)", "85");
        weight_categories.add("86 lbs (39.01 kg)");
        wt_map.put("86 lbs (39.01 kg)", "86");
        weight_categories.add("87 lbs (39.46 kg)");
        wt_map.put("87 lbs (39.46 kg)", "87");
        weight_categories.add("88 lbs (39.92 kg)");
        wt_map.put("88 lbs (39.92 kg)", "88");
        weight_categories.add("89 lbs (40.37 kg)");
        wt_map.put("89 lbs (40.37 kg)", "89");
        weight_categories.add("90 lbs (40.82 kg)");
        wt_map.put("90 lbs (40.82 kg)", "90");
        weight_categories.add("91 lbs (41.28 kg)");
        wt_map.put("91 lbs (41.28 kg)", "91");
        weight_categories.add("92 lbs (41.73 kg)");
        wt_map.put("92 lbs (41.73 kg)", "92");
        weight_categories.add("93 lbs (42.18 kg)");
        wt_map.put("93 lbs (42.18 kg)", "93");
        weight_categories.add("94 lbs (42.64 kg)");
        wt_map.put("94 lbs (42.64 kg)", "94");
        weight_categories.add("95 lbs (43.09 kg)");
        wt_map.put("95 lbs (43.09 kg)", "95");
        weight_categories.add("96 lbs (43.54 kg)");
        wt_map.put("96 lbs (43.54 kg)", "96");
        weight_categories.add("97 lbs (44 kg)");
        wt_map.put("97 lbs (44 kg)", "97");
        weight_categories.add("98 lbs (44.45 kg)");
        wt_map.put("98 lbs (44.45 kg)", "98");
        weight_categories.add("99 lbs (44.91 kg)");
        wt_map.put("99 lbs (44.91 kg)", "99");
        weight_categories.add("100 lbs (45.36 kg)");
        wt_map.put("100 lbs (45.36 kg)", "100");
        weight_categories.add("101 lbs (45.81 kg)");
        wt_map.put("101 lbs (45.81 kg)", "101");
        weight_categories.add("102 lbs (46.27 kg)");
        wt_map.put("102 lbs (46.27 kg)", "102");
        weight_categories.add("103 lbs (46.72 kg)");
        wt_map.put("103 lbs (46.72 kg)", "103");
        weight_categories.add("104 lbs (47.17 kg)");
        wt_map.put("104 lbs (47.17 kg)", "104");
        weight_categories.add("105 lbs (47.63 kg)");
        wt_map.put("105 lbs (47.63 kg)", "105");
        weight_categories.add("106 lbs (48.08 kg)");
        wt_map.put("106 lbs (48.08 kg)", "106");
        weight_categories.add("107 lbs (48.53 kg)");
        wt_map.put("107 lbs (48.53 kg)", "107");
        weight_categories.add("108 lbs (48.99 kg)");
        wt_map.put("108 lbs (48.99 kg)", "108");
        weight_categories.add("109 lbs (49.44 kg)");
        wt_map.put("109 lbs (49.44 kg)", "109");
        weight_categories.add("110 lbs (49.9 kg)");
        wt_map.put("110 lbs (49.9 kg)", "110");
        weight_categories.add("111 lbs (50.35 kg)");
        wt_map.put("111 lbs (50.35 kg)", "111");
        weight_categories.add("112 lbs (50.8 kg)");
        wt_map.put("112 lbs (50.8 kg)", "112");
        weight_categories.add("113 lbs (51.26 kg)");
        wt_map.put("113 lbs (51.26 kg)", "113");
        weight_categories.add("114 lbs (51.71 kg)");
        wt_map.put("114 lbs (51.71 kg)", "114");
        weight_categories.add("115 lbs (52.16 kg)");
        wt_map.put("115 lbs (52.16 kg)", "115");
        weight_categories.add("116 lbs (52.62 kg)");
        wt_map.put("116 lbs (52.62 kg)", "116");
        weight_categories.add("117 lbs (53.07 kg)");
        wt_map.put("117 lbs (53.07 kg)", "117");
        weight_categories.add("118 lbs (53.52 kg)");
        wt_map.put("118 lbs (53.52 kg)", "118");
        weight_categories.add("119 lbs (53.98 kg)");
        wt_map.put("119 lbs (53.98 kg)", "119");
        weight_categories.add("120 lbs (54.43 kg)");
        wt_map.put("120 lbs (54.43 kg)", "120");
        weight_categories.add("121 lbs (54.88 kg)");
        wt_map.put("121 lbs (54.88 kg)", "121");
        weight_categories.add("122 lbs (55.34 kg)");
        wt_map.put("122 lbs (55.34 kg)", "122");
        weight_categories.add("123 lbs (55.79 kg)");
        wt_map.put("123 lbs (55.79 kg)", "123");
        weight_categories.add("124 lbs (56.25 kg)");
        wt_map.put("124 lbs (56.25 kg)", "124");
        weight_categories.add("125 lbs (56.7 kg)");
        wt_map.put("125 lbs (56.7 kg)", "125");
        weight_categories.add("126 lbs (57.15 kg)");
        wt_map.put("126 lbs (57.15 kg)", "126");
        weight_categories.add("127 lbs (57.61 kg)");
        wt_map.put("127 lbs (57.61 kg)", "127");
        weight_categories.add("128 lbs (58.06 kg)");
        wt_map.put("128 lbs (58.06 kg)", "128");
        weight_categories.add("129 lbs (58.51 kg)");
        wt_map.put("129 lbs (58.51 kg)", "129");
        weight_categories.add("130 lbs (58.97 kg)");
        wt_map.put("130 lbs (58.97 kg)", "130");
        weight_categories.add("131 lbs (59.42 kg)");
        wt_map.put("131 lbs (59.42 kg)", "131");
        weight_categories.add("132 lbs (59.87 kg)");
        wt_map.put("132 lbs (59.87 kg)", "132");
        weight_categories.add("133 lbs (60.33 kg)");
        wt_map.put("133 lbs (60.33 kg)", "133");
        weight_categories.add("134 lbs (60.78 kg)");
        wt_map.put("134 lbs (60.78 kg)", "134");
        weight_categories.add("135 lbs (61.23 kg)");
        wt_map.put("135 lbs (61.23 kg)", "135");
        weight_categories.add("136 lbs (61.69 kg)");
        wt_map.put("136 lbs (61.69 kg)", "136");
        weight_categories.add("137 lbs (62.14 kg)");
        wt_map.put("137 lbs (62.14 kg)", "137");
        weight_categories.add("138 lbs (62.6 kg)");
        wt_map.put("138 lbs (62.6 kg)", "138");
        weight_categories.add("139 lbs (63.05 kg)");
        wt_map.put("139 lbs (63.05 kg)", "139");
        weight_categories.add("140 lbs (63.5 kg)");
        wt_map.put("140 lbs (63.5 kg)", "140");
        weight_categories.add("141 lbs (63.96 kg)");
        wt_map.put("141 lbs (63.96 kg)", "141");
        weight_categories.add("142 lbs (64.41 kg)");
        wt_map.put("142 lbs (64.41 kg)", "142");
        weight_categories.add("143 lbs (64.86 kg)");
        wt_map.put("143 lbs (64.86 kg)", "143");
        weight_categories.add("144 lbs (65.32 kg)");
        wt_map.put("144 lbs (65.32 kg)", "144");
        weight_categories.add("145 lbs (65.77 kg)");
        wt_map.put("145 lbs (65.77 kg)", "145");
        weight_categories.add("146 lbs (66.22 kg)");
        wt_map.put("146 lbs (66.22 kg)", "146");
        weight_categories.add("147 lbs (66.68 kg)");
        wt_map.put("147 lbs (66.68 kg)", "147");
        weight_categories.add("148 lbs (67.13 kg)");
        wt_map.put("148 lbs (67.13 kg)", "148");
        weight_categories.add("149 lbs (67.59 kg)");
        wt_map.put("149 lbs (67.59 kg)", "149");
        weight_categories.add("150 lbs (68.04 kg)");
        wt_map.put("150 lbs (68.04 kg)", "150");
        weight_categories.add("151 lbs (68.49 kg)");
        wt_map.put("151 lbs (68.49 kg)", "151");
        weight_categories.add("152 lbs (68.95 kg)");
        wt_map.put("152 lbs (68.95 kg)", "152");
        weight_categories.add("153 lbs (69.4 kg)");
        wt_map.put("153 lbs (69.4 kg)", "153");
        weight_categories.add("154 lbs (69.85 kg)");
        wt_map.put("154 lbs (69.85 kg)", "154");
        weight_categories.add("155 lbs (70.31 kg)");
        wt_map.put("155 lbs (70.31 kg)", "155");
        weight_categories.add("156 lbs (70.76 kg)");
        wt_map.put("156 lbs (70.76 kg)", "156");
        weight_categories.add("157 lbs (71.21 kg)");
        wt_map.put("157 lbs (71.21 kg)", "157");
        weight_categories.add("158 lbs (71.67 kg)");
        wt_map.put("158 lbs (71.67 kg)", "158");
        weight_categories.add("159 lbs (72.12 kg)");
        wt_map.put("159 lbs (72.12 kg)", "159");
        weight_categories.add("160 lbs (72.57 kg)");
        wt_map.put("160 lbs (72.57 kg)", "160");
        weight_categories.add("161 lbs (73.03 kg)");
        wt_map.put("161 lbs (73.03 kg)", "161");
        weight_categories.add("162 lbs (73.48 kg)");
        wt_map.put("162 lbs (73.48 kg)", "162");
        weight_categories.add("163 lbs (73.94 kg)");
        wt_map.put("163 lbs (73.94 kg)", "163");
        weight_categories.add("164 lbs (74.39 kg)");
        wt_map.put("164 lbs (74.39 kg)", "164");
        weight_categories.add("165 lbs (74.84 kg)");
        wt_map.put("165 lbs (74.84 kg)", "165");
        weight_categories.add("166 lbs (75.3 kg)");
        wt_map.put("166 lbs (75.3 kg)", "166");
        weight_categories.add("167 lbs (75.75 kg)");
        wt_map.put("167 lbs (75.75 kg)", "167");
        weight_categories.add("168 lbs (76.2 kg)");
        wt_map.put("168 lbs (76.2 kg)", "168");
        weight_categories.add("169 lbs (76.66 kg)");
        wt_map.put("169 lbs (76.66 kg)", "169");
        weight_categories.add("170 lbs (77.11 kg)");
        wt_map.put("170 lbs (77.11 kg)", "170");
        weight_categories.add("171 lbs (77.56 kg)");
        wt_map.put("171 lbs (77.56 kg)", "171");
        weight_categories.add("172 lbs (78.02 kg)");
        wt_map.put("172 lbs (78.02 kg)", "172");
        weight_categories.add("173 lbs (78.47 kg)");
        wt_map.put("173 lbs (78.47 kg)", "173");
        weight_categories.add("174 lbs (78.93 kg)");
        wt_map.put("174 lbs (78.93 kg)", "174");
        weight_categories.add("175 lbs (79.38 kg)");
        wt_map.put("175 lbs (79.38 kg)", "175");
        weight_categories.add("176 lbs (79.83 kg)");
        wt_map.put("176 lbs (79.83 kg)", "176");
        weight_categories.add("177 lbs (80.29 kg)");
        wt_map.put("177 lbs (80.29 kg)", "177");
        weight_categories.add("178 lbs (80.74 kg)");
        wt_map.put("178 lbs (80.74 kg)", "178");
        weight_categories.add("179 lbs (81.19 kg)");
        wt_map.put("179 lbs (81.19 kg)", "179");
        weight_categories.add("180 lbs (81.65 kg)");
        wt_map.put("180 lbs (81.65 kg)", "180");
        weight_categories.add("181 lbs (82.1 kg)");
        wt_map.put("181 lbs (82.1 kg)", "181");
        weight_categories.add("182 lbs (82.55 kg)");
        wt_map.put("182 lbs (82.55 kg)", "182");
        weight_categories.add("183 lbs (83.01 kg)");
        wt_map.put("183 lbs (83.01 kg)", "183");
        weight_categories.add("184 lbs (83.46 kg)");
        wt_map.put("184 lbs (83.46 kg)", "184");
        weight_categories.add("185 lbs (83.91 kg)");
        wt_map.put("185 lbs (83.91 kg)", "185");
        weight_categories.add("186 lbs (84.37 kg)");
        wt_map.put("186 lbs (84.37 kg)", "186");
        weight_categories.add("187 lbs (84.82 kg)");
        wt_map.put("187 lbs (84.82 kg)", "187");
        weight_categories.add("188 lbs (85.28 kg)");
        wt_map.put("188 lbs (85.28 kg)", "188");
        weight_categories.add("189 lbs (85.73 kg)");
        wt_map.put("189 lbs (85.73 kg)", "189");
        weight_categories.add("190 lbs (86.18 kg)");
        wt_map.put("190 lbs (86.18 kg)", "190");
        weight_categories.add("191 lbs (86.64 kg)");
        wt_map.put("191 lbs (86.64 kg)", "191");
        weight_categories.add("192 lbs (87.09 kg)");
        wt_map.put("192 lbs (87.09 kg)", "192");
        weight_categories.add("193 lbs (87.54 kg)");
        wt_map.put("193 lbs (87.54 kg)", "193");
        weight_categories.add("194 lbs (88 kg)");
        wt_map.put("194 lbs (88 kg)", "194");
        weight_categories.add("195 lbs (88.45 kg)");
        wt_map.put("195 lbs (88.45 kg)", "195");
        weight_categories.add("196 lbs (88.9 kg)");
        wt_map.put("196 lbs (88.9 kg)", "196");
        weight_categories.add("197 lbs (89.36 kg)");
        wt_map.put("197 lbs (89.36 kg)", "197");
        weight_categories.add("198 lbs (89.81 kg)");
        wt_map.put("198 lbs (89.81 kg)", "198");
        weight_categories.add("199 lbs (90.26 kg)");
        wt_map.put("199 lbs (90.26 kg)", "199");
        weight_categories.add("200 lbs (90.72 kg)");
        wt_map.put("200 lbs (90.72 kg)", "200");
        weight_categories.add("201 lbs (91.17 kg)");
        wt_map.put("201 lbs (91.17 kg)", "201");
        weight_categories.add("202 lbs (91.63 kg)");
        wt_map.put("202 lbs (91.63 kg)", "202");
        weight_categories.add("203 lbs (92.08 kg)");
        wt_map.put("203 lbs (92.08 kg)", "203");
        weight_categories.add("204 lbs (92.53 kg)");
        wt_map.put("204 lbs (92.53 kg)", "204");
        weight_categories.add("205 lbs (92.99 kg)");
        wt_map.put("205 lbs (92.99 kg)", "205");
        weight_categories.add("206 lbs (93.44 kg)");
        wt_map.put("206 lbs (93.44 kg)", "206");
        weight_categories.add("207 lbs (93.89 kg)");
        wt_map.put("207 lbs (93.89 kg)", "207");
        weight_categories.add("208 lbs (94.35 kg)");
        wt_map.put("208 lbs (94.35 kg)", "208");
        weight_categories.add("209 lbs (94.8 kg)");
        wt_map.put("209 lbs (94.8 kg)", "209");
        weight_categories.add("210 lbs (95.25 kg)");
        wt_map.put("210 lbs (95.25 kg)", "210");
        weight_categories.add("211 lbs (95.71 kg)");
        wt_map.put("211 lbs (95.71 kg)", "211");
        weight_categories.add("212 lbs (96.16 kg)");
        wt_map.put("212 lbs (96.16 kg)", "212");
        weight_categories.add("213 lbs (96.62 kg)");
        wt_map.put("213 lbs (96.62 kg)", "213");
        weight_categories.add("214 lbs (97.07 kg)");
        wt_map.put("214 lbs (97.07 kg)", "214");
        weight_categories.add("215 lbs (97.52 kg)");
        wt_map.put("215 lbs (97.52 kg)", "215");
        weight_categories.add("216 lbs (97.98 kg)");
        wt_map.put("216 lbs (97.98 kg)", "216");
        weight_categories.add("217 lbs (98.43 kg)");
        wt_map.put("217 lbs (98.43 kg)", "217");
        weight_categories.add("218 lbs (98.88 kg)");
        wt_map.put("218 lbs (98.88 kg)", "218");
        weight_categories.add("219 lbs (99.34 kg)");
        wt_map.put("219 lbs (99.34 kg)", "219");
        weight_categories.add("220 lbs (99.79 kg)");
        wt_map.put("220 lbs (99.79 kg)", "220");
        weight_categories.add("221 lbs (100.24 kg)");
        wt_map.put("221 lbs (100.24 kg)", "221");
        weight_categories.add("222 lbs (100.7 kg)");
        wt_map.put("222 lbs (100.7 kg)", "222");
        weight_categories.add("223 lbs (101.15 kg)");
        wt_map.put("223 lbs (101.15 kg)", "223");
        weight_categories.add("224 lbs (101.6 kg)");
        wt_map.put("224 lbs (101.6 kg)", "224");
        weight_categories.add("225 lbs (102.06 kg)");
        wt_map.put("225 lbs (102.06 kg)", "225");
        weight_categories.add("226 lbs (102.51 kg)");
        wt_map.put("226 lbs (102.51 kg)", "226");
        weight_categories.add("227 lbs (102.97 kg)");
        wt_map.put("227 lbs (102.97 kg)", "227");
        weight_categories.add("228 lbs (103.42 kg)");
        wt_map.put("228 lbs (103.42 kg)", "228");
        weight_categories.add("229 lbs (103.87 kg)");
        wt_map.put("229 lbs (103.87 kg)", "229");
        weight_categories.add("230 lbs (104.33 kg)");
        wt_map.put("230 lbs (104.33 kg)", "230");
        weight_categories.add("231 lbs (104.78 kg)");
        wt_map.put("231 lbs (104.78 kg)", "231");
        weight_categories.add("232 lbs (105.23 kg)");
        wt_map.put("232 lbs (105.23 kg)", "232");
        weight_categories.add("233 lbs (105.69 kg)");
        wt_map.put("233 lbs (105.69 kg)", "233");
        weight_categories.add("234 lbs (106.14 kg)");
        wt_map.put("234 lbs (106.14 kg)", "234");
        weight_categories.add("235 lbs (106.59 kg)");
        wt_map.put("235 lbs (106.59 kg)", "235");
        weight_categories.add("236 lbs (107.05 kg)");
        wt_map.put("236 lbs (107.05 kg)", "236");
        weight_categories.add("237 lbs (107.5 kg)");
        wt_map.put("237 lbs (107.5 kg)", "237");
        weight_categories.add("238 lbs (107.95 kg)");
        wt_map.put("238 lbs (107.95 kg)", "238");
        weight_categories.add("239 lbs (108.41 kg)");
        wt_map.put("239 lbs (108.41 kg)", "239");
        weight_categories.add("240 lbs (108.86 kg)");
        wt_map.put("240 lbs (108.86 kg)", "240");
        weight_categories.add("241 lbs (109.32 kg)");
        wt_map.put("241 lbs (109.32 kg)", "241");
        weight_categories.add("242 lbs (109.77 kg)");
        wt_map.put("242 lbs (109.77 kg)", "242");
        weight_categories.add("243 lbs (110.22 kg)");
        wt_map.put("243 lbs (110.22 kg)", "243");
        weight_categories.add("244 lbs (110.68 kg)");
        wt_map.put("244 lbs (110.68 kg)", "244");
        weight_categories.add("245 lbs (111.13 kg)");
        wt_map.put("245 lbs (111.13 kg)", "245");
        weight_categories.add("246 lbs (111.58 kg)");
        wt_map.put("246 lbs (111.58 kg)", "246");
        weight_categories.add("247 lbs (112.04 kg)");
        wt_map.put("247 lbs (112.04 kg)", "247");
        weight_categories.add("248 lbs (112.49 kg)");
        wt_map.put("248 lbs (112.49 kg)", "248");
        weight_categories.add("249 lbs (112.94 kg)");
        wt_map.put("249 lbs (112.94 kg)", "249");
        weight_categories.add("250 lbs (113.4 kg)");
        wt_map.put("250 lbs (113.4 kg)", "250");
        weight_categories.add("251 lbs (113.85 kg)");
        wt_map.put("251 lbs (113.85 kg)", "251");
        weight_categories.add("252 lbs (114.31 kg)");
        wt_map.put("252 lbs (114.31 kg)", "252");
        weight_categories.add("253 lbs (114.76 kg)");
        wt_map.put("253 lbs (114.76 kg)", "253");
        weight_categories.add("254 lbs (115.21 kg)");
        wt_map.put("254 lbs (115.21 kg)", "254");
        weight_categories.add("255 lbs (115.67 kg)");
        wt_map.put("255 lbs (115.67 kg)", "255");
        weight_categories.add("256 lbs (116.12 kg)");
        wt_map.put("256 lbs (116.12 kg)", "256");
        weight_categories.add("257 lbs (116.57 kg)");
        wt_map.put("257 lbs (116.57 kg)", "257");
        weight_categories.add("258 lbs (117.03 kg)");
        wt_map.put("258 lbs (117.03 kg)", "258");
        weight_categories.add("259 lbs (117.48 kg)");
        wt_map.put("259 lbs (117.48 kg)", "259");
        weight_categories.add("260 lbs (117.93 kg)");
        wt_map.put("260 lbs (117.93 kg)", "260");
        weight_categories.add("261 lbs (118.39 kg)");
        wt_map.put("261 lbs (118.39 kg)", "261");
        weight_categories.add("262 lbs (118.84 kg)");
        wt_map.put("262 lbs (118.84 kg)", "262");
        weight_categories.add("263 lbs (119.29 kg)");
        wt_map.put("263 lbs (119.29 kg)", "263");
        weight_categories.add("264 lbs (119.75 kg)");
        wt_map.put("264 lbs (119.75 kg)", "264");
        weight_categories.add("265 lbs (120.2 kg)");
        wt_map.put("265 lbs (120.2 kg)", "265");
        weight_categories.add("266 lbs (120.66 kg)");
        wt_map.put("266 lbs (120.66 kg)", "266");
        weight_categories.add("267 lbs (121.11 kg)");
        wt_map.put("267 lbs (121.11 kg)", "267");
        weight_categories.add("268 lbs (121.56 kg)");
        wt_map.put("268 lbs (121.56 kg)", "268");
        weight_categories.add("269 lbs (122.02 kg)");
        wt_map.put("269 lbs (122.02 kg)", "269");
        weight_categories.add("270 lbs (122.47 kg)");
        wt_map.put("270 lbs (122.47 kg)", "270");
        weight_categories.add("271 lbs (122.92 kg)");
        wt_map.put("271 lbs (122.92 kg)", "271");
        weight_categories.add("272 lbs (123.38 kg)");
        wt_map.put("272 lbs (123.38 kg)", "272");
        weight_categories.add("273 lbs (123.83 kg)");
        wt_map.put("273 lbs (123.83 kg)", "273");
        weight_categories.add("274 lbs (124.28 kg)");
        wt_map.put("274 lbs (124.28 kg)", "274");
        weight_categories.add("275 lbs (124.74 kg)");
        wt_map.put("275 lbs (124.74 kg)", "275");
        weight_categories.add("276 lbs (125.19 kg)");
        wt_map.put("276 lbs (125.19 kg)", "276");
        weight_categories.add("277 lbs (125.65 kg)");
        wt_map.put("277 lbs (125.65 kg)", "277");
        weight_categories.add("278 lbs (126.1 kg)");
        wt_map.put("278 lbs (126.1 kg)", "278");
        weight_categories.add("279 lbs (126.55 kg)");
        wt_map.put("279 lbs (126.55 kg)", "279");
        weight_categories.add("280 lbs (127.01 kg)");
        wt_map.put("280 lbs (127.01 kg)", "280");
        weight_categories.add("281 lbs (127.46 kg)");
        wt_map.put("281 lbs (127.46 kg)", "281");
        weight_categories.add("282 lbs (127.91 kg)");
        wt_map.put("282 lbs (127.91 kg)", "282");
        weight_categories.add("283 lbs (128.37 kg)");
        wt_map.put("283 lbs (128.37 kg)", "283");
        weight_categories.add("284 lbs (128.82 kg)");
        wt_map.put("284 lbs (128.82 kg)", "284");
        weight_categories.add("285 lbs (129.27 kg)");
        wt_map.put("285 lbs (129.27 kg)", "285");
        weight_categories.add("286 lbs (129.73 kg)");
        wt_map.put("286 lbs (129.73 kg)", "286");
        weight_categories.add("287 lbs (130.18 kg)");
        wt_map.put("287 lbs (130.18 kg)", "287");
        weight_categories.add("288 lbs (130.63 kg)");
        wt_map.put("288 lbs (130.63 kg)", "288");
        weight_categories.add("289 lbs (131.09 kg)");
        wt_map.put("289 lbs (131.09 kg)", "289");
        weight_categories.add("290 lbs (131.54 kg)");
        wt_map.put("290 lbs (131.54 kg)", "290");
        weight_categories.add("291 lbs (132 kg)");
        wt_map.put("291 lbs (132 kg)", "291");
        weight_categories.add("292 lbs (132.45 kg)");
        wt_map.put("292 lbs (132.45 kg)", "292");
        weight_categories.add("293 lbs (132.9 kg)");
        wt_map.put("293 lbs (132.9 kg)", "293");
        weight_categories.add("294 lbs (133.36 kg)");
        wt_map.put("294 lbs (133.36 kg)", "294");
        weight_categories.add("295 lbs (133.81 kg)");
        wt_map.put("295 lbs (133.81 kg)", "295");
        weight_categories.add("296 lbs (134.26 kg)");
        wt_map.put("296 lbs (134.26 kg)", "296");
        weight_categories.add("297 lbs (134.72 kg)");
        wt_map.put("297 lbs (134.72 kg)", "297");
        weight_categories.add("298 lbs (135.17 kg)");
        wt_map.put("298 lbs (135.17 kg)", "298");
        weight_categories.add("299 lbs (135.62 kg)");
        wt_map.put("299 lbs (135.62 kg)", "299");
        weight_categories.add("300 lbs (136.08 kg)");
        wt_map.put("300 lbs (136.08 kg)", "300");
        weight_categories.add("301 lbs (136.53 kg)");
        wt_map.put("301 lbs (136.53 kg)", "301");
        weight_categories.add("302 lbs (136.98 kg)");
        wt_map.put("302 lbs (136.98 kg)", "302");
        weight_categories.add("303 lbs (137.44 kg)");
        wt_map.put("303 lbs (137.44 kg)", "303");
        weight_categories.add("304 lbs (137.89 kg)");
        wt_map.put("304 lbs (137.89 kg)", "304");
        weight_categories.add("305 lbs (138.35 kg)");
        wt_map.put("305 lbs (138.35 kg)", "305");
        weight_categories.add("306 lbs (138.8 kg)");
        wt_map.put("306 lbs (138.8 kg)", "306");
        weight_categories.add("307 lbs (139.25 kg)");
        wt_map.put("307 lbs (139.25 kg)", "307");
        weight_categories.add("308 lbs (139.71 kg)");
        wt_map.put("308 lbs (139.71 kg)", "308");
        weight_categories.add("309 lbs (140.16 kg)");
        wt_map.put("309 lbs (140.16 kg)", "309");
        weight_categories.add("310 lbs (140.61 kg)");
        wt_map.put("310 lbs (140.61 kg)", "310");
        weight_categories.add("311 lbs (141.07 kg)");
        wt_map.put("311 lbs (141.07 kg)", "311");
        weight_categories.add("312 lbs (141.52 kg)");
        wt_map.put("312 lbs (141.52 kg)", "312");
        weight_categories.add("313 lbs (141.97 kg)");
        wt_map.put("313 lbs (141.97 kg)", "313");
        weight_categories.add("314 lbs (142.43 kg)");
        wt_map.put("314 lbs (142.43 kg)", "314");
        weight_categories.add("315 lbs (142.88 kg)");
        wt_map.put("315 lbs (142.88 kg)", "315");
        weight_categories.add("316 lbs (143.34 kg)");
        wt_map.put("316 lbs (143.34 kg)", "316");
        weight_categories.add("317 lbs (143.79 kg)");
        wt_map.put("317 lbs (143.79 kg)", "317");
        weight_categories.add("318 lbs (144.24 kg)");
        wt_map.put("318 lbs (144.24 kg)", "318");
        weight_categories.add("319 lbs (144.7 kg)");
        wt_map.put("319 lbs (144.7 kg)", "319");
        weight_categories.add("320 lbs (145.15 kg)");
        wt_map.put("320 lbs (145.15 kg)", "320");
        weight_categories.add("321 lbs (145.6 kg)");
        wt_map.put("321 lbs (145.6 kg)", "321");
        weight_categories.add("322 lbs (146.06 kg)");
        wt_map.put("322 lbs (146.06 kg)", "322");
        weight_categories.add("323 lbs (146.51 kg)");
        wt_map.put("323 lbs (146.51 kg)", "323");
        weight_categories.add("324 lbs (146.96 kg)");
        wt_map.put("324 lbs (146.96 kg)", "324");
        weight_categories.add("325 lbs (147.42 kg)");
        wt_map.put("325 lbs (147.42 kg)", "325");
        weight_categories.add("326 lbs (147.87 kg)");
        wt_map.put("326 lbs (147.87 kg)", "326");
        weight_categories.add("327 lbs (148.32 kg)");
        wt_map.put("327 lbs (148.32 kg)", "327");
        weight_categories.add("328 lbs (148.78 kg)");
        wt_map.put("328 lbs (148.78 kg)", "328");
        weight_categories.add("329 lbs (149.23 kg)");
        wt_map.put("329 lbs (149.23 kg)", "329");
        weight_categories.add("330 lbs (149.69 kg)");
        wt_map.put("330 lbs (149.69 kg)", "330");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(weight_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = weight_categories.getItem(which);
                String select_value = (wt_map).get(weight_categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                weight_id_val = select_value;
                wt_name = select_text;

                weight_title.setText(wt_name);
            }
        });
        builderSingle.show();
    }


    class Async_CheckMobnoExist extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Patient_Profile_Activity.this);
            dialog.setMessage("Validating your mobile no. Please Wait...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "checkmobnoexists");

                System.out.println("Parameters---------------" + urls[0]);
                System.out.println("Response json---------------" + login_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {


                isValid_val = login_jsonobj.getString("isValid");
                System.out.println("isValid_val ---" + isValid_val);

                if (isValid_val.equals("true")) {

                    ask_Login();

                } else {

                    String edt_name_text = edt_name.getText().toString();
                    String edt_email_text = edt_email.getText().toString();
                    String edt_phoneno_text = edt_phoneno.getText().toString();
                    String edt_age_text = edt_age.getText().toString();

                    //---------------------------------------
                    if (check_male.isChecked()) {
                        gender_val = "1";
                    } else if (check_female.isChecked()) {
                        gender_val = "2";
                    } else if (check_thirdgender.isChecked()) {
                        gender_val = "3";
                    } else {
                        gender_val = "0";
                    }
                    //---------------------------------------


                    System.out.println("tit_val------------" + tit_val);
                    System.out.println("edt_name_text------------" + edt_name_text);
                    System.out.println("edt_email_text------------" + edt_email_text);
                    System.out.println("selected_cc_value------------" + selected_cc_value);
                    System.out.println("edt_phoneno_text------------" + edt_phoneno_text);
                    System.out.println("blood_group_val------------" + blood_group_val);
                    System.out.println("gender_val------------" + gender_val);
                    System.out.println("edt_age_text------------" + edt_age_text);
                    System.out.println("height_id_val------------" + height_id_val);
                    System.out.println("weight_id_val------------" + weight_id_val);


                    if ((edt_name_text.length() > 0)) {
                        if ((edt_email_text.length() > 0)) {
                            if ((edt_phoneno_text.length() > 0)) {

                                try {

                                    json_validate = new JSONObject();
                                    json_validate.put("user_id", Model.id);
                                    json_validate.put("token", Model.token);
                                    json_validate.put("title", tit_val);
                                    json_validate.put("name", edt_name_text);
                                    json_validate.put("email", edt_email_text);
                                    json_validate.put("country_code_mobile", selected_cc_value);
                                    json_validate.put("mobile", edt_phoneno_text);
                                    json_validate.put("blood_group", blood_group_val);
                                    json_validate.put("gender", gender_val);
                                    json_validate.put("dob", cons_select_date);
                                    json_validate.put("height_id", height_id_val);
                                    json_validate.put("weight_id", weight_id_val);

                                    System.out.println("json_validate----" + json_validate.toString());

                                    new Async_PostPatProfile().execute(json_validate);

                                    //--------------------------------------------------
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                edt_phoneno.setError("Mobile number is mandatory");
                            }
                        } else {
                            edtemail.setError("Please enter your valid Email address");
                        }
                    } else {
                        edtname.setError("Please enter your name");
                    }
                }

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void ask_Login() {

        final MaterialDialog alert = new MaterialDialog(Patient_Profile_Activity.this);
        //alert.setTitle("Mobile no not Exist..!");
        alert.setMessage("Provided mobile number already exists.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }


}
