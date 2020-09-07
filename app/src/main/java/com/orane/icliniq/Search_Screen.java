package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.adapter.SearchListAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.SearchListParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search_Screen extends BaseActivity implements ObservableScrollViewCallbacks {


    public Menu menu;
    TextView tv_url, textview_title, textview_short, textview_docname, textview_ctype;
    View vi;
    public String qa_photo_url, qa_abstract, qa_title, qa_doctor_name, qa_url, speciality, title;
    MenuItem item;
    public StringBuffer json_response = new StringBuffer();
    public String url;
    JSONObject jsonobj1;
    CircleImageView imageview_poster;
    LinearLayout innerLay, layout_offer1, layout_offer2, b0_layout, b1_layout, b2_layout, b3_layout;
    TextView tv_more;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String search_tag = "search_tag_key";

    List<Item> arrayOfList;
    SearchListAdapter objAdapter;

    public String str_response, search_tag_text, json_search_str, ctype, title_text, art_url, doctid, search_type, search_url, uname, pass, Log_Status, search_text, conttype_val;
    ProgressBar progressBar, progressBar_bottom;
    LinearLayout layout1, layout2, layout3, tag_layout, empty_layout, first_layout, bottom_layout, share_layout, titlayout;
    ObservableListView listview;
    //CircleImageView imageview_poster;
    Button btnask, btnsignup, btnlogin;
    ImageView img_search, leftback, delete_ico;
    EditText edt_searchword;
    TextView tv_tag, tv_viewall;
    ScrollView search_scrollview;
    public boolean pagination = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        search_tag_text = sharedpreferences.getString(search_tag, "");
        //=========================================================
        Model.kiss.record("android.patient.Search_Screen");
        //----------------------------------------------------------------------------
        FlurryAgent.onPageView();

        //----------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Search health issue here");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //----------------------------------------------------------

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");

        edt_searchword = (EditText) findViewById(R.id.edt_searchword);
        img_search = (ImageView) findViewById(R.id.img_search);
        leftback = (ImageView) findViewById(R.id.leftback);
        delete_ico = (ImageView) findViewById(R.id.delete_ico);
        btnask = (Button) findViewById(R.id.btnask);
        first_layout = (LinearLayout) findViewById(R.id.first_layout);
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        //share_layout = (LinearLayout) findViewById(R.id.share_layout);
        //imageview_poster = (CircleImageView) findViewById(R.id.imageview_poster);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        btnsignup = (Button) findViewById(R.id.btnsignup);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        listview = (ObservableListView) findViewById(R.id.listview);
        titlayout = (LinearLayout) findViewById(R.id.titlayout);
        tag_layout = (LinearLayout) findViewById(R.id.tag_layout);
        search_scrollview = (ScrollView) findViewById(R.id.search_scrollview);
        innerLay = (LinearLayout) findViewById(R.id.innerLay);
        tv_viewall = (TextView) findViewById(R.id.tv_viewall);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);

        b0_layout = (LinearLayout) findViewById(R.id.b0_layout);
        b1_layout = (LinearLayout) findViewById(R.id.b1_layout);
        b2_layout = (LinearLayout) findViewById(R.id.b2_layout);
        b3_layout = (LinearLayout) findViewById(R.id.b3_layout);

        tv_more = (TextView) findViewById(R.id.tv_more);

        //-------------- Font ---------------------------------------------------------
        Typeface robo_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        edt_searchword.setTypeface(robo_reg);

        ((TextView) findViewById(R.id.findmore)).setTypeface(robo_reg);
        ((TextView) findViewById(R.id.cat_tit1)).setTypeface(robo_bold);
        ((TextView) findViewById(R.id.cat_desc1)).setTypeface(robo_reg);
        ((TextView) findViewById(R.id.cat_tit2)).setTypeface(robo_bold);
        ((TextView) findViewById(R.id.cat_desc2)).setTypeface(robo_reg);
        ((TextView) findViewById(R.id.cat_tit3)).setTypeface(robo_bold);
        ((TextView) findViewById(R.id.cat_desc3)).setTypeface(robo_reg);
        ((TextView) findViewById(R.id.tv_recent)).setTypeface(robo_bold);
        ((TextView) findViewById(R.id.tv_topsearch)).setTypeface(robo_reg);
        ((EditText) findViewById(R.id.edt_searchword)).setTypeface(robo_reg);
        //-------------- Font ---------------------------------------------------------


        try {
            Intent intent = getIntent();
            search_type = intent.getStringExtra("search_type");
            System.out.println("search_type------------->" + search_type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        full_process();


        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Screen.this, SpecialityListActivity.class);
                startActivity(intent);
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Screen.this, DoctorsListActivity.class);
                startActivity(intent);
            }
        });


        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b0_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView tv_gridtext0 = (TextView) findViewById(R.id.tv_gridtext0);
                String spec_name = tv_gridtext0.getText().toString();
                System.out.println("spec_name----------" + spec_name);

                search_by_word(spec_name);

            }
        });
        b1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView tv_gridtext1 = (TextView) findViewById(R.id.tv_gridtext1);
                String spec_name = tv_gridtext1.getText().toString();
                System.out.println("spec_name----------" + spec_name);

                search_by_word(spec_name);

            }
        });

        b2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView tv_gridtext2 = (TextView) findViewById(R.id.tv_gridtext2);
                String spec_name = tv_gridtext2.getText().toString();
                System.out.println("spec_name----------" + spec_name);

                search_by_word(spec_name);

            }
        });

        b3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView tv_gridtext3 = (TextView) findViewById(R.id.tv_gridtext3);
                String spec_name = tv_gridtext3.getText().toString();
                System.out.println("spec_name----------" + spec_name);

                search_by_word(spec_name);

            }
        });

