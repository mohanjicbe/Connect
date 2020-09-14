package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.icliniq.Model.Item;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.adapter.DoctorsRowAdapter;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.utils.RetrofitService;
import com.orane.icliniq.utils.WalletDetailsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Calendar;
import java.util.List;

import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connect_Home_Actvity extends AppCompatActivity {

    Button btn_started;
    public static Connect_Home_Actvity otpinst;
    DoctorsRowAdapter objAdapter;

    public static Connect_Home_Actvity instance() {
        return otpinst;
    }

    @Override
    public void onStart() {
        super.onStart();
        otpinst = this;
    }

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
    public static final String bcountry = "bcountry_key";
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
    public static final String first_query = "first_query_key";
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_has_free_follow = "sp_has_free_follow_key";
    public static final String token = "token_key";
    public static final String chat_tip = "chat_tip_key";
    public static final String hospital_name = "hospital_name_key";
    public static final String hospital_domain = "hospital_domain_key";
    public static final String hospital_logo = "hospital_domain_key";
    public static final String base_url = "base_url_key";

    String str_response;
    ArrayAdapter<String> dataAdapter = null;
    Item objItem;
    ImageView menu_dots;
    TextView copyright_text, tv_privacy, tv_terms;
    public List<Item> listArray;
    public List<Item> arrayOfList;
    LinearLayout innerLay, mydoctors_layout, layout_2, layout_1, home_myqueries;
    CardView card_view_hosp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_home);

        //------------------------------------------------------------------------------------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String hospital_name_text = sharedpreferences.getString(hospital_name, "");
        String hospital_domain_text = sharedpreferences.getString(hospital_domain, "");
        String hospital_logo_text = sharedpreferences.getString(hospital_logo, "");
        String base_url_text = sharedpreferences.getString(base_url, "");


        Model.hosp_name = hospital_name_text;
        Model.hosp_domain = hospital_domain_text;
        Model.hosp_logo = hospital_logo_text;
        Model.BASE_URL = base_url_text;

        System.out.println("hospital_name_text------" + hospital_name_text);
        System.out.println("hospital_domain_text----" + hospital_domain_text);
        System.out.println("hospital_logo_text----" + hospital_logo_text);
        //------------------------------------------------------------------------------------------

        menu_dots = (ImageView) findViewById(R.id.menu_dots);
        ImageView hosp_logo = (ImageView) findViewById(R.id.hosp_logo);
        TextView tv_hospname = (TextView) findViewById(R.id.tv_hospname);
        copyright_text = (TextView) findViewById(R.id.copyright_text);
        tv_terms = (TextView) findViewById(R.id.tv_terms);
        tv_privacy = (TextView) findViewById(R.id.tv_privacy);

        home_myqueries = (LinearLayout) findViewById(R.id.home_myqueries);
        innerLay = (LinearLayout) findViewById(R.id.innerLay);
        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        layout_2 = (LinearLayout) findViewById(R.id.layout_2);
        mydoctors_layout = (LinearLayout) findViewById(R.id.mydoctors_layout);
        card_view_hosp=findViewById(R.id.card_view_hosp);
        try {
            Picasso.with(getApplicationContext()).load(Model.hosp_logo).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(hosp_logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_hospname.setText(Model.hosp_name);

        Integer year_val = Calendar.getInstance().get(Calendar.YEAR);

        copyright_text.setText("Copyright \u00a9 " + year_val + " " +"iCliniq " + " - All rights reserved");

        //------------------------------------------
        String url = Model.BASE_URL + "sapp/doctors?user_id=" + (Model.id) + "&page=1&sp_id=0&token=" + (Model.token) + "&enc=1";
        System.out.println("url---" + url);
        new Doctor_list_Async().execute(url);
        //-----------------------------------------
        card_view_hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    directionApiCall();
                    AlertMethod();
            }
        });


        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(Connect_Home_Actvity.this, ConsultationListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(Connect_Home_Actvity.this);
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Home_consultations", params);
                    //------------ Google firebase Analitics--------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(Connect_Home_Actvity.this, BookingListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(Connect_Home_Actvity.this);
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Home_Bookings", params);
                    //------------ Google firebase Analitics--------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mydoctors_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(Connect_Home_Actvity.this, MyDoctorsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(Connect_Home_Actvity.this);
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Home_Bookings", params);
                    //------------ Google firebase Analitics--------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        home_myqueries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(Connect_Home_Actvity.this, QueryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(Connect_Home_Actvity.this);
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Home_Queries", params);
                    //------------ Google firebase Analitics--------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent(Connect_Home_Actvity.this, Terms_WebViewActivity.class);
                    //i.putExtra("url", Model.BASE_URL + "p/terms?nolayout=1");
                    i.putExtra("url", Model.Basic_server + "pages/display/page/terms?nolayout=1");
                    i.putExtra("type", "Terms of use");
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tv_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent(Connect_Home_Actvity.this, Terms_WebViewActivity.class);
                    //i.putExtra("url", Model.BASE_URL + "p/privacy?nolayout=1");
                    i.putExtra("url", Model.Basic_server + "p/privacy?nolayout=1");
                    i.putExtra("type", "Privacy Policy");
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        menu_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    PopupMenu popup = new PopupMenu(Connect_Home_Actvity.this, menu_dots);
                    popup.getMenuInflater()
                            .inflate(R.menu.home_popup_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            System.out.println("item id------- " + item.getItemId());
                            System.out.println("item name------- " + item.getTitle());
                            String menu_name = "" + item.getTitle();

                            switch (menu_name) {
                                case "Edit Profile":
                                    startActivity(new Intent(Connect_Home_Actvity.this, Patient_Profile_Activity.class));
                                    break;
                                case "My Hospitals":
                                    startActivity(new Intent(Connect_Home_Actvity.this, Connect_hosp_list.class));
                                    break;
                                case "My Family":
                                    startActivity(new Intent(Connect_Home_Actvity.this, FamilyProfileListActivity.class));

                                    break;
                                case "My Wallet":
                                    startActivity(new Intent(Connect_Home_Actvity.this, WalletDetailsActivity.class));

                                    break;
                                case "Logout":
                                    //============================================================
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Login_Status, "0");
                                    editor.apply();
                                    //============================================================

                                    finishAffinity();
                                    Intent i = new Intent(Connect_Home_Actvity.this,Connect_Hosp_Select.class);
                                    startActivity(i);
                                    finish();
                                    break;
                                default:
                                    break;
                            }

                            return true;
                        }
                    });

                    popup.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void directionApiCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
