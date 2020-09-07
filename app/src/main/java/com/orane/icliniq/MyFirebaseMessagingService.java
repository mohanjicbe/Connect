package com.orane.icliniq;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Sqlite.Contact;
import com.orane.icliniq.Sqlite.DatabaseHandler;
import com.orane.icliniq.zoom.Consultation_View;
import com.orane.icliniq.zoom.Video_Calling_Activity;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public String mtype, push_qid, push_msg, push_type, push_title, appt_id, meeting_id, doc_photo, doc_name, doc_sp, fb_url, title, icliniq_url, message, img_url;
    public String ticker_text, item_type, ContentTitle, ContentText, SummaryText;
    Bitmap bitmap_images;
    Intent intent;
    PendingIntent pIntent;
    public String noti_sound_val, stop_noti_val;

    Intent i;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("Push Noti Msg Received------------------" + remoteMessage.getData().toString());
        System.out.println("Push Noti Msg Received------------------" + remoteMessage.toString());


        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            System.out.println("Message data payload------- " + remoteMessage.getData());
/*
            if (*//* Check if data needs to be processed by long running job *//* true) {
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

        }


        try {

            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);


            String title = object.getString("title");
            String body_text = object.getString("body");

            System.out.println("title-------------" + title);
            System.out.println("body_text-------------" + body_text);

            JSONObject data = new JSONObject(body_text);
            System.out.println("body_data-------------" + data);


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

                if (data.has("item_type")) {
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

            // Notification();

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(MyFirebaseMessagingService.this);
            Bundle params_new = new Bundle();
            params_new.putString("user_id", Model.id);
            params_new.putString("Body", body_text);
            Model.mFirebaseAnalytics.logEvent("Push_Notification", params_new);
            //------------ Google firebase Analitics--------------------


        } catch (Exception e) {
            e.printStackTrace();
        }





/*
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
*/


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

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
    }

/*    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());
    }*/


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

            System.out.println("PushNotification-------------- 29");

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

          /*  //------- Sqlite ---------------------------------------
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm a");
            String formattedDate = df.format(c.getTime());

            long time = System.currentTimeMillis();
            DatabaseHandler db = new DatabaseHandler(this);
            db.addContact(new Notify("" + time, push_msg, push_type, push_title, formattedDate));
            //------- Sqlite ---------------------------------------
*/
/*
            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Pushnotification");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("android.patient.push.title", push_title);
            properties.put("android.patient.push.msg", push_msg);
            properties.put("android.patient.push.type", push_type);
            properties.put("android.patient.push.qid", push_qid);
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------*/

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


        if (push_type.equals("28")) {
            System.out.println("img_url--=-=-=-" + img_url);
            //new convertBitmap().execute(img_url);

            if (img_url != null && !img_url.isEmpty() && !img_url.equals("null") && !img_url.equals("")) {
                //------------- Notification with Images -----------------------
                new convertBitmap().execute(img_url);
                //------------- Notification with Images -----------------------
            } else new convertBitmap().execute("");

        } else {

        }

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);

            Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.click_sound);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();


            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            channel.setSound(soundUri, audioAttributes);
            mNotificationManager.createNotificationChannel(channel);

            System.out.println("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION-----------------");
        }


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "default")
                .setSmallIcon(R.mipmap.logo)
                .setTicker(push_title)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(push_title)
                //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap_images).setSummaryText(Html.fromHtml(push_title)))
                //.setStyle(inboxStyle)
                .setLargeIcon(BitmapFactory.decodeResource(MyFirebaseMessagingService.this.getResources(), R.mipmap.logo))
                //.setSound(alarmSound)
                .setAutoCancel(true);
        //Intent intent = new Intent(MyFirebaseMessagingService.this, AboutAppActivity.class);
        PendingIntent pi = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify((new Random().nextInt(9999 - 1000) + 1000), mBuilder.build());

        System.out.println("Notify END-----------------");


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
