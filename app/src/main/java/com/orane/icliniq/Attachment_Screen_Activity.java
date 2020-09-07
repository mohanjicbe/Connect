package com.orane.icliniq;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Model.MultipartEntity2;
import com.orane.icliniq.fileattach_library.DefaultCallback;
import com.orane.icliniq.fileattach_library.EasyImage;
import com.orane.icliniq.network.JSONParser;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


public class Attachment_Screen_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button btn_submit, unmute;
    EditText edt_feedback;
    public String family_list, str_response, feedback_val, edt_desc_text;
    JSONObject json, json_response_obj, jsonobj_postquery;
    public StringBuilder total;
    List<String> title_categories;
    Map<String, String> tit_map = new HashMap<String, String>();
    Spinner spinner_type;
    InputStream is = null;
    ImageView thumb_img;
    LinearLayout file_list;
    int serverResponseCode = 0;
    EditText edt_new_type, edt_desc;
    TextView tv_attach_url, tv_attach_id;
    String attach_status, item_type, item_id, attach_file_url, type_name, type_val, attach_filename, local_url, contentAsString, upLoadServerUri, last_upload_file, attach_id, attach_qid, upload_response, image_path, selectedfilename, type_nametype_val, cons_select_date, serverResponseMessage, selectedPath;
    Button btn_attach, btn_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachment_screen);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Attach your files");
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        item_id = intent.getStringExtra("item_id");
        item_type = intent.getStringExtra("item_type");

        System.out.println("Get item_id----" + item_id);
        System.out.println("Get item_type----" + item_type);
        //------ getting Values ---------------------------

        edt_desc = findViewById(R.id.edt_desc);
        edt_feedback = findViewById(R.id.edt_feedback);
        btn_submit = findViewById(R.id.btn_submit);
        spinner_type = findViewById(R.id.spinner_type);
        edt_new_type = findViewById(R.id.edt_new_type);
        btn_date = findViewById(R.id.btn_date);
        btn_attach = findViewById(R.id.btn_attach);
        file_list = findViewById(R.id.file_list);

        //add_rec_type();


        //-------------------------------------------------------------------
        String get_family_url = Model.BASE_URL + "sapp/getReportTypes?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("JSON_getTypes---------" + get_family_url);
        new JSON_getTypes().execute(get_family_url);
        //-------------------------------------------------------------------

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    System.out.println("selectedPath--------" + selectedPath);


                    type_name = spinner_type.getSelectedItem().toString();
                    type_val = tit_map.get(type_name);
                    System.out.println("type_val--------" + type_val);


                    if (selectedPath != null && !selectedPath.isEmpty() && !selectedPath.equals("null") && !selectedPath.equals("")) {

                        edt_desc_text = edt_desc.getText().toString();

                        System.out.println("edt_desc_text--------" + edt_desc_text);


                        if (edt_desc_text != null && !edt_desc_text.isEmpty() && !edt_desc_text.equals("null") && !edt_desc_text.equals("")) {

                            if (type_val != null && !type_val.isEmpty() && !type_val.equals("0") && !type_val.equals("")) {
                                if (cons_select_date != null && !cons_select_date.isEmpty() && !cons_select_date.equals("null") && !cons_select_date.equals("")) {

                                    if (type_val.equals("other")) {

                                        String new_type_text = edt_new_type.getText().toString();
                                        System.out.println("new_type_text--------" + new_type_text);

                                        if (new_type_text != null && !new_type_text.isEmpty() && !new_type_text.equals("null") && !new_type_text.equals("")) {

                                            new AsyncTask_fileupload().execute(selectedPath);

                                        } else {
                                            Toast.makeText(Attachment_Screen_Activity.this, "Please attach new report", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        new AsyncTask_fileupload().execute(selectedPath);
                                    }

                                } else {
                                    Toast.makeText(Attachment_Screen_Activity.this, "Please select the report date", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Attachment_Screen_Activity.this, "Please select the report type", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Attachment_Screen_Activity.this, "Please enter the description", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(Attachment_Screen_Activity.this, "Please attach the file", Toast.LENGTH_SHORT).show();
                    }



                    /*edt_desc_text = edt_desc.getText().toString();

                    System.out.println("type_name----------" + type_name);
                    System.out.println("type_val----------" + type_val);

                    json = new JSONObject();
                    json.put("item_id", qid);
                    json.put("item_type", "query");
                    json.put("attach_id", attach_id);
                    json.put("reportDesc", edt_desc_text);
                    json.put("reportType", type_val);
                    json.put("reportDate", cons_select_date);

                    System.out.println("Query Post json----------" + json.toString());

                    if (type_val.equals("other")) {
                        String new_type_text = edt_new_type.getText().toString();
                        json.put("reportTypeTxt", new_type_text);
                    }


                    new JSONPostQuery().execute(json);*/


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attach_dialog();

            }
        });

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                type_name = spinner_type.getSelectedItem().toString();
                type_val = tit_map.get(type_name);

                System.out.println("tit_name----------" + type_name);
                System.out.println("tit_val----------" + type_val);

                //----------------------------------------
                if (type_val.equals("other")) {
                    edt_new_type.setVisibility(View.VISIBLE);
                } else {
                    edt_new_type.setVisibility(View.GONE);
                }
                //----------------------------------------
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Attachment_Screen_Activity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);

                //dpd.setAccentColor(Color.parseColor("#9C27B0"));
                dpd.setTitle("Select Date");

                calendar.setTimeInMillis(System.currentTimeMillis());
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(Attachment_Screen_Activity.this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                datePickerDialog.setMaxDate(calendar);


                /*Calendar[] dates = new Calendar[300];
                for (int i = 1; i <= 300; i++) {
                    Calendar date = Calendar.getInstance();
                    date.add(Calendar.DATE, i);
                    dates[i - 1] = date;
                }

                dpd.setSelectableDays(dates);*/

/*                Calendar[] dates = new Calendar[13];
                for (int i = -6; i <= 6; i++) {
                    Calendar date = Calendar.getInstance();
                    date.add(Calendar.WEEK_OF_YEAR, i);
                    dates[i + 6] = date;
                }
                dpd.setHighlightedDays(dates);
*/

                // calendar.add(Calendar.DATE, 2);

                // dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });
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

    public void add_rec_type() {

        //------ Setting Height Spinner ----------------------------
        final List<String> title_categories = new ArrayList<String>();

        //title_categories.add("Select Title");

        title_categories.add("Mr.");
        tit_map.put("Mr.", "1");

        title_categories.add("Miss.");
        tit_map.put("Miss.", "2");

        title_categories.add("Mrs.");
        tit_map.put("Mrs.", "3");

        title_categories.add("Baby");
        tit_map.put("Baby", "5");


        ArrayAdapter<String> tit_dataAdapter = new ArrayAdapter<String>(Attachment_Screen_Activity.this, android.R.layout.simple_spinner_item, title_categories);
        tit_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(tit_dataAdapter);
        //---------------------------------------------
        //------ Setting Height Spinner ----------------------------
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        try {
            //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            cons_select_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            System.out.println("Cal Date------" + cons_select_date);

            //--------- for System -------------------
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
            Date dateObj = curFormater.parse(cons_select_date);
            String newDateStr = curFormater.format(dateObj);
            System.out.println("For System select_date---------" + newDateStr);
            //--------------------------------

            btn_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            System.out.println("cons_select_date---------" + cons_select_date);

        } catch (Exception e) {
            e.printStackTrace();
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

                    int permissionCheck = ContextCompat.checkSelfPermission(Attachment_Screen_Activity.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(Attachment_Screen_Activity.this, 0);
                    } else {
                        Nammu.askForPermission(Attachment_Screen_Activity.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(Attachment_Screen_Activity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(Attachment_Screen_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(Attachment_Screen_Activity.this, 0);
                    } else {
                        Nammu.askForPermission(Attachment_Screen_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(Attachment_Screen_Activity.this, 0);
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

    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(Attachment_Screen_Activity.this);
                dialog.setMessage("Uploading. Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

                //file_list.removeAllViews();

                JSONObject jObj = new JSONObject(upload_response);

                //attach_qid = jObj.getString("qid");
                attach_status = jObj.getString("status");
                //attach_file_url = jObj.getString("url");
                //attach_filename = jObj.getString("filename");

                if(jObj.has("attach_id")){
                    attach_id = jObj.getString("attach_id");

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("attach_status", attach_status);
                    params.putString("attach_id", attach_id);
                    Model.mFirebaseAnalytics.logEvent("AskQuery2_File_Upload", params);
                    //------------ Google firebase Analitics--------------------

                    System.out.println("attach_status-------" + attach_status);
                    System.out.println("attach_attach_id-------" + attach_id);

                    if (attach_status.equals("1")) {

                        try {

                            edt_desc_text = edt_desc.getText().toString();

                            System.out.println("type_name----------" + type_name);
                            System.out.println("type_val----------" + type_val);

                            json = new JSONObject();
                            json.put("item_id", item_id);
                            json.put("item_type", item_type);
                            json.put("attach_id", attach_id);
                            json.put("reportDesc", edt_desc_text);
                            json.put("reportType", type_val);
                            json.put("reportDate", cons_select_date);

                            System.out.println("Query Post json----------" + json.toString());

                            if (type_val.equals("other")) {
                                String new_type_text = edt_new_type.getText().toString();
                                json.put("reportTypeTxt", new_type_text);
                            }


                            new JSON_File_Desc_Submit().execute(json);

                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        //------------------------------------
                    } else {
                        Toast.makeText(Attachment_Screen_Activity.this, "Attaching failed. Please try again", Toast.LENGTH_SHORT).show();
                    }

                    last_upload_file = "";
                }
                else{

                }



            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }


  /*  public String upload_file(String fullpath) {

        String fpath_filename = fullpath.substring(fullpath.lastIndexOf("/") + 1);

        local_url = fullpath;

        System.out.println("fpath-------" + fullpath);
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
                upLoadServerUri = Model.BASE_URL + "/sapp/uploadHReports?item_id=" + item_id + "&item_type=" + item_type + "&user_id=" + (Model.id) + "&token=" + Model.token + "&enc=1";
                System.out.println("upLoadServerUri---------------------" + upLoadServerUri);

                FileInputStream fileInputStream = new FileInputStream(fullpath);
                System.out.println("fullpath---------------------------------" + fullpath);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                //conn.setRequestProperty("Connection", "Keep-Alive");
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

                serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();

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

    public String convertInputStreamToString(InputStream stream) throws IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
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


    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Attachment_Screen_Activity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> returnedPhotos) {

        //photos.addAll(returnedPhotos);
        file_list.removeAllViews();

        for (int i = 0; i < returnedPhotos.size(); i++) {
            System.out.println(returnedPhotos.get(i));

            System.out.println("File Name------------------" + (returnedPhotos.get(i)).getName());

            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();

            //tv_upload_filename.setText(selectedfilename);

/*            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.Qid", (attach_qid));
            articleParams.put("android.patient.attach_file_path", selectedPath);
            articleParams.put("android.patient.attach_filename", selectedfilename);
            FlurryAgent.logEvent("android.patient.Attach_Take_Photo", articleParams);
            //----------- Flurry -------------------------------------------------*/

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id);
            params.putString("Qid", attach_qid);
            params.putString("attach_file_path", selectedPath);
            params.putString("attach_filename", selectedfilename);
            Model.mFirebaseAnalytics.logEvent("Attach_Files", params);
            //------------ Google firebase Analitics--------------------


            //------------------------------------
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.upload_file_list, null);

            TextView tv_quest = addView.findViewById(R.id.tv_quest);
            ImageView close_button = addView.findViewById(R.id.close_button);
            thumb_img = addView.findViewById(R.id.imageView4);
            tv_attach_url = addView.findViewById(R.id.tv_attach_url);
            tv_attach_id = addView.findViewById(R.id.tv_attach_id);


            close_button.setVisibility(View.GONE);

            tv_quest.setText(selectedfilename);
            //tv_attach_id.setText(attach_id);
            //tv_attach_url.setText(attach_file_url);
            thumb_img.setImageBitmap(BitmapFactory.decodeFile(selectedPath));

            System.out.println("Model.upload_files-----------" + (last_upload_file));
            System.out.println("Model.attach_qid-----------" + (attach_qid));
            System.out.println("Model.attach_id-----------" + (attach_id));
            System.out.println("Model.attach_file_url-----------" + (attach_file_url));


            thumb_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //preview_image(local_url);
                }
            });

            file_list.addView(addView);

            //new AsyncTask_fileupload().execute(selectedPath);

        }

    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }


    private class JSON_getTypes extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Attachment_Screen_Activity.this);
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

                System.out.println("family_list----" + family_list);

                title_categories = new ArrayList<String>();

                JSONObject jtypes = new JSONObject(family_list);
                Iterator<String> iterator = jtypes.keys();

                title_categories.add("Select report type");
                tit_map.put("Select report type", "0");

                while (iterator.hasNext()) {
                    String key_text = iterator.next();
                    String value_text = jtypes.optString(key_text);

                    title_categories.add(key_text);
                    tit_map.put(key_text, value_text);
                }

                title_categories.add("Others");
                tit_map.put("Others", "other");

                ArrayAdapter<String> tit_dataAdapter = new ArrayAdapter<String>(Attachment_Screen_Activity.this, android.R.layout.simple_spinner_item, title_categories);
                tit_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_type.setAdapter(tit_dataAdapter);
                //---------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }


    private class JSON_File_Desc_Submit extends AsyncTask<JSONObject, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Attachment_Screen_Activity.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj_postquery = jParser.JSON_POST(urls[0], "new_file_desc_upload");

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                String status_postquery = jsonobj_postquery.getString("status");
                System.out.println("status_postquery---------------" + status_postquery);

                if (status_postquery.equals("1")) {

                    Toast.makeText(Attachment_Screen_Activity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();

                    finish();
                    Model.query_launch = "attached_file";

                } else if (status_postquery.equals("0")) {

                    String status_msg = jsonobj_postquery.getString("msg");
                    Toast.makeText(Attachment_Screen_Activity.this, status_msg, Toast.LENGTH_SHORT).show();

                    System.out.println("postquery Submit Failed---------------");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }


    private String upload_file(String file_path) {

        String ServerUploadPath = Model.BASE_URL + "/sapp/uploadHReports?item_id=" + item_id + "&item_type=" + item_type + "&user_id=" + (Model.id) + "&token=" + Model.token + "&enc=1";

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
