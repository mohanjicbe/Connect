package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.obsez.android.lib.filechooser.ChooserDialog;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.expand.ExpandableLayout;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import me.drakeet.materialdialog.MaterialDialog;


public class ExpandableActivity extends AppCompatActivity {

    Button btn_submit;
    EditText edt_feedback;

    public String item_type, item_id, fileUrlSecure_text, feedback_val, report_response;
    JSONObject jsonobj1, json_feedback, json_response_obj;
    LinearLayout expand_layout;
    String family_list;

    private static String dirPath;
    private String _path, extension;
    int downloadIdOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_layout);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //------------------------------------------

        try {
            Intent intent = getIntent();
            item_id = intent.getStringExtra("item_id");
            item_type = intent.getStringExtra("item_type");

            System.out.println("Get item_id---" + item_id);
            System.out.println("Get item_type---" + item_type);
        } catch (Exception e) {
            e.printStackTrace();
        }


        expand_layout = findViewById(R.id.expand_layout);

        //-------------------------------------------------------------------
        String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + item_id + "&item_type=" + item_type + "&user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("JSON_getFileList---------" + get_family_url);
        new JSON_getFileList().execute(get_family_url);
        //-------------------------------------------------------------------

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class JSON_getFileList extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ExpandableActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                family_list = jParser.getJSONString(urls[0]);

                System.out.println("Family URL---------------" + urls[0]);

                return family_list;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            //apply_relaships_radio(family_list);

            try {

                System.out.println("Attached_Files_List----" + family_list);

                JSONObject jsonFileList = new JSONObject(family_list);

                String det_text = jsonFileList.getString("det");
                System.out.println("det_text-----------" + det_text);


                JSONArray det_jarray = new JSONArray(det_text);

                expand_layout.removeAllViews();

                System.out.println("Loop count----" + det_jarray.length());

                for (int i = 0; i < det_jarray.length(); i++) {

                    jsonobj1 = det_jarray.getJSONObject(i);
                    System.out.println("jsonobj_first-----" + jsonobj1.toString());

                    String reportsLabel_text = jsonobj1.getString("reportsLabel");
                    String data_text = jsonobj1.getString("data");

                    JSONArray data_jarray = new JSONArray(data_text);

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.attachment_expand_view, null);

                    final LinearLayout expand_inner_view = addView.findViewById(R.id.expand_inner_view);
                    TextView tv_att_title = addView.findViewById(R.id.tv_att_title);
                    final ImageView img_right_arrow = addView.findViewById(R.id.img_right_arrow);

                    //img_right_arrow.setImageResource(R.mipmap.down_icon);
                    tv_att_title.setText(reportsLabel_text);

                    ExpandableLayout expandableLayout = addView.findViewById(R.id.expandable_layout);

/*                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    expandableLayout.setBackgroundColor(color);*/


                    expandableLayout.setOnExpandListener(new ExpandableLayout.OnExpandListener() {
                        @Override
                        public void onExpand(boolean expanded) {
                            //Toast.makeText(AskQuery2.this, "expand?" + expanded, Toast.LENGTH_SHORT).show();

                            if (expanded) {
                                img_right_arrow.setImageResource(R.mipmap.up_icon);
                            } else {
                                img_right_arrow.setImageResource(R.mipmap.down_icon);
                            }
                        }
                    });

                    for (int j = 0; j < data_jarray.length(); j++) {

                        JSONObject file_jobj = data_jarray.getJSONObject(j);
                        System.out.println("jsonobj_first-----" + file_jobj.toString());

                        final String attach_id_text = file_jobj.getString("attach_id");
                        String reportDate_text = file_jobj.getString("reportDate");

                        fileUrlSecure_text = file_jobj.getString("fileUrl");

                        String isDelete_text = file_jobj.getString("isDelete");
                        String reportDesc = file_jobj.getString("reportDesc");
                        String ext_text = file_jobj.getString("ext");

                        //--------------------------------------------------------------
                        if (file_jobj.has("fileUrlSecure")) {
                            fileUrlSecure_text = file_jobj.getString("fileUrlSecure");

                        } else {
                            fileUrlSecure_text = file_jobj.getString("fileUrl");
                        }

                        final String fileUrl_text = fileUrlSecure_text;
                        //--------------------------------------------------------------


                        //final String fileUrl_text = file_jobj.getString("fileUrl");
                        //final String fileUrl_text = file_jobj.getString("fileUrlSecure");

/*                        final String fileUrl_text  = file_jobj.getString("fileUrlSecure");
                        if (file_jobj.has("fileUrlSecure")) {
                            fileUrlSecure_text = file_jobj.getString("fileUrlSecure");

                        } else {
                            fileUrlSecure_text = "";
                        }*/

                        //------------------------------------
                        LayoutInflater layoutInflater2 = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView2 = layoutInflater2.inflate(R.layout.attachment_expand_view2, null);

                        TextView tv_summ_title2 = addView2.findViewById(R.id.tv_summ_title2);
                        TextView tv_desc_summary_text = addView2.findViewById(R.id.tv_desc_summary_text);
                        TextView tv_file_path = addView2.findViewById(R.id.tv_file_path);
                        TextView tv_file_ext = addView2.findViewById(R.id.tv_file_ext);
                        TextView tv_isWebView = addView2.findViewById(R.id.tv_isWebView);

                        Button btn_open = addView2.findViewById(R.id.btn_open);
                        Button btn_remove = addView2.findViewById(R.id.btn_remove);

                        tv_summ_title2.setText("File : " + (j + 1) + " Dated on " + reportDate_text);
                        tv_desc_summary_text.setText(reportDesc);
                        tv_file_path.setText(fileUrl_text);
                        tv_file_ext.setText(ext_text);
                        //tv_isWebView.setText(isWebView_val);

                        img_right_arrow.setImageBitmap(BitmapFactory.decodeFile(fileUrl_text));

                        System.out.println("attach_id_text-----------" + (attach_id_text));
                        System.out.println("reportDate_text-----------" + (reportDate_text));
                        System.out.println("isDelete_text-----------" + (isDelete_text));
                        System.out.println("reportDesc-----------" + (reportDesc));

                        //------------------------------------------
                        if (isDelete_text.equals("1")) {
                            btn_remove.setVisibility(View.VISIBLE);
                        } else {
                            btn_remove.setVisibility(View.GONE);
                        }
                        //------------------------------------------

                        btn_open.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                View parent = (View) v.getParent();

                                TextView tv_full_path = parent.findViewById(R.id.tv_file_path);
                                TextView tv_file_ext = parent.findViewById(R.id.tv_file_ext);
                                TextView tv_isWebView_val = parent.findViewById(R.id.tv_isWebView);

                                String path_text = tv_full_path.getText().toString();
                                String tv_file_ext_val = tv_file_ext.getText().toString();
                                String isWebView_text = tv_isWebView_val.getText().toString();

                                String file_full__url = "";

                                if ("?".contains(fileUrl_text)) {
                                    file_full__url = fileUrl_text + "&token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                                } else {
                                    file_full__url = fileUrl_text + "?token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                                }

                                System.out.println("file_full__url-------------" + tv_file_ext_val);
                                System.out.println("tv_file_ext_val-------------" + file_full__url);


                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(file_full__url));
                                startActivity(i);

/*
                                if (tv_file_ext_val.equals("jpg")
                                        || (tv_file_ext_val.equals("JPG"))
                                        || (tv_file_ext_val.equals("png"))
                                        || (tv_file_ext_val.equals("PNG"))
                                        || (tv_file_ext_val.equals("jpeg"))
                                        || (tv_file_ext_val.equals("JPEG"))
                                        || (tv_file_ext_val.equals("tiff"))
                                ) {

                                    //-------------------- webview -------------------------------------------
                                    Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                                    i.putExtra("url", file_full__url);
                                    i.putExtra("type", "Attachment View");
                                    startActivity(i);
                                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                    //-------------------- webview -------------------------------------------

                                } else {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(file_full__url));
                                    startActivity(i);
                                }
*/
                            }
                        });

                        expand_inner_view.addView(addView2);
                        //-----------------------------------

                    }

                    expand_layout.addView(addView);

                    Model.query_launch = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

  /*  public void Download_file(final String file_path, final String extension) {

        try {

            final ProgressDialog dialog = new ProgressDialog(ExpandableActivity.this);

            downloadIdOne = PRDownloader.download(file_path, dirPath, "filename." + extension)

                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                            dialog.setMessage("please wait");
                            dialog.show();
                            dialog.setCancelable(false);
                        }
                    })

                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {
                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {
                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {


                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            dialog.cancel();

                            File imgFile = new File(dirPath + "/" + "filename." + extension);
                            System.out.println("imgFile--------------" + imgFile.toString());


                            if (extension.equals("pdf") || (extension.equals("PDF"))) {
                                //-------------------- webview -------------------------------------------
                                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                                i.putExtra("url", "file://" + imgFile.toString());
                                i.putExtra("type", "PDF View");
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                //-------------------- webview -------------------------------------------
                            } else if (extension.equals("jpg")
                                    || (extension.equals("JPG"))
                                    || (extension.equals("png"))
                                    || (extension.equals("PNG"))
                                    || (extension.equals("jpeg"))
                                    || (extension.equals("JPEG"))
                                    || (extension.equals("tiff"))
                            ) {
                                //-------------------- webview -------------------------------------------
                                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                                i.putExtra("url", "file://" + imgFile.toString());
                                i.putExtra("type", "Attachment View");
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                //-------------------- webview -------------------------------------------
                            } else {
                                ask_to_save(file_path);
                            }
                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public void ask_to_save(final String file_path) {

        try {

            final MaterialDialog alert = new MaterialDialog(ExpandableActivity.this);
            alert.setTitle("Downloading file..");
            alert.setMessage("This file can be viewed only after download. Do you want to download now?");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Download", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //--------------- Choose Folder -----------------------------------
                    final Context ctx = ExpandableActivity.this;
                    new ChooserDialog().with(ctx)
                            .withIcon(R.mipmap.ic_launcher)
                            .withFilter(true, false)
                            .withStartFile(_path)
                            .withDateFormat("HH:mm")
                            .withResources(R.string.choose, R.string.btn_ok,
                                    R.string.cancel)

                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(String path, File pathFile) {
                                    // Toast.makeText(ctx, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                    _path = path;
                                    System.out.println("_path---------" + _path);

                                    // Asked_Download_file(file_path, extension, _path);
                                }
                            })
                            .build()
                            .show();
                    //--------------- Choose Folder -----------------------------------

                    alert.dismiss();


                }
            });

            alert.setNegativeButton("No", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    public void Asked_Download_file(final String file_path, final String extension, final String dirPath_text) {

        try {
            downloadIdOne = PRDownloader.download(file_path, dirPath_text, "filename." + extension)
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {
                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {
                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            File imgFile = new File(dirPath_text + "/" + "filename." + extension);
                            System.out.println("imgFile--------------" + imgFile.toString());

                            System.out.println("File Saved--on------------" + imgFile.toString());

                            Toast.makeText(getApplicationContext(), "File downloaded successfully..!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
