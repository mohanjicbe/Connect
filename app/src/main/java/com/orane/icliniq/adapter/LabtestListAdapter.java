package com.orane.icliniq.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.orane.icliniq.Model.Item;
import com.orane.icliniq.R;

import java.util.List;

public class LabtestListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    ViewHolder holder;

    public LabtestListAdapter(Activity act, int resource, List<Item> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.items = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;


        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);
            holder = new ViewHolder();

            holder.tv_testtype = (TextView) view.findViewById(R.id.tv_testtype);
            holder.tv_testname = (TextView) view.findViewById(R.id.tv_testname);
            holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            holder.tv_amt = (TextView) view.findViewById(R.id.tv_amt);
            holder.btn_viewtests = (Button) view.findViewById(R.id.btn_viewtests);
            holder.btn_addtocart = (Button) view.findViewById(R.id.btn_addtocart);
            holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
            holder.tv_isadded = (TextView) view.findViewById(R.id.tv_isadded);
            holder.tv_cartText = (TextView) view.findViewById(R.id.tv_cartText);
            holder.tv_code = (TextView) view.findViewById(R.id.tv_code);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }


        if ((items == null) || ((position + 1) > items.size()))
            return view;

        objBean = items.get(position);

        if (holder.tv_id != null && null != objBean.getTestid()
                && objBean.getTestid().trim().length() > 0) {
            holder.tv_id.setText(Html.fromHtml(objBean.getTestid()));
        }
        if (holder.tv_testtype != null && null != objBean.getDisease()
                && objBean.getDisease().trim().length() > 0) {
            holder.tv_testtype.setText(Html.fromHtml(objBean.getDisease()));
        }

        if (holder.tv_testname != null && null != objBean.getTestname()
                && objBean.getTestname().trim().length() > 0) {
            holder.tv_testname.setText(Html.fromHtml(objBean.getTestname()));
        }

        if (holder.tv_desc != null && null != objBean.getTestCount() && objBean.getTestCount().trim().length() > 0 && objBean.getTestCount().equals(0)) {
            holder.tv_desc.setText(Html.fromHtml(objBean.getTestCount()));
        } else {
            holder.tv_desc.setVisibility(View.GONE);
        }

        if (holder.tv_amt != null && null != objBean.getTprice()
                && objBean.getTprice().trim().length() > 0) {
            holder.tv_amt.setText(Html.fromHtml(objBean.getTprice()));
        }

        if (holder.tv_code != null && null != objBean.getCode()
                && objBean.getCode().trim().length() > 0) {
            holder.tv_code.setText(Html.fromHtml(objBean.getCode()));
        }

        if (holder.tv_isadded != null && objBean.getIsadded().equals("1")) {
            holder.tv_isadded.setText(Html.fromHtml(objBean.getIsadded()));
            holder.btn_addtocart.setVisibility(View.GONE);
            holder.tv_cartText.setVisibility(View.VISIBLE);
        } else {
            holder.tv_isadded.setText(Html.fromHtml(objBean.getIsadded()));
            holder.btn_addtocart.setVisibility(View.VISIBLE);
            holder.tv_cartText.setVisibility(View.GONE);
        }


/*        (holder.btn_addtocart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (holder.btn_addtocart).setVisibility(View.GONE);
            }
        });*/



/*      //---------- Font -------------------------------------
        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvquery.setTypeface(font_reg);
        holder.tvdate.setTypeface(font_reg);
        holder.tvstatus.setTypeface(font_bold);
        holder.tv_doctname.setTypeface(font_reg);
        //---------- Font -------------------------------------*/

        return view;
    }

    public class ViewHolder {

        public TextView tv_testname, tv_code,tv_cartText, tv_isadded, tv_id, tv_amt, tv_desc, tv_testtype;
        private Button btn_addtocart, btn_viewtests;
    }

}