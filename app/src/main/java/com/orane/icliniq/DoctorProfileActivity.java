package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;


public class DoctorProfileActivity extends AppCompatActivity {

    public Menu mOptionsMenu;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    JSONObject jsonobj, jsonobj_makefav;
    public File imageFile;
    CircleImageView imageview_poster;
    TextView opt_videocons, opt_phonecons, tv_subtitle, tv_queryfee, tv_constfee, tv_pfee, tv_vfee, tv_qfee, tvdocname, tvedu, tvspec;
    EditText edt_query;
    Button btn_submit;
    public String persona_id_val,Share_text, fav_url, is_hotline, cons_type, is_fav, docurl, doc_photo_url, Doc_id, Docname, Docedu, Docspec, cfee, qfee;
    public String query_txt, qid;
    public LinearLayout netcheck_layout, full_layout;
    public JSONObject jsonobj_postq, json, jsonobj_docprof;
    Toolbar toolbar;
    ScrollView scrollview, doc_layout;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    SharedPreferences sharedpreferences;
    public String str_response, uname, pass, Log_Status;
    RadioButton time_band1, time_band2, time_band3;
    LinearLayout track1, track2, track3, view_hl_plans;
    Button btn_hotlineplans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_new);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //------------ Object Creations --------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Doctor Profile");
        }

        FlurryAgent.onPageView();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //------------ Object Creations --------------------------------------------------

        try {
            Intent intent = getIntent();
            Doc_id = intent.getStringExtra("tv_doc_id");
            System.out.println("Docid------------->" + Doc_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        tv_constfee = (TextView) findViewById(R.id.tv_constfee);
        tv_queryfee = (TextView) findViewById(R.id.tv_queryfee);
        imageview_poster = (CircleImageView) findViewById(R.id.imageview_poster);
        tv_pfee = (TextView) findViewById(R.id.tv_pfee);
        tv_vfee = (TextView) findViewById(R.id.tv_vfee);
        tv_qfee = (TextView) findViewById(R.id.tv_qfee);
        tvdocname = (TextView) findViewById(R.id.tvdocname);
        tvedu = (TextView) findViewById(R.id.tvedu);
        tvspec = (TextView) findViewById(R.id.tvspec);
        edt_query = (EditText) findViewById(R.id.edt_query);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_hotlineplans = (Button) findViewById(R.id.btn_hotlineplans);
        view_hl_plans = (LinearLayout) findViewById(R.id.view_hl_plans);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        opt_phonecons = (TextView) findViewById(R.id.opt_phonecons);
        opt_videocons = (TextView) findViewById(R.id.opt_videocons);
        full_layout = (LinearLayout) findViewById(R.id.full_layout);
        doc_layout = (ScrollView) findViewById(R.id.doc_layout);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);

        if (!(Model.browser_country).equals("IN")) {
            tv_subtitle.setText(Model.DoctorProfile_subtitle);
            opt_phonecons.setText(Model.DoctorProfile_opttext1);
            opt_videocons.setText(Model.DoctorProfile_opttext2);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (new NetCheck().netcheck(DoctorProfileActivity.this)) {

            try {

                //--------------------------------
                String url = Model.BASE_URL + "sapp/doctor?user_id=" + (Model.id) + "&id=" + Doc_id + "&token=" + Model.token + "&enc=1";
                System.out.println("url-------" + url);
                new JSON_DoctorProf().execute(url);
                //--------------------------------
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            doc_layout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
        }

        cons_type = "1";

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");

        time_band1 = (RadioButton) findViewById(R.id.time_band1);
        time_band2 = (RadioButton) findViewById(R.id.time_band2);
        time_band3 = (RadioButton) findViewById(R.id.time_band3);

        track1 = (LinearLayout) findViewById(R.id.track1);
        track2 = (LinearLayout) findViewById(R.id.track2);
        track3 = (LinearLayout) findViewById(R.id.track3);

        track1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_band1.setChecked(true);
                cons_type = "1";
            }
        });
        track2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_band2.setChecked(true);
                cons_type = "4";
            }
        });
        track3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_band3.setChecked(true);
                cons_type = "0";
            }
        });

        time_band1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //---------------------------------------------
                if (time_band1.isChecked()) {
                    time_band2.setChecked(false);
                    time_band3.setChecked(false);
                    cons_type = "1";
                }
                //---------------------------------------------
            }
        });

        time_band2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //---------------------------------------------
                if (time_band2.isChecked()) {
                    time_band1.setChecked(false);
                    time_band3.setChecked(false);
                    cons_type = "4";
                }
                //---------------------------------------------
            }
        });

        time_band3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //---------------------------------------------
                if (time_band3.isChecked()) {
                    time_band1.setChecked(false);
                    time_band2.setChecked(false);
                    cons_type = "0";
                }
                //---------------------------------------------
            }
        });

        btn_hotlineplans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorProfileActivity.this, HotlinePackagesActivity.class);
                intent.putExtra("Doctor_id", Doc_id);
                intent.putExtra("Doctor_name", Docname);
                intent.putExtra("tv_docurl", doc_photo_url);
                startActivity(intent);
            }
        });

        view_hl_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorProfileActivity.this, HotlinePackagesActivity.class);
                intent.putExtra("Doctor_id", Doc_id);
                intent.putExtra("Doctor_name", Docname);
                intent.putExtra("tv_docurl", doc_photo_url);
                startActivity(intent);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                Log_Status = sharedpreferences.getString(Login_Status, "");

                try {

                    if (Log_Status.equals("1")) {
                        query_txt = edt_query.getText().toString();

                        if (Doc_id != null && !Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                            System.out.println("Docid is Valid---------------------------");
                        } else {
                            Doc_id = "0";
                        }

                        System.out.println("query_txt-----------" + query_txt);
                        System.out.println("Doc_id-----------" + Doc_id);
                        System.out.println("cons_type-----------" + cons_type);

                        if ((query_txt.length()) > 0) {

                            if (cons_type.equals("1")) {
                                post_query();
                            } else {

                                Intent intent = new Intent(DoctorProfileActivity.this, Doc_Consultation1.class);
                                intent.putExtra("Query", query_txt);
                                intent.putExtra("cons_type", cons_type);
                                intent.putExtra("Doc_id", Doc_id);

                                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                                    @Override
                                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                                        DoctorProfileActivity.this.finish();
                                    }
                                });
                                startActivityForResult(intent, 1);

                                Model.query_launch = "Doc_Consultation1";
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            }

                        } else

                            edt_query.setError("Please enter your query");

                    } else {
                        System.out.println("Log_Status---Zero---" + Log_Status);
                        ask_login();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setPalette() {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = getResources().getColor(R.color.app_color);
                int primary = getResources().getColor(R.color.primary);
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
            }
        });
    }

    public void ask_login() {

        final MaterialDialog alert = new MaterialDialog(DoctorProfileActivity.this);
        alert.setTitle("You need to login..!");
        alert.setMessage("Please Login or Signup");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Login", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishAffinity();
                Intent i = new Intent(DoctorProfileActivity.this, LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });


        alert.setNegativeButton("Signup", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                Intent i = new Intent(DoctorProfileActivity.this, SignupActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });

        alert.show();
    }


    private class JSON_DoctorProf extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(DoctorProfileActivity.this);
            dialog.setMessage("Please wait...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

/*              JSONParser jParser = new JSONParser();
                System.out.println("urls[0]-----" + urls[0]);
                jsonobj_docprof = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_docprof-----" + jsonobj_docprof.toString());  */

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                //scrollview.setVisibility(View.VISIBLE);

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
                        Intent intent = new Intent(DoctorProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    Doc_id = jsonobj_docprof.getString("id");
                    Docname = jsonobj_docprof.getString("name");
                    doc_photo_url = jsonobj_docprof.getString("photo_url");
                    Docedu = jsonobj_docprof.getString("edu");
                    Docspec = jsonobj_docprof.getString("speciality");
                    cfee = jsonobj_docprof.getString("cfee");
                    qfee = jsonobj_docprof.getString("qfee");
                    docurl = jsonobj_docprof.getString("url");

                    System.out.println("Doc_id----" + Doc_id);
                    System.out.println("Docname----" + Docname);
                    System.out.println("doc_photo_url----" + doc_photo_url);
                    System.out.println("Docedu----" + Docedu);
                    System.out.println("Docspec----" + Docspec);
                    System.out.println("cfee----" + cfee);
                    System.out.println("qfee----" + qfee);
                    System.out.println("docurl----" + docurl);

                    //----------------------Show Hotline Plans----------------------------
                    if (jsonobj_docprof.has("has_hline")) {
                        System.out.println("hline is ther----");
                        is_hotline = jsonobj_docprof.getString("has_hline");

                        if (is_hotline.equals("1")) {
                            view_hl_plans.setVisibility(View.VISIBLE);
                        } else {
                            view_hl_plans.setVisibility(View.GONE);
                        }
                    }
                    //----------------------Show Hotline Plans ----------------------------

                    if (jsonobj_docprof.has("is_fav")) {
                        is_fav = jsonobj_docprof.getString("is_fav");

                        System.out.println("is_fav==============>" + is_fav);

                        //-----------------------------
                        if (is_fav.equals("1")) {
                            mOptionsMenu.findItem(R.id.nav_fav).setIcon(R.mipmap.fav_full);
                        } else {
                            mOptionsMenu.findItem(R.id.nav_fav).setIcon(R.mipmap.fav_empty);
                        }
                        //-----------------------------

                    } else {
                        is_fav = "0";
                        System.out.println("is_fav===NO===========");
                    }

                    Picasso.with(getApplicationContext()).load(doc_photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(imageview_poster);

                    tv_queryfee.setText("Query Fee: " + qfee);
                    tv_constfee.setText("Phone/Video Fee: " + cfee);
                    tv_qfee.setText("(" + qfee + ")");
                    tv_pfee.setText("(" + cfee + ")");
                    tv_vfee.setText("(" + cfee + ")");
                    tvedu.setText(Docedu);

                    tvdocname.setText(Docname);
                    tvedu.setText(Docedu);
                    tvspec.setText(Docspec);

                    System.out.println("Reach----------------------");

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("doctor_id", Doc_id);
                    articleParams.put("doctor_name", Docname);
                    FlurryAgent.logEvent("android.patient.Doctor_Profile", articleParams);
                    //----------- Flurry -------------------------------------------------
                }

            } catch (Exception e) {
                System.out.println("Exception--Doctor_Profile---" + e.toString());
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.docprofile_menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_fav) {

            System.out.println("is_fav----" + is_fav);

            if (is_fav != null && !is_fav.isEmpty() && !is_fav.equals("null") && !is_fav.equals("")) {
                if (is_fav.equals("1")) {
                    mOptionsMenu.findItem(R.id.nav_fav).setIcon(R.mipmap.fav_empty);
                    //------------------------------------
                    fav_url = Model.BASE_URL + "/sapp/add2fav?user_id=" + (Model.id) + "&item_type_id=1&remove=1&item_id=" + Doc_id + "&token=" + Model.token + "&enc=1";
                    System.out.println("Remove Favoueite url-------------" + fav_url);
                    //------------------------------------
                    is_fav = "0";

                } else {
                    mOptionsMenu.findItem(R.id.nav_fav).setIcon(R.mipmap.fav_full);
                    //------------------------------------
                    fav_url = Model.BASE_URL + "/sapp/add2fav?user_id=" + (Model.id) + "&item_type_id=1&item_id=" + Doc_id + "&token=" + Model.token + "&enc=1";
                    System.out.println("Favoueite url-------------" + fav_url);
                    //------------------------------------
                    is_fav = "1";
                }

                new JSON_make_favupdate().execute(fav_url);
            }
        }


        if (id == R.id.nav_qashare) {

            System.out.println("qtitle_textval-------------" + Docname);
            System.out.println("qaurl-------------" + docurl);

            TakeScreenshot_Share();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class JSON_make_favupdate extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                jsonobj_makefav = new JSONObject(str_response);

                if (jsonobj_makefav.has("token_status")) {
                    String token_status = jsonobj_makefav.getString("token_status");
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

                    String Status_val = jsonobj_makefav.getString("status");
                    System.out.println("Status_val-------------" + Status_val);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // dialog.dismiss();
        }
    }

    public void post_query() {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String Log_Status = sharedpreferences.getString(Login_Status, "");

        Model.query_cache = edt_query.getText().toString();

        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("") && !(Model.id).equals("null")) {
            try {

                //String query_str = edt_query.getText().toString();
                query_txt = edt_query.getText().toString();


                if ((query_txt.length()) > 0) {

                    //query_txt = URLEncoder.encode((edt_query.getText().toString()), "UTF-8");

                    json = new JSONObject();
                    json.put("query", query_txt);
                    json.put("speciality", "0");
                    json.put("doctor_id", Doc_id);
                    json.put("pqid", "0");
                    json.put("qid", "0");

                    System.out.println("Query post json----" + json.toString());

                    if (Log_Status.equals("1")) {
                        new JSONPostQueryOnly().execute(json);
                    } else {
                        ask_login();
                    }
                } else
                    edt_query.setError("Please enter your query");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            force_logout();
        }
    }

    class JSONPostQueryOnly extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DoctorProfileActivity.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj_postq = jParser.JSON_POST(urls[0], "PostQuery");

                System.out.println("DoctorProfile Post Query---------------" + urls[0]);
                System.out.println("DoctorProfile jsonobj_postq---------------" + jsonobj_postq.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                if (jsonobj_postq.has("token_status")) {
                    String token_status = jsonobj_postq.getString("token_status");

                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(DoctorProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    qid = jsonobj_postq.getString("qid");

                    //------------ persona_id---------------------
                    if (jsonobj.has("persona_id")) {
                        persona_id_val = jsonobj.getString("persona_id");
                    } else {
                        persona_id_val = "0";
                    }
                    //------------ persona_id---------------------

                    System.out.println("Response.qid--------------->" + qid);

                    // Model.upload_files = "";
                    Model.compmore = "";
                    Model.prevhist = "";
                    Model.curmedi = "";
                    Model.pastmedi = "";
                    Model.labtest = "";

                    Intent intent = new Intent(DoctorProfileActivity.this, AskQuery2.class);
                    intent.putExtra("qid", qid);
                    intent.putExtra("persona_id", persona_id_val);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            DoctorProfileActivity.this.finish();
                        }
                    });
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void force_logout() {

        final MaterialDialog alert = new MaterialDialog(DoctorProfileActivity.this);
        alert.setTitle("Please re-login the App..!");
        alert.setMessage("Something went wrong. Please go back and try again..!e");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                finishAffinity();
                Intent i = new Intent(DoctorProfileActivity.this, LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });
        alert.show();

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

            if (!(Model.browser_country).equals("IN")) {
                Share_text = Model.DoctorProfile_share_text1 + Docname + " online at iCliniq. \n\n" + docurl;
            } else {
                Share_text = "It is quick to consult doctor " + Docname + " online at iCliniq. \n\n" + docurl;
            }

            shareIntent.putExtra(Intent.EXTRA_TEXT, Share_text);
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
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "It is quick to consult doctor " + Docname + " online at iCliniq. \n\n" + docurl);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

        }
    }

}
