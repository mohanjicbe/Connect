package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.SetLanguage;
import com.orane.icliniq.network.ShareIntent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class CommonActivity extends AppCompatActivity {

    LinearLayout mywallet_layout, terms_layout, profile_layout, policy_layout, reportissue_layout, rate_layout, share_layout, aredoctor_layout, pv_consult_layout, signout_layout;
    Switch switch_notisound, switch_stopnoti;
    EditText edt_feedback;
    public String noti_sound_val, stop_noti_val;
    ImageView facebook, twitter, youtube, pinterest, gplus;
    TextView phno;
    public String radio_sel, do_like_flag_val, feedback_val, report_response;
    JSONObject json_feedback, json_response_obj;
    TextView tvaddress, tvaddress2, tvcallcenter, tvemail;
    LinearLayout feedback_layout, contactus_layout;

    SharedPreferences sharedpreferences;
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String app_language = "app_language_key";
    public static final String do_like_flag = "do_like_flag_key";

    Button btn_submit;
    LinearLayout terms;
    TextView tv_appver;
    TextView tv_apprel;
    Button btn_check;

    Map<String, String> lang_map = new HashMap<String, String>();
    Spinner spinner_lang;

    String lang_name, type_text, lang_val;
    View view;
    TextView mTitle;
    Typeface font_reg, font_bold;
    LinearLayout aboutus_layout, settings_layout, support_layout, report_layout;
    TableRow support_mail_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity);

        try {

            Intent intent = getIntent();
            type_text = intent.getStringExtra("type");

            System.out.println("type_text---------" + type_text);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //------------ Object Creations -------------------------------

        support_mail_id = (TableRow) findViewById(R.id.support_mail_id);
        aboutus_layout = (LinearLayout) findViewById(R.id.aboutus_layout);
        settings_layout = (LinearLayout) findViewById(R.id.settings_layout);
        support_layout = (LinearLayout) findViewById(R.id.support_layout);
        report_layout = (LinearLayout) findViewById(R.id.report_layout);
        feedback_layout = (LinearLayout) findViewById(R.id.feedback_layout);
        contactus_layout = (LinearLayout) findViewById(R.id.contactus_layout);

        signout_layout = (LinearLayout) findViewById(R.id.signout_layout);
        mywallet_layout = (LinearLayout) findViewById(R.id.mywallet_layout);
        terms_layout = (LinearLayout) findViewById(R.id.terms_layout);
        policy_layout = (LinearLayout) findViewById(R.id.policy_layout);
        reportissue_layout = (LinearLayout) findViewById(R.id.reportissue_layout);
        rate_layout = (LinearLayout) findViewById(R.id.rate_layout);
        share_layout = (LinearLayout) findViewById(R.id.share_layout);
        aredoctor_layout = (LinearLayout) findViewById(R.id.aredoctor_layout);
        pv_consult_layout = (LinearLayout) findViewById(R.id.pv_consult_layout);
        profile_layout = (LinearLayout) findViewById(R.id.profile_layout);
        switch_notisound = (Switch) findViewById(R.id.switch_notisound);
        switch_stopnoti = (Switch) findViewById(R.id.switch_stopnoti);
        spinner_lang = (Spinner) findViewById(R.id.spinner_lang);

        tvaddress = (TextView) findViewById(R.id.tvaddress);
        tvaddress2 = (TextView) findViewById(R.id.tvaddress2);
        tvcallcenter = (TextView) findViewById(R.id.tvcallcenter);
        tvemail = (TextView) findViewById(R.id.tvemail);

        tv_apprel = (TextView) findViewById(R.id.tv_apprel);
        tv_appver = (TextView) findViewById(R.id.tv_appver);
        terms = (LinearLayout) findViewById(R.id.terms);
        btn_check = (Button) findViewById(R.id.btn_check);

        facebook = (ImageView) findViewById(R.id.facebook);
        twitter = (ImageView) findViewById(R.id.twitter);
        youtube = (ImageView) findViewById(R.id.youtube);
        pinterest = (ImageView) findViewById(R.id.pinterest);
        gplus = (ImageView) findViewById(R.id.gplus);
        phno = (TextView) findViewById(R.id.phno);

        //----------- Feedback Content -----------------------------------
        edt_feedback = (EditText) findViewById(R.id.edt_feedback);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        feedback_layout = (LinearLayout) findViewById(R.id.feedback_layout);
        //----------- Feedback Content -----------------------------------

        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_share_tit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_rate_app)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_share_friends)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_sugg_doc)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_app_sett)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_noti_sound)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_noti_stat)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_about)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_terms)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_privatepolicy)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_reportissue)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_rudoc)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_signout)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_mywallet)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_profile)).setTypeface(font_reg);

        ((TextView) findViewById(R.id.tv_appver)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_apprel)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv1)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.desc)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tvterms)).setTypeface(font_reg);

        ((TextView) findViewById(R.id.fb_tit)).setTypeface(font_bold);
        ((RadioButton) findViewById(R.id.radioButton1)).setTypeface(font_reg);
        ((RadioButton) findViewById(R.id.radioButton2)).setTypeface(font_reg);
        ((RadioButton) findViewById(R.id.radioButton3)).setTypeface(font_reg);
        ((RadioButton) findViewById(R.id.radioButton4)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.fb_head)).setTypeface(font_reg);
        ((EditText) findViewById(R.id.edt_feedback)).setTypeface(font_reg);

        btn_submit.setTypeface(font_bold);

        radio_sel = "I am facing issues with consultation";

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radioButton1:
                        feedback_layout.setVisibility(View.GONE);
                        radio_sel = "I am facing issues with consultation";
                        break;
                    case R.id.radioButton2:
                        feedback_layout.setVisibility(View.GONE);
                        radio_sel = "I am facing technical issues";
                        break;
                    case R.id.radioButton3:
                        feedback_layout.setVisibility(View.GONE);
                        radio_sel = "I am facing issues with submit payment";
                        break;
                    case R.id.radioButton4:
                        feedback_layout.setVisibility(View.VISIBLE);
                        radio_sel = "4";
                        break;
                }
            }
        });


        support_mail_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "icliniq@icliniq.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radio_sel.equals("4")) {

                    String feedback_text = edt_feedback.getText().toString();

                    if (!feedback_text.equals("")) {
                        apply_feedback(feedback_text);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter the feedback", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    apply_feedback(radio_sel);
                }
                //say_success();
            }
        });


        //------ About us Content ------------------------------
        String app_ver = "App version: " + Model.App_ver;
        String rel_ver = "Released on: " + Model.App_Rel;

        tv_appver.setText(app_ver);
        tv_apprel.setText(rel_ver);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CommonActivity.this, WebViewActivity.class);
                i.putExtra("url", Model.BASE_URL + "p/terms?nolayout=1");
                i.putExtra("type", "Terms");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                Model.mFirebaseAnalytics.logEvent("terms", params);
                //------------ Google firebase Analitics--------------------
            }
        });
        //------ About us Content ------------------------------

        tvaddress.setText(Html.fromHtml("<br>No.174, Rangasamy Goundan Pudur,<br>" +
                "Chinniyampalayam (P.O),<br>" +
                "Avinashi Road,<br>" +
                "Coimbatore - 641062.<br>" +
                "Tamil Nadu, India.<br>" +
                "Phone: 0422-2626663"));

        tvaddress2.setText(Html.fromHtml("No.117/A, 18th Main, 24th Cross,<br>" +
                "Sector 3, HSR Layout,<br>" +
                "Bengaluru - 560 102,<br>" +
                "Karnataka, India"));

        //---------------- Support Content -----------------------------------
        gplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://plus.google.com/110128099624226749397"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://www.facebook.com/icliniq"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://www.twitter.com/icliniq"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://www.youtube.com/icliniq"));
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        pinterest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://www.pinterest.com/icliniq/"));
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*phno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+" + phno.getText().toString().trim()));
                startActivity(callIntent);
            }
        });*/
        //---------------- Support Content -----------------------------------

        //================ Initialize ======================---------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "on");
        noti_sound_val = sharedpreferences.getString(noti_sound, "on");
        lang_val = sharedpreferences.getString(app_language, "en");

        if (stop_noti_val.equals("on")) switch_stopnoti.setChecked(true);
        else switch_stopnoti.setChecked(false);

        if (noti_sound_val.equals("on")) switch_notisound.setChecked(true);
        else switch_notisound.setChecked(false);
        //================ Initialize ======================---------------

        if (type_text.equals("settings")) {
            settings_layout.setVisibility(View.VISIBLE);
            aboutus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.GONE);
            mTitle.setText("Settings");
        } else if (type_text.equals("aboutus")) {
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.VISIBLE);
            support_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.GONE);
            mTitle.setText("About the app");
        } else if (type_text.equals("support")) {
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.VISIBLE);
            report_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.GONE);
            mTitle.setText("Customer Support");
        } else if (type_text.equals("feedback")) {
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.VISIBLE);
            contactus_layout.setVisibility(View.GONE);
            mTitle.setText("Feedback");

        } else if (type_text.equals("contactus")) {
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.VISIBLE);
            mTitle.setText("Contact us");
        }


        //------- Setting Language ----------------------
        final List<String> lang_categories = new ArrayList<String>();

        lang_categories.add("Choose Language");
        lang_map.put("Choose Language", "en");


        lang_categories.add("English");
        lang_map.put("English", "en");
        lang_categories.add("Hindi");
        lang_map.put("Hindi", "hi");
        lang_categories.add("Telugu");
        lang_map.put("Telugu", "te");
        lang_categories.add("Tamil");
        lang_map.put("Tamil", "ta");
        lang_categories.add("Kannada");
        lang_map.put("Kannada", "ka");

