package com.orane.icliniq;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.orane.icliniq.Model.Constants;
import com.orane.icliniq.Model.FileInfo;
import com.orane.icliniq.adapter.FileArrayAdapter;

import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class FileChooserBookingActivity extends ListActivity {

    private File currentFolder;
    private FileArrayAdapter fileArrayListAdapter;
    private FileFilter fileFilter;
    private File fileSelected;
    private ArrayList<String> extensions;
    public String sel_filename;

    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    public JSONObject json;
    String upLoadServerUri = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras
                    .getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS) != null) {
                extensions = extras
                        .getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS);
                fileFilter = new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return ((pathname.isDirectory()) || (pathname.getName()
                                .contains(".") && extensions.contains(pathname
                                .getName().substring(
                                        pathname.getName().lastIndexOf(".")))));
                    }
                };
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        currentFolder = new File(Environment.getDataDirectory()
                .getAbsolutePath());
        fill(currentFolder);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((!currentFolder.getName().equals(
                    Environment.getDataDirectory().getName()))
                    && (currentFolder.getParentFile() != null)) {
                currentFolder = currentFolder.getParentFile();
                fill(currentFolder);
            } else {
                Log.i("FILE CHOOSER", "canceled");
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void fill(File f) {
        File[] folders = null;
        if (fileFilter != null)
            folders = f.listFiles(fileFilter);
        else
            folders = f.listFiles();

        this.setTitle(getString(R.string.currentDir) + ": " + f.getName());
        List<FileInfo> dirs = new ArrayList<FileInfo>();
        List<FileInfo> files = new ArrayList<FileInfo>();
        try {
            for (File file : folders) {
                if (file.isDirectory() && !file.isHidden())
                    //si es un directorio en el data se ponemos la contante folder
                    dirs.add(new FileInfo(file.getName(),
                            Constants.FOLDER, file.getAbsolutePath(),
                            true, false));
                else {
                    if (!file.isHidden())
                        files.add(new FileInfo(file.getName(),
                                getString(R.string.fileSize) + ": "
                                        + file.length(),
                                file.getAbsolutePath(), false, false));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(dirs);
        Collections.sort(files);
        dirs.addAll(files);
        if (!f.getName().equalsIgnoreCase(
                Environment.getDataDirectory().getName())) {
            if (f.getParentFile() != null)
                //si es un directorio padre en el data se ponemos la contante adeacuada
                dirs.add(0, new FileInfo("..",
                        Constants.PARENT_FOLDER, f.getParent(),
                        false, true));
        }
        fileArrayListAdapter = new FileArrayAdapter(FileChooserBookingActivity.this,
                R.layout.file_row, dirs);
        this.setListAdapter(fileArrayListAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        FileInfo fileDescriptor = fileArrayListAdapter.getItem(position);
        if (fileDescriptor.isFolder() || fileDescriptor.isParent()) {
            currentFolder = new File(fileDescriptor.getPath());
            fill(currentFolder);
        } else {

            fileSelected = new File(fileDescriptor.getPath());

            Intent intent = new Intent();
            intent.putExtra(Constants.KEY_FILE_SELECTED, fileSelected.getAbsolutePath());
            setResult(Activity.RESULT_OK, intent);
            Log.i("FILE CHOOSER", "result ok");

            sel_filename = (fileSelected.getAbsolutePath()).substring((fileSelected.getAbsolutePath()).lastIndexOf("/") + 1);

/*			String path=":/storage/sdcard0/DCIM/Camera/1414240995236.jpg";//it contain your path of image..im using a temp string..
            String filename=path.substring(path.lastIndexOf("/")+1);*/

            System.out.println("fileSelected.getAbsolutePath()----------" + fileSelected.getAbsolutePath());
            System.out.println("fileSelected)----------" + sel_filename);


            //=======================================================
            final MaterialDialog alert = new MaterialDialog(FileChooserBookingActivity.this);
            View view = LayoutInflater.from(FileChooserBookingActivity.this).inflate(R.layout.image_preview, null);
            alert.setView(view);
            alert.setTitle("");

            final ImageView image1 = (ImageView) view.findViewById(R.id.image1);
            /*final ImageView image_close = (ImageView) view.findViewById(R.id.image_close);
            final Button btn_upload = (Button) view.findViewById(R.id.btn_upload);
*/
            System.out.println("sel_filename.endsWith(\".jpg\")----------" + sel_filename.endsWith(".jpg"));
            System.out.println("sel_filename.endsWith(\".pdf\")----------" + sel_filename.endsWith(".pdf"));

            image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
/*
            //------------ Images -----------------------
            if (sel_filename.endsWith(".jpg")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".png")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".tiff")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".gif")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".bmp")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".jpeg")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".JPG")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".GIF")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".BMP")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".JPEG")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".TIFF")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            if (sel_filename.endsWith(".PNG")) image1.setImageBitmap(BitmapFactory.decodeFile(fileSelected.getAbsolutePath()));
            //------------ Images -----------------------*/
            //------------ Audio & Video -----------------------------------
            if (sel_filename.endsWith(".mp3")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".wav")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4a")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4b")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4p")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".wma")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".dat")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".mpeg")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".mp4")) image1.setBackgroundResource(R.mipmap.multimedia_file);
            //------------ Audio & Video -----------------------------------
            if (sel_filename.endsWith(".pdf")) image1.setBackgroundResource(R.mipmap.pdf_file);
            if (sel_filename.endsWith(".doc")) image1.setBackgroundResource(R.mipmap.text_file);
            if (sel_filename.endsWith(".xls")) image1.setBackgroundResource(R.mipmap.text_file);
            if (sel_filename.endsWith(".zip")) image1.setBackgroundResource(R.mipmap.zip_file);
            if (sel_filename.endsWith(".rar")) image1.setBackgroundResource(R.mipmap.zip_file);
            //------------ Doc -----------------------------------

/*
            image_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent2 = new Intent(FileChooserBookingActivity.this, UploadBookingToServer.class);
                    intent2.putExtra("KEY_path", fileSelected.getAbsolutePath());
                    intent2.putExtra("KEY_filename", sel_filename);
                    startActivity(intent2);
                    finish();

                    alert.dismiss();
                }
            });*/


            alert.setCanceledOnTouchOutside(true);
/*            alert.setPositiveButton("UPLOAD", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
            alert.setNegativeButton("CANCEL", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });*/
            alert.show();
            //===============================================================


/*            Intent intent2  = new Intent(FileChooserBookingActivity.this,UploadBookingToServer.class);
            intent2.putExtra("KEY_path", fileSelected.getAbsolutePath());
            intent2.putExtra("KEY_filename", sel_filename);
            startActivity(intent2);
            finish();*/



        }
    }




}