//        addressCall
        service.addressCall().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AlertMethod() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert, null);
        dialogBuilder.setView(dialogView);
        TextView txt_address=dialogView.findViewById(R.id.txt_address);
        Button btn_cancel=dialogView.findViewById(R.id.btnCancel);
        Button btnShow=dialogView.findViewById(R.id.btnShow);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ShowDirections(txt_address.getText().toString());
            }
        });
    }

    private void ShowDirections(String address) {

        Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+"&daddr="+address+"&mode=driving"));
//        i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(i);

//        String map = "http://maps.google.co.in/maps?q=" + "yourAddress";
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
//        startActivity(intent);
    }


    class Doctor_list_Async extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Connect_Home_Actvity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                str_response = new JSONParser().getJSONString(params[0]);
                System.out.println("str_response--------------" + str_response);

                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                //----------------------------------------------------------
                Object json = new JSONTokener(str_response).nextValue();
                if (json instanceof JSONObject) {
                    System.out.println("This is JSON OBJECT---------------" + str_response);

                    JSONObject jobject = new JSONObject(str_response);

                    if (jobject.has("token_status")) {
                        String token_status = jobject.getString("token_status");
                        if (token_status.equals("0")) {

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //============================================================

                            finishAffinity();
                            Intent intent = new Intent(Connect_Home_Actvity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);

                    //listArray = new ArrayList<Item>();
                    innerLay.removeAllViews();

                    for (int i = 0; i < jsonarr.length(); i++) {

                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        System.out.println("jsonobj1------------" + jsonobj1.toString());

                        String doc_id = jsonobj1.getString("id");
                        String doctor_name = jsonobj1.getString("name");
                        String doc_edu = jsonobj1.getString("edu");
                        String doc_speciality = jsonobj1.getString("speciality");
                        String doc_photo_url = jsonobj1.getString("photo_url");
                        String doctor_url = jsonobj1.getString("doctor_url");
                        String doc_cfee = jsonobj1.getString("cfee");
                        String doc_qfee = jsonobj1.getString("qfee");


                        View recc_vi = getLayoutInflater().inflate(R.layout.connect_home_doctors_row, null);

                        LinearLayout full_layout = recc_vi.findViewById(R.id.full_layout);
                        LinearLayout last_layout = recc_vi.findViewById(R.id.last_layout);
                        CircleImageView imageview_poster = recc_vi.findViewById(R.id.imageview_poster);
                        TextView tvdocname = recc_vi.findViewById(R.id.tvdocname);
                        TextView tvedu = recc_vi.findViewById(R.id.tvedu);
                        TextView tvspec = recc_vi.findViewById(R.id.tvspec);
                        TextView tvid = recc_vi.findViewById(R.id.tvid);
                        TextView tvqfee = recc_vi.findViewById(R.id.tvqfee);

                        TextView tv_doclink = recc_vi.findViewById(R.id.tv_doclink);
                        TextView tvdocname_new = recc_vi.findViewById(R.id.tvdocname_new);
                        TextView tv_spec_new = recc_vi.findViewById(R.id.tv_spec_new);

                        Button btn_book = recc_vi.findViewById(R.id.btn_book);

                        tvdocname.setText(doctor_name);
                        tvedu.setText(doc_edu);
                        tvspec.setText(doc_speciality);
                        tvid.setText(doc_id);
                        tvqfee.setText(doc_qfee);

                        tvdocname_new.setText(doctor_name);
                        tv_spec_new.setText(doc_speciality);
                        tv_doclink.setText(doctor_url);

                        try {
                            Picasso.with(Connect_Home_Actvity.this).load(doc_photo_url).placeholder(R.mipmap.thread_bg).error(R.mipmap.logo).into(imageview_poster);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        innerLay.addView(recc_vi);
                    }


                    View recc_vi2 = getLayoutInflater().inflate(R.layout.connect_home_doctors_row_last, null);

                    LinearLayout full_layout = recc_vi2.findViewById(R.id.full_layout);


                    full_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Connect_Home_Actvity.this, DoctorsListActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        }
                    });

                    innerLay.addView(recc_vi2);

                }
                //----------------------------------------------------------

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick_consult_button(View v) {

        try {

            View parent = (View) v.getParent();
            View grand_parent = (View) parent.getParent();

            TextView tvid = grand_parent.findViewById(R.id.tvid);

            String Doc_id = tvid.getText().toString();

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("doctor_id", tvid.getText().toString());
            Model.mFirebaseAnalytics.logEvent("doctor_book_select", params);
            //------------ Google firebase Analitics--------------------

            if (Doc_id != null && !Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                Intent intent = new Intent(Connect_Home_Actvity.this, ParallexMainActivity.class);
                intent.putExtra("tv_doc_id", Doc_id);
                startActivity(intent);
            } else {
                Toast.makeText(Connect_Home_Actvity.this, "Sorry, Something went wrong. GO Back and Try again..!", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDoctorShare_home(View v) {

        try {

            switch (v.getId()) {

                case R.id.sharedoc_layout:

                    //View parent = (View) v.getParent();
                    TextView tv_doclink = v.findViewById(R.id.tv_doclink);
                    TextView tvdocname_new = v.findViewById(R.id.tvdocname_new);
                    TextView tv_spec_new = v.findViewById(R.id.tv_spec_new);

                    String doclink = tv_doclink.getText().toString();
                    String docname_share_val = tvdocname_new.getText().toString();
                    String spec_new_val = tv_spec_new.getText().toString();

                    System.out.println("doclink-----------" + doclink);
                    System.out.println("docname_share_val-----------" + docname_share_val);
                    System.out.println("spec_new_val-----------" + spec_new_val);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    //i.putExtra(Intent.EXTRA_SUBJECT, ); "\n\n
                    String sAux = "It is quick to consult doctor " + docname_share_val + " " + spec_new_val + " online at  iCliniq Connect. \n\n " + doclink;
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onClick_doctor_list(View v) {

        try {

            Intent intent = new Intent(Connect_Home_Actvity.this, DoctorsListActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
