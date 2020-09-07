package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
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

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ConsultFragment;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;


public class Doc_Consultation_Screen extends AppCompatActivity {

    public static final String TAG = ConsultFragment.class.getSimpleName();

    public Menu mOptionsMenu;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    JSONObject jsonobj, jsonobj_makefav;
    public File imageFile;
    CircleImageView imageview_poster;
    TextView opt_videocons, opt_phonecons, tv_subtitle, tv_queryfee, tv_constfee, tv_pfee, tv_vfee, tv_qfee, tvdocname, tvedu, tvspec;
    EditText edt_query;
    Button btn_submit;
    public String Share_text, fav_url, is_hotline, cons_type, is_fav, docurl, doc_photo_url, Doc_id, Docname, Docedu, Docspec, cfee, qfee;
    public String query_txt, qid;
    public LinearLayout netcheck_layout, full_layout, howitw_layout;
    public JSONObject docprofile_repose_jsonobj, jsonobj_postq, json, jsonobj_docprof;
    Toolbar toolbar;
    ScrollView doc_layout;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    SharedPreferences sharedpreferences;
    public String str_response, uname, pass, Log_Status;
    RadioButton time_band1, time_band2, time_band3;
    LinearLayout track1, track2, track3, view_hl_plans;
    Button btn_hotlineplans;
    View view;
    Typeface font_reg, font_bold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_consult_screen);

        //--------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Book a Consultation");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //----------- initialize --------------------------------------

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);


        try {
            Intent intent = getIntent();
            Doc_id = intent.getStringExtra("tv_doc_id");

            System.out.println("Docid------------->" + Doc_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tv_pfee = (TextView) findViewById(R.id.tv_pfee);
        tv_vfee = (TextView) findViewById(R.id.tv_vfee);
        tv_qfee = (TextView) findViewById(R.id.tv_qfee);
        edt_query = (EditText) findViewById(R.id.edt_query);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        opt_phonecons = (TextView) findViewById(R.id.opt_phonecons);
        opt_videocons = (TextView) findViewById(R.id.opt_videocons);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        howitw_layout = (LinearLayout) findViewById(R.id.howitw_layout);

        font_reg = Typeface.createFromAsset(Doc_Consultation_Screen.this.getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(Doc_Consultation_Screen.this.getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_toptitle)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_toptdesc)).setTypeface(font_reg);
        //((EditText) findViewById(R.id.edt_query)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_subtitle)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_askqtit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_askqsub)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_qfee)).setTypeface(font_reg);

        ((TextView) findViewById(R.id.opt_phonecons)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.opt_phonecons2)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_pfee)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.opt_videocons)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.opt_videocons2)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_vfee)).setTypeface(font_reg);
        ((Button) findViewById(R.id.btn_submit)).setTypeface(font_reg);

        ((TextView) findViewById(R.id.tv_askquery)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_sub_ask_query)).setTypeface(font_reg);


        if ((Model.browser_country) != null && !(Model.browser_country).isEmpty() && !(Model.browser_country).equals("null") && !(Model.browser_country).equals("") && !(Model.browser_country).equals("IN")) {
            tv_subtitle.setText(Model.DoctorProfile_subtitle);
            opt_phonecons.setText(Model.DoctorProfile_opttext1);
            opt_videocons.setText(Model.DoctorProfile_opttext2);
            ((TextView) findViewById(R.id.tv_sub_ask_query)).setText("Read the instruction of the Phone/Video chat");
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        cons_type = "2";

        sharedpreferences = Doc_Consultation_Screen.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");

        time_band1 = (RadioButton) findViewById(R.id.time_band1);
        time_band2 = (RadioButton) findViewById(R.id.time_band2);
        time_band3 = (RadioButton) findViewById(R.id.time_band3);
        track1 = (LinearLayout) findViewById(R.id.track1);
        track2 = (LinearLayout) findViewById(R.id.track2);
        track3 = (LinearLayout) findViewById(R.id.track3);

        howitw_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_tips();
            }
        });


    /*    track1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_band1.setChecked(true);
                cons_type = "1";
            }
        });*/
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

        if (new NetCheck().netcheck(Doc_Consultation_Screen.this)) {
            try {
                //--------------------------------------------------
                String url = Model.BASE_URL + "sapp/doctor?user_id=" + (Model.id) + "&id=" + Doc_id + "&token=" + Model.token;
                System.out.println("url-------" + url);
                new JSON_DoctorProf().execute(url);
                //--------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Doc_Consultation_Screen.this, "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                Log_Status = sharedpreferences.getString(Login_Status, "");

                try {

                    if (Log_Status.equals("1")) {
                        query_txt = edt_query.getText().toString();

                        if (Doc_id != null && !Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                        } else {
                            Doc_id = "0";
                        }

                        System.out.println("query_txt-----------" + query_txt);
                        System.out.println("Doc_id-----------" + Doc_id);
                        System.out.println("cons_type-----------" + cons_type);

                        if ((query_txt.length()) > 0) {

                            if (cons_type.equals("1")) {
                                //post_query();


                                Intent intent = new Intent(Doc_Consultation_Screen.this, Ask_FamilyProfile.class);
                                intent.putExtra("qid", qid);
                                intent.putExtra("qid_text", query_txt);
                                intent.putExtra("Doc_id", Doc_id);
                                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                                    @Override
                                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                                        finish();
                                    }
                                });
                                startActivityForResult(intent, 1);


                            } else {

                                Intent intent = new Intent(Doc_Consultation_Screen.this, Doc_Consultation1.class);
                                intent.putExtra("Query", query_txt);
                                intent.putExtra("cons_type", cons_type);
                                intent.putExtra("Doc_id", Doc_id);

                                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                                    @Override
                                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                                        finish();
                                    }
                                });
                                startActivityForResult(intent, 1);
                                Model.query_launch = "Doc_Consultation1";


                                //------------ Google firebase Analitics--------------------
                                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(Doc_Consultation_Screen.this);
                                Bundle params = new Bundle();
                                params.putString("User", Model.id);
                                params.putString("query", query_txt);
                                params.putString("doctor_id", Doc_id);
                                params.putString("cons_type", cons_type);
                                Model.mFirebaseAnalytics.logEvent("Doctor_Profile_Post_Consultation", params);
                                //------------ Google firebase Analitics--------------------


                            }

                        } else
                            edt_query.setError("Please enter your query");

                    } else {
                        System.out.println("Log_Status---Zero-----------" + Log_Status);
                        ask_login();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public void post_query() {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");

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

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(Doc_Consultation_Screen.this);
                        Bundle params = new Bundle();
                        params.putString("User", Model.id);
                        params.putString("query", query_txt);
                        params.putString("doctor_id", Doc_id);
                        Model.mFirebaseAnalytics.logEvent("Doctor_Profile_Post_Query", params);
                        //------------ Google firebase Analitics--------------------


                    } else {
                        ask_login();
                    }
                } else
                    edt_query.setError("Please enter your query");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ask_login() {

        try {
            final MaterialDialog alert = new MaterialDialog(Doc_Consultation_Screen.this);
            alert.setTitle("You need to login..!");
            alert.setMessage("Please Signup/Login");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Login", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(Doc_Consultation_Screen.this, LoginActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    finish();
                }
            });


            alert.setNegativeButton("Signup", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent i = new Intent(Doc_Consultation_Screen.this, SignupActivity.class);
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


    private class JSONPostQueryOnly extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Doc_Consultation_Screen.this);
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
                        Intent intent = new Intent(Doc_Consultation_Screen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    qid = jsonobj_postq.getString("qid");

                    System.out.println("Response.qid--------------->" + qid);

                    // Model.upload_files = "";
                    Model.compmore = "";
                    Model.prevhist = "";
                    Model.curmedi = "";
                    Model.pastmedi = "";
                    Model.labtest = "";

/*                    Intent intent = new Intent(getActivity(), AskQuery2.class);
                    intent.putExtra("qid", qid);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            getActivity().finish();
                        }
                    });
                    startActivityForResult(intent, 1);*/

                    Intent intent = new Intent(Doc_Consultation_Screen.this, Ask_FamilyProfile.class);
                    intent.putExtra("qid", qid);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            finish();
                        }
                    });
                    startActivityForResult(intent, 1);


                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class JSON_DoctorProf extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Doc_Consultation_Screen.this);
            dialog.setMessage("Please wait...");
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

                docprofile_repose_jsonobj = new JSONObject(str_response);
                System.out.println("docprofile_repose_jsonobj-------" + docprofile_repose_jsonobj.toString());

                if (docprofile_repose_jsonobj.has("token_status")) {
                    String token_status = docprofile_repose_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(Doc_Consultation_Screen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }


                } else {

                    try {

                        cfee = docprofile_repose_jsonobj.getString("cfee");
                        qfee = docprofile_repose_jsonobj.getString("qfee");
                        docurl = docprofile_repose_jsonobj.getString("url");

                        tv_qfee.setText(qfee + " Query Fee");
                        tv_pfee.setText(cfee + " Consultation Fee");
                        tv_vfee.setText(cfee + " Consultation Fee");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }

    public void show_tips() {

        final MaterialDialog alert = new MaterialDialog(Doc_Consultation_Screen.this);
        View view = LayoutInflater.from(Doc_Consultation_Screen.this).inflate(R.layout.howitworks_consult, null);
        alert.setView(view);
        alert.setTitle("How it Works?");
        alert.setCanceledOnTouchOutside(false);

        TextView tvaabtq = (TextView) view.findViewById(R.id.tvaabtq);
        TextView tvappverview = (TextView) view.findViewById(R.id.tvappverview);

        tvaabtq.setTypeface(font_reg);
        tvappverview.setTypeface(font_bold);

        tvaabtq.setText(Html.fromHtml(getString(R.string.howitworks)));

        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
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

}
