package com.orane.icliniq.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDoctorsRowAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public MyDoctorsRowAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tvcfee= (TextView) view.findViewById(R.id.tvcfee);
        holder.tvqfee = (TextView) view.findViewById(R.id.tvqfee);
        holder.tvdocname = (TextView) view.findViewById(R.id.tvdocname);
        holder.tvedu = (TextView) view.findViewById(R.id.tvedu);
        holder.tvspec = (TextView) view.findViewById(R.id.tvspec);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);

        holder.imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);


        //------------- Font Style -----------------------------------------
        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvdocname.setTypeface(font_bold);
        holder.tvedu.setTypeface(font_reg);
        holder.tvspec.setTypeface(font_reg);
        //------------- Font Style -----------------------------------------


        if (holder.tvcfee != null && null != objBean.getCfee()
                && objBean.getCfee().trim().length() > 0) {
            holder.tvcfee.setText(Html.fromHtml(objBean.getCfee()));
        }
        if (holder.tvqfee != null && null != objBean.getQfee()
                && objBean.getQfee().trim().length() > 0) {
            holder.tvqfee.setText(Html.fromHtml(objBean.getQfee()));
        }

        if (holder.tvdocname != null && null != objBean.getDocname()
                && objBean.getDocname().trim().length() > 0) {
            holder.tvdocname.setText(Html.fromHtml(objBean.getDocname()));
        }
        if (holder.tvedu != null && null != objBean.getDocedu()
                && objBean.getDocedu().trim().length() > 0) {
            holder.tvedu.setText(Html.fromHtml(objBean.getDocedu()));
        }
        if (holder.tvspec != null && null != objBean.getDocspec()
                && objBean.getDocspec().trim().length() > 0) {
            holder.tvspec.setText(Html.fromHtml(objBean.getDocspec()));
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





        return view;
    }

    public class ViewHolder {

        public TextView tvid, tvspec, tvedu, tvdocname,tvcfee,tvqfee;
        CircleImageView imageview_poster;
    }

}