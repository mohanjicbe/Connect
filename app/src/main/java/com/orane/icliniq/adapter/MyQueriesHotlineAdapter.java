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

import java.util.List;

public class MyQueriesHotlineAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;

    private Item objBean;
    private int row;

    private Typeface robo_reg, robo_bold;

    public MyQueriesHotlineAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.status_icon = (ImageView) view.findViewById(R.id.status_icon);
        holder.lable_bullet = (TextView) view.findViewById(R.id.lable_bullet);
        holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
        holder.tvstatus = (TextView) view.findViewById(R.id.tvstatus);
        holder.tv_qid = (TextView) view.findViewById(R.id.tv_qid);
        holder.tvdate = (TextView) view.findViewById(R.id.tvdate);

        //-------------- Font --------------------------
        robo_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        robo_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvquery.setTypeface(robo_bold);
        holder.tvstatus.setTypeface(robo_reg);
        //-------------- Font --------------------------


        if (holder.tvquery != null && null != objBean.getQuestion()
                && objBean.getQuestion().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getQuestion()));
        }
        if (holder.tvstatus != null && null != objBean.getStatus()
                && objBean.getStatus().trim().length() > 0) {
            holder.tvstatus.setText(Html.fromHtml(objBean.getStatus()));
        }
        if (holder.tv_qid != null && null != objBean.getQid()
                && objBean.getQid().trim().length() > 0) {
            holder.tv_qid.setText(Html.fromHtml(objBean.getQid()));
        }
        if (holder.tvdate != null && null != objBean.getDatetime()
                && objBean.getDatetime().trim().length() > 0) {
            holder.tvdate.setText(Html.fromHtml(objBean.getDatetime()));
        }

        holder.lable_bullet.setVisibility(View.GONE);

        //-----------------------------------------------------------
        if ((objBean.getStatus()).equals("pending_review")) {
            holder.tvstatus.setText("Awaiting for Reply");
            (holder.lable_bullet).setBackgroundResource(R.drawable.circle_pending_query);
            holder.lable_bullet.setText("W");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.orange_800));
            //holder.status_icon.setBackgroundResource(R.mipmap.waiting);
        } else if ((objBean.getStatus()).equals("new")) {
            holder.lable_bullet.setBackgroundResource(R.drawable.circle_repost_query);
            holder.tvstatus.setText("Awaiting for Reply");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.orange_800));
            holder.lable_bullet.setText("W");
            //holder.status_icon.setBackgroundResource(R.mipmap.waiting);
        } else if ((objBean.getStatus()).equals("answered")) {
            holder.lable_bullet.setBackgroundResource(R.drawable.circle_common_query);
            holder.tvstatus.setText("Replied");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.green_800));
            holder.lable_bullet.setText("A");
            //holder.status_icon.setBackgroundResource(R.mipmap.answered);
        } else if ((objBean.getStatus()).equals("8")) {
            holder.lable_bullet.setBackgroundResource(R.drawable.circle_direct_query);
            holder.tvstatus.setText("Draft");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.dark_purple_600));
            holder.lable_bullet.setText("D");
            //holder.status_icon.setBackgroundResource(R.mipmap.draft);
        } else if ((objBean.getStatus()).equals("5")) {
            holder.lable_bullet.setBackgroundResource(R.drawable.circle_repost_query);
            holder.tvstatus.setText("Re-Post");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.dark_purple_600));
            holder.lable_bullet.setText("R");
            //holder.status_icon.setBackgroundResource(R.mipmap.draft);
            holder.tvstatus.setVisibility(View.GONE);
        } else if ((objBean.getStatus()).equals("unpaid")) {
            holder.lable_bullet.setBackgroundResource(R.drawable.circle_repost_query);
            holder.tvstatus.setText("Awaiting for Reply");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.red_700));
            holder.lable_bullet.setText("NP");
            //holder.status_icon.setBackgroundResource(R.mipmap.paypend);
        } else {
            holder.lable_bullet.setBackgroundResource(R.drawable.circle_common_query);
            holder.tvstatus.setText("Query");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.mds_bluegrey_800));
            holder.lable_bullet.setText("Q");
            //holder.status_icon.setBackgroundResource(R.mipmap.quest);
            holder.tvstatus.setVisibility(View.GONE);
        }
        //---------------------------------------------------------

        return view;
    }

    public class ViewHolder {

        public TextView tvquery, tvstatus, tv_qid, tvdate, lable_bullet;
        ImageView status_icon;
    }

}