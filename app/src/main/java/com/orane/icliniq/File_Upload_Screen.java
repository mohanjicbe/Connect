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
import android.text.Html;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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


public class File_Upload_Screen extends AppCompatActivity {


    View addView;
    GridView gridGallery;
    Handler handler;
    ImageView imgSinglePick;
    Button btnGalleryPick;
    Button btnGalleryPickMul;
    JSONObject json_getfee, docprof_jsonobj;
    String action, fee_q, fee_q_inr;
    ViewSwitcher viewSwitcher;

    InputStream is = null;
    int serverResponseCode = 0;
    ArrayList<String> imagePaths;
    ImageView thumb_img;
    View recc_vi;
    private static final int FILE_SELECT_CODE = 0;
    Uri selectedImageUri;
    LinearLayout layout_attachfile, file_list, takephoto_layout, browse_layout;
    public String str_response, compmore, prevhist, curmedi, pastmedi, labtest, serverResponseMessage, selectedPath, inv_id, inv_fee, inv_strfee, status_postquery, qid, sel_filename, last_upload_file, attach_status, attach_file_url, attach_filename, local_url, contentAsString, upLoadServerUri, attach_id, attach_qid, upload_response, image_path, selectedfilename;
    Button btn_attach, btn_submit;
    public JSONObject upload_jsonobj, jsonobj_prepinv, json;
    Toolbar toolbar;
    ScrollView scrollView1;
    TextView tvtit, tvmore, tv_attach_id, tv_attach_url;

    private static final String TAG = "FileChooserExampleActivity";
    private static final int REQUEST_CODE = 6384; // onActivityResult request

    EditText tv_compmore, tv_prevhist, tv_curmedi, tv_pastmedi, tv_labtest;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String first_query = "first_query_key";
    public static final String have_free_credit = "have_free_credit";
    SharedPreferences sharedpreferences;
    private static final int CAMERA_REQUEST = 1888;

    TextView tv_attach_warn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload_screen);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface tf = Typeface.createFromAsset(getAssets(), Model.font_name);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Attach Files");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        TextView tvattach = (TextView) findViewById(R.id.tvattach);
        tvtit = (TextView) findViewById(R.id.tvtit);
        tvattach.setTypeface(tf);
        tvtit.setTypeface(tf);

        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        btn_attach = (Button) findViewById(R.id.btn_attach);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        layout_attachfile = (LinearLayout) findViewById(R.id.layout_attachfile);
        takephoto_layout = (LinearLayout) findViewById(R.id.takephoto_layout);
        browse_layout = (LinearLayout) findViewById(R.id.browse_layout);
        file_list = (LinearLayout) findViewById(R.id.file_list);
        tv_attach_warn = (TextView) findViewById(R.id.tv_attach_warn);

        try {
            //------ getting Values ---------------------------
            Intent intent = getIntent();
            qid = intent.getStringExtra("qid");
            System.out.println("Get Intent qid-----" + qid);
            //------ getting Values ---------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

        initImageLoader();
        //init();

        try {
            json_getfee = new JSONObject();
            json_getfee.put("user_id", (Model.id));
            json_getfee.put("item_type", "single_query");

            System.out.println("json_getfee---" + json_getfee.toString());

            new JSON_getFee().execute(json_getfee);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //------------------ Initialize File Attachment ---------------------------------
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
        //------------------ Initialize File Attachment ---------------------------------


        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attach_dialog();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    if (new NetCheck().netcheck(File_Upload_Screen.this)) {
                        String url = (Model.BASE_URL) + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=query&item_id=" + qid + "&token=" + Model.token + "&enc=1";
                        System.out.println("File Attach Screen Prepare Invoice url-------------" + url);
                        new JSON_Prepare_inv().execute(url);
                    } else {
                        Toast.makeText(File_Upload_Screen.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initImageLoader() {

      /*  DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);*/
    }


    @Override
    protected void onResume() {

        super.onResume();
        System.out.println("Model.upload_files--------------" + Model.upload_files);

        if (!(Model.upload_files).equals("")) {

            //------------------------------------
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.upload_file_list, null);
            TextView tv_quest = (TextView) addView.findViewById(R.id.tv_quest);
            ImageView close_button = (ImageView) addView.findViewById(R.id.close_button);
            thumb_img = (ImageView) addView.findViewById(R.id.imageView4);
            tv_attach_url = (TextView) addView.findViewById(R.id.tv_attach_url);
            tv_attach_id = (TextView) addView.findViewById(R.id.tv_attach_id);

            tv_quest.setText(Model.upload_files);
            tv_attach_id.setText(Model.attach_id);
            tv_attach_url.setText(Model.attach_file_url);
            thumb_img.setImageBitmap(BitmapFactory.decodeFile(Model.local_file_url));

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

                    String att_url = tv_attach_url.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(att_url));
                    startActivity(i);

                }
            });

            file_list.addView(addView);
            //Model.upload_files = "";
            //------------------------------------
        }

        Model.upload_files = "";
    }


    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("resultCode------------" + resultCode);
        System.out.println("requestCode------------" + requestCode);

        imagePaths = new ArrayList<String>();

        try {


            // ------------- Take Photo -------------------------------
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
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

                new AsyncTask_fileupload().execute(selectedPath);

            }
            // ------------- Take Photo -------------------------------

            // ------------ Browse Photo -------------------------------
            if (requestCode == 110) {

                //adapter.clear();

                if (data.getData() != null) {
                    selectedImageUri = data.getData();

                    selectedPath = getPath(selectedImageUri);
                    selectedfilename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);

                    System.out.println("selectedPath-------" + selectedPath);
                    System.out.println("selectedfilename-------" + selectedfilename);
                }

                viewSwitcher.setDisplayedChild(1);
                String single_path = data.getStringExtra("single_path");
                imagePaths.add(single_path);
                imageLoader.displayImage("file://" + single_path, imgSinglePick);

                image_path = single_path;
                System.out.println("single_path------" + single_path);

                //----------------- Kissmetrics ----------------------------------
                Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                Model.kiss.record("android.patient.Attach_Images");
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("android.patient.Qid", (attach_qid));
                properties.put("android.patient.attach_file_path", single_path);
                Model.kiss.set(properties);
                //----------------- Kissmetrics ----------------------------------

                new AsyncTask_fileupload().execute(single_path);
            }
        } catch (Exception e) {
            System.out.println("Exception Attachments----" + e.toString());
        }
    }
