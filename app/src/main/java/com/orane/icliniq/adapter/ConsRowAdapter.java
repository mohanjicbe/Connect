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
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsRowAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public ConsRowAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tvdocname = (TextView) view.findViewById(R.id.tvdocname);
        holder.tvnotes = (TextView) view.findViewById(R.id.tvnotes);
        holder.tvctype = (TextView) view.findViewById(R.id.tvctype);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);
        holder.tvdate = (TextView) view.findViewById(R.id.tvdate);
        holder.tvtime = (TextView) view.findViewById(R.id.tvtime);
        holder.imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);
        holder.consul_type_phone_icon = (ImageView) view.findViewById(R.id.consul_type_phone_icon);
        holder.consul_type_video_icon = (ImageView) view.findViewById(R.id.consul_type_video_icon);


        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvdocname.setTypeface(font_bold);
        holder.tvnotes.setTypeface(font_reg);
        holder.tvdate.setTypeface(font_bold);
        holder.tvtime.setTypeface(font_bold);


        System.out.println("objBean.getCdname()----" + objBean.getCdname());


        if (holder.tvdocname != null && null != objBean.getCdname()
                && objBean.getCdname().trim().length() > 0) {
            holder.tvdocname.setText(Html.fromHtml(objBean.getCdname()));
        }
        if (holder.tvnotes != null && null != objBean.getCnotes()
                && objBean.getCnotes().trim().length() > 0) {
            holder.tvnotes.setText(Html.fromHtml("Notes: " + objBean.getCnotes()));
        }

        if (holder.tvctype != null && null != objBean.getDocname()
                && objBean.getDocname().trim().length() > 0) {
            holder.tvctype.setText(Html.fromHtml(objBean.getDocname()));
        }
        if (holder.tvdate != null && null != objBean.getCdate()
                && objBean.getCdate().trim().length() > 0) {
            holder.tvdate.setText(Html.fromHtml("On: " + objBean.getCdate()));
        }
        if (holder.tvtime != null && null != objBean.getCtime()
                && objBean.getCtime().trim().length() > 0) {
            holder.tvtime.setText(Html.fromHtml("At: " + objBean.getCtime()));
        }
        if (holder.tvid != null && null != objBean.getDocid()
                && objBean.getDocid().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getDocid()));
        }

        if (holder.imageview_poster != null && null != objBean.getDocimage()
                && objBean.getDocimage().trim().length() > 0) {

            System.out.println("objBean.getDocimage()---------------" + objBean.getDocimage());
            Picasso.with(getContext()).load(objBean.getDocimage()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(holder.imageview_poster);

            //Picasso.with(getContext()).load("https://img.icliniq.com//uploads//77400//77491//profile//thumbnail//56b33ebd7cdff.jpg//200_112_56b33ebd7cdff.jpg").into(holder.imageview_poster);
        }

        if (null != objBean.getTy() && objBean.getTy().trim().length() > 0 && !objBean.getTy().equals("none")) {

            if (objBean.getTy().equals("Phone")) {
                holder.consul_type_phone_icon.setVisibility(View.VISIBLE);
                holder.consul_type_video_icon.setVisibility(View.GONE);

            } else if (objBean.getTy().equals("Video")) {
                holder.consul_type_phone_icon.setVisibility(View.GONE);
                holder.consul_type_video_icon.setVisibility(View.VISIBLE);
            } else {
                holder.consul_type_phone_icon.setVisibility(View.GONE);
                holder.consul_type_video_icon.setVisibility(View.GONE);
            }

            System.out.println("objBean.getTy()---------------" + objBean.getTy());
            Picasso.with(getContext()).load(objBean.getDocimage()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(holder.imageview_poster);

            //Picasso.with(getContext()).load("https://img.icliniq.com//uploads//77400//77491//profile//thumbnail//56b33ebd7cdff.jpg//200_112_56b33ebd7cdff.jpg").into(holder.imageview_poster);
        } else {
            holder.consul_type_phone_icon.setVisibility(View.GONE);
            holder.consul_type_video_icon.setVisibility(View.GONE);
        }


        return view;
    }

    public class ViewHolder {

        public TextView tvdocname, tvnotes, tvctype, tvid, tvdate, tvtime;
        CircleImageView imageview_poster;
        ImageView consul_type_phone_icon, consul_type_video_icon;
    }

}