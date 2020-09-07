package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Offers_List_activity extends AppCompatActivity {

    Spinner spinner_ccode;
    EditText edt_mobno, edt_docname, edt_email;
    Map<String, String> cc_map = new HashMap<String, String>();
    Button btn_submit;
    JSONObject json, jsonobj_invite;
    public String deals_offers_list, isActivePlan, docname, email, cc_name, cccode;
    Typeface font_reg, font_bold;
    LinearLayout full_layout;
    TextView tv_tooltit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals_offers_list);

        FlurryAgent.onPageView();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tv_tooltit = toolbar.findViewById(R.id.tv_tooltit);
        tv_tooltit.setTypeface(font_reg);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Deals/Offers List");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------

        full_layout = findViewById(R.id.full_layout);

        //----------- Deals and Offers--------------------------------------------------------
        String get_family_url = Model.BASE_URL + "/sapp/listDealsAndOffers?page=1&limit=10&type=offers&user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("Home List of Offers---------" + get_family_url);
        new JSON_deals_offers().execute(get_family_url);
        //------------Deals and Offers-------------------------------------------------------

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


    private class JSON_deals_offers extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Offers_List_activity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                deals_offers_list = jParser.getJSONString(urls[0]);

                System.out.println("Family URL---------------" + urls[0]);
                System.out.println("deals_offers_list-------------" + deals_offers_list);

                return deals_offers_list;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            try {

                JSONObject jobject_offers = new JSONObject(deals_offers_list);

                String data_text = jobject_offers.getString("data");
                String is_offer_text = jobject_offers.getString("is_offer");

                JSONArray data_text_array = new JSONArray(data_text);

                System.out.println("data_text_array------" + data_text_array.toString());

                full_layout.removeAllViews();

                for (int i = 0; i < data_text_array.length(); i++) {
                    JSONObject jsonobj_files = data_text_array.getJSONObject(i);

                    String title_text = jsonobj_files.getString("title");
                    String bg_img_text = jsonobj_files.getString("bg_img");
                    String offers_id = jsonobj_files.getString("id");
                    String is_hline_val = jsonobj_files.getString("is_hline");
                    String btn_lbl = jsonobj_files.getString("btn_lbl");
                    String doc_id = jsonobj_files.getString("doc_id");
                    String fcode = jsonobj_files.getString("fcode");
                    String qid = jsonobj_files.getString("qid");
                    String strhtml_data = jsonobj_files.getString("strHtml");

                    if (jsonobj_files.has("isActivePlan")) {
                        isActivePlan = jsonobj_files.getString("isActivePlan");
                    }


                    View recc_vi = getLayoutInflater().inflate(R.layout.deals_offers_list_row, null);

                    LinearLayout deal_full_layout = recc_vi.findViewById(R.id.deal_full_layout);
                    ObservableWebView webview = recc_vi.findViewById(R.id.webview_top);

                    Button btn_submit = recc_vi.findViewById(R.id.btn_submit);

                    TextView tv_offers_id = recc_vi.findViewById(R.id.tv_offers_id);
                    TextView tv_hline = recc_vi.findViewById(R.id.tv_hline);
                    TextView tv_fcode = recc_vi.findViewById(R.id.tv_fcode);
                    TextView tv_doc_id = recc_vi.findViewById(R.id.tv_doc_id);
                    TextView tv_qid = recc_vi.findViewById(R.id.tv_qid);
                    TextView tv_offer_type = recc_vi.findViewById(R.id.tv_offer_type);
                    TextView tv_isActivePlan = recc_vi.findViewById(R.id.tv_isActivePlan);

                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadDataWithBaseURL("", strhtml_data, "text/html", "UTF-8", "");
                    webview.setLongClickable(false);

                    tv_offers_id.setText(Html.fromHtml(offers_id));
                    tv_hline.setText(Html.fromHtml(is_hline_val));
                    tv_fcode.setText(fcode);
                    tv_doc_id.setText(doc_id);
                    tv_qid.setText(qid);
                    btn_submit.setText(btn_lbl);
                    tv_isActivePlan.setText(isActivePlan);

                    tv_offer_type.setText(btn_lbl);


                    if (isActivePlan.equals("0")) {
                        btn_submit.setBackgroundResource(R.drawable.button_rounded_green);
                        btn_submit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        if (is_hline_val.equals("0")) {
                            btn_submit.setBackgroundResource(R.drawable.button_rounded_new);
                            btn_submit.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            btn_submit.setBackgroundResource(R.drawable.button_border_blue);
                            btn_submit.setTextColor(getResources().getColor(R.color.app_color));
                        }
                    }

                    full_layout.addView(recc_vi);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

    public void offers_list_click(View v) {

        View parent = (View) v.getParent();
        View grand_parent = (View) parent.getParent();

        TextView tv_offers_id = grand_parent.findViewById(R.id.tv_offers_id);
        TextView tv_hline = grand_parent.findViewById(R.id.tv_hline);
        TextView tv_fcode = grand_parent.findViewById(R.id.tv_fcode);
        TextView tv_isActivePlan = grand_parent.findViewById(R.id.tv_isActivePlan);
        TextView tv_doc_id = grand_parent.findViewById(R.id.tv_doc_id);
        TextView tv_qid = grand_parent.findViewById(R.id.tv_qid);
        TextView tv_offer_type = grand_parent.findViewById(R.id.tv_offer_type);

        String offer_id = tv_offers_id.getText().toString();
        String tv_hline_text = tv_hline.getText().toString();
        String tv_fcode_text = tv_fcode.getText().toString();
        String tv_isActivePlan_text = tv_isActivePlan.getText().toString();
        String tv_doc_id_text = tv_doc_id.getText().toString();
        String tv_qid_text = tv_qid.getText().toString();
        String tv_offer_type_text = tv_offer_type.getText().toString();

        System.out.println("offer_id-----------------" + offer_id);
        System.out.println("tv_hline_text-----------------" + tv_hline_text);
        System.out.println("tv_fcode_text-----------------" + tv_fcode_text);
        System.out.println("tv_isActivePlan_text-----------------" + tv_isActivePlan_text);
        System.out.println("tv_doc_id_text-----------------" + tv_doc_id_text);
        System.out.println("tv_qid_text-----------------" + tv_qid_text);
        System.out.println("tv_offer_type_text-----------------" + tv_offer_type_text);


        if (tv_offer_type_text.equals("View Details")) {
            Intent intent = new Intent(Offers_List_activity.this, Offers_view.class);
            intent.putExtra("offer_id", offer_id);
            startActivity(intent);
        } else {
            if (tv_hline_text.equals("1")) {
                Intent intent = new Intent(Offers_List_activity.this, HotlineChatViewActivity.class);
                intent.putExtra("selqid", tv_qid_text);
                intent.putExtra("Doctor_id", tv_doc_id_text);
                intent.putExtra("docurl", "");
                intent.putExtra("fcode", tv_fcode_text);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Offers_List_activity.this, Instant_Chat.class);
                intent.putExtra("doctor_id", tv_doc_id_text);
                intent.putExtra("plan_id", offer_id);
                startActivity(intent);
            }
        }

    }
}
