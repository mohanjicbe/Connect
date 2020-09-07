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

import java.util.List;

public class TransactionsAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;
    Typeface font_reg,font_bold;

    public TransactionsAdapter(Activity act, int resource, List<Item> arrayList) {
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


        holder.tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
        holder.tv_amt = (TextView) view.findViewById(R.id.tv_amt);


        if (holder.tvDesc != null && null != objBean.getDes()
                && objBean.getDes().trim().length() > 0) {
            holder.tvDesc.setText(Html.fromHtml(objBean.getDes()));
        }
        if (holder.tv_date != null && null != objBean.getDatetime()
                && objBean.getDatetime().trim().length() > 0) {
            holder.tv_date.setText(Html.fromHtml(objBean.getDatetime()));
        }

        if (holder.tv_amt != null && null != objBean.getAmt()
                && objBean.getAmt().trim().length() > 0) {
            holder.tv_amt.setText(Html.fromHtml(objBean.getAmt()));
        }

        //--------------- Font ----------------------------
        font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvDesc.setTypeface(font_reg);
        holder.tv_date.setTypeface(font_reg);
        holder.tv_amt.setTypeface(font_bold);
        //--------------- Font ----------------------------

        return view;
    }

    public class ViewHolder {

        public TextView tvDesc, tv_date,tv_amt;
    }


}