package com.orane.icliniq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Model.MultipartEntity2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

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
import java.util.HashMap;

public class UploadToHotlineServer extends Activity {

    InputStream is = null;
    String serverResponseMessage, contentAsString,upload_response;
    TextView messageText;
    Button uploadButton;
    int serverResponseCode = 0;
    //ProgressDialog dialog = null;
    public JSONObject json;
    String upLoadServerUri = null;
    public String fullpath, selqid;
    public String fpath, fname;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);


        uploadButton = (Button) findViewById(R.id.uploadButton);
        messageText = (TextView) findViewById(R.id.messageText);

        try {
            Intent intent = getIntent();
            fpath = intent.getStringExtra("KEY_path");
            fname = intent.getStringExtra("KEY_filename");
            selqid = intent.getStringExtra("selqid");

            messageText.setText("Uploading file path :" + fpath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        upLoadServerUri = Model.BASE_URL + "/sapp/upload?qid=" + selqid + "&hline=1&token=" + Model.token + "&enc=1";
        System.out.println("upLoadServerUri---------------------" + upLoadServerUri);

        new JSONAsyncTask().execute("");

      /*  dialog = ProgressDialog.show(UploadToHotlineServer.this, "", "Uploading file...", true);
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("uploading started.....");
                    }
                });


            }
        }).start();*/
    }

    private class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                //uploadFile(fpath + "" + fname);
                //uploadFile(fpath);
                upload_response = upload_file(fpath);

                System.out.println("Upload File-------" + fpath + "" + fname);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            System.out.println("Upload File upload_response-----------------" + upload_response);
            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if (serverResponseCode == 200) {
                runOnUiThread(new Runnable() {
                    public void run() {

                        try {

                            JSONObject jObj = new JSONObject(upload_response);
                            System.out.println("jObj-------------" + jObj.toString());

                            String attach_id = jObj.getString("attach_id");
                            String status = jObj.getString("status");

                            Model.attach_qid = attach_id;
                            Model.attach_status = status;

                            System.out.println("Uploading file Qid-------" + attach_id);
                            System.out.println("status-------" + status);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(UploadToHotlineServer.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                        System.out.println("Upload File Response-----------------" + serverResponseMessage);
                    }
                });
            }


            finish();
        }
    }

   /* public int uploadFile() {


        Model.local_file_url = fpath;

        System.out.println("fpath---------" + fpath);
        System.out.println("fname---------" + fname);

        String fpath_filename = fpath.substring(fpath.lastIndexOf("/") + 1);
        fullpath = fpath;

        if ((Model.upload_files).equals("")) {
            Model.upload_files = fpath_filename;
        } else {
            Model.upload_files = Model.upload_files + "," + fpath_filename;
        }

        Model.upload_files = fpath_filename;

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

            dialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("Source File not exist :" + uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;
        } else {
            try {

                FileInputStream fileInputStream = new FileInputStream(fullpath);
                System.out.println("fullpath---------------------------------" + fullpath);
                Model.local_file_url = fullpath;
                URL url = new URL(upLoadServerUri);

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
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

                serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();


                // JSON Response on Server
                int response = conn.getResponseCode();
                System.out.println("response-------" + response);
                is = conn.getInputStream();
                int length = 35500;
                contentAsString = convertInputStreamToString(is, length);


                System.out.println("Upload File Response-----------------" + contentAsString);
                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                JSONObject jObj = new JSONObject(contentAsString);
                                System.out.println("jObj-------------" + jObj.toString());

                                String attach_id = jObj.getString("attach_id");
                                String status = jObj.getString("status");

                                Model.attach_qid = attach_id;
                                Model.attach_status = status;

                                System.out.println("Uploading file Qid-------" + attach_id);
                                System.out.println("status-------" + status);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(UploadToHotlineServer.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Upload File Response-----------------" + serverResponseMessage);
                        }
                    });
                }

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadToHotlineServer.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }

                });

            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadToHotlineServer.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

*/
    public String convertInputStreamToString(InputStream stream, int length) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }


    private String upload_file(String file_path) {

        String ServerUploadPath = Model.BASE_URL + "/sapp/upload?qid=" + selqid + "&hline=1&token=" + Model.token + "&enc=1";
        System.out.println("upLoadServerUri---------------------" + upLoadServerUri);


        System.out.println("ServerUploadPath-------------" + ServerUploadPath);
        System.out.println("file_path-------------" + file_path);

        if ((Model.upload_files).equals("")) {
            Model.upload_files = file_path;
        } else {
            Model.upload_files = Model.upload_files + "," + file_path;
        }

        Model.upload_files = file_path;


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