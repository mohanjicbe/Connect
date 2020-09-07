package com.orane.icliniq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.razorpay.Checkout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stripe.android.SourceCallback;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceCardData;
import com.stripe.android.model.SourceParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;


public class Invoice_Page_New extends AppCompatActivity {

    private String _ccNumber = "";
    TextView mTitle, tv_gst_val, tv_gst_text, tv_igcharges, tv_ihfee_val, tv_base_text, tv_base_val, tv_cgst_val, tv_cgst_text, tv_sgst_text, tv_sgst_val, tv_paymethod_text, inv_amt, tv_third_pay, tv_second_pay, inv_title, tv_ftrackmode, tvdelmodelab, tv_revert, tv_strinvno, tv_strfees, tv_invdesc, tv_invfeetot, tv_mywallbal, ftrack_text;
    Button btn_paypal, btn_submit, btn_coupon_apply, btn_make_pay, btn_confirm;
    public String str_response, mob_no, inv_fee, txtidval, ftrack_str, ftrack_mode, str_opt_enable, str_opt_disable, ftrack_opt, inv_title_txt, inv_desc_txt, inv_fee_payable, inv_strfee_payable, inv_walletfee, inv_browsercountry, inv_str_response, coupon_code, apply_coupon_status, edt_coupon_text, qid, inv_id, inv_strfee, type_val;
    Boolean inv_btnconfirm;
    JSONObject jsonobj1;
    RelativeLayout coupon_layout;
    ImageView img_paytm, img_free, arrow1, arrow2, arrow3;
    ScrollView scrollview;
    LinearLayout other_fee__layout;
    RadioButton radio1, radio2, radio3;
    String card_number;
    View vi;
    LinearLayout sub_layout, revert_layout, india_pay_layout, second_inner_layout, third_inner_layout;
    JSONObject json_txn, razorpay_jsonobj, json_applycoupon, json_viewinv, json_coupon_param, json_prepinv;
    MaterialEditText edt_coupon;
    ProgressBar progressBar;
    ImageView up_arrow, down_arrow;
    RelativeLayout first_layout, second_layout, third_layout;
    LinearLayout first_inner_layout, ihc_layout, show_wallet_layout;
    RelativeLayout ftrack_layout;
    CheckBox btn_check;

    EditText edt_name_on_card, edt_phone;
    MaterialEditText edt_cvv;
    //MaskEditText edt_cardnum, edt_expiry_date;
    MaterialEditText edt_cardnum, edt_expiry_date;
    CardInputWidget card_input_widget;

    boolean doubleBackToExitPressedOnce = false;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
    public static final String isValid = "isValid";
    public static final String id = "id";
    public static final String browser_country = "browser_country";
    public static final String email = "email";
    public static final String fee_q = "fee_q";
    public static final String fee_consult = "fee_consult";
    public static final String fee_q_inr = "fee_q_inr";
    public static final String fee_consult_inr = "fee_consult_inr";
    public static final String currency_symbol = "currency_symbol";
    public static final String currency_label = "currency_label";
    public static final String have_free_credit = "have_free_credit";
    public static final String photo_url = "photo_url";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String sp_qid = "sp_qid_key";
    public static final String sp_mnum = "sp_mnum_key";

    Card card;
    Stripe stripe;
    Button btn_add;

