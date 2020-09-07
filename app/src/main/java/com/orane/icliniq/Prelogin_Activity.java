package com.orane.icliniq;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orane.icliniq.Model.Model;


public class Prelogin_Activity extends AppCompatActivity {

    Button btnlogin;
    LinearLayout signup_layout;
    TextView tv_start, tv_start2, tv_start3,tv2,tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        signup_layout = (LinearLayout) findViewById(R.id.signup_layout);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_start2 = (TextView) findViewById(R.id.tv_start2);
        tv_start3 = (TextView) findViewById(R.id.tv_start3);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);

        //------------------Font-------------------------------
        Typeface font_Bold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        Typeface font_regular = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);

        tv_start.setTypeface(font_Bold);
        tv_start2.setTypeface(font_Bold);
        tv_start3.setTypeface(font_Bold);
        btnlogin.setTypeface(font_regular);
        tv2.setTypeface(font_regular);
        tv3.setTypeface(font_Bold);
        //------------------Font-------------------------------


        signup_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prelogin_Activity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prelogin_Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
