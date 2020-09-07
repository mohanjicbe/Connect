package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class HotlinePackagesActivity extends AppCompatActivity {

    public View vi;
    private FrameLayout li;
    JSONObject docprof_jsonobj, jsonobj_prepinv, jsonobj, jsonobj1;
    JSONArray jsonarray;
    JSONObject jsonobj_icq100, jsonobj_icq50, json_response_obj;
    public String fee_str, share_url, fee_str_text, inv_fee, inv_id, off_id, off_label, strfee, strdesc1, strdesc2, Doctor_id, Doctor_name, docurl, Doc_id, Docname, doc_photo_url, Docedu, Docspec;
    LinearLayout parent_offer_layout;
    ProgressBar progressBar;
    CircleImageView imageview_poster;
    TextView tvdocname, tvedu, tvspec;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String icq100fee = "icq100fee_key";

    SharedPreferences sharedpreferences;
    public String icq100fee_val, val_int_val, str_response, subdomain, uname, pass, Log_Status;
    Toolbar toolbar;
    Typeface font_reg, font_bold;
    TextView offer_amt, offer_amt2;
    Button btn_get, btn_get2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotline_packages);

        imageview_poster = (CircleImageView) findViewById(R.id.imageview_poster);
        tvdocname = (TextView) findViewById(R.id.tvdocname);
        offer_amt = (TextView) findViewById(R.id.offer_amt);
        offer_amt2 = (TextView) findViewById(R.id.offer_amt2);
        tvedu = (TextView) findViewById(R.id.tvedu);
        tvspec = (TextView) findViewById(R.id.tvspec);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_get2 = (Button) findViewById(R.id.btn_get2);
        parent_offer_layout = (LinearLayout) findViewById(R.id.parent_offer_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //tv_hotlinedesc.setText(Html.fromHtml("Subscribe to the doctor's <b>hotline</b> and reach out to the doctor anytime 24/7 for <b>medical advice</b>."));

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        icq100fee_val = sharedpreferences.getString(icq100fee, "");
        //============================================================

        //------------ Google firebase Analitics-----------------------------------------------
        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        Bundle params = new Bundle();
        params.putString("user_id", Model.id);
        Model.mFirebaseAnalytics.logEvent("icq100Plans", params);
        //------------ Google firebase Analitics----------------------------------------------


        //------------ Object Creations -------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Hotline Packages");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //----------------------------------------------------------

        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tvdocname.setTypeface(font_bold);
        tvedu.setTypeface(font_reg);

        ((TextView) findViewById(R.id.tv_title1)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_title2)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_title3)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_title4)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_title5)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_title6)).setTypeface(font_reg);

        ((TextView) findViewById(R.id.offer_title)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.offer_amt)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.desc1)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.btn_get)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.offer_title2)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.offer_amt2)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.desc2)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.btn_get2)).setTypeface(font_bold);

        TextView tv_title2 = (TextView) findViewById(R.id.tv_title2);

        tvedu.setTypeface(font_reg);

        try {
            Intent intent = getIntent();

            Doctor_id = intent.getStringExtra("Doctor_id");
            Doctor_name = intent.getStringExtra("Doctor_name");
            docurl = intent.getStringExtra("tv_docurl");
            share_url = intent.getStringExtra("share_url");

            System.out.println("selecting_Doctor_id---------" + Doctor_id);
            System.out.println("Doctor_name---------" + Doctor_name);
            System.out.println("docurl---------" + docurl);

            if (getSupportActionBar() != null) {
                if (Doctor_name != null && !Doctor_name.isEmpty() && !Doctor_name.equals("null") && !Doctor_name.equals("")) {
                    getSupportActionBar().setTitle(Doctor_name);

                    tvedu.setText("Get medical advice from " + Doctor_name + "24/7 via whatsapp like to chat");

                } else {
                    getSupportActionBar().setTitle("Hotline Packages");
                }
            }

            System.out.println("selecting_Doctor_id---------" + Doctor_id);

        } catch (Exception e) {
            System.out.println("selecting_Doctor_id Exception---------" + e.toString());
            e.printStackTrace();
        }


        //------------- Get ICQ100 Fees ---------------------------------------------
        try {
            JSONObject json_get_icq100_fee = new JSONObject();
            json_get_icq100_fee.put("user_id", (Model.id));
            json_get_icq100_fee.put("doctor_id", Doctor_id);
            json_get_icq100_fee.put("item_type", "icq100");
            System.out.println("json_getfee---" + json_get_icq100_fee.toString());
            new JSON_getFee_100().execute(json_get_icq100_fee);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        //------------- Get ICQ100 Fees ---------------------------------------------

        //------------- Get ICQ50 Fees ---------------------------------------------
        try {
            JSONObject json_get_icq50_fee = new JSONObject();
            json_get_icq50_fee.put("user_id", (Model.id));
            json_get_icq50_fee.put("doctor_id", Doctor_id);
            json_get_icq50_fee.put("item_type", "icq50");
            System.out.println("json_getfee---" + json_get_icq50_fee.toString());
            new JSON_getFee_50().execute(json_get_icq50_fee);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        //------------- Get ICQ50 Fees ---------------------------------------------


        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Log_Status.equals("1")) {
                        Intent intent = new Intent(HotlinePackagesActivity.this, Instant_Chat.class);
                        intent.putExtra("doctor_id", Doctor_id);
                        intent.putExtra("plan_id", "icq100");
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else {
                        ask_login();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_get2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Log_Status.equals("1")) {
                    Intent intent = new Intent(HotlinePackagesActivity.this, Instant_Chat.class);
                    intent.putExtra("doctor_id", Doctor_id);
                    intent.putExtra("plan_id", "icq50");
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                } else {
                    ask_login();
                }
            }
        });


    }

    /* @Override
     public void onClick(View v) {

         try {
             if (Log_Status.equals("1")) {
                 View parent = (View) v.getParent();
                 TextView tvid = (TextView) parent.findViewById(R.id.tvoid);

                 String tvidval = tvid.getText().toString();
                 System.out.println("tvidval-----------------------------------" + tvidval);


                 Intent intent = new Intent(HotlinePackagesActivity.this, Icliniq100hrs.class);
                 intent.putExtra("doctor_id", Doctor_id);
                 intent.putExtra("plan_id", "");
                 startActivity(intent);
                 overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


             } else {
                 System.out.println("Log_Status---Zero------------------------------------------------" + Log_Status);
                 ask_login();
             }

         } catch (Exception e) {
             e.printStackTrace();
         }

     }
 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.hotline_pack_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_share) {
            TakeScreenshot_Share();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ask_login() {
        try {
            final MaterialDialog alert = new MaterialDialog(HotlinePackagesActivity.this);
            alert.setTitle("Please Re-Login the App..!");
            alert.setMessage("Something went wrong. Please go back and try again..!e");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "0");
                    editor.apply();
                    //============================================================
                    finishAffinity();
                    Intent i = new Intent(HotlinePackagesActivity.this, LoginActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    finish();
                }
            });
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void TakeScreenshot_Share() {

        try {
            //------- Making Screen shot---------------------------
            Date now = new Date();
            long milliseconds = now.getTime();
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + milliseconds + ".jpg";
            System.out.println("mPath---" + mPath);

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            //------- Making Screen shot---------------------------

            //------- Share ---------------------------
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(imageFile);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "subscribe to doctor's icliniq hotline and reach out to the doctor anytime 24/7 for medical advice. Doctor: " + Doctor_name + "\n\n" + share_url);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "send"));
            //------- Share ---------------------------

        } catch (Throwable e) {
            //e.printStackTrace();
            System.out.println("Exception from Taking Screenshot---" + e.toString());
            System.out.println("Exception Screenshot---" + e.toString());

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "subscribe to doctor's icliniq hotline and reach out to the doctor anytime 24/7 for medical advice. Doctor: " + Doctor_name + "\n\n" + share_url);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
    }


    private class JSON_getFee_100 extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(HotlinePackagesActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj_icq100 = jParser.JSON_POST(urls[0], "getFee");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.print("docprof_jsonobj---------------" + jsonobj_icq100.toString());
                if (jsonobj_icq100.has("str_fee")) {
                    fee_str_text = jsonobj_icq100.getString("str_fee");
                    offer_amt.setText(fee_str_text);
                } else {
                    offer_amt.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }


    private class JSON_getFee_50 extends AsyncTask<JSONObject, Void, Boolean> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(HotlinePackagesActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj_icq50 = jParser.JSON_POST(urls[0], "getFee");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.print("docprof_jsonobj---------------" + jsonobj_icq50.toString());
                if (jsonobj_icq50.has("str_fee")) {
                    fee_str_text = jsonobj_icq50.getString("str_fee");
                    offer_amt2.setText(fee_str_text);
                } else {
                    offer_amt2.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }
}