    LinearLayout layout_curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_page_new);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mob_no = sharedpreferences.getString(sp_mnum, "");

        //-------------------- Toolbar --------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //-------------------- Toolbar --------------------------------------

        tv_gst_val = findViewById(R.id.tv_gst_val);
        tv_gst_text = findViewById(R.id.tv_gst_text);
        up_arrow = findViewById(R.id.up_arrow);
        down_arrow = findViewById(R.id.down_arrow);
        btn_paypal = findViewById(R.id.btn_paypal);
        btn_add = findViewById(R.id.btn_add);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_coupon_apply = findViewById(R.id.btn_coupon_apply);
        coupon_layout = findViewById(R.id.coupon_layout);
        sub_layout = findViewById(R.id.sub_layout);
        india_pay_layout = findViewById(R.id.india_pay_layout);
        scrollview = findViewById(R.id.scrollview);
        edt_coupon = findViewById(R.id.edt_coupon);
        ftrack_text = findViewById(R.id.ftrack_text);
        tv_paymethod_text = findViewById(R.id.tv_paymethod_text);
        progressBar = findViewById(R.id.progressBar);
        tv_third_pay = findViewById(R.id.tv_third_pay);
        inv_title = findViewById(R.id.inv_title);
        tv_strfees = findViewById(R.id.tv_strfees);
        tv_invdesc = findViewById(R.id.tv_invdesc);
        tv_invfeetot = findViewById(R.id.tv_invfeetot);
        tv_mywallbal = findViewById(R.id.tv_mywallbal);
        tv_revert = findViewById(R.id.tv_revert);
        tvdelmodelab = findViewById(R.id.tvdelmodelab);
        tv_ftrackmode = findViewById(R.id.tv_ftrackmode);
        tv_second_pay = findViewById(R.id.tv_second_pay);
        revert_layout = findViewById(R.id.revert_layout);
        show_wallet_layout = findViewById(R.id.show_wallet_layout);
        ftrack_layout = findViewById(R.id.ftrack_layout);
        first_layout = findViewById(R.id.first_layout);
        first_inner_layout = findViewById(R.id.first_inner_layout);
        second_layout = findViewById(R.id.second_layout);
        second_inner_layout = findViewById(R.id.second_inner_layout);
        third_layout = findViewById(R.id.third_layout);
        btn_make_pay = findViewById(R.id.btn_make_pay);
        img_free = findViewById(R.id.img_free);
        img_paytm = findViewById(R.id.img_paytm);
        btn_submit = findViewById(R.id.btn_submit);
        btn_check = findViewById(R.id.btn_check);
        inv_amt = findViewById(R.id.inv_amt);
        card_input_widget = findViewById(R.id.card_input_widget);

        tv_base_text = findViewById(R.id.tv_base_text);
        tv_base_val = findViewById(R.id.tv_base_val);
        tv_cgst_text = findViewById(R.id.tv_cgst_text);
        tv_cgst_val = findViewById(R.id.tv_cgst_val);
        tv_sgst_text = findViewById(R.id.tv_sgst_text);
        tv_sgst_val = findViewById(R.id.tv_sgst_val);
        tv_ihfee_val = findViewById(R.id.tv_ihfee_val);
        tv_igcharges = findViewById(R.id.tv_igcharges);


        third_inner_layout = findViewById(R.id.third_inner_layout);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        arrow1 = findViewById(R.id.arrow1);
        arrow2 = findViewById(R.id.arrow2);
        arrow3 = findViewById(R.id.arrow3);

        edt_name_on_card = findViewById(R.id.edt_name_on_card);
        edt_phone = findViewById(R.id.edt_phone);
        edt_cardnum = findViewById(R.id.edt_cardnum);
        edt_expiry_date = findViewById(R.id.edt_expiry_date);
        edt_cvv = findViewById(R.id.edt_cvv);

        layout_curr = findViewById(R.id.layout_curr);
        ihc_layout = findViewById(R.id.ihc_layout);
        other_fee__layout = findViewById(R.id.other_fee__layout);

        //--------------------- Fonts Sett -------------------------------------------
        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.ftrack_text)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.inv_title)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_strfees)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_invdesc)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_invfeetot)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_mywallbal)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_revert)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tvdelmodelab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_ftrackmode)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_payable)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_invfeetot)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_paymethod1)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_paymethod2)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_paymethod3)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.btn_submit)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_second_pay)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.btn_make_pay)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_sec_note)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_third_pay)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_pay_logos)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.btn_coupon_apply)).setTypeface(font_reg);

        ((TextView) findViewById(R.id.inv_title)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_igcharges)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_ihfee_val)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_base_text)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_base_val)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_cgst_text)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_cgst_val)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_sgst_text)).setTypeface(font_reg);

        //--------------------- Fonts Sett -------------------------------------------

        sub_layout.setVisibility(View.GONE);

        try {

            Intent intent = getIntent();
            qid = intent.getStringExtra("qid");
            inv_id = intent.getStringExtra("inv_id");
            inv_strfee = intent.getStringExtra("inv_strfee");
            type_val = intent.getStringExtra("type");

            System.out.println("qid---------" + qid);
            System.out.println("inv_id---------" + inv_id);
            System.out.println("inv_strfee---------" + inv_strfee);
            System.out.println("type_val---------" + type_val);

            if (qid.equals("0")) {
                coupon_layout.setVisibility(View.GONE);
            }

            if (new NetCheck().netcheck(Invoice_Page_New.this)) {
                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                    if (inv_id != null && !inv_id.isEmpty() && !inv_id.equals("null") && !inv_id.equals("")) {
                        //--------------------------------------------------------
                        String url = Model.BASE_URL + "/sapp/getinv?user_id=" + (Model.id) + "&id=" + inv_id + "&token=" + Model.token + "&enc=1";
                        System.out.println("get Invoice url-----------" + url);
                        new JSON_View_inv().execute(url);
                        //--------------------------------------------------------------------
                    } else {
                        scrollview.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Something went Wrong..! Go back and try Again", Toast.LENGTH_LONG).show();
                    }
                } else {
                    force_logout();
                }
            } else {
                Toast.makeText(Invoice_Page_New.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        edt_cardnum.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());
        edt_expiry_date.addTextChangedListener(new ExpiryDateTextWatcher());


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //--------------------------------------------------------
                String url3 = Model.BASE_URL + "/app/doTxn?user_id=" + (Model.id) + "&inv_id=" + inv_id + "&token=" + Model.token;
                System.out.println("url-----------" + url3);
                new JSON_txnid().execute(url3);
                //-------------------------------------------------------
            }
        });

        other_fee__layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (down_arrow.getVisibility() == View.VISIBLE) {
                    //ihc_layout.setVisibility(View.VISIBLE);
                    down_arrow.setVisibility(View.GONE);
                    up_arrow.setVisibility(View.VISIBLE);

                    sub_layout.setVisibility(View.VISIBLE);

                } else {
                    //ihc_layout.setVisibility(View.GONE);
                    down_arrow.setVisibility(View.VISIBLE);
                    up_arrow.setVisibility(View.GONE);

                    sub_layout.setVisibility(View.GONE);


                }
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.user_id", (Model.id));
                    articleParams.put("android.patient.inv_id", inv_id);
                    articleParams.put("android.patient.inv_strfee", inv_strfee);
                    FlurryAgent.logEvent("android.patient.Invoice_FastTrack", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //---------------------------------------------------------
                    String url = Model.BASE_URL + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=pftrack&item_id=" + (inv_id) + "&token=" + Model.token + "&enc=1";
                    System.out.println("Fast Track Invoice url-------------" + url);
                    new JSON_Prepare_inv().execute(url);
                    //---------------------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Invoice_Page_New.this);
                    final EditText edittext = new EditText(Invoice_Page_New.this);
                    //alert.setMessage("Enter the Coupon code");
                    alert.setTitle("Enter the Coupon code");

                    alert.setView(edittext);

                    alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String YouEditTextValue = edittext.getText().toString();
                            System.out.println("YouEditTextValue------" + YouEditTextValue);

                            if (!YouEditTextValue.equals("")) {
                                btn_check.setChecked(true);
                                try {
                                    json_coupon_param = new JSONObject();
                                    json_coupon_param.put("user_id", Model.id);
                                    json_coupon_param.put("inv_id", inv_id);
                                    json_coupon_param.put("coupon", YouEditTextValue);
                                    json_coupon_param.put("country_code", (Model.inv_browsercountry));

                                    System.out.println("json_coupon_param---" + json_coupon_param.toString());

                                    new JSON_Coupon_Apply().execute(json_coupon_param);

                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            } else {
                                btn_check.setChecked(false);
                                Toast.makeText(getApplicationContext(), "Pleas enter Coupon Code", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            btn_check.setChecked(false);
                        }
                    });

                    alert.show();
                }
            }
        });

        try {
            LinearLayout l1 = findViewById(R.id.someone_layout);
        } catch (Exception e) {
            e.printStackTrace();
        }


        layout_curr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Invoice_Page_New.this);
                builder.setTitle("Choose a currency");

                String[] animals = {"INR (Indian Ruppes)", "USD (United States Dollar)", "EUR (Euro)", "GBP (Pound sterling)", "AUD (Australian Dollar)", "PHP (Philippines Peso)", "ILS (New Israeli Shekel)"};
                int checkedItem = 3;
                builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btn_coupon_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_coupon_text = edt_coupon.getText().toString();
                //coupon_code = "yes";

                if (!edt_coupon_text.equals("")) {

                    try {
                        json_coupon_param = new JSONObject();
                        json_coupon_param.put("user_id", Model.id);
                        json_coupon_param.put("inv_id", inv_id);
                        json_coupon_param.put("coupon", edt_coupon_text);
                        json_coupon_param.put("country_code", (Model.inv_browsercountry));

                        System.out.println("json_coupon_param---" + json_coupon_param.toString());

                        new JSON_Coupon_Apply().execute(json_coupon_param);

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Coupon Code", Toast.LENGTH_LONG).show();
                }

            }
        });


