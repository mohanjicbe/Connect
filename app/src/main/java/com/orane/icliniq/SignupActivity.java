package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hbb20.CountryCodePicker;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

public class SignupActivity extends AppCompatActivity {

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
    public static final String ref_code = "ref_code_key";
    public String pin_val, err_val, isValid_val, country_code_val, selected_cc_value, selected_cc_text, str_response, userId, mobno = "", fname, emailid, c_pwd, pwd, country;
    public String err_text, isvalid, userid;
    Button btn_cancel;
    ScrollView scrollview;
    JSONObject jsonobj;
    JSONObject post_json, login_jsonobj, json, json_validate;
    CheckBox checkterms;
    Spinner spinner_speciality;
    ProgressBar progressBar;
    Button btn_submit;
    TextView tv_terms, tv_privacy;
    EditText edt_promo, edt_otp;
    LinearLayout signup_layout;
    EditText edtmobno, edt_phoneno, edtconfirmpassword;
    EditText edtname, edtemail, edt_confirm_password;
    ShowHidePasswordEditText edtpassword;
    Map<String, String> spec_map = new HashMap<String, String>();
    LinearLayout mob_layout, otp_layout;
    TextView tv_mobno, tv_timertext;
    TextView tvterms, tv_ccode, tv_resend;
    LinearLayout timer_layout;
    RelativeLayout ccode_layout;
    MyCountDownTimer myCountDownTimer;
    CheckBox check0;
    Button btn_otp_submit;
    Map<String, String> cc_map = new HashMap<String, String>();
    CountryCodePicker countryCodePicker;
    SharedPreferences sharedpreferences;
    CheckBox check1;
    RelativeLayout name_title_layout;
    private boolean hasStarted = false;

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        FlurryAgent.onPageView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //------------------------------------------------------------
        Model.kiss.record("android.patient.Signup_Screen");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("android.patient.id", Model.id);
        Model.kiss.set(properties);
        //------------------------------------------------------------

