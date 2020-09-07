package com.orane.icliniq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orane.icliniq.Model.Constants;
import com.orane.icliniq.Model.FileInfo;
import com.orane.icliniq.R;

import java.util.List;


@SuppressLint("DefaultLocale")
public class FileArrayAdapter extends ArrayAdapter<FileInfo> {

    private Context context;
    private int resorceID;
    private List<FileInfo> items;

    public FileArrayAdapter(Context context, int textViewResourceId,
                            List<FileInfo> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.resorceID = textViewResourceId;
        this.items = objects;
    }

    public FileInfo getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resorceID, null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView
                    .findViewById(android.R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.details = (TextView) convertView
                    .findViewById(R.id.details);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FileInfo option = items.get(position);
        if (option != null) {

            if (option.getData().equalsIgnoreCase(Constants.FOLDER)) {
                viewHolder.icon.setImageResource(R.mipmap.folder);
            } else if (option.getData().equalsIgnoreCase(
                    Constants.PARENT_FOLDER)) {
                viewHolder.icon.setImageResource(R.mipmap.back);
            } else {
                String name = option.getName().toLowerCase();
                if (name.endsWith(Constants.XLS)
                        || name.endsWith(Constants.XLSX))
                    viewHolder.icon.setImageResource(R.mipmap.xls);
                else if (name.endsWith(Constants.DOC)
                        || name.endsWith(Constants.DOCX))
                    viewHolder.icon.setImageResource(R.mipmap.doc);
                else if (name.endsWith(Constants.PPT)
                        || option.getName().endsWith(Constants.PPTX))
                    viewHolder.icon.setImageResource(R.mipmap.ppt);
                else if (name.endsWith(Constants.PDF))
                    viewHolder.icon.setImageResource(R.mipmap.pdf);
                else if (name.endsWith(Constants.APK))
                    viewHolder.icon.setImageResource(R.mipmap.apk);
                else if (name.endsWith(Constants.TXT))
                    viewHolder.icon.setImageResource(R.mipmap.txt);
                else if (name.endsWith(Constants.JPG)
                        || name.endsWith(Constants.JPEG))
                    viewHolder.icon.setImageResource(R.mipmap.jpg);
                else if (name.endsWith(Constants.PNG))
                    viewHolder.icon.setImageResource(R.mipmap.png);
                else if (name.endsWith(Constants.ZIP))
                    viewHolder.icon.setImageResource(R.mipmap.zip);
                else if (name.endsWith(Constants.RTF))
                    viewHolder.icon.setImageResource(R.mipmap.rtf);
                else if (name.endsWith(Constants.GIF))
                    viewHolder.icon.setImageResource(R.mipmap.gif);
                else if (name.endsWith(Constants.AVI))
                    viewHolder.icon.setImageResource(R.mipmap.avi);
                else if (name.endsWith(Constants.MP3))
                    viewHolder.icon.setImageResource(R.mipmap.mp3);
                else if (name.endsWith(Constants.MP4))
                    viewHolder.icon.setImageResource(R.mipmap.mp4);
                else if (name.endsWith(Constants.RAR))
                    viewHolder.icon.setImageResource(R.mipmap.rar);
                else if (name.endsWith(Constants.ACC))
                    viewHolder.icon.setImageResource(R.mipmap.aac);
                else
                    viewHolder.icon.setImageResource(R.mipmap.blank);
            }

            viewHolder.name.setText(option.getName());
            viewHolder.details.setText(option.getData());

        }
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        TextView details;
    }

}
