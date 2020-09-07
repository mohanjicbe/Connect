package com.orane.icliniq;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.orane.icliniq.Model.Model;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSomeOneActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {


    Spinner spinner_gender;
    Map<String, String> gen_map = new HashMap<String, String>();
    MaterialEditText edt_name, edt_age;
    ImageView img_close;
    String cons_select_date;
    Button btn_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.someone_edit);

        img_close = (ImageView) findViewById(R.id.img_close);
        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        edt_name = (MaterialEditText) findViewById(R.id.edt_name);
        edt_age = (MaterialEditText) findViewById(R.id.edt_age);
        btn_date = (Button) findViewById(R.id.btn_date);

        //------- Setting spinner_gender ----------------------
        final List<String> lang_categories = new ArrayList<String>();

        lang_categories.add("Select Gender");

        lang_categories.add("Male");
        gen_map.put("Male", "1");
        lang_categories.add("Female");
        gen_map.put("Female", "2");
        lang_categories.add("Female");
        gen_map.put("Thirdgender", "3");

        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(EditSomeOneActivity.this, android.R.layout.simple_spinner_item, lang_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(lang_dataAdapter);
        //---------------------------------------------


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        Typeface tf = Typeface.createFromAsset(getAssets(), Model.font_name);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Closing-----");
                finish();
            }
        });

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String gen_name = spinner_gender.getSelectedItem().toString();
                String gen_val = gen_map.get(gen_name);

                System.out.println("gen_name----------" + gen_name);
                System.out.println("gen_val----------" + gen_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditSomeOneActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);

                dpd.setAccentColor(Color.parseColor("#9C27B0"));
                dpd.setTitle("Please select your DOB");

                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });

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
            System.out.println("For System select_date---------" + newDateStr);
            //--------------------------------

            btn_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            System.out.println("cons_select_date---------" + cons_select_date);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