        Model.terms_isagree = "false";
        Model.mobvalidate = "0";
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Signing Up");
        }
        //------------ Object Creations -------------------------------
        countryCodePicker = findViewById(R.id.ccp);
        btn_cancel = findViewById(R.id.btn_cancel);
        scrollview = findViewById(R.id.scrollview);
        tvterms = findViewById(R.id.tvterms);
        tv_ccode = findViewById(R.id.tv_ccode);
        mob_layout = findViewById(R.id.mob_layout);
        checkterms = findViewById(R.id.checkterms);
        edtconfirmpassword = findViewById(R.id.edtconfirmpassword);
        edt_promo = findViewById(R.id.edt_promo);
        edtname = findViewById(R.id.edtname);
        edtemail = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassword);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        edtmobno = findViewById(R.id.edtmobno);
        edt_phoneno = findViewById(R.id.edt_phoneno);
        btn_submit = findViewById(R.id.btn_submit);
        ccode_layout = findViewById(R.id.ccode_layout);
        progressBar = findViewById(R.id.progressBar);
        signup_layout = findViewById(R.id.signup_layout);
        otp_layout = findViewById(R.id.otp_layout);
        timer_layout = findViewById(R.id.timer_layout);
        tv_timertext = findViewById(R.id.tv_timertext);
        tv_mobno = findViewById(R.id.tv_mobno);
        tv_resend = findViewById(R.id.tv_resend);
        btn_otp_submit = findViewById(R.id.btn_otp_submit);
        edt_otp = findViewById(R.id.edt_otp);
        check1 = findViewById(R.id.check1);
        check0 = findViewById(R.id.check0);
        tv_terms = findViewById(R.id.tv_terms);
        tv_privacy = findViewById(R.id.tv_privacy);

        countryCodePicker.registerCarrierNumberEditText(edt_phoneno);

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                System.out.println("getSelectedCountryCode------------" + countryCodePicker.getSelectedCountryCode());
                System.out.println("getSelectedCountryCodeAsInt------------" + countryCodePicker.getSelectedCountryCodeAsInt());
                System.out.println("getSelectedCountryName------------" + countryCodePicker.getSelectedCountryName());
                System.out.println("getSelectedCountryNameCode------------" + countryCodePicker.getSelectedCountryNameCode());

                country_code_val = "" + countryCodePicker.getSelectedCountryCodeAsInt();
            }
        });

        countryCodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignupActivity.this, Terms_WebViewActivity.class);
                i.putExtra("url", Model.Basic_server + "pages/display/page/terms?nolayout=1");
                i.putExtra("type", "Terms of use");
                startActivity(i);
            }
        });

        tv_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignupActivity.this, Terms_WebViewActivity.class);
                i.putExtra("url", Model.Basic_server + "p/privacy?nolayout=1");
                i.putExtra("type", "privacy policy");
                startActivity(i);
            }
        });

        //tv_ccode.setText("+" + country_code_val);
        selected_cc_value = Model.country_code;
        selected_cc_text = Model.pat_country;

        ccode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ccode();
            }
        });

        progressBar.setMax(60);

        otp_layout.setVisibility(View.GONE);
        signup_layout.setVisibility(View.VISIBLE);


        tvterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignupActivity.this, WebViewActivity.class);
                i.putExtra("url", Model.BASE_URL + "p/terms?nolayout=1");
                i.putExtra("type", "Terms and Conditions");
                startActivity(i);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        full_process();

        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  if (isChecked) {
                                                      edt_promo.setVisibility(View.VISIBLE);
                                                  } else {
                                                      edt_promo.setVisibility(View.GONE);
                                                  }
                                              }
                                          }
        );

        checkterms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent i = new Intent(SignupActivity.this, Terms_WebViewActivity.class);
                    i.putExtra("url", Model.BASE_URL + "p/terms?nolayout=1");
                    i.putExtra("type", "Terms");
                    startActivity(i);
                    //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            }
        });


        //-------------------------------------------------------------------------------
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fname = edtname.getText().toString();
                emailid = edtemail.getText().toString();
                pwd = edtpassword.getText().toString();
                c_pwd = edt_confirm_password.getText().toString();
                mobno = edt_phoneno.getText().toString();

                if ((fname.length() > 0)) {
                    if ((emailid.length() > 0)) {
                        if ((pwd.length() > 0)) {
                            if ((pwd.equals(c_pwd))) {

                                if ((mobno.length() > 0)) {
                                    if (check0.isChecked()) {

                                        if (pwd.length() < 8 || !isValidPassword(pwd)) {
                                            System.out.println("Pwd is Not Valid");
                                            edtpassword.requestFocus();
                                            new AlertDialog.Builder(SignupActivity.this)
                                                    //.setIcon(android.R.drawable.ic_dialog_alert)
                                                    .setTitle("In valid Password")
                                                    .setMessage("Password should contain atleast 1 Uppercase, 1 lowercase, 1 number, 1 special symbol and minimum 8 charecters")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    //.setNegativeButton("No", null)
                                                    .show();
                                        } else {

                                            if (check1.isChecked()) {
                                                String pcode = edt_promo.getText().toString();
                                                if (!pcode.equals("")) {
                                                    sumbit_signup();
                                                } else {
                                                    edt_promo.setError("Please enter the promo code");
                                                    edt_promo.requestFocus();
                                                }
                                            } else {
                                                sumbit_signup();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please agree the terms and privacy policy", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_phoneno.setError("Please enter your valid mobile number");
                                    edt_phoneno.requestFocus();
                                }
                            } else {
                                edt_confirm_password.setError("Entered passwords do not match");
                                edt_confirm_password.requestFocus();
                            }

                        } else {
                            edtpassword.setError("Please enter your password");
                            edtpassword.requestFocus();
                        }
                    } else {
                        edtemail.setError("Please enter your valid Email address");
                        edtemail.requestFocus();
                    }
                } else {
                    edtname.setError("Please enter your name");
                    edtname.requestFocus();
                }
            }
        });


        btn_otp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String opt_val = edt_otp.getText().toString();

                if (!opt_val.equals("")) {
                    if (opt_val.equals(pin_val)) {
                        try {
                            post_json = new JSONObject();
                            post_json.put("fname", fname);
                            post_json.put("email", emailid);
                            post_json.put("pwd", pwd);
                            post_json.put("mobile", mobno);
                            post_json.put("ccode", country_code_val);

                            System.out.println("json------------" + post_json.toString());

                            new JSON_Post_Signup().execute(post_json);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "The OTP you have entered is incorrect. Please try again/Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the code", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "OTP has been sent again to your mobile number.", Toast.LENGTH_SHORT).show();

                progressBar.setMax(60);
                //--------------Send OTP---------------------------------------------
                try {
                    json_validate = new JSONObject();
                    json_validate.put("mobile", mobno);
                    json_validate.put("country_code", country_code_val);

                    System.out.println("json_validate----" + json_validate.toString());

                    new Async_SendOTP().execute(json_validate);

                    //--------------------------------------------------
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //--------------Send OTP---------------------------------------------

                //---------- Start Timer -------------------------------
                try {
                    if (hasStarted) {
                        myCountDownTimer.cancel();
                    }
                    timer_layout.setVisibility(View.VISIBLE);
                    myCountDownTimer = new MyCountDownTimer(60000, 1000);
                    myCountDownTimer.start();
                    hasStarted = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //---------- Start Timer -------------------------------

            }
        });


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
            String url = Model.BASE_URL + "sapp/country";
            System.out.println("url-------------" + url);
            new JSON_getCountry().execute(url);
            //----------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void full_process() {

        if (isInternetOn()) {
            process_code();
        } else {

            final MaterialDialog alert = new MaterialDialog(SignupActivity.this);
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


    public void dialog_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SignupActivity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select Country code");

        final ArrayAdapter<String> categories = new ArrayAdapter<String>(SignupActivity.this, R.layout.dialog_list_textview);

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

                tv_ccode.setText("+" + selected_cc_value);
                country_code_val = selected_cc_value;
            }
        });
        builderSingle.show();
    }

    public void ask_Signup(String err) {

        final MaterialDialog alert = new MaterialDialog(SignupActivity.this);
        alert.setTitle("Warning..!");
        alert.setMessage(err);
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });
        alert.show();
    }

    public void sumbit_signup() {

        try {

            String fname_text = edtname.getText().toString();
            String pwd_text = edtpassword.getText().toString();

            mobno = mobno.replace(" ", "");
            mobno = mobno.trim();

            json_validate = new JSONObject();
            json_validate.put("email", emailid);
            json_validate.put("ccode", country_code_val);
            json_validate.put("mobile", mobno);
            json_validate.put("fname", fname_text);
            json_validate.put("pwd", pwd_text);

            System.out.println("json_validate----" + json_validate.toString());

            new Async_validateEmailMobno().execute(json_validate);

            //--------------------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Model.terms_isagree) != null && !(Model.terms_isagree).isEmpty() && !(Model.terms_isagree).equals("null") && !(Model.terms_isagree).equals("") && !(Model.terms_isagree).equals("false")) {
            checkterms.setChecked(true);
        }
    }

    private class JSON_getCountry extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            dialog = new ProgressDialog(SignupActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);*/
            scrollview.setVisibility(View.VISIBLE);
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

                    country = jsonobj.getString("country");
                    country_code_val = jsonobj.getString("code");

                    System.out.println("country----------" + country);
                    System.out.println("country_code_val----------" + country_code_val);

                    Model.browser_country = country;
                    Model.pat_country = country;
                    tv_ccode.setText("+" + country_code_val);

