package com.orane.icliniq;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Model.MultipartEntity2;
import com.orane.icliniq.expand.ExpandableLayout;
import com.orane.icliniq.file_picking.utils.FileUtils;
import com.orane.icliniq.fileattach_library.DefaultCallback;
import com.orane.icliniq.fileattach_library.EasyImage;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class Consultation3 extends AppCompatActivity {


    View addView;
    GridView gridGallery;
    Handler handler;
    ArrayList<String> imagePaths;
    ImageView imgSinglePick;
    Button btnGalleryPick;
    Button btnGalleryPickMul;

    String action;
    public StringBuilder total;
    ViewSwitcher viewSwitcher;

    public String fee_q_inr, fee_q, persona_response, compmore, prevhist, curmedi, pastmedi, labtest, serverResponseMessage, selectedPath, inv_id, inv_fee, inv_strfee, status_postquery, persona_id_val, qid, sel_filename, last_upload_file, attach_status, attach_file_url, attach_filename, local_url, contentAsString, upLoadServerUri, attach_id, attach_qid, upload_response, image_path, selectedfilename;

    private File fileSelected;
    ImageView thumb_img;
    InputStream is = null;
    Uri selectedImageUri;
    JSONObject jsonobj1;
    LinearLayout expand_layout, layout_attachfile, file_list, takephoto_layout, browse_layout;
    public String family_list, str_response, consult_id, file_fileUrl_text, filename, cur_qid, files_List = "";
    Button btn_submit, btn_attach;
    int serverResponseCode = 0;
    public JSONObject prepay_jobject, json, jsonobj_postquery;
    Toolbar toolbar;
    TextView tvtit, tv_attach_id, tv_attach_url;
    View v;
    JSONObject jsonobj_prepinv;
    ScrollView scrollView1;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String first_query = "first_query_key";
    public static final String have_free_credit = "have_free_credit";
    SharedPreferences sharedpreferences;

    private static final int CAMERA_REQUEST = 1888;
    private static final String TAG = "FileChooserExampleActivity";
    private static final int REQUEST_CODE = 6384; // onActivityResult request

    EditText tv_compmore, tv_prevhist, tv_curmedi, tv_pastmedi, tv_labtest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation3);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        FlurryAgent.onPageView();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Attach Files");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //------------ Object Creations -------------------------------
        expand_layout = (LinearLayout) findViewById(R.id.expand_layout);
        btn_attach = (Button) findViewById(R.id.btn_attach);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        /*TextView tvattach = (TextView) findViewById(R.id.tvattach);*/
        tvtit = (TextView) findViewById(R.id.tvtit);

        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        layout_attachfile = (LinearLayout) findViewById(R.id.layout_attachfile);
        takephoto_layout = (LinearLayout) findViewById(R.id.takephoto_layout);
        browse_layout = (LinearLayout) findViewById(R.id.browse_layout);
        file_list = (LinearLayout) findViewById(R.id.file_list);

        tv_compmore = (EditText) findViewById(R.id.tv_compmore);
        tv_prevhist = (EditText) findViewById(R.id.tv_prevhist);
        tv_curmedi = (EditText) findViewById(R.id.tv_curmedi);
        tv_pastmedi = (EditText) findViewById(R.id.tv_pastmedi);
        tv_labtest = (EditText) findViewById(R.id.tv_labtest);


        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_attach)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.btn_attach)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_attached_files)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_note)).setTypeface(font_reg);
        ((Button) findViewById(R.id.btn_submit)).setTypeface(font_bold);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface tf = Typeface.createFromAsset(getAssets(), Model.font_name);

        tv_compmore.setTypeface(tf);
        tv_prevhist.setTypeface(tf);
        tv_curmedi.setTypeface(tf);
        tv_pastmedi.setTypeface(tf);
        tv_labtest.setTypeface(tf);


        //initImageLoader();
        //init();

   /*     //------------------ Initialize File Attachment ---------------------------------
        Nammu.init(this);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }
        EasyImage.configuration(this)
                .setImagesFolderName("Attachments")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(true);
        //------------------ Initialize File Attachment ---------------------------------*/

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        consult_id = intent.getStringExtra("consult_id");
        System.out.println("consult_id-----------" + consult_id);
        //------ getting Values ---------------------------

        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attach_dialog();

                Intent intent = new Intent(Consultation3.this, Attachment_Screen_Activity.class);
                intent.putExtra("item_id", consult_id);
                intent.putExtra("item_type", "booking");
                startActivity(intent);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    compmore = tv_compmore.getText().toString();
                    prevhist = tv_prevhist.getText().toString();
                    curmedi = tv_curmedi.getText().toString();
                    pastmedi = tv_pastmedi.getText().toString();
                    labtest = tv_labtest.getText().toString();

                    if (compmore.equals("")) {
                        compmore = "";
                    }
                    if (prevhist.equals("")) {
                        prevhist = "";
                    }
                    if (curmedi.equals("")) {
                        curmedi = "";
                    }
                    if (pastmedi.equals("")) {

                        pastmedi = "";
                    }
                    if (labtest.equals("")) {
                        labtest = "";
                    }

                    json = new JSONObject();
                    json.put("book_id", consult_id);
                    json.put("complaint_more", compmore);
                    json.put("p_history", prevhist);
                    json.put("c_medications", curmedi);
                    json.put("p_medications", pastmedi);
                    json.put("tests", labtest);

                    if (new NetCheck().netcheck(Consultation3.this)) {
                        new JSONPostQuery().execute(json);
                    }

                    //============================================================

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(first_query, "no");
                    Model.first_query = "no";
                    editor.apply();
                    //============================================================

                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Model.kiss.record("android.Patient.Query_Submit_Crash");
                        HashMap<String, String> properties = new HashMap<String, String>();
                        properties.put("android.Patient.Crash_ID", e.toString());
                        Model.kiss.set(properties);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }


            }
        });
    }

