package com.orane.icliniq.Parallex;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.orane.icliniq.Model.ItemCons;
import com.orane.icliniq.Parallex.libs.NotifyingScrollView;
import com.orane.icliniq.Parallex.libs.ScrollViewFragment;
import com.orane.icliniq.R;
import com.orane.icliniq.adapter.ConsHistoryRowAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;


public class ReviewFragment extends ScrollViewFragment {

    public static final String TAG = ReviewFragment.class.getSimpleName();

    public Menu mOptionsMenu;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    JSONObject jsonobj, jsonobj_makefav;
    public File imageFile;
    TextView opt_videocons, opt_phonecons, tv_subtitle, tv_queryfee, tv_constfee, tv_pfee, tv_vfee, tv_qfee, tvdocname, tvedu, tvspec;
    EditText edt_query;
    Button btn_submit;
    public String Share_text, fav_url, is_hotline, cons_type, is_fav, docurl, doc_photo_url, Doc_id, Docname, Docedu, Docspec, cfee, qfee;
    public String query_txt, qid;
    public LinearLayout full_layout, howitw_layout;
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
    Typeface font_reg, font_bold;

    ConsHistoryRowAdapter objAdapter;
    LinearLayout nolayout, netcheck_layout, article_layout;
    RelativeLayout like_layout, share_layout;
    ItemCons objItem;
    List<ItemCons> listArray;
    List<ItemCons> arrayOfList;
    RelativeLayout bottom_layout;
    ImageView art_img;
    TextView tv_likes, tv_titile, tv_tag;
    JSONArray articles_array;
    public String params, title, title_hash, photo_url, description;
    private ProgressBar progressBar;
    public String scroll_text, flash_text;
    ProgressBar progressBar_Bottom;
    View vi, view;
    Integer int_floor;
    NotifyingScrollView mScrollView;


    public static Fragment newInstance(int position) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ReviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);

        view = inflater.inflate(R.layout.docarticles_list, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);
        //setScrollViewOnScrollListener();

       /* //---------------------------------------------------------
        String url = Model.BASE_URL + "app/docArticles?user_id=" + Model.doctor_id;
        System.out.println("url-------" + url);
        //new JSON_DoctorProf().execute(url);
        //---------------------------------------------------------*/


        return view;
    }

   /* private class JSON_DoctorProf extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                System.out.println("urls[0]-----" + urls[0]);
                articles_array = jParser.getQueryNew(urls[0]);
                System.out.println("articles_array-----" + articles_array.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("articles_array.length()-------------------" + articles_array.length());

                if (articles_array.length() > 0) {

                    for (int i = 0; i < articles_array.length(); i++) {
                        try {
                            JSONObject jobj = articles_array.getJSONObject(i);
                            System.out.println("jobj-----" + jobj.toString());

                            title = jobj.getString("title");
                            title_hash = jobj.getString("title_hash");
                            photo_url = jobj.getString("photo_url");
                            description = jobj.getString("description");

                            System.out.println("title------" + title);
                            System.out.println("title_hash------" + title_hash);
                            System.out.println("photo_url------" + photo_url);
                            System.out.println("description------" + description);

                            vi = getActivity().getLayoutInflater().inflate(R.layout.doc_articles_row, null);
                            tv_titile = (TextView) vi.findViewById(R.id.tv_titile);
                            tv_likes = (TextView) vi.findViewById(R.id.tv_likes);
                            art_img = (ImageView) vi.findViewById(R.id.art_img);
                            like_layout = (RelativeLayout) vi.findViewById(R.id.like_layout);
                            share_layout = (RelativeLayout) vi.findViewById(R.id.share_layout);
                            bottom_layout = (RelativeLayout) vi.findViewById(R.id.bottom_layout);

                            bottom_layout.setVisibility(View.GONE);
                            //art_img.setVisibility(View.GONE);

*//*                            //---------------- Custom default Font --------------------
                            Typeface robo_regular = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
                            Typeface robo_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);

                            tv_tag.setTypeface(robo_regular);
                            //---------------- Custom default Font --------------------*//*

                            tv_titile.setText(title);

                            //Picasso.with(getActivity()).load(photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.doctor_icon).into(art_img);

                            article_layout.addView(vi);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
*//*                    progressBar.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.VISIBLE);
                    article_layout.setVisibility(View.GONE);
                    progressBar_Bottom.setVisibility(View.GONE);*//*
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }*/
}