*/
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // getMenuInflater().inflate(R.menu.ask_menu, menu);
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


    class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(File_Upload_Screen.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj_prepinv = jParser.getJSONFromUrl(urls[0]);

                System.out.println("jsonobj--------" + jsonobj_prepinv.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {


            try {

                if (jsonobj_prepinv.has("token_status")) {
                    String token_status = jsonobj_prepinv.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(File_Upload_Screen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    inv_id = jsonobj_prepinv.getString("id");
                    inv_fee = jsonobj_prepinv.getString("fee");
                    inv_strfee = jsonobj_prepinv.getString("str_fee");

                    System.out.println("inv_id--------" + inv_id);
                    System.out.println("inv_fee--------" + (inv_fee));
                    System.out.println("inv_strfee--------" + inv_strfee);

                    if (inv_id.equals("0")) {
                        System.out.println("inv_id--zero-" + inv_id);
                        try {

                            Model.have_free_credit = "0";
                            Model.first_query = "no";

                            //----------------- Kissmetrics ----------------------------------
                            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                            Model.kiss.record("android.patient.Free_Followup_Submit_Success");
                            HashMap<String, String> properties = new HashMap<String, String>();
                            properties.put("Query_id:", qid);
                            properties.put("Invoice_id:", inv_id);
                            properties.put("Invoice_fee:", inv_strfee);
                            Model.kiss.set(properties);
                            //----------------- Kissmetrics ----------------------------------
                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("Query_id:", qid);
                            articleParams.put("Invoice_id:", inv_id);
                            articleParams.put("Invoice_fee:", inv_strfee);
                            FlurryAgent.logEvent("android.patient.Free_Followup_Submit_Success", articleParams);
                            //----------- Flurry -------------------------------------------------

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(have_free_credit, "0");
                            editor.putString(first_query, "no");
                            editor.apply();
                            //============================================================


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("query_id--------------" + qid);
                        Intent i = new Intent(File_Upload_Screen.this, QueryViewActivity.class);
                        i.putExtra("qid", qid);
                        startActivity(i);
                        finish();

                    } else {

                        Model.have_free_credit = "0";

                        try {
                            //----------------- Kissmetrics ----------------------------------
                            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                            Model.kiss.record("android.patient.Paid_Followup_Submit_Success");
                            HashMap<String, String> properties = new HashMap<String, String>();
                            properties.put("Query_id:", qid);
                            properties.put("Invoice_id:", inv_id);
                            properties.put("Invoice_fee:", inv_strfee);
                            Model.kiss.set(properties);
                            //----------------- Kissmetrics ----------------------------------

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("Query_id:", qid);
                            articleParams.put("Invoice_id:", inv_id);
                            articleParams.put("Invoice_fee:", inv_strfee);
                            FlurryAgent.logEvent("android.patient.Paid_Followup_Submit_Success", articleParams);
                            //----------- Flurry -------------------------------------------------

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(File_Upload_Screen.this, Invoice_Page_New.class);
                        intent.putExtra("qid", qid);
                        intent.putExtra("inv_id", inv_id);
                        intent.putExtra("inv_strfee", inv_strfee);
                        intent.putExtra("type", "query");
                        startActivity(intent);
                        finish();

                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }

               /* if (!inv_id.equals("0")) {

                    Model.have_free_credit = "0";
                    try {
                        //----------------- Kissmetrics ----------------------------------
                        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                        Model.kiss.record("android.patient.Paid_Followup_Submit_Success");
                        HashMap<String, String> properties = new HashMap<String, String>();
                        properties.put("Query_id:", qid);
                        properties.put("Invoice_id:", inv_id);
                        properties.put("Invoice_fee:", inv_strfee);
                        Model.kiss.set(properties);
                        //----------------- Kissmetrics ----------------------------------
                    } catch (Exception e) {
                        System.out.println("Exception Kissmetrics--" + e.toString());
                    }

                    Intent intent = new Intent(File_Upload_Screen.this, Invoice_Page_New.class);
                    intent.putExtra("qid", qid);
                    intent.putExtra("inv_id", inv_id);
                    intent.putExtra("inv_strfee", inv_strfee);
                    startActivity(intent);
                    finish();

                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


                } else {

                    System.out.println("inv_id--zero-" + inv_id);
                    try {
                        Model.have_free_credit = "0";
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(have_free_credit, "0");
                        editor.apply();
                        //============================================================
                        //============================================================
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedpreferences.edit();
                        editor2.putString(first_query, "no");
                        Model.first_query = "no";
                        editor.apply();
                        //============================================================

                        //----------------- Kissmetrics ----------------------------------
                        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                        Model.kiss.record("android.patient.Free_Followup_Submit_Success");
                        HashMap<String, String> properties = new HashMap<String, String>();
                        properties.put("Query_id:", qid);
                        properties.put("Invoice_id:", inv_id);
                        properties.put("Invoice_fee:", inv_strfee);
                        Model.kiss.set(properties);
                        //----------------- Kissmetrics ----------------------------------
                    } catch (Exception e) {
                        System.out.println("Exception Kissmetrics-3-" + e.toString());
                    }


                    System.out.println("query_id--------------" + qid);
                    Intent i = new Intent(File_Upload_Screen.this, QueryViewActivity.class);
                    i.putExtra("qid", qid);
                    startActivity(i);
                    finish();
                }
*/
                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(File_Upload_Screen.this);
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

            try {
                upload_jsonobj = new JSONObject(str_response);
                System.out.println("upload_jsonobj-----------" + upload_jsonobj.toString());


                if (upload_jsonobj.has("token_status")) {
                    String token_status = upload_jsonobj.getString("token_status");
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
                    System.out.println("File uploaded successfully");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }

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

                    int permissionCheck = ContextCompat.checkSelfPermission(File_Upload_Screen.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(File_Upload_Screen.this, 0);
                    } else {
                        Nammu.askForPermission(File_Upload_Screen.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(File_Upload_Screen.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(File_Upload_Screen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(File_Upload_Screen.this, 0);
                    } else {
                        Nammu.askForPermission(File_Upload_Screen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(File_Upload_Screen.this, 0);
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

    class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(File_Upload_Screen.this);
            dialog.setMessage("Uploading. Please wait...");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                upload_response = upload_file(urls[0]); //ok
                System.out.println("upload_response---------" + upload_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                JSONObject jObj = new JSONObject(upload_response);

                attach_qid = jObj.getString("qid");
                attach_status = jObj.getString("status");
                attach_file_url = jObj.getString("url");
                attach_filename = jObj.getString("filename");
                attach_id = jObj.getString("attach_id");


                System.out.println("attach_qid-------" + attach_qid);
                System.out.println("attach_status-------" + attach_status);
                System.out.println("attach_file_url-------" + attach_file_url);
                System.out.println("attach_filename-------" + attach_filename);
                System.out.println("attach_attach_id-------" + attach_id);
                System.out.println("last_upload_file--------------" + last_upload_file);

                if (!(last_upload_file).equals("")) {

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.upload_file_list, null);
                    TextView tv_quest = (TextView) addView.findViewById(R.id.tv_quest);
                    ImageView close_button = (ImageView) addView.findViewById(R.id.close_button);
                    thumb_img = (ImageView) addView.findViewById(R.id.imageView4);
                    tv_attach_url = (TextView) addView.findViewById(R.id.tv_attach_url);
                    tv_attach_id = (TextView) addView.findViewById(R.id.tv_attach_id);

                    tv_quest.setText(last_upload_file);
                    tv_attach_id.setText(attach_id);
                    tv_attach_url.setText(attach_file_url);

                    thumb_img.setImageBitmap(BitmapFactory.decodeFile(local_url));

                    System.out.println("Model.upload_files-----------" + (last_upload_file));
                    System.out.println("Model.attach_qid-----------" + (attach_qid));
                    System.out.println("Model.attach_id-----------" + (attach_id));
                    System.out.println("Model.attach_file_url-----------" + (attach_file_url));

                    close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent = (View) v.getParent();
                            //View grand_parent = (View)parent.getParent();

                            tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                            String attid = tv_attach_id.getText().toString();

                            String url = Model.BASE_URL + "/sapp/removeQAttachment?user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token + "&enc=1";
                            System.out.println("Remover Attach url-------------" + url);
                            new JSON_remove_file().execute(url);

                            System.out.println("Removed attach_id-----------" + attid);
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });

                    thumb_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
/*
                            String att_url = tv_attach_url.getText().toString();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(att_url));
                            startActivity(i);
                            */
                            preview_image(local_url);

                        }
                    });

                    layout_attachfile.setVisibility(View.VISIBLE);

                    file_list.addView(addView);
                    //------------------------------------

                } else {
                    //scrollView1.setVisibility(View.GONE);
                }
                last_upload_file = "";

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }


   /* public String upload_file(String fullpath) {

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

                upLoadServerUri = Model.BASE_URL + "/sapp/upload?qid=" + qid + "&token=" + (Model.token);
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
                int length = 25500;
                contentAsString = convertInputStreamToString(is, length);
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

    public String convertInputStreamToString(InputStream stream, int length) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }

    public void preview_image(String file_url) {
/*
        try {

            sel_filename = file_url.substring(file_url.lastIndexOf("/") + 1);

            final MaterialDialog alert = new MaterialDialog(File_Upload_Screen.this);
            View view = LayoutInflater.from(File_Upload_Screen.this).inflate(R.layout.image_preview, null);
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
            System.out.println("Exception File Preview----" + e.toString());
        }*/
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

  /*  @Override
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
                            Toast.makeText(File_Upload_Screen.this, "File Selected: " + path, Toast.LENGTH_LONG).show();

                            //----------------- Kissmetrics ----------------------------------
                            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                            Model.kiss.record("android.patient.Browse_Images");
                            HashMap<String, String> properties = new HashMap<String, String>();
                            properties.put("android.patient.qid", (attach_qid));
                            properties.put("android.patient.attach_file_path", path);
                            Model.kiss.set(properties);
                            //----------------- Kissmetrics ----------------------------------

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.patient.qid", (attach_qid));
                            articleParams.put("android.patient.attach_file_path", path);
                            FlurryAgent.logEvent("android.patient.Browse_Images", articleParams);
                            //----------- Flurry -------------------------------------------------

                            new AsyncTask_fileupload().execute(path);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/

    @Override
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(File_Upload_Screen.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

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


    private class JSON_getFee extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                JSONParser jParser = new JSONParser();
                docprof_jsonobj = jParser.JSON_POST(urls[0], "getqFee");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + docprof_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (docprof_jsonobj.has("token_status")) {
                    String token_status = docprof_jsonobj.getString("token_status");
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

                    fee_q_inr = docprof_jsonobj.getString("fee_val");
                    fee_q = docprof_jsonobj.getString("str_fee");

                    //tvqfee.setText("(" + fee_q + ")");


                    //---------------------------------------------------------------------------
                    if ((Model.have_free_credit).equals("1")) {
                        tv_attach_warn.setVisibility(View.VISIBLE);

                        String warn_text = "Note : Doctors take more effort to read your reports/photos. So queries with reports have to be posted as a Paid query <b>" + fee_q + "</b>";
                        tv_attach_warn.setText(Html.fromHtml(warn_text));

                    } else {
                        tv_attach_warn.setVisibility(View.GONE);
                    }
                    //---------------------------------------------------------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private String upload_file(String file_path) {

        String ServerUploadPath  = Model.BASE_URL + "/sapp/upload?qid=" + qid + "user_id="+ Model.id + "&token=" + (Model.token);

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
