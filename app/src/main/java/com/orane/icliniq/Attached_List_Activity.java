
package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class Attached_List_Activity extends AppCompatActivity {


    ImageView thumb_img;
    TextView tv_attach_url;
    TextView tv_attach_id, tv_fileurl;
    CardView card_view;
    JSONArray jarray_files;
    JSONObject jsonobj_files;
    LinearLayout file_list_layout;
    String qid_val, q_files_text, attach_file_text, file_url, file_attached_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attached_list);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Attachment Files");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------------------------------------------------------

        try {
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("Android.patient.attachement_view");

            Intent intent = getIntent();
            q_files_text = intent.getStringExtra("filetxt");
            qid_val = intent.getStringExtra("qid");

            System.out.println("get hash map-----");

        } catch (Exception e) {
            e.printStackTrace();
        }

        file_list_layout = (LinearLayout) findViewById(R.id.file_list_layout);

        try {

            //---------------- Files ---------------------------------------
            if ((q_files_text.length()) > 2) {

                attach_file_text = "";

                System.out.println("files_text------" + q_files_text);
                jarray_files = new JSONArray(q_files_text);

                for (int m = 0; m < jarray_files.length(); m++) {
                    jsonobj_files = jarray_files.getJSONObject(m);

                    System.out.println("jsonobj_files--" + m + " ----" + jsonobj_files.toString());

                    file_attached_id = jsonobj_files.getString("attach_id");
                    String file_text = jsonobj_files.getString("file");
                    String ext_text = jsonobj_files.getString("ext");
                    file_url = jsonobj_files.getString("file_url");

                    System.out.println("file_text--------" + file_text);
                    System.out.println("ext_text--------" + ext_text);
                    System.out.println("file_attached_id--------" + file_attached_id);
                    System.out.println("file_url--------" + file_url);

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.attched_file_remove, null);

                    thumb_img = (ImageView) addView.findViewById(R.id.imageView4);
                    tv_attach_url = (TextView) addView.findViewById(R.id.tv_attach_url);
                    tv_attach_id = (TextView) addView.findViewById(R.id.tv_attach_id);
                    tv_fileurl = (TextView) addView.findViewById(R.id.tv_fileurl);
                    TextView tv_fname = (TextView) addView.findViewById(R.id.tv_fname);
                    Button btn_down_button = (Button) addView.findViewById(R.id.btn_down_button);
                    Button btn_rem_button = (Button) addView.findViewById(R.id.btn_rem_button);

                    tv_attach_id.setText(file_attached_id);
                    tv_fileurl.setText(file_url);
                    tv_fname.setText(file_text + "." + ext_text);

                    System.out.println("tv_fileurl------------ " + file_url);

                    //Picasso.with(getApplicationContext()).load(file_url).placeholder(R.mipmap.banner_palceholder).error(R.mipmap.doctor_icon).into(thumb_img);

                    switch (ext_text) {

                        //------------ Images --------------------------------------
                        case "jpg":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "jpeg":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "gif":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "png":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "tiff":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "bmp":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "psd":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "IMG":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "JPE":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        case "WMF":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.jpg).into(thumb_img);
                            break;
                        //------------ Images --------------------------------------

                        case "pdf":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.pdf).into(thumb_img);
                            break;
                        case "doc":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.doc).into(thumb_img);
                            break;
                        case "docx":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.doc).into(thumb_img);
                            break;
                        case "xls":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.xls).into(thumb_img);
                            break;
                        case "xlsx":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.xls).into(thumb_img);
                            break;
                        case "ppt":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.ppt).into(thumb_img);
                            break;
                        case "pptx":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.ppt).into(thumb_img);
                            break;
                        case "pps":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.ppt).into(thumb_img);
                            break;
                        case "ppsx":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.ppt).into(thumb_img);
                            break;
                        case "txt":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.txt).into(thumb_img);
                            break;
                        case "rft":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.txt).into(thumb_img);
                            break;
                        case "mp4":
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.attach_icon).into(thumb_img);
                            break;

                        default:
                            Picasso.with(Attached_List_Activity.this).load(file_url).placeholder(R.drawable.progress_animation).error(R.mipmap.attach_icon).into(thumb_img);
                            break;
                    }


                    //thumb_img.setImageBitmap(BitmapFactory.decodeFile(file_url));


                    btn_down_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent = (View) v.getParent();
                            TextView tv_fileurl = (TextView) parent.findViewById(R.id.tv_fileurl);

                            String url = tv_fileurl.getText().toString();

                            System.out.println("Attach url-------------- " + url);

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);

                        }
                    });


                    btn_rem_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent = (View) v.getParent();
                            //View grand_parent = (View)parent.getParent();

                            tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                            String attid = tv_attach_id.getText().toString();

                            //---------------------------
                            String url = Model.BASE_URL + "/sapp/removeQAttachment?qid=" + qid_val + "&user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token;
                            System.out.println("Remover Attach url-------------" + url);
                            new JSON_remove_file().execute(url);
                            //---------------------------

                            System.out.println("Removed attach_id-----------" + attid);
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });

                    thumb_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {

                                View parent = (View) v.getParent();
                                TextView tv_fileurl = (TextView) parent.findViewById(R.id.tv_fileurl);

                                String url_text = tv_fileurl.getText().toString();
                                System.out.println("url_text-------------" + url_text);

    /*                          Intent intent = new Intent(Attached_List_Activity.this, Image_Preview.class);
                                intent.putExtra("url", url_text);
                                startActivity(intent);*/

                                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                                i.putExtra("url", "http://docs.google.com/viewer?url=" + url_text);
                                i.putExtra("type", "Attachment View");
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    file_list_layout.addView(addView);
                    //------------------------------------
                }
            }

            //---------------- Files---------------------------------------
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


    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Attached_List_Activity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                new JSONParser().getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            dialog.dismiss();

        }
    }


}
