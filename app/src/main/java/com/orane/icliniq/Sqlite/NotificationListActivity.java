package com.orane.icliniq.Sqlite;

import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationListActivity extends AppCompatActivity implements View.OnClickListener {

    public View vi;
    private FrameLayout li;
    JSONObject jsonobj, jsonobj1, jsonobj_offers;
    JSONArray jsonarray;
    public String full_url, off_id, prep_inv_id, prep_inv_fee, prep_inv_strfee, off_label, strfee, strdesc1, strdesc2;
    LinearLayout parent_offer_layout;
    RelativeLayout full_layout,nolayout;
    LinearLayout offer_layout, netcheck_layout;
    ProgressBar progressBar;
    TextView tv_title, tv_desc, offer_title, off_desc, offid, tv_msg, tv_date, tv_id;
    Long startTime;

    public File imageFile;
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Notifications");
        }
        //------------ Object Creations -------------------------------.
        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Notification_List");
        //----------------- Kissmetrics ----------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        nolayout = (RelativeLayout) findViewById(R.id.nolayout);
        parent_offer_layout = (LinearLayout) findViewById(R.id.parent_layout);
        full_layout = (RelativeLayout) findViewById(R.id.full_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);


        //-------------------- Insert Data -----------------------------------
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
        String currentDateandTime = sdf.format(new Date());
        DatabaseHandler db = new DatabaseHandler(this);

        db.deleteAll();
        //db.addContact(new Contact("Seeks to further amend Notification No. 27/2011-Customs, dated the 1st March, 2011, so as to provide exemption from export duty to sugar exported under Advance Authorization Scheme subject to specified conditions.", currentDateandTime));
        //-------------------- Insert Data -----------------------------------

        List<Contact> contacts = db.getAllContacts();

        if (contacts.size() > 0) {

            full_layout.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);

            for (Contact cn : contacts) {

                vi = getLayoutInflater().inflate(R.layout.notification_row, null);
                tv_msg = (TextView) vi.findViewById(R.id.tv_msg);
                tv_date = (TextView) vi.findViewById(R.id.tv_date);
                tv_id = (TextView) vi.findViewById(R.id.tv_id);
                offer_layout = (LinearLayout) vi.findViewById(R.id.offer_layout);

                //tv_id.setText(cn.getID());
                tv_msg.setText(cn.getName());
                tv_date.setText(cn.getPhoneNumber());

                offer_layout.setOnClickListener(NotificationListActivity.this);

                parent_offer_layout.addView(vi);
            }
            //--------------------------------------------------------------------------------------------------------
        } else {
            full_layout.setVisibility(View.GONE);
            nolayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {

        System.out.println("OnClick----------------");

/*        View parent = (View) v.getParent();

        TextView tvid = (TextView) parent.findViewById(R.id.tvoid);
        String tvidval = tvid.getText().toString();
        System.out.println("tvidval-----------------------------------" + tvidval);

        String url = Model.BASE_URL + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=prepay&item_id=" + tvidval + "&token=" + Model.token;
        System.out.println("Prep_Inv_url-----------" + url);
        new JSON_Prepare_inv().execute(url);

        //----------------- Kissmetrics ----------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Prepaid_Invoice_View");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("Packid:", tvidval);
        Model.kiss.set(properties);
        //----------------- Kissmetrics ----------------------------------*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.prepack_menu, menu);
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

    public final boolean isInternetOn() {


        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
