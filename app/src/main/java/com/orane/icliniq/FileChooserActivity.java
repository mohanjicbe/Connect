package com.orane.icliniq;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Constants;
import com.orane.icliniq.Model.FileInfo;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Model.MultipartEntity2;
import com.orane.icliniq.adapter.FileArrayAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class FileChooserActivity extends ListActivity {

    private File currentFolder;
    private FileArrayAdapter fileArrayListAdapter;
    private FileFilter fileFilter;
    private File fileSelected;
    private ArrayList<String> extensions;
    public String sel_filename, fname, fext;
    private final String[] okFileExtensions = new String[]{"jpg", "png", "gif", "jpeg"};
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    public JSONObject json;
    String upLoadServerUri = null;
    public String fule_full_path, contentAsString, serverResponseMessage, cur_qid, local_url, last_upload_file, upload_response, attach_qid, attach_status, attach_file_url, attach_filename, attach_id;
    InputStream is = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        try {
            Intent intent = getIntent();
            cur_qid = intent.getStringExtra("cur_qid");
        } catch (Exception e) {
            System.out.println("One Exception---------" + e.toString());
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras
                    .getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS) != null) {
                extensions = extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS);
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
        fileArrayListAdapter = new FileArrayAdapter(FileChooserActivity.this,
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

            Model.fule_full_path = fileSelected.getAbsolutePath();
            fule_full_path = fileSelected.getAbsolutePath();

            sel_filename = (fileSelected.getAbsolutePath()).substring((fileSelected.getAbsolutePath()).lastIndexOf("/") + 1);

            System.out.println("fileSelected.getAbsolutePath()----------" + fileSelected.getAbsolutePath());
            System.out.println("fileSelected)----------" + sel_filename);

            //=======================================================
            final MaterialDialog alert = new MaterialDialog(FileChooserActivity.this);
            View view = LayoutInflater.from(FileChooserActivity.this).inflate(R.layout.image_preview, null);
            alert.setView(view);
            alert.setTitle("");

            final ImageView image1 = (ImageView) view.findViewById(R.id.image1);
           /* final ImageView image_close = (ImageView) view.findViewById(R.id.image_close);
            final Button btn_upload = (Button) view.findViewById(R.id.btn_upload);
*/

            System.out.println("sel_filename.endsWith(\".jpg\")----------" + sel_filename.endsWith(".jpg"));
            System.out.println("sel_filename.endsWith(\".pdf\")----------" + sel_filename.endsWith(".pdf"));

            image1.setBackgroundResource(R.mipmap.text_file);

           /* image_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AsyncTask_fileupload().execute(fule_full_path);

                    alert.dismiss();
                }
            });*/

            alert.setCanceledOnTouchOutside(true);
            alert.show();
            //===============================================================
        }
    }

    class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(FileChooserActivity.this);
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

                Model.attach_qid = jObj.getString("qid");
                Model.attach_status = jObj.getString("status");
                Model.attach_file_url = jObj.getString("url");
                Model.attach_filename = jObj.getString("filename");
                Model.attach_id = jObj.getString("attach_id");
                Model.upload_files = jObj.getString("filename");

                //----------------- Kissmetrics ----------------------------------
                Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                Model.kiss.record("android.patient.Attach_Files");
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("android.patient.Qid", (Model.attach_qid));
                properties.put("android.patient.attach_status", (Model.attach_status));
                properties.put("android.patient.attach_file_url", (Model.attach_file_url));
                properties.put("android.patient.attach_filename", (Model.attach_filename));
                properties.put("android.patient.attach_id", (Model.attach_id));
                Model.kiss.set(properties);
                //----------------- Kissmetrics ----------------------------------

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.patient.Qid", (Model.attach_qid));
                articleParams.put("android.patient.attach_status", (Model.attach_status));
                articleParams.put("android.patient.attach_file_url", (Model.attach_file_url));
                articleParams.put("android.patient.attach_filename", (Model.attach_filename));
                articleParams.put("android.patient.attach_id", (Model.attach_id));
                FlurryAgent.logEvent("android.patient.Attach_Files", articleParams);
                //----------- Flurry -------------------------------------------------

                FlurryAgent.onPageView();

                System.out.println("attach_qid-------" + attach_qid);
                System.out.println("attach_status-------" + attach_status);
                System.out.println("attach_file_url-------" + attach_file_url);
                System.out.println("attach_filename-------" + attach_filename);
                System.out.println("attach_attach_id-------" + attach_id);
                System.out.println("last_upload_file--------------" + last_upload_file);

                finish();

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

                upLoadServerUri = Model.BASE_URL + "/sapp/upload?qid=" + cur_qid;
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


    private String upload_file(String file_path) {

        String ServerUploadPath = Model.BASE_URL + "/sapp/upload?user_id=" + (Model.id) + "&qid=" + (cur_qid) + "&token=" + Model.token + "&enc=1";

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