/*                  if (country.equals("IN")) {
                    mob_layout.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);

                    } else {
                        mob_layout.setVisibility(View.GONE);
                        btn_submit.setVisibility(View.GONE);
                    }*/
                }

            } catch (Exception e) {
                country = "";
                e.printStackTrace();
            }

            scrollview.setVisibility(View.VISIBLE);
            //dialog.cancel();

        }
    }

    class Async_validateEmailMobno extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SignupActivity.this);
            dialog.setMessage("Validating. Please Wait...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                System.out.println("Parameters---------------" + urls[0]);

                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "validatemobnoexists");

                System.out.println("Signup json---------------" + login_jsonobj.toString());

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

                    otp_layout.setVisibility(View.VISIBLE);
                    signup_layout.setVisibility(View.GONE);

                    tv_mobno.setText("+" + country_code_val + " " + mobno);

                    //startPhoneNumberVerification(mobno);

                    //--------------Send OTP---------------------------------------------
                    try {
                        json_validate = new JSONObject();
                        json_validate.put("mobile", mobno);
                        json_validate.put("country_code", country_code_val);

                        System.out.println("json_validate----" + json_validate.toString());

                        new Async_SendOTP().execute(json_validate);

                        //--------------------------------------------------
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //--------------Send OTP---------------------------------------------

                    //---------- Start Timer -------------------------------

                    try {
                        if (hasStarted) {
                            myCountDownTimer.cancel();
                        }
                        timer_layout.setVisibility(View.VISIBLE);
                        myCountDownTimer = new MyCountDownTimer(60000, 1000);
                        myCountDownTimer.start();
                        hasStarted = true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //---------- Start Timer -------------------------------

                } else {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    if (login_jsonobj.has("err")) {
                        err_val = login_jsonobj.getString("err");

                        ask_Signup(err_val);
                    }
                }

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

            dialog = new ProgressDialog(SignupActivity.this);
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
                            Model.refcode = jsonobj.getString("ref_code");

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
                            editor.putString(ref_code, Model.refcode);
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

                                //------------ Google firebase Analitics--------------------
                                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                                Bundle params = new Bundle();
                                params.putString("name", edtname.getText().toString());
                                params.putString("emailid", edtemail.getText().toString());
                                params.putString("App_Version", Model.App_ver);
                                params.putString("pwd", pwd);
                                params.putString("mobno", mobno);
                                params.putString("country", country);
                                Model.mFirebaseAnalytics.logEvent("Signup_Success", params);
                                //------------ Google firebase Analitics--------------------

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
                            Intent i = new Intent(SignupActivity.this, Connect_Home_Actvity.class);
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

            dialog = new ProgressDialog(SignupActivity.this);
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
}
