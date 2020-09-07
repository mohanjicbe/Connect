package com.orane.icliniq.Model;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class Model {

    public static String App_ver = "19.9.76";
    public static String App_ver_slno = "120243";
    public static String App_Rel = "29-May-2020";

    //public static String font_name = "fonts/NotoSans-Regular.ttf";
    public static String font_name = "fonts/OpenSans-Regular.ttf";
    public static String font_name_italic = "fonts/OpenSans-Italic.ttf";
    public static String font_name_light = "fonts/OpenSans-Light.ttf";
    public static String font_name_bold_200 = "fonts/OpenSans-Bold.ttf";
    public static String font_name_bold = "fonts/OpenSans-SemiBold.ttf";

    public static String app_vendor_type = "SaaS";
    public static String hosp_domain,hosp_logo, hosp_name;

    public static String BASE_URL;
    //public static String Basic_server = "https://www.icliniq.com/";
    public static String Basic_server = "https://pentest.icliniq.com/";

    //public static String BASE_URL = "http://192.168.0.114/icliniq-git/web/index.php/";
    //public static String BASE_URL = "https://www.icliniq.com/";
    //public static String BASE_URL = "https://sriramakrishnahospital.icliniq.com/";
    //public static String BASE_URL = "https://pentest.icliniq.com/";
    //public static String BASE_URL = "http://3.83.253.45/index.php/";
    //public static String BASE_URL = "http://192.168.0.128/projects/icliniq/web/index.php/";
    //public static String BASE_URL = "http://192.168.1.101/projects/icliniq/web/index.php";
    //public static String BASE_URL = "http://192.168.0.132/projects/icliniq/web/index.php/";
    //public static String BASE_URL = "https://ecsdemo.icliniq.com/";
    //public static String BASE_URL="https://icliniq.cloudapp.net/index.php/";
    //public static Bitmap.Config config = new Bitmap.Config("icliniq", "HhEvRrQ9Qdar_mkHVXo_Qg");
    //http://icliniq.cloudapp.net/index.php

    public static KISSmetricsAPI kiss;
    public static FirebaseAnalytics mFirebaseAnalytics;

    public static String clinics, terms_isagree, device_token, vendor_name, agency_name, agency_val, doc_lang, treatment_skills, edit_query, doc_photo, doc_name, doc_sp, appt_id, meeting_id, docurl, screen_status, fcode, ftrack_show, cons_phno, sel_spec_code, query, speciality, photo_url, from, return_qid, local_file_url, mcode, mnum, str_status, first_query,
            mobvalidate, ftrack_fee, login_status, kmid, isValid, password, name, id, status, browser_country, email, fee_q, fee_consult, fee_q_inr, fee_consult_inr, currency_symbol, currency_label, have_free_credit;
    public static String qid = "";
    public static String dqid = "";
    public static String app_lang = "";
    public static String query_id = "";
    public static File imageFile;
    public static int mNotificationCounter = 5;
    public static String query_reponse, first_time;
    public static String family_list, fee_cp, edt_search_text_val, disease_type_text, fee_hp, fee_lp, cons_type, country_code, pat_country, selected_qid, push_qid, push_msg, push_type, push_title, select_specname, select_spec_val, ftrack_str_status, invlabel, inv_curr, querystatus, upload_files = "";
    public static String ftrack_mode, dashboard = "";
    public static String consult_id, open_url, query_cache, query_launch = "";
    public static String refcode, token, has_free_follow, spec_name, spec_code, cons_timezone_name, navi_next = "";
    public static String fule_full_path, sel_country_name, sel_country_code, cons_lang_code, cons_timezone_val, cons_select_date = "";
    public static String sel_timerange_code, cons_query, time_range, cons_date, cons_lang, cons_ccode, cons_number, cons_timezone;

    public static String prep_inv_id, prep_inv_fee, prep_inv_strfee, txn_id, askdrafttext, doctor_id, compmore, prevhist, curmedi, pastmedi, labtest;
    public static String attach_qid, chime_url, attach_status, attach_file_url, attach_filename, attach_id, inv_title, inv_desc, inv_fee, inv_walletfee, inv_btnconfirm, inv_browsercountry, invfeetot;
    public static JSONArray mydoctor, hotlinedocs, myconsultation, mybooking, myquery_aaray, doctor_aaray;
    public static JSONObject getTimeZone, prepaid_pack_json;

    public static String voxeet_cons_key = "OXEyM3VpZDZjYjVwdg==";
    public static String voxeet_cons_alias_name = "cGY2ZXM2M2ExaGZ1a3JkaWhzOGJzYmVybQ==";

    public static String affiliated_id;

    public static int update_alert_days = 2;

    //--------- Non-India text---------------------------------------------------
    public static String Main_Activity_title1 = "Phone / Video chats";
    public static String Main_Activity_how_desc = "Get a second opinion through phone call or video chat. It is private with mute and other privacy options";
    public static String Main_Activity_list1 = "3500+ Doctors from 80+ Specialities available to talk anytime.";
    public static String Main_Activity_list2 = "Post your health issue with a time preference.";
    public static String Main_Activity_list3 = "Pay the second opinion Fee.";
    public static String Main_Activity_list4 = "Doctor will confirm your schedule time.";

    public static String Main_Activity_bott = "Be ready and login on time to chat with the doctor. It is absolutely Private and Secure.";

    public static String MyConsultation_Toolbar_title = "Calls";
    public static String MyConsultation_nolist_placeholder = "You have no calls available right now,";
    public static String MyConsultation_nolist_desc = "Find the best doctors and book for a call with phone or video to contact them online now.";
    public static String MyConsultation_button = "Book Now";

    public static String Main_Activity_row_cons_tit = "Book for Phone/Video conversation";
    public static String Main_Activity_row_cons_desc = "Discuss with doctors Phone/Video conversation";

    public static String DoctorProfile_subtitle = "Select your preferences";
    public static String DoctorProfile_opttext1 = "Via Phone Talk";
    public static String DoctorProfile_opttext2 = "Via Video Chat";
    public static String DoctorProfile_share_text1 = "It is quick to ask a doctor";

    public static String Consultation_Home_Toolbar_title = "Get second opinion";
    public static String Consultation_Home_title_text1 = "Talk via Phone/Video";
    public static String Consultation_Home_title_text2 = "Talk with the doctors via Phone/Video";
    public static String Consultation_Home_title_text3 = "Join the Conversation";
    public static String Consultation_Home_button = "Book now";

    public static String Consultation1_toolbar_title = "Talk with Doctor via Phone/Video ";
    public static String Consultation1_opt_text1 = "On Phone Talk ";
    public static String Consultation1_opt_text2 = "On Video Chat";

    /* //--------- Nudgespot---------------------------------------------------
     public static String rest_apikey = "cc90c9217b3ffa0592bf358cd60b3a5c";
     public static String javascript_apikey = "9d67cce9132261dc918ff0bec8f46179";
     //--------- Nudgespot---------------------------------------------------*/

    //--------- AppSee------------------------------------------------
    //public static String appsee_apikey = "78d4aae237014aabbd1d76ada63bef46";
    public static String appsee_apikey = "";
    //--------- AppSee------------------------------------------------

    public static String app_url = "https://play.google.com/store/apps/details?id=com.orane.icliniq&hl=en_US";


    public static String kissmetric_apikey = "1e5980757ab1f8f93ba4261e104cebcee3d8d07e";
    //public static String kissmetric_apikey = "";


    public static String stripe_apikey = "pk_live_armHTlGI0IUJh8YhqKgUaKCs";
    //--------- KISSmetricsAPI----------------------------------------

    //--------- GCM---------------------------------------------------
    public static String PROJECT_NUMBER = "836825415714";
    //public static String PROJECT_NUMBER = "913715673848";  //Firebase
    //public static String PROJECT_NUMBER = "";
    //--------- GCM ---------------------------------------------------

    //--------- Google_analytics---------------------------------------------------
    public static String google_analytics_tracking_code = "UA-75517313-1";
    //public static String google_analytics_tracking_code = "";
    //--------- Google_analytics---------------------------------------------------

    //--------- Google_analytics---------------------------------------------------
    public static String razor_pay_public_key = "rzp_live_rqj0TdNTXlZUhQ";
    //--------- Google_analytics---------------------------------------------------


}
