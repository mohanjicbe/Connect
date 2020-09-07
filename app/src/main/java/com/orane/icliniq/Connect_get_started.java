package com.orane.icliniq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Connect_get_started extends AppCompatActivity {

    Button btn_started;
    public static Connect_get_started otpinst;

    public static Connect_get_started instance() {
        return otpinst;
    }

    @Override
    public void onStart() {
        super.onStart();
        otpinst = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_get_started);

        btn_started = (Button) findViewById(R.id.btn_started);

        btn_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Connect_get_started.this, Connect_Hosp_Select.class);
                startActivity(i);
                finish();
            }
        });


    }

}
