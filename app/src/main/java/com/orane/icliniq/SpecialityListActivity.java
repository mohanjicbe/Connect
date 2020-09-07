package com.orane.icliniq;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialityListActivity extends AppCompatActivity {

    ArrayAdapter<String> dataAdapter = null;
    Map<String, String> spec_map = new HashMap<String, String>();
    public String params, spec_val, select_Speciality;
    Toolbar toolbar;
    LinearLayout icon_layout, search_box, b1_layout, b2_layout, b3_layout, b4_layout, b5_layout, b6_layout, b7_layout, b8_layout, b9_layout;
    Button btn_viewall;
    TextView toolbar_menu;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speciality_search);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_menu = (TextView) toolbar.findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);

        //----------- Flurry -------------------------------------------------
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("User_ID", Model.id);
        FlurryAgent.logEvent("android.patient.Speciality", properties);
        //----------- Flurry -------------------------------------------------

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        btn_viewall = (Button) findViewById(R.id.btn_viewall);
        icon_layout = (LinearLayout) findViewById(R.id.icon_layout);
        search_box = (LinearLayout) findViewById(R.id.search_box);
        listView = (ListView) findViewById(R.id.listView1);

        b1_layout = (LinearLayout) findViewById(R.id.b1_layout);
        b2_layout = (LinearLayout) findViewById(R.id.b2_layout);
        b3_layout = (LinearLayout) findViewById(R.id.b3_layout);
        b4_layout = (LinearLayout) findViewById(R.id.b4_layout);
        b5_layout = (LinearLayout) findViewById(R.id.b5_layout);
        b6_layout = (LinearLayout) findViewById(R.id.b6_layout);
        b7_layout = (LinearLayout) findViewById(R.id.b7_layout);
        b8_layout = (LinearLayout) findViewById(R.id.b8_layout);
        b9_layout = (LinearLayout) findViewById(R.id.b9_layout);


        if ((Model.query_launch).equals("ask_query")) {
            toolbar_menu.setVisibility(View.GONE);
        } else {
            toolbar_menu.setVisibility(View.VISIBLE);
        }


        toolbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spec_val = "0";
                Model.select_spec_val = "0";
                Model.query_launch = "SpecialityListActivity";
                finish();
            }
        });

        btn_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon_layout.setVisibility(View.GONE);
                search_box.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        displayListView();

    }

    public void onlayoutclick(View v) {

        try {

            switch (v.getId()) {

                case R.id.b1_layout:
                    Model.select_spec_val = "193";
                    Model.select_specname = "Gynecology";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;
                case R.id.b2_layout:
                    Model.select_spec_val = "185";
                    Model.select_specname = "Dermatology";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;
                case R.id.b3_layout:
                    Model.select_spec_val = "233";
                    Model.select_specname = "Internal Medicine";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;
                case R.id.b4_layout:
                    Model.select_spec_val = "248";
                    Model.select_specname = "Sexology";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;
                case R.id.b5_layout:
                    Model.select_spec_val = "244";
                    Model.select_specname = "H.I.V";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;
                case R.id.b6_layout:
                    Model.select_spec_val = "6";
                    Model.select_specname = "Dentistry";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;
                case R.id.b7_layout:
                    Model.select_spec_val = "211";
                    Model.select_specname = "Psychology";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;
                case R.id.b8_layout:
                    Model.select_spec_val = "205";
                    Model.select_specname = "Pediatrics";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;
                case R.id.b9_layout:
                    Model.select_spec_val = "4";
                    Model.select_specname = "Cardiology";
                    System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));
                    Model.query_launch = "SpecialityListActivity";
                    finish();
                    break;

            }

        } catch (Exception e) {
            System.out.println("Exception-------" + e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.spec_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Model.query_launch = "";
            finish();
            return true;
        }

        /*if (id == R.id.nav_cfilter) {
            spec_val = "0";
            Model.select_spec_val = "0";
            Model.query_launch = "SpecialityListActivity";
            finish();

            return true;
        }*/


        return true;
    }


    private void displayListView() {

        //Array list of countries
        List<String> categories = new ArrayList<String>();

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

        //create an ArrayAdaptar from the String Array
        dataAdapter = new ArrayAdapter<String>(this, R.layout.speciality_list_row, categories);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                TextView tvspecname = (TextView) view.findViewById(R.id.tvspecname);
                //Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                select_Speciality = tvspecname.getText().toString();
                Model.select_specname = tvspecname.getText().toString();

                spec_val = spec_map.get(select_Speciality);
                Model.select_spec_val = spec_map.get(select_Speciality);

                System.out.println("Model.select_specname--------------" + (Model.select_specname));
                System.out.println("Model.select_spec_val--------------" + (Model.select_spec_val));

                Model.query_launch = "SpecialityListActivity";
                finish();

            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Model.query_launch = "";
        finish();
    }
}
