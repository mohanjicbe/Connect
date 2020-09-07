package com.orane.icliniq.Parallex;


import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orane.icliniq.Model.ItemCons;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.libs.NotifyingScrollView;
import com.orane.icliniq.Parallex.libs.ScrollViewFragment;
import com.orane.icliniq.R;
import com.orane.icliniq.adapter.ConsHistoryRowAdapter;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ArticlesFragment extends ScrollViewFragment {

    ConsHistoryRowAdapter objAdapter;
    LinearLayout nolayout, netcheck_layout, article_layout;
    RelativeLayout like_layout, share_layout;
    RelativeLayout bottom_layout;
    ImageView art_img;
    TextView tv_likes, tv_titile, tv_tag;
    JSONArray articles_array;
    public String params, title, title_hash, photo_url, description;
    View vi, view;
    NotifyingScrollView mScrollView;

    public static final String TAG = ArticlesFragment.class.getSimpleName();

    public static Fragment newInstance(int position) {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ArticlesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        view = inflater.inflate(R.layout.parallex_review_list, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);
        setScrollViewOnScrollListener();

        //article_layout = (LinearLayout) view.findViewById(R.id.article_layout);

/*      progressBar_Bottom = (ProgressBar) view.findViewById(R.id.progressBar_bottom);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        netcheck_layout = (LinearLayout) view.findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) view.findViewById(R.id.nolayout);*/

        try {
            full_process();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setAdapter();
        //setListViewOnScrollListener();

        return view;
    }


    public void full_process() {

        try {

            if (isInternetOn()) {

                //---------------- Get Doctor Articles -----------------------------------------
                String url = Model.BASE_URL + "app/docArticles?user_id=" + Model.doctor_id;
                System.out.println("url-------" + url);
                new JSON_DoctorProf().execute(url);
                //----------------- Get Doctor Articles ----------------------------------------

            } else {
/*                progressBar.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.VISIBLE);
                nolayout.setVisibility(View.GONE);
                article_layout.setVisibility(View.GONE);
                progressBar_Bottom.setVisibility(View.GONE);*/
            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    private class JSON_DoctorProf extends AsyncTask<String, Void, Boolean> {

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

/*                            //---------------- Custom default Font --------------------
                            Typeface robo_regular = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);

                            Typeface robo_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);

                            tv_tag.setTypeface(robo_regular);
                            //---------------- Custom default Font --------------------*/

                            tv_titile.setText(title);

                            //Picasso.with(getActivity()).load(photo_url).placeholder(R.mipmap.doctor_icon).error(R.mipmap.doctor_icon).into(art_img);

                            article_layout.addView(vi);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
/*                  progressBar.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.VISIBLE);
                    article_layout.setVisibility(View.GONE);
                    progressBar_Bottom.setVisibility(View.GONE);*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
