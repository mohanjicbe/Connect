package com.orane.icliniq.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorsRowAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;
    Typeface font_reg, font_bold;

    public DoctorsRowAdapter(Activity act, int resource, List<Item> arrayList) {
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
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);

            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((items == null) || ((position + 1) > items.size()))
            return view;

        objBean = items.get(position);


        holder.tv_spec_new = view.findViewById(R.id.tv_spec_new);
        holder.tvdocname_new = view.findViewById(R.id.tvdocname_new);
        holder.tvcfee = view.findViewById(R.id.tvcfee);
        holder.tvqfee = view.findViewById(R.id.tvqfee);
        holder.tvdocname = view.findViewById(R.id.tvdocname);
        holder.tvedu = view.findViewById(R.id.tvedu);
        holder.tvspec = view.findViewById(R.id.tvspec);
        holder.tvid = view.findViewById(R.id.tvid);
        holder.tv_url = view.findViewById(R.id.tv_url);
        holder.tv_doclink = view.findViewById(R.id.tv_doclink);
        holder.imageview_poster = view.findViewById(R.id.imageview_poster);
        holder.img_fav_ribbon = view.findViewById(R.id.img_fav_ribbon);
        holder.tv_star_text = view.findViewById(R.id.tv_star_text);
        holder.ratingBar = view.findViewById(R.id.ratingBar);


        //----------------Ratting------------------------------------------------
        if (holder.ratingBar != null && null != objBean.getAmt() && objBean.getAmt().trim().length() > 0) {

            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.tv_star_text.setVisibility(View.VISIBLE);

            holder.ratingBar.setRating(Float.parseFloat(objBean.getAmt()));
            System.out.println("Inner Ratting Value.........." + objBean.getDocname() + "----" + objBean.getAmt());
        } else {
            holder.ratingBar.setVisibility(View.GONE);
            holder.tv_star_text.setVisibility(View.GONE);
        }
        //----------------------------------------------------------------
        System.out.println("Outer Ratting Value.........." + objBean.getDocname() + "----" + objBean.getAmt());

        //----------------------------------------------------------------
        if (holder.tv_star_text != null && null != objBean.getArtTitle()
                && objBean.getArtTitle().trim().length() > 0) {
            holder.tv_star_text.setVisibility(View.GONE);
            holder.tv_star_text.setText(Html.fromHtml(objBean.getArtTitle()));
        } else {
            holder.tv_star_text.setVisibility(View.GONE);
        }
        //----------------------------------------------------------------

        if (holder.tvcfee != null && null != objBean.getCfee()
                && objBean.getCfee().trim().length() > 0) {
            holder.tvcfee.setText(Html.fromHtml(objBean.getCfee()));
        }

        if (holder.tvqfee != null && null != objBean.getQfee()
                && objBean.getQfee().trim().length() > 0) {
            holder.tvqfee.setText(Html.fromHtml(objBean.getQfee()));
        }

        if (holder.tvdocname != null && null != objBean.getDocname() && objBean.getDocname().trim().length() > 0) {
            holder.tvdocname.setText(Html.fromHtml(objBean.getDocname()));
            holder.tvdocname_new.setText(Html.fromHtml(objBean.getDocname()));
        }

        if (holder.tvedu != null && null != objBean.getDocedu()
                && objBean.getDocedu().trim().length() > 0) {
            holder.tvedu.setText(Html.fromHtml(objBean.getDocedu()));
        }
        if (holder.tvspec != null && null != objBean.getDocspec()
                && objBean.getDocspec().trim().length() > 0) {
            holder.tvspec.setText(Html.fromHtml(objBean.getDocspec()));
            holder.tv_spec_new.setText(Html.fromHtml(objBean.getDocspec()));
        }
        if (holder.tvid != null && null != objBean.getDocid()
                && objBean.getDocid().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getDocid()));
        }

        if (holder.tv_doclink != null && null != objBean.getDocurl()
                && objBean.getDocurl().trim().length() > 0) {
            holder.tv_doclink.setText(Html.fromHtml(objBean.getDocurl()));
        }


        //----------- IS Favorite ---------------------------------------
        if (holder.img_fav_ribbon != null && null != objBean.getFav()
                && objBean.getFav().trim().length() > 0 && objBean.getFav().equals("1")) {
            holder.img_fav_ribbon.setVisibility(View.VISIBLE);
        } else {
            holder.img_fav_ribbon.setVisibility(View.GONE);
        }
        //----------- IS Favorite ---------------------------------------


        if (holder.imageview_poster != null && null != objBean.getDocimage()
                && objBean.getDocimage().trim().length() > 0) {

            holder.tv_url.setText(Html.fromHtml(objBean.getDocimage()));
            Picasso.with(getContext()).load(objBean.getDocimage()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(holder.imageview_poster);

            //Picasso.with(getContext()).load("https://img.icliniq.com//uploads//77400//77491//profile//thumbnail//56b33ebd7cdff.jpg//200_112_56b33ebd7cdff.jpg").into(holder.imageview_poster);
        }

        font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvdocname.setTypeface(font_bold);

        holder.tvcfee.setTypeface(font_reg);
        holder.tvqfee.setTypeface(font_reg);
        holder.tvedu.setTypeface(font_reg);
        holder.tvspec.setTypeface(font_reg);

        return view;
    }

    public class ViewHolder {

        public TextView tvid, tv_star_text, tv_doclink, tvspec, tvedu, tvdocname, tvcfee, tvqfee, tv_url, tvdocname_new, tv_spec_new;
        CircleImageView imageview_poster;
        ImageView img_fav_ribbon;
        RatingBar ratingBar;
    }


}