package com.orane.icliniq.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingRowAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public BookingRowAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tvcdate = (TextView) view.findViewById(R.id.tvcdate);
        holder.tvctime = (TextView) view.findViewById(R.id.tvctime);
        holder.tvstatus = (TextView) view.findViewById(R.id.tvstatus);
        holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);
        holder.tv_date_lab = (TextView) view.findViewById(R.id.tv_date_lab);
        holder.tv_time_range = (TextView) view.findViewById(R.id.tv_time_range);
        holder.tv_status_lab = (TextView) view.findViewById(R.id.tv_status_lab);

        holder.imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);

        System.out.println("objBean.getCdname()----" + objBean.getBcdate());


        //------------------- Font Setting ----------------------------------
        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvcdate.setTypeface(font_bold);
        holder.tvctime.setTypeface(font_bold);
        holder.tvquery.setTypeface(font_bold);
        holder.tvstatus.setTypeface(font_bold);

        holder.tv_date_lab.setTypeface(font_reg);
        holder.tv_time_range.setTypeface(font_reg);
        holder.tv_status_lab.setTypeface(font_reg);
        //------------------- Font Setting ----------------------------------


        if (holder.tvcdate != null && null != objBean.getBcdate()
                && objBean.getBcdate().trim().length() > 0) {
            holder.tvcdate.setText(Html.fromHtml(objBean.getBcdate()));
        }
        if (holder.tvctime != null && null != objBean.getBstrtrange()
                && objBean.getBstrtrange().trim().length() > 0) {
            holder.tvctime.setText(Html.fromHtml(objBean.getBstrtrange()));
        }

        System.out.println("objBean.getBstatus()-----------------" + objBean.getBstatus());
        //-------------- Null -------------------------
        if (holder.tvstatus != null && null != objBean.getBstatus() && objBean.getBstatus().trim().length() > 0 && !objBean.getBstatus().equals("null")) {
            holder.tvstatus.setText(Html.fromHtml(objBean.getBstatus()));

        } else {
            holder.tvstatus.setText("Call is not confirmed yet");
            holder.tvstatus.setTextColor(Color.parseColor("#bdbdbd"));

        }
        //-------------- Null -------------------------

        if (holder.tvquery != null && null != objBean.getBquery()
                && objBean.getBquery().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getBquery()));
        }
        if (holder.tvid != null && null != objBean.getBid()
                && objBean.getBid().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getBid()));
        }


        System.out.println("Status value--------" + objBean.getBctype());


        if ((objBean.getBctype()).equals("Phone")) {
            holder.imageview_poster.setBackgroundResource(R.mipmap.phone_cons_ico_color);
        } else if ((objBean.getBctype()).equals("Direct Visit")) {
            holder.imageview_poster.setBackgroundResource(R.mipmap.direct_walk);
        } else {
            holder.imageview_poster.setBackgroundResource(R.mipmap.video_cons_ico_color);
        }

/*        if (holder.imageview_poster != null && null != objBean.getDocimage()
                && objBean.getDocimage().trim().length() > 0) {

            System.out.println("objBean.getDocimage()---------------" + objBean.getDocimage());
            Picasso.with(getContext()).load(objBean.getDocimage()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(holder.imageview_poster);

            //Picasso.with(getContext()).load("https://img.icliniq.com//uploads//77400//77491//profile//thumbnail//56b33ebd7cdff.jpg//200_112_56b33ebd7cdff.jpg").into(holder.imageview_poster);
        }*/

        return view;
    }

    public class ViewHolder {

        public TextView tvcdate, tvctime, tvstatus, tvquery, tvid, tv_date_lab, tv_time_range, tv_status_lab;
        CircleImageView imageview_poster;
    }

}