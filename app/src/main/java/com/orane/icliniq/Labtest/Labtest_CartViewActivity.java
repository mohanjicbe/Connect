package com.orane.icliniq.Labtest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.orane.icliniq.CommonActivity;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.Video_WebViewActivity;
import com.orane.icliniq.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

public class Labtest_CartViewActivity extends AppCompatActivity {


    Animation shake;
    LinearLayout price_show, price_layout, empty_layout;
    ScrollView scrollview;
    String str_response, hme_collection, hc_lal_amt, total_val, cartlist;
    Toolbar toolbar;
    JSONObject cartitem_jsonobj, jobj1, json_qty, json_response_obj;
    TextView price_tv_head;
    JSONArray cartlist_jsonArray;
    Button btn_continue;
    RelativeLayout remove_layout, lal_layout, thyro_layout, bottom_layout, wish_layout;
    LinearLayout cart_list_layout;
    View vi;
    TextView tv_tot_amt, tv_hme_collection_lal, tv_bott_tot_amt, tv_hme_collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labtest_cart_view);

        //-------------------------- Toolbar -------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("My Cart");

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //-------------------------- Toolbar -------------------------------

        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        lal_layout = (RelativeLayout) findViewById(R.id.lal_layout);
        thyro_layout = (RelativeLayout) findViewById(R.id.thyro_layout);
        price_layout = (LinearLayout) findViewById(R.id.price_layout);
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        price_show = (LinearLayout) findViewById(R.id.price_show);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        price_tv_head = (TextView) findViewById(R.id.price_tv_head);
        tv_hme_collection = (TextView) findViewById(R.id.tv_hme_collection);
        tv_bott_tot_amt = (TextView) findViewById(R.id.tv_bott_tot_amt);
        tv_tot_amt = (TextView) findViewById(R.id.tv_tot_amt);
        tv_hme_collection_lal = (TextView) findViewById(R.id.tv_hme_collection_lal);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        cart_list_layout = (LinearLayout) findViewById(R.id.cart_list_layout);

        price_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollview.fullScroll(View.FOCUS_DOWN);

                //------------------ Button Animation -------------------------
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanim);
                price_tv_head.startAnimation(shake);
                //------------------ Button Animation -------------------------
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                Intent intent = new Intent(Labtest_CartViewActivity.this, Labtest_Beneficiary.class);
                intent.putExtra("amt_pay", total_val);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        Labtest_CartViewActivity.this.finish();
                    }
                });

                startActivityForResult(intent, 1);

