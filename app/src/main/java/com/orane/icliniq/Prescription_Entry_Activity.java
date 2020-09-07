package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Prescription_Entry_Activity extends AppCompatActivity {

    Button btn_submit;
    EditText edt_feedback;
    public String when_to_take, drug_type_name, drug_name, dose_text, qty_val, how_to_take, days_val, add_type, prescription_id, cur_qid, whentotake_name, whentotake_val, howtotake_name, howtotake_val, pat_id, drug_type_val, report_response;
    JSONObject json_feedback, json_response_obj;
    Spinner spinner_dtype, spinner_how, spinner_when;
    Button btn_entry_submit;
    EditText edt_drug_name, edt_drug_days, edt_drug_qty, edt_dosage;
    RelativeLayout drugtype_layout, howtotake_layout, when_layout;
    TextView tv_drug_type_name, tv_howtotake_name, tv_whentotake_name;

    Map<String, String> drug_type_map = new HashMap<String, String>();
    Map<String, String> howtotake_map = new HashMap<String, String>();
    Map<String, String> whentotake_map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_entry);


        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        spinner_dtype = (Spinner) findViewById(R.id.spinner_dtype);
        spinner_how = (Spinner) findViewById(R.id.spinner_how);
        spinner_when = (Spinner) findViewById(R.id.spinner_when);

        btn_entry_submit = (Button) findViewById(R.id.btn_entry_submit);
        tv_drug_type_name = (TextView) findViewById(R.id.tv_drug_type_name);
        tv_howtotake_name = (TextView) findViewById(R.id.tv_howtotake_name);

        edt_drug_name = (EditText) findViewById(R.id.edt_drug_name);
        edt_dosage = (EditText) findViewById(R.id.edt_dosage);
        edt_drug_days = (EditText) findViewById(R.id.edt_drug_days);
        edt_drug_qty = (EditText) findViewById(R.id.edt_drug_qty);
        drugtype_layout = (RelativeLayout) findViewById(R.id.drugtype_layout);
        howtotake_layout = (RelativeLayout) findViewById(R.id.howtotake_layout);
        when_layout = (RelativeLayout) findViewById(R.id.when_layout);
        tv_whentotake_name = (TextView) findViewById(R.id.tv_whentotake_name);

        apply_map();

        try {
            Intent intent = getIntent();
            add_type = intent.getStringExtra("add_type");
            pat_id = intent.getStringExtra("pat_id");
            cur_qid = intent.getStringExtra("cur_qid");
            prescription_id = intent.getStringExtra("prescription_id");

            System.out.println("pat_id-------------- " + pat_id);

            if (add_type.equals("update")) {

                drug_name = intent.getStringExtra("drug_name");
                dose_text = intent.getStringExtra("dose_text");
                qty_val = intent.getStringExtra("qty_val");
                how_to_take = intent.getStringExtra("how_to_take");
                days_val = intent.getStringExtra("days_val");
                drug_type_name = intent.getStringExtra("drug_type");
                when_to_take = intent.getStringExtra("when_to_take");

                System.out.println("Get drug_name---" + drug_name);
                System.out.println("Get dose_text---" + dose_text);
                System.out.println("Get qty_val---" + qty_val);
                System.out.println("Get how_to_take---" + how_to_take);
                System.out.println("Get days_val---" + days_val);
                System.out.println("Get drug_type_name---" + drug_type_name);
                System.out.println("Get when_to_take---" + when_to_take);

                whentotake_val = whentotake_map.get(when_to_take);
                howtotake_val = howtotake_map.get(how_to_take);
                drug_type_val = drug_type_map.get(drug_type_name);

                System.out.println("whentotake_val-------" + whentotake_val);

                tv_drug_type_name.setText(drug_type_name);
                edt_drug_name.setText(drug_name);
                edt_dosage.setText(dose_text);
                edt_drug_qty.setText(qty_val);
                tv_howtotake_name.setText(how_to_take);
                edt_drug_days.setText(days_val);
                tv_whentotake_name.setText(when_to_take);
            }

            System.out.println("add_type-----" + add_type);
            System.out.println("Get pat_id---" + pat_id);
            System.out.println("Get cur_qid---" + cur_qid);
            System.out.println("Get prescription_id---" + prescription_id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        drugtype_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ccode();
            }
        });

        howtotake_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howtotake_layout_ccode();
            }
        });

        when_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whentotake_layout_ccode();
            }
        });


        //------------------clinics------------------------------------------------------------------
        spinner_dtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                drug_type_name = spinner_dtype.getSelectedItem().toString();
                drug_type_val = drug_type_map.get(drug_type_name);

                System.out.println("drug_type_name------" + drug_type_name);
                System.out.println("drug_type_val------" + drug_type_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------clinics------------------------------------------------------------------

        //------------------clinics------------------------------------------------------------------
        spinner_how.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                howtotake_name = spinner_how.getSelectedItem().toString();
                howtotake_val = howtotake_map.get(howtotake_name);

                System.out.println("howtotake_name------" + howtotake_name);
                System.out.println("howtotake_val------" + drug_type_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------clinics------------------------------------------------------------------

        //------------------clinics------------------------------------------------------------------
        spinner_when.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                whentotake_name = spinner_when.getSelectedItem().toString();
                whentotake_val = whentotake_map.get(whentotake_name);

                System.out.println("whentotake_name------" + whentotake_name);
                System.out.println("whentotake_val------" + whentotake_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------clinics------------------------------------------------------------------


        btn_entry_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("drug_type_name---" + drug_type_name);
                System.out.println("drug_type_val---" + drug_type_val);

                System.out.println("howtotake_name---" + howtotake_name);
                System.out.println("howtotake_val---" + howtotake_val);

                System.out.println("whentotake_name---" + whentotake_name);
                System.out.println("whentotake_val---" + whentotake_val);

                String drug_name = edt_drug_name.getText().toString();
                String dose_name = edt_dosage.getText().toString();
                String drug_qty = edt_drug_qty.getText().toString();
                String drug_days = edt_drug_days.getText().toString();

                System.out.println("drug_name---" + drug_name);
                System.out.println("dose_name---" + dose_name);
                System.out.println("drug_qty---" + drug_qty);
                System.out.println("drug_days---" + drug_days);

                try {

                    json_feedback = new JSONObject();
                    json_feedback.put("drug_type", drug_type_val);
                    json_feedback.put("drug_name", drug_name);
                    json_feedback.put("drug_dose", dose_name);
                    json_feedback.put("quantity", drug_qty);
                    json_feedback.put("for_days", drug_days);
                    json_feedback.put("when_take", whentotake_val);
                    json_feedback.put("how_take", howtotake_val);
                    json_feedback.put("patientId", pat_id);
                    json_feedback.put("queryId", cur_qid);
                    json_feedback.put("prescriptionId", prescription_id);

                    System.out.println("json_feedback---" + json_feedback.toString());

                    new JSON_Feedback().execute(json_feedback);

                    //say_success();


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.doc.entry:", json_feedback.toString());
                    FlurryAgent.logEvent("android.doc.prescription_entry", articleParams);
                    //----------- Flurry -------------------------------------------------

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(Prescription_Entry_Activity.this);
                dialog.setMessage("Please wait..");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "writePrescription");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                report_response = json_response_obj.getString("status");
                System.out.println("report_response--------------" + report_response);

                Model.query_launch = "writePrescription";

                if (report_response.equals("1")) {
                    //say_success();
                    Toast.makeText(getApplicationContext(), "Prescription saved successfully..!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Prescription entered failed, try again", Toast.LENGTH_LONG).show();
                }

                finish();

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
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


    public void dialog_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Prescription_Entry_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Select Drug Type");

        final ArrayAdapter<String> currency_categories = new ArrayAdapter<String>(Prescription_Entry_Activity.this, R.layout.dialog_list_textview);

        currency_categories.add("Tablet");
        drug_type_map.put("Tablet", "1");

        currency_categories.add("Capsule");
        drug_type_map.put("Capsule", "2");

        currency_categories.add("Suspension");
        drug_type_map.put("Suspension", "3");

        currency_categories.add("Syrub");
        drug_type_map.put("Syrub", "4");

        currency_categories.add("Sachet");
        drug_type_map.put("Sachet", "9");

        currency_categories.add("Injection");
        drug_type_map.put("Injection", "5");

        currency_categories.add("Cream");
        drug_type_map.put("Cream", "6");

        currency_categories.add("Lotion");
        drug_type_map.put("Lotion", "7");

        currency_categories.add("Oinment");
        drug_type_map.put("Oinment", "8");

        currency_categories.add("Inhaler");
        drug_type_map.put("Inhaler", "10");

        currency_categories.add("Patch");
        drug_type_map.put("Patch", "11");

        currency_categories.add("Other");
        drug_type_map.put("Other", "12");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(currency_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = currency_categories.getItem(which);
                String select_value = (drug_type_map).get(currency_categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                drug_type_val = select_value;
                drug_type_name = select_text;

                tv_drug_type_name.setText(drug_type_name);
            }
        });
        builderSingle.show();
    }


    public void howtotake_layout_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Prescription_Entry_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("How to take");

        final ArrayAdapter<String> howtotake_map_categories = new ArrayAdapter<String>(Prescription_Entry_Activity.this, R.layout.dialog_list_textview);

        howtotake_map_categories.add("1-0-0");
        howtotake_map.put("1-0-0", "1");

        howtotake_map_categories.add("0-1-0");
        howtotake_map.put("0-1-0", "2");

        howtotake_map_categories.add("0-0-1");
        howtotake_map.put("0-0-1", "3");

        howtotake_map_categories.add("1-1-0");
        howtotake_map.put("1-1-0", "4");

        howtotake_map_categories.add("0-1-1");
        howtotake_map.put("0-1-1", "5");

        howtotake_map_categories.add("1-0-1");
        howtotake_map.put("1-0-1", "6");

        howtotake_map_categories.add("1-1-1");
        howtotake_map.put("1-1-1", "7");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(howtotake_map_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = howtotake_map_categories.getItem(which);
                String select_value = (howtotake_map).get(howtotake_map_categories.getItem(which));

                System.out.println("howtotake_val---" + select_value);
                System.out.println("howtotake_name---" + select_text);

                howtotake_val = select_value;
                howtotake_name = select_text;

                tv_howtotake_name.setText(howtotake_name);
            }
        });
        builderSingle.show();
    }


    public void whentotake_layout_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Prescription_Entry_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("When to take");

        final ArrayAdapter<String> whentotake_map_categories = new ArrayAdapter<String>(Prescription_Entry_Activity.this, R.layout.dialog_list_textview);

        whentotake_map_categories.add("After Food");
        whentotake_map.put("After Food", "1");

        whentotake_map_categories.add("Before Food");
        whentotake_map.put("Before Food", "2");

        whentotake_map_categories.add("On Empty Stomach");
        whentotake_map.put("On Empty Stomach", "3");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(whentotake_map_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = whentotake_map_categories.getItem(which);
                String select_value = (whentotake_map).get(whentotake_map_categories.getItem(which));

                System.out.println("whentotake_val---" + select_value);
                System.out.println("whentotake_name---" + select_text);

                whentotake_val = select_value;
                whentotake_name = select_text;

                tv_whentotake_name.setText(whentotake_name);
            }
        });
        builderSingle.show();
    }


    public void apply_map() {

        //------- Setting Drug Type----------------------
        final List<String> currency_categories = new ArrayList<String>();

        currency_categories.add("Tablet");
        drug_type_map.put("Tablet", "1");

        currency_categories.add("Capsule");
        drug_type_map.put("Capsule", "2");

        currency_categories.add("Suspension");
        drug_type_map.put("Suspension", "3");

        currency_categories.add("Syrub");
        drug_type_map.put("Syrub", "4");

        currency_categories.add("Sachet");
        drug_type_map.put("Sachet", "5");

        currency_categories.add("Injection");
        drug_type_map.put("Injection", "6");

        currency_categories.add("Cream");
        drug_type_map.put("Cream", "7");

        currency_categories.add("Lotion");
        drug_type_map.put("Lotion", "8");

        currency_categories.add("Oinment");
        drug_type_map.put("Oinment", "9");

        currency_categories.add("Inhaler");
        drug_type_map.put("Inhaler", "10");

        currency_categories.add("Patch");
        drug_type_map.put("Patch", "11");

        currency_categories.add("Other");
        drug_type_map.put("Other", "12");


        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(Prescription_Entry_Activity.this, android.R.layout.simple_spinner_item, currency_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dtype.setAdapter(lang_dataAdapter);
        //------- Setting Drug Type----------------------


        //------- Setting howtotake_map----------------------
        final List<String> howtotake_map_categories = new ArrayList<String>();

        howtotake_map_categories.add("1-0-0");
        howtotake_map.put("1-0-0", "1");

        howtotake_map_categories.add("0-1-0");
        howtotake_map.put("0-1-0", "2");

        howtotake_map_categories.add("0-0-1");
        howtotake_map.put("0-0-1", "3");

        howtotake_map_categories.add("1-1-0");
        howtotake_map.put("1-1-0", "4");

        howtotake_map_categories.add("0-1-1");
        howtotake_map.put("0-1-1", "5");

        howtotake_map_categories.add("1-0-1");
        howtotake_map.put("1-0-1", "6");

        howtotake_map_categories.add("1-1-1");
        howtotake_map.put("1-1-1", "7");


        ArrayAdapter<String> howtotake_dataAdapter = new ArrayAdapter<String>(Prescription_Entry_Activity.this, android.R.layout.simple_spinner_item, howtotake_map_categories);
        howtotake_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_how.setAdapter(howtotake_dataAdapter);
        //------- Setting Drug Type----------------------


        //------- Setting When to Take---------------------
        final List<String> whentotake_map_categories = new ArrayList<String>();

        whentotake_map_categories.add("After Food");
        whentotake_map.put("After Food", "1");

        whentotake_map_categories.add("Before Food");
        whentotake_map.put("Before Food", "2");

        whentotake_map_categories.add("On Empty Stomach");
        whentotake_map.put("On Empty Stomach", "3");


        ArrayAdapter<String> whentotake_dataAdapter = new ArrayAdapter<String>(Prescription_Entry_Activity.this, android.R.layout.simple_spinner_item, whentotake_map_categories);
        whentotake_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_when.setAdapter(whentotake_dataAdapter);
        //------- Setting When to Take----------------------

    }
}

