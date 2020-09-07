package com.orane.icliniq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hbb20.CountryCodePicker;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class Doc_Consultation1 extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private TextView timeTextView;
    private TextView dateTextView;
    private CheckBox mode24Hours;
    private CheckBox modeDarkTime;
    private CheckBox modeDarkDate;
    private CheckBox modeCustomAccentTime;
    private CheckBox modeCustomAccentDate;
    private CheckBox vibrateTime;
    private CheckBox vibrateDate;
    private CheckBox dismissTime;
    private CheckBox dismissDate;
    private CheckBox titleTime;
    private CheckBox titleDate;
    private CheckBox showYearFirst;
    private CheckBox enableSeconds;
    private CheckBox limitTimes;
    private CheckBox limitDates;
    private CheckBox highlightDates;

    private int i = 1;
    private final static String CLICK = "click";
    private final static String NEXT_DATA = "next";
    Button btn_date, btn_submit;
    Spinner spinner_lang, spinner_timerange, spinner_ccode, spinner_timezone;
    Map<String, String> timerange_map = new HashMap<String, String>();
    public String sel_country_code, sel_country_name, spintz, lang_name, Doc_id, cc_name, cons_select_date, fee_hp, fee_lp, cons_phone, cons_type_text, cons_type, ftrack_show, consult_id, cons_phno, Query, spec_val, lang_val, cccode, sel_timerange_code, sel_timerange, sel_timezone, content_str, timezone_str, times_values;
    JSONObject cons_booking_jsonobj, post_json, jsonobj, jsonobj1c;
    TextView tv_changetimezone, tv_tz_present, tv_fee_hp, tv_fee_lp;
    public static Activity fa;
    Map<String, String> cc_map = new HashMap<String, String>();
    EditText edt_phoneno;
    Map<String, String> lang_map = new HashMap<String, String>();
    String timerange_name;
    CountryCodePicker countryCodePicker;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_consultation1);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.upload_files = "";

        FlurryAgent.onPageView();

        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        spinner_lang = (Spinner) findViewById(R.id.spinner_lang);
        edt_phoneno = (EditText) findViewById(R.id.edt_phoneno);
        spinner_ccode = (Spinner) findViewById(R.id.spinner_ccode);

