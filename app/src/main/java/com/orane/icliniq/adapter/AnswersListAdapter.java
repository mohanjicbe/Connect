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
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswersListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;

    public AnswersListAdapter(Activity act, int resource, List<Item> arrayList) {
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
        holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
        holder.tv_doctname = (TextView) view.findViewById(R.id.tv_doctname);
        holder.tv_hash_url = (TextView) view.findViewById(R.id.tv_hash_url);

        if (holder.imageview_poster != null && null != objBean.getHlphoto_url() && objBean.getHlphoto_url().trim().length() > 0) {
            holder.imageview_poster.setVisibility(View.VISIBLE);
            //holder.lable_bullet.setVisibility(View.GONE);
            Picasso.with(getContext()).load(objBean.getHlphoto_url()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.doctor_icon).into(holder.imageview_poster);
        } else {
            holder.imageview_poster.setVisibility(View.GONE);
            //holder.lable_bullet.setVisibility(View.VISIBLE);
        }

        if (holder.tv_docurl != null && null != objBean.getDocimage()
                && objBean.getDocimage().trim().length() > 0) {
            holder.tv_docurl.setText(Html.fromHtml(objBean.getDocimage()));
        }

        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);

        holder.tvquery.setTypeface(font_reg);
        holder.tv_doctname.setTypeface(font_reg);


        if (holder.tvquery != null && null != objBean.getTitle()
                && objBean.getTitle().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getTitle()));
        }

/*
        if (holder.tvquery != null && null != objBean.getArtAbs()
                && objBean.getArtAbs().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getArtAbs()));
        }
*/

        if (holder.tv_hash_url != null && null != objBean.getArturl()
                && objBean.getArturl().trim().length() > 0) {
            holder.tv_hash_url.setText(Html.fromHtml(objBean.getArturl()));
        }

        if (holder.tv_doctname != null && null != objBean.getDocname() && objBean.getDocname().trim().length() > 0) {
            holder.tv_doctname.setText(Html.fromHtml(objBean.getDocname()));
        } else {
            holder.tv_doctname.setText("Doctor not assigned yet");
        }
        //---------------------------------------------------------

        return view;
    }

    public class ViewHolder {

        public TextView tvquery, tv_id, tv_hash_url, tvstatus, tv_doctname, tv_qid, tvdate, lable_bullet, tv_hlstatus, tv_docurl, tv_qtype;
        ImageView status_icon;
        public Button btn_qview, btn_qedit;
        CircleImageView imageview_poster;
    }

}