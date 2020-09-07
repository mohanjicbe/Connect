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

public class QueryListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;

    public QueryListAdapter(Activity act, int resource, List<Item> arrayList) {
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
        holder.btn_qview = (Button) view.findViewById(R.id.btn_qview);
        holder.btn_qedit = (Button) view.findViewById(R.id.btn_qedit);
        holder.status_icon = (ImageView) view.findViewById(R.id.status_icon);
        holder.lable_bullet = (TextView) view.findViewById(R.id.lable_bullet);
        holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
        holder.tvstatus = (TextView) view.findViewById(R.id.tvstatus);
        holder.tv_qid = (TextView) view.findViewById(R.id.tv_qid);
        holder.tvdate = (TextView) view.findViewById(R.id.tvdate);
        holder.tv_hlstatus = (TextView) view.findViewById(R.id.tv_hlstatus);
        holder.tv_doctname = (TextView) view.findViewById(R.id.tv_doctname);
        holder.tv_qtype = (TextView) view.findViewById(R.id.tv_qtype);
        holder.tv_consulted_for = (TextView) view.findViewById(R.id.tv_consulted_for);
        holder.tv_paynow = (TextView) view.findViewById(R.id.tv_paynow);
        holder.tvstatus_answered = (TextView) view.findViewById(R.id.tvstatus_answered);
        holder.answered_layout = (RelativeLayout) view.findViewById(R.id.answered_layout);
        holder.draft_layout = (RelativeLayout) view.findViewById(R.id.draft_layout);
        holder.awaiting_layout = (RelativeLayout) view.findViewById(R.id.awaiting_layout);

        if (holder.imageview_poster != null && null != objBean.getDocimage() && objBean.getDocimage().trim().length() > 0) {
            holder.imageview_poster.setVisibility(View.VISIBLE);
            //holder.lable_bullet.setVisibility(View.GONE);
            Picasso.with(getContext()).load(objBean.getDocimage()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.doctor_icon).into(holder.imageview_poster);
        } else {
            holder.imageview_poster.setVisibility(View.GONE);
            //holder.lable_bullet.setVisibility(View.VISIBLE);
        }

        if (holder.tv_docurl != null && null != objBean.getDocimage()
                && objBean.getDocimage().trim().length() > 0) {
            holder.tv_docurl.setText(Html.fromHtml(objBean.getDocimage()));
        }

        if (holder.tv_hlstatus != null && null != objBean.getHotline_status()
                && objBean.getHotline_status().trim().length() > 0) {
            holder.tv_hlstatus.setText(Html.fromHtml(objBean.getHotline_status()));
        }

        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_name_light = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_light);

        holder.tvquery.setTypeface(font_reg);
        holder.tvdate.setTypeface(font_name_light);
        holder.tv_consulted_for.setTypeface(font_name_light);
        holder.tvstatus_answered.setTypeface(font_reg);
        holder.tvstatus.setTypeface(font_bold);
        holder.tv_doctname.setTypeface(font_reg);


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


        if (holder.tv_consulted_for != null && null != objBean.getTestname()
                && objBean.getTestname().trim().length() > 0) {
            holder.tv_consulted_for.setVisibility(View.VISIBLE);
            holder.tv_consulted_for.setText(Html.fromHtml(objBean.getTestname()));
        } else {
            holder.tv_consulted_for.setVisibility(View.GONE);
        }

        if (holder.tv_doctname != null && null != objBean.getDocname() && objBean.getDocname().trim().length() > 0) {
            holder.tv_doctname.setText(Html.fromHtml(objBean.getDocname()));
        } else {
            holder.tv_doctname.setText("Doctor not assigned yet");
        }

        //-----------------------------------------------------------
        if ((objBean.getStatus()).equals("pending_review")) {
            holder.tvstatus.setText("Awaiting for Answer");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.blue_900));

            // (holder.lable_bullet).setBackgroundResource(R.drawable.circle_pending_query);
            // holder.lable_bullet.setText("W");
            //holder.status_icon.setBackgroundResource(R.mipmap.waiting);
            holder.lable_bullet.setBackgroundColor(getContext().getResources().getColor(R.color.blue_900));
            holder.btn_qview.setVisibility(View.GONE);
            holder.btn_qedit.setVisibility(View.GONE);
            holder.tv_paynow.setVisibility(View.GONE);
            holder.answered_layout.setVisibility(View.GONE);
            holder.draft_layout.setVisibility(View.GONE);
            holder.awaiting_layout.setVisibility(View.VISIBLE);

        } else if ((objBean.getStatus()).equals("new")) {
            // holder.lable_bullet.setBackgroundResource(R.drawable.circle_repost_query);
            holder.tvstatus.setText("Awaiting for Answer");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.magenta));

            //holder.lable_bullet.setText("W");
            //holder.status_icon.setBackgroundResource(R.mipmap.waiting);
            holder.lable_bullet.setBackgroundColor(Color.MAGENTA);
            holder.btn_qview.setVisibility(View.GONE);
            holder.btn_qedit.setVisibility(View.GONE);
            holder.tv_paynow.setVisibility(View.GONE);

            holder.draft_layout.setVisibility(View.GONE);
            holder.answered_layout.setVisibility(View.GONE);
            holder.awaiting_layout.setVisibility(View.VISIBLE);

        } else if ((objBean.getStatus()).equals("answered")) {
            // holder.lable_bullet.setBackgroundResource(R.drawable.circle_common_query);
            holder.tvstatus.setText("Answered");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.green_800));
            //holder.lable_bullet.setText("A");
            //holder.status_icon.setBackgroundResource(R.mipmap.answered);
            holder.lable_bullet.setBackgroundColor(getContext().getResources().getColor(R.color.green_800));
            holder.btn_qview.setVisibility(View.GONE);
            holder.btn_qedit.setVisibility(View.GONE);
            holder.tv_paynow.setVisibility(View.GONE);
            holder.tvstatus.setVisibility(View.GONE);

            holder.draft_layout.setVisibility(View.GONE);
            holder.answered_layout.setVisibility(View.VISIBLE);
            holder.awaiting_layout.setVisibility(View.GONE);

        } else if ((objBean.getStatus()).equals("8")) {
            // holder.lable_bullet.setBackgroundResource(R.drawable.circle_direct_query);
            holder.tvstatus.setText("Draft");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.picker_button_background_selected));
            // holder.lable_bullet.setText("D");
            //holder.status_icon.setBackgroundResource(R.mipmap.draft);
            holder.lable_bullet.setBackgroundColor(getContext().getResources().getColor(R.color.picker_button_background_selected));
            holder.btn_qview.setVisibility(View.VISIBLE);
            holder.btn_qedit.setVisibility(View.VISIBLE);
            holder.tv_paynow.setVisibility(View.GONE);

            holder.draft_layout.setVisibility(View.VISIBLE);
            holder.answered_layout.setVisibility(View.GONE);
            holder.awaiting_layout.setVisibility(View.GONE);

        } else if ((objBean.getStatus()).equals("5")) {
            // holder.lable_bullet.setBackgroundResource(R.drawable.circle_repost_query);
            holder.tvstatus.setText("Re-Post");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.yellow_900));
            // holder.lable_bullet.setText("R");
            //holder.status_icon.setBackgroundResource(R.mipmap.repost);
            holder.lable_bullet.setBackgroundColor(getContext().getResources().getColor(R.color.yellow_900));
            holder.btn_qview.setVisibility(View.GONE);
            holder.btn_qedit.setVisibility(View.GONE);
            holder.tv_paynow.setVisibility(View.GONE);

            holder.draft_layout.setVisibility(View.GONE);
            holder.answered_layout.setVisibility(View.GONE);
            holder.awaiting_layout.setVisibility(View.GONE);

        } else if ((objBean.getStatus()).equals("unpaid")) {
            //holder.lable_bullet.setBackgroundResource(R.drawable.circle_repost_query);
            holder.tvstatus.setText("Payment Pending");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.red_800));
            // holder.lable_bullet.setText("NP");
            //holder.status_icon.setBackgroundResource(R.mipmap.paypend);
            holder.lable_bullet.setBackgroundColor(getContext().getResources().getColor(R.color.red_800));
            holder.btn_qview.setVisibility(View.GONE);
            holder.btn_qedit.setVisibility(View.GONE);
            holder.tv_paynow.setVisibility(View.VISIBLE);


            holder.draft_layout.setVisibility(View.GONE);
            holder.answered_layout.setVisibility(View.GONE);
            holder.awaiting_layout.setVisibility(View.GONE);

        } else {
            // holder.lable_bullet.setBackgroundResource(R.drawable.circle_common_query);
            holder.tvstatus.setText("Query");
            holder.tvstatus.setTextColor(getContext().getResources().getColor(R.color.red_700));
            // holder.lable_bullet.setText("Q");
            holder.lable_bullet.setBackgroundColor(getContext().getResources().getColor(R.color.red_700));
            //holder.status_icon.setBackgroundResource(R.mipmap.quest);
            holder.btn_qview.setVisibility(View.GONE);
            holder.btn_qedit.setVisibility(View.GONE);

            holder.draft_layout.setVisibility(View.GONE);
            holder.answered_layout.setVisibility(View.GONE);
            holder.awaiting_layout.setVisibility(View.GONE);
        }

        if ((objBean.getBctype()).equals("1")) {
            holder.tv_qtype.setVisibility(View.VISIBLE);
            holder.tv_qtype.setText("This is chat");
        } else {
            holder.tv_qtype.setVisibility(View.GONE);
        }
        //---------------------------------------------------------

        return view;
    }

    public class ViewHolder {

        public TextView tvquery, tvstatus_answered, tv_paynow, tv_consulted_for, tvstatus, tv_doctname, tv_qid, tvdate, lable_bullet, tv_hlstatus, tv_docurl, tv_qtype;
        ImageView status_icon;
        public Button btn_qview, btn_qedit;
        CircleImageView imageview_poster;
        RelativeLayout draft_layout, answered_layout, awaiting_layout;
    }

}