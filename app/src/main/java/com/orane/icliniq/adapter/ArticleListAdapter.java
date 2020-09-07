package com.orane.icliniq.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class ArticleListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Typeface robo_reg, robo_bold;

    public ArticleListAdapter(Activity act, int resource, List<Item> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.items = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((items == null) || ((position + 1) > items.size()))
            return view;

        objBean = items.get(position);

        holder.imageview_poster = (ImageView) view.findViewById(R.id.imageview_poster);
        holder.img_qasebanner = (ImageView) view.findViewById(R.id.img_qasebanner);
        holder.article_title = (TextView) view.findViewById(R.id.article_title);
        holder.article_desc = (TextView) view.findViewById(R.id.article_desc);
        //holder.tv_speciality = (TextView) view.findViewById(R.id.tv_speciality);
        holder.tv_docname = (TextView) view.findViewById(R.id.tv_docname);
        holder.tv_url = (TextView) view.findViewById(R.id.tv_url);
        holder.tv_share_url = (TextView) view.findViewById(R.id.tv_share_url);
        holder.tv_docname_share = (TextView) view.findViewById(R.id.tv_docname_share);
        holder.tv_id = (TextView) view.findViewById(R.id.tv_id);

        //-------------------------------------------------------
        robo_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        robo_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.article_title.setTypeface(robo_bold);
        holder.article_desc.setTypeface(robo_reg);
//      holder.tv_speciality.setTypeface(robo_reg);
        holder.tv_docname.setTypeface(robo_reg);
        //holder.tv_url.setTypeface(robo_reg);
        //-------------------------------------------------------

        //--------------------Doc Image--------------------------------------------
        if (holder.imageview_poster != null && null != objBean.getArtimgurl() && objBean.getArtimgurl().trim().length() > 0) {
            holder.imageview_poster.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(objBean.getArtimgurl()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.doctor_icon).into(holder.imageview_poster);
        } else {
            holder.imageview_poster.setVisibility(View.GONE);
        }
        //--------------------Doc Image--------------------------------------------

        //----------------banner------------------------------------------------
        if (holder.img_qasebanner != null && null != objBean.getCdurl() && objBean.getCdurl().trim().length() > 0) {
            holder.img_qasebanner.setVisibility(View.VISIBLE);
            System.out.println("Adaptor-------" + objBean.getCdurl());

/*
            try {

                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                (holder.imageview_poster).setImageBitmap(bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }

*/
            Picasso.with(getContext()).load(objBean.getCdurl()).placeholder(R.mipmap.banner_palceholder).error(R.mipmap.banner_palceholder).into(holder.img_qasebanner);

            //Picasso.with(getContext()).load(objBean.getCdurl()).placeholder(R.mipmap.banner_palceholder).error(R.mipmap.banner_palceholder).into(holder.imageview_poster);
        } else {
            holder.img_qasebanner.setVisibility(View.GONE);
        }
        //--------------------banner--------------------------------------------


        if (holder.article_title != null && null != objBean.getArtTitle()
                && objBean.getArtTitle().trim().length() > 0) {
            holder.article_title.setText(Html.fromHtml(objBean.getArtTitle()));
        }

        if (holder.tv_id != null && null != objBean.getId()
                && objBean.getId().trim().length() > 0) {
            holder.tv_id.setText(Html.fromHtml(objBean.getId()));
        }

        if (holder.article_desc != null && null != objBean.getArtAbs()
                && objBean.getArtAbs().trim().length() > 0) {
            holder.article_desc.setText(Html.fromHtml(objBean.getArtAbs()));
        }


        if (holder.tv_docname != null && null != objBean.getArtDocname() && objBean.getArtDocname().trim().length() > 0) {
            holder.tv_docname.setText(Html.fromHtml(objBean.getArtDocname()));
            holder.tv_docname_share.setText(Html.fromHtml(objBean.getArtDocname()));
        }

/*
        if (holder.tv_speciality != null && null != objBean.getArtAbs()
                && objBean.getArtAbs().trim().length() > 0) {
            holder.tv_speciality.setText(Html.fromHtml(objBean.getArtAbs()));
        }
*/

        if (holder.tv_url != null && null != objBean.getArturl() && objBean.getArturl().trim().length() > 0) {
            holder.tv_url.setText(Html.fromHtml(objBean.getArturl()));
        }

        if (holder.tv_share_url != null && null != objBean.getSurl() && objBean.getSurl().trim().length() > 0) {
            holder.tv_share_url.setText(Html.fromHtml(objBean.getSurl()));
        }

        return view;
    }

    public class ViewHolder {

        public TextView article_title, article_desc, tv_speciality, tv_id, tv_docname_share, tv_share_url, tv_url, tv_docname;
        ImageView imageview_poster, img_qasebanner;
    }

}