/*
    private void initImageLoader() {

        try {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                    new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();

        try {
            if (!(Model.upload_files).equals("")) {

                layout_attachfile.setVisibility(View.VISIBLE);
                //------------------------------------
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.upload_file_list, null);
                TextView tv_quest = (TextView) addView.findViewById(R.id.tv_quest);
                ImageView close_button = (ImageView) addView.findViewById(R.id.close_button);
                thumb_img = (ImageView) addView.findViewById(R.id.imageView4);
                tv_attach_url = (TextView) addView.findViewById(R.id.tv_attach_url);
                tv_attach_id = (TextView) addView.findViewById(R.id.tv_attach_id);

                tv_quest.setText(Model.upload_files);

                tv_attach_id.setText(Model.attach_id);
                tv_attach_url.setText(Model.attach_file_url);

                thumb_img.setImageBitmap(BitmapFactory.decodeFile(Model.attach_file_url));

                System.out.println("Model.upload_files-----------" + (Model.upload_files));
                System.out.println("Model.attach_qid-----------" + (Model.attach_qid));
                System.out.println("Model.attach_id-----------" + (Model.attach_id));
                System.out.println("Model.attach_file_url-----------" + (Model.attach_file_url));

                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        View parent = (View) v.getParent();
                        //View grand_parent = (View)parent.getParent();

                        tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                        String attid = tv_attach_id.getText().toString();

                        //--------------------- Remove Attachment ------------------------------
                        String url = Model.BASE_URL + "/sapp/removeQAttachment?user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token + "&enc=1";
                        System.out.println("Remover Attach url-------------" + url);
                        new JSON_remove_file().execute(url);
                        //--------------------- Remove Attachment ------------------------------

                        System.out.println("Removed attach_id-----------" + attid);
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });

                thumb_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // preview_image(Model.fule_full_path);
                    }
                });

                file_list.addView(addView);
                //------------------------------------------
            }


            if ((Model.query_launch).equals("attached_file")) {

                //-------------------------------------------------------------------
                String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + consult_id + "&item_type=booking&user_id=" + Model.id + "&token=" + Model.token;
                System.out.println("get_family_url---------" + get_family_url);
                new JSON_getFileList().execute(get_family_url);
                //-------------------------------------------------------------------
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        Model.upload_files = "";
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

    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation3.this);
            dialog.setMessage("Uploading. Please wait...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                //str_response = upload_file(urls[0]);
                str_response = upload_file(urls[0]);
                System.out.println("upload_response---------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                prepay_jobject = new JSONObject(str_response);

                if (prepay_jobject.has("token_status")) {
                    String token_status = prepay_jobject.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    JSONObject jObj = new JSONObject(str_response);

                    if (jObj.has("item_id")) {
                        attach_qid = jObj.getString("item_id");
                    }
                    if (jObj.has("status")) {
                        attach_status = jObj.getString("status");
                    }

                    System.out.println("attach_qid-------" + attach_qid);
                    System.out.println("attach_status-------" + attach_status);

                    if (!(last_upload_file).equals("")) {

                        scrollView1.setVisibility(View.VISIBLE);
                        layout_attachfile.setVisibility(View.VISIBLE);

                        //------------------------------------------------------
                        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.upload_file_list, null);
                        TextView tv_quest = (TextView) addView.findViewById(R.id.tv_quest);
                        ImageView close_button = (ImageView) addView.findViewById(R.id.close_button);
                        thumb_img = (ImageView) addView.findViewById(R.id.imageView4);
                        tv_attach_url = (TextView) addView.findViewById(R.id.tv_attach_url);
                        tv_attach_id = (TextView) addView.findViewById(R.id.tv_attach_id);

                        tv_quest.setText(last_upload_file);
                        tv_attach_id.setText("");
                        tv_attach_url.setText("");

                        thumb_img.setImageBitmap(BitmapFactory.decodeFile(local_url));

                        System.out.println("Model.upload_files-----------" + (last_upload_file));
                        System.out.println("Model.attach_qid-----------" + (attach_qid));

                        close_button.setVisibility(View.GONE);

                        file_list.addView(addView);
                        //------------------------------------

                    } else {
                        scrollView1.setVisibility(View.GONE);
                    }

                    last_upload_file = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation3.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();

            //-------------------------------------------------------------------
            String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + consult_id + "&item_type=booking&user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("get_family_url---------" + get_family_url);
            new JSON_getFileList().execute(get_family_url);
            //-------------------------------------------------------------------
        }
    }


    private class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Consultation3.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                jsonobj_prepinv = new JSONObject(str_response);
                if (jsonobj_prepinv.has("token_status")) {
                    String token_status = jsonobj_prepinv.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {


                    if (jsonobj_prepinv.has("token_status")) {
                        String token_status = jsonobj_prepinv.getString("token_status");
                        if (token_status.equals("0")) {
                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //============================================================
                            finishAffinity();
                            Intent intent = new Intent(Consultation3.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {

                        inv_id = jsonobj_prepinv.getString("id");
                        inv_fee = jsonobj_prepinv.getString("fee");
                        inv_strfee = jsonobj_prepinv.getString("str_fee");

                        System.out.println("jsonobj--------" + jsonobj_prepinv.toString());

                        System.out.println("inv_id--------" + (inv_id));
                        System.out.println("inv_fee--------" + (inv_fee));
                        System.out.println("inv_strfee--------" + (inv_strfee));

                        if (!(inv_id).equals("0")) {


                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.consult_id", consult_id);
                            articleParams.put("android.patient.inv_id", inv_id);
                            articleParams.put("android.patient.inv_fee", inv_fee);
                            articleParams.put("android.patient.inv_strfee", inv_strfee);
                            FlurryAgent.logEvent("android.patient.Prepare_Invoice", articleParams);
                            //----------- Flurry -------------------------------------------------

                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id);
                            params.putString("consult_id", consult_id);
                            params.putString("inv_id", inv_id);
                            params.putString("inv_fee", inv_fee);
                            params.putString("inv_strfee", inv_strfee);
                            Model.mFirebaseAnalytics.logEvent("Prepare_Invoice", params);
                            //------------ Google firebase Analitics--------------------

                            ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                            Model.query_launch = "Consult3";
                            Intent intent = new Intent(Consultation3.this, Invoice_Page_New.class);
                            intent.putExtra("qid", consult_id);
                            intent.putExtra("inv_id", inv_id);
                            intent.putExtra("inv_strfee", inv_strfee);
                            intent.putExtra("type", "consultation");
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                        } else {

                            //Toast.makeText(getApplicationContext(), "Sorry.. Something went wrong. please tryagain..", Toast.LENGTH_LONG).show();

                            Model.query_launch = "Consult3";
                            ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());
                            Intent intent = new Intent(Consultation3.this, ThankyouActivity.class);
                            intent.putExtra("type", "consultation");
                            startActivity(intent);
                            finish();
                        }
                        dialog.cancel();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /*public String upload_file(String fullpath) {

        String fpath_filename = fullpath.substring(fullpath.lastIndexOf("/") + 1);

        local_url = fullpath;
        System.out.println("fpath---------" + fullpath);
        System.out.println("fpath_filename---------" + fpath_filename);

        last_upload_file = fpath_filename;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(fullpath);

        if (!sourceFile.isFile()) {

            System.out.println("Source File not exist :" + fullpath);
            return "";

        } else {
            try {

                upLoadServerUri = Model.BASE_URL + "/sapp/upload4booking?item_id=" + consult_id + "&token=" + Model.token + "&enc=1";
                System.out.println("upLoadServerUri---------------------" + upLoadServerUri);


                FileInputStream fileInputStream = new FileInputStream(fullpath);
                System.out.println("fullpath---------------------------------" + fullpath);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fullpath);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + fullpath + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();


                // JSON Response on Server
                int response = conn.getResponseCode();
                System.out.println("response-------" + response);
                is = conn.getInputStream();
                contentAsString = convertInputStreamToString(is);
                System.out.println("Upload File Response-----------------" + contentAsString);

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return contentAsString;
        }
    }*/

   /* public String convertInputStreamToString(InputStream stream, int length) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }*/

    public String convertInputStreamToString(InputStream stream) throws IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
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

