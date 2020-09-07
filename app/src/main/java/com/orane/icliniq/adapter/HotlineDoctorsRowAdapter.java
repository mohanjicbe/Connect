package com.orane.icliniq.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HotlineDoctorsRowAdapter extends ArrayAdapter<Item>{

    Typeface font_reg, font_bold;
    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    View view;
    ViewHolder selectedHolder;

    public HotlineDoctorsRowAdapter(Activity act, int resource, List<Item> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.items = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
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

        holder.btn_viewprofile = (Button) view.findViewById(R.id.btn_viewprofile);
        holder.btn_hotlineplans = (Button) view.findViewById(R.id.btn_hotlineplans);
        holder.img_menu = (ImageView) view.findViewById(R.id.img_menu);
        holder.side_ribbon = (ImageView) view.findViewById(R.id.side_ribbon);
        holder.plan_text = (TextView) view.findViewById(R.id.plan_text);
        holder.tv_docurl = (TextView) view.findViewById(R.id.tv_docurl);
        holder.tvdocname = (TextView) view.findViewById(R.id.tvdocname);
        holder.tvedu = (TextView) view.findViewById(R.id.tvedu);
        holder.tvspec = (TextView) view.findViewById(R.id.tvspec);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);
        holder.plan_amt = (TextView) view.findViewById(R.id.plan_amt);
        holder.plan_layout = (RelativeLayout) view.findViewById(R.id.plan_layout);
        holder.imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);

        font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvdocname.setTypeface(font_bold);
        holder.tvedu.setTypeface(font_reg);
        holder.tvspec.setTypeface(font_reg);
        holder.plan_amt.setTypeface(font_reg);
        holder.btn_viewprofile.setTypeface(font_bold);
        holder.btn_hotlineplans.setTypeface(font_bold);


        if (holder.plan_amt != null && null != objBean.getCurrlable()
                && objBean.getCurrlable().trim().length() > 0) {
            holder.plan_amt.setText(Html.fromHtml(objBean.getCurrlable() + "" + objBean.getHlfee3()));
        }
        if (holder.tvdocname != null && null != objBean.getHlname()
                && objBean.getHlname().trim().length() > 0) {
            holder.tvdocname.setText(Html.fromHtml(objBean.getHlname()));
        }
        if (holder.tvedu != null && null != objBean.getHledu()
                && objBean.getHledu().trim().length() > 0) {
            holder.tvedu.setText(Html.fromHtml(objBean.getHledu()));
        }
        if (holder.tvspec != null && null != objBean.getHlsp()
                && objBean.getHlsp().trim().length() > 0) {
            holder.tvspec.setText(Html.fromHtml(objBean.getHlsp()));
        }
        if (holder.tvid != null && null != objBean.getHlid()
                && objBean.getHlid().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getHlid()));
        }
        if (holder.tv_docurl != null && null != objBean.getHlphoto_url()
                && objBean.getHlphoto_url().trim().length() > 0) {
            holder.tv_docurl.setText(Html.fromHtml(objBean.getHlphoto_url()));
        }


        if (holder.imageview_poster != null && null != objBean.getHlphoto_url() && objBean.getHlphoto_url().trim().length() > 0) {
            System.out.println("objBean.getDocimage()---------------" + objBean.getHlphoto_url());
            Picasso.with(getContext()).load(objBean.getHlphoto_url()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(holder.imageview_poster);
        }

        //================================================
        if (objBean.getHlis_subscribed().equals("true")) {
            holder.side_ribbon.setVisibility(View.VISIBLE);
            holder.plan_layout.setVisibility(View.GONE);
        } else {
            holder.side_ribbon.setVisibility(View.GONE);
            holder.plan_layout.setVisibility(View.VISIBLE);
        }
        //================================================


        return view;
    }

    public class ViewHolder {

        public TextView plan_amt, tvid, tvspec, tvedu, tvdocname, plan_text, tvqfee, tv_docurl;
        CircleImageView imageview_poster;
        ImageView side_ribbon, img_menu;
        RelativeLayout plan_layout;
        Button btn_viewprofile, btn_hotlineplans;
    }


}