/*
        ftrack_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.user_id", (Model.id));
                    articleParams.put("android.patient.inv_id", inv_id);
                    articleParams.put("android.patient.inv_strfee", inv_strfee);
                    FlurryAgent.logEvent("android.patient.Invoice_FastTrack", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //---------------------------------------------------------
                    String url = Model.BASE_URL + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=pftrack&item_id=" + (inv_id) + "&token=" + Model.token + "&enc=1";
                    System.out.println("Fast Track Invoice url-------------" + url);
                    new JSON_Prepare_inv().execute(url);
                    //---------------------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
*/


/*        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             *//*   Intent intent = new Intent(Invoice_Page_New.this, PaymentActivity.class);
                startActivity(intent);*//*
            }
        });*/


        tv_revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    //-----------------------------------------------------
                    String url = Model.BASE_URL + "/sapp/prepareInv?user_id=" + (Model.id) + "&inv_for=rev_ftrack&item_id=" + inv_id + "&token=" + Model.token + "&enc=1";
                    System.out.println("Prepare Invoice url-------------" + url);
                    new JSON_Prepare_inv().execute(url);
                    //-----------------------------------------------------


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("qid", qid);
                    articleParams.put("Invoice_no", (inv_id));
                    articleParams.put("Invoice_fee", (inv_strfee));
                    FlurryAgent.logEvent("android.patient.Invoice_Fasttrack_Revert", articleParams);
                    //----------- Flurry -------------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        first_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_inner_layout.getVisibility() == View.VISIBLE) {
                    first_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                } else if (first_inner_layout.getVisibility() == View.GONE) {
                    first_inner_layout.setVisibility(View.VISIBLE);
                    second_inner_layout.setVisibility(View.GONE);
                    third_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(true);
                    radio2.setChecked(false);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.up_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                }

            }
        });


        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_inner_layout.getVisibility() == View.VISIBLE) {
                    first_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                } else if (first_inner_layout.getVisibility() == View.GONE) {

                    first_inner_layout.setVisibility(View.VISIBLE);
                    second_inner_layout.setVisibility(View.GONE);
                    third_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(true);
                    radio2.setChecked(false);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.up_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                }
            }
        });

        second_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (second_inner_layout.getVisibility() == View.VISIBLE) {
                    second_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                } else if (second_inner_layout.getVisibility() == View.GONE) {
                    first_inner_layout.setVisibility(View.GONE);
                    second_inner_layout.setVisibility(View.VISIBLE);
                    third_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(false);
                    radio2.setChecked(true);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.up_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                }

            }
        });

        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (second_inner_layout.getVisibility() == View.VISIBLE) {
                    second_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                } else if (second_inner_layout.getVisibility() == View.GONE) {
                    first_inner_layout.setVisibility(View.GONE);
                    second_inner_layout.setVisibility(View.VISIBLE);
                    third_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(false);
                    radio2.setChecked(true);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.up_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                }

            }
        });


        third_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (third_inner_layout.getVisibility() == View.VISIBLE) {
                    third_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                } else if (third_inner_layout.getVisibility() == View.GONE) {
                    first_inner_layout.setVisibility(View.GONE);
                    second_inner_layout.setVisibility(View.GONE);
                    third_inner_layout.setVisibility(View.VISIBLE);

                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(true);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.up_icon);
                }

            }
        });


        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (third_inner_layout.getVisibility() == View.VISIBLE) {
                    third_inner_layout.setVisibility(View.GONE);

                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(false);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.down_icon);

                } else if (third_inner_layout.getVisibility() == View.GONE) {
                    first_inner_layout.setVisibility(View.GONE);
                    second_inner_layout.setVisibility(View.GONE);
                    third_inner_layout.setVisibility(View.VISIBLE);

                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(true);

                    arrow1.setImageResource(R.mipmap.down_icon);
                    arrow2.setImageResource(R.mipmap.down_icon);
                    arrow3.setImageResource(R.mipmap.up_icon);
                }

            }
        });


        btn_make_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("qid:", qid);
                articleParams.put("Invoice_no:", inv_id);
                articleParams.put("Invoice_fee:", inv_strfee);
                FlurryAgent.logEvent("android.patient.Invoice_RazorPay", articleParams);
                //----------- Flurry -------------------------------------------------

                startPayment();

            }
        });

        img_paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

/*                    String url = Model.BASE_URL + "/account/payinvurl?inv_id=" + inv_id + "&use_paytm=1";
                    System.out.println("Paytm url------------" + url);

                    //---------------------------------------------------------------------------------
                    Intent intent = new Intent(Invoice_Page_New.this, Paytmwebview_Activity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("inv_id", inv_id);
                    intent.putExtra("type", type_val);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            Invoice_Page_New.this.finish();
                        }
                    });
                    startActivityForResult(intent, 1);
                    //---------------------------------------------------------------------------------*/

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("user_id", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Invoice_Paytm", params);
                    //------------ Google firebase Analitics---------------------------------------------

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("Query_id:", qid);
                    articleParams.put("Invoice_no:", (inv_id));
                    articleParams.put("Invoice_fee:", (inv_strfee));
                    FlurryAgent.logEvent("android.patient.Invoice_Paytm", articleParams);
                    //----------- Flurry -------------------------------------------------

                    Intent i = new Intent(Invoice_Page_New.this, WebViewActivity.class);
                    i.putExtra("url", Model.BASE_URL + "/account/payinvurl?inv_id=" + inv_id + "&use_paytm=1");
                    i.putExtra("type", "paytm");
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    new Async_source().execute("");

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("user_id", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Invoice_Stripe", params);
                    //------------ Google firebase Analitics---------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }


                /*
                String cno = edt_cardnum.getText().toString();
                String exp_dt = edt_expiry_date.getText().toString();
                String cvv_no = edt_cvv.getText().toString();

                if (!cno.equals("")) {

                    if (validateCardExpiryDate(exp_dt)) {

                        if (!cvv_no.equals("")) {

                            new Async_source().execute("");

                            //------------ Google firebase Analitics-----------------------------------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("user_id", Model.id);
                            Model.mFirebaseAnalytics.logEvent("Invoice_Stripe", params);
                            //------------ Google firebase Analitics---------------------------------------------


                        } else {
                            edt_cvv.setError("Enter CVV number");
                        }
                    } else {
                        edt_expiry_date.setError("Expiry date is not valid");
                    }
                } else {
                    edt_cardnum.setError("Enter card number");
                }
*/

            }
        });

        img_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

