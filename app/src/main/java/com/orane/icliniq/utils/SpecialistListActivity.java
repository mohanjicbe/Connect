package com.orane.icliniq.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.utils.adapters.SpecialistAdapter;
import com.orane.icliniq.utils.walletModel.SpecialistModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SpecialistListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noData;
    ArrayList<SpecialistModel>specialist=new ArrayList<>();
    String str_response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyclerView);
        noData=findViewById(R.id.noData);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Model.select_specname = specialist.get(position).getSpName();
                Model.select_spec_val = specialist.get(position).getSpiId();
                Model.query_launch="SpecialistListActivity";
                Log.e("special------",specialist.get(position).getSpiId()+"  "+specialist.get(position).getSpName());
                finish();

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        String url = Model.BASE_URL + "sapp/getAllSpecialities?user_id=" + Model.id+ "&token=" + Model.token ;
        Log.e("url",url+"  ");
        new MySpecialist_Doctors().execute(url);
//        getSpecialistList();
    }

    private void getSpecialistList() {
        ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Model.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitService service = retrofit.create(RetrofitService.class);
            service.specialistDetails(Model.id,Model.token).enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject>response) {
                    progressDialog.dismiss();
                    if (response.code()==200){
                        Log.e("object",response.body()+" ");
                        progressDialog.dismiss();
                       JsonObject categoryJSONObj;

                        if (response.body() != null) {
//                                categoryJSONObj = new JsonObject(response.body().toString());
                            Log.e("object",response.body()+" ");
                            Iterator<String> iterator = response.body().keys();

                            while (iterator.hasNext()) {

                                String key = iterator.next();
                                String value_of_key = response.body().optString(key);

                                Log.e("key",key+"  ");
                                Log.e(" value_of_key", value_of_key+"  ");
                                SpecialistModel model = new SpecialistModel();
                                model.setSpiId(key);
                                model.setSpName(value_of_key);
                                specialist.add(model);
                            }
                        }

                            if(specialist!=null && specialist.size()>0) {
                                noData.setVisibility(View.GONE);
                                SpecialistAdapter adapter = new SpecialistAdapter(specialist);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(SpecialistListActivity.this));
                                recyclerView.setAdapter(adapter);
                                Log.e("Size", specialist.size() + "  ");
                            }else{
                                noData.setVisibility(View.VISIBLE);
                            }

                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();

                }
            });
        }

        class MySpecialist_Doctors  extends AsyncTask<String, Void, Void> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                 progressDialog=new ProgressDialog(SpecialistListActivity.this);
                progressDialog.setMessage("Please wait");
                progressDialog.show();

            }

            @Override
            protected Void doInBackground(String... params) {
                try {
                   str_response = new JSONParser().getJSONString(params[0]);
                   Log.e("  String str_response", str_response+" ");
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

                        JSONObject jobject = new JSONObject(str_response);

                        Iterator<String> iterator = jobject .keys();

                        while (iterator.hasNext()) {

                            String key = iterator.next();
                            String value_of_key =jobject.optString(key);

                            Log.e("key",key+"  ");
                            Log.e(" value_of_key", value_of_key+"  ");
                            SpecialistModel model = new SpecialistModel();
                            model.setSpiId(key);
                            model.setSpName(value_of_key);
                            specialist.add(model);
                        }
                    SpecialistAdapter adapter = new SpecialistAdapter(specialist);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SpecialistListActivity.this));
                    recyclerView.setAdapter(adapter);
                    Log.e("Size",specialist.size()+"  ");

                progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                }
            }
}