/*
                Intent intent = new Intent(Labtest_CartViewActivity.this, Labtest_Beneficiary.class);
                //intent.putExtra("title", tv_gridtext2.getText().toString());
                startActivity(intent);*/
            }
        });

        //--------- Listings ---------------------
        String full_url = Model.BASE_URL + "sapp/myCart?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("full_url-------------" + full_url);
        new JSON_cart_list().execute(full_url);
        //--------- Listings ---------------------

    }


    private class JSON_cart_list extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Labtest_CartViewActivity.this);
            dialog.setMessage("Please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("Cart List response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("str_response--------------" + str_response);

                JSONObject cart_resp = new JSONObject(str_response);

                if (cart_resp.has("status")) {

                    String status_text = cart_resp.getString("status");

                    if (status_text.equals("0")) {

                        String message_text = cart_resp.getString("message");

                        cart_list_layout.setVisibility(View.GONE);
                        price_layout.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                        bottom_layout.setVisibility(View.GONE);

                    }
                } else {

                    String cartlist_text = cart_resp.getString("cartlist");

                    hme_collection = cart_resp.getString("homeCollection");
                    hc_lal_amt = cart_resp.getString("hc_lal");
                    total_val = cart_resp.getString("total");

                    if (cart_resp.has("item_count")) {
                        String item_count_text = cart_resp.getString("item_count");
                        Model.mNotificationCounter = Integer.parseInt(item_count_text);
                    }


                    //-------------Apply Details------------------------------------
                    //-------------------------------------------------
                    if (hc_lal_amt != null && !hc_lal_amt.isEmpty() && !hc_lal_amt.equals("null") && !hc_lal_amt.equals("") && !hc_lal_amt.equals("0")) {
                        tv_hme_collection_lal.setText("Rs. " + hc_lal_amt);
                        lal_layout.setVisibility(View.VISIBLE);
                    } else {
                        lal_layout.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------

                    //-------------------------------------------------
                    if (hme_collection != null && !hme_collection.isEmpty() && !hme_collection.equals("null") && !hme_collection.equals("") && !hme_collection.equals("0")) {
                        tv_hme_collection.setText("Rs. " + hme_collection);
                        thyro_layout.setVisibility(View.VISIBLE);
                    } else {
                        thyro_layout.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------

                    tv_tot_amt.setText("Rs. " + total_val);
                    tv_bott_tot_amt.setText("Rs. " + total_val);

                    //-------------Apply Details------------------------------------

                    Model.query_launch = "cart";

                    produce_list(cartlist_text);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.doclist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            Model.query_launch = "cart";

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        //------------------ Button Animation -------------------------
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanim);
        price_tv_head.startAnimation(shake);
        //------------------ Button Animation -------------------------

        Model.query_launch = "cart";
    }


    public void produce_list(String cart_response) {

        try {
            cart_list_layout.removeAllViews();

            cartlist_jsonArray = new JSONArray(cart_response);

            System.out.println("cart_response----------" + cart_response);


            if (cartlist_jsonArray.length() > 0) {

                for (int i = 0; i < cartlist_jsonArray.length(); i++) {

                    cartitem_jsonobj = cartlist_jsonArray.getJSONObject(i);
                    System.out.println("cartitem_jsonobj-----" + cartitem_jsonobj.toString());

                    String id_val = cartitem_jsonobj.getString("id");
                    String name = cartitem_jsonobj.getString("name");
                    String price = cartitem_jsonobj.getString("price");
                    String vendor_name = cartitem_jsonobj.getString("vendor");

                    System.out.println("id_val-----" + id_val);
                    System.out.println("name-----" + name);
                    System.out.println("price-----" + price);
                    System.out.println("vendor_name-----" + vendor_name);

                    final View vi = getLayoutInflater().inflate(R.layout.labtest_cart_view_row, null);
                    TextView tv_textname = (TextView) vi.findViewById(R.id.tv_textname);
                    TextView tv_vendor = (TextView) vi.findViewById(R.id.tv_vendor);
                    TextView tv_id = (TextView) vi.findViewById(R.id.tv_id);
                    TextView tv_testlist = (TextView) vi.findViewById(R.id.tv_testlist);
                    TextView tv_newprice = (TextView) vi.findViewById(R.id.tv_newprice);
                    remove_layout = (RelativeLayout) vi.findViewById(R.id.remove_layout);
                    wish_layout = (RelativeLayout) vi.findViewById(R.id.wish_layout);
                    ImageView img_closebutton = (ImageView) vi.findViewById(R.id.img_closebutton);

                    //-------- Setting Values ------------------------------------
                    tv_textname.setText(name);
                    tv_id.setText(id_val);
                    tv_newprice.setText("Rs. " + price);
                    tv_vendor.setText(vendor_name);

                    if (vendor_name.equals("Thyro Care")) {
                        tv_vendor.setBackground(getResources().getDrawable(R.drawable.labtest_vendor_rounded));
                    } else {
                        tv_vendor.setBackground(getResources().getDrawable(R.drawable.labtest_vendor_rounded2));
                    }


                    //-------- Setting Values ------------------------------------

                    img_closebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            new AlertDialog.Builder(Labtest_CartViewActivity.this)
                                    //.setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Remove!")
                                    .setMessage("Are you sure you want to remove the item/test from this cart?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            //--------- Getting Item ID ----------------------------
                                            View paret = (View) v.getParent();
                                            //View grand_paret = (View) paret.getParent();
                                            //View grand_paret2 = (View) paret.getParent();

                                            TextView tv_id = (TextView) paret.findViewById(R.id.tv_id);

                                            String id_val = tv_id.getText().toString();
                                            System.out.println("id_val----------" + id_val);
                                            //--------- Getting Item ID ----------------------------

                                            //------------ Removing Item -------------------------------
                                            try {
                                                json_qty = new JSONObject();
                                                json_qty.put("user_id", (Model.id));
                                                json_qty.put("id", id_val);
                                                json_qty.put("token", Model.token);

                                                System.out.println("Json_feedback---" + json_qty.toString());

                                                new Json_remove_cart().execute(json_qty);

                                                ((LinearLayout) vi.getParent()).removeView(vi);

                                            } catch (Exception e2) {
                                                e2.printStackTrace();
                                            }
                                            //------------ Removing Item -------------------------------


                                        }

                                    })
                                    .setNegativeButton("No", null)
                                    .show();



                            // apply_qty("" + cur_qty_int_val); Android App
                        }
                    });


                    //---------------- Custom default Font --------------------
                    Typeface robo_regular = Typeface.createFromAsset(getAssets(), Model.font_name);
                    Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);
                    tv_textname.setTypeface(robo_bold);
                    tv_newprice.setTypeface(robo_regular);
                    //---------------- Custom default Font --------------------

                    cart_list_layout.addView(vi);


                }

                cart_list_layout.setVisibility(View.VISIBLE);
                price_layout.setVisibility(View.VISIBLE);
                empty_layout.setVisibility(View.GONE);
                bottom_layout.setVisibility(View.VISIBLE);

            } else {
                cart_list_layout.setVisibility(View.GONE);
                price_layout.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                bottom_layout.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class Json_remove_cart extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Labtest_CartViewActivity.this);
            dialog.setMessage("Removing.. please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "remove_cart");

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

                System.out.println("Response--------" + json_response_obj.toString());

                String cart_test = json_response_obj.getString("cart");

                JSONObject cart_obj = new JSONObject(cart_test);

                if (cart_obj.has("status")) {

                    String sta_val = cart_obj.getString("status");

                    if (sta_val.equals("0")) {
                        cart_list_layout.setVisibility(View.GONE);
                        price_layout.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                        bottom_layout.setVisibility(View.GONE);
                    }

                } else {

                    String cartlist_text = cart_obj.getString("cartlist");

                    hme_collection = cart_obj.getString("homeCollection");
                    hc_lal_amt = cart_obj.getString("hc_lal");
                    total_val = cart_obj.getString("total");

                    if (cart_obj.has("item_count")) {
                        String item_count_text = cart_obj.getString("item_count");
                        Model.mNotificationCounter = Integer.parseInt(item_count_text);
                        System.out.println("Model.mNotificationCounter==remove----------" + Model.mNotificationCounter);
                    }


                    //-------------Apply Details------------------------------------
                    //-------------------------------------------------
                    if (hc_lal_amt != null && !hc_lal_amt.isEmpty() && !hc_lal_amt.equals("null") && !hc_lal_amt.equals("") && !hc_lal_amt.equals("0")) {
                        tv_hme_collection_lal.setText("Rs. " + hc_lal_amt);
                        lal_layout.setVisibility(View.VISIBLE);
                    } else {
                        lal_layout.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------

                    //-------------------------------------------------
                    if (hme_collection != null && !hme_collection.isEmpty() && !hme_collection.equals("null") && !hme_collection.equals("") && !hme_collection.equals("0")) {
                        tv_hme_collection.setText("Rs. " + hme_collection);
                        thyro_layout.setVisibility(View.VISIBLE);
                    } else {
                        thyro_layout.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------

                    tv_tot_amt.setText("Rs. " + total_val);
                    tv_bott_tot_amt.setText("Rs. " + total_val);

                    //-------------Apply Details------------------------------------

                    Model.query_launch = "cart";

                    produce_list(cartlist_text);

                }


                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
