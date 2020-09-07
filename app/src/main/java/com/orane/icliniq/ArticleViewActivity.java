package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
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
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.square1.richtextlib.ui.RichContentView;
import me.drakeet.materialdialog.MaterialDialog;

public class ArticleViewActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    ObservableScrollView mScrollable;
    RelativeLayout mToolbar;
    ImageView img_close;
    public File imageFile;
    RichContentView mContentView;
    ProgressBar progressBar;
    Button btnask;
    String speciality_text;
    TextView tvdocname, tv_doc_id, docname, tv_docedu, tv_docsp;
    TextView tv_desc;
    LinearLayout feedback_layout, recome_parentlay, reccom_layout;
    LinearLayout bottom_layout;
    EditText search_text;
    ImageView img_banner, img_search_hit;
    public String article_response_str, id_val, doc_photo_url, doc_id, doc_name;
    ObservableScrollView scrollview;
    public JSONObject jsonobj_article, json_articlefeedback;
    public String atr_title, doc_edu, doc_sp, url, ctype, art_url, art_date, art_title, art_cat, art_abstract, art_description;
    HashMap<String, String> reccom_list = new HashMap<String, String>();

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    SharedPreferences sharedpreferences;
    public String img_url, pass, Log_Status;
    CircleImageView doc_photo;
    TextView tv_share, tv_title, tv_abstract, tv_docname, tv_cat, tv_date;
    public static final String token = "token_key";
    JSONArray reccom_qa;
    Button btn_yes, btn_no;
    LinearLayout doctor_layout;
    ObservableWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.tipsview);

        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        img_banner = findViewById(R.id.img_banner);
        img_close = findViewById(R.id.img_close);
        mToolbar = findViewById(R.id.top_layout);
        bottom_layout = findViewById(R.id.bottom_layout);
        feedback_layout = findViewById(R.id.feedback_layout);
        mScrollable = findViewById(R.id.scrollable);

        tv_doc_id = findViewById(R.id.tv_doc_id);
        tv_date = findViewById(R.id.tv_date);
        tv_docedu = findViewById(R.id.tv_docedu);
        tv_docsp = findViewById(R.id.tv_docsp);
        tv_docname = findViewById(R.id.tv_docname);
        tv_title = findViewById(R.id.tv_title);
        tv_share = findViewById(R.id.tv_share);
        doc_photo = findViewById(R.id.doc_photo);
        btn_yes = findViewById(R.id.btn_yes);
        btn_no = findViewById(R.id.btn_no);
        recome_parentlay = findViewById(R.id.recome_parentlay);
        progressBar = findViewById(R.id.progressBar);
        tv_title = findViewById(R.id.tv_title);
        //docname = (TextView) findViewById(R.id.docname);
        tv_abstract = findViewById(R.id.tv_abstract);
        tv_desc = findViewById(R.id.tv_desc);
        tv_cat = findViewById(R.id.tv_cat);
        tvdocname = findViewById(R.id.tvdocname);
        btnask = findViewById(R.id.btnask);
        search_text = findViewById(R.id.search_text);
        img_search_hit = findViewById(R.id.img_search_hit);
        scrollview = findViewById(R.id.scrollview);
        reccom_layout = findViewById(R.id.reccom_layout);
        //bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        doctor_layout = findViewById(R.id.doctor_layout);
        webview = findViewById(R.id.webview);

        mScrollable.setScrollViewCallbacks(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");

        //tv_desc.setTransformationMethod(new LinkTransformationMethod());
        tv_desc.setMovementMethod(LinkMovementMethod.getInstance());

        bottom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplicationContext(), AskQuery1.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        doctor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    TextView tv_doc_id = view.findViewById(R.id.tv_doc_id);
                    String Doc_id = tv_doc_id.getText().toString();

                    System.out.println("Doc_id------------------ " + Doc_id);

                    Intent intent = new Intent(ArticleViewActivity.this, ParallexMainActivity.class);
                    intent.putExtra("tv_doc_id", Doc_id);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_feedback("5");
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_feedback("1");
            }
        });

        try {
            //------------- getting Values ------------------------------------

            Intent intent = getIntent();
            id_val = intent.getStringExtra("id");
            ctype = intent.getStringExtra("KEY_ctype");
            url = intent.getStringExtra("KEY_url");
            img_url = intent.getStringExtra("img_url");
            art_title = intent.getStringExtra("title");

            System.out.println("get Intent ctype----------" + ctype);
            System.out.println("get Intent url----------" + url);
            System.out.println("get Intent img_url----------" + img_url);
            System.out.println("get Intent art_title----------" + art_title);

            if (img_url != null && !img_url.isEmpty() && !img_url.equals("null") && !img_url.equals("")) {


                Picasso.with(getApplicationContext()).load(img_url).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(img_banner);
            } else {
                img_banner.setVisibility(View.GONE);
            }

            if (art_title != null && !art_title.isEmpty() && !art_title.equals("null") && !art_title.equals("")) {
                tv_title.setText(Html.fromHtml(art_title));
            }
            //------------- getting Values ------------------------------------

            //------------- Load Content------------------------------------
            String full_url = Model.BASE_URL + "app/loadContent?content_type=2&url=" + url;
            System.out.println("full_url------------" + full_url);
            new JSONAsyncTask().execute(full_url);
            //------------- Load Content------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("art_title-------------" + art_title);
                System.out.println("art_url-------------" + art_url);

                TakeScreenshot_Share();
            }
        });
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

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {
        System.out.println("scroll Down--------");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        //System.out.println("scrollState--------" + scrollState);

        if (scrollState == ScrollState.UP) {
            System.out.println("scrollDir-----UP---" + scrollState);
            //bottom_layout.animate().translationY(bottom_layout.getHeight());

            if (toolbarIsShown()) {
                hideToolbar();
                hideBottomBar();
            }

        } else if (scrollState == ScrollState.DOWN) {
            System.out.println("scrollDir-----Down---" + scrollState);
            //bottom_layout.animate().translationY(0);

            if (toolbarIsHidden()) {
                showToolbar();
                showBottomBar();
            }
        }
    }


    //------------ Toolbar Hide ----------------------------------------------------------------
    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -mToolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mToolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
                ViewHelper.setTranslationY(mScrollable, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mScrollable.getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                mScrollable.requestLayout();
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
                mScrollable.requestLayout();
            }
        });
        animator.start();
    }
    //------------ Toolbar Hide ----------------------------------------------------------------


    private class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mScrollable.setVisibility(View.GONE);

            pd = new ProgressDialog(ArticleViewActivity.this);
            //pd = new ProgressDialog(TipsViewActivity.this);
            pd.setMessage("Loading. Please wait...");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_article = jParser.getJSONFromUrl(urls[0]);
                System.out.println("urls[0]------------------------ " + urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                //------------------------ Response Values------------------------------------------------
                art_title = jsonobj_article.getString("title");
                art_cat = jsonobj_article.getString("category");
                art_abstract = jsonobj_article.getString("abstract");
                art_description = jsonobj_article.getString("description");
                art_date = jsonobj_article.getString("date");
                art_url = jsonobj_article.getString("url");
                speciality_text = jsonobj_article.getString("speciality");


                if (jsonobj_article.has("img_url")) {

                    img_url = jsonobj_article.getString("img_url");

                    if (img_url != null && !img_url.isEmpty() && !img_url.equals("null") && !img_url.equals("")) {
                        Picasso.with(getApplicationContext()).load(img_url).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(img_banner);
                    } else {
                        img_banner.setVisibility(View.GONE);
                    }
                }
                if (jsonobj_article.has("image_url")) {

                    img_url = jsonobj_article.getString("image_url");

                    if (img_url != null && !img_url.isEmpty() && !img_url.equals("null") && !img_url.equals("")) {
                        Picasso.with(getApplicationContext()).load(img_url).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(img_banner);
                    } else {
                        img_banner.setVisibility(View.GONE);
                    }
                }


                //------------------------ Doctor  ------------
                JSONObject doc_json = jsonobj_article.getJSONObject("doctor");
                doc_id = doc_json.getString("id");
                doc_name = doc_json.getString("name");
                doc_photo_url = doc_json.getString("photo");
                doc_edu = doc_json.getString("edu");
                doc_sp = doc_json.getString("sp");
                //------------------------ Doctor  ------------

                tv_doc_id.setText(doc_id);

                reccom_qa = jsonobj_article.getJSONArray("recommended_qa");

                String art_description2 = art_description.replace("../../../", "https://www.icliniq.com/");


                //------------------------ Response Values------------------------------------------------
                System.out.println("art_title-------" + art_title);
                System.out.println("art_cat-------" + art_cat);
                System.out.println("art_abstract-------" + art_abstract);
                System.out.println("art_description-------" + art_description2);
                System.out.println("art_date-------" + art_date);
                System.out.println("art_url-------" + art_url);
                System.out.println("doc_id-------" + doc_id);
                System.out.println("doc_name-------" + doc_name);
                System.out.println("doc_photo_url-------" + doc_photo_url);

                Typeface noto_reg = Typeface.createFromAsset(getAssets(), Model.font_name);

                tv_title.setTypeface(noto_reg);
                tv_abstract.setTypeface(noto_reg);
                tv_desc.setTypeface(noto_reg);
                tv_cat.setTypeface(noto_reg);

                tv_docname.setTypeface(noto_reg);
                tv_date.setTypeface(noto_reg);

                if (img_url != null && !img_url.isEmpty() && !img_url.equals("null") && !img_url.equals("")) {
                    img_banner.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext()).load(img_url).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(img_banner);
                } else {
                    img_banner.setVisibility(View.GONE);
                }

                tv_title.setText(Html.fromHtml(art_title));
                tv_abstract.setText(Html.fromHtml(art_abstract));
                //tv_desc.setText(Html.fromHtml(art_description2));

                webview.getSettings().setJavaScriptEnabled(true);
                webview.loadDataWithBaseURL("", art_description2, "text/html", "UTF-8", "");
                webview.setLongClickable(false);

                tv_docname.setText(doc_name);
                tv_docedu.setText(doc_edu);
                tv_docsp.setText(doc_sp);
                tv_cat.setText(speciality_text);
                tv_date.setText(art_date);

                Picasso.with(getApplicationContext()).load(doc_photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(doc_photo);


/*
                Spannable s = (Spannable) Html.fromHtml(art_description);
                for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
                    s.setSpan(new UnderlineSpan() {
                        public void updateDrawState(TextPaint tp) {
                            tp.setUnderlineText(false);
                        }
                    }, s.getSpanStart(u), s.getSpanEnd(u), 0);
                }
*/

             /*   //------------------------------------------------------
                HtmlTextView textView = (HtmlTextView) findViewById(R.id.html_text);

                //text.setRemoveFromHtmlSpace(false); // default is true
                textView.setClickableTableSpan(new ClickableTableSpanImpl());
                DrawTableLinkSpan drawTableLinkSpan = new DrawTableLinkSpan();
                drawTableLinkSpan.setTableLinkText("[tap for table]");
                textView.setDrawTableLinkSpan(drawTableLinkSpan);

                // Best to use indentation that matches screen density.
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                textView.setListIndentPx(metrics.density * 10);

                textView.setHtml(art_description, new HtmlResImageGetter(textView));

                textView.setTypeface(noto_reg);
                //------------------------------------------------------*/
                //tv_desc.setText(s);

                //------------------------ Setting Values---------
                //Picasso.with(getApplicationContext()).load(doc_photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(doc_photo);
                //String doc_str = "by " + doc_name + " at " + art_date + " on " + art_cat;
                //------------------------ Setting Values---------


                //------------------------------------------------
                if (reccom_qa.length() > 0)
                    recome_parentlay.setVisibility(View.VISIBLE);
                else recome_parentlay.setVisibility(View.GONE);
                //------------------------------------------------

                for (int i = 0; i < reccom_qa.length(); i++) {
                    JSONObject jsonobj3 = reccom_qa.getJSONObject(i);

                    String recc_title = jsonobj3.getString("title");
                    String recc_title_hash = jsonobj3.getString("title_hash");

                    View recc_vi = getLayoutInflater().inflate(R.layout.recomm_quest, null);
                    TextView tv_quest = recc_vi.findViewById(R.id.tv_quest);
                    tv_quest.setText(Html.fromHtml(recc_title));

                    Typeface arimo_reg1 = Typeface.createFromAsset(getAssets(), Model.font_name);
                    tv_quest.setTypeface(arimo_reg1);

                    reccom_list.put(recc_title, recc_title_hash);
                    recome_parentlay.addView(recc_vi);
                }

                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                        mScrollable.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View v) {

        try {

            TextView tv_quest = v.findViewById(R.id.tv_quest);
            String str = tv_quest.getText().toString();
            String qtext_title_hash = reccom_list.get(str);
            System.out.println("str-----------" + str);
            System.out.println("qtext_title_hash-----------" + qtext_title_hash);

            Intent intent = new Intent(ArticleViewActivity.this, QADetailNew.class);
            intent.putExtra("KEY_ctype", "1");
            intent.putExtra("KEY_url", qtext_title_hash);
            startActivity(intent);
            finish();


        } catch (Exception e) {
            e.printStackTrace();
        }

        //scrollview.fullScroll(View.FOCUS_DOWN);
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

            System.out.println("Share immediatly--------------------");

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(imageFile);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Read the article on iCliniq \n\n" + art_title + "\n\n" + art_url);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "send"));

        } catch (Throwable e) {
            //e.printStackTrace();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Read the article on iCliniq" + art_title + "\n\n" + art_url);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
    }

    public void ask_feedback(final String star_val) {

        try {
            final MaterialDialog alert = new MaterialDialog(ArticleViewActivity.this);
            View view = LayoutInflater.from(ArticleViewActivity.this).inflate(R.layout.article_feedback, null);
            alert.setView(view);
            alert.setTitle("Feedback");

            final EditText edt_feedback = view.findViewById(R.id.edt_feedback);

            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String feedback_text = edt_feedback.getText().toString();

                    if (!feedback_text.equals("")) {

                        try {

                            json_articlefeedback = new JSONObject();
                            json_articlefeedback.put("user_id", (Model.id));
                            json_articlefeedback.put("article_id", id_val);
                            json_articlefeedback.put("article_type", "1");
                            json_articlefeedback.put("star", star_val);
                            json_articlefeedback.put("comment", feedback_text);

                            System.out.println("Json_feedback---" + json_articlefeedback.toString());

                            new JSON_Feedback().execute(json_articlefeedback);

                            //say_success();


                            alert.dismiss();


                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }

                    } else {
                        edt_feedback.setError("Please enter your feedback");
                    }
                }
            });

            alert.setNegativeButton("CANCEL", new View.OnClickListener() {
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


    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ArticleViewActivity.this);
            dialog.setMessage("Submitting..., please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                article_response_str = jParser.JSON_STR_POST(urls[0], "article_Feedback_post");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + article_response_str);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {
                JSONObject jobj = new JSONObject(article_response_str);
                System.out.println("jobj-----------" + jobj);

                if (jobj.has("status")) {
                    if ((jobj.getString("status")).equals("1")) {
                        Toast.makeText(getApplicationContext(), "Feedback has been sent", Toast.LENGTH_SHORT).show();
                        feedback_layout.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*
             *
             * */

            dialog.cancel();

        }
    }

    private void showDialog(String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
