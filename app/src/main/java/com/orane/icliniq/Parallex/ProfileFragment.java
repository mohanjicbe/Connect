package com.orane.icliniq.Parallex;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.libs.NotifyingScrollView;
import com.orane.icliniq.Parallex.libs.ScrollViewFragment;
import com.orane.icliniq.R;
import com.orane.icliniq.Video_WebViewActivity;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONObject;

public class ProfileFragment extends ScrollViewFragment {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    View view;
    TextView tv_imspec, tv_abt_text, tv_imspec_text, tv_spectreat, tv_spectreat_text, tv_mylang,
            tv_mylang_text, tv_perscons, tv_perscons_text, tv_clinics_tit, tv_clinics_desc;

    public JSONObject jsonobj_docprof, json;
    public String clinic_text, doc_hash_text, Docspec, is_video_text, about_text, language, treatment_skills, clinics;
    FrameLayout video_layout;
    boolean is_expanded = false;
    LinearLayout abt_layout;

    public static Fragment newInstance(int position) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);

        view = inflater.inflate(R.layout.parallex_profile, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);
        setScrollViewOnScrollListener();

        tv_abt_text = (TextView) view.findViewById(R.id.tv_abt_text);
        tv_imspec = (TextView) view.findViewById(R.id.tv_imspec);
        tv_imspec_text = (TextView) view.findViewById(R.id.tv_imspec_text);
        tv_spectreat = (TextView) view.findViewById(R.id.tv_spectreat);
        tv_spectreat_text = (TextView) view.findViewById(R.id.tv_spectreat_text);
        tv_mylang = (TextView) view.findViewById(R.id.tv_mylang);
        tv_mylang_text = (TextView) view.findViewById(R.id.tv_mylang_text);
        tv_perscons = (TextView) view.findViewById(R.id.tv_perscons);
        tv_perscons_text = (TextView) view.findViewById(R.id.tv_perscons_text);
        tv_clinics_tit = (TextView) view.findViewById(R.id.tv_clinics_tit);
        tv_clinics_desc = (TextView) view.findViewById(R.id.tv_clinics_desc);
        video_layout = (FrameLayout) view.findViewById(R.id.video_layout);
        abt_layout = (LinearLayout) view.findViewById(R.id.abt_layout);

        Typeface robo_med = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);
        Typeface robo_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);

        tv_imspec.setTypeface(robo_med);
        tv_imspec_text.setTypeface(robo_reg);
        tv_spectreat.setTypeface(robo_med);
        tv_spectreat_text.setTypeface(robo_reg);
        tv_mylang.setTypeface(robo_med);
        tv_mylang_text.setTypeface(robo_reg);
        tv_perscons.setTypeface(robo_med);
        tv_perscons_text.setTypeface(robo_reg);
        tv_clinics_tit.setTypeface(robo_med);
        tv_clinics_desc.setTypeface(robo_reg);
        tv_abt_text.setTypeface(robo_reg);

        System.out.println("Model.doc_sp---------" + Model.doc_sp);
        System.out.println("Model.treatment_skills---------" + Model.treatment_skills);
        System.out.println("Model.doc_lang---------" + Model.doc_lang);
        System.out.println("Model.clinics---------" + Model.clinics);
        System.out.println("Model.doc_sp---------" + Model.doc_sp);

        //---------------------------------------------------------
        String url = Model.BASE_URL + "sapp/doctor?user_id=" + (Model.id) + "&id=" + (Model.doctor_id) + "&token=" + Model.token;
        System.out.println("doctor url-------" + url);
        new JSON_DoctorProf().execute(url);
        //---------------------------------------------------------

        tv_abt_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_expanded) {
                    tv_abt_text.setMaxLines(4);
                    tv_abt_text.setEllipsize(TextUtils.TruncateAt.END);
                    is_expanded = false;
                } else {
                    tv_abt_text.setMaxLines(2000);
                    is_expanded = true;

                }
            }
        });

        video_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Video_WebViewActivity.class);
                i.putExtra("url", Model.BASE_URL + "video/" + doc_hash_text + "?t=mob&layout=empty&user_id=" + Model.id + "&token=" + Model.token);
                i.putExtra("type", "Video Gallery");
                startActivity(i);
            }
        });

        return view;
    }


    private class JSON_DoctorProf extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();

                System.out.println("urls[0]-----" + urls[0]);
                jsonobj_docprof = jParser.getJSONFromUrl(urls[0]);

                System.out.println("jsonobj_docprof-----" + jsonobj_docprof.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                Docspec = jsonobj_docprof.getString("speciality");
                language = jsonobj_docprof.getString("language");
                treatment_skills = jsonobj_docprof.getString("treatment_skills");
                clinics = jsonobj_docprof.getString("clinics");

                if (jsonobj_docprof.has("is_video")) {
                    is_video_text = jsonobj_docprof.getString("is_video");
                } else {
                    is_video_text = "false";
                }

                about_text = jsonobj_docprof.getString("about");
                doc_hash_text = jsonobj_docprof.getString("doc_hash");

                System.out.println("Docspec------" + Docspec);
                System.out.println("language------" + language);
                System.out.println("treatment_skills------" + treatment_skills);
                System.out.println("clinics------" + clinics);
                System.out.println("is_video_text------" + is_video_text);
                System.out.println("about_text------" + about_text);

                //--------------------------------------------
                if (Docspec != null && !Docspec.isEmpty() && !Docspec.equals("null") && !Docspec.equals("")) {
                    tv_imspec_text.setText(Docspec);
                } else {
                    tv_imspec_text.setText("--- ");
                }
                //--------------------------------------------
                if (language != null && !language.isEmpty() && !language.equals("null") && !language.equals("")) {
                    tv_mylang_text.setText(language);
                } else {
                    tv_mylang_text.setText("---");
                }
                //--------------------------------------------
                if (treatment_skills != null && !treatment_skills.isEmpty() && !treatment_skills.equals("null") && !treatment_skills.equals("")) {
                    tv_spectreat_text.setText(treatment_skills);
                } else {
                    tv_spectreat_text.setText("---");
                }
                //--------------------------------------------

                if (about_text != null && !about_text.isEmpty() && !about_text.equals("null") && !about_text.equals("")) {
                    tv_abt_text.setText(Html.fromHtml(about_text));
                } else {
                    abt_layout.setVisibility(View.GONE);
                    //tv_abt_text.setText("--- No content ---");
                }


                if (is_video_text.equals("true")) {
                    video_layout.setVisibility(View.VISIBLE);
                } else {
                    video_layout.setVisibility(View.GONE);
                }

                //----------- Clinic Parsing ----------------------------------------
                clinic_text = "";

                String str = clinics.replaceAll("\\[", "").replaceAll("\\]", "");
                String[] splitterString = str.split("\"");

                for (String s : splitterString) {
                    System.out.println("s======================" + s);

                    if (s != null && !s.isEmpty() && !s.equals("null") && !s.equals(",") && !s.equals("") && !s.equals("[\"\"]")) {
                        if (clinic_text.equals("")) {
                            clinic_text = s;
                        } else {
                            clinic_text = clinic_text + "\n\n" + s;
                        }
                    }
                }

                System.out.println("clinic_text============" + clinic_text);
                //----------- Clinic Parsing ----------------------------------------

                //tv_clinics_desc.setText(Html.fromHtml(clinic_text));

                if (clinic_text != null && !clinic_text.isEmpty() && !clinic_text.equals("null") && !clinic_text.equals(",") && !clinic_text.equals("") && !clinic_text.equals("[\"\"]")) {
                    tv_clinics_desc.setText(clinic_text);
                } else {
                    tv_clinics_desc.setText("---");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
