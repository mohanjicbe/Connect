package com.orane.icliniq.network;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.orane.icliniq.Model.Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class ShareIntent {

    public String sAux;

    public void ShareApp(Activity activity, String actname) throws IOException {

        try {

            if (actname.equals("MainActivity")) {
                sAux = "\nGet iCliniq App and talk with the doctors instantly from your phone\n\n";
                sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en_US");
            } else if (actname.equals("SubscriptionPackActivity")) {
                sAux = "iCliniq Subscription packages - Choose a subscription plan that best suits to you";
            } else if (actname.equals("PrePackActivity")) {
                sAux = "iCliniq Prepaid packages - Topup your icliniq wallet with a prepaid package and get up to 25%off on every transaction";
            }

            try {
                Model.kiss.record("android.Patient.Share_App");
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("android.Patient.Content", sAux);
                Model.kiss.set(properties);
            } catch (Exception ee) {
                ee.printStackTrace();
            }


            Date now = new Date();
            long milliseconds = now.getTime();
            String mPath = Environment.getExternalStorageDirectory() + "/" + milliseconds + ".jpg";

            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();


            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.fromFile(imageFile);

            shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(shareIntent, "send"));


        } catch (Throwable e) {
            //e.printStackTrace();
            System.out.println("Exception from Taking Screenshot---" + e.toString());
            System.out.println("Exception Screenshot---" + e.toString());

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, sAux);
            activity.startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
    }


    public void SubcriptionShare(Activity activity) throws IOException {

        try {

            Date now = new Date();
            long milliseconds = now.getTime();

            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
            String mPath = Environment.getRootDirectory() + "/" + milliseconds + ".jpg";

            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();


            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.fromFile(imageFile);

            sAux = "";
            String sAux = "\nGet iCliniq App and consult doctors instantly from your phone\n\n";
            sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en_US");

            shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(shareIntent, "send"));


        } catch (Throwable e) {
            //e.printStackTrace();
            System.out.println("Exception from Taking Screenshot---" + e.toString());
            System.out.println("Exception Screenshot---" + e.toString());

            sAux = "";
            sAux = "\nGet iCliniq App and consult doctors instantly from your phone\n\n";
            sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en_US");

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, sAux);
            activity.startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
    }


}