/*

    public void preview_image(String file_url) {

        try {

            sel_filename = file_url.substring(file_url.lastIndexOf("/") + 1);

            final MaterialDialog alert = new MaterialDialog(Consultation3.this);
            View view = LayoutInflater.from(Consultation3.this).inflate(R.layout.image_preview, null);
            alert.setView(view);
            alert.setTitle("");

            final ImageView image1 = (ImageView) view.findViewById(R.id.image1);
            final ImageView image_close = (ImageView) view.findViewById(R.id.image_close);
            final Button btn_upload = (Button) view.findViewById(R.id.btn_upload);


            System.out.println("sel_filename.endsWith(\".jpg\")----------" + file_url.endsWith(".jpg"));
            System.out.println("sel_filename.endsWith(\".pdf\")----------" + file_url.endsWith(".pdf"));

            image1.setImageBitmap(BitmapFactory.decodeFile(file_url));

            //------------ Audio & Video -----------------------------------
            if (sel_filename.endsWith(".mp3"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".wav"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4a"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4b"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4p"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".wma"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".dat"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".mpeg"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".mp4"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            //------------ Audio & Video -----------------------------------

            if (sel_filename.endsWith(".pdf")) image1.setBackgroundResource(R.mipmap.pdf_file);
            if (sel_filename.endsWith(".doc")) image1.setBackgroundResource(R.mipmap.text_file);
            if (sel_filename.endsWith(".xls")) image1.setBackgroundResource(R.mipmap.text_file);
            if (sel_filename.endsWith(".zip")) image1.setBackgroundResource(R.mipmap.zip_file);
            if (sel_filename.endsWith(".rar")) image1.setBackgroundResource(R.mipmap.zip_file);
            //------------ Doc -----------------------------------


            image_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alert.dismiss();
                }
            });

            alert.setCanceledOnTouchOutside(true);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Photo");
        mAnimals.add("Browse Files");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Files/Images");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Photo")) {

                    int permissionCheck = ContextCompat.checkSelfPermission(Consultation3.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(Consultation3.this, 0);
                    } else {
                        Nammu.askForPermission(Consultation3.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(Consultation3.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(Consultation3.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(Consultation3.this, 0);
                    } else {
                        Nammu.askForPermission(Consultation3.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(Consultation3.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                }
            }
        });
        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }

    public static void dumpIntent(Intent i) {

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println("Data------>" + "[" + key + "=" + bundle.get(key) + "]");
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void showChooser() {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case CAMERA_REQUEST:
                // ------------- Take Photo -------------------------------
                if (resultCode == RESULT_OK) {

                    Bitmap mphoto = (Bitmap) data.getExtras().get("data");

                    Uri tempUri = getImageUri(getApplicationContext(), mphoto);
                    selectedPath = getPath(tempUri);
                    selectedfilename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);

                    System.out.println("selectedPath-------" + selectedPath);
                    System.out.println("selectedfilename-------" + selectedfilename);
                    dumpIntent(data);

                    //----------------- Kissmetrics ----------------------------------
                    Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                    Model.kiss.record("android.patient.Attach_Take_Photo");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.patient.Qid", (attach_qid));
                    properties.put("android.patient.attach_file_path", selectedPath);
                    properties.put("android.patient.attach_filename", selectedfilename);
                    Model.kiss.set(properties);
                    //----------------- Kissmetrics ----------------------------------
                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.Qid", (attach_qid));
                    articleParams.put("android.patient.attach_file_path", selectedPath);
                    articleParams.put("android.patient.attach_filename", selectedfilename);
                    FlurryAgent.logEvent("android.patient.Attach_Take_Photo", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    params.putString("Qid", attach_qid);
                    params.putString("attach_file_path", selectedPath);
                    params.putString("attach_filename", selectedfilename);
                    Model.mFirebaseAnalytics.logEvent("Attach_Take_Photo", params);
                    //------------ Google firebase Analytics--------------------

                    new AsyncTask_fileupload().execute(selectedPath);

                }

                // ------------- Take Photo -------------------------------

                break;


            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        try {
                            final String path = FileUtils.getPath(this, uri);
                            Toast.makeText(Consultation3.this, "File Selected: " + path, Toast.LENGTH_LONG).show();

                            //----------------- Kissmetrics ----------------------------------
                            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                            Model.kiss.record("android.patient.Consultation_Attach_Images");
                            HashMap<String, String> properties = new HashMap<String, String>();
                            properties.put("android.patient.qid", (attach_qid));
                            properties.put("android.patient.attach_file_path", path);
                            Model.kiss.set(properties);
                            //----------------- Kissmetrics ----------------------------------
                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.qid", (attach_qid));
                            articleParams.put("android.patient.attach_file_path", path);
                            FlurryAgent.logEvent("android.patient.Consultation_Attach_Images", articleParams);
                            //----------- Flurry -------------------------------------------------

                            //------------ Google firebase Analitics-----------------------------------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id);
                            params.putString("Qid", attach_qid);
                            params.putString("attach_file_path", path);
                            Model.mFirebaseAnalytics.logEvent("Consultation_Attach_Images", params);
                            //------------ Google firebase Analitics-----------------------------------------------

                            new AsyncTask_fileupload().execute(path);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
                System.out.println("Selected file------------" + source.toString());

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Consultation3.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }*/
/*

    private void onPhotosReturned(List<File> returnedPhotos) {

        //photos.addAll(returnedPhotos);

        for (int i = 0; i < returnedPhotos.size(); i++) {
            System.out.println(returnedPhotos.get(i));

            System.out.println("File Name------------------" + (returnedPhotos.get(i)).getName());

            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();

            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Attach_Take_Photo");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("android.patient.Qid", (attach_qid));
            properties.put("android.patient.attach_file_path", selectedPath);
            properties.put("android.patient.attach_filename", selectedfilename);
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------

            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.Qid", (attach_qid));
            articleParams.put("android.patient.attach_file_path", selectedPath);
            articleParams.put("android.patient.attach_filename", selectedfilename);
            FlurryAgent.logEvent("android.patient.Attach_Take_Photo", articleParams);
            //----------- Flurry -------------------------------------------------

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id);
            params.putString("Qid", attach_qid);
            params.putString("attach_file_path", selectedPath);
            params.putString("attach_filename", selectedfilename);
            Model.mFirebaseAnalytics.logEvent("Attach_Files", params);
            //------------ Google firebase Analitics--------------------

            new AsyncTask_fileupload().execute(selectedPath);

        }

    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }
*/


    private class JSON_getFileList extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation3.this);
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

                JSONArray det_jarray = new JSONArray(det_text);

                expand_layout.removeAllViews();


                for (int i = 0; i < det_jarray.length(); i++) {
                    jsonobj1 = det_jarray.getJSONObject(i);
                    System.out.println("jsonobj_first-----" + jsonobj1.toString());

                    String reportsLabel_text = jsonobj1.getString("reportsLabel");
                    String data_text = jsonobj1.getString("data");

                    JSONArray data_jarray = new JSONArray(data_text);

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.attachment_expand_view, null);

                    final LinearLayout expand_inner_view = (LinearLayout) addView.findViewById(R.id.expand_inner_view);
                    TextView tv_att_title = (TextView) addView.findViewById(R.id.tv_att_title);
                    final ImageView img_right_arrow = (ImageView) addView.findViewById(R.id.img_right_arrow);

                    //img_right_arrow.setImageResource(R.mipmap.down_icon);
                    tv_att_title.setText(reportsLabel_text);

                    ExpandableLayout expandableLayout = (ExpandableLayout) addView.findViewById(R.id.expandable_layout);

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
                        final String fileUrl_text = file_jobj.getString("fileUrl");
                        String isDelete_text = file_jobj.getString("isDelete");
                        String reportDesc = file_jobj.getString("reportDesc");

                        //------------------------------------
                        LayoutInflater layoutInflater2 = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView2 = layoutInflater2.inflate(R.layout.attachment_expand_view2, null);

                        TextView tv_summ_title2 = (TextView) addView2.findViewById(R.id.tv_summ_title2);
                        TextView tv_desc_summary_text = (TextView) addView2.findViewById(R.id.tv_desc_summary_text);
                        Button btn_open = (Button) addView2.findViewById(R.id.btn_open);
                        Button btn_remove = (Button) addView2.findViewById(R.id.btn_remove);


                        tv_summ_title2.setText("File : " + (j + 1) + " Dated on " + reportDate_text);
                        tv_desc_summary_text.setText(reportDesc);
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


                        btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            /*    View parent = (View) v.getParent();
                                //View grand_parent = (View)parent.getParent();

                                tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                                String attid = tv_attach_id.getText().toString();*/

                                //---------------------------
                                String url = Model.BASE_URL + "/sapp/removeHReportData?item_id=" + consult_id + "&item_type=booking&attach_id=" + attach_id_text + "&user_id=" + (Model.id) + "&attach_id=" + attach_id_text + "&token=" + Model.token;
                                System.out.println("Remover Attach url-------------" + url);
                                new JSON_remove_file().execute(url);
                                //---------------------------

                                // System.out.println("Removed attach_id-----------" + attid);
                                 //((LinearLayout) addView.getParent()).removeView(addView);
                            }
                        });

                        btn_open.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if ("?".contains(fileUrl_text)) {
                                    file_fileUrl_text = fileUrl_text + "&user_id=" + Model.id + "&token=" + Model.token + "&enc=1";
                                } else {
                                    file_fileUrl_text = fileUrl_text + "?user_id=" + Model.id + "&token=" + Model.token + "&enc=1";
                                }


                             /*   Intent intent = new Intent(Consultation3.this, WebViewActivity.class);
                                intent.putExtra("url", file_fileUrl_text);
                                intent.putExtra("type", "attachment");
                                startActivity(intent);*/


                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(file_fileUrl_text));
                                startActivity(i);
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


    private class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation3.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj_postquery = jParser.JSON_POST(urls[0], "extraCons");

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                //-------------------------------------------
                String url = Model.BASE_URL + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=consult&item_id=" + consult_id + "&token=" + Model.token + "&enc=1";
                System.out.println("Prepare Invoice url-------------" + url);
                new JSON_Prepare_inv().execute(url);
                //-------------------------------------------
                ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }


    private String upload_file(String file_path) {

        String ServerUploadPath = Model.BASE_URL + "/sapp/upload4booking?item_id=" + consult_id + "&token=" + Model.token + "&enc=1";
        System.out.println("upLoadServerUri---------------------" + upLoadServerUri);


        System.out.println("ServerUploadPath-------------" + ServerUploadPath);
        System.out.println("file_path-------------" + file_path);

        File file_value = new File(file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(ServerUploadPath);
            MultipartEntity2 reqEntity = new MultipartEntity2();
            reqEntity.addPart("file", file_value);
            post.setEntity(reqEntity);

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();

            try {
                final String response_str = EntityUtils.toString(resEntity);

                if (resEntity != null) {
                    System.out.println("response_str-------" + response_str);
                    contentAsString =response_str;

                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }

        return  contentAsString;
    }

}