/*

        tagcontainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

                System.out.println("text------------------" + text);
                System.out.println("position------------------" + position);

                search_text = text;
                edt_searchword.setText(search_text);
                //=========================================================
                Model.kiss.record("android.patient.tag_select");
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("android.patient.tag_word", search_text);
                Model.kiss.set(properties);
                //----------------------------------------------------------------------------
                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.patient.tag_word", search_text);
                FlurryAgent.logEvent("android.patient.tag_select", articleParams);
                //----------- Flurry -------------------------------------------------

                //------------ Google firebase Analitics-----------------------------------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User_id", Model.id);
                params.putString("tag_word", search_text);
                Model.mFirebaseAnalytics.logEvent("tag_select", params);
                //------------ Google firebase Analitics---------------------------------------------


                if (isInternetOn()) {
                    //-------------------------------------------------------
                    String search_url = Model.BASE_URL + "app/search?q=" + search_text + "&page=0";
                    System.out.println("search_url------------------" + search_url);
                    new MyTask_server().execute(search_url);
                    //-------------------------------------------------------

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } else {
                    //Toast.makeText(Search_Screen.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
                    Snackbar.make(titlayout, "Internet is not connected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onTagLongClick(final int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
*/


        if (search_tag_text != null && !search_tag_text.isEmpty() && !search_tag_text.equals("null") && !search_tag_text.equals("")) {
            try {
                str_response = search_tag_text;
                System.out.println("Local response--------------" + str_response);

                //---------------------------------------------------------
                JSONArray temp = new JSONArray(str_response);
                String[] mArray = temp.join(",").split(",");

                for (int i = 0; i < mArray.length; i++) {
                    System.out.println("Value of i---------" + mArray[i]);

                    vi = getLayoutInflater().inflate(R.layout.search_tag, null);
                    tv_tag = (TextView) vi.findViewById(R.id.tv_tag);

                    String replaced = mArray[i].replace("\"", "");

                    tv_tag.setText(replaced);

                    tag_layout.addView(vi);
                }
                //-------------------------------------------------------------------------

                //--------------------------------------------------------
                String tags_url = Model.BASE_URL + "app/tags";
                System.out.println("get tags_url-----------" + tags_url);
                new Get_JSON_getTags().execute(tags_url);
                //--------------------------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //--------------------------------------------------------
            String tags_url = Model.BASE_URL + "app/tags";
            System.out.println("get tags_url-----------" + tags_url);
            new JSON_getTags().execute(tags_url);
            //--------------------------------------------------------------------
        }

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //search_text = edt_searchword.getText().toString();
                try {
                    search_text = URLEncoder.encode((edt_searchword.getText().toString()), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("search_text------------------" + search_text);

                if (isInternetOn()) {
                    //search(search_text);

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }
        });

        leftback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listview.getVisibility() == View.VISIBLE) {
                    first_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    listview.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    finish();
                }
            }
        });

        edt_searchword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //search_text = edt_searchword.getText().toString();

                    try {
                        search_text = URLEncoder.encode((edt_searchword.getText().toString()), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("search_text------------------" + search_text);

                    //=========================================================
                    Model.kiss.record("android.patient.Search");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.patient.Search_text", search_text);
                    Model.kiss.set(properties);
                    //----------------------------------------------------------------------------
                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.Search_text", search_text);
                    FlurryAgent.logEvent("android.patient.Search", articleParams);
                    //----------- Flurry -------------------------------------------------


                    if (isInternetOn()) {

                        pagination = true;

                        //-----------------------------
                        String search_url = Model.BASE_URL + "app/search?q=" + search_text + "&page=0";
                        System.out.println("search_url------" + search_url);
                        new MyTask_server().execute(search_url);
                        //-----------------------------

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    } else {
                        //Toast.makeText(Search_Screen.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
                        Snackbar.make(v, "Internet is not connected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                    return true;
                }
                return false;
            }
        });

        edt_searchword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("S-----------------" + s);
                System.out.println("S--Length---------------" + s.length());

                if (s.length() > 0) {
                    delete_ico.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);
                } else {
                    delete_ico.setVisibility(View.GONE);
                    img_search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        delete_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_searchword.setText("");
                edt_searchword.requestFocus();
            }
        });


        btnask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), AskQuery1.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("Log_Status----One-------------" + Log_Status);

                    TextView tv_url = (TextView) view.findViewById(R.id.tv_url);
                    TextView textview_ctype = (TextView) view.findViewById(R.id.textview_ctype);
                    TextView tv_doc_id = (TextView) view.findViewById(R.id.tv_docid);
                    TextView textview_title = (TextView) view.findViewById(R.id.textview_title);

                    share_layout = (LinearLayout) view.findViewById(R.id.share_layout);

                    ctype = textview_ctype.getText().toString();
                    url = tv_url.getText().toString();
                    System.out.println("QA url-------------" + Log_Status);
                    System.out.println("ctype-------------" + ctype);

                    title_text = textview_title.getText().toString();


                    switch (ctype) {
                        case "Article":
                            conttype_val = "2";
                            break;
                        case "Doctor":
                            conttype_val = "3";
                            break;
                        case "Tool":
                            conttype_val = "4";
                            break;
                        default:
                            conttype_val = "1";
                            break;
                    }


