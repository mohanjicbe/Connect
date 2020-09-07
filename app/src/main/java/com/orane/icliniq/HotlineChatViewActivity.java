package com.orane.icliniq;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.attachment_view.GridViewActivity;
import com.orane.icliniq.file_picking.utils.FileUtils;
import com.orane.icliniq.fileattach_library.DefaultCallback;
import com.orane.icliniq.fileattach_library.EasyImage;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.pubnub.api.Pubnub;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class HotlineChatViewActivity extends AppCompatActivity {

    ImageView imgSinglePick;
    Button btnGalleryPick;
    Button btnGalleryPickMul;
    String action, attach_file_text;
    ViewSwitcher viewSwitcher;
    ArrayList<String> imagePaths;
    GridView gridGallery;
    public final Handler handler = new Handler();
    public Timer timer;
    public TimerTask timerTask;
    //public Handler handler;

    Timer myTimer;
    TextView attachfile;
    Uri selectedImageUri;
    ProgressBar progressBar;
    Bitmap bitmap;
    JSONArray jarray_files;
    CircleImageView doc_photo;
    LinearLayout files_layout, layout_attachfile, ans_layout, myLayout, query_layout, send_message_layout;
    View vi_ans, vi, vi_files;
    public JSONObject hotline_jsonobj, jsonobj_view, jsonobj_files, chat_post_jsonobj, json, feedback_json, feedback_jsonobj, jsonobj, jsonobj1;
    public JSONArray jsonarray;
    ScrollView scrollview;
    EditText edt_chat_msg;
    TextView tv_filename, tv_ext, tv_userid, tvt_morecomp, tv_morecomp, tvt_prevhist, tv_prevhist, tvt_curmedi, tv_curmedi, tvt_pastmedi, tv_pastmedi, tvt_labtest, tv_labtest, tv_query, tv_answer, tv_datetime;
    Button btn_send;
    CircleImageView imageview_poster;
    ImageView file_image, take_photo_image;

    public String str_response, image_path, file_url, selectedfilename, is_hline, doc_photo_url, Doctor_name, Log_Status, speciality_json, doctor_json_text, status_text, qid, files_text, answer_ext_text, more_comp, prev_hist, cur_medi, past_medi, lab_tests, prob_caus, inv_done, diff_diag, prob_diag, treat_plan, prev_measu, reg_folup;
    public String question_ext, docurl, Doctor_id, selqid, fcode, chat_msg, prep_inv_id, prep_inv_fee, prep_inv_strfee, feedback_status, docname, answer, answerval, status, created_at, question, ratting_val, feedback_text, query_id_val, cur_answer_id, answerval_id, pass, uname, str_status, unpaid_fee, unpaid_invid, unpaid_json_text, str_follow_fee, ftrack_str_status_val, ftrack_str_status, ftrack_fee, ftrack_str, selquery_id;
    public String follouwupcode, file_user_id, file_doctype, file_file, file_ext, selectedPath, filename, files_List;

    Pubnub pubnub;

    SharedPreferences sharedpreferences;
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
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_qid = "sp_qid_key";
    public static final String token = "token_key";

    private static final int CAMERA_REQUEST = 1888;
    private static final String TAG = "FileChooserExampleActivity";
    private static final int REQUEST_CODE = 6384; // onActivityResult request

    TextView tv_tooltit, tv_tooldesc;
    LinearLayout layout_file;
    CircleImageView toolbar_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotline_chat_view);

        FlurryAgent.onPageView();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pubnub = new Pubnub(PubnubKeys.PUBLISH_KEY, PubnubKeys.SUBSCRIBE_KEY);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getSupportActionBar().setTitle("Chat View");

            tv_tooltit = toolbar.findViewById(R.id.tv_tooltit);
            tv_tooldesc = toolbar.findViewById(R.id.tv_tooldesc);

            Typeface tf_answer = Typeface.createFromAsset(getAssets(), Model.font_name);
            tv_tooltit.setTypeface(tf_answer);
            tv_tooldesc.setTypeface(tf_answer);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        initImageLoader();
        // init();

        //================ Shared Pref ===============================================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        uname = sharedpreferences.getString(user_name, "");
        docname = sharedpreferences.getString(Name, "");
        Model.name = sharedpreferences.getString(Name, "");
        pass = sharedpreferences.getString(password, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        Model.fee_q = sharedpreferences.getString(fee_q, "");
        Model.fee_consult = sharedpreferences.getString(fee_consult, "");
        Model.currency_label = sharedpreferences.getString(currency_label, "");
        Model.currency_symbol = sharedpreferences.getString(currency_symbol, "");
        Model.have_free_credit = sharedpreferences.getString(have_free_credit, "");
        Model.photo_url = sharedpreferences.getString(photo_url, "");
        Model.kmid = sharedpreferences.getString(sp_km_id, "");
        Model.token = sharedpreferences.getString(token, "");
        //================ Shared Pref ===============================================

        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        toolbar_image = findViewById(R.id.toolbar_image);
        take_photo_image = findViewById(R.id.take_photo_image);
        scrollview = findViewById(R.id.scrollview);
        myLayout = findViewById(R.id.parent_qalayout);
        progressBar = findViewById(R.id.progressBar);
        send_message_layout = findViewById(R.id.send_message_layout);
        edt_chat_msg = findViewById(R.id.edt_chat_msg);
        btn_send = findViewById(R.id.btn_send);


        try {

            Intent intent = getIntent();
            selqid = intent.getStringExtra("selqid");
            Doctor_id = intent.getStringExtra("Doctor_id");
            doc_photo_url = intent.getStringExtra("docurl");
            follouwupcode = intent.getStringExtra("fcode");

            Model.fcode = "";

            System.out.println("selecting_Doctor_id---------" + Doctor_id);
            System.out.println("selqid---------" + selqid);
            System.out.println("doc_photo_url---------" + doc_photo_url);
            System.out.println("follouwupcode---------" + follouwupcode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (new NetCheck().netcheck(HotlineChatViewActivity.this)) {
            if (Log_Status.equals("1")) {
                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                    fullprocess();
                } else {
                    force_logout();
                }
            } else {
                force_logout();
            }

        } else {
            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.VISIBLE);
        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        //------------------ Initialize File Attachment ---------------------------------
        Nammu.init(this);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        //------------------ Initialize File Attachment ---------------------------------
        EasyImage.configuration(this)
                .setImagesFolderName("Attachments")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(true);
        //------------------ Initialize File Attachment ---------------------------------


        take_photo_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //attach_dialog();

                int permissionCheck = ContextCompat.checkSelfPermission(HotlineChatViewActivity.this, Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    EasyImage.openCamera(HotlineChatViewActivity.this, 0);
                } else {
                    Nammu.askForPermission(HotlineChatViewActivity.this, Manifest.permission.CAMERA, new PermissionCallback() {
                        @Override
                        public void permissionGranted() {
                            EasyImage.openCamera(HotlineChatViewActivity.this, 0);
                        }

                        @Override
                        public void permissionRefused() {

                        }
                    });
                }


               /* try {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.Qid:", selqid);
                    articleParams.put("android.patient.Doctor_id:", Doctor_id);
                    articleParams.put("android.patient.follouwupcode:", follouwupcode);
                    FlurryAgent.logEvent("android.patient.Hotline_Chat_TakePhoto", articleParams);
                    //----------- Flurry -------------------------------------------------

                } catch (Exception e) {
                    System.out.println("Exception---Camera---123--" + e.toString());
                    e.printStackTrace();
                }*/

            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    chat_msg = edt_chat_msg.getText().toString();
                    add_chat_text(chat_msg);
                    //add_received_text("" + (chat_msg));

                    if (!chat_msg.equals("")) {

                        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                            if (Doctor_id != null && !Doctor_id.isEmpty() && !Doctor_id.equals("null") && !Doctor_id.equals("")) {
                                try {
                                    json = new JSONObject();
                                    json.put("txt", chat_msg);
                                    json.put("fcode", follouwupcode);
                                    json.put("ses_uid", (Model.id));
                                    json.put("doctor_id", Doctor_id);

                                    if (new NetCheck().netcheck(HotlineChatViewActivity.this)) {
                                        System.out.println("Chat post json------" + json);
                                        new JSONPostQuery().execute(json);
                                    } else {
                                        Toast.makeText(HotlineChatViewActivity.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
                                    }

                                    getWindow().setSoftInputMode(
                                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                    );

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(edt_chat_msg.getWindowToken(), 0);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("Doctor id is null------");
                            }
                        } else {
                            System.out.println("id is null------");
                        }
                    } else {
                        System.out.println("Chat text is Empty");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initImageLoader() {

        /*try {

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                    new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /*private void init() {


        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new CustomGalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(HotlineChatViewActivity.this, "" + imagePaths.get(i), Toast.LENGTH_LONG).show();
            }
        });

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);
        imgSinglePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagePaths != null)// if minimum image is choose
                    Toast.makeText(HotlineChatViewActivity.this, "" + imagePaths.get(0), Toast.LENGTH_LONG).show();
            }
        });

        btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
        btnGalleryPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Action.ACTION_PICK);
                startActivityForResult(i, 110);

            }
        });

        btnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Manifest recognize our multiple request by this way
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });

    }*/


    public void fullprocess() {
        try {
            System.out.println("selqid-----" + selqid);
            if (!(selqid).equals("")) {
                //---------------------------------------------------
                String full_url = Model.BASE_URL + "sapp/viewq?id=" + selqid + "&user_id=" + (Model.id) + "&format=json&token=" + Model.token + "&enc=1&isAFiles=1";
                System.out.println("full_url-------------" + full_url);
                new JSONAsyncTask().execute(full_url);
                //---------------------------------------------------
            }
        } catch (Exception e) {
            System.out.println("Exception1-----" + e.toString());
            e.printStackTrace();
        }
    }


    private class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

/*          progressBar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);*/
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                chat_post_jsonobj = jParser.JSON_POST(urls[0], "Chat_PostQuery");

                System.out.println("urls[0]---------------" + urls[0]);
                System.out.println("chat_post_jsonobj---------------" + chat_post_jsonobj.toString());


                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                String status_txt = chat_post_jsonobj.getString("status");
                System.out.println("Chat post Status----- " + status_txt);

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.patient.Qid:", selqid);
                articleParams.put("android.patient.Doctor_id:", Doctor_id);
                articleParams.put("android.patient.follouwupcode:", follouwupcode);
                articleParams.put("android.patient.Msg:", chat_msg);
                articleParams.put("android.patient.status:", status_txt);
                FlurryAgent.logEvent("android.patient.Hotline_Chat_PostMsg", articleParams);
                //----------- Flurry -------------------------------------------------

                //------------ Google firebase Analitics-----------------------------------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("qid", selqid);
                params.putString("Doctor_id", Doctor_id);
                Model.mFirebaseAnalytics.logEvent("Hotline_Chat_PostMsg", params);
                //------------ Google firebase Analitics-----------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.chat_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_attach) {

            attach_dialog();
/*            Intent intent = new Intent(HotlineChatViewActivity.this, HotlineFileChooserActivity.class);
            intent.putExtra("selqid", selqid);
            startActivity(intent);*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

/*                JSONParser jParser = new JSONParser();
                jsonobj_view = jParser.getJSONFromUrl(urls[0]);

                System.out.println("URL------------------------ " + urls[0]);
                System.out.println("jsonobj------------------------ " + jsonobj_view.toString());*/

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            myLayout.removeAllViews();

            try {

                jsonobj_view = new JSONObject(str_response);

                if (jsonobj_view.has("token_status")) {
                    String token_status = jsonobj_view.getString("token_status");
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

                    jsonarray = new JSONArray();
                    jsonarray.put(jsonobj_view);

                    System.out.println("jsonarray.length()-----" + jsonarray.length());
                    System.out.println("jsonarray-----" + jsonarray.toString());


                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobj1 = jsonarray.getJSONObject(i);
                        System.out.println("jsonobj_first-----" + jsonobj1.toString());

                        for (int j = 1; j <= (jsonobj1.length()); j++) {

                            question = "";

                            String s = "" + j;
                            if (jsonobj1.has("" + s)) {

                                String thread = jsonobj1.getString("" + s);

                                System.out.println("s value-----" + s);
                                System.out.println("thread-----" + thread);

                                JSONObject jsononjsec = new JSONObject(thread);

                                System.out.println("jsononjsec-----------------" + jsononjsec.toString());

                                query_id_val = jsononjsec.getString("id");
                                question = jsononjsec.getString("question");
                                question_ext = jsononjsec.getString("question_ext");
                                files_text = jsononjsec.getString("q_files");
                                Doctor_id = jsononjsec.getString("doctor_id");

                                if (jsononjsec.has("status")) {
                                    status_text = jsononjsec.getString("status");
                                } else status_text = "";

                                answer = jsononjsec.getString("answer");
                                created_at = jsononjsec.getString("created_at");
                                follouwupcode = jsononjsec.getString("fcode");
                                is_hline = jsononjsec.getString("is_hline");

/*                            if(is_hline.equals("1")){
                                System.out.println("Redirect to QueryView Page qid--------------" + selqid);

                                Intent i1 = new Intent(HotlineChatViewActivity.this, QueryViewActivity.class);
                                i1.putExtra("qtype", "followup");
                                i1.putExtra("qid", selqid);
                                startActivity(i1);
                                finish();
                            }*/

                                vi = getLayoutInflater().inflate(R.layout.chat_view_question, null);

                                files_layout = vi.findViewById(R.id.files_layout);
                                tvt_morecomp = vi.findViewById(R.id.tvt_morecomp);
                                tv_morecomp = vi.findViewById(R.id.tv_morecomp);
                                tvt_prevhist = vi.findViewById(R.id.tvt_prevhist);
                                tv_prevhist = vi.findViewById(R.id.tv_prevhist);
                                tvt_curmedi = vi.findViewById(R.id.tvt_curmedi);
                                tv_curmedi = vi.findViewById(R.id.tv_curmedi);
                                tvt_pastmedi = vi.findViewById(R.id.tvt_pastmedi);
                                tv_pastmedi = vi.findViewById(R.id.tv_pastmedi);
                                tvt_labtest = vi.findViewById(R.id.tvt_labtest);
                                tv_labtest = vi.findViewById(R.id.tv_labtest);

                                layout_attachfile = vi.findViewById(R.id.layout_attachfile);
                                query_layout = vi.findViewById(R.id.query_layout);
                                tv_query = vi.findViewById(R.id.tv_query);
                                tv_datetime = vi.findViewById(R.id.tv_datetime);

                                layout_file = vi.findViewById(R.id.layout_file);
                                attachfile = vi.findViewById(R.id.attachfile);
                                tv_filename = vi.findViewById(R.id.tv_filename);

                                //question = question.replace("\\\n", System.getProperty("line.separator"));
                                Typeface tf = Typeface.createFromAsset(getAssets(), Model.font_name);
                                tv_query.setTypeface(tf);

                                tv_query.setText(Html.fromHtml(question));
                                tv_datetime.setText(created_at);

                                //----------------------question_ext---------------------------------------------------------
                                if ((question_ext.length()) > 2) {
                                    JSONObject qext_json = new JSONObject(question_ext);
                                    System.out.println("jsonquestext-----" + qext_json.toString());

                                    if (qext_json.has("complaint_more")) {
                                        more_comp = qext_json.getString("complaint_more");
                                    }
                                    if (qext_json.has("p_history")) {
                                        prev_hist = qext_json.getString("p_history");
                                    }
                                    if (qext_json.has("c_medications")) {
                                        cur_medi = qext_json.getString("c_medications");
                                    }
                                    if (qext_json.has("p_medications")) {
                                        past_medi = qext_json.getString("p_medications");
                                    }
                                    if (qext_json.has("tests")) {
                                        lab_tests = qext_json.getString("tests");
                                    }

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

                                    //myLayout.addView(vi);

                                } else {
                                    tv_curmedi.setVisibility(View.GONE);
                                    tvt_curmedi.setVisibility(View.GONE);
                                    tv_labtest.setVisibility(View.GONE);
                                    tvt_labtest.setVisibility(View.GONE);
                                    tv_pastmedi.setVisibility(View.GONE);
                                    tvt_pastmedi.setVisibility(View.GONE);
                                    tv_prevhist.setVisibility(View.GONE);
                                    tvt_prevhist.setVisibility(View.GONE);
                                    tvt_morecomp.setVisibility(View.GONE);
                                    tv_morecomp.setVisibility(View.GONE);


                                    //myLayout.addView(vi);
                                }

                                //---------------- Files ---------------------------------------
                                if ((files_text.length()) > 2) {

                                    layout_attachfile.setVisibility(View.VISIBLE);

                                    System.out.println("files_text------" + files_text);
                                    jarray_files = jsononjsec.getJSONArray("q_files");

                                    System.out.println("jsonobj_items------" + jarray_files.toString());
                                    System.out.println("jarray_files.length()------" + jarray_files.length());

                                    attachfile.setText("Attached " + jarray_files.length() + " File(s)");

                                    attach_file_text = "";

                                    for (int m = 0; m < jarray_files.length(); m++) {
                                        jsonobj_files = jarray_files.getJSONObject(m);

                                        System.out.println("jsonobj_files--" + m + " ----" + jsonobj_files.toString());

                                        file_user_id = jsonobj_files.getString("user_id");
                                        file_doctype = jsonobj_files.getString("doctype");
                                        file_file = jsonobj_files.getString("file");
                                        file_ext = jsonobj_files.getString("ext");
                                        file_url = jsonobj_files.getString("file_url");

                                        //------------------------ File Attached Text --------------------------------
                                        if (attach_file_text.equals("")) {
                                            attach_file_text = file_url;
                                            System.out.println("attach_file_text-------" + attach_file_text);
                                        } else {
                                            attach_file_text = attach_file_text + "###" + file_url;
                                            System.out.println("attach_file_text-------" + attach_file_text);
                                        }
                                        //------------------------ File Attached Text --------------------------------

                                        System.out.println("file_user_id--------" + file_user_id);
                                        System.out.println("file_doctype--------" + file_doctype);
                                        System.out.println("filename--------" + file_file);
                                        System.out.println("file_ext--------" + file_ext);
                                        System.out.println("file_url--------" + file_url);

                                        vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                                        ImageView file_image = vi_files.findViewById(R.id.file_image);
                                        //tv_filename = (TextView) vi_files.findViewById(R.id.tv_filename);
                                        tv_ext = vi_files.findViewById(R.id.tv_ext);
                                        tv_userid = vi_files.findViewById(R.id.tv_userid);
                                        TextView tv_filename_new = vi_files.findViewById(R.id.tv_filename);

                                        //tv_filename.setText(Html.fromHtml(file_url));
                                        //tv_filename.setText(attach_file_text);

                                        System.out.println("Final attach_file_text-------" + attach_file_text);

                                        tv_filename_new.setText(file_url);
                                        tv_ext.setText(file_ext);
                                        tv_userid.setText(file_user_id);

                                        files_layout.addView(vi_files);
                                        files_layout.setVisibility(View.VISIBLE);
                                    }

                                    tv_filename.setText(files_text);

                                    files_layout.setVisibility(View.VISIBLE);
                                } else {
                                    layout_attachfile.setVisibility(View.GONE);
                                }
                                //---------------- Files---------------------------------------

                                /*
                                //---------------- Files ---------------------------------------
                                if ((files_text.length()) > 2) {

                                    //layout_attachfile.setVisibility(View.VISIBLE);

                                    System.out.println("files_text------" + files_text);
                                    JSONArray jarray_files = jsononjsec.getJSONArray("q_files");
                                    System.out.println("jsonobj_items------" + jarray_files.toString());
                                    System.out.println("jarray_files.length()------" + jarray_files.length());

                                    //tvattached.setText("Attached " + jarray_files.length() + " File(s)");

                                    for (int jk = 0; jk < jarray_files.length(); jk++) {
                                        jsonobj_files = jarray_files.getJSONObject(jk);

                                        System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                                        file_user_id = jsonobj_files.getString("user_id");
                                        file_doctype = jsonobj_files.getString("doctype");
                                        file_file = jsonobj_files.getString("file");
                                        file_ext = jsonobj_files.getString("ext");
                                        file_url = jsonobj_files.getString("file_url");

                                        System.out.println("file_user_id--------" + file_user_id);
                                        System.out.println("file_doctype--------" + file_doctype);
                                        System.out.println("filename--------" + file_file);
                                        System.out.println("file_ext--------" + file_ext);
                                        System.out.println("file_url--------" + file_url);

                                        vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                                        file_image = (ImageView) vi_files.findViewById(R.id.file_image);
                                        tv_filename = (TextView) vi_files.findViewById(R.id.tv_filename);
                                        tv_ext = (TextView) vi_files.findViewById(R.id.tv_ext);
                                        tv_userid = (TextView) vi_files.findViewById(R.id.tv_userid);

                                        //tv_filename.setText(Html.fromHtml(file_file));
                                        tv_filename.setText(Html.fromHtml(file_url));
                                        tv_ext.setText(file_ext);
                                        tv_userid.setText(file_user_id);


                                        files_layout.addView(vi_files);
                                    }

                                } else {
                                    layout_attachfile.setVisibility(View.GONE);
                                }
                                //---------------- Files---------------------------------------
*/
                                if (!status_text.equals("8")) {
                                    myLayout.addView(vi);
                                } else {
                                    edt_chat_msg.setText(question);
                                }

                                System.out.println("question------------" + question);
                                System.out.println("created_at------------" + created_at);


                                if ((answer.length()) > 2) {

                                    JSONArray ansjsonarray = jsononjsec.getJSONArray("answer");
                                    for (int k = 0; k < ansjsonarray.length(); k++) {

                                        answerval = "";

                                        vi_ans = getLayoutInflater().inflate(R.layout.chat_view_answer, null);

                                        TextView tvt_probcause = vi_ans.findViewById(R.id.tvt_probcause);
                                        TextView tv_probcause = vi_ans.findViewById(R.id.tv_probcause);
                                        TextView tvt_invdone = vi_ans.findViewById(R.id.tvt_invdone);
                                        TextView tv_invdone = vi_ans.findViewById(R.id.tv_invdone);
                                        TextView tvt_diffdiag = vi_ans.findViewById(R.id.tvt_diffdiag);
                                        TextView tv_diffdiag = vi_ans.findViewById(R.id.tv_diffdiag);
                                        TextView tvt_probdiag = vi_ans.findViewById(R.id.tvt_probdiag);
                                        TextView tv_probdiag = vi_ans.findViewById(R.id.tv_probdiag);
                                        TextView tvt_tratplan = vi_ans.findViewById(R.id.tvt_tratplan);
                                        TextView tv_tratplan = vi_ans.findViewById(R.id.tv_tratplan);
                                        TextView tvt_prevmeasure = vi_ans.findViewById(R.id.tvt_prevmeasure);
                                        TextView tv_prevmeasure = vi_ans.findViewById(R.id.tv_prevmeasure);
                                        TextView tvt_follup = vi_ans.findViewById(R.id.tvt_follup);
                                        TextView tv_follup = vi_ans.findViewById(R.id.tv_follup);

                                        imageview_poster = vi_ans.findViewById(R.id.imageview_poster);
                                        ans_layout = vi_ans.findViewById(R.id.ans_layout);
                                        tv_answer = vi_ans.findViewById(R.id.tv_answer);
                                        tv_datetime = vi_ans.findViewById(R.id.tv_datetime);

                                        JSONObject ansjsonobject = ansjsonarray.getJSONObject(k);
                                        System.out.println("ansjsonobject---" + ansjsonobject);

                                        answerval = ansjsonobject.getString("answer");
                                        answerval_id = ansjsonobject.getString("id");
                                        created_at = ansjsonobject.getString("created_at");
                                        answer_ext_text = ansjsonobject.getString("answer_ext");


                                        //--------------- Doctor Dets ----------------------------
                                        if (ansjsonobject.has("doctor")) {
                                            doctor_json_text = ansjsonobject.getString("doctor");
                                            if ((doctor_json_text.length()) > 2) {

                                                //--------- Doctor --------------------------
                                                JSONObject doctor_dets_json = new JSONObject(doctor_json_text);

                                                if ((doctor_dets_json.getString("id")) != null && !(doctor_dets_json.getString("id")).isEmpty() && !(doctor_dets_json.getString("id")).equals("null") && !(doctor_dets_json.getString("id")).equals("")) {
                                                    Doctor_id = doctor_dets_json.getString("id");
                                                    Doctor_name = doctor_dets_json.getString("name");
                                                    doc_photo_url = doctor_dets_json.getString("photo");
                                                    speciality_json = doctor_dets_json.getString("speciality");
                                                }

                                                Picasso.with(getApplicationContext()).load(doc_photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(imageview_poster);

                                            }
                                            //--------------- Doctor Dets --------------------------------
                                            if (getSupportActionBar() != null) {
                                                getSupportActionBar().setTitle(Doctor_name);

                                                if (Doctor_name != null && !Doctor_name.isEmpty() && !Doctor_name.equals("null") && !Doctor_name.equals("")) {
                                                    tv_tooltit.setText(Doctor_name);
                                                    Picasso.with(getApplicationContext()).load(doc_photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(toolbar_image);

                                                } else {
                                                    tv_tooltit.setVisibility(View.GONE);
                                                }

                                                if (speciality_json != null && !speciality_json.isEmpty() && !speciality_json.equals("null") && !speciality_json.equals("")) {
                                                    tv_tooldesc.setText(speciality_json);
                                                } else {
                                                    tv_tooltit.setVisibility(View.GONE);
                                                }

                                            }
                                        }

                                        System.out.println("answer-----" + answerval);
                                        System.out.println("answer_id-----" + answerval_id);
                                        System.out.println("created_at-----" + created_at);
                                        System.out.println("Doctor_id-----" + Doctor_id);
                                        System.out.println("answer_ext_text-----" + answer_ext_text);
                                        System.out.println("answer_ext_text.length()-----" + answer_ext_text.length());

                                        answerval = answerval.replace("\\\n", System.getProperty("line.separator"));

                                        Typeface tf_answer = Typeface.createFromAsset(getAssets(), Model.font_name);
                                        tv_answer.setTypeface(tf_answer);

                                        tv_answer.setText(Html.fromHtml(answerval));
                                        tv_datetime.setText(Html.fromHtml(created_at));

                                        if ((answer_ext_text.length()) > 2) {

                                            JSONObject ansextrajson = new JSONObject(answer_ext_text);
                                            if (ansextrajson.has("pb_cause")) {
                                                prob_caus = ansextrajson.getString("pb_cause");
                                            }
                                            if (ansextrajson.has("lab_t")) {
                                                inv_done = ansextrajson.getString("lab_t");
                                            }
                                            if (ansextrajson.has("ddx")) {
                                                diff_diag = ansextrajson.getString("ddx");
                                            }
                                            if (ansextrajson.has("pdx")) {
                                                prob_diag = ansextrajson.getString("pdx");
                                            }
                                            if (ansextrajson.has("treatment_plan")) {
                                                treat_plan = ansextrajson.getString("treatment_plan");
                                            }
                                            if (ansextrajson.has("followup")) {
                                                reg_folup = ansextrajson.getString("followup");
                                            }
                                            if (ansextrajson.has("p_tips")) {
                                                prev_measu = ansextrajson.getString("p_tips");
                                            }
                                            //--------------- Answer Extra --------------------------------------------------
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

                                        } else {
                                            tv_probcause.setVisibility(View.GONE);
                                            tvt_probcause.setVisibility(View.GONE);
                                            tv_invdone.setVisibility(View.GONE);
                                            tvt_invdone.setVisibility(View.GONE);
                                            tv_diffdiag.setVisibility(View.GONE);
                                            tvt_diffdiag.setVisibility(View.GONE);
                                            tv_probdiag.setVisibility(View.GONE);
                                            tvt_probdiag.setVisibility(View.GONE);
                                            tv_tratplan.setVisibility(View.GONE);
                                            tvt_tratplan.setVisibility(View.GONE);
                                            tv_prevmeasure.setVisibility(View.GONE);
                                            tvt_prevmeasure.setVisibility(View.GONE);
                                            tv_follup.setVisibility(View.GONE);
                                            tvt_follup.setVisibility(View.GONE);
                                        }


                                        myLayout.addView(vi_ans);
                                    }
                                }
                            }
                        }
                    }

                    //-------- Auto Scroll to Bottom------------------
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    //-------- Auto Scroll to Bottom------------------

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.qid:", selqid);
                    articleParams.put("android.patient.doctor_id:", Doctor_id);
                    articleParams.put("android.patient.follouwupcode:", follouwupcode);
                    FlurryAgent.logEvent("android.patient.Hotline_ChatView", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("qid", selqid);
                    params.putString("Doctor_id", Doctor_id);
                    Model.mFirebaseAnalytics.logEvent("Hotline_ChatView", params);
                    //------------ Google firebase Analitics----------------------------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.VISIBLE);
        }

    }


    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }


    public void add_chat_text(String chat_msg_text) {

        try {
            chat_msg = chat_msg_text;
            System.out.println("chat_msg------------" + chat_msg);

            if (!chat_msg.equals("")) {

                vi = getLayoutInflater().inflate(R.layout.chat_view_question, null);
                query_layout = vi.findViewById(R.id.query_layout);
                tv_query = vi.findViewById(R.id.tv_query);
                tv_datetime = vi.findViewById(R.id.tv_datetime);

                layout_attachfile = vi.findViewById(R.id.layout_attachfile);
                tvt_morecomp = vi.findViewById(R.id.tvt_morecomp);
                tv_morecomp = vi.findViewById(R.id.tv_morecomp);
                tvt_prevhist = vi.findViewById(R.id.tvt_prevhist);
                tv_prevhist = vi.findViewById(R.id.tv_prevhist);
                tvt_curmedi = vi.findViewById(R.id.tvt_curmedi);
                tv_curmedi = vi.findViewById(R.id.tv_curmedi);
                tvt_pastmedi = vi.findViewById(R.id.tvt_pastmedi);
                tv_pastmedi = vi.findViewById(R.id.tv_pastmedi);
                tvt_labtest = vi.findViewById(R.id.tvt_labtest);
                tv_labtest = vi.findViewById(R.id.tv_labtest);

                tv_curmedi.setVisibility(View.GONE);
                tvt_curmedi.setVisibility(View.GONE);
                tv_labtest.setVisibility(View.GONE);
                tvt_labtest.setVisibility(View.GONE);
                tv_pastmedi.setVisibility(View.GONE);
                tvt_pastmedi.setVisibility(View.GONE);
                tv_prevhist.setVisibility(View.GONE);
                tvt_prevhist.setVisibility(View.GONE);
                tvt_morecomp.setVisibility(View.GONE);
                tv_morecomp.setVisibility(View.GONE);
                layout_attachfile.setVisibility(View.GONE);

                tv_query.setText(Html.fromHtml(chat_msg));

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentTimeStamp = dateFormat.format(new Date());
                tv_datetime.setText(currentTimeStamp);

                myLayout.addView(vi);

                edt_chat_msg.setText("");

                //-------- Auto Scroll to Bottom------------------
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                //-------- Auto Scroll to Bottom------------------
            }
        } catch (Exception e) {
            System.out.println("Exception---add text---" + e.toString());
            e.printStackTrace();
        }
    }

    public void add_received_text(String chat_msg_text) {

        try {
            chat_msg = Model.push_msg;
            System.out.println("chat_msg------------" + chat_msg);

            if (!chat_msg_text.equals("")) {

                vi_ans = getLayoutInflater().inflate(R.layout.chat_view_answer, null);

                imageview_poster = vi_ans.findViewById(R.id.imageview_poster);
                ans_layout = vi_ans.findViewById(R.id.ans_layout);
                tv_answer = vi_ans.findViewById(R.id.tv_answer);
                tv_datetime = vi_ans.findViewById(R.id.tv_datetime);

                //Picasso.with(getApplicationContext()).load(doc_photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(imageview_poster);

                TextView tvt_probcause = vi_ans.findViewById(R.id.tvt_probcause);
                TextView tv_probcause = vi_ans.findViewById(R.id.tv_probcause);
                TextView tvt_invdone = vi_ans.findViewById(R.id.tvt_invdone);
                TextView tv_invdone = vi_ans.findViewById(R.id.tv_invdone);
                TextView tvt_diffdiag = vi_ans.findViewById(R.id.tvt_diffdiag);
                TextView tv_diffdiag = vi_ans.findViewById(R.id.tv_diffdiag);
                TextView tvt_probdiag = vi_ans.findViewById(R.id.tvt_probdiag);
                TextView tv_probdiag = vi_ans.findViewById(R.id.tv_probdiag);
                TextView tvt_tratplan = vi_ans.findViewById(R.id.tvt_tratplan);
                TextView tv_tratplan = vi_ans.findViewById(R.id.tv_tratplan);
                TextView tvt_prevmeasure = vi_ans.findViewById(R.id.tvt_prevmeasure);
                TextView tv_prevmeasure = vi_ans.findViewById(R.id.tv_prevmeasure);
                TextView tvt_follup = vi_ans.findViewById(R.id.tvt_follup);
                TextView tv_follup = vi_ans.findViewById(R.id.tv_follup);

                tv_probcause.setVisibility(View.GONE);
                tvt_probcause.setVisibility(View.GONE);
                tv_invdone.setVisibility(View.GONE);
                tvt_invdone.setVisibility(View.GONE);
                tv_diffdiag.setVisibility(View.GONE);
                tvt_diffdiag.setVisibility(View.GONE);
                tv_probdiag.setVisibility(View.GONE);
                tvt_probdiag.setVisibility(View.GONE);
                tv_tratplan.setVisibility(View.GONE);
                tvt_tratplan.setVisibility(View.GONE);
                tv_prevmeasure.setVisibility(View.GONE);
                tvt_prevmeasure.setVisibility(View.GONE);
                tv_follup.setVisibility(View.GONE);
                tvt_follup.setVisibility(View.GONE);

                tv_answer.setText(Html.fromHtml(chat_msg_text));

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentTimeStamp = dateFormat.format(new Date());
                tv_datetime.setText(currentTimeStamp);

                myLayout.addView(vi_ans);

                //--------------- Alert Service -------------------------------------------------------
                if (checkRingerIsOn(getApplicationContext())) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click_sound);
                    mediaPlayer.start();
                } else {
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(500);
                }
                //--------------- Alert Service -------------------------------------------------------

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.patient.chat_msg:", chat_msg);
                articleParams.put("android.patient.Doctor_id:", Doctor_id);
                articleParams.put("android.patient.follouwupcode:", follouwupcode);
                FlurryAgent.logEvent("android.patient.Hotline_Chat_Receive", articleParams);
                //----------- Flurry -------------------------------------------------

                //------------ Google firebase Analitics-----------------------------------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("chat_msg", chat_msg);
                params.putString("Doctor_id", Doctor_id);
                Model.mFirebaseAnalytics.logEvent("Hotline_Chat_Receive", params);
                //------------ Google firebase Analitics----------------------------------------------

                Model.fcode = "";
                edt_chat_msg.setText("");

                //-------- Auto Scroll to Bottom------------------
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                //-------- Auto Scroll to Bottom------------------
            }
        } catch (Exception e) {
            System.out.println("Exception---add text---" + e.toString());
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startTimer();

        Model.screen_status = "true";
        System.out.println("Model.screen_status--onResume-----------" + Model.screen_status);
        System.out.println("Model.upload_files--------------" + Model.upload_files);

        try {

            if (!(Model.upload_files).equals("")) {

                vi = getLayoutInflater().inflate(R.layout.chat_view_question, null);
                tv_query = vi.findViewById(R.id.tv_query);
                tv_datetime = vi.findViewById(R.id.tv_datetime);

                tvt_morecomp = vi.findViewById(R.id.tvt_morecomp);
                tv_morecomp = vi.findViewById(R.id.tv_morecomp);
                tvt_prevhist = vi.findViewById(R.id.tvt_prevhist);
                tv_prevhist = vi.findViewById(R.id.tv_prevhist);
                tvt_curmedi = vi.findViewById(R.id.tvt_curmedi);
                tv_curmedi = vi.findViewById(R.id.tv_curmedi);
                tvt_pastmedi = vi.findViewById(R.id.tvt_pastmedi);
                tv_pastmedi = vi.findViewById(R.id.tv_pastmedi);
                tvt_labtest = vi.findViewById(R.id.tvt_labtest);
                tv_labtest = vi.findViewById(R.id.tv_labtest);

                tv_curmedi.setVisibility(View.GONE);
                tvt_curmedi.setVisibility(View.GONE);
                tv_labtest.setVisibility(View.GONE);
                tvt_labtest.setVisibility(View.GONE);
                tv_pastmedi.setVisibility(View.GONE);
                tvt_pastmedi.setVisibility(View.GONE);
                tv_prevhist.setVisibility(View.GONE);
                tvt_prevhist.setVisibility(View.GONE);
                tvt_morecomp.setVisibility(View.GONE);
                tv_morecomp.setVisibility(View.GONE);

                tv_query.setText("File Uploaded: " + (Model.upload_files));

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentTimeStamp = dateFormat.format(new Date());
                tv_datetime.setText(currentTimeStamp);

                myLayout.addView(vi);

                //-------- Auto Scroll to Bottom------------------
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                //-------- Auto Scroll to Bottom------------------

                Model.upload_files = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Model.upload_files = "";
    }

    public void onClick(View v) {

        try {

            TextView tv_filename = v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();
            String file_ext = tv_ext.getText().toString();
            String file_userid = tv_userid.getText().toString();

            System.out.println("str_filename-------" + file_name);

            //String url = Model.BASE_URL + "tools/downloadFile/user_id/" + (file_userid) + "/doctype/query_attachment?file=" + file_name + "&ext=" + file_ext + "&isapp=1";
            System.out.println("File url-------------" + file_name);

            String file_full__url = "";

            if ("?".contains(file_name)) {
                file_full__url = file_name + "&token=" + Model.token;
            } else {
                file_full__url = file_name + "?token=" + Model.token;
            }

            System.out.println("file_full__url-------------" + file_full__url);

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(file_full__url));
            startActivity(i);


/*            Intent i = new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse(file_name));
            i.setData(Uri.parse("http://docs.google.com/gview?embedded=true&url=" + file_name));
            startActivity(i);*/

/*            //---------------------- PDF Viewer --------------------------------------
            if (file_ext.equals("pdf")) {
                Intent intent = new Intent(HotlineChatViewActivity.this, WebviewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("file_ext", file_ext);
                startActivity(intent);

            } else {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            //---------------------- PDF Viewer --------------------------------------*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Model.screen_status = "false";
        //stoptimertask();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        System.out.println("Model.screen_status- onPause------------" + Model.screen_status);
    }

    public void startTimer() {
        try {
            timer = new Timer();
            initializeTimerTask();
            timer.schedule(timerTask, 3000, 3000); //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        try {
            timerTask = new TimerTask() {
                public void run() {

                    try {

                        handler.post(new Runnable() {
                            public void run() {

                                try {
                                    if ((Model.fcode) != null && !(Model.fcode).isEmpty() && !(Model.fcode).equals("null") && !(Model.fcode).equals("")) {
                                        System.out.println("Timer fcode-Received-----" + Model.fcode);
                                        add_received_text("" + (Model.push_msg));
                                    } else {
                                        System.out.println("Timer fcode------" + Model.fcode);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkRingerIsOn(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    public void force_logout() {

        try {

            final MaterialDialog alert = new MaterialDialog(HotlineChatViewActivity.this);
            alert.setTitle("Please Re-Login the App..!");
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
                    Intent i = new Intent(HotlineChatViewActivity.this, LoginActivity.class);
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

    public static void dumpIntent(Intent i) {

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println("Data------>" + "[" + key + "=" + bundle.get(key) + "]");
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Photo");
        mAnimals.add("Browse Files");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Files/Images");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Photo")) {

                    int permissionCheck = ContextCompat.checkSelfPermission(HotlineChatViewActivity.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(HotlineChatViewActivity.this, 0);
                    } else {
                        Nammu.askForPermission(HotlineChatViewActivity.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(HotlineChatViewActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(HotlineChatViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(HotlineChatViewActivity.this, 0);
                    } else {
                        Nammu.askForPermission(HotlineChatViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(HotlineChatViewActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {
                            }
                        });
                    }
                }
            }
        });
        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }


    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CAMERA_REQUEST:
                // ------------- Take Photo -------------------------------
                if (resultCode == RESULT_OK) {

                    Bitmap mphoto = (Bitmap) data.getExtras().get("data");

                    Uri tempUri = getImageUri(getApplicationContext(), mphoto);
                    selectedPath = getPath(tempUri);
                    selectedfilename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);

                    System.out.println("selectedPath-------" + selectedPath);
                    System.out.println("selectedfilename-------" + selectedfilename);
                    dumpIntent(data);

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.qid", (selqid));
                    articleParams.put("android.patient.attach_file_path", selectedPath);
                    articleParams.put("android.patient.attach_filename", selectedfilename);
                    FlurryAgent.logEvent("android.patient.Hotline_Take_Photo", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("attach_file_path", selectedPath);
                    params.putString("attach_filename", selectedfilename);
                    Model.mFirebaseAnalytics.logEvent("Hotline_Take_Photo", params);
                    //------------ Google firebase Analitics----------------------------------------------

                    Intent intent2 = new Intent(HotlineChatViewActivity.this, UploadToHotlineServer.class);
                    intent2.putExtra("KEY_path", selectedPath);
                    intent2.putExtra("KEY_filename", selectedfilename);
                    intent2.putExtra("selqid", selqid);
                    startActivity(intent2);
                }
                // ------------- Take Photo -------------------------------
                break;


            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Toast.makeText(HotlineChatViewActivity.this, "File Selected: " + path, Toast.LENGTH_LONG).show();

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.qid", selqid);
                            articleParams.put("android.patient.attach_file_path", path);
                            FlurryAgent.logEvent("android.patient.Browse_Images", articleParams);
                            //----------- Flurry -------------------------------------------------

                            //------------ Google firebase Analitics-----------------------------------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("attach_file_path", path);
                            Model.mFirebaseAnalytics.logEvent("Browse_Images", params);
                            //------------ Google firebase Analitics----------------------------------------------

                            Intent intent2 = new Intent(HotlineChatViewActivity.this, UploadToHotlineServer.class);
                            intent2.putExtra("KEY_path", path);
                            intent2.putExtra("KEY_filename", selectedfilename);
                            intent2.putExtra("selqid", selqid);
                            startActivity(intent2);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
                System.out.println("Selected file------------" + source.toString());

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(HotlineChatViewActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> returnedPhotos) {

        //photos.addAll(returnedPhotos);

        for (int i = 0; i < returnedPhotos.size(); i++) {
            System.out.println(returnedPhotos.get(i));

            System.out.println("File Name------------------" + (returnedPhotos.get(i)).getName());

            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();


            Intent intent2 = new Intent(HotlineChatViewActivity.this, UploadToHotlineServer.class);
            intent2.putExtra("KEY_path", selectedPath);
            intent2.putExtra("KEY_filename", selectedfilename);
            intent2.putExtra("selqid", selqid);
            startActivity(intent2);

            //new AsyncTask_fileupload().execute(selectedPath);

        }

    }

    public void onClickFile(View v) {

        try {
            TextView tv_filename = v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();
            System.out.println("str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