/*        modeDarkTime.setChecked(Utils.isDarkTheme(this, modeDarkTime.isChecked()));
        modeDarkDate.setChecked(Utils.isDarkTheme(this, modeDarkDate.isChecked()));*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Schedule the consultation");
        }
        //------------ Object Creations ------------------------------
        tv_tz_present = (TextView) findViewById(R.id.tv_tz_present);
        tv_changetimezone = (TextView) findViewById(R.id.tv_changetimezone);
        spinner_timezone = (Spinner) findViewById(R.id.spinner_timezone);
        spinner_timerange = (Spinner) findViewById(R.id.spinner_timerange);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        //Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_ph_tit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_tz_present)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_schedule)).setTypeface(font_bold);
        ((Button) findViewById(R.id.btn_date)).setTypeface(font_bold);
        ((Button) findViewById(R.id.btn_submit)).setTypeface(font_bold);


        countryCodePicker.registerCarrierNumberEditText(edt_phoneno);

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                System.out.println("getSelectedCountryCode------------" + countryCodePicker.getSelectedCountryCode());
                System.out.println("getSelectedCountryCodeAsInt------------" + countryCodePicker.getSelectedCountryCodeAsInt());
                System.out.println("getSelectedCountryName------------" + countryCodePicker.getSelectedCountryName());
                System.out.println("getSelectedCountryNameCode------------" + countryCodePicker.getSelectedCountryNameCode());

                cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
            }
        });

        countryCodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        try {
            cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
            System.out.println("Start cccode-----" + cccode);

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            //------ getting Values ---------------------------
            Intent intent = getIntent();
            Query = intent.getStringExtra("Query");
            cons_type = intent.getStringExtra("cons_type");
            Doc_id = intent.getStringExtra("Doc_id");
            //------ getting Values ---------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }


        //------- Setting Language ----------------------
        final List<String> lang_categories = new ArrayList<String>();

        lang_categories.add("English");
        lang_map.put("English", "en");
        lang_categories.add("Hindi");
        lang_map.put("Hindi", "hi");
        lang_categories.add("Arabic");
        lang_map.put("Arabic", "ar");

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

        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, lang_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lang.setAdapter(lang_dataAdapter);
        //---------------------------------------------


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons_phno = edt_phoneno.getText().toString();

                if (!cons_phno.equals("")) {

                    if (new NetCheck().netcheck(Doc_Consultation1.this)) {
                        post_consultation();
                    } else {
                        Toast.makeText(Doc_Consultation1.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your callback number ", Toast.LENGTH_LONG).show();
                }

            }
        });

        try {
            //------------------------------------------------------------
            String url = Model.BASE_URL + "/sapp/getTimeRange?format=json_new&token=" + Model.token + "&enc=1";
            System.out.println("url-------------" + url);
            new JSON_Time_Range().execute(url);
            //---------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        //------------------------------------------------------------------------------------
        spinner_ccode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) spinner_ccode.getSelectedView();
                cc_name = textView.getText().toString();
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

        //------------------------------------------------------------------------------------
        spinner_timezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) spinner_timezone.getSelectedView();
                spintz = textView.getText().toString();

                if (spintz.equals("Choose another timezone")) {
                    System.out.println("spintz---------" + spintz);
                    Intent intent = new Intent(Doc_Consultation1.this, TimeZoneActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------
        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) spinner_lang.getSelectedView();
                lang_name = textView.getText().toString();
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

        //------------------------------------------------------------------------------------
        spinner_timerange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {

                    TextView textView = (TextView) spinner_timerange.getSelectedView();
                    sel_timerange = textView.getText().toString();
                    sel_timerange_code = timerange_map.get(sel_timerange);
                    Model.sel_timerange_code = timerange_map.get(sel_timerange);

                    Model.time_range = sel_timerange;
                    System.out.println("Model.time_range------" + Model.time_range);
                    System.out.println("Sel_timerange_code------" + Model.sel_timerange_code);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------------------------------------------------------------------------
        tv_changetimezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Doc_Consultation1.this, TimeZoneActivity.class);
                startActivity(intent);
            }
        });
        //----------------------------------------------------------------------------------

        //-----------------------------------------------------------------
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            Doc_Consultation1.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setThemeDark(false);
                    dpd.vibrate(true);
                    dpd.dismissOnPause(false);
                    dpd.showYearPickerFirst(false);
                    dpd.setAccentColor(Color.parseColor("#9C27B0"));
                    dpd.setTitle("DatePicker Title");
                    //--------------------------------------

                    //---------Limit Dates-----------------------------
                    Calendar[] lim_dates = new Calendar[300];
                    for (int i = 1; i <= 300; i++) {
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.DATE, i);
                        lim_dates[i - 1] = date;
                    }
                    dpd.setSelectableDays(lim_dates);
                    //---------Limit Dates-----------------------------

                    //--------------------------------------
                    Calendar[] dates = new Calendar[13];
                    for (int i = -6; i <= 6; i++) {
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.WEEK_OF_YEAR, i);
                        dates[i + 6] = date;
                    }
                    dpd.setHighlightedDays(dates);
                    //--------------------------------------

                    dpd.show(getFragmentManager(), "Datepickerdialog");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (tpd != null) tpd.setOnTimeSetListener(this);
        if (dpd != null) dpd.setOnDateSetListener(this);

        //--------------------------------------------------
        if ((Model.cons_timezone_val) != null && !(Model.cons_timezone_val).isEmpty() && !(Model.cons_timezone_val).equals("null") && !(Model.cons_timezone_val).equals("")) {
            try {
                //----------------------------
                String url = Model.BASE_URL + "/sapp/getTimeRange?format=json_new&tz_name=" + (Model.cons_timezone_val) + "&token=" + Model.token + "&enc=1";
                System.out.println("getTimerange url--------" + url);
                new JSON_Time_Range().execute(url);
                //----------------------------
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //--------------------------------------------------
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        try {
            String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
            String minuteString = minute < 10 ? "0" + minute : "" + minute;
            String secondString = second < 10 ? "0" + second : "" + second;
            String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
            timeTextView.setText(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        try {
            //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            cons_select_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            System.out.println("Cal Date------" + cons_select_date);

            //--------- for System -------------------
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
            Date dateObj = curFormater.parse(cons_select_date);
            String newDateStr = curFormater.format(dateObj);
            System.out.println("For System  select_date---------" + newDateStr);
            //--------------------------------
            btn_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            System.out.println("cons_select_date---------" + cons_select_date);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Doc_Consultation1.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                cons_booking_jsonobj = jParser.JSON_POST(urls[0], "Submit_consultation");

                System.out.println("cons_booking_jsonobj---------------" + cons_booking_jsonobj.toString());
                System.out.println("Cons_Booking_params--------------" + urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                if (cons_booking_jsonobj.has("token_status")) {
                    String token_status = cons_booking_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(Doc_Consultation1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    consult_id = cons_booking_jsonobj.getString("id");
                    Model.consult_id = consult_id;
                    Model.qid = consult_id;

                    System.out.println("consult_id-----------" + consult_id);

                    Model.query_launch = "Consultation2";

                    Intent intent = new Intent(Doc_Consultation1.this, Consultation3.class);
                    intent.putExtra("consult_id", consult_id);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            Doc_Consultation1.this.finish();
                        }
                    });
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class JSON_Time_Range extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Doc_Consultation1.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj = jParser.getJSONFromUrl(urls[0]);
                System.out.println("Time_Range jsonobj-----" + jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            final List<String> categories_tr = new ArrayList<String>();

            try {

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(Doc_Consultation1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    content_str = jsonobj.getString("content");
                    timezone_str = jsonobj.getString("timezone");

                    Model.cons_timezone = timezone_str;

                    System.out.println("content_str--------" + content_str);
                    System.out.println("timezone_str--------" + timezone_str);
                    System.out.println("Model.cons_timezone--------" + Model.cons_timezone);

                    JSONObject content_obj = new JSONObject(content_str);
                    String fone = content_obj.getString("1");
                    System.out.println("fone--------" + fone);

                    JSONArray jsonarray = new JSONArray();
                    jsonarray.put(content_obj);


                    categories_tr.add("Select Time Range");
                    timerange_map.put(times_values, "0");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobj1c = jsonarray.getJSONObject(i);
                        System.out.println("jsonobj1c-----" + jsonobj1c.toString());

                        for (int j = 1; j <= 24; j++) {

                            if (jsonobj1c.has("" + j)) {
                                times_values = jsonobj1c.getString("" + j);
                                //======================================================================
                                categories_tr.add(times_values);
                                timerange_map.put(times_values, "" + j);
                                System.out.println("times_values-----" + times_values);
                                System.out.println("times_values---j---" + j);
                                //=========================================================
                            }
                        }

                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories_tr);
                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_timerange.setAdapter(dataAdapter2);
                    }


                    //----------- Time Zone ------------------------
                    final List<String> categories2 = new ArrayList<String>();
                    categories2.add(timezone_str);
                    categories2.add("Choose another timezone");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories2);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_timezone.setAdapter(dataAdapter2);
                    //----------- Time Zone ------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
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


    public void post_consultation() {

        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

            System.out.println("Model.id-----" + Model.id);
            System.out.println("Model.const_type-----" + cons_type);
            System.out.println("Model.cons_query-----" + Query);
            System.out.println("Model.time_range-----" + sel_timerange_code);
            System.out.println("Model.cons_select_date-----" + cons_select_date);
            System.out.println("Model.cons_lang-----" + lang_val);
            System.out.println("Model.cons_ccode-----" + sel_country_code);
            System.out.println("Model.cons_number-----" + cons_phno);
            System.out.println("Model.cons_timezone-----" + Model.cons_timezone);
            System.out.println("Doc_id-----" + Doc_id);

            try {
                cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
                System.out.println("Start cccode-----" + cccode);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                post_json = new JSONObject();
                post_json.put("user_id", (Model.id));
                post_json.put("doctor_id", Doc_id);
                post_json.put("consult_type", cons_type);
                post_json.put("query", Query);
                post_json.put("time_range", sel_timerange_code);
                post_json.put("consult_date", cons_select_date);
                post_json.put("lang", lang_val);
                post_json.put("ccode", cccode);
                post_json.put("number", cons_phno);
                post_json.put("timezone", (Model.cons_timezone));
                post_json.put("speciality", "0");

                System.out.println("post_json-------------" + post_json.toString());

                String date_text = btn_date.getText().toString();

                TextView textView = (TextView) spinner_timerange.getSelectedView();
                timerange_name = textView.getText().toString();

                //------------------------------------------------
                Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                Model.kiss.record("android.patient.Direct_Consultation_Post");
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("android.patient.User_ID", Model.id);
                properties.put("android.patient.consult_type", cons_type);
                properties.put("android.patient.Query", Query);
                properties.put("android.patient.time_range", cons_type);
                properties.put("android.patient.Time_Range", sel_timerange_code);
                properties.put("android.patient.Consult_date", cons_select_date);
                properties.put("android.patient.Country_code", cccode);
                properties.put("android.patient.Ph_number", cons_phno);
                properties.put("android.patient.timezone", (Model.cons_timezone));
                properties.put("android.patient.speciality", "0");
                Model.kiss.set(properties);
                //-----------------------------------------------------------
                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.patient.consult_type", cons_type);
                articleParams.put("android.patient.Query", Query);
                articleParams.put("android.patient.time_range", cons_type);
                articleParams.put("android.patient.Time_Range", sel_timerange_code);
                articleParams.put("android.patient.Consult_date", cons_select_date);
                articleParams.put("android.patient.Country_code", cccode);
                articleParams.put("android.patient.Ph_number", cons_phno);
                articleParams.put("android.patient.timezone", (Model.cons_timezone));
                articleParams.put("android.patient.speciality", "0");
                FlurryAgent.logEvent("android.patient.Direct_Consultation_Post", articleParams);
                //----------- Flurry -------------------------------------------------

                //------------ Google firebase Analitics-----------------------------------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                params.putString("consult_type", cons_type);
                params.putString("Consult_date", cons_select_date);
                Model.mFirebaseAnalytics.logEvent("Direct_Consultation_Post", params);
                //------------ Google firebase Analitics-----------------------------------------------

                if (!date_text.equals("Select Date")) {
                    if (!timerange_name.equals("Select Time Range")) {
                        new JSONPostQuery().execute(post_json);
                    } else
                        Toast.makeText(getApplicationContext(), "Select Time Range for consultation", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Select Consultation Date", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            force_logout();
        }
    }

    public void force_logout() {

        try {
            final MaterialDialog alert = new MaterialDialog(Doc_Consultation1.this);
            alert.setTitle("Oops..!");
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
                    Intent i = new Intent(Doc_Consultation1.this, LoginActivity.class);
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

}
