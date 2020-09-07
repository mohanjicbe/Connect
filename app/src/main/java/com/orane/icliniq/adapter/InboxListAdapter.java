package com.orane.icliniq.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;

    public InboxListAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tv_qid = (TextView) view.findViewById(R.id.tv_qid);
        holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
        holder.tvstatus = (TextView) view.findViewById(R.id.tvstatus);
        holder.tvdate = (TextView) view.findViewById(R.id.tvdate);

        if (holder.tv_qid != null && null != objBean.getId()
                && objBean.getId().trim().length() > 0) {
            holder.tv_qid.setText(Html.fromHtml(objBean.getId()));
        }
        if (holder.tvquery != null && null != objBean.getTitle()
                && objBean.getTitle().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getTitle()));
        }

        if (holder.tvstatus != null && null != objBean.getDocname()
                && objBean.getDocname().trim().length() > 0) {
            holder.tvstatus.setText(Html.fromHtml(objBean.getDocname()));
        }

        if (holder.tvdate != null && null != objBean.getDatetime()
                && objBean.getDatetime().trim().length() > 0) {
            holder.tvdate.setText(Html.fromHtml(objBean.getDatetime()));
        }


        return view;
    }

    public class ViewHolder {

        public TextView tvquery, tvstatus, tv_doctname, tv_qid, tvdate, lable_bullet, tv_hlstatus, tv_docurl, tv_qtype;
        ImageView status_icon;
        public Button btn_qview, btn_qedit;
        CircleImageView imageview_poster;
    }

}