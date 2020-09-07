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

public class FamilyListAdaptor extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Typeface robo_reg, robo_bold;

    public FamilyListAdaptor(Activity act, int resource, List<Item> arrayList) {
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

        holder.img_female_icon = (CircleImageView) view.findViewById(R.id.img_female_icon);
        holder.img_male_icon = (CircleImageView) view.findViewById(R.id.img_male_icon);
        holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
        holder.tv_relation = (TextView) view.findViewById(R.id.tv_relation);
        holder.tv_name = (TextView) view.findViewById(R.id.tv_name);


        if (holder.tv_id != null && null != objBean.getDocid()
                && objBean.getDocid().trim().length() > 0) {
            holder.tv_id.setText(Html.fromHtml(objBean.getDocid()));
        }

        if (holder.tv_relation != null && null != objBean.getDocname()
                && objBean.getDocname().trim().length() > 0) {
            holder.tv_relation.setText(Html.fromHtml(objBean.getDocname()));
        }

        if (holder.tv_name != null && null != objBean.getDocedu()
                && objBean.getDocedu().trim().length() > 0) {
            holder.tv_name.setText(Html.fromHtml(objBean.getDocedu()));
        }

        if (objBean.getDocspec().equals("1")) {
            holder.img_male_icon.setVisibility(View.VISIBLE);
            holder.img_female_icon.setVisibility(View.GONE);
        } else {
            holder.img_male_icon.setVisibility(View.GONE);
            holder.img_female_icon.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public class ViewHolder {

        public TextView tv_relation, tv_name, tv_id;
        CircleImageView img_female_icon, img_male_icon;
    }

}