package com.orane.icliniq.attachment_view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.flurry.android.FlurryAgent;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.orane.icliniq.Attachment_WebViewActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Model.Utils;
import com.orane.icliniq.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class GridViewActivity extends AppCompatActivity {

    private static final String TAG = GridViewActivity.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    //public HashMap<String, String> attach_files_map;
    public String attach_file_text, contentAsString;
    JSONObject jsonobj_files;
    String file_url, extension, file_attached_id;
    JSONArray jarray_files;
    StringBuilder total;
    Map<String, String> attach_map = new HashMap<String, String>();
    private static String dirPath;
    int downloadIdOne;
    private String _path;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attach_activity_gridview);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Attachment(s)");
        }
        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        try {
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.attachement_view");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent intent = getIntent();
            attach_file_text = intent.getStringExtra("filetxt");
            System.out.println("get attach_file_text---------- " + attach_file_text);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGridView = findViewById(R.id.gridView);
        mProgressBar = findViewById(R.id.progressBar);

        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.attach_grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        parseResult(attach_file_text);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position

                try {

                    GridItem item = (GridItem) parent.getItemAtPosition(position);
                    String url_text = item.getImage();
                    extension = attach_map.get(url_text);

                    System.out.println("url_text-------------" + url_text);
                    System.out.println("Extension-------------" + extension);

                    dirPath = Utils.getRootDirPath(getApplicationContext());
                    //dirPath = ""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    String file_full__url = "";

                    if ("?".contains(url_text)) {
                        file_full__url = url_text + "&token=" + Model.token + "&enc=1";
                    } else {
                        file_full__url = url_text + "?token=" + Model.token + "&enc=1";
                    }

                    System.out.println("file_full__url------------------- " + file_full__url);

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(file_full__url));
                    startActivity(i);


                    //Download_file(file_full__url, extension);



                   /* if (extension.equals("pdf")
                            || (extension.equals("PDF"))
                            || (extension.equals("xls"))
                            || (extension.equals("ppt"))
                            || (extension.equals("pps"))
                            || (extension.equals("pptx"))
                            || (extension.equals("ppsx"))
                            || (extension.equals("txt"))
                            || (extension.equals("rtf"))
                            ) {

                        //-------------------- webview -------------------------------------------
                        Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                        String file_full__url = "";
                        if ("?".contains(url_text)) {
                            file_full__url = url_text + "&token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                        } else {
                            file_full__url = url_text + "?token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                        }
                        System.out.println("file_full__url-------------" + file_full__url);
                        String final_url = URLEncoder.encode(file_full__url, "utf-8");
                        i.putExtra("url", "http://docs.google.com/viewer?url=" + final_url);
                        i.putExtra("type", "Attachment View");
                        //startActivity(i);

                        Download_file(file_full__url, extension);
                        //ask_to_save(file_full__url,extension);

                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        //-------------------- webview -------------------------------------------

                    } else {

                        //-------------------- Browser-------------------------------------------
                        String file_full__url = "";
                        if ("?".contains(url_text)) {
                            file_full__url = url_text + "&token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                        } else {
                            file_full__url = url_text + "?token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                        }
                        System.out.println("file_full__url-------------" + file_full__url);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(file_full__url));
                        //startActivity(i);

                        Download_file(file_full__url, extension);

                       *//* //--------------- Choose Folder -----------------------------------
                        final Context ctx = GridViewActivity.this;
                        new ChooserDialog().with(ctx)
                                .withIcon(R.mipmap.ic_launcher)
                                .withFilter(true, false)
                                .withStartFile(_path)
                                .withDateFormat("HH:mm")
                                .withResources(R.string.app_name, R.string.btn_ok,
                                        R.string.cancel)

                                .withChosenListener(new ChooserDialog.Result() {
                                    @Override
                                    public void onChoosePath(String path, File pathFile) {
                                        Toast.makeText(ctx, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                        _path = path;
                                        System.out.println("_path---------" + _path);
                                        //_tv.setText(_path);
                                    }
                                })
                                .build()
                                .show();
                        //--------------- Choose Folder -----------------------------------
*//*

                        //-------------------- Browser-------------------------------------------

                    }*/


                    //--------- Flurry ----------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.url:", url_text);
                    FlurryAgent.logEvent("Open_Attachemnt", articleParams);
                    //--------- Flurry ----------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void parseResult(String q_files_text) {
        try {

            GridItem item;

            if ((q_files_text.length()) > 2) {

                System.out.println("files_text------" + q_files_text);
                jarray_files = new JSONArray(q_files_text);

                for (int m = 0; m < jarray_files.length(); m++) {
                    jsonobj_files = jarray_files.getJSONObject(m);

                    System.out.println("jsonobj_files--" + m + " ----" + jsonobj_files.toString());

                    //String file_text = jsonobj_files.getString("file");
                    String ext_text = jsonobj_files.getString("ext");
                    file_url = jsonobj_files.getString("url");

                    //file_url = "https://www.icliniq.com/tools/downloadQfile/p/8nLl3jrv4KOy3kFIDjQyQRx1HlhjVNdiIhmiqPghmWXqIvwCQ%240Z%24DiZR-VjCUqf";

                    //System.out.println("file_text--------" + file_text);
                    System.out.println("ext_text--------" + ext_text);
                    System.out.println("file_url--------" + file_url);

                    attach_map.put(file_url, ext_text);

                    item = new GridItem();
                    item.setTitle("");
                    item.setExt(ext_text);
                    item.setImage(file_url);

                    mGridData.add(item);
                }

                for (Map.Entry<String, String> entry : attach_map.entrySet()) {

                    String key = entry.getKey();
                    String value = entry.getValue();

                    System.out.println("key-------------" + key);
                    System.out.println("value-------------" + value);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public String getUrl(String url) throws IOException {


        return null;
    }

    public String convertInputStreamToString(InputStream stream) throws IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total.toString();

    }


   /* public void Download_file(final String file_path, final String extension) {

        try {

            final ProgressDialog dialog = new ProgressDialog(GridViewActivity.this);

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
                                Intent i = new Intent(getApplicationContext(), Attachment_WebViewActivity.class);
                                i.putExtra("url", "http://drive.google.com/viewerng/viewer?embedded=true&url=file:///" + imgFile.toString());
                                //i.putExtra("url", "file:///storage/emulated/0/Android/data/com.orane.icliniq/files/filename.pdf");
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
                                Intent i = new Intent(getApplicationContext(), Attachment_WebViewActivity.class);
                                i.putExtra("url", "file:///" + imgFile.toString());
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

            final MaterialDialog alert = new MaterialDialog(GridViewActivity.this);
            alert.setTitle("Downloading file..");
            alert.setMessage("This file can be viewed only after download. Do you want to download now?");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Download", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //--------------- Choose Folder -----------------------------------
                    final Context ctx = GridViewActivity.this;
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

                                    Asked_Download_file(file_path, extension, _path);
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
    }
}
