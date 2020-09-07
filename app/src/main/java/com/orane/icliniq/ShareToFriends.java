package com.orane.icliniq;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.android.material.snackbar.Snackbar;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;


public class ShareToFriends extends AppCompatActivity {

    Toolbar toolbar;
    ImageView img_mail, img_whatsapp, img_facebook, img_share;
    Animation myAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_friends);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.SharetoFriends");
        FlurryAgent.onPageView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Share to Friends");
        }


        final Animation animTranslate = AnimationUtils.loadAnimation(
                this, R.anim.anim_translate);
        final Animation animAlpha = AnimationUtils.loadAnimation(this,
                R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this,
                R.anim.anim_scale);
        final Animation animRotate = AnimationUtils.loadAnimation(
                this, R.anim.anim_rotate);
        myAnimation = AnimationUtils.loadAnimation(this,
                R.anim.myanimation);

        img_share = (ImageView) findViewById(R.id.img_share);
        img_whatsapp = (ImageView) findViewById(R.id.img_whatsapp);
        img_facebook = (ImageView) findViewById(R.id.img_facebook);
        img_mail = (ImageView) findViewById(R.id.img_mail);


        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    img_share.startAnimation(myAnimation);

                    //--------------- Share Others-----------------------------------------------
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri uri = Uri.fromFile(Model.imageFile);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    String sAux = "\nGet iCliniq App and consult doctors instantly from your phone\n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en_US");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);
                    //shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "send"));
                    //--------------- Share Others-----------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        img_whatsapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                img_whatsapp.startAnimation(myAnimation);

                try {
                    //--------------- Share WhatsApp -----------------------------------------------
                    Uri uri = Uri.fromFile(Model.imageFile);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    //Target whatsapp:
                    shareIntent.setPackage("com.whatsapp");
                    String sAux = "\nGet iCliniq App and consult doctors instantly from your phone\n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en");

                    shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(shareIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Snackbar snackbar = Snackbar
                                .make(v, "WhatsApp is not Installed", Snackbar.LENGTH_LONG)
                                .setAction("Ok", this);
                        snackbar.setActionTextColor(Color.WHITE);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(Color.GRAY);
                        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                    //--------------- Share WhatsApp -----------------------------------------------
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        img_facebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                img_facebook.startAnimation(myAnimation);

                try {

                    //--------------- Share Facebook -----------------------------------------------
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    //String msg="Please Downloard iCliniq App from Google Play store, <a href=\"https://play.google.com/store/apps/details?id=com.orane.docassist&amp;hl=en\">tap here</a>";
                    String sAux = "\nGet iCliniq App and consult doctors instantly from your phone \n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sAux);
                    Uri uri = Uri.fromFile(Model.imageFile);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sendIntent.setType("image/jpeg");
                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    sendIntent.setPackage("com.facebook.katana");

                    try {
                        //startActivity(sendIntent);
                        startActivity(Intent.createChooser(sendIntent, "send"));
                    } catch (android.content.ActivityNotFoundException ex) {

                        Snackbar snackbar = Snackbar.make(v, "Facebook is not Installed", Snackbar.LENGTH_LONG).setAction("Ok", null);
                        snackbar.setActionTextColor(Color.WHITE);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(Color.GRAY);
                        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                    //--------------- Share Facebook -----------------------------------------------
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main_query, menu);
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
}