/*
                    if (ctype.equals("Article"))
                        conttype_val = "2";
                    else if (ctype.equals("Doctor"))
                        conttype_val = "3";
                    else
                        conttype_val = "1";
*/


                    share_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            View parent = (View) view.getParent();

                            TextView tv_url = (TextView) parent.findViewById(R.id.tv_url);

                            art_url = Model.BASE_URL + "app/loadContent?content_type=1&url=" + tv_url.getText().toString();

                            System.out.println("art_url---------" + art_url);
                            System.out.println("doctid---------" + doctid);


                            if (conttype_val.equals("1")) {
                                TakeScreenshot_Share("Read the Question and Answers on iCliniq for ", title_text, art_url);
                            } else if (conttype_val.equals("2")) {
                                TakeScreenshot_Share("Read the Articles on iCliniq for ", title_text, art_url);
                            } else if (conttype_val.equals("2")) {
                                TakeScreenshot_Share("Health Tools ", title_text, art_url);
                            } else {
                                TakeScreenshot_Share("It is quick to consult doctor " + title_text + " on iCliniq", "", art_url);
                            }
                        }
                    });


                    if (conttype_val.equals("1")) {
                        Model.query_launch = "MovieSearchActivity";
                        //Intent intent = new Intent(getApplicationContext(), QADetailNew.class);
                        Intent intent = new Intent(getApplicationContext(), QADetailNew.class);
                        intent.putExtra("KEY_ctype", conttype_val);
                        intent.putExtra("KEY_url", url);
                        startActivity(intent);
                        //finish();
                    }

                    if (conttype_val.equals("2")) {

                        //Intent intent = new Intent(Search_Screen.this, ArticleDetailActivity.class);
                        Intent intent = new Intent(getApplicationContext(), ArticleViewActivity.class);
                        intent.putExtra("img_url", "");
                        intent.putExtra("title", title_text);
                        intent.putExtra("KEY_ctype", conttype_val);
                        intent.putExtra("KEY_url", url);
                        startActivity(intent);
                        //finish();
                    }

                    if (conttype_val.equals("3")) {


                        Intent intent = new Intent(getApplicationContext(), ParallexMainActivity.class);
                        intent.putExtra("tv_doc_id", tv_doc_id.getText().toString());
                        startActivity(intent);

                    }

                    if (conttype_val.equals("4")) {

                        Intent i = new Intent(Search_Screen.this, WebViewActivity.class);
                        i.putExtra("url", url + "?t=mob&layout=empty&medium=app_search");
                        i.putExtra("type", title_text);
                        startActivity(i);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Double floor_val;
                Integer int_floor = 0;

                int threshold = 1;
                int count = listview.getCount();
                System.out.println("count----- " + count);

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listview.getLastVisiblePosition() >= count - threshold) {

                        double cur_page = (listview.getAdapter().getCount()) / 10;
                        System.out.println("cur_page 1----" + cur_page);

                        if (count < 10) {
                            System.out.println("No more to Load");
                            //Toast.makeText(getApplicationContext(), "No more to queries load", Toast.LENGTH_LONG).show();
                            int_floor = 0;

                        } else if (count == 10) {
                            floor_val = cur_page + 1;

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();

                        } else {
                            floor_val = Math.floor(cur_page);
                            Double diff = cur_page - floor_val;

                            System.out.println("cur_page 2----" + cur_page);
                            System.out.println("floor_val 2----" + floor_val);
                            System.out.println("diff 2----" + diff);

                            if (diff == 0) {
                                floor_val = floor_val + 1;
                            } else if (diff > 0) {
                                floor_val = floor_val + 2;
                            }

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();

                        }
                        if (int_floor != 0 && (pagination)) {
                            //-----------------------------------------------
                            String search_url = Model.BASE_URL + "app/search?q=" + search_text + "&page=" + int_floor;
                            System.out.println("search_url-----------" + search_url);
                            new MyTask_Pagination().execute(search_url);
                            //-----------------------------------------------
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        first_layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        listview.setVisibility(View.GONE);
        empty_layout.setVisibility(View.GONE);
        progressBar_bottom.setVisibility(View.GONE);

        listview.setScrollViewCallbacks(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        System.out.println("scrollY---------" + scrollY);
        System.out.println("firstScroll---------" + firstScroll);
        System.out.println("dragging---------" + dragging);
    }

    @Override
    public void onDownMotionEvent() {

        System.out.println("Scrolling Down---------");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        System.out.println("Scrolling----------------------" + scrollState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (listview.getVisibility() == View.VISIBLE) {
            first_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            listview.setVisibility(View.GONE);
            empty_layout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);

            edt_searchword.setText("");
        } else {
            finish();
        }
    }


    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    class MyTask_server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            first_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);
            listview.setVisibility(View.GONE);
            empty_layout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                arrayOfList = new SearchListParser().getData(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                if (null == arrayOfList || arrayOfList.size() == 0) {

                    first_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                    listview.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.VISIBLE);

                } else {

                    if (arrayOfList.size() < 10) {
                        pagination = false;
                    }

                    first_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    empty_layout.setVisibility(View.GONE);

                    setAdapterToListview();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MyTask_Pagination extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            first_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                arrayOfList = new SearchListParser().getData(params[0]);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                if (null == arrayOfList || arrayOfList.size() == 0) {

                    //Toast.makeText(getApplicationContext(), "No more queries to load", Toast.LENGTH_LONG).show();

                    first_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    empty_layout.setVisibility(View.GONE);

                } else {

                    if (arrayOfList.size() < 10) {
                        pagination = false;
                    }

                    first_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    empty_layout.setVisibility(View.GONE);

                    add_page_AdapterToListview();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapterToListview() {

        try {
            objAdapter = new SearchListAdapter(Search_Screen.this, R.layout.search_row, arrayOfList);
            listview.setAdapter(objAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_page_AdapterToListview() {

        try {
            objAdapter.addAll(arrayOfList);
            listview.setSelection(objAdapter.getCount() - (arrayOfList.size()));
            objAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void full_process() {

        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

            if (search_type.equals("articles")) {
                edt_searchword.setHint("Search a health articles");
                search_url = Model.BASE_URL + "app/articles?page=1";
                System.out.println("search_url----------" + search_url);
            } else if (search_type.equals("qa")) {
                edt_searchword.setHint("Search a question answers");
                search_url = Model.BASE_URL + "app/qa?page=1";
                System.out.println("search_url----------" + search_url);

            } else {

                edt_searchword.setHint("Search a Health issue, doctors, articles, Q/A");

                first_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                progressBar_bottom.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                empty_layout.setVisibility(View.GONE);
            }

            //new MyTask_server().execute(search_url);

            if (search_tag_text != null && !(search_tag_text).isEmpty() && !(search_tag_text).equals("null") && !(search_tag_text).equals("")) {

                try {
                    JSONArray temp = new JSONArray(json_search_str);
                    String[] mArray = temp.join(",").split(",");

                    for (int i = 0; i < 10; i++) {
                        System.out.println("Value of i---------" + mArray[i]);

                        vi = getLayoutInflater().inflate(R.layout.search_tag, null);
                        tv_tag = (TextView) vi.findViewById(R.id.tv_tag);

                        //---------------- Custom default Font --------------------
                        Typeface robo_regular = Typeface.createFromAsset(getAssets(), Model.font_name);
                        Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);
                        tv_tag.setTypeface(robo_regular);
                        //---------------- Custom default Font --------------------

                        String replaced = mArray[i].replace("\"", "");
                        //tv_tag.setText(replaced);

                       // tagcontainerLayout1.addTag(replaced);

                        //tag_layout.addView(vi); DVD Write,
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //--------------------------------------------------------
                String tags_url = Model.BASE_URL + "app/tags";
                System.out.println("get tags_url-----------" + tags_url);
                new JSON_getTags().execute(tags_url);
                //--------------------------------------------------------------------

            } else {
                //--------------------------------------------------------
                String tags_url = Model.BASE_URL + "app/tags";
                System.out.println("get tags_url-----------" + tags_url);
                new JSON_getTags().execute(tags_url);
                //--------------------------------------------------------------------
            }


            //-------- Getting QA --------------------------------
            String qa_url = Model.BASE_URL + "app/qa?page=1";
            System.out.println("qa_url-------------" + qa_url);
            //new JSON_qa_server().execute(qa_url);
            //-------- Getting QA --------------------------------

            //-------- Articles--------------------------------
            String DoctorList_url = Model.BASE_URL + "app/articles?page=1";
            System.out.println("DoctorList_url------------" + DoctorList_url);
            //new get_Articles().execute(DoctorList_url);
            //-------- Articles--------------------------------


        }
    }

    private class JSON_qa_server extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                System.out.println("urls[]--------------" + urls[0]);
                str_response = new JSONParser().getJSONString(urls[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("Server response--------------" + str_response);

                //apply_offers(str_response);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*public void apply_offers(String jsonarray_str) {

        try {
            innerLay.removeAllViews();

            JSONArray jsonarray = new JSONArray(jsonarray_str);

            System.out.println("jsonarray.length()-----" + jsonarray.length());
            System.out.println("jsonarray-----" + jsonarray.toString());

            if (jsonarray.length() > 0) {

                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobj1 = jsonarray.getJSONObject(i);
                    System.out.println("jsonobj_first-----" + jsonobj1.toString());

                    qa_url = jsonobj1.getString("url");
                    qa_doctor_name = jsonobj1.getString("doctor_name");
                    qa_title = jsonobj1.getString("title");
                    qa_abstract = jsonobj1.getString("abstract");
                    qa_photo_url = jsonobj1.getString("photo_url");

                    System.out.println("jsononjsec-----" + jsonobj1.toString());

                    vi = getLayoutInflater().inflate(R.layout.cardview_qa, null);
                    textview_title = (TextView) vi.findViewById(R.id.textview_title);
                    textview_short = (TextView) vi.findViewById(R.id.textview_short);
                    textview_docname = (TextView) vi.findViewById(R.id.textview_docname);
                    textview_ctype = (TextView) vi.findViewById(R.id.textview_ctype);
                    tv_url = (TextView) vi.findViewById(R.id.tv_url);
                    imageview_poster = (CircleImageView) vi.findViewById(R.id.imageview_poster);

                    //---------------- Custom Font --------------------
                    Typeface robo_regular = Typeface.createFromAsset(getAssets(), Model.font_name);
                    Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                    textview_title.setTypeface(robo_bold);
                    textview_short.setTypeface(robo_regular);
                    textview_docname.setTypeface(robo_regular);
                    textview_ctype.setTypeface(robo_regular);
                    //---------------- Custom Font --------------------

                    textview_title.setText(qa_title);
                    textview_short.setText(qa_abstract);
                    textview_docname.setText(qa_doctor_name);
                    textview_ctype.setText("QA");
                    tv_url.setText(qa_url);

                    Picasso.with(Search_Screen.this).load(qa_photo_url).placeholder(R.drawable.progress_animation).error(R.mipmap.user_grey_icon).into(imageview_poster);

                    innerLay.addView(vi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    private void TakeScreenshot_Share(String ctext, String title, String str_url) {

        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, ctext + "\n\n" + title + "\n\n" + art_url);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_getTags extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                json_search_str = jParser.getJSONString(urls[0]);

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(search_tag, json_search_str);
                editor.apply();
                //============================================================

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                tag_layout.removeAllViews();

                System.out.println("json_search_str------" + json_search_str);

                //=========================================================
                Model.kiss.record("android.patient.getTags");
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("android.patient.tags", json_search_str);
                Model.kiss.set(properties);
                //----------------------------------------------------------------------------
                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.patient.tags", json_search_str);
                FlurryAgent.logEvent("android.patient.getTags", articleParams);
                //----------- Flurry -------------------------------------------------

                //String json_search_str_brace = json_search_str.replace("[", "{").replace("]", "}");
                JSONArray temp = new JSONArray(json_search_str);
                String[] mArray = temp.join(",").split(",");


                for (int i = 0; i < mArray.length; i++) {
                    System.out.println("Value of i---------" + mArray[i]);

                    vi = getLayoutInflater().inflate(R.layout.search_tag, null);
                    tv_tag = (TextView) vi.findViewById(R.id.tv_tag);

                    //---------------- Custom default Font --------------------
                    Typeface robo_regular = Typeface.createFromAsset(getAssets(), Model.font_name);
                    Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);
                    tv_tag.setTypeface(robo_regular);
                    //---------------- Custom default Font --------------------

                    String replaced = mArray[i].replace("\"", "");
                    //tv_tag.setText(replaced);
                    //tagcontainerLayout1.addTag(replaced);

                    //tag_layout.addView(vi);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class Get_JSON_getTags extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                json_search_str = jParser.getJSONString(urls[0]);

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(search_tag, json_search_str);
                editor.apply();
                //============================================================

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
        }
    }


    public void onClick2(View v) {

        View parent = (View) v.getParent();

        TextView tv_tag = (TextView) parent.findViewById(R.id.tv_tag);
        String tv_tagval = tv_tag.getText().toString();
        System.out.println("tv_tagval-----------------------------------" + tv_tagval);

        edt_searchword.setText(tv_tagval);

        try {
            search_text = URLEncoder.encode(tv_tagval, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        search_by_word(search_text);

    }

    public void onClick_qa(View v) {

        try {

            switch (v.getId()) {

                case R.id.qa_layout:

                    //----------- get Doc Id ---------------------------------------
                    View parent_fav = (View) v.getParent();
                    TextView tv_url = (TextView) parent_fav.findViewById(R.id.tv_url);
                    String tv_url_val = tv_url.getText().toString();
                    System.out.println("tv_url_val-----------" + tv_url_val);
                    //----------- get Doc Id ---------------------------------------

                    Intent intent = new Intent(getApplicationContext(), QADetailNew.class);
                    intent.putExtra("KEY_ctype", "1");
                    intent.putExtra("KEY_url", tv_url_val);
                    startActivity(intent);

                    break;
            }

            System.out.println("onClick-------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            if ((Model.query_launch).equals("SpecialityListActivity")) {

                System.out.println("Resume Model.query_launch-----" + Model.select_spec_val);
                System.out.println("Resume Model.select_specname-----" + Model.select_specname);

                if (!Model.select_spec_val.equals("0")) {

                    System.out.println("text------------------" + Model.select_specname);

                    search_by_word(Model.select_specname);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search_by_word(String search_str) {
        System.out.println("text------------------" + Model.select_specname);

        search_text = search_str;
        edt_searchword.setText(search_text);
        //=========================================================
        Model.kiss.record("android.patient.speciality_search");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("android.patient.tag_word", search_text);
        Model.kiss.set(properties);
        //----------------------------------------------------------------------------
        //----------- Flurry -------------------------------------------------
        Map<String, String> articleParams = new HashMap<String, String>();
        articleParams.put("android.patient.tag_word", search_text);
        FlurryAgent.logEvent("android.patient.speciality_search", articleParams);
        //----------- Flurry -------------------------------------------------

        //------------ Google firebase Analitics-----------------------------------------------
        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        Bundle params = new Bundle();
        params.putString("User_id", Model.id);
        params.putString("tag_word", search_text);
        Model.mFirebaseAnalytics.logEvent("speciality_search", params);
        //------------ Google firebase Analitics---------------------------------------------


        if (isInternetOn()) {
            //-------------------------------------------------------
            String search_url = Model.BASE_URL + "app/search?q=" + search_text + "&page=0";
            System.out.println("search_url------------------" + search_url);
            new MyTask_server().execute(search_url);
            //-------------------------------------------------------

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } else {
            //Toast.makeText(Search_Screen.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
            Snackbar.make(titlayout, "Internet is not connected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }
}
