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

public class UploadBookingToServer extends Activity {

    InputStream is = null;
    String serverResponseMessage, contentAsString;
    TextView messageText;
    Button uploadButton;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    public JSONObject json;
    String upLoadServerUri = null;
    public String fullpath;

    final String uploadFilePath = "/storage/emulated/0/DCIM/Facebook/";
    final String uploadFileName = "FB_IMG_1457926221137.jpg";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        uploadButton = (Button) findViewById(R.id.uploadButton);
        messageText = (TextView) findViewById(R.id.messageText);


        dialog = ProgressDialog.show(UploadBookingToServer.this, "", "Uploading file...", true);
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("uploading started.....");
                    }
                });

                new JSONAsyncTask().execute("");

                //uploadFile(fpath + "" + fname);

            }
        }).start();
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                uploadFile(uploadFilePath + "" + uploadFileName);
                //uploadFile(uploadFilePath);
                System.out.println("Upload File-------" + uploadFilePath + "" + uploadFileName);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            //dialog.cancel();
            finish();

        }
    }

    public int uploadFile(String sourceFileUri) {

        Intent intent = getIntent();
        String fpath = intent.getStringExtra("KEY_path");
        String fname = intent.getStringExtra("KEY_filename");

        Model.local_file_url = fpath;

        System.out.println("fpath---------" + fpath);
        System.out.println("fname---------" + fname);

        //String fileName = sourceFileUri;
        String fpath_filename = fpath.substring(fpath.lastIndexOf("/") + 1);
        //String fileName = fpath;
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

            Log.e("uploadFile", "Source File not exist :" + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :" + uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;
        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(fullpath);
                System.out.println("fullpath---------------------------------" + fullpath);
                Model.local_file_url = fullpath;
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
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


                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }


                // send multipart form data necesssary after file data...
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
                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            try {
                                JSONObject jObj = new JSONObject(contentAsString);
                                String qid = jObj.getString("qid");
                                String status = jObj.getString("status");
                                String file_url = jObj.getString("url");
                                String filename = jObj.getString("filename");
                                String attach_id = jObj.getString("attach_id");

                                Model.attach_qid = qid;
                                Model.attach_status = status;
                                Model.attach_file_url = file_url;
                                Model.attach_filename = filename;
                                Model.attach_id = attach_id;

                                System.out.println("Uploading file Qid-------" + qid);
                                System.out.println("status-------" + status);
                                System.out.println("file_url-------" + file_url);
                                System.out.println("Uploading filename-------" + filename);
                                System.out.println("attach_id-------" + attach_id);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(UploadBookingToServer.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Upload File Response-----------------" + serverResponseMessage);
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadBookingToServer.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }

                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadBookingToServer.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }


    public String convertInputStreamToString(InputStream stream, int length) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }
}