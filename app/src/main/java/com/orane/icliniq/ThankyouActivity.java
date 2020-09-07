package com.orane.icliniq;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;

public class ThankyouActivity extends AppCompatActivity {

    TextView tv_close_button, note_title, note_desc;
    LinearLayout query_post_layout, cons_post_layout;
    String title, type_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankuforquery);

        //--------- Get Intent -------------------------------
        Intent intent = getIntent();
        type_val = intent.getStringExtra("type");
        System.out.println("Thankyou page type_val----------------" + type_val);
        //--------- Get Intent -------------------------------

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        tv_close_button = (TextView) findViewById(R.id.tv_close_button);
        note_title = (TextView) findViewById(R.id.note_title);
        note_desc = (TextView) findViewById(R.id.note_desc);

        query_post_layout = (LinearLayout) findViewById(R.id.query_post_layout);
        cons_post_layout = (LinearLayout) findViewById(R.id.cons_post_layout);

        Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name);
        tv_close_button.setTypeface(robo_bold);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }


        if (type_val.equals("query")) {
            note_title.setText("Your query has been successfully posted");
            note_desc.setText("You will be notified once the doctor answer the query");
        } else if (type_val.equals("consultation")) {
            note_title.setText("Your Consultation booking has been done.");
            note_desc.setText("You will be notified once the doctor confirm the consultation");
        } else if (type_val.equals("subscription")) {
            note_title.setText("Your subscription has been done.");
            note_desc.setText("");
        } else if (type_val.equals("prepaid")) {
            note_title.setText("Your prepaid recharge has been done.");
            note_desc.setText("");
        } else if (type_val.equals("icq100")) {
            note_title.setText("Your #icliniq100hrs Query has been posted");
            note_desc.setText("You will be notified once the doctor answer the query");
        } else if (type_val.equals("labtest")) {
            note_title.setText("Thank you for payment");
            note_desc.setText("Our home collection staff will contact you");
        } else {
            note_title.setText("Your payment has been done successfully...!");
            note_desc.setText("");
        }


        tv_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (type_val.equals("query")) {
                        //finishAffinity();
                        Intent intent = new Intent(ThankyouActivity.this, QueryActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (type_val.equals("consultation")) {
                        //finishAffinity();
                        Intent intent = new Intent(ThankyouActivity.this, BookingListActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (type_val.equals("subscription")) {
                        finishAffinity();
                        Intent intent = new Intent(ThankyouActivity.this, Connect_Home_Actvity.class);
                        startActivity(intent);
                        finish();
                    } else if (type_val.equals("prepaid")) {
                        finishAffinity();
                        Intent intent = new Intent(ThankyouActivity.this, Connect_Home_Actvity.class);
                        startActivity(intent);
                        finish();
                    } else if (type_val.equals("icq100")) {
                        Intent intent = new Intent(ThankyouActivity.this, QueryActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //Intent intent = new Intent(ThankyouActivity.this, Connect_Home_Actvity.class);
                        //startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
 /*                   if (Model.query_launch.equals("Askquery2")) {
                        Intent i = new Intent(ThankyouActivity.this, QueryActivity.class);
                        startActivity(i);
                        finish();
                    } else if (Model.query_launch.equals("Consult3")) {
                        Intent i = new Intent(ThankyouActivity.this, BookingListActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent intent = new Intent(ThankyouActivity.this, Connect_Home_Actvity.class);
                        startActivity(intent);
                        finish();
                    }*/

                    if (type_val.equals("query")) {
                        Intent i = new Intent(ThankyouActivity.this, QueryActivity.class);
                        startActivity(i);
                        finish();
                    } else if (type_val.equals("consultation")) {
                        Intent i = new Intent(ThankyouActivity.this, BookingListActivity.class);
                        startActivity(i);
                        finish();
                    } else if (type_val.equals("subscription")) {
                        finishAffinity();
                        Intent i = new Intent(ThankyouActivity.this, Connect_Home_Actvity.class);
                        startActivity(i);
                        finish();
                    } else if (type_val.equals("prepaid")) {
                        finishAffinity();
                        Intent i = new Intent(ThankyouActivity.this, Connect_Home_Actvity.class);
                        startActivity(i);
                        finish();
                    } else if (type_val.equals("icq100")) {
                        //finishAffinity();
                        Intent i = new Intent(ThankyouActivity.this, QueryActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent intent = new Intent(ThankyouActivity.this, Connect_Home_Actvity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5000);
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
}