/*        if ((Model.browser_country).equals("IN")) {
            lang_categories.add("English");
            lang_map.put("English", "en");
            lang_categories.add("Hindi");
            lang_map.put("Hindi", "hi");
            lang_categories.add("Telugu");
            lang_map.put("Telugu", "te");
            lang_categories.add("Tamil");
            lang_map.put("Tamil", "ta");
            lang_categories.add("Kannada");
            lang_map.put("Kannada", "ka");
        }*/

        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(CommonActivity.this, android.R.layout.simple_spinner_item, lang_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lang.setAdapter(lang_dataAdapter);
        //---------------------------------------------

        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lang_name = spinner_lang.getSelectedItem().toString();
                lang_val = lang_map.get(lang_name);
                Model.app_lang = lang_val;

                /*Model.cons_lang = lang_name;
                Model.cons_lang_code = lang_val;*/


                //----------- Flurry -------------------------------------------------
                HashMap<String, String> properties2 = new HashMap<String, String>();
                properties2.put("lang_val", lang_val);
                properties2.put("lang_name", lang_name);
                FlurryAgent.logEvent("android.patient.Sett_Language", properties2);
                //----------- Flurry -------------------------------------------------


                System.out.println("lang_name----------" + lang_name);
                System.out.println("lang_val----------" + lang_val);

                set_lang();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        switch_notisound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) noti_sound_val = "on";
                else noti_sound_val = "off";

                //===============Apply Noti Settings Values=============================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(noti_sound, noti_sound_val);
                editor.apply();
                //===============Apply Noti Settings Values=============================================

                System.out.println("noti_sound_val-------" + noti_sound_val);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("switch_notisound", noti_sound_val);
                Model.mFirebaseAnalytics.logEvent("Settings", params);
                //------------ Google firebase Analitics--------------------

            }
        });

        switch_stopnoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) stop_noti_val = "on";
                else stop_noti_val = "off";

                //===============Apply Noti Settings Values=============================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(noti_status, stop_noti_val);
                editor.apply();
                //===============Apply Noti Settings Values=============================================

                System.out.println("stop_noti_val-------" + stop_noti_val);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("stop_noti_val", stop_noti_val);
                Model.mFirebaseAnalytics.logEvent("Settings", params);
                //------------ Google firebase Analitics--------------------

            }
        });


        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CommonActivity.this, WebViewActivity.class);
                i.putExtra("url", Model.Basic_server + "p/terms?nolayout=1");
                i.putExtra("type", "Terms");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);



            }
        });

        policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CommonActivity.this, WebViewActivity.class);
                i.putExtra("url", Model.Basic_server + "p/privacy?nolayout=1");
                i.putExtra("type", "Privacy Policy");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


            }
        });

        reportissue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                settings_layout.setVisibility(View.GONE);
                aboutus_layout.setVisibility(View.GONE);
                support_layout.setVisibility(View.GONE);
                report_layout.setVisibility(View.VISIBLE);
                mTitle.setText("Feedback");

            }
        });


     /*   mywallet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), MyWalletActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
*/

        rate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en_US";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ShareIntent sintent = new ShareIntent();
                    sintent.ShareApp(CommonActivity.this, "MainActivity");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                Model.mFirebaseAnalytics.logEvent("Settings_AppShare", params);
                //------------ Google firebase Analitics--------------------

            }
        });

        aredoctor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = "https://play.google.com/store/apps/details?id=com.orane.docassist";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                Model.mFirebaseAnalytics.logEvent("AreYouDoctor", params);
                //------------ Google firebase Analitics--------------------


            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*
                Intent i = new Intent(CommonActivity.this, MyProfileActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                Model.mFirebaseAnalytics.logEvent("View_Profile", params);
                //------------ Google firebase Analitics--------------------*/
            }
        });


        pv_consult_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CommonActivity.this, Invite_doctors.class);
                startActivity(intent);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                Model.mFirebaseAnalytics.logEvent("Invite_Doctors", params);
                //------------ Google firebase Analitics--------------------


            }
        });

        signout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                Model.mFirebaseAnalytics.logEvent("Logout", params);
                //------------ Google firebase Analitics--------------------

                ask_logout();
            }
        });

    }

    public void ask_logout() {


        final MaterialDialog alert = new MaterialDialog(CommonActivity.this);
        alert.setTitle("Logout.!");
        alert.setMessage("Are you sure you want to logout?");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                finishAffinity();
                Intent intent = new Intent(CommonActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        alert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }


    public void set_lang() {
        //================ Set Language ======================---------------
        SetLanguage sl = new SetLanguage();
        sl.set_lang((TextView) view.findViewById(R.id.tv_pref_tit), lang_val, "tv_pref_tit");
        sl.set_lang((TextView) view.findViewById(R.id.tv_share_tit), lang_val, "tv_share_tit");
        sl.set_lang((TextView) view.findViewById(R.id.tv_rate_app), lang_val, "tv_rate_app");
        sl.set_lang((TextView) view.findViewById(R.id.tv_share_friends), lang_val, "tv_share_friends");
        sl.set_lang((TextView) view.findViewById(R.id.tv_sugg_doc), lang_val, "tv_sugg_doc");
        sl.set_lang((TextView) view.findViewById(R.id.tv_noti_sound), lang_val, "tv_noti_sound");
        sl.set_lang((TextView) view.findViewById(R.id.tv_noti_stat), lang_val, "tv_noti_stat");
        sl.set_lang((TextView) view.findViewById(R.id.tv_terms), lang_val, "tv_terms");
        sl.set_lang((TextView) view.findViewById(R.id.tv_privatepolicy), lang_val, "tv_privatepolicy");
        sl.set_lang((TextView) view.findViewById(R.id.tv_reportissue), lang_val, "tv_reportissue");
        sl.set_lang((TextView) view.findViewById(R.id.tv_rudoc), lang_val, "tv_rudoc");
        sl.set_lang((TextView) view.findViewById(R.id.tv_signout), lang_val, "tv_signout");
        //================ Set Language ======================---------------
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

    public void apply_feedback(String feedback) {

        try {
            json_feedback = new JSONObject();
            json_feedback.put("user_id", (Model.id));
            json_feedback.put("text", feedback);
            //json_feedback.put("option", feedback);

            System.out.println("Json_feedback---" + json_feedback.toString());

            try {

                //--------------------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.patient.user_id:", Model.id);
                articleParams.put("android.patient.report_issue_text:", feedback);
                FlurryAgent.logEvent("android.patient.report_issue", articleParams);
                //----------- Flurry -------------------------------------------------

                //------------ Google firebase Analitics-----------------------------------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                params.putString("Text", feedback);
                Model.mFirebaseAnalytics.logEvent("Feedback", params);
                //------------ Google firebase Analitics-----------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }

            new JSON_Feedback().execute(json_feedback);

            //say_success();

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CommonActivity.this);
            dialog.setMessage("Submitting..., please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "Feedback_post");

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

                if (json_response_obj.has("token_status")) {

                    String token_status = json_response_obj.getString("token_status");
                    if (token_status.equals("0")) {

                        finishAffinity();
                        Intent intent = new Intent(CommonActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    do_like_flag_val = "false";

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(do_like_flag, "false");
                    editor.apply();
                    //============================================================

                    report_response = json_response_obj.getString("status");
                    System.out.println("report_response--------------" + report_response);

                    if (report_response.equals("1")) {

                        say_success();

                    } else {
                        Toast.makeText(getApplicationContext(), report_response, Toast.LENGTH_LONG).show();
                    }
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void say_success() {

        final MaterialDialog alert = new MaterialDialog(CommonActivity.this);
        alert.setTitle("Thank you.!");
        alert.setMessage("Would you like to post your review on play store. This will help and motivate us a lot");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Sure", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();
                try {
                    final String appPackageName = getPackageName();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.setNegativeButton("No, thanks", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();
            }
        });
        alert.show();

    }
}
