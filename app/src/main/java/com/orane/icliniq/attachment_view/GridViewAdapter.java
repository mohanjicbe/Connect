package com.orane.icliniq.attachment_view;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orane.icliniq.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<GridItem> {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();
    public String full_path;

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.tv_ext = (TextView) row.findViewById(R.id.tv_ext);
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getTitle()));
        holder.tv_ext.setText(item.getExt());

        //Picasso.with(mContext).load(item.getImage()).into(holder.imageView);

        full_path = item.getImage();

/*        String url_text = item.getImage();
        extension = attach_map.get(url_text);*/

        /*        String extension = full_path.substring(full_path.lastIndexOf(".") + 1);
         */

        String extension = item.getExt();
        System.out.println("Adapter extension-------------" + extension);

        switch (extension) {

            //------------ Images --------------------------------------
            case "jpg":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "jpeg":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "gif":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "png":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "tiff":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "bmp":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "psd":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "IMG":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "JPE":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "WMF":
                Picasso.with(mContext).load(R.mipmap.jpg).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            //------------ Images --------------------------------------


            case "pdf":
                Picasso.with(mContext).load(R.mipmap.pdf).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "doc":
                Picasso.with(mContext).load(R.mipmap.doc).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "docx":
                Picasso.with(mContext).load(R.mipmap.doc).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "xls":
                Picasso.with(mContext).load(R.mipmap.xls).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "xlsx":
                Picasso.with(mContext).load(R.mipmap.xls).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "ppt":
                Picasso.with(mContext).load(R.mipmap.ppt).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "pptx":
                Picasso.with(mContext).load(R.mipmap.ppt).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "pps":
                Picasso.with(mContext).load(R.mipmap.ppt).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "ppsx":
                Picasso.with(mContext).load(R.mipmap.ppt).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "txt":
                Picasso.with(mContext).load(R.mipmap.txt).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            case "rft":
                Picasso.with(mContext).load(R.mipmap.rtf).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
            default:
                Picasso.with(mContext).load(R.mipmap.attach_icon).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);
                break;
        }


        //Picasso.with(mContext).load(item.getImage()).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(holder.imageView);


        return row;
    }

    static class ViewHolder {
        TextView titleTextView,tv_ext;
        ImageView imageView;
    }


}