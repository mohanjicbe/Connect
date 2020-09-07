package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hbb20.CountryCodePicker;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consultation1 extends AppCompatActivity {

    JSONObject jsonobj, fees_json;
    Map<String, String> spec_map = new HashMap<String, String>();
    Map<String, String> lang_map = new HashMap<String, String>();
    Map<String, String> cc_map = new HashMap<String, String>();
    CardView card_query;

    RadioGroup radioGroup;
    Spinner spinner_lang, spinner_speciality;
    RadioButton check_phone, check_video;
    EditText edt_issue, edt_phoneno;
    LinearLayout select_layout, vidoecons_layout, phonecons_layout;
    public String selected_cc_value, selected_cc_text, fee_cp, cons_type, ftrack_show, fee_hp, fee_lp, phone_no, cc_name, cccode, lang_name, lang_val, spec_name, text_query, spec_val;
    JSONArray jsonarray;
    Button btn_submit;
    TextView tv_title, tv_spec_name;
    ImageView img_remove;
    RadioButton rad_phone_cons, rad_video_cons;
    //private GoogleApiClient client;
    TextView mTitle, tv_ccode;
    RelativeLayout ccode_layout;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";

    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation1);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        countryCodePicker = findViewById(R.id.ccp);
        tv_title = findViewById(R.id.tv_title);
        tv_ccode = findViewById(R.id.tv_ccode);
        tv_spec_name = findViewById(R.id.tv_spec_name);
        img_remove = findViewById(R.id.img_remove);

        rad_phone_cons = findViewById(R.id.rad_phone_cons);
        rad_video_cons = findViewById(R.id.rad_video_cons);

        btn_submit = findViewById(R.id.btn_submit);
        spinner_speciality = findViewById(R.id.spinner_speciality);
        spinner_lang = findViewById(R.id.spinner_lang);
        check_phone = findViewById(R.id.check_phone);
        check_video = findViewById(R.id.check_video);
        edt_issue = findViewById(R.id.edt_issue);
        edt_phoneno = findViewById(R.id.edt_phoneno);
        phonecons_layout = findViewById(R.id.phonecons_layout);
        vidoecons_layout = findViewById(R.id.vidoecons_layout);
        select_layout = findViewById(R.id.select_layout);
        radioGroup = findViewById(R.id.group1);
        ccode_layout = findViewById(R.id.ccode_layout);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
            mTitle = toolbar.findViewById(R.id.toolbar_title);

            //-------------------Font Setting --------------------------------------
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            Typeface khand = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
            mTitle.setTypeface(khandBold);
            //-------------------Font Setting --------------------------------------

            TextView tvtit = findViewById(R.id.tv_ask_tit);

            tvtit.setTypeface(khandBold);

            ((TextView) findViewById(R.id.tv_ask_tit)).setTypeface(khandBold);
            ((TextView) findViewById(R.id.tvtit)).setTypeface(khand);
            ((RadioButton) findViewById(R.id.rad_phone_cons)).setTypeface(khand);
            ((RadioButton) findViewById(R.id.rad_video_cons)).setTypeface(khand);
            ((EditText) findViewById(R.id.edt_issue)).setTypeface(khand);
            ((TextView) findViewById(R.id.tv_spec_name)).setTypeface(khand);
            ((TextView) findViewById(R.id.tv_selectlang)).setTypeface(khand);
            ((EditText) findViewById(R.id.edt_phoneno)).setTypeface(khand);
            ((Button) findViewById(R.id.btn_submit)).setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Model.select_specname = "";
        Model.select_spec_val = "0";
        tv_spec_name.setText("Select Speciality (optional)");
        img_remove.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(edt_phoneno);

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                System.out.println("getSelectedCountryCode------------" + countryCodePicker.getSelectedCountryCode());
                System.out.println("getSelectedCountryCodeAsInt------------" + countryCodePicker.getSelectedCountryCodeAsInt());
                System.out.println("getSelectedCountryName------------" + countryCodePicker.getSelectedCountryName());
                System.out.println("getSelectedCountryNameCode------------" + countryCodePicker.getSelectedCountryNameCode());

                selected_cc_value = "" + countryCodePicker.getSelectedCountryCodeAsInt();

                tv_ccode.setText("+" + selected_cc_value);
                Model.cons_ccode = selected_cc_value;

                System.out.println("selected_cc_value---" + selected_cc_value);
            }
        });


        countryCodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //------------------------------- Country-------------------------------

        if (!(Model.browser_country).equals("IN")) {
            if (getSupportActionBar() != null) {
                //getSupportActionBar().setTitle(Model.Consultation1_toolbar_title);
                mTitle.setText(Model.Consultation1_toolbar_title);
                ((TextView) findViewById(R.id.tv_ask_tit)).setText("Book a call");
            }

            System.out.println("Model.browser_country-----------------------------" + Model.browser_country);
            rad_phone_cons.setText(Model.Consultation1_opt_text2);
            rad_video_cons.setText(Model.Consultation1_opt_text1);

        } else {
            System.out.println("Model.browser_country-----------------------------" + Model.browser_country);
            mTitle.setText("Phone/Video Consultation");
            ((TextView) findViewById(R.id.tv_ask_tit)).setText("Book a Consultation");

        }
        //------------------------ Country-------------------------------
        edt_issue.setHint(Html.fromHtml("Please Type your health query here..<br><br>Eg: I have hurt my leg as a cupboard fell on it, and I have a bruise that is around 20 cm in diameter. the hardness is still there. Why is the hardness not going away? Is it normal?"));
        Model.navi_next = "no";

        //country_code();

        if (new NetCheck().netcheck(Consultation1.this)) {
            //--------- Getting Consultation Fees --------------------
            String url = Model.BASE_URL + "/sapp/getBookingFtrackFee?user_id=" + (Model.id) + "&token=" + Model.token;
            System.out.println("Fees url-------------" + url);
            new JSON_getting_fees().execute(url);
            //------------Getting Consultation Fees -------------------
        } else {
            Toast.makeText(Consultation1.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }

        ccode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog_ccode();
            }
        });

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.select_specname = "";
                Model.select_spec_val = "0";
                tv_spec_name.setText("Select Speciality (optional)");
                img_remove.setVisibility(View.GONE);
            }
        });


        select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consultation1.this, SpecialityListActivity.class);
                startActivity(intent);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if (checkedRadioButtonId == -1) {
                    Toast.makeText(getApplicationContext(), "Please select consultation type", Toast.LENGTH_LONG).show();
                } else {
                    if (checkedRadioButtonId == R.id.rad_phone_cons) {
                        cons_type = "4";
                    }

                    if (checkedRadioButtonId == R.id.rad_video_cons) {
                        cons_type = "0";
                    }

                    if (new NetCheck().netcheck(Consultation1.this)) {
                        submit_form();
                    } else {
                        Toast.makeText(Consultation1.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        spinner_speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spec_name = spinner_speciality.getSelectedItem().toString();
                spec_val = spec_map.get(spec_name);

                Model.spec_name = spec_name;
                Model.spec_code = spec_val;

                System.out.println("Model.spec_name----------" + Model.spec_name);
                System.out.println("Model.spec_code----------" + Model.spec_code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lang_name = spinner_lang.getSelectedItem().toString();
                lang_val = lang_map.get(lang_name);

                Model.cons_lang = lang_name;
                Model.cons_lang_code = lang_val;

                System.out.println("Model.cons_lang----------" + Model.cons_lang);
                System.out.println("Model.cons_lang_code----------" + Model.cons_lang_code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


/*
        spinner_ccode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cc_name = spinner_ccode.getSelectedItem().toString();
                cccode = cc_map.get(cc_name);

                sel_country_code = cccode;
                sel_country_name = cc_name;

                System.out.println("sel_country_code------" + sel_country_code);
                System.out.println("sel_country_name------" + sel_country_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
*/
/*

        final List<String> categories = new ArrayList<String>();
        //categories.add("Choose a Speciality");
        //======================================================================
        categories.add("Choose a Speciality (optional)");
        spec_map.put("Choose a Speciality (optional)", "0");

        categories.add("Allergy Specialist");
        spec_map.put("Allergy Specialist", "269");

        categories.add("Andrology");
        spec_map.put("Andrology", "2");

        categories.add("Anesthesiology");
        spec_map.put("Anesthesiology", "1");

        categories.add("Audiology");
        spec_map.put("Audiology", "3");

        categories.add("Ayurveda Specialist");
        spec_map.put("Ayurveda Specialist", "226");

        categories.add("Cardiology");
        spec_map.put("Cardiology", "4");
        categories.add("Cardiothoracic Surgery");
        spec_map.put("Cardiothoracic Surgery", "5");
        categories.add("Child Health");
        spec_map.put("Child Health", "255");

        categories.add("Childbirth Educator");
        spec_map.put("Childbirth Educator", "228");

        categories.add("Childbirth Educator");
        spec_map.put("Childbirth Educator", "228");

        categories.add("Chiropractor");
        spec_map.put("Chiropractor", "225");

        categories.add("Community Medicine");
        spec_map.put("Community Medicine", "264");

        categories.add("Cosmetology");
        spec_map.put("Cosmetology", "258");

        categories.add("Critical Care Physician");
        spec_map.put("Critical Care Physician", "236");

        categories.add("Dentistry");
        spec_map.put("Dentistry", "6");

        categories.add("Dermatology");
        spec_map.put("Dermatology", "185");

        categories.add("Diabetology");
        spec_map.put("Diabetology", "186");

        categories.add("Dietician");
        spec_map.put("Dietician", "223");

        categories.add("Endocrinology");
        spec_map.put("Endocrinology", "187");

        categories.add("Endodontist");
        spec_map.put("Endodontist", "235");

        categories.add("Family Physician");
        spec_map.put("Family Physician", "220");

        categories.add("Fetal Medicine");
        spec_map.put("Fetal Medicine", "266");

        categories.add("Fitness Expert");
        spec_map.put("Fitness Expert", "224");

        categories.add("Forensic Medicine");
        spec_map.put("Forensic Medicine", "188");

        categories.add("General Medicine");
        spec_map.put("General Medicine", "239");

        categories.add("General Practitioner");
        spec_map.put("General Practitioner", "242");

        categories.add("General Surgery");
        spec_map.put("General Surgery", "191");

        categories.add("Geriatrics");
        spec_map.put("Geriatrics", "192");

        categories.add("Hair Transplant Surgeon");
        spec_map.put("Hair Transplant Surgeon", "249");

        categories.add("Hematology");
        spec_map.put("Hematology", "194");

        categories.add("HIV/AIDS Specialist");
        spec_map.put("HIV/AIDS Specialist", "244");

        categories.add("Homeopathy");
        spec_map.put("Homeopathy", "227");

        categories.add("Infertility");
        spec_map.put("Infertility", "267");

        categories.add("Internal Medicine");
        spec_map.put("Internal Medicine", "233");

        categories.add("Interventional Radiology");
        spec_map.put("Interventional Radiology", "250");

        categories.add("Lactation Counselor");
        spec_map.put("Lactation Counselor", "229");

        categories.add("Maxillofacial Prosthodontist");
        spec_map.put("Maxillofacial Prosthodontist", "246");

        categories.add("Medical Gastroenterology");
        spec_map.put("Medical Gastroenterology", "189");

        categories.add("Medical Oncology");
        spec_map.put("Medical Oncology", "200");

        categories.add("Medical Oncology");
        spec_map.put("Medical Oncology", "200");

        categories.add("Microbiology");
        spec_map.put("Microbiology", "195");

        categories.add("Naturopathy");
        spec_map.put("Naturopathy", "256");

        categories.add("Nephrology");
        spec_map.put("Nephrology", "196");

        categories.add("Neuro Surgery");
        spec_map.put("Neuro Surgery", "198");

        categories.add("Neurology");
        spec_map.put("Neurology", "197");

        categories.add("Nuclear Medicine");
        spec_map.put("Nuclear Medicine", "199");

        categories.add("Nutritionist");
        spec_map.put("Nutritionist", "222");

        categories.add("Obstetrics And Gynaecology");
        spec_map.put("Obstetrics And Gynaecology", "193");

        categories.add("Occupational Therapy");
        spec_map.put("Occupational Therapy", "273");

        categories.add("Ophthalmology (Eye Care)");
        spec_map.put("Ophthalmology (Eye Care)", "202");

        categories.add("Oral Implantologist");
        spec_map.put("Oral Implantologist", "247");

        categories.add("Orthodontist");
        spec_map.put("Orthodontist", "234");

        categories.add("Orthopedics And Traumatology");
        spec_map.put("Orthopedics And Traumatology", "203");

        categories.add("Otolaryngology (E.N.T)");
        spec_map.put("Otolaryngology (E.N.T)", "204");

        categories.add("Paediatric Dentistry");
        spec_map.put("Paediatric Dentistry", "237");

        categories.add("Paediatric Surgery");
        spec_map.put("Paediatric Surgery", "206");

        categories.add("Paediatrics");
        spec_map.put("Paediatrics", "205");

        categories.add("Pain Medicine");
        spec_map.put("Pain Medicine", "230");

        categories.add("Pathology");
        spec_map.put("Pathology", "207");

        categories.add("Pediatric Allergy/Asthma Specialist");
        spec_map.put("Pediatric Allergy/Asthma Specialist", "270");

        categories.add("Periodontist");
        spec_map.put("Periodontist", "238");

        categories.add("Pharmacology");
        spec_map.put("Pharmacology", "208");

        categories.add("Physiotherapy");
        spec_map.put("Physiotherapy", "221");

        categories.add("Plastic Surgery, Reconstructive And Cosmetic");
        spec_map.put("Plastic Surgery, Reconstructive And Cosmetic", "209");

        categories.add("Preventive Medicine");
        spec_map.put("Preventive Medicine", "210");

        categories.add("Psychiatry");
        spec_map.put("Psychiatry", "211");

        categories.add("Psychologist/ Counsellor");
        spec_map.put("Psychologist/ Counsellor", "219");

        categories.add("Psychotherapy");
        spec_map.put("Psychotherapy", "259");

        categories.add("Pulmonology (Asthma Doctors)");
        spec_map.put("Pulmonology (Asthma Doctors)", "212");

        categories.add("Radiation Oncology");
        spec_map.put("Radiation Oncology", "212");

        categories.add("Radiodiagnosis");
        spec_map.put("Radiodiagnosis", "213");

        categories.add("Radiology");
        spec_map.put("Radiology", "245");

        categories.add("Radiotherapy");
        spec_map.put("Radiotherapy", "214");

        categories.add("Rheumatology");
        spec_map.put("Rheumatology", "215");

        categories.add("Sexology");
        spec_map.put("Sexology", "248");

        categories.add("Siddha Medicine");
        spec_map.put("Siddha Medicine", "260");

        categories.add("Sleep Medicine");
        spec_map.put("Sleep Medicine", "268");

        categories.add("Sonologist");
        spec_map.put("Sonologist", "262");

        categories.add("Speech Therapist");
        spec_map.put("Speech Therapist", "240");

        categories.add("Spine Surgery");
        spec_map.put("Spine Surgery", "253");

        categories.add("Stem Cell Therapy");
        spec_map.put("Stem Cell Therapy", "272");

        categories.add("Surgical Gastroenterology");
        spec_map.put("Surgical Gastroenterology", "190");

        categories.add("Surgical Oncology");
        spec_map.put("Surgical Oncology", "201");

        categories.add("Toxicology");
        spec_map.put("Toxicology", "216");

        categories.add("Urology");
        spec_map.put("Urology", "217");

        categories.add("Vascular Surgery");
        spec_map.put("Vascular Surgery", "218");

        categories.add("Venereology");
        spec_map.put("Venereology", "263");

        categories.add("Yoga");
        spec_map.put("Yoga", "257");
        //=========================================================

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_speciality.setAdapter(dataAdapter);*/


        //------- Setting Language ----------------------
        final List<String> lang_categories = new ArrayList<String>();

        lang_categories.add("English");
        lang_map.put("English", "en");

        lang_categories.add("British English");
        lang_map.put("British English", "en_GB");

        lang_categories.add("Hindi");
        lang_map.put("Hindi", "hi");

        lang_categories.add("Tamil");
        lang_map.put("Tamil", "ta");

        lang_categories.add("Telugu");
        lang_map.put("Telugu", "te");

        lang_categories.add("French");
        lang_map.put("French", "fr");

        lang_categories.add("Spanish");
        lang_map.put("Spanish", "es");


        lang_categories.add("Abkhazian");
        lang_map.put("Abkhazian", "ab");
        lang_categories.add("Achinese");
        lang_map.put("Achinese", "ace");
        lang_categories.add("Acoli");
        lang_map.put("Acoli", "ach");
        lang_categories.add("Adangme");
        lang_map.put("Adangme", "ada");
        lang_categories.add("Adyghe");
        lang_map.put("Adyghe", "ady");
        lang_categories.add("Afar");
        lang_map.put("Afar", "aa");
        lang_categories.add("Afrihili");
        lang_map.put("Afrihili", "afh");
        lang_categories.add("Afrikaans");
        lang_map.put("Afrikaans", "af");
        lang_categories.add("Afro-Asiatic Language");
        lang_map.put("Afro-Asiatic Language", "afa");
        lang_categories.add("Ainu");
        lang_map.put("Ainu", "ain");
        lang_categories.add("Akan");
        lang_map.put("Akan", "ak");
        lang_categories.add("Akkadian");
        lang_map.put("Akkadian", "akk");
        lang_categories.add("Albanian");
        lang_map.put("Albanian", "sq");
        lang_categories.add("Aleut");
        lang_map.put("Aleut", "ale");
        lang_categories.add("Algonquian Language");
        lang_map.put("Algonquian Language", "alg");
        lang_categories.add("Altaic Language");
        lang_map.put("Altaic Language", "tut");
        lang_categories.add("Amharic");
        lang_map.put("Amharic", "am");
        lang_categories.add("Ancient Egyptian");
        lang_map.put("Ancient Egyptian", "egy");
        lang_categories.add("Ancient Greek");
        lang_map.put("Ancient Greek", "grc");
        lang_categories.add("Angika");
        lang_map.put("Angika", "anp");
        lang_categories.add("Apache Language");
        lang_map.put("Apache Language", "apa");
        lang_categories.add("Arabic");
        lang_map.put("Arabic", "ar");
        lang_categories.add("Aragonese");
        lang_map.put("Aragonese", "an");
        lang_categories.add("Aramaic");
        lang_map.put("Aramaic", "arc");
        lang_categories.add("Arapaho");
        lang_map.put("Arapaho", "arp");
        lang_categories.add("Araucanian");
        lang_map.put("Araucanian", "arn");
        lang_categories.add("Arawak");
        lang_map.put("Arawak", "arw");
        lang_categories.add("Armenian");
        lang_map.put("Armenian", "hy");
        lang_categories.add("Aromanian");
        lang_map.put("Aromanian", "rup");
        lang_categories.add("Artificial Language");
        lang_map.put("Artificial Language", "art");
        lang_categories.add("Assamese");
        lang_map.put("Assamese", "as");
        lang_categories.add("Asturian");
        lang_map.put("Asturian", "ast");
        lang_categories.add("Athapascan Language");
        lang_map.put("Athapascan Language", "ath");
        lang_categories.add("Atsam");
        lang_map.put("Atsam", "cch");
        lang_categories.add("Australian English");
        lang_map.put("Australian English", "en_AU");
        lang_categories.add("Australian Language");
        lang_map.put("Australian Language", "aus");
        lang_categories.add("Austrian German");
        lang_map.put("Austrian German", "de_AT");
        lang_categories.add("Austronesian Language");
        lang_map.put("Austronesian Language", "map");
        lang_categories.add("Avaric");
        lang_map.put("Avaric", "av");
        lang_categories.add("Avestan");
        lang_map.put("Avestan", "ae");
        lang_categories.add("Awadhi");
        lang_map.put("Awadhi", "awa");
        lang_categories.add("Aymara");
        lang_map.put("Aymara", "ay");
        lang_categories.add("Azerbaijani");
        lang_map.put("Azerbaijani", "az");
        lang_categories.add("Balinese");
        lang_map.put("Balinese", "ban");
        lang_categories.add("Baltic Language");
        lang_map.put("Baltic Language", "bat");
        lang_categories.add("Baluchi");
        lang_map.put("Baluchi", "bal");
        lang_categories.add("Bambara");
        lang_map.put("Bambara", "bm");
        lang_categories.add("Bamileke Language");
        lang_map.put("Bamileke Language", "bai");
        lang_categories.add("Banda");
        lang_map.put("Banda", "bad");
        lang_categories.add("Bantu");
        lang_map.put("Bantu", "bnt");
        lang_categories.add("Basa");
        lang_map.put("Basa", "bas");
        lang_categories.add("Bashkir");
        lang_map.put("Bashkir", "ba");
        lang_categories.add("Basque");
        lang_map.put("Basque", "eu");
        lang_categories.add("Batak");
        lang_map.put("Batak", "btk");
        lang_categories.add("Beja");
        lang_map.put("Beja", "bej");
        lang_categories.add("Belarusian");
        lang_map.put("Belarusian", "be");
        lang_categories.add("Bemba");
        lang_map.put("Bemba", "bem");
        lang_categories.add("Bengali");
        lang_map.put("Bengali", "bn");
        lang_categories.add("Berber");
        lang_map.put("Berber", "ber");
        lang_categories.add("Bhojpuri");
        lang_map.put("Bhojpuri", "bho");
        lang_categories.add("Bihari");
        lang_map.put("Bihari", "bh");
        lang_categories.add("Bikol");
        lang_map.put("Bikol", "bik");
        lang_categories.add("Bini");
        lang_map.put("Bini", "bin");
        lang_categories.add("Bislama");
        lang_map.put("Bislama", "bi");
        lang_categories.add("Blin");
        lang_map.put("Blin", "byn");
        lang_categories.add("Blissymbols");
        lang_map.put("Blissymbols", "zbl");
        lang_categories.add("Bosnian");
        lang_map.put("Bosnian", "bs");
        lang_categories.add("Braj");
        lang_map.put("Braj", "bra");
        lang_categories.add("Brazilian Portuguese");
        lang_map.put("Brazilian Portuguese", "pt_BR");
        lang_categories.add("Breton");
        lang_map.put("Breton", "br");
        lang_categories.add("British English");
        lang_map.put("British English", "en_GB");
        lang_categories.add("Buginese");
        lang_map.put("Buginese", "bug");
        lang_categories.add("Bulgarian");
        lang_map.put("Bulgarian", "bg");
        lang_categories.add("Buriat");
        lang_map.put("Buriat", "bua");
        lang_categories.add("Burmese");
        lang_map.put("Burmese", "my");
        lang_categories.add("Caddo");
        lang_map.put("Caddo", "cad");
        lang_categories.add("Canadian English");
        lang_map.put("Canadian English", "en_CA");
        lang_categories.add("Canadian French");
        lang_map.put("Canadian French", "fr_CA");
        lang_categories.add("Carib");
        lang_map.put("Carib", "car");
        lang_categories.add("Catalan");
        lang_map.put("Catalan", "ca");
        lang_categories.add("Caucasian Language");
        lang_map.put("Caucasian Language", "cau");
        lang_categories.add("Cebuano");
        lang_map.put("Cebuano", "ceb");
        lang_categories.add("Celtic Language");
        lang_map.put("Celtic Language", "cel");
        lang_categories.add("Central American Indian Language");
        lang_map.put("Central American Indian Language", "cai");
        lang_categories.add("Chagatai");
        lang_map.put("Chagatai", "chg");
        lang_categories.add("Chamic Language");
        lang_map.put("Chamic Language", "cmc");
        lang_categories.add("Chamorro");
        lang_map.put("Chamorro", "ch");
        lang_categories.add("Chechen");
        lang_map.put("Chechen", "ce");
        lang_categories.add("Cherokee");
        lang_map.put("Cherokee", "chr");
        lang_categories.add("Cheyenne");
        lang_map.put("Cheyenne", "chy");
        lang_categories.add("Chibcha");
        lang_map.put("Chibcha", "chb");
        lang_categories.add("Chinese");
        lang_map.put("Chinese", "zh");
        lang_categories.add("Chinook Jargon");
        lang_map.put("Chinook Jargon", "chn");
        lang_categories.add("Chipewyan");
        lang_map.put("Chipewyan", "chp");
        lang_categories.add("Choctaw");
        lang_map.put("Choctaw", "cho");
        lang_categories.add("Church Slavic");
        lang_map.put("Church Slavic", "cu");
        lang_categories.add("Chuukese");
        lang_map.put("Chuukese", "chk");
        lang_categories.add("Chuvash");
        lang_map.put("Chuvash", "cv");
        lang_categories.add("Classical Newari");
        lang_map.put("Classical Newari", "nwc");
        lang_categories.add("Classical Syriac");
        lang_map.put("Classical Syriac", "syc");
        lang_categories.add("Coptic");
        lang_map.put("Coptic", "cop");
        lang_categories.add("Cornish");
        lang_map.put("Cornish", "kw");
        lang_categories.add("Corsican");
        lang_map.put("Corsican", "co");
        lang_categories.add("Cree");
        lang_map.put("Cree", "cr");
        lang_categories.add("Creek");
        lang_map.put("Creek", "mus");
        lang_categories.add("Creole or Pidgin");
        lang_map.put("Creole or Pidgin", "crp");
        lang_categories.add("Crimean Turkish");
        lang_map.put("Crimean Turkish", "crh");
        lang_categories.add("Croatian");
        lang_map.put("Croatian", "hr");
        lang_categories.add("Cushitic Language");
        lang_map.put("Cushitic Language", "cus");
        lang_categories.add("Czech");
        lang_map.put("Czech", "cs");
        lang_categories.add("Dakota");
        lang_map.put("Dakota", "dak");
        lang_categories.add("Danish");
        lang_map.put("Danish", "da");
        lang_categories.add("Dargwa");
        lang_map.put("Dargwa", "dar");
        lang_categories.add("Dayak");
        lang_map.put("Dayak", "day");
        lang_categories.add("Delaware");
        lang_map.put("Delaware", "del");
        lang_categories.add("Dinka");
        lang_map.put("Dinka", "din");
        lang_categories.add("Divehi");
        lang_map.put("Divehi", "dv");
        lang_categories.add("Dogri");
        lang_map.put("Dogri", "doi");
        lang_categories.add("Dogrib");
        lang_map.put("Dogrib", "dgr");
        lang_categories.add("Dravidian Language");
        lang_map.put("Dravidian Language", "dra");
        lang_categories.add("Duala");
        lang_map.put("Duala", "dua");
        lang_categories.add("Dutch");
        lang_map.put("Dutch", "nl");
        lang_categories.add("Dyula");
        lang_map.put("Dyula", "dyu");
        lang_categories.add("Dzongkha");
        lang_map.put("Dzongkha", "dz");
        lang_categories.add("Eastern Frisian");
        lang_map.put("Eastern Frisian", "frs");
        lang_categories.add("Efik");
        lang_map.put("Efik", "efi");
        lang_categories.add("Ekajuk");
        lang_map.put("Ekajuk", "eka");
        lang_categories.add("Elamite");
        lang_map.put("Elamite", "elx");
        lang_categories.add("English");
        lang_map.put("English", "en");
        lang_categories.add("English-based Creole or Pidgin");
        lang_map.put("English-based Creole or Pidgin", "cpe");
        lang_categories.add("Erzya");
        lang_map.put("Erzya", "myv");
        lang_categories.add("Esperanto");
        lang_map.put("Esperanto", "eo");
        lang_categories.add("Estonian");
        lang_map.put("Estonian", "et");
        lang_categories.add("Ewe");
        lang_map.put("Ewe", "ee");
        lang_categories.add("Ewondo");
        lang_map.put("Ewondo", "ewo");
        lang_categories.add("Fang");
        lang_map.put("Fang", "fan");
        lang_categories.add("Fanti");
        lang_map.put("Fanti", "fat");
        lang_categories.add("Faroese");
        lang_map.put("Faroese", "fo");
        lang_categories.add("Fijian");
        lang_map.put("Fijian", "fj");
        lang_categories.add("Filipino");
        lang_map.put("Filipino", "fil");
        lang_categories.add("Finnish");
        lang_map.put("Finnish", "fi");
        lang_categories.add("Finno-Ugrian Language");
        lang_map.put("Finno-Ugrian Language", "fiu");
        lang_categories.add("Flemish");
        lang_map.put("Flemish", "nl_BE");
        lang_categories.add("Fon");
        lang_map.put("Fon", "fon");
        lang_categories.add("French");
        lang_map.put("French", "fr");
        lang_categories.add("French-based Creole or Pidgin");
        lang_map.put("French-based Creole or Pidgin", "cpf");
        lang_categories.add("Friulian");
        lang_map.put("Friulian", "fur");
        lang_categories.add("Fulah");
        lang_map.put("Fulah", "ff");
        lang_categories.add("Ga");
        lang_map.put("Ga", "gaa");
        lang_categories.add("Galician");
        lang_map.put("Galician", "gl");
        lang_categories.add("Ganda");
        lang_map.put("Ganda", "lg");
        lang_categories.add("Gayo");
        lang_map.put("Gayo", "gay");
        lang_categories.add("Gbaya");
        lang_map.put("Gbaya", "gba");
        lang_categories.add("Geez");
        lang_map.put("Geez", "gez");
        lang_categories.add("Georgian");
        lang_map.put("Georgian", "ka");
        lang_categories.add("German");
        lang_map.put("German", "de");
        lang_categories.add("Germanic Language");
        lang_map.put("Germanic Language", "gem");
        lang_categories.add("Gilbertese");
        lang_map.put("Gilbertese", "gil");
        lang_categories.add("Gondi");
        lang_map.put("Gondi", "gon");
        lang_categories.add("Gorontalo");
        lang_map.put("Gorontalo", "gor");
        lang_categories.add("Gothic");
        lang_map.put("Gothic", "got");
        lang_categories.add("Grebo");
        lang_map.put("Grebo", "grb");
        lang_categories.add("Greek");
        lang_map.put("Greek", "el");
        lang_categories.add("Guarani");
        lang_map.put("Guarani", "gn");
        lang_categories.add("Gujarati");
        lang_map.put("Gujarati", "gu");
        lang_categories.add("Gwichʼin");
        lang_map.put("Gwichʼin", "gwi");
        lang_categories.add("Haida");
        lang_map.put("Haida", "hai");
        lang_categories.add("Haitian");
        lang_map.put("Haitian", "ht");
        lang_categories.add("Hausa");
        lang_map.put("Hausa", "ha");
        lang_categories.add("Hawaiian");
        lang_map.put("Hawaiian", "haw");
        lang_categories.add("Hebrew");
        lang_map.put("Hebrew", "he");
        lang_categories.add("Herero");
        lang_map.put("Herero", "hz");
        lang_categories.add("Hiligaynon");
        lang_map.put("Hiligaynon", "hil");
        lang_categories.add("Himachali");
        lang_map.put("Himachali", "him");
        lang_categories.add("Hindi");
        lang_map.put("Hindi", "hi");
        lang_categories.add("Hiri Motu");
        lang_map.put("Hiri Motu", "ho");
        lang_categories.add("Hittite");
        lang_map.put("Hittite", "hit");
        lang_categories.add("Hmong");
        lang_map.put("Hmong", "hmn");
        lang_categories.add("Hungarian");
        lang_map.put("Hungarian", "hu");
        lang_categories.add("Hupa");
        lang_map.put("Hupa", "hup");
        lang_categories.add("Iban");
        lang_map.put("Iban", "iba");
        lang_categories.add("Iberian Portuguese");
        lang_map.put("Iberian Portuguese", "pt_PT");
        lang_categories.add("Iberian Spanish");
        lang_map.put("Iberian Spanish", "es_ES");
        lang_categories.add("Icelandic");
        lang_map.put("Icelandic", "is");
        lang_categories.add("Ido");
        lang_map.put("Ido", "io");
        lang_categories.add("Igbo");
        lang_map.put("Igbo", "ig");
        lang_categories.add("Ijo");
        lang_map.put("Ijo", "ijo");
        lang_categories.add("Iloko");
        lang_map.put("Iloko", "ilo");
        lang_categories.add("Inari Sami");
        lang_map.put("Inari Sami", "smn");
        lang_categories.add("Indic Language");
        lang_map.put("Indic Language", "inc");
        lang_categories.add("Indo-European Language");
        lang_map.put("Indo-European Language", "ine");
        lang_categories.add("Indonesian");
        lang_map.put("Indonesian", "id");
        lang_categories.add("Ingush");
        lang_map.put("Ingush", "inh");
        lang_categories.add("Interlingua");
        lang_map.put("Interlingua", "ia");
        lang_categories.add("Interlingue");
        lang_map.put("Interlingue", "ie");
        lang_categories.add("Inuktitut");
        lang_map.put("Inuktitut", "iu");
        lang_categories.add("Inupiaq");
        lang_map.put("Inupiaq", "ik");
        lang_categories.add("Iranian Language");
        lang_map.put("Iranian Language", "ira");
        lang_categories.add("Irish");
        lang_map.put("Irish", "ga");
        lang_categories.add("Iroquoian Language");
        lang_map.put("Iroquoian Language", "iro");
        lang_categories.add("Italian");
        lang_map.put("Italian", "it");
        lang_categories.add("Japanese");
        lang_map.put("Japanese", "ja");
        lang_categories.add("Javanese");
        lang_map.put("Javanese", "jv");
        lang_categories.add("Jju");
        lang_map.put("Jju", "kaj");
        lang_categories.add("Judeo-Arabic");
        lang_map.put("Judeo-Arabic", "jrb");
        lang_categories.add("Judeo-Persian");
        lang_map.put("Judeo-Persian", "jpr");
        lang_categories.add("Kabardian");
        lang_map.put("Kabardian", "kbd");
        lang_categories.add("Kabyle");
        lang_map.put("Kabyle", "kab");
        lang_categories.add("Kachin");
        lang_map.put("Kachin", "kac");
        lang_categories.add("Kalaallisut");
        lang_map.put("Kalaallisut", "kl");
        lang_categories.add("Kalmyk");
        lang_map.put("Kalmyk", "xal");
        lang_categories.add("Kamba");
        lang_map.put("Kamba", "kam");
        lang_categories.add("Kannada");
        lang_map.put("Kannada", "kn");
        lang_categories.add("Kanuri");
        lang_map.put("Kanuri", "kr");
        lang_categories.add("Karachay-Balkar");
        lang_map.put("Karachay-Balkar", "krc");
        lang_categories.add("Kara-Kalpak");
        lang_map.put("Kara-Kalpak", "kaa");
        lang_categories.add("Karelian");
        lang_map.put("Karelian", "krl");
        lang_categories.add("Karen");
        lang_map.put("Karen", "kar");
        lang_categories.add("Kashmiri");
        lang_map.put("Kashmiri", "ks");
        lang_categories.add("Kashubian");
        lang_map.put("Kashubian", "csb");
        lang_categories.add("Kawi");
        lang_map.put("Kawi", "kaw");
        lang_categories.add("Kazakh");
        lang_map.put("Kazakh", "kk");
        lang_categories.add("Khasi");
        lang_map.put("Khasi", "kha");
        lang_categories.add("Khmer");
        lang_map.put("Khmer", "km");
        lang_categories.add("Khoisan Language");
        lang_map.put("Khoisan Language", "khi");
        lang_categories.add("Khotanese");
        lang_map.put("Khotanese", "kho");
        lang_categories.add("Kikuyu");
        lang_map.put("Kikuyu", "ki");
        lang_categories.add("Kimbundu");
        lang_map.put("Kimbundu", "kmb");
        lang_categories.add("Kinyarwanda");
        lang_map.put("Kinyarwanda", "rw");
        lang_categories.add("Kirghiz");
        lang_map.put("Kirghiz", "ky");
        lang_categories.add("Klingon");
        lang_map.put("Klingon", "tlh");
        lang_categories.add("Komi");
        lang_map.put("Komi", "kv");
        lang_categories.add("Kongo");
        lang_map.put("Kongo", "kg");
        lang_categories.add("Konkani");
        lang_map.put("Konkani", "kok");
        lang_categories.add("Korean");
        lang_map.put("Korean", "ko");
        lang_categories.add("Koro");
        lang_map.put("Koro", "kfo");
        lang_categories.add("Kosraean");
        lang_map.put("Kosraean", "kos");
        lang_categories.add("Kpelle");
        lang_map.put("Kpelle", "kpe");
        lang_categories.add("Kru");
        lang_map.put("Kru", "kro");
        lang_categories.add("Kuanyama");
        lang_map.put("Kuanyama", "kj");
        lang_categories.add("Kumyk");
        lang_map.put("Kumyk", "kum");
        lang_categories.add("Kurdish");
        lang_map.put("Kurdish", "ku");
        lang_categories.add("Kurukh");
        lang_map.put("Kurukh", "kru");
        lang_categories.add("Kutenai");
        lang_map.put("Kutenai", "kut");
        lang_categories.add("Ladino");
        lang_map.put("Ladino", "lad");
        lang_categories.add("Lahnda");
        lang_map.put("Lahnda", "lah");
        lang_categories.add("Lamba");
        lang_map.put("Lamba", "lam");
        lang_categories.add("Lao");
        lang_map.put("Lao", "lo");
        lang_categories.add("Latin");
        lang_map.put("Latin", "la");
        lang_categories.add("Latin American Spanish");
        lang_map.put("Latin American Spanish", "es_419");
        lang_categories.add("Latvian");
        lang_map.put("Latvian", "lv");
        lang_categories.add("Lezghian");
        lang_map.put("Lezghian", "lez");
        lang_categories.add("Limburgish");
        lang_map.put("Limburgish", "li");
        lang_categories.add("Lingala");
        lang_map.put("Lingala", "ln");
        lang_categories.add("Lithuanian");
        lang_map.put("Lithuanian", "lt");
        lang_categories.add("Lojban");
        lang_map.put("Lojban", "jbo");
        lang_categories.add("Lower Sorbian");
        lang_map.put("Lower Sorbian", "dsb");
        lang_categories.add("Low German");
        lang_map.put("Low German", "nds");
        lang_categories.add("Lozi");
        lang_map.put("Lozi", "loz");
        lang_categories.add("Luba-Katanga");
        lang_map.put("Luba-Katanga", "lu");
        lang_categories.add("Luba-Lulua");
        lang_map.put("Luba-Lulua", "lua");
        lang_categories.add("Luiseno");
        lang_map.put("Luiseno", "lui");
        lang_categories.add("Lule Sami");
        lang_map.put("Lule Sami", "smj");
        lang_categories.add("Lunda");
        lang_map.put("Lunda", "lun");
        lang_categories.add("Luo");
        lang_map.put("Luo", "luo");
        lang_categories.add("Lushai");
        lang_map.put("Lushai", "lus");
        lang_categories.add("Luxembourgish");
        lang_map.put("Luxembourgish", "lb");
        lang_categories.add("Macedonian");
        lang_map.put("Macedonian", "mk");
        lang_categories.add("Madurese");
        lang_map.put("Madurese", "mad");
        lang_categories.add("Magahi");
        lang_map.put("Magahi", "mag");
        lang_categories.add("Maithili");
        lang_map.put("Maithili", "mai");
        lang_categories.add("Makasar");
        lang_map.put("Makasar", "mak");
        lang_categories.add("Malagasy");
        lang_map.put("Malagasy", "mg");
        lang_categories.add("Malay");
        lang_map.put("Malay", "ms");
        lang_categories.add("Malayalam");
        lang_map.put("Malayalam", "ml");
        lang_categories.add("Maltese");
        lang_map.put("Maltese", "mt");
        lang_categories.add("Manchu");
        lang_map.put("Manchu", "mnc");
        lang_categories.add("Mandar");
        lang_map.put("Mandar", "mdr");
        lang_categories.add("Mandingo");
        lang_map.put("Mandingo", "man");
        lang_categories.add("Manipuri");
        lang_map.put("Manipuri", "mni");
        lang_categories.add("Manobo Language");
        lang_map.put("Manobo Language", "mno");
        lang_categories.add("Manx");
        lang_map.put("Manx", "gv");
        lang_categories.add("Maori");
        lang_map.put("Maori", "mi");
        lang_categories.add("Marathi");
        lang_map.put("Marathi", "mr");
        lang_categories.add("Mari");
        lang_map.put("Mari", "chm");
        lang_categories.add("Marshallese");
        lang_map.put("Marshallese", "mh");
        lang_categories.add("Marwari");
        lang_map.put("Marwari", "mwr");
        lang_categories.add("Masai");
        lang_map.put("Masai", "mas");
        lang_categories.add("Mayan Language");
        lang_map.put("Mayan Language", "myn");
        lang_categories.add("Mende");
        lang_map.put("Mende", "men");
        lang_categories.add("Micmac");
        lang_map.put("Micmac", "mic");
        lang_categories.add("Middle Dutch");
        lang_map.put("Middle Dutch", "dum");
        lang_categories.add("Middle English");
        lang_map.put("Middle English", "enm");
        lang_categories.add("Middle French");
        lang_map.put("Middle French", "frm");
        lang_categories.add("Middle High German");
        lang_map.put("Middle High German", "gmh");
        lang_categories.add("Middle Irish");
        lang_map.put("Middle Irish", "mga");
        lang_categories.add("Minangkabau");
        lang_map.put("Minangkabau", "min");
        lang_categories.add("Mirandese");
        lang_map.put("Mirandese", "mwl");
        lang_categories.add("Miscellaneous Language");
        lang_map.put("Miscellaneous Language", "mis");
        lang_categories.add("Mohawk");
        lang_map.put("Mohawk", "moh");
        lang_categories.add("Moksha");
        lang_map.put("Moksha", "mdf");
        lang_categories.add("Moldavian");
        lang_map.put("Moldavian", "mo");
        lang_categories.add("Mongo");
        lang_map.put("Mongo", "lol");
        lang_categories.add("Mongolian");
        lang_map.put("Mongolian", "mn");
        lang_categories.add("Mon-Khmer Language");
        lang_map.put("Mon-Khmer Language", "mkh");
        lang_categories.add("Morisyen");
        lang_map.put("Morisyen", "mfe");
        lang_categories.add("Mossi");
        lang_map.put("Mossi", "mos");
        lang_categories.add("Multiple Languages");
        lang_map.put("Multiple Languages", "mul");
        lang_categories.add("Munda Language");
        lang_map.put("Munda Language", "mun");
        lang_categories.add("Nahuatl");
        lang_map.put("Nahuatl", "nah");
        lang_categories.add("Nauru");
        lang_map.put("Nauru", "na");
        lang_categories.add("Navajo");
        lang_map.put("Navajo", "nv");
        lang_categories.add("Ndonga");
        lang_map.put("Ndonga", "ng");
        lang_categories.add("Neapolitan");
        lang_map.put("Neapolitan", "nap");
        lang_categories.add("Nepali");
        lang_map.put("Nepali", "ne");
        lang_categories.add("Newari");
        lang_map.put("Newari", "new");
        lang_categories.add("Nias");
        lang_map.put("Nias", "nia");
        lang_categories.add("Niger-Kordofanian Language");
        lang_map.put("Niger-Kordofanian Language", "nic");
        lang_categories.add("Nilo-Saharan Language");
        lang_map.put("Nilo-Saharan Language", "ssa");
        lang_categories.add("Niuean");
        lang_map.put("Niuean", "niu");
        lang_categories.add("N’Ko");
        lang_map.put("N’Ko", "nqo");
        lang_categories.add("Nogai");
        lang_map.put("Nogai", "nog");
        lang_categories.add("No linguistic content");
        lang_map.put("No linguistic content", "zxx");
        lang_categories.add("North American Indian Language");
        lang_map.put("North American Indian Language", "nai");
        lang_categories.add("Northern Frisian");
        lang_map.put("Northern Frisian", "frr");
        lang_categories.add("Northern Sami");
        lang_map.put("Northern Sami", "se");
        lang_categories.add("Northern Sotho");
        lang_map.put("Northern Sotho", "nso");
        lang_categories.add("North Ndebele");
        lang_map.put("North Ndebele", "nd");
        lang_categories.add("Norwegian");
        lang_map.put("Norwegian", "no");
        lang_categories.add("Norwegian Bokmål");
        lang_map.put("Norwegian Bokmål", "nb");
        lang_categories.add("Norwegian Nynorsk");
        lang_map.put("Norwegian Nynorsk", "nn");
        lang_categories.add("Nubian Language");
        lang_map.put("Nubian Language", "nub");
        lang_categories.add("Nyamwezi");
        lang_map.put("Nyamwezi", "nym");
        lang_categories.add("Nyanja");
        lang_map.put("Nyanja", "ny");
        lang_categories.add("Nyankole");
        lang_map.put("Nyankole", "nyn");
        lang_categories.add("Nyasa Tonga");
        lang_map.put("Nyasa Tonga", "tog");
        lang_categories.add("Nyoro");
        lang_map.put("Nyoro", "nyo");
        lang_categories.add("Nzima");
        lang_map.put("Nzima", "nzi");
        lang_categories.add("Occitan");
        lang_map.put("Occitan", "oc");
        lang_categories.add("Ojibwa");
        lang_map.put("Ojibwa", "oj");
        lang_categories.add("Old English");
        lang_map.put("Old English", "ang");
        lang_categories.add("Old French");
        lang_map.put("Old French", "fro");
        lang_categories.add("Old High German");
        lang_map.put("Old High German", "goh");
        lang_categories.add("Old Irish");
        lang_map.put("Old Irish", "sga");
        lang_categories.add("Old Norse");
        lang_map.put("Old Norse", "non");
        lang_categories.add("Old Persian");
        lang_map.put("Old Persian", "peo");
        lang_categories.add("Old Provençal");
        lang_map.put("Old Provençal", "pro");
        lang_categories.add("Oriya");
        lang_map.put("Oriya", "or");
        lang_categories.add("Oromo");
        lang_map.put("Oromo", "om");
        lang_categories.add("Osage");
        lang_map.put("Osage", "osa");
        lang_categories.add("Ossetic");
        lang_map.put("Ossetic", "os");
        lang_categories.add("Otomian Language");
        lang_map.put("Otomian Language", "oto");
        lang_categories.add("Ottoman Turkish");
        lang_map.put("Ottoman Turkish", "ota");
        lang_categories.add("Pahlavi");
        lang_map.put("Pahlavi", "pal");
        lang_categories.add("Palauan");
        lang_map.put("Palauan", "pau");
        lang_categories.add("Pali");
        lang_map.put("Pali", "pi");
        lang_categories.add("Pampanga");
        lang_map.put("Pampanga", "pam");
        lang_categories.add("Pangasinan");
        lang_map.put("Pangasinan", "pag");
        lang_categories.add("Papiamento");
        lang_map.put("Papiamento", "pap");
        lang_categories.add("Papuan Language");
        lang_map.put("Papuan Language", "paa");
        lang_categories.add("Pashto");
        lang_map.put("Pashto", "ps");
        lang_categories.add("Persian");
        lang_map.put("Persian", "fa");
        lang_categories.add("Philippine Language");
        lang_map.put("Philippine Language", "phi");
        lang_categories.add("Phoenician");
        lang_map.put("Phoenician", "phn");
        lang_categories.add("Pohnpeian");
        lang_map.put("Pohnpeian", "pon");
        lang_categories.add("Polish");
        lang_map.put("Polish", "pl");
        lang_categories.add("Portuguese");
        lang_map.put("Portuguese", "pt");
        lang_categories.add("Portuguese-based Creole or Pidgin");
        lang_map.put("Portuguese-based Creole or Pidgin", "cpp");
        lang_categories.add("Prakrit Language");
        lang_map.put("Prakrit Language", "pra");
        lang_categories.add("Punjabi");
        lang_map.put("Punjabi", "pa");
        lang_categories.add("Quechua");
        lang_map.put("Quechua", "qu");
        lang_categories.add("Rajasthani");
        lang_map.put("Rajasthani", "raj");
        lang_categories.add("Rapanui");
        lang_map.put("Rapanui", "rap");
        lang_categories.add("Rarotongan");
        lang_map.put("Rarotongan", "rar");
        lang_categories.add("Rhaeto-Romance");
        lang_map.put("Rhaeto-Romance", "rm");
        lang_categories.add("Romance Language");
        lang_map.put("Romance Language", "roa");
        lang_categories.add("Romanian");
        lang_map.put("Romanian", "ro");
        lang_categories.add("Romany");
        lang_map.put("Romany", "rom");
        lang_categories.add("Root");
        lang_map.put("Root", "root");
        lang_categories.add("Rundi");
        lang_map.put("Rundi", "rn");
        lang_categories.add("Russian");
        lang_map.put("Russian", "ru");
        lang_categories.add("Salishan Language");
        lang_map.put("Salishan Language", "sal");
        lang_categories.add("Samaritan Aramaic");
        lang_map.put("Samaritan Aramaic", "sam");
        lang_categories.add("Sami Language");
        lang_map.put("Sami Language", "smi");
        lang_categories.add("Samoan");
        lang_map.put("Samoan", "sm");
        lang_categories.add("Sandawe");
        lang_map.put("Sandawe", "sad");
        lang_categories.add("Sango");
        lang_map.put("Sango", "sg");
        lang_categories.add("Sanskrit");
        lang_map.put("Sanskrit", "sa");
        lang_categories.add("Santali");
        lang_map.put("Santali", "sat");
        lang_categories.add("Sardinian");
        lang_map.put("Sardinian", "sc");
        lang_categories.add("Sasak");
        lang_map.put("Sasak", "sas");
        lang_categories.add("Scots");
        lang_map.put("Scots", "sco");
        lang_categories.add("Scottish Gaelic");
        lang_map.put("Scottish Gaelic", "gd");
        lang_categories.add("Selkup");
        lang_map.put("Selkup", "sel");
        lang_categories.add("Semitic Language");
        lang_map.put("Semitic Language", "sem");
        lang_categories.add("Serbian");
        lang_map.put("Serbian", "sr");
        lang_categories.add("Serbo-Croatian");
        lang_map.put("Serbo-Croatian", "sh");
        lang_categories.add("Serer");
        lang_map.put("Serer", "srr");
        lang_categories.add("Shan");
        lang_map.put("Shan", "shn");
        lang_categories.add("Shona");
        lang_map.put("Shona", "sn");
        lang_categories.add("Sichuan Yi");
        lang_map.put("Sichuan Yi", "ii");
        lang_categories.add("Sicilian");
        lang_map.put("Sicilian", "scn");
        lang_categories.add("Sidamo");
        lang_map.put("Sidamo", "sid");
        lang_categories.add("Sign Language");
        lang_map.put("Sign Language", "sgn");
        lang_categories.add("Siksika");
        lang_map.put("Siksika", "bla");
        lang_categories.add("Simplified Chinese");
        lang_map.put("Simplified Chinese", "zh_Hans");
        lang_categories.add("Sindhi");
        lang_map.put("Sindhi", "sd");
        lang_categories.add("Sinhala");
        lang_map.put("Sinhala", "si");
        lang_categories.add("Sino-Tibetan Language");
        lang_map.put("Sino-Tibetan Language", "sit");
        lang_categories.add("Siouan Language");
        lang_map.put("Siouan Language", "sio");
        lang_categories.add("Skolt Sami");
        lang_map.put("Skolt Sami", "sms");
        lang_categories.add("Slave");
        lang_map.put("Slave", "den");
        lang_categories.add("Slavic Language");
        lang_map.put("Slavic Language", "sla");
        lang_categories.add("Slovak");
        lang_map.put("Slovak", "sk");
        lang_categories.add("Slovenian");
        lang_map.put("Slovenian", "sl");
        lang_categories.add("Sogdien");
        lang_map.put("Sogdien", "sog");
        lang_categories.add("Somali");
        lang_map.put("Somali", "so");
        lang_categories.add("Songhai");
        lang_map.put("Songhai", "son");
        lang_categories.add("Soninke");
        lang_map.put("Soninke", "snk");
        lang_categories.add("Sorbian Language");
        lang_map.put("Sorbian Language", "wen");
        lang_categories.add("South American Indian Language");
        lang_map.put("South American Indian Language", "sai");
        lang_categories.add("Southern Altai");
        lang_map.put("Southern Altai", "alt");
        lang_categories.add("Southern Sami");
        lang_map.put("Southern Sami", "sma");
        lang_categories.add("Southern Sotho");
        lang_map.put("Southern Sotho", "st");
        lang_categories.add("South Ndebele");
        lang_map.put("South Ndebele", "nr");
        lang_categories.add("Spanish");
        lang_map.put("Spanish", "es");
        lang_categories.add("Sranan Tongo");
        lang_map.put("Sranan Tongo", "srn");
        lang_categories.add("Sukuma");
        lang_map.put("Sukuma", "suk");
        lang_categories.add("Sumerian");
        lang_map.put("Sumerian", "sux");
        lang_categories.add("Sundanese");
        lang_map.put("Sundanese", "su");
        lang_categories.add("Susu");
        lang_map.put("Susu", "sus");
        lang_categories.add("Swahili");
        lang_map.put("Swahili", "sw");
        lang_categories.add("Swati");
        lang_map.put("Swati", "ss");
        lang_categories.add("Swedish");
        lang_map.put("Swedish", "sv");
        lang_categories.add("Swiss French");
        lang_map.put("Swiss French", "fr_CH");
        lang_categories.add("Swiss German");
        lang_map.put("Swiss German", "gsw");
        lang_categories.add("Swiss High German");
        lang_map.put("Swiss High German", "de_CH");
        lang_categories.add("Syriac");
        lang_map.put("Syriac", "syr");
        lang_categories.add("Tagalog");
        lang_map.put("Tagalog", "tl");
        lang_categories.add("Tahitian");
        lang_map.put("Tahitian", "ty");
        lang_categories.add("Tai Language");
        lang_map.put("Tai Language", "tai");
        lang_categories.add("Tajik");
        lang_map.put("Tajik", "tg");
        lang_categories.add("Tamashek");
        lang_map.put("Tamashek", "tmh");
        lang_categories.add("Tamil");
        lang_map.put("Tamil", "ta");
        lang_categories.add("Taroko");
        lang_map.put("Taroko", "trv");
        lang_categories.add("Tatar");
        lang_map.put("Tatar", "tt");
        lang_categories.add("Telugu");
        lang_map.put("Telugu", "te");
        lang_categories.add("Tereno");
        lang_map.put("Tereno", "ter");
        lang_categories.add("Tetum");
        lang_map.put("Tetum", "tet");
        lang_categories.add("Thai");
        lang_map.put("Thai", "th");
        lang_categories.add("Tibetan");
        lang_map.put("Tibetan", "bo");
        lang_categories.add("Tigre");
        lang_map.put("Tigre", "tig");
        lang_categories.add("Tigrinya");
        lang_map.put("Tigrinya", "ti");
        lang_categories.add("Timne");
        lang_map.put("Timne", "tem");
        lang_categories.add("Tiv");
        lang_map.put("Tiv", "tiv");
        lang_categories.add("Tlingit");
        lang_map.put("Tlingit", "tli");
        lang_categories.add("Tokelau");
        lang_map.put("Tokelau", "tkl");
        lang_categories.add("Tok Pisin");
        lang_map.put("Tok Pisin", "tpi");
        lang_categories.add("Tonga");
        lang_map.put("Tonga", "to");
        lang_categories.add("Traditional Chinese");
        lang_map.put("Traditional Chinese", "zh_Hant");
        lang_categories.add("Tsimshian");
        lang_map.put("Tsimshian", "tsi");
        lang_categories.add("Tsonga");
        lang_map.put("Tsonga", "ts");
        lang_categories.add("Tswana");
        lang_map.put("Tswana", "tn");
        lang_categories.add("Tumbuka");
        lang_map.put("Tumbuka", "tum");
        lang_categories.add("Tupi Language");
        lang_map.put("Tupi Language", "tup");
        lang_categories.add("Turkish");
        lang_map.put("Turkish", "tr");
        lang_categories.add("Turkmen");
        lang_map.put("Turkmen", "tk");
        lang_categories.add("Tuvalu");
        lang_map.put("Tuvalu", "tvl");
        lang_categories.add("Tuvinian");
        lang_map.put("Tuvinian", "tyv");
        lang_categories.add("Twi");
        lang_map.put("Twi", "tw");
        lang_categories.add("Tyap");
        lang_map.put("Tyap", "kcg");
        lang_categories.add("Udmurt");
        lang_map.put("Udmurt", "udm");
        lang_categories.add("Ugaritic");
        lang_map.put("Ugaritic", "uga");
        lang_categories.add("Uighur");
        lang_map.put("Uighur", "ug");
        lang_categories.add("Ukrainian");
        lang_map.put("Ukrainian", "uk");
        lang_categories.add("Umbundu");
        lang_map.put("Umbundu", "umb");
        lang_categories.add("Unknown or Invalid Language");
        lang_map.put("Unknown or Invalid Language", "und");
        lang_categories.add("Upper Sorbian");
        lang_map.put("Upper Sorbian", "hsb");
        lang_categories.add("Urdu");
        lang_map.put("Urdu", "ur");
        lang_categories.add("U.S. English");
        lang_map.put("U.S. English", "en_US");
        lang_categories.add("Uzbek");
        lang_map.put("Uzbek", "uz");
        lang_categories.add("Vai");
        lang_map.put("Vai", "vai");
        lang_categories.add("Venda");
        lang_map.put("Venda", "ve");
        lang_categories.add("Vietnamese");
        lang_map.put("Vietnamese", "vi");
        lang_categories.add("Volapük");
        lang_map.put("Volapük", "vo");
        lang_categories.add("Votic");
        lang_map.put("Votic", "vot");
        lang_categories.add("Wakashan Language");
        lang_map.put("Wakashan Language", "wak");
        lang_categories.add("Walamo");
        lang_map.put("Walamo", "wal");
        lang_categories.add("Walloon");
        lang_map.put("Walloon", "wa");
        lang_categories.add("Waray");
        lang_map.put("Waray", "war");
        lang_categories.add("Washo");
        lang_map.put("Washo", "was");
        lang_categories.add("Welsh");
        lang_map.put("Welsh", "cy");
        lang_categories.add("Western Frisian");
        lang_map.put("Western Frisian", "fy");
        lang_categories.add("Wolof");
        lang_map.put("Wolof", "wo");
        lang_categories.add("Xhosa");
        lang_map.put("Xhosa", "xh");
        lang_categories.add("Yakut");
        lang_map.put("Yakut", "sah");
        lang_categories.add("Yao");
        lang_map.put("Yao", "yao");
        lang_categories.add("Yapese");
        lang_map.put("Yapese", "yap");
        lang_categories.add("Yiddish");
        lang_map.put("Yiddish", "yi");
        lang_categories.add("Yoruba");
        lang_map.put("Yoruba", "yo");
        lang_categories.add("Yupik Language");
        lang_map.put("Yupik Language", "ypk");
        lang_categories.add("Zande");
        lang_map.put("Zande", "znd");
        lang_categories.add("Zapotec");
        lang_map.put("Zapotec", "zap");
        lang_categories.add("Zaza");
        lang_map.put("Zaza", "zza");
        lang_categories.add("Zenaga");
        lang_map.put("Zenaga", "zen");
        lang_categories.add("Zhuang");
        lang_map.put("Zhuang", "za");
        lang_categories.add("Zulu");
        lang_map.put("Zulu", "zu");
        lang_categories.add("Zuni");
        lang_map.put("Zuni", "zun");

        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(Consultation1.this, android.R.layout.simple_spinner_item, lang_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lang.setAdapter(lang_dataAdapter);
        //---------------------------------------------

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

/*
    public void country_code() {



        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Consultation1.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ccode.setAdapter(dataAdapter);
    }
*/


    private class JSON_getting_fees extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Consultation1.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                fees_json = jParser.getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (fees_json.has("token_status")) {
                    String token_status = fees_json.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(Consultation1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    fee_hp = fees_json.getString("0");
                    fee_lp = fees_json.getString("-1");
                    fee_cp = fees_json.getString("1");

                    if (fees_json.has("opt_ftrack")) {
                        String ft_val = fees_json.getString("opt_ftrack");
                        if (ft_val.equals("1")) {
                            ftrack_show = "true";
                        } else {
                            ftrack_show = "false";
                        }
                    } else {
                        ftrack_show = "false";
                    }

                    Model.ftrack_show = ftrack_show;
                    Model.fee_hp = fee_hp;
                    Model.fee_lp = fee_lp;
                    Model.fee_cp = fee_cp;

                    System.out.println("Model.fee_hp-----" + Model.fee_hp);
                    System.out.println("Model.fee_cp-----" + Model.fee_cp);
                    System.out.println("Model.fee_lp-----" + Model.fee_lp);
                    System.out.println("Model.ftrack_show-----" + Model.ftrack_show);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            if ((Model.query_launch).equals("SpecialityListActivity")) {

                System.out.println("Resume Model.query_launch-----" + Model.select_spec_val);
                System.out.println("Resume Model.select_specname-----" + Model.select_specname);

                if ((Model.select_spec_val).equals("0")) {
                    Model.select_specname = "";
                    Model.select_spec_val = "0";
                    tv_spec_name.setText("Select Speciality (optional)");
                    img_remove.setVisibility(View.GONE);
                }
                else{
                    if ((Model.select_specname) != null && !(Model.select_specname).isEmpty() && !(Model.select_specname).equals("null") && !(Model.select_specname).equals("")) {
                        tv_spec_name.setText(Model.select_specname);
                        img_remove.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submit_form() {

        try {
            text_query = edt_issue.getText().toString();
            phone_no = edt_phoneno.getText().toString();

            phone_no = phone_no.replace(" ", "");
            phone_no = phone_no.trim();

            System.out.println("text_query-------------------" + text_query);
            System.out.println("phone_no-------------------" + phone_no);

            Model.query = text_query;
            //Model.sel_spec_code = spec_val;
            spec_val = Model.select_spec_val;

            Model.cons_lang_code = lang_val;
            Model.cons_ccode = selected_cc_value;
            Model.cons_phno = phone_no;
            Model.cons_type = cons_type;

            System.out.println("Model.query----------" + Model.query);
            System.out.println("spec_val----------" + spec_val);
            System.out.println("Model.cons_lang_code----------" + Model.cons_lang_code);
            System.out.println("Model.cons_ccode----------" + Model.cons_ccode);
            System.out.println("Model.cons_phno----------" + Model.cons_phno);
            System.out.println("Model.ftrack_show----------" + Model.ftrack_show);
            System.out.println("cons_type----------" + Model.cons_type);

            System.out.println("edttext.length==" + text_query.length());

            if (text_query.length() > 0) {
                if (phone_no.length() > 0) {

                    ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent intent = new Intent(Consultation1.this, Consultation2.class);
                    intent.putExtra("Query", text_query);
                    intent.putExtra("spec_val", spec_val);
                    intent.putExtra("lang_val", lang_val);
                    intent.putExtra("cccode", selected_cc_value);
                    intent.putExtra("cons_phno", phone_no);
                    intent.putExtra("ftrack_show", ftrack_show);
                    intent.putExtra("cons_type", cons_type);
                    intent.putExtra("fee_hp", fee_hp);
                    intent.putExtra("fee_lp", fee_lp);
                    intent.putExtra("fee_cp", fee_cp);

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.query:", text_query);
                    articleParams.put("android.patient.spec_val:", spec_val);
                    articleParams.put("android.patient.lang_val:", lang_val);
                    articleParams.put("android.patient.cccode:", selected_cc_value);
                    articleParams.put("android.patient.cons_phno:", phone_no);
                    articleParams.put("android.patient.ftrack_show:", ftrack_show);
                    articleParams.put("android.patient.cons_type:", cons_type);
                    articleParams.put("android.patient.fee_hp:", fee_hp);
                    articleParams.put("android.patient.fee_lp:", fee_lp);
                    articleParams.put("android.patient.fee_cp:", fee_cp);
                    FlurryAgent.logEvent("android.patient.Consultation1", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    params.putString("query", text_query);
                    Model.mFirebaseAnalytics.logEvent("Consultation1", params);
                    //------------ Google firebase Analitics--------------------


                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            Consultation1.this.finish();
                        }
                    });
                    //startActivity(intent);
                    startActivityForResult(intent, 1);

                    Model.query_launch = "Consultation1";
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                } else {
                    edt_phoneno.setError("Mobile number is mandatory");
                    Toast.makeText(getApplicationContext(), "Mobile number is mandatory", Toast.LENGTH_LONG).show();
                }
            } else {
                edt_issue.setError("Please enter your health issue.");
                Toast.makeText(getApplicationContext(), "Enter health issue", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* public void dialog_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Consultation1.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select Country code");

        final ArrayAdapter<String> categories = new ArrayAdapter<String>(Consultation1.this, R.layout.dialog_list_textview);

        cc_map = new HashMap<String, String>();

        categories.add("United States (+1), Canada (+1)");
        cc_map.put("United States (+1), Canada (+1)", "1");
        categories.add("United Kingdom (+44)");
        cc_map.put("United Kingdom (+44)", "44");
        categories.add("India (+91)");
        cc_map.put("India (+91)", "91");
        categories.add("Russian Federation (+7), Kazakhstan (+7)");
        cc_map.put("Russian Federation (+7), Kazakhstan (+7)", "7");
        categories.add("Egypt (+20)");
        cc_map.put("Egypt (+20)", "20");
        categories.add("South Africa (+27)");
        cc_map.put("South Africa (+27)", "27");
        categories.add("Greece (+30)");
        cc_map.put("Greece (+30)", "30");
        categories.add("Netherlands (+31)");
        cc_map.put("Netherlands (+31)", "31");
        categories.add("Belgium (+32)");
        cc_map.put("Belgium (+32)", "32");
        categories.add("France (+33)");
        cc_map.put("France (+33)", "33");
        categories.add("Spain (+34)");
        cc_map.put("Spain (+34)", "34");
        categories.add("Hungary (+36)");
        cc_map.put("Hungary (+36)", "36");
        categories.add("Italy (+39)");
        cc_map.put("Italy (+39)", "39");
        categories.add("Romania (+40)");
        cc_map.put("Romania (+40)", "40");
        categories.add("Switzerland (+41)");
        cc_map.put("Switzerland (+41)", "41");
        categories.add("Austria (+43)");
        cc_map.put("Austria (+43)", "43");
        categories.add("Denmark (+45)");
        cc_map.put("Denmark (+45)", "45");
        categories.add("Sweden (+46)");
        cc_map.put("Sweden (+46)", "46");
        categories.add("Norway (+47)");
        cc_map.put("Norway (+47)", "47");
        categories.add("Poland (+48)");
        cc_map.put("Poland (+48)", "48");
        categories.add("Germany (+49)");
        cc_map.put("Germany (+49)", "49");
        categories.add("Peru (+51)");
        cc_map.put("Peru (+51)", "51");
        categories.add("Mexico (+52)");
        cc_map.put("Mexico (+52)", "52");
        categories.add("Cuba (+53)");
        cc_map.put("Cuba (+53)", "53");
        categories.add("Argentina (+54)");
        cc_map.put("Argentina (+54)", "54");
        categories.add("Brazil (+55)");
        cc_map.put("Brazil (+55)", "55");
        categories.add("Chile (+56)");
        cc_map.put("Chile (+56)", "56");
        categories.add("Colombia (+57)");
        cc_map.put("Colombia (+57)", "57");
        categories.add("Venezuela (+58)");
        cc_map.put("Venezuela (+58)", "58");
        categories.add("Malaysia (+60)");
        cc_map.put("Malaysia (+60)", "60");
        categories.add("Australia (+61)");
        cc_map.put("Australia (+61)", "61");
        categories.add("Indonesia (+62)");
        cc_map.put("Indonesia (+62)", "62");
        categories.add("Philippines (+63)");
        cc_map.put("Philippines (+63)", "63");
        categories.add("New Zealand (+64)");
        cc_map.put("New Zealand (+64)", "64");
        categories.add("Singapore (+65)");
        cc_map.put("Singapore (+65)", "65");
        categories.add("Thailand (+66)");
        cc_map.put("Thailand (+66)", "66");
        categories.add("Japan (+81)");
        cc_map.put("Japan (+81)", "81");
        categories.add("Korea, Republic of (+82)");
        cc_map.put("Korea, Republic of (+82)", "82");
        categories.add("Viet Nam (+84)");
        cc_map.put("Viet Nam (+84)", "84");
        categories.add("China (+86)");
        cc_map.put("China (+86)", "86");
        categories.add("Turkey (+90)");
        cc_map.put("Turkey (+90)", "90");
        categories.add("Pakistan (+92)");
        cc_map.put("Pakistan (+92)", "92");
        categories.add("Afghanistan (+93)");
        cc_map.put("Afghanistan (+93)", "93");
        categories.add("Sri Lanka (+94)");
        cc_map.put("Sri Lanka (+94)", "94");
        categories.add("Myanmar (+95)");
        cc_map.put("Myanmar (+95)", "95");
        categories.add("Iran, Islamic Republic of (+98)");
        cc_map.put("Iran, Islamic Republic of (+98)", "98");
        categories.add("South Sudan (+211)");
        cc_map.put("South Sudan (+211)", "211");
        categories.add("Morocco (+212)");
        cc_map.put("Morocco (+212)", "212");
        categories.add("Algeria (+213)");
        cc_map.put("Algeria (+213)", "213");
        categories.add("Tunisia (+216)");
        cc_map.put("Tunisia (+216)", "216");
        categories.add("Libya (+218)");
        cc_map.put("Libya (+218)", "218");
        categories.add("Gambia (+220)");
        cc_map.put("Gambia (+220)", "220");
        categories.add("Senegal (+221)");
        cc_map.put("Senegal (+221)", "221");
        categories.add("Mauritania (+222)");
        cc_map.put("Mauritania (+222)", "222");
        categories.add("Mali (+223)");
        cc_map.put("Mali (+223)", "223");
        categories.add("Guinea (+224)");
        cc_map.put("Guinea (+224)", "224");
        categories.add("Cote d'Ivoire (+225)");
        cc_map.put("Cote d'Ivoire (+225)", "225");
        categories.add("Burkina Faso (+226)");
        cc_map.put("Burkina Faso (+226)", "226");
        categories.add("Niger (+227)");
        cc_map.put("Niger (+227)", "227");
        categories.add("Togo (+228)");
        cc_map.put("Togo (+228)", "228");
        categories.add("Benin (+229)");
        cc_map.put("Benin (+229)", "229");
        categories.add("Mauritius (+230)");
        cc_map.put("Mauritius (+230)", "230");
        categories.add("Liberia (+231)");
        cc_map.put("Liberia (+231)", "231");
        categories.add("Sierra Leone (+232)");
        cc_map.put("Sierra Leone (+232)", "232");
        categories.add("Ghana (+233)");
        cc_map.put("Ghana (+233)", "233");
        categories.add("Nigeria (+234)");
        cc_map.put("Nigeria (+234)", "234");
        categories.add("Chad (+235)");
        cc_map.put("Chad (+235)", "235");
        categories.add("Central African Republic (+236)");
        cc_map.put("Central African Republic (+236)", "236");
        categories.add("Cameroon (+237)");
        cc_map.put("Cameroon (+237)", "237");
        categories.add("Cape Verde (+238)");
        cc_map.put("Cape Verde (+238)", "238");
        categories.add("Sao Tome and Principe (+239)");
        cc_map.put("Sao Tome and Principe (+239)", "239");
        categories.add("Equatorial Guinea (+240)");
        cc_map.put("Equatorial Guinea (+240)", "240");
        categories.add("Gabon (+241)");
        cc_map.put("Gabon (+241)", "241");
        categories.add("Congo (+242)");
        cc_map.put("Congo (+242)", "242");
        categories.add("Democratic Republic of the Congo (+243)");
        cc_map.put("Democratic Republic of the Congo (+243)", "243");
        categories.add("Angola (+244)");
        cc_map.put("Angola (+244)", "244");
        categories.add("GuineaBissau (+245)");
        cc_map.put("GuineaBissau (+245)", "245");
        categories.add("British Indian Ocean Territory (+246)");
        cc_map.put("British Indian Ocean Territory (+246)", "246");
        categories.add("Seychelles (+248)");
        cc_map.put("Seychelles (+248)", "248");
        categories.add("Sudan (+249)");
        cc_map.put("Sudan (+249)", "249");
        categories.add("Rwanda (+250)");
        cc_map.put("Rwanda (+250)", "250");
        categories.add("Ethiopia (+251)");
        cc_map.put("Ethiopia (+251)", "251");
        categories.add("Somalia (+252)");
        cc_map.put("Somalia (+252)", "252");
        categories.add("Djibouti (+253)");
        cc_map.put("Djibouti (+253)", "253");
        categories.add("Kenya (+254)");
        cc_map.put("Kenya (+254)", "254");
        categories.add("United Republic of Tanzania (+255)");
        cc_map.put("United Republic of Tanzania (+255)", "255");
        categories.add("Uganda (+256)");
        cc_map.put("Uganda (+256)", "256");
        categories.add("Burundi (+257)");
        cc_map.put("Burundi (+257)", "257");
        categories.add("Mozambique (+258)");
        cc_map.put("Mozambique (+258)", "258");
        categories.add("Zambia (+260)");
        cc_map.put("Zambia (+260)", "260");
        categories.add("Madagascar (+261)");
        cc_map.put("Madagascar (+261)", "261");
        categories.add("Mayotte (+262)");
        cc_map.put("Mayotte (+262)", "262");
        categories.add("Zimbabwe (+263)");
        cc_map.put("Zimbabwe (+263)", "263");
        categories.add("Namibia (+264)");
        cc_map.put("Namibia (+264)", "264");
        categories.add("Malawi (+265)");
        cc_map.put("Malawi (+265)", "265");
        categories.add("Lesotho (+266)");
        cc_map.put("Lesotho (+266)", "266");
        categories.add("Botswana (+267)");
        cc_map.put("Botswana (+267)", "267");
        categories.add("Swaziland (+268)");
        cc_map.put("Swaziland (+268)", "268");
        categories.add("Comoros (+269)");
        cc_map.put("Comoros (+269)", "269");
        categories.add("Saint Helena (+290)");
        cc_map.put("Saint Helena (+290)", "290");
        categories.add("Eritrea (+291)");
        cc_map.put("Eritrea (+291)", "291");
        categories.add("Aruba (+297)");
        cc_map.put("Aruba (+297)", "297");
        categories.add("Faroe Islands (+298)");
        cc_map.put("Faroe Islands (+298)", "298");
        categories.add("Greenland (+299)");
        cc_map.put("Greenland (+299)", "299");
        categories.add("Gibraltar (+350)");
        cc_map.put("Gibraltar (+350)", "350");
        categories.add("Portugal (+351)");
        cc_map.put("Portugal (+351)", "351");
        categories.add("Luxembourg (+352)");
        cc_map.put("Luxembourg (+352)", "352");
        categories.add("Ireland (+353)");
        cc_map.put("Ireland (+353)", "353");
        categories.add("Iceland (+354)");
        cc_map.put("Iceland (+354)", "354");
        categories.add("Albania (+355)");
        cc_map.put("Albania (+355)", "355");
        categories.add("Malta (+356)");
        cc_map.put("Malta (+356)", "356");
        categories.add("Cyprus (+357)");
        cc_map.put("Cyprus (+357)", "357");
        categories.add("Finland (+358)");
        cc_map.put("Finland (+358)", "358");
        categories.add("Bulgaria (+359)");
        cc_map.put("Bulgaria (+359)", "359");
        categories.add("Lithuania (+370)");
        cc_map.put("Lithuania (+370)", "370");
        categories.add("Latvia (+371)");
        cc_map.put("Latvia (+371)", "371");
        categories.add("Estonia (+372)");
        cc_map.put("Estonia (+372)", "372");
        categories.add("Moldova, Republic of (+373)");
        cc_map.put("Moldova, Republic of (+373)", "373");
        categories.add("Armenia (+374)");
        cc_map.put("Armenia (+374)", "374");
        categories.add("Belarus (+375)");
        cc_map.put("Belarus (+375)", "375");
        categories.add("Andorra (+376)");
        cc_map.put("Andorra (+376)", "376");
        categories.add("Monaco (+377)");
        cc_map.put("Monaco (+377)", "377");
        categories.add("San Marino (+378)");
        cc_map.put("San Marino (+378)", "378");
        categories.add("Holy See (Vatican City State) (+379)");
        cc_map.put("Holy See (Vatican City State) (+379)", "379");
        categories.add("Ukraine (+380)");
        cc_map.put("Ukraine (+380)", "380");
        categories.add("Serbia (+381)");
        cc_map.put("Serbia (+381)", "381");
        categories.add("Montenegro (+382)");
        cc_map.put("Montenegro (+382)", "382");
        categories.add("Croatia (+385)");
        cc_map.put("Croatia (+385)", "385");
        categories.add("Slovenia (+386)");
        cc_map.put("Slovenia (+386)", "386");
        categories.add("Bosnia and Herzegovina (+387)");
        cc_map.put("Bosnia and Herzegovina (+387)", "387");
        categories.add("Macedonia, the Former Yugoslav Republic of (+389)");
        cc_map.put("Macedonia, the Former Yugoslav Republic of (+389)", "389");
        categories.add("Czech Republic (+420)");
        cc_map.put("Czech Republic (+420)", "420");
        categories.add("Slovakia (+421)");
        cc_map.put("Slovakia (+421)", "421");
        categories.add("Liechtenstein (+423)");
        cc_map.put("Liechtenstein (+423)", "423");
        categories.add("Falkland Islands (Malvinas) (+500)");
        cc_map.put("Falkland Islands (Malvinas) (+500)", "500");
        categories.add("Belize (+501)");
        cc_map.put("Belize (+501)", "501");
        categories.add("Guatemala (+502)");
        cc_map.put("Guatemala (+502)", "502");
        categories.add("El Salvador (+503)");
        cc_map.put("El Salvador (+503)", "503");
        categories.add("Honduras (+504)");
        cc_map.put("Honduras (+504)", "504");
        categories.add("Nicaragua (+505)");
        cc_map.put("Nicaragua (+505)", "505");
        categories.add("Costa Rica (+506)");
        cc_map.put("Costa Rica (+506)", "506");
        categories.add("Panama (+507)");
        cc_map.put("Panama (+507)", "507");
        categories.add("Saint Pierre and Miquelon (+508)");
        cc_map.put("Saint Pierre and Miquelon (+508)", "508");
        categories.add("Haiti (+509)");
        cc_map.put("Haiti (+509)", "509");
        categories.add("Saint Barthelemy (+590)");
        cc_map.put("Saint Barthelemy (+590)", "590");
        categories.add("Bolivia (+591)");
        cc_map.put("Bolivia (+591)", "591");
        categories.add("Guyana (+592)");
        cc_map.put("Guyana (+592)", "592");
        categories.add("Ecuador (+593)");
        cc_map.put("Ecuador (+593)", "593");
        categories.add("French Guiana (+594)");
        cc_map.put("French Guiana (+594)", "594");
        categories.add("Paraguay (+595)");
        cc_map.put("Paraguay (+595)", "595");
        categories.add("Martinique (+596)");
        cc_map.put("Martinique (+596)", "596");
        categories.add("Suriname (+597)");
        cc_map.put("Suriname (+597)", "597");
        categories.add("Uruguay (+598)");
        cc_map.put("Uruguay (+598)", "598");
        categories.add("Bonaire (+599), Curacao (+599)");
        cc_map.put("Bonaire (+599), Curacao (+599)", "599");
        categories.add("TimorLeste (+670)");
        cc_map.put("TimorLeste (+670)", "670");
        categories.add("Antarctica (+672)");
        cc_map.put("Antarctica (+672)", "672");
        categories.add("Brunei Darussalam (+673)");
        cc_map.put("Brunei Darussalam (+673)", "673");
        categories.add("Nauru (+674)");
        cc_map.put("Nauru (+674)", "674");
        categories.add("Papua New Guinea (+675)");
        cc_map.put("Papua New Guinea (+675)", "675");
        categories.add("Tonga (+676)");
        cc_map.put("Tonga (+676)", "676");
        categories.add("Solomon Islands (+677)");
        cc_map.put("Solomon Islands (+677)", "677");
        categories.add("Vanuatu (+678)");
        cc_map.put("Vanuatu (+678)", "678");
        categories.add("Fiji (+679)");
        cc_map.put("Fiji (+679)", "679");
        categories.add("Palau (+680)");
        cc_map.put("Palau (+680)", "680");
        categories.add("Wallis and Futuna (+681)");
        cc_map.put("Wallis and Futuna (+681)", "681");
        categories.add("Cook Islands (+682)");
        cc_map.put("Cook Islands (+682)", "682");
        categories.add("Niue (+683)");
        cc_map.put("Niue (+683)", "683");
        categories.add("Samoa (+685)");
        cc_map.put("Samoa (+685)", "685");
        categories.add("Kiribati (+686)");
        cc_map.put("Kiribati (+686)", "686");
        categories.add("New Caledonia (+687)");
        cc_map.put("New Caledonia (+687)", "687");
        categories.add("Tuvalu (+688)");
        cc_map.put("Tuvalu (+688)", "688");
        categories.add("French Polynesia (+689)");
        cc_map.put("French Polynesia (+689)", "689");
        categories.add("Tokelau (+690)");
        cc_map.put("Tokelau (+690)", "690");
        categories.add("Micronesia, Federated States of (+691)");
        cc_map.put("Micronesia, Federated States of (+691)", "691");
        categories.add("Marshall Islands (+692)");
        cc_map.put("Marshall Islands (+692)", "692");
        categories.add("Korea, Democratic People's Republic of (+850)");
        cc_map.put("Korea, Democratic People's Republic of (+850)", "850");
        categories.add("Hong Kong (+852)");
        cc_map.put("Hong Kong (+852)", "852");
        categories.add("Macao (+853)");
        cc_map.put("Macao (+853)", "853");
        categories.add("Cambodia (+855)");
        cc_map.put("Cambodia (+855)", "855");
        categories.add("Lao People's Democratic Republic (+856)");
        cc_map.put("Lao People's Democratic Republic (+856)", "856");
        categories.add("Pitcairn (+870)");
        cc_map.put("Pitcairn (+870)", "870");
        categories.add("Bangladesh (+880)");
        cc_map.put("Bangladesh (+880)", "880");
        categories.add("Taiwan, Province of China (+886)");
        cc_map.put("Taiwan, Province of China (+886)", "886");
        categories.add("Maldives (+960)");
        cc_map.put("Maldives (+960)", "960");
        categories.add("Lebanon (+961)");
        cc_map.put("Lebanon (+961)", "961");
        categories.add("Jordan (+962)");
        cc_map.put("Jordan (+962)", "962");
        categories.add("Syrian Arab Republic (+963)");
        cc_map.put("Syrian Arab Republic (+963)", "963");
        categories.add("Iraq (+964)");
        cc_map.put("Iraq (+964)", "964");
        categories.add("Kuwait (+965)");
        cc_map.put("Kuwait (+965)", "965");
        categories.add("Saudi Arabia (+966)");
        cc_map.put("Saudi Arabia (+966)", "966");
        categories.add("Yemen (+967)");
        cc_map.put("Yemen (+967)", "967");
        categories.add("Oman (+968)");
        cc_map.put("Oman (+968)", "968");
        categories.add("Palestine, State of (+970)");
        cc_map.put("Palestine, State of (+970)", "970");
        categories.add("United Arab Emirates (+971)");
        cc_map.put("United Arab Emirates (+971)", "971");
        categories.add("Israel (+972)");
        cc_map.put("Israel (+972)", "972");
        categories.add("Bahrain (+973)");
        cc_map.put("Bahrain (+973)", "973");
        categories.add("Qatar (+974)");
        cc_map.put("Qatar (+974)", "974");
        categories.add("Bhutan (+975)");
        cc_map.put("Bhutan (+975)", "975");
        categories.add("Mongolia (+976)");
        cc_map.put("Mongolia (+976)", "976");
        categories.add("Nepal (+977)");
        cc_map.put("Nepal (+977)", "977");
        categories.add("Tajikistan (+992)");
        cc_map.put("Tajikistan (+992)", "992");
        categories.add("Turkmenistan (+993)");
        cc_map.put("Turkmenistan (+993)", "993");
        categories.add("Azerbaijan (+994)");
        cc_map.put("Azerbaijan (+994)", "994");
        categories.add("Georgia (+995)");
        cc_map.put("Georgia (+995)", "995");
        categories.add("Kyrgyzstan (+996)");
        cc_map.put("Kyrgyzstan (+996)", "996");
        categories.add("Uzbekistan (+998)");
        cc_map.put("Uzbekistan (+998)", "998");
        categories.add("Bahamas (+1242)");
        cc_map.put("Bahamas (+1242)", "1242");
        categories.add("Barbados (+1246)");
        cc_map.put("Barbados (+1246)", "1246");
        categories.add("Anguilla (+1264)");
        cc_map.put("Anguilla (+1264)", "1264");
        categories.add("Antigua and Barbuda (+1268)");
        cc_map.put("Antigua and Barbuda (+1268)", "1268");
        categories.add("British Virgin Islands (+1284)");
        cc_map.put("British Virgin Islands (+1284)", "1284");
        categories.add("US Virgin Islands (+1340)");
        cc_map.put("US Virgin Islands (+1340)", "1340");
        categories.add("Cayman Islands (+1345)");
        cc_map.put("Cayman Islands (+1345)", "1345");
        categories.add("Bermuda (+1441)");
        cc_map.put("Bermuda (+1441)", "1441");
        categories.add("Grenada (+1473)");
        cc_map.put("Grenada (+1473)", "1473");
        categories.add("Turks and Caicos Islands (+1649)");
        cc_map.put("Turks and Caicos Islands (+1649)", "1649");
        categories.add("Montserrat (+1664)");
        cc_map.put("Montserrat (+1664)", "1664");
        categories.add("Northern Mariana Islands (+1670)");
        cc_map.put("Northern Mariana Islands (+1670)", "1670");
        categories.add("Guam (+1671)");
        cc_map.put("Guam (+1671)", "1671");
        categories.add("American Samoa (+1684)");
        cc_map.put("American Samoa (+1684)", "1684");
        categories.add("Sint Maarten (Dutch part) (+1721)");
        cc_map.put("Sint Maarten (Dutch part) (+1721)", "1721");
        categories.add("Saint Lucia (+1758)");
        cc_map.put("Saint Lucia (+1758)", "1758");
        categories.add("Dominica (+1767)");
        cc_map.put("Dominica (+1767)", "1767");
        categories.add("Saint Vincent and the Grenadines (+1784)");
        cc_map.put("Saint Vincent and the Grenadines (+1784)", "1784");
        categories.add("Dominican Republic (+1809)");
        cc_map.put("Dominican Republic (+1809)", "1809");
        categories.add("Trinidad and Tobago (+1868)");
        cc_map.put("Trinidad and Tobago (+1868)", "1868");
        categories.add("Saint Kitts and Nevis (+1869)");
        cc_map.put("Saint Kitts and Nevis (+1869)", "1869");
        categories.add("Jamaica (+1876)");
        cc_map.put("Jamaica (+1876)", "1876");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = categories.getItem(which);
                String select_value = (cc_map).get(categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                selected_cc_value = select_value;
                selected_cc_text = select_text;

                tv_ccode.setText("+" + selected_cc_value);
                Model.cons_ccode = selected_cc_value;
            }
        });
        builderSingle.show();
    }*/


}

