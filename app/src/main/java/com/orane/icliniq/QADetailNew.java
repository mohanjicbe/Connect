package com.orane.icliniq;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.network.JSONParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class QADetailNew extends BaseActivity implements ObservableScrollViewCallbacks, View.OnClickListener, TextLinkClickListener {


    private LinkEnabledTextView check;
    public File imageFile;
    ProgressBar progressBar;
    LinearLayout bottom_layout, recc_layout, myLayout, reccom_myLayout;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    SharedPreferences sharedpreferences;
    public String uname, pass, Log_Status;
    View vi;
    ImageView img_title;
    TextView qtitle, tv_tooltit;
    Button btn_askquery;
    public String ctype, url, image_url, art_url, doc_edu,doc_sp,qtitle_textval, qaurl, doc_photo_url, doc_id, doc_name, qtitle_text;
    public String recc_title, recc_title_hash, more_comp, prev_hist, cur_medi, past_medi, lab_tests, prob_caus, inv_done, diff_diag, prob_diag, treat_plan, prev_measu, reg_folup;
    public JSONObject jsonobj;
    public TextView tv_quest;
    ObservableScrollView scrollview;
    Button btnask;

    HashMap<String, String> reccom_list = new HashMap<String, String>();

    private RadioGroup radioGroup;
    private CheckBox headerCheckBox;
    private CheckBox footerCheckBox;
    private CheckBox expandedCheckBox;
    Typeface noto_reg, noto_bold;
    Toolbar toolbar;
    public static final String token = "token_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_detail);

        FlurryAgent.onPageView();
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        noto_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        noto_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        check = new LinkEnabledTextView(QADetailNew.this, null);
        check.setOnTextLinkClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");

        //------------ Object Creations -------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Question and Answers");

            tv_tooltit = (TextView) toolbar.findViewById(R.id.tv_tooltit);
            tv_tooltit.setTypeface(noto_reg);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //--------------------------Toolbar---------------------------------------------
        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        headerCheckBox = (CheckBox) findViewById(R.id.header_check_box);
        footerCheckBox = (CheckBox) findViewById(R.id.footer_check_box);
        expandedCheckBox = (CheckBox) findViewById(R.id.expanded_check_box);

        //------------ Object Creations -------------------------------
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        scrollview = (ObservableScrollView) findViewById(R.id.scrollview);
        recc_layout = (LinearLayout) findViewById(R.id.recc_layout);
        myLayout = (LinearLayout) findViewById(R.id.parent_qalayout);
        reccom_myLayout = (LinearLayout) findViewById(R.id.sugg_layout);
        btnask = (Button) findViewById(R.id.btnask);
        qtitle = (TextView) findViewById(R.id.qtitle);


        img_title = (ImageView) findViewById(R.id.img_title);

        //tv_quest.setTypeface(noto_reg);


        Intent intent = getIntent();
        ctype = intent.getStringExtra("KEY_ctype");
        url = intent.getStringExtra("KEY_url");


        try {
            //----------------------------------------
            String full_url = Model.BASE_URL + "app/loadContent?content_type=1&url=" + url;
            System.out.println("full_url------------" + full_url);
            new JSONAsyncTask().execute(full_url);
            //----------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        bottom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(QADetailNew.this, AskQuery1.class);
                    startActivity(intent);
                    //finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        scrollview.setScrollViewCallbacks(this);

    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        System.out.println("Scrolling----------------------" + scrollState);

        if (scrollState == ScrollState.UP) {
            System.out.println("scrollDir-----UP---" + scrollState);
            bottom_layout.animate().translationY(bottom_layout.getHeight());

/*            if (toolbarIsShown()) {
                hideToolbar();
                hideBottomBar();
            }*/

        } else if (scrollState == ScrollState.DOWN) {
            System.out.println("scrollDir-----Down---" + scrollState);
            bottom_layout.animate().translationY(0);

/*            if (toolbarIsHidden()) {
                showToolbar();
                showBottomBar();
            }*/
        }
    }


    //------------ Toolbar Hide ----------------------------------------------------------------
    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(toolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(toolbar) == -toolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-toolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(toolbar) == toTranslationY) {
            return;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(toolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(toolbar, translationY);
                ViewHelper.setTranslationY(scrollview, translationY);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) scrollview.getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                scrollview.requestLayout();
            }
        });
        animator.start();
    }
    //------------ Toolbar Hide ----------------------------------------------------------------


    //------------ Bottom Bar Hide ----------------------------------------------------------------
    private void showBottomBar() {
        moveBottomBar(0);
    }

    private void hideBottomBar() {
        moveBottomBar(bottom_layout.getHeight());
    }

    private void moveBottomBar(float toTranslationY) {
        if (ViewHelper.getTranslationY(bottom_layout) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(bottom_layout), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(bottom_layout, translationY);
/*                ViewHelper.setTranslationY((View) mScrollable, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) mScrollable).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;*/
                scrollview.requestLayout();
            }
        });
        animator.start();
    }
    //------------ Bottom Hide ----------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.qa_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_qashare) {
            TakeScreenshot_Share();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            scrollview.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            bottom_layout.setVisibility(View.GONE);

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj = jParser.getJSONFromUrl(urls[0]);

                System.out.println("urls[0]------------------------ " + urls[0]);
                System.out.println("jsonobj--------------- " + jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            //dialog.cancel();

            reccom_myLayout.removeAllViews();
            myLayout.removeAllViews();

            doc_id = "";
            doc_name = "";
            doc_photo_url = "";

            try {
                qtitle_text = jsonobj.getString("title");
                qtitle_textval = jsonobj.getString("title");
                image_url = jsonobj.getString("image_url");

                System.out.println("image_url--------------------------------------------" + image_url);

                //------------------------ QA ------------------------------------------------
                if (image_url != null && !image_url.isEmpty() && !image_url.equals("null") && !image_url.equals("")) {
                    Picasso.with(getApplicationContext()).load(image_url).placeholder(R.mipmap.banner_palceholder).error(R.mipmap.doctor_icon).into(img_title);
                } else {
                    img_title.setVisibility(View.GONE);
                }
                qaurl = jsonobj.getString("url");
                //------------------------ QA ------------------------------------------------


                JSONArray reccom_qa = jsonobj.getJSONArray("recommended_qa");

                for (int i = 0; i < reccom_qa.length(); i++) {
                    JSONObject jsonobj3 = reccom_qa.getJSONObject(i);

                    recc_title = jsonobj3.getString("title");
                    recc_title_hash = jsonobj3.getString("title_hash");

                    View recc_vi = getLayoutInflater().inflate(R.layout.recomm_quest, null);
                    TextView tv_quest = (TextView) recc_vi.findViewById(R.id.tv_quest);
                    LinearLayout full_layout = (LinearLayout) recc_vi.findViewById(R.id.quest_layout);

                    tv_quest.setTypeface(noto_reg);

                    tv_quest.setText(recc_title);
                    reccom_list.put(recc_title, recc_title_hash);
                    reccom_myLayout.addView(recc_vi);

                  /*  full_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            System.out.println("recc_title-----------" + recc_title);
                            System.out.println("recc_title_hash-----------" + recc_title_hash);

                            String QA_Doc_full_url = Model.BASE_URL + "app/loadContent?content_type=1&url=" + recc_title_hash;
                            System.out.println("QA_Doc_full_url-------------------" + QA_Doc_full_url);
                            new JSONAsyncTask().execute(QA_Doc_full_url);

                            scrollview.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });*/
                }


                if ((reccom_qa.length() > 0)) {
                    reccom_myLayout.setVisibility(View.VISIBLE);
                    recc_layout.setVisibility(View.VISIBLE);
                } else {
                    reccom_myLayout.setVisibility(View.GONE);
                    recc_layout.setVisibility(View.GONE);
                }

                //------------------------ Doctor  ------------------------------------------------
                JSONObject doc_json = jsonobj.getJSONObject("doctor");
                doc_id = doc_json.getString("id");
                doc_name = doc_json.getString("name");
                doc_photo_url = doc_json.getString("photo");
                doc_edu = doc_json.getString("edu");
                doc_sp = doc_json.getString("sp");
                //------------------------ Doctor  ------------------------------------------------


                //------------------------ QA ------------------------------------------------
                JSONArray qa_jarray = jsonobj.getJSONArray("qa");
                for (int i = 0; i < qa_jarray.length(); i++) {
                    JSONObject jsonobj1 = qa_jarray.getJSONObject(i);

                    String q = jsonobj1.getString("q");
                    // String q_ext = jsonobj1.getString("q_ext");

                    more_comp = "";
                    prev_hist = "";
                    cur_medi = "";
                    past_medi = "";
                    lab_tests = "";
                    prob_caus = "";
                    inv_done = "";
                    diff_diag = "";
                    prob_diag = "";
                    treat_plan = "";
                    prev_measu = "";
                    reg_folup = "";
                    //------------------------ Q Extra  ------------------------------------------------

                    String q_ext_ext = jsonobj1.getString("q_ext");
                    System.out.println("q_ext length--------" + q_ext_ext.length());

                    String a_ext_ext = jsonobj1.getString("a_ext");
                    System.out.println("a_ext length--------" + a_ext_ext.length());


                    if ((q_ext_ext.length()) > 2) {

                        JSONObject qext_json = jsonobj1.getJSONObject("q_ext");

                        if (qext_json.has("More details about the presenting complaint")) {
                            more_comp = qext_json.getString("More details about the presenting complaint");
                        }
                        if (qext_json.has("Previous history of the same issue")) {
                            prev_hist = qext_json.getString("Previous history of the same issue");
                        }
                        if (qext_json.has("Current medications")) {
                            cur_medi = qext_json.getString("Current medications");
                        }
                        if (qext_json.has("Past medications of the same issue")) {
                            past_medi = qext_json.getString("Past medications of the same issue");
                        }
                        if (qext_json.has("Lab tests performed")) {
                            lab_tests = qext_json.getString("Lab tests performed");
                        }
                        //------------------------ Q Extra  ------------------------------------------------
                    }

                    if ((a_ext_ext.length()) > 2) {

                        JSONObject aext_json = jsonobj1.getJSONObject("a_ext");

                        //------------------------ A Extra  ------------------------------------------------
                        if (aext_json.has("The Probable causes")) {
                            prob_caus = aext_json.getString("The Probable causes");
                        }
                        if (aext_json.has("Investigations to be done")) {
                            inv_done = aext_json.getString("Investigations to be done");
                        }
                        if (aext_json.has("Differential diagnosis")) {
                            diff_diag = aext_json.getString("Differential diagnosis");
                        }

                        if (aext_json.has("Probable diagnosis")) {
                            prob_diag = aext_json.getString("Probable diagnosis");
                        }
                        if (aext_json.has("Treatment plan")) {
                            treat_plan = aext_json.getString("Treatment plan");
                        }
                        if (aext_json.has("Preventive measures")) {
                            prev_measu = aext_json.getString("Preventive measures");
                        }
                        if (aext_json.has("Regarding follow up")) {
                            reg_folup = aext_json.getString("Regarding follow up");
                        }
                    }
                    //------------------------ A Extra  ------------------------------------------------

                    String a = jsonobj1.getString("a");
                    String a_ext = jsonobj1.getString("a_ext");

                    System.out.println("q---------" + q);
                    System.out.println("a---------" + a);
                    System.out.println("a_ext---------" + a_ext);

                    //myLayout = (LinearLayout) findViewById(R.id.parent_qalayout);
                    vi = getLayoutInflater().inflate(R.layout.search_qa_thread_view, null);
                    btn_askquery = (Button) vi.findViewById(R.id.btn_askquery);
                    TextView tv_patq = (TextView) vi.findViewById(R.id.pq);
                    TextView tv_docans = (TextView) vi.findViewById(R.id.docans);
                    TextView tvdocname = (TextView) vi.findViewById(R.id.tvdocname);
                    TextView tv_docedu = (TextView) vi.findViewById(R.id.tv_docedu);
                    TextView tv_docsp = (TextView) vi.findViewById(R.id.tv_docsp);
                    TextView tvdocid = (TextView) vi.findViewById(R.id.tvdocid);
                    CircleImageView doc_photo = (CircleImageView) vi.findViewById(R.id.doc_photo);

                    TextView tvt_morecomp = (TextView) vi.findViewById(R.id.tvt_morecomp);
                    TextView tv_morecomp = (TextView) vi.findViewById(R.id.tv_morecomp);
                    TextView tvt_prevhist = (TextView) vi.findViewById(R.id.tvt_prevhist);
                    TextView tv_prevhist = (TextView) vi.findViewById(R.id.tv_prevhist);
                    TextView tvt_curmedi = (TextView) vi.findViewById(R.id.tvt_curmedi);
                    TextView tv_curmedi = (TextView) vi.findViewById(R.id.tv_curmedi);
                    TextView tvt_pastmedi = (TextView) vi.findViewById(R.id.tvt_pastmedi);
                    TextView tv_pastmedi = (TextView) vi.findViewById(R.id.tv_pastmedi);
                    TextView tvt_labtest = (TextView) vi.findViewById(R.id.tvt_labtest);
                    TextView tv_labtest = (TextView) vi.findViewById(R.id.tv_labtest);

                    TextView tvt_probcause = (TextView) vi.findViewById(R.id.tvt_probcause);
                    TextView tv_probcause = (TextView) vi.findViewById(R.id.tv_probcause);
                    TextView tvt_invdone = (TextView) vi.findViewById(R.id.tvt_invdone);
                    TextView tv_invdone = (TextView) vi.findViewById(R.id.tv_invdone);
                    TextView tvt_diffdiag = (TextView) vi.findViewById(R.id.tvt_diffdiag);
                    TextView tv_diffdiag = (TextView) vi.findViewById(R.id.tv_diffdiag);
                    TextView tvt_probdiag = (TextView) vi.findViewById(R.id.tvt_probdiag);
                    TextView tv_probdiag = (TextView) vi.findViewById(R.id.tv_probdiag);
                    TextView tvt_tratplan = (TextView) vi.findViewById(R.id.tvt_tratplan);
                    TextView tv_tratplan = (TextView) vi.findViewById(R.id.tv_tratplan);
                    TextView tvt_prevmeasure = (TextView) vi.findViewById(R.id.tvt_prevmeasure);
                    TextView tv_prevmeasure = (TextView) vi.findViewById(R.id.tv_prevmeasure);
                    TextView tvt_follup = (TextView) vi.findViewById(R.id.tvt_follup);
                    TextView tv_follup = (TextView) vi.findViewById(R.id.tv_follup);

                    tv_patq.setText(noTrailingwhiteLines(Html.fromHtml(q)));
                    tv_patq.setTypeface(noto_reg);

                    //--------------- Question Extra --------------------------------------------------------------------------------------------------------------
                    //-------------------- More comp -----------------------------------------------
                    if (more_comp != null && !more_comp.isEmpty() && !more_comp.equals("")) {
                        tv_morecomp.setText(Html.fromHtml(more_comp));
                        tv_morecomp.setVisibility(View.VISIBLE);
                        tvt_morecomp.setVisibility(View.VISIBLE);
                    } else {
                        tvt_morecomp.setVisibility(View.GONE);
                        tv_morecomp.setVisibility(View.GONE);
                    }
                    //-------------------- More comp -----------------------------------------------
                    //--------------------Prev Hist -----------------------------------------------
                    if (prev_hist != null && !prev_hist.isEmpty() && !prev_hist.equals("")) {
                        tv_prevhist.setVisibility(View.VISIBLE);
                        tvt_prevhist.setVisibility(View.VISIBLE);
                        tv_prevhist.setText(Html.fromHtml(prev_hist));
                    } else {
                        tv_prevhist.setVisibility(View.GONE);
                        tvt_prevhist.setVisibility(View.GONE);
                    }
                    //---------------------Prev Hist ------------------------------------------------

                    //--------------------Cur Medi--------------------------------------------
                    if (cur_medi != null && !cur_medi.isEmpty() && !cur_medi.equals("")) {
                        tvt_curmedi.setVisibility(View.VISIBLE);
                        tv_curmedi.setVisibility(View.VISIBLE);
                        tv_curmedi.setText(Html.fromHtml(cur_medi));
                    } else {
                        tv_curmedi.setVisibility(View.GONE);
                        tvt_curmedi.setVisibility(View.GONE);
                    }
                    //----------------------Cur Medi---------------------------------------------------

                    //--------------------past Medi--------------------------------------------
                    if (past_medi != null && !past_medi.isEmpty() && !past_medi.equals("")) {
                        tvt_pastmedi.setVisibility(View.VISIBLE);
                        tv_pastmedi.setVisibility(View.VISIBLE);
                        tv_pastmedi.setText(Html.fromHtml(past_medi));
                    } else {
                        tv_pastmedi.setVisibility(View.GONE);
                        tvt_pastmedi.setVisibility(View.GONE);
                    }
                    //---------------------past Medi---------------------------------------------------

                    //--------------------lab test-------------------------------------------
                    if (lab_tests != null && !lab_tests.isEmpty() && !lab_tests.equals("")) {
                        tvt_labtest.setVisibility(View.VISIBLE);
                        tv_labtest.setVisibility(View.VISIBLE);
                        tv_labtest.setText(Html.fromHtml(lab_tests));
                    } else {
                        tv_labtest.setVisibility(View.GONE);
                        tvt_labtest.setVisibility(View.GONE);
                    }
                    //---------------------lab test---------------------------------------------------
                    //--------------- Question Extra -------------------------------------------------

                    //tv_docans.setText(noTrailingwhiteLines(Html.fromHtml(a, null, new MyTagHandler())));

                    check.setOnTextLinkClickListener(QADetailNew.this);
                    check = new LinkEnabledTextView(QADetailNew.this, null);
                    check.gatherLinksForText(a);
                    //check.setTextColor(Color.WHITE);
                    check.setLinkTextColor(Color.GREEN);

                    MovementMethod m = check.getMovementMethod();
                    if ((m == null) || !(m instanceof LinkMovementMethod)) {
                        if (check.getLinksClickable()) {
                            check.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                    }
                    //tv_docans.setText(check.getText());
                    tv_docans.setTypeface(noto_reg);
                    tv_docans.setText(noTrailingwhiteLines(Html.fromHtml(check.getText().toString(), null, new MyTagHandler())));

                    //--------------- Answer Extra --------------------------------------------------------------------------------------------------------------
                    //-------------------- Prob Cause -----------------------------------------------
                    if (prob_caus != null && !prob_caus.isEmpty() && !prob_caus.equals("")) {
                        tv_probcause.setText(Html.fromHtml(prob_caus));
                        tv_probcause.setVisibility(View.VISIBLE);
                        tvt_probcause.setVisibility(View.VISIBLE);
                    } else {
                        tv_probcause.setVisibility(View.GONE);
                        tvt_probcause.setVisibility(View.GONE);
                    }
                    //-------------------- Prob cause-----------------------------------------------

                    //-------------------- Inv Done -----------------------------------------------
                    if (inv_done != null && !inv_done.isEmpty() && !inv_done.equals("")) {
                        tv_invdone.setText(Html.fromHtml(inv_done));
                        tvt_invdone.setVisibility(View.VISIBLE);
                        tvt_invdone.setVisibility(View.VISIBLE);
                    } else {
                        tv_invdone.setVisibility(View.GONE);
                        tvt_invdone.setVisibility(View.GONE);
                    }
                    //-------------------- Inv Done---------------------------------------------

                    //-------------------- Diff Diag-----------------------------------------------
                    if (diff_diag != null && !diff_diag.isEmpty() && !diff_diag.equals("")) {
                        tv_diffdiag.setText(Html.fromHtml(diff_diag));
                        tv_diffdiag.setVisibility(View.VISIBLE);
                        tvt_diffdiag.setVisibility(View.VISIBLE);
                    } else {
                        tv_diffdiag.setVisibility(View.GONE);
                        tvt_diffdiag.setVisibility(View.GONE);
                    }
                    //--------------------Diff Diag---------------------------------------------
                    //-------------------- Prob Diag-----------------------------------------------
                    if (prob_diag != null && !prob_diag.isEmpty() && !prob_diag.equals("")) {
                        tv_probdiag.setText(Html.fromHtml(prob_diag));
                        tv_probdiag.setVisibility(View.VISIBLE);
                        tvt_probdiag.setVisibility(View.VISIBLE);
                    } else {
                        tv_probdiag.setVisibility(View.GONE);
                        tvt_probdiag.setVisibility(View.GONE);
                    }
                    //--------------------Prob Diag---------------------------------------------
                    //--------------------Treat Plan----------------------------------------------
                    if (treat_plan != null && !treat_plan.isEmpty() && !treat_plan.equals("")) {
                        tv_tratplan.setText(Html.fromHtml(treat_plan));
                        tv_tratplan.setVisibility(View.VISIBLE);
                        tvt_tratplan.setVisibility(View.VISIBLE);
                    } else {
                        tv_tratplan.setVisibility(View.GONE);
                        tvt_tratplan.setVisibility(View.GONE);
                    }
                    //--------------------Treat Plan---------------------------------------------
                    //--------------------pREV mEASURE--------------------------------------------
                    if (prev_measu != null && !prev_measu.isEmpty() && !prev_measu.equals("")) {
                        tv_prevmeasure.setText(Html.fromHtml(prev_measu));
                        tv_prevmeasure.setVisibility(View.VISIBLE);
                        tvt_prevmeasure.setVisibility(View.VISIBLE);
                    } else {
                        tv_prevmeasure.setVisibility(View.GONE);
                        tvt_prevmeasure.setVisibility(View.GONE);
                    }
                    //--------------------pREV mEASURE--------------------------------------------
                    //-------------------Follow up-------------------------------------------
                    if (reg_folup != null && !reg_folup.isEmpty() && !reg_folup.equals("")) {
                        tv_follup.setText(Html.fromHtml(reg_folup));
                        tv_follup.setVisibility(View.VISIBLE);
                        tvt_follup.setVisibility(View.VISIBLE);
                    } else {
                        tv_follup.setVisibility(View.GONE);
                        tvt_follup.setVisibility(View.GONE);
                    }
                    //--------------------Follow up--------------------------------------------
                    //--------------- Answer Extra --------------------------------------------------------------------------------------------------------------

                    tvdocname.setTypeface(noto_bold);
                    tv_docedu.setTypeface(noto_reg);
                    tv_docsp.setTypeface(noto_reg);

                    tvdocname.setText(Html.fromHtml(doc_name));
                    tv_docedu.setText(doc_edu);
                    tv_docsp.setText(doc_sp);
                    tvdocid.setText(Html.fromHtml(doc_id));

                    //----------- Profile Photo -----------------------------------------------
                    String pathToFile = doc_photo_url;
                    DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(doc_photo);
                    downloadTask.execute(pathToFile);
                    //----------- Profile Photo -----------------------------------------------

                    myLayout.addView(vi);

                    //btn_askquery.setOnClickListener(SearchDetailActivity.this);

                    //------------------------ QA ------------------------------------------------
                    btn_askquery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            System.out.println("Getting QA doc_id----------- " + doc_id);

                            //=========================================================
                            Model.kiss.record("android.patient.QA_Doc_Select");
                            HashMap<String, String> properties = new HashMap<String, String>();
                            properties.put("android.patient.docid", doc_id);
                            Model.kiss.set(properties);
                            //----------------------------------------------------------------------------

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.docid", doc_id);
                            FlurryAgent.logEvent("android.patient.QA_Doc_Select", articleParams);
                            //----------- Flurry -------------------------------------------------

                            Intent intent = new Intent(QADetailNew.this, ParallexMainActivity.class);
                            intent.putExtra("tv_doc_id", doc_id);
                            startActivity(intent);
                        }
                    });
                }
                scrollview.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                bottom_layout.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            qtitle.setTypeface(noto_bold);
            qtitle.setText(Html.fromHtml("<b>Q:</b> " + qtitle_text));

            //-------------------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Read_QA");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("android.patient.Title_text", qtitle_text);
            properties.put("android.patient.URL", qaurl);
            properties.put("android.patient.Doctor_id", doc_id);
            properties.put("android.patient.Doctor_Name", doc_name);
            Model.kiss.set(properties);
            //-------------------------------------------
            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.Title_text", qtitle_text);
            articleParams.put("android.patient.URL", qaurl);
            articleParams.put("android.patient.Doctor_id", doc_id);
            articleParams.put("android.patient.Doctor_Name", doc_name);
            FlurryAgent.logEvent("android.patient.Read_QA", articleParams);
            //----------- Flurry -------------------------------------------------


        }
    }

    public void onClick(View v) {
        TextView tv_quest = (TextView) v.findViewById(R.id.tv_quest);

        String str = tv_quest.getText().toString();
        String qtext_title_hash = reccom_list.get(str);

        System.out.println("qtext_title_hash-----------" + str);
        System.out.println("qtext_title_hash-----------" + qtext_title_hash);

        //=========================================================
        Model.kiss.record("android.patient.qa_reccom_word");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("android.patient.qtext_title_hash", qtext_title_hash);
        Model.kiss.set(properties);
        //----------------------------------------------------------------------------
        //----------- Flurry -------------------------------------------------
        Map<String, String> articleParams = new HashMap<String, String>();
        articleParams.put("android.patient.qtext_title_hash", qtext_title_hash);
        FlurryAgent.logEvent("android.patient.qa_reccom_word", articleParams);
        //----------- Flurry -------------------------------------------------


        //---------------------------------------------------------------
        String QA_Doc_full_url = Model.BASE_URL + "app/loadContent?content_type=1&url=" + qtext_title_hash;
        System.out.println("QA_Doc_full_url-------------------" + QA_Doc_full_url);
        new JSONAsyncTask().execute(QA_Doc_full_url);
        //---------------------------------------------------------------

        scrollview.fullScroll(ScrollView.FOCUS_UP);
    }


    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            try {
                bmImage.setImageBitmap(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleP(SpannableStringBuilder text) {
        int len = text.length();

        if (len >= 1 && text.charAt(len - 1) == '\n') {
            if (len >= 2 && text.charAt(len - 2) == '\n') {
                return;
            }

            text.append("\n");
            return;
        }

        if (len != 0) {
            text.append("\n\n");
        }
    }

    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }


    public void onTextLinkClick(View textView, String clickedString) {
        //android.util.Log.v('Hyperlink clicked is :: ' + clickedString, 'Hyperlink clicked is :: ' + clickedString);
        System.out.println("clickedString--------" + clickedString);
    }


    private void TakeScreenshot_Share() {

        try {
            Date now = new Date();
            long milliseconds = now.getTime();
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + milliseconds + ".jpg";
            System.out.println("mPath---" + mPath);

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot_other(imageFile);

            System.out.println("Share immediatly--------------------");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(imageFile);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Read Q/A on iCliniq: " + qtitle_textval + "\n\n" + qaurl);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "send"));

        } catch (Throwable e) {
            //e.printStackTrace();

            System.out.println("Exception from Taking Screenshot---" + e.toString());
            System.out.println("Exception Screenshot---" + e.toString());

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Read Q/A on iCliniq: " + qtitle_textval + "\n\n" + qaurl);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

        }
    }


}
