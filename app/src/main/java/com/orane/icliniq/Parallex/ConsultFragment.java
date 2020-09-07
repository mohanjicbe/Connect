package com.orane.icliniq.Parallex;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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
import com.orane.icliniq.Ask_FamilyProfile;
import com.orane.icliniq.Doc_Consultation1;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.libs.NotifyingScrollView;
import com.orane.icliniq.Parallex.libs.ScrollViewFragment;
import com.orane.icliniq.R;
import com.orane.icliniq.SignupActivity;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;


public class ConsultFragment extends ScrollViewFragment {

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
    public LinearLayout netcheck_layout, full_layout, howitw_layout, main_layout;
    public JSONObject docprofile_repose_jsonobj, jsonobj_postq, json, jsonobj_docprof;
    Toolbar toolbar;
    ScrollView scrollview, doc_layout;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    SharedPreferences sharedpreferences;
    public String str_response, uname, pass, Log_Status;
    RadioButton time_band1, time_band2, time_band3;
    LinearLayout track1, track2, track3, view_hl_plans;
    Button btn_hotlineplans;
    View view;
    Typeface font_reg, font_bold;
    LinearLayout bg_layout;

    public static Fragment newInstance(int position) {
        ConsultFragment fragment = new ConsultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ConsultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);

        view = inflater.inflate(R.layout.parallex_consult, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);
        setScrollViewOnScrollListener();


        try {
            Intent intent = getActivity().getIntent();
            Doc_id = intent.getStringExtra("tv_doc_id");

            System.out.println("Docid------------->" + Doc_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        scrollview = (ScrollView) view.findViewById(R.id.scrollview);
        tv_pfee = (TextView) view.findViewById(R.id.tv_pfee);
        tv_vfee = (TextView) view.findViewById(R.id.tv_vfee);
        tv_qfee = (TextView) view.findViewById(R.id.tv_qfee);
        edt_query = (EditText) view.findViewById(R.id.edt_query);
        tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        opt_phonecons = (TextView) view.findViewById(R.id.opt_phonecons);
        opt_videocons = (TextView) view.findViewById(R.id.opt_videocons);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        howitw_layout = (LinearLayout) view.findViewById(R.id.howitw_layout);
        bg_layout = (LinearLayout) view.findViewById(R.id.bg_layout);
        main_layout = (LinearLayout) view.findViewById(R.id.main_layout);

        font_reg = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);

        ((TextView) view.findViewById(R.id.tv_toptitle)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_toptdesc)).setTypeface(font_reg);
        //((EditText) view.findViewById(R.id.edt_query)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_subtitle)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_askqtit)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_askqsub)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_qfee)).setTypeface(font_reg);

        ((TextView) view.findViewById(R.id.opt_phonecons)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.opt_phonecons2)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_pfee)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.opt_videocons)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.opt_videocons2)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_vfee)).setTypeface(font_reg);
        ((Button) view.findViewById(R.id.btn_submit)).setTypeface(font_reg);

        ((TextView) view.findViewById(R.id.tv_askquery)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_sub_ask_query)).setTypeface(font_reg);


        if ((Model.browser_country) != null && !(Model.browser_country).isEmpty() && !(Model.browser_country).equals("null") && !(Model.browser_country).equals("") && !(Model.browser_country).equals("IN")) {
            tv_subtitle.setText(Model.DoctorProfile_subtitle);
            opt_phonecons.setText(Model.DoctorProfile_opttext1);
            opt_videocons.setText(Model.DoctorProfile_opttext2);
            ((TextView) view.findViewById(R.id.tv_sub_ask_query)).setText("Read the instruction of the Phone/Video chat");
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        cons_type = "1";

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");

        time_band1 = (RadioButton) view.findViewById(R.id.time_band1);
        time_band2 = (RadioButton) view.findViewById(R.id.time_band2);
        time_band3 = (RadioButton) view.findViewById(R.id.time_band3);
        track1 = (LinearLayout) view.findViewById(R.id.track1);
        track2 = (LinearLayout) view.findViewById(R.id.track2);
        track3 = (LinearLayout) view.findViewById(R.id.track3);

        howitw_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_tips();
            }
        });


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

        if (new NetCheck().netcheck(getActivity())) {
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
            Toast.makeText(getActivity(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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


                                Intent intent = new Intent(getActivity(), Ask_FamilyProfile.class);
                                intent.putExtra("qid", qid);
                                intent.putExtra("qid_text", query_txt);
                                intent.putExtra("Doc_id", Doc_id);
                                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                                    @Override
                                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                                        getActivity().finish();
                                    }
                                });
                                startActivityForResult(intent, 1);


                            } else {

                                Intent intent = new Intent(getActivity(), Doc_Consultation1.class);
                                intent.putExtra("Query", query_txt);
                                intent.putExtra("cons_type", cons_type);
                                intent.putExtra("Doc_id", Doc_id);

                                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                                    @Override
                                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                                        getActivity().finish();
                                    }
                                });
                                startActivityForResult(intent, 1);
                                Model.query_launch = "Doc_Consultation1";


                                //------------ Google firebase Analitics--------------------
                                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
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
                            edt_query.requestFocus();

                    } else {
                        System.out.println("Log_Status---Zero-----------" + Log_Status);
                        ask_login();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    public void post_query() {

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
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
            final MaterialDialog alert = new MaterialDialog(getActivity());
            alert.setTitle("You need to login..!");
            alert.setMessage("Please Signup/Login");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Login", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    getActivity().finish();
                }
            });


            alert.setNegativeButton("Signup", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((android.os.ResultReceiver) getActivity().getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent i = new Intent(getActivity(), SignupActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    getActivity().finish();
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
            dialog = new ProgressDialog(getActivity());
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
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
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

                    Intent intent = new Intent(getActivity(), Ask_FamilyProfile.class);
                    intent.putExtra("qid", qid);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            getActivity().finish();
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

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.show();
            dialog.setCancelable(false);

            bg_layout.setVisibility(View.VISIBLE);
            main_layout.setVisibility(View.GONE);
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

                        getActivity().finishAffinity();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }


                } else {

                    try {

                        cfee = docprofile_repose_jsonobj.getString("cfee");
                        qfee = docprofile_repose_jsonobj.getString("qfee");
                        docurl = docprofile_repose_jsonobj.getString("url");

                        tv_qfee.setText("Query Fee : " + qfee);
                        tv_pfee.setText("Consultation Fee : " + cfee);
                        tv_vfee.setText("Consultation Fee : " + cfee);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                bg_layout.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }

    public void show_tips() {

        final MaterialDialog alert = new MaterialDialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.howitworks_consult, null);
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


}
