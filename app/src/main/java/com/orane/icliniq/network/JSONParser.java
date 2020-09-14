package com.orane.icliniq.network;


import android.util.Log;

import com.orane.icliniq.Model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JSONParser {

    final String TAG = "JsonParser.java";
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    String contentAsString, new_sub_url;
    static JSONArray JArr = null;
    public String result;
    StringBuilder total;
    public String sub_url;
    long startTime;
    URL url2;


    public JSONObject JSON_POST(JSONObject jsonobj, String post_flag) throws IOException {

        if (post_flag.equals("ConfirmPassword")) {
            sub_url = "/app/updateNewPassword";
        } else if (post_flag.equals("forgotpwd_request")) {
            sub_url = "/app/getForgotPassPin";
        } else if (post_flag.equals("Submit_consultation")) {
            //sub_url = "mobileajax/submitCommonBookingN";
            sub_url = "sapp/submitCommonBookingN" + "?token=" + Model.token;
        } else if (post_flag.equals("Feedback_post")) {
            //sub_url = "mobileajax/feedback";
            sub_url = "sapp/feedback?token=" + Model.token;
        } else if (post_flag.equals("Coupon_apply")) {
            //sub_url = "mobileajax/applyCoupon";
            sub_url = "sapp/applyCoupon?token=" + Model.token;
        } else if (post_flag.equals("Chat_PostQuery")) {
            sub_url = "query/sendText";
        } else if (post_flag.equals("request_sms")) {
            sub_url = "app/sendMobileOTP";
        } else if (post_flag.equals("Validating_sms")) {
            sub_url = "app/verifyMobileOTP";
        } else if (post_flag.equals("Invite_doc")) {
            //sub_url = "mobileajax/inviteMyDoc";
            sub_url = "sapp/inviteMyDoc?token=" + Model.token;
        } else if (post_flag.equals("PostQuery")) {
            //sub_url = "mobileajax/saveq?user_id=" + (Model.id);
            sub_url = "sapp/saveq?user_id=" + (Model.id) + "&token=" + Model.token;
        } else if (post_flag.equals("Signup")) {
            sub_url = "mobileajax/signupPatient";
        } else if (post_flag.equals("extraQuery")) {
            //sub_url = "mobileajax/saveqext?user_id=" + (Model.id);
            sub_url = "sapp/saveqext?user_id=" + (Model.id) + "&token=" + Model.token;
        } else if (post_flag.equals("postFeedback")) {
            //sub_url = "mobileajax/dolike";
            sub_url = "sapp/dolike?token=" + Model.token;
        } else if (post_flag.equals("getLogin")) {
            sub_url = "mobileajax/loginSubmit";
        } else if (post_flag.equals("getFee")) {
            sub_url = "sapp/getFee?token=" + Model.token;
        } else if (post_flag.equals("getqFee")) {
            sub_url = "sapp/getFee?token=" + Model.token;
        } else if (post_flag.equals("labtest_post")) {
            sub_url = "sapp/labTestHome";
        } else if (post_flag.equals("addtocart")) {
            sub_url = "sapp/addToCart";
        } else if (post_flag.equals("remove_cart")) {
            sub_url = "sapp/deleteCart";
        } else if (post_flag.equals("profile_post")) {
            sub_url = "sapp/patientProfileUpdate?token=" + Model.token;
        } else if (post_flag.equals("checkmobnoexists")) {
            sub_url = "mobileajax/checkMobileNoExist";
        } else if (post_flag.equals("validatemobnoexists")) {
            sub_url = "mobileajax/validateMobileNoEmail";
        } else if (post_flag.equals("SendOTP")) {
            sub_url = "/app/sendMobileOTP";
        } else if (post_flag.equals("newFamily")) {
            sub_url = "sapp/saveFamilyProfile?isApp=true&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("labTestBookOrder")) {
            sub_url = "sapp/labTestBookOrder";
        } else if (post_flag.equals("pat_profile_submit")) {
            sub_url = "sapp/savePatientProfile?user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("VerifyOTP")) {
            sub_url = "app/verifyMobileOTP";
        } else if (post_flag.equals("logout")) {
            sub_url = "sapp/appLogout";
        } else if (post_flag.equals("new_file_desc_upload")) {
            sub_url = "sapp/addHReportsData?user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("extraCons")) {
            sub_url = "sapp/saveBookExt?user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("post_ratting")) {
            sub_url = "sapp/doStarRating?user_id=" + Model.id + "&token=" + Model.token;
        }else if (post_flag.equals("submit_hosp")) {
            sub_url = "sapp/addMe2Hospital?user_id=" + Model.id + "&token=" + Model.token;
        }

        url2 = new URL(Model.BASE_URL + sub_url);
        InputStream is = null;

        try {

            System.out.println("url--------" + url2);

            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jsonobj.toString());
            wr.flush();
            wr.close();
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);

            System.out.println("JSON response----" + response);

            try {
                jObj = new JSONObject(contentAsString.trim());
                System.out.println("jObj----" + jObj.toString());

            } catch (Throwable t) {
                t.printStackTrace();
            }

            return jObj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String JSON_String_POST(JSONObject jsonobj, String post_flag) throws IOException {

        if (post_flag.equals("newFamily")) {
            sub_url = "sapp/saveFamilyProfile?isApp=true&user_id=" + Model.id + "&token=" + Model.token;
        }
        if (post_flag.equals("view_test")) {
            sub_url = "sapp/subTests?user_id=" + Model.id + "&token=" + Model.token;
        }

        InputStream is = null;

        try {

            URL url2 = new URL(Model.BASE_URL + sub_url);
            System.out.println("url--------" + url2);

            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jsonobj.toString());
            wr.flush();
            wr.close();
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);

            System.out.println("JSON response----" + response);

            return contentAsString;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String JSON_STR_POST(JSONObject jsonobj, String post_flag) throws IOException {

        if (post_flag.equals("labtest")) {
            sub_url = "sapp/labTestList?token=" + Model.token;
        } else if (post_flag.equals("article_Feedback_post")) {
            sub_url = "sapp/articleStarRating?token=" + Model.token;
        } else if (post_flag.equals("labtest_invoice")) {
            sub_url = "sapp/invoice?token=" + Model.token;
        }

        InputStream is = null;

        try {
            URL url2 = new URL(Model.BASE_URL + sub_url);
            System.out.println("url--------" + url2);

            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jsonobj.toString());
            wr.flush();
            wr.close();
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);

            System.out.println("JSON response----" + response);

            return contentAsString;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    public JSONObject getPrePack(String url) throws IOException {

        InputStream is = null;

        try {
            URL url2 = new URL(url + "&token=" + (Model.token));

            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            System.out.println("response-------" + response);
            is = conn.getInputStream();
            String contentAsString = convertInputStreamToString(is);
            System.out.println("contentAsString-------" + contentAsString);

            try {
                jObj = new JSONObject(contentAsString.trim());
                System.out.println("jObj-------" + jObj.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jObj;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String getJSONString(String url) throws IOException {

        InputStream is = null;

        try {
            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            System.out.println("response-------" + response);

            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);
            System.out.println("contentAsString-------" + contentAsString);

            return contentAsString;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

/*
    public String getJSONString2(String url) throws IOException {

        InputStream is = null;

        try {
            HttpUrl mySearchUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("www.google.com")
                    .addPathSegment("search")
                     .addQueryParameter("q", "polar bears")
                    .build();


            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-rapidapi-host", "covid-19-data.p.rapidapi.com");
            conn.setRequestProperty("x-rapidapi-key", "7aa728380dmshf6c31f3e0b09fbbp10f746jsne190aea0f62e");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            System.out.println("response-------" + response);

            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);
            System.out.println("contentAsString-------" + contentAsString);

            return contentAsString;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }*/

    public JSONObject getJSONFromUrl(String url) throws IOException {

        InputStream is = null;

        try {
            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();

            System.out.println("response-------" + response);

            is = conn.getInputStream();
            String contentAsString = convertInputStreamToString(is);

            System.out.println("contentAsString-------" + contentAsString);

            try {
                jObj = new JSONObject(contentAsString.trim());
                System.out.println("jObj-------" + jObj.toString());

                Model.prepaid_pack_json = jObj;

            } catch (JSONException e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }
            return jObj;

        } finally {
            if (is != null) {
                is.close();
            }
        }

    }


    public JSONArray getSearch(String url) throws IOException {
        InputStream is = null;

        try {
            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            System.out.println("response-------" + response);
            is = conn.getInputStream();


            contentAsString = convertInputStreamToString(is);
            System.out.println("contentAsString----" + contentAsString);

            JSONArray jsonarray = new JSONArray(contentAsString.trim());
            System.out.println("Length----" + jsonarray.length());

            return jsonarray;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public JSONArray getQueryNew(String url) throws IOException {
        InputStream is = null;
        try {
            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            System.out.println("response-------" + response);
            is = conn.getInputStream();

            contentAsString = convertInputStreamToString(is);
            System.out.println("contentAsString----" + contentAsString);

            JArr = new JSONArray(contentAsString);
            System.out.println("JArr----" + JArr.toString());

            return JArr;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String convertInputStreamToString(InputStream stream) throws
            IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
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


    public String getQA(String url) throws IOException {

        InputStream is = null;

        try {
            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            System.out.println("response-------" + response);

            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);
            System.out.println("contentAsString-------" + contentAsString);

            return contentAsString;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    private final OkHttpClient client = new OkHttpClient();

    public void getJSONString2(String url) throws Exception {
       /* RequestBody formBody = new FormEncodingBuilder()
                .add("email", "Jurassic@Park.com")
                .add("tel", "90301171XX")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();*/

        Request request = new Request.Builder()
                .url("https://covid-19-data.p.rapidapi.com/totals?format=undefined")
                .get()
                .addHeader("x-rapidapi-host", "covid-19-data.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "7aa728380dmshf6c31f3e0b09fbbp10f746jsne190aea0f62e")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println("COVID-----------" + response.body().string());
    }

}