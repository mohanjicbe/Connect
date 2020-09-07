package com.orane.icliniq.network;

import android.widget.TextView;


public class SetLanguage {

    public String sAux;

    public void set_lang(TextView tv, String lang, String tvname) {

        try {

            if (lang.equals("ta")) {
                //-------- settings fragment ----------------
                if (tvname.equals("tv_pref_tit")) tv.setText("விருப்பங்கள்");
                if (tvname.equals("tv_share_tit")) tv.setText("பகிர்ந்து கொள்ள");
                if (tvname.equals("tv_rate_app")) tv.setText("iCliniq App-ஐ Rate செய்க");
                if (tvname.equals("tv_share_friends"))
                    tv.setText("உங்கள் நண்பர்களிடம் பகிருங்கள் ");
                if (tvname.equals("tv_sugg_doc")) tv.setText("உங்களின் மருத்துவரை சிபாரிசு செய்ய");
                if (tvname.equals("tv_noti_sound")) tv.setText("அறிவிப்பு தகவலின் ஒலி ");
                if (tvname.equals("tv_noti_stat")) tv.setText("அறிவிப்பு தகவலை நிறுத்த ");
                if (tvname.equals("tv_terms")) tv.setText("விதிமுறைகள் மற்றும் நிபந்தனைகள்");
                if (tvname.equals("tv_privatepolicy")) tv.setText("தனியுரிமை கொள்கை");
                if (tvname.equals("tv_reportissue")) tv.setText("சிக்கலைப் புகார் செய்ய ");
                if (tvname.equals("tv_rudoc")) tv.setText("நீங்கள் மருத்துவரா?");
                if (tvname.equals("tv_signout")) tv.setText("வெளியேறு ");
                //-------- settings fragment ----------------
            } else {
                //-------- settings fragment ----------------
                if (tvname.equals("tv_pref_tit")) tv.setText("Preferences");
                if (tvname.equals("tv_share_tit")) tv.setText("Share");
                if (tvname.equals("tv_rate_app")) tv.setText("Rate icliniq app");
                if (tvname.equals("tv_share_friends")) tv.setText("Share App to your friends");
                if (tvname.equals("tv_sugg_doc")) tv.setText("Suggest icliniq to your doctor");
                if (tvname.equals("tv_noti_sound")) tv.setText("Notification sound");
                if (tvname.equals("tv_noti_stat")) tv.setText("Stop notification");
                if (tvname.equals("tv_terms")) tv.setText("Terms &amp; Conditions");
                if (tvname.equals("tv_privatepolicy")) tv.setText("Privacy policy");
                if (tvname.equals("tv_reportissue")) tv.setText("Report an issue");
                if (tvname.equals("tv_rudoc")) tv.setText("Are you a doctor?");
                if (tvname.equals("tv_signout")) tv.setText("Sign out");
                //-------- settings fragment ----------------
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}