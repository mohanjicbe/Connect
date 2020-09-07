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
import com.orane.icliniq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Presription_ListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;

    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;

    public Presription_ListAdapter(Activity act, int resource, List<Item> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.items = arrayList;
    }

    public void setSelection(int position) {
        if (selectedPos == position) {
            selectedPos = NOT_SELECTED;
        } else {
            selectedPos = position;
        }
        notifyDataSetChanged();
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

        holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
        holder.tvaskedname = (TextView) view.findViewById(R.id.tvaskedname);
        holder.tvprice = (TextView) view.findViewById(R.id.tvprice);
        holder.tvspeciality = (TextView) view.findViewById(R.id.tvspeciality);
        holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
        holder.tv_when_to_take = (TextView) view.findViewById(R.id.tv_when_to_take);
        holder.tv_how_to_take = (TextView) view.findViewById(R.id.tv_how_to_take);
        holder.tv_days = (TextView) view.findViewById(R.id.tv_days);
        holder.tv_drug_type = (TextView) view.findViewById(R.id.tv_drug_type);
        holder.imageview_poster = (ImageView) view.findViewById(R.id.imageview_poster);


/*
        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvquery.setTypeface(font_reg);
        holder.tvspeciality.setTypeface(font_reg);
        holder.tvprice.setTypeface(font_bold);
        holder.tvaskedname.setTypeface(font_bold);
        holder.tvDate.setTypeface(font_reg);
        holder.tv_info.setTypeface(font_reg);

*/

/*
        noto_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        noto_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvquery.setTypeface(noto_reg);
        holder.tvspeciality.setTypeface(noto_reg);
        holder.tvDate.setTypeface(noto_reg);

*/

        if (holder.tvquery != null && null != objBean.getName()
                && objBean.getName().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getName()));
        }
        if (holder.tv_when_to_take != null && null != objBean.getDes()
                && objBean.getDes().trim().length() > 0) {
            holder.tv_when_to_take.setText(Html.fromHtml(objBean.getDes()));
        }
        if (holder.tv_drug_type != null && null != objBean.getTy()
                && objBean.getTy().trim().length() > 0) {
            System.out.println("objBean.getTy()----------" + objBean.getTy());
            holder.tv_drug_type.setText(objBean.getTy());
        }

        if (holder.imageview_poster != null) {
            if (objBean.getTy().equals("Tablet")) {
                System.out.println("objBean.getTy()----------" + objBean.getTy());
                Picasso.with(getContext()).load(R.mipmap.tablet_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(holder.imageview_poster);
            } else if (objBean.getTy().equals("Cream")) {
                Picasso.with(getContext()).load(R.mipmap.cream_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(holder.imageview_poster);
            } else if (objBean.getTy().equals("Lotion")) {
                Picasso.with(getContext()).load(R.mipmap.lotion_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(holder.imageview_poster);
            } else {
                Picasso.with(getContext()).load(R.mipmap.medicine_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(holder.imageview_poster);
            }
        }


        if (holder.tv_id != null && null != objBean.getId()
                && objBean.getId().trim().length() > 0) {
            holder.tv_id.setText(Html.fromHtml(objBean.getId()));
        }

        if (holder.tvaskedname != null && null != objBean.getWamt()
                && objBean.getWamt().trim().length() > 0) {
            holder.tvaskedname.setText(Html.fromHtml(objBean.getWamt()));
        }

        if (holder.tvprice != null && null != objBean.getAmt() && objBean.getAmt().trim().length() > 0) {
            holder.tvprice.setText(Html.fromHtml(objBean.getAmt()));
        }

        if (holder.tvspeciality != null && !(objBean.getPri()).equals("null") && null != objBean.getPri() && objBean.getPri().trim().length() > 0) {
            holder.tvspeciality.setText(Html.fromHtml(objBean.getPri()));
        } else {
            holder.tvspeciality.setVisibility(View.GONE);
        }

        if (holder.tv_how_to_take != null && null != objBean.getWamt()
                && objBean.getWamt().trim().length() > 0) {
            holder.tv_how_to_take.setText(Html.fromHtml(objBean.getWamt()));
        }
        if (holder.tv_days != null && null != objBean.getSpec()
                && objBean.getSpec().trim().length() > 0) {
            holder.tv_days.setText(Html.fromHtml(objBean.getSpec()));
        }

        return view;
    }

    public class ViewHolder {

        public TextView tvquery, tvaskedname, tv_days, tv_how_to_take, tv_drug_type, tv_when_to_take, tv_id, tvprice, tvspeciality;
        public ImageView imageview_poster;
    }

}