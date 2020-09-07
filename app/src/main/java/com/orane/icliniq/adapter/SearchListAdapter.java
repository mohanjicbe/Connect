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

public class SearchListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;

    public SearchListAdapter(Activity act, int resource, List<Item> arrayList) {
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


        holder.imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);
        holder.textview_title = (TextView) view.findViewById(R.id.textview_title);
        holder.textview_short = (TextView) view.findViewById(R.id.textview_short);
        holder.textview_speciality = (TextView) view.findViewById(R.id.textview_speciality);
        holder.textview_ctype = (TextView) view.findViewById(R.id.textview_ctype);
        holder.tv_url = (TextView) view.findViewById(R.id.tv_url);
        holder.tv_docid = (TextView) view.findViewById(R.id.tv_docid);

        Typeface robo_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface robo_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.textview_title.setTypeface(robo_bold);
        holder.textview_short.setTypeface(robo_reg);
        holder.textview_speciality.setTypeface(robo_reg);
        holder.textview_ctype.setTypeface(robo_reg);

        if (holder.imageview_poster != null && null != objBean.getSphoto_url() && objBean.getSphoto_url().trim().length() > 0) {
            holder.imageview_poster.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(objBean.getSphoto_url()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.doctor_icon).into(holder.imageview_poster);
        } else {
            holder.imageview_poster.setVisibility(View.GONE);
        }

        if (holder.textview_title != null && null != objBean.getStitle()
                && objBean.getStitle().trim().length() > 0) {
            holder.textview_title.setText(Html.fromHtml(objBean.getStitle()));
        }

        if (holder.textview_short != null && null != objBean.getSdesc()
                && objBean.getSdesc().trim().length() > 0) {
            holder.textview_short.setText(Html.fromHtml(objBean.getSdesc()));
        }

        if (holder.textview_speciality != null && null != objBean.getSspeciality()
                && objBean.getSspeciality().trim().length() > 0) {

            String non_str = (objBean.getSspeciality()).replaceAll("\\[", "").replaceAll("\\]", "");
            non_str = non_str.replace("\"", "");

            holder.textview_speciality.setText(Html.fromHtml(non_str));
        }

        if (holder.textview_ctype != null && null != objBean.getSlabel()
                && objBean.getSlabel().trim().length() > 0) {
            holder.textview_ctype.setText(Html.fromHtml(objBean.getSlabel()));
        }


        //----------------------------------------------------------------------
        if (objBean.getSitemtype().equals("1")) {
            holder.textview_ctype.setText(Html.fromHtml("Q&amp;A"));
            holder.textview_ctype.setBackgroundResource(R.drawable.qa_blue);
        }
        if (objBean.getSitemtype().equals("2")) {
            holder.textview_ctype.setText("Article");
            holder.textview_ctype.setBackgroundResource(R.drawable.article_green);
        }
        if (objBean.getSitemtype().equals("3")) {
            holder.textview_ctype.setText("Doctor");
            holder.textview_ctype.setBackgroundResource(R.drawable.doctor_green);
        }
        if (objBean.getSitemtype().equals("4")) {
            holder.textview_ctype.setText("Tool");
            holder.textview_ctype.setBackgroundResource(R.drawable.tool_orange);
        }
        //----------------------------------------------------------------------


        if (holder.tv_url != null && null != objBean.getSurl()
                && objBean.getSurl().trim().length() > 0) {
            holder.tv_url.setText(Html.fromHtml(objBean.getSurl()));
        }

        if (holder.tv_docid != null && null != objBean.getSitemid()
                && objBean.getSitemid().trim().length() > 0) {
            holder.tv_docid.setText(Html.fromHtml(objBean.getSitemid()));

            System.out.println("Item ID----------------------" + objBean.getSitemid());


        }

        return view;
    }

    public class ViewHolder {

        public TextView textview_title, textview_short, textview_speciality, textview_ctype, tv_url, tv_docid;
        CircleImageView imageview_poster;
    }

}