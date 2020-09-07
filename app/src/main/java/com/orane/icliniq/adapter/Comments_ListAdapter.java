package com.orane.icliniq.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comments_ListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;

    public Comments_ListAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tv_feedback = view.findViewById(R.id.tv_feedback);
        holder.tv_name = view.findViewById(R.id.tv_name);
        holder.tv_edu = view.findViewById(R.id.tv_edu);
        holder.tv_id = view.findViewById(R.id.tv_id);
        holder.tv_pat_name = view.findViewById(R.id.tv_pat_name);
        holder.tv_doc_id = view.findViewById(R.id.tv_doc_id);
        holder.doc_img = view.findViewById(R.id.doc_img);

        if (holder.doc_img != null && null != objBean.getDocurl() && objBean.getDocurl().trim().length() > 0) {
            holder.doc_img.setVisibility(View.VISIBLE);
            //holder.lable_bullet.setVisibility(View.GONE);
            Picasso.with(getContext()).load(objBean.getDocurl()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.doctor_icon).into(holder.doc_img);
        } else {
            holder.doc_img.setVisibility(View.GONE);
            //holder.lable_bullet.setVisibility(View.VISIBLE);
        }

        if (holder.tv_feedback != null && null != objBean.getFeed()
                && objBean.getFeed().trim().length() > 0) {
            holder.tv_feedback.setText(Html.fromHtml(objBean.getFeed()));
        }

        if (holder.tv_name != null && null != objBean.getDocname()
                && objBean.getDocname().trim().length() > 0) {
            holder.tv_name.setText(Html.fromHtml(objBean.getDocname()));
        }

        if (holder.tv_edu != null && null != objBean.getDocedu()
                && objBean.getDocedu().trim().length() > 0) {
            holder.tv_edu.setText(Html.fromHtml(objBean.getDocedu()));
        }
        if (holder.tv_pat_name != null && null != objBean.getName()
                && objBean.getName().trim().length() > 0) {
            holder.tv_pat_name.setText(Html.fromHtml(objBean.getName()));
        }
        if (holder.tv_doc_id != null && null != objBean.getDocid()
                && objBean.getDocid().trim().length() > 0) {
            holder.tv_doc_id.setText(Html.fromHtml(objBean.getDocid()));
        }


        return view;
    }

    public class ViewHolder {


        public TextView tv_name, tv_edu, tv_id, tv_pat_name, tv_feedback, tv_doc_id;
        CircleImageView doc_img;
        LinearLayout doc_layout, feedback_layout;
    }

}