/*                    String url = Model.BASE_URL + "/account/payinvurl?inv_id=" + inv_id + "&use_fcharge=1";
                    System.out.println("Paytm url------------" + url);

                    //---------------------------------------------------------------------------------
                    Intent intent = new Intent(Invoice_Page_New.this, Paytmwebview_Activity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("inv_id", inv_id);
                    intent.putExtra("type", type_val);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            Invoice_Page_New.this.finish();
                        }
                    });
                    startActivityForResult(intent, 1);
                    //---------------------------------------------------------------------------------*/

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("user_id", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Invoice_freecharge", params);
                    //------------ Google firebase Analitics---------------------------------------------


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("Query_id:", qid);
                    articleParams.put("Invoice_no:", (inv_id));
                    articleParams.put("Invoice_fee:", (inv_strfee));
                    FlurryAgent.logEvent("android.patient.Invoice_freecharge", articleParams);
                    //----------- Flurry -------------------------------------------------

                    Intent i = new Intent(Invoice_Page_New.this, WebViewActivity.class);
                    i.putExtra("url", Model.BASE_URL + "/account/payinvurl?inv_id=" + inv_id + "&use_fcharge=1");
                    i.putExtra("type", "freecharge");
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    //------------ Google firebase Analitics-----------------------------------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("user_id", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Invoice_PayPal", params);
                    //------------ Google firebase Analitics---------------------------------------------


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("qid:", qid);
                    articleParams.put("Invoice_no:", inv_id);
                    articleParams.put("Invoice_fee:", inv_strfee);
                    FlurryAgent.logEvent("android.patient.Invoice_PayPal", articleParams);
                    //----------- Flurry -------------------------------------------------

/*                  Intent intent = new Intent(Invoice_Page_New.this, Paypalwebview_Activity.class);
                    intent.putExtra("qid", qid);
                    intent.putExtra("inv_id", inv_id);
                    intent.putExtra("inv_strfee", inv_strfee);
                    startActivity(intent);*/

                    Intent i = new Intent(Invoice_Page_New.this, WebViewActivity.class);
                    i.putExtra("url", Model.BASE_URL + "account/payinvurl?inv_id=" + inv_id);
                    i.putExtra("type", "paypal");
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // getMenuInflater().inflate(R.menu.ask_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finishAffinity();
            Intent i = new Intent(Invoice_Page_New.this, Connect_Home_Actvity.class);
            startActivity(i);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        try {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;

                    finishAffinity();
                    Intent i = new Intent(Invoice_Page_New.this, Connect_Home_Actvity.class);
                    startActivity(i);
                    finish();

                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_txnid extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                json_txn = jParser.getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (json_txn.has("txn_id")) {
                    txtidval = json_txn.getString("txn_id");

                    if (txtidval.equals("-1")) {

                        try {

                            Model.query_launch = "Askquery2";

                              Intent intent = new Intent(Invoice_Page_New.this, ThankyouActivity.class);
                            intent.putExtra("type", type_val);
                            startActivity(intent);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //--------------------------------------------------------
                        String url = Model.BASE_URL + "/sapp/getinv?user_id=" + (Model.id) + "&id=" + (inv_id) + "&token=" + Model.token + "&enc=1";
                        System.out.println("get Invoice url-----------" + url);
                        new JSON_View_inv().execute(url);
                        //--------------------------------------------------------------------
                    }
                }

                progressBar.setVisibility(View.GONE);
                scrollview.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_View_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                inv_str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("inv_str_response--------------" + inv_str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                json_viewinv = new JSONObject(inv_str_response);

                if (json_viewinv.has("token_status")) {
                    String token_status = json_viewinv.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    System.out.println("json_viewinv------------" + json_viewinv.toString());

                    inv_title_txt = json_viewinv.getString("title");
                    inv_desc_txt = json_viewinv.getString("description");
                    inv_strfee = json_viewinv.getString("fee");
                    inv_fee_payable = json_viewinv.getString("fee_payable");
                    inv_strfee_payable = json_viewinv.getString("str_fee_payable");

                    inv_walletfee = json_viewinv.getString("wallet_fee");
                    inv_btnconfirm = json_viewinv.getBoolean("btn_confirm");
                    inv_browsercountry = json_viewinv.getString("browser_country");
                    Model.browser_country = json_viewinv.getString("browser_country");

                    btn_submit.setText("Proceed to pay " + inv_strfee_payable);
                    tv_second_pay.setText(inv_strfee_payable);
                    tv_third_pay.setText(inv_strfee_payable);


                    System.out.println("inv_browsercountry--------------------" + inv_browsercountry);


                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.qid", qid);
                    articleParams.put("android.patient.inv_id", inv_id);
                    articleParams.put("android.patient.inv_title_txt", inv_title_txt);
                    articleParams.put("android.patient.inv_desc_txt", inv_desc_txt);
                    articleParams.put("android.patient.inv_strfee", inv_strfee);
                    articleParams.put("android.patient.inv_walletfee", inv_walletfee);
                    articleParams.put("android.patient.inv_btnconfirm", "" + inv_btnconfirm);
                    articleParams.put("android.patient.inv_browsercountry", inv_browsercountry);
                    FlurryAgent.logEvent("android.patient.Invoice_View", articleParams);
                    //----------- Flurry -------------------------------------------------

                    //-----------------------ftrack----------------------------------------
                    if (json_viewinv.has("ftrack")) {
                        ftrack_opt = json_viewinv.getString("ftrack");
                        if ((ftrack_opt.length()) > 2) {
                            JSONObject ftrack_json = new JSONObject(ftrack_opt);
                            System.out.println("ftrack_json-----" + ftrack_json.toString());

                            if (ftrack_json.has("opt_disable")) {
                                str_opt_disable = ftrack_json.getString("opt_disable");
                                ftrack_str = "";
                            } else {
                                str_opt_disable = "";
                            }

                            if (ftrack_json.has("opt_enable")) {
                                str_opt_enable = ftrack_json.getString("opt_enable");
                                ftrack_str = ftrack_json.getString("str_fee");
                            } else {
                                str_opt_enable = "";
                            }
                        }
                    }
                    //-----------------------ftrack--------------------------------------

                    if (json_viewinv.has("str_delivery_mode")) {
                        ftrack_mode = json_viewinv.getString("str_delivery_mode");
                        System.out.println("ftrack_mode--------" + ftrack_mode);
                    }

                    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    System.out.println("inv date--------" + date);

                    System.out.println("==========Model.inv_fee ----------------" + inv_strfee);
                    System.out.println("inv_strfee----------------" + inv_strfee);

                    if (inv_strfee.equals("0")) {
                        Intent intent = new Intent(Invoice_Page_New.this, ThankyouActivity.class);
                        intent.putExtra("type", type_val);
                        startActivity(intent);
                        finish();
                    }
                    //--------------------------------------------------------------------------
                    System.out.println("inv_btnconfirm---->" + inv_btnconfirm);

                    if (inv_btnconfirm) {

                        btn_confirm.setVisibility(View.VISIBLE);
                        india_pay_layout.setVisibility(View.GONE);
                        tv_paymethod_text.setVisibility(View.GONE);
                        btn_paypal.setVisibility(View.GONE);

                    } else {

                        if (inv_browsercountry.equals("IN")) {

                            india_pay_layout.setVisibility(View.VISIBLE);
                            tv_paymethod_text.setVisibility(View.VISIBLE);
                            btn_paypal.setVisibility(View.GONE);
                            btn_confirm.setVisibility(View.GONE);

                            System.out.println("First Check inv_btnconfirm---->" + inv_btnconfirm);

                        } else {
                            tv_paymethod_text.setVisibility(View.GONE);
                            india_pay_layout.setVisibility(View.GONE);
                            btn_paypal.setVisibility(View.VISIBLE);
                            btn_confirm.setVisibility(View.GONE);

                            System.out.println("Second Check inv_btnconfirm---->" + inv_btnconfirm);
                        }
                    }
                    //--------------------------------------------------------------------------

                    inv_title.setText(inv_title_txt);
                    tv_invdesc.setText(Html.fromHtml(inv_desc_txt));

                    tv_strfees.setText(inv_strfee);
                    tv_invfeetot.setText(inv_strfee_payable);
                    inv_amt.setText(inv_strfee_payable);


                    tv_mywallbal.setText(inv_walletfee);

                    System.out.println("inv_title_txt-----------" + inv_title_txt);
                    System.out.println("inv_strfee-----------" + inv_strfee);
                    System.out.println("inv_walletfee-----------" + inv_walletfee);

                    //--------------------------------------------------------------------------
                    System.out.println("ftrack_mode---->" + ftrack_mode);
                    if (ftrack_mode != null && !ftrack_mode.isEmpty() && !ftrack_mode.equals("null") && !ftrack_mode.equals("")) {
                        tvdelmodelab.setVisibility(View.VISIBLE);
                        tv_revert.setVisibility(View.VISIBLE);
                        revert_layout.setVisibility(View.VISIBLE);
                    } else {
                        tvdelmodelab.setVisibility(View.GONE);
                        tv_revert.setVisibility(View.GONE);
                        revert_layout.setVisibility(View.GONE);
                    }
                    //--------------------------------------------------------------------------

                    //--------------------------------------------------------------------------
                    System.out.println("str_opt_disable---->" + str_opt_disable);
                    if (str_opt_disable != null && !str_opt_disable.isEmpty() && !str_opt_disable.equals("null") && !str_opt_disable.equals("")) {
                        if (str_opt_disable.equals("1")) {
                            tv_ftrackmode.setText(ftrack_mode);
                            tv_revert.setText(Html.fromHtml("  (<u>revert to normal)</u>"));
                            ftrack_text.setText("");

                            revert_layout.setVisibility(View.VISIBLE);
                            tvdelmodelab.setVisibility(View.VISIBLE);
                            ftrack_layout.setVisibility(View.GONE);
                        }
                    }
                    //--------------------------------------------------------------------------

                    //--------------------------------------------------------------------------
                    System.out.println("str_opt_enable---->" + str_opt_enable);
                    if (str_opt_enable != null && !str_opt_enable.isEmpty() && !str_opt_enable.equals("null") && !str_opt_enable.equals("")) {
                        if (str_opt_enable.equals("1")) {

                            tv_ftrackmode.setText("");
                            tv_revert.setText("");
                            //ftrack_text.setText(Html.fromHtml("<bNote:</b> To get priority reply with in <b>one hour</b>, pay <b><u>" + ftrack_str + " </b></u>and fast track your query."));
                            ftrack_text.setText(Html.fromHtml("<b>Fast Track Query " + ftrack_str + " </b>"));
                            System.out.println("<b> Fast Track Query " + ftrack_str + " </b>");
                            ftrack_layout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ftrack_layout.setVisibility(View.GONE);
                    }
                    //--------------------------------------------------------------------------

                    //--------------------------------------------------------------------------
                    System.out.println("str_opt_disable---->" + str_opt_disable);
                    if (str_opt_disable != null && !str_opt_disable.isEmpty() && !str_opt_disable.equals("null") && !str_opt_disable.equals("")) {
                        if (str_opt_disable.equals("0")) {

                            revert_layout.setVisibility(View.GONE);
                            tvdelmodelab.setVisibility(View.GONE);

                        }
                    }
                    //--------------------------------------------------------------------------


                    if (json_viewinv.has("gst")) {

                        ihc_layout.setVisibility(View.VISIBLE);

                        String gst_details = json_viewinv.getString("gst");
                        JSONObject json_gst_obj = new JSONObject(gst_details);

                        String str_fee_excluded_gst = json_gst_obj.getString("str_fee_excluded_gst");
                        String ihs_fee_sum = json_gst_obj.getString("ihs_fee_sum");
                        String ihs_base_fee = json_gst_obj.getString("ihs_base_fee");
                        String cgst_val = json_gst_obj.getString("cgst_val");
                        String sgst_val = json_gst_obj.getString("sgst_val");
                        String cgst_percent = json_gst_obj.getString("cgst_percent");
                        String sgst_percent = json_gst_obj.getString("sgst_percent");
                        String str_gst_val = json_gst_obj.getString("str_gst");


                        tv_igcharges.setText("Internet Handling Fees");

                        float ihs_fee_sum_float = Float.parseFloat(ihs_fee_sum);
                        String grand_tot_ihs_sum = String.format("%.02f", ihs_fee_sum_float);
                        tv_ihfee_val.setText("Rs." + grand_tot_ihs_sum);


                        //------------ Percentage Sum --------------------------------
                        Integer cgst_percent_int = Integer.parseInt(cgst_percent);
                        Integer sgst_percent_int = Integer.parseInt(sgst_percent);
                        Integer tot_percent = cgst_percent_int + sgst_percent_int;
                        tv_gst_text.setText("Includes GST @ " + tot_percent + "%");
                        //------------ Percentage Sum --------------------------------

                        //------------ GST Amount Sum --------------------------------
                        float cgst_val_float = Float.parseFloat(cgst_val);
                        float sgst_val_float = Float.parseFloat(sgst_val);
                        float tot_val_float = cgst_val_float + sgst_val_float;

                        String grand_tot_gst = String.format("%.02f", tot_val_float);
                        //tv_gst_val.setText("" + grand_tot_gst);
                        tv_gst_val.setText(str_gst_val);
                        //------------ GST Amount Sum --------------------------------

                        tv_base_text.setText("Base Amount");
                        tv_base_val.setText(ihs_base_fee);

                        tv_cgst_text.setText("CGST " + cgst_percent + "% on Internet handling fees");
                        tv_cgst_val.setText(cgst_val);

                        tv_sgst_text.setText("SGST " + sgst_percent + "% on Internet handling fees");
                        tv_sgst_val.setText(sgst_val);

                        tv_strfees.setText(str_fee_excluded_gst);

                    } else {
                        ihc_layout.setVisibility(View.GONE);
                    }


/*                    //--------------------------------------------------------------------------


                    System.out.println("inv_btnconfirm---->" + inv_btnconfirm);
                    if (inv_btnconfirm) {
                        btn_confirm.setVisibility(View.VISIBLE);
                        india_pay_layout.setVisibility(View.GONE);
                    } else {
                        btn_confirm.setVisibility(View.GONE);
                        india_pay_layout.setVisibility(View.VISIBLE);
                    }
                    //--------------------------------------------------------------------------*/



/*                    //--------------------------------------------------------------------------
                    System.out.println("inv_fee====>" + inv_strfee);
                    if (inv_strfee.equals("Rs.0")) {
                        btn_confirm.setVisibility(View.GONE);
                        INR_Layout.setVisibility(View.GONE);
                        btn_paypal.setVisibility(View.GONE);
                    }
                    //--------------------------------------------------------------------------*/
/*                    //--------------------------------------------------------------------------
                    System.out.println("inv_fee====>" + inv_fee);
                    if (inv_fee.equals("Rs.0")) {
                        btn_confirm.setVisibility(View.GONE);
*//*                        INR_Layout.setVisibility(View.GONE);
                        btn_paypal.setVisibility(View.GONE);*//*
                    }
                    //--------------------------------------------------------------------------*/

                    //--------------------------------------------------------------------------
                    System.out.println("inv_walletfee====>" + inv_walletfee);
                    if (inv_walletfee != null && !inv_walletfee.isEmpty() && !inv_walletfee.equals("null") && !inv_walletfee.equals("")) {
                        if (inv_walletfee.equals("0")) show_wallet_layout.setVisibility(View.GONE);
                        else show_wallet_layout.setVisibility(View.VISIBLE);
                    } else {
                        show_wallet_layout.setVisibility(View.GONE);
                    }
                    //--------------------------------------------------------------------------
                }

                progressBar.setVisibility(View.GONE);
                scrollview.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Invoice_Page_New.this);
            dialog.setMessage("Preparing Invoice. Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                json_prepinv = jParser.getJSONFromUrl(urls[0]);

                System.out.println("Prepare Invoice Parameter----------" + urls[0]);
                System.out.println("Prepare Invoice Response json----------" + json_prepinv.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                inv_id = json_prepinv.getString("id");
                inv_fee = json_prepinv.getString("fee");
                inv_strfee = json_prepinv.getString("str_fee");

                System.out.println("json_prepinv--------" + json_prepinv.toString());
                System.out.println("inv_id--------" + (inv_id));
                System.out.println("inv_fee--------" + (inv_fee));
                System.out.println("inv_strfee--------" + (inv_strfee));

                //--------------------------------------------------------
                String url = Model.BASE_URL + "sapp/getinv?user_id=" + (Model.id) + "&id=" + (inv_id) + "&token=" + Model.token + "&enc=1";
                System.out.println("get Invoice url-----------" + url);
                new JSON_View_inv().execute(url);
                //--------------------------------------------------------

              /*  String url2 = Model.BASE_URL + "app/doTxn?user_id=" + (Model.id) + "&inv_id=" + (inv_id);
                System.out.println("url-----------" + url2);
                new JSON_txnid().execute(url2);
                //--------------------------------------------------------*/

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_Coupon_Apply extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Invoice_Page_New.this);
            dialog.setMessage("Applying Coupon, please wait...");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_applycoupon = jParser.JSON_POST(urls[0], "Coupon_apply");

                System.out.println("Coupon URL---------------" + urls[0]);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("json_applycoupon---------------------" + json_applycoupon.toString());

                if (json_applycoupon.has("token_status")) {

                    String token_status = json_applycoupon.getString("token_status");
                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(Invoice_Page_New.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    apply_coupon_status = json_applycoupon.getString("status");
                    System.out.println("apply_coupon_status--------------" + apply_coupon_status);

                    coupon_code = "no";

                    if (apply_coupon_status.equals("true")) {

                        btn_check.setChecked(false);
                        edt_coupon.setText("");

                        Toast.makeText(getApplicationContext(), "Your coupon has been applied successfully", Toast.LENGTH_LONG).show();

                        //----------- Flurry -------------------------------------------------
                        HashMap<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.patient.user_id", (Model.id));
                        articleParams.put("android.patient.inv_id", inv_id);
                        articleParams.put("android.patient.coupon", edt_coupon_text);
                        articleParams.put("android.patient.country_code", (Model.inv_browsercountry));
                        FlurryAgent.logEvent("android.patient.Invoice_Coupon", articleParams);
                        //----------- Flurry -------------------------------------------------

                        //--------------------------------------------------------
                        String url2 = Model.BASE_URL + "/sapp/getinv?user_id=" + (Model.id) + "&id=" + (inv_id) + "&token=" + Model.token + "&enc=1";
                        System.out.println("get Invoice url-----------" + url2);
                        new JSON_View_inv().execute(url2);
                        //--------------------------------------------------------

                        //--------------------------------------------------------
                        String url3 = Model.BASE_URL + "/app/doTxn?user_id=" + (Model.id) + "&inv_id=" + inv_id + "&token=" + Model.token;
                        System.out.println("url-----------" + url3);
                        new JSON_txnid().execute(url3);
                        //--------------------------------------------------------

                    } else {

                        btn_check.setChecked(false);
                        edt_coupon.setError(apply_coupon_status);
                        Toast.makeText(getApplicationContext(), apply_coupon_status, Toast.LENGTH_LONG).show();
                    }

                    coupon_code = "no";
                    //coupon_success();
                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void force_logout() {

        try {

            final MaterialDialog alert = new MaterialDialog(Invoice_Page_New.this);
            alert.setTitle("Please re-login the app..!");
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
                    Intent i = new Intent(Invoice_Page_New.this, LoginActivity.class);
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

    //=============================================== RAZOR PAY ====================================
    public void startPayment() {

        //final String public_key = "rzp_live_rqj0TdNTXlZUhQ";
        final String public_key = Model.razor_pay_public_key;
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setPublicKey(public_key);

        try {

            JSONObject options = new JSONObject("{" +
                    "description: '" + (inv_title_txt) + "'," +
                    "image: '" + Model.BASE_URL + "images/boot/icliniq.png'," +
                    "currency: 'INR'}"
            );

            System.out.println("Totamt-------------" + inv_fee_payable);

/*            Double amtval = Double.parseDouble(inv_fee_payable);
            Double totamt = amtval * 100;*/

            Double amtval = Double.parseDouble(inv_fee_payable);
            Double totamt = amtval * 100;

            int final_totamt = (int) Math.round(totamt);

            System.out.println("Totamt--double------------" + final_totamt);
            System.out.println("Notes---------" + new JSONObject("{inv_id: '" + (inv_id) + "'}").toString());
            System.out.println("prefill---------" + new JSONObject("{email: '" + (Model.email) + "', contact: '" + (mob_no) + "'}"));

            options.put("amount", final_totamt);
            options.put("name", "Pay Invoice");
            options.put("prefill", new JSONObject("{email: '" + (Model.email) + "', contact: '" + (mob_no) + "'}"));
            options.put("notes", new JSONObject("{inv_id: '" + (inv_id) + "'}"));
            System.out.println("options--------" + options);

            co.open(activity, options);

        } catch (Exception e) {
/*            Toast.makeText(Invoice_Page_New.this, e.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
            //err_msg(e.getMessage());
            System.out.println("Exception--------------" + e.getClass().getSimpleName());*/
            e.printStackTrace();
        }
    }


    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            try {

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("razorpayPaymentID:", razorpayPaymentID);
                articleParams.put("Invoice_no:", (inv_id));
                articleParams.put("Invoice_fee:", (inv_strfee));
                FlurryAgent.logEvent("android.patient.RazorPay_Successful", articleParams);
                //----------- Flurry -------------------------------------------------

                Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();

                //-----------------------------------------------------------------------
                String url = Model.BASE_URL + "sapp/doneRazorpay?rz_payment_id=" + razorpayPaymentID + "&inv_id=" + (inv_id) + "&user_id=" + (Model.id) + "&token=" + Model.token + "&enc=1";
                System.out.println("Razor_success_API-----------" + url);
                new JSON_Razor_pay_success().execute(url);
                //-----------------------------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            //Log.e("com.merchant", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void onPaymentError(int code, String response) {

        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();

            try {
                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("Invoice_no:", inv_id);
                articleParams.put("Invoice_fee:", inv_strfee);
                articleParams.put("Response:", response);
                articleParams.put("Response_Code:", "" + code);
                FlurryAgent.logEvent("android.patient.RazorPay_Failed", articleParams);
                //----------- Flurry -------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_Razor_pay_success extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Invoice_Page_New.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                System.out.println("str_response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                razorpay_jsonobj = new JSONObject(str_response);
                System.out.println("razorpay_Response-------------" + razorpay_jsonobj.toString());

                if (razorpay_jsonobj.has("token_status")) {
                    String token_status = razorpay_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    Model.query_launch = "Askquery2";
                    Intent intent = new Intent(Invoice_Page_New.this, ThankyouActivity.class);
                    intent.putExtra("type", type_val);
                    startActivity(intent);
                    finish();
                }

            } catch (Exception e) {
                System.out.println("Exception33--" + e.toString());
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }


    private class Async_task extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Invoice_Page_New.this);
            dialog.setMessage("Processing, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                SourceParams cardSourceParams = SourceParams.createCardParams(card);
                Source source = stripe.createSourceSynchronous(cardSourceParams, Model.stripe_apikey);
                SourceCardData cardData = (SourceCardData) source.getSourceTypeModel();

                String threeDStatus = cardData.getThreeDSecureStatus();
                if (SourceCardData.REQUIRED.equals(threeDStatus)) {
                } else if (SourceCardData.OPTIONAL.equals(threeDStatus)) {
                } else if (SourceCardData.UNKNOWN.equals(threeDStatus)) {
                } else {
                }

                System.out.println("inv_fee_payable----------" + inv_fee_payable);

                String cardSourceId = source.getId();
                System.out.println("cardSourceId----------" + cardSourceId);

                //----- Converting to Long ----------------------------
                Double d = Double.parseDouble(inv_fee_payable.trim());

                d = d * 100;

                Long long_value = d.longValue();
                System.out.println("long_value1----------" + long_value);

                //long_value = long_value * 100;
                //System.out.println("long_value-2---------" + long_value);
                //----- Converting to Long ----------------------------

                String red_url = Model.BASE_URL + "account/payinvurl?inv_id=" + inv_id + "&pay_amt=" + long_value + "&pay_cur=inr";
                System.out.println("red_url----------" + red_url);

                SourceParams threeDParams = SourceParams.createThreeDSecureParams(
                        long_value,
                        "INR", // a currency
                        red_url,
                        cardSourceId);

                System.out.println("Redirect url---" + red_url);

                //Source mThreeDSource;
                //mThreeDSource = stripe.createSourceSynchronous(threeDParams);

                stripe.createSource(threeDParams, new SourceCallback() {
                    @Override
                    public void onError(Exception error) {
                        // handle the error
                        System.out.println("Source Error---------");
                        error.printStackTrace();
                    }

                    @Override
                    public void onSuccess(Source source) {
                        //mThreeDSource = source;
                        System.out.println("Source Success---------" + source.toString());

                        String externalUrl = source.getRedirect().getUrl();

/*                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(externalUrl));
                            startActivity(browserIntent);*/

                        //---------------------------------------------------------------------------------
                        Intent intent = new Intent(Invoice_Page_New.this, Stripewebview_Activity.class);
                        intent.putExtra("url", externalUrl);
                        intent.putExtra("title", inv_title_txt);
                        intent.putExtra("type", type_val);
                        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                            @Override
                            protected void onReceiveResult(int resultCode, Bundle resultData) {
                                Invoice_Page_New.this.finish();
                            }
                        });
                        startActivityForResult(intent, 1);
                        //---------------------------------------------------------------------------------

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
                //err_msg(e.getClass().getSimpleName());
            }
        }
    }


    private class Async_source extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Invoice_Page_New.this);
            dialog.setMessage("Processing, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {


            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                dialog.cancel();

                try {

                    String edt_name_on_card_text = edt_name_on_card.getText().toString();
                    String phno = edt_phone.getText().toString();
                    String cardnum = edt_cardnum.getText().toString();
                    String expdate = edt_expiry_date.getText().toString();
                    String cvv_num = edt_cvv.getText().toString();

                    System.out.println("edt_name_on_card_text-------------------------------" + edt_name_on_card_text);
                    System.out.println("cardnum-------------------------------" + cardnum);
                    System.out.println("expdate-------------------------------" + expdate);
                    System.out.println("cvv_num-------------------------------" + cvv_num);


                    if (edt_name_on_card_text.length() > 3) {
                        System.out.println("There-----------------");
                    } else {
                        System.out.println("No There-----------------");
                        Toast.makeText(Invoice_Page_New.this, "Please enter name on card", Toast.LENGTH_SHORT).show();
                    }


                    card = card_input_widget.getCard();

                    card.setCurrency("INR");
                    card.setName(edt_name_on_card_text);
                    //card.setAddressZip("");

                    if (card.validateNumber() && card.validateCVC()) {

                        System.out.println("valid Card---------===================");

                        stripe = new Stripe(getApplicationContext(), Model.stripe_apikey);
                        stripe.createToken(card,
                                new TokenCallback() {
                                    public void onSuccess(Token token) {
                                        System.out.println("Stripe token---------------------" + token);
                                        new Async_task().execute("");
                                    }

                                    public void onError(Exception error) {
                                        // Show localized error message
                                        error.printStackTrace();
                                        System.out.println("Stripe Error---------------------");
                                        Toast.makeText(Invoice_Page_New.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        );

                    } else {
                        System.out.println("Invlaid Card---------===================");
                        Toast.makeText(Invoice_Page_New.this, "Invalid card details", Toast.LENGTH_SHORT).show();
                    }



                  /*
                    if (!edt_name_on_card_text.equals("")) {
                        if (!cardnum.equals("")) {
                            if (!expdate.equals("")) {
                                if (!cvv_num.equals("")) {


                                    card = card_input_widget.getCard();
                                    card.setCurrency("INR");
                                    card.setName(edt_name_on_card_text);
                                    //card.setAddressZip("");

                                    if (card.validateNumber() && card.validateCVC()) {

                                        System.out.println("vlaid Card---------===================");

                                        stripe = new Stripe(getApplicationContext(), Model.stripe_apikey);
                                        stripe.createToken(card,
                                                new TokenCallback() {
                                                    public void onSuccess(Token token) {
                                                        System.out.println("Stripe token---------------------" + token);
                                                        new Async_task().execute("");
                                                    }

                                                    public void onError(Exception error) {
                                                        // Show localized error message
                                                        error.printStackTrace();
                                                        System.out.println("Stripe Error---------------------");
                                                        Toast.makeText(Invoice_Page_New.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                        );

                                    } else {
                                        System.out.println("Invlaid Card---------===================");
                                        Toast.makeText(Invoice_Page_New.this, "Invalid card details", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter card cvv number", Toast.LENGTH_SHORT).show();
                                    edt_cvv.setError("Enter card cvv number");
                                    edt_cvv.requestFocus();
                                }
                            } else {
                                Toast.makeText(Invoice_Page_New.this, "Enter card expiry date", Toast.LENGTH_SHORT).show();
                                edt_expiry_date.setError("Enter card expiry date");
                                edt_expiry_date.requestFocus();
                            }
                        } else {
                            Toast.makeText(Invoice_Page_New.this, "Enter card number", Toast.LENGTH_SHORT).show();
                            edt_cardnum.setError("Enter card number");
                            edt_cardnum.requestFocus();
                        }

                    } else {
                        Toast.makeText(Invoice_Page_New.this, "Enter name on card", Toast.LENGTH_SHORT).show();
                        edt_name_on_card.setError("Enter the name on card");
                        edt_name_on_card.requestFocus();
                    }
*/

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception--------------" + e.getClass().getSimpleName());
                }

                System.out.println("Post Executive--------------");

                //new Async_task().execute("");

            } catch (Exception e) {
                e.printStackTrace();
/*                Toast.makeText(Invoice_Page_New.this, e.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                //err_msg(e.getMessage());
                System.out.println("Exception--------------" + e.getClass().getSimpleName());*/
            }
        }
    }

/*    public void err_msg(String msg) {

        final MaterialDialog alert = new MaterialDialog(Invoice_Page_New.this);
        //alert.setTitle("Please re-login the app..!");
        alert.setMessage(msg);
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }*/


    public static class CreditCardNumberFormattingTextWatcher implements TextWatcher {

        private boolean lock;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (lock || s.length() > 16) {
                return;
            }
            lock = true;
            for (int i = 4; i < s.length(); i += 5) {
                if (s.toString().charAt(i) != ' ') {
                    s.insert(i, " ");
                }
            }
            lock = false;

            System.out.println("s--------------" + s);
        }
    }


    public static class ExpiryDateTextWatcher implements TextWatcher {

        private boolean lock;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (lock || s.length() > 5) {
                return;
            }
            lock = true;
            for (int i = 2; i < s.length(); i += 3) {
                if (s.toString().charAt(i) != '/') {
                    s.insert(i, "/");
                }
            }
            lock = false;

            System.out.println("s--------------" + s);
        }
    }


    boolean validateCardExpiryDate(String expiryDate) {
        return expiryDate.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
    }

}
