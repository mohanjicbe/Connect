package com.orane.icliniq.Parallex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.AskQuery1;
import com.orane.icliniq.HotlinePackagesActivity;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Model.SlidingTabLayout;
import com.orane.icliniq.Parallex.libs.ParallaxFragmentPagerAdapter;
import com.orane.icliniq.Parallex.libs.ParallaxViewPagerBaseActivity;
import com.orane.icliniq.R;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class ParallexMainActivity extends ParallaxViewPagerBaseActivity {

    private LinearLayout mTopImage;
    private SlidingTabLayout mNavigBar;
    LinearLayout bg_layout;

    ImageView image;
    public File imageFile;
    CircleImageView imageview_poster;
    TextView tv_tooltit, tv_star_text, tv_tooldesc, tvdocname, tvedu, tvspec;
    EditText edt_query;
    Button btn_submit;
    public String strHtml_text, rating_lbl, is_star_val, avg_rating_val, str_response, has_hline, clinics, treatment_skills, language, docurl, doc_photo_url, Doc_id, Docname, Docedu, Docspec, cfee, qfee;
    public String query_txt, qid;
    public LinearLayout rating_layout,netcheck_layout, full_layout;
    public JSONObject jsonobj_postq, json, jsonobj_docprof;
    Toolbar toolbar;
    ScrollView scrollview, doc_layout;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    SharedPreferences sharedpreferences;
    public String uname, pass, Log_Status;
    Button btn_hotlineplans, btn_hotline;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallex_activity_main);

        //--------------------------------------------------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Doctor Profile");

            tv_tooltit = toolbar.findViewById(R.id.tv_tooltit);
            tv_tooldesc = toolbar.findViewById(R.id.tv_tooldesc);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //----------- initialize --------------------------------------

        initValues();

        bg_layout = findViewById(R.id.bg_layout);
        mTopImage = findViewById(R.id.image);
        mViewPager = findViewById(R.id.view_pager);
        mNavigBar = findViewById(R.id.navig_tab);
        ratingBar = findViewById(R.id.ratingBar);
        tv_star_text = findViewById(R.id.tv_star_text);

        mHeader = findViewById(R.id.header);

        if (savedInstanceState != null) {
            mTopImage.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
            mHeader.setTranslationY(savedInstanceState.getFloat(HEADER_TRANSLATION_Y));
        }

        FlurryAgent.onPageView();
        setupAdapter();

        try {
            Intent intent = getIntent();
            Doc_id = intent.getStringExtra("tv_doc_id");
            System.out.println("Docid----get_Intent----------->" + Doc_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

/*      GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.loading);*/

        tvdocname = findViewById(R.id.tvdocname);

        tvedu = findViewById(R.id.tvedu);
        tvspec = findViewById(R.id.tvspec);
        btn_hotline = findViewById(R.id.btn_hotline);
        imageview_poster = findViewById(R.id.imageview_poster);
        rating_layout = findViewById(R.id.rating_layout);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tvdocname.setTypeface(font_bold);
        tvedu.setTypeface(font_reg);
        tvspec.setTypeface(font_reg);

        tv_tooltit.setTypeface(font_reg);
        tv_tooldesc.setTypeface(font_reg);

        Model.doctor_id = Doc_id;

        if (new NetCheck().netcheck(ParallexMainActivity.this)) {

            try {
                //-------------------------------------------
                String url = Model.BASE_URL + "sapp/doctor?user_id=" + (Model.id) + "&id=" + Doc_id + "&token=" + Model.token;
                System.out.println("url-------" + url);
                new JSON_DoctorProf().execute(url);
                //----------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
        }

        btn_hotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParallexMainActivity.this, HotlinePackagesActivity.class);
                intent.putExtra("Doctor_id", Doc_id);
                intent.putExtra("Doctor_name", Docname);
                intent.putExtra("tv_docurl", doc_photo_url);
                intent.putExtra("share_url", docurl);
                startActivity(intent);
            }
        });

        rating_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_tips();
            }
        });


    }


    private class JSON_DoctorProf extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ParallexMainActivity.this);
            dialog.setMessage("Please wait...");
            dialog.show();
            dialog.setCancelable(false);

            mTopImage.setVisibility(View.GONE);
            bg_layout.setVisibility(View.VISIBLE);
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
                jsonobj_docprof = new JSONObject(str_response);

                if (jsonobj_docprof.has("token_status")) {
                    String token_status = jsonobj_docprof.getString("token_status");
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

                    System.out.println("jsonobj_docprof----" + jsonobj_docprof.toString());

                    Doc_id = jsonobj_docprof.getString("id");
                    Docname = jsonobj_docprof.getString("name");
                    doc_photo_url = jsonobj_docprof.getString("photo_url");
                    Docedu = jsonobj_docprof.getString("edu");
                    Docspec = jsonobj_docprof.getString("speciality");
                    language = jsonobj_docprof.getString("language");
                    treatment_skills = jsonobj_docprof.getString("treatment_skills");
                    clinics = jsonobj_docprof.getString("clinics");
                    cfee = jsonobj_docprof.getString("cfee");
                    qfee = jsonobj_docprof.getString("qfee");
                    docurl = jsonobj_docprof.getString("url");
                    has_hline = jsonobj_docprof.getString("has_hline");

                    is_star_val = jsonobj_docprof.getString("is_star");
                    avg_rating_val = jsonobj_docprof.getString("avg_rating");
                    rating_lbl = jsonobj_docprof.getString("rating_lbl");
                    strHtml_text = jsonobj_docprof.getString("strHtmlContent");


                    if (is_star_val != null && !is_star_val.isEmpty() && !is_star_val.equals("null") && !is_star_val.equals("")) {
                        if (is_star_val.equals("1")) {
                            ratingBar.setVisibility(View.VISIBLE);
                            tv_star_text.setVisibility(View.VISIBLE);

                            ratingBar.setRating(Float.parseFloat(avg_rating_val));
                            tv_star_text.setText(rating_lbl);
                        } else {
                            ratingBar.setVisibility(View.GONE);
                            tv_star_text.setVisibility(View.GONE);
                        }
                    } else {
                        ratingBar.setVisibility(View.GONE);
                        tv_star_text.setVisibility(View.GONE);
                    }


                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    params.putString("Docname", Docname);
                    params.putString("doctor_id", Doc_id);
                    Model.mFirebaseAnalytics.logEvent("Doctor_Profile", params);
                    //------------ Google firebase Analitics--------------------

                    Model.doc_sp = Docspec;
                    Model.treatment_skills = treatment_skills;
                    Model.doc_lang = language;
                    Model.clinics = clinics;

                    if (has_hline.equals("1")) {
                        btn_hotline.setVisibility(View.VISIBLE);
                    } else if (has_hline.equals("0")) {
                        btn_hotline.setVisibility(View.GONE);
                    }

                    tvdocname.setText(Docname);
                    tv_tooltit.setText(Docname);
                    tv_tooldesc.setText(Docedu);

                    try {
                        getSupportActionBar().setTitle(Docname);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tvedu.setText(Docedu);
                    tvspec.setText(Docspec);

                    Picasso.with(getApplicationContext()).load(doc_photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(imageview_poster);

                    //----------------- Kissmetrics ----------------------------------
                    Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                    Model.kiss.record("android.patient.Doctor_Profile");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("doctor_id", Doc_id);
                    properties.put("doctor_name", Docname);
                    Model.kiss.set(properties);
                    //----------------- Kissmetrics ----------------------------------

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("doctor_id", Doc_id);
                    articleParams.put("doctor_name", Docname);
                    FlurryAgent.logEvent("android.patient.Doctor_Profile", articleParams);
                    //----------- Flurry -------------------------------------------------
                }

                mTopImage.setVisibility(View.VISIBLE);
                bg_layout.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }


    @Override
    protected void initValues() {
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + tabHeight;
        mNumFragments = 2;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(IMAGE_TRANSLATION_Y, mTopImage.getTranslationY());
        outState.putFloat(HEADER_TRANSLATION_Y, mHeader.getTranslationY());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void setupAdapter() {
        if (mAdapter == null) {
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mNumFragments);
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mNumFragments);
        mNavigBar.setOnPageChangeListener(getViewPagerChangeListener());
        mNavigBar.setViewPager(mViewPager);
    }

    @Override
    protected void scrollHeader(int scrollY) {
        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        mHeader.setTranslationY(translationY);
        mTopImage.setTranslationY(-translationY / 3);
    }

    private static class ViewPagerAdapter extends ParallaxFragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm, int numFragments) {
            super(fm, numFragments);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = ConsultFragment.newInstance(position);
                    break;
                case 1:
                    fragment = ProfileFragment.newInstance(position);
                    break;
                /*case 2:
                    fragment = ReviewFragment.newInstance(position);
                    break;*/

                default:
                    throw new IllegalArgumentException("Wrong page given----" + position);
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                case 0:
                    return "Consult";

                case 1:
                    return "Profile";

/*                case 2:
                    return "Articles";*/

                /* case 3:
                    return "Reviews";  */

                default:
                    throw new IllegalArgumentException("Wrong position for the fragment in vehicle page");
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


    public void show_tips() {

        try {

            final MaterialDialog alert = new MaterialDialog(ParallexMainActivity.this);
            View view = LayoutInflater.from(ParallexMainActivity.this).inflate(R.layout.tipstoratting, null);
            alert.setView(view);
            alert.setTitle("How Rating works?");
            alert.setCanceledOnTouchOutside(false);

            ObservableWebView webview = view.findViewById(R.id.webview);

            webview.getSettings().setJavaScriptEnabled(true);
            webview.setBackgroundColor(Color.TRANSPARENT);
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            webview.loadDataWithBaseURL("", strHtml_text, "text/html", "UTF-8", "");
            webview.setLongClickable(false);

            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
