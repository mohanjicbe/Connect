/*
package com.orane.icliniq;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.gcm.GcmListenerService;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Sqlite.Contact;
import com.orane.icliniq.Sqlite.DatabaseHandler;
import com.orane.icliniq.zoom.Consultation_View;
import com.orane.icliniq.zoom.Video_Calling_Activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PushNotificationService extends GcmListenerService {

    Intent intent, i;
    PendingIntent pIntent;
    public String mtype, push_qid, push_msg, push_type, push_title, appt_id, meeting_id, doc_photo, doc_name, doc_sp, fb_url, title, icliniq_url, message, img_url;
    public String ticker_text, item_type, ContentTitle, ContentText, SummaryText;
    Bitmap bitmap_images;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";

    public String noti_sound_val, stop_noti_val;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        try {

            System.out.println("Push Noti Msg Received------------------" + remoteMessage.getData().toString());
            System.out.println("Push Noti Msg Received------------------" + remoteMessage.toString());

            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }






            System.out.println("Push Bundle Data-----" + bundle2string(data));

            FlurryAgent.onPageView();

            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

            push_msg = data.getString("message");
            push_type = data.getString("msg_type");
            push_title = data.getString("title");

            Model.push_msg = push_msg;

            if ((push_type).equals("25")) {
                push_qid = data.getString("id");
                System.out.println("push_qid---------" + (push_qid));
            } else if ((push_type).equals("31")) {
                System.out.println("push_msg-----" + push_msg);
                System.out.println("push_type-----" + push_type);
                System.out.println("push_title-----" + push_title);

            } else if ((push_type).equals("28")) {

                fb_url = data.getString("fb_url");
                push_title = data.getString("title");
                icliniq_url = data.getString("icliniq_url");
                message = data.getString("message");
                img_url = data.getString("img_url");

                if (data.containsKey("item_type")) {
                    item_type = data.getString("item_type");
                } else {
                    item_type = "0";
                }

                System.out.println("push_title-----" + push_title);
                System.out.println("message-----" + message);
                System.out.println("fb_url-----" + fb_url);
                System.out.println("icliniq_url-----" + icliniq_url);
                System.out.println("img_url-----" + img_url);
                System.out.println("item_type-----" + item_type);

            } else if ((push_type).equals("29")) {

                Model.fcode = data.getString("fcode");
                Model.qid = data.getString("id");

                push_title = "Patient Hotline Chat message received";
                System.out.println("Patient Model.fcode-----" + Model.fcode);
                System.out.println("Patient Model.qid-----" + Model.qid);

            } else if ((push_type).equals("33")) {

                Model.qid = data.getString("id");
            } else if ((push_type).equals("30")) {

                appt_id = data.getString("id");
                meeting_id = data.getString("meeting_id");
                doc_photo = data.getString("doc_photo");
                doc_name = data.getString("doc_name");
                doc_sp = data.getString("doc_sp");

                push_title = "Video Call Notification Received";
                System.out.println("Patient appt_id-----" + appt_id);
                System.out.println("Patient meeting_id-----" + meeting_id);
                System.out.println("Patient doc_photo-----" + Model.doc_photo);
                System.out.println("Patient doc_name-----" + Model.doc_name);
                System.out.println("Patient doc_sp-----" + Model.doc_sp);
            }

        } catch (Exception e) {
            System.out.println("Push Receive Exception------" + e.toString());
            e.printStackTrace();
        }

        try {

            if ((Model.fcode) != null && !(Model.fcode).isEmpty() && !(Model.fcode).equals("null") && !(Model.fcode).equals("")) {
                if ((Model.screen_status) != null && !(Model.screen_status).isEmpty() && !(Model.screen_status).equals("null") && !(Model.screen_status).equals("")) {
                    if ((Model.screen_status).equals("true")) {
                        //Model.fcode = push_msg;
                        System.out.println("Patient Push Model.screen_status-----" + Model.screen_status);
                        System.out.println("Patient Push Blocked-sent to View-----");
                    } else {
                        System.out.println("Patient Push Model.screen_status-----" + Model.screen_status);
                        System.out.println("Patient Push one Exception------");
                        if ((push_type).equals("30")) {
                            i = new Intent(this, Video_Calling_Activity.class);
                            i.putExtra("tv_cons_id", appt_id);
                            i.putExtra("doc_photo", doc_photo);
                            i.putExtra("doc_name", doc_name);
                            i.putExtra("doc_sp", doc_sp);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            Notification();
                        }
                    }
                } else {
                    System.out.println("Patient Push Two Exception------");
                    System.out.println("Patient Push Model.screen_status-----" + Model.screen_status);
                    if ((push_type).equals("30")) {
                        i = new Intent(this, Video_Calling_Activity.class);
                        i.putExtra("tv_cons_id", appt_id);
                        i.putExtra("doc_photo", doc_photo);
                        i.putExtra("doc_name", doc_name);
                        i.putExtra("doc_sp", doc_sp);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        Notification();
                    }
                }
            } else {
                System.out.println("Patient Push Three------");
                System.out.println("Patient Push screen_status-----" + Model.screen_status);

                if ((push_type).equals("30")) {
                    i = new Intent(this, Video_Calling_Activity.class);
                    i.putExtra("tv_cons_id", appt_id);
                    i.putExtra("doc_photo", doc_photo);
                    i.putExtra("doc_name", doc_name);
                    i.putExtra("doc_sp", doc_sp);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Notification();
                }
            }

        } catch (Exception e) {
            System.out.println("Push Exception------" + e.toString());
            e.printStackTrace();
        }
    }

    public void Notification() {

        //-------------------- Insert Data -----------------------------------
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
        String currentDateandTime = sdf.format(new Date());
        DatabaseHandler db = new DatabaseHandler(this);
        db.addContact(new Contact(push_msg, currentDateandTime));
        //-------------------- Insert Data -----------------------------------


        if ((push_type).equals("25")) {
            intent = new Intent(this, QueryViewActivity.class);
            intent.putExtra("qid", push_qid);
            intent.putExtra("qtype", "0");
            Model.query_launch = "Pushnotification";

        } else if ((push_type).equals("30")) {
            intent = new Intent(this, Consultation_View.class);
            intent.putExtra("tv_cons_id", appt_id);
            Model.query_launch = "PushNotificationService";

        } else if ((push_type).equals("33")) {
            intent = new Intent(this, Inbox_view.class);
            intent.putExtra("qid", Model.qid);
            Model.query_launch = "InboxMessage";

        } else if ((push_type).equals("28")) {

            if (item_type.equals("2")) {
                intent = new Intent(this, ArticleViewActivity.class);
                intent.putExtra("KEY_url", icliniq_url);
                intent.putExtra("img_url", img_url);
                intent.putExtra("KEY_ctype", push_type);
                intent.putExtra("title", push_title);

            } else if (item_type.equals("1")) {
                intent = new Intent(this, QADetailNew.class);
                intent.putExtra("KEY_ctype", push_type);
                intent.putExtra("KEY_url", icliniq_url);
                //startActivity(intent);

            } else if (item_type.equals("5")) {
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", icliniq_url);
                intent.putExtra("type", push_title);
                //startActivity(intent);
            } else {
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", icliniq_url);
                intent.putExtra("type", push_title);
                //startActivity(intent);
            }

            Model.query_launch = "PushNotificationService";

        } else if ((push_type).equals("29")) {

            intent = new Intent(this, HotlineChatViewActivity.class);
            intent.putExtra("follouwupcode", Model.fcode);
            intent.putExtra("selqid", Model.qid);
            Model.query_launch = "PushNotificationService";

        } else if (push_type.equals("31")) {

            intent = new Intent(this, NotificationViewActivity.class);
            intent.putExtra("push_msg", push_msg);
            intent.putExtra("push_type", push_type);
            intent.putExtra("push_title", push_title);
            Model.query_launch = "PushNotificationService";

        } else {
            intent = new Intent(this, Connect_Home_Actvity.class);
        }

        try {

            //------- Sqlite ---------------------------------------
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => "+c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm a");
            String formattedDate = df.format(c.getTime());

            long time = System.currentTimeMillis();
            DatabaseHandler db = new DatabaseHandler(this);
            db.addContact(new Notify("" + time, push_msg, push_type, push_title,formattedDate));
            //------- Sqlite ---------------------------------------


            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Pushnotification");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("android.patient.push.title", push_title);
            properties.put("android.patient.push.msg", push_msg);
            properties.put("android.patient.push.type", push_type);
            properties.put("android.patient.push.qid", push_qid);
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------

            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.push.title", push_title);
            articleParams.put("android.patient.push.msg", push_msg);
            articleParams.put("android.patient.push.type", push_type);
            articleParams.put("android.patient.push.qid", push_qid);
            FlurryAgent.logEvent("android.patient.Pushnotification", articleParams);
            //----------- Flurry -------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Open NotificationView.java Activity
        //Create Notification using NotificationCompat.Builder

  Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();


        //================ Initialize ======================---------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "off");
        noti_sound_val = sharedpreferences.getString(noti_sound, "off");
        //================ Initialize ======================---------------

        if (push_type.equals("28")) {
            System.out.println("img_url--=-=-=-" + img_url);
            new convertBitmap().execute(img_url);

                if (img_url != null && !img_url.isEmpty() && !img_url.equals("null") && !img_url.equals("")) {
                    //------------- Notification with Images -----------------------
                    new convertBitmap().execute(img_url);
                    //------------- Notification with Images -----------------------
                } else new convertBitmap().execute("");

        } else {

            System.out.println("Sounds is ON");
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();

            //---------------- Normal Notify -----------------------------------------
            pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.logo)
                    .setTicker(push_title)
                    .setContentTitle(push_title)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                    //.setDefaults(new NotificationCompat().DEFAULT_SOUND)
                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(push_msg)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);

            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationmanager.notify(0, builder.build());
            //---------------- Normal Notify -----------------------------------------
        }


        //---------------- Big Notify -----------------------------------------
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(Html.fromHtml(push_msg))
                .setBigContentTitle(Html.fromHtml(push_title))
                .setSummaryText(Html.fromHtml(push_msg)))
                .setContentTitle(Html.fromHtml(push_title))
                .setContentText(Html.fromHtml(push_msg))
                .setSmallIcon(R.mipmap.logo);

        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
        //---------------- Big Notify -----------------------------------------

    }

















































    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

    class convertBitmap extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("AsyncTask---Pre Execute");
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap_images = BitmapFactory.decodeStream(input);

                System.out.println("AsyncTask---background");
                System.out.println("AsyncTask-Images----" + bitmap_images);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Boolean result) {

            System.out.println("AsyncTask---PostExecute");

            ticker_text = push_title;
            ContentTitle = push_title;
            ContentText = message;
            SummaryText = message;

if (noti_sound_val.equals("on")) {
                System.out.println("Sounds is ON");
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
                System.out.println("Sounds is OFF");
            }


            pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.logo)
                    .setTicker(ticker_text)
                    .setContentTitle(ContentTitle)
                    // .setStyle(new NotificationCompat.BigTextStyle().bigText("Bigtext Content This is a Sample Message for the Sample testing for the Application of Docassist"))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                    //.addAction(R.mipmap.logo, "show activity", pIntent)
                    //.setDefaults(new NotificationCompat().DEFAULT_VIBRATE)
                    //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap_images).setSummaryText(SummaryText))
                    .setContentText(ContentText)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);

            System.out.println("AsyncTask-Images----" + bitmap_images);

            NotificationManager notificationmanager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            notificationmanager.notify(0, builder.build());

        }
    }


}
*/
