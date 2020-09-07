package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hbb20.CountryCodePicker;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

public class Invite_doctors extends AppCompatActivity {

    Spinner spinner_ccode;
    EditText edt_mobno, edt_docname, edt_email;
    Map<String, String> cc_map = new HashMap<String, String>();
    Button btn_submit;
    JSONObject json, jsonobj_invite;
    public String mobno, docname, email, cc_name, cccode;
    Typeface font_reg, font_bold;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_doctors);

        FlurryAgent.onPageView();

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Invite Doctors");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------

        spinner_ccode = (Spinner) findViewById(R.id.spinner_ccode);
        edt_mobno = (EditText) findViewById(R.id.edt_mobno);
        edt_docname = (EditText) findViewById(R.id.edt_docname);
        edt_email = (EditText) findViewById(R.id.edt_email);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);

        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_appre1)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_req)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_docname)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_docname2)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_doc_email)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_doc_email2)).setTypeface(font_reg);

        btn_submit.setTypeface(font_bold);

        countryCodePicker.registerCarrierNumberEditText(edt_mobno);

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                System.out.println("getSelectedCountryCode------------" + countryCodePicker.getSelectedCountryCode());
                System.out.println("getSelectedCountryCodeAsInt------------" + countryCodePicker.getSelectedCountryCodeAsInt());
                System.out.println("getSelectedCountryName------------" + countryCodePicker.getSelectedCountryName());
                System.out.println("getSelectedCountryNameCode------------" + countryCodePicker.getSelectedCountryNameCode());

                cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
            }
        });

        countryCodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        country_code();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new NetCheck().netcheck(Invite_doctors.this)) {

                    email = edt_email.getText().toString();

                    try {

                        cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
                        System.out.println("cccode--------" + cccode);

                        mobno = edt_mobno.getText().toString();
                        docname = edt_docname.getText().toString();
                        email = edt_email.getText().toString();

                        mobno = mobno.replace(" ", "");

                        if (!mobno.equals("")) {
                            json = new JSONObject();
                            json.put("user_id", (Model.id));
                            json.put("country_code_mobile", cccode);
                            json.put("mobile", mobno);
                            json.put("name", docname);
                            json.put("email", email);

                            System.out.println("json----" + json.toString());
                            new Async_InviteDoctor().execute(json);

                        } else {
                            edt_mobno.setError("Mobile number is mandatory");
                            edt_mobno.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Internet connection is not available, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        spinner_ccode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cc_name = spinner_ccode.getSelectedItem().toString();
                cccode = cc_map.get(cc_name);

                System.out.println("Country_Name------" + cc_name);
                System.out.println("Country_Code------" + cccode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private class Async_InviteDoctor extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Invite_doctors.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj_invite = jParser.JSON_POST(urls[0], "Invite_doc");

                System.out.println("Parameters---------------" + urls[0]);
                System.out.println("Response jsonobj---------------" + jsonobj_invite.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                if (jsonobj_invite.has("token_status")) {
                    String token_status = jsonobj_invite.getString("token_status");
                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(Invite_doctors.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    String status_val = jsonobj_invite.getString("status");
                    System.out.println("status_val------------------" + status_val);

                    if (status_val.equals("1")) {


                        //------------ Google firebase Analitics-----------------------------------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("user_id", Model.id);
                        Model.mFirebaseAnalytics.logEvent("invite_doctor_success", params);
                        //------------ Google firebase Analitics---------------------------------------------


                        //----------------- Kissmetrics ----------------------------------
                        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                        Model.kiss.record("android.patient.invite_doctor_success");
                        HashMap<String, String> properties = new HashMap<String, String>();
                        properties.put("android.patient.user_id:", (Model.id));
                        properties.put("android.patient.country_Code:", cccode);
                        properties.put("android.patient.doc_mobile:", mobno);
                        properties.put("android.patient.doc_name:", docname);
                        properties.put("android.patient.doc_email:", email);
                        Model.kiss.set(properties);
                        //----------------- Kissmetrics ----------------------------------

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.patient.user_id:", (Model.id));
                        articleParams.put("android.patient.country_Code:", cccode);
                        articleParams.put("android.patient.doc_mobile:", mobno);
                        articleParams.put("android.patient.doc_name:", docname);
                        articleParams.put("android.patient.doc_email:", email);
                        FlurryAgent.logEvent("android.patient.invite_doctor_success", articleParams);
                        //----------- Flurry -------------------------------------------------

                        final MaterialDialog alert = new MaterialDialog(Invite_doctors.this);
                        alert.setTitle("Success..!");
                        alert.setMessage("Your invitation has been sent to the doctor.");
                        alert.setCanceledOnTouchOutside(false);
                        alert.setPositiveButton("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                finish();
                            }
                        });
                        alert.show();

                    } else {

                        final MaterialDialog alert = new MaterialDialog(Invite_doctors.this);
                        alert.setTitle("Failed..!");
                        alert.setMessage("Sorry. Your invitation failed. please try again");
                        alert.setCanceledOnTouchOutside(false);
                        alert.setPositiveButton("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                        alert.show();
                    }
                }

                dialog.cancel();

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

    public void country_code() {

        final List<String> categories = new ArrayList<String>();

        categories.add("India (+91)");
        cc_map.put("India (+91)", "91");
        categories.add("US and Canada (+1)");
        cc_map.put("US and Canada (+1)", "1");
        categories.add("United Kingdom (+44)");
        cc_map.put("United Kingdom (+44)", "44");
        categories.add("Pakistan(+92)");
        cc_map.put("Pakistan(+92)", "92");
        categories.add("Australia (+61)");
        cc_map.put("Australia (+61)", "61");
        categories.add("Philippines (+63)");
        cc_map.put("Philippines (+63)", "63");
        categories.add("Nigeria (+234)");
        cc_map.put("Nigeria (+234)", "234");
        categories.add("Malaysia (+60)");
        cc_map.put("Malaysia (+60)", "60");
        categories.add("Bangladesh (+880)");
        cc_map.put("Bangladesh (+880)", "880");
        categories.add("Kenya (+254)");
        cc_map.put("Kenya (+254)", "254");
        categories.add("spain (+34)");
        cc_map.put("spain (+34)", "34");
        categories.add("Saudi Arabia(+966)");
        cc_map.put("Saudi Arabia(+966)", "966");
        categories.add("Singapore (+65)");
        cc_map.put("Singapore (+65)", "65");
        categories.add("Netherlands (+31)");
        cc_map.put("Netherlands (+31)", "31");
        categories.add("Qatar(+974)");
        cc_map.put("Qatar(+974)", "974");
        categories.add("United Arab Emirates(+971)");
        cc_map.put("UAE (+971)", "971");

        categories.add("---------------");
        categories.add("Austria (+43)");
        cc_map.put("Austria (+43)", "43");

        categories.add("Egypt (+20)");
        cc_map.put("Egypt (+20)", "20");

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

        categories.add("Mauritania(+222)");
        cc_map.put("Mauritania(+222)", "222");

        categories.add("Mali (+223)");
        cc_map.put("Mali (+223)", "223");

        categories.add("Guinea (+224)");
        cc_map.put("Guinea (+224)", "224");

        categories.add("Ivory Coast (+225)");
        cc_map.put("Ivory Coast (+225)", "225");

        categories.add("Burkina Faso (+226)");
        cc_map.put("Burkina Faso (+226)", "226");

        categories.add("Niger (+227)");
        cc_map.put("Niger (+227)", "227");

        categories.add("Togo (+228)");
        cc_map.put("Togo (+228)", "228");

        categories.add("Benin(+229)");
        cc_map.put("Benin(+229)", "229");

        categories.add("Mauritius (+230)");
        cc_map.put("Mauritius (+230)", "230");

        categories.add("Liberia (+231)");
        cc_map.put("Liberia (+231)", "231");

        categories.add("Sierra Leone (+232)");
        cc_map.put("Sierra Leone (+232)", "232");

        categories.add("Ghana (+233)");
        cc_map.put("Ghana (+233)", "233");

        categories.add("Chad (+235)");
        cc_map.put("Chad (+235)", "235");

        categories.add("Central African Republic (+236)");
        cc_map.put("Central African Republic (+236)", "236");

        categories.add("Cameroon (+237)");
        cc_map.put("Cameroon (+237)", "237");

        categories.add("Cape Verde (+238)");
        cc_map.put("Cape Verde (+238)", "238");

        categories.add("Sao Tome and Príncipe (+239)");
        cc_map.put("Sao Tome and Príncipe (+239)", "239");

        categories.add("Equatorial Guinea (+240)");
        cc_map.put("Equatorial Guinea (+240)", "240");

        categories.add("Gabon (+241)");
        cc_map.put("Gabon (+241)", "241");

        categories.add("Republic of the Congo (+242)");
        cc_map.put("Republic of the Congo (+242)", "242");

        categories.add("Democratic Republic of the congo (+243)");
        cc_map.put("Democratic Republic of the congo (+243)", "243");

        categories.add("Angola (+244)");
        cc_map.put("Angola (+244)", "244");

        categories.add("Guinea-Bissau (+245)");
        cc_map.put("Guinea-Bissau (+245)", "245");

        categories.add("British Indian Ocean Territory (+246)");
        cc_map.put("British Indian Ocean Territory (+246)", "246");

        categories.add("Ascension Island (+247)");
        cc_map.put("Ascension Island (+247)", "247");

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
        cc_map.put("Djibouti (+253", "253");

        categories.add("Tanzania (+255)");
        cc_map.put("Tanzania (+255)", "255");

        categories.add("Uganda (+256)");
        cc_map.put("Uganda (+256)", "256");

        categories.add("Berundi (+257)");
        cc_map.put("Uganda (+256)", "257");

        categories.add("Mozambique (+258)");
        cc_map.put("Mozambique (+258)", "258");

        categories.add("Zambia (+260)");
        cc_map.put("Zambia (+260)", "260");

        categories.add("Madagascar (+261)");
        cc_map.put("Madagascar (+261)", "261");

        categories.add("Reunion (+262)");
        cc_map.put("Reunion (+262)", "262");

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

        categories.add("Saint  Helena (+290)");
        cc_map.put("Saint  Helena (+290)", "290");

        categories.add("Aruba (+297)");
        cc_map.put("Aruba (+297)", "297");

        categories.add("Faroe Islands (+298)");
        cc_map.put("Faroe Islands (+298)", "298");

        categories.add("Greenland (+299)");
        cc_map.put("Greenland (+299)", "299");

        categories.add("South Africa (+27)");
        cc_map.put("South Africa (+27)", "27");

        categories.add("Greece (+30)");
        cc_map.put("Greece (+30)", "30");

        categories.add("Belgium (+32)");
        cc_map.put("Belgium (+32)", "32");

        categories.add("France (+33)");
        cc_map.put("France (+33)", "33");

        categories.add("Gibraltar (+350)");
        cc_map.put("Gibraltar (+350)", "350");

        categories.add("Portugal (+351)");
        cc_map.put("Portugal (+351)", "351");

        categories.add("Luxembourg (+352)");
        cc_map.put("Luxembourg (+352)", "352");

        categories.add("Ireland (+353)");
        cc_map.put("Ireland (+353)", "353");

        categories.add("Iceland(+354)");
        cc_map.put("Iceland(+354)", "354");

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

        categories.add("Hungary (+36)");
        cc_map.put("Hungary (+36)", "36");

        categories.add("Italy (+39)");
        cc_map.put("Italy (+39)", "39");

        categories.add("Lithuania (+370)");
        cc_map.put("Lithuania (+370)", "370");

        categories.add("Latvia (+371)");
        cc_map.put("Latvia (+371)", "371");

        categories.add("Estonia(+372)");
        cc_map.put("Estonia(+372)", "372");

        categories.add("Moldova (+373)");
        cc_map.put("Moldova (+373)", "373");

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

        categories.add("Vatican City (+379)");
        cc_map.put("Vatican City (+379)", "379");

        categories.add("Ukraine (+380)");
        cc_map.put("Ukraine (+380)", "380");

        categories.add("Serbia (+381)");
        cc_map.put("Serbia (+381)", "381");

        categories.add("Montenegro (+383)");
        cc_map.put("Montenegro (+383)", "383");

        categories.add("Croatia (+385)");
        cc_map.put("Croatia (+385)", "385");

        categories.add("Slovenia (+386)");
        cc_map.put("Slovenia (+386)", "386");

        categories.add("Bosnia and Herzegovina (+387)");
        cc_map.put("Bosnia and Herzegovina (+387)", "387");

        categories.add("Romania (+40)");
        cc_map.put("Romania (+40)", "40");

        categories.add("Switzerland (+41)");
        cc_map.put("Switzerland (+41)", "41");

        categories.add("Czech Republic (+420)");
        cc_map.put("Czech Republic (+420)", "420");

        categories.add("Slovakia (+421)");
        cc_map.put("Slovakia (+421)", "421");

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

        categories.add("Mexico(+52)");
        cc_map.put("Mexico(+52)", "52");

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

        categories.add("Guadeloupe (+590)");
        cc_map.put("Guadeloupe (+590)", "590");

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

        categories.add("Indonesia (+62)");
        cc_map.put("Indonesia (+62)", "62");

        categories.add("New Zealand (+64)");
        cc_map.put("New Zealand (+64)", "64");

        categories.add("Thailand (+66)");
        cc_map.put("Thailand (+66)", "66");

        categories.add("Martinique (+596)");
        cc_map.put("Martinique (+596)", "596");

        categories.add("Suriname (+597)");
        cc_map.put("Suriname (+597)", "597");

        categories.add("Uruguay(+598)");
        cc_map.put("Uruguay(+598)", "598");

        categories.add("Brunei(+673)");
        cc_map.put("Brunei(+673)", "673");

        categories.add("Nauru(+674)");
        cc_map.put("Nauru(+674)", "674");

        categories.add("Papua new Guinea(+675)");
        cc_map.put("Papua new Guinea(+675)", "675");

        categories.add("Tonga(+676)");
        cc_map.put("Tonga(+676)", "676");

        categories.add("Solomon Islands(+677)");
        cc_map.put("Solomon Islands(+677)", "677");

        categories.add("Vanuatu(+678)");
        cc_map.put("Vanuatu(+678)", "678");

        categories.add("Fizi(+679)");
        cc_map.put("Fizi(+679)", "679");

        categories.add("Palau(+680)");
        cc_map.put("Palau(+680)", "680");

        categories.add("Wallis and Futuna(+681)");
        cc_map.put("Wallis and Futuna(+681)", "681");

        categories.add("Cook islands(+682)");
        cc_map.put("Cook islands(+682)", "682");

        categories.add("Niue(+683)");
        cc_map.put("Niue(+683)", "683");

        categories.add("Tuvalu(+688)");
        cc_map.put("Tuvalu(+688)", "688");

        categories.add("Samoa(+685)");
        cc_map.put("Samoa(+685)", "685");

        categories.add("Kiribati(+686)");
        cc_map.put("Kiribati(+686)", "686");

        categories.add("New Polynesia(+687)");
        cc_map.put("New Polynesia(+687)", "687");

        categories.add("French Polynesia(+689)");
        cc_map.put("French Polynesia(+689)", "689");

        categories.add("Tokelau(+690)");
        cc_map.put("Tokelau(+690)", "690");

        categories.add("Federated States of Micronesia(+691)");
        cc_map.put("Federated States of Micronesia(+691)", "691");

        categories.add("Marshall islands(+692)");
        cc_map.put("Marshall islands(+692)", "692");

        categories.add("Russia(+7)");
        cc_map.put("Russia(+7)", "7");

        categories.add("Japan(+81)");
        cc_map.put("Japan(+81)", "81");

        categories.add("South Korea(+82)");
        cc_map.put("South Korea(+82)", "82");

        categories.add("Vietnam(+84)");
        cc_map.put("Vietnam(+84)", "84");

        categories.add("China(+86)");
        cc_map.put("China(+86)", "86");

        categories.add("Taiwan(+886)");
        cc_map.put("Taiwan(+886)", "886");

        categories.add("Turkey(+90)");
        cc_map.put("Turkey(+90)", "90");

        categories.add("Afghanistan(+93)");
        cc_map.put("Afghanistan(+93)", "93");

        categories.add("Srilanka(+94)");
        cc_map.put("Srilanka(+94)", "94");

        categories.add("Myanmar(+95)");
        cc_map.put("Myanmar(+95)", "95");

        categories.add("Maldives(+960)");
        cc_map.put("Maldives(+960)", "960");

        categories.add("Lebanon(+961)");
        cc_map.put("Lebanon(+961)", "961");

        categories.add("Jordan(+962)");
        cc_map.put("Jordan(+962)", "962");

        categories.add("Syria(+963)");
        cc_map.put("Syria(+963)", "963");

        categories.add("Iraq(+964)");
        cc_map.put("Iraq(+964)", "964");

        categories.add("Kuwait(+965)");
        cc_map.put("Kuwait(+965)", "965");

        categories.add("Yemen(+967)");
        cc_map.put("Yemen(+967)", "967");

        categories.add("Oman(+968)");
        cc_map.put("Oman(+968)", "968");

        categories.add("Palestinian territories(+970)");
        cc_map.put("Palestinian territories(+970)", "970");

        categories.add("Israel(+972)");
        cc_map.put("Israel(+972)", "972");

        categories.add("Bahrain(+973)");
        cc_map.put("Bahrain(+973)", "973");

        categories.add("Bhutan(+975)");
        cc_map.put("Bhutan(+975)", "975");

        categories.add("Mongolia(+976)");
        cc_map.put("Mongolia(+976)", "976");

        categories.add("Nepal(+977)");
        cc_map.put("Nepal(+977)", "977");

        categories.add("Iran(+98)");
        cc_map.put("Iran(+98)", "98");

        categories.add("Tajikistan(+992)");
        cc_map.put("Tajikistan(+992)", "992");

        categories.add("Turkmenistan(+993)");
        cc_map.put("Turkmenistan(+993)", "993");

        categories.add("Azerbaijan(+994)");
        cc_map.put("Azerbaijan(+994)", "994");

        categories.add("Georgia(+995)");
        cc_map.put("Georgia(+995)", "995");

        categories.add("Kyrgyzstan(+996)");
        cc_map.put("Kyrgyzstan(+996)", "996");

        categories.add("Uzbekistan(+998)");
        cc_map.put("Uzbekistan(+998)", "998");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Invite_doctors.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ccode.setAdapter(dataAdapter);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
